package com.topideal.dao.reporting;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipSaleReturnOdepotDetailsDTO;
import com.topideal.entity.vo.reporting.VipSaleReturnOdepotDetailsModel;


public interface VipSaleReturnOdepotDetailsDao extends BaseDao<VipSaleReturnOdepotDetailsModel> {

	int deleteByModel(VipSaleReturnOdepotDetailsModel model);

	int batchSave(List<VipSaleReturnOdepotDetailsModel> tempList);

	List<VipSaleReturnOdepotDetailsDTO> listDTO(VipSaleReturnOdepotDetailsDTO model);
		










}
