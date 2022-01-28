package com.topideal.service.pushback.sale.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.json.inventory.j04.BackRootJson;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.pushback.sale.DSLoadingDeliveriesPushBackService;

import net.sf.json.JSONObject;

/**
 *  电商订单-装载交运库存扣减成功回推
 * @author 杨创
 *2019/02/27
 */
@Service
public class DSLoadingDeliveriesPushBackServiceImpl implements DSLoadingDeliveriesPushBackService {

	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(DSLoadingDeliveriesPushBackServiceImpl.class);
	
    @Autowired
    private OrderDao orderDao ;
    @Autowired
    private OrderItemDao orderItemDao ;
    @Autowired
	private MerchantInfoMongoDao merchantMongoDao;
    /**
     * 修改电商订单
     */
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201153300,model=DERP_LOG_POINT.POINT_13201153300_Label,keyword="code")
	public void updateDSLoadingDeliveriesPushBackInfo(String json, String keys, String topics, String tags) throws Exception {		
		Thread.sleep(100);
		//获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		BackRootJson rootJson = (BackRootJson) JSONObject.toBean(jsonData, BackRootJson.class,classMap);
		Map<String, Object> customParam = rootJson.getCustomParam();
		String code =  (String) customParam.get("code");
		OrderModel model= new OrderModel();
		model.setCode(code);
		model = orderDao.searchByModel(model);		
		if (model==null) {
			LOGGER.error(DERP.MQ_FAILTYPE_04 + "根据电商订单号没有查到电商订单:code"+code);
			throw new  RuntimeException(DERP.MQ_FAILTYPE_04 + "根据电商订单号没有查到电商订单:code"+code);
		}
		Map<String, Object> merchantMap = new HashMap<String, Object>();
		merchantMap.put("merchantId", model.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
		boolean isTradeMechant = "1".equals(merchant.getRegisteredType()) ? true : false;//是否是内贸主体
		BigDecimal tradePayment = BigDecimal.ZERO;
		BigDecimal tradePaymentTax = BigDecimal.ZERO;
		if(isTradeMechant) {//仅对公司主体的注册地类型为 境内的主体计算 内贸税额、内贸实付金额（不含税）
			/**
			 * 内贸实付金额（不含税）= 订单实付金额/（1+13%），得出的金额保留2位小数，做四舍五入
			 * 内贸税额 = 订单实付金额 - 内贸实付金额（不含税），得出金额保留2位小数，做四舍五入
			 */
			tradePayment = model.getPayment().divide(new BigDecimal(1.13),2,BigDecimal.ROUND_HALF_UP);//内贸实付金额（不含税
			tradePaymentTax = model.getPayment().subtract(tradePayment).setScale(2,BigDecimal.ROUND_HALF_UP);//内贸税额
			
		}
		
		// 修改电商订单状态为已发货 、计算内贸结算金额
		OrderModel orderModel = new OrderModel();
		orderModel.setId(model.getId());
		orderModel.setStatus(DERP_ORDER.ORDER_STATUS_4);
		orderModel.setTradePayment(tradePayment);
		orderModel.setTradePaymentTax(tradePaymentTax);
		orderDao.modify(orderModel);
		
		//计算商品内贸结算金额
		OrderItemModel itemModel= new OrderItemModel();
		itemModel.setOrderId(model.getId());
		List<OrderItemModel> itemList = orderItemDao.list(itemModel);
		for(OrderItemModel item : itemList) {
			if(isTradeMechant) {//仅对公司主体的注册地类型为 境内的主体 计算内贸商品结算税额 、内贸商品结算金额（不含税）
				/**
				 * 内贸商品结算金额（不含税）= 商品结算金额/（1+13%），得出的金额保留2位小数，做四舍五入
				 * 内贸商品结算税额 = 商品结算金额 - 内贸商品结算金额（不含税），得出金额保留2位小数，做四舍五入
				 */
				BigDecimal itemTradePayment = item.getDecTotal().divide(new BigDecimal(1.13),2,BigDecimal.ROUND_HALF_UP);
				BigDecimal itemTradePaymentTax = item.getDecTotal().subtract(itemTradePayment).setScale(2,BigDecimal.ROUND_HALF_UP);
				
				item.setTradeDecTotal(itemTradePayment);
				item.setTradeDecTax(itemTradePaymentTax);
				
				orderItemDao.modify(item);
			}
		}
		
		
	}
    
	
}
