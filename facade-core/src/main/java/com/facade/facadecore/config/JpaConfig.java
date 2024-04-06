package com.facade.facadecore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Puck Wang
 * @project Blog
 * @created 4/1/2024
 */

@Configuration
@EnableJpaRepositories(value = "com.facade.facadecore.core")
public class JpaConfig {
}
