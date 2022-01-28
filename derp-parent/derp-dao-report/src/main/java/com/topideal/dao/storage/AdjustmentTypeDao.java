package com.topideal.dao.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.storage.AdjustmentTypeModel;

/**
 * 类型调整 dao
 * @author lian_
 */
public interface AdjustmentTypeDao extends BaseDao<AdjustmentTypeModel> {
		



	/**
	 * 报表api 根据 商家开始时间结束时间分页查询类型调整单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getListPage(Long merchantId, String startTime, String endTime, Integer startIndex, Integer pageSize, String InventoryType)throws SQLException;
	
	/**
	 * 报表api 根据 商家开始时间结束时间 查询类型调整单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(Long merchantId, String startTime, String endTime, String InventoryType);

	/**
	 * 根据创建修改时间查询出库单
	 * */
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap);
	/**
	 * 根据创建修改时间查询出库单统计数量
	 * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap);
	/**
	 * 根据出库单号查询出库单商品-货号变更、效期调整
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes);
	/**
	 * 根据出库单查询商品批次-货号变更、效期调整
	 * */
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap);
	/**
	 * 根据出库单号查询出库单商品-好坏品互转出、大货理货出
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes14(List<String> codes);
	/**
	 * 根据出库单查询商品批次-好坏品互转出、大货理货出
	 * */
	public List<Map<String, Object>> getItemBatchByCode14(Map<String, Object> paramMap);

}
