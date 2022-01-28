package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.PlatformStatementItemDao;
import com.topideal.dao.bill.PlatformStatementOrderDao;
import com.topideal.dao.common.PlatformSettlementConfigDao;
import com.topideal.dao.platformdata.AlipayDailyFeeDao;
import com.topideal.dao.platformdata.AlipayDailySettleDao;
import com.topideal.dao.platformdata.AlipayDailySettlebatchDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.bill.PlatformStatementItemModel;
import com.topideal.entity.vo.bill.PlatformStatementOrderModel;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;
import com.topideal.entity.vo.platformdata.AlipayDailyFeeModel;
import com.topideal.entity.vo.platformdata.AlipayDailySettleModel;
import com.topideal.entity.vo.platformdata.AlipayDailySettlebatchModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.mongo.dao.AttachmentInfoMongoDao;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.SavePlatformStatementOrderService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.*;

@Service
public class SavePlatformStatementOrderServiceImpl implements SavePlatformStatementOrderService{

	@Autowired
	private PlatformStatementOrderDao platformStatementOrderDao ;
	@Autowired
	private PlatformStatementItemDao platformStatementItemDao ;
	@Autowired
	private YunjiAccountDataDao yunjiAccountDataDao;
	@Autowired
	private YunjiDeliveryDetailDao yunjiDeliveryDetailDao ;
	@Autowired
	private YunjiReturnDetailDao yunjiReturnDetailDao ;
	@Autowired
	ReptileConfigMongoDao reptileConfigMongoDao;
	@Autowired
	private CrawlerBillDao crawlerBillDao ;
	@Autowired
	private CrawlerVipExtraDataDao crawlerVipExtraDataDao ;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private CrawlerVipFileDataDao crawlerVipFileDataDao ;
	@Autowired
	private AttachmentInfoMongoDao attachmentInfoMongoDao ;
	@Autowired
	private AlipayDailySettlebatchDao alipayDailySettlebatchDao ;
	@Autowired
	private AlipayDailyFeeDao alipayDailyFeeDao;
	@Autowired
	private AlipayDailySettleDao alipayDailySettleDao ;
	@Autowired
	private PlatformSettlementConfigDao platformSettlementConfigDao ;
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao ;
	
	private static final String FILE_PREFIX = "https://fdfs.hpdd.com/" ;
	
	/**
	 * 生成云集平台结算单
	 * @throws Exception 
	 */
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201169500,model=DERP_LOG_POINT.POINT_13201169500_Label)
	public void saveYJPlatStatementOrder(String json, String keys, String topics, String tags) throws Exception {
		
		JSONObject jsonObject = JSONObject.fromObject(json);
		
		//平台结算单号
		String billCode = null ;
		if(jsonObject.get("billCode") != null) {
			billCode = jsonObject.getString("billCode") ;
		}
		
		//月份 若为空，默认为上月和本月
		String months = null ;
		if(jsonObject.get("month") != null) {
			months = jsonObject.getString("month") ;
		}else {
			months = TimeUtils.getLastTwoMonthsByNow() ;
		}
		
		Long merchantId = null ;
		if(jsonObject.get("merchantId") != null) {
			merchantId = jsonObject.getLong("merchantId") ;
		}
		
		/**根据云集主数据ID*/
		Map<String, Object> customerMap = new HashMap<>() ;
		customerMap.put("mainId", "1000000690") ;
		customerMap.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_1) ;
		
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerMap);
		
		String[] montharr = months.split(",");
		for (String month : montharr) {
			/**根据条件查询云集结算单*/
			Map<String, Object> queryMap = new HashMap<>() ;
			queryMap.put("month", month) ;
			queryMap.put("settleId", billCode) ;
			queryMap.put("merchantId", merchantId) ;
			
			List<YunjiAccountDataModel> accountList = yunjiAccountDataDao.getPlatformStatementData(queryMap) ;
			
			if(accountList.isEmpty()) {
				continue ;
			}
			
			for (YunjiAccountDataModel yunjiAccountDataModel : accountList) {
				
				PlatformStatementOrderModel platformStatementOrderModel = new PlatformStatementOrderModel() ;
				platformStatementOrderModel.setBillCode(yunjiAccountDataModel.getSettleId());
				
				platformStatementOrderModel = platformStatementOrderDao.searchByModel(platformStatementOrderModel) ;
				
				//过滤已开票的结算单
				if(platformStatementOrderModel != null 
						&& DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_1.equals(platformStatementOrderModel.getIsInvoice())) {
					continue ;
				}
				
				//总金额
				BigDecimal billAmount = new BigDecimal(0) ;
				
				/**根据结算单号，sku汇总云集发货明细*/
				Map<String, Object> itemQueryMap = new HashMap<>() ;
				itemQueryMap.put("settleId", yunjiAccountDataModel.getSettleId()) ;
				
				List<PlatformStatementItemModel> saveList = new ArrayList<PlatformStatementItemModel>() ;
				
				List<YunjiDeliveryDetailModel> deliveryList = yunjiDeliveryDetailDao.getPlatformStatementSumData(itemQueryMap) ;
				
				for (YunjiDeliveryDetailModel yunjiDeliveryDetailModel : deliveryList) {
					
					//总金额
					BigDecimal settlementTotalPrice = yunjiDeliveryDetailModel.getSettlementTotalPrice();
					
					billAmount = billAmount.add(settlementTotalPrice) ;
						
					PlatformStatementItemModel item = new PlatformStatementItemModel() ;
					item.setType(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_1);
					item.setBarcode(yunjiDeliveryDetailModel.getSkuNo());
					item.setGoodsName(yunjiDeliveryDetailModel.getItemName());
					item.setSettlementNum(yunjiDeliveryDetailModel.getQty());
					item.setSettlementAmount(settlementTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
					item.setCreateDate(TimeUtils.getNow());
					
					saveList.add(item) ;
					
				}
				/**根据结算单号，sku汇总云集发货明细*/
				
				/**根据结算单号，sku汇总云集退货明细*/
				List<YunjiReturnDetailModel> retrunList = yunjiReturnDetailDao.getPlatformStatementSumData(itemQueryMap) ;
				
				for (YunjiReturnDetailModel yunjiReturnDetailModel : retrunList) {
					
					//总金额
					BigDecimal settlementTotalPrice = yunjiReturnDetailModel.getSettlementTotalPrice();
					
					billAmount = billAmount.subtract(settlementTotalPrice) ;
					
					PlatformStatementItemModel item = new PlatformStatementItemModel() ;
					item.setType(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_2);
					item.setBarcode(yunjiReturnDetailModel.getSkuNo());
					item.setGoodsName(yunjiReturnDetailModel.getItemName());
					item.setCreateDate(TimeUtils.getNow());
					
					Integer qty = yunjiReturnDetailModel.getQty();
					qty = new BigDecimal(qty).negate().intValue() ;
					
					item.setSettlementNum(qty);
					item.setSettlementAmount(settlementTotalPrice.negate().setScale(2, BigDecimal.ROUND_HALF_UP));
					
					
					saveList.add(item) ;
					
				}
				/**根据结算单号，sku汇总云集退货明细*/
				
				Long orderId = null ;
				if(platformStatementOrderModel == null) {
					
					platformStatementOrderModel = new PlatformStatementOrderModel() ;
					platformStatementOrderModel.setBillAmount(billAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
					platformStatementOrderModel.setBillCode(yunjiAccountDataModel.getSettleId());
					platformStatementOrderModel.setCurrency(yunjiAccountDataModel.getCurrencyType());
					platformStatementOrderModel.setCustomerType(DERP_ORDER.PLATFORM_STATEMENT_CUSTOMER_TYPE_1);
					platformStatementOrderModel.setIsInvoice(DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_0);
					platformStatementOrderModel.setIsCreateReceive(DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_0);
					platformStatementOrderModel.setMerchantId(yunjiAccountDataModel.getMerchantId());
					platformStatementOrderModel.setMerchantName(yunjiAccountDataModel.getMerchantName());
					platformStatementOrderModel.setMonth(month);
					platformStatementOrderModel.setCustomerId(customer.getCustomerid());
					platformStatementOrderModel.setCustomerName(customer.getName());
					platformStatementOrderModel.setCreateDate(TimeUtils.getNow());
					platformStatementOrderModel.setType(DERP_ORDER.PLATFORM_STATEMENT_TYPE_1);
					
					orderId = platformStatementOrderDao.save(platformStatementOrderModel);
					
				}else {
					
					platformStatementItemDao.deleteByOrderId(platformStatementOrderModel.getId()) ;
					
					platformStatementOrderModel.setBillAmount(billAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
					platformStatementOrderModel.setModifyDate(TimeUtils.getNow());
					
					platformStatementOrderDao.modify(platformStatementOrderModel) ;
					
					orderId = platformStatementOrderModel.getId() ;
				}
				
				for (PlatformStatementItemModel saveItem : saveList) {
					saveItem.setPlatformStatementOrderId(orderId);
					
					platformStatementItemDao.save(saveItem) ;
				}
				
				/**生成附件信息*/
				String fileKey = yunjiAccountDataModel.getFileKey();
				
				saveAttachment(fileKey, yunjiAccountDataModel.getSettleId());
				/**生成附件信息*/
				
			}
		}
		
	}

	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201169501,model=DERP_LOG_POINT.POINT_13201169501_Label)
	public void saveVipPlatStatementOrder(String json, String keys, String topics, String tags) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(json);
		
		//平台结算单号
		String billCode = null ;
		if(jsonObject.get("billCode") != null) {
			billCode = jsonObject.getString("billCode") ;
		}
		
		//月份 若为空，默认为上月
		String months = null ;
		if(jsonObject.get("month") != null) {
			months = jsonObject.getString("month") ;
		}else {
			months = TimeUtils.getLastTwoMonthsByNow() ;
		}
		
		Long merchantId = null ;
		if(jsonObject.get("merchantId") != null) {
			merchantId = jsonObject.getLong("merchantId") ;
		}
		
		String[] montharr = months.split(",");
		for(String month : montharr){
			/**根据条件查询唯品结算单*/
			Map<String, Object> queryMap = new HashMap<>() ;
			queryMap.put("month", month) ;
			queryMap.put("billCode", billCode) ;
			queryMap.put("merchantId", merchantId) ;
			
			List<String> billCodes = crawlerBillDao.getPlatformStatementCode(queryMap) ;
			
			if(billCodes.isEmpty()) {
				continue ;
			}
			
			for (String tempBillCode : billCodes) {
				
				PlatformStatementOrderModel platformStatementOrderModel = new PlatformStatementOrderModel() ;
				platformStatementOrderModel.setBillCode(tempBillCode);
				
				platformStatementOrderModel = platformStatementOrderDao.searchByModel(platformStatementOrderModel) ;
				
				//过滤已开票的结算单
				if(platformStatementOrderModel != null 
						&& DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_1.equals(platformStatementOrderModel.getIsInvoice())) {
					continue ;
				}
				
				//总金额
				Map<String,ReptileConfigMongo> reptileMap = new HashMap<String,ReptileConfigMongo>() ;
				
				List<PlatformStatementItemModel> saveList = new ArrayList<PlatformStatementItemModel>() ;
				
				Map<String, Object> itemQueryMap = new HashMap<>() ;
				itemQueryMap.put("billCode", tempBillCode) ;
				
				/**生成销售明细*/
				itemQueryMap.put("type", DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_1) ;
				List<CrawlerBillModel> salesList = crawlerBillDao.getPlatformStatementData(itemQueryMap) ;
				/**生成客退明细*/
				itemQueryMap.put("type", DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_2) ;
				List<CrawlerBillModel> returnList = crawlerBillDao.getPlatformStatementData(itemQueryMap) ;
				/**生成国检明细*/
				itemQueryMap.put("type", DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_3) ;
				List<CrawlerBillModel> gjList = crawlerBillDao.getPlatformStatementData(itemQueryMap) ;
				/**生成盘亏明细*/
				itemQueryMap.put("type", DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_4) ;
				List<CrawlerBillModel> pkList = crawlerBillDao.getPlatformStatementData(itemQueryMap) ;
				/**生成报废明细*/
				itemQueryMap.put("type", DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_5) ;
				List<CrawlerBillModel> bfList = crawlerBillDao.getPlatformStatementData(itemQueryMap) ;
				/**生成盘盈明细*/
				itemQueryMap.put("type", DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_6) ;
				List<CrawlerBillModel> pyList = crawlerBillDao.getPlatformStatementData(itemQueryMap) ;
				
				/**生成客退补贴明细*/
				itemQueryMap.put("type", DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_7) ;
				List<CrawlerVipExtraDataModel> ktbtList = crawlerVipExtraDataDao.getPlatformStatementData(itemQueryMap) ;
				/**生成活动折扣明细*/
				itemQueryMap.put("type", DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_8) ;
				List<CrawlerVipExtraDataModel> hdzkList = crawlerVipExtraDataDao.getPlatformStatementData(itemQueryMap) ;
				/**生成补偿折扣明细*/
				itemQueryMap.put("type", DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_9) ;
				List<CrawlerVipExtraDataModel> bckkList = crawlerVipExtraDataDao.getPlatformStatementData(itemQueryMap) ;
				
				savePlatformStatementItemList(salesList, reptileMap, saveList, DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_1);
				savePlatformStatementItemList(returnList, reptileMap, saveList, DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_2);
				savePlatformStatementItemList(gjList, reptileMap, saveList, DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_3);
				savePlatformStatementItemList(pkList, reptileMap, saveList, DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_4);
				savePlatformStatementItemList(bfList, reptileMap, saveList, DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_5);
				savePlatformStatementItemList(pyList, reptileMap, saveList, DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_6);
				
				savePlatformStatementItemExtraList(ktbtList, reptileMap, saveList, DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_7);
				savePlatformStatementItemExtraList(hdzkList, reptileMap, saveList, DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_8);
				savePlatformStatementItemExtraList(bckkList, reptileMap, saveList, DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_9);
			
				ktbtList.addAll(hdzkList) ;
				ktbtList.addAll(bckkList) ;
				
				Map<String, Object> amountAndCurrencyMap = new HashMap<String, Object>() ;
				amountAndCurrencyMap.put("billCode", tempBillCode) ;
				
				CrawlerBillModel sumModel = crawlerBillDao.getPlatformStatementAmountAndCurrency(amountAndCurrencyMap) ;
				//CrawlerVipExtraDataModel sumExtraModel = crawlerVipExtraDataDao.getPlatformStatementAmountAndCurrency(amountAndCurrencyMap) ;
				
				BigDecimal amount = new BigDecimal(0) ;
				String currency = null ;
				ReptileConfigMongo reptileConfigMongo = null ;
				
				if(sumModel != null) {
					amount = amount.add(sumModel.getBillAmount()) ;
					currency = sumModel.getCurrencyCode() ;
					reptileConfigMongo = reptileMap.get(sumModel.getUserCode()) ;
				}
				
				if(!ktbtList.isEmpty()) {
					
					for (CrawlerVipExtraDataModel tempModel : ktbtList) {
						amount = amount.add(tempModel.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)) ;
					}
					
				}
				
				Long orderId = null ;
				if(platformStatementOrderModel == null) {
					
					platformStatementOrderModel = new PlatformStatementOrderModel() ;
					platformStatementOrderModel.setBillAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
					platformStatementOrderModel.setBillCode(tempBillCode);
					platformStatementOrderModel.setCurrency(currency);
					platformStatementOrderModel.setCustomerType(DERP_ORDER.PLATFORM_STATEMENT_CUSTOMER_TYPE_2);
					platformStatementOrderModel.setIsInvoice(DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_0);
					platformStatementOrderModel.setIsCreateReceive(DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_0);
					platformStatementOrderModel.setMerchantId(reptileConfigMongo.getMerchantId());
					platformStatementOrderModel.setMerchantName(reptileConfigMongo.getMerchantName());
					platformStatementOrderModel.setMonth(month);
					platformStatementOrderModel.setCreateDate(TimeUtils.getNow());
					platformStatementOrderModel.setCustomerId(sumModel.getCustomerId());
					platformStatementOrderModel.setCustomerName(sumModel.getCustomerName());
					platformStatementOrderModel.setType(DERP_ORDER.PLATFORM_STATEMENT_TYPE_1);
					
					orderId = platformStatementOrderDao.save(platformStatementOrderModel);
					
				}else {
					
					platformStatementItemDao.deleteByOrderId(platformStatementOrderModel.getId()) ;
					
					platformStatementOrderModel.setBillAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
					platformStatementOrderModel.setModifyDate(TimeUtils.getNow());
					
					platformStatementOrderDao.modify(platformStatementOrderModel) ;
					
					orderId = platformStatementOrderModel.getId() ;
				}
				
				for (PlatformStatementItemModel saveItem : saveList) {
					saveItem.setPlatformStatementOrderId(orderId);
					
					platformStatementItemDao.save(saveItem) ;
				}
				
				/**生成附件信息*/
				CrawlerVipFileDataModel crawlerVipFileDataModel = new CrawlerVipFileDataModel() ;
				crawlerVipFileDataModel.setBillCode(tempBillCode);
				
				crawlerVipFileDataModel = crawlerVipFileDataDao.searchByModel(crawlerVipFileDataModel) ;
				
				if(crawlerVipFileDataModel == null) {
					continue ;
				}
				
				//获取远程文件信息
				String fileKey = crawlerVipFileDataModel.getFileKey();
				
				saveAttachment(fileKey, tempBillCode);
				
				/**生成附件信息*/
			}
		}
		
	}

	/**
	 * 唯品账单明细
	 * @param crawlerList
	 * @param reptileConfigMap
	 * @param saveList
	 * @param type
	 * @throws SQLException
	 */
	private void savePlatformStatementItemList(List<CrawlerBillModel> crawlerList, Map<String,ReptileConfigMongo> reptileConfigMap,
			List<PlatformStatementItemModel> saveList, String type) throws SQLException {
		for (CrawlerBillModel crawlerBillModel : crawlerList) {
			
			ReptileConfigMongo reptileConfig = reptileConfigMap.get(crawlerBillModel.getUserCode());
			
			if(reptileConfig == null) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("loginName", crawlerBillModel.getUserCode());
				List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
				if(reptileConfigList == null || reptileConfigList.size()==0){
					continue ;
				}
				
				reptileConfig = reptileConfigList.get(0);
				
				reptileConfigMap.put(crawlerBillModel.getUserCode(), reptileConfig) ;
			}
			
			//总金额
			BigDecimal billAmount = crawlerBillModel.getBillAmount();
			
			PlatformStatementItemModel item = new PlatformStatementItemModel() ;
			item.setType(type);
			item.setPoNo(crawlerBillModel.getPoNo());
			item.setBrandParent(crawlerBillModel.getBrandName());
			item.setBarcode(crawlerBillModel.getGoodsNo());
			item.setGoodsName(crawlerBillModel.getGoodsName());
			item.setCreateDate(TimeUtils.getNow());
			item.setSettlementNum(crawlerBillModel.getTotalSalesQty());
			item.setSettlementAmount(billAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
			
			saveList.add(item) ;
		}
	}
	
	/**
	 * 唯品折扣明细
	 * @param crawlerExtraList
	 * @param reptileConfigMap
	 * @param saveList
	 * @param type
	 * @throws SQLException
	 */
	private void savePlatformStatementItemExtraList(List<CrawlerVipExtraDataModel> crawlerExtraList, Map<String,ReptileConfigMongo> reptileConfigMap,
			List<PlatformStatementItemModel> saveList, String type) throws SQLException {
		for (CrawlerVipExtraDataModel crawlerVipExtraDataModel : crawlerExtraList) {
			
			ReptileConfigMongo reptileConfig = reptileConfigMap.get(crawlerVipExtraDataModel.getUserCode());
			
			if(reptileConfig == null) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("loginName", crawlerVipExtraDataModel.getUserCode());
				List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(params);
				if(reptileConfigList == null || reptileConfigList.size()==0){
					continue ;
				}
				
				reptileConfig = reptileConfigList.get(0);
				
				reptileConfigMap.put(crawlerVipExtraDataModel.getUserCode(), reptileConfig) ;
			}
			
			//总金额
			BigDecimal amount = crawlerVipExtraDataModel.getAmount();
				
			PlatformStatementItemModel item = new PlatformStatementItemModel() ;
			item.setType(type);
			item.setPoNo(crawlerVipExtraDataModel.getPoNo());
			item.setBrandParent(crawlerVipExtraDataModel.getBrandName());
			item.setBarcode(crawlerVipExtraDataModel.getGoodsNo());
			item.setCreateDate(TimeUtils.getNow());
			item.setSettlementNum(crawlerVipExtraDataModel.getQuantity());
			item.setSettlementAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
			item.setGoodsName(crawlerVipExtraDataModel.getGoodsName());
			
			/**若商品名为空，以SKU维度去爬虫表*/
			if(crawlerVipExtraDataModel.getGoodsName() == null) {
				Map<String, Object> queryGoodsName = new HashMap<>() ;
				queryGoodsName.put("goodsNo", crawlerVipExtraDataModel.getGoodsNo()) ;
				
				String goodsName = crawlerBillDao.getLastGoodsNameBySku(queryGoodsName) ;
				
				item.setGoodsName(goodsName);
			}
			
			saveList.add(item) ;
			
		}
	}
	
	private void saveAttachment(String fileKey, String tempBillCode) throws Exception {
		
		if(StringUtils.isBlank(fileKey)) {
			return ;
		}
		
		URL url = new URL(fileKey);
		URLConnection conn = url.openConnection();
		long fileSize = conn.getContentLengthLong() ;
		
		String ext = fileKey.substring(fileKey.lastIndexOf(".") + 1) ;
		String fileName = tempBillCode + "." + ext;
		
		Map<String, Object> attQueryMap = new HashMap<>() ;
		attQueryMap.put("relationCode", tempBillCode) ;
		
		AttachmentInfoMongo attachment = attachmentInfoMongoDao.findOne(attQueryMap);
		
		if(attachment != null) {
			attachmentInfoMongoDao.remove(attQueryMap);
		}
		
		AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo() ;
        attachmentInfoMongo.setAttachmentName(fileName); 		//附件名
        attachmentInfoMongo.setAttachmentSize(fileSize); 		//附件大小
        attachmentInfoMongo.setAttachmentType(ext);		       	//附件类型
        attachmentInfoMongo.setCreatorName("账单同步");
        attachmentInfoMongo.setCreateDate(new Date());
        attachmentInfoMongo.setRelationCode(tempBillCode);              //关联订单编码
        attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
        attachmentInfoMongo.setAttachmentUrl(fileKey);              //设置Url
        attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
        attachmentInfoMongo.setModule("平台结算单");
        
        attachmentInfoMongoDao.insert(attachmentInfoMongo);
	}

	@Override
	@SystemServiceLog(point=DERP_LOG_POINT.POINT_13201169502,model=DERP_LOG_POINT.POINT_13201169502_Label)
	public void saveTMPlatStatementOrder(String json, String keys, String topics, String tags) throws Exception {
		
		JSONObject jsonObject = JSONObject.fromObject(json);
		
		//平台结算单号
		String billCode = null ;
		if(jsonObject.get("billCode") != null) {
			billCode = jsonObject.getString("billCode") ;
		}
		
		//月份 若为空，默认为上月和本月
		String months = null ;
		if(jsonObject.get("month") != null) {
			months = jsonObject.getString("month") ;
		}else {
			months = TimeUtils.getLastTwoMonthsByNow() ;
		}
		
		Long merchantId = null ;
		if(jsonObject.get("merchantId") != null) {
			merchantId = jsonObject.getLong("merchantId") ;
		}
		
		/**根据天猫主数据ID*/
		Map<String, Object> customerMap = new HashMap<>() ;
		customerMap.put("mainId", "1000002643") ;
		customerMap.put("cusType", DERP_SYS.CUSTOMERINFO_CUSTYPE_1) ;
		
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerMap);
		
		String[] montharr = months.split(",");
		for (String month : montharr) {
			/**根据条件查询云集结算单*/
			Map<String, Object> queryMap = new HashMap<>() ;
			queryMap.put("month", month) ;
			queryMap.put("settleNo", billCode) ;
			queryMap.put("merchantId", merchantId) ;
			
			List<AlipayDailySettlebatchModel> batchList = alipayDailySettlebatchDao.getPlatformStatementData(queryMap) ;
			
			for (AlipayDailySettlebatchModel batchModel : batchList) {
				
				PlatformStatementOrderModel platformStatementOrderModel = new PlatformStatementOrderModel() ;
				platformStatementOrderModel.setBillCode(batchModel.getSettleNo());
				platformStatementOrderModel.setCurrency(batchModel.getCurrency());
				
				platformStatementOrderModel = platformStatementOrderDao.searchByModel(platformStatementOrderModel) ;
				
				//过滤已创建应收的结算单
				if(platformStatementOrderModel != null 
						&& DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_1.equals(platformStatementOrderModel.getIsCreateReceive())) {
					continue ;
				}
				
				BigDecimal totalAmount = new BigDecimal(0) ;
				List<PlatformStatementItemModel> saveList = new ArrayList<PlatformStatementItemModel>() ;
				
				PlatformSettlementConfigModel queryConfigModel = new PlatformSettlementConfigModel() ;
				
				queryConfigModel.setStorePlatformCode(DERP.STOREPLATFORM_1000000310);
				List<PlatformSettlementConfigModel> configList = platformSettlementConfigDao.list(queryConfigModel);
				
				/**获取结算明细*/
				AlipayDailySettleModel querySettleModel = new AlipayDailySettleModel() ;
				querySettleModel.setCurrency(batchModel.getCurrency());
				querySettleModel.setSettleNo(batchModel.getSettleNo());
				
				List<AlipayDailySettleModel> settleList = alipayDailySettleDao.list(querySettleModel);
				
				for (AlipayDailySettleModel alipayDailySettleModel : settleList) {
					
					/**
					 * 对于该表字段“类型”为settle时，直取对应电商订单的结算金额（RMB）
					 */
					PlatformStatementItemModel saveItemModel = new PlatformStatementItemModel() ;
					
					saveItemModel.setCreateDate(TimeUtils.getNow());
					saveItemModel.setPoNo(alipayDailySettleModel.getPartnerTransactionId());
					saveItemModel.setRate(alipayDailySettleModel.getRate());
					saveItemModel.setSettlementAmount(alipayDailySettleModel.getAmount());
					saveItemModel.setSettlementAmountRmb(alipayDailySettleModel.getRmbAmount());
					saveItemModel.setType(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_10);
					
					/**取对应费项配置*/
					for (PlatformSettlementConfigModel config : configList) {
						
						if("订单实付".indexOf(config.getName()) > -1) {
							saveItemModel.setSettlementConfigId(config.getSettlementConfigId());
							saveItemModel.setSettlementConfigName(config.getSettlementConfigName());
							saveItemModel.setPlatformSettlementConfigId(config.getId());
							saveItemModel.setPlatformSettlementConfigName(config.getName());
							break ;
						}
						
					}
					
					if(saveItemModel.getSettlementConfigId() == null
							|| saveItemModel.getSettlementConfigName() == null) {
						throw new DerpException("结算单：" + batchModel.getSettleNo() + "币种：" + batchModel.getCurrency() + " 结算类型费项经分销平台配置未找到") ;
					}
					
					saveList.add(saveItemModel) ;
					
					totalAmount = totalAmount.add(alipayDailySettleModel.getAmount()) ;
					
				}
				
				/**获取费项明细*/
				AlipayDailyFeeModel queryFeeModel = new AlipayDailyFeeModel() ;
				queryFeeModel.setSettleNo(batchModel.getSettleNo());
				queryFeeModel.setCurrency(batchModel.getCurrency());
				
				List<AlipayDailyFeeModel> feeList = alipayDailyFeeDao.list(queryFeeModel);
				
				for (AlipayDailyFeeModel alipayDailyFeeModel : feeList) {
					
					/**
					 * 对于该表字段“类型”为fee时，取对应电商订单的结算金额（RMB）*（-1），例如原本爬虫数据为正数，
					 * 生成系统平台结算单则为负数；原本爬虫数据为负数，生成系统平台结算单则为正数
					 */
					
					BigDecimal amount = alipayDailyFeeModel.getAmount();
					BigDecimal rmbAmount = alipayDailyFeeModel.getRmbAmount();
					
					amount = amount.multiply(new BigDecimal(-1)) ;
					rmbAmount = rmbAmount.multiply(new BigDecimal(-1)) ;
					
					PlatformStatementItemModel saveItemModel = new PlatformStatementItemModel() ;
					
					saveItemModel.setCreateDate(TimeUtils.getNow());
					saveItemModel.setPoNo(alipayDailyFeeModel.getPartnerTransactionId());
					saveItemModel.setRate(alipayDailyFeeModel.getRate());
					saveItemModel.setSettlementAmount(amount);
					saveItemModel.setSettlementAmountRmb(rmbAmount);
					saveItemModel.setTmRemarks(alipayDailyFeeModel.getRemark());
					saveItemModel.setType(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_11);
					
					/**取对应费项配置*/
					for (PlatformSettlementConfigModel config : configList) {
						
						if(alipayDailyFeeModel.getRemark().indexOf(config.getName()) > -1) {
							saveItemModel.setSettlementConfigId(config.getSettlementConfigId());
							saveItemModel.setSettlementConfigName(config.getSettlementConfigName());
							saveItemModel.setPlatformSettlementConfigId(config.getId());
							saveItemModel.setPlatformSettlementConfigName(config.getName());
							break ;
						}
						
					}
					
					if(saveItemModel.getSettlementConfigId() == null
							|| saveItemModel.getSettlementConfigName() == null) {
						throw new DerpException("结算单：" + batchModel.getSettleNo() + "币种：" + batchModel.getCurrency() + " " +
								alipayDailyFeeModel.getRemark() + " 经分销平台配置未找到") ;
					}
					
					saveList.add(saveItemModel) ;
					
					totalAmount = totalAmount.add(amount) ;
					
				}
				
				Long orderId = null ;
				if(platformStatementOrderModel == null) {
					
					platformStatementOrderModel = new PlatformStatementOrderModel() ;
					platformStatementOrderModel.setBillAmount(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
					platformStatementOrderModel.setBillCode(batchModel.getSettleNo());
					platformStatementOrderModel.setCurrency(batchModel.getCurrency());
					platformStatementOrderModel.setCustomerType(DERP_ORDER.PLATFORM_STATEMENT_CUSTOMER_TYPE_3);
					platformStatementOrderModel.setIsInvoice(DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_0);
					platformStatementOrderModel.setIsCreateReceive(DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_0);
					platformStatementOrderModel.setMerchantId(batchModel.getMerchantId());
					platformStatementOrderModel.setMerchantName(batchModel.getMerchantName());
					platformStatementOrderModel.setMonth(month);
					platformStatementOrderModel.setBillDate(batchModel.getSettleDate());
					platformStatementOrderModel.setCreateDate(TimeUtils.getNow());
					platformStatementOrderModel.setCustomerId(customer.getCustomerid());
					platformStatementOrderModel.setCustomerName(customer.getName());
					platformStatementOrderModel.setType(DERP_ORDER.PLATFORM_STATEMENT_TYPE_2);
					
					String shopCode = "" ;
					
					/**
					 * 根据爬虫对应的账号查找 “爬虫数据信息” 找到对应的公司主体
					 *	1、宝信公司主体下的To C结算单中：
					 *	爬虫账号为：btw.pg1@buytheworld.com.cn 时，对应店铺为：宝信天猫国际店铺 ， 店铺编码为：100045422；
					 *	2、健太公司主体下的To C结算单中：
					 *	爬虫账号为：yayuan.xu@topideal.com时，对应店铺为：mondelez海外旗舰店 ， 店铺编码为：100046145；
					 *	3、卓烨公司主体下的To C结算单中：
					 *	（1）爬虫账号为：2890531468@qq.com时，对应店铺为：美赞臣卓烨海外专卖店 ， 店铺编码为：100046572；
					 *	（2）爬虫账号为：zhuoy@topideal.com.cn 时，对应店铺为：淘分销卓烨店铺，店铺编码为：100048121；
					 *	4、广旺公司主体下的To C结算单中：
					 *	（1）爬虫账号为：2365241623@qq.com时，对应店铺为：proplan冠能海外旗舰店 ， 店铺编码为：100047205；
					 *	（2）爬虫账号为：2921717158@qq.com时，对应店铺为：purina普瑞纳海外旗舰店 ， 店铺编码为：3542279457；
					 */
					
					switch (batchModel.getUserCode()) {
					case "btw.pg1@buytheworld.com.cn":
						shopCode = "100045422" ;
						break;
						
					case "yayuan.xu@topideal.com":
						shopCode = "100046145" ;
						break;
						
					case "2890531468@qq.com":
						shopCode = "100046572" ;
						break;
						
					case "zhuoy@topideal.com.cn":
						shopCode = "100048121" ;
						break;
						
					case "2365241623@qq.com":
						shopCode = "100047205" ;
						break;
						
					case "2921717158@qq.com":
						shopCode = "3542279457" ;
						break;

					default:
						break;
					}
					
					Map<String, Object> queryShopMap = new HashMap<>() ;
					queryShopMap.put("shopCode", shopCode) ;
					queryShopMap.put("merchantId", batchModel.getMerchantId()) ;
					queryShopMap.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1) ;
					
					MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(queryShopMap);
					
					if(shop != null) {
						platformStatementOrderModel.setShopCode(shop.getShopCode());
						platformStatementOrderModel.setShopName(shop.getShopName());
						platformStatementOrderModel.setBuId(shop.getBuId());
						platformStatementOrderModel.setBuName(shop.getBuName());
					}
					
					orderId = platformStatementOrderDao.save(platformStatementOrderModel);
					
				}else {
					
					platformStatementItemDao.deleteByOrderId(platformStatementOrderModel.getId()) ;
					
					platformStatementOrderModel.setBillAmount(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
					platformStatementOrderModel.setModifyDate(TimeUtils.getNow());
					
					platformStatementOrderDao.modify(platformStatementOrderModel) ;
					
					orderId = platformStatementOrderModel.getId() ;
				}
				
				for (PlatformStatementItemModel saveItem : saveList) {
					saveItem.setPlatformStatementOrderId(orderId);
					
					platformStatementItemDao.save(saveItem) ;
				}
				
				/**生成附件信息*/
				String fileKey = batchModel.getFileKey();
				
				if(!fileKey.contains("http")) {
					fileKey = FILE_PREFIX + fileKey ;
				}
				
				saveAttachment(fileKey, batchModel.getSettleNo() + "_" +batchModel.getCurrency());
			}
		}
	}
}
