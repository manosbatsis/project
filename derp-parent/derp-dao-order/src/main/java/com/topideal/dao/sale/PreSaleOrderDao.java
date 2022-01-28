package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.PreSaleOrderDTO;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.dto.sale.SaleOrderItemDTO;
import com.topideal.entity.vo.sale.PreSaleOrderModel;

import java.sql.SQLException;
import java.util.List;


public interface PreSaleOrderDao extends BaseDao<PreSaleOrderModel> {

    /**
     * 根据条件查询（分页）
     * @return
     */
    PreSaleOrderDTO queryDTOListByPage(PreSaleOrderDTO dto)throws SQLException;
    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
    PreSaleOrderDTO searchDTOById(Long id)throws SQLException;
    /**
     * 根据条件查询
     * @return
     */
    List<PreSaleOrderDTO> queryDTOList(PreSaleOrderDTO dto)throws SQLException;
    /**
     * 逻辑删除
     * @param ids
     * @return
     * @throws SQLException
     */
    int delPreSaleOrder(List ids) throws SQLException;
    /**
     * 根据审核时间排序预售单
     * @param ids
     * @return
     */
    List<PreSaleOrderDTO> listPreSaleByAuditDate(List ids)throws SQLException;

    int getTotal(PreSaleOrderDTO dto) throws SQLException;
}
