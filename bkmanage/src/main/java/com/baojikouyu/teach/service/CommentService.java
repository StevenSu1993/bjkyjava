package com.baojikouyu.teach.service;

import com.baojikouyu.teach.pojo.Comment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【comment】的数据库操作Service
* @createDate 2022-05-13 18:58:37
*/
public interface CommentService extends IService<Comment> {

    Page<Comment> getCommentPage(Integer start, Integer size, Integer homeWorkId);

    List<Comment> getCommentLisByCommentId(Integer commentId);
}
