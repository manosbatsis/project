package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipTransferDepotDetailsDTO;
import com.topideal.entity.vo.reporting.VipTransferDepotDetailsModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface VipTransferDepotDetailsMapper extends BaseMapper<VipTransferDepotDetailsModel> {

	Integer deleteByModel(VipTransferDepotDetailsModel model);

	int batchInsert(List<VipTransferDepotDetailsModel> list);

	List<VipTransferDepotDetailsDTO> listDTO(VipTransferDepotDetailsDTO dto);

}
