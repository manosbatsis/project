package com.topideal.service.pushback.purchase;

public interface PurchaseWriteOffPushbackService {

    void modifyStatus(String json, String keys, String topics, String tags) throws Exception;

}
