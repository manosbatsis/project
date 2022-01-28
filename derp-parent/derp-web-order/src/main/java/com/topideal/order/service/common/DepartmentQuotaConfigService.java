package com.topideal.order.service.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.DepartmentQuatoDTO;
import com.topideal.entity.dto.common.DepartmentQuatoItemDTO;

public interface DepartmentQuotaConfigService {

	DepartmentQuatoDTO getListByPage(DepartmentQuatoDTO dto, User user);

	Long saveOrUpdateDepartmentQuotaConfig(DepartmentQuatoDTO dto, User user) throws SQLException;

	DepartmentQuatoDTO getDepartmentQuotaConfigById(Long id) throws SQLException;

	void auditDepartmentQuotaConfig(Long id, User user) throws SQLException;

	void delDepartmentQuotaConfig(String ids) throws SQLException;

	void updateDepartmentQuota(DepartmentQuatoDTO dto, User user) throws SQLException;

	List<DepartmentQuatoItemDTO> getItemList(Long departmentQuatoId) throws SQLException;

}
