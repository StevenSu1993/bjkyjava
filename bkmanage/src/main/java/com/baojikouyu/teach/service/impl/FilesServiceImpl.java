package com.baojikouyu.teach.service.impl;

import com.baojikouyu.teach.mapper.FilesMapper;
import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.pojo.FilesDto;
import com.baojikouyu.teach.service.FilesService;
import com.baojikouyu.teach.util.AsyncTasks;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【files】的数据库操作Service实现
 * @createDate 2022-03-20 01:00:03
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "files", keyGenerator = "keyGenerator")
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
        implements FilesService {

    @Autowired
    private FilesMapper filesMapper;

    @Autowired
    @Lazy
    private AsyncTasks asyncTasks;

    @Override
//    @Cacheable(value = "getPageByType")
    @Cacheable()
    public Page<Files> getPageByType(Integer start, Integer size, Integer type, Integer grade, Integer parentFolderId) {
        LambdaQueryWrapper<Files> queryWrapper = Wrappers.lambdaQuery();
        Page<Files> page = new Page<>();
        page.setCurrent(start);
        page.setSize(size);
     /*   if (type != 0) {
            //判断是图片还是视频，还是文件
            queryWrapper.eq(Files::getType, type);
        }*/
        queryWrapper.eq(type != 0, Files::getType, type);
        queryWrapper.eq(grade != null, Files::getFolderGrade, grade);
        queryWrapper.eq(parentFolderId != null, Files::getParentFolderId, parentFolderId);
        queryWrapper.orderByDesc(Files::getType, Files::getFolderGrade, Files::getUploadTime);
        return filesMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
//    @CacheEvict(value = {"getPageByType", "getFileByNameAndSort"},
//            allEntries = true)
    @CacheEvict(cacheNames = "files",
            allEntries = true)
    public Boolean saveFile(String filePath, String newFileName, MultipartFile file, Files files) {
        log.info("saveFile方法线程:{}", Thread.currentThread().getName());
        //异步的去存文件，比同步执行提升的速度就是往数据库中存数据的时间。
        final Future<Boolean> future = asyncTasks.saveFile(filePath, newFileName, file);
        int insert = 0;
        try {
            insert = filesMapper.insert(files);
        } catch (Exception e) {
            asyncTasks.deleteFile(filePath + newFileName);
            //这里是手动回滚了不需要在抛一个RuntimeException了 , 因为spring管理的事务只能捕获到RuntimeException
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
        //插入数据库成功
        if (insert > 0) {
            Boolean isSucessed = null;
            try {
                isSucessed = future.get();
                //磁盘写入失败
                if (!isSucessed) {
                    asyncTasks.deleteFile(filePath + newFileName);
                    //这里是手动回滚了不需要在抛一个RuntimeException了 , 因为spring管理的事务只能捕获到RuntimeException
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } catch (Exception e) {
                asyncTasks.deleteFile(filePath + newFileName);
                //这里是手动回滚了不需要在抛一个RuntimeException了 , 因为spring管理的事务只能捕获到RuntimeException
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            //没插入成功的情况,删除掉文件
            asyncTasks.deleteFile(filePath + newFileName);
            return false;
        }
    }

    @Override
    @Cacheable
    public Files getOneByParentFolderId(Integer parentFolderId) {
        return filesMapper.selectOne(Wrappers.lambdaQuery(Files.class).eq(parentFolderId != null, Files::getId, parentFolderId));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "files",
            allEntries = true)
//    @CacheEvict(value = {"getPageByType", "getFileByNameAndSort"},
//            allEntries = true)
    public Boolean deleteFilesByIds(Integer[] ids) {
        int i = 0;
        List<Integer> idList = Arrays.stream(ids).filter(id -> id != null).collect(Collectors.toList());
        List<Files> files;
        try {
            files = filesMapper.selectBatchIds(idList);
            i = filesMapper.deleteBatchIds(Arrays.asList(ids));
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
        files.forEach(files1 -> {
            asyncTasks.deleteFolder(files1);
        });
        return i > 0;
    }

    @Override
    @Cacheable
//    @Cacheable(value = "getFileByNameAndSort")
    public Page<Files> getFileByNameAndSort(FilesDto fdto) {
        Page<Files> page = new Page<>(fdto.getStart() == 0 ? 0 : fdto.getStart(), fdto.getSize() == 0 ? 10 : fdto.getSize());
        LambdaQueryWrapper<Files> queryWrapper = Wrappers.lambdaQuery(Files.class);
        if (!StringUtils.isBlank(fdto.getName())) {
            queryWrapper.like(Files::getFileName, fdto.getName());
        }
        if (fdto.getType() != null && fdto.getType() != 0) {
            queryWrapper.eq(Files::getType, fdto.getType());
        }
        if (fdto.getRang() == 0) {
            //0 仅自己创建的
            queryWrapper.eq(Files::getCreaterName, fdto.getCreater());
        }
        if (fdto.getParentFolderId() != null) {
            queryWrapper.eq(Files::getParentFolderId, fdto.getParentFolderId());
        }
        if (fdto.getFolderGrade() != null) {
            queryWrapper.eq(Files::getFolderGrade, fdto.getFolderGrade());
        }
        // 排除文件夹
        if (fdto.getExcludeFolder() != null && fdto.getExcludeFolder() == true) {
            queryWrapper.eq(Files::getIsFolder, false);
        }
        queryWrapper.orderByDesc(Files::getType);
        if (fdto.getOrderValue() == 0) {
            //按照名称排序
            queryWrapper.orderByAsc(Files::getFileName);
        } else {
            //按照时间排序
            queryWrapper.orderByAsc(Files::getUploadTime);
        }

        return filesMapper.selectPage(page, queryWrapper);
    }

    @Override
    @CacheEvict(cacheNames = "files",
            allEntries = true)
    public Boolean saveFolder(Files files) {
        int insert = filesMapper.insert(files);
        if (insert > 0) {
            return true;
        } else return false;
    }

    @Override
    @Cacheable
    public List<Files> getAllFolder() {
        LambdaQueryWrapper<Files> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Files::getIsFolder, true);
        return getTreeFolder(filesMapper.selectList(queryWrapper));
    }

    @Override
    @Cacheable
    public Boolean getFolderByParentFolderIdAndFileName(boolean b, Integer parentFolderId, String floderName) {
        LambdaQueryWrapper<Files> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Files::getIsFolder, true);
        queryWrapper.eq(Files::getParentFolderId, parentFolderId);
        queryWrapper.eq(Files::getFileName, floderName);

        return filesMapper.exists(queryWrapper);

    }

    @Override
    @CacheEvict(cacheNames = "files",
            allEntries = true)
    @Transactional
    public Boolean updateByIds(Integer[] ids, Integer to, Integer grade) {
        LambdaUpdateWrapper<Files> updateWrapper = Wrappers.lambdaUpdate(Files.class);
        updateWrapper.in(ids.length > 0, Files::getId, ids);
        updateWrapper.set(to != 0, Files::getParentFolderId, to);
        updateWrapper.set(grade != null, Files::getFolderGrade, grade);
        int update = filesMapper.update(null, updateWrapper);
        if (update > 0) {
            // 按道理来说应该是放到rabbitMq 中去。
            List<Files> all = getBatchById(ids);
            final List<Files> allFolder = all.stream().filter(f -> f.getIsFolder()).collect(Collectors.toList());
            recursion(allFolder);
            return true;
        } else return false;
    }

    @Override
    @Cacheable
    public Files getOneById(Integer to) {
        return filesMapper.selectOne(Wrappers.lambdaQuery(Files.class).eq(to != null, Files::getId, to));
    }

    @Override
    @Cacheable
    public List<Files> getBatchById(Integer[] ids) {
        return filesMapper.selectBatchIds(Arrays.asList(ids));
    }

    @Override
    @Cacheable
    public List<Files> getChildrenByFolderParentId(Integer id) {
        return filesMapper.selectList(Wrappers.lambdaQuery(Files.class).eq(id != null, Files::getParentFolderId, id));
    }

    public List<Files> getTreeFolder(List<Files> filesList) {
        ArrayList<Files> finalFiles = Lists.newArrayList();
        for (int i = 0; i < filesList.size(); i++) {

            Files files = filesList.get(i);

            for (int j = 0; j < filesList.size(); j++) {
                Files tmp = filesList.get(j);
                if (tmp.getParentFolderId() == files.getId()) {
                    files.getChildrenFolder().add(tmp);
                }
            }
            // 提取出父节点
            if (files.getParentFolderId() == null || files.getParentFolderId() == 0) {
                finalFiles.add(files);
            }
        }
        return finalFiles;
    }


    public void recursion(List<Files> filesList) {
        filesList.stream().forEach(i -> {
            if (i.getIsFolder()) {
                // 文件夹的grade 也要该
                List<Files> childrenByFolderParentId = getChildrenByFolderParentId(i.getId());
                Files parentFolder = getOneByParentFolderId(i.getParentFolderId());
                i.setFolderGrade(parentFolder.getFolderGrade() + 1);
                updateById(i);
                // d递归调用
                recursion(childrenByFolderParentId);
            } else {
                // 修改普通文件grade
                Files parentFolder = getOneByParentFolderId(i.getParentFolderId());
                i.setFolderGrade(parentFolder.getFolderGrade() + 1);
                updateById(i);
            }
        });
    }

}




