package com.topideal.dao.main;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.TariffRateConfigDTO;
import com.topideal.entity.vo.main.TariffRateConfigModel;


public interface TariffRateConfigDao extends BaseDao<TariffRateConfigModel> {

    /**
     * 查询税率配置
     * @param model
     * @return
     */
    public TariffRateConfigDTO getListByPage(TariffRateConfigDTO model);
		










}
