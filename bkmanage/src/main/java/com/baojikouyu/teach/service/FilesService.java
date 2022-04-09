package com.baojikouyu.teach.service;

import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.pojo.FilesDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
* @author Administrator
* @description 针对表【files】的数据库操作Service
* @createDate 2022-03-20 01:00:03
*/
public interface FilesService extends IService<Files> {

    Page<Files> getPageByType(Integer start, Integer size, Integer type, Integer grade,Integer parentFolderId);

//    Future<Boolean> saveFile(String filePath, String newFileName, MultipartFile file);

    Boolean saveFile(String filePath, String newFileName, MultipartFile file, Files files) throws InterruptedException, ExecutionException;

    Files getOneByParentFolderId(Integer parentFolderId);

    Boolean deleteFilesByIds(Integer[] ids);

    Page<Files> getFileByNameAndSort(FilesDto fdto);

    Boolean saveFolder(Files files);

    List<Files> getAllFolder();

    Boolean getFolderByParentFolderIdAndFileName(boolean b, Integer parentFolderId, String floderName);

    Boolean updateByIds(Integer[] ids, Integer to, Integer grade);

    Files getOneById(Integer to);

    List<Files> getBatchById(Integer[] ids);

    List<Files> getChildrenByFolderParentId(Integer id);
}
