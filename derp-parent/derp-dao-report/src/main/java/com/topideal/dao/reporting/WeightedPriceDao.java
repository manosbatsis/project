package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.WeightedPriceDTO;
import com.topideal.entity.vo.reporting.WeightedPriceModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface WeightedPriceDao extends BaseDao<WeightedPriceModel> {
		

    WeightedPriceDTO getDtoListByPage(WeightedPriceDTO dto) throws SQLException;

    List<WeightedPriceDTO> listPrice(WeightedPriceDTO dto) throws SQLException;
    /**
     * 根据条件删除 加权单价数据
     * @param map
     * @return
     * @throws SQLException
     */
    int delWeightedPrice(Map<String, Object> map) throws SQLException;

    /**
     * 获取最近一个月加权单价
     * @param map
     * @return
     * @throws SQLException
     */
    WeightedPriceDTO getLastWeightedPrice(Map<String, Object> map) throws SQLException;
    /**
     * 获取小于当前月份的所有标准条码
     * @param paramMap
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getLastWeightedPriceAll(Map<String, Object> paramMap)throws SQLException;
}
