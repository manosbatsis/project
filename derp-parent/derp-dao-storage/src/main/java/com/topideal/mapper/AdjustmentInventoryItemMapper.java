package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.AdjustmentInventoryItemModel;

/**
 * 库存调整详情表
 * @author lian_
 *
 */
@MyBatisRepository
public interface AdjustmentInventoryItemMapper extends BaseMapper<AdjustmentInventoryItemModel> {

	public List<Map<String, Object>> getItemByInventoryIds(List<Long> inventoryIds);

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);

	public List<Map<String, Object>> getByInventoryIds(List<Long> inventoryIds);

	Long countNoExistBu(@Param("id") Long id) throws SQLException;
	
}

