package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.MonthlyAccountDTO;
import com.topideal.entity.vo.MonthlyAccountModel;
/**
 * 库存管理 -月库存转结-service实现类
 */
public interface MonthlyAccountService {
	// 修改成未结转状态
	Map<String, Object>updateNotSettlement(Long id)throws SQLException;
	
	/**
	 * 月库存转结列表（分页）
	 * @param model 
	 * @return
	 */
	MonthlyAccountModel listMonthlyAccount(MonthlyAccountModel model) throws SQLException;

	/**
	 * 月库存转结列表（分页）
	 * @param model 
	 * @return
	 */
	MonthlyAccountDTO listDTOMonthlyAccount(MonthlyAccountDTO model) throws Exception;
	/**
	 * 根据主键id 查询月库存转结详情
	 * @param model 
	 * @return
	 */
	MonthlyAccountModel	searchById(Long id) throws SQLException;
	
	/**
	 * 根据主键id 查询月库存转结详情DTO
	 * @param model 
	 * @return
	 */
	MonthlyAccountDTO searchDTOById(Long id) throws Exception;
	
	/**
	 * 更新月结账单状态和时间
	 * @param merchantInfoModel
	 * @return
	 * @throws Exception
	 */
	boolean updateMonthlyAccountState(MonthlyAccountModel model,Long userId,String name) throws Exception;
	
	
	  /**
     *  用于库存新增扣减接口 根据归属月份查询未结转的账单
     * @param monthlyAccountModel
     * @return
     * @throws Exception
     */
	List<MonthlyAccountModel> getMonthlyAccountListByMonth(MonthlyAccountModel monthlyAccountModel)throws Exception;

	/**
	 *  生成库存调整单
	 * @param monthlyAccountModel
	 * @param mapList
	 * @return
	 * @throws Exception
	 */
    String producedAdjustmentInventory(Long userId,String name, MonthlyAccountModel monthlyAccountModel,List<Map<String,Object>> mapList)throws Exception;
    
    /**
     * 导出月结账单表头
     * @param monthlyAccountModel
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> exportMonthlyAccountMap (MonthlyAccountModel monthlyAccountModel)throws Exception;

    List<MonthlyAccountModel> listByMonthlyAccount(MonthlyAccountModel monthlyAccountModel) throws SQLException;
    /**
     *新增月结表头
     * @param user
     * @param depotId
     * @param month
     * @return
     * @throws Exception
     */
    Map<String,Object> saveMonthlyAccount(User user,Long depotId,String month) throws Exception;


	int countByMonthlyAccount(MonthlyAccountModel monthlyAccountModel) throws SQLException;
}

