package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.dto.base.TariffRateConfigDTO;
import com.topideal.entity.vo.main.TariffRateConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface TariffRateConfigMapper extends BaseMapper<TariffRateConfigModel> {

   public PageDataModel<TariffRateConfigDTO> getListByPage(TariffRateConfigDTO model);


}
