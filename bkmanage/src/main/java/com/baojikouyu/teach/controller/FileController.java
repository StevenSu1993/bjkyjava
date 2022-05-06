package com.baojikouyu.teach.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.baojikouyu.teach.annotation.Log;
import com.baojikouyu.teach.constant.rabbitMqConstant;
import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.pojo.FilesDto;
import com.baojikouyu.teach.pojo.ResponseBean;
import com.baojikouyu.teach.pojo.wangEditorDto;
import com.baojikouyu.teach.service.FilesService;
import com.baojikouyu.teach.util.FileTypeUtil;
import com.baojikouyu.teach.util.ShiroUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class FileController {

    @Value("${upFile.path}")
    private String fileRootPath;

    @Value("${serverUrl}")
    private String serverPath;

    @Autowired
    private RabbitTemplate rabbitTemplate;

/*    @Autowired
    private DirectoryService directoryService;*/

    @Autowired
    private FilesService filesService;


    @RequiresAuthentication
    @RequiresRoles("admin")
    @GetMapping("/auth/moveFileToFolder")
    public ResponseBean moveFileToFolder(FilesDto fdto) {
        if (fdto.getIds().length < 1 || fdto.getTo() == null) {
            return new ResponseBean(400, "请求参数有误，请核对后在提交", "请求参数有误，请核对后在提交");
        }
        Files files = filesService.getOneById(fdto.getTo());
        if (files != null) {
            Boolean isSucess = filesService.updateByIds(fdto.getIds(), fdto.getTo(), files.getFolderGrade() + 1);
            if (isSucess) {
                // 根据ids 查询出所有的文件夹，然后放到rabbitmq 中去更新数据库操作。，为啥放到中间件中呢，
                // 如果文件夹下的文件很多在这里做更新grade 操作那么效率是非常慢的。用户体验相当不好

                List<Files> all = filesService.getBatchById(fdto.getIds());
                final List<Files> allFolder = all.stream().filter(f -> f.getIsFolder()).collect(Collectors.toList());
//                recursion(AllFolder);
                rabbitTemplate.convertAndSend(rabbitMqConstant.UPDATEGRADEEXCHANGE, rabbitMqConstant.UPDATEGRADEROUTINGKEY, allFolder);
                return new ResponseBean(200, "修改成功", "移动成功");
            } else {
                return new ResponseBean(400, "修改失败", "修改失败");
            }
        } else {
            return new ResponseBean(400, "不好意思您操作的目录已经被别人删除了", "不好意思您操作的目录已经被别人删除了");

        }


    }

    @RequiresAuthentication
    @GetMapping("/auth/getParentFolderId")
    public ResponseBean getParentFolderId(FilesDto fdto) {
        if (fdto.getCurentFolderId() == null) {
            return new ResponseBean(400, "curentFolderId不能为null ", "curentFolderId不能为null");
        } else {
            Files parent = filesService.getOneById(fdto.getCurentFolderId());
            return new ResponseBean(200, "查到当前文件对应的bean ", parent);
        }
    }

    @RequiresAuthentication
    @RequiresRoles("admin")
    @GetMapping("/auth/createFolder")
    public ResponseBean createFolder(FilesDto fdto) {

        if (fdto.getParentFolderId() == null) {
            return new ResponseBean(400, "parentFolderId不能为null ", "parentFolderId不能为null");
        }

        // 首先判断是否重复名字了。
        Boolean isExist = filesService.getFolderByParentFolderIdAndFileName(true, fdto.getParentFolderId(), fdto.getFolderName());

        if (isExist) {
            return new ResponseBean(400, "同级目录中已经存在了改名字，请换一个", "同级目录中已经存在了改名字，请换一个");
        }
        Files parent = filesService.getOneByParentFolderId(fdto.getParentFolderId());
        Files files = new Files();

        files.setFileName(fdto.getFolderName());
        files.setIsFolder(true);
        files.setParentFolderId(fdto.getParentFolderId());
        files.setFolderGrade(parent.getFolderGrade() + 1);
        files.setUploadTime(DateUtil.date());
        files.setType(7);
        files.setCreaterName(ShiroUtil.getUserName());
        files.setCreaterId(ShiroUtil.getUserId());
        Boolean isSuccess = filesService.saveFolder(files);
        if (isSuccess) {
            // mybaits 默认的情况会把id给赋值成功。所以传递给前端的时候是有id的
            return new ResponseBean(200, "成功创建文件夹", files);
        } else {

            // 如果没有插入成功就用消息中间件在去插一次
            rabbitTemplate.convertAndSend(rabbitMqConstant.FOLDEREXCHANGE, rabbitMqConstant.FOLDERROUTINGKEY, files);
            return new ResponseBean(201, "正在创建中请稍后", files);
        }
    }


    @RequiresAuthentication
    @RequiresRoles("admin")
    @GetMapping("/auth/getAllFolder")
    public ResponseBean getAllFolder() {

        List<Files> filesList = filesService.getAllFolder();
        return new ResponseBean(200, "成功获取所有文件夹", filesList);
    }


    @RequiresAuthentication
    @RequiresRoles("admin")
    @GetMapping("/auth/deleteFileByIds")
    public ResponseBean deleteFilesByIds(FilesDto fdto) {
        if (CollectionUtils.isEmpty(Arrays.asList(fdto.getIds()))) {
            return new ResponseBean(400, "请传入正确的参数", false);
        }
        filesService.deleteFilesByIds(fdto.getIds());
        return new ResponseBean(200, "成功删除文件", null);
    }

    @RequiresAuthentication
    @GetMapping("/auth/getFileByNameAndSort")
    public ResponseBean getFileByNameAndSort(FilesDto fdto) {
        String userName = ShiroUtil.getUserName();
        fdto.setCreater(userName);
        fdto.setRang(Optional.ofNullable(fdto.getRang()).orElse(0));
        fdto.setOrderValue(Optional.ofNullable(fdto.getOrderValue()).orElse(0));

        // fdto.getExcludeFolder() 如果为null ,则查询所有包括文件夹
        if (fdto.getExcludeFolder() == null || fdto.getExcludeFolder() == false) {
            Page<Files> pageByType = filesService.getFileByNameAndSort(fdto);
            return new ResponseBean(200, "按照名称查询文件并且排序", pageByType);
        } else { // 否则就是模糊查询，不应该查询文件夹
            Page<Files> pageByType = filesService.getFileByNameAndSort(fdto);
            return new ResponseBean(200, "按照名称查询文件并且排序", pageByType);
        }
    }


    /*
       查询某个目录下的指定类型的文件
     */
    @RequiresAuthentication
    @GetMapping("/auth/getFileByType")
    public ResponseBean getFile(FilesDto fdto) {
        Integer startQuery = Optional.ofNullable(fdto.getStart()).orElse(0);
        Integer sizeQuery = Optional.ofNullable(fdto.getSize()).orElse(10);
        Integer gradeQuery = Optional.ofNullable(fdto.getFolderGrade()).orElse(2);
        Integer orderValueQuery = Optional.ofNullable(fdto.getOrderValue()).orElse(0);

        Page<Files> pageByType;
        if (fdto.getIsCreated() == null || fdto.getIsCreated() == false) {
            pageByType = filesService.getPageByType(orderValueQuery, startQuery, sizeQuery, fdto.getType(), null, fdto.getParentFolderId());
        } else {
            // 页面初始化的时候，第一次需要grade
            pageByType = filesService.getPageByType(orderValueQuery, startQuery, sizeQuery, fdto.getType(), gradeQuery, fdto.getParentFolderId());
        }
        return new ResponseBean(200, "成功获取上传的文件", pageByType);
    }

    /*
     根据类型查询所有级别的文件
   */
    @RequiresAuthentication
    @GetMapping("/auth/getAllFileByType")
    public ResponseBean getAllFileByType(FilesDto fdto) {
        Integer startQuery = Optional.ofNullable(fdto.getStart()).orElse(0);
        Integer sizeQuery = Optional.ofNullable(fdto.getSize()).orElse(10);
        Integer orderValueQuery = Optional.ofNullable(fdto.getOrderValue()).orElse(0);

        Page<Files> pageByType;

        if (StringUtils.isEmpty(fdto.getName())) {
            pageByType = filesService.getPageByType(orderValueQuery, startQuery, sizeQuery, fdto.getType(), null, null);
        }else {
            pageByType = filesService.getPageByType(fdto.getName(),orderValueQuery, startQuery, sizeQuery, fdto.getType(), null, null);
        }

        return new ResponseBean(200, "成功获取上传的文件", pageByType);
    }


    @Log
    @PostMapping("/auth/uploadFile")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public ResponseBean uploadFile(MultipartFile file, Integer curentFolderId, HttpServletRequest request) throws ExecutionException, InterruptedException {
        if (file.isEmpty()) {
            return new ResponseBean(400, "上传的文件不能为空！请重新上传", false);
        }
        if (file.getSize() <= 0) {
            return new ResponseBean(400, "上传的文件大小需要大于0kb", false);
        }
//        if (curentFolderId == null) {
//            return new ResponseBean(400, "curentFolderId 不能为空", false);
//        }
        HashMap<String, Object> resultMap = toSaveFiles(file, curentFolderId == null ? filesService.getRootFolder() : filesService.getOneById(curentFolderId));
        if (!(Boolean) resultMap.get("isSuccess")) {
            return new ResponseBean(400, "上传的文件失败", (String) resultMap.get("newFileName"));
        }
        return new ResponseBean(200, "上传的文件成功", (String) resultMap.get("newFileName"));
    }


    @Log
    @PostMapping("/auth/wangEditorupImage")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public wangEditorDto wangEditorupImage(MultipartFile file, HttpServletRequest request) throws ExecutionException, InterruptedException {
        wangEditorDto wangDto = new wangEditorDto();
        if (file.isEmpty() || file.getSize() <= 0) {
            wangDto.setErrno(1);
            return wangDto;
        }
        HashMap<String, Object> resultMap = toSaveFiles(file, filesService.getRootFolder());

        if ((Boolean) resultMap.get("isSuccess")) {
            wangDto.setErrno(0);
            HashMap<String, String> map = new HashMap<>();
            map.put("url", ((Files) resultMap.get("files")).getUrl());
            map.put("alt", ((Files) resultMap.get("files")).getFileName());
            map.put("href", ((Files) resultMap.get("files")).getUrl());
            Map[] result = new Map[1];
            result[0] = map;
//            {
//                url: "图片地址",
//                        alt: "图片文字说明",
//                    href: "跳转链接"
//            },

            wangDto.setData(result);
        } else {
            wangDto.setErrno(1);
        }
        return wangDto;
    }

    @Log
    @PostMapping("/auth/wangEditorupVideo")
    @RequiresAuthentication
    @RequiresRoles("admin")
    public wangEditorDto wangEditorupVideo(MultipartFile file, HttpServletRequest request) throws ExecutionException, InterruptedException {
        wangEditorDto wangDto = new wangEditorDto();
        if (file.isEmpty() || file.getSize() <= 0) {
            wangDto.setErrno(1);
            return wangDto;
        }
        HashMap<String, Object> resultMap = toSaveFiles(file, filesService.getRootFolder());
        if ((Boolean) resultMap.get("isSuccess")) {
            wangDto.setErrno(0);
            HashMap<String, String> result = new HashMap<>();
            result.put("url", ((Files) resultMap.get("files")).getUrl());
            wangDto.setData(result);
        } else {
            wangDto.setErrno(1);
        }
        return wangDto;
    }


    public Files getFilesByUpFile(MultipartFile file, Files parentFiles, String uuidFileName, String newFileName) {
        String userName = ShiroUtil.getUserName();
        Integer userId = ShiroUtil.getUserId();
        System.out.println(file.getContentType());//image/png
        String fileExtension = Objects.requireNonNull(file.getContentType()).split("/")[1].toUpperCase();
        Integer fileType = FileTypeUtil.isFileType(fileExtension);
        String originFileName = file.getOriginalFilename();//获取文件原始的名称
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
        files.setUploadTime(DateUtil.date());
        files.setUrl(serverPath + newFileName);
        files.setType(fileType);
        files.setIsFolder(false);
//        final Integer grade = Integer.valueOf(request.getParameter("grade"));
        files.setFolderGrade(parentFiles.getFolderGrade() + 1);
        files.setParentFolderId(parentFiles.getId());
        return files;

    }


    public HashMap<String, Object> toSaveFiles(MultipartFile file, Files parentFiles) throws ExecutionException, InterruptedException {
        String uuidFileName = UUID.randomUUID().toString();
        String newFileName = uuidFileName + "." + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        Files files = getFilesByUpFile(file, parentFiles, uuidFileName, newFileName);
        //开启异步任务一个是往数据库中插入，一个是往磁盘中写文件
        HashMap<String, Object> map = new HashMap<>();
        Boolean isSuccess = filesService.saveFile(fileRootPath, newFileName, file, files);
        map.put("isSuccess", isSuccess);
        map.put("newFileName", newFileName);
        map.put("files", files);
        return map;
    }
  /*  public void recursion(List<Files> filesList) {
        filesList.stream().forEach(i -> {
            if (i.getIsFolder()) {
                // 文件夹的grade 也要该
                List<Files> childrenByFolderParentId = filesService.getChildrenByFolderParentId(i.getId());
                Files parentFolder = filesService.getOneByParentFolderId(i.getParentFolderId());
                i.setFolderGrade(parentFolder.getFolderGrade() + 1);
                filesService.updateById(i);
                // d递归调用
                recursion(childrenByFolderParentId);
            } else {
                // 修改普通文件grade
                Files parentFolder = filesService.getOneByParentFolderId(i.getParentFolderId());
                i.setFolderGrade(parentFolder.getFolderGrade() + 1);
                filesService.updateById(i);
            }
        });
    }*/
}
