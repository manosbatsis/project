package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleSdOrderItemDTO;
import com.topideal.entity.vo.sale.SaleSdOrderItemModel;


public interface SaleSdOrderItemDao extends BaseDao<SaleSdOrderItemModel>{
	
	List<SaleSdOrderItemDTO> listDTO(SaleSdOrderItemDTO dto)throws SQLException;
	
	int delItemBySaleSdIds(List<Long> saleSdOrderIdList)throws SQLException;

}
