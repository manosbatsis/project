package com.topideal.order.service.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.SdTypeConfigDTO;
import com.topideal.entity.vo.common.SdTypeConfigModel;

public interface SdTypeService {

	SdTypeConfigDTO getSdTypeConfigListByPage(SdTypeConfigDTO dto);

	void saveOrModify(SdTypeConfigModel model, User user) throws SQLException, Exception;

	SdTypeConfigModel searchByModel(SdTypeConfigModel model) throws SQLException;

	List<SdTypeConfigModel> getList(SdTypeConfigModel model) throws SQLException;

}
