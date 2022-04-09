package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.pojo.Course;
import com.baojikouyu.teach.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CourseTest {

    @Autowired
    private CourseService courseService;


    @Test
    public void test(){
        final Page<Course> allCourseByPage = courseService.getAllCourseByPage(0, 10);
        System.out.println(allCourseByPage);

    }



    @Test
    public void test2(){
        Course course = new Course();

        course.setName("这是tessfsdt");
            courseService.create(course);


    }
}
