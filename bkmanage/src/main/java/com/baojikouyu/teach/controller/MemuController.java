package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.pojo.Menu;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.service.MenuService;
import com.baojikouyu.teach.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MemuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("auth/getMenu")
    @RequiresAuthentication
    public ResponseBean getAllMenu(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            String userName = JWTUtil.getUsername(subject.getPrincipal().toString());
            List<Menu> menus = menuService.getMenuByUserName(userName);
            return new ResponseBean(200, "成功获取菜单", menus);
        } else {
            return new ResponseBean(401, "请登录后再来", null);
        }
    }

}
