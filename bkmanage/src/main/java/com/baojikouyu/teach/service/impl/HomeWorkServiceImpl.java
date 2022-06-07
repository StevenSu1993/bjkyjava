package com.baojikouyu.teach.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baojikouyu.teach.pojo.HomeWork;
import com.baojikouyu.teach.service.HomeWorkService;
import com.baojikouyu.teach.mapper.HomeWorkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【home_work】的数据库操作Service实现
 * @createDate 2022-05-06 18:15:44
 */
@Service
@CacheConfig(cacheNames = "home_work", keyGenerator = "keyGenerator")
public class HomeWorkServiceImpl extends ServiceImpl<HomeWorkMapper, HomeWork>
        implements HomeWorkService {

    @Autowired
    private HomeWorkMapper homeWorkMapper;

    @Override
    @CacheEvict(cacheNames = "home_work",
            allEntries = true)
    public boolean save(HomeWork entity) {
        return super.save(entity);
    }

    @Override
    @Cacheable
    public List<HomeWork> getAllHomeWorkByCourseId(Integer courseId) {
        LambdaQueryWrapper<HomeWork> lambdaQuery = Wrappers.lambdaQuery(HomeWork.class);
        lambdaQuery.eq(courseId != null, HomeWork::getCourseId, courseId);
        return homeWorkMapper.selectList(lambdaQuery);
    }
}




