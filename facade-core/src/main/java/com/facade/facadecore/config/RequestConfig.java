package com.facade.facadecore.config;

import com.facade.facadecore.interceptor.HttpRequestInterceptor;
import com.facade.facadecore.redis.RedisManager;
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
    private final RedisManager redisManager;

    public RequestConfig(SecretConfig secretConfig, RedisManager redisManager) {
        this.secretConfig = secretConfig;
        this.redisManager = redisManager;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpRequestInterceptor(redisManager));
//        registry.addInterceptor(new SecretTokenInterceptor(secretConfig));
    }
}
