package com.regintel.ai.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI regIntelOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RegIntel AI API")
                        .description("Agentic AI platform that converts regulatory documents into "
                                + "actionable enterprise impact and engineering plans.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("RegIntel AI Team")
                                .email("team@regintel.ai"))
                        .license(new License().name("Apache 2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Local Development")
                ));
    }
}
