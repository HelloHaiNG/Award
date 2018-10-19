package com.ucar.awards;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.LinkedBlockingQueue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardsApplicationTests {

    private LinkedBlockingQueue queue;

    @Test
    public void contextLoads() {
        queue = new LinkedBlockingQueue(5);
        System.out.println(queue.size()+"       999999999");
    }

    @Test
    public void put1() {
        contextLoads();
        queue.offer("111");
        System.out.println("2222222.......");
    }

    @Test
    public void put2() {
        contextLoads();
        queue.offer("222");
        System.out.println("333333333.......");
    }

    @Test
    public void put3() {
        contextLoads();
        queue.offer("333");
        System.out.println("444444444.......");
    }
}




