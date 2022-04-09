package com.baojikouyu.teach.config;

import com.baojikouyu.teach.pojo.FailMqMessage;
import com.baojikouyu.teach.service.FailMqMessageService;
import com.baojikouyu.teach.util.AsyncTasks;
import com.baojikouyu.teach.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableRabbit
@ConfigurationProperties(prefix = "mail.to")
@Slf4j
public class rabbitConfig {

    private String name;

    private String title;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FailMqMessageService failMqMessageService;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private AsyncTasks asyncTasks;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    //队列 起名：TestDirectQueue
    @Bean("myQueue")
    public Queue TestDirectQueue() {
        return new Queue("hello", true, false, false);
    }

    @Bean("myExchange")
    DirectExchange TestDirectExchange() {
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange("helloExchange", true, false);
    }


    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("haha");
    }

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("消息成功投递到rabbitServer: {}", correlationData);
            }
        });

        // 消息没有成功的投递到队列中去的时候才会回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {

            @Override
            public void returnedMessage(Message message, int replyCode,
                                        String replyText, String exchange, String routingKey) {
                log.info("消息未到达rabbitMqserver中去 ： 消息id {} : replyCode : {}, replyText : {}, exchange : {}, routingKey : {}, message: {}", message.getMessageProperties().getMessageId(), replyCode, replyText, exchange, routingKey, message);
                // 存放到mysql 中
                final FailMqMessage failMqMessage = new FailMqMessage();
                failMqMessage.setExchage(exchange);
                failMqMessage.setMessageId(message.getMessageProperties().getMessageId());
                failMqMessage.setPhase(false);
                failMqMessage.setReplyCode(replyCode);
                failMqMessage.setReplyText(replyText);
                failMqMessage.setRoutingKey(routingKey);
                failMqMessage.setMessageBody(new String(message.getBody()));
                failMqMessage.setPhase(false);

                failMqMessageService.save(failMqMessage);
                // 发送邮件给管理员
                asyncTasks.sendMail(name,title, failMqMessage);

            }
        });

    }

}
