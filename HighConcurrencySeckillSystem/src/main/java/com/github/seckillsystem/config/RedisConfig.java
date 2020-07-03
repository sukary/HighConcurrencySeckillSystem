package com.github.seckillsystem.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 缓存管理器
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.create(factory);
    }
    /**
     * 序列化java对象
     * 序列化：把对象转化为可传输的字节序列过程称为序列化。
     * 反序列化：把字节序列还原为对象的过程称为反序列化。
     * spring-data-redis默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。
     * Jackson2JsonRedisSerializer: 跟JacksonJsonRedisSerializer实际上是一样的
     * JacksonJsonRedisSerializer: 序列化object对象为json字符串
     * JdkSerializationRedisSerializer: 序列化java对象（被序列化的对象必须实现Serializable接口）,无法转义成对象。
     * StringRedisSerializer: 简单的字符串序列化
     * GenericToStringSerializer:类似StringRedisSerializer的字符串序列化
     * GenericJackson2JsonRedisSerializer:类似Jackson2JsonRedisSerializer，但使用时构造函数不用特定的类
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(connectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
