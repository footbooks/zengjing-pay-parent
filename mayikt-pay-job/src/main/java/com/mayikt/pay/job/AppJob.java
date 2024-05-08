package com.mayikt.pay.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.mayikt.pay.job.mapper")
@EnableAsync
public class AppJob {
    public static void main(String[] args) {
        SpringApplication.run(AppJob.class);
    }
}
