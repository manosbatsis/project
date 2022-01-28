package com.topideal.mapper.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.storage.AdjustmentInventoryModel;

/**
 * 库存调整
 * @author zhanghx	
 */
@MyBatisRepository
public interface AdjustmentInventoryMapper extends BaseMapper<AdjustmentInventoryModel> {
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
	 * 报表api 根据 商家开始时间结束时间分页类型调整单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getListPage(@Param("merchantId") Long merchantId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize, @Param("inventoryType") String inventoryType)throws SQLException;
	/**
	 * 报表api 根据 商家开始时间结束时间查询类型调整单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(@Param("merchantId") Long merchantId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("inventoryType") String inventoryType);


	/**
	 * 事业部务经销存总表 本月销毁数量
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuAdjustmentInventoryDestroyNum(Map<String, Object> paramMap);
	

	/**
	 * 获取(财务经销存)销毁明细表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuFinanceDestroy(Map<String, Object> map)throws SQLException;

}

