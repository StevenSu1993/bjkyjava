package com.baojikouyu.teach.pojo;

import lombok.Data;

@Data
public class FilesDto {

    private Integer id;

    private Integer start;

    private Integer size;

    private String creater;
    //根据文件名模糊搜索
    private String name;

    //返回查询 仅自己还是全部 1：自己， 0：all
    private Integer rang;

    //排序规则 0 按名称 1按时间
    private Integer orderValue;

    //按照类型查询  1 表示图片,2 表示文档,3 表示视频,4 表示种子,5 表示音乐,6 表示其它
    private Integer type;
}
