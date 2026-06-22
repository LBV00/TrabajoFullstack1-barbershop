package com.barbershop.pago_service.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI pagoServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Barbershop - API de Pagos")
                        .description("API REST para administrar pagos.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Barbershop")
                                .email("barbershop@duoc.cl")));
    }
}