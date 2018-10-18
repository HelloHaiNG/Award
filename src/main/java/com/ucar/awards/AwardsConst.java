package com.ucar.awards;


public final class AwardsConst {

    public final static String EXCHANGE = "ucar.direct";//MQ exchange
    public final static String QUEUE1 = "ucar.queue1";//队列
    public final static String QUEUE2 = "ucar.queue2";//队列
    public final static String USER = "user:";
    public final static String USERID = "userId";
    public final static String REGISTERDATE = "registerDate";
    public final static String LASTLOGINDATE = "lastLoginDate";
    public final static String CONTINUEDAY = "continueDay";
    public final static String USERID_COINS = "userId_coins";//用户夺宝次数的hk
    public final static String PRIZE = "prize:";
    public final static String PID = "pid";
    public final static String PNAME = "pname";
    public final static String CODE_NUMBER = "codeNumber";
    public final static String PRIZE_CODES = "prize_codes:";  //奖品夺宝码的生成
    public final static String PRIZE_CODES_TEMP = "prize_codes_temp:";   //临时奖品夺宝码生成
    public final static Integer MAX_COINS_CODES = 3;    //一个人最多夺宝次数概率
    public final static String USER_PRIZE = "user_prize:";  //人分配的夺宝码
    public final static String JOIN_PRIZE_COUNT = "join_count_prize:"; //参与抽奖的人数

}
