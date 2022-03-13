package com.baojikouyu.teach.mapper;
import com.baojikouyu.teach.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【permission】的数据库操作Mapper
* @createDate 2022-03-03 19:58:07
* @Entity com.baojikouyu.teach.pojo.Permission
*/
public interface PermissionMapper extends BaseMapper<Permission> {


    List<Permission> findById(@Param("id") Integer id);

}




