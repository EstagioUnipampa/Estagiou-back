package com.lab.estagiou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringdocOpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .components(new Components().addSecuritySchemes("security", securityScheme()))
                                .info(new Info()
                                                .title("Demo Park API")
                                                .description("API RESTful para gestão de estacionamentos")
                                                .version("1.0")
                                                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                                                .contact(new Contact()
                                                                .name("Ricardo Costa")
                                                                .email("contato.ricardolhc@gmail.com")));
        }

        private SecurityScheme securityScheme() {
                return new SecurityScheme()
                                .description("Insira um bearer token para autenticação")
                                .type(SecurityScheme.Type.HTTP)
                                .in(SecurityScheme.In.HEADER)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .name("security");
        }
}
