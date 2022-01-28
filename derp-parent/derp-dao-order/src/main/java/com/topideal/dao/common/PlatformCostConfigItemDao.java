package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.PlatformCostConfigItemDTO;
import com.topideal.entity.vo.common.PlatformCostConfigItemModel;

import java.util.List;


public interface PlatformCostConfigItemDao extends BaseDao<PlatformCostConfigItemModel> {

    /**
     * 根据平台费用配置id查看表体
     * @param config
     * @return
     */
    List<PlatformCostConfigItemDTO> getConfigById(Long config);










}
