package com.topideal.inventory.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.MonthlyAccountItemDTO;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;

/**
 * 库存管理 -月结账单商品明细-service实现类
 */
public interface MonthlyAccountItemService {

	/**
	 * 月结账单商品明细列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	MonthlyAccountItemModel listMonthlyAccountItem(MonthlyAccountItemModel model) throws SQLException;

	/**
	 * 根据主键id 查询月库存转结详情
	 * 
	 * @param model
	 * @return
	 */
	List<MonthlyAccountItemDTO> searchlist(MonthlyAccountItemDTO model,Long depotId, Timestamp firstDate) throws SQLException;

	/**
	 * 按计算库存量结转
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean confirmMonthlySurplusNum(MonthlyAccountItemModel model) throws SQLException;
	
	/**
	 * 校验月结现存量和 库存余量是否相等
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<MonthlyAccountItemModel>  checkMonthlySurplusNum(MonthlyAccountItemModel model) throws SQLException;

	/**
	 * 按计算仓库现存量结转（生成库存调整单数据）
	 * @param firstDate 
	 * @param depotId 
	 * @param model
	 * @return
	 * @throws SQLException
	 */

	List<Map<String,Object>> createMonthlyAvailableNum(Long depotId, Timestamp firstDate, MonthlyAccountItemModel model) throws SQLException;

	/**
	 * 按现存量结转 (修改月结状态,同时更新期末库存值)
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */

	boolean  confirmMonthlyAvailableNum(MonthlyAccountItemModel model) throws SQLException;
	
	
	 /**
     * 导出月结账单表体
     * @param monthlyAccountModel
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> exportMonthlyAccountItemMap (MonthlyAccountItemModel model)throws SQLException;

    /**
     * 刷新月结库存数据
     * @param itemModelList
     * @return
     * @throws Exception
     */
    public void refreshMonthlyBill(MonthlyAccountModel monthlyAccountModel ,Timestamp  endDate,Long id)throws Exception;
    
    
}
