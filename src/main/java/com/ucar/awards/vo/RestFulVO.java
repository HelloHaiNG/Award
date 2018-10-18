package com.ucar.awards.vo;

import com.ucar.awards.msg.PostRetEnum;

public class RestFulVO<T> {

    /**
     * 返回编号
     */
    private String restCode;

    /**
     * 返回消息
     */
    private String restMsg;

    /**
     * 数据返回
     */
    private T data;

    public RestFulVO() {
        super();
    }

    public RestFulVO(String restCode, String restMsg) {
        this.restCode = restCode;
        this.restMsg = restMsg;
    }

    public RestFulVO(String restCode, String restMsg, T data) {
        this.restCode = restCode;
        this.restMsg = restMsg;
        this.data = data;
    }

    public RestFulVO(PostRetEnum postResultEnum, T data) {
        this.restCode = postResultEnum.getCode();
        this.restMsg = postResultEnum.getMsg();
        this.data = data;
    }

    public RestFulVO(PostRetEnum postResultEnum) {
        this.restCode = postResultEnum.getCode();
        this.restMsg = postResultEnum.getMsg();
    }

    public String getRestCode() {
        return restCode;
    }

    public void setRestCode(String restCode) {
        this.restCode = restCode;
    }

    public String getRestMsg() {
        return restMsg;
    }

    public void setRestMsg(String restMsg) {
        this.restMsg = restMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RestFulVO{");
        sb.append("restCode='").append(restCode).append('\'');
        sb.append(", restMsg='").append(restMsg).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}

