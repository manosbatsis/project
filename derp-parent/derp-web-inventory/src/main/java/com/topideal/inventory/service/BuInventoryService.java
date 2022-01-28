package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.BuInventoryDTO;
import com.topideal.entity.dto.BuInventoryItemDTO;
import com.topideal.entity.vo.BuInventoryModel;
/**
 * 库存管理 -批次库存明细-service实现类
 */
public interface BuInventoryService {
	
	/**
	 * 获取事业部库存表体信息
	 * @return
	 */
	List<BuInventoryItemDTO> getBuInventoryItem(BuInventoryItemDTO dto)throws SQLException;
	
	/**
	 * 批次事业部库存（分页）
	 * @param model 
	 * @return
	 */
	BuInventoryDTO listBuInventory(User user,BuInventoryDTO model) throws SQLException;
	
	

    /**
     *  导出事业部库存
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String, Object>>   exportBuInventory(User user,BuInventoryDTO model)throws Exception;
    /**
     *  导出事业部库存表体
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String, Object>>   exportBuInventoryItem(User user,BuInventoryDTO model)throws Exception;
	

	/**
	 * @Author Chen Yiluan
	 * @Description 根据公司主体统计当前月份各事业部的库存占比
	 * @Date 2020/5/7
	 * @Param
	 * @return
	 */
	List<Map<String, Object>> countByMonthAndMerchant(BuInventoryDTO model) throws SQLException;

	/**
	 * @Author Chen Yiluan
	 * @Description 根据公司主体统计当前月份各事业部的仓库库存占比
	 * @Date 2020/5/7
	 * @Param
	 * @return
	 */
	List<Map<String, Object>> countBuInventoryRate(BuInventoryDTO model) throws SQLException;



	
}
