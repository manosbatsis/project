package com.topideal.dao.sale;

import java.sql.SQLException;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleFinancingOrderDTO;
import com.topideal.entity.vo.sale.SaleFinancingOrderModel;


public interface SaleFinancingOrderDao extends BaseDao<SaleFinancingOrderModel> {
	/**
     * 根据条件查询
     * @param dto
     * @return
     */
    SaleFinancingOrderDTO searchByDTO(SaleFinancingOrderDTO dto) throws SQLException;
}
