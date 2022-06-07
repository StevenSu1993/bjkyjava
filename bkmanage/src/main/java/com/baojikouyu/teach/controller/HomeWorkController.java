package com.baojikouyu.teach.controller;

import cn.hutool.core.date.DateTime;
import com.baojikouyu.teach.annotation.Log;
import com.baojikouyu.teach.pojo.HomeWork;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.service.HomeWorkService;
import com.baojikouyu.teach.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeWorkController {

    @Autowired
    private HomeWorkService homeWorkService;


    @Log
    @PostMapping("/auth/createHomeWork")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean createHomeWork(@RequestBody HomeWork homeWork) {
        homeWork.setCreaterId(ShiroUtil.getUserId());
        homeWork.setCreater(ShiroUtil.getUserName());
        homeWork.setCreateTime(DateTime.now());
        homeWorkService.save(homeWork);
        return new ResponseBean(200, "创建成功", homeWork.getName());
    }


    @Log
    @GetMapping("/auth/getAllHomeWorkByCourseId")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean getAllHomeWorkByCourseId(Integer courseId) {


        List<HomeWork> list = homeWorkService.getAllHomeWorkByCourseId(courseId);
        return new ResponseBean(200, "创建成功", list);
    }


}
