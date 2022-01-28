package com.topideal.mapper.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.vo.common.SdSaleConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SdSaleConfigMapper extends BaseMapper<SdSaleConfigModel> {
	
	PageDataModel<SdSaleConfigDTO> listDTOByPage(SdSaleConfigDTO dto) throws SQLException;
	
	SdSaleConfigDTO queryExistConfig(Map<String,Object> map) throws SQLException;
	
	List<SdSaleConfigDTO> listDTO(SdSaleConfigDTO dto) throws SQLException;
}
