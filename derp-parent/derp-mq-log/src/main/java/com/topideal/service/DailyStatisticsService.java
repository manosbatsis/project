package com.topideal.service;

/**
 * 统计MQ消费情况-发送邮件
 */
public interface DailyStatisticsService {

    //发送邮件
    boolean sendMail(String json, String topics, String tags) throws Exception;

}
