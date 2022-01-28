package com.topideal.dao.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.storage.AdjustmentTypeItemModel;

/**
 * 类型调整详情表 dao
 * @author lian_
 */
public interface AdjustmentTypeItemDao extends BaseDao<AdjustmentTypeItemModel> {
		


	/**
	 * 根据盘类型调整ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(List ids, String type)throws SQLException;







}
