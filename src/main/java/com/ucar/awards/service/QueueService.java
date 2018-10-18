package com.ucar.awards.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author liaohong
 * @since 2018/10/18 16:51
 */

@Service
public class QueueService {

    private LinkedBlockingQueue<String> queue;

    /**
     * 初始化队列
     *
     * @param length
     */
    public void initQueue(Integer length) {
        queue = new LinkedBlockingQueue<>(length);
    }

    /**
     * 入队
     *
     * @param data
     * @return
     */
    public boolean pushQueue(String data) {
        return queue.offer(data);
    }

    /**
     * 出队
     *
     * @return
     */
    public List<String> popQueue() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < queue.size(); i++) {
            list.add(queue.poll());
        }
        return list;
    }
}
