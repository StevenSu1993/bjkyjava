package com.baojikouyu.teach.shiro;

import com.baojikouyu.teach.pojo.User;
import com.baojikouyu.teach.service.UserService;
import com.baojikouyu.teach.util.JWTUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MyRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private RedisTemplate redisTemplate;

    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    @Lazy
    public UserService userService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = JWTUtil.getUsername(principals.toString());

        //获取角色和权限
        User user = userService.getUserAndRoleAndPermission(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new CoustomSimpleAuthorizationInfo();
//        获取角色放到simpleAuthorizationInfo中
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        user.getRoles().forEach(e -> roles.add(e.getName()));
        user.getRoles().forEach(e -> permissions.add(e.getName()));
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permissions);
        //shiro 会自动给你放入到缓存中去
//        redisTemplate.boundHashOps("shiro_redis_authorcache").put(username, user);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {

        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }

        User user = userService.getUser(username);
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (!JWTUtil.verify(token, username, user.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }
        //放到redis缓存中去;
        redisTemplate.boundHashOps("shiro_redis_authencache").put(token, token);
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }

}
