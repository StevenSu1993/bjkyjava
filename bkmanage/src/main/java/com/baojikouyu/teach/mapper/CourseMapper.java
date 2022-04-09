package com.baojikouyu.teach.mapper;

import com.baojikouyu.teach.pojo.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author Administrator
* @description 针对表【course】的数据库操作Mapper
* @createDate 2022-03-14 20:27:13
* @Entity com.baojikouyu.teach.pojo.Course
*/
@Mapper
@Repository
public interface CourseMapper extends BaseMapper<Course> {

}




