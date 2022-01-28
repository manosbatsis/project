package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;
import com.topideal.entity.vo.base.ApiExternalConfigModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;

/**
 * 对外接口配置表 maper
 * @author lian_
 */
@MyBatisRepository
public interface ApiExternalConfigMapper extends BaseMapper<ApiExternalConfigModel> {


	PageDataModel<ApiExternalConfigDTO> getListByPage(ApiExternalConfigDTO dto) throws SQLException;

	ApiExternalConfigDTO searchDTOById(Long id);

}
