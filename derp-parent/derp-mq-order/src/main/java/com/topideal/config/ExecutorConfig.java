package com.topideal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**线程池配置类
 * 使用方法：在需要使用多线程异步执行的方法上加上注解即可：@Async("asyncServiceExecutor")
 * */
@Configuration
@EnableAsync
public class ExecutorConfig {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    @Bean
    public Executor asyncServiceExecutor() {
        logger.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最小空闲线程数量
        executor.setCorePoolSize(1);
        //最大运行线程数量 设置为1变成队列模式运行
        executor.setMaxPoolSize(10);
        //配置队列大小
        executor.setQueueCapacity(Integer.MAX_VALUE);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("异步线程池-");
        //拒绝策略：CALLER_RUNS：如果线程池满后则不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
