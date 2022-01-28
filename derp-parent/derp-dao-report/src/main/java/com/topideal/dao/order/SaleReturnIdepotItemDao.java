package com.topideal.dao.order;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.SaleReturnIdepotItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 销售退货入库表体  dao
 * @author lian_
 */
public interface SaleReturnIdepotItemDao extends BaseDao<SaleReturnIdepotItemModel> {
		

	/**
	 * 根据销售退货入库ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(List ids)throws SQLException;

	Double getPrice(String orderCode, String goodsNo) throws SQLException;

	Integer getYJVeriSaleRetunIdepotAccount(Map<String, Object> queryMap);


}
