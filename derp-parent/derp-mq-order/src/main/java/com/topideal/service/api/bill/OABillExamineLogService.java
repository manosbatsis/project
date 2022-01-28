package com.topideal.service.api.bill;

public interface OABillExamineLogService {

    /**
     * 存储OA审批推送日志
      * @param json
     * @param keys
     * @param topics
     * @param tags
     * @return
     * @throws Exception
     */
   public  boolean insertExamineLog(String json,String keys,String topics,String tags)throws Exception;
}
