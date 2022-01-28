package com.topideal.service.timer;

import java.text.ParseException;

/**
 * @Description: toc暂估应收账单核销toc结算单service
 * @Author: Chen Yiluan
 * @Date: 2021-01-07 10:20
 **/
public interface ToCTempBillVerifyService {

    /**
     * 获取指定月份的toc 暂估应收单，以“电商订单号+商品货号”查询平台结算单（仅待核销、部分核销、已核销、作废待审的结算应收单号）；
     * 对已有平台结算信息的电商订单号，回填结算金额、结算日期、对应平台结算单号；
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @throws ParseException
     */
    void updateVerifyToCTempBill(String json, String keys, String topics, String tags) throws Exception;


    void updateVerifyToCTempCostBill(String json, String keys, String topics, String tags) throws Exception;

}
