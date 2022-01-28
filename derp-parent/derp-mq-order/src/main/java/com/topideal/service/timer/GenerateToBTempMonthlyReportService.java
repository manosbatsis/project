package com.topideal.service.timer;

/**
 * @Description: 生成Tob暂估报表service
 * @Author: Chen Yiluan
 **/
public interface GenerateToBTempMonthlyReportService {

    /**
     * 生成tob暂估收入月结报表
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @return
     * @throws Exception
     */
    boolean Save2BRevenueMonthly(String json,String keys,String topics,String tags) throws Exception;


    /**
     * 生成tob暂估费用月结报表
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @return
     * @throws Exception
     */
    boolean Save2BCostMonthly(String json,String keys,String topics,String tags) throws Exception;

}
