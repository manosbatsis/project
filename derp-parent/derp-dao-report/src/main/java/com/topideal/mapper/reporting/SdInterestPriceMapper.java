package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.SdInterestPriceDTO;
import com.topideal.entity.vo.reporting.SdInterestPriceModel;
import com.topideal.mapper.BaseMapper;


public interface SdInterestPriceMapper extends BaseMapper<SdInterestPriceModel> {

    /**
     * 根据条件删除 利息加权单价数据
     * @param map
     * @return
     * @throws SQLException
     */
    int delSdInterestPrice(Map<String, Object> map) throws SQLException;

    PageDataModel<SdInterestPriceDTO> getDtoListByPage(SdInterestPriceDTO dto);

    List<SdInterestPriceDTO> listPrice(SdInterestPriceDTO dto);
    
    /**
     * 获取最近一个月SD利息加权单价
     * @param map
     * @return
     * @throws SQLException
     */
    SdInterestPriceDTO getLastWeightedPrice(Map<String, Object> map) throws SQLException;
}
