package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.CrawlerInventoryModel;


public interface CrawlerInventoryDao extends BaseDao<CrawlerInventoryModel> {
		

	/**
	 * 根据条件搜索
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<CrawlerInventoryModel> listInventory(CrawlerInventoryModel model) throws SQLException;


	/**
	 * 根据货号集合、商家id、日期 统计该日的库存量
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	CrawlerInventoryModel countInventoryNum(CrawlerInventoryModel model) throws SQLException;
	/**
	 * 根据商家id 货号 和日期查询爬虫库存
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<CrawlerInventoryModel> getListByModel(CrawlerInventoryModel model) throws SQLException;

	/**
	 * 统计全年的爬虫库存商品
	 */
	List<Map<String, Object>> byYearTimeCrawlerInventory(String startTimeyear, String endTimeyear)throws SQLException;

	/**
	 * 获取当日云集的库存数据 (保税仓)
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getDayCrawlerInventory(CrawlerInventoryModel model) throws SQLException;
	
	/**
	 * 获取当日云集的库存数据 (退货仓)
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getDayReturnbinInventory(CrawlerInventoryModel model) throws SQLException;
	
}

