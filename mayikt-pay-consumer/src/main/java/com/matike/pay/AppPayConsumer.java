package com.matike.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.matike.pay.mapper")
public class AppPayConsumer {
    public static void main(String[] args) {
        SpringApplication.run(AppPayConsumer.class);
    }
}
