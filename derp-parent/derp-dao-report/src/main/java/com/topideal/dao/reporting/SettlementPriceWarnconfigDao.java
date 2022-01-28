package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.SettlementPriceWarnconfigDTO;
import com.topideal.entity.vo.reporting.SettlementPriceWarnconfigModel;


public interface SettlementPriceWarnconfigDao extends BaseDao<SettlementPriceWarnconfigModel>{
		
	SettlementPriceWarnconfigDTO getListByPage(SettlementPriceWarnconfigDTO dto);

	SettlementPriceWarnconfigDTO searchDTOById(Long id);









}
