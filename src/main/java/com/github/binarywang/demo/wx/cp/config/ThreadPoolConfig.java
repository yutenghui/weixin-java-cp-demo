package com.github.binarywang.demo.wx.cp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ThreadPoolConfig {

    @Value("${otherThreadPool.corePoolSize:30}")
    private Integer corePoolSize;
    @Value("${ThreadPool.searchThreadPool:50}")
    private Integer maximumPoolSize;
    @Value("${ThreadPool.queueSize:100}")
    private Integer queueSize;

    @Bean("otherThreadPoolExecutor")
    public ThreadPoolExecutor otherThreadPoolExecutor() {
        LinkedBlockingDeque<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<>(queueSize);
        ThreadFactory threadFactory = new ThreadFactory() {
            final AtomicInteger atomicInteger = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("普通业务线程" + atomicInteger.getAndIncrement());
                return thread;
            }
        };
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 10, TimeUnit.SECONDS, linkedBlockingDeque, threadFactory);
        return threadPoolExecutor;
    }
}
