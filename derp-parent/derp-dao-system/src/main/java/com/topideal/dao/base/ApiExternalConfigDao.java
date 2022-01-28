package com.topideal.dao.base;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;
import com.topideal.entity.vo.base.ApiExternalConfigModel;

import java.sql.SQLException;

/**
 * 对外接口配置表 dao
 * @author lian_
 */
public interface ApiExternalConfigDao extends BaseDao<ApiExternalConfigModel> {
		

	ApiExternalConfigDTO getListByPage(ApiExternalConfigDTO dto) throws SQLException;

	ApiExternalConfigDTO searchDTOById(Long id);









}
