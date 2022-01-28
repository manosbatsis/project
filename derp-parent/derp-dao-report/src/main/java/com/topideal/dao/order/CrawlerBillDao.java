package com.topideal.dao.order;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.CrawlerBillModel;

/**
 * 爬虫账单
 * @author lian_
 *
 */
public interface CrawlerBillDao extends BaseDao<CrawlerBillModel> {

	
	/**
	 * 根据条件搜索
	 * @param cb 
	 * @return
	 * @throws SQLException 
	 */
	List<CrawlerBillModel> listBillModel() throws SQLException;
		
	

	
	
	/**
	 * 定时器:查询昨天之前的所有的类型为1的爬虫账单
	 * @param cb
	 * @return
	 */
	List<CrawlerBillModel> timeBeforeYesterdayCrawler(CrawlerBillModel cb) throws SQLException;


	/**
	 * 根据billDate时间范围查询类型为云集的爬虫表
	 * @param model
	 * @return
	 */
	/*List<CrawlerBillModel> synYunJiByDate(CrawlerBillModel model) throws SQLException;*/

	/**
	 * 根据po、sku（单个/多个）去爬虫账单表账单总量
	 * @param crawlerQueryMap
	 * @return
	 */
	List<Map<String, Object>> getBillByPoAndSku(Map<String, Object> crawlerQueryMap);


	/**
	 * 查询本po号、sku的所有爬虫账单明细
	 * @param queryMap
	 * @return
	 */
	List<CrawlerBillModel> listByPoAndSkus(Map<String, Object> queryMap);

	/**
	 * 根据账单编号获取账单类型
	 * @param vipBillCode
	 * @return
	 */
	String getBillTypeByBillCode(String vipBillCode);


	String getCustomerNameByPO(String poNo);

	/**
	 * 获取唯品自动校验爬虫账单列表
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getVipAutoVeriList(Map<String, Object> params);


	/***
	 * 修改唯品校验状态
	 * @param updateMap
	 */
	int updateVipVeriState(Map<String, Object> updateMap);

	/**
	 * 获取爬虫账单国检抽样、报废、盘亏数量
	 * @param queryCrawlerMap
	 * @return
	 */
	Integer getDecreaseNum(Map<String, Object> queryCrawlerMap);


	int countByPoAndSkus(Map<String, Object> queryMap);
	
	/**
	 * 获取当年云集的 账单数据
	 * @param cb
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getYearYunjiCrawlerBill(CrawlerBillModel model) throws SQLException;
	/**
	 * 获取当月云集的账单数据
	 * @param cb
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getMonthYunjiCrawlerBill(CrawlerBillModel model) throws SQLException;
	/**
	 * 获取当日云集的账单数据
	 * @param cb
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getDayYunjiCrawlerBill(CrawlerBillModel model) throws SQLException;




	/**
	 * 获取未校验爬虫账单
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getUnVeriList(Map<String, Object> params);
}

