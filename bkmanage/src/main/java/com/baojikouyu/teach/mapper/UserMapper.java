package com.baojikouyu.teach.mapper;

import com.baojikouyu.teach.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("SqlDialectInspection")
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {


    @Select("SELECT * FROM user WHERE name = #{username}")
    List<User> findByUsername(@Param("username") String username);

    //数据量小的情况下效率会高
    @Select({"SELECT  u.* , r.id rid, r.name rname, p.id pid, p.name pname" +
            " FROM user u, role r, user_role ur, permission p, user_permission up" +
            " WHERE  u.id = ur.user_id and r.id = ur.role_id and u.id = up.user_id and p.id = up.permission_id" +
            " AND u.name = #{username} "})
    @ResultMap("userRolePermissionMap")
    User getUserAndRoleAndPermission(@Param("username") String username);



}
