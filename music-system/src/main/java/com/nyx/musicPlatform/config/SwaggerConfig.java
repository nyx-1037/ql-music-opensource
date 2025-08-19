package com.nyx.musicPlatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * SpringDoc OpenAPI配置类
 * 
 * @author nyx
 * @since 2025-06
 */
@Configuration
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = false)
public class SwaggerConfig {

    /**
     * 创建OpenAPI文档
     * 
     * @return OpenAPI
     */
    @Bean
    public OpenAPI createOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes("Authorization", securityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    /**
     * API信息
     * 
     * @return Info
     */
    private Info apiInfo() {
        return new Info()
                .title("七洛音乐平台API文档")
                .description("基于Spring Boot + MyBatis-Plus + Redis的音乐平台系统API接口文档")
                .version("1.0.0")
                .contact(new Contact()
                        .name("nyx")
                        .url("https://github.com/nyx-1037")
                        .email("nyx1037678078@Gmail.com"))
                .license(new License()
                        .name("Apache License 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

    /**
     * 安全模式
     * 
     * @return SecurityScheme
     */
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }
}