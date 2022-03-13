package com.baojikouyu.teach.service.impl;

import com.baojikouyu.teach.mapper.UserMapper;
import com.baojikouyu.teach.pojo.User;
import com.baojikouyu.teach.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSeviceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<User> getAll(Integer start, Integer size) {
        return userMapper.selectPage(new Page<>(start, size), Wrappers.lambdaQuery());
    }

    @Override
    public User getUser(String name) {
/*        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> eq1 = userQueryWrapper.eq("name", name);
        userMapper.selectOne(eq1);*/
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class).eq(User::getName, name);
        return getOne(wrapper);
    }

    @Override
    public User getUserAndRole(String name) {

        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getName, name));
        return user;
    }

    @Override
    public User getUserAndRoleAndPermission(String username) {
        return userMapper.getUserAndRoleAndPermission(username);
    }

    public User get(String name) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class).eq(User::getName, name);
        return userMapper.selectOne(wrapper);
    }
}
