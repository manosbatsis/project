package com.topideal.dao.reporting;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipOutDepotDetailsDTO;
import com.topideal.entity.vo.reporting.VipOutDepotDetailsModel;


public interface VipOutDepotDetailsDao extends BaseDao<VipOutDepotDetailsModel> {

	List<VipOutDepotDetailsDTO> listDTO(VipOutDepotDetailsDTO dto);

	int deleteByModel(VipOutDepotDetailsModel model);

	int batchSave(List<VipOutDepotDetailsModel> tempList);
		










}
