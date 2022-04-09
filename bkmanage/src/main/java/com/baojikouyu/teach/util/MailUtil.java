package com.baojikouyu.teach.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class MailUtil {

    @Value("${spring.mail.username}")
    private String from;


    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 这是发送一个文本邮件
     *
     * @param to      发送目标邮箱
     * @param subject 标题
     * @param content 内容
     * @throws Exception
     */

    public void sendSimpleMail(String to, String subject, String content) throws Exception {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(content);
        mail.setFrom(from);
        javaMailSender.send(mail);
    }

    /**
     * 带有附件的邮件
     *
     * @param to       接收地址
     * @param subject  标题
     * @param content  内容
     * @param filePath 文件路径
     * @throws Exception
     */
    public void sendOnlyAttachmentMail(String to, String subject, String content, String filePath) throws Exception {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            //多个附件只是多加一个Attachment的东西
            //helper.addAttachment(fileName+"_test",file);
            helper.addAttachment(fileName, file);
            javaMailSender.send(message);


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



}



