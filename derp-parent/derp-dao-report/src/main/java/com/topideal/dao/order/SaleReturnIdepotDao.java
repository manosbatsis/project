package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.SaleReturnIdepotModel;

/**
 * 销售退货入库 dao
 * @author lian_
 */
public interface SaleReturnIdepotDao extends BaseDao<SaleReturnIdepotModel> {
		

	/**
	 * 报表api 根据 商家开始时间结束时间分页查询销售退货入库单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getListPage(Long merchantId, String startTime, String endTime, Integer startIndex, Integer pageSize)throws SQLException;
	
	/**
	 * 报表api 根据 商家开始时间结束时间 查询调销售退货入库单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(Long merchantId, String startTime, String endTime);

}
