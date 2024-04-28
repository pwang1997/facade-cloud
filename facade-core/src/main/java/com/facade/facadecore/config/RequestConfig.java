package com.facade.facadecore.config;

import com.facade.facadecore.interceptor.HttpRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Puck Wang
 * @project Blog
 * @created 2/24/2024
 */

@Configuration
public class RequestConfig implements WebMvcConfigurer {

    private final SecretConfig secretConfig;

    public RequestConfig(SecretConfig secretConfig) {
        this.secretConfig = secretConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpRequestInterceptor());
//        registry.addInterceptor(new SecretTokenInterceptor(secretConfig));
    }
}
