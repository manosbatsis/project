package com.topideal.service.timer;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/22 11:16
 * @Description: 应收关账
 */
public interface ReceiveCloseAccountsService {

    /**
     * 生成应收关账
     * @param json
     * @param keys
     * @param topics
     * @param tags
     */
    void saveReceiveCloseAccounts(String json, String keys, String topics, String tags) throws Exception;
}
