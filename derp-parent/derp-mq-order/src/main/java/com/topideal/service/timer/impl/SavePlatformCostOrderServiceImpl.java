package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.PlatformCostOrderDao;
import com.topideal.dao.bill.PlatformCostOrderItemDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.sale.CrawlerVipExtraDataDao;
import com.topideal.dao.sale.MerchandiseContrastDao;
import com.topideal.dao.sale.MerchandiseContrastItemDao;
import com.topideal.entity.vo.bill.PlatformCostOrderItemModel;
import com.topideal.entity.vo.bill.PlatformCostOrderModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.sale.CrawlerVipExtraDataModel;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;
import com.topideal.entity.vo.sale.MerchandiseContrastModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.SavePlatformCostOrderService;

import net.sf.json.JSONObject;

/**
 *
 * 生成平台费用订单
 * @author 杨创 2020/08/27
 */
@Service
public class SavePlatformCostOrderServiceImpl implements SavePlatformCostOrderService {
	@Autowired
	private CrawlerVipExtraDataDao crawlerVipExtraDataDao;//唯品账单活动折扣明细
	@Autowired
	private PlatformCostOrderDao platformCostOrderDao;// 平台费用订单
	@Autowired
	private PlatformCostOrderItemDao platformCostOrderItemDao;// 平台费用单表体
	@Autowired
	private MerchandiseContrastDao merchandiseContrastDao;	
	@Autowired
	private MerchandiseContrastItemDao merchandiseContrastItemDao;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品
	@Autowired
	private SettlementConfigDao settlementConfigDao;
	//private SettlementConfigItemDao settlementConfigItemDao;//

	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SavePlatformCostOrderServiceImpl.class);



	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201169200,model=DERP_LOG_POINT.POINT_13201169200_Label,keyword = "billCodeKeyWord")
	public boolean savePlatformCostOrder(String json, String keys, String topics, String tags,Map<String, Object> map) throws Exception {
		LOGGER.info("生成平台费用订单:"+json);
		JSONObject jsonData = JSONObject.fromObject(json);
		Long merchantId = (Long) map.get("merchant_id");
		Long customerId = (Long) map.get("customer_id");
		String billCode = (String) map.get("bill_code");
		String userCode = (String) map.get("user_code");
		String processingType = (String) map.get("processing_type");
		String currencyCode = (String) map.get("currency_code");

		
		// 根据商家id查询商家
		Map<String, Object> merchantMap=new HashMap<>();
		merchantMap.put("merchantId", merchantId);
		List<MerchantInfoMongo> merchantInfoMongoList = merchantInfoMongoDao.findAll(merchantMap);
		if (merchantInfoMongoList==null||merchantInfoMongoList.size()==0)throw new  RuntimeException("没有查询到商家信息merchantId:"+merchantId);
		if (merchantInfoMongoList.size()>1)throw new  RuntimeException("查询到多条商家信息merchantId:"+merchantId);
		MerchantInfoMongo merchantInfoMongo=merchantInfoMongoList.get(0);
		// 根据客户id查询客户
		Map<String, Object> customerMap=new HashMap<>();
		customerMap.put("customerid", customerId);
		List<CustomerInfoMogo> customerInfoMongoList = customerInfoMongoDao.findAll(customerMap);
		if (customerInfoMongoList==null||customerInfoMongoList.size()==0)throw new  RuntimeException("没有客户信息customerId:"+customerId);
		if (customerInfoMongoList.size()>1)throw new  RuntimeException("查询到多条客户信息customerId:"+customerId);
		CustomerInfoMogo customerInfoMogo = customerInfoMongoList.get(0);				
	
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId", merchantId);
		paramMap.put("customerId", customerId);
		paramMap.put("userCode", userCode);
		paramMap.put("billCode", billCode);
		paramMap.put("processingType", processingType);
		paramMap.put("currencyCode", currencyCode);
		
		List<String> goodsNoList=new ArrayList<>();
		//获取parmMap平台费用单 判断哪些sku没有配置爬虫商品配置
		List<Map<String, Object>> goodNoCrawlerVipExtra = crawlerVipExtraDataDao.getGoodNoCrawlerVipExtra(paramMap);
		for (Map<String, Object> goodNoMap : goodNoCrawlerVipExtra) {
			String crawlerGoodsNo = (String) goodNoMap.get("goods_no");
			MerchandiseContrastModel merchandiseContrastQuey=new MerchandiseContrastModel();
			merchandiseContrastQuey.setCrawlerGoodsNo(crawlerGoodsNo);
			merchandiseContrastQuey.setMerchantId(merchantId);
			merchandiseContrastQuey.setType("2");// 1云集 2唯品
			merchandiseContrastQuey.setStatus("0");// 0-启用 1-禁用
			List<MerchandiseContrastModel> merchandiseContrastList = merchandiseContrastDao.list(merchandiseContrastQuey);
			CrawlerVipExtraDataModel updateModel=new CrawlerVipExtraDataModel();
			updateModel.setMerchantId(merchantId);
			updateModel.setCustomerId(customerId);
			updateModel.setGoodsNo(crawlerGoodsNo);
			updateModel.setBillCode(billCode);
			updateModel.setProcessingType(processingType);
			updateModel.setUserCode(userCode);
			updateModel.setCurrencyCode(currencyCode);
			if (merchandiseContrastList.size()==0) {
				updateModel.setReason("没有找到对应的爬虫商品配置表头信息");
				crawlerVipExtraDataDao.updateCrawlerVipExtra(updateModel);
				goodsNoList.add(crawlerGoodsNo);
			}
			if (merchandiseContrastList.size()>1) {
				updateModel.setReason("找到多条对应的爬虫商品配置表头信息");
				crawlerVipExtraDataDao.updateCrawlerVipExtra(updateModel);
				goodsNoList.add(crawlerGoodsNo);
			}
			
		}
		paramMap.put("goodsNoList", goodsNoList);
		int startIndex = 0;
		// 判断是否包含 根据商家/ 用户 /账单 /sku 对应数量金额汇总
		Map<String, Map<String, Object>>containsUpdateMap=new HashMap<>();
		//根据商家/ 用户 /账单 /sku 封装已使用的 id 
		Map<String, List<Long>> updateIdsMap=new HashMap<>();
		// 存储出库单号 /根据 出库单号判断哪些id是已使用
		Map<String, String>orderCodeMap=new HashMap<>();
	    while (true) {
	    	paramMap.put("startIndex", startIndex);
	    	// 按 skuNo分页查询
			List<Map<String, Object>> groupByGoodNoCrawlerVipExtraListAll = crawlerVipExtraDataDao.getGroupByGoodNoCrawlerVipExtra(paramMap);
			if (groupByGoodNoCrawlerVipExtraListAll==null||groupByGoodNoCrawlerVipExtraListAll.size()==0)break;	
			for (Map<String, Object> vipMap : groupByGoodNoCrawlerVipExtraListAll) {
				Long costId = (Long) vipMap.get("id");// id 
				String crawlerGoodsNo = (String) vipMap.get("goods_no");	
				String poNo = (String) vipMap.get("po_no");			
				BigDecimal amount = (BigDecimal) vipMap.get("amount");//该sku,po下的 订单金额
				Integer skuNum = (Integer) vipMap.get("num");////该sku,po下的 订单数量
				if (skuNum==null)continue;
				BigDecimal skuNumBig=new BigDecimal(skuNum);
				if (amount==null) amount=new BigDecimal(0);
				String updateCostKey=merchantId+","+customerId+","+userCode+","+billCode+","+processingType+","
				+currencyCode+","+poNo+","+crawlerGoodsNo;
				
				
				List<Long> updateIds = updateIdsMap.get(updateCostKey);	
				if (updateIds==null)updateIds=new ArrayList<>();
				if (updateIds.contains(costId))continue;// 防止分页查询重复id
				if (containsUpdateMap.containsKey(updateCostKey)) {
					Map<String, Object> goodNomap = (Map<String, Object>) containsUpdateMap.get(updateCostKey);
					BigDecimal numMap = (BigDecimal) goodNomap.get("num");
					BigDecimal amountMap = (BigDecimal) goodNomap.get("amount");
					if (numMap==null)numMap=new BigDecimal(0);
					if (amountMap==null)amountMap=new BigDecimal(0);
					goodNomap.put("num", skuNumBig.add(numMap));
					goodNomap.put("amount", amount.add(amountMap));
					containsUpdateMap.put(updateCostKey, goodNomap);// 存储数量/金额合计
					//根据商家 用户 账单 sku 封装已使用的 id 
					updateIds.add(costId);						
				}else {
					Map<String, Object> goodNomap = new HashMap<>();
					goodNomap.put("num", skuNumBig);
					goodNomap.put("amount", amount);
					goodNomap.put("po_no", poNo);
					goodNomap.put("goods_no", crawlerGoodsNo);											
					containsUpdateMap.put(updateCostKey, goodNomap);
					updateIds.add(costId);
				}
				updateIdsMap.put(updateCostKey, updateIds);

			}
			startIndex=startIndex+5000;

	    }
	    
	    // 各个sku/Po的总量
		List<Map<String, Object>> groupByGoodNoCrawlerVipExtraList=new ArrayList<>();
	    Set<String> updateCostKeySet = containsUpdateMap.keySet();
	    for (String updateCostKey : updateCostKeySet) {
	    	Map<String, Object> goodNomap = containsUpdateMap.get(updateCostKey);
	    	groupByGoodNoCrawlerVipExtraList.add(goodNomap);
		}
	    
		if (groupByGoodNoCrawlerVipExtraList.size()==0) {
			throw new RuntimeException("商家id:"+merchantId+",billCode:"+"没有找到对应的平台货号"+"平台货号"+goodsNoList.toString());
		}
		// 按/商家/客户/用户/账单号/类型/币种/商品/poNo分组
		for (Map<String, Object> goodNomap : groupByGoodNoCrawlerVipExtraList) {
			String crawlerGoodsNo = (String) goodNomap.get("goods_no");			
			if (StringUtils.isBlank(crawlerGoodsNo)) throw new RuntimeException("爬虫商品货号不能为空");
			MerchandiseContrastModel merchandiseContrastQuey=new MerchandiseContrastModel();
			merchandiseContrastQuey.setCrawlerGoodsNo(crawlerGoodsNo);
			merchandiseContrastQuey.setMerchantId(merchantId);
			merchandiseContrastQuey.setType("2");// 1云集 2唯品
			merchandiseContrastQuey.setStatus("0");// 0-启用 1-禁用
			List<MerchandiseContrastModel> merchandiseContrastList = merchandiseContrastDao.list(merchandiseContrastQuey);
			if (merchandiseContrastList.size()==0)throw new RuntimeException("merchantId:"+merchantId+",goods_no:"+crawlerGoodsNo+"没有找到对应的爬虫商品配置表头信息");
			if (merchandiseContrastList.size()>1)throw new RuntimeException("merchantId:"+merchantId+",goods_no:"+crawlerGoodsNo+"找到多条对应的爬虫商品配置表头信息");				
			MerchandiseContrastModel merchandiseContrast = merchandiseContrastList.get(0);			
			// 根据表头查询表体
			MerchandiseContrastItemModel contrastItemQuery=new MerchandiseContrastItemModel();
			contrastItemQuery.setContrastId(merchandiseContrast.getId());
			List<MerchandiseContrastItemModel> contrastItemList = merchandiseContrastItemDao.list(contrastItemQuery);
			if (contrastItemList.size()==0) throw new RuntimeException("merchantId:"+merchantId+",goods_no:"+crawlerGoodsNo+"没有找到对应的爬虫商品配置表体信息");	
			Long buId = merchandiseContrast.getBuId();
			String buName = merchandiseContrast.getBuName();
			if (buId==null||StringUtils.isBlank(buName)) {
				throw new RuntimeException("merchantId:"+merchantId+",goods_no:"+crawlerGoodsNo+",对应的事业部为空");
			}
			BigDecimal contrastItemAmount	=new BigDecimal(0);	
			for (MerchandiseContrastItemModel contrastIteml : contrastItemList) {
				// 查询商品信息
				Map<String, Object>merchandiseMap= new HashMap<>();
				merchandiseMap.put("merchantId", merchantId);
				merchandiseMap.put("goodsNo", contrastIteml.getGoodsNo());
				MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseMap);
				if (merchandiseInfoMogo==null) {
					throw new RuntimeException("merchantId:"+merchantId+",goods_no:"+crawlerGoodsNo+",对应的商品货号:"+contrastIteml.getGoodsNo()+"没有找到对应的商品信息");
				}
				BigDecimal price = contrastIteml.getPrice();
				Integer itemNum = contrastIteml.getNum();
				if (itemNum==null||itemNum==0) {
					throw new RuntimeException("merchantId:"+merchantId+",goods_no:"+crawlerGoodsNo+"爬虫商品对照表表体数量为空/0");
				}
				contrastItemAmount = contrastItemAmount.add(price.multiply(new BigDecimal(itemNum)));			
			}

			if (contrastItemAmount.compareTo(BigDecimal.ZERO)==0) {
				throw new RuntimeException("merchantId:"+merchantId+",goods_no:"+crawlerGoodsNo+"爬虫商品对照表表体金额为0");

			}
			
			goodNomap.put("buId", buId);
			goodNomap.put("buName", buName);
			goodNomap.put("contrastItemAmount", contrastItemAmount);			
			goodNomap.put("contrastItemList", contrastItemList);
		}
		

		// 按商家, 客户,事业部,消费类型,币种生产平台消费单据
		Map<String,PlatformCostOrderModel>costOrderMap= new HashMap<>();// 表头
		Map<String,List<PlatformCostOrderItemModel>>itemOrderMap= new HashMap<>();// 表体
		Map<String,Map<String,Object>>orderNumMap= new HashMap<>();// 存储表头总金额和总数量
		for (Map<String, Object> goodNomap : groupByGoodNoCrawlerVipExtraList) {
			String crawlerGoodsNo = (String) goodNomap.get("goods_no");	
			String poNo = (String) goodNomap.get("po_no");			
			BigDecimal amountAll = (BigDecimal) goodNomap.get("amount");//该sku,po下的 订单金额
			BigDecimal skuNum = (BigDecimal) goodNomap.get("num");////该sku,po下的 订单数量
			BigDecimal contrastItemAmount = (BigDecimal) goodNomap.get("contrastItemAmount"); // sku 对应的 商品对照表总金额
			Long buId = (Long) goodNomap.get("buId");
			String buName = (String) goodNomap.get("buName");
			List<MerchandiseContrastItemModel>  contrastItemList = (List<MerchandiseContrastItemModel>) goodNomap.get("contrastItemList");
			
			
			String costKey=merchantId+","+customerId+","+userCode+","+billCode+","+processingType+","+currencyCode+","+buId;
			String orderCode="";
			// 封装表头
			if (!costOrderMap.containsKey(costKey)) {
				PlatformCostOrderModel platformCostOrderModel=getPlatformCostOrder(merchantInfoMongo,customerInfoMogo,billCode,processingType,currencyCode,buId,buName);
				orderCode=platformCostOrderModel.getCode();
				costOrderMap.put(costKey, platformCostOrderModel);
			}else {
				PlatformCostOrderModel platformCostOrderModel = costOrderMap.get(costKey);
				orderCode=platformCostOrderModel.getCode();
			}
			getPlatformCostOrderItem(contrastItemList,crawlerGoodsNo,poNo,amountAll,contrastItemAmount,skuNum.intValue(),itemOrderMap,costKey,orderNumMap);

			String updateCostKey=merchantId+","+customerId+","+userCode+","+billCode+","+processingType+","
					+currencyCode+","+poNo+","+crawlerGoodsNo;
			orderCodeMap.put(updateCostKey,orderCode);
			
			// 修改成已使用封装
			/*CrawlerVipExtraDataModel updateIsUserModel= new CrawlerVipExtraDataModel();
			updateIsUserModel.setMerchantId(merchantId);
			updateIsUserModel.setCustomerId(customerId);
			updateIsUserModel.setGoodsNo(crawlerGoodsNo);
			updateIsUserModel.setBillCode(billCode);
			updateIsUserModel.setProcessingType(processingType);
			updateIsUserModel.setUserCode(userCode);
			updateIsUserModel.setPlatformCostCode(orderCode);
			updateIsUserModel.setCurrencyCode(currencyCode);
			updateIsUserList.add(updateIsUserModel);*/
		}
		
		//生成费用单表头和表体
		Set<String> keySet = costOrderMap.keySet();
		for (String costKey : keySet) {
			PlatformCostOrderModel platformCostOrderModel = costOrderMap.get(costKey);
			Map<String, Object> numMap = orderNumMap.get(costKey);
			Integer totalNum = (Integer) numMap.get("totalNum");
			BigDecimal totalAmount = (BigDecimal) numMap.get("totalAmount");
			if (totalAmount.compareTo(BigDecimal.ZERO)==-1) {
				platformCostOrderModel.setCostType(DERP_ORDER.PLATFORM_COST_ORDER_COST_TYPE_2);// 扣款
			}else {
				platformCostOrderModel.setCostType(DERP_ORDER.PLATFORM_COST_ORDER_COST_TYPE_1);// 补款
			}
			platformCostOrderModel.setNum(totalNum);
			platformCostOrderModel.setAmount(totalAmount);

			// 新增表头
			Long id = platformCostOrderDao.save(platformCostOrderModel);
			// 新增表体
			List<PlatformCostOrderItemModel> itemList = itemOrderMap.get(costKey);
			for (int i = 0; i < itemList.size(); i++) {

				PlatformCostOrderItemModel itemModel = itemList.get(i);
				itemModel.setPlatformCostOrderId(id);
				platformCostOrderItemDao.save(itemModel);
				
			}			

		}

		Set<String> orderCodekeySet = orderCodeMap.keySet();
		for (String orderCodekey : orderCodekeySet) {
			String platformCostCode = orderCodeMap.get(orderCodekey);
			List<Long> list = updateIdsMap.get(orderCodekey);
			for (Long id : list) {
				CrawlerVipExtraDataModel updateIsUserModel=new CrawlerVipExtraDataModel();
				updateIsUserModel.setPlatformCostCode(platformCostCode);
				updateIsUserModel.setModifyDate(TimeUtils.getNow());
				updateIsUserModel.setId(id);
				updateIsUserModel.setIsUsed("1");
				updateIsUserModel.setReason("成功");
				crawlerVipExtraDataDao.modify(updateIsUserModel);
			}
		}
		
		/*for (CrawlerVipExtraDataModel updateIsUserModel : updateIsUserList) {
			crawlerVipExtraDataDao.updateIsUser(updateIsUserModel);
		}*/		
		LOGGER.info("生成平台费用单结束:");
		
		return true;
	}



	/**
	 * 获取平台费用单表体
	 * @param model
	 * @return
	 */
	private void getPlatformCostOrderItem(List<MerchandiseContrastItemModel> contrastItemList,
			String crawlerGoodsNo,String poNo,BigDecimal amountAll,
			BigDecimal contrastItemAmount,Integer skuNum,
			Map<String,List<PlatformCostOrderItemModel>>itemOrderMap,
			String costKey,
			Map<String,Map<String,Object>>orderNumMap) {
		// 获取表体
		List<PlatformCostOrderItemModel> costOrderItemList = itemOrderMap.get(costKey);
		if (costOrderItemList==null||costOrderItemList.size()==0)costOrderItemList=new ArrayList<PlatformCostOrderItemModel>();
		BigDecimal itemAddAmount=new BigDecimal(0);// 本次金额
		for (int i = 0; i < contrastItemList.size(); i++) {
			MerchandiseContrastItemModel contrastItem = contrastItemList.get(i);
			Integer itemNum = contrastItem.getNum();
			BigDecimal itemPrice = contrastItem.getPrice();
			// 表体分摊后金额/单价/数量
			BigDecimal amount=new BigDecimal(0);
			BigDecimal price=new BigDecimal(0);
			Integer num=skuNum*itemNum;
			LOGGER.info("amount"+amount+",price"+price+",num"+num);
			if (i==contrastItemList.size()-1) {
				amount=amountAll.subtract(itemAddAmount);
				if (num!=null&&num.intValue()!=0) {
					price= amount.divide(new BigDecimal(num),10,BigDecimal.ROUND_HALF_UP);
				}
				
			}else {
				amount = amountAll.multiply(itemPrice).multiply(new BigDecimal(itemNum)).divide(contrastItemAmount,2,BigDecimal.ROUND_HALF_UP);
				if (num!=null&&num.intValue()!=0) {
					price= amount.divide(new BigDecimal(num),10,BigDecimal.ROUND_HALF_UP);
				}				
				itemAddAmount=itemAddAmount.add(amount);
			}
			
			// 查询商品
			Map<String, Object>merchandiseMap= new HashMap<>();
			merchandiseMap.put("merchandiseId", contrastItem.getGoodsId());
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseMap);
			if (merchandiseInfoMogo==null) {
				throw new RuntimeException("根据商品merchandiseId:"+contrastItem.getGoodsId()+"没有找到对应的商品信息");
			}
			LOGGER.info("amount"+amount+",price"+price+",num"+num);
			// 封装表体
			PlatformCostOrderItemModel itemModel=new PlatformCostOrderItemModel();
			itemModel.setGoodsName(merchandiseInfoMogo.getName());
			itemModel.setGoodsId(merchandiseInfoMogo.getMerchandiseId());
			itemModel.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
			itemModel.setAmount(amount);	
			itemModel.setNum(num);
			itemModel.setPrice(price);
			itemModel.setPoNo(poNo);
			itemModel.setSkuNo(crawlerGoodsNo);
			costOrderItemList.add(itemModel);
			if (orderNumMap.containsKey(costKey)) {
				Map<String, Object> numMap = orderNumMap.get(costKey);
				BigDecimal totalAmount = (BigDecimal) numMap.get("totalAmount");
				Integer totalNum = (Integer) numMap.get("totalNum");
				numMap.put("totalAmount", totalAmount.add(amount));
				numMap.put("totalNum", totalNum+num.intValue());
				orderNumMap.put(costKey, numMap);
			}else {
				Map<String,Object> numMap=new HashMap<>();
				numMap.put("totalAmount", amount);
				numMap.put("totalNum", num.intValue());
				orderNumMap.put(costKey, numMap);
			}
			
		}
		
		itemOrderMap.put(costKey, costOrderItemList);

	}


	/**
	 * 生产平台费用单表头
	 * @param model
	 * @return
	 * @throws Exception
	 */
	private PlatformCostOrderModel getPlatformCostOrder(MerchantInfoMongo merchantInfoMongo,
			CustomerInfoMogo customerInfoMogo,
			String billCode,String processingType,String currencyCode,
			Long buId,String buName) throws Exception {

		SettlementConfigModel settlementConfigModel=new SettlementConfigModel();
		if (StringUtils.isBlank(processingType)) {
			throw new  RuntimeException("爬虫活动折扣明细 不能为空,请维护,billCode:"+billCode);
		}
		settlementConfigModel.setProjectName(processingType);
		settlementConfigModel = settlementConfigDao.searchByModel(settlementConfigModel);
		if (settlementConfigModel==null) {
			throw new  RuntimeException("费用配置表体没有对应的项目名称,请维护,billCode:"+billCode);
		}


		PlatformCostOrderModel costModel =new PlatformCostOrderModel();
		costModel.setMerchantId(merchantInfoMongo.getMerchantId());
		costModel.setMerchantName(merchantInfoMongo.getName());
		costModel.setBuName(buName);
		costModel.setBuId(buId);
		costModel.setCustomerId(customerInfoMogo.getCustomerid());
		costModel.setCustomerName(customerInfoMogo.getName());
		costModel.setCode(SmurfsUtils.getID(DERP.UNIQUE_ID_FYD));
		costModel.setBillCode(billCode);
		costModel.setItemProjectId(settlementConfigModel.getId());
		costModel.setItemProjectName(processingType);
		//costModel.setNum(num);
		//costModel.setAmount(amount);
		costModel.setCurrency(currencyCode);
		//costModel.setCostType(costType);
		costModel.setStatus(DERP_ORDER.PLATFORM_COST_ORDER_STATUS_2);
		costModel.setSource(DERP_ORDER.PLATFORM_COST_ORDER_source_1);
		costModel.setConfirmName("系统生成");
		costModel.setConfirmDate(TimeUtils.getNow());
		return costModel;
	}






}
