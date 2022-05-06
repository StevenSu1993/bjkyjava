package com.baojikouyu.teach.mapper;

import com.baojikouyu.teach.pojo.Template;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author Administrator
* @description 针对表【template】的数据库操作Mapper
* @createDate 2022-04-18 05:56:20
* @Entity com.baojikouyu.teach.pojo.Template
*/
@Mapper
@Repository
public interface TemplateMapper extends BaseMapper<Template> {

}




