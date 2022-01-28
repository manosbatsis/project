package com.topideal.service.api.bill;

/**
 * OA 审批结果推送接口
 */
public interface OAExamineResultService {

    /**
     * 修改OA审批结果推送
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @return
     * @throws Exception
     */
    public  boolean updateExamineResult(String json,String keys,String topics,String tags)throws Exception;
}
