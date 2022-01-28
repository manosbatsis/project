package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleCreditOrderItemDTO;
import com.topideal.entity.vo.sale.SaleCreditOrderItemModel;


public interface SaleCreditOrderItemDao extends BaseDao<SaleCreditOrderItemModel>{
		
	List<SaleCreditOrderItemDTO> listDTO(SaleCreditOrderItemDTO dto) throws SQLException;

}
