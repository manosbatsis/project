package com.topideal.service.timer;

import net.sf.json.JSONObject;

/**
 * @Author: Wilson Lau
 * @Date: 2021/10/8 17:59
 * @Description:
 */
public interface InvailOrderDataSendAlertEmailService {

    /**
     * 检测无效调拨数据并发送预警电邮
     * @param jsonData
     * @param keys
     * @param topics
     * @param tags
     */
    void checkTransferDataAndSendAlertEmail(JSONObject jsonData, String keys, String topics, String tags) throws Exception;

    /**
     * 检测无效电商数据并发送预警邮电
     * @param jsonData
     * @param keys
     * @param topics
     * @param tags
     */
    void checkOrderDataAndSendAlertEmail(JSONObject jsonData, String keys, String topics, String tags) throws Exception;

    void checkDataAndSendAlertEmail(String json, String keys, String topics, String tags) throws Exception;
}
