package com.topideal.dao.reporting;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipAdjustmentInventoryDetailsDTO;
import com.topideal.entity.vo.reporting.VipAdjustmentInventoryDetailsModel;


public interface VipAdjustmentInventoryDetailsDao extends BaseDao<VipAdjustmentInventoryDetailsModel> {

	int deleteByModel(VipAdjustmentInventoryDetailsModel model);

	int batchSave(List<VipAdjustmentInventoryDetailsModel> tempList);

	List<VipAdjustmentInventoryDetailsDTO> listDTO(VipAdjustmentInventoryDetailsDTO dto);









}
