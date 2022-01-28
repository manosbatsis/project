package com.topideal.mapper.common;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.common.TradeLinkConfigDTO;
import com.topideal.entity.vo.common.TradeLinkConfigModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface TradeLinkConfigMapper extends BaseMapper<TradeLinkConfigModel> {

    /**
     * 分页查询
     * @param dto
     * @return
     */
    PageDataModel<TradeLinkConfigDTO> getTradeLinkConfigListByPage(TradeLinkConfigDTO  dto);
    /**
     * 是否存在相同交易链路
     * @param dto
     * @return
     */
    List<TradeLinkConfigDTO> isExistsSameTradeLink(TradeLinkConfigDTO dto);

}
