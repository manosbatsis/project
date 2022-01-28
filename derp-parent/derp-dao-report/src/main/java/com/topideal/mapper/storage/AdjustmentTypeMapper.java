package com.topideal.mapper.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.storage.AdjustmentTypeModel;

/**
 * 类型调整 mapper
 * @author lian_
 */
@MyBatisRepository
public interface AdjustmentTypeMapper extends BaseMapper<AdjustmentTypeModel> {

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
	List<Map<String, Object>> getListPage(@Param("merchantId") Long merchantId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize, @Param("type") String type)throws SQLException;
	/**
	 * 报表api 根据 商家开始时间结束时间查询类型调整单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(@Param("merchantId") Long merchantId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("type") String type);
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
