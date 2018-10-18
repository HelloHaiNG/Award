package com.ucar.awards.msg;

import org.apache.commons.lang3.StringUtils;

/**
 * ClassName PostRetEnum.java
 * Description 返回信息枚举类
 **/
public enum PostRetEnum {

    SUCCESS("0000", "操作成功"),

    PARAM_EXCEPTION("1000", "参数异常"),

    LOGIN_ERROR("1001", "登陆失败"),

    ADD_PRIZE_ERROR("1002","添加奖品信息失败"),

    JOIN_PRIZE_ERROR("1003","参与抽奖失败"),

    COINS_LESS("1004","夺宝币不足"),

    ERROR("1111", "操作失败");

    private String code;
    private String msg;

    PostRetEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(String code) {
        for (PostRetEnum systemException : PostRetEnum.values()) {
            if (StringUtils.equals(systemException.getCode(), code)) {
                return systemException.getMsg();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
