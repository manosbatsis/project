package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.SaleSdOrderDTO;
import com.topideal.entity.vo.sale.SaleSdOrderModel;


public interface SaleSdOrderDao extends BaseDao<SaleSdOrderModel>{
		
	SaleSdOrderDTO listDTOByPage(SaleSdOrderDTO dto) throws SQLException;
	
	List<Map<String,Object>> exportSaleSdOrder(SaleSdOrderDTO dto)throws SQLException;
}
