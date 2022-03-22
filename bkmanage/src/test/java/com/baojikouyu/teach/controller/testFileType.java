package com.baojikouyu.teach.controller;



import cn.hutool.core.io.FileTypeUtil;

import java.io.File;

public class testFileType {


    public static void main(String[] args) {


        File file1 = new File("testFile.txt");


//        final Integer jpg = FileTypeUtil.isFileType("PNG");

        final String png = FileTypeUtil.getType(file1);
        System.out.println( png);


    }
}
