package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.dao.sale.WayBillDao;
import com.topideal.dao.sale.WayBillItemDao;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.entity.vo.sale.WayBillItemModel;
import com.topideal.entity.vo.sale.WayBillModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.service.timer.CrawlerRookieGetOneOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 消费爬虫菜鸟电商订单单个数据
 * @author 杨创
 *2021/03/25
 */
@Service
public class CrawlerRookieGetOneOrderServiceImpl implements CrawlerRookieGetOneOrderService {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerRookieGetOneOrderServiceImpl.class);
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;// 商家信息
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;//仓库
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品信息
	@Autowired
	private OrderDao orderDao;// 电商订单
	@Autowired
	private OrderItemDao orderItemDao;// 电商订单表体
	@Autowired
	private WayBillDao wayBillDao;// 运单表
	@Autowired
	private WayBillItemDao wayBillItemDao;// 运单表体
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;

	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao;// 商家和店铺中间表

	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private MerchantShopShipperMongoDao merchantShopShipperMongoDao;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;

	/**
	 * 单个插入
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201140003, model = DERP_LOG_POINT.POINT_13201140003_Label,keyword="tradeId")
	public void insertOrder(String json,String keys,String topics,String tags) throws Exception {
		JSONObject jSONOrderObject = JSONObject.fromObject(json);
		//数据校验
		dataCheck(jSONOrderObject);
		// 获取店铺
		String shopId = jSONOrderObject.getString("shopId");
		MerchantShopRelMongo merchantShopRelMongo=getMerchantShopRel(shopId);
		// 获取商家
		MerchantInfoMongo merchantInfoMongo=getMerchantInfoMongdb(merchantShopRelMongo);
		//获取仓库
		DepotInfoMongo depotInfoMongo=getDepotInfoMongo(merchantShopRelMongo.getDepotId());
		JSONArray itemList = jSONOrderObject.getJSONArray("itemList");
		// 订单表头随机取一条
		JSONObject jsonData = (JSONObject) itemList.get(0);
		Timestamp deliveryTime = TimeUtils.parse(jsonData.getString("consignTime"),"yyyy-MM-dd HH:mm:ss");//发货时时间
		//校验是否关账
		CheckCloseAccounts(merchantInfoMongo,depotInfoMongo,deliveryTime);

		// 封装电商订单表头
		OrderModel orderModel = getOrderModel(jsonData, merchantInfoMongo, depotInfoMongo,merchantShopRelMongo);
		// 封装运单表头
		WayBillModel wayBillModel = getWayBill(orderModel);


		// 获取所有商品的 总金额
		BigDecimal itemTotalAmount=getitemTotalAmount(merchantInfoMongo,itemList);


		// 推送库存表体
		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		//电商订单表体
		List<OrderItemModel> orderItemList=new ArrayList<>();
		//运单表体
		List<WayBillItemModel> wayItemList=new ArrayList<>();
		// 封装订单表体  运单表体 推送库存表体
		getItemModel(orderModel,itemList,itemTotalAmount,merchantInfoMongo,merchantShopRelMongo,orderItemList,wayItemList,invetAddOrSubtractGoodsListJsonList);

		//计算每个品的金额数据

		OrderModel orderQuery=new OrderModel();
		orderQuery.setExternalCode(jSONOrderObject.getString("tradeId"));
		orderQuery = orderDao.searchByModel(orderQuery);

		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(jSONOrderObject.getString("tradeId"));
			orderExternalCodeModel.setOrderSource(1);	// 电商订单
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			throw new RuntimeException("爬虫电商订单外部单号来源表已经存在 单号：" + jSONOrderObject.getString("tradeId") + "  保存失败");
		}
		// 新增订单
		Long id = orderDao.save(orderModel);
		// 向运单表中插入数据
		wayBillModel.setOrderId(orderModel.getId());//订单id
		wayBillDao.save(wayBillModel);
		// 新增订单表体
		for (OrderItemModel orderItemModel : orderItemList) {
			orderItemModel.setOrderId(orderModel.getId());//订单id
			orderItemDao.save(orderItemModel);
		}
		// 新增运单表体
		for (WayBillItemModel wayBillItemModel : wayItemList) {
			wayBillItemModel.setBillId(wayBillModel.getId());//运单id
			// 运单表体插入数据
			wayBillItemDao.save(wayBillItemModel);
		}

		//扣减电商订单出库库存量
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractRootJson.setMerchantId(String.valueOf(merchantInfoMongo.getMerchantId()));
		invetAddOrSubtractRootJson.setMerchantName(merchantInfoMongo.getName());
		invetAddOrSubtractRootJson.setTopidealCode(merchantInfoMongo.getTopidealCode());
		invetAddOrSubtractRootJson.setDepotId(String.valueOf(depotInfoMongo.getDepotId()));
		invetAddOrSubtractRootJson.setDepotName(depotInfoMongo.getName());
		invetAddOrSubtractRootJson.setDepotCode(depotInfoMongo.getCode());
		invetAddOrSubtractRootJson.setDepotType(depotInfoMongo.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(depotInfoMongo.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(orderModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(orderModel.getExternalCode());
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);
		invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_008);
		invetAddOrSubtractRootJson.setSourceDate(TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss"));
		invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.format(orderModel.getDeliverDate(), "yyyy-MM-dd HH:mm:ss"));
		invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(orderModel.getDeliverDate()));
		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
		//电商 订单回调设置
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTags());//回调标签
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTopic());//回调主题
		customParam.put("code", orderModel.getCode());// 电商订单内部单号
		invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
		invetAddOrSubtractRootJson.setStorePlatformName(orderModel.getStorePlatformName());	// 电商平台编码
		invetAddOrSubtractRootJson.setShopCode(orderModel.getShopCode());// 店铺编码
		// 推送库存加减接口
		rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
	}

	/**
	 * 获取所有商品总金额
	 * @param merchantInfoMongo
	 * @param itemList
	 * @return
	 */
	private BigDecimal getitemTotalAmount(MerchantInfoMongo merchantInfoMongo, JSONArray itemList) {
		BigDecimal itemTotalAmount=new BigDecimal(0);
		for (Object goodsObject : itemList) {
			JSONObject goodsJSONObject=(JSONObject) goodsObject;
			String quantity = goodsJSONObject.getString("quantity");// 数量
			String goodsNo = goodsJSONObject.getString("goodsCode");// 商品货号
			Map<String, Object> merchandiseInfoMap = new HashMap<>();
			merchandiseInfoMap.put("merchantId", merchantInfoMongo.getMerchantId());//商品id
			merchandiseInfoMap.put("goodsNo", goodsNo);//商品货号
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 查询商品信息
			if (merchandiseInfoMogo==null) {
				throw new RuntimeException("根据商家和货号没有查询到商品信息:货号"+goodsNo+",商家id:"+merchantInfoMongo.getMerchantId());
			}
			BigDecimal filingPrice = merchandiseInfoMogo.getFilingPrice();
			if (filingPrice==null)filingPrice=new BigDecimal(0);
			BigDecimal amont=filingPrice.multiply(new BigDecimal(quantity));
			itemTotalAmount=itemTotalAmount.add(amont);
		}
		return itemTotalAmount;
	}

	/**
	 * 封装订单表体 运单表体 推送库存表体
	 * @param itemList
	 * @param merchantInfoMongo
	 * @param merchantShopRelMongo
	 * @param orderItemList
	 * @param wayItemList
	 * @param invetAddOrSubtractGoodsListJsonList
	 */
	private void getItemModel(OrderModel orderModel,JSONArray itemList,BigDecimal itemTotalAmount, MerchantInfoMongo merchantInfoMongo,
			MerchantShopRelMongo merchantShopRelMongo, List<OrderItemModel> orderItemList,
			List<WayBillItemModel> wayItemList,
			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList) {
		BigDecimal payment = orderModel.getPayment();
		BigDecimal taxes = orderModel.getTaxes();
		BigDecimal wayFrtFee = orderModel.getWayFrtFee();
		if (payment==null)payment=new BigDecimal(0);
		if (taxes==null)taxes=new BigDecimal(0);
		if (wayFrtFee==null)wayFrtFee=new BigDecimal(0);

		BigDecimal orderAmount=payment.subtract(taxes).subtract(wayFrtFee);
		// 防止除0
		if (itemTotalAmount.compareTo(BigDecimal.ZERO)==0) itemTotalAmount=new BigDecimal(1);
		BigDecimal orderAmountTemp=new BigDecimal(0);
		BigDecimal wayFrtFeeTemp=new BigDecimal(0);
		BigDecimal taxesTemp=new BigDecimal(0);
		BuStockLocationTypeConfigMongo stockLocationMongo = null;
		if(merchantShopRelMongo.getStockLocationTypeId() != null){
			Map<String,Object> stockLocationMap = new HashMap<>();
			stockLocationMap.put("buStockLocationTypeId", merchantShopRelMongo.getStockLocationTypeId());
			stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
			stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);
		}
		for (int i = 0; i < itemList.size(); i++) {
			Object goodsObject = itemList.get(i);
			JSONObject goodsJSONObject=(JSONObject) goodsObject;
			String quantity = goodsJSONObject.getString("quantity");// 数量
			String goodsNo = goodsJSONObject.getString("goodsCode");// 商品货号
			Map<String, Object> merchandiseInfoMap = new HashMap<>();
			merchandiseInfoMap.put("merchantId", merchantInfoMongo.getMerchantId());//商品id
			merchandiseInfoMap.put("goodsNo", goodsNo);//商品货号
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 查询商品信息
			if (merchandiseInfoMogo==null) {
				throw new RuntimeException("根据商家和货号没有查询到商品信息:货号"+goodsNo+",商家id:"+merchantInfoMongo.getMerchantId() + ",店铺编码:"+merchantShopRelMongo.getShopCode());
			}
			BigDecimal filingPrice = merchandiseInfoMogo.getFilingPrice();
			if (filingPrice==null)filingPrice=new BigDecimal(0);
			BigDecimal itemOrderAmount=new BigDecimal(0);
			BigDecimal itemWayFrtFeeAmount=new BigDecimal(0);
			BigDecimal itemTaxesAmount=new BigDecimal(0);
			if (i==itemList.size()-1) {
				itemOrderAmount = orderAmount.subtract(orderAmountTemp).setScale(2,BigDecimal.ROUND_HALF_UP);
				itemWayFrtFeeAmount = wayFrtFee.subtract(wayFrtFeeTemp).setScale(2,BigDecimal.ROUND_HALF_UP);
				itemTaxesAmount = taxes.subtract(taxesTemp).setScale(2,BigDecimal.ROUND_HALF_UP);
			}else {
				itemOrderAmount=new BigDecimal(quantity).multiply(filingPrice).multiply(orderAmount).divide(itemTotalAmount, 2, BigDecimal.ROUND_HALF_UP);
				orderAmountTemp=orderAmountTemp.add(itemOrderAmount);
				itemWayFrtFeeAmount=new BigDecimal(quantity).multiply(filingPrice).multiply(wayFrtFee).divide(itemTotalAmount, 2, BigDecimal.ROUND_HALF_UP);
				wayFrtFeeTemp=wayFrtFeeTemp.add(itemWayFrtFeeAmount);

				itemTaxesAmount=new BigDecimal(quantity).multiply(filingPrice).multiply(taxes).divide(itemTotalAmount, 2, BigDecimal.ROUND_HALF_UP);
				taxesTemp=taxesTemp.add(itemTaxesAmount);
			}

			BigDecimal price=itemOrderAmount.divide(new BigDecimal(quantity), 2, BigDecimal.ROUND_HALF_UP);
			// 获取原货号
			// 插入电商表体
			OrderItemModel 	orderItemModel =new OrderItemModel();
			//orderItemModel.setSkuId(skuId);
			//orderItemModel.setOrderId(orderModel.getId());//电商订单ID'
			orderItemModel.setGoodsName(merchandiseInfoMogo.getName());//商品名称
			orderItemModel.setGoodsId(merchandiseInfoMogo.getMerchandiseId());//商品ID
			orderItemModel.setGoodsCode(merchandiseInfoMogo.getGoodsCode());//商品编码
			orderItemModel.setBarcode(merchandiseInfoMogo.getBarcode());//条形码
			orderItemModel.setGoodsNo(merchandiseInfoMogo.getGoodsNo());//商品货号
			orderItemModel.setCommbarcode(merchandiseInfoMogo.getCommbarcode());//商品标准条码
			orderItemModel.setTax(itemTaxesAmount);//商品税费
			orderItemModel.setWayFrtFee(itemWayFrtFeeAmount);
			orderItemModel.setPrice(price);// 单价
			orderItemModel.setDecTotal(itemOrderAmount);// 总价
			orderItemModel.setGoodsDiscount(new BigDecimal(0));// 商品优惠减免金额
			orderItemModel.setOriginalPrice(price);// 单价
			orderItemModel.setOriginalDecTotal(itemOrderAmount);
			orderItemModel.setNum(Integer.valueOf(quantity));// 数量
			orderItemModel.setBuId(merchantShopRelMongo.getBuId());
			orderItemModel.setBuName(merchantShopRelMongo.getBuName());
			if(stockLocationMongo != null){
				orderItemModel.setStockLocationTypeId(stockLocationMongo.getBuStockLocationTypeId());
				orderItemModel.setStockLocationTypeName(stockLocationMongo.getName());
			}
			orderItemList.add(orderItemModel);

			// 向运单表体中查入数据
			WayBillItemModel wayBillItemModel = new WayBillItemModel();
			//wayBillItemModel.setBillId(wayBillModel.getId());
			wayBillItemModel.setGoodsId(merchandiseInfoMogo.getMerchandiseId());
			wayBillItemModel.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
			wayBillItemModel.setGoodsName(merchandiseInfoMogo.getName());
			wayBillItemModel.setGoodsCode(merchandiseInfoMogo.getGoodsCode());
			wayBillItemModel.setBarcode(merchandiseInfoMogo.getBarcode());
			wayBillItemModel.setNum(Integer.valueOf(quantity));
			wayBillItemModel.setPrice(price);
			wayItemList.add(wayBillItemModel);



			MerchandiseInfoMogo orgMerchandiseMogo=null;
			InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
			invetAddOrSubtractGoodsListJson.setGoodsId(merchandiseInfoMogo.getMerchandiseId().toString());
			invetAddOrSubtractGoodsListJson.setGoodsName(merchandiseInfoMogo.getName());
			invetAddOrSubtractGoodsListJson.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
			invetAddOrSubtractGoodsListJson.setBarcode(merchandiseInfoMogo.getBarcode());
			invetAddOrSubtractGoodsListJson.setType("0");// 好品
			invetAddOrSubtractGoodsListJson.setNum(Integer.valueOf(quantity));
			invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
			invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）
			//事业部
			invetAddOrSubtractGoodsListJson.setBuId(merchantShopRelMongo.getBuId().toString());
			invetAddOrSubtractGoodsListJson.setBuName(merchantShopRelMongo.getBuName());
			if(stockLocationMongo != null){
				invetAddOrSubtractGoodsListJson.setStockLocationTypeId(stockLocationMongo.getBuStockLocationTypeId().toString());
				invetAddOrSubtractGoodsListJson.setStockLocationTypeName(stockLocationMongo.getName());
			}
			invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
		}

	}
	/**
	 * 校验是否关账
	 * @param merchantInfoMongo
	 * @param depotInfoMongo
	 * @param deliveryTime
	 */
	private void CheckCloseAccounts(MerchantInfoMongo merchantInfoMongo, DepotInfoMongo depotInfoMongo,
			Timestamp deliveryTime) {
		// 判断发货日期是否小于 关账月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(merchantInfoMongo.getMerchantId());
		closeAccountsMongo.setDepotId(depotInfoMongo.getDepotId());
		String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 关账下个月日期大于 入库日期
			if (deliveryTime.getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("归属时间必须大于关账日期/月结日期");
			}
		}

	}
	/**
	 * 获取运单
	 */
	private WayBillModel getWayBill(OrderModel orderModel) {
		WayBillModel wayBillModel = new WayBillModel();
		//wayBillModel.setOrderId(orderModel.getId());
		wayBillModel.setWayBillNo(orderModel.getWayBillNo());// 自定义运单信息
		wayBillModel.setDeliverDate(orderModel.getDeliverDate());
		wayBillModel.setLogisticsName(orderModel.getLogisticsName());//物流公司编码
		wayBillModel.setMerchantId(orderModel.getMerchantId());// 商家id
		wayBillModel.setMerchantName(orderModel.getMerchantName());// 商家名称
		return wayBillModel;

	}
	/**
	 * 封装电商订单表头
	 * @param jSONOrderObject
	 * @return
	 * @throws Exception
	 */
	private OrderModel getOrderModel(JSONObject jSONOrderObject,MerchantInfoMongo merchantInfoMongo,
			DepotInfoMongo depotInfoMongo,MerchantShopRelMongo merchantShopRel) throws Exception {
		//交易时间
		Timestamp tradeTime=null;
		if (jSONOrderObject.get("tradeTime")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("tradeTime"))) {
			tradeTime = TimeUtils.StringToTimestamp(jSONOrderObject.getString("tradeTime"));
		}
		//发货时间
		Timestamp consignTime= TimeUtils.StringToTimestamp(jSONOrderObject.getString("consignTime"));;
		//外部交易单号
		String tradeId=jSONOrderObject.getString("tradeId");
		//物流企业名称
		String logisticEnterprise=null;
		if (jSONOrderObject.get("logisticEnterprise")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("logisticEnterprise"))) {
			logisticEnterprise=jSONOrderObject.getString("logisticEnterprise");
		}
		//运单号
		String wayBillNo=null;
		if (jSONOrderObject.get("waybillNo")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("waybillNo"))) {
			wayBillNo = jSONOrderObject.getString("waybillNo");
		}else {
			wayBillNo = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZDY);// 自定义运单;
		}

		// 收件人
		String consignee=null;
		if (jSONOrderObject.get("consignee")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("consignee"))) {
			consignee=jSONOrderObject.getString("consignee");

		}
		//手机号
		String phoneNumber=null;
		if (jSONOrderObject.get("phoneNumber")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("phoneNumber"))) {
			phoneNumber=jSONOrderObject.getString("phoneNumber");
		}
		//省份
		String province=null;
		if (jSONOrderObject.get("province")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("province"))) {
			province=jSONOrderObject.getString("province");
		}
		//市/区
		String cityAndDistrict=null;
		if (jSONOrderObject.get("cityAndDistrict")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("cityAndDistrict"))) {
			cityAndDistrict=jSONOrderObject.getString("cityAndDistrict");
		}
		//详细地址
		String address=null;
		if (jSONOrderObject.get("address")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("address"))) {
			address=jSONOrderObject.getString("address");
		}
		//实付金额
		BigDecimal amount=new BigDecimal(0);
		if (jSONOrderObject.get("amount")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("amount"))) {
			amount=new BigDecimal(jSONOrderObject.getString("amount"));
		}
		//运费
		BigDecimal freight=new BigDecimal(0);
		if (jSONOrderObject.get("freight")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("freight"))) {
			freight=new BigDecimal(jSONOrderObject.getString("freight"));
		}
		//税费
		BigDecimal tax=new BigDecimal(0);
		if (jSONOrderObject.get("tax")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("tax"))) {
			tax=new BigDecimal(jSONOrderObject.getString("tax"));
		}
		//创建时间
		Timestamp createdAt=null;
		if (jSONOrderObject.get("createdAt")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("createdAt"))) {
			createdAt = TimeUtils.StringToTimestamp(jSONOrderObject.getString("createdAt"));
		}

		OrderModel orderModel =new OrderModel();
		orderModel.setTradingDate(tradeTime);// 交易时间
		orderModel.setLogisticsName(logisticEnterprise);//物流企业编码
		orderModel.setWayBillNo(wayBillNo);
		orderModel.setReceiverName(consignee);// 收件人名称
		orderModel.setReceiverTel(phoneNumber);// 收件人手机
		orderModel.setExternalCode(tradeId);// 外部单号
		orderModel.setLogisticsName(logisticEnterprise);//物流企业编码
		orderModel.setReceiverProvince(province);// 收件人省份
		orderModel.setReceiverCity(cityAndDistrict);// 城市
		orderModel.setReceiverArea(cityAndDistrict);// 区
		orderModel.setReceiverAddress(address);// 地址
		orderModel.setOrderSource(5);//订单来源  1:跨境宝推送, 2:导入',3 第e仓  4 订单100
		orderModel.setCurrency(DERP.CURRENCYCODE_CNY);	// 默认人民币
		orderModel.setWayFrtFee(freight);//运费，2位小数
		orderModel.setPayment(amount);//订单实付金额
		orderModel.setTaxes(tax);
		orderModel.setWayIndFee(new BigDecimal(0));
		orderModel.setDiscount(new BigDecimal(0));
		orderModel.setDeliverDate(consignTime);// 发货时间
		orderModel.setMakingTime(createdAt);// 制单时间
		orderModel.setCustomerId(merchantShopRel.getCustomerId());
		orderModel.setCustomerName(merchantShopRel.getCustomerName());
		orderModel.setStorePlatformName(merchantShopRel.getStorePlatformCode());		// 电商平台编码
		orderModel.setShopCode(merchantShopRel.getShopCode());// 店铺编码
		orderModel.setShopName(merchantShopRel.getShopName());// 店铺名称
		orderModel.setShopTypeCode(merchantShopRel.getShopTypeCode());// 店铺类型编码
		orderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DSDD)); // 电商订单编码
		orderModel.setStatus(DERP_ORDER.ORDER_STATUS_027);//单据状态：1:待申报 2.待放行,3:待发货,4:已发货,5:已取消' 027 出库中
		orderModel.setDepotId(depotInfoMongo.getDepotId());// 仓库id
		orderModel.setDepotName(depotInfoMongo.getName());// 仓库名称
		orderModel.setMerchantId(merchantInfoMongo.getMerchantId());// 商家id
		orderModel.setMerchantName(merchantInfoMongo.getName());// 商家名称
		orderModel.setWayBillNo(wayBillNo);
		return orderModel;
	}

	/**
	 * 数据校验
	 * @param jSONOrderObject
	 * @param jsonGoodArray
	 */
	private void dataCheck(JSONObject jSONOrderObject) {
		if (jSONOrderObject.get("tradeId")==null||StringUtils.isBlank(jSONOrderObject.getString("tradeId"))) {
			throw new RuntimeException("tradeId is null");
		}
		if (jSONOrderObject.get("itemList")==null) {
			throw new RuntimeException("itemList is null");
		}
		//店铺id
		if (jSONOrderObject.get("shopId")==null||StringUtils.isBlank(jSONOrderObject.getString("shopId"))) {
			throw new RuntimeException("shopId is null");
		}
		//店铺编码
		if (jSONOrderObject.get("shopCode")==null||StringUtils.isBlank(jSONOrderObject.getString("shopCode"))) {
			throw new RuntimeException("shopCode is null");
		}
		String tradeId =  jSONOrderObject.getString("tradeId"); //外部单号
		JSONArray itemList = jSONOrderObject.getJSONArray("itemList");

		if (itemList.size()==0) {
			throw new RuntimeException("商品数据为空");
		}
		// 订单表头随机取一条
		JSONObject jsonData = (JSONObject) itemList.get(0);
		//发货时间
		if (jsonData.get("tradeTime")==null||StringUtils.isBlank(jsonData.getString("tradeTime"))) {
			throw new RuntimeException("tradeTime is null");
		}
		Timestamp deliveryTime = TimeUtils.parse(jsonData.getString("consignTime"),"yyyy-MM-dd HH:mm:ss");//发货时时间
		if (jsonData.get("tradeTime")!=null&&StringUtils.isNotBlank(jsonData.getString("tradeTime"))) {
			Timestamp tradeTime = TimeUtils.StringToTimestamp(jsonData.getString("tradeTime"));
			if (deliveryTime.getTime()<tradeTime.getTime()) {
				throw new RuntimeException("发货时间consignTime不能小于交易时间tradeTime");
			}
		}
		//校验表体
		for (Object object : itemList) {
			JSONObject itemJson=(JSONObject) object;
			if (itemJson.get("quantity")==null||StringUtils.isBlank(itemJson.getString("quantity"))) {
				throw new RuntimeException("quantity is null");
			}
			if (Integer.valueOf(itemJson.getString("quantity")).intValue()<=0) {
				throw new RuntimeException("数量必须大于0");
			}
			/*
			 * if (itemJson.get("goodsId")==null||StringUtils.isBlank(itemJson.getString(
			 * "goodsId"))) { throw new RuntimeException("goodsId is null"); }
			 */
			if (itemJson.get("goodsCode")==null||StringUtils.isBlank(itemJson.getString("goodsCode"))) {
				throw new RuntimeException("goodsCode is null");
			}
		}

	}

	/**
	 * 获取仓库
	 * @param depotId
	 * @return
	 */
	private DepotInfoMongo getDepotInfoMongo(Long depotId) {
		Map<String, Object> depotInfoMap = new HashMap<>();
		depotInfoMap.put("depotId",depotId);//仓库id
		List<DepotInfoMongo> depotInfoMongoList = depotInfoMongoDao.findAll(depotInfoMap);// 仓库信息
		if(depotInfoMongoList==null||depotInfoMongoList.size()==0)throw new RuntimeException("根据仓库id："+depotId+"没找到仓库信息");
		if (depotInfoMongoList.size()>1)throw new RuntimeException("根据仓库id："+depotId+"找到多条仓库信息");
		return depotInfoMongoList.get(0);
	}

	/**
	 * 获取商家信息
	 * @param merchantShopRel
	 * @return
	 */
	private MerchantInfoMongo getMerchantInfoMongdb(MerchantShopRelMongo merchantShopRel) {
		// 根据商家id查询商家信息
		Long merchantId=null;// 存放商家ID
		/*if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRel.getStoreTypeCode())){//如果为外部店就要查货主表
			// 根据店铺编码查找商家店铺关联表中拿到店铺id，然后根据店铺id到店铺货主表查到对应数据，若有多条则报错
			Map<String, Object> shopShipperParams = new HashMap<String, Object>();
			shopShipperParams.put("shopId", merchantShopRel.getShopId()) ;	// 店铺ID
			List<MerchantShopShipperMongo> merchantShopShipperList = merchantShopShipperMongoDao.findAll(shopShipperParams);
			if(merchantShopShipperList==null || merchantShopShipperList.size()==0){
				throw new RuntimeException("根据店铺编码："+merchantShopRel.getShopCode()+"在店铺货主表没有找到对应信息");
			}
			HashSet<Long> merchantSet = new HashSet<>();
			for (MerchantShopShipperMongo shipper: merchantShopShipperList) {
				merchantSet.add(shipper.getMerchantId());
			}
			if (merchantSet.size()>1)throw new RuntimeException("根据店铺编码："+merchantShopRel.getShopCode()+"在店铺货主表找到多条不同货主信息");
			MerchantShopShipperMongo merchantShopShipper=merchantShopShipperList.get(0);
			merchantId=merchantShopShipper.getMerchantId();
		}*/

		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRel.getStoreTypeCode())){ // 如果为单主店直接查询关联表的商家id
			merchantId=merchantShopRel.getMerchantId();
		}
		if (merchantId==null) {
			throw new RuntimeException("没有找到对应的商家id");
		}

		// 查询商家
		Map<String, Object> merchantMap = new HashMap<>();
		merchantMap.put("merchantId", merchantId); // 商家id
		List<MerchantInfoMongo> merchantInfoMongoList = merchantInfoMongoDao.findAll(merchantMap);// 商家信息
		if(merchantInfoMongoList==null||merchantInfoMongoList.size()==0)throw new RuntimeException("根据商家id："+merchantId+"没找到商家信息");
		if (merchantInfoMongoList.size()>1)throw new RuntimeException("根据商家id："+merchantId+"找到多条商家信息");
		return merchantInfoMongoList.get(0);
	}

	/**
	 * 获取商家店铺关系
	 * @param derpShopCode
	 * @return
	 */
	private MerchantShopRelMongo getMerchantShopRel(String shopId) {
		Map<String, Object> merchantShopRelMap = new HashMap<>();
		merchantShopRelMap.put("shopId",Long.valueOf(shopId));
		merchantShopRelMap.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1);//'状态(1使用中,0已禁用)'
		merchantShopRelMap.put("dataSourceCode", DERP.DATASOURCE_5);	// 数据来源：爬虫
		List<MerchantShopRelMongo> merchantShopRelList = merchantShopRelMongoDao.findAll(merchantShopRelMap);
		if (merchantShopRelList.size()>1) {
			throw new RuntimeException("查到多个数据来源是爬虫的店铺数据");
		}
		if (merchantShopRelList.size()==0) {
			throw new RuntimeException("没有查询到数量来源是爬虫的店铺数据");
		}
		MerchantShopRelMongo merchantShopRelMongo = merchantShopRelList.get(0);
		if (!DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(merchantShopRelMongo.getShopTypeCode())) {
			throw new RuntimeException("运营类型必须是POP店");

		}
		if (!DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode())) {
			throw new RuntimeException("店铺类型必须是单主店");
		}
		return merchantShopRelMongo;
	}


}
