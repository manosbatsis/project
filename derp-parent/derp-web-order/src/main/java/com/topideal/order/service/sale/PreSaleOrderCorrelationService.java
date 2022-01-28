package com.topideal.order.service.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;

/**
 * @Description: 预售勾稽明细service
 * @Date: 2020-06-02 15:55
 **/
public interface PreSaleOrderCorrelationService {

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    PreSaleOrderCorrelationDTO listPreSaleOrderCorrelationByPage(PreSaleOrderCorrelationDTO dto,User user) throws SQLException;

    /**
     * 获取预售单表头信息
     * @param dto
     * @return
     * @throws SQLException
     */
    PreSaleOrderCorrelationDTO detailPreSaleOrderCorrelation(PreSaleOrderCorrelationDTO dto) throws SQLException;

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
    List<Map<String, Object>> detailForExport(PreSaleOrderCorrelationDTO dto,User user) throws SQLException;

    /**
     * 导出汇总
     * @param dto
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> listForExport(PreSaleOrderCorrelationDTO dto,User user) throws SQLException;
}
