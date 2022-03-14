package com.baojikouyu.teach.controller;

import cn.hutool.core.util.IdUtil;
import com.baojikouyu.teach.exception.UnauthorizedException;

import com.baojikouyu.teach.pojo.AuthUserDto;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.pojo.User;
import com.baojikouyu.teach.service.UserService;
import com.baojikouyu.teach.util.JWTUtil;
import com.baojikouyu.teach.util.RedisUtils;
import com.baojikouyu.teach.util.RsaProperties;
import com.baojikouyu.teach.util.RsaUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/auth/getCodeByNumber")
    public ResponseBean getCode() {
        // 获取运算的结果
        Captcha captcha = new ArithmeticCaptcha(111, 36);
//        Captcha captcha = new SpecCaptcha(111, 36);
        captcha.setLen(2);
        String uuid = "code-key-" + IdUtil.simpleUUID();
        String captchaValue = captcha.text();
        // 保存 缓存时间2分钟
        redisUtils.set(uuid, captchaValue, 120L);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
//        log.info(captcha.toBase64());
        return new ResponseBean(200, "获取验证码成功", imgResult);
    }

    @GetMapping("/auth/getCode")
    public ResponseBean getCodeByNormal() {
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        String captchaValue = specCaptcha.text().toLowerCase();


        String uuid = "code-key-" + IdUtil.simpleUUID();
        // 保存 缓存时间2分钟
        redisUtils.set(uuid, captchaValue, 120L);

        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", specCaptcha.toBase64());
            put("uuid", uuid);
        }};

//        log.info(captcha.toBase64());
        return new ResponseBean(200, "获取验证码成功", imgResult);
    }


    @PostMapping(value = "/auth/login")
    public ResponseBean login(@Validated @RequestBody AuthUserDto authUser) throws Exception {
        // 前段传递的密码是进过ras加密的
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 查询Redis中的验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new Exception("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new Exception("验证码错误");
        }
        //认证授权
        User getUser = userService.getUser(authUser.getUsername());
        //数据库中存放的密码都是经过base64加密的
        if (Base64.decodeToString(getUser.getPassword()).equals(password)) {
            final String sign = JWTUtil.sign(getUser.getId(), getUser.getName(), getUser.getPassword());
            final Integer userId = JWTUtil.getUserId(sign);
            System.out.println(userId);
            //放入到缓存中去
            if (authUser.getRememberMe()) {
                //往缓存中放置七天。默认是一天
                redisUtils.hset("shiro_redis_authencache", sign, sign, 60 * 60 * 24 * 7);
            } else {
                redisUtils.hset("shiro_redis_authencache", sign, sign, 60 * 60 * 24);
            }
            return new ResponseBean(200, "Login success", sign);
        } else {
            throw new UnauthorizedException();
        }
    }


    @GetMapping("/auth/getAllUser")
    @Cacheable(value = "getAllUser")
    public ResponseBean getAll(Integer start, Integer size) {
        Optional.ofNullable(start).orElse(0);
        Optional.ofNullable(size).orElse(10);
        log.info("进入到getAllUser");
        Page<User> userPage = userService.getAll(start, size);
        log.info("查询到所有用户分页数据: {}", userPage);
        return new ResponseBean(200, "获取用户成功", userPage);
    }


    @PostMapping("/login")
    public ResponseBean login(@RequestBody User user) {
        User getUser = userService.getUser(user.getName());
        if (getUser.getPassword().equals(user.getPassword())) {
            final String sign = JWTUtil.sign(getUser.getId(), user.getName(), user.getPassword());
//        Object k = hashKey(key);
//            redisTemplate.boundHashOps("shiro_redis_authencache").put(sign, sign);
            return new ResponseBean(200, "Login success", sign);
        } else {
            throw new UnauthorizedException();
        }
    }


    @GetMapping("/getMenu")
    @RequiresAuthentication
    public ResponseBean requireAuth() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {


            return new ResponseBean(200, "You are already logged in", null);
        } else {
            return new ResponseBean(200, "请先登录", null);
        }
    }


}
