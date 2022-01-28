package com.topideal.service.base;


import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.base.ApiSecretConfigDTO;
import com.topideal.entity.vo.base.ApiSecretConfigModel;

/**
 * 接口配置
 * @author zhanghx
 */
public interface ApiSecretConfigService {

	/**
	 * 列表（分页）
	 * @param model 
	 * @return
	 */
	ApiSecretConfigDTO getListByPage(ApiSecretConfigDTO dto) throws SQLException;
	
	/**
	 * 新增
	 * @param model 
	 * @return
	 */
	boolean saveApiSecret(ApiSecretConfigModel model) throws Exception;
	
	/**
	 * 编辑
	 * @param model 
	 * @return
	 */
	boolean modify(ApiSecretConfigModel model) throws SQLException;
	/**
	 * 修改
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	boolean modifyAuditApi(ApiSecretConfigModel model) throws SQLException;
	
	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	ApiSecretConfigDTO getDetails(Long id) throws SQLException;
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	boolean delApiSecretConfig(List<Long> list) throws SQLException;
	
}

