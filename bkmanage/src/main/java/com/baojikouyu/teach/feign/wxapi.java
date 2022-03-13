package com.baojikouyu.teach.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//  name 为调用服务的服务名 (针对的是nacos 配置下)
// path 对应的就是Controller指定的requsetMapping, 如果Controller对应类上没有指定requestMapping 就可以不写
//url 如果没有nacos配置，指定服务地址端口号，名字就可以随便取值了
@FeignClient(name = "wxapiaaa", url = "http://localhost:8082")
//@FeignClient(name = "wxapi")
public interface wxapi {

    @RequestMapping("/getUser")
    List<Object> getUser(@RequestParam("name") String name);

/*
    @RequestMapping("/getUser")
    List<User> getUser1(@RequestParam("name") String name);*/

}
