package com.facade.facadecore.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @author Puck Wang
 * @project facade-cloud
 * @created 4/28/2024
 */


@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Allow requests from any origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specified methods
                .allowedHeaders("*") // Allow all headers
//                .allowCredentials(true) // Allow sending cookies
                .maxAge(3600); // Cache preflight requests for 1 hour
    }
}
