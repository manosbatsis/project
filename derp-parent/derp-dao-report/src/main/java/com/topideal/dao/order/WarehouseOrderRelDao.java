package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.WarehouseOrderRelModel;

/**
 * 采购入库关联采购订单表 dao
 * @author zhanghx
 */
public interface WarehouseOrderRelDao extends BaseDao<WarehouseOrderRelModel> {
	/**
	 * 查询采购入库关联采购订单表    (根据采购入库单升序排列)
	 * @param modle
	 * @return
	 */
	List<WarehouseOrderRelModel> getAscPurchaseOrderIdList(WarehouseOrderRelModel modle)throws SQLException ;
}

