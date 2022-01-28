package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.SaleOrderDTO;
import com.topideal.entity.vo.order.SaleOrderModel;

/**
 * 销售订单 dao
 * @author lian_
 *
 */
public interface SaleOrderDao extends BaseDao<SaleOrderModel> {
	


	/**
     * 根据条件查询
     * @return
     */
	List<SaleOrderModel> queryList(SaleOrderModel model)throws SQLException;

	/**
	 * 根据销售单号获取表体备注
	 * */
	//List<Map<String, Object>> getOrderRemark(List<String> list);

	/**
	 * 报表API获取销售订单
	 * @param queryMap
	 * @return
	 */
	List<SaleOrderDTO> getRepotApiList(Map<String, Object> queryMap);

	Integer getRepotApiListCount(Map<String, Object> queryMap);
	/**查询时间范围内有修改的销售单号
	 * */
	List<String> getSaleCodeList(Map<String,Object> map);
	/**根据单号查询订单
	 * */
	List<Map<String,Object>> getSaleByCodeList(Map<String,Object> map);
	/**根据单号查询订单表体
	 * */
	List<Map<String,Object>> getSaleItemByCodeList(Map<String,Object> map);
}

