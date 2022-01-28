package com.topideal.service.impl;

import com.supplychain.gateway.api.GatewayResponse;
import com.topideal.api.dstp.DSTPUtil;
import com.topideal.api.dstp.d01.GoodsDstp;
import com.topideal.api.dstp.d02.PurchaseGoods;
import com.topideal.api.dstp.d02.PurchaseOrderDstp;
import com.topideal.api.dstp.d03.SaleOrderToBDstp;
import com.topideal.api.dstp.d03.SaleOrderToBGoods;
import com.topideal.api.dstp.d04.SaleOrderToC;
import com.topideal.api.dstp.d04.SaleOrderToCBatchDstp;
import com.topideal.api.dstp.d04.SaleOrderToCGoods;
import com.topideal.api.dstp.d04.SaleOrderToCRecipient;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.OrderDao;
import com.topideal.dao.order.PurchaseOrderDao;
import com.topideal.dao.order.SaleOrderDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.service.DSTPSynOrderService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

@Service
public class DSTPSynOrderServiceImpl implements DSTPSynOrderService {
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	@Autowired
	private SaleOrderDao saleOrderDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private MerchantInfoDao merchantInfoDao ;
	
	/* 打印日志 */
    private static final Logger logger = LoggerFactory.getLogger(DSTPSynOrderServiceImpl.class);
    

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_20600000003, model = DERP_LOG_POINT.POINT_20600000003_Label)
	public void synOrderDSTP(String json, String keys, String topics, String tags) throws Exception{
		JSONObject jsonData = JSONObject.fromObject(json);
		Map<String, Object> jsonMap = jsonData;
		Integer merchantId = (Integer) jsonMap.get("merchantId");//商家id
		String orderCode = (String) jsonMap.get("orderCode");//采购单号
		String startTime = (String) jsonMap.get("startTime");//开始时间
		String endTime = (String) jsonMap.get("endTime");//结束时间
		String orderType = (String) jsonMap.get("orderType");//1-采购 2-销售TOB 3-销售TOB

		List<String> orderCodeList = null;
		if(StringUtils.isNotBlank(orderCode)) orderCodeList = Arrays.asList(StringUtils.split(orderCode, ","));

		/**1.计算同步时间 未指定时间则默认为昨天*/
		if(StringUtils.isBlank(startTime)||StringUtils.isBlank(endTime)){
			String yesterday = TimeUtils.getYesterday();
			startTime = yesterday+" 00:00:00";
			endTime = yesterday+" 23:59:59";
		}

         /**查询公司 只同步宝信、健太、润佰、卓烨*/
		MerchantInfoModel queryModel = new MerchantInfoModel();
		if(merchantId!=null){
			queryModel.setId(Long.valueOf(merchantId));
		}
		List<MerchantInfoModel> merchantList = merchantInfoDao.listDstp(queryModel);
		for(MerchantInfoModel merchant : merchantList){

           if(StringUtils.isBlank(orderType)||orderType.equals("1")){//1-采购
			   synPurchaseOrder(merchant,orderCodeList,startTime,endTime);
		   }
           if(StringUtils.isBlank(orderType)||orderType.equals("2")){//1-销售TOB
			   synSaleTOBOrder(merchant,orderCodeList,startTime,endTime);
		   }
           if(StringUtils.isBlank(orderType)||orderType.equals("3")){
			   synOrderTOC(merchant,orderCodeList,startTime,endTime);
		   }

		}


	}
    /**
	 * 1.同步采购单到dstp
	 * */
	private void synPurchaseOrder(MerchantInfoModel merchant,List<String> orderCodeList,String startTime,String endTime){
		//分页查询昨天新增和修改的单号
		Integer startIndex = 0;
		Integer pageSize = 100;
		while(true){
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("merchantId",merchant.getId());
			paramMap.put("startIndex",startIndex);
			paramMap.put("pageSize",pageSize);
			if(orderCodeList!=null&&orderCodeList.size()>0){
				paramMap.put("orderCodeList",orderCodeList);
			}else{
				paramMap.put("startTime",startTime);
				paramMap.put("endTime",endTime);
			}

			//查询有变更的单号
			List<String> codeList = purchaseOrderDao.getPurchaseCodeList(paramMap);
            if(codeList==null||codeList.size()<=0){
				logger.info("新增/修改的订单数量为0，结束");
				break;
			}
            //查询表头
			Map<String,Object> queryMap = new HashMap<>();
			queryMap.put("codeList",codeList);
			List<Map<String,Object>> orderList = purchaseOrderDao.getPurchaseByCodeList(queryMap);
            //查询表体
			List<Map<String,Object>> orderItemList = purchaseOrderDao.getPurchaseItemByCodeList(queryMap);

			//封装实体
			List<PurchaseOrderDstp> orderEntityList = createPurchaseEntity(merchant,orderList,orderItemList);
			//推送商品到DSTP
			for(PurchaseOrderDstp purchaseOrderDstp:orderEntityList){
				JSONObject jsonObj = JSONObject.fromObject(purchaseOrderDstp);
				GatewayResponse response = DSTPUtil.sendPurchaseDSTP(jsonObj.toString(),merchant.getTopidealCode());
				if(response==null||!response.getFlag().equals("success")){
					logger.info("采购订单同步DSTP失败,merchantId="+merchant.getId()+","+startTime+"-"+endTime+" 单号="+purchaseOrderDstp.getEntOrderNo()+" DSTP:"+response.getMessage());
					throw new RuntimeException("采购订单同步DSTP失败,merchantId="+merchant.getId()+","+startTime+"-"+endTime+" 单号="+purchaseOrderDstp.getEntOrderNo()+" DSTP:"+response.getMessage());
				}
			}
			startIndex += pageSize;
		}
	}
	/**封装采购报文
	 * */
	private List<PurchaseOrderDstp> createPurchaseEntity(MerchantInfoModel merchant, List<Map<String,Object>> orderList, List<Map<String,Object>> orderItemList){
		List<PurchaseOrderDstp> orderEntityList = new ArrayList<>();
		//按采购单号分组 并累计数量金额
		Map<String,List<PurchaseGoods>> orderItemMap = new HashMap<>();//存储表体
		Map<String,Integer> orderNumMap = new HashMap<>();//存储累计数量
		Map<String,BigDecimal> orderAmountMap = new HashMap<>();//存储累计金额
		for(Map<String,Object> itemMap:orderItemList){
			String code = (String)itemMap.get("code");
			Integer num  = (Integer)itemMap.get("num");
			BigDecimal amount = (BigDecimal)itemMap.get("amount");
			amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

			PurchaseGoods goods = new PurchaseGoods();
			goods.setGoodsName(String.valueOf(itemMap.get("goods_name")));
			goods.setBarCode(String.valueOf(itemMap.get("barcode")));
			goods.setQty(num);
			List<PurchaseGoods> goodsList = orderItemMap.get(code);
            if(goodsList==null) goodsList = new ArrayList<>();
			goodsList.add(goods);
			orderItemMap.put(code,goodsList);

			//累计数量
			Integer totalNum = orderNumMap.get(code);
			if(totalNum==null) totalNum = 0;
			totalNum += num;
			orderNumMap.put(code,totalNum);

			//累计金额
			BigDecimal totalAmout = orderAmountMap.get(code);
			if(totalAmout==null) totalAmout = new BigDecimal(0);
			totalAmout = totalAmout.add(amount);
			orderAmountMap.put(code,totalAmout);
		}

		//封装实体
		for(Map<String,Object> orderMap:orderList){
			String code = String.valueOf(orderMap.get("code"));
			String status = String.valueOf(orderMap.get("status"));//状态 007-已入库  006-已删除
			String writeOffStatus = String.valueOf(orderMap.get("write_off_status"));//红冲状态 003-已红冲
			if(StringUtils.isBlank(writeOffStatus)) writeOffStatus = "";

			String OrderStatus = "";
			if(status.equals(DERP_ORDER.PURCHASEORDER_STATUS_007)){
				OrderStatus = "20";//已入库:20-已完成
			}else if(status.equals(DERP.DEL_CODE_006)||writeOffStatus.equals(DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_003)){
				OrderStatus = "30";//已删除、已红冲:30-已取消
			}

			PurchaseOrderDstp purchaseOrder = new PurchaseOrderDstp();
			purchaseOrder.setCustomerCode(merchant.getRegistrationCert());
			purchaseOrder.setEntOrderNo(code);
			if(orderMap.get("attribution_date")!=null){
				purchaseOrder.setOrderTime(String.valueOf(orderMap.get("attribution_date")));
			}else{
				purchaseOrder.setOrderTime(String.valueOf(orderMap.get("create_date")));
			}
			purchaseOrder.setContractNo(String.valueOf(orderMap.get("contract_no")));
			purchaseOrder.setPoOrderNo(String.valueOf(orderMap.get("po_no")));
			purchaseOrder.setFinancingOrderNo(String.valueOf(orderMap.get("financing_no")));
			purchaseOrder.setPurchaseQty(orderNumMap.get(code));
			purchaseOrder.setPurchaseAmount(String.valueOf(orderAmountMap.get(code)));
			purchaseOrder.setCurrency(String.valueOf(orderMap.get("currency")));
			purchaseOrder.setPayTime(String.valueOf(orderMap.get("pay_date")));
			purchaseOrder.setWmsOrderNo(String.valueOf(orderMap.get("wmsOrderNo")));
			purchaseOrder.setInboundTime(String.valueOf(orderMap.get("inbound_date")));
			purchaseOrder.setOrderStatus(OrderStatus);
			purchaseOrder.setLinKid(String.valueOf(orderMap.get("link_uniue_code")));
			purchaseOrder.setGoodsList(orderItemMap.get(code));
			orderEntityList.add(purchaseOrder);
		}

      return orderEntityList;
	}
    /**
	 * 2.同步销售TOB单到dstp
	 * */
	private void synSaleTOBOrder(MerchantInfoModel merchant,List<String> orderCodeList,String startTime,String endTime){
		//分页查询昨天新增和修改的单号
		Integer startIndex = 0;
		Integer pageSize = 100;
		while(true){
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("merchantId",merchant.getId());
			paramMap.put("startIndex",startIndex);
			paramMap.put("pageSize",pageSize);
			if(orderCodeList!=null&&orderCodeList.size()>0){
				paramMap.put("orderCodeList",orderCodeList);
			}else{
				paramMap.put("startTime",startTime);
				paramMap.put("endTime",endTime);
			}

			//查询有变更的单号
			List<String> codeList = saleOrderDao.getSaleCodeList(paramMap);
			if(codeList==null||codeList.size()<=0){
				logger.info("新增/修改的订单数量为0，结束");
				break;
			}
			//查询表头
			Map<String,Object> queryMap = new HashMap<>();
			queryMap.put("codeList",codeList);
			List<Map<String,Object>> orderList = saleOrderDao.getSaleByCodeList(queryMap);
			//查询表体
			List<Map<String,Object>> orderItemList = saleOrderDao.getSaleItemByCodeList(queryMap);

			//封装实体
			List<SaleOrderToBDstp> orderEntityList = createSaleEntity(merchant,orderList,orderItemList);
			//推送商品到DSTP
			for(SaleOrderToBDstp saleOrderToBDstp:orderEntityList){
				JSONObject jsonObj = JSONObject.fromObject(saleOrderToBDstp);
				GatewayResponse response = DSTPUtil.sendSaleDSTP(jsonObj.toString(),merchant.getTopidealCode());
				if(response==null||!response.getFlag().equals("success")){
					logger.info("销售订单同步DSTP失败,merchantId="+merchant.getId()+","+startTime+"-"+endTime+" 单号="+saleOrderToBDstp.getEntOrderNo()+" DSTP:"+response.getMessage());
					throw new RuntimeException("销售订单同步DSTP失败,merchantId="+merchant.getId()+","+startTime+"-"+endTime+" 单号="+saleOrderToBDstp.getEntOrderNo()+" DSTP:"+response.getMessage());
				}
			}
			startIndex += pageSize;
		}
	}
	/**封装销售报文
	 * */
	private List<SaleOrderToBDstp> createSaleEntity(MerchantInfoModel merchant, List<Map<String,Object>> orderList, List<Map<String,Object>> orderItemList){
		List<SaleOrderToBDstp> orderEntityList = new ArrayList<>();
		//按销售单号分组 并累计数量金额
		Map<String,List<SaleOrderToBGoods>> orderItemMap = new HashMap<>();//存储表体
		Map<String,Integer> orderNumMap = new HashMap<>();//存储累计数量
		Map<String,BigDecimal> orderAmountMap = new HashMap<>();//存储累计金额
		for(Map<String,Object> itemMap:orderItemList){
			String code = String.valueOf(itemMap.get("code"));
			Integer num = (Integer)itemMap.get("num");
			BigDecimal amount = (BigDecimal)itemMap.get("amount");
			amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);

			SaleOrderToBGoods goods = new SaleOrderToBGoods();
			goods.setGoodsName(String.valueOf(itemMap.get("goods_name")));
			goods.setBarCode(String.valueOf(itemMap.get("barcode")));
			goods.setQty(num);
			List<SaleOrderToBGoods> goodsList = orderItemMap.get(code);
			if(goodsList==null) goodsList = new ArrayList<>();
			goodsList.add(goods);
			orderItemMap.put(code,goodsList);

			//累计数量
			Integer totalNum = orderNumMap.get(code);
			if(totalNum==null) totalNum = 0;
			totalNum += num;
			orderNumMap.put(code,totalNum);

			//累计金额
			BigDecimal totalAmout = orderAmountMap.get(code);
			if(totalAmout==null) totalAmout = new BigDecimal(0);
			totalAmout = totalAmout.add(amount);
			orderAmountMap.put(code,totalAmout);
		}

		//封装实体
		for(Map<String,Object> orderMap:orderList){
			String code = String.valueOf(orderMap.get("code"));
			String state = String.valueOf(orderMap.get("state"));//状态 018-已入库 026-已上架  006-已删除
			String writeOffStatus = String.valueOf(orderMap.get("write_off_status"));//红冲状态 003-已红冲
			if(StringUtils.isBlank(writeOffStatus)) writeOffStatus = "";

			String OrderStatus = "";
			if(state.equals(DERP_ORDER.SALEORDER_STATE_018)){
				OrderStatus = "10";//已出库:10-执行中
			}else if(state.equals(DERP_ORDER.SALEORDER_STATE_026)){
				OrderStatus = "20";//已上架:20-已完成
			}else if(state.equals(DERP.DEL_CODE_006)||writeOffStatus.equals(DERP_ORDER.SALEORDER_WRITEOFF_STATUS_003)){
				OrderStatus = "30";//已删除、已红冲:30-已取消
			}

			SaleOrderToBDstp saleOrder = new SaleOrderToBDstp();
			saleOrder.setCustomerCode(merchant.getRegistrationCert());
			saleOrder.setEntOrderNo(code);
			if(orderMap.get("audit_date")!=null){
				saleOrder.setOrderTime(String.valueOf(orderMap.get("audit_date")));
			}else{
				saleOrder.setOrderTime(String.valueOf(orderMap.get("create_date")));
			}
			saleOrder.setContractNo(String.valueOf(orderMap.get("contract_no")));
			saleOrder.setPoOrderNo(String.valueOf(orderMap.get("po_no")));
			saleOrder.setWmsOrderNo(String.valueOf(orderMap.get("wmsOrderNo")));
			saleOrder.setOutboundTime(String.valueOf(orderMap.get("deliver_date")));
			saleOrder.setSalesQty(orderNumMap.get(code));
			saleOrder.setSalesAmount(String.valueOf(orderAmountMap.get(code)));
			saleOrder.setCurrency(String.valueOf(orderMap.get("currency")));
			saleOrder.setOrderStatus(OrderStatus);
			saleOrder.setGoodsList(orderItemMap.get(code));
			orderEntityList.add(saleOrder);
		}

		return orderEntityList;
	}
	/**
	 * 3.同步销售TOC单到dstp
	 * */
	private void synOrderTOC(MerchantInfoModel merchant,List<String> orderCodeList,String startTime,String endTime) throws Exception {
		/**查询卓烨公司获取一件代发客户的注册登记证号*/
		MerchantInfoModel zyMerchant = new MerchantInfoModel();
		zyMerchant.setTopidealCode("0000134");
		zyMerchant = merchantInfoDao.searchByModel(zyMerchant);

		//分页查询昨天新增和修改的单号
		Integer startIndex = 0;
		Integer pageSize = 100;
		while(true){
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("merchantId",merchant.getId());
			paramMap.put("startIndex",startIndex);
			paramMap.put("pageSize",pageSize);
			if(orderCodeList!=null&&orderCodeList.size()>0){
				paramMap.put("orderCodeList",orderCodeList);
			}else{
				paramMap.put("startTime",startTime);
				paramMap.put("endTime",endTime);
			}

			//查询有变更的单号
			List<String> codeList = orderDao.getOrderCodeList(paramMap);
			if(codeList==null||codeList.size()<=0){
				logger.info("新增/修改的订单数量为0，结束");
				break;
			}
			//查询表头
			Map<String,Object> queryMap = new HashMap<>();
			queryMap.put("codeList",codeList);
			List<Map<String,Object>> orderList = orderDao.getOrderByCodeList(queryMap);
			//查询表体
			List<Map<String,Object>> orderItemList = orderDao.getOrderItemByCodeList(queryMap);

			//封装实体
			SaleOrderToCBatchDstp saleOrderToCBatchDstp = createOrderEntity(merchant,zyMerchant,orderList,orderItemList);
			if(saleOrderToCBatchDstp==null ||saleOrderToCBatchDstp.getOrderList()==null||saleOrderToCBatchDstp.getOrderList().size()<=0) continue;
			//推送商品到DSTP
			JSONObject jsonObj = JSONObject.fromObject(saleOrderToCBatchDstp);
			GatewayResponse response = DSTPUtil.sendTocOrderDSTP(jsonObj.toString(),merchant.getTopidealCode());
			if(response==null||!response.getFlag().equals("success")){
				logger.info("电商订单同步DSTP失败,merchantId="+merchant.getId()+","+startTime+"-"+endTime+" DSTP:"+response.getMessage());
				throw new RuntimeException("电商订单同步DSTP失败,merchantId="+merchant.getId()+","+startTime+"-"+endTime+" DSTP:"+response.getMessage());
			}

			startIndex += pageSize;
		}
	}
	/**封装销售报文
	 * */
	private SaleOrderToCBatchDstp createOrderEntity(MerchantInfoModel merchant, MerchantInfoModel zyMerchant,
													 List<Map<String,Object>> orderList, List<Map<String,Object>> orderItemList){

		//按单号分组
		Map<String,List<SaleOrderToCGoods>> orderItemMap = new HashMap<>();//存储表体
		for(Map<String,Object> itemMap:orderItemList){
			String code = String.valueOf(itemMap.get("code"));

			SaleOrderToCGoods goods = new SaleOrderToCGoods();
			goods.setGoodsName(String.valueOf(itemMap.get("goods_name")));
			goods.setBarCode(String.valueOf(itemMap.get("barcode")));
			goods.setQty(String.valueOf(itemMap.get("num")));
			List<SaleOrderToCGoods> goodsList = orderItemMap.get(code);
			if(goodsList==null) goodsList = new ArrayList<>();
			goodsList.add(goods);
			orderItemMap.put(code,goodsList);
		}

		//封装实体
		List<SaleOrderToC> orderTocList = new ArrayList<>();
		for(Map<String,Object> orderMap:orderList){
			String code = String.valueOf(orderMap.get("code"));
			String status = String.valueOf(orderMap.get("status"));//状态 4-已发货 006-已删除
			String shopTypeCode = String.valueOf(orderMap.get("shop_type_code"));//店铺类型值编码 001:POP; 002:一件代发'
			String storePlatformName = String.valueOf(orderMap.get("store_platform_name"));//电商平台编码

			String OrderStatus = "";
	        if(status.equals(DERP_ORDER.ORDER_STATUS_4)){
				OrderStatus = "20";//已发货:20-已完成
			}else if(status.equals(DERP.DEL_CODE_006)){
				OrderStatus = "30";//已删除:30-已取消
			}

			//收货人地址
			SaleOrderToCRecipient toCRecipient = new SaleOrderToCRecipient();
			toCRecipient.setCountry(String.valueOf(orderMap.get("receiver_country")));
			toCRecipient.setProvince(String.valueOf(orderMap.get("receiver_province")));
			toCRecipient.setCity(String.valueOf(orderMap.get("receiver_city")));
			toCRecipient.setDistrict(String.valueOf(orderMap.get("receiver_area")));

			//订单信息
			SaleOrderToC saleOrder = new SaleOrderToC();
			saleOrder.setEntOrderNo(code);
			saleOrder.setPlatformOrderNo(String.valueOf(orderMap.get("external_code")));
			if(orderMap.get("trading_date")!=null){
				saleOrder.setOrderTime(String.valueOf(orderMap.get("trading_date")));
			}else{
				saleOrder.setOrderTime(String.valueOf(orderMap.get("deliver_date")));
			}
			saleOrder.setDeliveryTime(String.valueOf(orderMap.get("deliver_date")));
			saleOrder.setDeclareTime(String.valueOf(orderMap.get("declare_date")));
			saleOrder.setReleaseTime(String.valueOf(orderMap.get("release_date")));
			saleOrder.setWarehouse(String.valueOf(orderMap.get("depot_name")));
			saleOrder.setLogisticsName(String.valueOf(orderMap.get("logistics_name")));
			if(StringUtils.isNotBlank(shopTypeCode)&&shopTypeCode.equals("002")){//一件代发
				saleOrder.setOrderType("1");//非自营
				saleOrder.setAgentCode(zyMerchant.getRegistrationCert());
				saleOrder.setAgentName(zyMerchant.getEnglishName());
			}else{//自营
				saleOrder.setOrderType("2");//自营
			}
			saleOrder.setCbepcomName(DERP.getLabelByKey(DERP.storePlatformList,storePlatformName));
			saleOrder.setShippingFee(String.valueOf(orderMap.get("way_frt_fee")));
			saleOrder.setTaxFee(String.valueOf(orderMap.get("taxes")));
			saleOrder.setTotalAmount(String.valueOf(orderMap.get("payment")));
			saleOrder.setCurrency(String.valueOf(orderMap.get("currency")));
			saleOrder.setOrderStatus(OrderStatus);
			saleOrder.setGoodList(orderItemMap.get(code));
			saleOrder.setRecipient(toCRecipient);
			if(orderItemMap.get(code)!=null) {
				orderTocList.add(saleOrder);
			}
		}

		SaleOrderToCBatchDstp saleOrderToCBatchDstp = new SaleOrderToCBatchDstp();
		saleOrderToCBatchDstp.setCustomerCode(merchant.getRegistrationCert());
		saleOrderToCBatchDstp.setOrderList(orderTocList);
		return saleOrderToCBatchDstp;
	}
}
