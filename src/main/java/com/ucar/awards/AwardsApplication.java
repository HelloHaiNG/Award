package com.ucar.awards;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class AwardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwardsApplication.class, args);
    }
}
