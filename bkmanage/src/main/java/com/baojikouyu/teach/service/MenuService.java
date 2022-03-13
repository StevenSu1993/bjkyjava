package com.baojikouyu.teach.service;

import com.baojikouyu.teach.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【menu】的数据库操作Service
* @createDate 2022-03-11 18:06:30
*/
public interface MenuService extends IService<Menu> {

    List<Menu> getMenuByUserId(Integer userId);

    List<Menu> getMenuByUserName(String  userName);
}
