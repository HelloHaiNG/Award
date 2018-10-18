package com.ucar.awards.entities;

import java.util.Date;

/**
 * @author liaohong
 * @since 2018/10/9 10:24
 */
public class User {

    private String userId;
    private Date registerDate;
    private Date lastLoginDate;
    private String continueDay;

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getContinueDay() {
        return continueDay;
    }

    public void setContinueDay(String continueDay) {
        this.continueDay = continueDay;
    }
}
