package com.topideal.dao.order;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.BillOutinDepotItemModel;


public interface BillOutinDepotItemDao extends BaseDao<BillOutinDepotItemModel> {

	/**
	 * 根据处理类型汇总
	 * @param queryMap
	 * @return
	 */
	List<BillOutinDepotItemModel> getVipPoAccountByType(Map<String, Object> queryMap);

	/**
	 * 根据处理类型查询明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVipDetails(Map<String, Object> queryMap);

	/**
	 * 获取自动校验出入库量
	 * @param queryMap
	 * @return
	 */
	BillOutinDepotItemModel getAutoVeriOutinDepotAccount(Map<String, Object> queryMap);
		










}
