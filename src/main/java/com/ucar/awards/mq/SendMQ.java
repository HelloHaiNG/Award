package com.ucar.awards.mq;

import com.ucar.awards.AwardsConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author liaohong
 * @since 2018/10/9 10:12
 */
@Service
public class SendMQ {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMQ.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMsgLogin(String userId, LocalDate localDate) {
        String message = userId + "_" + localDate.toString();
        LOGGER.info("发送的mq消息是：" + message);
        rabbitTemplate.convertAndSend(AwardsConst.EXCHANGE, AwardsConst.QUEUE1, message);
    }

    public void sendMsgUserPrizeCodes(String pid) {
        LOGGER.info("发送的mq消息是:  抽奖人数已达上限，开始分配给用户奖品码：" + pid);
        rabbitTemplate.convertAndSend(AwardsConst.EXCHANGE, AwardsConst.QUEUE2, pid);
    }
}
