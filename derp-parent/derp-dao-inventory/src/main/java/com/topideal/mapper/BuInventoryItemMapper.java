package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.BuInventoryItemDTO;
import com.topideal.entity.vo.BuInventoryItemModel;


public interface BuInventoryItemMapper extends BaseMapper<BuInventoryItemModel> {

	/**
	 * 删除 该商家 仓库 事业部 月份 的事业部库存表体
	 * @return
	 */
	int delBuInventoryItem(Map<String, Object> map) throws SQLException;
	/**
	 * 获取事业部库存表体信息
	 * @return
	 */
	List<BuInventoryItemDTO> getBuInventoryItem(BuInventoryItemDTO dto)throws SQLException;
}
