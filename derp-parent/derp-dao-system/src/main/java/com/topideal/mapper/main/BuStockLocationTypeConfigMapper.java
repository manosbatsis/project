package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.BuStockLocationTypeConfigDTO;
import com.topideal.entity.vo.main.BuStockLocationTypeConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuStockLocationTypeConfigMapper extends BaseMapper<BuStockLocationTypeConfigModel> {


    PageDataModel<BuStockLocationTypeConfigDTO> listDTOByPage(BuStockLocationTypeConfigDTO dto);
}
