package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.SaleShelfModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售上架信息表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleShelfMapper extends BaseMapper<SaleShelfModel> {

	/**
	 * 根据po查出对应的上架总量信息（旧版：代销）
	 * @param merchantId 
	 * @param poNo
	 * @return
	 */
	List<Map<String, Object>> getShelfAccountByPo(Map<String, Object> queryMap);

	/**
	 * 获取唯品PO上架明细
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

	List<Map<String, Object>> getNewVipShelfDetails(Map<String, Object> queryMap);



}
