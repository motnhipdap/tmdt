package com.dev.dungcony.commons.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Token";

        return new OpenAPI()
                .info(new Info()
                        .title("Dungcony E-Commerce API")
                        .version("1.0")
                        .description("""
                                API documentation cho hệ thống thương mại điện tử.
                                
                                ## Hướng dẫn sử dụng:
                                1. Gọi **POST /v1/api/auth/login** để lấy access token
                                2. Click nút **Authorize** 🔓 phía trên
                                3. Nhập token: `Bearer <your_token>`
                                4. Gọi các API khác bình thường
                                
                                ## Modules:
                                - **Auth** — Đăng ký, đăng nhập, refresh token
                                - **Products** — Quản lý sản phẩm, danh mục, nhà cung cấp
                                - **Promotions** — Quản lý khuyến mãi
                                - **Users** — Quản lý thông tin người dùng
                                """)
                        .contact(new Contact()
                                .name("dungcony")
                                .email("dungcony@dev.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập JWT token (không cần prefix 'Bearer ')")
                        ));
    }
}

