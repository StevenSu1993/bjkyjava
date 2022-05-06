package com.baojikouyu.teach.service.impl;

import com.baojikouyu.teach.mapper.MenuMapper;
import com.baojikouyu.teach.pojo.Menu;
import com.baojikouyu.teach.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【menu】的数据库操作Service实现
 * @createDate 2022-03-11 18:06:30
 */
@Service
@CacheConfig(cacheNames = "menu",keyGenerator = "keyGenerator")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Autowired
    @Qualifier("menuMapper")
    private MenuMapper menuMapper;


    @Override
//    @Cacheable(value = "getMenuByUserId", key = "#userId")
    @Cacheable
    public List<Menu> getMenuByUserId(Integer userId) {
        return menuMapper.getAllByUserId(userId);
    }

    @Override
    public List<Menu> getMenuByUserName(String userName) {
        List<Menu> treeMenu = getTreeMenu(menuMapper.getAllByUserName(userName));
        treeMenu.sort((m1, m2) -> m1.getId() - m2.getId());//默认是升序
        return treeMenu;
    }

    public List<Menu> getTreeMenu(List<Menu> menus) {
        ArrayList<Menu> finalMenus = Lists.newArrayList();
        if (menus != null && menus.size() > 0) {
            //因为在数据中返回的menu该字段并没有赋值
//            menus.forEach(menu -> menu.setChildMenu(new LinkedList<>()));
            for (Menu menu : menus) {
                for (Menu menu1 : menus) {
                    if (menu.getId() == menu1.getPid()) {
                        menu.getChildMenu().add(menu1);
                    }
                }
                // 提取出父节点
                if (menu.getPid() == null || menu.getPid() == 0) {
                    finalMenus.add(menu);
                }
            }
        }
        //循环变量
        return finalMenus;
    }
}




