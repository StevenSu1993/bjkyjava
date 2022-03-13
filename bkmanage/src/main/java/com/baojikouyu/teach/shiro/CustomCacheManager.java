package com.baojikouyu.teach.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

//@Component
public class CustomCacheManager<K, V> implements CacheManager {

//    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        CustomAuthenticationCache<K, V> kvCustomAuthenticationCache = new CustomAuthenticationCache<>();
        kvCustomAuthenticationCache.setRedisTemplate(redisTemplate);
        return kvCustomAuthenticationCache;
    }
}
