package com.topideal.dao.reporting;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipTransferDepotDetailsDTO;
import com.topideal.entity.vo.reporting.VipTransferDepotDetailsModel;


public interface VipTransferDepotDetailsDao extends BaseDao<VipTransferDepotDetailsModel> {

	Integer deleteByModel(VipTransferDepotDetailsModel model);

	int batchSave(List<VipTransferDepotDetailsModel> tempList);

	List<VipTransferDepotDetailsDTO> listDTO(VipTransferDepotDetailsDTO dto);




}
