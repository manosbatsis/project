package com.topideal.dao.order;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.SaleShelfModel;

/**
 * 销售上架信息表 dao
 * @author lian_
 *
 */
public interface SaleShelfDao extends BaseDao<SaleShelfModel> {

	/**
	 * 根据po查出对应的上架总量信息（旧版：代销）
	 * @param merchantId 
	 * @param poNo
	 * @return
	 */
	List<Map<String, Object>> getShelfAccountByPo(Map<String, Object> queryMap);

	/**
	 * 根据po、标准条码、sku,获取销售上架明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVipShelfDetails(Map<String, Object> queryMap);

	Map<String, Object> getShelfAccountByPoAndCommbarcode(Map<String, Object> queryMap);

	/**
	 * 根据po查出对应的上架总量信息（新版：购销实销实接）
	 * @param merchantId 
	 * @param poNo
	 * @return
	 */
	List<Map<String, Object>> getNewShelfAccountByPo(Map<String, Object> shelfAccountQueryMap);

	Map<String, Object> getNewShelfAccountByPoAndCommbarcode(Map<String, Object> queryMap);

	/**
	 * 根据po、标准条码、sku,获取销售上架明细(实销实结)
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getNewVipShelfDetails(Map<String, Object> queryMap);
		










}
