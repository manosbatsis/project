package com.topideal.mapper.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.storage.TakesStockResultsModel;

/**
 * 盘点结果表
 * @author zhanghx
 */
@MyBatisRepository
public interface TakesStockResultsMapper extends BaseMapper<TakesStockResultsModel> {
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询盘点结果单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getListPage(@Param("merchantId") Long merchantId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize)throws SQLException;
	/**
	 * 报表api 根据 商家开始时间结束时间查询盘点结果单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(@Param("merchantId") Long merchantId, @Param("startTime") String startTime, @Param("endTime") String endTime);
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
	 * 唯品核销获取盘盈盘亏数
	 * @param queryMap
	 * @return
	 */
	Integer getStockAccount(Map<String, Object> queryMap);
	
	/**
	 * 获取唯品盘点结果明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVipStockResultsDetails(Map<String, Object> queryMap);
	
	/**
	 * 唯品自动校验获取系统盘点结果数量
	 * @param queryMap
	 * @return
	 */
	Integer getStockSystemAccount(Map<String, Object> queryMap);
}

