package com.topideal.dao.main;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.MiningMarketingPriceDTO;
import com.topideal.entity.vo.main.MiningMarketingPriceModel;

/**
 *  采销报价
 * @author lian_
 */
public interface MiningMarketingPriceDao extends BaseDao<MiningMarketingPriceModel> {

	MiningMarketingPriceDTO getListByPage(MiningMarketingPriceDTO dto);
		










}

