package com.topideal.order.service.purchase;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.vo.purchase.PurchaseWarehouseBatchModel;


/**
 * 采购入库商品批次
 * @author zhanghx
 */
public interface PurchaseWarehouseBatchService {

	/**
	 * 根据采购入库单id获取商品信息和批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseBatchModel> getGoodsAndBatch(Long id) throws SQLException;
	
}
