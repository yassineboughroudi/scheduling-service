package org.universiapolis.fablab.pfe.schedulingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI schedulingServiceOpenAPI() {
        return new OpenAPI()
                .openapi("3.0.3") // Explicitly set OpenAPI version
                .info(new Info()
                        .title("Scheduling Service API")
                        .description("API for managing appointments")
                        .version("1.0")
                );
    }
}