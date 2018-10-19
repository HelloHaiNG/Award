package com.ucar.awards.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
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
    public LinkedBlockingQueue initQueue(Integer length) {
        return queue = new LinkedBlockingQueue<>(length);
    }

    /**
     * 入队
     *
     * @param data
     * @return
     */
    public boolean pushQueue(String data, LinkedBlockingQueue queue1) {
        return queue1.offer(data);
    }

    /**
     * 出队
     *
     * @return
     */
    public List<String> popQueue(LinkedBlockingQueue queue1) {
        int size = queue1.size();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add((String) queue1.poll());
        }
        return list;
    }
}
