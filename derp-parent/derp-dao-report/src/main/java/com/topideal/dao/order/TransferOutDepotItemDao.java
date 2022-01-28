package com.topideal.dao.order;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.TransferOutDepotItemModel;

import java.util.List;
import java.util.Map;

/**
 * 调拨出库表体 dao
 * @author lian_
 *
 */
public interface TransferOutDepotItemDao extends BaseDao<TransferOutDepotItemModel> {

	/**
	 * 唯品ByPO获取调拨出库量
	 * @param queryMap
	 * @return
	 */
	Integer getVIPOutDepotAccount(Map<String, Object> queryMap);

	/**
	 * 获取调拨出库明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVIPOutDepotDetails(Map<String, Object> queryMap);



}
