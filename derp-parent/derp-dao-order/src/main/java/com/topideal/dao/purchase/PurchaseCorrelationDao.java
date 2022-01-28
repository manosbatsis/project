package com.topideal.dao.purchase;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.purchase.PurchaseCorrelationModel;


public interface PurchaseCorrelationDao extends BaseDao<PurchaseCorrelationModel>{
		
	/**
	 * 根据入库单集合id获取数据
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseCorrelationModel> getDetailsByIds(List list) throws SQLException;

}

