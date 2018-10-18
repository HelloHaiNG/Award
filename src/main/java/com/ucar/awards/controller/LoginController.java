package com.ucar.awards.controller;

import com.ucar.awards.AwardsConst;
import com.ucar.awards.base.BaseRest;
import com.ucar.awards.mq.SendMQ;
import com.ucar.awards.msg.PostRetEnum;
import com.ucar.awards.service.JedisService;
import com.ucar.awards.service.UserService;
import com.ucar.awards.utils.DateUtils;
import com.ucar.awards.vo.RestFulVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

/**
 * @author liaohong
 * @since 2018/10/9 9:40
 */

@RestController
public class LoginController extends BaseRest {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private JedisService jedisService;

    @Autowired
    private SendMQ mq;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public RestFulVO login(String userId) {
        if (userId != null || StringUtils.isNotBlank(userId)) {
            boolean flag = jedisService.hexists(AwardsConst.USER + userId, AwardsConst.USERID);
            if (flag) {
                boolean isOlder = jedisService.hexists(AwardsConst.USER + userId, AwardsConst.CONTINUEDAY);
                if (!isOlder) {
                    //老用户
                    List<String> list = jedisService.hmget(AwardsConst.USER + userId,AwardsConst.REGISTERDATE);
                    DateUtils dateUtils = new DateUtils();
                    LocalDate now = LocalDate.now();
                    LocalDate registerDate = dateUtils.stringToLocalDate(list.get(0));
                    Integer dates = dateUtils.compareDate(registerDate,now);
                    Integer giftCoins = dateUtils.giftConis(dates);
                    userService.oldUser(userId,now,giftCoins);
                } else {
                    //新用户
                    LocalDate localDate = LocalDate.now();
                    String setnxKey = userId + ":" + localDate.toString();
                    String setnxValue = jedisService.get(setnxKey);
                    //只有每天第一次登陆发送MQ消息
                    if (setnxValue == null) {
                        jedisService.setnx(setnxKey, setnxKey);
                        //发送mq消息
                        mq.sendMsgLogin(userId, localDate);
                    }
                }
                return new RestFulVO(PostRetEnum.SUCCESS);
            } else {
                LOGGER.info("该用户不存在");
            }
        } else {
            LOGGER.error("登陆参数userId不能为空");
            return new RestFulVO(PostRetEnum.PARAM_EXCEPTION);
        }
        return new RestFulVO(PostRetEnum.LOGIN_ERROR);
    }
}
