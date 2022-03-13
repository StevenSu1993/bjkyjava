package com.baojikouyu.teach.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baojikouyu.teach.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
@SuppressWarnings("SqlDialectInspection")
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getAllById(@Param("id") Integer id);

    List<Menu> getAllByRoleId(@Param("roleId") Integer roleId);


    @Select({" SELECT  menu.* " +
            " FROM user, role, user_role, role_menu, menu" +
            " WHERE user_id = user_role.user_id" +
            " AND user_role.role_id = role.id" +
            " AND role.id = role_menu.role_id" +
            " AND menu.id = role_menu.menu_id" +
            " AND user_id = user_role.user_id" +
            " AND user.id = #{userId}"})
    List<Menu> getAllByUserId(@Param("userId") Integer userId);


    @Select({" SELECT  menu.* " +
            " FROM user, role, user_role, role_menu, menu" +
            " WHERE user_id = user_role.user_id" +
            " AND user_role.role_id = role.id" +
            " AND role.id = role_menu.role_id" +
            " AND menu.id = role_menu.menu_id" +
            " AND user_id = user_role.user_id" +
            " AND user.name = #{userName}"})
    List<Menu> getAllByUserName(String userName);
}




