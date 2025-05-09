package com.cj.cgv.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${deployment.server.url}")
    private String serverUrl;

    private Info apiInfo() {
        return new Info()
                .title("CGV API")
                .description("CGV API 설명서")
                .version("1.0.0");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("http://localhost:8080/api"),
                        new Server().url(serverUrl)));
    }
}
