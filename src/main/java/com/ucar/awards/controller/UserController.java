package com.ucar.awards.controller;

import com.ucar.awards.AwardsConst;
import com.ucar.awards.mq.SendMQ;
import com.ucar.awards.msg.PostRetEnum;
import com.ucar.awards.service.ConcurrentHashMapService;
import com.ucar.awards.service.JedisService;
import com.ucar.awards.service.QueueService;
import com.ucar.awards.vo.RestFulVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author liaohong
 * @since 2018/10/9 17:16
 */
@RestController
public class UserController {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private JedisService jedisService;

    @Autowired
    private SendMQ mq;

    @Autowired
    private QueueService queueService;

    @Autowired
    private ConcurrentHashMapService hashMapService;

    /**
     * 参与抽奖
     *
     * @param userId
     * @param coins  消费的夺宝币
     * @param pid
     * @return
     */
    @PostMapping("/joinPrize")
    public RestFulVO joinPrize(String userId, String coins, String pid) {
        if (userId == null || coins == null || pid == null) {
            LOGGER.error("joinPrize方法参数不能为空");
            return new RestFulVO(PostRetEnum.PARAM_EXCEPTION);
        }
        boolean isExistUser = jedisService.hexists(AwardsConst.USER + userId, AwardsConst.USERID);
        boolean isExistPrize = jedisService.hexists(AwardsConst.PRIZE + pid, AwardsConst.PID);
        if (isExistPrize && isExistUser) {
            LinkedBlockingQueue queue = hashMapService.get(pid);
            if (queue != null) {
                //夺宝币的判断
                List<String> listUserCoins = jedisService.hmget(AwardsConst.USERID_COINS, userId);
                String personCoins = listUserCoins.get(0);
                Long intCoins = Long.valueOf(coins);
                if (intCoins > Integer.valueOf(personCoins)) {
                    LOGGER.error("夺宝币不足");
                    return new RestFulVO(PostRetEnum.COINS_LESS);
                }

                //个人可选择的最多抽奖机会
                Long coinCounts = jedisService.llen(AwardsConst.PRIZE_CODES + pid);
                Long maxCoins = coinCounts * AwardsConst.MAX_COINS_CODES / 10;
                if (intCoins > maxCoins) {
                    intCoins = maxCoins;
                }

                //入队
                String queueData = userId + "_" + String.valueOf(intCoins) + "_" + pid;
                boolean isFull = queueService.pushQueue(queueData, queue);
                if (!isFull) {
                    String queuePrize = jedisService.get(AwardsConst.QUEUE_PRIZE + pid);
                    if (queuePrize != null) {
                        jedisService.delete(AwardsConst.QUEUE_PRIZE + pid);
                        LOGGER.info("该抽奖人数已达上限，开始分配给用户奖品码...........");
                        mq.sendMsgUserPrizeCodes(pid);
                    }
                    return new RestFulVO(PostRetEnum.PRIZE_FULL);
                }
            } else {
                LOGGER.error("该奖品已经抽奖已经结束...." + pid);
                return new RestFulVO(PostRetEnum.PRIZE_END);
            }
        } else {
            LOGGER.error("用户或者奖品信息redis中不存在");
            return new RestFulVO(PostRetEnum.JOIN_PRIZE_ERROR);
        }
        LOGGER.info("用户参与抽奖成功，等待开奖........." + userId);
        return new RestFulVO(PostRetEnum.SUCCESS);
    }
}
