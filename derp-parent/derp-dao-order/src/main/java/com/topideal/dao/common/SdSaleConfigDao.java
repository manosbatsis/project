package com.topideal.dao.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.vo.common.SdSaleConfigModel;


public interface SdSaleConfigDao extends BaseDao<SdSaleConfigModel>{
		
	SdSaleConfigDTO listDTOByPage(SdSaleConfigDTO dto) throws SQLException;
	
	SdSaleConfigDTO queryExistConfig(Map<String,Object> map) throws SQLException;
	
	List<SdSaleConfigDTO> listDTO(SdSaleConfigDTO dto) throws SQLException;
}
