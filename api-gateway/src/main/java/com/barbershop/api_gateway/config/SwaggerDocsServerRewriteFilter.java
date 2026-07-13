package com.barbershop.api_gateway.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class SwaggerDocsServerRewriteFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(SwaggerDocsServerRewriteFilter.class);
    private static final String GATEWAY_URL = "http://localhost:7090";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (!path.endsWith("/v3/api-docs")) {
            return chain.filter(exchange);
        }

        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                Flux<DataBuffer> fluxBody = Flux.from(body);

                return super.writeWith(
                        fluxBody.collectList().flatMapMany(dataBuffers -> {
                            DataBuffer joined = bufferFactory.join(dataBuffers);
                            byte[] rawBytes = new byte[joined.readableByteCount()];
                            joined.read(rawBytes);
                            DataBufferUtils.release(joined);

                            String originalJson = new String(rawBytes, StandardCharsets.UTF_8);
                            String rewrittenJson = originalJson;

                            try {
                                JsonNode rootNode = objectMapper.readTree(originalJson);
                                if (rootNode instanceof ObjectNode objectRoot) {
                                    ArrayNode serversArray = objectRoot.putArray("servers");
                                    ObjectNode serverNode = serversArray.addObject();
                                    serverNode.put("url", GATEWAY_URL);

                                    ObjectNode componentsNode = objectRoot.has("components")
                                            ? (ObjectNode) objectRoot.get("components")
                                            : objectRoot.putObject("components");
                                    ObjectNode securitySchemesNode = componentsNode.has("securitySchemes")
                                            ? (ObjectNode) componentsNode.get("securitySchemes")
                                            : componentsNode.putObject("securitySchemes");
                                    ObjectNode bearerAuthNode = securitySchemesNode.putObject("bearerAuth");
                                    bearerAuthNode.put("type", "http");
                                    bearerAuthNode.put("scheme", "bearer");
                                    bearerAuthNode.put("bearerFormat", "JWT");

                                    ArrayNode securityArray = objectRoot.putArray("security");
                                    ObjectNode securityRequirement = securityArray.addObject();
                                    securityRequirement.putArray("bearerAuth");

                                    rewrittenJson = objectMapper.writeValueAsString(objectRoot);
                                }
                            } catch (Exception e) {
                                rewrittenJson = originalJson.replaceAll("http://[a-zA-Z0-9_-]+:70[0-9]{2}", GATEWAY_URL);
                            }

                            byte[] rewrittenBytes = rewrittenJson.getBytes(StandardCharsets.UTF_8);
                            getDelegate().getHeaders().set(
                                    HttpHeaders.CONTENT_LENGTH,
                                    String.valueOf(rewrittenBytes.length)
                            );

                            return Flux.just(bufferFactory.wrap(rewrittenBytes));
                        })
                );
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
