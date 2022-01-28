package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.WayBillItemDTO;
import com.topideal.entity.vo.order.WayBillItemModel;

/**
 * 运单表体 dao
 * @author lian_
 */
public interface WayBillItemDao extends BaseDao<WayBillItemModel> {
		

	/**
	 * 根据电商订单ids 获取获取运单信息和商品信息
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(List ids)throws SQLException;



	WayBillItemModel getWayBillItemByOrder(Map<String, Object> map);
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String, Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String, Object> map);




}
