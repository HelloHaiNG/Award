package com.ucar.awards.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

/**
 * @author liaohong
 * @since 2018/10/9 11:05
 */
public class DateUtils {

    /**
     * 字符串转日期
     *
     * @param string
     * @return
     */
    public LocalDate stringToLocalDate(String string) {
        Integer year = Integer.valueOf(StringUtils.substring(string, 0, 4));
        Integer month = Integer.valueOf(StringUtils.substring(string, 5, 7));
        Integer day = Integer.valueOf(StringUtils.substring(string, 8));
        LocalDate localDate = LocalDate.of(year, month, day);
        return localDate;
    }

    /**
     * 比较日期相隔天数
     *
     * @param localDate1
     * @param localDate2
     * @return
     */
    public Integer compareDate(LocalDate localDate1, LocalDate localDate2) {
        Period period = Period.between(localDate1, localDate2);
        return period.getDays();
    }

    /**
     * 补偿老用户夺宝币策略
     * @param dates
     * @return
     */
    public Integer giftConis(Integer dates) {
        if (dates >= 365) {
            return 10;
        } else if (dates >= 180) {
            return 5;
        } else if (dates >= 90) {
            return 3;
        } else {
            return 1;
        }
    }

    public String formatTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        return format.format(date);
    }

}
