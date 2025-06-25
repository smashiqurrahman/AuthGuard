package com.example.authsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth System API")
                        .description("Spring Boot Authentication System with Email Verification & JWT")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("you@example.com")
                        ));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/api/**")
                .build();
    }
}
