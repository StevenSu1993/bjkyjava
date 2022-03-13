package com.baojikouyu.teach.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

@Slf4j
public class CustomAuthorizationCache<K, V> implements Cache<K, V> {

    private final static String cacheKey = "shiro_redis_authorcache";


    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    private RedisTemplate redisTemplate;


    public CustomAuthorizationCache() {
    }

    public CustomAuthorizationCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public V get(K key) throws CacheException {
        BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
        return hash.get(key.toString());
//        Object k = hashKey(key);
//        String username = JWTUtil.getUsername(key.toString());
//        User user;
//        user = (User) hash.get(username);
//        if (user == null) {
//            return null;
//        }
//        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
////        获取角色放到simpleAuthorizationInfo中
//        Set<String> roles = new HashSet<>();
//        Set<String> permissions = new HashSet<>();
//        user.getRoles().forEach(e -> roles.add(e.getName()));
//        user.getRoles().forEach(e -> permissions.add(e.getName()));
//        simpleAuthorizationInfo.addRoles(roles);
//        simpleAuthorizationInfo.addStringPermissions(permissions);
//        System.out.println("去缓存中拿认证信息");
//        return (V) simpleAuthorizationInfo;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        String strKey = key.toString();
        BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
//        Object k = hashKey(key);
        try {
            hash.put((K) strKey, value);
        } catch (Exception e) {
            log.info(e.getMessage());

        }
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
//        Object k = hashKey(key);
        V value = hash.get(key);
        hash.delete(key);
        return value;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.delete(cacheKey);
    }

    @Override
    public int size() {
        BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
        return hash.size().intValue();
    }

    @Override
    public Set<K> keys() {
        BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
        return hash.keys();
    }

    @Override
    public Collection<V> values() {
        BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
        return hash.values();
    }
}
