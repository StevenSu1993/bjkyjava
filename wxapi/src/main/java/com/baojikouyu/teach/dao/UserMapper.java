package com.baojikouyu.teach.dao;

import com.baojikouyu.teach.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE name = #{username}")
    List<User> findByUsername(@Param("username") String username);

}
