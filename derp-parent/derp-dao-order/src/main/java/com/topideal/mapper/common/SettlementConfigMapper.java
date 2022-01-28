package com.topideal.mapper.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.SettlementConfigDTO;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.common.SettlementModuleRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SettlementConfigMapper extends BaseMapper<SettlementConfigModel> {

	/**
	 * 分页查询
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<SettlementConfigDTO> getSettlementListByPage(SettlementConfigDTO dto) throws SQLException;

	/**
	 * 分页查询
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<SettlementConfigDTO> getSettlementListByModuleTypeByPage(SettlementConfigDTO dto) throws SQLException;

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

	/**
	 * 修改
	 * @param model
	 */
	void  modifySettlementConfig(SettlementConfigModel model);

	/**
	 * 根据指定客户满足条件的费项明细
	 * @param dto
	 * @return
	 */
	SettlementConfigDTO getDetailByCustomer(SettlementConfigDTO dto) throws SQLException;
}
