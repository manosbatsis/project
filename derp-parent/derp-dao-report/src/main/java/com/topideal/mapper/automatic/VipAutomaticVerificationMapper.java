package com.topideal.mapper.automatic;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.VipAutomaticVerificationModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface VipAutomaticVerificationMapper extends BaseMapper<VipAutomaticVerificationModel> {

	List<VipAutomaticVerificationDTO> getPageList(VipAutomaticVerificationDTO dto);

	List<VipAutomaticVerificationDTO> getExportList(VipAutomaticVerificationDTO dto);

	Integer getTotalCount(VipAutomaticVerificationDTO dto);



}
