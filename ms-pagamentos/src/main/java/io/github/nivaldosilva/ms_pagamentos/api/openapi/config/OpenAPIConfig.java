package io.github.nivaldosilva.ms_pagamentos.api.openapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact()
                .email("nivaldosilva.contato@gmail.com")
                .name("Nivaldo Silva")
                .url("https://github.com/Nivaldo-Silva");

        Info info = new Info()
                .title("API Microserviço de Pagamentos")
                .version("v1")
                .contact(contact)
                .description("API para gerenciamento de pagamentos.");

        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Servidor local de desenvolvimento");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}