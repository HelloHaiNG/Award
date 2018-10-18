package com.ucar.awards.controller;

import com.ucar.awards.AwardsConst;
import com.ucar.awards.mq.SendMQ;
import com.ucar.awards.msg.PostRetEnum;
import com.ucar.awards.service.JedisService;
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

    /**
     * 参与抽奖
     * @param userId
     * @param coins   消费的夺宝币
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
            Long coinCounts = jedisService.llen(AwardsConst.PRIZE_CODES + pid);
            Long coinRemainCounts = jedisService.llen(AwardsConst.PRIZE_CODES_TEMP + pid);
            //个人可选择的最多抽奖机会
            double maxCoins = coinCounts * AwardsConst.MAX_COINS_CODES;
            if (intCoins > maxCoins) {
                LOGGER.error("选择的夺宝机会过多，请重新输入:   " + intCoins);
                return new RestFulVO(PostRetEnum.JOIN_PRIZE_ERROR);
            }
            if (intCoins > coinRemainCounts) {
                intCoins = coinRemainCounts;
            }
            HashMap<String, String> hashMap = new HashMap<String, String>();
            for (int i = 0; i < intCoins; i++) {
                //移除奖品夺宝码
                String popCode = jedisService.lpop(AwardsConst.PRIZE_CODES_TEMP + pid);
                hashMap.put(popCode, userId);
            }
            //更新参与夺宝的人数
            jedisService.incr(AwardsConst.JOIN_PRIZE_COUNT + pid);

            //分配奖品码之后的用户和奖品码信息
            jedisService.hmset(AwardsConst.USER_PRIZE + pid, hashMap);

            Long afterCoins = 0 - intCoins;
            //更新用户夺宝币
            jedisService.hincrby(AwardsConst.USERID_COINS, userId, afterCoins);

            Long size = jedisService.llen(AwardsConst.PRIZE_CODES_TEMP + pid);
            LOGGER.info("参与抽奖成功，请等开奖........");
            if (size == 0) {
                LOGGER.info("奖品码分配完毕，马上开奖...........");
                mq.sendMsgPrize(pid);
            }
        } else {
            LOGGER.error("用户或者奖品信息不存在");
            return new RestFulVO(PostRetEnum.JOIN_PRIZE_ERROR);
        }
        return new RestFulVO(PostRetEnum.SUCCESS);
    }
}
