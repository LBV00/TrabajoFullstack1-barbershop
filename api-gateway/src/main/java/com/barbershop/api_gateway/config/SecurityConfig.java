package com.barbershop.api_gateway.config;

import com.barbershop.api_gateway.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Configuración de seguridad centralizada para el API Gateway.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private static final String[] PUBLIC_PATHS = {
            "/auth/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/webjars/**",
            "/auth/v3/api-docs",
            "/users/v3/api-docs",
            "/reservas/v3/api-docs",
            "/productos/v3/api-docs",
            "/pagos/v3/api-docs",
            "/resenas/v3/api-docs",
            "/empleados/v3/api-docs",
            "/inventarios/v3/api-docs",
            "/notificaciones/v3/api-docs",
            "/servicios/v3/api-docs",
            "/sucursales/v3/api-docs"
    };

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .exceptionHandling(exc -> exc
                        .authenticationEntryPoint((exchange, ex) -> {
                            log.warn("Acceso no autorizado a: {} — {}",
                                    exchange.getRequest().getURI().getPath(), ex.getMessage());
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            String body = """
                                    {
                                      "status": 401,
                                      "error": "No autorizado",
                                      "mensaje": "Debe incluir un token JWT válido en el header: Authorization: Bearer <token>",
                                      "path": "%s"
                                    }
                                    """.formatted(exchange.getRequest().getURI().getPath());
                            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                            return exchange.getResponse().writeWith(Mono.just(buffer));
                        })
                        .accessDeniedHandler((exchange, ex) -> {
                            log.warn("Acceso denegado a: {} — {}",
                                    exchange.getRequest().getURI().getPath(), ex.getMessage());
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            String body = """
                                    {
                                      "status": 403,
                                      "error": "Acceso denegado",
                                      "mensaje": "No tiene permisos para acceder a este recurso",
                                      "path": "%s"
                                    }
                                    """.formatted(exchange.getRequest().getURI().getPath());
                            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                            return exchange.getResponse().writeWith(Mono.just(buffer));
                        })
                )
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(PUBLIC_PATHS).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private AuthenticationWebFilter jwtAuthenticationWebFilter() {
        org.springframework.security.authentication.ReactiveAuthenticationManager authManager =
                authentication -> {
                    String token = (String) authentication.getCredentials();
                    try {
                        if (jwtUtil.validateToken(token)) {
                            String username = jwtUtil.getUsernameFromToken(token);
                            log.debug("JWT válido — usuario: {}", username);
                            return Mono.just(new UsernamePasswordAuthenticationToken(
                                    username,
                                    token,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            ));
                        }
                    } catch (Exception e) {
                        log.warn("Token JWT inválido: {}", e.getMessage());
                    }
                    return Mono.error(new BadCredentialsException("Token JWT inválido o expirado"));
                };

        AuthenticationWebFilter filter = new AuthenticationWebFilter(authManager);

        filter.setServerAuthenticationConverter(exchange -> {
            String header = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (header != null && header.length() > 7 && header.regionMatches(true, 0, "Bearer ", 0, 7)) {
                String token = header.substring(7).trim();
                if (!token.isEmpty()) {
                    return Mono.just(new UsernamePasswordAuthenticationToken(token, token));
                }
            }
            return Mono.empty();
        });

        return filter;
    }
}
