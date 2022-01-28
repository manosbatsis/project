package com.topideal.service.timer;

public interface ReceiveVerifyBackfillCreditOrderService {

    /**
     * 赊销单/赊销收款单创建的应收单收款核销回填
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @throws Exception
     */
    public void saveBackfillCredit(String json, String keys, String topics, String tags) throws Exception;
}
