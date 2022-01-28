package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.CrawlerInventoryModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface CrawlerInventoryMapper extends BaseMapper<CrawlerInventoryModel> {

	/**
	 * 根据条件获取云集库存数据，多条相同的sku获取最后一条记录
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<CrawlerInventoryModel> getCrawlerInventoryByModel(CrawlerInventoryModel model)throws SQLException;
}

