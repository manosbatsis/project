package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.MiningMarketingPriceDTO;
import com.topideal.entity.vo.main.MiningMarketingPriceModel;
import com.topideal.mapper.BaseMapper;

/**
 *  采销报价
 * @author lian_
 */
@MyBatisRepository
public interface MiningMarketingPriceMapper extends BaseMapper<MiningMarketingPriceModel> {

	PageDataModel<MiningMarketingPriceDTO> getListByPage(MiningMarketingPriceDTO dto);



}

