package com.topideal.dao.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.storage.TakesStockResultsModel;

/**
 * 盘点结果表
 * @author zhanghx
 */
public interface TakesStockResultsDao extends BaseDao<TakesStockResultsModel> {
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询盘点结果
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getListPage(Long merchantId, String startTime, String endTime, Integer startIndex, Integer pageSize)throws SQLException;
	
	/**
	 * 报表api 根据 商家开始时间结束时间 查询盘点结果总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(Long merchantId, String startTime, String endTime);
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
	 * 唯品自动校验获取盘点结果数量
	 * @param queryMap
	 * @return
	 */
	Integer getStockSystemAccount(Map<String, Object> queryMap);

}

