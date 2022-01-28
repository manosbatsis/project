package com.topideal.service;

/**
 * MQ消费监控 MQ
 * @author zhanghx
 * 2018/8/17
 */
public interface ConsumeMonitorService {

    //MQ消费监控
    boolean save(String json, String topics, String tags) throws Exception;

}
