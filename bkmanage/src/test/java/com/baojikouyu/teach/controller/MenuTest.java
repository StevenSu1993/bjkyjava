package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.mapper.MenuMapper;
import com.baojikouyu.teach.pojo.Menu;
import com.baojikouyu.teach.service.impl.MenuServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MenuTest {

    @Autowired
    private MenuServiceImpl menuService;

    @Autowired
    private MenuMapper menuMapper;

    @Test
    public void test(){

        final List<Menu> allById = menuMapper.getAllById(1);
        System.out.println(allById);

    }


    @Test
    public void test1(){
        final List<Menu> allById = menuMapper.getAllByUserId(3);
        System.out.println(allById);

    }


    @Test
    public void test2(){
        final List<Menu> admin = menuService.getMenuByUserName("admin");
        for (Menu menu : admin) {
            System.out.println(menu.getId());
        }
    }



}
