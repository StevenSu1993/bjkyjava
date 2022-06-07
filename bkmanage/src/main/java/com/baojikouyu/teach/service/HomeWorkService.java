package com.baojikouyu.teach.service;

import com.baojikouyu.teach.pojo.HomeWork;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【home_work】的数据库操作Service
* @createDate 2022-05-06 18:15:44
*/
public interface HomeWorkService extends IService<HomeWork> {

    List<HomeWork> getAllHomeWorkByCourseId(Integer courseId);
}
