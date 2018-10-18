package com.ucar.awards.service;

import com.ucar.awards.AwardsConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
