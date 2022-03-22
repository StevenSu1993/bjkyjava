package com.baojikouyu.teach.controller;


import cn.hutool.core.lang.UUID;
import com.baojikouyu.teach.annotation.Log;
import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.pojo.FilesDto;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.service.FilesService;
import com.baojikouyu.teach.util.FileTypeUtil;
import com.baojikouyu.teach.util.JWTUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
public class FileController {

    @Value("${upFile.path}")
    private String fileRootPath;

    @Value("${serverUrl}")
    private String serverPath;


    @Autowired
    private FilesService filesService;

    @RequiresAuthentication
    @RequiresRoles("admin")
    @GetMapping("/auth/deleteFileByIds")
    public ResponseBean deleteFilesByIds(Integer[] ids) {
        if (CollectionUtils.isEmpty(Arrays.asList(ids))) {
            return new ResponseBean(400, "请传入正确的参数", false);
        }
        filesService.deleteFilesByIds(ids);
        return new ResponseBean(200, "成功删除文件", null);
    }

    @RequiresAuthentication
    @GetMapping("/auth/getFileByNameAndSort")
    public ResponseBean getFileByNameAndSort(FilesDto fdto) {
        Subject subject = SecurityUtils.getSubject();
        String userName = JWTUtil.getUsername(subject.getPrincipal().toString());
        fdto.setCreater(userName);
        fdto.setRang(Optional.ofNullable(fdto.getRang()).orElse(0));
        fdto.setOrderValue(Optional.ofNullable(fdto.getOrderValue()).orElse(0));
        Page<Files> pageByType = filesService.getFileByNameAndSort(fdto);
        return new ResponseBean(200, "按照名称查询文件并且排序", pageByType);
    }


    @RequiresAuthentication
    @GetMapping("/auth/getFileByType")
    public ResponseBean getFile(Integer start, Integer size, Integer type) {

        Optional.ofNullable(start).orElse(0);
        Optional.ofNullable(size).orElse(10);
        Page<Files> pageByType = filesService.getPageByType(start, size, type);
        return new ResponseBean(200, "成功获取上传的文件", pageByType);
    }

    @Log
    @PostMapping("/auth/uploadFile")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean uploadFile(MultipartFile file, HttpServletRequest request) throws ExecutionException, InterruptedException {
        if (file.isEmpty()) {
            return new ResponseBean(400, "上传的文件不能为空！请重新上传", false);
        }
        if (file.getSize() <= 0) {
            return new ResponseBean(400, "上传的文件大小需要大于0kb", false);
        }
        Subject subject = SecurityUtils.getSubject();
        String userName = JWTUtil.getUsername(subject.getPrincipal().toString());
        Integer userId = JWTUtil.getUserId(subject.getPrincipal().toString());
        System.out.println(file.getContentType());//image/png
        String fileExtension = Objects.requireNonNull(file.getContentType()).split("/")[1].toUpperCase();
        Integer fileType = FileTypeUtil.isFileType(fileExtension);
        String originFileName = file.getOriginalFilename();//获取文件原始的名称
        String uuidFileName = UUID.randomUUID().toString();
        String newFileName = uuidFileName + "." + file.getContentType().split("/")[1];
        //获取项目运行的绝对路径
//        String filePath = System.getProperty("user.dir");
        String filePath = fileRootPath;

        Files files = new Files();
        files.setCreaterName(userName);
        files.setCreaterId(userId);
        files.setFileName(originFileName);
        files.setFilePath(filePath);
        files.setUuidFileName(newFileName);
        files.setFileType(file.getContentType());
        files.setUploadTime(new Date());
        files.setUrl(serverPath + newFileName);
        files.setType(fileType);

        //开启异步任务一个是往数据库中插入，一个是往磁盘中写文件
        Boolean isSuccess = filesService.saveFile(filePath, newFileName, file, files);
        if (!isSuccess) {
            return new ResponseBean(400, "上传的文件失败", newFileName);
        }
        return new ResponseBean(200, "上传的文件成功", newFileName);
    }


}
