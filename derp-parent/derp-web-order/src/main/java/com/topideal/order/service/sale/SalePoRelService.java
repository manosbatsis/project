package com.topideal.order.service.sale;

import java.util.List;

import org.springframework.stereotype.Service;

import com.topideal.entity.vo.sale.SalePoRelModel;

@Service
public interface SalePoRelService {
	   List<SalePoRelModel> getAllByOrderId (Long orderId,Long merchantId);
}
