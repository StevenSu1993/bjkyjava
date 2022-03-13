package com.baojikouyu.teach.shiro;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

public class CustomAuthenticationCache<K, V> implements Cache<K, V> {

    public CustomAuthenticationCache() {
    }

    public CustomAuthenticationCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public RedisTemplate redisTemplate;

    private final static String cacheKey = "shiro_redis_authencache";

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public V get(K key) throws CacheException {
        BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
//        Object k = hashKey(key);
        V v = hash.get(key);
        if (v == null) {
            //第一次在缓存中拿不到直接返回null ，然后继续走数据库
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(v, v, "my_realm");
        System.out.println("登录缓存命中");
        return (V) authenticationInfo;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
//        Object k = hashKey(key);
        hash.put((K) key, value);
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

//    protected Object hashKey(K key) {
//        //此处很重要,如果key是登录凭证,那么这是访问用户的授权缓存;将登录凭证转为user对象,
//        //返回user的name属性做为hash key,否则会以user对象做为hash key,这样就不好清除指定用户的缓存了
//        if (key instanceof String){
//            String username = JWTUtil.getUsername((String) key);
//            return username;
//        }
//        if (key instanceof PrincipalCollection) {
//            PrincipalCollection pc = (PrincipalCollection) key;
//            User user = (User) pc.getPrimaryPrincipal();
//            return user.getName();
//        } else if (key instanceof User) {
//            User user = (User) key;
//            return user.getName();
//        }
//        return key;
//    }
}
