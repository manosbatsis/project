package com.topideal.dao.platformdata;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.platformdata.AlipayDailySettlebatchModel;


public interface AlipayDailySettlebatchDao extends BaseDao<AlipayDailySettlebatchModel>{
	/**
	 * 批量新增	
	 * @param saveList
	 * @return
	 */
	Integer alipayBatchSave(List<AlipayDailySettlebatchModel> saveList);

	/**
	 * 平台结算单-统计
	 * @param queryMap
	 * @return
	 */
	List<AlipayDailySettlebatchModel> getPlatformStatementData(Map<String, Object> queryMap);









}
