package com.baojikouyu.teach.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName directory
 */
@TableName(value ="directory")
@Data
public class Directory implements Serializable {
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
    private Integer createrId;

    /**
     *
     */
    private String creater;

    /**
     *
     */
    private Date createTime;

    /**
     * 目录级别
     */
    private Integer grade;

    /**
     * 是否是夫目录
     */
    private Boolean isParent;

    /**
     * 夫目录的id
     */
    private Integer parentId;

    private Directory parentDirectory;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
