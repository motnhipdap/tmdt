// package com.dev.dungcony.commons.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import
// org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.StringRedisSerializer;

// @Configuration
// public class RedisConfig {

// @Value("${spring.redis.host}")
// private String host;
// @Value("${spring.redis.port}")
// private int port;

// @Bean
// public RedisConnectionFactory redisConnectionFactory() {
// return new LettuceConnectionFactory(host, port);
// }

// @Bean
// public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory
// connectionFactory) {
// RedisTemplate<String, String> template = new RedisTemplate<>();

// // Gán connection factory để template biết cách kết nối Redis
// template.setConnectionFactory(connectionFactory);

// // Cấu hình serializer cho key (dùng String)
// template.setKeySerializer(new StringRedisSerializer());

// // Cấu hình serializer cho value (dùng String)
// template.setValueSerializer(new StringRedisSerializer());

// // Cấu hình serializer cho hash key (dùng String)
// template.setHashKeySerializer(new StringRedisSerializer());

// // Cấu hình serializer cho hash value (dùng String)
// template.setHashValueSerializer(new StringRedisSerializer());

// // Khởi tạo template sau khi đã cấu hình xong
// template.afterPropertiesSet();

// return template;
// }

// }
