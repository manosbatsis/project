package com.topideal.service.api.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.api.v3_3.ESaleLoadingDeliveriesMQGoodsListJson;
import com.topideal.json.api.v3_3.ESaleLoadingDeliveriesMQRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.json.inventory.j06.InventoryDetailJson;
import com.topideal.json.inventory.j06.InventoryGoodsDetailJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.service.api.sale.DSRookieLoadingDeliveriesService;
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
 * 电商订单菜鸟-装载交运回推接口实现类
 */
@Service
public class DSRookieLoadingDeliveriesServiceImpl implements DSRookieLoadingDeliveriesService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DSRookieLoadingDeliveriesServiceImpl.class);
	// 电商订单
	@Autowired
	private OrderDao orderDao;
	// 电商订单商品
	@Autowired
	private OrderItemDao orderItemDao;
	// 运单表
	@Autowired
	private WayBillDao wayBillDao;
	// 运单表体
	@Autowired
	private WayBillItemDao wayBillItemDao;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	// 事业部移库单表头
	@Autowired
	private BuMoveInventoryDao buMoveInventoryDao;
	// 事业部移库单表体
	@Autowired
	private BuMoveInventoryItemDao buMoveInventoryItemDao;
	@Autowired
	private BuInventoryMongoDao buInventoryMongoDao;
	// 商家店铺Mongo
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao ;
	@Autowired
	private MerchantShopShipperMongoDao merchantShopShipperMongoDao;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;// 商家信息
	// 汇率信息
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao;
	// 协议单价配置
	@Autowired
	private AgreementCurrencyConfigDao agreementCurrencyConfigDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;
	/**
	 * 保存装载交运信息 说明: 装载交运信息推进来(电商订单变成已发货并且生成交运单和交运单商品 和减库存)
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203120500, model = DERP_LOG_POINT.POINT_12203120500_Label,keyword="orderCode")
	public boolean saveRookieLoadingDeliveriesInfo(String json,String keys,String topics,String tags) throws Exception {
		/**
		 * 说明:电商订单只接受 服务类型为20 的订单
		 */
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",ESaleLoadingDeliveriesMQGoodsListJson.class);
		// JSON对象转实体
		ESaleLoadingDeliveriesMQRootJson rootJson = (ESaleLoadingDeliveriesMQRootJson) JSONObject.toBean(jsonData, ESaleLoadingDeliveriesMQRootJson.class, classMap);
		// 商品信息
		List<ESaleLoadingDeliveriesMQGoodsListJson> orderGoodsList = rootJson.getGoodsList();
		String orderCode = rootJson.getOrderCode();// 订单号
		String wayBillNo = rootJson.getWayBillNo();// 运单号(电商订单必传)
		//电商订单(电商订单订单号来着其他系统,具有不确定性 要从数据库中查下)
		OrderModel orderModel = new OrderModel();
		orderModel.setMerchantId(rootJson.getMerchantId());
		orderModel.setExternalCode(orderCode);// 外部单号
		orderModel = orderDao.searchByModel(orderModel);
		if(orderModel == null){
			LOGGER.error(DERP.MQ_FAILTYPE_01 + "电商订单不存在,订单编号" + orderCode);
			throw new RuntimeException(DERP.MQ_FAILTYPE_01 + "电商订单不存在,订单编号" + orderCode);
		}
		if ("4".equals(orderModel.getStatus())) {
			LOGGER.error("电商订单已经发货,订单编号" + orderCode);
			throw new RuntimeException("电商订单已经发货,订单编号" + orderCode);
		}else if ("027".equals(orderModel.getStatus())) {
			LOGGER.error("电商订单出库中,订单编号" + orderCode);
			throw new RuntimeException("电商订单出库中,订单编号" + orderCode);
		}

		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderCode);
			orderExternalCodeModel.setOrderSource(2);		// 订单来源  1:电商订单, 2:装载交运 3:销售出库
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("电商订单菜鸟-装载交运回推接口外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
			throw new RuntimeException("电商订单菜鸟-装载交运回推接口外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
		}

		// 根据运单号查询运单信息表
		WayBillModel wayBillModel = new WayBillModel();
		wayBillModel.setWayBillNo(wayBillNo);// 运单号
		wayBillModel = wayBillDao.searchByModel(wayBillModel);
		if (null != wayBillModel) {
			LOGGER.error("运单信息已经存在,订单号" + orderCode);
			throw new RuntimeException("运单信息已经存在,订单号" + orderCode);
		}

		// 根据仓库id查询仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", orderModel.getDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		if (mongo==null) {
			LOGGER.error("根据仓库id没有查询到仓库,订单编号" + orderCode);
			throw new RuntimeException("根据仓库id没有查询到仓库,订单编号" + orderCode);
		}

		// 查询商家
		Map<String, Object> merchantMap = new HashMap<String, Object>();
		merchantMap.put("merchantId", orderModel.getMerchantId());// 电商订单的商家id
		MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantMap);

		if (mongo.getName().contains("菜鸟")) {
			LOGGER.error("不接受仓库是菜鸟的流程" + orderCode);
			throw new RuntimeException("不接受仓库是菜鸟的流程" + orderCode);
		}
		if (orderGoodsList==null||orderGoodsList.size()==0) {
			LOGGER.error("商品信息不能为null,订单编号" + orderCode);
			throw new RuntimeException("商品信息不能为null" + orderCode);
		}

		// 批次效期强校验
		// 批次效期强校验：1-是 0-否
		String tallyingUnitTemp="";
		for (ESaleLoadingDeliveriesMQGoodsListJson goodsListJson : orderGoodsList) {
			String tallyingUnit = goodsListJson.getTallyingUnit();

			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
				// 校验理货单位不能为空
				if (StringUtils.isBlank(tallyingUnit)) {
					LOGGER.error(mongo.getName()+",是海外仓,理货单位不能为空" + orderCode);
					throw new RuntimeException(mongo.getName()+",是海外仓,理货单位不能为空" + orderCode);
				}
				// 校验商品中的理货单位必须相同
				if (StringUtils.isNotBlank(tallyingUnitTemp)&&!tallyingUnitTemp.equals(tallyingUnit)) {
					LOGGER.error(mongo.getName()+",商品理货单位不相同" + orderCode);
					throw new RuntimeException(mongo.getName()+",商品理货单位不相同" + orderCode);
				}
				tallyingUnitTemp=tallyingUnit;
			}
			if ("1".equals(mongo.getBatchValidation())) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(mongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(mongo.getName()+",设置了批次效期强校验"+"批次效和期不能为空,订单号:" + orderCode);
				}
			}


		}


		notRookieProcess(rootJson, orderModel, orderGoodsList,mongo,merchantInfoMongo,tallyingUnitTemp);



		return true;
	}

	/**
	 * 菜鸟的流程
	 * @param rootJson
	 * @param mongo
	 */


	/**
	 * 非菜鸟的流程
	 * @param rootJson
	 * @param mongo
	 */
	public void notRookieProcess(ESaleLoadingDeliveriesMQRootJson rootJson,OrderModel orderModel,List<ESaleLoadingDeliveriesMQGoodsListJson> orderGoodsList,DepotInfoMongo mongo,MerchantInfoMongo merchantInfoMongo,String tallyingUnit)throws Exception{
		String orderCode = rootJson.getOrderCode();// 订单号
		String type = rootJson.getType();// 服务类型 业务类型10：B2B 20：B2B2C 必填
		String topidealCode = rootJson.getTopidealCode();// 卓志编码必传
		String wayBillNo = rootJson.getWayBillNo();// 运单号(电商订单必传)
		String logisticsName = rootJson.getLogisticsName();// 物流企业名称非必填
		String logisticsCode = rootJson.getLogisticsCode();// 物流企业编码非必填
		String blNo = rootJson.getBlNo();// 提单号非必填
		String remark = rootJson.getRemark();
		String deliver = rootJson.getDeliverDate();// 发货时间 非必填
		Double loadWeight = rootJson.getLoadWeight();// 包裹重量 非必填

		Timestamp deliverDate = null;// 发货时间
		if (StringUtils.isNotBlank(deliver)) {
			if (deliver.length() == 10) {
				deliverDate = Timestamp.valueOf(deliver + " 00:00:00"); // 发货时间
			} else {
				deliverDate = Timestamp.valueOf(deliver);// 发货时间
			}
			//交易时间不能大于发货时间
			if(orderModel.getTradingDate() != null){
				Timestamp tradingDate = null;
				if(deliver.length() == 10){
					tradingDate = TimeUtils.parseDay(TimeUtils.formatDay(orderModel.getTradingDate()));
				}else{
					tradingDate = orderModel.getTradingDate();
				}
				if(tradingDate.getTime() > deliverDate.getTime()){
					throw new RuntimeException("交易时间不能大于发货时间");
				}
			}
		}
		// 判断发货日期是否小于 关账月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(merchantInfoMongo.getMerchantId());
		closeAccountsMongo.setDepotId(mongo.getDepotId());
		String maxdate = "";
		if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
		}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
		}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
		}
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 关账下个月日期大于 入库日期
			if (deliverDate.getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("归属时间必须大于关账日期/月结日期");
			}
		}


		/*if(null == orderModel.getBuId()){
			LOGGER.error("该电商订单事业部信息为空,外部单号" + orderModel.getExternalCode());
			throw new RuntimeException("该电商订单事业部信息为空,外部单号" + orderModel.getExternalCode());
		}*/
		// 电商订单存在重复商品 要合并商品数量
		Map<Long,Integer> goodsMap=new HashMap<Long,Integer>();// 合并后商品的数量
		for (ESaleLoadingDeliveriesMQGoodsListJson orderGoodsModel : orderGoodsList) {
			long goodsId=orderGoodsModel.getGoodsId();
			if(goodsMap.containsKey(goodsId)){
				Integer goodsNum = goodsMap.get(goodsId);
				Integer toaleNum=goodsNum+orderGoodsModel.getNum();
				goodsMap.put(goodsId, toaleNum);
			}else{
				goodsMap.put(goodsId,orderGoodsModel.getNum());
			}
		}
		// b2c订单的商品数
		Map<Long,Integer> b2cGoodsMap=new HashMap<Long,Integer>();// 合并后商品的数量
		Map<Long, OrderItemModel> orderItemModelMap = new HashMap<>(); //表体集合
		OrderItemModel orderItemModel = new OrderItemModel();
		orderItemModel.setOrderId(orderModel.getId());// 电商订单id
		List<OrderItemModel> orderItemList = orderItemDao.list(orderItemModel);
		for (OrderItemModel orItemModel : orderItemList) {
			Long goodsId = orItemModel.getGoodsId();
			if (b2cGoodsMap.containsKey(goodsId)) {
				Integer goodsNum = b2cGoodsMap.get(goodsId);
				Integer toaleNum=goodsNum+orItemModel.getNum();
				b2cGoodsMap.put(goodsId, toaleNum);
			}else {
				b2cGoodsMap.put(goodsId, orItemModel.getNum());
			}
			orderItemModelMap.put(goodsId, orItemModel);
		}
		// 判断商品个数是否相等
		if (b2cGoodsMap.size()!=goodsMap.size()) {
			LOGGER.error("b2c订单的商品和装载交运商品量不等,订单号ororderCode:"+orderCode);
			throw new RuntimeException("b2c订单的商品量和装载交运商品量不等,订单号ororderCode:"+orderCode);
		}
		// 判断商品量是否相等
		for (Long goodsId : goodsMap.keySet()) {
			Integer goodsNum =  goodsMap.get(goodsId);
			Integer b2cGoodsNum = b2cGoodsMap.get(goodsId);
			// 判断商品是否一致
			if(goodsNum==null || b2cGoodsNum==null){
				LOGGER.error("装载交运的商品与B2C电商订单的商品不一致,订单号ororderCode:"+orderCode);
				throw new RuntimeException("装载交运的商品与B2C电商订单的商品不一致,订单号ororderCode:"+orderCode);
			}
			if (b2cGoodsNum.intValue()!=goodsNum.intValue()) {
				LOGGER.error("B2C电商订单的商品数量和装载交运的商品数量不同,订单号ororderCode:"+orderCode+",商品id:"+goodsId);
				throw new RuntimeException("B2C电商订单的商品数量和装载交运的商品数量不同,订单号ororderCode:"+orderCode+",商品id:"+goodsId);
			}
		}
		// 修改电商订单
		OrderModel oModel = new OrderModel();
		oModel.setId(orderModel.getId());
		oModel.setWayBillNo(wayBillNo);// 运单号
		oModel.setStatus(DERP_ORDER.ORDER_STATUS_027);// 单据状态
		oModel.setDeliverDate(deliverDate);// 发货时间
		oModel.setLogisticsName(logisticsName);// 物流企业名称
		oModel.setWeight(loadWeight);// 包裹重量
		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
			oModel.setTallyingUnit(tallyingUnit);
		}

		orderDao.modify(oModel);
		// 新增运单
		WayBillModel wModel = new WayBillModel();
		wModel.setOrderId(orderModel.getId());// 电商订单ID
		wModel.setWayBillNo(wayBillNo);// 运单号
		wModel.setDeliverDate(deliverDate);// 发货时间
		wModel.setLogisticsCode(logisticsCode);// 物流公司代码
		wModel.setLogisticsName(logisticsName);// 物流公司名称
		wModel.setBlNo(blNo);// 提单号
		wModel.setType(type);// 服务类型
		wModel.setRemark(remark);// 备注
		wayBillDao.save(wModel);

		// 新增运单表体
		// 减库存和释放冻结
		List<WayBillItemModel> itemList = new ArrayList<WayBillItemModel>();
		for (ESaleLoadingDeliveriesMQGoodsListJson goodsListJson : orderGoodsList) {
			// 运单表体
			WayBillItemModel wayBillItemModel = new WayBillItemModel();
			wayBillItemModel.setBillId(wModel.getId());// '运单号id
			wayBillItemModel.setGoodsId(goodsListJson.getGoodsId());// 商品ID
			wayBillItemModel.setGoodsNo(goodsListJson.getGoodsNo());// 商品货号
			wayBillItemModel.setGoodsName(goodsListJson.getGoodsName());// 商品名称
			wayBillItemModel.setGoodsCode(goodsListJson.getGoodsCode());// 商品编号
			wayBillItemModel.setBarcode(goodsListJson.getBarcode());// 商品条形码
			wayBillItemModel.setNum(goodsListJson.getNum());// 数量
			wayBillItemModel.setPrice(goodsListJson.getPrice());// 单价
			wayBillItemModel.setBatchNo(goodsListJson.getBatchNo());// 批次号
			wayBillItemModel.setCphTaxRate(goodsListJson.getCphTaxRate());// 税率'																				// 没有这数据
			wayBillItemModel.setCphTaxFee(goodsListJson.getCphTaxFee());// 税率'
			String productionDate = goodsListJson.getProductionDate();//生产日期
			if (StringUtils.isNotBlank(productionDate)) {
				if (productionDate.length()==10) {
					wayBillItemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
				}else {
					wayBillItemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
				}
			}

			String overdueDate = goodsListJson.getOverdueDate();//生产日期
			if (StringUtils.isNotBlank(overdueDate)) {
				if (overdueDate.length()==10) {
					wayBillItemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//失效日期
				}else {
					wayBillItemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//失效日期
				}
			}
			wayBillItemDao.save(wayBillItemModel);

			//获取事业部
			OrderItemModel itemModel = orderItemModelMap.get(wayBillItemModel.getGoodsId());
			if (itemModel != null) {
				wayBillItemModel.setBuName(itemModel.getBuName());
				wayBillItemModel.setBuId(itemModel.getBuId());
			}
			// 推库存商品
			itemList.add(wayBillItemModel);
		}
		/*查询所有启用的店铺*/
		Map<String, Object> merchantShopRelMap = new HashMap<>();
		merchantShopRelMap.put("shopCode",orderModel.getShopCode());
		merchantShopRelMap.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1);//'状态(1使用中,0已禁用)'
		merchantShopRelMap.put("dataSourceCode", DERP.DATASOURCE_1);	// 数据来源1-跨境宝
		List<MerchantShopRelMongo> merchantShopRelList = merchantShopRelMongoDao.findAll(merchantShopRelMap);

		if (merchantShopRelList.size()==0) {
			throw new RuntimeException("店铺编码在商家店铺关联表没有查询到该店铺编码:" + orderModel.getShopCode());
		}
		MerchantShopRelMongo merchantShopRelMongo = merchantShopRelList.get(0);
		// 跨境宝：需以“商家（货主公司）+店铺id”查询店铺信息表维护的货主事业部，若找不到时，报错预警；
		Map<String, Object> shopShipperParams = new HashMap<String, Object>();
		shopShipperParams.put("merchantId", orderModel.getMerchantId()) ;
		shopShipperParams.put("shopId", merchantShopRelMongo.getShopId()) ;	// 店铺ID
		List<MerchantShopShipperMongo> merchantShopShipperList = merchantShopShipperMongoDao.findAll(shopShipperParams);

		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRelMongo.getStoreTypeCode())) {// 外部店货主信息不能为空
			if(merchantShopShipperList==null ||merchantShopShipperList.size()==0){
				throw new RuntimeException("货主id:"+orderModel.getMerchantId()+"店铺id:"+merchantShopRelMongo.getShopId()+"在店铺货主表没找到对应信息");
			}
		}
		//记录库存类型信息
		Map<Long,String> stockLocationMap = new HashMap<Long,String>();
		for (WayBillItemModel item : itemList) {
			// 1.先看店铺类型是否为单主店还是外部店，若为单主店就先拿开店事业部查询库存
			Map<String, Object> buInventoryParams = new HashMap<String, Object>();
			buInventoryParams.put("merchantId", orderModel.getMerchantId());//商家
			buInventoryParams.put("depotId", orderModel.getDepotId());	// 仓库ID
			buInventoryParams.put("goodsId", item.getGoodsId());// 商品id
			buInventoryParams.put("month", TimeUtils.formatMonth(oModel.getDeliverDate()));// 月份(取发货时间)
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
					shopShipperStockLocationTypeId= null;
				}
			}
			OrderItemModel updateOrderItemModel = new OrderItemModel();
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
			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode())  || isNotChange==1){
				updateOrderItemModel.setBuId(buId);
				updateOrderItemModel.setBuName(buName);
				updateOrderItemModel.setStockLocationTypeId(stockLocationTypeId);
				updateOrderItemModel.setStockLocationTypeName(stockLocationTypeName);
			}else if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRelMongo.getStoreTypeCode())){// 外部店
				updateOrderItemModel.setBuId(shopShipperBuId);
				updateOrderItemModel.setBuName(shopShipperBuName);
				updateOrderItemModel.setStockLocationTypeId(shopShipperStockLocationTypeId);
				updateOrderItemModel.setStockLocationTypeName(shopShipperStockLocationTypeName);
			}

			updateOrderItemModel.setGoodsId(item.getGoodsId());
			updateOrderItemModel.setModifyDate(TimeUtils.getNow());
			OrderItemModel oItemModel = new OrderItemModel();
			oItemModel.setOrderId(orderModel.getId());
			oItemModel.setGoodsId(item.getGoodsId());
			List<OrderItemModel> oItemList = orderItemDao.list(oItemModel);
			for (int i = 0; i < oItemList.size(); i++) {
				updateOrderItemModel.setId(oItemList.get(i).getId());
				orderItemDao.modify(updateOrderItemModel);// 修改表体事业部
			}


			if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode()) && countHave==1){// 如果是单主店，需要移库
				saveBuMoveInventory(deliverDate,orderModel,oItemList.get(0),mongo,merchantInfoMongo, shopShipperBuId,shopShipperBuName,
						merchantShopRelMongo,item,shopShipperStockLocationTypeId,stockLocationMap);
			}
		}
		//获取电商订单信息
		oModel = new OrderModel();
		oModel.setId(orderModel.getId());
		oModel = orderDao.searchByModel(oModel);
		//释放并减少冻结量
		InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		inventoryFreezeRootJson.setMerchantId(oModel.getMerchantId().toString());
		inventoryFreezeRootJson.setMerchantName(oModel.getMerchantName());
		inventoryFreezeRootJson.setDepotId(oModel.getDepotId().toString());
		inventoryFreezeRootJson.setDepotName(oModel.getDepotName());
		inventoryFreezeRootJson.setOrderNo(oModel.getCode());
		inventoryFreezeRootJson.setBusinessNo(oModel.getExternalCode());// 外部订单号
		inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);
		inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_008);
		inventoryFreezeRootJson.setSourceDate(now);
		inventoryFreezeRootJson.setOperateType("1");
		List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		for (ESaleLoadingDeliveriesMQGoodsListJson goodsListJson : orderGoodsList) {
			InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();
			inventoryFreezeGoodsListJson.setGoodsId(goodsListJson.getGoodsId().toString());
			inventoryFreezeGoodsListJson.setGoodsName(goodsListJson.getGoodsName());
			inventoryFreezeGoodsListJson.setGoodsNo(goodsListJson.getGoodsNo());
			String deliverDate1 = null;// 发货时间
			if (StringUtils.isNotBlank(deliver)) {
				if (deliver.length() == 10) {
					deliverDate1 = deliver + " 00:00:00"; // 发货时间
				} else {
					deliverDate1 = deliver;// 发货时间
				}
			}
			inventoryFreezeGoodsListJson.setDivergenceDate(deliverDate1);
			inventoryFreezeGoodsListJson.setNum(goodsListJson.getNum());
			inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
		}
		inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
		rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
		//扣减销售出库库存量
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		String now1 = sdf.format(new Date());
		invetAddOrSubtractRootJson.setMerchantId(orderModel.getMerchantId().toString());
		invetAddOrSubtractRootJson.setMerchantName(orderModel.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(topidealCode);
		invetAddOrSubtractRootJson.setDepotId(orderModel.getDepotId().toString());
		invetAddOrSubtractRootJson.setDepotName(orderModel.getDepotName());
		invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
		invetAddOrSubtractRootJson.setDepotType(mongo.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(orderModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(orderModel.getExternalCode());// 外部单号
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);
		invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_008);
		invetAddOrSubtractRootJson.setSourceDate(now1);
		invetAddOrSubtractRootJson.setStorePlatformName(orderModel.getStorePlatformName());	// 电商平台名称
		invetAddOrSubtractRootJson.setShopCode(orderModel.getShopCode());// 电铺编码
//		invetAddOrSubtractRootJson.setBuId(String.valueOf(orderModel.getBuId()));// 事业部
//		invetAddOrSubtractRootJson.setBuName(orderModel.getBuName());
		String deliverDate1 = null;// 发货时间
		if (StringUtils.isNotBlank(deliver)) {
			if (deliver.length() == 10) {
				deliverDate1 = deliver + " 00:00:00"; // 发货时间
			} else {
				deliverDate1 = deliver;// 发货时间
			}
		}
		invetAddOrSubtractRootJson.setDivergenceDate(deliverDate1);
		// 获取当前年月
		invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDate));
		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		for (WayBillItemModel item : itemList) {
			InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
			invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
			invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
			invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
			invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
			invetAddOrSubtractGoodsListJson.setType("0");
			invetAddOrSubtractGoodsListJson.setNum(item.getNum());
			invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
			invetAddOrSubtractGoodsListJson.setBatchNo(item.getBatchNo());
			if (item.getProductionDate()!=null) {
				invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(item.getProductionDate()));
			}
			if (item.getOverdueDate()!=null) {
				invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(item.getOverdueDate()));
				String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
				invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）
			}else {
				invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）
			}
			String unit="";
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
				if (DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)) {
					unit=DERP.INVENTORY_UNIT_0;
				}else if (DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)) {
					unit=DERP.INVENTORY_UNIT_1;
				}else if (DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit)) {
					unit=DERP.INVENTORY_UNIT_2;
				}
			}
			invetAddOrSubtractGoodsListJson.setUnit(unit);
			// 电商订单表体信息
			OrderItemModel itemModel = new OrderItemModel();
			itemModel.setOrderId(orderModel.getId());
			itemModel.setGoodsId(item.getGoodsId());
			List<OrderItemModel> itemAllList = orderItemDao.list(itemModel);
			//事业部
			if (StringUtils.isNotBlank(itemAllList.get(0).getBuName()) && itemAllList.get(0).getBuId() != null) {
				invetAddOrSubtractGoodsListJson.setBuId(String.valueOf(itemAllList.get(0).getBuId()));// 事业部
				invetAddOrSubtractGoodsListJson.setBuName(itemAllList.get(0).getBuName());
			}
			invetAddOrSubtractGoodsListJson.setStockLocationTypeId(itemAllList.get(0).getStockLocationTypeId() == null ? "" : itemAllList.get(0).getStockLocationTypeId().toString());
			invetAddOrSubtractGoodsListJson.setStockLocationTypeName(itemAllList.get(0).getStockLocationTypeName());
			invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
		}
		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTags());//回调标签
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTopic());//回调主题
		customParam.put("code", orderModel.getCode());// 电商订单内部单号
		invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
		rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());


	}
	/**
	 * 生成移库单
	 */
	public void saveBuMoveInventory(Timestamp deliverDate,OrderModel orderModel, OrderItemModel orderItemModel,DepotInfoMongo mongo,MerchantInfoMongo merchantInfoMongo,
									Long shopShipperBuId, String shopShipperBuName, MerchantShopRelMongo merchantShopRelMongo, WayBillItemModel wayBillItemModel,
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
		buMoveInventoryModel.setDepotId(orderModel.getDepotId());
		buMoveInventoryModel.setDepotName(orderModel.getDepotName());
		buMoveInventoryModel.setMoveDate(deliverDate);// 移库时间
		buMoveInventoryModel.setMerchantId(orderModel.getMerchantId());
		buMoveInventoryModel.setMerchantName(orderModel.getMerchantName());
		buMoveInventoryModel.setCreateName("系统自动生成");
		buMoveInventoryModel.setCreateDate(TimeUtils.getNow());
		buMoveInventoryModel.setOrderType(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD);//  DSDD:电商订单
		// 海外仓保存理货单位
		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
			buMoveInventoryModel.setTallyingUnit(orderModel.getTallyingUnit());
		}

		buMoveInventoryDao.save(buMoveInventoryModel);//表头

		// 保存移库单表体
		BuMoveInventoryItemModel buMoveItemModel = new BuMoveInventoryItemModel();
		buMoveItemModel.setMoveId(buMoveInventoryModel.getId());
		buMoveItemModel.setGoodsId(wayBillItemModel.getGoodsId());
		buMoveItemModel.setGoodsCode(wayBillItemModel.getGoodsCode());
		buMoveItemModel.setGoodsName(wayBillItemModel.getGoodsName());
		buMoveItemModel.setGoodsNo(wayBillItemModel.getGoodsNo());
		buMoveItemModel.setBarcode(wayBillItemModel.getBarcode());
		buMoveItemModel.setCommbarcode(orderItemModel.getCommbarcode());
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

		List<InventoryGoodsDetailJson> inInventoryGoodsDetailList = new ArrayList<>();//移入商品集合
		// 移入商品实体
		InventoryGoodsDetailJson inInventoryGoodsDetailJson = new InventoryGoodsDetailJson();
		Long goodsId = wayBillItemModel.getGoodsId();
		String goodsName = wayBillItemModel.getGoodsName();
		String goodsNo = wayBillItemModel.getGoodsNo();
		String commbarcode = orderItemModel.getCommbarcode();
		String barcode = wayBillItemModel.getBarcode();

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
		inInventoryDetailJson.setDepotId(String.valueOf(mongo.getDepotId()));
		inInventoryDetailJson.setDepotCode(mongo.getDepotCode());
		inInventoryDetailJson.setDepotName(mongo.getName());
		inInventoryDetailJson.setDepotType(mongo.getType());
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
