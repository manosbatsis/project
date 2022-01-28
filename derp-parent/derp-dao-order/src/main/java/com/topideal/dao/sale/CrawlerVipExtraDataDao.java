package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.CrawlerVipExtraDataModel;


public interface CrawlerVipExtraDataDao extends BaseDao<CrawlerVipExtraDataModel> {

	Integer batchSave(List<CrawlerVipExtraDataModel> saveList);
		
	// 分组获取平台费用单根据 商家,客户,费用类型,币种
	List<Map<String,Object>>getGroupCrawlerVipExtra(Map<String, Object> map)throws SQLException;

	// 分页获取平台费用单数据
	//List<CrawlerVipExtraDataModel>getCrawlerVipExtraDatailList(Map<String, Object> map)throws SQLException;
	
	// 获取平台费用单数据 按货号分组
	List<Map<String,Object>>getGroupByGoodNoCrawlerVipExtra(Map<String, Object> map)throws SQLException;
	//获取parmMap平台费用单 判断哪些sku没有配置爬虫商品配置
	List<Map<String,Object>>getGoodNoCrawlerVipExtra(Map<String, Object> map)throws SQLException;
	// 修改为已使用
	int updateCrawlerVipExtra(CrawlerVipExtraDataModel model)throws SQLException;

	/***
	 * 获取平台结算单指定类型汇总明细
	 * @param itemQueryMap
	 * @return 
	 */
	List<CrawlerVipExtraDataModel> getPlatformStatementData(Map<String, Object> itemQueryMap);

	/**
	 * 获取平台结算单金额和币种
	 * @param amountAndCurrencyMap
	 * @return
	 */
	CrawlerVipExtraDataModel getPlatformStatementAmountAndCurrency(Map<String, Object> amountAndCurrencyMap);




}
