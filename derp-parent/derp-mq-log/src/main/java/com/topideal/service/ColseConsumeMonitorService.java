package com.topideal.service;

/**
 * 关闭日志监控
 * @author 杨创
 * 2020/10/27
 */
public interface ColseConsumeMonitorService {

    //MQ消费监控
    boolean saveClose(String json, String topics, String tags) throws Exception;

}
