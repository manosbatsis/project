package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.entity.vo.sale.PreSaleOrderCorrelationModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.entity.vo.sale.SaleOutDepotModel;


public interface PreSaleOrderCorrelationDao extends BaseDao<PreSaleOrderCorrelationModel> {


    // 获得某个预售单的预售总量
    Integer getPreNumByPreSaleId(List<String> preSaleOrderCodes,Long goodsId);

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    PreSaleOrderCorrelationDTO listPreSaleOrderCorrelationByPage(PreSaleOrderCorrelationDTO dto) throws SQLException;
    // 获得某个预售单的已销售量
    Integer getSaleNumByPreSaleId(List<String> preSaleOrderCodes,Long goodsId);


    /**
     * 根据列表查询条件统计总数
     * @Param
     * @return
     */
    int getTotal(PreSaleOrderCorrelationDTO dto) throws SQLException;


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
    
    /**
     *预售勾稽明细表出库数据
     * @param model
     * @return
     */
    List<PreSaleOrderCorrelationModel> getPreSaleOrderCorList(SaleOutDepotModel saleOutDepotModel,SaleOrderModel saleOrderModel,List<Map<String, Object>> itemMapList) throws Exception;

}
