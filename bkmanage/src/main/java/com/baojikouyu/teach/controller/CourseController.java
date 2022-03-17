package com.baojikouyu.teach.controller;

import cn.hutool.core.date.DateTime;
import com.baojikouyu.teach.annotation.Log;
import com.baojikouyu.teach.pojo.Course;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.service.CourseService;
import com.baojikouyu.teach.util.JWTUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("auth/test")
    public ResponseBean test() {
        System.out.println(111111);
        return new ResponseBean(200, "成功获取菜单", true);
    }


    @PostMapping("auth/test1")
    @RequiresAuthentication
    public ResponseBean test1() {
        System.out.println(111111);
        return new ResponseBean(200, "成功获取菜单", true);
    }

    @Log(desc = "新建课程")
    @PostMapping("auth/createCourse")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean creatCourse(@RequestBody Course course) {
        Subject subject = SecurityUtils.getSubject();
        String userName = JWTUtil.getUsername(subject.getPrincipal().toString());
        Integer userId = JWTUtil.getUserId(subject.getPrincipal().toString());
        course.setCreateTime(DateTime.now());
        course.setCreator(userName);
        course.setCreateId(userId);
        final int i = courseService.create(course);
        if (i > 0) {
            //成功以后 把课程发布到中间件 然后通知给订阅方.把缓存中的值要给清理掉
            return new ResponseBean(200, "成功插入数据库", true);
        } else {
            return new ResponseBean(400, "创建失败", false);
        }
    }


    @GetMapping("/auth/getCountCourse")
    public ResponseBean getCountCourse() {
        long count = courseService.getCountCourse();
        return new ResponseBean(200, "查询课程总数", count);
    }

    @GetMapping("/auth/getCountCourseByName")
    public ResponseBean getCountCourseByName(String name) {
        long count = courseService.getCountCourseByName(name);
        return new ResponseBean(200, "查询课程总数根据传递name", count);
    }

    @GetMapping("/auth/getAllCourse")
    public ResponseBean getAllCourse(Integer start, Integer size, String name) {
        Integer startParam = Optional.ofNullable(start).orElse(0);
        Integer sizeParam = Optional.ofNullable(size).orElse(10);
        Page<Course> allCourseByPage = null;
        if (!StringUtils.isBlank(name)) {
            allCourseByPage = courseService.getCourseByCreateOrCourseName(name, startParam, sizeParam);
        } else {
            allCourseByPage = courseService.getAllCourseByPage(startParam, sizeParam);
        }
        return new ResponseBean(200, "查询课程总数", allCourseByPage);
    }


}
