package com.topideal.dao.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.SettlementConfigDTO;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.common.SettlementModuleRelModel;


public interface SettlementConfigDao extends BaseDao<SettlementConfigModel>{
		
	/**
	 * 分页查询
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SettlementConfigDTO getSettlementListByPage(SettlementConfigDTO dto) throws SQLException;



	/**
	 * 根据适用模块获取费项配置列表分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SettlementConfigDTO getSettlementListByModuleTypePage(SettlementConfigDTO dto) throws SQLException;

	/**
	 * 查询费项配置下拉列表
	 * @param
	 * */
	List<SelectBean> getSelectBean(SettlementConfigDTO dto) throws SQLException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SettlementConfigDTO searchDetail(SettlementConfigDTO dto) throws SQLException;
	
	/**
	 * 导出费用项
	 * @param id
	 * @return
	 */
	List<SettlementConfigDTO> exportSettlementList(SettlementConfigDTO dto) throws SQLException;


	void update(SettlementConfigModel model);

	/**
	 * 根据指定客户满足条件的费项明细
	 * @param dto
	 * @return
	 */
	SettlementConfigDTO getDetailByCustomer(SettlementConfigDTO dto) throws SQLException;
}
