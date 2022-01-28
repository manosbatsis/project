package com.topideal.service.timer;

import java.util.List;
import java.util.Map;

import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.entity.MerchantInfoMongo;

public interface YunJiDeliveryAndReturnDetailService {
	/**清空临时表
	 * */
    public void clearTable(Map<String, Object> map);
    
    /**
     * 云集退货
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @param idList
     * @return
     * @throws Exception
     */
    List<InvetAddOrSubtractRootJson> saveYunJiReturnDetail(String json, String keys, String topics, String tags,List<String> settleIdList,MerchantInfoMongo merchant,String accountUserCode) throws Exception;
	
	/**
	 * 云集发货
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	List<InvetAddOrSubtractRootJson> saveYunJiDeliveryDetail(String json, String keys, String topics, String tags,List<String> settleIdList,MerchantInfoMongo merchant,String accountUserCode,String settleId) throws Exception;
	
	/**
	 * 批量推送库存
	 * @param rootJsonList
	 */
	void pushMQ(List<InvetAddOrSubtractRootJson> rootJsonList);
}
