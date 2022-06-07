package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.annotation.Log;
import com.baojikouyu.teach.pojo.Comment;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.service.CommentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;


    @Log
    @GetMapping("/auth/getBlobBytes")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public byte[] getBlobBytes() {
        Comment byId = commentService.getById(5);
        return byId.getContent();
    }


    @Log
    @GetMapping("/auth/getBlobBytes1")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean getBlobBytes1() {
        Comment byId = commentService.getById(5);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pojo", byId);
        map.put("content", byId.getContent());
        return new ResponseBean(200, "创建成功", map);
    }


    @Log
    @GetMapping("/auth/getCommentListPage")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean getCommentListPage(Integer start, Integer size, Integer homeWorkId) {
        Integer startQuery = Optional.ofNullable(start).orElse(0);
        Integer sizeQuery = Optional.ofNullable(size).orElse(10);
        if (homeWorkId == null) {
            return new ResponseBean(400, "homeWorkId 不能为空", homeWorkId);
        }
        Page<Comment> page = commentService.getCommentPage(startQuery, sizeQuery, homeWorkId);
        return new ResponseBean(200, "创建成功", page);
    }


    @Log
    @GetMapping("/auth/getCommentListByCommentId")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean getCommentListByCommentId(Integer commentId) {
        if (commentId == null) {
            return new ResponseBean(400, "commentId 不能为空", commentId);
        }
        List<Comment> list = commentService.getCommentLisByCommentId(commentId);
        return new ResponseBean(200, "查询成功", list);
    }

    @Log
    @PostMapping("/auth/creatComment")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean creatComment(MultipartFile[] files, String desc, String startValue,Integer commentId, HttpServletRequest request) {

        log.info("files : {}, desc : {}, startValue : {}, request : {}", files, desc, startValue, request);

        return new ResponseBean(200, "查询成功", 11111);
    }

    @Log
    @PostMapping("/auth/creatComment1")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean creatComment1(@RequestParam MultipartFile files, String desc, String startValue, HttpServletRequest request) {

        log.info("files : {}, desc : {}, startValue : {}, request : {}", files, desc, startValue, request);

        return new ResponseBean(200, "查询成功", 11111);
    }

}
