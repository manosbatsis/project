package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.SdWeightedPriceDTO;
import com.topideal.entity.vo.reporting.SdWeightedPriceModel;


public interface SdWeightedPriceDao extends BaseDao<SdWeightedPriceModel> {

    SdWeightedPriceDTO getDtoListByPage(SdWeightedPriceDTO dto) throws SQLException;

    List<SdWeightedPriceDTO> listPrice(SdWeightedPriceDTO dto) throws SQLException;
    /**
     * 根据条件删除 加权单价数据
     * @param map
     * @return
     * @throws SQLException
     */
    int delSdWeightedPrice(Map<String, Object> map) throws SQLException;



    /**
     * 获取最近一个月SD加权单价
     * @param map
     * @return
     * @throws SQLException
     */
    SdWeightedPriceDTO getLastWeightedPrice(Map<String, Object> map) throws SQLException;


}
