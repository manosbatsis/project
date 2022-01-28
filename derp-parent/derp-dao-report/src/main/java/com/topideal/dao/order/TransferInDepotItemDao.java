package com.topideal.dao.order;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.TransferInDepotItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨入库表体 dao
 * @author lian_
 */
public interface TransferInDepotItemDao extends BaseDao<TransferInDepotItemModel> {
		
	/**
	 * 根据调拨ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(List ids)throws SQLException;

	Integer getVIPInDepotAccount(Map<String, Object> queryMap);

	/**
	 * 唯品By PO获取调拨入库明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVIPInDepotDetails(Map<String, Object> queryMap);

}
