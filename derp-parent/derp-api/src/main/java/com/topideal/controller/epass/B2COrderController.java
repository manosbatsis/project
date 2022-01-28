package com.topideal.controller.epass;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.B2COrderService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * B2C订单接口(电商交易订单)(2)
 * @author 杨创
 *2018/5/17
 */
@Controller
@RequestMapping("/api/1204")
public class B2COrderController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(B2COrderController.class);
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private B2COrderService b2COrderService;//B2C订单接口
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	
	
	/**
	 * 电商交易订单回推接口
	 * @param json
	 * @return
	 */
	@RequestMapping(params="method=erp8478",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point="12103120300",model="B2C订单接口",keyword = "order_id")
	public JSONObject getB2COrder (@RequestBody String json){
		LOGGER.info("电商交易订单回推接口请求开始,请求报文"+json);
		JSONObject jsonData=new JSONObject();
		try {
			jsonData = JSONObject.fromObject(json);
			
			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
			
			//订单号
			if (jsonData.get("order_id")==null||StringUtils.isBlank(jsonData.getString("order_id"))) {
				return ResponseFactory.error("order_id","" , "order_id is NULL");
			}			
			//订单时间，格式:yyyy-MM-dd HH:mm:ss。
			if (jsonData.get("created")==null||StringUtils.isBlank(jsonData.getString("created"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "created is NULL");
			}			
			//订单实付总额，浮点数，double，2位小数。货款+运费+税款-优惠金额，与支付保持一致
			if (jsonData.get("payment")==null||StringUtils.isBlank(jsonData.getString("payment"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "payment is NULL");
			}
			//运杂费，浮点数，double，2位小数。不包含在商品价格中的运杂费，如无填0
			if (jsonData.get("way_frt_fee")==null||StringUtils.isBlank(jsonData.getString("way_frt_fee"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "way_frt_fee is NULL");
			}
			//保费，浮点数，double，2位小数
			if (jsonData.get("way_ind_fee")==null||StringUtils.isBlank(jsonData.getString("way_ind_fee"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "way_ind_fee is NULL");
			}
			//税费，浮点数，double，2位小数
			/*if (jsonData.get("way_tax_fee")==null||StringUtils.isBlank(jsonData.getString("way_tax_fee"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "way_tax_fee is NULL");
			}*/
			//优惠减免金额，使用积分、虚拟货币、代金券等非现金支付金额，无则填写"0"。浮点数，double，2位小数
			if (jsonData.get("discount")==null||StringUtils.isBlank(jsonData.getString("discount"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "discount is NULL");
			}
			//仓库编码
			if (jsonData.get("warehouse_id")==null||StringUtils.isBlank(jsonData.getString("warehouse_id"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "warehouse_id is NULL");
			}
			//申报方案
			if (jsonData.get("declare_plan")==null||StringUtils.isBlank(jsonData.getString("declare_plan"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "declare_plan is NULL");
			}
			//收货人姓名
			if (jsonData.get("receiver_name")==null||StringUtils.isBlank(jsonData.getString("receiver_name"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "receiver_name is NULL");
			}
			//收货人手机号码
			if (jsonData.get("receiver_mobile")==null||StringUtils.isBlank(jsonData.getString("receiver_mobile"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "receiver_mobile is NULL");
			}
			//国家
			if (jsonData.get("receiver_country")==null||StringUtils.isBlank(jsonData.getString("receiver_country"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "receiver_country is NULL");
			}
			//省份
			if (jsonData.get("receiver_state")==null||StringUtils.isBlank(jsonData.getString("receiver_state"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "receiver_state is NULL");
			}
			//市
			if (jsonData.get("receiver_city")==null||StringUtils.isBlank(jsonData.getString("receiver_city"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "receiver_city is NULL");
			}
			//地址
			if (jsonData.get("receiver_address")==null||StringUtils.isBlank(jsonData.getString("receiver_address"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "receiver_address is NULL");
			}
			//  电商企业
			if (jsonData.get("electric_code")==null||StringUtils.isBlank(jsonData.getString("electric_code"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "electric_code is NULL");
			}
			// 电商平台
			if (jsonData.get("cbepcom_code")==null||StringUtils.isBlank(jsonData.getString("cbepcom_code"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "cbepcom_code is NULL");
			}
			// 电商平台不能为空
			if (jsonData.get("shop_id")==null||StringUtils.isBlank(jsonData.getString("shop_id"))) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "shop_id is NULL");
			}
			
			
			JSONArray jsonOrderGoodsList =(JSONArray)jsonData.get("order_goods");
			//商品信息标签，可循环
			if (jsonOrderGoodsList==null||jsonOrderGoodsList.size()==0) {
				return ResponseFactory.error("order_id",jsonData.getString("order_id"), "order_goods is NULL");
			}

			for (int i = 0; i < jsonOrderGoodsList.size(); i++) {
				JSONObject jsonOrderGoods = (JSONObject) jsonOrderGoodsList.get(i);				
				jsonOrderGoods.put("index", i);
				
				//商品货号
				if (jsonOrderGoods.get("goods_id")==null||StringUtils.isBlank(jsonOrderGoods.getString("goods_id"))) {
					return ResponseFactory.error("order_id",jsonData.getString("order_id"), "goods_id is NULL");
				}
				//商品名称，BC申报时，订单与清单名称必须一致
				if (jsonOrderGoods.get("good_name")==null||StringUtils.isBlank(jsonOrderGoods.getString("good_name"))) {
					return ResponseFactory.error("order_id",jsonData.getString("order_id"), "good_name is NULL");
				}
				//实际售价，浮点数，double，2位小数
				if (jsonOrderGoods.get("price")==null||StringUtils.isBlank(jsonOrderGoods.getString("price"))) {
					return ResponseFactory.error("order_id",jsonData.getString("order_id"), "price is NULL");
				}
				//商品数量，整数
				if (jsonOrderGoods.get("num")==null||StringUtils.isBlank(jsonOrderGoods.getString("num"))) {
					return ResponseFactory.error("order_id",jsonData.getString("order_id"), "num is NULL");
				}
				//商品总价，浮点数，double，2位小数。商品数量*实际售价
				if (jsonOrderGoods.get("dec_total")==null||StringUtils.isBlank(jsonOrderGoods.getString("dec_total"))) {
					return ResponseFactory.error("order_id",jsonData.getString("order_id"), "dec_total is NULL");
				}
			}
			JSONObject jsonMQData = b2COrderService.b2COrderInfo(jsonData.toString(),merchantId);
			rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_B2C_ORDER_2.getTopic(),MQOrderEnum.EPASS_B2C_ORDER_2.getTags());
			LOGGER.info("电商交易订单回推接口,推送MQ报文"+jsonMQData.toString());
			
		} catch (Exception e) {
			LOGGER.error("电商交易订单回推接口异常{}", e.getMessage());
			return ResponseFactory.error("order_id", jsonData.getString("order_id"), e.getMessage());
		}
		return ResponseFactory.success("order_id", jsonData.getString("order_id"));
	}

}
