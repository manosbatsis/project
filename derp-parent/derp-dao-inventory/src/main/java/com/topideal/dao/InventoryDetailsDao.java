package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryDetailsDTO;
import com.topideal.entity.vo.InventoryDetailsModel;

/**
 * 库存收发明细 dao
 * @author lian_
 *
 */
public interface InventoryDetailsDao extends BaseDao<InventoryDetailsModel>{
		


    /**
     * 导出商品收发明细
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String,Object>> exportInventoryDetailsMap(InventoryDetailsDTO model ) throws Exception;

	/**
	 *  分页查询商品收发明细
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	InventoryDetailsDTO getlistByPage(InventoryDetailsDTO model) throws SQLException;

	/**
	 * 根据货号、订单号统计收发明细
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> countDetailsByOrderNo(InventoryDetailsModel model) throws SQLException;
	/**
	 * 根据条件获取收发明细
	 * @param merchantId  商家id
	 * @param depotId  仓库id
	 * @param divergenceMonth  归属月份
	 * @param operateType  操作类型
	 * @return
	 */
	List<InventoryDetailsModel> getDetailsByParams(Long merchantId, Long depotId, String divergenceMonth, String operateType);
	
	/**
	 * 获取上个月的 所有加减明细商家和仓库
	 * @param lastMonth
	 * @return
	 */
	List<InventoryDetailsModel>getAllDetailMerchantOrDepotByTime(String lastMonth)throws Exception;
  	/**
	 * 获取本月 商家 仓库 事业部 的 加的明细 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getMonthBuAddOrSubInventory(Map<String, Object> map) throws SQLException;
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String,Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String,Object> map);
}

