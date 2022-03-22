package com.baojikouyu.teach.exception;

import com.baojikouyu.teach.annotation.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Log4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    @Log(desc = "异步任务失败")
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        StringBuilder paramStr = new StringBuilder();
        for (Object param : params) {
            paramStr.append(param.toString() + "---");
            System.out.println("Parameter value - " + param);
        }
        log.error(method.getName() + " ==>" + " 方法参数 : "+ paramStr.toString() + "异常信息 : " + ex.getMessage());
    }
}
