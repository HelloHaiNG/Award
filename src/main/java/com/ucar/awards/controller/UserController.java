package com.ucar.awards.controller;

import com.ucar.awards.AwardsConst;
import com.ucar.awards.mq.SendMQ;
import com.ucar.awards.msg.PostRetEnum;
import com.ucar.awards.service.JedisService;
import com.ucar.awards.service.QueueService;
import com.ucar.awards.vo.RestFulVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

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

            String queueData = userId + "_" + String.valueOf(intCoins) + "_" + pid;
            boolean isFull = queueService.pushQueue(queueData);
            if (!isFull) {
                jedisService.setnx(pid, pid);
                return new RestFulVO(PostRetEnum.PRIZE_FULL);
            }
        } else {
            LOGGER.error("用户或者奖品信息不存在");
            return new RestFulVO(PostRetEnum.JOIN_PRIZE_ERROR);
        }
        return new RestFulVO(PostRetEnum.SUCCESS);
    }
}
