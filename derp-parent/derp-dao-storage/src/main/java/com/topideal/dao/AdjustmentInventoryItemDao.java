package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.vo.AdjustmentInventoryItemModel;
import org.apache.ibatis.annotations.Param;

/**
 * 库存调整详情表
 * @author lian_
 *
 */
public interface AdjustmentInventoryItemDao extends BaseDao<AdjustmentInventoryItemModel>{
		

	public List<Map<String, Object>> getItemByInventoryIds(List<Long> inventoryIds);

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(Long id);

	public List<Map<String, Object>> getByInventoryIds(List<Long> inventoryIds);

	Long countNoExistBu(Long id) throws SQLException;
}

