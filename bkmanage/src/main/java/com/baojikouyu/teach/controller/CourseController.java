package com.baojikouyu.teach.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import com.baojikouyu.teach.annotation.Log;
import com.baojikouyu.teach.pojo.Course;
import com.baojikouyu.teach.pojo.Menu;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.service.CourseService;
import com.baojikouyu.teach.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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

    @Log("新建课程")
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
            //成功以后 把课程发布到中间件 然后通知给订阅方
            return new ResponseBean(200, "成功插入数据库", true);
        } else {
            return new ResponseBean(400, "创建失败", false);
        }
      /*  if (subject.isAuthenticated()) {
            List<Menu> menus = menuService.getMenuByUserName(userName);
            return new ResponseBean(200, "成功获取菜单", menus);
        } else {

        }
*/
    }


}
