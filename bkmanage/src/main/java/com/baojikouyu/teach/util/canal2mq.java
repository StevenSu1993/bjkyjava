package com.baojikouyu.teach.util;


import com.baojikouyu.teach.pojo.FailMqMessage;
import com.baojikouyu.teach.service.FailMqMessageService;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class canal2mq {

    @Value("${mail.to.name}")
    private String toName;

    @Autowired
    private RedisCacheManager cacheManager;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    FailMqMessageService failMqMessageService;

    @Autowired
    private AsyncTasks asyncTasks;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "bjmsgQueue", durable = "true", exclusive = "false"),//声明临时队列
                    exchange = @Exchange(value = "bjmsg", type = "topic"),
                    key = {"canal.bjky"}
            )
    })
    public void receive4(Message message, Channel channel) throws Exception {
        try {
            //TODO 根据message消息去更新缓存
            String jsonStr = new String(message.getBody());
            final Gson gson = new Gson();
            final HashMap hashMap = gson.fromJson(jsonStr, HashMap.class);
            final String table = (String) hashMap.get("table");
            final Cache cache = cacheManager.getCache(table);
            if (cache != null) {
                // 我这里操作比较简单。拿到更新后的值就直接给删除了。
                cache.clear();
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                log.info("消息已经重复处理失败，请联系管理员");

                //  把消息存放到数据库方
                final FailMqMessage failMqMessage = new FailMqMessage();
                failMqMessage.setExchage(message.getMessageProperties().getReceivedExchange());
                failMqMessage.setMessageId(message.getMessageProperties().getMessageId());
                failMqMessage.setPhase(false);
                failMqMessage.setReplyCode(message.getMessageProperties().getReceivedDelay());
                failMqMessage.setReplyText(message.getMessageProperties().getReplyTo());
                failMqMessage.setRoutingKey(message.getMessageProperties().getReceivedRoutingKey());
                failMqMessage.setMessageBody(new String(message.getBody()));
                failMqMessage.setPhase(true);
                failMqMessageService.save(failMqMessage);

                asyncTasks.sendMail(toName, "canal消息更新失败，请手动更新", new Gson().toJson(message));
                // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                // 有可能是在应答消息的时候出现了网络异常。
                log.info("第一次处理消息时发生了异常：{}", e.getMessage());
                // requeue为是否重新回到队列，true重新入队 ,fasle 不重新入列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }


    }


}
