package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.mapper.UserMapper;
import com.baojikouyu.teach.pojo.User;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class mybatisTest {


    @Autowired
    @Qualifier("userMapper")
    UserMapper userMapper;

    @Test
    public void test1() {

        final Page<User> userPage = userMapper.selectPage(new Page<>(1, 2), Wrappers.lambdaQuery());
        System.out.println(userPage.getSize());
        userPage.getRecords().forEach(System.out::println);

    }





}
