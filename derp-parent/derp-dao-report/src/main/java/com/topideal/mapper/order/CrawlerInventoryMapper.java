package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.CrawlerInventoryModel;


@MyBatisRepository
public interface CrawlerInventoryMapper extends BaseMapper<CrawlerInventoryModel> {

	List<CrawlerInventoryModel> listInventory(CrawlerInventoryModel model);


	/**
	 * 根据货号集合、商家id、日期 统计该日的库存量
	 * @param model
	 * @return
	 */
	CrawlerInventoryModel countInventoryNum(CrawlerInventoryModel model);
	/**
	 * 根据商家id 货号 和日期查询爬虫库存
	 * @param model
	 * @return
	 */
	List<CrawlerInventoryModel> getListByModel(CrawlerInventoryModel model);

	
	/**
	 * 统计全年的库存商品
	 * @param model
	 * @return
	 */
	List<Map<String, Object>> byYearTimeCrawlerInventory(@Param("startTimeyear") String startTimeyear, @Param("endTimeyear") String endTimeyear);
	/**
	 * 获取当日云集的库存数据(保税仓)
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getDayCrawlerInventory(CrawlerInventoryModel model) throws SQLException;
	
	/**
	 * 获取当日云集的库存数据(退货仓)
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getDayReturnbinInventory(CrawlerInventoryModel model) throws SQLException;
	
	

}

