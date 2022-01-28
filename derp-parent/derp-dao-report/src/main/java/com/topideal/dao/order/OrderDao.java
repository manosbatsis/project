package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.BrandSaleDTO;
import com.topideal.entity.dto.OrderDTO;
import com.topideal.entity.vo.order.OrderModel;

/**
 * 电商订单表 dao
 * @author zhanghx
 */
public interface OrderDao extends BaseDao<OrderModel> {
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
	 * 报表api 根据 商家开始时间结束时间分页查询电商订单
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
	 * 报表api 根据 商家开始时间结束时间 查询电商订单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(Long merchantId, String startTime, String endTime);

	/**
	 * @Description 统计指定业务模式下的电商订单的所有客户的销售数量
	 */
	public List<BrandSaleDTO> countOrderByMerchantIdAndShopType(Long merchantId, String shopType, String deliverDate);

	/**
	 * @Description 统计指定业务模式下的电商订单的所有客户销售top 10 的品牌
	 */
	public List<BrandSaleDTO> getOrderTop10ByBrand(Long merchantId, String shopType, String deliverDate);
	
	/**
	 * 报表API获取电商订单
	 * @param queryMap
	 * @return
	 */
	public List<OrderDTO> getRepotApiList(Map<String, Object> queryMap);
	
	public Integer getRepotApiListCount(Map<String, Object> queryMap);
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String, Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String, Object> map);
	
	/**
	 * 本月出库的电商订单
	 * @return
	 */
	public List<OrderModel> getCurrentMonthOdepotList();
	/**查询时间范围内有修改的电商订单号
	 * */
	List<String> getOrderCodeList(Map<String,Object> map);
	/**根据单号查询订单
	 * */
	List<Map<String,Object>> getOrderByCodeList(Map<String,Object> map);
	/**根据单号查询订单表体
	 * */
	List<Map<String,Object>> getOrderItemByCodeList(Map<String,Object> map);
}

