package com.baojikouyu.teach.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String path;

    /**
     *
     */
    private Integer homeWorkId;

    /**
     *
     */
    private Integer courseId;

    /**
     *
     */
    private Integer userId;

    /**
     *
     */
    private String userName;

    /**
     * 上传时间
     */
    private Date createTime;

    /**
     * 评论的文字消息
     */
    private String textContent;

    /**
     * 点评的分数
     */
    private Integer score;

    /**
     * 点评的某个评论
     */
    private Integer commentId;

    /**
     * 点评的文字内容
     */
    private String commentTextContent;

    /**
     * 点评语音的url
     */
    private String commentPath;


    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 音频内容
     */
    private byte[] content;

    /**
     * 点评语音
     */
    private byte[] commentVoice;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
