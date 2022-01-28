package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.entity.vo.sale.PreSaleOrderCorrelationModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface PreSaleOrderCorrelationMapper extends BaseMapper<PreSaleOrderCorrelationModel> {

    // 获得某个预售单的预售总量
    Integer getPreNumByPreSaleId(@Param("preSaleOrderCodes")List<String> preSaleOrderCodes,@Param("goodsId")Long goodsId);
    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<PreSaleOrderCorrelationDTO> listPreSaleOrderCorrelationByPage(PreSaleOrderCorrelationDTO dto) throws SQLException;

    /**
     * 根据列表查询条件统计总数
     * @Param
     * @return
     */
    int getTotal(PreSaleOrderCorrelationDTO dto) throws SQLException;

    // 获得某个预售单的已销售量
    Integer getSaleNumByPreSaleId(@Param("preSaleOrderCodes")List<String> preSaleOrderCodes,@Param("goodsId")Long goodsId);
    /**
     * 获取预售单表头信息
     * @Param
     * @return
     */
    PreSaleOrderCorrelationDTO detail(PreSaleOrderCorrelationDTO dto) throws SQLException;

    /**
     * 获取预售单表体信息
     * @param dto
     * @return
     * @throws SQLException
     */
    List<PreSaleOrderCorrelationDTO> detailByCodeAndGoodsNo(PreSaleOrderCorrelationDTO dto) throws SQLException;

    /**
     * 导出勾稽明细
     * @param dto
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> detailForExport(PreSaleOrderCorrelationDTO dto) throws SQLException;

    /**
     * 导出汇总
     * @param dto
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> listForExport(PreSaleOrderCorrelationDTO dto) throws SQLException;
}
