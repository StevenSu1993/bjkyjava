package com.baojikouyu.teach.service.impl;

import com.baojikouyu.teach.pojo.Course;
import com.baojikouyu.teach.pojo.FilesDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.service.FilesService;
import com.baojikouyu.teach.mapper.FilesMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Administrator
 * @description 针对表【files】的数据库操作Service实现
 * @createDate 2022-03-20 01:00:03
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
        implements FilesService {

    @Autowired
    private FilesMapper filesMapper;

    @Override
    @Cacheable(value = "getPageByType")
    public Page<Files> getPageByType(Integer start, Integer size, Integer type) {
        LambdaQueryWrapper<Files> queryWrapper = Wrappers.lambdaQuery();
        Page<Files> page = new Page<>();
        page.setCurrent(start);
        page.setSize(size);
        if (type != 0) {
            //判断是图片还是视频，还是文件
            queryWrapper.eq(Files::getType, type);
        }
        return filesMapper.selectPage(page, queryWrapper);
    }


    @Async
    @Override
    public Future<Boolean> saveFile(String filePath, String newFileName, MultipartFile file) {
        File file1 = new File(filePath);

        if (!file1.exists()) {
            file1.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath + newFileName);
            fileOutputStream.write(file.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return new AsyncResult<>(true);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return new AsyncResult<>(false);
        }
    }

    @Async
    public void deleteFile(String filePath) {
        File file1 = new File(filePath);
        boolean delete = file1.delete();
        if (!delete) {
            file1.deleteOnExit();//等待别的线程对文件操作完成的时候删除掉文件
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"getPageByType", "getFileByNameAndSort"},
            allEntries = true)
    public Boolean saveFile(String filePath, String newFileName, MultipartFile file, Files files) {
        //异步的去存文件，比同步执行提升的速度就是往数据库中存数据的时间。
        final Future<Boolean> future = saveFile(filePath, newFileName, file);
        int insert = 0;
        try {
            insert = filesMapper.insert(files);
        } catch (Exception e) {
            deleteFile(filePath + newFileName);
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
                    deleteFile(filePath + newFileName);
                    //这里是手动回滚了不需要在抛一个RuntimeException了 , 因为spring管理的事务只能捕获到RuntimeException
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            } catch (Exception e) {
                deleteFile(filePath + newFileName);
                //这里是手动回滚了不需要在抛一个RuntimeException了 , 因为spring管理的事务只能捕获到RuntimeException
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            //没插入成功的情况,删除掉文件
            deleteFile(filePath + newFileName);
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"getPageByType", "getFileByNameAndSort"},
            allEntries = true)
    public Boolean deleteFilesByIds(Integer[] ids) {
        int i = 0;
        List<Files> files;
        try {
            files = filesMapper.selectBatchIds(Arrays.asList(ids));
            i = filesMapper.deleteBatchIds(Arrays.asList(ids));
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
        files.forEach(files1 -> {
            deleteFile(files1.getFilePath() + files1.getUuidFileName());
        });
        return i > 0;
    }

    @Override
    @Cacheable(value = "getFileByNameAndSort")
    public Page<Files> getFileByNameAndSort(FilesDto fdto) {
        Page<Files> page = new Page<>(fdto.getStart() == 0 ? 0 : fdto.getStart(), fdto.getSize() == 0 ? 10 : fdto.getSize());
        LambdaQueryWrapper<Files> queryWrapper = Wrappers.lambdaQuery(Files.class);
        if (!StringUtils.isBlank(fdto.getName())) {
            queryWrapper.like(Files::getFileName, fdto.getName());
        }
        if (fdto.getType() != 0) {
            queryWrapper.eq(Files::getType, fdto.getType());
        }
        if (fdto.getRang() == 0) {
            //0 仅自己创建的
            queryWrapper.eq(Files::getCreaterName, fdto.getCreater());
        }
        if (fdto.getOrderValue() == 0) {
            //按照名称排序
            queryWrapper.orderByAsc(Files::getFileName);
        } else {
            //按照时间排序
            queryWrapper.orderByAsc(Files::getUploadTime);
        }
        return filesMapper.selectPage(page, queryWrapper);
    }
}




