package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.QuotaPeriodConfigDTO;
import com.topideal.entity.vo.common.QuotaPeriodConfigModel;


public interface QuotaPeriodConfigDao extends BaseDao<QuotaPeriodConfigModel>{

	QuotaPeriodConfigDTO getListByPage(QuotaPeriodConfigDTO dto);
		










}
