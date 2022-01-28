package com.topideal.mapper.common;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.SdTypeConfigDTO;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SdTypeConfigMapper extends BaseMapper<SdTypeConfigModel> {

	PageDataModel<SdTypeConfigModel> getListByPage(SdTypeConfigDTO dto);



}
