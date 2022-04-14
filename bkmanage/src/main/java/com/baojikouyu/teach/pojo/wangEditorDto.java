package com.baojikouyu.teach.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class wangEditorDto implements Serializable {

    private Integer errno;
    // 如果是视频就是返对象。 图片就是对象数组
    private Object data;
}
