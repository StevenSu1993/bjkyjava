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
 * @TableName course
 */
@TableName(value ="course")
@Data
public class Course implements Serializable {
    /**
     *让主键递增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

/*    @TableId
    private String id;*/

    /**
     *
     */
    private String className;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private Integer workNum;

    /**
     *
     */
    private Integer studentNum;

    /**
     *
     */
    private String creator;

    /**
     *
     */
    private Integer workSubmitNum;

    /**
     *
     */
    private Date createTime;

    private  String hint;

    private  String hintText;

    private  Integer createId;

    /**
     *
     */
    private Integer classId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
