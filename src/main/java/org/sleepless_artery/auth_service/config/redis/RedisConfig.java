package org.sleepless_artery.auth_service.config.redis;

import lombok.RequiredArgsConstructor;
import org.sleepless_artery.auth_service.config.redis.properties.RedisConfigProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisConfigProperties properties;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(properties.getHost(), properties.getPort())
        );
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
        Map<String, RedisCacheConfiguration> redisCacheConfiguration = new HashMap<>();

        redisCacheConfiguration.put("credentials",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(1))
                        .disableCachingNullValues());

        redisCacheConfiguration.put("userDetails",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(1))
                        .disableCachingNullValues());

        redisCacheConfiguration.put("tokens",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(1))
                        .disableCachingNullValues());

        redisCacheConfiguration.put("roles",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(12))
                        .disableCachingNullValues());

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(15)))
                .withInitialCacheConfigurations(redisCacheConfiguration)
                .build();
    }
}
