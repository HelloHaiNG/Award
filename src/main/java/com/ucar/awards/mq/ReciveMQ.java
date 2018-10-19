package com.ucar.awards.mq;

import com.ucar.awards.AwardsConst;
import com.ucar.awards.service.ConcurrentHashMapService;
import com.ucar.awards.service.JedisService;
import com.ucar.awards.service.PrizeService;
import com.ucar.awards.service.QueueService;
import com.ucar.awards.service.UserService;
import com.ucar.awards.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author liaohong
 * @since 2018/10/9 10:18
 */
@Service
public class ReciveMQ {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReciveMQ.class);

    @Autowired
    private PrizeService prizeService;

    @Autowired
    private UserService userService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private ConcurrentHashMapService hashMapService;

    @RabbitListener(queues = AwardsConst.QUEUE1)
    public void receviceLogin(String message) {
        LOGGER.info("登陆成功收到消息：" + message);
        String userId = StringUtils.substringBefore(message, "_");
        String localDate = StringUtils.substringAfter(message, "_");
        userService.updateUser(userId, localDate);
    }

    @RabbitListener(queues = AwardsConst.QUEUE2)
    public void receviceUserPrizeCode(String message) {
        LOGGER.info("开奖收到的消息是：   " + message);
        LinkedBlockingQueue queue = hashMapService.get(message);
        hashMapService.remove(message);
        List<String> list = queueService.popQueue(queue);
        if (list != null && list.size() > 0) {
            for (String string : list) {
                String userId = StringUtils.substringBefore(string, "_");
                String coins = StringUtils.substringBetween(string, "_", "_");
                boolean flag = prizeService.prizeAndUserAndCodes(message, userId, coins);
                if (!flag) {
                    break;
                }
            }
            //生成幸运码
            String luckyCode = prizeService.luckyCode(message);

            //幸运用户
            userService.luckyUser(luckyCode,message);
        }
    }

}
