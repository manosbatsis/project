package com.topideal.dao.inventory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.inventory.InitInventoryModel;

/**
 * 期初库存 dao
 * @author lian_
 *
 */
public interface InitInventoryDao extends BaseDao<InitInventoryModel> {
		

	
	/**
	 * 商家、仓库、货号的期初库存量
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getGoodsNoInitSum(Map<String, Object> paramMap) throws SQLException;
	
}

