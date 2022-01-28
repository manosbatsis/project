package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.tools.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.entity.dto.sale.OrderItemDTO;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.service.timer.OrderApportionTaxAndFreightService;

import net.sf.json.JSONObject;

/**
 * 电商订单分摊税费运费
 * @author qiancheng.chen
 * @date 2020-12-24
 *
 */
@Service
public class OrderApportionTaxAndFreightServiceImpl implements OrderApportionTaxAndFreightService{
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(OrderApportionTaxAndFreightServiceImpl.class);
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemDao orderItemDao;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201120310,model=DERP_LOG_POINT.POINT_13201120310_Label)
	public void apportionTaxAndFreight(String json, String keys, String topics, String tags) throws Exception {
		String startDate ="";		
		String endDate = "";
		LOGGER.info("==============json:========="+json.toString());
		//1、判断时间是否为空，为空则查找当前时间前4小时的订单
		JSONObject jSONOrderObject = JSONObject.fromObject(json);
		startDate = (String) jSONOrderObject.get("startTime"); 
		endDate = (String) jSONOrderObject.get("endTime"); 
		
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 4);
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    
		startDate = StringUtils.isNotBlank(startDate) ? startDate :df.format(calendar.getTime());
		endDate = StringUtils.isNotBlank(endDate) ? endDate:df.format(new Date());
		
		//2、获取新增、修改的 税费运费不为0电商订单
		while(true){
			List<Long> ids = orderDao.getApportionOrder(startDate,endDate);
			if(ids != null && ids.size() > 0) {
				for(Long id: ids) {
					OrderModel orderModel = orderDao.searchById(id);
					if(orderModel != null) {
						//3、 税费进行分摊
						apportionTax(orderModel);
						//4、 运费进行分摊
						apportionFrt(orderModel);
					}
				}

			}else{
				break;
			}

		}

		
	}
	
	private void apportionTax(OrderModel dto) throws SQLException{
		OrderItemDTO itemDTO = new OrderItemDTO();
		itemDTO.setOrderId(dto.getId());
		List<OrderItemDTO> orderItemList = orderItemDao.listItemDTO(itemDTO);		
		if(orderItemList != null && orderItemList.size() > 0) {
			List<OrderItemModel> list = new ArrayList<OrderItemModel>();
			//3.1 若商品行数只有1行，商品分摊税费=订单税费
			if(orderItemList.size() == 1) {
				OrderItemModel model = new OrderItemModel();
				model.setId(orderItemList.get(0).getId());
				model.setTax(dto.getTaxes());
				list.add(model);
			}else if(dto.getTaxes() == null ||  dto.getTaxes().compareTo(BigDecimal.ZERO) == 0){
				for(int i = 0; i<orderItemList.size(); i++) {
					OrderItemModel model = new OrderItemModel();
					model.setId(orderItemList.get(i).getId());
					model.setTax(BigDecimal.ZERO);
					list.add(model);
				}
			}else {
				//3.2 若订单行数多余1行，前N-1行的商品分摊税费=订单税费*(当前商品结算金额/商品结算总额
				BigDecimal sumAmount = new BigDecimal(0);//记录商品结算总额
				BigDecimal apportionTax = new BigDecimal(0);//记录分摊税费
				BigDecimal taxTotal = new BigDecimal(0);//记录前N个商品分摊税费总额
				for(OrderItemDTO item:orderItemList) {
					sumAmount = sumAmount.add(item.getDecTotal());
				}				
				for(int i = 0; i<orderItemList.size(); i++) {
					//3.3 若订单行数多余1行，第N商品分摊税费=订单税费-前N个商品分摊税费总额
					if(i == orderItemList.size()-1) {
						apportionTax = dto.getTaxes().subtract(taxTotal);
					}else {
						apportionTax = orderItemList.get(i).getDecTotal().divide(sumAmount,2, BigDecimal.ROUND_HALF_UP).multiply(dto.getTaxes());						
					}
					apportionTax = apportionTax.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入保留两位
					taxTotal = taxTotal.add(apportionTax);
					
					OrderItemModel model = new OrderItemModel();
					model.setId(orderItemList.get(i).getId());
					model.setTax(apportionTax);
					list.add(model);
				}		
				
				
			}			
			if(list != null && list.size() > 0) {
				for(OrderItemModel model :list) {
					orderItemDao.modify(model);
				}
			}
			
		}
	}
	
	
	private void apportionFrt(OrderModel dto) throws SQLException{
		OrderItemDTO itemDTO = new OrderItemDTO();
		itemDTO.setOrderId(dto.getId());
		List<OrderItemDTO> orderItemList = orderItemDao.listItemDTO(itemDTO);		
		if(orderItemList != null && orderItemList.size() > 0) {
			List<OrderItemModel> list = new ArrayList<OrderItemModel>();
			//4.1 若商品行数只有1行，商品分摊运费=订单运费
			if(orderItemList.size() == 1) {
				OrderItemModel model = new OrderItemModel();
				model.setId(orderItemList.get(0).getId());
				model.setWayFrtFee(dto.getWayFrtFee());	
				list.add(model);
			}else if(dto.getWayFrtFee() == null ||  dto.getWayFrtFee().compareTo(BigDecimal.ZERO) == 0){
				for(int i = 0; i<orderItemList.size(); i++) {
					OrderItemModel model = new OrderItemModel();
					model.setId(orderItemList.get(i).getId());
					model.setWayFrtFee(BigDecimal.ZERO);
					list.add(model);
				}
			}else {
				//4.2 若订单行数多余1行，前N-1行的商品分摊运费=订单运费*(当前商品结算金额/商品结算总额）
				BigDecimal sumAmount = new BigDecimal(0);//记录商品结算总额
				BigDecimal apportionFrt = new BigDecimal(0);//记录分摊运费
				BigDecimal frtTotal = new BigDecimal(0);//记录前N个商品分摊运费总额
				for(OrderItemDTO item:orderItemList) {
					sumAmount = sumAmount.add(item.getDecTotal());
				}				
				for(int i = 0; i<orderItemList.size(); i++) {
					//4.3 若订单行数多余1行，第N商品分摊税费=订单税费-前N个商品分摊税费总额；第N商品分摊运费=订单运费-前N个商品分摊运费总额
					if(i == orderItemList.size()-1) {
						apportionFrt = dto.getWayFrtFee().subtract(frtTotal);
					}else {
						apportionFrt = orderItemList.get(i).getDecTotal().divide(sumAmount,2, BigDecimal.ROUND_HALF_UP).multiply(dto.getWayFrtFee());
					}
					apportionFrt = apportionFrt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入保留两位
					frtTotal = frtTotal.add(apportionFrt);
					
					OrderItemModel model = new OrderItemModel();
					model.setId(orderItemList.get(i).getId());
					model.setWayFrtFee(apportionFrt);
					list.add(model);
				}		
				
				
			}			
			if(list != null && list.size() > 0) {
				for(OrderItemModel model :list) {
					orderItemDao.modify(model);
				}
			}
			
		}
	}

}
