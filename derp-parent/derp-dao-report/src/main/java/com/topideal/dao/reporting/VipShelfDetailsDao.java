package com.topideal.dao.reporting;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipShelfDetailsDTO;
import com.topideal.entity.vo.reporting.VipShelfDetailsModel;


public interface VipShelfDetailsDao extends BaseDao<VipShelfDetailsModel> {

	List<VipShelfDetailsDTO> listDTO(VipShelfDetailsDTO dto);

	int deleteByModel(VipShelfDetailsModel model);

	int batchSave(List<VipShelfDetailsModel> tempList);
		










}
