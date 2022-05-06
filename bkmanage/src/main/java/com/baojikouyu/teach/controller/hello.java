package com.baojikouyu.teach.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.baojikouyu.teach.annotation.Log;
import com.baojikouyu.teach.pojo.DemoData;
import com.baojikouyu.teach.pojo.User;
import com.baojikouyu.teach.service.UserService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class hello {

    Logger logger = LoggerFactory.getLogger("hello");


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    @Lazy
    private UserService1 userService1;

    @Log
    @RequestMapping("/hello")
    public String hello1() throws InterruptedException {
        Thread.sleep(5000L);
        logger.info("this  is  my firt controller");
        return "hworddsds";
    }


    @RequestMapping("/getUser")
    public List<User> getUser(String name) {
        logger.info("this  is  getUser", name);
        return userService1.getUserByname(name);
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

    @GetMapping("/write")
    public String writeExcel() {
        String fileName = "aaa.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        // 分页查询数据
        EasyExcel.write(fileName, DemoData.class)
                .sheet("模板")
                .doWrite(this::data);
        //下载excel 的话就是忘response的输出流中把文件的数据写进去就行

        return "写入成功";

    }

    public List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }
}
