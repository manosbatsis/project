package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleFinancingOrderItemDTO;
import com.topideal.entity.vo.sale.SaleFinancingOrderItemModel;


public interface SaleFinancingOrderItemDao extends BaseDao<SaleFinancingOrderItemModel> {
		
	/**
     * 查询所有信息
     * @param dto
     * @return
     */
    List<SaleFinancingOrderItemDTO> listByDTO(SaleFinancingOrderItemDTO dto) throws SQLException;

}
