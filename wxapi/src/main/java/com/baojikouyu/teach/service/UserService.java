package com.baojikouyu.teach.service;

import com.baojikouyu.teach.dao.UserMapper;
import com.baojikouyu.teach.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

   @Resource
    private UserMapper userMapper;

    public List<User> getUserByname(String name){
        return  userMapper.findByUsername(name);
    }

}
