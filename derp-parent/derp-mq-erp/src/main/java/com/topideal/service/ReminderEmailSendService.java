package com.topideal.service;

import java.sql.SQLException;


public interface ReminderEmailSendService  {

    /**
     * 发送邮件提醒类型
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @throws SQLException
     */
    void saveSendMail(String json, String keys, String topics, String tags) throws SQLException;
}
