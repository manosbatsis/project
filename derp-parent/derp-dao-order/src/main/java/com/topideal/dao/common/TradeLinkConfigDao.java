package com.topideal.dao.common;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.common.TradeLinkConfigDTO;
import com.topideal.entity.vo.common.TradeLinkConfigModel;

import java.sql.SQLException;
import java.util.List;


public interface TradeLinkConfigDao extends BaseDao<TradeLinkConfigModel> {

    TradeLinkConfigDTO getTradeLinkConfigListByPage(TradeLinkConfigDTO dto)throws SQLException;

    /**
     * 是否存在相同交易链路
     * @param dto
     * @return
     */
    List<TradeLinkConfigDTO> isExistsSameTradeLink(TradeLinkConfigDTO dto);






}
