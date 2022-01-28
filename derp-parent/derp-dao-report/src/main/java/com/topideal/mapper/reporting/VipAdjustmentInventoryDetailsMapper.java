package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipAdjustmentInventoryDetailsDTO;
import com.topideal.entity.vo.reporting.VipAdjustmentInventoryDetailsModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface VipAdjustmentInventoryDetailsMapper extends BaseMapper<VipAdjustmentInventoryDetailsModel> {

	int deleteByModel(VipAdjustmentInventoryDetailsModel model);

	List<Map<String, Object>> getVipAjuInvenDetails(Map<String, Object> queryMap);

	int batchInsert(List<VipAdjustmentInventoryDetailsModel> list);

	List<VipAdjustmentInventoryDetailsDTO> listDTO(VipAdjustmentInventoryDetailsDTO dto);


}
