package com.compo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Compo API",
                version = "1.0.0",
                description = "API REST para la gestión musical de compositores, directores, intérpretes, obras e interpretaciones. " +
                        "Autenticación mediante JWT Bearer Token.",
                contact = @Contact(
                        name = "Compo Team",
                        email = "admin@compo.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor de desarrollo")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Ingrese el token JWT obtenido en /api/auth/login"
)
public class OpenApiConfig {
}
