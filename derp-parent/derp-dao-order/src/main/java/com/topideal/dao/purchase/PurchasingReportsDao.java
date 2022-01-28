package com.topideal.dao.purchase;

import java.sql.Connection;
import java.util.List;

import com.topideal.entity.dto.purchase.PurchasingReportsDTO;


public interface PurchasingReportsDao {
		
	PurchasingReportsDTO getListByPage(PurchasingReportsDTO dto) throws Exception;
	
	List<PurchasingReportsDTO> exportList(PurchasingReportsDTO dto) throws Exception;
	
	Integer getTotalNum(PurchasingReportsDTO dto, Connection conn) throws Exception;

}
