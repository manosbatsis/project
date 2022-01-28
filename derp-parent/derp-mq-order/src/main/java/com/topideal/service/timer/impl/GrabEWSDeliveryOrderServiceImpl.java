package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.*;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.log.MQLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j06.InventoryDetailJson;
import com.topideal.json.inventory.j06.InventoryGoodsDetailJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.service.timer.GrabEWSDeliveryOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 抓取寄售商e仓发货订单
 *
 * @author 杨创 2018/09/19
 */
@Service
public class GrabEWSDeliveryOrderServiceImpl implements GrabEWSDeliveryOrderService {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(GrabEWSDeliveryOrderServiceImpl.class);
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
	private WayBillItemDao wayBillItemDao;// 运单表表体
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
	private RMQLogProducer rMQLogProducer;// 日志MQ
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;	// 电商订单外部单号来源
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao;// 商家和店铺中间表
	@Autowired
	private MerchantShopShipperMongoDao merchantShopShipperMongoDao;
	@Autowired
	private BuInventoryMongoDao buInventoryMongoDao;
	// 事业部移库单表头
	@Autowired
	private BuMoveInventoryDao buMoveInventoryDao;
	// 事业部移库单表体
	@Autowired
	private BuMoveInventoryItemDao buMoveInventoryItemDao;
	// 汇率信息
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao;
	// 协议单价配置
	@Autowired
	private AgreementCurrencyConfigDao agreementCurrencyConfigDao;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;
	/**
	 * 订单订单100 单个数据过滤
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13204122200, model = DERP_LOG_POINT.POINT_13204122200_Label,keyword="order_id")
	public void insertDatafilterGrabEWSDeliveryOrder(String json,String keys,String topics,String tags) throws Exception {
		JSONObject jSONOrderObject = JSONObject.fromObject(json);
		//仓库
		if (("null".equals(jSONOrderObject.getString("warehouse_id")))||StringUtils.isBlank(jSONOrderObject.getString("warehouse_id"))) {
			throw new RuntimeException("warehouse_id Is Null");
		}
		// 根据仓库编码和非代销仓查询到mongoDB中查询 仓库信息
		Map<String, Object> depotInfoMap = new HashMap<>();
		depotInfoMap.put("isTopBooks", DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);// 调出仓库id
		depotInfoMap.put("code", jSONOrderObject.getString("warehouse_id"));// 调出仓库id
		depotInfoMap.put("status", DERP_SYS.DEPOTINFO_STATUS_1);
		DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);// 调入仓库信息
		if(depotInfoMongo == null) {
			throw new RuntimeException("根据仓库编码和非卓志仓储状态,没有查询到对应的仓库信息,仓库编码:"+jSONOrderObject.getString("warehouse_id"));
		}
		// 卓志编码
		if ("null".equals(jSONOrderObject.getString("merchant_code"))||StringUtils.isBlank(jSONOrderObject.getString("merchant_code"))) {
			throw new RuntimeException("merchant_code Is Null");
		}
		// 根据卓志编码查询商家信息
		Map<String, Object> merchantMap = new HashMap<>();
		merchantMap.put("topidealCode", jSONOrderObject.getString("merchant_code"));// 卓志编码
		MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantMap);// 商家信息
		if (merchantInfoMongo==null) {
			throw new RuntimeException("更据卓志编码未查到对应的商家信息,卓志编码"+jSONOrderObject.getString("merchant_code"));
		}
		Long merchantId = merchantInfoMongo.getMerchantId();// 商家id
		/*查询所有启用的店铺*/
		Map<String, Object> merchantShopRelMap = new HashMap<>();
		merchantShopRelMap.put("shopCode",jSONOrderObject.getString("shop_id") );
		merchantShopRelMap.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1);//'状态(1使用中,0已禁用)'
		merchantShopRelMap.put("dataSourceCode", DERP.DATASOURCE_3);	// 数据来源：第e仓
		List<MerchantShopRelMongo> merchantShopRelList = merchantShopRelMongoDao.findAll(merchantShopRelMap);

		if (merchantShopRelList.size()==0) {
			throw new RuntimeException("抓取寄售商e仓的店铺编码在商家店铺关联表没有查询到该店铺编码:" + jSONOrderObject.getString("shop_id"));
		}
		MerchantShopRelMongo merchantShopRelMongo = merchantShopRelList.get(0);
		// 第e仓：需以“商家（货主公司）+店铺id”查询店铺信息表维护的货主事业部，若找不到时，报错预警；
		Map<String, Object> shopShipperParams = new HashMap<String, Object>();
		shopShipperParams.put("merchantId", merchantInfoMongo.getMerchantId()) ;
		shopShipperParams.put("shopId", merchantShopRelMongo.getShopId()) ;	// 店铺ID
		List<MerchantShopShipperMongo> merchantShopShipperList = merchantShopShipperMongoDao.findAll(shopShipperParams);
		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRelMongo.getStoreTypeCode())) {// 外部店货主信息不能为空
			if(merchantShopShipperList==null ||merchantShopShipperList.size()==0){
				throw new RuntimeException("货主id:"+merchantInfoMongo.getMerchantId()+"店铺id:"+merchantShopRelMongo.getShopId()+"在店铺货主表没找到对应信息");
			}
		}

		//1. 必填字段校验
		requiredCheck(jSONOrderObject,merchantInfoMongo,depotInfoMongo);

		LOGGER.info("每一条订单的json"+jSONOrderObject);
		String merchantCode = jSONOrderObject.getString("merchant_code");// 卓志编码
		String orderId = jSONOrderObject.getString("order_id");// 外部单号
		String deliverDate = jSONOrderObject.getString("deliver_date");// 发货时间
		String warehouseId = jSONOrderObject.getString("warehouse_id");// 仓库编码
		String logisticsCode = (String)jSONOrderObject.get("logistics_code");// 物流企业编码
		String wayBillNo = jSONOrderObject.getString("way_bill_no");// 运单号
		String created = jSONOrderObject.getString("created");// 订单时间
		//String payTime = jSONOrderObject.getString("pay_time");// 支付时间
		String payment = jSONOrderObject.getString("payment");// 订单实付总额
		String wayFrtFee = jSONOrderObject.getString("way_frt_fee");// 运杂费
		String wayIndFee = jSONOrderObject.getString("way_ind_fee");// 保费

		String buyerRegNo = jSONOrderObject.getString("buyer_reg_no");// 订购人注册号
		String buyerTelephone = jSONOrderObject.getString("buyer_telephone");// 订购人电话号码
		String buyerName = jSONOrderObject.getString("buyer_name");// 订购人姓名
		String receiverName = jSONOrderObject.getString("receiver_name");// 收货人姓名
		String receiverMobile = jSONOrderObject.getString("receiver_mobile");// 收货人手机号码
		String receiverCountry = jSONOrderObject.getString("receiver_country");// 国家
		String receiverState = jSONOrderObject.getString("receiver_state");// 省份
		String receiverCity = jSONOrderObject.getString("receiver_city");// 市
		String receiverDistrict = jSONOrderObject.getString("receiver_district");// 区
		String receiverAddress = jSONOrderObject.getString("receiver_address");// 运单号
		String notes = jSONOrderObject.getString("notes");// 备注
		String shopCode=jSONOrderObject.getString("shop_id");	// 获取店铺编码

		//商品批次信息
		JSONArray jsonGoodArray = jSONOrderObject.getJSONArray("good_list");
		//商品信息
		JSONArray jsonGoodPrArray = jSONOrderObject.getJSONArray("goodpr_list");

		// 判断发货日期是否小于 关账月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(merchantId);
		closeAccountsMongo.setDepotId(depotInfoMongo.getDepotId());
		String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		Timestamp deliverTimestamp = TimeUtils.parseFullTime(jSONOrderObject.getString("deliver_date"));
		if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 关账下个月日期大于 入库日期
			if (deliverTimestamp.getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("归属时间必须大于关账日期/月结日期");
			}
		}

		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderId+""+merchantId);
			orderExternalCodeModel.setOrderSource(1);	// 订单来源  1:电商订单, 2:装载交运 3:销售出库
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("第e仓外部单号来源表已经存在 单号：" + orderId + "  保存失败");
			throw new RuntimeException("第e仓外部单号来源表已经存在 单号：" + orderId + "  保存失败");
		}

		OrderModel orderModel =new OrderModel();
		//插入订单数据
		orderModel.setCustomerId(merchantShopRelMongo.getCustomerId());
		orderModel.setCustomerName(merchantShopRelMongo.getCustomerName());
		orderModel.setStatus(DERP_ORDER.ORDER_STATUS_027);//单据状态：1:待申报 2.待放行,3:待发货,4:已发货,5:已取消' 027 出库中
		orderModel.setExternalCode(orderId);
		orderModel.setDeliverDate(TimeUtils.parseFullTime(deliverDate));
		orderModel.setDepotId(depotInfoMongo.getDepotId());
		orderModel.setDepotName(depotInfoMongo.getName());
		orderModel.setMerchantId(merchantId);
		orderModel.setMerchantName(merchantInfoMongo.getName());
		orderModel.setOrderSource(Integer.valueOf(DERP.DATASOURCE_3));//订单来源  1:跨境宝推送, 2:导入',3 第e仓
//		orderModel.setCode(CodeGeneratorUtil.getNo("DSDD")); // 电商订单编码
		orderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DSDD)); // 电商订单编码
		orderModel.setWayBillNo(wayBillNo);// 运单号
		orderModel.setTradingDate(TimeUtils.parseFullTime(created));//  订单交易时间
		orderModel.setLogisticsName(logisticsCode);// 物流企业名称

		if (!"null".equals(buyerTelephone)&&StringUtils.isNotBlank(buyerTelephone)) {
			orderModel.setMakerTel(buyerTelephone);// 订购人电话
		}
		if (!"null".equals(buyerName)&&StringUtils.isNotBlank(buyerName)) {
			orderModel.setMakerName(buyerName);// 订购人名称
		}
		if (!"null".equals(buyerRegNo)&&StringUtils.isNotBlank(buyerRegNo)) {
			orderModel.setMakerRegisterNo(buyerRegNo);// 订购人注册号
		}
		orderModel.setReceiverName(receiverName);// 收件人名称
		orderModel.setReceiverTel(receiverMobile);// 收件人电话
		orderModel.setReceiverCountry(receiverCountry);// 收件人国家
		orderModel.setReceiverProvince(receiverState);// 省
		orderModel.setReceiverCity(receiverCity);//市
		if (!"null".equals(receiverDistrict)&&StringUtils.isNotBlank(receiverDistrict)) {
			orderModel.setReceiverArea(receiverDistrict);// 区
		}
		orderModel.setReceiverAddress(receiverAddress);//收件人地址
		if (!"null".equals(notes)&&StringUtils.isNotBlank(notes)) {
			orderModel.setRemark(notes);//备注
		}
		if (!"null".equals(wayIndFee)&&StringUtils.isNotBlank(wayIndFee)) {
			BigDecimal wayIndFeeBig=new BigDecimal(wayFrtFee);
			orderModel.setWayFrtFee(wayIndFeeBig);//
		}
		orderModel.setWayFrtFee(BigDecimal.ZERO);//保费
		orderModel.setMakingTime(TimeUtils.parseFullTime(created));//制单时间
		orderModel.setStorePlatformName(merchantShopRelMongo.getStorePlatformCode()); // 平台编码
		orderModel.setShopCode(merchantShopRelMongo.getShopCode());	// 店铺编码
		orderModel.setShopName(merchantShopRelMongo.getShopName()); 	// 店铺名称
		orderModel.setShopTypeCode(merchantShopRelMongo.getShopTypeCode());	// 店铺类型编码
		orderModel.setCurrency(DERP.CURRENCYCODE_CNY);	// 默认人民币

		//检验商品批次信息与商品信息数量是否一致
		Map<String, Integer> batchGoodsNumMap = new HashMap<>();
		Map<String, Integer> goodsNumMap = new HashMap<>();
		// 实付总额为= 每个sku的销售总金额合计+税费+运费+保费-优惠金额；

		/**若接口的店铺ID字段（shop_id）为SK190814110900014（美赞臣DSC）/SK191119181600031（美赞臣D2O），字段取值不同
		 * 商品行的佣金取佣金字段（sale_com），
		 * 商品的销售单价/结算单价取销售价字段(price)，
		 * 商品的销售总金额/结算总额取商品总价（dec_total），
		 * 商品的数量取数量字段（num）
		 * 优惠金额（discount）不为空或0时，摊分到商品金额上，需重算商品成交单价和成交金额
		 *
		 *
		 * 非美赞臣DSC
		 * 表头 运费取0，税费取0，优惠金额取0，实付总额取结算总额
		 * 商品行的佣金取0，
		 * 优惠金额取discount,
		 * 商品的销售单价取代发价字段(delivery_price)，
		 * 商品的数量取数量字段（num），
		 * 商品的销售总金额 = 销售单价 * 数量
		 */
		// 订单佣金
		List<OrderItemModel> orderItemList=new ArrayList<>();
		List<WayBillItemModel> wayBillItemList=new ArrayList<>();
		Map<Long,String> stockLocationMap = new HashMap<Long,String>();//记录库存类型信息
		Map<Long,String> moveGoodsIdMap = new HashMap<>();// 存放需要移库的商品
		if ("SK190814110900014".equals(shopCode) || ("SK191119181600031").equals(shopCode)) {	//美赞臣DSC/美赞臣D2O
			moveGoodsIdMap = getBxmOrderItem(jsonGoodPrArray,jsonGoodArray,merchantInfoMongo,merchantShopRelMongo,
					depotInfoMongo,batchGoodsNumMap,goodsNumMap,orderItemList,wayBillItemList, orderModel,
					jSONOrderObject,merchantShopShipperList,stockLocationMap);
		} else {//非美赞臣DSC
			// 获取电商订单表体
			moveGoodsIdMap = getOrderItem(jsonGoodPrArray,jsonGoodArray,merchantInfoMongo,
					depotInfoMongo,merchantShopRelMongo,batchGoodsNumMap,goodsNumMap,orderItemList,wayBillItemList,
					orderModel,jSONOrderObject,merchantShopShipperList, stockLocationMap);
		}

		if (goodsNumMap.size() != batchGoodsNumMap.size()) {
			throw new RuntimeException("商品信息与批次信息货号数量不一致");
		}
		// 批次效期强校验的比较
		for (String goodsNo : goodsNumMap.keySet()) {
			if (batchGoodsNumMap.get(goodsNo) != goodsNumMap.get(goodsNo)) {
				throw new RuntimeException("商品信息与批次信息同个货号数量不一致:货号"+goodsNo+"商家id:"+merchantId);
			}
		}
		orderDao.save(orderModel);// 保存表头
		// 向运单表中插入数据
		WayBillModel wayBillModel = new WayBillModel();
		wayBillModel.setOrderId(orderModel.getId());
		wayBillModel.setWayBillNo(wayBillNo);
		wayBillModel.setDeliverDate(TimeUtils.parseFullTime(deliverDate));
		wayBillModel.setLogisticsCode(logisticsCode);//物流公司编码
		wayBillModel.setMerchantId(merchantId);// 商家id
		wayBillModel.setMerchantName(merchantInfoMongo.getName());// 商家名称
		wayBillDao.save(wayBillModel);
		// 生成电商订单表体
		for (int i = 0; i < orderItemList.size(); i++) {
			OrderItemModel orderItemModel = orderItemList.get(i);
			orderItemModel.setOrderId(orderModel.getId());
			orderItemDao.save(orderItemModel);
		}

		// 加减库存商品实体
		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		// 生成运单表体
		for (WayBillItemModel wayBillItemModel : wayBillItemList) {
			wayBillItemModel.setBillId(wayBillModel.getId());
			wayBillItemDao.save(wayBillItemModel);

			// 电商订单表体信息
			OrderItemModel itemModel = new OrderItemModel();
			itemModel.setOrderId(wayBillModel.getOrderId());
			itemModel.setGoodsId(wayBillItemModel.getGoodsId());
			List<OrderItemModel> itemAllList = orderItemDao.list(itemModel);

			if(!moveGoodsIdMap.isEmpty()){ // 生成移库单
				if(moveGoodsIdMap.containsKey(wayBillItemModel.getGoodsId())){
					String buInfo = moveGoodsIdMap.get(wayBillItemModel.getGoodsId());
					String[] buInfoStr = buInfo.split(",");
					// 生成移库单
					saveBuMoveInventory(orderModel,itemAllList.get(0),depotInfoMongo,merchantInfoMongo,
							Long.valueOf(buInfoStr[0]),buInfoStr[1],merchantShopRelMongo,wayBillItemModel,Long.valueOf(buInfoStr[2]), stockLocationMap);
				}
			}
			// 推送库存表体
			// 减库存
			InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
			Long goodsId = wayBillItemModel.getGoodsId();
			String goodsName = wayBillItemModel.getGoodsName();
			String goodsNo = wayBillItemModel.getGoodsNo();
			String barcode = wayBillItemModel.getBarcode();

			invetAddOrSubtractGoodsListJson.setGoodsId(goodsId.toString());
			invetAddOrSubtractGoodsListJson.setGoodsName(goodsName);
			invetAddOrSubtractGoodsListJson.setGoodsNo(goodsNo);
			invetAddOrSubtractGoodsListJson.setBarcode(barcode);// 商品条码
			invetAddOrSubtractGoodsListJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）
			invetAddOrSubtractGoodsListJson.setNum(wayBillItemModel.getNum());
			invetAddOrSubtractGoodsListJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 字符串 0 调减 1调增
			invetAddOrSubtractGoodsListJson.setBatchNo(wayBillItemModel.getBatchNo());
			if (wayBillItemModel.getProductionDate()!=null) {
				invetAddOrSubtractGoodsListJson.setProductionDate(TimeUtils.formatFullTime(wayBillItemModel.getProductionDate()));
			}
			if (wayBillItemModel.getOverdueDate()!=null) {
				invetAddOrSubtractGoodsListJson.setOverdueDate(TimeUtils.formatFullTime(wayBillItemModel.getOverdueDate()));
			}

			String expDate = TimeUtils.formatFullTime(wayBillItemModel.getOverdueDate());
			if (StringUtils.isNotBlank(expDate)) {
				Timestamp exTtime = TimeUtils.parseFullTime(expDate);
				String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
				invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否	)
			}else {
				invetAddOrSubtractGoodsListJson.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
			}
			//事业部
			if (itemAllList.get(0).getBuId() != null && StringUtils.isNotBlank(itemAllList.get(0).getBuName())) {
				invetAddOrSubtractGoodsListJson.setBuId(String.valueOf(itemAllList.get(0).getBuId()));// 拿电商订单表体的
				invetAddOrSubtractGoodsListJson.setBuName(itemAllList.get(0).getBuName());
			}
			invetAddOrSubtractGoodsListJson.setStockLocationTypeId(itemAllList.get(0).getStockLocationTypeId() == null ?"": itemAllList.get(0).getStockLocationTypeId().toString());
			invetAddOrSubtractGoodsListJson.setStockLocationTypeName(itemAllList.get(0).getStockLocationTypeName());
			invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
		}




		//扣减销售出库库存量
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String now1 = sdf.format(new Date());
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractRootJson.setMerchantId(String.valueOf(merchantId));
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
		invetAddOrSubtractRootJson.setSourceDate(now1);
		invetAddOrSubtractRootJson.setDivergenceDate(deliverDate);
		// 获取当前年月
		Timestamp deliverDateTime = Timestamp.valueOf(deliverDate);
		invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDateTime));
		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
		//电商 订单回调设置
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTags());//回调标签
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTopic());//回调主题
		customParam.put("code", orderModel.getCode());// 电商订单内部单号
		invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
		invetAddOrSubtractRootJson.setStorePlatformName(merchantShopRelMongo.getStorePlatformCode());// 电商平台编码
		invetAddOrSubtractRootJson.setShopCode(orderModel.getShopCode());// 店铺编码
		// 推送库存加减接口
		rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
	}

	/**
	 * 抓取第e仓数据异常 保存到mongodb
	 * @param jsonError
	 */
	@Override
	public void grabEWSDeliveryOrderError(JSONObject jsonError,JSONObject json,String orderNo){
		LOGGER.info("定时器抓取寄售商e仓发货订单jsonError"+jsonError);
		Date date=new Date();
		Long startDate = date.getTime();
		Long endDate = startDate;
		MQLog mqLog=new MQLog();
		mqLog.setModel("定时器抓取寄售商e仓发货订单");
		mqLog.setPoint(13204122201L);
		if (StringUtils.isBlank(orderNo)) {
			mqLog.setKeyword("Time"+String.valueOf(date.getTime()));
		}else {
			mqLog.setKeyword(orderNo);
		}
		mqLog.setKeywordName("order_id");
		mqLog.setMethod("com.topideal.service.timer.impl.GrabEWSDeliveryOrderServiceImpl.grabEWSDeliveryOrderError()");
		mqLog.setStartDate(startDate);
		mqLog.setEndDate(endDate);
		//mqLog.setDesc((String)errorJSONObject.get("expMsg"));
		mqLog.setState(0);
		mqLog.setExpMsg((String)jsonError.get("expMsg"));
		mqLog.setTopics(MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTopic());
		mqLog.setTags(MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTags());
		JSONObject jsonObject=JSONObject.fromObject(mqLog);
		jsonObject.put("id", UUID.randomUUID().toString());
		jsonObject.put("requestMessage", json);
		jsonObject.put("returnMessage",(String)jsonError.get("expMsg"));
		jsonObject.put("moduleCode", LogModuleType.MODULE_ORDER.getType());
        try {
        	rMQLogProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
		} catch (Exception e) {
			LOGGER.error("定时器抓取寄售商e仓发货订单---推送保存mongdb日志mq失败"+ jsonError);
			e.printStackTrace();
		}
	}

	@Override
	public void getOneEWSDeliveryOrderError(JSONObject jsonError,JSONObject json,String orderNo){
		LOGGER.info("定时器抓取寄售商e仓发货订单jsonError推送单个"+jsonError);
		Date date=new Date();
		Long startDate = date.getTime();
		Long endDate = startDate;
		MQLog mqLog=new MQLog();
		mqLog.setModel("消费单个e仓发货订单");
		mqLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_13204122200));
		if (StringUtils.isBlank(orderNo)) {
			mqLog.setKeyword("Time"+String.valueOf(date.getTime()));
		}else {
			mqLog.setKeyword(orderNo);
		}
		mqLog.setKeywordName("order_id");
		mqLog.setMethod("com.topideal.service.timer.impl.GrabEWSDeliveryOrderServiceImpl.grabEWSDeliveryOrderError()");
		mqLog.setStartDate(startDate);
		mqLog.setEndDate(endDate);
		//mqLog.setDesc((String)errorJSONObject.get("expMsg"));
		mqLog.setState(0);
		mqLog.setExpMsg((String)jsonError.get("expMsg"));
		mqLog.setTopics(MQOrderEnum.GETONE_EWS_DELIVERY_ORDER.getTopic());
		mqLog.setTags(MQOrderEnum.GETONE_EWS_DELIVERY_ORDER.getTags());
		JSONObject jsonObject=JSONObject.fromObject(mqLog);
		jsonObject.put("id", UUID.randomUUID().toString());
		jsonObject.put("requestMessage", json);
		jsonObject.put("returnMessage",(String)jsonError.get("expMsg"));
		jsonObject.put("moduleCode", LogModuleType.MODULE_ORDER.getType());
        try {
        	rMQLogProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
		} catch (Exception e) {
			LOGGER.error("定时器抓取寄售商e仓发货订单---推送保存mongdb日志mq失败"+ jsonError);
			e.printStackTrace();
		}
	}

	/**
	 * 采用加权摊分方法摊分商品价格
	 */
/*	public Map<String, Double> getSplitPrice(JSONArray orderGoodsList, Double discount) {
		Map<String, Double> priceMap = new HashMap<>();
		BigDecimal deductPrice = new BigDecimal("0");
		BigDecimal totalPrice = new BigDecimal("0");
		BigDecimal discountBigDecimal = new BigDecimal(String.valueOf(discount));
		for (int i = 0; i < orderGoodsList.size(); i++) {
			JSONObject goodsJSON = (JSONObject) orderGoodsList.get(i);
			String num = goodsJSON.getString("num");
			String priceDouble = goodsJSON.getString("price");
			totalPrice = new BigDecimal(num).multiply(new BigDecimal(priceDouble)).add(totalPrice);
		}
		for (int i = 0; i < orderGoodsList.size(); i++) {
			JSONObject goodsJSON = (JSONObject) orderGoodsList.get(i);
			String num = goodsJSON.getString("num");
			String priceDouble = goodsJSON.getString("price");
			String goodsNo = goodsJSON.getString("goods_no");
			if (i == orderGoodsList.size()-1) {
				*//** 第N个商品的摊分后成交金额=∑商品成交金额-优惠金额-∑前N-1个商品的摊分后成交金额
				 * 	 第N个商品的成交单价=当前商品摊分后成交金额/当前商品成交数量*//*
				BigDecimal gPriceBigDecimal = (totalPrice.subtract(discountBigDecimal).subtract(deductPrice)).divide(new BigDecimal(num), 5, BigDecimal.ROUND_HALF_UP);
				priceMap.put(goodsNo, gPriceBigDecimal.doubleValue());
			} else {
				// 前N-1个商品的摊分后成交金额=当前商品成交金额-当前商品成交金额/∑商品成交金额*优惠金额
				BigDecimal gPriceBigDecimal = new BigDecimal(priceDouble).multiply(new BigDecimal(num)).divide(totalPrice, 4, BigDecimal.ROUND_HALF_UP).multiply(discountBigDecimal);
				BigDecimal gPriceTotal = new BigDecimal(priceDouble).multiply(new BigDecimal(num)).subtract(gPriceBigDecimal);
				// 前N-1个商品的成交单价
				BigDecimal gPrice = gPriceTotal.divide(new BigDecimal(num), 5, BigDecimal.ROUND_HALF_UP);
				priceMap.put(goodsNo, gPrice.doubleValue());
				deductPrice =  gPriceTotal.add(deductPrice);
			}
		}
		return priceMap;
	}*/

	/**
	 * 必填字段校验
	 * @param jSONOrderObject
	 * @return
	 */
	public void requiredCheck(JSONObject jSONOrderObject,MerchantInfoMongo merchantInfoMongo,DepotInfoMongo depotInfoMongo) throws Exception{
		LOGGER.info("每一条订单的json"+jSONOrderObject);
		// 订单
		if ("null".equals(jSONOrderObject.getString("order_id"))||StringUtils.isBlank(jSONOrderObject.getString("order_id"))) {
			throw new RuntimeException("order_id Is Null");
		}

		if(jSONOrderObject.get("shop_id")==null || StringUtils.isBlank(jSONOrderObject.getString("shop_id"))){
			throw new RuntimeException("抓取寄售商e仓获取到的店铺编码为空");
		}

		// 发货时间
		if (("null".equals(jSONOrderObject.getString("deliver_date")))||StringUtils.isBlank(jSONOrderObject.getString("deliver_date"))) {
			throw new RuntimeException("deliver_date Is Null");
		}
		// 不接发货时间小于2019-04-01 00:00:00的单
		Timestamp deliverTimestamp = TimeUtils.parseFullTime(jSONOrderObject.getString("deliver_date"));
		Timestamp deliverTimestamp2 = Timestamp.valueOf("2019-04-01 00:00:00");
		if (deliverTimestamp.getTime()<deliverTimestamp2.getTime()) {
			throw new RuntimeException("发货日期小于2019-04-01");
		}

		// 物流企业
		if (("null".equals((String)jSONOrderObject.get("logistics_code")))||StringUtils.isBlank((String)jSONOrderObject.get("logistics_code"))) {
			throw new RuntimeException("logistics_code Is Null");
		}
		// 运单
		if (("null".equals(jSONOrderObject.getString("way_bill_no")))||StringUtils.isBlank(jSONOrderObject.getString("way_bill_no"))) {
			throw new RuntimeException("way_bill_no Is Null");
		}
		// 订单时间
		if (("null".equals( jSONOrderObject.getString("created")))||StringUtils.isBlank( jSONOrderObject.getString("created"))) {
			throw new RuntimeException("created Is Null");
		}
		// 支付时间
		if (("null".equals(jSONOrderObject.getString("pay_time")))||StringUtils.isBlank(jSONOrderObject.getString("pay_time"))) {
			throw new RuntimeException("pay_time Is Null");
		}

		// 收货人姓名
		if (("null".equals(jSONOrderObject.getString("receiver_name")))||StringUtils.isBlank(jSONOrderObject.getString("receiver_name"))) {
			throw new RuntimeException("receiver_name Is Null");
		}
		// 收货人手机号码
		if (("null".equals(jSONOrderObject.getString("receiver_mobile")))||StringUtils.isBlank(jSONOrderObject.getString("receiver_mobile"))) {
			throw new RuntimeException("receiver_mobile Is Null");
		}
		//国家
		if (("null".equals(jSONOrderObject.getString("receiver_country")))||StringUtils.isBlank(jSONOrderObject.getString("receiver_country"))) {
			throw new RuntimeException("receiver_country Is Null");
		}

		// 省份
		if (("null".equals(jSONOrderObject.getString("receiver_state")))||StringUtils.isBlank(jSONOrderObject.getString("receiver_state"))) {
			throw new RuntimeException("receiver_state Is Null");
		}
		// 市
		if (("null".equals(jSONOrderObject.getString("receiver_city")))||StringUtils.isBlank(jSONOrderObject.getString("receiver_city"))) {
			throw new RuntimeException("receiver_city Is Null");
		}
		// 地址
		if (("null".equals(jSONOrderObject.getString("receiver_address")))||StringUtils.isBlank(jSONOrderObject.getString("receiver_address"))) {
			throw new RuntimeException("receiver_address Is Null");
		}

		// 发货时间 必须大于等于 交易时间
		Timestamp tradingDate = TimeUtils.parseFullTime(jSONOrderObject.getString("created"));
		if (deliverTimestamp.getTime()<tradingDate.getTime()) {
			throw new RuntimeException("发货时间deliver_date不能小于交易时间created");
		}


		OrderModel orderQuery=new OrderModel();
		orderQuery.setExternalCode(jSONOrderObject.getString("order_id"));
		orderQuery.setMerchantId(merchantInfoMongo.getMerchantId());
		orderQuery = orderDao.searchByModel(orderQuery);
		if (orderQuery!=null) {
			throw new RuntimeException("外部单号数据库已经存在,订单号:"+jSONOrderObject.getString("order_id"));
		}
		WayBillModel wayBillQuery = new WayBillModel();
		wayBillQuery.setWayBillNo(jSONOrderObject.getString("way_bill_no"));
		wayBillQuery.setMerchantId(merchantInfoMongo.getMerchantId());
		wayBillQuery = wayBillDao.searchByModel(wayBillQuery);
		if (wayBillQuery!=null) {
			throw new RuntimeException("运单号已经存在数据库已经存在,订单号:"+jSONOrderObject.getString("order_id")+"运单号:"+jSONOrderObject.getString("way_bill_no"));
		}


		//商品批次信息
		JSONArray jsonGoodArray = jSONOrderObject.getJSONArray("good_list");
		if (null==jsonGoodArray||jsonGoodArray.size()<1) {
			throw new RuntimeException("good_list is null");
		}
		if ("1".equals(depotInfoMongo.getBatchValidation())) {
			// 商品批次信息必填字段校验
			for (Object goodsObject : jsonGoodArray) {
				JSONObject goodsJSONObject = (JSONObject)goodsObject;
				//商品货号
				if (("null".equals(goodsJSONObject.getString("goods_no")))||StringUtils.isBlank(goodsJSONObject.getString("goods_no"))) {
					throw new RuntimeException("goods_no is null");
				}
				//数量
				if (("null".equals(goodsJSONObject.getString("num")))||StringUtils.isBlank(goodsJSONObject.getString("num"))) {
					throw new RuntimeException("num is null");
				}
				//批次号
				if (("null".equals(goodsJSONObject.getString("batch_id")))||StringUtils.isBlank(goodsJSONObject.getString("batch_id"))) {
					throw new RuntimeException("batch_id is null");
				}
				//生产日期
				if (("null".equals(goodsJSONObject.getString("production_time")))||StringUtils.isBlank(goodsJSONObject.getString("production_time"))) {
					throw new RuntimeException("production_time is null");
				}
				//失效日期
				if (("null".equals(goodsJSONObject.getString("exp_date")))||StringUtils.isBlank(goodsJSONObject.getString("exp_date"))) {
					throw new RuntimeException("exp_date is null");
				}
			}
		}


		//商品信息
		JSONArray jsonGoodPrArray = jSONOrderObject.getJSONArray("goodpr_list");
		if (null==jsonGoodPrArray||jsonGoodPrArray.size()<1) {
			throw new RuntimeException("goodpr_list is null");
		}
		for (int i = 0; i < jsonGoodPrArray.size(); i++) {
			JSONObject goodsPrJSONObject = (JSONObject) jsonGoodPrArray.get(i);
			goodsPrJSONObject.put("index", i);// 存储表体顺序
			if (("null".equals(goodsPrJSONObject.getString("goods_no")))||StringUtils.isBlank(goodsPrJSONObject.getString("goods_no"))) {
				throw new RuntimeException("goods_no Is Null");
			}
			//数量
			if (("null".equals(goodsPrJSONObject.getString("num")))||StringUtils.isBlank(goodsPrJSONObject.getString("num"))) {
				throw new RuntimeException("num Is Null");
			}
			//代发价
			if (("null".equals(goodsPrJSONObject.getString("delivery_price")))||StringUtils.isBlank(goodsPrJSONObject.getString("delivery_price"))) {
				throw new RuntimeException("delivery_price Is Null");
			}
			// 商品总金额
			if (("null".equals(goodsPrJSONObject.getString("delivery_total")))||StringUtils.isBlank(goodsPrJSONObject.getString("delivery_total"))) {
				throw new RuntimeException("delivery_total Is Null");
			}

			// 美赞臣店铺多校验
			if ("SK190814110900014".equals(jSONOrderObject.getString("shop_id"))) {
				//实际销售价
				if (("null".equals(goodsPrJSONObject.getString("price")))||StringUtils.isBlank(goodsPrJSONObject.getString("price"))) {
					throw new RuntimeException("price Is Null");
				}
				//商品总价
				if (("null".equals(goodsPrJSONObject.getString("dec_total")))||StringUtils.isBlank(goodsPrJSONObject.getString("dec_total"))) {
					throw new RuntimeException("dec_total Is Null");
				}
			}

		}


	}

	/**
	 * 查询商品
	 * @param merchantInfoMongo
	 * @param goodsNo
	 * @return
	 */
	public MerchandiseInfoMogo getMerchandiseInfo(MerchantInfoMongo merchantInfoMongo,String goodsNo){
		Map<String, Object> merchandiseInfoMap = new HashMap<>();
		merchandiseInfoMap.put("merchantId", merchantInfoMongo.getMerchantId());//商品id
		merchandiseInfoMap.put("goodsNo", goodsNo);//商品货号
		MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 查询商品信息
		if (merchandiseInfoMogo==null) {
			throw new RuntimeException("根据商家和货号没有查询到商品信息:货号"+goodsNo+",商家id:"+merchantInfoMongo.getMerchantId());
		}
		return merchandiseInfoMogo;
	};

	/**
	 * 批次效期强校验生产运单表体
	 * @param jsonGoodArray
	 * @param merchandiseInfoMogo
	 * @throws Exception
	 */
	public void getWayWayBillItem(JSONArray jsonGoodArray,MerchantInfoMongo merchantInfoMongo,DepotInfoMongo depotInfoMongo,List<WayBillItemModel> wayBillItemList,
			Map<String, Integer> batchGoodsNumMap) throws Exception{
		//遍历商品批次信息
		for (Object goodsObject : jsonGoodArray) {
			JSONObject goodsJSONObject = (JSONObject)goodsObject;
			String goodsNoStr = goodsJSONObject.getString("goods_no");
			String numStr = goodsJSONObject.getString("num");
			String batchNo = goodsJSONObject.getString("batch_id");
			String productionTime =goodsJSONObject.getString("production_time");
			String expDate = goodsJSONObject.getString("exp_date");
			//获取商品
			MerchandiseInfoMogo merchandiseInfoMogo = getMerchandiseInfo(merchantInfoMongo, goodsNoStr);

			String goodsName=merchandiseInfoMogo.getName();Long goodsId=merchandiseInfoMogo.getMerchandiseId();
			String goodsCode=merchandiseInfoMogo.getGoodsCode();String barcode=merchandiseInfoMogo.getBarcode();
			String goodsNo=merchandiseInfoMogo.getGoodsNo();String commbarcode=merchandiseInfoMogo.getCommbarcode();

			// 向运单表体中查入数据
			WayBillItemModel wayBillItemModel = new WayBillItemModel();
			//wayBillItemModel.setBillId(wayBillModel.getId());
			wayBillItemModel.setGoodsId(goodsId);
			wayBillItemModel.setGoodsNo(goodsNo);
			wayBillItemModel.setGoodsName(goodsName);
			wayBillItemModel.setGoodsCode(goodsCode);
			wayBillItemModel.setBarcode(barcode);
			wayBillItemModel.setNum(Integer.valueOf(numStr));
			wayBillItemModel.setBatchNo(batchNo);
			if ((!"null".equals(productionTime))&&StringUtils.isNotBlank(productionTime)) {
				wayBillItemModel.setProductionDate(TimeUtils.parseFullTime(productionTime));
			}
			if ((!"null".equals(expDate))&&StringUtils.isNotBlank(expDate)) {
				wayBillItemModel.setOverdueDate(TimeUtils.parseFullTime(expDate));
			}
			wayBillItemList.add(wayBillItemModel);
			if (batchGoodsNumMap.containsKey(goodsNo)) {
				batchGoodsNumMap.put(goodsNo, batchGoodsNumMap.get(goodsNo) + Integer.valueOf(numStr));
			} else {
				batchGoodsNumMap.put(goodsNo, Integer.valueOf(numStr));
			}
		}

	};
	/**
	 * 获取美赞臣电商表体信息
	 */
	public Map<Long,String> getBxmOrderItem(JSONArray jsonGoodPrArray,JSONArray jsonGoodArray,
			MerchantInfoMongo merchantInfoMongo,MerchantShopRelMongo merchantShopRelMongo,DepotInfoMongo depotInfoMongo
			,Map<String, Integer> batchGoodsNumMap, Map<String, Integer> goodsNumMap,List<OrderItemModel> orderItemList,List<WayBillItemModel> wayBillItemList,
			OrderModel orderModel, JSONObject jSONOrderObject,List<MerchantShopShipperMongo> merchantShopShipperList,Map<Long,String> stockLocationMap) throws Exception{
		Map<Long,String> goodsIdMap = new HashMap<>();// 需要移库的商品,value:移出事业部id+移出事业部名字
		String deliverDate = jSONOrderObject.getString("deliver_date");// 发货时间
		String wayTaxFee = jSONOrderObject.getString("way_tax_fee");// 税费
		String discount = jSONOrderObject.getString("discount");// 优惠减免金额
		String payment = jSONOrderObject.getString("payment");// 订单实付总额

		//运单表体  判断是否是批次效期强校验 是的话 取批次   否的取商品
		if ("1".equals(depotInfoMongo.getBatchValidation())) {
			getWayWayBillItem(jsonGoodArray,merchantInfoMongo,depotInfoMongo,wayBillItemList,batchGoodsNumMap);
		}

		BigDecimal discountBig=new BigDecimal(0);
		if (!"null".equals(discount)&&StringUtils.isNotBlank(discount)) {
			discountBig=new BigDecimal(discount);
		}

		//获取商品结算金额
		getDecTotal(jsonGoodPrArray,discountBig);
		BigDecimal saleComTotalBigDecimal = new BigDecimal("0");
		// 获取电商订单表体信息
		for (Object goodsPrObject : jsonGoodPrArray) {
			JSONObject goodsPrJSONObject = (JSONObject)goodsPrObject;
			String goodsNoStr = goodsPrJSONObject.getString("goods_no"); //商品货号
			String numStr = goodsPrJSONObject.getString("num"); //数量
			String saleCom = goodsPrJSONObject.getString("sale_com"); //佣金
			String deliveryPrice = goodsPrJSONObject.getString("delivery_price"); //代发价
			String derpDecTotal = goodsPrJSONObject.getString("derpDecTotal"); //结算总金额
			String derpPrice = goodsPrJSONObject.getString("derpPrice"); //结算单价


			// 根据商家id和商品货号查询商品;
			MerchandiseInfoMogo merchandiseInfoMogo = getMerchandiseInfo(merchantInfoMongo, goodsNoStr);
			saleComTotalBigDecimal = saleComTotalBigDecimal.add(new BigDecimal(saleCom)); //表头佣金

			String goodsName=merchandiseInfoMogo.getName();Long goodsId=merchandiseInfoMogo.getMerchandiseId();
			String goodsCode=merchandiseInfoMogo.getGoodsCode();String barcode=merchandiseInfoMogo.getBarcode();
			String goodsNo=merchandiseInfoMogo.getGoodsNo();String commbarcode=merchandiseInfoMogo.getCommbarcode();

			// 结算金额均摊
			// 电商表体
			OrderItemModel 	orderItemModel =new OrderItemModel();
			orderItemModel.setGoodsName(goodsName);//商品名称
			orderItemModel.setGoodsId(goodsId);//商品ID
			orderItemModel.setGoodsCode(goodsCode);//商品编码
			orderItemModel.setBarcode(barcode);//条形码
			orderItemModel.setGoodsNo(goodsNo);//商品货号
			orderItemModel.setCommbarcode(commbarcode); //标准条码
			orderItemModel.setSaleCom(new BigDecimal(saleCom)); //佣金
			orderItemModel.setPrice(new BigDecimal(derpPrice).setScale(2, BigDecimal.ROUND_HALF_UP));// 结算单价
			orderItemModel.setDecTotal(new BigDecimal(derpDecTotal).setScale(2, BigDecimal.ROUND_HALF_UP));// 结算总金额
			orderItemModel.setDeliveryPrice(new BigDecimal(deliveryPrice));// 代发价格
			orderItemModel.setNum(Integer.valueOf(numStr));// 数量
			orderItemModel.setOriginalPrice(new BigDecimal(goodsPrJSONObject.getString("price")).setScale(2, BigDecimal.ROUND_HALF_UP));//销售单价
			orderItemModel.setOriginalDecTotal(new BigDecimal(goodsPrJSONObject.getString("dec_total")).setScale(2, BigDecimal.ROUND_HALF_UP));//销售总金额

			// 1.先看店铺类型是否为单主店还是外部店，若为单主店就先拿开店事业部查询库存
			Map<String, Object> buInventoryParams = new HashMap<String, Object>();
			buInventoryParams.put("merchantId", merchantInfoMongo.getMerchantId());//商家
			buInventoryParams.put("depotId", depotInfoMongo.getDepotId());	// 仓库ID
			buInventoryParams.put("goodsId", merchandiseInfoMogo.getMerchandiseId());// 商品id
			Timestamp deliverDateTime = Timestamp.valueOf(deliverDate);
			buInventoryParams.put("month", TimeUtils.formatMonth(deliverDateTime));// 月份(取发货时间)
			int flag=0;// 是否查询货主事业部0:不查；1：查
			Long buId = null;//事业部
			String buName = null;
			Long stockLocationTypeId = null;//库存类型id
			Long shopShipperStockLocationTypeId = null;//库存类型id
			// 为单主店就先拿开店事业部查询库存(外部店不会有移库操作)
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode())){
				buInventoryParams.put("buId",merchantShopRelMongo.getBuId());// 开店事业部
				List<BuInventoryMongo> buInventoryList = buInventoryMongoDao.findAll(buInventoryParams);
				if(buInventoryList==null||buInventoryList.size()==0){	// 若没有库存，就查货主事业部
					flag=1;
				}else if(buInventoryList!=null && buInventoryList.size()>1){
					throw new RuntimeException("根据商家+仓库+事业部+商品id+月份在事业部库存表找到多条信息");
				}else if(buInventoryList.size()==1 && buInventoryList.get(0).getOkNum()>0){
					buId = merchantShopRelMongo.getBuId();//拿开店事业部
					buName = merchantShopRelMongo.getBuName();
					stockLocationTypeId = merchantShopRelMongo.getStockLocationTypeId();
				}else if(buInventoryList.get(0).getOkNum()<=0){// 若没有库存，就查货主事业部
					flag=1;
				}
			}
			int isNotChange=0;//按现有逻辑，以唯一开店事业部生成单据并出库
			int countHave=0;	// 记录某个商品是否每个事业部都有库存
			int countNone=0;	// 记录某个商品是否每个事业部都没库存
			Long shopShipperBuId=null; //货主事业部id
			String shopShipperBuName=null; //货主事业部名称
			// 外部店就直接查货主事业部（外部店无开店事业部）或者 为单主店但是开店事业部无库存
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRelMongo.getStoreTypeCode()) ||
					flag==1){
				if(merchantShopShipperList==null || merchantShopShipperList.size()==0){
					isNotChange=1;
					countNone=10;
				}else{
					for (int i = 0; i < merchantShopShipperList.size(); i++) {
						MerchantShopShipperMongo merchantShopShipperMongo = merchantShopShipperList.get(i);
						buInventoryParams.put("buId",merchantShopShipperMongo.getBuId());// 货主事业部
						List<BuInventoryMongo> buInventoryList = buInventoryMongoDao.findAll(buInventoryParams);
						if(buInventoryList==null || buInventoryList.size()==0){
							countNone=countNone+1;
						}else if(buInventoryList!=null && buInventoryList.size()>1){
							throw new RuntimeException("根据商家+仓库+事业部+商品id+月份在事业部库存表找到多条信息");
						}else if(buInventoryList.size()==1 && buInventoryList.get(0).getOkNum()>0){
							shopShipperBuId = merchantShopShipperMongo.getBuId();//拿货主事业部
							shopShipperBuName = merchantShopShipperMongo.getBuName();
							shopShipperStockLocationTypeId = merchantShopShipperMongo.getStockLocationTypeId();
							if(flag==1){	// 单主店若有库存，也是拿开店事业部生成单据
								buId = merchantShopRelMongo.getBuId();//拿开店事业部
								buName = merchantShopRelMongo.getBuName();
								stockLocationTypeId = merchantShopRelMongo.getStockLocationTypeId();
							}
							countHave=countHave+1;
						}else if(buInventoryList.get(0).getOkNum()<=0){
							countNone=countNone+1;
						}
					}
				}
				if(countNone==merchantShopShipperList.size() || countHave>1){
					buId=null;// 事业部设为空，进入待补录列表
					buName=null;
					shopShipperBuId=null;
					shopShipperBuName=null;
					stockLocationTypeId = null;
				}
			}
			if(isNotChange==1){//以唯一开店事业部生成单据并出库
				buId = merchantShopRelMongo.getBuId();//拿开店事业部
				buName = merchantShopRelMongo.getBuName();
				stockLocationTypeId = merchantShopRelMongo.getStockLocationTypeId();
			}
			String stockLocationTypeName = (stockLocationTypeId != null && stockLocationTypeId.intValue() != 0)? stockLocationMap.get(stockLocationTypeId) : "";
			if(stockLocationTypeId != null && stockLocationTypeId.intValue() != 0 && StringUtils.isBlank(stockLocationTypeName)){
				Map<String, Object> params = new HashMap<>();
				params.put("buStockLocationTypeId", stockLocationTypeId);
				params.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
				BuStockLocationTypeConfigMongo stockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(params);
				stockLocationTypeName = stockLocationTypeConfigMongo.getName();

				stockLocationMap.put(stockLocationTypeId, stockLocationTypeName);
			}
			String shopShipperStockLocationTypeName = (shopShipperStockLocationTypeId != null && shopShipperStockLocationTypeId.intValue() != 0)? stockLocationMap.get(shopShipperStockLocationTypeId) : "";
			if(shopShipperStockLocationTypeId != null && shopShipperStockLocationTypeId.intValue() != 0 && StringUtils.isBlank(shopShipperStockLocationTypeName)){
				Map<String, Object> params = new HashMap<>();
				params.put("buStockLocationTypeId", shopShipperStockLocationTypeId);
				params.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
				BuStockLocationTypeConfigMongo stockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(params);
				shopShipperStockLocationTypeName = stockLocationTypeConfigMongo.getName();

				stockLocationMap.put(shopShipperStockLocationTypeId, shopShipperStockLocationTypeName);
			}
			//单主店以开店事业部生成单据（无论开店事业部是否有库存）
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode()) || isNotChange==1){
				orderItemModel.setBuId(buId);
				orderItemModel.setBuName(buName);
				orderItemModel.setStockLocationTypeId(stockLocationTypeId);
				orderItemModel.setStockLocationTypeName(stockLocationTypeName);
			}else if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRelMongo.getStoreTypeCode())){// 外部店
				orderItemModel.setBuId(shopShipperBuId);
				orderItemModel.setBuName(shopShipperBuName);
				orderItemModel.setStockLocationTypeId(shopShipperStockLocationTypeId);
				orderItemModel.setStockLocationTypeName(shopShipperStockLocationTypeName);
			}
			orderItemList.add(orderItemModel);

			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode()) && countHave==1){// 如果是单主店，需要移库
				goodsIdMap.put(merchandiseInfoMogo.getMerchandiseId(),shopShipperBuId+","+shopShipperBuName+","+shopShipperStockLocationTypeId);// 需移库的商品
			}
			//运单表体  判断是否是批次效期强校验 是的话 取批次   否的取商品
			if (!"1".equals(depotInfoMongo.getBatchValidation())) {
				WayBillItemModel wayBillItemModel = new WayBillItemModel();
				wayBillItemModel.setGoodsId(goodsId);
				wayBillItemModel.setGoodsNo(goodsNo);
				wayBillItemModel.setGoodsName(goodsName);
				wayBillItemModel.setGoodsCode(goodsCode);
				wayBillItemModel.setBarcode(barcode);
				wayBillItemModel.setNum(Integer.valueOf(numStr));
				wayBillItemList.add(wayBillItemModel);
				if (batchGoodsNumMap.containsKey(goodsNo)) {
					batchGoodsNumMap.put(goodsNo, batchGoodsNumMap.get(goodsNo) + Integer.valueOf(numStr));
				} else {
					batchGoodsNumMap.put(goodsNo, Integer.valueOf(numStr));
				}
			}

			if (goodsNumMap.containsKey(goodsNo)) {
				goodsNumMap.put(goodsNo, goodsNumMap.get(goodsNo) + Integer.valueOf(numStr));
			} else {
				goodsNumMap.put(goodsNo, Integer.valueOf(numStr));
			}
		}
		if (!"null".equals(wayTaxFee)&&StringUtils.isNotBlank(wayTaxFee)) {
			BigDecimal taxesBig=new BigDecimal(wayTaxFee);
			orderModel.setTaxes(taxesBig);//税费
		}

		orderModel.setDiscount(discountBig);//优惠金额
		if (!"null".equals(payment)&&StringUtils.isNotBlank(payment)) {
			BigDecimal paymentBig=new BigDecimal(payment);
			orderModel.setPayment(paymentBig);//订单实付金额
		}
		orderModel.setSaleCom(saleComTotalBigDecimal);
		return goodsIdMap;
	}


	/**
	 * 均摊结算金额   并计算单价
	 * @param jsonGoodPrArray
	 * @param discountBig
	 * @return
	 */
	private void getDecTotal(JSONArray jsonGoodPrArray, BigDecimal discountBig) {
		// 单个商品直接分配
		if (jsonGoodPrArray.size()==1) {
			JSONObject goodsPrJSONObject = (JSONObject) jsonGoodPrArray.get(0);
			BigDecimal decTotal = new BigDecimal(goodsPrJSONObject.getString("dec_total"));
			BigDecimal derpDecTotal = decTotal.subtract(discountBig);
			String numStr = goodsPrJSONObject.getString("num"); //数量
			BigDecimal derpPrice=new BigDecimal(0);
			if (!numStr.equals("0"))derpPrice=derpDecTotal.divide(new BigDecimal(numStr),2, BigDecimal.ROUND_HALF_UP);
			derpDecTotal = derpDecTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
			goodsPrJSONObject.put("derpDecTotal", derpDecTotal);// 结算总金额
			goodsPrJSONObject.put("derpPrice", derpPrice);// 结算单价
		}
		// 多商品均摊
		if (jsonGoodPrArray.size()>1) {
			BigDecimal decTotalBigDecimal=new BigDecimal(0);
			for (int i = 0; i < jsonGoodPrArray.size(); i++) {// 金额累加
				JSONObject goodsPrJSONObject = (JSONObject) jsonGoodPrArray.get(i);
				BigDecimal decTotal = new BigDecimal(goodsPrJSONObject.getString("dec_total"));
				decTotalBigDecimal = decTotalBigDecimal.add(decTotal);
			}
			BigDecimal discountBigAdd=new BigDecimal(0);
			// 均摊
			for (int i = 0; i < jsonGoodPrArray.size(); i++) {
				JSONObject goodsPrJSONObject = (JSONObject) jsonGoodPrArray.get(i);
				if (i==jsonGoodPrArray.size()-1) {
					BigDecimal decTotal = new BigDecimal(goodsPrJSONObject.getString("dec_total"));
					BigDecimal discountShengyu = discountBig.subtract(discountBigAdd);//最后一个取剩余金额
					BigDecimal derpDecTotal = decTotal.subtract(discountShengyu);//结算总金额
					String numStr = goodsPrJSONObject.getString("num"); //数量
					BigDecimal derpPrice=new BigDecimal(0);
					if (!numStr.equals("0"))derpPrice=derpDecTotal.divide(new BigDecimal(numStr),2, BigDecimal.ROUND_HALF_UP);
					derpDecTotal = derpDecTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
					goodsPrJSONObject.put("derpDecTotal", derpDecTotal);
					goodsPrJSONObject.put("derpPrice", derpPrice);
				}else {

					BigDecimal decTotal = new BigDecimal(goodsPrJSONObject.getString("dec_total"));
					BigDecimal discountDecTotal=new BigDecimal(0);
					// 分母不为0
					if (0!=decTotalBigDecimal.compareTo(BigDecimal.ZERO))discountDecTotal=decTotal.divide(decTotalBigDecimal,2, BigDecimal.ROUND_HALF_UP).multiply(discountBig);
					discountBigAdd = discountBigAdd.add(discountDecTotal);
					BigDecimal derpDecTotal = decTotal.subtract(discountDecTotal);
					String numStr = goodsPrJSONObject.getString("num"); //数量
					BigDecimal derpPrice=new BigDecimal(0);
					if (!numStr.equals("0"))derpPrice=derpDecTotal.divide(new BigDecimal(numStr),2, BigDecimal.ROUND_HALF_UP);
					derpDecTotal = derpDecTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
					goodsPrJSONObject.put("derpDecTotal", derpDecTotal);
					goodsPrJSONObject.put("derpPrice", derpPrice);
				}

			}
		}



	}

	/**
	 * 获取电商订单表体
	 */
	public Map<Long,String> getOrderItem(JSONArray jsonGoodPrArray,JSONArray jsonGoodArray,
			MerchantInfoMongo merchantInfoMongo,DepotInfoMongo	depotInfoMongo,
			MerchantShopRelMongo merchantShopRelMongo,Map<String, Integer> batchGoodsNumMap,
			Map<String, Integer> goodsNumMap,List<OrderItemModel> orderItemList,
			List<WayBillItemModel> wayBillItemList, OrderModel orderModel, JSONObject jSONOrderObject,
			List<MerchantShopShipperMongo> merchantShopShipperList, Map<Long,String> stockLocationMap) throws Exception{
		Map<Long,String> goodsIdMap = new HashMap<>();// 需要移库的商品,value:移出事业部id+移出事业部名字
		String deliverDate = jSONOrderObject.getString("deliver_date");// 发货时间
		// 订单实付总金额不能为空
		if ("null".equals(jSONOrderObject.get("delivery_amount"))||StringUtils.isBlank(jSONOrderObject.getString("delivery_amount"))) {
			throw new RuntimeException("delivery_amount Is Null");
		}
		String deliveryAmount = jSONOrderObject.getString("delivery_amount");//
		BigDecimal totalPrice = new BigDecimal(deliveryAmount);// 订单实付总金额
		//运单表体  判断是否是批次效期强校验 是的话 取批次   否的取商品
		if ("1".equals(depotInfoMongo.getBatchValidation())) {
			getWayWayBillItem(jsonGoodArray,merchantInfoMongo,depotInfoMongo,wayBillItemList,batchGoodsNumMap);// 添加运单
		}


		for (Object goodsPrObject : jsonGoodPrArray) {
			JSONObject goodsPrJSONObject = (JSONObject)goodsPrObject;
			String goodsNoStr = goodsPrJSONObject.getString("goods_no"); //商品货号
			String numStr = goodsPrJSONObject.getString("num"); //数量
			String deliveryPrice = goodsPrJSONObject.getString("delivery_price"); //实际销售价
			String deliveryTotal = goodsPrJSONObject.getString("delivery_total"); //订单商品实付总金额
			// 根据商家id和商品货号查询商品;
			MerchandiseInfoMogo merchandiseInfoMogo = getMerchandiseInfo(merchantInfoMongo, goodsNoStr);

			String goodsName=merchandiseInfoMogo.getName();Long goodsId=merchandiseInfoMogo.getMerchandiseId();
			String goodsCode=merchandiseInfoMogo.getGoodsCode();String barcode=merchandiseInfoMogo.getBarcode();
			String goodsNo=merchandiseInfoMogo.getGoodsNo();String commbarcode=merchandiseInfoMogo.getCommbarcode();

			// 插入电商表体
			OrderItemModel 	orderItemModel =new OrderItemModel();
			orderItemModel.setGoodsName(goodsName);//商品名称
			orderItemModel.setGoodsId(goodsId);//商品ID
			orderItemModel.setGoodsCode(goodsCode);//商品
			orderItemModel.setBarcode(barcode);//条形码
			orderItemModel.setGoodsNo(goodsNo);//商品货号
			orderItemModel.setCommbarcode(commbarcode); //标准条码
			orderItemModel.setSaleCom(new BigDecimal(0)); //佣金

			BigDecimal price = new BigDecimal(deliveryPrice).setScale(2,BigDecimal.ROUND_HALF_UP);
			orderItemModel.setPrice(price);// 单价
			//BigDecimal decTotal=new BigDecimal(deliveryPrice).multiply(new BigDecimal(numStr)).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal decTotal =new BigDecimal(deliveryTotal).setScale(2,BigDecimal.ROUND_HALF_UP);
			orderItemModel.setDecTotal(decTotal);// dec_total
			//totalPrice = totalPrice.add(decTotal);
			BigDecimal deliveryPriceBig=new BigDecimal(deliveryPrice);
			orderItemModel.setDeliveryPrice(deliveryPriceBig);
			orderItemModel.setNum(Integer.valueOf(numStr));// 数量
			orderItemModel.setOriginalPrice(new BigDecimal(goodsPrJSONObject.getString("price")).setScale(2, BigDecimal.ROUND_HALF_UP));//销售单价
			orderItemModel.setOriginalDecTotal(new BigDecimal(goodsPrJSONObject.getString("dec_total")).setScale(2, BigDecimal.ROUND_HALF_UP));//销售总金额
			// 1.先看店铺类型是否为单主店还是外部店，若为单主店就先拿开店事业部查询库存
			Map<String, Object> buInventoryParams = new HashMap<String, Object>();
			buInventoryParams.put("merchantId", merchantInfoMongo.getMerchantId());//商家
			buInventoryParams.put("depotId", depotInfoMongo.getDepotId());	// 仓库ID
			buInventoryParams.put("goodsId", merchandiseInfoMogo.getMerchandiseId());// 商品id
			Timestamp deliverDateTime = Timestamp.valueOf(deliverDate);
			buInventoryParams.put("month", TimeUtils.formatMonth(deliverDateTime));// 月份(取发货时间)

			// 第e仓：需以“商家（货主公司）+店铺id”查询店铺信息表维护的货主事业部，若找不到时，报错预警；
			int flag=0;// 是否查询货主事业部0:不查；1：查
			Long buId = null;//事业部
			String buName = null;
			Long stockLocationTypeId = null;//库存类型id
			Long shopShipperStockLocationTypeId = null;//库存类型id
			// 为单主店就先拿开店事业部查询库存(外部店不会有移库操作)
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode())){
				buInventoryParams.put("buId",merchantShopRelMongo.getBuId());// 开店事业部
				List<BuInventoryMongo> buInventoryList = buInventoryMongoDao.findAll(buInventoryParams);
				if(buInventoryList==null || buInventoryList.size()==0){	// 若没有库存，就查货主事业部
					flag=1;
				}else if(buInventoryList!=null && buInventoryList.size()>1){
					throw new RuntimeException("根据商家+仓库+事业部+商品id+月份在事业部库存表找到多条信息");
				}else if(buInventoryList.size()==1 && buInventoryList.get(0).getOkNum()>0){
					buId = merchantShopRelMongo.getBuId();//拿开店事业部
					buName = merchantShopRelMongo.getBuName();
					stockLocationTypeId = merchantShopRelMongo.getStockLocationTypeId();
				}else if(buInventoryList.get(0).getOkNum()<=0){// 若没有库存，就查货主事业部
					flag=1;
				}
			}
			int isNotChange=0;//按现有逻辑，以唯一开店事业部生成单据并出库
			int countHave=0;	// 记录某个商品是否每个事业部都有库存
			int countNone=0;	// 记录某个商品是否每个事业部都没库存
			Long shopShipperBuId=null; //货主事业部id
			String shopShipperBuName=null; //货主事业部名称
			// 外部店就直接查货主事业部（外部店无开店事业部）或者 为单主店但是开店事业部无库存
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRelMongo.getStoreTypeCode()) ||
					flag==1){
				if(merchantShopShipperList==null || merchantShopShipperList.size()==0){
					isNotChange=1;
					countNone=10;
				}else{
					for (int i = 0; i < merchantShopShipperList.size(); i++) {
						MerchantShopShipperMongo merchantShopShipperMongo = merchantShopShipperList.get(i);
						buInventoryParams.put("buId",merchantShopShipperMongo.getBuId());// 货主事业部
						List<BuInventoryMongo> buInventoryList = buInventoryMongoDao.findAll(buInventoryParams);
						if(buInventoryList==null || buInventoryList.size()==0){
							countNone=countNone+1;
						}else if(buInventoryList!=null && buInventoryList.size()>1){
							throw new RuntimeException("根据商家+仓库+事业部+商品id+月份在事业部库存表找到多条信息");
						}else if(buInventoryList.size()==1 && buInventoryList.get(0).getOkNum()>0){
							shopShipperBuId = merchantShopShipperMongo.getBuId();//拿货主事业部
							shopShipperBuName = merchantShopShipperMongo.getBuName();
							shopShipperStockLocationTypeId = merchantShopShipperMongo.getStockLocationTypeId();
							if(flag==1){	// 单主店若有库存，也是拿开店事业部生成单据
								buId = merchantShopRelMongo.getBuId();//拿开店事业部
								buName = merchantShopRelMongo.getBuName();
								stockLocationTypeId = merchantShopRelMongo.getStockLocationTypeId();
							}
							countHave=countHave+1;
						}else if(buInventoryList.get(0).getOkNum()<=0){
							countNone=countNone+1;
						}
					}
				}
				if(countNone==merchantShopShipperList.size() || countHave>1){
					buId=null;// 事业部设为空，进入待补录列表
					buName=null;
					shopShipperBuId=null;
					shopShipperBuName=null;
					stockLocationTypeId = null;
					shopShipperStockLocationTypeId = null;
				}
			}
			if(isNotChange==1){//以唯一开店事业部生成单据并出库
				buId = merchantShopRelMongo.getBuId();//拿开店事业部
				buName = merchantShopRelMongo.getBuName();
				stockLocationTypeId = merchantShopRelMongo.getStockLocationTypeId();
			}
			String stockLocationTypeName = (stockLocationTypeId != null && stockLocationTypeId.intValue() != 0)? stockLocationMap.get(stockLocationTypeId) : "";
			if(stockLocationTypeId != null && stockLocationTypeId.intValue() != 0 && StringUtils.isBlank(stockLocationTypeName)){
				Map<String, Object> params = new HashMap<>();
				params.put("buStockLocationTypeId", stockLocationTypeId);
				params.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
				BuStockLocationTypeConfigMongo stockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(params);
				stockLocationTypeName = stockLocationTypeConfigMongo.getName();

				stockLocationMap.put(stockLocationTypeId, stockLocationTypeName);
			}
			String shopShipperStockLocationTypeName = (shopShipperStockLocationTypeId != null && shopShipperStockLocationTypeId.intValue() != 0) ? stockLocationMap.get(shopShipperStockLocationTypeId) : "";
			if(shopShipperStockLocationTypeId != null && shopShipperStockLocationTypeId.intValue() != 0 && StringUtils.isBlank(shopShipperStockLocationTypeName)){
				Map<String, Object> params = new HashMap<>();
				params.put("buStockLocationTypeId", shopShipperStockLocationTypeId);
				params.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
				BuStockLocationTypeConfigMongo stockLocationTypeConfigMongo = buStockLocationTypeConfigMongoDao.findOne(params);
				shopShipperStockLocationTypeName = stockLocationTypeConfigMongo.getName();

				stockLocationMap.put(shopShipperStockLocationTypeId, shopShipperStockLocationTypeName);
			}
			//单主店以开店事业部生成单据（无论开店事业部是否有库存）
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode()) || isNotChange==1){
				orderItemModel.setBuId(buId);
				orderItemModel.setBuName(buName);
				orderItemModel.setStockLocationTypeId(stockLocationTypeId);
				orderItemModel.setStockLocationTypeName(stockLocationTypeName);
			}else if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRelMongo.getStoreTypeCode())){// 外部店
				orderItemModel.setBuId(shopShipperBuId);
				orderItemModel.setBuName(shopShipperBuName);
				orderItemModel.setStockLocationTypeId(shopShipperStockLocationTypeId);
				orderItemModel.setStockLocationTypeName(shopShipperStockLocationTypeName);
			}
			orderItemList.add(orderItemModel);
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode()) && countHave==1){// 如果是单主店，需要移库
				goodsIdMap.put(merchandiseInfoMogo.getMerchandiseId(),shopShipperBuId+","+shopShipperBuName+","+shopShipperStockLocationTypeId);// 需移库的商品
			}


			//运单表体  判断是否是批次效期强校验 是的话 取批次   否的取商品
			if (!"1".equals(depotInfoMongo.getBatchValidation())) {
				WayBillItemModel wayBillItemModel = new WayBillItemModel();
				//wayBillItemModel.setBillId(wayBillModel.getId());
				wayBillItemModel.setGoodsId(goodsId);
				wayBillItemModel.setGoodsNo(goodsNo);
				wayBillItemModel.setGoodsName(goodsName);
				wayBillItemModel.setNum(Integer.valueOf(numStr));
				wayBillItemModel.setGoodsCode(goodsCode);
				wayBillItemModel.setBarcode(barcode);
				wayBillItemList.add(wayBillItemModel);// 添加运单
				if (batchGoodsNumMap.containsKey(goodsNo)) {
					batchGoodsNumMap.put(goodsNo, batchGoodsNumMap.get(goodsNo) + Integer.valueOf(numStr));
				} else {
					batchGoodsNumMap.put(goodsNo, Integer.valueOf(numStr));
				}
			}

			if (goodsNumMap.containsKey(goodsNo)) {
				goodsNumMap.put(goodsNo, goodsNumMap.get(goodsNo) + Integer.valueOf(numStr));
			} else {
				goodsNumMap.put(goodsNo, Integer.valueOf(numStr));
			}

		}
		orderModel.setTaxes(BigDecimal.ZERO); //优惠金额
		orderModel.setDiscount(BigDecimal.ZERO); //税费
		orderModel.setPayment(totalPrice);
		return goodsIdMap;
	};


	/**
	 * 生成移库单
	 */
	public void saveBuMoveInventory(OrderModel orderModel,OrderItemModel orderItemModel,DepotInfoMongo depotInfoMongo,MerchantInfoMongo merchantInfoMongo,
			Long shopShipperBuId,String shopShipperBuName,MerchantShopRelMongo merchantShopRelMongo,WayBillItemModel wayBillItemModel,
									Long shopShipperStockLocationTypeId,Map<Long, String> stockLocationMap) throws Exception{
		BuMoveInventoryModel buMoveInventoryModel = new BuMoveInventoryModel();
		// 保存移库单表头
		buMoveInventoryModel.setInBuId(merchantShopRelMongo.getBuId());// 移入事业部
		buMoveInventoryModel.setInBuName(merchantShopRelMongo.getBuName());
		buMoveInventoryModel.setOutBuId(shopShipperBuId);// 移出事业部
		buMoveInventoryModel.setOutBuName(shopShipperBuName);
		buMoveInventoryModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_SYBYK));// 事业部移库单号
		buMoveInventoryModel.setBusinessNo(orderModel.getExternalCode());// 外部业务单号
		buMoveInventoryModel.setOrderSource(DERP_ORDER.BUMOVEINVENTORY_ORDERSOURCE_2);//单据来源 2：系统自动生成
		buMoveInventoryModel.setStatus(DERP_ORDER.BUMOVEINVENTORY_STATUS_027);//移库状态
		buMoveInventoryModel.setDepotId(depotInfoMongo.getDepotId());
		buMoveInventoryModel.setDepotName(depotInfoMongo.getName());
		buMoveInventoryModel.setMoveDate(orderModel.getDeliverDate());// 移库时间(单据发货时间)
		buMoveInventoryModel.setMerchantId(merchantInfoMongo.getMerchantId());
		buMoveInventoryModel.setMerchantName(merchantInfoMongo.getName());
		buMoveInventoryModel.setCreateName("系统自动生成");
		buMoveInventoryModel.setCreateDate(TimeUtils.getNow());
		buMoveInventoryModel.setOrderType(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD);//  DSDD:电商订单

		buMoveInventoryDao.save(buMoveInventoryModel);//表头
		// 保存移库单表体
		BuMoveInventoryItemModel buMoveItemModel = new BuMoveInventoryItemModel();

		buMoveItemModel.setMoveId(buMoveInventoryModel.getId());
		buMoveItemModel.setGoodsId(wayBillItemModel.getGoodsId());
		buMoveItemModel.setGoodsCode(wayBillItemModel.getGoodsCode());
		buMoveItemModel.setGoodsName(wayBillItemModel.getGoodsName());
		buMoveItemModel.setGoodsNo(wayBillItemModel.getGoodsNo());
		buMoveItemModel.setBarcode(wayBillItemModel.getBarcode());
		buMoveItemModel.setCommbarcode(orderItemModel.getCommbarcode());// 标准条码
		buMoveItemModel.setNum(wayBillItemModel.getNum());
		buMoveItemModel.setCreateDate(TimeUtils.getNow());
		buMoveItemModel.setInStockLocationTypeId(merchantShopRelMongo.getStockLocationTypeId());
		buMoveItemModel.setInStockLocationTypeName(stockLocationMap.get(merchantShopRelMongo.getStockLocationTypeId()));
		buMoveItemModel.setOutStockLocationTypeId(shopShipperStockLocationTypeId);
		buMoveItemModel.setOutStockLocationTypeName(stockLocationMap.get(shopShipperStockLocationTypeId));

		/**
		 * 以“移入事业部+移出事业部+商品货号”为维度查协议单价配置表，若存在配置则带出，若不存在配置则留空；
		 */
		String accountCurrency = merchantInfoMongo.getAccountCurrency();// 成本核算币种
		Map<String,Object> findMap = new HashMap<>();
		findMap.put("merchantId",orderModel.getMerchantId());
		findMap.put("inBuId",merchantShopRelMongo.getBuId());
		findMap.put("outBuId",shopShipperBuId);
		findMap.put("goodsId",wayBillItemModel.getGoodsId());
		Map<String, Object> configMap = agreementCurrencyConfigDao.getConfigByMap(findMap);
		if(configMap!=null){	//若有配置
			String agreementCurrency = String.valueOf(configMap.get("currency"));// 协议币种
			// 保存事业部移库单表头的币种
			BuMoveInventoryModel modifyModel = new BuMoveInventoryModel();
			modifyModel.setId(buMoveInventoryModel.getId());
			modifyModel.setCurrency(accountCurrency);//移库币种
			modifyModel.setAgreementCurrency(agreementCurrency);// 协议币种
			modifyModel.setModifyDate(TimeUtils.getNow());
			buMoveInventoryDao.modify(modifyModel);

			String agreementPriceStr = String.valueOf(configMap.get("price"));// 协议单价
			BigDecimal agreementPrice = new BigDecimal(0);
			if(StringUtils.isNotBlank(agreementPriceStr)){
				agreementPrice = new BigDecimal(agreementPriceStr);
				buMoveItemModel.setAgreementPrice(agreementPrice);// 协议单价
			}
			if(agreementCurrency.equals(accountCurrency)){//币种一致
				buMoveItemModel.setPrice(agreementPrice);//移库单价
				BigDecimal amountBd = agreementPrice.multiply(new BigDecimal(wayBillItemModel.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
				buMoveItemModel.setAmount(amountBd);//移库总金额
				buMoveItemModel.setRate(1.0);//汇率
			}else{
				// 查汇率表
				Map<String,Object> exchangeRateMap = new HashMap<>();
				exchangeRateMap.put("origCurrencyCode",agreementCurrency);//协议币种
				exchangeRateMap.put("tgtCurrencyCode",accountCurrency);//成本核算币种(移库币种)
				exchangeRateMap.put("effectiveDate",TimeUtils.format(buMoveInventoryModel.getMoveDate(), "yyyy-MM-dd"));//根据移库日期
				List<ExchangeRateMongo> exchangeRateList = exchangeRateMongoDao.findAll(exchangeRateMap);
				if(exchangeRateList!=null && exchangeRateList.size()==1){
					ExchangeRateMongo exchangeRateMongo = exchangeRateList.get(0);
					if(null!=exchangeRateMongo.getRate()){
						Double rate = exchangeRateMongo.getRate();
						BigDecimal rateBd = new BigDecimal(rate);//汇率
						BigDecimal price = agreementPrice.multiply(rateBd);
						buMoveItemModel.setPrice(price);//移库单价
						BigDecimal amountBd = price.multiply(new BigDecimal(wayBillItemModel.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
						buMoveItemModel.setAmount(amountBd.stripTrailingZeros());//移库金额
						buMoveItemModel.setRate(rate);//汇率
					}
				}
			}
		}
		buMoveInventoryItemDao.save(buMoveItemModel);

		List<InventoryGoodsDetailJson> inInventoryGoodsDetailList = new ArrayList<>();// 移入

		// 移入商品实体
		InventoryGoodsDetailJson inInventoryGoodsDetailJson = new InventoryGoodsDetailJson();
		Long goodsId = wayBillItemModel.getGoodsId();
		String goodsName = wayBillItemModel.getGoodsName();
		String goodsNo = wayBillItemModel.getGoodsNo();
		String barcode = wayBillItemModel.getBarcode();
		String commbarcode = orderItemModel.getCommbarcode();

		inInventoryGoodsDetailJson.setGoodsId(String.valueOf(goodsId));
		inInventoryGoodsDetailJson.setGoodsName(goodsName);
		inInventoryGoodsDetailJson.setGoodsNo(goodsNo);
		inInventoryGoodsDetailJson.setCommbarcode(commbarcode);// 标准条码
		inInventoryGoodsDetailJson.setBarcode(barcode);
		inInventoryGoodsDetailJson.setBatchNo(wayBillItemModel.getBatchNo());//批次号
		inInventoryGoodsDetailJson.setNum(wayBillItemModel.getNum());
		if (wayBillItemModel.getProductionDate()!=null) {
			inInventoryGoodsDetailJson.setProductionDate(TimeUtils.formatFullTime(wayBillItemModel.getProductionDate()));
		}
		if (wayBillItemModel.getOverdueDate()!=null) {
			inInventoryGoodsDetailJson.setOverdueDate(TimeUtils.formatFullTime(wayBillItemModel.getOverdueDate()));
		}
		String expDate = TimeUtils.formatFullTime(wayBillItemModel.getOverdueDate());
		if (StringUtils.isNotBlank(expDate)) {
			Timestamp exTtime = TimeUtils.parseFullTime(expDate);
			String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
			inInventoryGoodsDetailJson.setIsExpire(isExpire);//是否过期（0是 1否	)
		}else {
			inInventoryGoodsDetailJson.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
		}
		inInventoryGoodsDetailJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//库存加减操作类型 1-调增
		inInventoryGoodsDetailJson.setBuId(String.valueOf(merchantShopRelMongo.getBuId()));
		inInventoryGoodsDetailJson.setBuName(merchantShopRelMongo.getBuName());
		inInventoryGoodsDetailJson.setStockLocationTypeId(merchantShopRelMongo.getStockLocationTypeId() == null ? "" : merchantShopRelMongo.getStockLocationTypeId().toString());
		inInventoryGoodsDetailJson.setStockLocationTypeName(stockLocationMap.get(merchantShopRelMongo.getStockLocationTypeId()));
		inInventoryGoodsDetailList.add(inInventoryGoodsDetailJson);

		// 移出商品实体
		InventoryGoodsDetailJson outInventoryGoodsDetailJson = new InventoryGoodsDetailJson();
		outInventoryGoodsDetailJson.setGoodsId(String.valueOf(goodsId));
		outInventoryGoodsDetailJson.setGoodsName(goodsName);
		outInventoryGoodsDetailJson.setGoodsNo(goodsNo);
		outInventoryGoodsDetailJson.setCommbarcode(commbarcode);// 标准条码
		outInventoryGoodsDetailJson.setBarcode(barcode);
		outInventoryGoodsDetailJson.setBatchNo(wayBillItemModel.getBatchNo());//批次号
		outInventoryGoodsDetailJson.setNum(wayBillItemModel.getNum());
		if (wayBillItemModel.getProductionDate()!=null) {
			outInventoryGoodsDetailJson.setProductionDate(TimeUtils.formatFullTime(wayBillItemModel.getProductionDate()));
		}
		if (wayBillItemModel.getOverdueDate()!=null) {
			outInventoryGoodsDetailJson.setOverdueDate(TimeUtils.formatFullTime(wayBillItemModel.getOverdueDate()));
		}
		if (StringUtils.isNotBlank(expDate)) {
			Timestamp exTtime = TimeUtils.parseFullTime(expDate);
			String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
			outInventoryGoodsDetailJson.setIsExpire(isExpire);//是否过期（0是 1否	)
		}else {
			outInventoryGoodsDetailJson.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
		}
		outInventoryGoodsDetailJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//库存加减操作类型  0-调减
		// 移出事业部
		outInventoryGoodsDetailJson.setBuId(String.valueOf(shopShipperBuId));
		outInventoryGoodsDetailJson.setBuName(shopShipperBuName);
		outInventoryGoodsDetailJson.setStockLocationTypeId(shopShipperStockLocationTypeId == null ? "" : shopShipperStockLocationTypeId.toString());
		outInventoryGoodsDetailJson.setStockLocationTypeName(stockLocationMap.get(shopShipperStockLocationTypeId));
		inInventoryGoodsDetailList.add(outInventoryGoodsDetailJson);


		// 实体
		InventoryDetailJson inInventoryDetailJson = new InventoryDetailJson();
		inInventoryDetailJson.setMerchantId(String.valueOf(orderModel.getMerchantId()));
		inInventoryDetailJson.setMerchantName(orderModel.getMerchantName());
		inInventoryDetailJson.setTopidealCode(merchantInfoMongo.getTopidealCode());
		inInventoryDetailJson.setDepotId(String.valueOf(depotInfoMongo.getDepotId()));
		inInventoryDetailJson.setDepotCode(depotInfoMongo.getDepotCode());
		inInventoryDetailJson.setDepotName(depotInfoMongo.getName());
		inInventoryDetailJson.setDepotType(depotInfoMongo.getType());
		inInventoryDetailJson.setOrderNo(buMoveInventoryModel.getCode());//来源单据号(存移库单号)
		inInventoryDetailJson.setBusinessNo(orderModel.getCode());//业务单据号(存电商订单号)
		inInventoryDetailJson.setOwnMonth(TimeUtils.formatMonth(buMoveInventoryModel.getMoveDate()));// 归属月份
		inInventoryDetailJson.setDivergenceDate(TimeUtils.formatFullTime(buMoveInventoryModel.getMoveDate()));
		inInventoryDetailJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0019);
		inInventoryDetailJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0031);//移库单
		inInventoryDetailJson.setSourceDate(TimeUtils.formatFullTime());
		inInventoryDetailJson.setShopCode(merchantShopRelMongo.getShopCode());// 店铺编码
		inInventoryDetailJson.setStorePlatformName(orderModel.getStorePlatformName());// 平台编码

		inInventoryDetailJson.setGoodsList(inInventoryGoodsDetailList);// 放入移入商品


		//回调设置
		Map<String, Object> customParam=new HashMap<String, Object>();
		inInventoryDetailJson.setBackTags(MQPushBackOrderEnum.MOVEORDER_INVENTORY_DETAIL_PUSH_BACK.getTags());//回调标签
		inInventoryDetailJson.setBackTopic(MQPushBackOrderEnum.MOVEORDER_INVENTORY_DETAIL_PUSH_BACK.getTopic());//回调主题
		customParam.put("code", buMoveInventoryModel.getCode());// 移库单单号
		inInventoryDetailJson.setCustomParam(customParam);////自定义回调参数
		// 移库单审核生成商品收发明细(移入)
		rocketMQProducer.send(JSONObject.fromObject(inInventoryDetailJson).toString(), MQInventoryEnum.INVENTORY_DETAIL_BY_MOVEORDER.getTopic(),MQInventoryEnum.INVENTORY_DETAIL_BY_MOVEORDER.getTags());

	}
}
