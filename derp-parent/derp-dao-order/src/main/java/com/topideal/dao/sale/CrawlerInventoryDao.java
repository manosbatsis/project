package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.CrawlerInventoryModel;


public interface CrawlerInventoryDao extends BaseDao<CrawlerInventoryModel>{
		
	/**
	 * 根据条件获取云集库存数据，多条相同的sku获取最后一条记录
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<CrawlerInventoryModel> getCrawlerInventoryByModel(CrawlerInventoryModel model)throws SQLException;









}

