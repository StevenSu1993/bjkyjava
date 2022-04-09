package com.baojikouyu.teach.util;


import com.baojikouyu.teach.pojo.FailMqMessage;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Future;

//一定要交给spring容器管理 因为使用的就是aop的东西
@Component
@Slf4j
public class AsyncTasks {

    @Autowired
    private MailUtil mailUtil;

    @Async
    public Future<Boolean> saveFile(String filePath, String newFileName, MultipartFile file) {
        log.info("save磁盘文件的线程：{}",Thread.currentThread().getName());
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
        log.info("deleterFile异步方法线程:{}", Thread.currentThread().getName());
        File file1 = new File(filePath);
        boolean delete = file1.delete();
        if (!delete) {
            file1.deleteOnExit();//等待别的线程对文件操作完成的时候删除掉文件
        }
    }


    @Async
    public void sendMail(String toName, String titile, FailMqMessage failMqMessage) {
        try {
            log.info("异步发送邮件 ： sendMail");
            mailUtil.sendSimpleMail(toName,titile,new Gson().toJson(failMqMessage));
        } catch (Exception e) {
            log.info("异步发送邮件失败原因：{}", e.getMessage());
            e.printStackTrace();
        }


    }

    @Async
    public void sendMail(String toName, String titile, String failMqMessage) {
        try {
            log.info("异步发送邮件 ： sendMail");
            mailUtil.sendSimpleMail(toName,titile,failMqMessage);
        } catch (Exception e) {
            log.info("异步发送邮件失败原因：{}", e.getMessage());
            e.printStackTrace();
        }


    }


}
