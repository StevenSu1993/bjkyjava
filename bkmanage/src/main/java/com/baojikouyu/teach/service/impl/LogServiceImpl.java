package com.baojikouyu.teach.service.impl;

import com.baojikouyu.teach.pojo.Log;
import com.baojikouyu.teach.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {


    @Override
    public List<Log> queryAll() {
        return null;
    }

    @Override
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, Log log) {

    }

    @Override
    public Object findByErrDetail(Long id) {
        return null;
    }
}
