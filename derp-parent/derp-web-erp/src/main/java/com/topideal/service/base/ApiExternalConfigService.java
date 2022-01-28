package com.topideal.service.base;


import com.topideal.entity.dto.base.ApiExternalConfigDTO;
import com.topideal.entity.vo.base.ApiExternalConfigModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 对外接口配置
 * @author lian_
 *
 */
public interface ApiExternalConfigService {

	/**
	 * 列表
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	ApiExternalConfigDTO getListByPage(ApiExternalConfigDTO dto) throws SQLException;
	
	/**
	 * 新增
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean saveApiExternal(ApiExternalConfigModel model) throws Exception;
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 * @throws SQLException
	 * @throws Exception 
	 */
	boolean delApiExternal(List<Long> list) throws Exception;
	/**
	 * 编辑
	 * @param model 
	 * @return
	 */
	boolean modifyApiExternal(ApiExternalConfigModel model) throws Exception;

	/**
	 * 详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	ApiExternalConfigDTO getDetails(Long id) throws SQLException;
	
	/**
	 * 禁用启用
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean auditApiExternal(ApiExternalConfigModel model) throws SQLException;

}
