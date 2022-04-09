package com.baojikouyu.teach.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baojikouyu.teach.pojo.FailMqMessage;
import com.baojikouyu.teach.service.FailMqMessageService;
import com.baojikouyu.teach.mapper.FailMqMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【fail_mq_message】的数据库操作Service实现
* @createDate 2022-04-05 20:33:14
*/
@Service
public class FailMqMessageServiceImpl extends ServiceImpl<FailMqMessageMapper, FailMqMessage>
    implements FailMqMessageService{

}




