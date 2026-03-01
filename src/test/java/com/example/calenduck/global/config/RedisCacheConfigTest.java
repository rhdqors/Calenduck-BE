package com.example.calenduck.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RedisCacheConfigTest {

    @Test
    @DisplayName("CacheManager가 RedisCacheManager 타입이다 (SimpleCacheManager 아님)")
    void cacheManager_isRedisCacheManager() {
        // Given
        RedisCacheConfig config = new RedisCacheConfig(new ObjectMapper());
        ReflectionTestUtils.setField(config, "cacheTtl", 90000000L);

        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);

        // When
        CacheManager cacheManager = config.cacheManager(connectionFactory);

        // Then
        assertThat(cacheManager).isInstanceOf(RedisCacheManager.class);
    }

    @Test
    @DisplayName("캐시 TTL이 설정되어 있다")
    void cacheConfiguration_hasTtlSet() {
        // Given
        RedisCacheConfig config = new RedisCacheConfig(new ObjectMapper());
        long expectedTtl = 90000000L; // 25시간
        ReflectionTestUtils.setField(config, "cacheTtl", expectedTtl);

        // When
        RedisCacheConfiguration cacheConfig = config.cacheConfiguration();

        // Then
        assertThat(cacheConfig.getTtl()).isEqualTo(Duration.ofMillis(expectedTtl));
    }
}
