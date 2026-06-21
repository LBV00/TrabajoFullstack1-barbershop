package com.barbershop.sucursal_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI sucursalServiceOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                         .title("Barbershop - API de Sucursales")
                         .description("API REST para administrar sucursales de BarberShop.")
                         .version("v1")
                         .contact(new Contact()
                         .name("Barbershop Team")
                         .email("barbershop@duoc.cl")));
    }
}