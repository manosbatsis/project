package com.topideal.order.service.sale.impl;

import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.dao.sale.SaleShelfDao;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleShelfModel;
import com.topideal.order.service.sale.SaleShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleShelfServiceImpl implements SaleShelfService{

	@Autowired
	private SaleShelfDao saleShelfDao ;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;

	@Override
	public List<SaleShelfDTO> listItemBySaleShelfId(Long id) throws SQLException {
		return saleShelfDao.getSaleShelfModelById(id);
	}

    @Override
    public List<SaleShelfDTO> searchDetailByOrderId(Long id) throws Exception {
		SaleShelfModel saleShelfModel = new SaleShelfModel();
		saleShelfModel.setOrderId(id);
		List<SaleShelfDTO> saleShelfList = saleShelfDao.searchDetailByOrderId(saleShelfModel);
		for(SaleShelfDTO saleShelfDTO : saleShelfList){
			SaleOrderItemModel saleOrderItem = saleOrderItemDao.searchById(saleShelfDTO.getSaleItemId());
			//上架总金额=上架好品量*销售单价
			BigDecimal shelfNum = new BigDecimal(saleShelfDTO.getShelfNum() == null ? 0 : saleShelfDTO.getShelfNum());
			BigDecimal shelfAmount = shelfNum.multiply(saleOrderItem.getPrice()).setScale(2 ,BigDecimal.ROUND_HALF_UP);
			saleShelfDTO.setPrice(saleOrderItem.getPrice());
			saleShelfDTO.setShelfAmount(shelfAmount);//上架金额
		}
		return saleShelfList;
    }
	@Override
	public List<Map<String,Object>> getItemByPoNoAndOrderId(Long orderId, List<String> poNoList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("poNoList", poNoList);
		List<Map<String,Object>> resultList =  saleShelfDao.getItemByPoNoAndOrderId(params);
		for(Map<String,Object> map : resultList){
			BigDecimal returnNum = (BigDecimal) map.get("returnNum");
			BigDecimal amount = (BigDecimal) map.get("amount");
			BigDecimal taxAmount = new BigDecimal((Double) map.get("taxAmount"));
			BigDecimal tax = (BigDecimal) map.get("tax");

			BigDecimal price = BigDecimal.ZERO;
			BigDecimal taxPrice = BigDecimal.ZERO;
			if(returnNum != null && returnNum.intValue() > 0){
				price = amount.divide(returnNum, 8 ,BigDecimal.ROUND_HALF_UP);
				taxPrice = taxAmount.divide(returnNum, 8 ,BigDecimal.ROUND_HALF_UP);
			}
			Double taxRate = 0.0;
			if(taxAmount != null && taxAmount.compareTo(BigDecimal.ZERO) > 0){
				taxRate = amount.divide(taxAmount, 2,BigDecimal.ROUND_HALF_UP).doubleValue();
			}

			map.put("price", price);
			map.put("taxPrice", taxPrice);
			map.put("taxRate",taxRate);
			map.put("tax",tax);

		}

		return resultList;
	}

	@Override
	public List<SaleShelfDTO> listDTO(SaleShelfDTO dto) throws Exception {
		return saleShelfDao.listDTO(dto);
	}
}
