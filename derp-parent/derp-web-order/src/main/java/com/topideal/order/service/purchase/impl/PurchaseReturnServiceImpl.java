package com.topideal.order.service.purchase.impl;

import com.alibaba.fastjson.JSONObject;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.dto.purchase.PurchaseReturnItemDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotItemDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOrderExportDTO;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreGoodsItem;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreOrderRoot;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreRecipient;
import com.topideal.json.pushapi.epass.e08.OutStoreOrderItemJson;
import com.topideal.json.pushapi.epass.e08.OutStoreOrderRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.purchase.PurchaseReturnService;
import com.topideal.order.webapi.sale.form.MerchandiseForm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseReturnServiceImpl implements PurchaseReturnService{

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(PurchaseReturnServiceImpl.class);
	@Autowired
	private PurchaseReturnOrderDao purchaseReturnOrderDao ;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private PurchaseReturnItemDao purchaseReturnItemDao ;
	@Autowired
	private PurchaseReturnRelDao purchaseReturnRelDao ;
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao ;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
	private PurchaseReturnOdepotDao purchaseReturnOdepotDao ;
	@Autowired
	private PurchaseReturnOdepotItemDao purchaseReturnOdepotItemDao ;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao ;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private BrandSuperiorMongoDao brandSuperiorMongoDao ;
//	@Autowired
//	private PurchaseAnalysisDao purchaseAnalysisDao;
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;

	@SuppressWarnings("unchecked")
	@Override
	public PurchaseReturnOrderDTO listPurchaseReturnPage(PurchaseReturnOrderDTO dto, User user) throws SQLException {

		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);

				return dto ;
			}

			dto.setBuIds(buIds);
		}

		Map<Long, DepotInfoMongo> depotCache = new HashMap<Long, DepotInfoMongo>() ;

		dto = purchaseReturnOrderDao.listPurchaseReturnPage(dto);
		List<PurchaseReturnOrderDTO> list = (List<PurchaseReturnOrderDTO>)dto.getList();

		for (PurchaseReturnOrderDTO purchaseReturnOrderDTO : list) {

			DepotInfoMongo depotInfoMongo = depotCache.get(purchaseReturnOrderDTO.getOutDepotId());

			if(depotInfoMongo == null) {
				Map<String,Object> queryMap = new HashMap<String, Object>() ;

				queryMap.put("depotId", purchaseReturnOrderDTO.getOutDepotId()) ;
				depotInfoMongo = depotInfoMongoDao.findOne(queryMap) ;

				if(depotInfoMongo != null) {
					depotCache.put(purchaseReturnOrderDTO.getOutDepotId(), depotInfoMongo) ;
				}

			}

			//判断是否存在出库单
			PurchaseReturnOdepotModel queryModel = new PurchaseReturnOdepotModel() ;
			queryModel.setPurchaseReturnId(purchaseReturnOrderDTO.getId());
			queryModel = purchaseReturnOdepotDao.searchByModel(queryModel) ;

			if(!DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depotInfoMongo.getBatchValidation())
					&& DERP_ORDER.PURCHASERETURNORDER_STATUS_003.equals(purchaseReturnOrderDTO.getStatus())
					&& queryModel == null) {
				purchaseReturnOrderDTO.setIsOutDepotAble("1");
			}else {
				purchaseReturnOrderDTO.setIsOutDepotAble("0");
			}

		}

		dto.setList(list);

		return dto ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void isSameParameter(String ids) throws SQLException, RuntimeException {
		PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
		Long depotId = null;	// 仓库
		Long supplierId = null;	// 客户
		Long buId = null;	// 销售类型
		List<Long> idList = StrUtils.parseIds(ids);
		for (Long id : idList) {
			// 根据id获取信息
			purchaseOrderModel = new PurchaseOrderModel();
			purchaseOrderModel.setId(id);
			purchaseOrderModel = purchaseOrderDao.searchByModel(purchaseOrderModel);
//			if(depotId == null){
//				depotId = purchaseOrderModel.getDepotId();
//			}else if(!depotId.equals(purchaseOrderModel.getDepotId())){
//				throw new DerpException("选单失败，只能选相同的入仓仓库");
//			}
			if(supplierId == null){
				supplierId = purchaseOrderModel.getSupplierId();
			}else if(!supplierId.equals(purchaseOrderModel.getSupplierId())){
				throw new DerpException("选单失败，只能选相同的供应商");
			}
			if(buId == null){
				buId = purchaseOrderModel.getBuId();
			}else if(!buId.equals(purchaseOrderModel.getBuId())){
				throw new DerpException("选单失败，只能选相同的事业部");
			}
		}
	}

	@Override
	public PurchaseReturnOrderModel listPurchaseOrderAndDepotOrder(List<Long> ids, Long merchantId) throws SQLException {
//		Map<String,PurchaseOrderItemModel> map = new HashMap<String,PurchaseOrderItemModel>();
		Long outDepotId = null;
		String outDepotName = null ;
		Long supplierId = null;
		String supplierName = null ;
		Long buId = null;
		String buName = null ;
		String purchaseOrderRelCode = "" ;
		int num=0;
		String currency="";
		List<PurchaseOrderItemModel> queryItemList = new ArrayList<>();
		for (Long id : ids) {
			PurchaseOrderModel model = new PurchaseOrderModel();
			model.setId(id);
			model = purchaseOrderDao.getDetails(model);
			currency=model.getCurrency();
			List<PurchaseOrderItemModel> purchaseItemList = model.getItemList();
			queryItemList.addAll(purchaseItemList);

			if(supplierId == null) {
				supplierId = model.getSupplierId() ;
				supplierName = model.getSupplierName() ;
			}

			if(outDepotId == null) {
				outDepotId = model.getDepotId() ;
				outDepotName = model.getDepotName() ;
			}

			if(buId == null) {
				buId = model.getBuId() ;
				buName = model.getBuName() ;
			}

			purchaseOrderRelCode += model.getCode() + ";";

			num=num+1;
		}

		// 注入表头数据
		PurchaseReturnOrderModel purchaseReturnOrderModel = new PurchaseReturnOrderModel();
		purchaseReturnOrderModel.setSupplierId(supplierId);;	// 供应商
		purchaseReturnOrderModel.setSupplierName(supplierName);;	// 客户
		purchaseReturnOrderModel.setOutDepotId(outDepotId);	// 退出仓库(出仓仓库)
		purchaseReturnOrderModel.setOutDepotName(outDepotName);
		purchaseReturnOrderModel.setBuId(buId);
		purchaseReturnOrderModel.setBuName(buName);
		purchaseReturnOrderModel.setPurchaseOrderRelCode(purchaseOrderRelCode);
		purchaseReturnOrderModel.setCurrency(currency);

		//注入表体数据
		List<PurchaseReturnItemModel> itemList = new ArrayList<>();
		Map<String,List<PurchaseOrderItemModel>> map = queryItemList.stream().collect(Collectors.groupingBy(PurchaseOrderItemModel::getBarcode));
		for(String key :map.keySet()){
			List<PurchaseOrderItemModel> iList = map.get(key);
			List<Long> purchaseOrderIdList = iList.stream().map(PurchaseOrderItemModel::getPurchaseOrderId).collect(Collectors.toList());
			PurchaseOrderItemModel item = iList.get(0);
			PurchaseReturnItemModel purchaseReturnItemModel = new PurchaseReturnItemModel();
			purchaseReturnItemModel.setGoodsId(item.getGoodsId());
			purchaseReturnItemModel.setGoodsName(item.getGoodsName());
			purchaseReturnItemModel.setGoodsNo(item.getGoodsNo());
			purchaseReturnItemModel.setBrandName(item.getBrandName());
			purchaseReturnItemModel.setReturnPrice(item.getPrice());
			purchaseReturnItemModel.setReturnNum(getwarehouseNum(StringUtils.join(purchaseOrderIdList,","),item.getBarcode()));
			Double rate =item.getTaxRate();
			purchaseReturnItemModel.setTaxRate(rate);
			rate = rate+1;

			//退货总金额（不含税）=退货数量*退货单价
			purchaseReturnItemModel.setReturnAmount(new BigDecimal(item.getNum()).multiply(purchaseReturnItemModel.getReturnPrice()));

			//退货总金额（含税）=（1+税率）*采购退金额
			BigDecimal taxReturnAmount=new BigDecimal(rate).multiply(purchaseReturnItemModel.getReturnAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
			purchaseReturnItemModel.setTaxReturnAmount(taxReturnAmount);

			//税额=退货总金额（含税）-退货总金额（不含税)
			purchaseReturnItemModel.setTax(purchaseReturnItemModel.getTaxReturnAmount().subtract(purchaseReturnItemModel.getReturnAmount()));

			//获取商品信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchandiseId", item.getGoodsId());
			MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(params);
			if(goods != null){
				purchaseReturnItemModel.setBarcode(goods.getBarcode());
				purchaseReturnItemModel.setDeclarePrice(goods.getFilingPrice());
			}

			itemList.add(purchaseReturnItemModel);
		}
		purchaseReturnOrderModel.setItemList(itemList);
		return purchaseReturnOrderModel;
	}

	/**
	 * 获取采购入库数量
	 * @param purchaseIds
	 * @param barcode
	 * @return
	 * @throws SQLException
	 */
	private Integer getwarehouseNum(String purchaseIds,String barcode) throws SQLException {
		//根据采购明细id获取已入库数量
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("purchaseOrderIds", purchaseIds);
		paramMap.put("barcode", barcode);
		paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
		List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
		Integer warehouseNum = 0;
		if(numList != null && numList.size() > 0){
			for(Map<String, Object> numMap : numList){
				BigDecimal queryNum = (BigDecimal) numMap.get("num");//当前商品已入库数量
				warehouseNum += queryNum.intValue();
			}

		}
		return warehouseNum;
	}

	@Override
	public String getDepotTypeByDepotId(Long outDepotId) {

		String depotType = "" ;

		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("depotId", outDepotId) ;
		DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);

		if(depot != null) {
			depotType = depot.getType() ;
		}

		return depotType;
	}

	@Override
	public Long saveOrModifyPurchaseReturnOrder(PurchaseReturnOrderModel model, User user, String operate) throws Exception {

		Long id = model.getId() ;
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("depotId", model.getOutDepotId()) ;
		DepotInfoMongo outDepot = depotInfoMongoDao.findOne(queryMap);

		if(model.getOutDepotId() !=null && outDepot != null) {
			model.setOutDepotName(outDepot.getName());
		}

		queryMap.put("depotId", model.getInDepotId()) ;
		DepotInfoMongo inDepot = depotInfoMongoDao.findOne(queryMap);

		if(model.getInDepotId() != null && inDepot != null) {
			model.setInDepotName(inDepot.getName());
		}
		// 获取该事业部信息
		Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
		merchantBuRelParam.put("merchantId", user.getMerchantId());
		merchantBuRelParam.put("buId", model.getBuId());
		merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
		if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
			throw new RuntimeException("事业部ID为：" + model.getBuId() + ",未查到该公司下事业部信息");
		}
		if("2".equals(operate)){
			//通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
			if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelMongo.getStockLocationManage()) && model.getStockLocationTypeId() == null){
				throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空") ;
			}else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(merchantBuRelMongo.getStockLocationManage()) && model.getStockLocationTypeId() != null){
				throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写") ;
			}
		}
		BuStockLocationTypeConfigMongo stockLocationMongo = null;
		if(model.getStockLocationTypeId() != null){
			Map<String,Object> stockLocationMap = new HashMap<>();
			stockLocationMap.put("buStockLocationTypeId", model.getStockLocationTypeId());
			stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
			stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);
			if(stockLocationMongo == null){
				throw new RuntimeException("库位类型输入有误") ;
			}
		}
		if(stockLocationMongo != null){
			model.setStockLocationTypeId(stockLocationMongo.getBuStockLocationTypeId());
			model.setStockLocationTypeName(stockLocationMongo.getName());
		}

		if(id != null) {
			model.setModifyDate(TimeUtils.getNow());
			purchaseReturnOrderDao.modify(model) ;
		}else {
			model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGTH));
			model.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_001);
			model.setCreateDate(TimeUtils.getNow());
			model.setCreateName(user.getName());
			model.setCreater(user.getId());
			id = purchaseReturnOrderDao.save(model) ;

			String purchaseOrderRelCode = model.getPurchaseOrderRelCode();
			if(StringUtils.isNotBlank(purchaseOrderRelCode)) {
				String[] purchaseOrderRelCodeArr = purchaseOrderRelCode.split(";") ;

				for(int j = 0; j < purchaseOrderRelCodeArr.length; j++) {
					String purchaseCode = purchaseOrderRelCodeArr[j] ;

					PurchaseOrderModel tempModel = new PurchaseOrderModel() ;
					tempModel.setCode(purchaseCode);
					tempModel = purchaseOrderDao.searchByModel(tempModel) ;

					if(tempModel != null) {

						if (StringUtils.isNotBlank(tempModel.getWriteOffStatus())) {
							throw new RuntimeException("采购订单：" + purchaseCode + DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_writeOffStatusList, tempModel.getWriteOffStatus()));
						}

						PurchaseReturnRelModel relModel = new PurchaseReturnRelModel() ;
						relModel.setPurchaseId(tempModel.getId());
						relModel.setPurchaseReturnId(id);
						relModel.setCreateDate(TimeUtils.getNow());

						purchaseReturnRelDao.save(relModel) ;
					}
				}
			}
		}

		Map<String, Object> delMap = new HashMap<String, Object>() ;
		delMap.put("orderId", id) ;
		int rows = purchaseReturnItemDao.delByMap(delMap) ;

		for (PurchaseReturnItemModel item : model.getItemList()) {
			item.setOrderId(id);
			purchaseReturnItemDao.save(item) ;
		}

		return id;
	}

	@Override
	public void auditPurchaseReturnOrder(PurchaseReturnOrderModel model, User user) throws Exception {

		Long id = saveOrModifyPurchaseReturnOrder(model, user, "2");

		if(id != null) {

			PurchaseReturnOrderModel tempModel = purchaseReturnOrderDao.searchById(id);
			String purchaseOrderRelCode = tempModel.getPurchaseOrderRelCode();

			if(!DERP_ORDER.PURCHASERETURNORDER_STATUS_001.equals(tempModel.getStatus())) {
				throw new RuntimeException("订单状态不为待审核") ;
			}
			List<Long> purchaseOrderRelIdList = new ArrayList<>();
			for(String tempPurchaseCode : purchaseOrderRelCode.split(";")){
				PurchaseOrderModel temModel = new PurchaseOrderModel();
				temModel.setCode(tempPurchaseCode);
				temModel = purchaseOrderDao.searchByModel(temModel);
				if(temModel != null){
					purchaseOrderRelIdList.add(temModel.getId());
				}
			}

			PurchaseReturnItemModel queryModel = new PurchaseReturnItemModel() ;
			queryModel.setOrderId(id);

			List<PurchaseReturnItemModel> itemTempList = purchaseReturnItemDao.list(queryModel);
			//校验采购订单表体
			for (PurchaseReturnItemModel purchaseReturnItemModel : itemTempList) {
				//获取入库量
				Integer warehouseNum = getwarehouseNum(StringUtils.join(purchaseOrderRelIdList,","),purchaseReturnItemModel.getBarcode());
				if( purchaseReturnItemModel.getReturnNum().intValue() > warehouseNum.intValue()){
					throw new RuntimeException("条形码：" + purchaseReturnItemModel.getBarcode() + " 退出数量大于对应采购订单入库数量") ;
				}
			}

			PurchaseReturnOrderModel updateModel = new PurchaseReturnOrderModel();
			updateModel.setId(id);
			updateModel.setAuditDate(TimeUtils.getNow());
			updateModel.setAuditName(user.getName());
			updateModel.setAuditor(user.getId());
			updateModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_003);

			purchaseReturnOrderDao.modify(updateModel) ;

			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			queryMap.put("merchantId", tempModel.getMerchantId()) ;
			MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(queryMap);

			queryMap.clear();
			Long outDepotId = tempModel.getOutDepotId();
			queryMap.put("depotId", outDepotId) ;
			DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);

			queryMap.put("merchantId", tempModel.getMerchantId()) ;
			DepotMerchantRelMongo relModel = depotMerchantRelMongoDao.findOne(queryMap);

			/**
			 * 若出库仓类型为海外仓且对应该公司下进出接口指令为是，调用1.17销售出仓订单
			 */
			List<PurchaseReturnItemModel> itemList = model.getItemList();
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()) && DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(relModel.getIsInOutInstruction())) {
				SalesOutStoreOrderRoot rootJson = new SalesOutStoreOrderRoot() ;

				rootJson.setOrder_id(tempModel.getCode());
				rootJson.setBusi_mode("10");
				rootJson.setWarehouse_id(depot.getCode());
				rootJson.setDestion_code(tempModel.getDestinationName());

				SalesOutStoreRecipient recipient = new SalesOutStoreRecipient();
		        recipient.setName(tempModel.getDeliveryName()); //收货人姓名
		        recipient.setCountry(tempModel.getCountry()); //国家
		        recipient.setAddress(tempModel.getDeliveryAddr()); //地址

				rootJson.setRecipient(recipient);

				Integer totalNum = 0;

				//如果原货号存在合并相同原货号数据
				Map<String, SalesOutStoreGoodsItem> sumMap = new LinkedHashMap<>();

				for(int i = 0;i < itemList.size() ; i++){
					PurchaseReturnItemModel item = itemList.get(i);
					SalesOutStoreGoodsItem itemRequest = new SalesOutStoreGoodsItem();
					itemRequest.setUom(tempModel.getTallyingUnit());// 理货单位
					itemRequest.setGoods_id(item.getGoodsNo());//调出货号
					itemRequest.setGoods_name(item.getGoodsName());

					Integer returnNum = item.getReturnNum();
		        	totalNum += returnNum;
		        	itemRequest.setAmount(returnNum);//数量


					sumMap.put(item.getGoodsNo(), itemRequest) ;

				}
				rootJson.setTotal_num(totalNum);

				if(!sumMap.isEmpty()) {
					rootJson.setGoods_list(new ArrayList<>(sumMap.values()));
				}

				JSONObject jsonObject = (JSONObject)JSONObject.toJSON(rootJson) ;
				jsonObject.put("method",EpassAPIMethodEnum.EPASS_E03_METHOD.getMethod());
				jsonObject.put("topideal_code", merchant.getTopidealCode());
				rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(),MQPushApiEnum.PUSH_EPASS.getTags());
			}
			/**
			 * 若出库仓类型为保税仓且对应该公司下进出接口指令为是，调用1.49出库订单推送
			 */
			else if(DERP_SYS.DEPOTINFO_TYPE_A.equals(depot.getType()) && DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(relModel.getIsInOutInstruction())) {
				OutStoreOrderRootJson rootJson = new OutStoreOrderRootJson() ;

				rootJson.setOrder_id(tempModel.getCode());
				rootJson.setBusi_scene("30");
				rootJson.setEbc_code(merchant.getTopidealCode());
				rootJson.setWarehouse_code(depot.getCode());
				rootJson.setCustoms_code(depot.getCustomsNo());
				rootJson.setContract_no(tempModel.getContractNo());
				rootJson.setCreated_time(TimeUtils.format(updateModel.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));

				//如果原货号存在合并相同原货号数据
				Map<String, OutStoreOrderItemJson> sumMap = new LinkedHashMap<>();

				int index = 1 ;
				for(int i = 0; i < itemList.size(); i ++){
					PurchaseReturnItemModel item = itemList.get(i);
					OutStoreOrderItemJson itemRequest = new OutStoreOrderItemJson();
					itemRequest.setIndex(index);
					itemRequest.setNotes(item.getRemark());
					itemRequest.setGood_id(item.getGoodsNo());

					//单价均取“申报单价”*商家商品备案价申报系数下推
					Double ratio = 1.00 ;
					BrandSuperiorMongo brandSuperiorModel = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(item.getGoodsId());

					if(brandSuperiorModel != null
							&& brandSuperiorModel.getPriceDeclareRatio() != null) {
						ratio = brandSuperiorModel.getPriceDeclareRatio() ;
					}

					BigDecimal price = item.getDeclarePrice().multiply(new BigDecimal(ratio)).setScale(3, BigDecimal.ROUND_HALF_UP);
					itemRequest.setUnit_price(price.doubleValue());

		        	Integer returnNum = item.getReturnNum();
		        	itemRequest.setAmount(returnNum);//数量


					sumMap.put(item.getGoodsNo(), itemRequest) ;
					index ++ ;

				}

				if(!sumMap.isEmpty()) {
					rootJson.setGood_list(new ArrayList<>(sumMap.values()));
				}

				JSONObject jsonObject = (JSONObject)JSONObject.toJSON(rootJson) ;
				jsonObject.put("method",EpassAPIMethodEnum.EPASS_E08_METHOD.getMethod());
				jsonObject.put("topideal_code", merchant.getTopidealCode());
				rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(),MQPushApiEnum.PUSH_EPASS.getTags());
			}

		}

	}

	@Override
	public PurchaseReturnOrderDTO getDTOById(Long id) {
		return purchaseReturnOrderDao.getDTOById(id);
	}

	@Override
	public List<PurchaseReturnItemModel> getItemListByOrderId(Long id) throws SQLException {

		PurchaseReturnItemModel itemModel = new PurchaseReturnItemModel() ;
		itemModel.setOrderId(id);

		return purchaseReturnItemDao.list(itemModel);
	}

	@Override
	public boolean delPurchaseReturnOrder(List<Long> list) throws SQLException {

		int flag = 0;
		for (Long id : list) {
			PurchaseReturnOrderModel model = purchaseReturnOrderDao.searchById(id);
			if (!DERP_ORDER.PURCHASERETURNORDER_STATUS_001.equals(model.getStatus())) {
				flag = 1;
				break;
			}
		}

		if (flag == 1) {
			throw new DerpException("操作失败，只能删除待审核订单");
		}

		for (Long id : list) {
			// 根据id获取信息
			PurchaseReturnOrderModel model = new PurchaseReturnOrderModel();
			model.setId(id);
			model.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_006);// 已删除
			purchaseReturnOrderDao.modify(model);
		}

		return true;
	}

	@Override
	public List<PurchaseReturnOrderExportDTO> getDetailsByExport(PurchaseReturnOrderDTO dto, User user) {

		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				return new ArrayList<PurchaseReturnOrderExportDTO>();
			}

			dto.setBuIds(buIds);
		}

		return purchaseReturnOrderDao.getDetailsByExport(dto);
	}

	@Override
	public void savePurchaseReturnOdepot(PurchaseReturnOdepotModel returnOdepotModel, List<PurchaseReturnOdepotItemDTO> itemList, User user) throws Exception {

		PurchaseReturnOrderModel returnOrder = purchaseReturnOrderDao.searchById(returnOdepotModel.getPurchaseReturnId());

		if(returnOrder == null) {
			throw new RuntimeException("采购退货单不存在") ;
		}

		// 判断归属日期是否小于 关账日期/月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(user.getMerchantId());
		closeAccountsMongo.setDepotId(returnOrder.getOutDepotId());
		closeAccountsMongo.setBuId(returnOrder.getBuId());

		String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 关账下个月日期大于 入库日期
			if (returnOdepotModel.getReturnOutDate().getTime() < Timestamp
					.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("出库时间必须大于关账日期/月结日期");
			}
		}

		returnOdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGTC));
		returnOdepotModel.setPurchaseReturnCode(returnOrder.getCode());
		returnOdepotModel.setBuId(returnOrder.getBuId());
		returnOdepotModel.setBuName(returnOrder.getBuName());
		returnOdepotModel.setOutDepotId(returnOrder.getOutDepotId());
		returnOdepotModel.setOutDepotName(returnOrder.getOutDepotName());
		returnOdepotModel.setMerchantId(returnOrder.getMerchantId());
		returnOdepotModel.setMerchantName(returnOrder.getMerchantName());
		returnOdepotModel.setSupplierId(returnOrder.getSupplierId());
		returnOdepotModel.setSupplierName(returnOrder.getSupplierName());
		returnOdepotModel.setCurrency(returnOrder.getCurrency());
		returnOdepotModel.setPoNo(returnOrder.getPoNo());
		returnOdepotModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_027);
		returnOdepotModel.setTallyingUnit(returnOrder.getTallyingUnit());
		returnOdepotModel.setCreater(user.getId());
		returnOdepotModel.setCreateName(user.getName());
		returnOdepotModel.setCreateDate(TimeUtils.getNow());

		Long id = purchaseReturnOdepotDao.save(returnOdepotModel);

		for (PurchaseReturnOdepotItemDTO purchaseReturnOdepotItemDTO : itemList) {
			if(StringUtils.isBlank(purchaseReturnOdepotItemDTO.getBarcode())){
				throw new RuntimeException("条形码为空") ;
			}
			PurchaseReturnItemModel itemQueryModel = new PurchaseReturnItemModel() ;
			itemQueryModel.setBarcode(purchaseReturnOdepotItemDTO.getBarcode());
			itemQueryModel.setOrderId(returnOdepotModel.getPurchaseReturnId());

			PurchaseReturnItemModel returnItem = purchaseReturnItemDao.searchByModel(itemQueryModel);

			if(purchaseReturnOdepotItemDTO.getNum() <= 0) {
				throw new RuntimeException("商品货号：" + returnItem.getGoodsNo() + "退货出库数量必须大于0") ;
			}
			if(purchaseReturnOdepotItemDTO.getNum() > returnItem.getReturnNum()) {
				throw new RuntimeException("商品货号：" + returnItem.getGoodsNo() + "退货出库数量不能超过退货数量") ;
			}

			PurchaseReturnOdepotItemModel purchaseReturnOdepotItemModel = new PurchaseReturnOdepotItemModel();
			purchaseReturnOdepotItemModel.setOdepotOrderId(id);
			purchaseReturnOdepotItemModel.setBarcode(returnItem.getBarcode());
			purchaseReturnOdepotItemModel.setGoodsId(returnItem.getGoodsId());
			purchaseReturnOdepotItemModel.setGoodsName(returnItem.getGoodsName());
			purchaseReturnOdepotItemModel.setGoodsNo(returnItem.getGoodsNo());
			purchaseReturnOdepotItemModel.setCreateDate(TimeUtils.getNow());
			purchaseReturnOdepotItemModel.setNum(purchaseReturnOdepotItemDTO.getNum());
			purchaseReturnOdepotItemModel.setBatchNo(purchaseReturnOdepotItemDTO.getBatchNo());
			if(StringUtils.isNotBlank(purchaseReturnOdepotItemDTO.getProductionDate())){
				purchaseReturnOdepotItemModel.setProductionDate(TimeUtils.parseDay(purchaseReturnOdepotItemDTO.getProductionDate()));
			}
			if(StringUtils.isNotBlank(purchaseReturnOdepotItemDTO.getOverdueDate())){
				purchaseReturnOdepotItemModel.setOverdueDate(TimeUtils.parseDay(purchaseReturnOdepotItemDTO.getOverdueDate()));
			}
			purchaseReturnOdepotItemDao.save(purchaseReturnOdepotItemModel) ;
		}

		/*
		 * ---------------------查询采购退货订单，设置汇率、本位币开始---------------------------------------------------
		 */
		Map<String, Object> queryMerchantMap = new HashMap<String, Object>() ;

		queryMerchantMap.put("merchantId", returnOrder.getMerchantId()) ;

		MerchantInfoMongo merchantInfoMongo = merchantMongoDao.findOne(queryMerchantMap) ;

		String accountCurrency = null ;
		Double rate = null ;
		if(merchantInfoMongo != null) {
			accountCurrency = merchantInfoMongo.getAccountCurrency();

			if(StringUtils.isNotBlank(accountCurrency)) {

				if(accountCurrency.equals(returnOrder.getCurrency())) {
					rate = 1.00 ;
				}else {
					Map<String, Object> queryRateMap = new HashMap<String, Object>() ;
					queryRateMap.put("origCurrencyCode", returnOrder.getCurrency()) ;
					queryRateMap.put("tgtCurrencyCode", accountCurrency) ;
					queryRateMap.put("effectiveDate", TimeUtils.format(returnOdepotModel.getReturnOutDate(), "yyyy-MM-dd")) ;

					ExchangeRateMongo rateMongo = exchangeRateMongoDao.findOne(queryRateMap);

					if(rateMongo != null) {
						rate = rateMongo.getRate();
					}
				}
			}

		}

		returnOrder.setTgtCurrency(accountCurrency);
		returnOrder.setModifyDate(TimeUtils.getNow());
		if(rate != null) {
			returnOrder.setRate(new BigDecimal(rate));
		}

		purchaseReturnOrderDao.modify(returnOrder) ;

		//校验采购订单表体
		PurchaseReturnItemModel queryItemModel = new PurchaseReturnItemModel() ;
		queryItemModel.setOrderId(returnOrder.getId());
		List<PurchaseReturnItemModel> returnItemList = purchaseReturnItemDao.list(queryItemModel);

		for (PurchaseReturnItemModel purchaseReturnItemModel : returnItemList) {
			if(rate == null) {
				continue ;
			}
			//设置本位单价、金额
			BigDecimal returnAmount = purchaseReturnItemModel.getReturnAmount();
			Integer returnNum = purchaseReturnItemModel.getReturnNum();

			BigDecimal tgtAmount = returnAmount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);

			BigDecimal tgtPrice = tgtAmount.divide(new BigDecimal(returnNum), 8, BigDecimal.ROUND_HALF_UP);

			purchaseReturnItemModel.setTgtReturnAmount(tgtAmount);
			purchaseReturnItemModel.setTgtReturnPrice(tgtPrice);

			purchaseReturnItemDao.modify(purchaseReturnItemModel) ;
		}

		/*
		 * ---------------------查询采购退货订单，设置汇率、本位币结束---------------------------------------------------
		 */

		PurchaseReturnOdepotModel tempModel = purchaseReturnOdepotDao.searchById(id) ;

		PurchaseReturnOdepotItemModel queryReturnOutItemModel = new PurchaseReturnOdepotItemModel() ;
		queryReturnOutItemModel.setOdepotOrderId(id);
		List<PurchaseReturnOdepotItemModel> itemReturnOutList = purchaseReturnOdepotItemDao.list(queryReturnOutItemModel) ;

		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", tempModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params); 	// 获取仓库信息

		//加减库存json
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();

		invetAddOrSubtractRootJson.setMerchantId(String.valueOf(tempModel.getMerchantId()));
		invetAddOrSubtractRootJson.setMerchantName(tempModel.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(user.getTopidealCode());
		invetAddOrSubtractRootJson.setDepotId(String.valueOf(tempModel.getOutDepotId()));
		invetAddOrSubtractRootJson.setDepotName(tempModel.getOutDepotName());
		invetAddOrSubtractRootJson.setDepotCode(depot.getCode());
		invetAddOrSubtractRootJson.setDepotType(depot.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(depot.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(tempModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(tempModel.getPurchaseReturnCode());
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0018);
		invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0030);
		invetAddOrSubtractRootJson.setSourceDate(TimeUtils.formatFullTime());	// 单据时间
		invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.format(tempModel.getReturnOutDate(), "yyyy-MM-dd HH:mm:ss"));	// 出库时间
		invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.format(tempModel.getReturnOutDate(), "yyyy-MM")); // 归属月份
		invetAddOrSubtractRootJson.setBuId(String.valueOf(tempModel.getBuId()));
		invetAddOrSubtractRootJson.setBuName(tempModel.getBuName());

		//回调mq
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTopic());
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTags());
		Map<String, Object> customParam = new HashMap<String, Object>();
		customParam.put("code", tempModel.getCode());	// 订单code
		invetAddOrSubtractRootJson.setCustomParam(customParam);

		//设置加减表体
		Map<String, Object> merchandiseMap = new HashMap<String, Object>() ;

		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		for (PurchaseReturnOdepotItemModel item : itemReturnOutList) {

			merchandiseMap.put("goodsNo", item.getGoodsNo());
			merchandiseMap.put("merchantId", tempModel.getMerchantId());
			merchandiseMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);

			InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
			invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
			invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());

			invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
			invetAddOrSubtractGoodsListJson.setBarcode(merchandise.getBarcode());
			invetAddOrSubtractGoodsListJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);	// 商品分类 （0 好品 1坏品 ）
			invetAddOrSubtractGoodsListJson.setNum(item.getNum());	//数量
			invetAddOrSubtractGoodsListJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 字符串 0 调减 1调增
			invetAddOrSubtractGoodsListJson.setIsExpire(DERP.ISEXPIRE_1);

			if(StringUtils.isNotBlank(item.getBatchNo())) {
				invetAddOrSubtractGoodsListJson.setBatchNo(item.getBatchNo());
			}

			if(item.getProductionDate() != null) {
				invetAddOrSubtractGoodsListJson.setProductionDate(TimeUtils.format(item.getProductionDate(), "yyyy-MM-dd"));
			}

			if(item.getOverdueDate() != null) {
				invetAddOrSubtractGoodsListJson.setOverdueDate(TimeUtils.format(item.getOverdueDate(), "yyyy-MM-dd"));;

				//判断过期
				if(TimeUtils.daysBetween(tempModel.getReturnOutDate(), item.getOverdueDate()) < 0) {
					invetAddOrSubtractGoodsListJson.setIsExpire(DERP.ISEXPIRE_0);
				}

			}

			if(DERP.ORDER_TALLYINGUNIT_00.equals(tempModel.getTallyingUnit())) {
				invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_0);
			}else if(DERP.ORDER_TALLYINGUNIT_01.equals(tempModel.getTallyingUnit())) {
				invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_1);
			}else if(DERP.ORDER_TALLYINGUNIT_02.equals(tempModel.getTallyingUnit())) {
				invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_2);
			}
			invetAddOrSubtractGoodsListJson.setStockLocationTypeId(returnOrder.getStockLocationTypeId() == null ? "" :returnOrder.getStockLocationTypeId().toString());
			invetAddOrSubtractGoodsListJson.setStockLocationTypeName(returnOrder.getStockLocationTypeName());
			invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
		}

		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

		//加库存
		net.sf.json.JSONObject addjson = net.sf.json.JSONObject.fromObject(invetAddOrSubtractRootJson) ;
		try {
			rocketMQProducer.send(addjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		} catch (Exception e) {
			LOGGER.error("--------采购退货出库单扣减库存消息发送失败-------", addjson.toString());
			LOGGER.error("--------采购退货出库单扣减库存失败-------", e);
		}
	}

	/**采购退选择商品弹窗**/
	@Override
	public PurchaseReturnItemDTO getPurchaseItemPopupByPage(MerchandiseForm form,User user) throws Exception {

		if(StringUtils.isBlank(form.getOrderCodes())){
			return new PurchaseReturnItemDTO();
		}
		PurchaseReturnItemDTO queryPurchaseReturnItemDTO = new PurchaseReturnItemDTO();
		queryPurchaseReturnItemDTO.setPurchaseOrderCodes(form.getOrderCodes());
		queryPurchaseReturnItemDTO.setUnNeedIds(form.getUnNeedGoodsJson());
		queryPurchaseReturnItemDTO.setGoodsName(form.getGoodsName());
		queryPurchaseReturnItemDTO.setBarcode(form.getBarcode());
		queryPurchaseReturnItemDTO = purchaseReturnItemDao.getPurchaseItemPopupByPage(queryPurchaseReturnItemDTO);
		List<PurchaseReturnItemDTO> queryDeclareItemList = queryPurchaseReturnItemDTO.getList();
		for(PurchaseReturnItemDTO purchaseReturnItem : queryDeclareItemList) {
			purchaseReturnItem.setBrandName("境外品牌(其他)");
			//根据采购id集合和条形码获取已入库数量
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("purchaseOrderIds", form.getOrderIds());
			paramMap.put("barcode", purchaseReturnItem.getBarcode());
			paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
			List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
			Integer warehouseNum = 0;
			if(numList != null && numList.size() > 0){
				BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
				warehouseNum = queryNum.intValue();
			}

			purchaseReturnItem.setReturnNum(warehouseNum);
			//获取商品信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", purchaseReturnItem.getGoodsId());
			MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(params);
			// 根据商品id查询商品母品牌 获取申报系数
			Double priceDeclareRatio = 1.0;
			BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(purchaseReturnItem.getGoodsId());
			if ((brandSuperiorMongo != null && brandSuperiorMongo.getPriceDeclareRatio() != null)) {
				priceDeclareRatio = brandSuperiorMongo.getPriceDeclareRatio();
			}

			// 备案单价*商家商品备案价申报系数
			BigDecimal declarePrice = merchandiseMongo.getFilingPrice().multiply(new BigDecimal(priceDeclareRatio)).setScale(5, BigDecimal.ROUND_HALF_UP);
			purchaseReturnItem.setDeclarePrice(declarePrice);
			purchaseReturnItem.setMerchantName(user.getMerchantName());

		}
		return queryPurchaseReturnItemDTO;
	}
}
