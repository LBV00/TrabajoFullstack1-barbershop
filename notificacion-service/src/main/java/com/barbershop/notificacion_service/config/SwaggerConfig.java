package com.barbershop.notificacion_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI notificacionServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Barbershop - API de Notificaciones")
                        .description("API REST para administrar notificaciones.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Barbershop")
                                .email("barbershop@duoc.cl")));
    }
}