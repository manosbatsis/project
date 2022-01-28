package com.topideal.dao.inventory;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.inventory.MonthlyAccountItemModel;

/**
 * 月结账单商品明细 dao
 * 
 * @author lian_
 *
 */
public interface MonthlyAccountItemDao extends BaseDao<MonthlyAccountItemModel> {

	/**
	 * 根据月结id删除明细
	 * 
	 * @param monthlyAccountId
	 */
	int delItemByMonthlyAccountId(Long monthlyAccountId);

	/**
	 * 统计存货跌价信息
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getInventoryFallingPriceReserves(Map<String, Object> queryMap);

}
