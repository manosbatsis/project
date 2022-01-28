package com.topideal.dao;

import com.topideal.entity.dto.TakesStockResultsDTO;
import com.topideal.entity.vo.TakesStockResultsModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 盘点结果表
 * @author lian_
 *
 */
public interface TakesStockResultsDao extends BaseDao<TakesStockResultsModel>{

    public TakesStockResultsDTO searchDTOByPage(TakesStockResultsDTO dto) throws SQLException;

    public TakesStockResultsDTO getDetail(Long id) throws SQLException;

    /**
     * 通过ids获取盘点结果表体盘亏的商品信息
     */
    List<Map<String, Object>> getItemByResultIds(List<Long> resultIds);


    /**
     * 通过ids获取盘点结果单
     * @param ids
     * @return
     * @throws SQLException
     */
    List<TakesStockResultsModel> getListByIds(List<Long> ids) throws SQLException;


    /**
     * 根据条件查询导出盘点结果
     */
    public List<Map<String, Object>> listForExport(TakesStockResultsDTO dto);

    /**
     * 根据条件查询导出盘点结果表体
     */
    public List<Map<String, Object>> listForExportItem(TakesStockResultsDTO dto);

}

