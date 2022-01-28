package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.CrawlerBillModel;

/**
 * 爬虫账单
 * @author lian_
 *
 */
@MyBatisRepository
public interface CrawlerBillMapper extends BaseMapper<CrawlerBillModel> {

	List<CrawlerBillModel> listBillModel();


	
	
	/**
	 * 定时器:查询昨天之前的所有的类型为1的爬虫账单
	 * @param model
	 * @return
	 */
	List<CrawlerBillModel> timeBeforeYesterdayCrawler(CrawlerBillModel model);

	/**
	 * 查询爬虫账单表核销表获取状态为未获取的账单
	 * @param poNo 
	 * @param poNo 
	 * @return
	 */
	List<Map<String , Object>> getNotAvailableBill(@Param("merchantId") Long merchantId, @Param("poNo") String poNo);

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
	 * 根据账单编码获取账单类型
	 * @param vipBillCode
	 * @return
	 */
	String getBillTypeByBillCode(@Param("vipBillCode") String vipBillCode);

	/**
	 * 根据PO号获取客户名
	 * @param poNo
	 * @return
	 */
	String getCustomerNameByPO(@Param("poNo") String poNo);

	/**
	 * 获取唯品自动校验爬虫账单列表
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getVipAutoVeriList(Map<String, Object> params);

	int updateVipVeriState(Map<String, Object> updateMap);

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

