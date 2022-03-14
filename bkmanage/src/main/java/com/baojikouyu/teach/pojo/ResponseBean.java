package com.baojikouyu.teach.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean implements Serializable {

    // http 状态码
    private int code;
    // 返回信息
    private String msg;
    // 返回的数据
    private Object data;
}
