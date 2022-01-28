package com.topideal.dao.inventory;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.ManageDepartmentInventoryDTO;
import com.topideal.entity.vo.inventory.BuInventoryModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface BuInventoryDao extends BaseDao<BuInventoryModel> {
		

	/**
	 * 获取事业部库存
	 * @param map
	 */
	List<Map<String, Object>> getBuDepotEndNumForMap(Map<String, Object> paramMap);

	/**
	 * 删除 该商家 仓库 事业部 月份 的事业部库存
	 * @return
	 */
	int delBuInventory(Map<String, Object> map) ;

	 /**
	  * 获取的 按商家 仓库事业库存分组的统计量
	  * @param map
	  * @return
	  * @throws SQLException
	  */
	List<Map<String, Object>> getBarcodeMonthBuInventory(Map<String, Object> map) throws SQLException;
	
	/**
	 * 查询 按商家 仓库事业库存 之前月份有没有数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int getBeforeMonthBarcodeCount(Map<String, Object> map) throws SQLException;

	/**
	 * 月结自动校验获取事业部库存
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getMonthlyAutoVeriListGroupByBarcode(Map<String, Object> params);

	/**
	 * 根据公司、仓库、货号查询最近日期有库存量的事业部
	 * @Param
	 * @return
	 */
	List<BuInventoryModel> getLatestInventory(BuInventoryModel buInventoryModel) throws SQLException;

	/**
	 * 异常邮件预计--获取库存为负数事业部库存
	 * @param queryMap
	 * @return
	 */
	List<BuInventoryModel> getNegativeNumList(Map<String, Object> queryMap);

	/**
	 * 根据公司、仓库、事业部、标准条码维度统计选择月份的事业部库存
	 * @Param
	 * @return
	 */
	List<BuInventoryModel> getBuInventoryByCommbarcode(String month, List<Long> buIds) throws SQLException;

	/**
	 * 部门库存统计（经营报表）
	 * @param paramMap
	 * @return
	 */
    List<ManageDepartmentInventoryDTO> getDepartmentInventoryStatistic(Map<String, Object> paramMap);
}
