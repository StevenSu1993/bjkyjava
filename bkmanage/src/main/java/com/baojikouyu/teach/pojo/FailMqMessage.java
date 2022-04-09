package com.baojikouyu.teach.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName fail_mq_message
 */
@TableName(value ="fail_mq_message")
@Data
public class FailMqMessage implements Serializable {
    /**
     *
     */
    @TableId
    private Integer id;

    /**
     *
     */
    private String messageId;

    /**
     *
     */
    private String messageBody;

    /**
     *
     */
    private String routingKey;

    /**
     *
     */
    private String exchage;

    /**
     *
     */
    private Integer replyCode;

    /**
     *
     */
    private String replyText;

    private Boolean phase;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
