package com.baojikouyu.teach.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baojikouyu.teach.pojo.Comment;
import com.baojikouyu.teach.service.CommentService;
import com.baojikouyu.teach.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【comment】的数据库操作Service实现
 * @createDate 2022-05-13 18:58:37
 */
@Service
@CacheConfig(cacheNames = "comment", keyGenerator = "keyGenerator")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Cacheable
    public Page<Comment> getCommentPage(Integer start, Integer size, Integer homeWorkId) {
        Page<Comment> page = new Page<>();
        page.setCurrent(start);
        page.setSize(size);
        LambdaQueryWrapper<Comment> queryWrapper = Wrappers.lambdaQuery(Comment.class);
        queryWrapper.eq(Comment::getHomeWorkId, homeWorkId);
        return commentMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Cacheable
    public List<Comment> getCommentLisByCommentId(Integer commentId) {
        LambdaQueryWrapper<Comment> queryWrapper = Wrappers.lambdaQuery(Comment.class);
        queryWrapper.eq(Comment::getCommentId, commentId);
        return commentMapper.selectList(queryWrapper);
    }
}




