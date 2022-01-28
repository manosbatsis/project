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
import com.topideal.service.timer.GrabSmurfsOrderCollectionService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 抓取蓝精灵已经发货和已完成订单
 * @author 杨创
 *2019/03/11
 */
@Service
public class GrabSmurfsOrderCollectionServiceImpl implements GrabSmurfsOrderCollectionService {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(GrabSmurfsOrderCollectionServiceImpl.class);
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
	private RMQLogProducer rMQLogProducer;// 日志MQ
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao;// 商家和店铺中间表
	@Autowired
	private GroupMerchandiseContrastDao groupMerchandiseContrastDao; //组合品对照表
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private MerchantShopShipperMongoDao merchantShopShipperMongoDao;
	// 事业部移库单表头
	@Autowired
	private BuMoveInventoryDao buMoveInventoryDao;
	// 事业部移库单表体
	@Autowired
	private BuMoveInventoryItemDao buMoveInventoryItemDao;
	@Autowired
	private BuInventoryMongoDao buInventoryMongoDao;
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
	 * 单个插入
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13205124500, model = DERP_LOG_POINT.POINT_13205124500_Label,keyword="orderNo")
	public void insertGrabSmurfsOrderCollection(String json,String keys,String topics,String tags) throws Exception {
		JSONObject jSONOrderObject = JSONObject.fromObject(json);
		//LOGGER.info("每一条订单的json"+jSONOrderObject);
		String derpIsEnforce = (String) jSONOrderObject.get("derpIsEnforce"); //是否强制抓单 1-是 0-否
		//String derpDeliveryTime = (String) jSONOrderObject.get("derpDeliveryTime"); //发货时间（强制抓单是，发货时间不能为空）
		String derpStatus = (String) jSONOrderObject.get("derpStatus"); //订单状态
		String derpShopCode = (String) jSONOrderObject.get("derpShopCode"); //店铺编码


		// 获取店铺
		MerchantShopRelMongo merchantShopRel=getMerchantShopRel(derpShopCode);
		// 获取商家
		MerchantInfoMongo merchantInfoMongo=getMerchantInfoMongdb(merchantShopRel);
		//获取仓库
		DepotInfoMongo depotInfoMongo=getDepotInfoMongo(merchantShopRel.getDepotId());
		//数据校验
		dataCheck(jSONOrderObject,merchantShopRel,derpStatus,derpIsEnforce);
		JSONArray jsonGoodArray = jSONOrderObject.getJSONArray("orderGoodsList");
		String orderNo = jSONOrderObject.getString("orderNo");// 外部单号
		String deliveryDate = jSONOrderObject.getString("deliveryDate");// 发货时间
		Timestamp deliveryTime = TimeUtils.parse(deliveryDate,"yyyy-MM-dd HH:mm:ss");//发货时时间
		Timestamp createdTime = TimeUtils.parse(jSONOrderObject.getString("orderCreateDate"),"yyyy-MM-dd HH:mm:ss");// 交易时间



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

		//结算金额拆分
		BigDecimal paymentBigDecimal=new BigDecimal(0);//实付总额
		BigDecimal wayFrtFeeBigDecimal=new BigDecimal(0);//
		if (jSONOrderObject.get("payment")!=null&&(!"null".equals(jSONOrderObject.getString("payment")))&&StringUtils.isNotBlank(jSONOrderObject.getString("payment"))) {
			paymentBigDecimal=new BigDecimal(jSONOrderObject.getString("payment"));
		}
		if (jSONOrderObject.get("wayFrtFee")!=null&&(!"null".equals(jSONOrderObject.getString("wayFrtFee")))&&StringUtils.isNotBlank(jSONOrderObject.getString("wayFrtFee"))) {
			wayFrtFeeBigDecimal=new BigDecimal(jSONOrderObject.getString("wayFrtFee"));
		}


		BigDecimal taxBigDecimal=new BigDecimal("0");//税费
		// 税费
		if ((!"null".equals(jSONOrderObject.getString("tax")))&&StringUtils.isNotBlank(jSONOrderObject.getString("tax"))) {
			taxBigDecimal=new BigDecimal(jSONOrderObject.getString("tax"));
		}

		//判断报文表体字段“subOrderTaxFee” 字段值是否都为空或者0
		int existNum = 0;
		boolean isSplitByTaxFee = false; //是否以表头税费摊分

		for (int i = 0; i < jsonGoodArray.size(); i++) {
			JSONObject goodsJSONObject=(JSONObject)jsonGoodArray.get(i);
			//商品税费
			if (!goodsJSONObject.containsKey("subOrderTaxFee")) {
				existNum++;
			} else if (StringUtils.isBlank(goodsJSONObject.getString("subOrderTaxFee"))) {
				existNum++;
			} else if (new BigDecimal(goodsJSONObject.getString("subOrderTaxFee")).compareTo(BigDecimal.ZERO) == 0) {
				existNum++;
			}
			goodsJSONObject.put("index", String.valueOf(i));
		}
		//若表体商品税费都为0或空且表头税费不为0，则以表头税费摊分
		if (existNum == jsonGoodArray.size() && taxBigDecimal.compareTo(BigDecimal.ZERO) != 0) {
			isSplitByTaxFee = true;
		}
		// 不同电商平台走不同的流程
		JSONArray newGoodJsonArray = new JSONArray();
		if (merchantShopRel.getStorePlatformCode().equals(DERP.STOREPLATFORM_1000000310)) { //天猫平台
			getTmallOrderItem(jsonGoodArray, newGoodJsonArray, taxBigDecimal.doubleValue(), merchantInfoMongo, merchantShopRel, isSplitByTaxFee);
		} else if (merchantShopRel.getStorePlatformCode().equals(DERP.STOREPLATFORM_1000000390)
				|| merchantShopRel.getStorePlatformCode().equals(DERP.STOREPLATFORM_1000002612)) { //小红书、考拉平台
			String orderDiscount = jSONOrderObject.getString("discount");// 订单优惠金额
			getKaoLaOrXHSOrderItem(jsonGoodArray,newGoodJsonArray, taxBigDecimal.doubleValue(),merchantInfoMongo, merchantShopRel, isSplitByTaxFee, orderDiscount);
		} else if(DERP.STOREPLATFORM_100044998.equals(merchantShopRel.getStorePlatformCode()))  {// 平台（京东）

			/*1、若订单商品行数为1条：payment-tax_total-way_frt_fee
			2、若订单商品行数大于1条：
			1）前(N-1）个商品：dec_total/(dec_total总额)*(payment-tax_total-way_frt_fee)
			2）第N个商品：payment-tax_total-way_frt_fee-前N-1个商品结算总额*/
			String orderDiscount = jSONOrderObject.getString("discount");// 订单优惠金额
			//结算金额拆分
			// 京东结算总金额的拆分逻辑
			BigDecimal orderDecTotal=paymentBigDecimal.subtract(wayFrtFeeBigDecimal).subtract(taxBigDecimal);


			getJDOrderItem(jsonGoodArray, newGoodJsonArray, orderDecTotal,taxBigDecimal.doubleValue(), merchantInfoMongo, merchantShopRel, isSplitByTaxFee, orderDiscount);
		}else {
			throw new RuntimeException("商家id:"+merchantInfoMongo.getMerchantId() + ",店铺编码:" + merchantShopRel.getShopCode()+",的平台没有对应的流程平台编码:"+merchantShopRel.getStorePlatformCode());
		}

		// 获取运单号
		String wayBillNo=null;
		Object goodsObject0 = newGoodJsonArray.get(0);
		JSONObject goodsJSONObject0=(JSONObject) goodsObject0;
		if (goodsJSONObject0.get("invoiceNo")!=null&&StringUtils.isNotBlank(goodsJSONObject0.getString("invoiceNo"))) {
			wayBillNo=goodsJSONObject0.getString("invoiceNo");
		}else {
			wayBillNo = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZDY);// 自定义运单;
		}
		// 获取物流企业名称
		String logisticsCompany=null;
		if (goodsJSONObject0.get("logisticsCompany")!=null&&StringUtils.isNotBlank(goodsJSONObject0.getString("logisticsCompany"))) {
			logisticsCompany=goodsJSONObject0.getString("logisticsCompany");
		}

		// 封装电商订单表头
		OrderModel orderModel=getOrderModel(jSONOrderObject,merchantInfoMongo,depotInfoMongo,merchantShopRel,
				createdTime,deliveryTime,orderNo,logisticsCompany,wayBillNo,taxBigDecimal);
		// 封装运单表头
		WayBillModel wayBillModel = getWayBill(orderModel,wayBillNo,deliveryTime,logisticsCompany);
		//插入唯一表校验
		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderNo);
			orderExternalCodeModel.setOrderSource(1);	// 订单100
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			throw new RuntimeException("订单100外部单号来源表已经存在 单号：" + orderNo + "  保存失败");
		}
		// 生成运单表
		Long id = orderDao.save(orderModel);
		// 向运单表中插入数据
		wayBillModel.setOrderId(orderModel.getId());
		wayBillDao.save(wayBillModel);

		// 加减库存商品实体
		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		Map<String, Object> shopShipperParams = new HashMap<String, Object>();
		shopShipperParams.put("merchantId", merchantInfoMongo.getMerchantId()) ;
		shopShipperParams.put("shopId", merchantShopRel.getShopId()) ;	// 店铺ID
		List<MerchantShopShipperMongo> merchantShopShipperList = merchantShopShipperMongoDao.findAll(shopShipperParams);

		//记录库存类型信息
		Map<Long,String> stockLocationMap = new HashMap<Long,String>();
		for (Object goodsObject : newGoodJsonArray) {
			JSONObject goodsJSONObject=(JSONObject) goodsObject;
			String goodsNo = goodsJSONObject.getString("goodsNo");// 商品货号
			String barcode = goodsJSONObject.getString("barcode");// 条形码
			String goodsCode = goodsJSONObject.getString("goodsCode");// 商品编号
			String goodsId = goodsJSONObject.getString("goodsId");// 商品id
			String goodsName = goodsJSONObject.getString("goodsName");// 商品名称
			String numStr = goodsJSONObject.getString("num");// 商品数量
			String priceStr = goodsJSONObject.getString("price");// 结算单价
			String totalPriceStr = goodsJSONObject.getString("totalPrice");// 结算总金额
			String originalPrice = goodsJSONObject.getString("originalPrice"); //销售单价
			String originalPriceTotal = goodsJSONObject.getString("originalPriceTotal"); //销售总金额
			String commbarcode = goodsJSONObject.getString("commbarcode");//标准条码
			String goodsTaxStr = goodsJSONObject.getString("goodsTax");//商品税费
			String skuId= goodsJSONObject.getString("skuId");
			//String totalPriceStr = goodsJSONObject.getString("totalPrice");// 总价
			//Object goodsDiscountObject = goodsJSONObject.get("goodsDiscount");// 减免金额
			// 插入电商表体
			OrderItemModel 	orderItemModel =new OrderItemModel();
			orderItemModel.setSkuId(skuId);
			orderItemModel.setOrderId(orderModel.getId());//电商订单ID'
			orderItemModel.setGoodsName(goodsName);//商品名称
			orderItemModel.setGoodsId(Long.valueOf(goodsId));//商品ID
			orderItemModel.setGoodsCode(goodsCode);//商品编码
			orderItemModel.setBarcode(barcode);//条形码
			orderItemModel.setGoodsNo(goodsNo);//商品货号
			orderItemModel.setCommbarcode(commbarcode);//商品标准条码
			if ((!"null".equals(goodsTaxStr))&&StringUtils.isNotBlank(goodsTaxStr)) {
				orderItemModel.setTax(new BigDecimal(goodsTaxStr));//商品税费
			}
			BigDecimal price=null;
			if ((!"null".equals(priceStr))&&StringUtils.isNotBlank(priceStr)) {
				orderItemModel.setPrice(new BigDecimal(priceStr).setScale(2, BigDecimal.ROUND_HALF_UP));// 单价
			}
			if ((!"null".equals(totalPriceStr))&&StringUtils.isNotBlank(totalPriceStr)) {
				orderItemModel.setDecTotal(new BigDecimal(totalPriceStr).setScale(2, BigDecimal.ROUND_HALF_UP));// 总价
			}
			if (null!=goodsJSONObject.get("goodsDiscount")&&!"null".equals(goodsJSONObject.getString("goodsDiscount"))&&StringUtils.isNotBlank(goodsJSONObject.getString("goodsDiscount"))) {
				String goodsDiscount = goodsJSONObject.getString("goodsDiscount");
				BigDecimal goodsDiscountDecimal = new BigDecimal(goodsDiscount);
				orderItemModel.setGoodsDiscount(goodsDiscountDecimal);// 商品优惠减免金额
			}
			if ((!"null".equals(originalPrice))&&StringUtils.isNotBlank(originalPrice)) {
				orderItemModel.setOriginalPrice(new BigDecimal(originalPrice).setScale(2, BigDecimal.ROUND_HALF_UP));// 单价
			}
			if ((!"null".equals(originalPriceTotal))&&StringUtils.isNotBlank(originalPriceTotal)) {
				orderItemModel.setOriginalDecTotal(new BigDecimal(originalPriceTotal).setScale(2, BigDecimal.ROUND_HALF_UP));// 总价
			}
			orderItemModel.setNum(Integer.valueOf(numStr));// 数量
			// 1.先看店铺类型是否为单主店还是外部店，若为单主店就先拿开店事业部查询库存
			Map<String, Object> buInventoryParams = new HashMap<String, Object>();
			buInventoryParams.put("merchantId", merchantInfoMongo.getMerchantId());//商家
			buInventoryParams.put("depotId", depotInfoMongo.getDepotId());	// 仓库ID
			buInventoryParams.put("goodsId", Long.valueOf(goodsId));// 商品id
			buInventoryParams.put("month", TimeUtils.formatMonth(orderModel.getDeliverDate()));// 月份(取发货时间)
			int flag=0;// 是否查询货主事业部0:不查；1：查
			Long buId = null;//事业部
			String buName = null;
			Long stockLocationTypeId = null;//库存类型id
			Long shopShipperStockLocationTypeId = null;//库存类型id
			// 为单主店就先拿开店事业部查询库存(外部店不会有移库操作)
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRel.getStoreTypeCode())){
				buInventoryParams.put("buId",merchantShopRel.getBuId());// 开店事业部
				List<BuInventoryMongo> buInventoryList = buInventoryMongoDao.findAll(buInventoryParams);
				if(buInventoryList==null || buInventoryList.size()==0){	// 若没有库存，就查货主事业部
					flag=1;
				}else if(buInventoryList!=null && buInventoryList.size()>1){
					throw new RuntimeException("根据商家+仓库+事业部+商品id+月份在事业部库存表找到多条信息");
				}else if(buInventoryList.size()==1 && buInventoryList.get(0).getOkNum()>0){
					buId = merchantShopRel.getBuId();//拿开店事业部
					buName = merchantShopRel.getBuName();
					stockLocationTypeId = merchantShopRel.getStockLocationTypeId();
				}else if(buInventoryList.get(0).getOkNum()<=0){// 若没有库存，就查货主事业部
					flag=1;
				}
			}
			int isNotChange=0;//按现有逻辑，以唯一开店事业部生成单据并出库
			int countHave=0;	// 记录某个商品是否每个事业部都有库存
			int countNone=0;	// 记录某个商品是否每个事业部都没库存
			Long shopShipperBuId=null; //货主事业部id
			String shopShipperBuName=null; //货主事业部名称
			// 外部店就直接查货主事业部（外部店无开店事业部）或者 为单主店但是开店事业部无库存(然后看是否有货主事业部)
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRel.getStoreTypeCode()) ||
					flag==1){
				if(merchantShopShipperList==null ||merchantShopShipperList.size()==0){
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
								buId = merchantShopRel.getBuId();//拿开店事业部
								buName = merchantShopRel.getBuName();
								stockLocationTypeId = merchantShopRel.getStockLocationTypeId();
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
					shopShipperStockLocationTypeId= null;
				}
			}
			if(isNotChange==1){//以唯一开店事业部生成单据并出库
				buId = merchantShopRel.getBuId();//拿开店事业部
				buName = merchantShopRel.getBuName();
				stockLocationTypeId = merchantShopRel.getStockLocationTypeId();
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
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRel.getStoreTypeCode()) || isNotChange==1){
				orderItemModel.setBuId(buId);
				orderItemModel.setBuName(buName);
				orderItemModel.setStockLocationTypeId(stockLocationTypeId);
				orderItemModel.setStockLocationTypeName(stockLocationTypeName);
			}else if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRel.getStoreTypeCode())){// 外部店
				orderItemModel.setBuId(shopShipperBuId);
				orderItemModel.setBuName(shopShipperBuName);
				orderItemModel.setStockLocationTypeId(shopShipperStockLocationTypeId);
				orderItemModel.setStockLocationTypeName(shopShipperStockLocationTypeName);
			}
			orderItemDao.save(orderItemModel);


			// 向运单表体中查入数据
			WayBillItemModel wayBillItemModel = new WayBillItemModel();
			wayBillItemModel.setBillId(wayBillModel.getId());
			wayBillItemModel.setGoodsId(Long.valueOf(goodsId));
			wayBillItemModel.setGoodsNo(goodsNo);
			wayBillItemModel.setGoodsName(goodsName);
			wayBillItemModel.setNum(Integer.valueOf(numStr));
			wayBillItemModel.setPrice(price);
			wayBillItemModel.setGoodsCode(goodsCode);
			wayBillItemModel.setBarcode(barcode);
			// 运单表体插入数据
			wayBillItemDao.save(wayBillItemModel);

			InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRel.getStoreTypeCode()) && countHave==1){// 如果是单主店，需要移库
				saveBuMoveInventory(orderModel,orderItemModel,depotInfoMongo,merchantInfoMongo,
						shopShipperBuId,shopShipperBuName,merchantShopRel,wayBillItemModel,shopShipperStockLocationTypeId,stockLocationMap);
			}

			invetAddOrSubtractGoodsListJson.setGoodsId(goodsId);
			invetAddOrSubtractGoodsListJson.setGoodsName(goodsName);
			invetAddOrSubtractGoodsListJson.setGoodsNo(goodsNo);
			invetAddOrSubtractGoodsListJson.setBarcode(barcode);


			invetAddOrSubtractGoodsListJson.setType("0");// 好品
			invetAddOrSubtractGoodsListJson.setNum(Integer.valueOf(numStr));
			invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
			invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）
			//事业部
			if (buId != null && StringUtils.isNotBlank(buName)) {
				invetAddOrSubtractGoodsListJson.setBuId(String.valueOf(buId));
				invetAddOrSubtractGoodsListJson.setBuName(buName);
			}
			invetAddOrSubtractGoodsListJson.setStockLocationTypeId(orderItemModel.getStockLocationTypeId()== null ? "":orderItemModel.getStockLocationTypeId().toString());
			invetAddOrSubtractGoodsListJson.setStockLocationTypeName(orderItemModel.getStockLocationTypeName());

			invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
		}
		//扣减销售出库库存量
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String now1 = sdf.format(new Date());
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
		invetAddOrSubtractRootJson.setSourceDate(now1);
		invetAddOrSubtractRootJson.setDivergenceDate(deliveryDate);
		// 获取当前年月
		Timestamp deliverDateTime = Timestamp.valueOf(deliveryDate);
		invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDateTime));
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
	 * 获取运单
	 */
	private WayBillModel getWayBill(OrderModel orderModel,String wayBillNo,Timestamp deliveryTime,String logisticsCompany) {
		WayBillModel wayBillModel = new WayBillModel();
		wayBillModel.setOrderId(orderModel.getId());
		wayBillModel.setWayBillNo(wayBillNo);// 自定义运单信息
		wayBillModel.setDeliverDate(deliveryTime);
		wayBillModel.setLogisticsCode(logisticsCompany);//物流公司编码
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
			DepotInfoMongo depotInfoMongo,MerchantShopRelMongo merchantShopRel,
			Timestamp createdTime,Timestamp deliveryTime,String orderNo,String logisticsCompany,String wayBillNo,
			BigDecimal taxBigDecimal) throws Exception {
		// 插入订单封装数据
		BigDecimal wayFrtFeeBigDecimal=new BigDecimal("0");
		// 运杂费
		if ((!"null".equals(jSONOrderObject.getString("wayFrtFee")))&&StringUtils.isNotBlank(jSONOrderObject.getString("wayFrtFee"))) {
			wayFrtFeeBigDecimal=new BigDecimal(jSONOrderObject.getString("wayFrtFee"));
		}
		BigDecimal discountBigDecimal=new BigDecimal("0");
		// 优惠减免金额
		if ((!"null".equals(jSONOrderObject.getString("discount")))&&StringUtils.isNotBlank(jSONOrderObject.getString("discount"))) {
			discountBigDecimal=new BigDecimal(jSONOrderObject.getString("discount"));
		}
		BigDecimal paymentBigDecimal=new BigDecimal("0");
		// 订单实付总额
		if ((!"null".equals(jSONOrderObject.getString("payment")))&&StringUtils.isNotBlank(jSONOrderObject.getString("payment"))) {
			paymentBigDecimal=new BigDecimal(jSONOrderObject.getString("payment"));
		}
		String orderCreateDate = jSONOrderObject.getString("orderCreateDate");//(创建时间)
		String orderModifyDate =  jSONOrderObject.getString("orderModifyDate");// 修改时间(蓝精灵上游订单修改时间)
		String receiverName = null;//收货人姓名
		if (jSONOrderObject.get("receiverName")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("receiverName"))) {
			receiverName = jSONOrderObject.getString("receiverName");//收货人手机号码
			receiverName = receiverName.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+","");// 收货人姓名 去掉特殊字符
		}
		String receiverMobile = null;
		if (jSONOrderObject.get("receiverMobile")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("receiverMobile"))) {
			receiverMobile = jSONOrderObject.getString("receiverMobile");//收货人手机号码
		}
		String receiverCountry =null;//国家
		if (jSONOrderObject.get("receiverCountry")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("receiverCountry"))) {
			receiverCountry = jSONOrderObject.getString("receiverCountry");//收货人手机号码
		}
		String receiverState =null;//省份
		if (jSONOrderObject.get("receiverState")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("receiverState"))) {
			receiverState = jSONOrderObject.getString("receiverState");//收货人手机号码
		}
		String receiverCity = null;//市
		if (jSONOrderObject.get("receiverCity")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("receiverCity"))) {
			receiverCity = jSONOrderObject.getString("receiverCity");//收货人手机号码
		}
		String receiverAddress = null;//地址
		if (jSONOrderObject.get("receiverAddress")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("receiverAddress"))) {
			receiverAddress = jSONOrderObject.getString("receiverAddress");//收货人手机号码
		}
		String receiverDistrict = null;//区
		if (jSONOrderObject.get("receiverDistrict")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("receiverDistrict"))) {
			receiverDistrict=jSONOrderObject.getString("receiverDistrict");
		}
		String receiverZip = null;//邮编
		if (jSONOrderObject.get("receiverZip")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("receiverZip"))) {
			receiverZip=jSONOrderObject.getString("receiverZip");
		}

		String remark = null;//备注
		if (jSONOrderObject.get("remark")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("remark"))) {
			remark=jSONOrderObject.getString("remark");
		}
		String taxStr = null;//备注
		if (jSONOrderObject.get("tax")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("tax"))) {
			taxStr=jSONOrderObject.getString("tax");
		}
		String payDateStr = null;//支付时间
		if (jSONOrderObject.get("payDate")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("payDate"))) {
			payDateStr=jSONOrderObject.getString("payDate");
		}
		// 新增电商订单
		OrderModel orderModel =new OrderModel();
		orderModel.setCustomerId(merchantShopRel.getCustomerId());
		orderModel.setCustomerName(merchantShopRel.getCustomerName());
		orderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DSDD)); // 电商订单编码
		if ((!"null".equals(payDateStr))&&StringUtils.isNotBlank(payDateStr)) {
			Timestamp payDate = TimeUtils.parse(payDateStr,"yyyy-MM-dd HH:mm:ss");
			orderModel.setTradingDate(payDate);// 交易时间
		}

		orderModel.setStatus("027");//单据状态：1:待申报 2.待放行,3:待发货,4:已发货,5:已取消' 027 出库中
		orderModel.setDepotId(depotInfoMongo.getDepotId());// 仓库id
		orderModel.setDepotName(depotInfoMongo.getName());// 仓库名称
		orderModel.setReceiverName(receiverName);// 收件人名称
		orderModel.setReceiverTel(receiverMobile);// 收件人手机
		orderModel.setReceiverCountry(receiverCountry);// 收件人国家
		orderModel.setReceiverProvince(receiverState);// 收件人省份
		orderModel.setReceiverCity(receiverCity);// 城市
		orderModel.setReceiverArea(receiverDistrict);// 区
		orderModel.setReceiverAddress(receiverAddress);// 地址
		orderModel.setOrderSource(4);//订单来源  1:跨境宝推送, 2:导入',3 第e仓  4 订单100
		orderModel.setStorePlatformName(merchantShopRel.getStorePlatformCode());		// 电商平台编码
		orderModel.setMerchantId(merchantInfoMongo.getMerchantId());// 商家id
		orderModel.setMerchantName(merchantInfoMongo.getName());// 商家名称
		orderModel.setShopCode(merchantShopRel.getShopCode());// 店铺编码
		orderModel.setShopName(merchantShopRel.getShopName());// 店铺名称
		orderModel.setShopTypeCode(merchantShopRel.getShopTypeCode());// 店铺类型编码
		orderModel.setCurrency(DERP.CURRENCYCODE_CNY);	// 默认人民币
		orderModel.setRemark(remark);// 备注
		orderModel.setWayFrtFee(wayFrtFeeBigDecimal);//运费，2位小数
		if (jSONOrderObject.get("paymentGoods")!=null&&(!"null".equals(jSONOrderObject.getString("paymentGoods")))&&StringUtils.isNotBlank(jSONOrderObject.getString("paymentGoods"))) {
			BigDecimal paymentGoods=new BigDecimal(jSONOrderObject.getString("paymentGoods"));
			orderModel.setGoodsAmount(paymentGoods);//货款
		}
		orderModel.setPayment(paymentBigDecimal);//订单实付金额
		orderModel.setDiscount(discountBigDecimal);
		orderModel.setMakingTime(createdTime);// 制单时间
		orderModel.setDeliverDate(deliveryTime);// 发货时间
		orderModel.setExternalCode(orderNo);// 外部单号
		orderModel.setLogisticsName(logisticsCompany);//物流企业编码
		orderModel.setWayBillNo(wayBillNo);
		orderModel.setTaxes(taxBigDecimal);

		return orderModel;
	}

	/**
	 * 数据校验
	 * @param jSONOrderObject
	 * @param
	 */
	private void dataCheck(JSONObject jSONOrderObject, MerchantShopRelMongo merchantShopRel,String derpStatus,String derpIsEnforce) {
		// 是否为空校验
		isNotBlankCheck(jSONOrderObject,"orderNo,orderCreateDate,orderModifyDate,wayFrtFee,discount,deliveryDate,orderGoodsList");
		// 获取第一条商品的json
		JSONArray jsonGoodArray = jSONOrderObject.getJSONArray("orderGoodsList");
		Object goodsjsonGood0 = jsonGoodArray.get(0);
		JSONObject jsonGood0JSONObject=(JSONObject)goodsjsonGood0;
		// 如果是强制抓单，则不判断运单号是否为空
		if (StringUtils.isEmpty(derpIsEnforce) || !"1".equals(derpIsEnforce)) {
			// 如果状态是4 已关闭 并且运单号为空的订单 不进行出库
			if ("4".equals(derpStatus)&&(jsonGood0JSONObject.get("invoiceNo")==null||StringUtils.isBlank(jsonGood0JSONObject.getString("invoiceNo"))||"null".equals(jsonGood0JSONObject.getString("invoiceNo")))) {
				throw new RuntimeException("订单是状态是已已关闭并且运单号invoiceNo为空 拦截");
			}
		}

		String deliveryDate = jSONOrderObject.getString("deliveryDate");
		Timestamp deliveryTime = TimeUtils.parse(deliveryDate,"yyyy-MM-dd HH:mm:ss");//发货时时间
		if (null==deliveryTime)throw new RuntimeException("发货时间格式不正确,deliveryDate");
		Timestamp createdTime = TimeUtils.parse(jSONOrderObject.getString("orderCreateDate"),"yyyy-MM-dd HH:mm:ss");
		if (null==createdTime)throw new RuntimeException("交易时间格式不正确,orderCreateDate");
		// 不接发货时间小于2019-04-01 00:00:00的单
		Timestamp deliverTimestamp = TimeUtils.parseFullTime(deliveryDate);
		Timestamp deliverTimestamp2 = Timestamp.valueOf("2019-03-01 00:00:00");
		if (deliverTimestamp.getTime()<deliverTimestamp2.getTime())throw new RuntimeException("发货日期小于2019-03-01");
		//天猫平台
		if (merchantShopRel.getStorePlatformCode().equals(DERP.STOREPLATFORM_1000000310)) {
			Timestamp deliverTimestamp3 = Timestamp.valueOf("2019-11-01 00:00:00");
			if (deliverTimestamp.getTime()<deliverTimestamp3.getTime())throw new RuntimeException("天猫店铺发货日期小于2019-11-01");
		}
		// 发货时间不能小于交易时间
		if (jSONOrderObject.get("payDate")!=null&&StringUtils.isNotBlank(jSONOrderObject.getString("payDate"))) {
			String payDateStr=jSONOrderObject.getString("payDate");// 支付时间
			Timestamp payDate = TimeUtils.parse(payDateStr,"yyyy-MM-dd HH:mm:ss");
			if (deliveryTime.getTime()<payDate.getTime()) {
				throw new RuntimeException("发货时间deliveryDate不能小于交易时间payDate");
			}
		}
	}

	/**
	 * 是否为校验
	 * @param jSONOrderObject
	 * @param codes
	 */
	private void isNotBlankCheck(JSONObject jSONOrderObject, String codes) {
		List<String> asList = Arrays.asList(codes.split(","));
		for (String code : asList) {
			if (jSONOrderObject.get(code)==null||StringUtils.isBlank(jSONOrderObject.getString(code))
					||"null".equals(jSONOrderObject.getString(code))) {
				throw new RuntimeException(code+" is null ");
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
		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRel.getStoreTypeCode())){//如果为外部店就要查货主表
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
		}

		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRel.getStoreTypeCode())){ // 如果为单主店直接查询关联表的商家id
			merchantId=merchantShopRel.getMerchantId();
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
	private MerchantShopRelMongo getMerchantShopRel(String derpShopCode) {
		Map<String, Object> merchantShopRelMap = new HashMap<>();
		merchantShopRelMap.put("shopCode",derpShopCode );
		merchantShopRelMap.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1);//'状态(1使用中,0已禁用)'
		merchantShopRelMap.put("dataSourceCode", DERP.DATASOURCE_4);	// 数据来源：订单100
		List<MerchantShopRelMongo> merchantShopRelList = merchantShopRelMongoDao.findAll(merchantShopRelMap);
		if (merchantShopRelList==null||merchantShopRelList.size()==0) {
			throw new RuntimeException("没有查到商家店铺关系表数据");
		}
		if (merchantShopRelList.size()>1) {
			throw new RuntimeException("存在多条商家店铺关系");
		}
		MerchantShopRelMongo merchantShopRel = merchantShopRelList.get(0);
		return merchantShopRel;
	}

	/**
	 * 抓取订单采集数据异常 保存到mongodb
	 * @param jsonError
	 */
	@Override
	public void grabSmurfsOrderCollectionError(JSONObject jsonError,JSONObject json,String orderNo){
		LOGGER.info("定时器抓蓝精灵订单采集数据jsonError"+jsonError);
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMddhhmmss");
		Date date=new Date();
		//String format = sdf1.format(date);// keyword 用当前时间来存
		Long startDate = date.getTime();
		Long endDate = startDate;
		MQLog mqLog=new MQLog();
		mqLog.setModel("定时器抓蓝精灵订单采集数据");
		mqLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_13205124501));
		if (StringUtils.isBlank(orderNo)) {
			mqLog.setKeyword("Time"+String.valueOf(date.getTime()));
		}else {
			mqLog.setKeyword(orderNo);
		}
		mqLog.setKeywordName("orderNo");
		mqLog.setMethod("com.topideal.service.timer.impl.GrabSmurfsOrderCollectionServiceImpl.getGrabSmurfsOrderCollection()");
		mqLog.setStartDate(startDate);
		mqLog.setEndDate(endDate);
		//mqLog.setDesc((String)errorJSONObject.get("expMsg"));
		mqLog.setState(0);
		mqLog.setExpMsg((String)jsonError.get("expMsg"));
		mqLog.setTopics(MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTopic());
		mqLog.setTags(MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTags());
		JSONObject jsonObject=JSONObject.fromObject(mqLog);
		jsonObject.put("id", UUID.randomUUID().toString());
		jsonObject.put("requestMessage", json);
		jsonObject.put("returnMessage",(String)jsonError.get("expMsg"));
		jsonObject.put("moduleCode", LogModuleType.MODULE_ORDER.getType());
        try {
        	rMQLogProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
		} catch (Exception e) {
			LOGGER.error("定时器抓蓝精灵订单采集数据---推送保存mongdb日志mq失败"+ jsonError);
			e.printStackTrace();
		}
	}

	/**
	 * 单个订单消费 订单采集数据异常 保存到mongodb
	 * @param jsonError
	 */
	@Override
	public void getOneSmurfsOrderCollectionError(JSONObject jsonError,JSONObject json,String orderNo){
		LOGGER.info("费单个蓝精灵订单采集数据jsonError"+jsonError);
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMddhhmmss");
		Date date=new Date();
		//String format = sdf1.format(date);// keyword 用当前时间来存
		Long startDate = date.getTime();
		Long endDate = startDate;
		MQLog mqLog=new MQLog();
		mqLog.setModel(DERP_LOG_POINT.POINT_13205124500_Label);
		mqLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_13205124500));
		if (StringUtils.isBlank(orderNo)) {
			mqLog.setKeyword("Time"+String.valueOf(date.getTime()));
		}else {
			mqLog.setKeyword(orderNo);
		}
		mqLog.setKeywordName("orderNo");
		mqLog.setMethod("com.topideal.service.timer.impl.GrabSmurfsOrderCollectionServiceImpl.getGrabSmurfsOrderCollection()");
		mqLog.setStartDate(startDate);
		mqLog.setEndDate(endDate);
		//mqLog.setDesc((String)errorJSONObject.get("expMsg"));
		mqLog.setState(0);
		mqLog.setExpMsg((String)jsonError.get("expMsg"));
		mqLog.setTopics(MQOrderEnum.GETONE_SMURFS_ORDER_COLLECTION_ORDER.getTopic());
		mqLog.setTags(MQOrderEnum.GETONE_SMURFS_ORDER_COLLECTION_ORDER.getTags());
		JSONObject jsonObject=JSONObject.fromObject(mqLog);
		jsonObject.put("id", UUID.randomUUID().toString());
		jsonObject.put("requestMessage", json);
		jsonObject.put("returnMessage",(String)jsonError.get("expMsg"));
		jsonObject.put("moduleCode", LogModuleType.MODULE_ORDER.getType());
        try {
        	rMQLogProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
		} catch (Exception e) {
			LOGGER.error("定时器抓蓝精灵订单采集数据---推送保存mongdb日志mq失败"+ jsonError);
			e.printStackTrace();
		}
	}

	@Override
	public boolean isExistOrderNo(String orderNo) throws SQLException {
		OrderModel orderQuery=new OrderModel();
		orderQuery.setExternalCode(orderNo);
		orderQuery = orderDao.searchByModel(orderQuery);
		// 订单订单 外部单号已经存在
		if (orderQuery!=null) {
			return true;
		}
		return false;
	}

	/**
	 * 拆分组合品价格
	 * @param goodsPrice 拆分价格
	 * @param list 拆分商品集合
	 * @param scale 保留小数位数
	 */
	public Map<String, Double> getSplitPrice(List<GroupMerchandiseContrastDetailModel> list, Double goodsPrice, Integer scale) {
		if (scale == null) {
			scale = 2; //若为空，默认保留4位小数
		}
		Map<String, Double> priceMap = new HashMap<>();
		BigDecimal totalPrice = new BigDecimal("0");
		BigDecimal deductPrice = new BigDecimal("0");
		BigDecimal goodsPriceBigDecimal = new BigDecimal(String.valueOf(goodsPrice));
		for (GroupMerchandiseContrastDetailModel model : list) {
			totalPrice = totalPrice.add(model.getPrice().multiply(new BigDecimal(model.getNum())));
		}
		for (int i = 0; i < list.size(); i++) {
			String goodsNo = list.get(i).getGoodsNo();
			if (i == list.size()-1) {
				BigDecimal gPrice = goodsPriceBigDecimal.subtract(deductPrice);
				priceMap.put(goodsNo, gPrice.doubleValue());
			} else {
				BigDecimal gPrice = (list.get(i).getPrice().multiply(new BigDecimal(list.get(i).getNum())))
                        .divide(totalPrice, 2, BigDecimal.ROUND_HALF_UP).multiply(goodsPriceBigDecimal).setScale(scale,BigDecimal.ROUND_HALF_UP);
				priceMap.put(goodsNo, gPrice.doubleValue());
				deductPrice = deductPrice.add(gPrice);
			}
		}
		return priceMap;
	}

	/**
	 * 平摊优惠金额
	 */
	public Map<String, Double> getSplitGoodsPrice(JSONArray goodsArray, Double discount) {
		Map<String, Double> priceMap = new HashMap<>();
		BigDecimal totalPrice = new BigDecimal("0");
		BigDecimal deductPrice = new BigDecimal("0");
		for (Object goods : goodsArray) {
			JSONObject goodsJson = (JSONObject) goods;
			Integer goodsNum = goodsJson.getInt("goodsNum");
			String goodsPrice = goodsJson.getString("goodsPrice");
			String discountFee = goodsJson.getString("discountFee");
			totalPrice = new BigDecimal(goodsNum).multiply(new BigDecimal(goodsPrice)).subtract(new BigDecimal(discountFee)).add(totalPrice);
		}
		for (int i = 0; i < goodsArray.size(); i++) {
			JSONObject goodsJson = (JSONObject) goodsArray.get(i);
			String goodsNo = goodsJson.getString("goodsNo");
			Integer goodsNum = goodsJson.getInt("goodsNum");
			String index = goodsJson.getString("index");
			String goodsPrice = goodsJson.getString("goodsPrice");
			String discountFee = goodsJson.getString("discountFee");
			if (i == goodsArray.size()-1) {
				Double gPrice = new BigDecimal(discount).subtract(deductPrice).doubleValue();
				priceMap.put(goodsNo+"_"+index, gPrice);
			} else {
				BigDecimal subTotalPrice = new BigDecimal(goodsNum).multiply(new BigDecimal(goodsPrice)).subtract(new BigDecimal(discountFee));
				BigDecimal gPrice = subTotalPrice.divide(totalPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(discount));
				priceMap.put(goodsNo+"_"+index, gPrice.doubleValue());
				deductPrice = gPrice.add(deductPrice);
			}
		}
		return priceMap;
	}

	/**
	 * 平摊税费
	 */
	public Map<String, Double> getSplitGoodsTax(List<Map<String, Object>> mapList, Double tax) {
		Map<String, Double> priceMap = new HashMap<>();
		BigDecimal totalPrice = new BigDecimal("0");
		BigDecimal deductPrice = new BigDecimal("0");
		for (Map<String, Object> goodsMap : mapList) {
			String goodsNum = (String) goodsMap.get("goodsNum");
			String goodsPrice = (String) goodsMap.get("goodsPrice");
			totalPrice = new BigDecimal(goodsNum).multiply(new BigDecimal(goodsPrice)).add(totalPrice);
		}
		for (int i = 0; i < mapList.size(); i++) {
			Map<String, Object> goodsMap = mapList.get(i);
			String index = (String) goodsMap.get("index");
			String goodsNo = (String) goodsMap.get("goodsNo");
			String goodsNum = (String) goodsMap.get("goodsNum");
			String goodsPrice = (String) goodsMap.get("goodsPrice");
			if (i == mapList.size()-1) {
				Double gPrice = new BigDecimal(tax).subtract(deductPrice).doubleValue();
				priceMap.put(goodsNo+"_"+index, gPrice);
			} else {
				BigDecimal subTotalPrice = new BigDecimal(goodsNum).multiply(new BigDecimal(goodsPrice));
				BigDecimal gPrice = subTotalPrice.divide(totalPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(tax));
				priceMap.put(goodsNo+"_"+index, gPrice.doubleValue());
				deductPrice = gPrice.add(deductPrice);
			}
		}
		return priceMap;
	}

	/**
	 * 获取天猫平台商品表体
	 */
	public void getTmallOrderItem(JSONArray goodsArray, JSONArray newGoodJsonArray, Double tax,
								  MerchantInfoMongo merchantInfoMongo,MerchantShopRelMongo mongo,
								  boolean isSplitByTax) throws SQLException {
		List<Map<String, Object>> goodsMapList = new ArrayList<>();
		for (Object goodsObject : goodsArray) {
			JSONObject goodsJSONObject = (JSONObject) goodsObject;
			String goodsNo = null;
			if (goodsJSONObject.get("skuId") == null || StringUtils.isBlank(goodsJSONObject.getString("skuId"))) {
				if (goodsJSONObject.get("numIid") == null || StringUtils.isBlank(goodsJSONObject.getString("numIid"))) {
					throw new RuntimeException("天猫平台 skuId 和 numIid 不能同时为空, 店铺编码:" + mongo.getShopCode());
				} else {
					goodsNo = goodsJSONObject.getString("numIid");
				}
			} else {
				goodsNo = goodsJSONObject.getString("skuId");
			}
			//商品税费
			if (goodsJSONObject.get("subOrderTaxFee") == null || StringUtils.isBlank(goodsJSONObject.getString("subOrderTaxFee"))) {
				throw new RuntimeException("subOrderTaxFee is null");
			}

			//分摊之后的实付金额
			if (goodsJSONObject.get("divideOrderFee") == null || StringUtils.isBlank(goodsJSONObject.getString("divideOrderFee"))) {
				throw new RuntimeException("divideOrderFee is null");
			}
			// 商品数量
			if (goodsJSONObject.get("goodsNum") == null || StringUtils.isBlank(goodsJSONObject.getString("goodsNum"))) {
				throw new RuntimeException("goodsNum is null");
			}

			// 商品售价
			if (goodsJSONObject.get("goodsPrice") == null || StringUtils.isBlank(goodsJSONObject.getString("goodsPrice"))) {
				throw new RuntimeException("goodsPrice is null");
			}

			if (isSplitByTax) {
				Map<String, Object> goodsMap = new HashMap<>();
				goodsMap.put("goodsNo", goodsNo);
				goodsMap.put("index", goodsJSONObject.get("index"));
				goodsMap.put("goodsPrice", goodsJSONObject.get("goodsPrice"));
				goodsMap.put("goodsNum", goodsJSONObject.get("goodsNum"));
				goodsMapList.add(goodsMap);
			}
		}
		//当表体的商品税金都为0时，摊分表头税金到商品
		Map<String, Double> taxMap = new HashMap<>();
		if (isSplitByTax) {
			taxMap = getSplitGoodsTax(goodsMapList, tax);
		}

		for (Object goodsObject : goodsArray) {
			JSONObject goodsJSONObject=(JSONObject)goodsObject;
			String index = (String) goodsJSONObject.get("index");
			String skuId=null;
			//根据店铺编号+SKU_ID查找对照表中维护的组合品SKU_ID单品信息做电商订单拆解
			GroupMerchandiseContrastModel groupMerchandiseContrastModel = null;
			if (goodsJSONObject.get("skuId")!=null&&StringUtils.isNotBlank(goodsJSONObject.getString("skuId"))) {
				groupMerchandiseContrastModel = new GroupMerchandiseContrastModel();
				groupMerchandiseContrastModel.setSkuId(goodsJSONObject.getString("skuId"));
				groupMerchandiseContrastModel.setShopCode(mongo.getShopCode());
				groupMerchandiseContrastModel.setStatus(DERP_ORDER.GROUPMERCHANDISECONTRAST_STATUS_1);
				groupMerchandiseContrastModel = groupMerchandiseContrastDao.getDetails(groupMerchandiseContrastModel);
				if (groupMerchandiseContrastModel==null) {
					throw new RuntimeException("SKU_ID不为空,根据店铺编号:" + mongo.getShopCode() + "和SKU_ID:"+goodsJSONObject.getString("skuId")+"没有查找到对照表中维护的组合品");
				}
				skuId=goodsJSONObject.getString("skuId");
			}
			//根据店铺编号+numIid查找对照表中维护的组合品numIid单品信息做电商订单拆解
			if (groupMerchandiseContrastModel == null&&goodsJSONObject.get("numIid")!=null&&StringUtils.isNotBlank(goodsJSONObject.getString("numIid"))) {
				groupMerchandiseContrastModel = new GroupMerchandiseContrastModel();
				groupMerchandiseContrastModel.setSkuId(goodsJSONObject.getString("numIid"));
				groupMerchandiseContrastModel.setShopCode(mongo.getShopCode());
				groupMerchandiseContrastModel.setStatus(DERP_ORDER.GROUPMERCHANDISECONTRAST_STATUS_1);
				groupMerchandiseContrastModel = groupMerchandiseContrastDao.getDetails(groupMerchandiseContrastModel);
				if (groupMerchandiseContrastModel == null) {
					throw new RuntimeException("根据店铺编号:" + mongo.getShopCode() +  "和SKU_ID:"+goodsJSONObject.getString("skuId")+"和numIid:" +goodsJSONObject.getString("numIid")+ "都没有查找到对照表中维护的组合品");
				}
				skuId=goodsJSONObject.getString("numIid");
			}

			List<GroupMerchandiseContrastDetailModel> groupMerchandiseContrastDetailModels = groupMerchandiseContrastModel.getDetailList();

			if (groupMerchandiseContrastDetailModels.size()>1) {
				Double divideOrderFee = goodsJSONObject.getDouble("divideOrderFee"); //	分摊之后的实付金额58.25
				Double subOrderTaxFee = goodsJSONObject.getDouble("subOrderTaxFee"); //	商品税费 6.54
				Integer originalGoodsNum = goodsJSONObject.getInt("goodsNum"); //商品数量1
				Double goodsPrice = goodsJSONObject.getDouble("goodsPrice"); //实际售价88
				Double totalPriceDouble= divideOrderFee-subOrderTaxFee;//结算总金额
				Double originalPriceDouble = goodsPrice * originalGoodsNum;//销售总金额
				//结算总金额拆分
				Map<String, Double> totalPriceMap = getSplitPrice(groupMerchandiseContrastDetailModels, totalPriceDouble, 2);
				//销售总金额拆分
				Map<String, Double> originalPriceMap = getSplitPrice(groupMerchandiseContrastDetailModels, originalPriceDouble, 2);
				//优惠金额拆分
				Map<String, Double> discountMap = new HashMap<>();
				if (goodsJSONObject.containsKey("discountFee") && !(goodsJSONObject.get("discountFee") == null || StringUtils.isBlank(goodsJSONObject.getString("discountFee")))) {
					discountMap = getSplitPrice(groupMerchandiseContrastDetailModels, goodsJSONObject.getDouble("discountFee"), 2);
				}
				//商品税金拆分
				Map<String, Double> goodsTaxMap = new HashMap<>();
				if (isSplitByTax) {
					goodsTaxMap = getSplitPrice(groupMerchandiseContrastDetailModels, taxMap.get(groupMerchandiseContrastModel.getSkuId() + "_" + index), 2);
				} else  {
					goodsTaxMap = getSplitPrice(groupMerchandiseContrastDetailModels, subOrderTaxFee, 2);
				}

				for (int i = 0; i < groupMerchandiseContrastDetailModels.size(); i++) {
					String goodsNo = groupMerchandiseContrastDetailModels.get(i).getGoodsNo();
					// 根据商家id和商品货号查询商品;
					Map<String, Object> merchandiseInfoMap = new HashMap<>();
					merchandiseInfoMap.put("merchantId", merchantInfoMongo.getMerchantId());//商品id
					merchandiseInfoMap.put("goodsNo", goodsNo);//商品货号
					MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 查询商品信息
					if (merchandiseInfoMogo==null) {
						throw new RuntimeException("根据商家和货号没有查询到商品信息:货号"+goodsNo+",商家id:"+merchantInfoMongo.getMerchantId()+",店铺编码:"+mongo.getShopCode());
					}

					//拆解后商品对应的正确数量
					Integer goodsNum = groupMerchandiseContrastDetailModels.get(i).getNum() * goodsJSONObject.getInt("goodsNum");
					//Double price = priceMap.get(goodsNo) / groupMerchandiseContrastDetailModels.get(i).getNum();

					JSONObject newGoodJSONObject =new JSONObject();

					newGoodJSONObject.put("skuId", skuId);
					newGoodJSONObject.put("goodsNo", merchandiseInfoMogo.getGoodsNo());// 商品货号
					newGoodJSONObject.put("barcode", merchandiseInfoMogo.getBarcode());// 商品条形码
					newGoodJSONObject.put("goodsCode", merchandiseInfoMogo.getGoodsCode());// 商品编码
					newGoodJSONObject.put("goodsId", merchandiseInfoMogo.getMerchandiseId());// 商品id
					newGoodJSONObject.put("goodsName", merchandiseInfoMogo.getName());	// 商品名称
					newGoodJSONObject.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
					newGoodJSONObject.put("num", goodsNum);// 商品数量
					newGoodJSONObject.put("goodsTax", goodsTaxMap.get(goodsNo)); //商品税费

					// 商品总价 = 商品单价 * 商品数量
					Double priceTotal = totalPriceMap.get(goodsNo);//结算总金额
					Double price=priceTotal/goodsNum; //结算单价
					Double originalPriceTotal = originalPriceMap.get(goodsNo); //销售总金额
					Double originalPrice = originalPriceTotal/goodsNum;//销售单价

					BigDecimal totalPrice=new BigDecimal(String.valueOf(priceTotal));
					//BigDecimal totalPrice = BigDecimal.valueOf(goodsNum * priceMap.get(goodsNo));
					newGoodJSONObject.put("price", price); //结算价格
					newGoodJSONObject.put("totalPrice", totalPrice); //结算总金额
					newGoodJSONObject.put("originalPriceTotal", originalPriceTotal); //销售总金额
					newGoodJSONObject.put("originalPrice", originalPrice); //销售单价
					if (discountMap.size() > 0) {
						newGoodJSONObject.put("goodsDiscount", discountMap.get(goodsNo));// 优惠减免金额
					}
					newGoodJSONObject.put("invoiceNo", goodsJSONObject.get("invoiceNo"));
					newGoodJSONObject.put("logisticsCompany", goodsJSONObject.get("logisticsCompany"));
					newGoodJsonArray.add(newGoodJSONObject);
//					num= num+1;
//					orderPayment=orderPayment.add(totalPrice);
				}
			}else {
				String goodsNo = groupMerchandiseContrastDetailModels.get(0).getGoodsNo();
				// 根据商家id和商品货号查询商品;
				Map<String, Object> merchandiseInfoMap = new HashMap<>();
				merchandiseInfoMap.put("merchantId", merchantInfoMongo.getMerchantId());//商品id
				merchandiseInfoMap.put("goodsNo", goodsNo);//商品货号
				MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 查询商品信息
				if (merchandiseInfoMogo==null) {
					throw new RuntimeException("根据商家和货号没有查询到商品信息:货号"+goodsNo+",商家id:"+merchantInfoMongo.getMerchantId() + ",店铺编码:"+mongo.getShopCode());
				}
				Double divideOrderFee = goodsJSONObject.getDouble("divideOrderFee");
				Double subOrderTaxFee = goodsJSONObject.getDouble("subOrderTaxFee");
				Double totalPriceDouble= divideOrderFee-subOrderTaxFee; //结算总金额
				Integer originalGoodsNum = goodsJSONObject.getInt("goodsNum"); //商品数量
				Double goodsPrice = goodsJSONObject.getDouble("goodsPrice"); //实际售价
				Double originalPriceDouble = goodsPrice * originalGoodsNum;//销售总金额

				Integer goodsNum = groupMerchandiseContrastDetailModels.get(0).getNum() * goodsJSONObject.getInt("goodsNum");
				Double price=totalPriceDouble/goodsNum; //结算单价
				Double originalPrice = originalPriceDouble/goodsNum;//销售单价
				JSONObject newGoodJSONObject =new JSONObject();
				newGoodJSONObject.put("skuId", skuId);
				newGoodJSONObject.put("goodsNo", merchandiseInfoMogo.getGoodsNo());// 商品货号
				newGoodJSONObject.put("barcode", merchandiseInfoMogo.getBarcode());// 商品条形码
				newGoodJSONObject.put("goodsCode", merchandiseInfoMogo.getGoodsCode());// 商品编码
				newGoodJSONObject.put("goodsId", merchandiseInfoMogo.getMerchandiseId());// 商品id
				newGoodJSONObject.put("goodsName", merchandiseInfoMogo.getName());	// 商品名称
				newGoodJSONObject.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
				newGoodJSONObject.put("num", goodsNum);// 商品数量
				if (isSplitByTax) {
					newGoodJSONObject.put("goodsTax", taxMap.get(groupMerchandiseContrastModel.getSkuId() + "_" + index)); //商品税费
				} else  {
					newGoodJSONObject.put("goodsTax", subOrderTaxFee); //商品税费
				}
				BigDecimal totalPrice=new BigDecimal(String.valueOf(totalPriceDouble));
				//BigDecimal totalPrice = BigDecimal.valueOf(goodsNum * priceMap.get(goodsNo));
				newGoodJSONObject.put("price", price); //结算单价
				newGoodJSONObject.put("totalPrice", totalPrice); //结算总金额
				String discountFee = goodsJSONObject.getString("discountFee");
				newGoodJSONObject.put("goodsDiscount", Double.valueOf(discountFee));// 优惠减免金额
				newGoodJSONObject.put("originalPriceTotal", originalPriceDouble); //销售总金额
				newGoodJSONObject.put("originalPrice", originalPrice); //销售单价
				newGoodJSONObject.put("invoiceNo", goodsJSONObject.get("invoiceNo"));
				newGoodJSONObject.put("logisticsCompany", goodsJSONObject.get("logisticsCompany"));
				newGoodJsonArray.add(newGoodJSONObject);
//				num= num+1;
//				orderPayment=orderPayment.add(totalPrice);
			}
//			count += groupMerchandiseContrastDetailModels.size();
		}
	}

	/**
	 * 获取小红书、考拉平台商品表体
	 */
	public void getKaoLaOrXHSOrderItem(JSONArray goodsArray, JSONArray newGoodJsonArray, Double tax,
								  MerchantInfoMongo merchantInfoMongo,MerchantShopRelMongo mongo,
								  boolean isSplitByTax, String orderDiscount) throws SQLException {
		List<Map<String, Object>> goodsMapList = new ArrayList<>();
		//校验必填项
		for (Object goodsObject : goodsArray) {
			JSONObject goodsJSONObject=(JSONObject)goodsObject;
			// 商品货号
			if (goodsJSONObject.get("goodsNo")==null||StringUtils.isBlank(goodsJSONObject.getString("goodsNo"))) {
				throw new RuntimeException("goodsNo is null");
			}

			// 商品价格
			if (goodsJSONObject.get("goodsPrice")==null||StringUtils.isBlank(goodsJSONObject.getString("goodsPrice"))) {
				throw new RuntimeException("goodsPrice is null");
			}
			// 商品数量
			if (goodsJSONObject.get("goodsNum")==null||StringUtils.isBlank(goodsJSONObject.getString("goodsNum"))) {
				throw new RuntimeException("goodsNum is null");
			}

			//优惠金额
			if (goodsJSONObject.get("discountFee")==null||StringUtils.isBlank(goodsJSONObject.getString("discountFee"))) {
				throw new RuntimeException("discountFee is null");
			}

			if (isSplitByTax) {
				Map<String, Object> goodsMap = new HashMap<>();
				goodsMap.put("index", goodsJSONObject.getString("index"));
				goodsMap.put("goodsNo", goodsJSONObject.getString("goodsNo"));
				goodsMap.put("goodsPrice", goodsJSONObject.get("goodsPrice"));
				goodsMap.put("goodsNum", goodsJSONObject.get("goodsNum"));
				goodsMapList.add(goodsMap);
			}
		}

		//当表体的商品税金都为0时，摊分表头税金到商品
		Map<String, Double> taxMap = new HashMap<>();
		if (isSplitByTax) {
			taxMap = getSplitGoodsTax(goodsMapList, tax);
		}

		Map<String, Double> orderDiscountMap = new HashMap<>();
		if (StringUtils.isNotBlank(orderDiscount) && new BigDecimal(orderDiscount).compareTo(BigDecimal.ZERO) != 0) {
			orderDiscountMap = getSplitGoodsPrice(goodsArray, new BigDecimal(orderDiscount).doubleValue());
		}
		for (Object goodsObject : goodsArray) {
			JSONObject goodsJSONObject = (JSONObject) goodsObject;
			String index = goodsJSONObject.getString("index"); //序号
			String jsonGoodsNo = goodsJSONObject.getString("goodsNo"); //商品货号
			Double goodsPrice = goodsJSONObject.getDouble("goodsPrice");// 商品单价
			Double goodsDiscountFee = goodsJSONObject.getDouble("discountFee");//商品优惠金额
			Integer originalGoodsNum = goodsJSONObject.getInt("goodsNum");//商品数量
			Double originalGoodsPriceTotal = goodsPrice * originalGoodsNum; //销售总金额
			Double orderGoodsDiscount = 0.0;
			if (orderDiscountMap.size() > 0) {
				orderGoodsDiscount = orderDiscountMap.get(jsonGoodsNo+"_"+index);
			}
			Double goodsPriceTotal = originalGoodsPriceTotal - goodsDiscountFee - orderGoodsDiscount; //结算总金额
			GroupMerchandiseContrastModel groupMerchandiseContrastModel = new GroupMerchandiseContrastModel();
			groupMerchandiseContrastModel.setSkuId(jsonGoodsNo);
			groupMerchandiseContrastModel.setShopCode(mongo.getShopCode());
			groupMerchandiseContrastModel.setStatus(DERP_ORDER.GROUPMERCHANDISECONTRAST_STATUS_1);
			groupMerchandiseContrastModel = groupMerchandiseContrastDao.getDetails(groupMerchandiseContrastModel);
			if (groupMerchandiseContrastModel==null) {
				throw new RuntimeException("goodsNo不为空,根据店铺编码:" + mongo.getShopCode() + "和goodsNo:"+jsonGoodsNo+"没有查找到对照表中维护的组合品");
			}

			List<GroupMerchandiseContrastDetailModel> groupMerchandiseContrastDetailModels = groupMerchandiseContrastModel.getDetailList();
			//结算总金额拆分
			Map<String, Double> totalPriceMap = getSplitPrice(groupMerchandiseContrastDetailModels, goodsPriceTotal, 2);
			//优惠金额拆分
			Map<String, Double> discountMap = getSplitPrice(groupMerchandiseContrastDetailModels, goodsDiscountFee, 2);
			//销售总金额拆分
			Map<String, Double> originalTotalPriceMap = getSplitPrice(groupMerchandiseContrastDetailModels, originalGoodsPriceTotal, 2);
			//商品税金拆分
			Map<String, Double> goodsTaxMap = new HashMap<>();
			if (isSplitByTax) {
				goodsTaxMap = getSplitPrice(groupMerchandiseContrastDetailModels, taxMap.get(groupMerchandiseContrastModel.getSkuId() + "_" + index), 2);
			} else if (goodsJSONObject.containsKey("subOrderTaxFee") && StringUtils.isNotBlank(goodsJSONObject.getString("subOrderTaxFee"))) {
				goodsTaxMap = getSplitPrice(groupMerchandiseContrastDetailModels, goodsJSONObject.getDouble("subOrderTaxFee"), 2);
			}

			for (int i = 0; i < groupMerchandiseContrastDetailModels.size(); i++) {
				String goodsNo = groupMerchandiseContrastDetailModels.get(i).getGoodsNo();
				// 根据商家id和商品货号查询商品;
				Map<String, Object> merchandiseInfoMap = new HashMap<>();
				merchandiseInfoMap.put("merchantId", merchantInfoMongo.getMerchantId());//商品id
				merchandiseInfoMap.put("goodsNo", goodsNo);//商品货号
				MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 查询商品信息
				if (merchandiseInfoMogo==null) {
					throw new RuntimeException("根据商家和货号没有查询到商品信息:货号"+goodsNo+",商家id:"+merchantInfoMongo.getMerchantId() + ",店铺编码:" + mongo.getShopCode());
				}

				//拆解后商品对应的正确数量
				Integer goodsNum = groupMerchandiseContrastDetailModels.get(i).getNum() * goodsJSONObject.getInt("goodsNum");
				//Double price = priceMap.get(goodsNo) / groupMerchandiseContrastDetailModels.get(i).getNum();

				JSONObject newGoodJSONObject =new JSONObject();
				newGoodJSONObject.put("skuId", jsonGoodsNo);
				newGoodJSONObject.put("goodsNo", merchandiseInfoMogo.getGoodsNo());// 商品货号
				newGoodJSONObject.put("barcode", merchandiseInfoMogo.getBarcode());// 商品条形码
				newGoodJSONObject.put("goodsCode", merchandiseInfoMogo.getGoodsCode());// 商品编码
				newGoodJSONObject.put("goodsId", merchandiseInfoMogo.getMerchandiseId());// 商品id
				newGoodJSONObject.put("goodsName", merchandiseInfoMogo.getName());	// 商品名称
				newGoodJSONObject.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
				newGoodJSONObject.put("num", goodsNum);// 商品数量
				newGoodJSONObject.put("goodsTax", goodsTaxMap.get(goodsNo)); //商品税费

				// 商品总价 = 商品单价 * 商品数量
				Double priceTotal = totalPriceMap.get(goodsNo); //结算总价
				Double price=priceTotal/goodsNum; //结算单价
				Double originalPriceTotal = originalTotalPriceMap.get(goodsNo); //销售总价
				Double originalPrice = originalPriceTotal/goodsNum; //销售单价

				BigDecimal totalPrice=new BigDecimal(String.valueOf(priceTotal));
				newGoodJSONObject.put("price", price);
				newGoodJSONObject.put("totalPrice", totalPrice);
				newGoodJSONObject.put("goodsDiscount", discountMap.get(goodsNo));// 优惠减免金额
				newGoodJSONObject.put("originalPriceTotal", originalPriceTotal); //销售总金额
				newGoodJSONObject.put("originalPrice", originalPrice); //销售单价
				newGoodJSONObject.put("invoiceNo", goodsJSONObject.get("invoiceNo"));
				newGoodJSONObject.put("logisticsCompany", goodsJSONObject.get("logisticsCompany"));
				newGoodJsonArray.add(newGoodJSONObject);
//				num= num+1;
//				count++;
			}
		}
	}

	/**
	 * 获取京东平台商品表体
	 */
	public void getJDOrderItem(JSONArray jsonGoodArray, JSONArray newGoodJsonArray,
			BigDecimal orderDecTotal,
			Double tax,
			MerchantInfoMongo merchantInfoMongo,MerchantShopRelMongo mongo,
			boolean isSplitByTax, String orderDiscount) throws SQLException {

		BigDecimal itemsTotalPrice=new BigDecimal(0);
		List<Map<String, Object>> goodsMapList = new ArrayList<>();
		// 获取订单的总金额
		for (Object goodsObject : jsonGoodArray) {
			JSONObject goodsJSONObject=(JSONObject)goodsObject;
			// 京东平台查商品配置
			if (goodsJSONObject.get("skuId")==null||StringUtils.isBlank(goodsJSONObject.getString("skuId"))) {
				throw new RuntimeException("goodsNo为 空 并且 skuId 为空");
			}

			// 商品价格
			if (goodsJSONObject.get("goodsPrice")==null||StringUtils.isBlank(goodsJSONObject.getString("goodsPrice"))) {
				throw new RuntimeException("goodsPrice is null");
			}
			// 商品数量
			if (goodsJSONObject.get("goodsNum")==null||StringUtils.isBlank(goodsJSONObject.getString("goodsNum"))) {
				throw new RuntimeException("goodsNum is null");
			}

			//优惠金额
			if (goodsJSONObject.get("discountFee")==null||StringUtils.isBlank(goodsJSONObject.getString("discountFee"))) {
				throw new RuntimeException("discountFee is null");
			}
			BigDecimal totalPrice = new BigDecimal(goodsJSONObject.getString("goodsNum")).multiply(new BigDecimal(goodsJSONObject.getString("goodsPrice")));
			itemsTotalPrice=itemsTotalPrice.add(totalPrice);

			if (isSplitByTax) {
				Map<String, Object> goodsMap = new HashMap<>();
				goodsMap.put("index", goodsJSONObject.getString("index"));
				goodsMap.put("goodsNo", goodsJSONObject.getString("skuId"));
				goodsMap.put("goodsPrice", goodsJSONObject.get("goodsPrice"));
				goodsMap.put("goodsNum", goodsJSONObject.get("goodsNum"));
				goodsMapList.add(goodsMap);
			}

		}

		if (BigDecimal.ZERO.compareTo(itemsTotalPrice)==0) {
			throw new RuntimeException("表体商品数据goodsPrice*goodsNum的和为0");
		}

		//当表体的商品税金都为0时，摊分表头税金到商品
		Map<String, Double> taxMap = new HashMap<>();
		if (isSplitByTax) {
			taxMap = getSplitGoodsTax(goodsMapList, tax);
		}
		BigDecimal lastOrderDecTotal=new BigDecimal(0);
		for (int i = 0; i < jsonGoodArray.size(); i++) {
			Object goodsObject = jsonGoodArray.get(i);
			JSONObject goodsJSONObject = (JSONObject) goodsObject;
			//根据店铺编号+SKU_ID查找对照表中维护的组合品SKU_ID单品信息做电商订单拆解
			GroupMerchandiseContrastModel groupMerchandiseContrastModel = new GroupMerchandiseContrastModel();
			groupMerchandiseContrastModel.setSkuId(goodsJSONObject.getString("skuId"));
			groupMerchandiseContrastModel.setShopCode(mongo.getShopCode());
			groupMerchandiseContrastModel.setStatus(DERP_ORDER.GROUPMERCHANDISECONTRAST_STATUS_1);
			groupMerchandiseContrastModel = groupMerchandiseContrastDao.getDetails(groupMerchandiseContrastModel);
			if (groupMerchandiseContrastModel == null) {
				throw new RuntimeException("SKU_ID不为空,根据店铺编号:" + mongo.getShopCode() + "和SKU_ID:" + goodsJSONObject.getString("skuId") + "没有查找到对照表中维护的组合品");
			}
			List<GroupMerchandiseContrastDetailModel> groupMerchandiseContrastDetailModels = groupMerchandiseContrastModel.getDetailList();

			if (groupMerchandiseContrastDetailModels.size() == 0) {
				throw new RuntimeException("SKU_ID不为空,根据店铺编号:" + mongo.getShopCode() + "和SKU_ID:" + goodsJSONObject.getString("skuId") + "没有查找到对照表详");
			}

			String index = goodsJSONObject.getString("index");// 序号
			Integer goodsNum = goodsJSONObject.getInt("goodsNum");// 商品数量
			Double goodsPrice = goodsJSONObject.getDouble("goodsPrice");// 商品单价
			Double discountFee = goodsJSONObject.getDouble("discountFee");// 商品优惠金额

			BigDecimal goodsTotalPrice=new BigDecimal(0);
			if (i == jsonGoodArray.size() - 1) {
				goodsTotalPrice = orderDecTotal.subtract(lastOrderDecTotal);
			} else {
				goodsTotalPrice=orderDecTotal.multiply(new BigDecimal(goodsNum)).multiply(new BigDecimal(goodsPrice.toString())).divide(itemsTotalPrice,2, BigDecimal.ROUND_HALF_UP);
				lastOrderDecTotal = lastOrderDecTotal.add(goodsTotalPrice);//除最后一次均摊前的总价
			}





			//商品单价拆分
			Map<String, Double> originalPriceMap = getSplitPrice(groupMerchandiseContrastDetailModels, goodsPrice, 2);
			//优惠金额拆分
			Map<String, Double> discountMap = getSplitPrice(groupMerchandiseContrastDetailModels, discountFee, 2);


			//组合商品总金额
			//BigDecimal groupTotalPrice = new BigDecimal("0");

			//如果 订单优惠金额不为空 并且大于0
			/*if (StringUtils.isNotBlank(orderDiscount) && new BigDecimal(orderDiscount).compareTo(BigDecimal.ZERO) == 1) {
				// 商品减去优惠金额后的总金额
				BigDecimal subTotalPrice = new BigDecimal(goodsNum).multiply(new BigDecimal(goodsPrice.toString())).subtract(new BigDecimal(discountFee.toString()));
				//订单优惠金额占比
				BigDecimal subOrderDiscount = subTotalPrice.divide(itemsTotalPrice, 2, BigDecimal.ROUND_HALF_UP)
						.multiply(new BigDecimal(orderDiscount)).setScale(2, BigDecimal.ROUND_HALF_UP);
				if (i == jsonGoodArray.size() - 1) {
					groupTotalPrice = subTotalPrice.subtract(new BigDecimal(orderDiscount).subtract(lastOrderDiscount));
				} else {
					groupTotalPrice = subTotalPrice.subtract(subOrderDiscount);
					lastOrderDiscount = lastOrderDiscount.add(subOrderDiscount);//除最后一次均摊前的总价
				}
//				orderPayment=orderPayment.add(groupTotalPrice);
			} else {
				groupTotalPrice = new BigDecimal(goodsNum).multiply(new BigDecimal(goodsPrice.toString())).subtract(new BigDecimal(discountFee.toString()));
//				orderPayment=orderPayment.add(groupTotalPrice);
			}*/
			//商品结算单价拆分
			Map<String, Double> priceMap = getSplitPrice(groupMerchandiseContrastDetailModels, goodsTotalPrice.doubleValue(), 2);
			//商品税金拆分
			Map<String, Double> goodsTaxMap = new HashMap<>();
			if (isSplitByTax) {
				goodsTaxMap = getSplitPrice(groupMerchandiseContrastDetailModels, taxMap.get(groupMerchandiseContrastModel.getSkuId() + "_" + index), 2);
			} else if (goodsJSONObject.containsKey("subOrderTaxFee") && StringUtils.isNotBlank(goodsJSONObject.getString("subOrderTaxFee"))) {
				goodsTaxMap = getSplitPrice(groupMerchandiseContrastDetailModels, goodsJSONObject.getDouble("subOrderTaxFee"), 2);
			}
			for (GroupMerchandiseContrastDetailModel detailModel : groupMerchandiseContrastDetailModels) {
				String goodsNo = detailModel.getGoodsNo();
				// 根据商家id和商品货号查询商品;
				Map<String, Object> merchandiseInfoMap = new HashMap<>();
				merchandiseInfoMap.put("merchantId", merchantInfoMongo.getMerchantId());//商品id
				merchandiseInfoMap.put("goodsNo", goodsNo);//商品货号
				MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 查询商品信息
				if (merchandiseInfoMogo == null) {
					throw new RuntimeException("根据商家和货号没有查询到商品信息:货号" + goodsNo + ",商家id:" + merchantInfoMongo.getMerchantId() + ",店铺编码:" + mongo.getShopCode());
				}
				//拆分后的商品销售总金额
				BigDecimal originalPriceTotal = new BigDecimal(originalPriceMap.get(goodsNo).toString()).multiply(new BigDecimal(goodsNum));
				//拆分后的商品优惠金额
				BigDecimal goodsDiscountFeeBig = new BigDecimal(discountMap.get(goodsNo).toString());
				//商品数量
				Integer splitGoodsNum = goodsNum * detailModel.getNum();
				// 拆分后的商品销售单价
				BigDecimal goodsPriceBig = originalPriceTotal.divide(new BigDecimal(splitGoodsNum), 2, BigDecimal.ROUND_HALF_UP);
				//结算总金额
				BigDecimal totalPrice = new BigDecimal(priceMap.get(goodsNo).toString());
				// 结算单价
				BigDecimal price = totalPrice.divide(new BigDecimal(splitGoodsNum), 2, BigDecimal.ROUND_HALF_UP);

				JSONObject newGoodJSONObject = new JSONObject();
				newGoodJSONObject.put("skuId", goodsJSONObject.getString("skuId"));
				newGoodJSONObject.put("goodsNo", merchandiseInfoMogo.getGoodsNo());// 商品货号
				newGoodJSONObject.put("barcode", merchandiseInfoMogo.getBarcode());// 商品条形码
				newGoodJSONObject.put("goodsCode", merchandiseInfoMogo.getGoodsCode());// 商品编码
				newGoodJSONObject.put("goodsId", merchandiseInfoMogo.getMerchandiseId());// 商品id
				newGoodJSONObject.put("goodsName", merchandiseInfoMogo.getName());    // 商品名称
				newGoodJSONObject.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
				newGoodJSONObject.put("num", splitGoodsNum);// 商品数量
				newGoodJSONObject.put("goodsTax", goodsTaxMap.get(goodsNo)); //商品税费
				newGoodJSONObject.put("price", price);// 商品单价
				newGoodJSONObject.put("totalPrice", totalPrice);
				newGoodJSONObject.put("goodsDiscount", goodsDiscountFeeBig.doubleValue());// 优惠减免金额
				newGoodJSONObject.put("originalPriceTotal", originalPriceTotal); //销售总金额
				newGoodJSONObject.put("originalPrice", goodsPriceBig.doubleValue()); //销售单价
				newGoodJsonArray.add(newGoodJSONObject);
//					num= num+1;
//					count +=1;
			}
		}
	}


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
		buMoveInventoryModel.setStatus(DERP_ORDER.BUMOVEINVENTORY_STATUS_027);//移库状态 027-移库中
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

		List<InventoryGoodsDetailJson> inInventoryGoodsDetailList = new ArrayList<>();// 移入商品集合
		// 移入商品实体
		InventoryGoodsDetailJson inInventoryGoodsDetailJson = new InventoryGoodsDetailJson();

		inInventoryGoodsDetailJson.setGoodsId(String.valueOf(wayBillItemModel.getGoodsId()));
		inInventoryGoodsDetailJson.setGoodsName(wayBillItemModel.getGoodsName());
		inInventoryGoodsDetailJson.setGoodsNo(wayBillItemModel.getGoodsNo());
		inInventoryGoodsDetailJson.setCommbarcode(orderItemModel.getCommbarcode());// 标准条码
		inInventoryGoodsDetailJson.setBarcode(wayBillItemModel.getBarcode());

		inInventoryGoodsDetailJson.setBatchNo(wayBillItemModel.getBatchNo());//批次号
		inInventoryGoodsDetailJson.setGoodsId(String.valueOf(wayBillItemModel.getGoodsId()));
		inInventoryGoodsDetailJson.setGoodsName(wayBillItemModel.getGoodsName());
		inInventoryGoodsDetailJson.setGoodsNo(wayBillItemModel.getGoodsNo());
		inInventoryGoodsDetailJson.setBatchNo(wayBillItemModel.getBatchNo());//批次号
		inInventoryGoodsDetailJson.setCommbarcode(orderItemModel.getCommbarcode());// 标准条码
		inInventoryGoodsDetailJson.setBarcode(wayBillItemModel.getBarcode());
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

		// 商品实体
		InventoryGoodsDetailJson outInventoryGoodsDetailJson = new InventoryGoodsDetailJson();

		outInventoryGoodsDetailJson.setGoodsId(String.valueOf(wayBillItemModel.getGoodsId()));
		outInventoryGoodsDetailJson.setGoodsName(wayBillItemModel.getGoodsName());
		outInventoryGoodsDetailJson.setGoodsNo(wayBillItemModel.getGoodsNo());
		outInventoryGoodsDetailJson.setCommbarcode(orderItemModel.getCommbarcode());// 标准条码
		outInventoryGoodsDetailJson.setBarcode(wayBillItemModel.getBarcode());



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
		outInventoryGoodsDetailJson.setBuId(String.valueOf(shopShipperBuId));
		outInventoryGoodsDetailJson.setBuName(shopShipperBuName);
		outInventoryGoodsDetailJson.setStockLocationTypeId(shopShipperStockLocationTypeId == null ? "" : shopShipperStockLocationTypeId.toString());
		outInventoryGoodsDetailJson.setStockLocationTypeName(stockLocationMap.get(shopShipperStockLocationTypeId));
		inInventoryGoodsDetailList.add(outInventoryGoodsDetailJson);

		// 移入实体
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

		inInventoryDetailJson.setGoodsList(inInventoryGoodsDetailList);
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
