package com.mayikt.pay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * springboot 整合线程池
 */
@EnableAsync(proxyTargetClass = true)
@Configuration
public class MayiktThreadPoolConfig {

    /**
     * 线程核心数
     */
    @Value("${zengjing.thread.corePoolSize}")
    private int corePoolSize;
    /**
     * 线程最大数
     */
    @Value("${zengjing.thread.maxPoolSize}")
    private int maxPoolSize;
    /**
     * 任务容量
     */
    @Value("${zengjing.thread.queueCapacity}")
    private int queueCapacity;
    /**
     * 允许空闲时间，默认60
     */
    @Value("${zengjing.thread.keepAlive}")
    private int keepAlive;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAlive);
        threadPoolTaskExecutor.setThreadNamePrefix("zengjingThread-");
        //设置拒绝策略 当线程数达到最大时，如何处理新任务
        //CallerRunsPolicy 不由线程池中线程执行，由调用者所在线程执行
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskExecutor;
    }
}