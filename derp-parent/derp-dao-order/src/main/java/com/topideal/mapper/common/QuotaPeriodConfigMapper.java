package com.topideal.mapper.common;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.QuotaPeriodConfigDTO;
import com.topideal.entity.vo.common.QuotaPeriodConfigModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface QuotaPeriodConfigMapper extends BaseMapper<QuotaPeriodConfigModel> {

	PageDataModel<QuotaPeriodConfigDTO> getListByPage(QuotaPeriodConfigDTO dto);



}
