package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseWriteOffItemDTO;
import com.topideal.entity.vo.purchase.PurchaseWarehouseItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 采购入库商品列表 dao
 * @author lianchenxing
 *
 */
public interface PurchaseWarehouseItemDao extends BaseDao<PurchaseWarehouseItemModel>{
		
	/**
	 * 根据勾选的入库单id查询入库勾稽明细
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseItemModel> getDetails(List list) throws SQLException;

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);
	/**
	 * 查询入库单商品、数量按商品分组合并
	 * */
	public List<Map<String, Object>> getWarehouseItem(Map<String,Object> paramMap);

	/**
	 * 根据采购入库单id集合查询表体
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWriteOffItemDTO> getDetailsByIds(List<Long> ids) throws SQLException;
}

