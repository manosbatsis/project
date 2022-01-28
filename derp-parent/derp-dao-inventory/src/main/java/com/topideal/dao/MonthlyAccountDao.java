package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.MonthlyAccountDTO;
import com.topideal.entity.vo.MonthlyAccountModel;



/**
 * 月结账单 dao
 * @author lian_
 *
 */
public interface MonthlyAccountDao extends BaseDao<MonthlyAccountModel>{
	


	  /**
     *  用于库存新增扣减接口 根据归属月份查询未结转的账单
     * @param monthlyAccountModel
     * @return
     * @throws Exception
     */
	List<MonthlyAccountModel> getMonthlyAccountListByMonth(MonthlyAccountModel monthlyAccountModel)throws Exception;

	
	
	 /**
     * 导出月结账单表头
     * @param monthlyAccountModel
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> exportMonthlyAccountMap (MonthlyAccountModel monthlyAccountModel)throws SQLException;

    
    /**
     * 根据条件获取表头数据
     * @param monthlyAccountModel
     * @return
     * @throws SQLException
     */
    List<MonthlyAccountModel>  getMonthlyAccount(MonthlyAccountModel monthlyAccountModel)throws SQLException;

    /**
	 * 获取上个月的 月结数量大于0的商家和仓库
	 * @param lastMonth
	 * @return
	 */
	List<MonthlyAccountModel>getAllMonthlyMerchantOrDepotByTime(String lastMonth)throws Exception;
	
	/**
	 * 获取本月之前的月结账单
	 * @param monthlyAccountModel
	 * @return
	 * @throws Exception
	 */
	List<MonthlyAccountModel> getBeforeMonthMonthlyAccount(MonthlyAccountModel monthlyAccountModel)throws Exception;
	
	/**
     * 分页查询数据DTO
     * @return
     */
	MonthlyAccountDTO searchDTOByPage(MonthlyAccountDTO monthlyAccountModel)throws Exception;
	
	/**
     * 根据id查询数据DTO
     * @return
     */
	MonthlyAccountDTO searchDTOById(Long id)throws Exception;

	/**
	 * 查询月结记录，按照结转月份升序排序
	 * @return
	 */
	List<MonthlyAccountModel> listOrderBySettlementMonth(MonthlyAccountModel monthlyAccountModel)throws SQLException;
	
	// 查询之前是否有过月结
	int getAfterMonthlyAccountCount(MonthlyAccountModel monthlyAccountModel)throws SQLException;

	int countByMonthlyAccount(MonthlyAccountModel monthlyAccountModel) throws SQLException;
}

