package com.baojikouyu.teach.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilesDto implements Serializable {

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

    private String folderName;

    private Boolean isFolder;

    private Boolean excludeFolder;

    private Integer parentFolderId;

    private Integer folderGrade;

    //id 集合
    private Integer[] ids;

    // 要移动到的父目录的id
    private Integer to;

    private Integer curentFolderId;

    private Boolean isCreated;

}
