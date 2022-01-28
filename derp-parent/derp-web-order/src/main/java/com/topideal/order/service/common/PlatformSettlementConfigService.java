package com.topideal.order.service.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.common.PlatformSettlementConfigDTO;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;

public interface PlatformSettlementConfigService {
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public PlatformSettlementConfigDTO getPlatSettlementListByPage(PlatformSettlementConfigDTO dto) throws SQLException;

	/**
	 * 新增
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> saveSettlement(PlatformSettlementConfigModel model) throws Exception;

	/**
	 * 修改
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object>  modifySettlement(PlatformSettlementConfigModel model,String oldName) throws Exception;




	/**
	 * 导出费用项
	 * @param id
	 * @return
	 */
	List<PlatformSettlementConfigDTO> exportSettlementList(PlatformSettlementConfigDTO dto) throws SQLException;
	
	/**
	 * 启用禁用
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean isOrNotEnable(PlatformSettlementConfigModel model)throws SQLException;
	
	/**
	 * 查询详情
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PlatformSettlementConfigModel searchDetail(PlatformSettlementConfigModel model) throws SQLException;
	
    /**平台商品导入
     * */
    Map savePlatformMerchandiseImport(List<Map<String, String>> data,User user) throws Exception;


	/**
	 * 获取已有维护且启用状态的平台费项，取 “平台费项名称”字段值
	 * @return
	 */
	List<PlatformSettlementConfigDTO>  getSelectBean(String storePlatformCode);
}
