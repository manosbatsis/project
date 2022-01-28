package com.topideal.service.timer;

/**
  * 电商订单分摊税费运费
 * @author qiancheng.chen
 * @date 2020-12-24
 *
 */
public interface OrderApportionTaxAndFreightService {
	
	public void apportionTaxAndFreight(String json, String keys, String topics, String tags) throws Exception;

}
