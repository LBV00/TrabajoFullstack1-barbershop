package com.barbershop.servicio_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI servicioServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Barbershop - API de Servicios")
                        .description("API REST para administrar servicios de BarberShop.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Barbershop Team")
                                .email("barbershop@duoc.cl")));
    }
}
