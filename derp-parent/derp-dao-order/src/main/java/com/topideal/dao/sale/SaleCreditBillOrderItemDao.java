package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.SaleCreditBillOrderItemModel;


public interface SaleCreditBillOrderItemDao extends BaseDao<SaleCreditBillOrderItemModel>{
	/**
	 * 获取赊销单对应商品已赎回数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Integer getRedempNum(Map<String,Object> map) throws SQLException;
}
