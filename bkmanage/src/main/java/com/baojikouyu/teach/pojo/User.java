package com.baojikouyu.teach.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName(value = "user")
public class User implements Serializable {


    private Integer id;
    private String name;

    private String password;
    @TableField("avatar_url")
    private String avatarUrl;
    private String phone;
    //备注 desc 为mysql 的关键字
    private String note;
    @TableField("total_course")
    private int totalCourse;
    @TableField("last_submit")
    private Date lastSubmit;
    @TableField("last_submit_course")
    private String lastSubmitCourse;
    @TableField("last_submit_course_id")
    private int lastSubmitCourseId;
    @TableField(exist = false)
    private List<Role> roles;
    @TableField(exist = false)
    private List<Permission> permissions;

}
