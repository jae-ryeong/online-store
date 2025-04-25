package com.project.onlinestore.order.service;

import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestRedisConfig {
    @Bean
    public RedissonClient redissonClient() {
        return mock(RedissonClient.class);
    }
}
