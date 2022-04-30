package shu.xyj.secdkill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 设置 key 的序列化方式，因为 key 是 string 类型，可以直接使用 StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 设置 Object 类型的 value 的序列化，
        // 默认是JDK序列化，是二进制的，JdkSerializationRedisSerializer
        // Jackson2JsonRedisSerializer 需要传入 Object 对象，输出的是json字符串
        // GenericJackson2JsonRedisSerializer 也是返回json字符串，但不需要传入对象
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());


        // 设置连接工厂
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
}
