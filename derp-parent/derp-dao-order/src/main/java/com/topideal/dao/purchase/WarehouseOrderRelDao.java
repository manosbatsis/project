package com.topideal.dao.purchase;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.entity.vo.purchase.WarehouseOrderRelModel;

/**
 * 采购入库关联采购订单表 dao
 * @author lian_
 */
public interface WarehouseOrderRelDao extends BaseDao<WarehouseOrderRelModel>{

	/**
	 * 根据采购单id获取最后入仓时间
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseModel> getInboundDateById(Long id) throws SQLException;

	/**
	 * 根据审核时间获取关联关系
	 * @param id
	 * @return
	 */
	List<WarehouseOrderRelModel> listOrderByAudthTime(Long id);

	/**
	 * 根据采购订单id 统计红冲与否的数量
	 * @param purchaseId
	 * @return
	 */
	Integer countWriteOffNum(Long purchaseId, String isWriteOff);
}

