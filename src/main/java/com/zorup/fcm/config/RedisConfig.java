package com.zorup.fcm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, Integer.parseInt(port));
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // serializer로 default는 json형태로 사용하도록 세팅 (java serialization보다 용량이 적다)
        // 원래의 default는 java serialization 사용하기때문에, default로 사용시에 Serializable 구현 안한 클래스의 객체 넣을시 예외발생했음
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());

        // 이런식으로 명시적 설정 가능
        redisTemplate.setKeySerializer(new StringRedisSerializer());  // String.getBytes() 사용. 설정시 key값으로 String만 가능
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 트랜잭션 관리에 포함 (@Transactional) (디폴트가 false)
        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }

}
