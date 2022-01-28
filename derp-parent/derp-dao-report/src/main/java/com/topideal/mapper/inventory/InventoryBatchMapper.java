package com.topideal.mapper.inventory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.inventory.InventoryBatchModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface InventoryBatchMapper extends BaseMapper<InventoryBatchModel> {


	/**
	 * 统计指定商家、仓库的库存批次明细
	 */
	public List<Map<String, Object>> getBatchNoDetails(Map<String, Object> map);
	/**
	 * 报表api 根据条件分页查询批次库存
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getRepotApiListPage(Map<String,Object> map) ;
	/**
	 * 报表api 根据条件查询批次库存总数
	 * @param map
	 * @return
	 */
	Integer getRepotApiCount(Map<String,Object> map);
}
