package com.topideal.service.timer.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.PlatformPurchaseOrderDao;
import com.topideal.dao.sale.PlatformPurchaseOrderItemDao;
import com.topideal.dao.sale.SaleOrderDao;
import com.topideal.dao.sale.SaleShelfDao;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderItemModel;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderModel;
import com.topideal.mongo.dao.BusinessUnitMongoDao;
import com.topideal.mongo.dao.MerchandiseExternalWarehouseMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.BusinessUnitMongo;
import com.topideal.mongo.entity.MerchandiseExternalWarehouseMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.PlatformPurNotShalfMessageService;

import net.sf.json.JSONObject;

@Service
public class PlatformPurNotShalfMessageServiceImpl implements PlatformPurNotShalfMessageService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlatformPurNotShalfMessageServiceImpl.class);

	@Autowired
	private PlatformPurchaseOrderDao platformPurchaseOrderDao;
	@Autowired
	private SaleOrderDao saleOrderDao;	
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
	private PlatformPurchaseOrderItemDao platformPurchaseOrderItemDao;
	@Autowired
	private SaleShelfDao saleShelfDao;
	@Autowired
	private MerchandiseExternalWarehouseMongoDao merchandiseExternalWarehouseMongoDao;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	@Autowired
	private BusinessUnitMongoDao businessUnitMongoDao;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201140004,model=DERP_LOG_POINT.POINT_13201140004_Label)
	public void sendPurNotShalfMessage(String json, String keys, String topics, String tags) throws Exception {
				
		// 按商家+事业部 存储发送邮件的实体
		Map<String, ReminderEmailUserDTO> emailSendMap = new HashMap<>();
		List<Map<String,Object>> noShelfList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> saleShelfList = new ArrayList<Map<String,Object>>();		
		Map<String,List<Map<String,Object>>> noShelfMap = new HashMap<>();
		Map<String,List<Map<String,Object>>> saleShelfMap = new HashMap<>();
		
		Set<String> buIdAndMerchantIdList = new HashSet<String>();
		int startIndex=0;
		int pageSize=1000;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startIndex", startIndex);
		paramMap.put("pageSize", pageSize);
		//分页查询近两个月的平台采购单
		while (true) {
			paramMap.put("startIndex", startIndex);
			List<PlatformPurchaseOrderModel> purchaseOrderMonthList = platformPurchaseOrderDao.getPurchaseOrderMonth(paramMap);
			if (purchaseOrderMonthList==null||purchaseOrderMonthList.size()==0)break;
			startIndex=startIndex+pageSize;	
			//判断否已经存在销售订单并且是否上架	
			noShelfList.addAll(getOrderMap(purchaseOrderMonthList));			
			
			//判断已上架的销售单，是否有存在单价、客户、币种、数量与平台不一致的单据	
			saleShelfList.addAll(getSaleShelfMap(purchaseOrderMonthList));
			purchaseOrderMonthList=null;
		}
		
		noShelfMap = noShelfList.stream().collect(Collectors.groupingBy(noShelf -> noShelf.get("buId").toString() +"-"+noShelf.get("merchantId").toString()));
		saleShelfMap = saleShelfList.stream().collect(Collectors.groupingBy(saleShelf -> saleShelf.get("buId").toString() +"-"+saleShelf.get("merchantId").toString()));
		
		buIdAndMerchantIdList.addAll(noShelfMap.keySet());
		buIdAndMerchantIdList.addAll(saleShelfMap.keySet());
		
		for(String buIdAndMerchantId : buIdAndMerchantIdList) {
			Long buId = Long.valueOf(buIdAndMerchantId.split("-")[0]);
			Long merchantId = Long.valueOf(buIdAndMerchantId.split("-")[1]);
			
			// 根据商家id查询 查询商家表				
			Map<String, Object> merchantMap = new HashMap<String, Object>();
			merchantMap.put("merchantId", merchantId);
			MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
			
			// 根据事业部id查询 查询事业部
			Map<String, Object> businessMap = new HashMap<String, Object>() ;
			businessMap.put("businessUnitId", buId) ;
	        BusinessUnitMongo buMongo = businessUnitMongoDao.findOne(businessMap);		
			
			if(emailSendMap.containsKey(buIdAndMerchantId)) {
				ReminderEmailUserDTO reminderEmailUserRoot = emailSendMap.get(buIdAndMerchantId);
				JSONObject dataJson = new JSONObject();
				if(noShelfMap.containsKey(buIdAndMerchantId)) {
					dataJson.put("noShelfList", noShelfMap.get(buIdAndMerchantId));
				}
				if(saleShelfMap.containsKey(buIdAndMerchantId)) {
					dataJson.put("saleShelfList", saleShelfMap.get(buIdAndMerchantId));
				}
				reminderEmailUserRoot.setDataJson(dataJson);
				emailSendMap.put(buIdAndMerchantId, reminderEmailUserRoot);				
				
			}else {
				//封装邮件头
				ReminderEmailUserDTO reminderEmailUserRoot=new ReminderEmailUserDTO();
				reminderEmailUserRoot.setMerchantId(merchantId);
				reminderEmailUserRoot.setMerchantName(merchant == null ? "" : merchant.getName());
				reminderEmailUserRoot.setBuId(buId);
				reminderEmailUserRoot.setBuName(buMongo == null ? "" : buMongo.getName());
				reminderEmailUserRoot.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_7);
				String type = DERP_SYS.REMINDER_EMAILCONFIG_TYPE_3;
				String typeName = DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_AllTypeList, type);
				reminderEmailUserRoot.setTypeName(typeName);
				reminderEmailUserRoot.setType(type);
				
				JSONObject dataJson = new JSONObject();
				if(noShelfMap.containsKey(buIdAndMerchantId)) {
					dataJson.put("noShelfList", noShelfMap.get(buIdAndMerchantId));
				}
				if(saleShelfMap.containsKey(buIdAndMerchantId)) {
					dataJson.put("saleShelfList", saleShelfMap.get(buIdAndMerchantId));
				}
				reminderEmailUserRoot.setDataJson(dataJson);
				emailSendMap.put(buIdAndMerchantId, reminderEmailUserRoot);
			}			
		}
		
		
		//发送邮件mq
		if (emailSendMap==null||emailSendMap.size()==0)return;
		Set<String> keySet = emailSendMap.keySet();
		for (String bukey : keySet) {
			LOGGER.info("商家Id+事业部id的bukey:"+bukey);
			ReminderEmailUserDTO reminderEmailUserDTO = emailSendMap.get(bukey);			
			if (reminderEmailUserDTO.getDataJson() == null) continue;
			JSONObject jSONObject = JSONObject.fromObject(reminderEmailUserDTO);
			LOGGER.info("平台采购单未上架信息发送消息报文"+jSONObject);
			try {
				rocketMQProducer.send(jSONObject.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),MQErpEnum.SEND_REMINDER_MAIL.getTags());				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("平台采购单未上架信息发送消息异常");
			}
		}		
	}



	/**
	 * 判断否已经存在销售订单 没有上架的平台订单
	 * @param purchaseOrderMonthList
	 * @throws SQLException 
	 */
	private List<Map<String,Object>> getOrderMap(List<PlatformPurchaseOrderModel> purchaseOrderMonthList) throws SQLException {	
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		for (PlatformPurchaseOrderModel purchaseOrder : purchaseOrderMonthList) {
			Map<String, Object>param=new HashMap<>(); 
			Long merchantId = purchaseOrder.getMerchantId();			
			param.put("merchantId", merchantId);
			param.put("poNo", purchaseOrder.getPoNo());
			// 根据平台采购单 poNo+商家查询  已经生成销售订单 但是销售订单未上架的数据
			List<Map<String, Object>> notShaleSaleByPoNoList = saleOrderDao.getNotShaleSaleByPoNo(param);
			if (notShaleSaleByPoNoList==null||notShaleSaleByPoNoList.size()==0) continue;
			Map<String, Object> map = notShaleSaleByPoNoList.get(0);
			String merchantName = (String) map.get("merchant_name");
			String saleCode = (String) map.get("code");
			String customerName = (String) map.get("customer_name");
			Long customerId = (Long) map.get("customer_id");
			Long buId = (Long) map.get("bu_id");
			String buName = (String) map.get("bu_name");

			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("buId", buId);
			resultMap.put("buName", buName);
			resultMap.put("merchantId", merchantId);
			resultMap.put("merchantName", merchantName);
			resultMap.put("customer", customerName);
			resultMap.put("orderCode", saleCode);
			resultMap.put("poNo", purchaseOrder.getPoNo());
			resultMap.put("deliverDate", TimeUtils.formatFullTime(purchaseOrder.getDeliverDate()));
			//状态
			String platformStatus = purchaseOrder.getPlatformStatus();
			String platformStatusName = DERP_ORDER.getLabelByKey(DERP_ORDER.platformPurchase_platformStatusList, platformStatus); 
			resultMap.put("status", platformStatusName);
			
			dataList.add(resultMap);
	
		}
		return dataList;
		
	}
	/**
	 * 判断否已经存在销售订单 已上架的平台订单
	 * @param purchaseOrderMonthList
	 * @throws SQLException 
	 */
	private List<Map<String,Object>> getSaleShelfMap(List<PlatformPurchaseOrderModel> purchaseOrderMonthList) throws SQLException {	
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		for (PlatformPurchaseOrderModel purchaseOrder : purchaseOrderMonthList) {
			PlatformPurchaseOrderItemModel queryModel = new PlatformPurchaseOrderItemModel() ;
			queryModel.setOrderId(purchaseOrder.getId());
			List<PlatformPurchaseOrderItemModel> itemList = platformPurchaseOrderItemDao.list(queryModel);
			
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("merchantId", purchaseOrder.getMerchantId());
			queryMap.put("poNo", purchaseOrder.getPoNo());
			for (PlatformPurchaseOrderItemModel item : itemList) {
				List<String> errMsgList = new ArrayList<String>();
				Map<String, Object> saleShelfMap = null ;
				//若平台采购单存在转销条码，按转销条码匹配				
				if(StringUtils.isNotBlank(item.getSaleBarcode())) {
					queryMap.put("barcode", item.getSaleBarcode());
					saleShelfMap = saleShelfDao.getSaleShelfByPoNoAndGoodsNoAndBarcode(queryMap);					
				}else {
					//若平台采购单不存在转销条码，按平台采购单的条形码匹配
					queryMap.put("barcode", item.getPlatformBarcode());
					saleShelfMap = saleShelfDao.getSaleShelfByPoNoAndGoodsNoAndBarcode(queryMap);
					//若平台采购单不存在转销条码，按平台采购单的条形码匹配匹配不到销售单的SKU，则按平台采购单商品对应的经分销货号匹配
					if(saleShelfMap == null) {
						queryMap.remove("barcode");
						Map<String, Object> merchandiseExternalParams = new HashMap<String, Object>();
						merchandiseExternalParams.put("goodsNo", item.getPlatformGoodsNo());
						merchandiseExternalParams.put("merchantId", purchaseOrder.getMerchantId());
						List<MerchandiseExternalWarehouseMongo> externalMerchandiseList =  merchandiseExternalWarehouseMongoDao.findAll(merchandiseExternalParams);
						if (externalMerchandiseList != null && !externalMerchandiseList.isEmpty()) {
							MerchandiseExternalWarehouseMongo externalMerchandiseMongo = externalMerchandiseList.get(0);
							if(StringUtils.isNotBlank(externalMerchandiseMongo.getDerpGoodsNo())){
								queryMap.put("goodsNo", externalMerchandiseMongo.getDerpGoodsNo());
								saleShelfMap = saleShelfDao.getSaleShelfByPoNoAndGoodsNoAndBarcode(queryMap);
							}
						}
					}
				}
				
				BigDecimal salePrice = BigDecimal.ZERO;
				String saleCurrency = "";
				Long saleCustomerId = null;					
				BigDecimal totalShelfNum = BigDecimal.ZERO;
				if(saleShelfMap != null) {					
					salePrice = (BigDecimal) saleShelfMap.get("price");
					saleCurrency = (String) saleShelfMap.get("currency");
					saleCustomerId = (Long) saleShelfMap.get("customerId");					
					totalShelfNum = (BigDecimal) saleShelfMap.get("totalShelfNum");
					
					if(!purchaseOrder.getCurrency().equals(saleCurrency)) {//币种是否一致
						errMsgList.add("币种不一致");
					}
					if(!purchaseOrder.getCustomerId().equals(saleCustomerId)) {//客户是否一致
						errMsgList.add("客户不一致");				
					}
					if(item.getPrice().compareTo(salePrice) != 0) {//单价是否一致
						errMsgList.add("单价不一致");
					}
					if(totalShelfNum.compareTo(new BigDecimal(item.getReceiptOkNum())) != 0) {//上架数量是否一致
						errMsgList.add("上架数量不一致");
					}					
				}
				
				if(errMsgList.size() > 0) {
					String saleGoodsNo = (String) saleShelfMap.get("goodsNo");
					String saleBarcode = (String) saleShelfMap.get("barcode");
					String saleCustomerName = (String) saleShelfMap.get("customerName");
					String saleOrderCode = (String) saleShelfMap.get("saleOrderCode");
					Long saleBuId = (Long) saleShelfMap.get("buId");
					String saleBuName = (String) saleShelfMap.get("buName");
					String saleMerchantName = (String) saleShelfMap.get("merchantName");
					Long saleMerchantId = (Long) saleShelfMap.get("merchantId");
					
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("buId", saleBuId);
					resultMap.put("buName", saleBuName);
					resultMap.put("merchantId", saleMerchantId);
					resultMap.put("merchantName", saleMerchantName);
					resultMap.put("saleOrderCode", saleOrderCode);
					resultMap.put("poNo", purchaseOrder.getPoNo());
					resultMap.put("goodsNo", saleGoodsNo);
					resultMap.put("barcode", saleBarcode);
					resultMap.put("platformCustomer", purchaseOrder.getCustomerName());
					resultMap.put("platformCurrency", purchaseOrder.getCurrency());
					resultMap.put("platformPrice", item.getPrice());
					resultMap.put("platformNum", item.getReceiptOkNum());
					resultMap.put("saleCustomer", saleCustomerName);
					resultMap.put("saleCurrency", saleCurrency);
					resultMap.put("salePrice", salePrice);
					resultMap.put("saleShelfNum", totalShelfNum);
					resultMap.put("errMessage", StringUtils.join(errMsgList,"<br>"));
					dataList.add(resultMap);
				}
				
			}
	
		}
		return dataList;
	}

}
