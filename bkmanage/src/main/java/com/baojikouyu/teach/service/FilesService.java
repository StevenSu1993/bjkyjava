package com.baojikouyu.teach.service;

import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.pojo.FilesDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
* @author Administrator
* @description 针对表【files】的数据库操作Service
* @createDate 2022-03-20 01:00:03
*/
public interface FilesService extends IService<Files> {

    Page<Files> getPageByType(Integer start, Integer size, Integer type);

//    Future<Boolean> saveFile(String filePath, String newFileName, MultipartFile file);

    Boolean saveFile(String filePath, String newFileName, MultipartFile file, Files files) throws InterruptedException, ExecutionException;

    Boolean deleteFilesByIds(Integer[] ids);

    Page<Files> getFileByNameAndSort(FilesDto fdto);
}
