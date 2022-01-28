package com.topideal.order.service.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.common.SettlementConfigDTO;
import com.topideal.entity.vo.common.SettlementConfigModel;

import net.sf.json.JSONArray;

public interface SettlementConfigService {
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public SettlementConfigDTO getSettlementListByPage(SettlementConfigDTO dto) throws SQLException;

	/**
	 * 查询单个
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SettlementConfigDTO searchDetail(Long id) throws SQLException;
	/**
	 * 新增
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> saveSettlement(String type,String module,SettlementConfigModel model,JSONArray customerIdArr) throws Exception;
	/**
	 * 修改表体
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object>  modifySettlement(String type,String module,SettlementConfigModel model,JSONArray customerIdArr,String oldProjectName) throws Exception;
	
	/**
	 * 获取父类下拉框
	 * @return
	 * @throws Exception
	 */
	List <SettlementConfigModel> getParentProjectNameList(SettlementConfigModel model)throws Exception;

	/**
	 * 根据适用模块获取费项配置列表分页
	 */
	SettlementConfigDTO getSettlementListByModuleTypePage(SettlementConfigDTO dto) throws SQLException;

	/**
	 * 查询费项配置下拉列表
	 * @param
	 * */
	List<SelectBean> getSelectBean(SettlementConfigDTO dto) throws SQLException;
	/**	
	 * 根据条件获取下拉框
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<SettlementConfigModel> getByModelList(SettlementConfigModel model) throws SQLException;
	
	/**
	 * 导出费用项
	 * @param id
	 * @return
	 */
	List<SettlementConfigDTO> exportSettlementList(SettlementConfigDTO dto) throws SQLException;
	
	/**
	 * 启用禁用
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean isOrNotEnable(SettlementConfigModel model)throws SQLException;

}
