package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.pojo.User;
import com.baojikouyu.teach.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class hello {

    Logger logger = LoggerFactory.getLogger("hello");


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    public String hello() {
        logger.info("this  is  my firt controller");
        return "hellofromwxapi";


    }


    @RequestMapping("/getUser")
    public List<User> getUser( String name) {
        logger.info("this  is  getUser {}", name);
        List<User> userByname = userService.getUserByname(name);
        for (User user : userByname) {
            logger.info("数据库拿到的数据 ：{}", user);
            logger.info("数据库拿到的数据名字  ：{}", user.getName());
        }

        return userByname;
    }


    @GetMapping(value = "/info")
    @Cacheable(value = "user1", key = "#id")
    public User getUser(@RequestParam(value = "id") Integer id) {
        User user = new User();
        System.out.println("看是否进入到缓存中去了不");
        user.setId(id);
        user.setName(id + ":" + System.currentTimeMillis());
        return user;
    }
}
