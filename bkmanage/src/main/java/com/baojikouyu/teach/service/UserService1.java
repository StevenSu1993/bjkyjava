package com.baojikouyu.teach.service;

import com.baojikouyu.teach.mapper.UserMapper;
import com.baojikouyu.teach.pojo.User;
import com.baojikouyu.teach.pojo.UserBean;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService1 {

    @Resource
    private UserMapper userMapper;

    public List<User> getUserByname(String name) {
        return userMapper.findByUsername(name);
    }


    public UserBean getUser(String name) {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        queryWrapper.lambda().eq(User::getName, name);
//        User user = userMapper.selectOne(queryWrapper);
//        return user;
        return null;
    }

    public User getUser1(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.lambda().eq(User::getName, name);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

}
