package com.topideal.dao.platformdata;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.platformdata.AlipayDailySettleModel;


public interface AlipayDailySettleDao extends BaseDao<AlipayDailySettleModel>{
		
	/**
	 * 批量新增	
	 * @param saveList
	 * @return
	 */
	Integer alipayBatchSave(List<AlipayDailySettleModel> saveList);









}
