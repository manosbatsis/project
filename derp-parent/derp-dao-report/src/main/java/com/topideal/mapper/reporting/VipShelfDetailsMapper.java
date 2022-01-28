package com.topideal.mapper.reporting;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipShelfDetailsDTO;
import com.topideal.entity.vo.reporting.VipPoBillVerificationModel;
import com.topideal.entity.vo.reporting.VipShelfDetailsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface VipShelfDetailsMapper extends BaseMapper<VipShelfDetailsModel> {

	List<VipShelfDetailsDTO> listDTO(VipShelfDetailsDTO model);

	int deleteByModel(VipShelfDetailsModel model);

	int batchInsert(List<VipShelfDetailsModel> list);



}
