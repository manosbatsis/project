package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderItemDTO;
import com.topideal.entity.vo.sale.OrderItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 电商订单表体 dao
 * @author lian_
 *
 */
public interface OrderItemDao extends BaseDao<OrderItemModel>{

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);
	
	List<OrderItemDTO> listItemDTO(OrderItemDTO dto);
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String,Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String,Object> map);

	List<OrderItemDTO> listItemByOrderDTO(List<Long> orderIds);

	/*根据电商订单id集合和事业部id查询关联的电商订单表体*/
	List<OrderItemDTO> listItemByOrderIdsAndBuId(List<Long> orderIds, Long buId);

	/**
	 * 根据电商订单id集合查询对应的结算金额最高的商品明细
	 * @param orderIds 电商订单Id List
	 * @param buIdFlag 是否需要具体到事业部的维度, 1-需要  2-不需要
	 * @return
	 */
	List<OrderItemModel> getMaxPriceByOrderId(List<Long> orderIds, Boolean buIdFlag);

	/*根据电商订单统计事业部*/
	List<Long> listBuByOrder(OrderDTO dto);
	//批量新增
	Integer batchSave(List<OrderItemModel> list) throws SQLException;
	//批量更新
	void batchUpdate(List<OrderItemModel> list) throws SQLException;
}

