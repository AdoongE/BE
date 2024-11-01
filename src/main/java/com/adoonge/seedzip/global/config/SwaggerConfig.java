package com.adoonge.seedzip.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
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
