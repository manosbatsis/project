package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.TakesStockResultsDTO;
import com.topideal.entity.vo.TakesStockResultsModel;
import org.apache.ibatis.annotations.Param;

/**
 * 盘点结果表
 * @author lian_
 *
 */
@MyBatisRepository
public interface TakesStockResultsMapper extends BaseMapper<TakesStockResultsModel> {


	/**
     * 查询所有数据
     * @return
     */
    PageDataModel<TakesStockResultsModel> getListByPage(TakesStockResultsModel model)throws SQLException;

    /**
     * 查询DTO所有数据
     * @return
     */
    PageDataModel<TakesStockResultsDTO> getDTOListByPage(TakesStockResultsDTO dto)throws SQLException;

    TakesStockResultsDTO getDetail(Long id)throws SQLException;

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
    List<TakesStockResultsModel> getListByIds(@Param("ids") List<Long> ids) throws SQLException;

    /**
     * 根据条件查询导出盘点结果
     */
    List<Map<String, Object>> listForExport(TakesStockResultsDTO dto);

    /**
     * 根据条件查询导出盘点结果表体
     */
    List<Map<String, Object>> listForExportItem(TakesStockResultsDTO dto);
}

