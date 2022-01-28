package com.topideal.order.service.sale.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.sale.SalePoRelDao;
import com.topideal.entity.vo.sale.SalePoRelModel;
import com.topideal.order.service.sale.SalePoRelService;

@Service
public class SalePoRelServiceImpl implements SalePoRelService{
	// 销售单_po关联表
	@Autowired
	private SalePoRelDao salePoRelDao;
	@Override
	public List<SalePoRelModel> getAllByOrderId(Long orderId, Long merchantId) {
		return salePoRelDao.getAllByOrderId(orderId, merchantId);
	}

}
