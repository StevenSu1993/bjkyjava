package com.baojikouyu.teach.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baojikouyu.teach.pojo.Course;
import com.baojikouyu.teach.service.CourseService;
import com.baojikouyu.teach.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @description 针对表【course】的数据库操作Service实现
 * @createDate 2022-03-14 20:27:13
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
        implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    // 指定清除user缓存区所有缓存数据
    @CacheEvict(
            value = {"getAllCourseByPage",
                    "getCountCourse",
                    "getCountCourseByName",
                    "getCourseByCreateOrCourseName"},
            allEntries = true)
    @Transactional
    public int create(Course course) {
        return courseMapper.insert(course);
    }


    @Override
    @Cacheable(value = "getAllCourseByPage")
    public Page<Course> getAllCourseByPage(Integer start, Integer size) {
        return courseMapper.selectPage(new Page<>(start, size), Wrappers.lambdaQuery());
    }

    @Override
    @Cacheable(value = "getCountCourse")
    public long getCountCourse() {
        return this.count();
    }

    @Override
    @Cacheable(value = "getCountCourseByName")
    public long getCountCourseByName(String name) {
        LambdaQueryWrapper<Course> queryWrapper = Wrappers.lambdaQuery(Course.class).like(Course::getName, name).or().eq(Course::getCreator, name);
        return this.count(queryWrapper);
    }


    @Override
    @Cacheable(value = "getCourseByCreateOrCourseName")
    public Page<Course> getCourseByCreateOrCourseName(String name, Integer start, Integer size) {
        LambdaQueryWrapper<Course> queryWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<Course> eq = queryWrapper.like(Course::getName, name).or().eq(Course::getCreator, name);
        return courseMapper.selectPage(new Page<>(start, size), eq);
    }


}




