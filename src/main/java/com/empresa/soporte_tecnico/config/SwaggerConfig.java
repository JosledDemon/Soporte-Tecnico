package com.empresa.soporte_tecnico.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI soporteTecnicoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Soporte Técnico")
                        .description("Documentación de la API para gestión de solicitudes de soporte técnico")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Soporte")
                                .email("soporte@empresa.com")
                                .url("https://empresa.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
