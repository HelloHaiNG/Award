package com.ucar.awards.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author liaohong
 * @since 2018/10/19 14:01
 */
@Component
@Service
public class ConcurrentHashMapService implements ApplicationRunner {

    private ConcurrentHashMap concurrentHashMap;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println("初始化ConcurrentHashMap............");
        concurrentHashMap = new ConcurrentHashMap();
    }

    public void put(String key, LinkedBlockingQueue blockingDeque) {
        concurrentHashMap.put(key, blockingDeque);
    }

    public LinkedBlockingQueue get(String key) {
        return (LinkedBlockingQueue) concurrentHashMap.get(key);
    }

    public void remove(String key) {
        concurrentHashMap.remove(key);
    }
}
