package com.topideal.dao.reporting;


import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.VipTakesStockResultsDetailsDTO;
import com.topideal.entity.vo.reporting.VipTakesStockResultsDetailsModel;


public interface VipTakesStockResultsDetailsDao extends BaseDao<VipTakesStockResultsDetailsModel> {

	/**
	 * 删除明细
	 * @param model
	 * @return
	 */
	int deleteByModel(VipTakesStockResultsDetailsModel model);

	int batchSave(List<VipTakesStockResultsDetailsModel> tempList);

	List<VipTakesStockResultsDetailsDTO> listDTO(VipTakesStockResultsDetailsDTO dto);








}
