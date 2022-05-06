package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.mapper.UserMapper;
import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.pojo.User;
import com.baojikouyu.teach.service.FilesService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

@SpringBootTest
public class mybatisTest {


    @Autowired
    @Qualifier("userMapper")
    UserMapper userMapper;

    @Autowired
    FilesService filesService;

    @Test
    public void test1() {

        final Page<User> userPage = userMapper.selectPage(new Page<>(1, 2), Wrappers.lambdaQuery());
        System.out.println(userPage.getSize());
        userPage.getRecords().forEach(System.out::println);

    }

    @Test
    public void test2() {

//
//        public Page<Files> getPageByType(Integer orderValueQuery, Integer start, Integer size, Integer type, Integer grade, Integer parentFolderId) {

        final Page<Files> pageByType = filesService.getPageByType(1, 0, 10, 0, null, 85);
        System.out.println(pageByType);

    }





}
