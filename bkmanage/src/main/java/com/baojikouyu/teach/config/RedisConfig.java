package com.baojikouyu.teach.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {


  /*  *//* @Bean
     @SuppressWarnings("all")
     public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)  {
         // 自定义 String Object
         RedisTemplate<String, Object> template = new RedisTemplate();
         // 配置连接工厂
         template.setConnectionFactory(redisConnectionFactory);
         // Json 序列化配置
         Jackson2JsonRedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
         // ObjectMapper 转译
         ObjectMapper objectMapper = new ObjectMapper();
         // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
         objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
         // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会报异
         objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
         objectJackson2JsonRedisSerializer.setObjectMapper(objectMapper);

         // String 的序列化
         StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

         // key 采用String的序列化方式
         template.setKeySerializer(stringRedisSerializer);
         // hash 的key也采用 String 的序列化方式
         template.setHashKeySerializer(stringRedisSerializer);
         // value 序列化方式采用 jackson
         template.setValueSerializer(objectJackson2JsonRedisSerializer);
         // hash 的 value 采用 jackson
         template.setHashValueSerializer(objectJackson2JsonRedisSerializer);
         template.afterPropertiesSet();

         return template;
     }*/
    @Bean
    @Lazy
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会报异
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean("keyGenerator")
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                String string = Arrays.asList(params).toString();
                System.out.println("target: " + target + "method" + method.getName() + "params : " + string);
                return method.getName() + ":" + string;
            }
        };
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        redisCacheConfiguration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        Map<String, RedisCacheConfiguration> redisExpireConfig = new HashMap<>();
        //这里设置了一个一分钟的超时配置，如果需要增加更多超时配置参考这个新增即可
        redisExpireConfig.put("1min", RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .entryTtl(Duration.ofMinutes(1)).disableCachingNullValues());

        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(redisExpireConfig)
                .transactionAware()
                .build();
    }
}
