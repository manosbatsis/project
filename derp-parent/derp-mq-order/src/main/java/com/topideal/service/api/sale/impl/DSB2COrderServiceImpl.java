package com.topideal.service.api.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.json.api.v3_1.ESaleB2COrderGoodsListJson;
import com.topideal.json.api.v3_1.ESaleB2COrderRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.dao.MerchantShopShipperMongoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.mongo.entity.MerchantShopShipperMongo;
import com.topideal.service.api.sale.DSB2COrderService;
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
 * B2C订单接口
 */
@Service
public class DSB2COrderServiceImpl implements DSB2COrderService{
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(DSB2COrderServiceImpl.class);
	@Autowired
	private OrderDao orderDao;//电商订单
	@Autowired
	private OrderItemDao orderItemDao;// 电商订单表体
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品信息
	// 商家店铺Mongo
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao ;
	@Autowired
	private MerchantShopShipperMongoDao merchantShopShipperMongoDao;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;// 商家信息

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203120300,model=DERP_LOG_POINT.POINT_12203120300_Label,keyword="externalCode")
	public boolean saveB2COrderInfo(String json,String keys,String topics,String tags) throws Exception {
		LOGGER.info("B2C订单接口MQ消费,开始json"+json);
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("orderGoods",ESaleB2COrderGoodsListJson.class);
		// JSON对象转实体
		ESaleB2COrderRootJson rootJson = (ESaleB2COrderRootJson) JSONObject.toBean(jsonData, ESaleB2COrderRootJson.class, classMap);
		String externalCode = rootJson.getExternalCode();
		// 向电商订单唯一标识表插入数据
		OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
		orderExternalCodeModel.setExternalCode(externalCode);
		orderExternalCodeModel.setOrderSource(1);	// 订单来源  1:电商订单, 2:装载交运 3:销售出库
		try {
			Long id = orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("电商订单外部单号来源表已经存在 单号：" + externalCode + "  保存失败");
			throw new RuntimeException("电商订单外部单号来源表已经存在 单号：" + externalCode + "  保存失败");
		}

		// 电商订单
		OrderModel oModel= new OrderModel();
		oModel.setMerchantId(rootJson.getMerchantId());// 商家id
		oModel.setMerchantName(rootJson.getMerchantName());// 商家名字
		//order_id  根据外部单号查询  电商订单 (如果电商订单存在抛异常)
		OrderModel orderModel=new OrderModel();
		orderModel.setMerchantId(rootJson.getMerchantId());
		orderModel.setExternalCode(externalCode);//订单号  ，不可重复  对应外部单号
		orderModel = orderDao.searchByModel(orderModel);
		if (null!=orderModel) {
			LOGGER.error("电商订单已经存在,外部单号"+externalCode);
			throw new RuntimeException("电商订单已经存在,外部单号"+externalCode);
		}
		// 查询商家
		Map<String, Object>merchantParamsMap=new HashMap<>();
		merchantParamsMap.put("merchantId", rootJson.getMerchantId());
		MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantParamsMap);
		if (merchantInfoMongo==null) throw new RuntimeException("根据商家id没有查询到对应的商家");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shopCode", rootJson.getShopCode()) ;
		params.put("status", "1") ;
		MerchantShopRelMongo shopRel = merchantShopRelMongoDao.findOne(params);
		if(shopRel == null) {
			LOGGER.error("没有找到对应店铺信息,外部单号"+externalCode);
			throw new RuntimeException("没有找到对应店铺信息,外部单号"+externalCode);
		}
		// 单据需根据导入的“商家+店铺id”查询店铺信息表维护的事业部，并回填事业部值到订单信息里，若找不到时，不成功
		Map<String, Object> shopShipperParams = new HashMap<String, Object>();
		shopShipperParams.put("merchantId", rootJson.getMerchantId()) ;
		shopShipperParams.put("shopId", shopRel.getShopId()) ;	// 店铺ID
		List<MerchantShopShipperMongo> merchantShopShipperList = merchantShopShipperMongoDao.findAll(shopShipperParams);

		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(shopRel.getStoreTypeCode())){// 外部店货主信息不能为空
			if(merchantShopShipperList==null ||merchantShopShipperList.size()==0){
				throw new RuntimeException("货主id:"+rootJson.getMerchantId()+"店铺id:"+shopRel.getShopId()+"在店铺货主表没找到对应信息");
			}
		}

		// 如果平台是拼多多 收货人信息是乱码不存
		String receiverName= rootJson.getReceiverName();//收件人姓名
		String receiverTel=rootJson.getReceiverTel();// 收件人电话
		String receiverAddress=rootJson.getReceiverAddress();//收件人地址
		String remark = rootJson.getRemark();
//		if (!"1000004790".equals(shopRel.getStorePlatformCode())) {
//			receiverName=rootJson.getReceiverName();
//			receiverTel=rootJson.getReceiverTel();
//			receiverAddress=rootJson.getReceiverAddress();
//		}
		if(StringUtils.isNotBlank(receiverName) && receiverName.length() > 50){
			receiverName = rootJson.getReceiverName().substring(0,50);
		}
		if(StringUtils.isNotBlank(receiverTel) && receiverTel.length() > 50){
			receiverTel = rootJson.getReceiverTel().substring(0,50);
		}
		if(StringUtils.isNotBlank(receiverAddress) && receiverAddress.length() > 200){
			receiverAddress = rootJson.getReceiverAddress().substring(0,200);
		}
		if(StringUtils.isNotBlank(remark) && remark.length() > 500){
			remark = rootJson.getRemark().substring(0,500);
		}

//		oModel.setCode(CodeGeneratorUtil.getNo("DSDD"));// 订单号(自生成)
		oModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DSDD));// 订单号(自生成)
		oModel.setExternalCode(externalCode);//外部单号
		String tradingDate = rootJson.getTradingDate();
		if (tradingDate.length()==10) {
			oModel.setTradingDate(Timestamp.valueOf(tradingDate+" 00:00:00"));//订单时间  对应交易时间
			oModel.setMakingTime(Timestamp.valueOf(tradingDate+" 00:00:00"));// 制单时间
		}else {
			oModel.setTradingDate(Timestamp.valueOf(tradingDate));//订单时间  对应交易时间
			oModel.setMakingTime(Timestamp.valueOf(tradingDate));// 制单时间
		}
		oModel.setStatus("1");//1:待申报 2.待放行,3:待发货,4:已发货,5:已取消'
		// 仓库编码    根据仓库编码查询仓库  可以获取申报方案
		oModel.setDepotId(rootJson.getDepotId());
		oModel.setDepotName(rootJson.getDepotName());
		oModel.setMakerName(rootJson.getMakerName());// 订购人姓名
		oModel.setMakerRegisterNo(rootJson.getMakerRegisterNo());//订购如注册号
		oModel.setMakerTel(rootJson.getMakerTel());// 订购人电话
		oModel.setReceiverName(receiverName);// 收件人姓名
		oModel.setReceiverTel(receiverTel);// 收件人电话
		oModel.setReceiverCountry(rootJson.getReceiverCountry());//收件人国家
		oModel.setReceiverProvince(rootJson.getReceiverProvince());// 收件人省份
		oModel.setReceiverCity(rootJson.getReceiverCity());// 收件人城市
		oModel.setReceiverArea(rootJson.getReceiverArea());// 收件人区
		oModel.setReceiverAddress(receiverAddress);// 收件人详细地址
		oModel.setOrderSource(1);//1:跨境宝推送, 2:导入
		oModel.setRemark(remark);
		// 电商平台名称 编码
		oModel.setStorePlatformName(rootJson.getStorePlatformName());
		oModel.setRemark(rootJson.getRemark());//备注
		oModel.setWayFrtFee(rootJson.getWayFrtFee());//运费，2位小数
		oModel.setWayIndFee(rootJson.getWayIndFee());//保税,2位小数
		oModel.setTaxes(rootJson.getTaxes());// 税费,2位小数
		oModel.setDiscount(rootJson.getDiscount());// 优惠减免金额
		oModel.setGoodsAmount(rootJson.getGoodsAmount());//货款
		oModel.setPayment(rootJson.getPayment());//订单实付金额
		oModel.setShopCode(rootJson.getShopCode());// 店铺编码
		oModel.setShopName(rootJson.getShopName());// 店铺名称
		oModel.setShopTypeCode(rootJson.getShopTypeCode());// 运营类型
		oModel.setCurrency(DERP.CURRENCYCODE_CNY);	// 默认人民币
		//除了拼多多店铺 其他店铺记录平台订单号
		if(!DERP.STOREPLATFORM_1000004790.equals(shopRel.getStorePlatformCode())){
			oModel.setExOrderId(rootJson.getExOrderId());	 // 平台订单号
		}
		oModel.setCustomerId(shopRel.getCustomerId());
		oModel.setCustomerName(shopRel.getCustomerName());

		// 向 电商订单中插入数据
		orderDao.save(oModel);

		// B2C订单存在重复商品 要合并商品数量
		List<ESaleB2COrderGoodsListJson> orderGoodsList = rootJson.getOrderGoods();

		// 存放冻结
		List<OrderItemModel> itemList= new ArrayList<OrderItemModel>();
		for (ESaleB2COrderGoodsListJson goodsListJson : orderGoodsList) {
			// 根据商家id和商品货号查询商品;
			Map<String, Object> merchandiseInfoMap = new HashMap<>();
			merchandiseInfoMap.put("merchantId", rootJson.getMerchantId());//商品id
			merchandiseInfoMap.put("goodsNo", goodsListJson.getGoodsNo());//商品货号
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseInfoMap);// 查询商品信息

			//电商订单表体实体类(用户新增)
			OrderItemModel itemModel =new OrderItemModel();
			itemModel.setGoodsId(goodsListJson.getGoodsId());//商品ID
			itemModel.setGoodsName(goodsListJson.getGoodsName());//商品名称
			itemModel.setBarcode(goodsListJson.getBarcode());//货品条形码
			itemModel.setGoodsCode(goodsListJson.getGoodsCode());// 商品编码
			itemModel.setGoodsNo(goodsListJson.getGoodsNo());// 商品货号
			itemModel.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
			itemModel.setOrderId(oModel.getId());//电商订单ID
			itemModel.setNum(goodsListJson.getNum());//销售数量
			BigDecimal decTotal = goodsListJson.getDecTotal();
			if (decTotal!=null) decTotal=decTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal price = goodsListJson.getPrice();
			if (price!=null)price=price.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal originalDecTotal = goodsListJson.getOriginalDecTotal();
			if (originalDecTotal!=null)originalDecTotal=originalDecTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal originalPrice = goodsListJson.getOriginalPrice();
			if (originalPrice!=null)originalPrice=originalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);

			itemModel.setDecTotal(decTotal);//结算总价
			itemModel.setPrice(price);//结算单价
			itemModel.setOriginalDecTotal(originalDecTotal); //销售总价
			itemModel.setOriginalPrice(originalPrice); //销售单价
			orderItemDao.save(itemModel);
			itemList.add(itemModel);
		}
		//冻结库存
		InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		inventoryFreezeRootJson.setMerchantId(oModel.getMerchantId().toString());
		inventoryFreezeRootJson.setMerchantName(oModel.getMerchantName());
		inventoryFreezeRootJson.setDepotId(oModel.getDepotId().toString());
		inventoryFreezeRootJson.setDepotName(oModel.getDepotName());
		inventoryFreezeRootJson.setOrderNo(oModel.getCode());
		inventoryFreezeRootJson.setBusinessNo(oModel.getExternalCode());// 电商订单
		inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);
		inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_007);
		inventoryFreezeRootJson.setSourceDate(now);
		inventoryFreezeRootJson.setOperateType("0");
		List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		for(ESaleB2COrderGoodsListJson goodsListJson : orderGoodsList){
			InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();
			inventoryFreezeGoodsListJson.setGoodsId(goodsListJson.getGoodsId().toString());
			inventoryFreezeGoodsListJson.setGoodsName(goodsListJson.getGoodsName());
			inventoryFreezeGoodsListJson.setGoodsNo(goodsListJson.getGoodsNo());
			inventoryFreezeGoodsListJson.setDivergenceDate(TimeUtils.format(oModel.getTradingDate(), "yyyy-MM-dd HH:mm:ss"));
			inventoryFreezeGoodsListJson.setNum(goodsListJson.getNum());
			inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
		}
		inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
		rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
		return true;
	}

}
