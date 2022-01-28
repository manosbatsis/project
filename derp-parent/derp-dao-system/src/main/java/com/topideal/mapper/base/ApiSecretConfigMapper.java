package com.topideal.mapper.base;

import java.sql.SQLException;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.ApiSecretConfigDTO;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.mapper.BaseMapper;

/**
 * api密钥配置 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface ApiSecretConfigMapper extends BaseMapper<ApiSecretConfigModel> {

	/**
	  * 分页
	  * @param model
	  * @return
	  * @throws SQLException
	  */
	 PageDataModel<ApiSecretConfigDTO> getListByPage(ApiSecretConfigDTO dto) throws SQLException;

	ApiSecretConfigDTO searchDTOById(Long id);

}

