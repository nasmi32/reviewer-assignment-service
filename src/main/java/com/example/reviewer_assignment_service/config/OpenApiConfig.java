package com.example.reviewer_assignment_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Pull Request reviewer assignment service",
                description = "Сервис для назначения ревьюверов на Pull Request в рамках команды.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Мишина Анастасия Алексеевна",
                        email = "nastyam3232@gmail.com",
                        url = "https://github.com/nasmi32/reviewer-assignment-service"
                )

        )
)
public class OpenApiConfig {
}
