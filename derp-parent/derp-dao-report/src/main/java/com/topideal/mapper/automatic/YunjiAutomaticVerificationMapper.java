package com.topideal.mapper.automatic;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.YunjiAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.YunjiAutomaticVerificationModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface YunjiAutomaticVerificationMapper extends BaseMapper<YunjiAutomaticVerificationModel> {

	List<YunjiAutomaticVerificationDTO> getPageList(YunjiAutomaticVerificationDTO dto);

	int getExportCount(YunjiAutomaticVerificationDTO dto);

	List<YunjiAutomaticVerificationDTO> getExportList(YunjiAutomaticVerificationDTO dto);

	List<YunjiAutomaticVerificationModel> getDifferentList();

	Integer getTotal(YunjiAutomaticVerificationDTO dto);

}
