package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.VipTakesStockResultsDetailsDTO;
import com.topideal.entity.vo.reporting.VipTakesStockResultsDetailsModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface VipTakesStockResultsDetailsMapper extends BaseMapper<VipTakesStockResultsDetailsModel> {

	/**
	 * 删除明细
	 * @param model
	 * @return
	 */
	int deleteByModel(VipTakesStockResultsDetailsModel model);

	int batchInsert(List<VipTakesStockResultsDetailsModel> list);

	List<VipTakesStockResultsDetailsDTO> listDTO(VipTakesStockResultsDetailsDTO dto);


}
