package com.baojikouyu.teach.mq;


import com.baojikouyu.teach.constant.rabbitMqConstant;
import com.baojikouyu.teach.pojo.Files;
import com.baojikouyu.teach.service.FilesService;
import com.baojikouyu.teach.util.MailUtil;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class floderMqConsumer {

    @Autowired
    private FilesService filesService;

    @Value("${mail.to.name}")
    private String toName;

    @Autowired
    private MailUtil mailUtil;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,//声明临时队列
                    exchange = @Exchange(value = rabbitMqConstant.FOLDEREXCHANGE, type = "fanout")
            )
    })
    public void updateGrade(List<Files> allFolder, Message message, Channel channel) throws Exception {
        try {
            log.info("更改某些文件夹下的所有的文件的grade", allFolder);
            recursion(allFolder);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("批量更新失败 手动处理");
            // 存放数据库
            mailUtil.sendSimpleMail(toName, "递归更改文件的grade失败", new Gson().toJson(allFolder));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }


    public void recursion(List<Files> filesList) {
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
    }


    public void recursion(Files files) {

        if (files.getIsFolder()) {
            List<Files> list = filesService.getChildrenByFolderParentId(files.getId());
            list.stream().forEach(
                    i -> {
                        recursion(i);
                    }
            );
        } else {
            Files parentFolder = filesService.getOneByParentFolderId(files.getParentFolderId());
            files.setFolderGrade(parentFolder.getFolderGrade() + 1);
            filesService.updateById(files);

        }

    }


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,//声明临时队列
                    exchange = @Exchange(value = rabbitMqConstant.FOLDEREXCHANGE, type = "fanout")
            )
    })
    public void receivel(Files files, Message message, Channel channel) throws IOException {

        try {
            log.info("新增文件夹操作" + "files");
            Boolean isSucess = filesService.saveFolder(files);

            if (isSucess) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                log.error("消息队列中也插入失败了。 请程序员检查检查");

                // TODO 发送邮件


                // 如果失败了的情况下。
                // 还需要设置一个邮件报警功能
            }

        } catch (IOException e) {
            if (message.getMessageProperties().getRedelivered()) {
                log.info("消息已重复处理失败,拒绝再次接收：{} ", files);
                // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println("消息即将再次返回队列处理");
                // requeue为是否重新回到队列，true重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            e.printStackTrace();
        }
    }

}
