package com.baojikouyu.teach.controller;

import com.baojikouyu.teach.mapper.CommentMapper;
import com.baojikouyu.teach.pojo.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

@SpringBootTest
public class voiceMsgTest {

    @Autowired
    private CommentMapper messageMapper;

    @Test
    public void test() throws Exception {
//        String filePath = "C:\\upFile\\temp\\0d867fa1-df88-4824-8e07-e7cce257776d.mp3";
        String filePath = "C:\\upFile\\temp\\0f82cb07-5ca0-43dc-b318-9952a0746c55.png";


        File f = new File(filePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        int buf_size = 1024;
        byte[] buffer = new byte[buf_size];
        int len = 0;
        while (-1 != (len = in.read(buffer, 0, buf_size))) {
            bos.write(buffer, 0, len);
        }
        final byte[] bytes = bos.toByteArray();

        Comment voiceMessage = new Comment();

        voiceMessage.setContent(bytes);
        voiceMessage.setUserId(1);
        voiceMessage.setUserName("admin");
        messageMapper.insert(voiceMessage);
    }

}
