package com.topideal.service.epass.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.main.DepotInfoDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.dao.main.MerchantShopRelDao;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.service.epass.B2COrderService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * B2C订单接口
 * 
 * @author 杨创 2018/6/4
 */
@Service
public class B2COrderServiceImpl implements B2COrderService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(B2COrderServiceImpl.class);
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	@Autowired
	private DepotInfoDao depotInfoDao;// 仓库部门
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;// 商品信息
	@Autowired
	private MerchantShopRelDao merchantShopRelDao;// 商家与店铺关系表 
	

	@Override
	@SystemServiceLog(point = "1204", model = "B2C订单接口")
	public JSONObject b2COrderInfo(String json,Long merchantId) throws Exception {
		LOGGER.info("B2C订单接口,请求开始json" + json);
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		
		// 实例化推送MQ的jJSON 对象
		JSONObject jsonMQData = new JSONObject();

		// 根据电商企业查询商家
		MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
		merchantInfoModel.setTopidealCode((String) jsonData.get("electric_code"));// 卓志编码
		merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);

		if (merchantInfoModel == null) {
			LOGGER.error("商家不存在,卓志编码" + (String) jsonData.get("electric_code"));
			throw new RuntimeException("商家不存在,商家卓志编码" + (String) jsonData.get("electric_code"));
		}
		if (merchantId==null||merchantId.intValue()!=merchantInfoModel.getId().intValue()) {			
			LOGGER.error("electric_code对应的商家 和app_key对应的商家不是同一商家");
			throw new RuntimeException("electric_code对应的商家 和app_key对应的商家不是同一商家");
		}
		// 0商家 1 代理商 
		if (DERP_SYS.MERCHANTINFO_ISPROXY_1.equals(merchantInfoModel.getIsProxy())) {
			LOGGER.error("B2C订单接口商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));
			throw new RuntimeException("B2C订单接口商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));	
	
		}		
		jsonMQData.put("merchantId", merchantInfoModel.getId());// 商家id
		jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称

		// 仓库编码 根据仓库编码查询仓库 可以获取申报方案
		DepotInfoModel depotInfoModel = new DepotInfoModel();
		String warehouseId = jsonData.getString("warehouse_id");
		depotInfoModel.setCode(warehouseId);
		depotInfoModel.setIsValetManage(DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_0);//  b2c订单查询非代客管理的仓库
		//depotInfoModel.setIsTopBooks(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);
		depotInfoModel = depotInfoDao.searchByModel(depotInfoModel);
		if (null == depotInfoModel) {
			LOGGER.error("仓库经不存在,仓库编码" + jsonData.getString("warehouse_id"));
			throw new RuntimeException("仓库经不存在,仓库编码" + jsonData.getString("warehouse_id"));
		}

		String shopId = jsonData.getString("shop_id");// 电商平台编码
		MerchantShopRelModel merchantShopRelModel=new MerchantShopRelModel();
		merchantShopRelModel.setShopCode(shopId);
		merchantShopRelModel.setStatus(DERP_SYS.MERCHANTSHOPREL_STATUS_1);
		merchantShopRelModel.setDataSourceCode(DERP.DATASOURCE_1);
		merchantShopRelModel = merchantShopRelDao.searchByModel(merchantShopRelModel);
		if (merchantShopRelModel==null) {
			LOGGER.error("根据店铺编码没有查询到商家于店铺关系表信息" + jsonData.getString("shop_id"));
			throw new RuntimeException("根据店铺编码没有查询到商家于店铺关系表信息" + jsonData.getString("shop_id"));
		}
		
		jsonMQData.put("depotId", depotInfoModel.getId());// 仓库id
		jsonMQData.put("depotName", depotInfoModel.getName());// 仓库名称
		jsonMQData.put("externalCode", (String) jsonData.get("order_id"));// 订单号，不可重复
		jsonMQData.put("wayBillNo", (String) jsonData.get("way_bill_no"));// 运单号(非必填)
		jsonMQData.put("tradingDate", (String) jsonData.get("created"));// 订单时间，格式:yyyy-MM-dd
																		// HH:mm:ss。(交易时间)
		jsonMQData.put("logisticsName", (String) jsonData.get("logistics_code"));// 物流企业编码
		// 申报方案 id 暂时无法存
		jsonMQData.put("storePlatformName", merchantShopRelModel.getStorePlatformCode());// 电商平台编码
		jsonMQData.put("makerRegisterNo", (String) jsonData.get("buyer_reg_no"));// 订购人注册号,																					// 订购人在电商平台唯一注册号
		jsonMQData.put("makerTel", (String) jsonData.get("buyer_telephone"));// 订购人电话号码
		jsonMQData.put("makerName", (String) jsonData.get("buyer_name"));// 订购人姓名
		jsonMQData.put("receiverName", (String) jsonData.get("receiver_name"));// 收货人姓名
		jsonMQData.put("receiverTel", (String) jsonData.get("receiver_mobile"));// 收货人手机号码
		jsonMQData.put("receiverCountry", (String) jsonData.get("receiver_country"));// 国家
		jsonMQData.put("receiverProvince", (String) jsonData.get("receiver_state"));// 省份
		jsonMQData.put("receiverCity", (String) jsonData.get("receiver_city"));// 市
		jsonMQData.put("receiverArea", (String) jsonData.get("receiver_district"));// 区县
		jsonMQData.put("receiverAddress", (String) jsonData.get("receiver_address"));// 地址
		jsonMQData.put("remark", (String) jsonData.get("notes"));// 备注
		jsonMQData.put("wayFrtFee", jsonData.get("way_frt_fee"));// 运费，2位小数
		jsonMQData.put("wayIndFee", jsonData.get("way_ind_fee"));// 保税,2位小数
		
		String taxTotal="0";
		if (jsonData.get("tax_total")!=null&&StringUtils.isNotBlank(jsonData.getString("tax_total"))&&!"null".equals(jsonData.getString("tax_total"))) {
			taxTotal=jsonData.getString("tax_total");
		}
		jsonMQData.put("taxes",  taxTotal);// 税费,2位小数
		jsonMQData.put("discount", jsonData.get("discount"));// 优惠减免金额
		jsonMQData.put("goodsAmount", jsonData.get("payment_goods"));// 货款
		jsonMQData.put("exOrderId", jsonData.get("exorder_id"));// 平台订单号
		
		jsonMQData.put("shopCode", merchantShopRelModel.getShopCode());// 店铺编码		
		jsonMQData.put("shopName", merchantShopRelModel.getShopName());// 店铺名称	
		jsonMQData.put("shopTypeCode", merchantShopRelModel.getShopTypeCode());// 运营类型
		
		// 获取商品
		JSONArray orderGoodsList = (JSONArray) jsonData.get("order_goods");// 商品信息
		// 承接商品MQ
		JSONArray orderGoodsMQList = new JSONArray();
		// 优惠金额拆分
		Map<String, Double> discountMap = new HashMap<>();
		String discount = jsonData.getString("discount");
		BigDecimal discountBD = new BigDecimal(discount);
		
		BigDecimal wayTaxFeeBD = new BigDecimal(taxTotal).setScale(2, BigDecimal.ROUND_HALF_UP);;		
		String wayFrtFee = jsonData.getString("way_frt_fee");
		BigDecimal wayFrtBD = new BigDecimal(wayFrtFee).setScale(2, BigDecimal.ROUND_HALF_UP);	
		String payment = jsonData.getString("payment");
		BigDecimal paymentBD = new BigDecimal(payment).setScale(2, BigDecimal.ROUND_HALF_UP);
		// 表体订单结算总金额
		/*1、若订单商品行数为1条：payment-tax_total-way_frt_fee
		2、若订单商品行数大于1条：
		1）前(N-1）个商品：dec_total/(dec_total总额)*(payment-tax_total-way_frt_fee)
		2）第N个商品：payment-tax_total-way_frt_fee-前N-1个商品结算总额*/
		
		BigDecimal goodsDecTotal=paymentBD.subtract(wayFrtBD).subtract(wayTaxFeeBD);
		Map<String, BigDecimal> goodsDecTotalMap = getSplitPrice(orderGoodsList, goodsDecTotal);
		for (int i = 0; i < orderGoodsList.size(); i++) {
			JSONObject goodsJSON = (JSONObject) orderGoodsList.get(i);
			BigDecimal numBig = new BigDecimal(goodsJSON.getString("num"));
	        BigDecimal originalPrice = new BigDecimal(goodsJSON.getString("price"));
	        BigDecimal decTotalDouBig = new BigDecimal( goodsJSON.getString("dec_total"));
			// 根据商家id和商品货号查询启用中的商品
			String goodsNo = goodsJSON.getString("goods_id");
			MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
			merchandiseInfoModel.setGoodsNo(goodsNo);// 商品货号
			merchandiseInfoModel.setMerchantId(merchantId);// 商家id
			merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1 );//状态(1使用中,0已禁用)
			merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);
			if (merchandiseInfoModel == null) {
				LOGGER.error("商品不存在，商品货号" + (String) goodsJSON.get("goods_id"));
				throw new RuntimeException("商品不存在，商品货号" + (String) goodsJSON.get("goods_id"));
			}
			// 判断标准条码是否为空
			if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
			}

			// 推MQ的商品JSON
			JSONObject goodsMQJSON = new JSONObject();
			BigDecimal decTotal=new BigDecimal(0);
			if (goodsDecTotalMap.size() > 0) {
				decTotal =goodsDecTotalMap.get(goodsJSON.getString("index")); //平摊后的结算金额
			}


			BigDecimal price = decTotal.divide(numBig, 2, BigDecimal.ROUND_HALF_UP); //结算单价
			goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
			goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());// 商品id
			goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());// 商品名称
			goodsMQJSON.put("num", goodsJSON.get("num"));// 商品数量
			goodsMQJSON.put("decTotal", decTotal);// 结算总金额
			goodsMQJSON.put("price", price);// 结算单价
			goodsMQJSON.put("originalDecTotal", decTotalDouBig);// 销售总价
			goodsMQJSON.put("originalPrice", originalPrice);// 实际售价，浮点数，double，2位小数
			goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());// 货品条码
			goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());// 商品编码
			goodsMQJSON.put("goodsNo", goodsJSON.getString("goods_id")); // 商品货号
			orderGoodsMQList.add(goodsMQJSON);
		}

		//订单实付金额 = 销售总金额合计+税费-优惠金额+运费
		jsonMQData.put("payment", payment);// 订单实付金额
		jsonMQData.put("wayFrtFee",wayFrtFee);	 // 运费
		jsonMQData.put("wayTaxFee",wayTaxFeeBD);	 //税费
		jsonMQData.put("discount",discountBD);	 //优惠金额
		jsonMQData.put("orderGoods", orderGoodsMQList);

		LOGGER.info("B2C订单接口,请求开始json" + jsonMQData.toString());

		return jsonMQData;
	}

	
	
	
	/**
	 * @Description 平摊金额
	 * @param orderGoodsList 商品列表
	 * @param price 需摊分金额
	 * @return
	 */
	public Map<String, BigDecimal> getSplitPrice(JSONArray orderGoodsList, BigDecimal amont) {
		Map<String, BigDecimal> priceMap = new HashMap<>();
		//orderGoodsList.size==1直接返回金额 
		if (orderGoodsList.size()==1) {
			JSONObject goodsJSON = (JSONObject) orderGoodsList.get(0);
			priceMap.put(goodsJSON.getString("index"), amont);
			return priceMap;
		}
		//orderGoodsList.size==1直接返回金额 
		BigDecimal decTotalAll = new BigDecimal("0");
		for (int i = 0; i < orderGoodsList.size(); i++) {
			JSONObject goodsJSON = (JSONObject) orderGoodsList.get(i);
			BigDecimal decTotal = new BigDecimal(goodsJSON.getString("dec_total"));
			decTotalAll=decTotalAll.add(decTotal);
		}
		
		//记录分摊后剩余金额
		BigDecimal decTotalTemp = new BigDecimal("0");
		for (int i = 0; i < orderGoodsList.size(); i++) {
			JSONObject goodsJSON = (JSONObject) orderGoodsList.get(i);			
			BigDecimal decTotal = new BigDecimal(goodsJSON.getString("dec_total"));						
			if (i == orderGoodsList.size()-1) {
				// 第N个商品摊分到的金额=金额-∑前N-1个商品所摊分的金额
				BigDecimal decTotalIndex = amont.subtract(decTotalTemp);
				priceMap.put(goodsJSON.getString("index"), decTotalIndex);
			} else {
				// 前N-1个商品所摊分的金额=当前商品成交金额/∑商品成交金额*优惠金额
				BigDecimal decTotalIndex = amont.multiply(decTotal).divide(decTotalAll, 2, BigDecimal.ROUND_HALF_UP);
				priceMap.put(goodsJSON.getString("index"), decTotalIndex);
				decTotalTemp=decTotalTemp.add(decTotalIndex);
			}
		}
		return priceMap;
	}

}
