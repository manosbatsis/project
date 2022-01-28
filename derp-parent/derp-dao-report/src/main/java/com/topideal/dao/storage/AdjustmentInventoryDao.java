package com.topideal.dao.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.storage.AdjustmentInventoryModel;

/**
 * 库存调整
 * @author zhanghx
 */
public interface AdjustmentInventoryDao extends BaseDao<AdjustmentInventoryModel> {
    
	/**
	 * 根据创建修改时间查询出库单
	 * */
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap);
	/**
	 * 根据创建修改时间查询出库单统计数量
	 * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap);
	/**
	 * 根据出库单号查询出库单商品
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes);
	/**
	 * 根据出库单查询商品批次
	 * */
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap);
	
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询库存调整单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getListPage(Long merchantId, String startTime, String endTime, Integer startIndex, Integer pageSize, String inventoryType)throws SQLException;
	
	/**
	 * 报表api 根据 商家开始时间结束时间 查询库存调整单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(Long merchantId, String startTime, String endTime, String inventoryType);
	

	/**
	 * 事业部务经销存总表 本月销毁数量
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuAdjustmentInventoryDestroyNum(Map<String, Object> paramMap);
	

	/**
	 * 获取(事业部财务经销存)销毁明细表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuFinanceDestroy(Map<String, Object> map)throws SQLException;

}

