package com.ucar.awards.service;

import com.ucar.awards.AwardsConst;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author liaohong
 * @since 2018/10/9 15:10
 */
@Service
public class JedisService {

    private String HOST = "localhost";
    private Integer PORT = 6379;
    private Jedis jedis;

    public JedisService() {
        JedisPool jedisPool = new JedisPool(HOST, PORT);
        jedis = jedisPool.getResource();
    }

    /**
     * hash取值
     *
     * @param key
     * @param field
     * @return
     */
    public List hmget(String key, String field) {
        List list = jedis.hmget(key, field);
        return list;
    }

    /**
     * hash判断hash中的value是否存在
     *
     * @param key
     * @param field
     * @return
     */
    public boolean hexists(String key, String field) {
        boolean flag = jedis.hexists(key, field);
        return flag;
    }

    /**
     * hash取值
     *
     * @param key
     * @param field1
     * @param field2
     * @return
     */
    public List<String> hmget1(String key, String field1, String field2) {
        List list = jedis.hmget(key, field1, field2);
        return list;
    }

    /**
     * hash操作赋值
     *
     * @param key
     * @param field1
     * @param field2
     */
    public void hset(String key, String field1, String field2) {
        jedis.hset(key, field1, field2);
    }

    /**
     * 遍历list
     *
     * @param key
     * @return
     */
    public List<String> lrang(String key) {
        List list = jedis.lrange(key, 0, -1);
        return list;
    }

    /**
     * redis自带锁
     *
     * @param key
     * @param value
     */
    public void setnx(String key, String value) {
        jedis.setnx(key, value);
    }

    public void addUser() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AwardsConst.USERID, "4");
        hashMap.put(AwardsConst.REGISTERDATE, "2017-10-01");
        hashMap.put(AwardsConst.LASTLOGINDATE, "2018-10-09");
        hashMap.put(AwardsConst.CONTINUEDAY, "5");
        jedis.hmset(AwardsConst.USER + "4", hashMap);
    }

    public void addCoins() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("4", "10");
        jedis.hmset(AwardsConst.USERID_COINS, hashMap);
    }

    /**
     * 添加奖品基础信息
     *
     * @param pid
     * @param pname
     */
    public void addPrize(String pid, String pname, String codeNumber) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AwardsConst.PID, pid);
        hashMap.put(AwardsConst.PNAME, pname);
        hashMap.put(AwardsConst.CODE_NUMBER, codeNumber);
        jedis.hmset(AwardsConst.PRIZE + pid, hashMap);
    }

    /**
     * hash赋值
     *
     * @param key
     * @param hash
     */
    public void hmset(String key, HashMap hash) {
        jedis.hmset(key, hash);
    }

    /**
     * 给奖品赋值奖品码
     *
     * @param pid
     * @param codeNumbers
     */
    public void prizeCodes(String pid, Integer codeNumbers) {
        List<String> list = new ArrayList<>();
        String[] strings = new String[codeNumbers];
        for (int i = 1; i <= codeNumbers; i++) {
            list.add(String.valueOf(i));
        }
        //乱序
        Collections.shuffle(list);
        strings = list.toArray(strings);
        jedis.lpush(AwardsConst.PRIZE_CODES + pid, strings);
        jedis.lpush(AwardsConst.PRIZE_CODES_TEMP + pid, strings);
    }

    /**
     * List操作移除string
     *
     * @param key
     */
    public String lpop(String key) {
        return jedis.lpop(key);
    }

    /**
     * String操作赋值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        jedis.set(key, value);
    }

    /**
     * String操作取值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return jedis.get(key);
    }

    /**
     * hash中给key指定的value增量
     *
     * @param key
     * @param field
     * @param increment
     */
    public void hincrby(String key, String field, Long increment) {
        jedis.hincrBy(key, field, increment);
    }

    /**
     * 获取列表长度List
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        return jedis.llen(key);
    }

    /**
     * String指定key的value加一
     *
     * @param key
     */
    public void incr(String key) {
        jedis.incr(key);
    }

    /**
     * 删除
     * @param key
     */
    public void delete(String key) {
        jedis.del(key);
    }

    public static void main(String[] args) {
        JedisService service = new JedisService();
        service.addUser();
        service.addCoins();
    }

}
