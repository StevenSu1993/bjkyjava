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
 * @TableName files
 */
@TableName(value ="files")
@Data
public class Files implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private String fileName;

    /**
     *
     */
    private String filePath;

    /**
     *
     */
    private Date uploadTime;


    private String uuidFileName;

    /**
     *
     */
    private String fileType;

    private Integer type;

    private String url;

    /**
     *
     */
    private String createrName;

    /**
     *
     */
    private Integer createrId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
