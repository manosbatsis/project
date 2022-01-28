package com.topideal.mapper.reporting;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipOutDepotDetailsDTO;
import com.topideal.entity.dto.VipShelfDetailsDTO;
import com.topideal.entity.vo.reporting.VipOutDepotDetailsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface VipOutDepotDetailsMapper extends BaseMapper<VipOutDepotDetailsModel> {

	List<VipOutDepotDetailsDTO> listDTO(VipOutDepotDetailsDTO model);

	int deleteByModel(VipOutDepotDetailsModel model);

	int batchInsert(List<VipOutDepotDetailsModel> list);



}
