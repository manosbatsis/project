package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购订单表体 mapper
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface PurchaseOrderItemMapper extends BaseMapper<PurchaseOrderItemModel> {

	/**
	 * 根据多条采购订单条件查询
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderItemModel> getByOrderIds(List ids) throws SQLException; 
	
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);
	/**
	 * 查询采购单商品采购数量 按商品分组
	 * */
	public List<Map<String,Object>> getGoodsNumGroupBy(Map<String,Object> map);

	Map<String,Object> countNumByGoodsNoAndIds(Map<String, Object> queryMap);


	/**
	 * 查询采购订单表体
	 * @param model
	 * @return
	 */
	List<PurchaseOrderItemModel> getPurchaseOrderItem(PurchaseOrderItemModel model);

	/**
	 * 统计
	 * @param idList
	 * @return
	 */
    Map<String, Object> statisticsByIds(List<Long> idList);

    int countByOrderId(Long orderId);
}

