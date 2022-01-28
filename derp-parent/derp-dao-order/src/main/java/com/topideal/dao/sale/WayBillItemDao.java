package com.topideal.dao.sale;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.WayBillItemDTO;
import com.topideal.entity.vo.sale.WayBillItemModel;

/**
 * 运单表体 dao
 * @author lian_
 *
 */
public interface WayBillItemDao extends BaseDao<WayBillItemModel>{
	/**
	 * 根据电商订单id获取运单表体信息
	 * @param id
	 * @return
	 */
	List<WayBillItemDTO> listWayBillItemByOrderId(Long id);
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String,Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String,Object> map);







}

