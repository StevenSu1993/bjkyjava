package com.baojikouyu.teach.mapper;

import com.baojikouyu.teach.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@SuppressWarnings("SqlDialectInspection")
public interface RoleMapper extends BaseMapper<Role> {

    @Select({"SELECT r_role.* " +
            " FROM role r_role , user_role u_r" +
            "AND r_role.id = u_r.id" +
            "WHERE r_role.id = #{roleId}"})
    List<Role> getById(@Param("id") Integer id);


    @Select({"SELECT r_role.* " +
            "FROM role r_role , user_role u_r" +
            "AND r_role.id = u_r.id" +
            "WHERE u_r.id = #{id}"})
    List<Role> getAllRoleByUserId(@Param("id") Integer id);




}




