package com.baojikouyu.teach.mapper;

import com.baojikouyu.teach.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @description 针对表【comment】的数据库操作Mapper
 * @createDate 2022-05-13 18:58:37
 * @Entity com.baojikouyu.teach.pojo.Comment
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

}




