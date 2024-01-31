package com.project.onlinestore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "online-store API 명세서")
)
@Configuration
public class SwaggerConfig {
    // http://localhost:8080/swagger-ui/index.html
}
