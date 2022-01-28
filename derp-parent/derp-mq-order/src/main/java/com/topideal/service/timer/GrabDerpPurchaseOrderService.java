package com.topideal.service.timer;

import java.util.List;

import net.sf.json.JSONObject;

/**
 * 抓取经分销收到发票没有付款的采购订单
 * @author 杨创
 *2018/10/19
 */
public interface GrabDerpPurchaseOrderService {
	/**
	 * 抓取经分销收到发票没有付款的采购订单
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean saveGrabDerpPurchaseOrder (String json,String keys,String topics,String tags) throws Exception;	
	

}
