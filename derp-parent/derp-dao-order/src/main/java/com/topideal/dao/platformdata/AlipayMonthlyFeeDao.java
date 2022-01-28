package com.topideal.dao.platformdata;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.platformdata.AlipayMonthlyFeeModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface AlipayMonthlyFeeDao extends BaseDao<AlipayMonthlyFeeModel>{
		
	/**
	 * 批量新增	
	 * @param saveList
	 * @return
	 */
	Integer alipayBatchSave(List<AlipayMonthlyFeeModel> saveList);


	/**
	 * 根据外部订单号集合和商家id批量统计以“电商订单号+结算单号”为维度的每月结算费用
	 * @param partnerTransactionIds 外部订单号集合
	 * @param merchantId 商家id
	 * @return
	 */
	List<AlipayMonthlyFeeModel> statisticsByExCodesAndMerId(List<String> partnerTransactionIds,Long merchantId) throws SQLException;

	/**
	 * 统计每月结算费用数量
	 * @param model
	 * @return
	 */
	Integer statisticsByModel(AlipayMonthlyFeeModel model) throws SQLException;


	/**
	 * distinct 获取数量
	 * @param alipayMonthlyFeeModel
	 * @return
	 */
    Integer statisticsDistinctByModel(AlipayMonthlyFeeModel alipayMonthlyFeeModel);

	/**
	 * list Model By Map
	 * @param queryMap
	 * @return
	 */
	List<AlipayMonthlyFeeModel> listByMap(Map<String, Object> queryMap);

    AlipayMonthlyFeeModel searchDistinctByPage(AlipayMonthlyFeeModel alipayMonthlyFeeModel);
}
