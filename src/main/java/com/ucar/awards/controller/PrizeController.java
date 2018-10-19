package com.ucar.awards.controller;

import com.ucar.awards.AwardsConst;
import com.ucar.awards.msg.PostRetEnum;
import com.ucar.awards.service.ConcurrentHashMapService;
import com.ucar.awards.service.JedisService;
import com.ucar.awards.service.QueueService;
import com.ucar.awards.vo.RestFulVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author liaohong
 * @since 2018/10/9 14:29
 */
@RestController
public class PrizeController {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PrizeController.class);

    @Autowired
    private JedisService jedisService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private ConcurrentHashMapService hashMapService;

    /**
     * 添加奖品
     *
     * @param pid
     * @param pname
     * @param codeNumber 奖品码的数量
     * @return
     */
    @PostMapping("/addPrize")
    public RestFulVO addPrize(String pid, String pname, @RequestParam(defaultValue = "10") String codeNumber) {
        if (pid == null || StringUtils.isNotBlank(pid) == false || pname == null || StringUtils.isNotBlank(pname) == false) {
            LOGGER.error("添加奖品信息不能为空");
            return new RestFulVO(PostRetEnum.PARAM_EXCEPTION);
        }
        Integer codeNumbers = Integer.valueOf(codeNumber);
        if (codeNumbers % 10 == 0) {
            boolean flag = jedisService.hexists(AwardsConst.PRIZE + pid, AwardsConst.PID);
            if (!flag) {
                //初始化队列长度
                LinkedBlockingQueue queue = queueService.initQueue(codeNumbers);
                hashMapService.put(pid,queue);
                jedisService.setnx(AwardsConst.QUEUE_PRIZE + pid, pid);
                jedisService.addPrize(pid, pname, codeNumber);
                jedisService.prizeCodes(pid, codeNumbers);
                jedisService.set(AwardsConst.JOIN_PRIZE_COUNT + pid, "0");
                LOGGER.info("奖品添加成功......." + pid + "    " + pname);
                return new RestFulVO(PostRetEnum.SUCCESS);
            } else {
                LOGGER.error("该商品id已经存在");
            }
        } else {
            LOGGER.error("奖品码设置的数量必须为10的倍数");
        }
        return new RestFulVO(PostRetEnum.ADD_PRIZE_ERROR);
    }

}
