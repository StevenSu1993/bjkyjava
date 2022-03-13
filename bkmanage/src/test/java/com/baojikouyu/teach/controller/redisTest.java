package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.pojo.User;
import com.baojikouyu.teach.service.UserService;
import com.baojikouyu.teach.shiro.CoustomSimpleAuthorizationInfo;
import com.baojikouyu.teach.shiro.JWTToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description: TODO
 * @author: steven
 * @date:3/4/2022 8:23 PM
 */
@SpringBootTest
class redisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        System.out.println(113);
        User user = new User();
        user.setName("你好");
        redisTemplate.getConnectionFactory().getConnection().flushDb();
        redisTemplate.opsForValue().set("ttt", user);
        redisTemplate.opsForValue().set("hhh", "你好");
        String str = (String) redisTemplate.opsForValue().get("hhh");
        System.out.println(str);

        redisTemplate.opsForHash().put(user.getName(), user.getName(), user);

    }

    @Test
    public void test1() {
        System.out.println(113);
        User user = new User();
        user.setName("你好");
        redisTemplate.boundHashOps("bound").put("boundUser", user);

    }

    @Test
    public void test2() {
        final Set keys = redisTemplate.keys("*");
        System.out.println(keys);

    }


    @Test
    public void test3() {
        final JWTToken jwtToken = new JWTToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InpoYW5nc2FuIn0._dqy3kHA32oIBlsQnw1bPXgUyz4VpcGnsYsNbUsF-WE");
        redisTemplate.boundHashOps("aaaa").put("bbbb", jwtToken);

    }

    @Test
    public void test4() {
        JWTToken jwtToken  = (JWTToken) redisTemplate.boundHashOps("aaaa").get("bbbb");
        System.out.println(jwtToken.getPrincipal());

    }



    @Test
    public void test5() {
        JWTToken jwtToken  = (JWTToken) redisTemplate.boundHashOps("aaaa").get("cccc");
        System.out.println(jwtToken.getPrincipal());
    }


    @Test
    public void test6() {

        //获取角色和权限
        User user = userService.getUserAndRoleAndPermission("zhangsan");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new CoustomSimpleAuthorizationInfo();
        //        获取角色放到simpleAuthorizationInfo中
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        user.getRoles().forEach(e -> roles.add(e.getName()));
        user.getRoles().forEach(e -> permissions.add(e.getName()));
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permissions);
//        放到redis中去
        redisTemplate.boundHashOps("shiro_redis_authorcache").put("zhangsan", simpleAuthorizationInfo);
    }


}
