package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.SaleReturnOrderModel;

/**
 * 销售退货订单 dao
 * @author lian_
 *
 */
public interface SaleReturnOrderDao extends BaseDao<SaleReturnOrderModel> {
	/**
	 * 逻辑删除
	 * @param ids
	 * @return
	 * @throws SQLException 
	 */
	int delSaleReturnOrder(List ids) throws SQLException;


}

