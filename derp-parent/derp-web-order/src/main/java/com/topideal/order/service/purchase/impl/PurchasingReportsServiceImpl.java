package com.topideal.order.service.purchase.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.purchase.PurchasingReportsDao;
import com.topideal.entity.dto.purchase.PurchasingReportsDTO;
import com.topideal.order.service.purchase.PurchasingReportsService;

@Service
public class PurchasingReportsServiceImpl implements PurchasingReportsService{
	@Autowired
	private PurchasingReportsDao dao;	
	
	/**
	  * 列表 分页
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@Override
	public PurchasingReportsDTO listPurchasingReports(PurchasingReportsDTO dto) throws Exception {		
		return  dao.getListByPage(dto);
	}
	/**
	  * 获取导出数据
	 * @param model
	 * @return
	 * @throws Exception 
	 */	
	@Override
	public List<PurchasingReportsDTO> exportList(PurchasingReportsDTO dto) throws Exception{		
		return dao.exportList(dto);
	}
	@Override
	public Integer getOrderCount(PurchasingReportsDTO dto) throws Exception {		
		return dao.getTotalNum(dto, null);
	}
	
	
	

}
