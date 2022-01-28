package com.topideal.service.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExcelExportXlsx;
import com.topideal.dao.inventory.BuInventoryDao;
import com.topideal.dao.order.OrderDao;
import com.topideal.dao.order.OrderItemDao;
import com.topideal.dao.order.PurchaseOrderDao;
import com.topideal.dao.order.PurchaseWarehouseDao;
import com.topideal.dao.system.ExchangeRateDao;
import com.topideal.entity.dto.PurchaseOrderDTO;
import com.topideal.entity.vo.inventory.BuInventoryModel;
import com.topideal.entity.vo.order.OrderModel;
import com.topideal.entity.vo.order.PurchaseWarehouseModel;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.service.AbnormalMonitoringService;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AbnormalMonitoringServiceImpl implements AbnormalMonitoringService{

	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(AbnormalMonitoringServiceImpl.class);

	@Autowired
	private BuInventoryDao buInventoryDao ;
	@Autowired
	private OrderDao orderDao ;
	@Autowired
	private OrderItemDao orderItemDao ;
	@Autowired
	private ExchangeRateDao exchangeRateDao ;
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao ;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;

	@Override
	public void sendMonitoringMail(String json, String keys, String topics, String tags) throws Exception {

		List<Map<String, Object>> negativeNumList = new ArrayList<>() ;
		List<PurchaseWarehouseModel> unCorrelateList = new ArrayList<>() ;
//		List<Map<String, Object>> dtoList = new ArrayList<>() ;
		List<Map<String, Object>> purchaseList = new ArrayList<>() ;

		boolean isNotExsitRateFlag = false ;

		/**获取当月*/
		String month = TimeUtils.format(new Date(), "yyyy-MM");

		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("month", month) ;

		/**1.库存为负检查*/
		List<BuInventoryModel> negativeNumTempList = buInventoryDao.getNegativeNumList(queryMap) ;

		if(!negativeNumTempList.isEmpty()) {
			for (BuInventoryModel buInitInventoryModel : negativeNumTempList) {
				Map<String, Object> tempMap = new HashMap<>() ;

				tempMap.put("merchantName", buInitInventoryModel.getMerchantName()) ;
				tempMap.put("buName", buInitInventoryModel.getBuName() == null ? "" : buInitInventoryModel.getBuName()) ;
				tempMap.put("depotName", buInitInventoryModel.getDepotName()) ;
				tempMap.put("goodsNo", buInitInventoryModel.getGoodsNo()) ;
				tempMap.put("surplusNum", buInitInventoryModel.getSurplusNum()) ;
				tempMap.put("okNum", buInitInventoryModel.getOkNum()) ;
				tempMap.put("wornNum", buInitInventoryModel.getWornNum()) ;
				tempMap.put("month", buInitInventoryModel.getMonth()) ;
				tempMap.put("createDate", TimeUtils.format(buInitInventoryModel.getCreateDate(), "yyyy-MM-dd")) ;

				negativeNumList.add(tempMap) ;
			}
		}


		/**2.查询各公司下采购入库单是否存在“勾稽状态”为未勾稽的入库单*/
//		unCorrelateList = purchaseWarehouseDao.getUnCorrelateList() ;

//		for (PurchaseWarehouseModel warehouseModel : unCorrelateList) {
//			Map<String, Object> dto = new HashMap<>() ;

//			dto.put("buName", warehouseModel.getBuName() == null ? "" : warehouseModel.getBuName());
//			dto.put("merchantName", warehouseModel.getMerchantName());
//			dto.put("depotName", warehouseModel.getDepotName());
//			dto.put("code", warehouseModel.getCode());
//			dto.put("correlationStatus", DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseWarehouse_correlationStatusList, warehouseModel.getCorrelationStatus()));
//			dto.put("inboundDate",warehouseModel.getInboundDate() != null ? TimeUtils.format(warehouseModel.getInboundDate(), "yyyy-MM-dd") : "");
//			dto.put("state", DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseWarehouse_stateList, warehouseModel.getState()));

//			StringBuffer codeSb = new StringBuffer() ;

//			Map<String, Object> queryWarehouseMap = new HashMap<>() ;
//			queryWarehouseMap.put("warehouseCode", warehouseModel.getCode()) ;
//			String purchaseIds = purchaseWarehouseDao.getPurchaseOrderIdByWarehouse(queryWarehouseMap);

//			String[] ids = {} ;
//			if (StringUtils.isNotBlank(purchaseIds)) {
//				ids = purchaseIds.split(",");
//			}

//			for (String id : ids) {
//				PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(Long.valueOf(id));
//
//				if (codeSb.length() > 0) {
//					codeSb.append(",") ;
//				}
//
//				codeSb.append(purchaseOrder.getCode()) ;
// 			}
//
//			dto.put("purchaseOrderCode", codeSb.toString());

//			dtoList.add(dto) ;
//		}

		/**4.查询汇率当天的即期汇率、平均汇率是否为空，有存在有一个兑换币种*/
		String today = TimeUtils.format(new Date(), "yyyy-MM-dd");
		ExchangeRateModel queryRateModel = new ExchangeRateModel() ;
		queryRateModel.setEffectiveDate(TimeUtils.parseDay(today));
		List<ExchangeRateModel> rateList = exchangeRateDao.list(queryRateModel);

		if(rateList.isEmpty()) {
			isNotExsitRateFlag = true ;
		}else {
			rateList = exchangeRateDao.getNullRateList() ;

			if(!rateList.isEmpty()) {
				isNotExsitRateFlag = true ;
			}
		}

		/**5.查询近两个月采购订单本位币金额为空*/
		List<PurchaseOrderDTO> purchaseOrderList = purchaseOrderDao.getNoTgtAmountOrder() ;
		for (PurchaseOrderDTO purchaseOrderDTO : purchaseOrderList) {

			if("toipidealtest".equals(purchaseOrderDTO.getTopidealCode())) {
				continue ;
			}

			Map<String, Object> dto = new HashMap<>() ;

			dto.put("merchantName", purchaseOrderDTO.getMerchantName());
			dto.put("inboundDate", TimeUtils.format(purchaseOrderDTO.getInboundDate(), "yyyy-MM-dd"));
			dto.put("code", purchaseOrderDTO.getCode());
			dto.put("currency", purchaseOrderDTO.getCurrency());

			purchaseList.add(dto) ;
		}


		/**3.查询前天出库的电商订单的“实付总额”是否等于“结算金额+分摊运费+商品税费”合计值*/
		List<OrderModel> currentMonthList = orderDao.getCurrentMonthOdepotList() ;

		List<Map<String, Object>> excelList = new ArrayList<Map<String, Object>>() ;

		for (OrderModel orderModel : currentMonthList) {

			Map<String, Object> sumMap = orderItemDao.sumDiscountAndDecTotalByOrderID(orderModel.getId()) ;

			if(sumMap == null) {
				continue ;
			}

			// 结算金额+分摊运费+商品税费金额
			BigDecimal differentNum = judgeIsNullOrNotReturnObj(sumMap.get("differentNum"), BigDecimal.class);

			if(differentNum == null) {
				differentNum = new BigDecimal(0) ;
			}

			if(differentNum.compareTo(orderModel.getPayment()) != 0) {

				Map<String, Object> excel = new HashMap<String, Object>() ;

				BigDecimal decTotal = judgeIsNullOrNotReturnObj(sumMap.get("decTotal"), BigDecimal.class);
				BigDecimal wayFrtFee = judgeIsNullOrNotReturnObj(sumMap.get("wayFrtFee"), BigDecimal.class);
				BigDecimal tax = judgeIsNullOrNotReturnObj(sumMap.get("tax"), BigDecimal.class);

				excel.put("merchantName", orderModel.getMerchantName());
				excel.put("externalCode", orderModel.getExternalCode());
				excel.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, orderModel.getStorePlatformName())) ;
				excel.put("decTotal", decTotal) ;
				excel.put("tax", tax) ;
				excel.put("wayFrtFee", wayFrtFee) ;

				excelList.add(excel) ;
			}
		}

		String fileUrl = "" ;
		if(excelList.size() > 0) {

			String[] columns = {"公司", "外部订单号", "平台", "商品结算金额合计", "商品税费合计", "分摊运费合计"} ;
			String[] colskeys = {"merchantName", "externalCode", "storePlatformName", "decTotal", "tax", "wayFrtFee"} ;

			SXSSFWorkbook wb = ExcelExportXlsx.createSXSSExcel("电商订单预警", columns, colskeys, excelList);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				wb.write(bos);
			} finally {
			    bos.close();
			}

			byte[] bytes = bos.toByteArray();

			JSONObject jsonObject = new JSONObject();
	        jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
	        jsonObject.put("fileName", "电商订单预警");
	        jsonObject.put("fileBytes", Base64.encodeBase64String(bytes));
	        jsonObject.put("fileExt","xlsx");
	        jsonObject.put("fileSize",String.valueOf(bytes.length));
	        String resultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

	        JSONObject jsonObj = JSONObject.fromObject(resultJson);

	        logger.info("蓝精灵上传附件返回结果：" + jsonObj);

	        if(jsonObj.getInt("code") == 200) {
	        	fileUrl = jsonObj.getString("url") ;
	        }

		}

		JSONObject jsonObject = new JSONObject();//推送内容
		JSONObject paramJson=new JSONObject();//存储所有数据的结果

		paramJson.put("today", today) ;
		paramJson.put("buInventoryList", negativeNumList) ;
//		paramJson.put("purchaseWarehouseList", dtoList) ;
		paramJson.put("fileUrl", fileUrl) ;
		paramJson.put("purchaseList", purchaseList) ;
		paramJson.put("isNotExsitRateFlag", isNotExsitRateFlag) ;

		jsonObject.put("recipients", ApolloUtil.smurfsRecipients);
		jsonObject.put("paramJson",paramJson);
        jsonObject.put("mailCode",SmurfsAPICodeEnum.EMAIL_M024.getCode());

        //调用外部接口发送邮件
        String resultMsg=SmurfsUtils.send(jsonObject,SmurfsAPIEnum.SMURFS_EMAIL);
        logger.info("蓝精灵发邮件返回结果：" + resultMsg);

	}

	@SuppressWarnings("unchecked")
	private <T>T judgeIsNullOrNotReturnObj(Object obj , Class<T> clazz){
       	if(obj == null) {
       		return null ;
       	}

       	return (T)obj ;
    }

}
