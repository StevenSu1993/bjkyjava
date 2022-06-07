package com.baojikouyu.teach.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName home_work
 */
@TableName(value = "home_work")
@Data
public class HomeWork implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private String creater;

    /**
     *
     */
    private Integer createrId;

    /**
     *
     */
    private String content;

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 课程开始时间
     */
    private Date startTime;

    /**
     * 课程结束时间
     */
    private Date stopTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
