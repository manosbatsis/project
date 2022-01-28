package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购订单表体 dao
 * 
 * @author lianchenxing
 *
 */
public interface PurchaseOrderItemDao extends BaseDao<PurchaseOrderItemModel> {

	/**
	 * 列表查询
	 * 
	 * @param model
	 * @return
	 */
	List<PurchaseOrderItemModel> list(PurchaseOrderItemModel model) throws SQLException;

	/**
	 * 根据实体类查询库存信息
	 * 
	 * @param DepotInfoModel
	 * @return
	 */
	public PurchaseOrderItemModel searchByModel(PurchaseOrderItemModel model) throws SQLException;

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
	Integer checkGoodsIsUse(Long id);
	/**
	 * 查询采购单商品采购数量 按商品分组
	 * */
	public List<Map<String,Object>> getGoodsNumGroupBy(Map<String,Object> map);

	Map<String, Object> countNumByGoodsNoAndIds(Map<String, Object> queryMap);


	/**
	 * 查询采购订单表体
	 * @param model
	 * @return
	 */
	public List<PurchaseOrderItemModel> getPurchaseOrderItem(PurchaseOrderItemModel model);

	/**
	 * 统计
	 * @param idList
	 * @return
	 */
    Map<String, Object> statisticsByIds(List<Long> idList);

    int countByOrderId(Long orderId);
}
