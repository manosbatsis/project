package com.topideal.dao.base;

import java.sql.SQLException;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.ApiSecretConfigDTO;
import com.topideal.entity.vo.base.ApiSecretConfigModel;

/**
 * api密钥配置 dao
 * @author lian_
 *
 */
public interface ApiSecretConfigDao extends BaseDao<ApiSecretConfigModel>{
		

	/**
	  * 分页查询
	  * @param model
	  * @return
	  * @throws SQLException
	  */
	ApiSecretConfigDTO getListByPage(ApiSecretConfigDTO dto) throws SQLException;

	ApiSecretConfigDTO searchDTOById(Long id);






}

