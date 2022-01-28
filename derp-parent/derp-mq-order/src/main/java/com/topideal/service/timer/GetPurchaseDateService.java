package com.topideal.service.timer;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/23 18:08
 * @Description:
 */
public interface GetPurchaseDateService {

    /**
     * 从OA获取采购框架合同
     * @param json
     * @param keys
     * @param topics
     * @param tags
     */
    void getPurchaseFrameContractFromOA(String json, String keys, String topics, String tags) throws Exception;

    /**
     * 从OA获取采购试单申请数据
     * @param json
     * @param keys
     * @param topics
     * @param tags
     */
    void getPurchaseTryApplyOrderFromOA(String json, String keys, String topics, String tags) throws Exception;
}
