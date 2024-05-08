package com.mayikt.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.mayikt.pay.mapper")
public class AppPay {
    public static void main(String[] args) {
        SpringApplication.run(AppPay.class);
    }
}
