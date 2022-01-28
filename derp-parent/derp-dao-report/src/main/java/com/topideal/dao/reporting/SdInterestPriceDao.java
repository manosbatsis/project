package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.SdInterestPriceDTO;
import com.topideal.entity.vo.reporting.SdInterestPriceModel;


public interface SdInterestPriceDao extends BaseDao<SdInterestPriceModel>{

    /**
     * 根据条件删除 利息加权单价数据
     * @param map
     * @return
     * @throws SQLException
     */
    int delSdInterestPrice(Map<String, Object> map) throws SQLException;

    SdInterestPriceDTO getDtoListByPage(SdInterestPriceDTO dto);

    List<SdInterestPriceDTO> listPrice(SdInterestPriceDTO dto);
    /**
     * 获取最近一个月SD利息加权单价
     * @param map
     * @return
     * @throws SQLException
     */
    SdInterestPriceDTO getLastWeightedPrice(Map<String, Object> map) throws SQLException;
}
