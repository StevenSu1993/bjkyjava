package com.baojikouyu.teach.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class ShiroUtil {


    public  static String getUserName(){
        Subject subject = SecurityUtils.getSubject();
        return JWTUtil.getUsername(subject.getPrincipal().toString());
    }

    public static Integer getUserId(){
        Subject subject = SecurityUtils.getSubject();
        return JWTUtil.getUserId(subject.getPrincipal().toString());
    }
}
