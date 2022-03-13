package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class nacosController {

    @Autowired
    private RestTemplate restTemplate;

    @Log
    @GetMapping("/getfenHello")
    public String gethello() {
        log.info("进入getHello方法");
        return restTemplate.getForObject("http://wxapi/hello", String.class);
    }

}
