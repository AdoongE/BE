package com.adoonge.seedzip.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${swagger.server.url}")
    private String swaggerServerUrl;

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        ArrayList<Server> servers = new ArrayList<>();
        if(swaggerServerUrl.contains("localhost")){
            servers.add(new Server().url(swaggerServerUrl).description("Local Server"));
        }else{
            servers.add(new Server().url("https://"+swaggerServerUrl).description("AdoongE Server"));
        }

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .servers(servers)
                .components(components);
    }
    private Info apiInfo() {
        return new Info()
                .title("Seedzip REST API") // API의 제목
                .description("made by AdoongE Backend Team") // API에 대한 설명
                .contact(new Contact()
                        .name("AdoongE BE Github")
                        .url("https://github.com/AdoongE/BE")) // BE 레포지토리 주소
                .version("1.0.0"); // API의 버전
    }
}
