package com.topideal.service.api.sale;
/**
 * 销售退货--初理货
 * @author 杨创
 *2018.7.26
 */
public interface XSTFirstTallyService {
	 //初存初理货信息
   public boolean saveTallyInfo(String json,String keys,String topics,String tags) throws Exception;


}
