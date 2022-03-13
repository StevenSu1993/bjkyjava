package com.baojikouyu.teach.service;

import com.baojikouyu.teach.pojo.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


public interface UserService extends IService<User> {

    Page<User> getAll(Integer start, Integer size);

    User getUser(String name);

    User getUserAndRole(String name);

    User getUserAndRoleAndPermission(String username);
}
