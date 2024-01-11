package com.fsd08.MediLink.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // 允许跨域请求的域名，例如：http://localhost:3000
                .allowedMethods("*")  // 允许的请求方法，例如：GET、POST、PUT、DELETE 等
                .allowedHeaders("*"); // 允许的请求头部信息，例如：Origin、Content-Type、Authorization 等
    }
}
