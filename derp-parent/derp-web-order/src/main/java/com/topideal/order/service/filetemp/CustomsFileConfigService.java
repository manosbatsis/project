package com.topideal.order.service.filetemp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.CustomsFileConfigDTO;
import com.topideal.entity.dto.common.CustomsFileDepotRelDTO;
import com.topideal.entity.vo.common.CustomsFileConfigModel;

public interface CustomsFileConfigService {

	CustomsFileConfigDTO listDTOByPage(CustomsFileConfigDTO dto) throws SQLException;
	
	CustomsFileConfigModel searchById(Long id) throws SQLException;
	
	List<CustomsFileDepotRelDTO> listDepotRel(Long fileId) throws SQLException;
	
	Map<String, String> saveOrModity(String json,User user) throws Exception;
	
	void delCustomsFileConfig(List<Long> ids) throws SQLException;	
	
	Map<String, Object> getExportTemplate(String json) throws Exception;
}
