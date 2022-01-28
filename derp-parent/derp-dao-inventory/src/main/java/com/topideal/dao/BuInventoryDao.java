package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.BuInventoryDTO;
import com.topideal.entity.vo.BuInventoryModel;


public interface BuInventoryDao extends BaseDao<BuInventoryModel>{
		

	 /**
	  * 获取的 按商家 仓库事业库存分组的统计量
	  * @param map
	  * @return
	  * @throws SQLException
	  */
	List<Map<String, Object>> getMonthBuInventory(Map<String, Object> map) throws SQLException;
	
	/**
	 * 查询 按商家 仓库事业库存 之前月份有没有数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int getBeforeMonthBuInventory(Map<String, Object> map) throws SQLException;
	
	 /**
	  * 获取事业库存分页  
	  * @param model
	  * @return
	  * @throws SQLException
	  */
	BuInventoryDTO	 getListBuInventoryByPage(BuInventoryDTO model) throws SQLException;
	
	/**
	 * 事业部库存导出
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> exportBuInventory(BuInventoryDTO model) throws SQLException;
	
	/**
	 * 获取事业部库分组数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getBuInventoryGruop(Map<String, Object> map) throws SQLException;
	/**
	 * 删除 该商家 仓库 事业部 月份 的事业部库存
	 * @return
	 */
	int delBuInventory(Map<String, Object> map) throws SQLException;


	/**
	 * @Author Chen Yiluan
	 * @Description 根据公司主体统计当前月份事业部的库存占比
	 * @Date 2020/5/7
	 * @Param
	 * @return
	 */
	List<Map<String, Object>> countByMonthAndMerchant(BuInventoryDTO model) throws SQLException;

	/**
	 * @Author Chen Yiluan
	 * @Description 根据公司主体统计当前月份各事业部的仓库库存量
	 * @Date 2020/5/7
	 * @Param
	 * @return
	 */
	List<Map<String, Object>> countBuInventoryByBuId(BuInventoryDTO model) throws SQLException;

	/**
	 * @Author Chen Yiluan
	 * @Description 根据公司主体统计当前月份各仓库的库存量
	 * @Date 2020/5/7
	 * @Param
	 * @return
	 */
	List<Map<String, Object>> countBuInventoryByDepotId(BuInventoryDTO model) throws SQLException;
    /**
     *  导出事业部库存表体
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String, Object>>   exportBuInventoryItem(BuInventoryDTO model)throws Exception;

}
