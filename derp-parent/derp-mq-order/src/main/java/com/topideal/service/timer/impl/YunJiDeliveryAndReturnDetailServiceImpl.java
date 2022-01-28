package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.BillOutinDepotDao;
import com.topideal.dao.sale.BillOutinDepotItemDao;
import com.topideal.dao.sale.CrawlerOutTempDao;
import com.topideal.dao.sale.MerchandiseContrastDao;
import com.topideal.dao.sale.MerchandiseContrastItemDao;
import com.topideal.dao.sale.SaleAnalysisDao;
import com.topideal.dao.sale.SaleOrderDao;
import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.dao.sale.SaleOutDepotDao;
import com.topideal.dao.sale.SaleOutDepotItemDao;
import com.topideal.dao.sale.SaleReturnIdepotDao;
import com.topideal.dao.sale.SaleReturnIdepotItemDao;
import com.topideal.dao.sale.SaleReturnOrderDao;
import com.topideal.dao.sale.SaleReturnOrderItemDao;
import com.topideal.dao.sale.YunjiAccountDataDao;
import com.topideal.dao.sale.YunjiDeliveryDetailDao;
import com.topideal.dao.sale.YunjiReturnDetailDao;
import com.topideal.entity.vo.sale.BillOutinDepotItemModel;
import com.topideal.entity.vo.sale.BillOutinDepotModel;
import com.topideal.entity.vo.sale.CrawlerOutTempModel;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;
import com.topideal.entity.vo.sale.MerchandiseContrastModel;
import com.topideal.entity.vo.sale.YunjiDeliveryDetailModel;
import com.topideal.entity.vo.sale.YunjiReturnDetailModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.CrawlerBillMongo;
import com.topideal.mongo.entity.CustomerMerchantRelMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.YunJiDeliveryAndReturnDetailService;

import net.sf.json.JSONObject;

/**
 * 云集-唯品 生成销售出库单
 */
@Service
public class YunJiDeliveryAndReturnDetailServiceImpl implements YunJiDeliveryAndReturnDetailService {
	private static final Logger logger = LoggerFactory.getLogger(YunJiDeliveryAndReturnDetailServiceImpl.class);
	//云集退货爬虫明细
	@Autowired
	private YunjiReturnDetailDao yunjiReturnDetailDao;
	//云集爬虫发货明细
	@Autowired
	private YunjiDeliveryDetailDao yunjiDeliveryDetailDao;
	// 账单出入库单表头
	@Autowired
	private BillOutinDepotDao billOutinDepotDao;
	//账单出入库单表体
	@Autowired
	private BillOutinDepotItemDao billOutinDepotItemDao;
	
	//爬虫配置表
	@Autowired
	private ReptileConfigMongoDao reptileConfigMongoDao ;
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;
	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;	
	@Autowired
	private YunjiAccountDataDao yunjiAccountDataDao;
	
	// 爬虫生成销售出库单临时表
	@Autowired
	private CrawlerOutTempDao crawlerOutTempDao;
	// 销售出库清单
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	// 销售出库表体
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 销售订单
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;

	// 商品对照表
	@Autowired
	private MerchandiseContrastDao merchandiseContrastDao;
	// 商品对照表体
	@Autowired
	private MerchandiseContrastItemDao merchandiseContrastItemDao;
	// 商品
	@Autowired
	MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 商家
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;

	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// MQ 业务
	@Autowired
	private RMQProducer mqProducer;
	// 日志MQ
	@Autowired
	private RMQLogProducer rocketMQProducer;
	//@Autowired
	//private InventoryLocationMappingMongoDao inventoryLocationMappingMongoDao;
	/**
	 * ----------------------------------云集start--------------------------------
	 */
	/**
	 * 云集退货爬虫明细生成销售退货入库单
	 *
	 */
	@Override
	//@SystemServiceLog(point = "13201146100", model = "云集退货爬虫明细生成销售退货入库单",keyword="order_id")
	public List<InvetAddOrSubtractRootJson> saveYunJiReturnDetail(String json, String keys, String topics, String tags,
			List<String> settleIdList,MerchantInfoMongo merchant,String accountUserCode) throws Exception {
		List<InvetAddOrSubtractRootJson> rootJsonList = null;
		try {
		
			//根据登录用户名获取爬虫配置信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("platformType", DERP.CRAWLER_TYPE_1);//爬虫平台类型：1-云集  2-唯品
			params.put("merchantId", merchant.getMerchantId());//mongdb 商家id
			params.put("loginName", accountUserCode);// 用户名
			List<ReptileConfigMongo> reptileConfigMongoList = reptileConfigMongoDao.findAll(params);			
			if (reptileConfigMongoList.size()==0) {
				logger.info("云集退货爬虫明细-未找到爬虫商家配置信息,结束运行");
				throw new RuntimeException("云集退货爬虫明细-未找到爬虫商家配置信息,结束运行");
			}
			if (reptileConfigMongoList.size()>1) {
				logger.info("云集退货爬虫明细-查询到多条爬虫商家配置信息,结束运行");
				throw new RuntimeException("云集退货爬虫明细-查询到多条爬虫商家配置信息,结束运行");
			}
			
			// 根据 爬虫配置表中的客户查询爬虫配置
			CustomerMerchantRelMongo customer=null;
			ReptileConfigMongo reptileConfigMongo = reptileConfigMongoList.get(0);// 爬虫商家配置
			if(reptileConfigMongo==null) {
				String msg="唯品-商家:"+merchant.getName()+",用户:"+accountUserCode+"未找到爬虫配置结束本账单号运行";
				logger.info(msg);
				throw new RuntimeException(msg);
			}				
			
			// 查询入库仓库
			Map<String, Object> inDepotMap = new HashMap<String, Object>();
			inDepotMap.put("depotId", reptileConfigMongo.getInDepotId());// 云集代销退货仓仓库自编码
			DepotInfoMongo inDepot = depotInfoMongoDao.findOne(inDepotMap);
			if (inDepot == null) {
				logger.info("云集退货爬虫明细-未找到仓库信息,结束运行");
				throw new RuntimeException("云集退货爬虫明细-未找到仓库信息,结束运行");
			}
						
			// 公共查询条件
			Map<String, Object> detailMap = new HashMap<String, Object>();
			detailMap.put("idList",settleIdList);
			detailMap.put("merchantId", merchant.getMerchantId());
			detailMap.put("userCode", accountUserCode);
			
			// 判断是否包含 根据商家/ 用户 /账单 /sku 对应数量金额汇总
			Map<String, Map<String, Object>>containsUpdateMap=new HashMap<>();
			//根据商家/ 用户 /账单 /sku 封装已使用的 id 
			Map<String, List<Long>> updateIdsMap=new HashMap<>();
			// 存储出库单号 /根据 出库单号判断哪些id是已使用
			Map<String, String>orderCodeMap=new HashMap<>();
			
			//1、查询云集状态为未使用的所有云集退货爬虫明细,如果有指定账单id则查指定账单id		
			List<Map<String, Object>> returnDetailListAll = yunjiReturnDetailDao.getYunjiReturnDetail(detailMap);
			for (Map<String, Object> map : returnDetailListAll) {
				Long returnId = (Long) map.get("return_id");// 表体id	
				String skuNo = (String) map.get("sku_no");// 唯品sku-对应经销货号
				Date businessEndDate = (Date) map.get("business_end_date");// 结算日期
				Integer qty = (Integer) map.get("qty");
				if (qty==null)continue;				
				Long id = (Long) map.get("id");// 
				String settleId = (String) map.get("settle_id");
				String currencyType = (String) map.get("currency_type");
				BigDecimal settlementTotalPrice = (BigDecimal) map.get("settlement_total_price");	
				if (settlementTotalPrice==null)settlementTotalPrice=new BigDecimal(0);
				BigDecimal qtyBig=new BigDecimal(qty);
				// 商家 /用户 /账单/sku维护
				String updateKey=merchant.getMerchantId()+","+accountUserCode+","+settleId+","+skuNo;
				List<Long> updateIds = updateIdsMap.get(updateKey);	
				if (updateIds==null)updateIds=new ArrayList<>();
				if (updateIds.contains(returnId))continue;
				if (containsUpdateMap.containsKey(updateKey)) {
					Map<String, Object> settleSkuMap = (Map<String, Object>) containsUpdateMap.get(updateKey);
					BigDecimal qtyMap = (BigDecimal) settleSkuMap.get("qty");
					BigDecimal settlementTotalPriceMap = (BigDecimal) settleSkuMap.get("settlement_total_price");
					if (qtyMap==null)qtyMap=new BigDecimal(0);
					if (settlementTotalPriceMap==null)settlementTotalPriceMap=new BigDecimal(0);
					settleSkuMap.put("qty", qtyBig.add(qtyMap));
					settleSkuMap.put("settlement_total_price", settlementTotalPrice.add(settlementTotalPriceMap));
					containsUpdateMap.put(updateKey, settleSkuMap);
					updateIds.add(returnId);

				}else {
					Map<String, Object> settleSkuMap = new HashMap<>();
					settleSkuMap.put("qty", qtyBig);
					settleSkuMap.put("settlement_total_price", settlementTotalPrice);
					settleSkuMap.put("sku_no", skuNo);
					settleSkuMap.put("business_end_date", businessEndDate);
					settleSkuMap.put("currency_type", currencyType);
					settleSkuMap.put("settle_id", settleId);
					settleSkuMap.put("id", id);
					containsUpdateMap.put(updateKey, settleSkuMap);
					updateIds.add(returnId);// 存储已使用的id
				}
				updateIdsMap.put(updateKey, updateIds);
			}
			
			 // 各个sku的总量
			List<Map<String, Object>> returnDetailList=new ArrayList<>();
		    Set<String> returnDetailKeySet = containsUpdateMap.keySet();
		    for (String returnDetailKey : returnDetailKeySet) {
		    	Map<String, Object> map = containsUpdateMap.get(returnDetailKey);
		    	returnDetailList.add(map);
			}
			// 为空返回null
			if (returnDetailList.size()==0) {
				return null;
			}
			//2、查找账单sku在商品对照表对应经分销的货号 
			returnDetailList = getSkuGoodsNo(returnDetailList,merchant,"4");
			
			
			// 账单出入库单list
			List<BillOutinDepotModel>billOutinDepotList=new ArrayList<>();

			
			// 加减库存list
			rootJsonList=new ArrayList<>();
			// 成功日志
			List<CrawlerBillMongo>sendCrawlerLogList=new ArrayList<>();
			// 3.循环生成账单出入库单 并且生成推送库存json
			createYunJiOutInBill(merchant,inDepot,accountUserCode,  returnDetailList,billOutinDepotList,
					rootJsonList,reptileConfigMongo,sendCrawlerLogList,orderCodeMap);	
			
			
			// 4.生成账单出入库单
			for (BillOutinDepotModel billOutinDepo : billOutinDepotList) {
				// 生成账单出入库单表头
				Long id = billOutinDepotDao.save(billOutinDepo);
				List<BillOutinDepotItemModel> itemList = billOutinDepo.getItemList();
				for (BillOutinDepotItemModel itemModel : itemList) {
					itemModel.setOutinDepotId(id);
					// 生成账单出入库单表体
					billOutinDepotItemDao.save(itemModel);
				}

			}
			
			// 5.云集退货爬虫明细更新为已使用
			Set<String> orderCodekeySet = orderCodeMap.keySet();
			for (String orderCodekey : orderCodekeySet) {
				String billOutinCode = orderCodeMap.get(orderCodekey);
				List<Long> list = updateIdsMap.get(orderCodekey);
				for (Long id : list) {
					YunjiReturnDetailModel yunjiReturnIsUserDetail=new YunjiReturnDetailModel();
					yunjiReturnIsUserDetail.setBillOutinCode(billOutinCode);
					yunjiReturnIsUserDetail.setReason("成功");
					yunjiReturnIsUserDetail.setModifyDate(TimeUtils.getNow());
					yunjiReturnIsUserDetail.setId(id);
					yunjiReturnIsUserDetail.setIsUsed("1");
					yunjiReturnDetailDao.modify(yunjiReturnIsUserDetail);
				}
			}

			// 发送成功日志
			for (CrawlerBillMongo logModel : sendCrawlerLogList) {
				sendCrawlerLog(logModel);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return rootJsonList;
	}
	
	
	/**
	 * 生成云集销售退货订单 和销售退货入库单
	 * @param merchant
	 */
	public void createYunJiOutInBill(MerchantInfoMongo merchant,DepotInfoMongo inDepot,
			String accountUserCode,
			List<Map<String, Object>> returnDetailList,
			List<BillOutinDepotModel>billOutinDepotList,
			List<InvetAddOrSubtractRootJson>rootJsonList,
			ReptileConfigMongo reptileMongo,
			List<CrawlerBillMongo>sendCrawlerLogList,
			Map<String, String>orderCodeMap)throws Exception {
		
		// 按商家 /用户/结算单/是事业部
		Map<String,BillOutinDepotModel>orderMap= new HashMap<>();// 表头
		Map<String,List<BillOutinDepotItemModel>>itemOrderMap= new HashMap<>();// 表体
		Map<String,Map<String,Object>>orderNumMap= new HashMap<>();// 存储表头总金额和总数量
		Map<String,InvetAddOrSubtractRootJson>addOrSubMap= new HashMap<>();// 加减库存表头
		Map<String,List<InvetAddOrSubtractGoodsListJson>>addOrSubItemMap= new HashMap<>();// 加减库存表体
		for (Map<String, Object> returnDetaiMap : returnDetailList) {				
			String skuNo = (String) returnDetaiMap.get("sku_no");// 唯品sku-对应经销货号
			Date businessEndDate = (Date) returnDetaiMap.get("business_end_date");// 结算日期
			BigDecimal qty = (BigDecimal) returnDetaiMap.get("qty");
			BigDecimal settlementTotalPriceAll = (BigDecimal) returnDetaiMap.get("settlement_total_price");
			if (settlementTotalPriceAll==null) settlementTotalPriceAll=new BigDecimal(0);
			Long id = (Long) returnDetaiMap.get("id");
			String settleId = (String) returnDetaiMap.get("settle_id");
			String currencyType = (String) returnDetaiMap.get("currency_type");
			BigDecimal contrastAmount = (BigDecimal) returnDetaiMap.get("contrastAmount");
			MerchandiseContrastModel merchandiseContrastModel = (MerchandiseContrastModel) returnDetaiMap.get("merchandiseContrast");
			List<MerchandiseContrastItemModel>contrastItemList =  (List<MerchandiseContrastItemModel>) returnDetaiMap.get("merchandiseContrastItem");
			// 商家/账户/仓库/结算单/事业部生成表头 
			String orderKey=merchant.getMerchantId()+","+accountUserCode+","+inDepot.getDepotId()+","+settleId+","+merchandiseContrastModel.getBuId();
			String orderCode="";
			if (!orderMap.containsKey(orderKey)) {
				BillOutinDepotModel billOutinDepotModel=getReturnBillOutinDepot(merchant,inDepot,reptileMongo,merchandiseContrastModel,accountUserCode,settleId,businessEndDate,currencyType);
				orderMap.put(orderKey, billOutinDepotModel);
				orderCode = billOutinDepotModel.getCode();
				// 封装推送库存表头
				InvetAddOrSubtractRootJson invetAddOrSubtractRootJson=getInvetAddOrSubtractRootJson(billOutinDepotModel,merchant,inDepot,merchandiseContrastModel,businessEndDate);
				addOrSubMap.put(orderKey, invetAddOrSubtractRootJson);
			}else {
				BillOutinDepotModel billOutinDepotModel = orderMap.get(orderKey);
				orderCode=billOutinDepotModel.getCode();
			}
			
			// 封装业务单据表体/加减库存表头
			getReturnBillOutinDepotItem(merchant,contrastItemList,contrastAmount,settlementTotalPriceAll,qty.intValue(),skuNo,orderKey,itemOrderMap,orderNumMap,addOrSubItemMap);								
					
			// 封装更新已使用
			String updateKey=merchant.getMerchantId()+","+accountUserCode+","+settleId+","+skuNo;
			orderCodeMap.put(updateKey,orderCode);	

			// 封装成功日志
			CrawlerBillMongo logModel=getCrawlerBillMongo(merchant,inDepot,skuNo,qty,orderCode,id,settleId,"云集退货入库成功");			
			sendCrawlerLogList.add(logModel);
	
		}
		

		
		//业务单据/加减库存 表头表体组合
		Set<String> keySet = orderMap.keySet();
		for (String orderKey : keySet) {
			// 业务单据表头表体组合
			BillOutinDepotModel billOutinDepotModel = orderMap.get(orderKey);
			List<BillOutinDepotItemModel> orderItemlist = itemOrderMap.get(orderKey);
			billOutinDepotModel.setItemList(orderItemlist);
			// 业务单据表头总数量和总金额赋值
			Map<String, Object> numMap = orderNumMap.get(orderKey);
			BigDecimal totalAmount = (BigDecimal) numMap.get("totalAmount");
			Integer totalNum = (Integer) numMap.get("totalNum");
			billOutinDepotModel.setTotalAmount(totalAmount);//账单金额
			billOutinDepotModel.setTotalNum(totalNum);//账单总量
			billOutinDepotList.add(billOutinDepotModel);
			
			// 加减库表体表体组合
			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = addOrSubMap.get(orderKey);
			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubItemList = addOrSubItemMap.get(orderKey);
			invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubItemList);	
			rootJsonList.add(invetAddOrSubtractRootJson);
		}
		
		
	};


	/**
	 * 封装成功日志表体
	 * @param merchant
	 * @param inDepot
	 * @param skuNo
	 * @param qty
	 * @param orderCode
	 * @param id
	 * @param settleId
	 * @return
	 */
	private CrawlerBillMongo getCrawlerBillMongo(MerchantInfoMongo merchant, DepotInfoMongo depot, String skuNo,
			BigDecimal qty, String orderCode, Long id, String settleId,String errorMsg) {
		CrawlerBillMongo logModel = new CrawlerBillMongo();			
		logModel.setMerchantId(merchant.getMerchantId());
		logModel.setMerchantName(merchant.getName());
		logModel.setDepotId(depot.getDepotId());
		logModel.setDepotName(depot.getName());
		logModel.setNum(qty.intValue());
		logModel.setGoodsName("");
		logModel.setGoodsNo("sku:"+skuNo);
		logModel.setBarcode("");
		logModel.setPlatformType("1");// 平台类型 1、云集 2、唯品
		logModel.setStatus(1);// 状态 1-成功，0-失败
		logModel.setSaleOutDepotCode(orderCode);
		logModel.setCrawlerBillId(id);
		logModel.setBillCode(settleId);
		logModel.setErrorMsg(errorMsg);
		logModel.setCreateDate(TimeUtils.getNow().getTime());
		return logModel;
	}


	/**
	 * 封装库存表头
	 * @param merchant
	 * @param inDepot
	 * @param merchandiseContrastModel
	 * @param businessEndDate
	 */
	private InvetAddOrSubtractRootJson getInvetAddOrSubtractRootJson(BillOutinDepotModel orderModel,MerchantInfoMongo merchant, DepotInfoMongo inDepot,
			MerchandiseContrastModel merchandiseContrastModel, Date businessEndDate) {
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractRootJson.setMerchantId(merchant.getMerchantId().toString());
		invetAddOrSubtractRootJson.setMerchantName(merchant.getName());
		invetAddOrSubtractRootJson.setTopidealCode(merchant.getTopidealCode());
		invetAddOrSubtractRootJson.setBuId(String.valueOf(merchandiseContrastModel.getBuId()));
		invetAddOrSubtractRootJson.setBuName(merchandiseContrastModel.getBuName());
		invetAddOrSubtractRootJson.setDepotId(inDepot.getDepotId().toString());
		invetAddOrSubtractRootJson.setDepotName(inDepot.getName());
		invetAddOrSubtractRootJson.setDepotCode(inDepot.getCode());
		invetAddOrSubtractRootJson.setDepotType(inDepot.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(inDepot.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(orderModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(orderModel.getCode());
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0016);
		invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0027);
		invetAddOrSubtractRootJson.setSourceDate(TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss"));
		invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.format(businessEndDate, "yyyy-MM-dd HH:mm:ss"));// 出入库日期
		invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.format(businessEndDate, "yyyy-MM"));// 归属月份
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setBackTags(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTags());//回调标签
		invetAddOrSubtractRootJson.setBackTopic(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTopic());//回调主题		
		return invetAddOrSubtractRootJson;
	}


	/**
	 * 封装云集退货订单表体
	 * @param contrastItemList
	 * @param contrastAmount
	 * @param settlementTotalPriceAll
	 * @param qty
	 * @param skuNo
	 * @param orderKey
	 * @throws Exception 
	 */
	private void getReturnBillOutinDepotItem(MerchantInfoMongo merchant,List<MerchandiseContrastItemModel> contrastItemList,
			BigDecimal contrastAmount, BigDecimal settlementTotalPriceAll, Integer qty, String skuNo,
			String orderKey,Map<String,List<BillOutinDepotItemModel>>itemOrderMap,
			Map<String,Map<String,Object>>orderNumMap,
			Map<String,List<InvetAddOrSubtractGoodsListJson>>addOrSubItemMap) throws Exception {
		// 获取业务表体
		List<BillOutinDepotItemModel> orderItemList = itemOrderMap.get(orderKey);
		if (orderItemList==null||orderItemList.size()==0)orderItemList=new ArrayList<BillOutinDepotItemModel>();
		// 获取加减库存表体
		List<InvetAddOrSubtractGoodsListJson> addOrSubItemList = addOrSubItemMap.get(orderKey);		
		if (addOrSubItemList==null||addOrSubItemList.size()==0)addOrSubItemList=new ArrayList<InvetAddOrSubtractGoodsListJson>();
		BigDecimal itemAddAmount=new BigDecimal(0);// 累加金额
		for (int i = 0; i < contrastItemList.size(); i++) {
			MerchandiseContrastItemModel contrastItem = contrastItemList.get(i);
			Integer itemNum = contrastItem.getNum();
			BigDecimal itemPrice = contrastItem.getPrice();
			// 表体分摊后金额/单价/数量
			BigDecimal amount=new BigDecimal(0);
			BigDecimal price=new BigDecimal(0);
			Integer num=qty*itemNum;			
			if (i==contrastItemList.size()-1) {
				amount=settlementTotalPriceAll.subtract(itemAddAmount);
				price= amount.divide(new BigDecimal(num),8,BigDecimal.ROUND_HALF_UP);
			}else {
				amount = settlementTotalPriceAll.multiply(itemPrice).multiply(new BigDecimal(itemNum)).divide(contrastAmount,2,BigDecimal.ROUND_HALF_UP);
				price= amount.divide(new BigDecimal(num),8,BigDecimal.ROUND_HALF_UP);
				itemAddAmount=itemAddAmount.add(amount);
			}
			Map<String, Object> merchandiseMap = new HashMap<String, Object>();
			merchandiseMap.put("merchandiseId", contrastItem.getGoodsId());
			merchandiseMap.put("status", "1");
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);
			// 获取原货号
			//InventoryLocationMappingMongo orgMerchandise = inventoryLocationMappingMongoDao.getOriGoodsNoMappingModel(merchant.getTopidealCode(), contrastItem.getGoodsNo());

			
			BillOutinDepotItemModel billOutinDepotItem=new BillOutinDepotItemModel();
			billOutinDepotItem.setNum(num);//数量				
			billOutinDepotItem.setPrice(price);//单价
			billOutinDepotItem.setAmount(amount);//金额
			billOutinDepotItem.setContrastNum(itemNum);
			//billOutinDepotItem.setOutinDepotId(outinDepotId);//出入库单id
			billOutinDepotItem.setGoodsId(merchandise.getMerchandiseId());//商品id
			billOutinDepotItem.setGoodsNo(merchandise.getGoodsNo());//商品货号
			billOutinDepotItem.setGoodsName(merchandise.getName());//商品名称
			billOutinDepotItem.setPlatformSku(skuNo);//平台账单号
			billOutinDepotItem.setCommbarcode(merchandise.getCommbarcode());//标准条码
			//billOutinDepotItem.setPoNo(poNo);//po号
			//billOutinDepotItem.setPlatformSaleCode(settleId);//平台销售单号
			//billOutinDepotItem.setBillId(yunjiReturn.getId());//账单id				
			//if (orgMerchandise!=null) {
			//	billOutinDepotItem.setOriginalGoodsId(orgMerchandise.getOriginalGoodsId());
			//	billOutinDepotItem.setOriginalGoodsNo(orgMerchandise.getOriginalGoodsNo());
			//}
			//云集、唯品爬虫账单默认为税率表中的0；
			billOutinDepotItem.setTaxRate(0.0);
			//云集、唯品爬虫账单税额默认为0
			billOutinDepotItem.setTax(BigDecimal.ZERO);
			//云集、唯品爬虫账单默认： 结算金额（含税）=结算金额（不含税）
			billOutinDepotItem.setTaxAmount(amount);
			//结算单价（含税）=结算金额（含税）/数量，保留8位小数
			billOutinDepotItem.setTaxPrice(price);
			
			orderItemList.add(billOutinDepotItem);	
			itemOrderMap.put(orderKey, orderItemList);
			// 获取表头总量/金额			
			if (orderNumMap.containsKey(orderKey)) {
				Map<String, Object> numMap = orderNumMap.get(orderKey);
				BigDecimal totalAmount = (BigDecimal) numMap.get("totalAmount");
				Integer totalNum = (Integer) numMap.get("totalNum");
				numMap.put("totalAmount", totalAmount.add(amount));
				numMap.put("totalNum", totalNum+num.intValue());
				orderNumMap.put(orderKey, numMap);
			}else {
				Map<String,Object> numMap=new HashMap<>();
				numMap.put("totalAmount", amount);
				numMap.put("totalNum", num.intValue());
				orderNumMap.put(orderKey, numMap);
			}
			// 封装加减库存表头
			// 加减库存商品
    		InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
    		
    		invetAddOrSubtractGoodsListJson.setGoodsId(merchandise.getMerchandiseId().toString());
			invetAddOrSubtractGoodsListJson.setGoodsName(merchandise.getName());
			invetAddOrSubtractGoodsListJson.setGoodsNo(merchandise.getGoodsNo());
			invetAddOrSubtractGoodsListJson.setBarcode(merchandise.getBarcode());
			
			invetAddOrSubtractGoodsListJson.setType("0");
			invetAddOrSubtractGoodsListJson.setNum(num);
			invetAddOrSubtractGoodsListJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 字符串 0 调减 1调增
			invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）//是否过期（0是 1否）				
			addOrSubItemList.add(invetAddOrSubtractGoodsListJson);
			addOrSubItemMap.put(orderKey, addOrSubItemList);
			
		}

		
	}


	/**
	 * 生成账单出入库表头
	 * @param merchant
	 * @param inDepot
	 * @param reptileMongo
	 * @param merchandiseContrastModel
	 * @param accountUserCode
	 * @param settleId
	 * @param businessEndDate
	 * @return
	 * @throws Exception 
	 */
	private BillOutinDepotModel getReturnBillOutinDepot(MerchantInfoMongo merchant, DepotInfoMongo inDepot,
			ReptileConfigMongo reptileMongo, MerchandiseContrastModel merchandiseContrastModel, String accountUserCode,
			String settleId, Date businessEndDate,String currencyType) throws Exception {
		//生成爬虫出入库单表头
		BillOutinDepotModel billOutinDepotModel=new BillOutinDepotModel();
		billOutinDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZDRK));//出入库单号
		billOutinDepotModel.setDepotId(inDepot.getDepotId());//仓库ID
		billOutinDepotModel.setDepotName(inDepot.getName());//仓库名称
		billOutinDepotModel.setCustomerId(reptileMongo.getCustomerId());//客户id
		billOutinDepotModel.setCustomerName(reptileMongo.getCustomerName());//客户名称
		billOutinDepotModel.setBillCode(settleId);//平台账单号
		billOutinDepotModel.setProcessingType(DERP_ORDER.PROCESSINGTYPE_KTR);//处理类型 XSC-销售出库 GJC-国检出库 PKC-盘亏出库 BFC-报废 PYR-盘盈入库 KTR-客退入库
		billOutinDepotModel.setDeliverDate(new Timestamp(businessEndDate.getTime()));//出库时间
		billOutinDepotModel.setOperateType(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_1);//库存调整类型  0 调减 1调增
		billOutinDepotModel.setState(DERP_ORDER.BILLOUTINDEPOT_STATE_01);//单据状态 00-待审核 01-处理中 02-已出库 03-已入库
		billOutinDepotModel.setBillSource(DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_1);//账单来源 1-云集爬虫账单 2-唯品爬虫账单 3-手动导入
		billOutinDepotModel.setMerchantId(merchant.getMerchantId());//商家id
		billOutinDepotModel.setMerchantName(merchant.getName());//商家名称
		billOutinDepotModel.setBuId(merchandiseContrastModel.getBuId());
		billOutinDepotModel.setBuName(merchandiseContrastModel.getBuName());
		billOutinDepotModel.setCurrency(currencyType);//币种
		billOutinDepotModel.setCreater("系统自动生成");			
		return billOutinDepotModel;
	}


	/**
	 * tag 4.爬虫退货 3爬虫发货
	 * 2、查找账单sku在商品对照表对应经分销的货号，
	 */
	public List<Map<String, Object>> getSkuGoodsNo(List<Map<String, Object>> returnDetailList,
			MerchantInfoMongo merchant,String tag) throws Exception {
		List<Map<String, Object>> resultReturnDetailList = new ArrayList<Map<String, Object>>();// 存储校验通过的账单
		/**
		 * 2、查找账单sku在商品对照表对应经分销的货号 2.1先找代理商家、sku对应的货号。 2.2若未找到，则找主商家、sku对应的货号。
		 * 2.3若未找到则写失败日志提示：对照表未匹配到货号，结束此账单。
		 */
		for (Map<String, Object> returnDetaiMap : returnDetailList) {// ---循环账单start
			String skuNo = (String) returnDetaiMap.get("sku_no");// 唯品sku-对应经销货号
			Date businessEndDate = (Date) returnDetaiMap.get("business_end_date");// 结算日期
			BigDecimal qty = (BigDecimal) returnDetaiMap.get("qty");
			Long id = (Long) returnDetaiMap.get("id");
			String settleId = (String) returnDetaiMap.get("settle_id");
			String currencyType = (String) returnDetaiMap.get("currency_type");
			String currencyName = DERP.getLabelByKey(DERP.currencyCodeList, currencyType);			
			if (StringUtils.isBlank(currencyName)) {
				String errorMsg="云集发货/退货爬虫明细-skuNo:" + skuNo + ",币种在配置文件找那个不存在";
				updateDetailSendLog(merchant, skuNo, qty, id, settleId, errorMsg,tag);				
				continue;
			}
			
			if (businessEndDate==null) {
				String errorMsg="云集发货/退货爬虫明细-skuNo:" + skuNo + ",结算日期为空";
				updateDetailSendLog(merchant, skuNo, qty, id, settleId, errorMsg,tag);	
				continue;
			}	
			MerchandiseContrastModel contrastModel = new MerchandiseContrastModel();
			contrastModel.setCrawlerGoodsNo(skuNo);
			contrastModel.setMerchantId(merchant.getMerchantId());
			contrastModel.setStatus("0");// 0-启用 1-禁用
			contrastModel.setType("1");
			List<MerchandiseContrastModel> contrastList = merchandiseContrastDao.list(contrastModel);
			if (contrastList == null || contrastList.size() <= 0) {
				String errorMsg="云集发货/退货爬虫明细-skuNo:" + skuNo + ",在商品对照表未找到对应货号";
				updateDetailSendLog(merchant, skuNo, qty, id, settleId, errorMsg,tag);
				continue;
			}
			if ( contrastList.size() >1) {
				String errorMsg="云集发货/退货爬虫明细-skuNo:" + skuNo + ",在商品对照表找到多条";
				updateDetailSendLog(merchant, skuNo, qty, id, settleId, errorMsg,tag);				
				continue;
			}
			MerchandiseContrastModel merchandiseContrastModel = contrastList.get(0);
			// 事业部 不能为空
			if (merchandiseContrastModel.getBuId()==null||StringUtils.isBlank(merchandiseContrastModel.getBuName())) {
				String errorMsg="云集发货/退货爬虫明细-skuNo:" + skuNo + ",在商品对照表事业部id/名称为空";
				updateDetailSendLog(merchant, skuNo, qty, id, settleId, errorMsg,tag);	
				continue;
			}
			// 根据表头查询表体
			MerchandiseContrastItemModel contrastItem=new MerchandiseContrastItemModel();
			contrastItem.setContrastId(merchandiseContrastModel.getId());
			List<MerchandiseContrastItemModel> contrastItemList = merchandiseContrastItemDao.list(contrastItem);
			if (contrastItemList==null ||contrastItemList.size()==0) {
				String errorMsg="云集发货/退货爬虫明细-skuNo:" + skuNo + ",爬虫商品对照表 表体不存在";
				updateDetailSendLog(merchant, skuNo, qty, id, settleId, errorMsg,tag);					
				continue;
			}
			
			BigDecimal contrastAmount	=new BigDecimal(0);		
			String flag="1";// 0失败 1成功
			// 计算金额
			for (MerchandiseContrastItemModel contras : contrastItemList) {				
				BigDecimal price = contras.getPrice();
				Integer num = contras.getNum();
				contrastAmount = contrastAmount.add(price.multiply(new BigDecimal(num)));	
				Map<String, Object> merchandiseMap = new HashMap<String, Object>();
				merchandiseMap.put("merchantId", merchant.getMerchantId());
				merchandiseMap.put("merchandiseId", contras.getGoodsId());
				merchandiseMap.put("status", "1");
				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);
				if (merchandise==null) {
					String errorMsg="云集发货/退货爬虫明细-在mongdb中没有找到商品:" + (String) returnDetaiMap.get("sku_no") + ",在商品对照表未找到对应货号";
					updateDetailSendLog(merchant, skuNo, qty, id, settleId, errorMsg,tag);	
					logger.info("云集发货/退货爬虫明细-在mongdb中没有找到商品:" + (String) returnDetaiMap.get("sku_no") + ",在商品对照表未找到对应货号");				
					flag="0";
					break;
				}
				
				
			}
			if ("0".equals(flag)) {
				continue;
			}
			
			returnDetaiMap.put("merchandiseContrast", merchandiseContrastModel);//爬虫商品对照表头
			returnDetaiMap.put("merchandiseContrastItem", contrastItemList);//爬虫商品对照表体
			returnDetaiMap.put("contrastAmount", contrastAmount);
			resultReturnDetailList.add(returnDetaiMap);
		} // ---循环账单end
		return resultReturnDetailList;
	}
	
	
	
	
	/**
	 * 修改云集退货/出库错误原因
	 * @param merchant
	 * @param object
	 * @param skuNo
	 * @param qty
	 * @param object2
	 * @param id
	 * @param settleId
	 * @param errorMsg
	 * @param i
	 * @throws Exception 
	 */
	private void updateDetailSendLog(MerchantInfoMongo merchant, String skuNo, BigDecimal qty,
			 Long id, String settleId, String errorMsg,String tag) throws Exception {
		logger.info(errorMsg);		
		CrawlerBillMongo logModel = new CrawlerBillMongo();			
		logModel.setMerchantId(merchant.getMerchantId());
		logModel.setMerchantName(merchant.getName());
		//logModel.setDepotId(depot.getDepotId());
		//logModel.setDepotName(depot.getName());	
		//logModel.setSaleOutDepotCode(orderCode);
		logModel.setNum(qty.intValue());
		logModel.setGoodsName("");
		logModel.setGoodsNo("sku:"+skuNo);
		logModel.setBarcode("");
		logModel.setPlatformType("1");// 平台类型 1、云集 2、唯品
		logModel.setStatus(0);// 状态 1-成功，0-失败		
		logModel.setCrawlerBillId(id);
		logModel.setBillCode(settleId);
		logModel.setErrorMsg(errorMsg);
		logModel.setCreateDate(TimeUtils.getNow().getTime());
		sendCrawlerLog(logModel);
		logModel.setGoodsNo(skuNo);
		if ("3".equals(tag)) {
			updateYunjiDeliveryDetail(settleId, skuNo, errorMsg);
		}else {
			updateYunjiReturnDetail(settleId, skuNo, errorMsg);
		}
		
	}


	/**
	 * 3、查询sku对应的所有货号的库存量，并保存到临时表(如果临时表已存在此货号的库存量则不需要再查库存)。
	 * 
	 * @throws Exception
	 */
	public void saveYJGoodsInventory(Connection conn, MerchantInfoMongo merchant,DepotInfoMongo depot, List<Map<String, Object>> detailList)
			throws Exception {
		if (detailList == null || detailList.size() <= 0) {
			logger.info("云集爬虫发货-待出库集合数量为0,结束查询库存量");
			return;
		}
		for (Map<String, Object> detaiMap : detailList) {			
			// 账单sku对应经分销的货号
			MerchandiseContrastModel merchandiseContrast = (MerchandiseContrastModel) detaiMap.get("merchandiseContrast");
			List<MerchandiseContrastItemModel> contrastItemList = (List<MerchandiseContrastItemModel>) detaiMap.get("merchandiseContrastItem");
			// 遍历货号
			for (MerchandiseContrastItemModel contrastItem : contrastItemList) {				
				// 查询原货号
				CrawlerOutTempModel outTemp = new CrawlerOutTempModel();
				outTemp.setSourceType("1");// 数据来源 1-云集 2-唯品
				outTemp.setDataType("1");// 数据类型 1-库存量 2-已有销售单账单数量分配
											// 3-新增销售单账单数量分配 4-唯品红冲
				outTemp.setMerchantId(merchant.getMerchantId());
				outTemp.setDepotId(depot.getDepotId());
				String goodsNo=contrastItem.getGoodsNo();
				outTemp.setGoodsNo(goodsNo);
				
				
				try {
					// 检查临时表本仓库、货号是否已存在库存量数据，有则无需再查
					List<CrawlerOutTempModel> list = crawlerOutTempDao.list(outTemp);
					if (list != null && list.size() > 0)
						continue;
					// 查询库存余量
					Integer surplusNum = getGoodsSurplusNum(conn, merchant.getMerchantId(), depot.getDepotId(), goodsNo);
					if (surplusNum.intValue() > 0) {
						// 保存货号库存余量到临时表
						outTemp.setSurplusNum(surplusNum);
						crawlerOutTempDao.save(outTemp);
					}
				} catch (Exception e) {
					e.printStackTrace();
					//logger.info("云集爬虫发货-保存库存余量失败，货号" + merchandiseContrast.getGoodsNo());
					//throw new RuntimeException("云集爬虫发货-保存库存余量失败，货号" + merchandiseContrast.getGoodsNo());
				}
			}
		}
	}
	


	/**
	 * 6、生成出库单、保存账单与出库单关系表、扣减库存报文、更新账单状态为已使用
	 * 
	 * @throws Exception
	 */
	public void saveBillOutinDepotYJ(List<Map<String, Object>> deliveryDetailList,MerchantInfoMongo merchant,DepotInfoMongo depot,
			String accountUserCode,
			List<BillOutinDepotModel> billOutinDepotList,
			List<InvetAddOrSubtractRootJson> rootJsonList,
			List<CrawlerBillMongo> okLogList,ReptileConfigMongo reptileMongo,
			Map<String, String>orderCodeMap) throws Exception {

		// 按商家 /用户/结算单/是事业部
		Map<String,BillOutinDepotModel>orderMap= new HashMap<>();// 表头
		Map<String,List<BillOutinDepotItemModel>>itemOrderMap= new HashMap<>();// 表体
		Map<String,Map<String,Object>>orderNumMap= new HashMap<>();// 存储表头总金额和总数量
		Map<String,InvetAddOrSubtractRootJson>addOrSubMap= new HashMap<>();// 加减库存表头
		Map<String,List<InvetAddOrSubtractGoodsListJson>>addOrSubItemMap= new HashMap<>();// 加减库存表体
		

		
		//新的出库表头
		for (Map<String, Object> detaiMap : deliveryDetailList) {
			String settleId = (String) detaiMap.get("settle_id");// 结算单号
			BigDecimal qty = (BigDecimal) detaiMap.get("qty");//数量
			//BigDecimal price = (BigDecimal) detaiMap.get("price");// 价格
			String skuNo = (String) detaiMap.get("sku_no");// 商品货号
			Long id = (Long) detaiMap.get("id");// 商品货号
			BigDecimal settlementTotalPriceAll = (BigDecimal) detaiMap.get("settlement_total_price");// 价格
			Date businessEndDate = (Date) detaiMap.get("business_end_date");// 结算日期
			String currencyType = (String) detaiMap.get("currency_type");
			BigDecimal contrastAmount = (BigDecimal) detaiMap.get("contrastAmount");
			MerchandiseContrastModel merchandiseContrast = (MerchandiseContrastModel) detaiMap.get("merchandiseContrast");
			List<MerchandiseContrastItemModel> contrastItemList = (List<MerchandiseContrastItemModel>) detaiMap.get("merchandiseContrastItem");
			// 商家/仓库/结算单/事业部生成表头 
			String orderKey=merchant.getMerchantId()+","+accountUserCode+","+depot.getDepotId()+","+settleId+","+merchandiseContrast.getBuId();
			String orderCode="";			
			if (!orderMap.containsKey(orderKey)) {
				BillOutinDepotModel billOutinDepotModel=getDeliveryBillOutinDepot(merchant,depot,reptileMongo,merchandiseContrast,accountUserCode,settleId,businessEndDate,currencyType);
				orderMap.put(orderKey, billOutinDepotModel);
				orderCode = billOutinDepotModel.getCode();
				// 封装推送库存表头
				InvetAddOrSubtractRootJson invetAddOrSubtractRootJson=getDeliveryInvetAddOrSubtractRootJson(billOutinDepotModel,merchant,depot,merchandiseContrast,businessEndDate);
				addOrSubMap.put(orderKey, invetAddOrSubtractRootJson);
			}else {
				BillOutinDepotModel billOutinDepotModel = orderMap.get(orderKey);
				orderCode = billOutinDepotModel.getCode();
			}
			// 封装业务单据表体/加减库存表头
			getDeliveryBillOutinDepotItem(merchant,contrastItemList,contrastAmount,settlementTotalPriceAll,qty.intValue(),skuNo,orderKey,itemOrderMap,orderNumMap,addOrSubItemMap);								
			// 发送成功分配日志
			String errorMsg="事业部:"+merchandiseContrast.getBuName()+"云集爬虫出库明细成功";
			CrawlerBillMongo logModel=getCrawlerBillMongo(merchant,depot,skuNo,qty,orderCode,id,settleId,errorMsg);			
			okLogList.add(logModel);
			
			String updateKey=merchant.getMerchantId()+","+accountUserCode+","+settleId+","+skuNo;
			orderCodeMap.put(updateKey,orderCode);		
		}

		//业务单据/加减库存 表头表体组合
		Set<String> keySet = orderMap.keySet();
		for (String orderKey : keySet) {
			// 业务单据表头表体组合
			BillOutinDepotModel billOutinDepotModel = orderMap.get(orderKey);
			List<BillOutinDepotItemModel> orderItemlist = itemOrderMap.get(orderKey);
			billOutinDepotModel.setItemList(orderItemlist);
			// 业务单据表头总数量和总金额赋值
			Map<String, Object> numMap = orderNumMap.get(orderKey);
			BigDecimal totalAmount = (BigDecimal) numMap.get("totalAmount");
			Integer totalNum = (Integer) numMap.get("totalNum");
			billOutinDepotModel.setTotalAmount(totalAmount);//账单金额
			billOutinDepotModel.setTotalNum(totalNum);//账单总量
			billOutinDepotList.add(billOutinDepotModel);			
			// 加减库表体表体组合
			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = addOrSubMap.get(orderKey);
			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubItemList = addOrSubItemMap.get(orderKey);
			invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubItemList);	
			rootJsonList.add(invetAddOrSubtractRootJson);
		}

		
	}

	/**
	 * 封装出库 业务表体/加减明细表体
	 * @param merchant
	 * @param contrastItemList
	 * @param contrastAmount
	 * @param settlementTotalPriceAll
	 * @param intValue
	 * @param skuNo
	 * @param orderKey
	 * @param itemOrderMap
	 * @param orderNumMap
	 * @param addOrSubItemMap
	 */
	private void getDeliveryBillOutinDepotItem(MerchantInfoMongo merchant,
			List<MerchandiseContrastItemModel> contrastItemList, BigDecimal contrastAmount,
			BigDecimal settlementTotalPriceAll, int qty, String skuNo, String orderKey,
			Map<String, List<BillOutinDepotItemModel>> itemOrderMap, Map<String, Map<String, Object>> orderNumMap,
			Map<String, List<InvetAddOrSubtractGoodsListJson>> addOrSubItemMap) {
		// 获取业务表体
		List<BillOutinDepotItemModel> orderItemList = itemOrderMap.get(orderKey);
		if (orderItemList==null||orderItemList.size()==0)orderItemList=new ArrayList<BillOutinDepotItemModel>();
		// 获取加减库存表体
		List<InvetAddOrSubtractGoodsListJson> addOrSubItemList = addOrSubItemMap.get(orderKey);		
		if (addOrSubItemList==null||addOrSubItemList.size()==0)addOrSubItemList=new ArrayList<InvetAddOrSubtractGoodsListJson>();
		BigDecimal itemAddAmount=new BigDecimal(0);// 累加金额
		for (int i = 0; i < contrastItemList.size(); i++) {
			MerchandiseContrastItemModel contrastItem = contrastItemList.get(i);
			Integer itemNum = contrastItem.getNum();
			BigDecimal itemPrice = contrastItem.getPrice();
			// 表体分摊后金额/单价/数量
			BigDecimal amount=new BigDecimal(0);
			BigDecimal price=new BigDecimal(0);
			Integer num=qty*itemNum;			
			if (i==contrastItemList.size()-1) {
				amount=settlementTotalPriceAll.subtract(itemAddAmount);
				price= amount.divide(new BigDecimal(num),8,BigDecimal.ROUND_HALF_UP);
			}else {
				amount = settlementTotalPriceAll.multiply(itemPrice).multiply(new BigDecimal(itemNum)).divide(contrastAmount,2,BigDecimal.ROUND_HALF_UP);
				price= amount.divide(new BigDecimal(num),8,BigDecimal.ROUND_HALF_UP);
				itemAddAmount=itemAddAmount.add(amount);
			}
			// 查询商品
			Map<String, Object> merchandiseMap = new HashMap<String, Object>();
			merchandiseMap.put("merchandiseId", contrastItem.getGoodsId());
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);
			
			// 封装实体
			BillOutinDepotItemModel billOutinDepotItem=new BillOutinDepotItemModel();
			billOutinDepotItem.setContrastNum(itemNum);
			
			billOutinDepotItem.setGoodsId(merchandise.getMerchandiseId());//商品id
			billOutinDepotItem.setGoodsNo(merchandise.getGoodsNo());//商品货号
			billOutinDepotItem.setGoodsName(merchandise.getName());//商品名称				
			billOutinDepotItem.setCommbarcode(merchandise.getCommbarcode());//标准条码
			billOutinDepotItem.setPlatformSku(skuNo);//平台账单号
			billOutinDepotItem.setNum(num);//数量
			billOutinDepotItem.setPrice(price);//单价
			billOutinDepotItem.setAmount(amount);//金额
			//云集、唯品爬虫账单默认为税率表中的0；
			billOutinDepotItem.setTaxRate(0.0);
			//云集、唯品爬虫账单税额默认为0
			billOutinDepotItem.setTax(BigDecimal.ZERO);
			//云集、唯品爬虫账单默认： 结算金额（含税）=结算金额（不含税）
			billOutinDepotItem.setTaxAmount(amount);
			//结算单价（含税）=结算金额（含税）/数量，保留8位小数
			billOutinDepotItem.setTaxPrice(price);
			
			orderItemList.add(billOutinDepotItem);	
			itemOrderMap.put(orderKey, orderItemList);
			// 获取表头总量/金额			
			if (orderNumMap.containsKey(orderKey)) {
				Map<String, Object> numMap = orderNumMap.get(orderKey);
				BigDecimal totalAmount = (BigDecimal) numMap.get("totalAmount");
				Integer totalNum = (Integer) numMap.get("totalNum");
				numMap.put("totalAmount", totalAmount.add(amount));
				numMap.put("totalNum", totalNum+num.intValue());
				orderNumMap.put(orderKey, numMap);
			}else {
				Map<String,Object> numMap=new HashMap<>();
				numMap.put("totalAmount", amount);
				numMap.put("totalNum", num.intValue());
				orderNumMap.put(orderKey, numMap);
			}
			// 封装加减库存表体
			InvetAddOrSubtractGoodsListJson goodsJson = new InvetAddOrSubtractGoodsListJson();			
			
			goodsJson.setGoodsId(merchandise.getMerchandiseId().toString());
			goodsJson.setGoodsName(merchandise.getName());
			goodsJson.setGoodsNo(merchandise.getGoodsNo());
			goodsJson.setBarcode(merchandise.getBarcode());
			goodsJson.setType("0");// 商品分类 （0 好品 1坏品 ）
			goodsJson.setIsExpire("1");// 是否过期 （0 是 1 否）
			goodsJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 字符串 0 调减 1调增
			goodsJson.setNum(num);// 库存扣减量
			addOrSubItemList.add(goodsJson);
			addOrSubItemMap.put(orderKey, addOrSubItemList);			
		}						
	}


	/**
	 * 封装出库加减库存表头
	 * @param billOutinDepotModel
	 * @param merchant
	 * @param depot
	 * @param merchandiseContrast
	 * @param businessEndDate
	 * @return
	 */
	private InvetAddOrSubtractRootJson getDeliveryInvetAddOrSubtractRootJson(BillOutinDepotModel billOutinDepotModel,
			MerchantInfoMongo merchant, DepotInfoMongo depot, MerchandiseContrastModel merchandiseContrast,
			Date businessEndDate) {
		InvetAddOrSubtractRootJson rootJson = new InvetAddOrSubtractRootJson();
		rootJson.setMerchantId(merchant.getMerchantId().toString());
		rootJson.setMerchantName(merchant.getName());
		rootJson.setTopidealCode(merchant.getTopidealCode());
		rootJson.setDepotId(depot.getDepotId().toString());
		rootJson.setDepotName(depot.getName());
		rootJson.setDepotCode(depot.getCode());
		rootJson.setDepotType(depot.getType());
		rootJson.setIsTopBooks(depot.getIsTopBooks());
		rootJson.setOrderNo(billOutinDepotModel.getCode());
		rootJson.setBusinessNo(billOutinDepotModel.getCode());
		rootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0016);	//003
		rootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0028);
		rootJson.setSourceDate(TimeUtils.formatFullTime());
		rootJson.setDivergenceDate(TimeUtils.format(billOutinDepotModel.getDeliverDate(), "yyyy-MM-dd HH:mm:ss"));
		rootJson.setOwnMonth(TimeUtils.format(billOutinDepotModel.getDeliverDate(), "yyyy-MM"));
		rootJson.setBuId(String.valueOf(merchandiseContrast.getBuId()));
		rootJson.setBuName(merchandiseContrast.getBuName());				
		Map<String, Object> customParam = new HashMap<String, Object>();
		rootJson.setBackTags(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTags());// 回调标签
		rootJson.setBackTopic(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTopic());// 回调主题			
		return rootJson;
	}


	/**
	 * 封装 云集出库表头
	 * @param merchant
	 * @param depot
	 * @param reptileMongo
	 * @param merchandiseContrast
	 * @param accountUserCode
	 * @param settleId
	 * @param businessEndDate
	 * @param currencyType
	 * @return
	 * @throws Exception 
	 */
	private BillOutinDepotModel getDeliveryBillOutinDepot(MerchantInfoMongo merchant, DepotInfoMongo depot,
			ReptileConfigMongo reptileMongo, MerchandiseContrastModel merchandiseContrast, String accountUserCode,
			String settleId, Date businessEndDate, String currencyType) throws Exception {
		// 出库表头
		BillOutinDepotModel billOutinDepotModel=new BillOutinDepotModel();
		billOutinDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZDCK));//出入库单号
		billOutinDepotModel.setDepotId(depot.getDepotId());//仓库ID
		billOutinDepotModel.setDepotName(depot.getName());//仓库名称
		billOutinDepotModel.setCustomerId(reptileMongo.getCustomerId());//客户id
		billOutinDepotModel.setCustomerName(reptileMongo.getCustomerName());//客户名称
		billOutinDepotModel.setBillCode(settleId);//平台账单号
		billOutinDepotModel.setProcessingType(DERP_ORDER.PROCESSINGTYPE_XSC);//处理类型 XSC-销售出库 GJC-国检出库 PKC-盘亏出库 BFC-报废 PYR-盘盈入库 KTR-客退入库
		billOutinDepotModel.setDeliverDate(new Timestamp(businessEndDate.getTime()));//出库时间
		billOutinDepotModel.setOperateType(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_0);//库存调整类型  0 调减 1调增
		billOutinDepotModel.setState(DERP_ORDER.BILLOUTINDEPOT_STATE_01);//单据状态 00-待审核 01-处理中 02-已出库 03-已入库
		billOutinDepotModel.setBillSource(DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_1);//账单来源 1-云集爬虫账单 2-唯品爬虫账单 3-手动导入
		billOutinDepotModel.setMerchantId(merchant.getMerchantId());//商家id
		billOutinDepotModel.setMerchantName(merchant.getName());//商家名称
		billOutinDepotModel.setCurrency(currencyType);//币种
		billOutinDepotModel.setCreater("系统自动生成");
		billOutinDepotModel.setBuId(merchandiseContrast.getBuId());
		billOutinDepotModel.setBuName(merchandiseContrast.getBuName());		
		return billOutinDepotModel;
	}


	/**
	 * 新版云集-校验库存余量是否足够 return true-通过,false-库存不足
	 * 
	 * @throws Exception
	 */
	public List<Map<String, Object>> checkInvent(MerchantInfoMongo merchant,DepotInfoMongo depot, List<Map<String, Object>> deliveryDetailList) throws Exception {
		List<Map<String, Object>> newDeliveryDetailList=new ArrayList<>();		
		for (Map<String, Object> detaiMap : deliveryDetailList) {
			MerchandiseContrastModel merchandiseContrast = (MerchandiseContrastModel) detaiMap.get("merchandiseContrast");
			List<MerchandiseContrastItemModel> contrastItemList = (List<MerchandiseContrastItemModel>) detaiMap.get("merchandiseContrastItem");		
			String settleId = (String) detaiMap.get("settle_id");// 结算单号
			BigDecimal qty = (BigDecimal) detaiMap.get("qty");//数量
			String skuNo = (String) detaiMap.get("sku_no");// 商品货号
			boolean flag=true;
			for (MerchandiseContrastItemModel contrastItem : contrastItemList) {
				Map<String, Object> invertMap = new HashMap<String, Object>();
				Integer num = contrastItem.getNum();//商品数量
				Integer qtyChange=qty.intValue()*num;
				
				invertMap.put("sourceType", "1");// 数据来源 1-云集 2-唯品
				invertMap.put("merchantId", merchant.getMerchantId());
				invertMap.put("depotId", depot.getDepotId());
				String goodsNo=contrastItem.getGoodsNo();
				invertMap.put("goodsNo", goodsNo);
				Integer surplusNum = crawlerOutTempDao.getSurplusNum(invertMap);
				if (surplusNum == null)
					surplusNum = 0;
				if (qtyChange.intValue() > surplusNum.intValue()) {
					// 记录日志
					String msg = "云集爬虫发货-结算单号:" + detaiMap.get("settle_id") + ",skuNo:" + skuNo + "未核销量" + qtyChange + ",库存量"
							+ surplusNum + "库存不足,经分销货号" + goodsNo;
					logger.info(msg);
					CrawlerBillMongo logModel = new CrawlerBillMongo();
					logModel.setGoodsNo(skuNo);
					logModel.setPlatformType("1");// 平台类型 1、云集 2、唯品
					logModel.setStatus(0);// 状态 1-成功，0-失败
					logModel.setNum(qtyChange.intValue());// 未出库数量
					logModel.setMerchantId(merchant.getMerchantId());
					logModel.setMerchantName(merchant.getName());
					logModel.setErrorMsg(msg);
					sendCrawlerLog(logModel);
					updateYunjiDeliveryDetail(settleId, skuNo, "未核销量库存不足,核销量为:"+qtyChange+"库存量为:"+surplusNum);
					flag=false;
				} 
			}
			
			if (flag==true) {
				newDeliveryDetailList.add(detaiMap);
			}
		}
		
		
		
		
		return newDeliveryDetailList;
	}

	/**
	 * 云集爬虫出库获取sku对应的货号未核销量>0的销售单及未核销量存储到whxOrderAllMap
	 * 
	 * @param goodsNoSet
	 *            货号
	 * @param whxOrderAllMap
	 *            存储所有sku对应的货号的未核销订单
	 */
/*	public void getDeliveryOrderGoodsWhxNum(MerchantInfoMongo merchant, DepotInfoMongo depot, HashSet<MerchandiseContrastModel> goodsNoSet,
			Map<String, List<Map<String, Object>>> whxOrderAllMap) {
		for (MerchandiseContrastModel merchandiseContrast : goodsNoSet) {
			List<Map<String, Object>> orderGoodsWhxNumList = whxOrderAllMap.get(merchant.getMerchantId() + "_" + merchandiseContrast.getGoodsNo());
			if (orderGoodsWhxNumList == null || orderGoodsWhxNumList.size() <= 0) {
				// 查找商家、仓库、货号对应的未完结的销售单剩余的未核销数量
				Map<String, Object> goodsWhxMap = new HashMap<String, Object>();
				goodsWhxMap.put("merchantId", merchant.getMerchantId());
				goodsWhxMap.put("depotId", depot.getDepotId());
				goodsWhxMap.put("goodsNo", merchandiseContrast.getGoodsNo());
				//云集爬虫出库获取sku对应的货号未核销量>0的销售单
				orderGoodsWhxNumList = saleOrderDao.getDeliveryOrderGoodsWhxNum(goodsWhxMap);
				if (orderGoodsWhxNumList != null && orderGoodsWhxNumList.size() > 0) {
					// key=merchantId_goodsNo
					whxOrderAllMap.put(merchant.getMerchantId() + "_" + merchandiseContrast.getGoodsNo(), orderGoodsWhxNumList);
				}
			}
		}
	}*/

	/**
	 * 分配账单数量到已有销售单上
	 * 
	 * @throws SQLException
	 * 
	 */
/*	public Integer allotNumToOrderYJ(Map<String, Object> detailMap,MerchantInfoMongo merchant, DepotInfoMongo depot,
			String  accountUserCode,
			List<Long> crawlerOutTempIdList) throws Exception {
		String settleId = (String) detailMap.get("settle_id");// 爬虫发货结算单号
		String skuNo = (String) detailMap.get("sku_no");
		BigDecimal billwhxNumBig = (BigDecimal) detailMap.get("qty");
		Integer billwhxNum=billwhxNumBig.intValue();
		Long id = (Long) detailMap.get("id");// 云集结算账单爬虫表id
		MerchandiseContrastModel merchandiseContrast = (MerchandiseContrastModel) detailMap.get("merchandiseContrast");
		List<MerchandiseContrastItemModel> contrastItemList = (List<MerchandiseContrastItemModel>) detailMap.get("merchandiseContrastItem");		
		*//** 循环分配账单数量到对应的货号上 *//*
		for (MerchandiseContrastModel merchandiseContrast : goodsNoSet) {// ---------循环sku对应的货号start			
			*//** 查询商品 先查代理商家再查本商家 *//*
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("merchantId", merchant.getMerchantId());
			//queryMap.put("goodsNo", merchandiseContrast.getGoodsNo());
			queryMap.put("status", "1");
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(queryMap);
			// 查询库存余量
			CrawlerOutTempModel outTemp = new CrawlerOutTempModel();
			outTemp.setSourceType("1");// 数据来源 1-云集 2-唯品
			outTemp.setDataType("1");// 数据类型 1-库存量 2-已有销售单账单数量分配
										// 3-新增销售单账单数量分配
			outTemp.setMerchantId(merchant.getMerchantId());
			outTemp.setDepotId(depot.getDepotId());
			if (merchandiseContrast.getOriginalGoodsNo()!=null) {
				outTemp.setGoodsNo(merchandiseContrast.getOriginalGoodsNo());
			}else {
				//outTemp.setGoodsNo(merchandiseContrast.getGoodsNo());
			}
			
			outTemp = crawlerOutTempDao.searchByModel(outTemp);

			if (outTemp == null)
				continue;
			Integer surplusNum = outTemp.getSurplusNum();
			if (surplusNum.intValue() <= 0)
				continue;
			

			*//**
			 * 4.3.1账单未核销数量<=本次库存量,则本次分配数量=账单未核销数量   否则  账单未核销数量=本次库存量
			 *//*
			Integer benHxNum = 0;// 本次分配数量
			if (billwhxNum.intValue()  <= surplusNum.intValue()) {
				benHxNum = billwhxNum;
			}else{
				benHxNum = surplusNum.intValue();
			}

			*//**
			 * 4.3.5保存分配明细到临时表类型为已有销售单,减掉库存余量更新到临时表,减掉销售单商品未核销数量,减掉账单未核销数量。
			 *//*
			CrawlerOutTempModel tempModel = new CrawlerOutTempModel();
			tempModel.setSourceType("1");// 数据来源 1-云集 2-唯品
			tempModel.setDataType("2");// 数据类型 1-库存量 2-已有销售单账单数量分配
										// 3-新增销售单账单数量分配
			tempModel.setMerchantId(merchant.getMerchantId());
			tempModel.setDepotId(depot.getDepotId());
			tempModel.setBillId(id);// 经销自增长账单id		

			if (StringUtils.isNotBlank(merchandiseContrast.getOriginalGoodsNo())) {
				tempModel.setGoodsNo(merchandiseContrast.getOriginalGoodsNo());// 经分销原货号	
				tempModel.setLocationGoodsNo(merchandise.getGoodsNo());
			}else {
				//tempModel.setGoodsNo(merchandiseContrast.getGoodsNo());// 经分销原货号
			}
			if (merchandiseContrast.getOriginalGoodsId()!=null) {
				tempModel.setGoodsId(merchandiseContrast.getOriginalGoodsId());// 商品id
				tempModel.setLocationGoodsId(merchandise.getMerchandiseId());
			}else {
				tempModel.setGoodsId(merchandise.getMerchandiseId());// 商品id
			}
						
			tempModel.setFpNum(benHxNum);// 分配数量
			tempModel.setSkuNo(skuNo);	// skuNo
			tempModel.setBuId(merchandiseContrast.getBuId());// 事业部id
			tempModel.setBuName(merchandiseContrast.getBuName());// 事业部名称
			tempModel.setUserCode(accountUserCode);// 爬虫登入用户
			
			crawlerOutTempDao.save(tempModel);// 保存分配明细
			
			
			crawlerOutTempIdList.add(tempModel.getId());// 如果 分配完还有数据 这个是要删除的临时表
			// 减掉库存余量更新到临时表
			Map<String, Object> subInventMap = new HashMap<String, Object>();
			subInventMap.put("sourceType", "1");// 数据来源 1-云集 2-唯品
			subInventMap.put("dataType", "1");// 数据类型 1-库存量 2-已有销售单账单数量分配
												// 3-新增销售单账单数量分配
			subInventMap.put("merchantId", merchant.getMerchantId());// 商家id
			subInventMap.put("depotId", depot.getDepotId());// 仓库id
			if (StringUtils.isNotBlank(merchandiseContrast.getOriginalGoodsNo())) {
				subInventMap.put("goodsNo", merchandiseContrast.getOriginalGoodsNo());// 仓库id
			}else {
				//subInventMap.put("goodsNo", merchandiseContrast.getGoodsNo());// 仓库id
			}
			
			subInventMap.put("lowerNum", benHxNum);// 扣减数量
			crawlerOutTempDao.updateLowerNum(subInventMap);
			
			*//** 4.3.6如果账单剩余未核销数量<=0则结束此账单分配。 *//*
			billwhxNum = billwhxNum - benHxNum;
			if (billwhxNum <= 0)
				return billwhxNum;// 核销完毕结束			
		} // ---------循环sku对应的货号start
				
		return billwhxNum;
	}*/

	/**
	 * 分配账单数量到新增销售单上
	 * 
	 * @throws SQLException
	 * 
	 */
	public void allotNumToAddOrderYJ(Integer billwhxNum, Map<String, Object> billMap, DepotInfoMongo depot)
			throws SQLException {
		Long billId = (Long) billMap.get("id");// 账单id（经分销自增长id）
		Long proxyId = (Long) billMap.get("proxy_id");
		Long merchantId = (Long) billMap.get("merchant_id");
		Long customerId = (Long) billMap.get("customer_id");
		HashSet<String> goodsNoSet = (HashSet<String>) billMap.get("skuGoodsNo");// 账单sku对应的货号
		/** 循环分配账单数量到对应的货号上 */
		for (String goodsNo : goodsNoSet) {// ---------循环sku对应的货号start
			// 查询库存余量
			CrawlerOutTempModel outTemp = new CrawlerOutTempModel();
			outTemp.setSourceType("1");// 数据来源 1-云集 2-唯品
			outTemp.setDataType("1");// 数据类型 1-库存量 2-已有销售单账单数量分配 3-新增销售单账单数量分配
			outTemp.setMerchantId(merchantId);
			outTemp.setDepotId(depot.getDepotId());
			outTemp.setGoodsNo(goodsNo);
			outTemp = crawlerOutTempDao.searchByModel(outTemp);
			if (outTemp == null)
				continue;
			Integer surplusNum = outTemp.getSurplusNum();
			if (surplusNum.intValue() <= 0)
				continue;

			/** 查询商品 先查代理商家再查本商家 */
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("merchantId", proxyId);
			queryMap.put("goodsNo", goodsNo);
			queryMap.put("status", "1");
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(queryMap);
			if (merchandise == null) {
				queryMap.put("merchantId", merchantId);
				merchandise = merchandiseInfoMogoDao.findOne(queryMap);
			}

			Integer benHxNum = 0;// 本次分配数量
			/** 4.4.1账单未核销数量<=库存量够,则本次分配数量=账单未核销数量,分配数量到新增销售单此货号上 */
			if (billwhxNum.intValue() <= surplusNum) {
				benHxNum = billwhxNum;
			}
			/** 4.4.2账单未核销数量>库存量,则本次分配数量=库存量,分配数量到新增销售单此货号上。 */
			else if (billwhxNum.intValue() > surplusNum) {
				benHxNum = surplusNum;
			}
			/** 4.4.3保存分配明细到临时表类型为新增销售单,减掉库存余量更新到临时表,减掉账单未核销数量 */
			CrawlerOutTempModel tempModel = new CrawlerOutTempModel();
			tempModel.setSourceType("1");// 数据来源 1-云集 2-唯品
			tempModel.setDataType("3");// 数据类型 1-库存量 2-已有销售单账单数量分配 3-新增销售单账单数量分配
										// 4-唯品红冲
			tempModel.setMerchantId(merchantId);
			tempModel.setCustomerId(customerId);// 经销客户id
			tempModel.setDepotId(depot.getDepotId());
			tempModel.setBillId(billId);// 经销自增长账单id
			tempModel.setGoodsNo(goodsNo);// 经分销货号
			tempModel.setGoodsId(merchandise.getMerchandiseId());// 商品id
			tempModel.setFpNum(benHxNum);// 分配数量
			crawlerOutTempDao.save(tempModel);// 保存分配明细
			// 减掉库存余量更新到临时表
			Map<String, Object> subInventMap = new HashMap<String, Object>();
			subInventMap.put("sourceType", "1");// 数据来源 1-云集 2-唯品
			subInventMap.put("dataType", "1");// 数据类型 1-库存量 2-已有销售单账单数量分配
												// 3-新增销售单账单数量分配
			subInventMap.put("merchantId", merchantId);// 商家id
			subInventMap.put("depotId", depot.getDepotId());// 仓库id
			subInventMap.put("goodsNo", goodsNo);// 仓库id
			subInventMap.put("lowerNum", benHxNum);// 扣减数量
			crawlerOutTempDao.updateLowerNum(subInventMap);

			// 账单剩余未核销数量
			billwhxNum = billwhxNum - benHxNum;
			if (billwhxNum <= 0)
				break;// 核销完毕结束
		} // ---------循环sku对应的货号start
	}

	/**
	 * ----------------------------------云集end---------------------------------
	 */
	/**
	 * 获取库存jdbc链接
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(ApolloUtilDB.jdbforName);
			String dburl = ApolloUtilDB.crInventoryUrl;
			String dbUserName = ApolloUtilDB.crInventoryUserName;
			String dbPassword = ApolloUtilDB.crInventoryPassword;
			conn = DriverManager.getConnection(dburl, dbUserName, dbPassword);
			if (conn == null) {// 重试一次
				conn = DriverManager.getConnection(dburl, dbUserName, dbPassword);
			}
		} catch (Exception e) {
			logger.error("创建库存链接失败" + e.getMessage());
		}
		return conn;
	}

	/**
	 * 根据商家、仓库、货号获取库存余量
	 */
	public Integer getGoodsSurplusNum(Connection conn, Long merchantId, Long depotId, String goodsNo) {
		Statement pst = null;
		ResultSet rs = null;
		Integer surplusNum = 0;
		try {
			String queryStr = "select SUM(surplus_num) surplus_num from i_inventory_batch " + "where merchant_id="
					+ merchantId + " AND depot_id=" + depotId + " AND goods_no='" + goodsNo
					+ "' AND TYPE='0' AND is_expire='1'";
			pst = conn.createStatement();
			rs = pst.executeQuery(queryStr);
			System.out.println("查询库存余量SQL:" + queryStr);
			while (rs.next()) {
				BigDecimal num = rs.getBigDecimal("surplus_num");
				if (num != null)
					surplusNum = num.intValue();
			}
		} catch (Exception e) {
			logger.error("查询库存余量失败" + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
			}
		}
		return surplusNum;
	}






	/**
	 * 发送爬虫日志
	 */
	public void sendCrawlerLog(CrawlerBillMongo logModel) throws Exception {
		logModel.setCreateDate(TimeUtils.getNow().getTime());
		JSONObject jsonObject = JSONObject.fromObject(logModel);
		logger.info("发送爬虫日志报文：" + jsonObject.toString());
		rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CRAWLER_BILL.getTopic(),
				MQLogEnum.LOG_CRAWLER_BILL.getTags());
	}
	
	/**
	 * 发送爬虫失败日志 
	 */
	public void sendCrawlerErrorLog(MerchantInfoMongo merchant,String billCode,String poNo,
			String sku,String platformType,Integer num,String msg) throws Exception {
		CrawlerBillMongo logModel = new CrawlerBillMongo();
		logModel.setBillCode(billCode);// 账单号
		logModel.setPoNo(poNo);
		logModel.setGoodsNo(sku);
		logModel.setPlatformType(platformType);// 平台类型 1、云集 2、唯品
		logModel.setStatus(0);// 状态 1-成功，0-失败
		logModel.setNum(num);// 未出库数量
		if(merchant!=null){
			logModel.setMerchantId(merchant.getMerchantId());
			logModel.setMerchantName(merchant.getName());
		}
		logModel.setErrorMsg(msg);
		logModel.setCreateDate(TimeUtils.getNow().getTime());
		JSONObject jsonObject = JSONObject.fromObject(logModel);
		logger.info("发送爬虫日志报文：" + jsonObject.toString());
		rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CRAWLER_BILL.getTopic(),
				MQLogEnum.LOG_CRAWLER_BILL.getTags());
	}

	
	/** 清空临时表 */
	public void clearTable(Map<String, Object> map) {
		crawlerOutTempDao.clearTable(map);
	}

	@Override
	//@SystemServiceLog(point = "13201146000", model = "云集发货爬虫明细生成销售出库单",keyword="order_id")
	public List<InvetAddOrSubtractRootJson> saveYunJiDeliveryDetail(String json, String keys, String topics, String tags, 
			List<String> settleIdList,MerchantInfoMongo merchant,String accountUserCode,String settleId)
			throws Exception {
		// 存储扣减库存报文
		List<InvetAddOrSubtractRootJson> rootJsonList = new ArrayList<InvetAddOrSubtractRootJson>();
		// 用于存储 更新已使用的数据
		//List<YunjiDeliveryDetailModel> yunjiDeliveryDetaiIsUsedlList= new ArrayList<>();
		// 生成销售出库单 表头和表体数据
		List<BillOutinDepotModel> billOutinDepotList = new ArrayList<BillOutinDepotModel>();
		// 正确的日志
		List<CrawlerBillMongo> okLogList = new ArrayList<CrawlerBillMongo>();
		try {
			
			//根据登录用户名获取爬虫配置信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("platformType", DERP.CRAWLER_TYPE_1);//爬虫平台类型：1-云集  2-唯品
			params.put("merchantId", merchant.getMerchantId());//mongdb 商家id
			params.put("loginName", accountUserCode);// 用户名
			List<ReptileConfigMongo> reptileConfigMongoList = reptileConfigMongoDao.findAll(params);			
			if (reptileConfigMongoList.size()==0) {
				logger.info("云集退货爬虫明细-未找到爬虫商家配置信息,结束运行");
				throw new RuntimeException("云集退货爬虫明细-未找到爬虫商家配置信息,结束运行");
			}
			if (reptileConfigMongoList.size()>1) {
				logger.info("云集退货爬虫明细-查询到多条爬虫商家配置信息,结束运行");
				throw new RuntimeException("云集退货爬虫明细-查询到多条爬虫商家配置信息,结束运行");
			}
			// 根据 爬虫配置表中的客户查询爬虫配置
			ReptileConfigMongo reptileConfigMongo = reptileConfigMongoList.get(0);// 爬虫商家配置
			
			// 查询仓库
			Map<String, Object> depotMap = new HashMap<String, Object>();
			depotMap.put("depotId", reptileConfigMongo.getOutDepotId());// 云集代销仓库自编码
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotMap);
			if (depot == null) {
				logger.info("云集发货爬虫明细-未找到仓库信息,结束运行");
				throw new RuntimeException("云集发货爬虫明细-未找到仓库信息,结束运行");
			}
						
			/** 1、查询云集发货爬虫明细状态为未使用的所有账单,如果有指定账单id则查指定账单id。 */
			Map<String, Object> detailMerchantMap = new HashMap<String, Object>();
			detailMerchantMap.put("idList", settleIdList);
			detailMerchantMap.put("merchantId", merchant.getMerchantId());
			detailMerchantMap.put("userCode", accountUserCode);
			detailMerchantMap.put("settleId", settleId);
			int startIndex = 0;
			// 判断是否包含 根据商家/ 用户 /账单 /sku 对应数量金额汇总
			Map<String, Map<String, Object>>containsUpdateMap=new HashMap<>();
			//根据商家/ 用户 /账单 /sku 封装已使用的 id 
			Map<String, List<Long>> updateIdsMap=new HashMap<>();
			// 存储出库单号 /根据 出库单号判断哪些id是已使用
			Map<String, String>orderCodeMap=new HashMap<>();
		    while (true) {
			    detailMerchantMap.put("startIndex", startIndex);
				List<Map<String, Object>> deliveryDetailListAll =yunjiDeliveryDetailDao.getYunjiDeliveryDetail(detailMerchantMap);
				if (deliveryDetailListAll==null||deliveryDetailListAll.size()==0)break;				
				for (Map<String, Object> map : deliveryDetailListAll) {
					String settleIdMap = (String) map.get("settle_id");		
					Long deliveryId = (Long) map.get("delivery_id");// 表体id	
					Long id = (Long) map.get("id");	// 表头id			
					Integer qty = (Integer) map.get("qty");
					if (qty==null) qty=0;					
					String skuNo = (String) map.get("sku_no");
					BigDecimal settlementTotalPrice = (BigDecimal) map.get("settlement_total_price");// 表体金额
					Date businessEndDate = (Date) map.get("business_end_date");//表头发货时间
					String currencyType = (String) map.get("currency_type");//表头币种		
					BigDecimal qtyBig=new BigDecimal(qty);
					if (settlementTotalPrice==null)settlementTotalPrice=new BigDecimal(0);
					// 商家/用户/账单/sku
					String updateKey=merchant.getMerchantId()+","+accountUserCode+","+settleId+","+skuNo;
					List<Long> updateIds = updateIdsMap.get(updateKey);	
					if (updateIds==null)updateIds=new ArrayList<>();
					if (updateIds.contains(deliveryId))continue;// 防止分页查询重复id
					if (containsUpdateMap.containsKey(updateKey)) {
						Map<String, Object> settleSkuMap = (Map<String, Object>) containsUpdateMap.get(updateKey);
						BigDecimal qtyMap = (BigDecimal) settleSkuMap.get("qty");
						BigDecimal settlementTotalPriceMap = (BigDecimal) settleSkuMap.get("settlement_total_price");
						if (qtyMap==null)qtyMap=new BigDecimal(0);
						if (settlementTotalPriceMap==null)settlementTotalPriceMap=new BigDecimal(0);
						settleSkuMap.put("qty", qtyBig.add(qtyMap));
						settleSkuMap.put("settlement_total_price", settlementTotalPrice.add(settlementTotalPriceMap));
						containsUpdateMap.put(updateKey, settleSkuMap);// 存储数量/金额合计
						//根据商家 用户 账单 sku 封装已使用的 id 
						updateIds.add(deliveryId);						
					}else {
						Map<String, Object> settleSkuMap = new HashMap<>();
						settleSkuMap.put("qty", qtyBig);
						settleSkuMap.put("settlement_total_price", settlementTotalPrice);
						settleSkuMap.put("sku_no", skuNo);
						settleSkuMap.put("business_end_date", businessEndDate);
						settleSkuMap.put("currency_type", currencyType);
						settleSkuMap.put("settle_id", settleIdMap);
						settleSkuMap.put("id", id);												
						containsUpdateMap.put(updateKey, settleSkuMap);
						updateIds.add(deliveryId);
					}
					updateIdsMap.put(updateKey, updateIds);
	
				}					
				startIndex=startIndex+5000;
		    }
		    // 各个sku的总量
			List<Map<String, Object>> deliveryDetailList=new ArrayList<>();
		    Set<String> deliveryDetailKeySet = containsUpdateMap.keySet();
		    for (String deliveryDetailKey : deliveryDetailKeySet) {
		    	Map<String, Object> map = containsUpdateMap.get(deliveryDetailKey);
		    	deliveryDetailList.add(map);
			}

			if (deliveryDetailList == null || deliveryDetailList.size() <= 0) {
				logger.info("云集发货爬虫明细-未使用的账单数量为0,结束运行");
				return null;
			}
			
			/** 2、查找账单sku在商品对照表对应经分销的货号 3发货 4 退货 */
			deliveryDetailList = getSkuGoodsNo(deliveryDetailList,merchant,"3");
			// 获取库存jdbc链接用于查库存余量
			Connection conn = getConnection();
			if (conn == null) {
				logger.info("云集发货爬虫明细-获取库存链接失败,结束运行");
				throw new RuntimeException("云集发货爬虫明细-获取库存链接失败,结束运行");
			}
			/** 3、查询sku对应的所有货号的库存量，并保存到临时表 */
			saveYJGoodsInventory(conn, merchant,depot,deliveryDetailList);
			if (conn != null)
				conn.close();// 关闭连接
			/**4.库存校验 获取新的出库信息*/
			/** 4.1校验库存量是否足够，从新获取新的出库的信息 */
			deliveryDetailList = checkInvent(merchant,depot, deliveryDetailList);
			//allotNumYJ(merchant,depot,accountUserCode ,deliveryDetailList);
			/** 5、生成出库单、保存出库单与账单关系表、扣减库存报文、更新账单id为已使用 */
			
			saveBillOutinDepotYJ(deliveryDetailList,merchant,depot,accountUserCode,billOutinDepotList,
					rootJsonList,okLogList,reptileConfigMongo,orderCodeMap);											
			/** 6.更新销售出库单表头和表体  */
			for (BillOutinDepotModel billOutinDepot: billOutinDepotList) {
				//生成账单出入库表头				
				Long outinDepotId = billOutinDepotDao.save(billOutinDepot);
				List<BillOutinDepotItemModel> itemList = billOutinDepot.getItemList();
				//生成账单出入库表体
				for (BillOutinDepotItemModel item : itemList) {
					item.setOutinDepotId(outinDepotId);
					billOutinDepotItemDao.save(item);					
				}
			}
			/** 7.发送更新成功的日志*/
			for (CrawlerBillMongo logModel : okLogList) {
				sendCrawlerLog(logModel);
			}
			
			/**8.更新库存为以使用  */
			// 已使用过的sku
			Set<String> orderCodekeySet = orderCodeMap.keySet();
			for (String orderCodekey : orderCodekeySet) {
				String billOutinCode = orderCodeMap.get(orderCodekey);
				List<Long> list = updateIdsMap.get(orderCodekey);
				for (Long id : list) {
					YunjiDeliveryDetailModel yunjiDeliveryDetail=new YunjiDeliveryDetailModel();
					yunjiDeliveryDetail.setBillOutinCode(billOutinCode);
					yunjiDeliveryDetail.setReason("成功");
					yunjiDeliveryDetail.setModifyDate(TimeUtils.getNow());
					yunjiDeliveryDetail.setId(id);
					yunjiDeliveryDetail.setIsUsed("1");
					yunjiDeliveryDetailDao.modify(yunjiDeliveryDetail);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return rootJsonList;
	}
	
	/**
	 * 修改云集账单发货详情和退货详情 错误信息
	 */
	public void updateYunjiDeliveryDetail(String settleId,String skuNo,String reason)throws SQLException {
		YunjiDeliveryDetailModel yunjiDeliveryDetail= new YunjiDeliveryDetailModel();
		yunjiDeliveryDetail.setSettleId(settleId);
		yunjiDeliveryDetail.setSkuNo(skuNo);
		yunjiDeliveryDetail.setReason(reason);
		yunjiDeliveryDetailDao.updateYunjiDeliveryDetail(yunjiDeliveryDetail);
		
	};
	/**
	 * 修改云集账单发货详情和退货详情 错误信息
	 */
	public void updateYunjiReturnDetail(String settleId,String skuNo,String reason)throws SQLException{
		
		YunjiReturnDetailModel yunjiReturnDetail=new YunjiReturnDetailModel();
		yunjiReturnDetail.setSettleId(settleId);
		yunjiReturnDetail.setSkuNo(skuNo);
		yunjiReturnDetail.setReason(reason);
		yunjiReturnDetailDao.updateYunjiReturnDetail(yunjiReturnDetail);
	}
	
	/**
	 * 批量推送库存
	 */
	@Override
	public void pushMQ(List<InvetAddOrSubtractRootJson> rootJsonList) {
		// 库存扣减
		if (rootJsonList != null && rootJsonList.size() > 0) {
			// 推送库存MQ-扣减库存
			for (InvetAddOrSubtractRootJson rootJson : rootJsonList) {
				JSONObject jsonObject = null;
				try {
					jsonObject = JSONObject.fromObject(rootJson);
					SendResult sendResult = mqProducer.send(jsonObject.toString(),
							MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
							MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
					logger.info("爬虫推送扣减库存报文pcmq：" + jsonObject.toString());
					logger.info("推送结果pcmq：" + sendResult);
					// 避免数据过多会导致锁库
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("库存扣减推送异常，报文pcmqfail：" + jsonObject.toString());
				}
			}
		}				
				
		
	};
}
