package com.baojikouyu.teach.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @TableName template
 */
@TableName(value = "template")
@Data
public class Template implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creater;

    /**
     *
     */
    private Integer createrId;

    /**
     * 内容(富文本的内容)
     */
    private String content;

    private Boolean isFolder;


    private Integer folderGrade;

    private Integer parentFolderId;

    @TableField(exist = false)
    private List<Template> childrenFolder = new ArrayList<>();

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
