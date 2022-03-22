package com.baojikouyu.teach.mapper;

import com.baojikouyu.teach.pojo.Files;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author Administrator
* @description 针对表【files】的数据库操作Mapper
* @createDate 2022-03-20 01:00:03
* @Entity com.baojikouyu.teach.pojo.Files
*/
@Mapper
@Repository
public interface FilesMapper extends BaseMapper<Files> {

}




