package com.topideal.mapper.reporting;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.SettlementPriceWarnconfigDTO;
import com.topideal.entity.vo.reporting.SettlementPriceWarnconfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SettlementPriceWarnconfigMapper extends BaseMapper<SettlementPriceWarnconfigModel> {

	PageDataModel<SettlementPriceWarnconfigDTO> getListByPage(SettlementPriceWarnconfigDTO dto);

	SettlementPriceWarnconfigDTO searchDTOById(Long id);

	

}
