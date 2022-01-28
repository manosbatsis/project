package com.topideal.dao.automatic;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.VipAutomaticVerificationModel;


public interface VipAutomaticVerificationDao extends BaseDao<VipAutomaticVerificationModel> {

	VipAutomaticVerificationDTO getListByPage(VipAutomaticVerificationDTO dto) throws SQLException;

	List<VipAutomaticVerificationDTO> getExportList(VipAutomaticVerificationDTO dto);
		










}
