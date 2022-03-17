package com.baojikouyu.teach.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @TableName menu
 */
@TableName(value = "menu")
@Data
public class Menu implements Serializable {

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
    private String url;

    /**
     *
     */
    private Integer ischild;

    /**
     *
     */
    private Integer pid;

    /**
     *
     */
    private Integer roleId;

    /**
     *
     */
    private Date creatTime;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private Integer type;

    /**
     *
     */
    private Integer createrId;

    /**
     *
     */
    private String createName;

    private String component;

    private List<Menu> childMenu = new LinkedList<>();

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
