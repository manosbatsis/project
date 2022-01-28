package com.topideal.service.order;

import java.text.ParseException;

/**
 * @Description: toc结算单核销toc暂估应收单service
 * @Author: Chen Yiluan
 * @Date: 2021-01-07 10:20
 **/
public interface ToCReceiveBillVerifyService {

    /**
     * 根据审核通过的平台结算单号，以“订单+商品货号”查询To C暂估应收跟踪表，
     * 找到对应被当前审核通过/作废通过的平台结算单核销的电商订单进行更新/删除结算信息
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @throws ParseException
     */
    void updateVerifyToCTempBill(String json, String keys, String topics, String tags) throws Exception;

}
