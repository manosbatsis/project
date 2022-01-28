package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.TransferInDepotModel;

/**
 * 调拨入库dao
 * @author zhanghx
 */
public interface TransferInDepotDao extends BaseDao<TransferInDepotModel> {
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询调拨入库单
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
	 * 报表api 根据 商家开始时间结束时间 查询调拨入库单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(Long merchantId, String startTime, String endTime);

}

