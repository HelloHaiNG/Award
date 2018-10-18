package com.ucar.awards.service;

import com.ucar.awards.AwardsConst;
import com.ucar.awards.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * @author liaohong
 * @since 2018/10/10 10:50
 */
@Service
public class UserService {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JedisService jedisService;

    /**
     * 登陆成功之后修改用户相关信息
     *
     * @param userId
     */
    public void updateUser(String userId, String localDate) {

        //得到用户的基本信息
        List<String> listUserInfo = jedisService.hmget1(AwardsConst.USER + userId, AwardsConst.LASTLOGINDATE, AwardsConst.CONTINUEDAY);
        if (listUserInfo != null && listUserInfo.size() > 0) {
            String lastLoginDate = listUserInfo.get(0);
            String continueDay = listUserInfo.get(1);
            Integer days = Integer.valueOf(continueDay);
            DateUtils dateUtils = new DateUtils();
            LocalDate localDate1 = dateUtils.stringToLocalDate(lastLoginDate);
            LocalDate localDate2 = dateUtils.stringToLocalDate(localDate);
            Integer flag = dateUtils.compareDate(localDate1, localDate2);
            if (flag == 1) {
                //登陆时间是连续的两天时间
                if ((days + 1) < 7) {
                    jedisService.hincrby(AwardsConst.USER + userId, AwardsConst.CONTINUEDAY, Long.valueOf(1));
                    jedisService.hincrby(AwardsConst.USERID_COINS, userId, Long.valueOf(1));
                } else {
                    continueDay = "1";
                    jedisService.hset(AwardsConst.USER + userId, AwardsConst.CONTINUEDAY, continueDay);
                    jedisService.hincrby(AwardsConst.USERID_COINS, userId, Long.valueOf(10));
                }
            } else {
                //登陆时间不是连续的两天时间
                continueDay = "1";
                jedisService.hset(AwardsConst.USER + userId, AwardsConst.CONTINUEDAY, continueDay);
                jedisService.hincrby(AwardsConst.USERID_COINS, userId, Long.valueOf(1));
            }
            jedisService.hset(AwardsConst.USER + userId, AwardsConst.LASTLOGINDATE, localDate2.toString());
        }

    }

    /**
     * 幸运用户
     *
     * @param luckyCode
     * @param pid
     * @return
     *
     */
    public String luckyUser(String luckyCode, String pid) {
        String luckyUser = "";
        List<String> list = jedisService.hmget(AwardsConst.USER_PRIZE + pid, luckyCode);
        if (list != null && list.size() > 0) {
            luckyUser = list.get(0);
            LOGGER.info("幸运用户是： " + luckyUser);
        }
        return luckyUser;
    }

    /**
     * 老用户新增信息
     *
     * @param userId
     * @param localDate
     * @param giftCoins
     */
    public void oldUser(String userId, LocalDate localDate, Integer giftCoins) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap1.put(AwardsConst.LASTLOGINDATE, localDate.toString());
        hashMap1.put(AwardsConst.CONTINUEDAY, "1");
        jedisService.hmset(AwardsConst.USER + userId,hashMap1);
        hashMap2.put(userId, String.valueOf(giftCoins));
        jedisService.hmset(AwardsConst.USERID_COINS, hashMap2);
    }
}
