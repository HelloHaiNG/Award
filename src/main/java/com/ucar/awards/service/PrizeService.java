package com.ucar.awards.service;

import com.ucar.awards.AwardsConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author liaohong
 * @since 2018/10/10 10:46
 */
@Service
public class PrizeService {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PrizeService.class);

    @Autowired
    private JedisService jedisService;

    /**
     * 奖品码分配给用户
     * @param pid
     * @param userId
     * @param coins
     * @return
     */
    public boolean prizeAndUserAndCodes(String pid, String userId, String coins) {
        Long size = jedisService.llen(AwardsConst.PRIZE_CODES_TEMP + pid);
        if (size > 0) {
            //更新参与夺宝的人数
            jedisService.incr(AwardsConst.JOIN_PRIZE_COUNT + pid);

            //更新用户夺宝币
            Long coinNumber = Long.valueOf(coins);
            if (coinNumber > size) {
                coinNumber = size;
            }
            Long decreaseCoins = 0 - coinNumber;
            jedisService.hincrby(AwardsConst.USERID_COINS, userId, decreaseCoins);

            //给用户分配奖品码
            HashMap<String, String> hashMap = new HashMap<String, String>();
            for (int i = 0; i < coinNumber; i++) {
                String popCode = jedisService.lpop(AwardsConst.PRIZE_CODES_TEMP + pid);
                hashMap.put(popCode, userId);
            }
            jedisService.hmset(AwardsConst.USER_PRIZE + pid, hashMap);
        } else {
            return false;
        }
        return true;
    }

    /**
     * 夺宝码生成策略
     *
     * @param pid
     * @return
     */
    public String luckyCode(String pid) {
        Long codeNumber = jedisService.llen(AwardsConst.PRIZE_CODES + pid);
        Random random = new Random();
        String luckyCode = String.valueOf(random.nextInt(codeNumber.intValue()) + 1);
        LOGGER.info("该奖品的幸运码是：" + luckyCode);
        return luckyCode;
    }
}
