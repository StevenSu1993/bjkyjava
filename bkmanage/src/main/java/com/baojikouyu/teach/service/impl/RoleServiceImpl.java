package com.baojikouyu.teach.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baojikouyu.teach.pojo.Role;
import com.baojikouyu.teach.service.RoleService;
import com.baojikouyu.teach.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2022-03-03 20:08:01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public Role getRoleById(Integer id) {
        return roleMapper.selectById(id);
    }

}




