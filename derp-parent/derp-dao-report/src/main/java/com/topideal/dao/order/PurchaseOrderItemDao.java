package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.PurchaseOrderItemModel;

/**
 * 采购订单表体 dao
 * @author zhanghx
 */
public interface PurchaseOrderItemDao extends BaseDao<PurchaseOrderItemModel> {
	

	/**
	 * 统计事业部商家、仓库、本月采购未上架明细
	 */
	List<Map<String, Object>> getBuMonthPurchaseNotshelfInfo(Map<String, Object> map) throws SQLException;
	

	/**业务经销存-本月来货残次
	 * 按理货单位分组统计
	 * */
	List<Map<String, Object>> getBuMonthInBadNum(Map<String, Object> map);
	/**
	 * 事业部来货残次：取本月采购入库中残次品
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuPurchaseInBad(Map<String, Object> map);
	/**
	 * 事业部来货残次：调拨入库单中坏品
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuTransferInBad(Map<String, Object> map);
	/**
	 * 事业部来货残次：退货入库单中坏品
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuSaleReturnInBad(Map<String, Object> map);
	/**
	 * 事业部来货残次：上架入库单中坏品
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuSaleShelfInBad(Map<String, Object> map);

	

	

	


	

	/**
	 * (事业部财务经销存)获取采购残损明细表 来货短缺数据
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuFinancePurchaseDamagedLackList(Map<String, Object> map);
	

	

	/**
	 * 获取(事业部财务经销存)累计采购在途明细表加权
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuFinanceAddPurchaseNotshelfDetailsWeighte(Map<String, Object> map);
	


	/**
	 * 获取 本期采购减少在途明细  此采购单,此商品入库时间等于本月 入库的 量
	 */
	//Map<String, Object> getFinanceDecreasePurchaseWarehouseNum(Map<String, Object> map);
	

	
	/**
	 * 在库天数获取采购表体
	 * @param queryPurchaseMap
	 * @return
	 */
	List<PurchaseOrderItemModel> getInWarehouserItem(Map<String, Object> queryPurchaseMap);
	
	/**
	 * 获取来货短汇总
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuLackNumSum(Map<String, Object> map);
	/**
	 * 查询表体list
	 *按效期区间排序
	 */
	List<PurchaseOrderItemModel> listOrderByEffective(PurchaseOrderItemModel itemModel) throws SQLException;
	
}
