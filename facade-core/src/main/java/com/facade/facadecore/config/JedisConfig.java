package com.facade.facadecore.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/20/2024
 */
@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
@Slf4j
public class JedisConfig {

    private final SecretConfig secretConfig;

    public JedisConfig(SecretConfig secretConfig) {
        this.secretConfig = secretConfig;
    }


    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        log.info("Loading JedisConnectionFactory on hostname {} port {}", secretConfig.getRedisHost(), secretConfig.getRedisPort());
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setMaxWait(Duration.ofMillis(1000L));

        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofMillis(1000L))
                .readTimeout(Duration.ofMillis(1000L))
                .usePooling().poolConfig(jedisPoolConfig)
                .build();

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(secretConfig.getRedisHost(), secretConfig.getRedisPort());
        redisStandaloneConfiguration.setPassword(secretConfig.getRedisPassword());
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.setEnableTransactionSupport(false);
        return redisTemplate;
    }


}
