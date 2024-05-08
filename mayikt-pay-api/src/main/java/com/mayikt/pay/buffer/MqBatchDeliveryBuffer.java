package com.mayikt.pay.buffer;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.pay.config.RabbitMQBatchConfig;
import com.mayikt.pay.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
@Slf4j
@Component
public class MqBatchDeliveryBuffer<E>  {
    @Autowired
    private AmqpTemplate amqpTemplate;
    public MqBatchDeliveryBuffer() {
        //单例线程池启动线程
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new BatchBufferThread());
    }
    /**
     * 缓存容器
     */
    private LinkedBlockingQueue<E> buffer = new LinkedBlockingQueue<>();
    /**
     * 缓存msg消息到容器中
     */
    public void add(E msg){
        buffer.offer(msg);
    }
    /**
     * 是否继续运行
     */
    private volatile boolean isRun = true;
    class BatchBufferThread implements Runnable{
        @Override
        public void run() {
            while (isRun){
                try{
                    List<E> bufferMsgs = new ArrayList<>();
                    for(int i=0;i< RabbitMQBatchConfig.BUFFER_SIZE;i++){
                        //从队列中获取msg
                        E msg = buffer.take();
                        bufferMsgs.add(msg);
                    }
                    //将多个msg消息合并成一条发送给mq
                    String msgs = JSONObject.toJSONString(bufferMsgs);
                    log.info("生产者批量投递消息:{}",msgs);
                    amqpTemplate.convertAndSend(RabbitMQBatchConfig.EXCHANGE_BATCH_MAYIKT_NAME,"",msgs);
                }catch (Exception e){
                    log.info("e:{}",e);
                }
            }
        }
    }
}
