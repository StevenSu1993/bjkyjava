package com.baojikouyu.teach.mapper;

import com.baojikouyu.teach.pojo.Directory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author Administrator
* @description 针对表【directory】的数据库操作Mapper
* @createDate 2022-03-25 07:14:59
* @Entity com.baojikouyu.teach.pojo.Directory
*/
@Mapper
@Repository
public interface DirectoryMapper extends BaseMapper<Directory> {

}




