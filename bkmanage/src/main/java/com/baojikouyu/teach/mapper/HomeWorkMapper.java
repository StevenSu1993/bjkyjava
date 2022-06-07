package com.baojikouyu.teach.mapper;

import com.baojikouyu.teach.pojo.HomeWork;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @description 针对表【home_work】的数据库操作Mapper
 * @createDate 2022-05-06 18:15:44
 * @Entity com.baojikouyu.teach.pojo.HomeWork
 */
@Mapper
@Repository
public interface HomeWorkMapper extends BaseMapper<HomeWork> {

}




