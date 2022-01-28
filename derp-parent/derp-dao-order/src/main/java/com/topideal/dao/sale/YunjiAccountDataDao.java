package com.topideal.dao.sale;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.YunjiAccountDataModel;

public interface YunjiAccountDataDao extends BaseDao<YunjiAccountDataModel>{
		

	/**
	 * 获取云集结算账单表头有哪些商家
	 * @return
	 */
	public List<Map<String, Object>> getYunjiAccountMerchant(Map<String, Object> map);

	/**
	 * 获取要生成平台结算单
	 * @param queryMap
	 * @return
	 */
	public List<YunjiAccountDataModel> getPlatformStatementData(Map<String, Object> queryMap);








}
