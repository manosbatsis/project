package com.topideal.dao.automatic;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.YunjiAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.YunjiAutomaticVerificationModel;


public interface YunjiAutomaticVerificationDao extends BaseDao<YunjiAutomaticVerificationModel> {

	YunjiAutomaticVerificationDTO listByPage(YunjiAutomaticVerificationDTO dto);

	int getExportCount(YunjiAutomaticVerificationDTO dto);

	List<YunjiAutomaticVerificationDTO> getExportList(YunjiAutomaticVerificationDTO dto);

	List<YunjiAutomaticVerificationModel> getDifferentList();
		










}
