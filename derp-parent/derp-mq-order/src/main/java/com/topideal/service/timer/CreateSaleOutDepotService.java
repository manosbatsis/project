package com.topideal.service.timer;

import java.sql.SQLException;
import java.util.List;

/**
 * 根据爬虫账单生成销售出库清单
 */
public interface CreateSaleOutDepotService {
	/**
	 * 唯品的账单数据生成销售出库清单
	 * @return 
	 * @throws SQLException
	 * @throws Exception 
	 */
	public List<String> insertVIPSaleOutDepot(String json,String keys,String topics,String tags)throws SQLException, Exception;
	/**
	 * 云集的账单数据生成销售出库清单
	 * @return 
	 * @throws SQLException
	 * @throws Exception 
	 */
	public List<String> insertYunJiSaleOutDepot(String json,String keys,String topics,String tags)throws SQLException, Exception;
	/**
	 * 推送库存MQ
	 * @param invetAddOrSubList
	 * @throws Exception
	 */
	public void pushInventoryMQ(List<String> invetAddOrSubList) throws Exception;
}
