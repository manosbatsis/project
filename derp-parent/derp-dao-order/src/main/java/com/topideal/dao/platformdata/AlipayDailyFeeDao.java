package com.topideal.dao.platformdata;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.platformdata.AlipayDailyFeeModel;


public interface AlipayDailyFeeDao extends BaseDao<AlipayDailyFeeModel>{
		

	/**
	 * 批量新增	
	 * @param saveList
	 * @return
	 */
	Integer alipayBatchSave(List<AlipayDailyFeeModel> saveList);








}
