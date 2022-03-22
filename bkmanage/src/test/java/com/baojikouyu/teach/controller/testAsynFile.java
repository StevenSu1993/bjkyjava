package com.baojikouyu.teach.controller;

import com.sun.jna.platform.win32.WinBase;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class testAsynFile {

    public static void main(String[] args) throws Exception {

        //首先测试多线程模式下能不能同时操作一个文件

        File file1 = new File("testFile.txt");

        System.out.println(file1.getAbsolutePath());
    /*    new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("开始删除文件");
                final boolean delete = file1.delete();
                System.out.println("是否删除成功" + delete); //是否删除成功false 当另一个线程在写文件的时候这个线程是删除不成功的
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"thread1").start();*/
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
                Set<Thread> threads = Thread.getAllStackTraces().keySet();

//                final boolean delete = file1.delete();
                file1.deleteOnExit(); //会在另一个线程对该文件操作完成的时候删除掉

//                threads.forEach(t ->{
//                   if (t.getName().equals("main")){
//                       t.interrupt();
//                   }
//                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"thread2").start();

        String content = "test\n";
        FileOutputStream fileOutputStream = null;

        fileOutputStream = new FileOutputStream(file1);

        boolean flag = true;
        int i = 0;
        while(flag){
            String s = String.valueOf(i);
            fileOutputStream.write((s + content).getBytes());
            System.out.println(s + content);
            System.out.println("主线程的名字为" + Thread.currentThread().getName());
            final boolean interrupted = Thread.currentThread().isInterrupted();
            if (interrupted) {
                System.out.println("检测到线程被终止了");
                break;
            }
            i++;
            TimeUnit.SECONDS.sleep(1);
            if (i == 10)
                flag = false;
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        System.out.println("系统阻塞在这不让主线程退出");
        System.in.read();

    }

}
