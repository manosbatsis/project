package com.topideal.service.pushback.sale;

public interface SaleWriteOffPushBackService {
    void modifyStatus(String json, String keys, String topics, String tags) throws Exception;
}
