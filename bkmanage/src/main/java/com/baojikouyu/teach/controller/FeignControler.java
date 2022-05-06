package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.feign.wxapi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeignControler {

    @Autowired
    private wxapi wxapi;

    @GetMapping("getHello")
    public Object getHello(String name) {
        System.out.println("浏览器传递过来的参数 ： " + name);
        List<Object> user = wxapi.getUser(name);
//        List<User> user1 = wxapi.getUser1(name);
        System.out.println(user);
//        System.out.println(user1);
        return user;
    }
}
