package com.baojikouyu.teach.service;

import com.baojikouyu.teach.pojo.Directory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【directory】的数据库操作Service
* @createDate 2022-03-25 07:14:59
*/
public interface DirectoryService extends IService<Directory> {

    Boolean saveDirectory(Directory directory);
}
