package com.topideal.mapper.common;

import com.topideal.entity.dto.common.PlatformCostConfigItemDTO;
import com.topideal.entity.vo.common.PlatformCostConfigItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


public interface PlatformCostConfigItemMapper extends BaseMapper<PlatformCostConfigItemModel> {

    /*
    根据平台配置id查看表体
    * */
    List<PlatformCostConfigItemDTO> getConfigById(@Param("platformConfigId") Long platformConfigId);

}
