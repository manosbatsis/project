package com.topideal.service.order;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/18 15:33
 * @Description: 生成toc暂估月结数据
 */
public interface SaveTocTempBillMonthlyService {

    /**
     * 生成toc暂估收入月结
     * @param toString
     * @param keys
     * @param topics
     * @param tags
     */
    void saveToCTempReceiveBillMonthly(String toString, String keys, String topics, String tags) throws Exception;

    /**
     * 生成toc暂估费用月结
     * @param toString
     * @param keys
     * @param topics
     * @param tags
     */
    void saveToCTempCostBillMonthly(String toString, String keys, String topics, String tags) throws Exception;

    /**
     * 生成toc月结
     * @param json
     * @param keys
     * @param topics
     * @param tags
     */
    void SaveToCTempMonthly(String json, String keys, String topics, String tags) throws Exception;
}
