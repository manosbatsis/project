package com.topideal.service.main;


import com.topideal.entity.dto.main.MiningMarketingPriceDTO;
import com.topideal.entity.vo.main.MiningMarketingPriceModel;

import java.sql.SQLException;

/**
 * 采销报价
 * @author zhanghx
 */
public interface MiningMarketingPriceService {

	/**
	 * 分页
	 * @param model 
	 * @return
	 */
	MiningMarketingPriceDTO listByPage(MiningMarketingPriceDTO dto) throws SQLException;
	
}
