package com.baojikouyu.teach.service;

import com.baojikouyu.teach.pojo.Course;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【course】的数据库操作Service
* @createDate 2022-03-14 20:27:13
*/
public interface CourseService extends IService<Course> {

    int create(Course course);

    Page<Course> getAllCourseByPage(Integer start , Integer size);

    long getCountCourse();

    long getCountCourseByName(String name);

    Page<Course> getCourseByCreateOrCourseName(String name,Integer start , Integer size);
}
