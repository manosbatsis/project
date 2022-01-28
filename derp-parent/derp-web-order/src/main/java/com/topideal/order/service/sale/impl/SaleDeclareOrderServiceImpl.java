package com.topideal.order.service.sale.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
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
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.LogisticsAttorneyDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.sale.SaleDeclareOrderDTO;
import com.topideal.entity.dto.sale.SaleDeclareOrderItemDTO;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreGoodsItem;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreOrderRoot;
import com.topideal.json.pushapi.epass.e03.SalesOutStoreRecipient;
import com.topideal.json.pushapi.epass.e08.OutStoreOrderItemJson;
import com.topideal.json.pushapi.epass.e08.OutStoreOrderRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.SaleDeclareOrderService;
import com.topideal.order.webapi.sale.form.MerchandiseForm;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaleDeclareOrderServiceImpl implements SaleDeclareOrderService{
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(SaleDeclareOrderServiceImpl.class);
	@Autowired
	private SaleDeclareOrderDao saleDeclareOrderDao ;
	@Autowired
	private SaleDeclareOrderItemDao saleDeclareOrderItemDao ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
	private SaleOrderDao saleOrderDao ;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	@Autowired
	private LogisticsAttorneyDao logisticsAttorneyDao ;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private BrandSuperiorMongoDao brandSuperiorMongoDao;
	@Autowired
	private UnitMongoDao unitMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;// MQ
	@Autowired
	private SalePoRelDao salePoRelDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private MerchandiseCustomsRelMongoDao merchandiseCustomsRelMongoDao;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	@Autowired
	DepotCustomsRelMongoDao depotCustomsRelMongoDao;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	@Autowired
	private FileTempDao fileTempDao;
	@Autowired
	private CountryOriginMongoDao countryOriginMongoDao;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao;
	@Autowired
	private MerchandiseExternalWarehouseMongoDao merchandiseExternalWarehouseMongoDao ;
	@Autowired
	private BrandMongoDao brandMongoDao;

	/**
	 * 获取列表分页信息
	 */
	@Override
	public SaleDeclareOrderDTO listDTOByPage(SaleDeclareOrderDTO dto, User user) throws Exception {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}

			dto.setBuList(buIds);
		}
		return saleDeclareOrderDao.listDTOByPage(dto);
	}

	/**
	 * 销售单转预申报单 封装数据展示
	 */
	@Override
	public SaleDeclareOrderDTO saleOrderToSaleDeclare(String saleIds) throws Exception {
		List<Long> ids = StrUtils.parseIds(saleIds);
		List<SaleDeclareOrderItemDTO> itemList = new ArrayList<SaleDeclareOrderItemDTO>();

		Integer seq = 1;
		List<String> codes = new ArrayList<String>();
		Set<String> lBXNoSet = new HashSet<String>();
		List<String> poNoList = new ArrayList<String>();
		BigDecimal billWeight = BigDecimal.ZERO;
		for(Long id : ids) {
			SaleOrderModel saleModel = saleOrderDao.searchById(id);
			SaleOrderItemModel saleItemModel = new SaleOrderItemModel();
			saleItemModel.setOrderId(saleModel.getId());
			List<SaleOrderItemModel> saleItemList = saleOrderItemDao.list(saleItemModel);
			if(saleItemList == null || saleItemList.size() < 1) {
				throw new RuntimeException("销售订单："+saleModel.getCode() +" 商品信息为空");
			}
			for(SaleOrderItemModel saleItem : saleItemList) {
				SaleDeclareOrderItemDTO saleDeclareOrderItemDTO = new SaleDeclareOrderItemDTO();
				saleDeclareOrderItemDTO.setSaleOrderId(saleModel.getId());
				saleDeclareOrderItemDTO.setSaleOrderCode(saleModel.getCode());
				saleDeclareOrderItemDTO.setPoNo(saleModel.getPoNo());
				saleDeclareOrderItemDTO.setSalePrice(saleItem.getPrice());
				saleDeclareOrderItemDTO.setGoodsId(saleItem.getGoodsId());
				saleDeclareOrderItemDTO.setGoodsNo(saleItem.getGoodsNo());
				saleDeclareOrderItemDTO.setGoodsCode(saleItem.getGoodsCode());
				saleDeclareOrderItemDTO.setGoodsName(saleItem.getGoodsName());
				saleDeclareOrderItemDTO.setBarcode(saleItem.getBarcode());
				saleDeclareOrderItemDTO.setCommbarcode(saleItem.getCommbarcode());
				saleDeclareOrderItemDTO.setBrandName("境外品牌(其他)");
				saleDeclareOrderItemDTO.setBoxNum(0);

				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("saleItemId",saleItem.getId());
				Integer outNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);//商品总出库量
				List<String> statusList = new ArrayList<>();
				statusList.add(DERP_ORDER.SALEDECLARE_STATUS_001);
				statusList.add(DERP_ORDER.SALEDECLARE_STATUS_002);
				statusList.add(DERP_ORDER.SALEDECLARE_STATUS_003);
				statusList.add(DERP_ORDER.SALEDECLARE_STATUS_008);
				statusList.add(DERP_ORDER.SALEDECLARE_STATUS_009);
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("saleOrderId", id);
				params.put("saleItemId", saleItem.getId());
				params.put("statusList", statusList);
				Integer declareNum = saleDeclareOrderItemDao.getToTalDeclareNum(params) ;//累计该销售订单对应商品申报数量
				saleDeclareOrderItemDTO.setNum(saleItem.getNum() - (outNum == null ? 0 : outNum) - (declareNum == null ?0 : declareNum));//默认 申报数量=销售数量-累计出库数量
				//获取商品信息
				params = new HashMap<String, Object>();
				params.put("merchandiseId", saleItem.getGoodsId());
				MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(params);

				Double grossWeightSum = new BigDecimal(merchandiseMongo.getGrossWeight()).multiply(new BigDecimal(saleDeclareOrderItemDTO.getNum()))
						.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
				Double netWeightSum = new BigDecimal(merchandiseMongo.getNetWeight()).multiply(new BigDecimal(saleDeclareOrderItemDTO.getNum()))
						.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
				saleDeclareOrderItemDTO.setGrossWeight(merchandiseMongo.getGrossWeight());
				saleDeclareOrderItemDTO.setGrossWeightSum(grossWeightSum);
				saleDeclareOrderItemDTO.setNetWeight(merchandiseMongo.getNetWeight());
				saleDeclareOrderItemDTO.setNetWeightSum(netWeightSum);
				saleDeclareOrderItemDTO.setConstituentRatio(merchandiseMongo.getComponent());

				// 根据商品id查询商品母品牌 获取申报系数
				Double priceDeclareRatio = 1.0;
				BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(saleItem.getGoodsId());
				if ((brandSuperiorMongo != null && brandSuperiorMongo.getPriceDeclareRatio() != null)) {
					priceDeclareRatio = brandSuperiorMongo.getPriceDeclareRatio();
				}

				BigDecimal declarePrice = merchandiseMongo.getFilingPrice().multiply(new BigDecimal(priceDeclareRatio)).setScale(5, BigDecimal.ROUND_HALF_UP);
				// 申报单价*商家商品备案价申报系数
				saleDeclareOrderItemDTO.setPrice(declarePrice);
				saleDeclareOrderItemDTO.setAmount(declarePrice.multiply(new BigDecimal(saleDeclareOrderItemDTO.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
				saleDeclareOrderItemDTO.setSeq(seq++);
				saleDeclareOrderItemDTO.setSaleItemId(saleItem.getId());

				itemList.add(saleDeclareOrderItemDTO);
				billWeight = billWeight.add(new BigDecimal(saleDeclareOrderItemDTO.getGrossWeightSum()));
			}

			codes.add(saleModel.getCode());
			lBXNoSet.add(saleModel.getLbxNo());

			for(String poNo : saleModel.getPoNo().split("&")) {
				if(!poNoList.contains(poNo)) {
					poNoList.add(poNo);
				}
			}
		}
		//公司+事业部+出库仓库+销售币种 相同，客户、入库仓随机取一条
		SaleOrderModel saleModel = saleOrderDao.searchById(ids.get(0));
		SaleDeclareOrderDTO dto =  new SaleDeclareOrderDTO();
		dto.setMerchantId(saleModel.getMerchantId());
		dto.setMerchantName(saleModel.getMerchantName());
		dto.setBuId(saleModel.getBuId());
		dto.setBuName(saleModel.getBuName());
		dto.setCustomerId(saleModel.getCustomerId());
		dto.setCustomerName(saleModel.getCustomerName());
		dto.setOutDepotId(saleModel.getOutDepotId());
		dto.setOutDepotName(saleModel.getOutDepotName());
		dto.setCurrency(saleModel.getCurrency());
		dto.setInDepotId(saleModel.getInDepotId());
		dto.setInDepotName(saleModel.getInDepotName());
		dto.setSaleOrderIds(StringUtils.join(ids,","));
		dto.setSaleOrderCodes(StringUtils.join(codes,","));
		dto.setLbxNo(StringUtils.join(lBXNoSet,","));
		dto.setPoNo(StringUtils.join(poNoList,"&"));
		dto.setBillWeight(billWeight.doubleValue());
		dto.setItemList(itemList);

		return dto;
	}
	/**
	 * 统计类型数量
	 */
	@Override
	public List<Map<String, Object>> getDeclareTypeNum(SaleDeclareOrderDTO dto, User user) {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return null;
			}
			dto.setBuList(buIds);
		}
		List<Map<String, Object>> mapList = saleDeclareOrderDao.getDeclareTypeNum(dto) ;

		Long total = new Long(0) ;

		for (Map<String, Object> object : mapList) {
			if(object.get("num") != null) {
				total += (Long) object.get("num") ;
			}
		}

		Map<String, Object> totalMap = new HashMap<String, Object>() ;
		totalMap.put("total", total) ;
		mapList.add(totalMap) ;

		return mapList;
	}

	/**
	 * 查询详情
	 */
	@Override
	public SaleDeclareOrderDTO searchDetail(Long id) throws Exception {
		SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
		dto.setId(id);
		dto = saleDeclareOrderDao.searchDetail(dto);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depotId", dto.getOutDepotId());
		params.put("merchantId", dto.getMerchantId());
		DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(params);

		if(depotMerchantRel != null) {
			dto.setDepotIsInOutInstruction(depotMerchantRel.getIsInOutInstruction());
		}
		// 获取仓库信息
		Map<String, Object> depotParams = new HashMap<String, Object>();
		depotParams.put("depotId", dto.getOutDepotId());
		DepotInfoMongo depot = depotInfoMongoDao.findOne(depotParams);
		if(depot != null){
			dto.setOutDepotType(depot.getType());
		}

		LogisticsAttorneyModel logisticsAttorneyModel = new LogisticsAttorneyModel() ;
		logisticsAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_2);
		logisticsAttorneyModel.setOrderId(id);
		logisticsAttorneyModel = logisticsAttorneyDao.searchByModel(logisticsAttorneyModel);
		dto.setCarrierInformationTempId(logisticsAttorneyModel.getCarrierInformationTempId());
		dto.setCarrierInformationTempName(logisticsAttorneyModel.getCarrierInformationTempName());
		dto.setCarrierInformationTempDetails(logisticsAttorneyModel.getCarrierInformationText());
		dto.setShipperTempId(logisticsAttorneyModel.getShipperTempId());
		dto.setShipperTempName(logisticsAttorneyModel.getShipperTempName());
		dto.setShipperTempDetails(logisticsAttorneyModel.getShipperText());
		dto.setConsigneeTempId(logisticsAttorneyModel.getConsigneeTempId());
		dto.setConsigneeTempName(logisticsAttorneyModel.getConsigneeTempName());
		dto.setConsigneeTempDetails(logisticsAttorneyModel.getConsigneeText());
		dto.setNotifierTempId(logisticsAttorneyModel.getNotifierTempId());
		dto.setNotifierTempName(logisticsAttorneyModel.getNotifierTempName());
		dto.setNotifierTempDetails(logisticsAttorneyModel.getNotifierText());
		dto.setDockingTempId(logisticsAttorneyModel.getDockingTempId());
		dto.setDockingTempName(logisticsAttorneyModel.getDockingTempName());
		dto.setDockingTempDetails(logisticsAttorneyModel.getDockingText());

		return dto;
	}
	/**
	 * 查询表体信息
	 */
	@Override
	public List<SaleDeclareOrderItemDTO> searchItemsByOrderId(Long id) throws Exception {
		SaleDeclareOrderItemDTO itemModel = new SaleDeclareOrderItemDTO();
		itemModel.setDeclareOrderId(id);
		return saleDeclareOrderItemDao.listDTO(itemModel);
	}

	/**
	 * 保存
	 */
	@Override
	public void saveSaleDeclareOrder(SaleDeclareOrderDTO dto, User user ,LogisticsAttorneyModel logisticsAttorneyModel) throws Exception {
		if (dto.getCustomerId() == null) {
			throw new RuntimeException("客户不能为空");
		}
		if (dto.getBuId() == null) {
			throw new RuntimeException("事业部不能为空");
		}
		if(StringUtils.isBlank(dto.getTradeTerms())) {
			throw new RuntimeException("贸易条款不能为空");
		}
		if(StringUtils.isBlank(dto.getLoadPort())) {
			throw new RuntimeException("装货港不能为空");
		}
		if(StringUtils.isBlank(dto.getPortDes())) {
			throw new RuntimeException("卸货港不能为空");
		}
		if(dto.getOutDepotId() == null) {
			throw new RuntimeException("出库仓库不能为空");
		}
		//客户信息
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("customerid", dto.getCustomerId());
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(params2);

		// 获取该事业部信息
		Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
		merchantBuRelParam.put("merchantId", dto.getMerchantId());
		merchantBuRelParam.put("buId", dto.getBuId());
		merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
		if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
			throw new RuntimeException("事业部ID为：" +  dto.getBuId() + ",未查到该公司下事业部信息");
		}

		// 用户事业部
		boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(),  dto.getBuId());
		if (!userRelateBu) {
			throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
		}

		// 获取出仓仓库信息
		DepotInfoMongo outDepot = null;
		if (dto.getOutDepotId() != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", dto.getOutDepotId() );
			outDepot = depotInfoMongoDao.findOne(params);
		}
		// 获取入仓仓库信息
		DepotInfoMongo inDepot = null;
		if (dto.getInDepotId() != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", dto.getInDepotId() );
			inDepot = depotInfoMongoDao.findOne(params);
		}
		// 出仓关区
		DepotCustomsRelMongo outDepotCustomsRelMongo = null;
		if (dto.getOutDepotId() != null && dto.getOutCustomsId() != null) {
			Map<String, Object> outDepotCustomsRelMap = new HashMap<String, Object>();
			outDepotCustomsRelMap.put("depotId", dto.getOutDepotId());
			outDepotCustomsRelMap.put("customsAreaId", dto.getOutCustomsId());
			outDepotCustomsRelMongo = depotCustomsRelMongoDao.findOne(outDepotCustomsRelMap);// 平台关区信息
			if (outDepotCustomsRelMongo == null) {
				throw new RuntimeException("保存失败，出仓仓库：" + outDepot.getName() + " 未关联选中平台关区");
			}
		}
		// 入仓关区
		DepotCustomsRelMongo inDepotCustomsRelMongo = null;
		if (dto.getInDepotId() != null && dto.getInCustomsId() != null) {
			Map<String, Object> inDepotCustomsRelMap = new HashMap<String, Object>();
			inDepotCustomsRelMap.put("depotId", dto.getInDepotId());
			inDepotCustomsRelMap.put("customsAreaId", dto.getInCustomsId());
			inDepotCustomsRelMongo = depotCustomsRelMongoDao.findOne(inDepotCustomsRelMap);
			if (inDepotCustomsRelMongo == null) {
				throw new RuntimeException("保存失败，入仓仓库：" + inDepot.getName() + " 未关联选中平台关区");
			}
		}

		if (StringUtils.isBlank(dto.getTransport())) {
			throw new RuntimeException("运输方式不能为空");
		}
		if (StringUtils.isBlank(dto.getShipper())) {
			throw new RuntimeException("境外发货人不能为空");
		}
		//海外仓时，理货单位必填
		if(outDepot != null && DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType()) ){
			if(StringUtils.isBlank(dto.getTallyingUnit())) {
				throw new RuntimeException("当海外仓时，理货单位不能为空") ;
			}
			if(StringUtils.isBlank(dto.getReceiverName())) {
				throw new RuntimeException("当海外仓时，收件人姓名不能为空") ;
			}
			if(StringUtils.isBlank(dto.getReceiverAddress())) {
				throw new RuntimeException("当海外仓时，收件人地址不能为空") ;
			}
			if(StringUtils.isBlank(dto.getCountry())) {
				throw new RuntimeException("当海外仓时，国家不能为空") ;
			}
		}
		//不同销售单，相同商品货号的申报单价必须相等
		Map<Long, BigDecimal> checkGoodsPrice = new HashMap<Long, BigDecimal>();
		for(SaleDeclareOrderItemDTO itemDTO : dto.getItemList()) {
			if(itemDTO.getSaleItemId() == null){
				throw new RuntimeException("销售明细id为空，请及时维护") ;
			}
			Long key = itemDTO.getGoodsId();
			if(checkGoodsPrice.containsKey(key) && itemDTO.getPrice().compareTo(checkGoodsPrice.get(key)) != 0) {
				throw new RuntimeException("商品货号："+ itemDTO.getGoodsNo() +" 申报单价不相等") ;
			}
			if(!checkGoodsPrice.containsKey(key)){
				checkGoodsPrice.put(key, itemDTO.getPrice());
			}
			if(itemDTO.getNum() < 1) {
				throw new RuntimeException("销售订单："+itemDTO.getSaleOrderCode()+"商品货号："+ itemDTO.getGoodsNo() +" 申报数量要大于0") ;
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("saleItemId",itemDTO.getSaleItemId());
			Integer outNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);//商品总出库量

			List<String> statusList = new ArrayList<>();
			statusList.add(DERP_ORDER.SALEDECLARE_STATUS_001);
			statusList.add(DERP_ORDER.SALEDECLARE_STATUS_002);
			statusList.add(DERP_ORDER.SALEDECLARE_STATUS_003);
			statusList.add(DERP_ORDER.SALEDECLARE_STATUS_008);
			statusList.add(DERP_ORDER.SALEDECLARE_STATUS_009);
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("saleOrderId", itemDTO.getSaleOrderId());
			params.put("saleItemId", itemDTO.getSaleItemId());
			params.put("statusList", statusList);
			if(dto.getId() != null) {//编辑修改数量，剔除当前预申报单商品数量
				params.put("declareOrderId", dto.getId());
			}
			Integer declareNum = saleDeclareOrderItemDao.getToTalDeclareNum(params) ;//累计该销售订单对应商品申报数量

			SaleOrderItemModel saleItemModel = saleOrderItemDao.searchById(itemDTO.getSaleItemId());

			Integer stayNum = saleItemModel.getNum() - (declareNum == null ? 0 :declareNum) - (outNum == null ? 0 :outNum);//剩余量

			if(stayNum < itemDTO.getNum()) {
				throw new RuntimeException("销售订单："+itemDTO.getSaleOrderCode()+"商品货号："+ itemDTO.getGoodsNo() +" 累计申报数量不能大于销售数量，剩余申报数量为:"+stayNum) ;
			}

		}

		//预申报单
		SaleDeclareOrderModel model = new SaleDeclareOrderModel();
		BeanUtils.copyProperties(dto,model);
		model.setMerchantName(merchantBuRelMongo.getMerchantName());
		model.setBuName(merchantBuRelMongo.getBuName());
		model.setCustomerName(customer == null ? "" :customer.getName());
		model.setOutDepotName(null);
		if (outDepot != null) {
			model.setOutDepotName(outDepot.getName());
		}
		if (inDepot != null) {
			model.setInDepotName(inDepot.getName());
		}
		if (outDepotCustomsRelMongo != null) {
			model.setOutCustomsName(outDepotCustomsRelMongo.getCustomsAreaName());
		}
		if (inDepotCustomsRelMongo != null) {
			model.setInCustomsName(inDepotCustomsRelMongo.getCustomsAreaName());
		}

		Long num = model.getId();
		if(model.getId() == null) {
			model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSOD));
			model.setStatus(DERP_ORDER.SALEDECLARE_STATUS_001);
			model.setApiStatus(DERP_ORDER.SALEDECLARE_APISTATUS_0);
			model.setCreateDate(TimeUtils.getNow());
			model.setCreater(user.getId());
			model.setCreateName(user.getName());

			num = saleDeclareOrderDao.save(model) ;

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, model.getCode(), "新增销售预申报", null, null);
			//物流委托书
			logisticsAttorneyModel.setOrderId(num);
			logisticsAttorneyModel.setCreateDate(TimeUtils.getNow());
			logisticsAttorneyDao.save(logisticsAttorneyModel);

			if(StringUtils.isNotBlank(dto.getSaleOrderCodes())) {

				String[] arr = dto.getSaleOrderCodes().split(",");

				// 更新销售单状态 生成日志
				for (String code : arr) {
					SaleOrderModel queryModel = new SaleOrderModel() ;
					queryModel.setCode(code);
					queryModel = saleOrderDao.searchByModel(queryModel);

					SaleOrderModel saleOrderModel = new SaleOrderModel();
					saleOrderModel.setId(queryModel.getId());
					saleOrderModel.setIsGenerateDeclare(DERP_ORDER.SALEDECLARE_ISGENERATE_1);// 是
					saleOrderDao.modify(saleOrderModel);
					commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_5, queryModel.getCode(), "生成销售预申报单", null, null);
				}
			}

		}else {
			//更新表头数据
			SaleDeclareOrderModel queryModel = new SaleDeclareOrderModel();
			queryModel = saleDeclareOrderDao.searchById(num);
			model.setCode(queryModel.getCode());
			model.setStatus(queryModel.getStatus());
			model.setApiStatus(queryModel.getApiStatus());
			model.setModifier(user.getId());
			model.setModifierUsername(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			model.setPallteizeDate(queryModel.getPallteizeDate());//打托时间
			model.setCustomsSubmitDate(queryModel.getCustomsSubmitDate());//提交清关资料 时间
			model.setCustomsConfirmDate(queryModel.getCustomsConfirmDate());//确认清关资料 时间
			model.setConfirmDeclarationDate(queryModel.getConfirmDeclarationDate());// 确认申报时间
			model.setLogisticsCommissionDate(queryModel.getLogisticsCommissionDate());//预约物流 时间
			model.setExportPallteizeDate(queryModel.getExportPallteizeDate());//导出打托资料 时间
			saleDeclareOrderDao.modifyWithNull(model);

			//物流委托书
			LogisticsAttorneyModel queryAttorneyModel = new LogisticsAttorneyModel() ;
			queryAttorneyModel.setOrderId(model.getId());
			queryAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_2);// 1-采购预申报 2-销售预申报
			queryAttorneyModel = logisticsAttorneyDao.searchByModel(queryAttorneyModel) ;

			logisticsAttorneyModel.setId(queryAttorneyModel.getId());
			logisticsAttorneyModel.setModifyDate(TimeUtils.getNow());
			logisticsAttorneyDao.modifyWithNull(logisticsAttorneyModel);

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, model.getCode(), "编辑销售预申报单", null, null);

		}

		//先删除表体
		saleDeclareOrderItemDao.delByOrderId(model.getId());

		//再新增表体
		for(SaleDeclareOrderItemDTO itemDTO : dto.getItemList()) {
			SaleDeclareOrderItemModel itemModel = new SaleDeclareOrderItemModel();
			BeanUtils.copyProperties(itemDTO, itemModel);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", itemModel.getGoodsId());
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);

			itemModel.setDeclareOrderId(num);// 表头id
			itemModel.setGoodsCode(mogo.getGoodsCode());// 商品编码
			itemModel.setGoodsName(mogo.getName());// 商品名称
			itemModel.setGoodsNo(mogo.getGoodsNo());// 商品货号
			itemModel.setBarcode(mogo.getBarcode());//条形码
			itemModel.setCommbarcode(mogo.getCommbarcode());//标准条码

			saleDeclareOrderItemDao.save(itemModel);
		}

	}

	/**
	 * 下推指令
	 */
	@Override
	public void modifyPushOutOrder(Long id, User user) throws Exception {
		//获取预申报单信息
		SaleDeclareOrderModel declareModel = saleDeclareOrderDao.searchById(id);
		if(declareModel.getCustomsConfirmDate() == null){
			throw new RuntimeException("请先确认清关资料");
		}

		// 出库仓
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depotId", declareModel.getOutDepotId());
		DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

		// 入库仓
		DepotInfoMongo inDepot = null;
		if (declareModel.getInDepotId() != null) {
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", declareModel.getInDepotId());
			inDepot = depotInfoMongoDao.findOne(params1);
		}

		// 仓库公司关联表
		Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
		depotMerchantRelParam.put("merchantId", declareModel.getMerchantId());
		depotMerchantRelParam.put("depotId", declareModel.getOutDepotId());
		DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
		if (depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)) {
			throw new RuntimeException( "单号：" + declareModel.getCode() + ",仓库ID为：" + declareModel.getOutDepotId() + "，未查到该公司下调出仓库信息");

		}

		// 获取销售表体（商品信息）
		SaleDeclareOrderItemModel saleOrderItem = new SaleDeclareOrderItemModel();
		saleOrderItem.setDeclareOrderId(id);
		List<SaleDeclareOrderItemModel> itemList = saleDeclareOrderItemDao.list(saleOrderItem);
		//合并相同商品的申报数量、毛重、净重
		Map<Long, SaleDeclareOrderItemModel> mergeMap = new HashMap<Long, SaleDeclareOrderItemModel>();
		for(SaleDeclareOrderItemModel itemModel :itemList) {
			Long key = null;
			key = itemModel.getGoodsId();

			SaleDeclareOrderItemModel mergeGoods = mergeMap.get(key);
			if (mergeGoods == null) {
				mergeGoods = itemModel;

			} else {
				Integer num = mergeGoods.getNum();
				num += itemModel.getNum();
				mergeGoods.setNum(num);

				BigDecimal grossWeightSum = new BigDecimal(mergeGoods.getGrossWeightSum());
				grossWeightSum.add(new BigDecimal(itemModel.getGrossWeightSum()));

				BigDecimal netWeightSum = new BigDecimal(mergeGoods.getNetWeightSum());
				netWeightSum.add(new BigDecimal(itemModel.getNetWeightSum()));

				mergeGoods.setGrossWeightSum(grossWeightSum.doubleValue());
				mergeGoods.setNetWeightSum(netWeightSum.doubleValue());

			}
			mergeMap.put(key, mergeGoods);
		}


		// 若销售出库仓类型为保税仓且对应该公司下进出接口指令为是，调用1.49出库订单推送
		// 出库仓为“需下推接口指令”且入库仓仓库类型为“代客管理仓库”则 不推送接口出库
		if (inDepot == null || (inDepot != null && !DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_1.equals(inDepot.getIsValetManage()))) {
			if (DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())
					&& depot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A)) {
				OutStoreOrderRootJson root = new OutStoreOrderRootJson();
				root.setContract_no(declareModel.getContractNo());// 合同号
				root.setOrder_id(declareModel.getCode()); // 订单编码
				root.setWarehouse_code(depot.getCode()); // 仓库编码
				root.setBusi_scene("20"); // 业务场景类型 20：销售出库
				root.setCreated_time(TimeUtils.formatFullTime(declareModel.getCreateDate()));
				root.setEbc_code(user.getTopidealCode());
				root.setCustoms_code(depot.getCustomsNo());
				Map<String, OutStoreOrderItemJson> sumMap = new LinkedHashMap<>();
				for (Long key : mergeMap.keySet()) {
					SaleDeclareOrderItemModel orderItem = mergeMap.get(key);
					OutStoreOrderItemJson itemTemp = new OutStoreOrderItemJson();
					itemTemp.setIndex(orderItem.getSeq());
					itemTemp.setGood_id(orderItem.getGoodsNo()); // 商品货号
					itemTemp.setAmount(orderItem.getNum()); // 数量
					itemTemp.setGross_weight(orderItem.getGrossWeightSum()); // 毛重
					itemTemp.setNet_weight(orderItem.getNetWeightSum()); // 净重
					HashMap<String, Object> map = new HashMap<>();
					map.put("merchandiseId", orderItem.getGoodsId());
					MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(map);
					HashMap<String, Object> map2 = new HashMap<>();
					map2.put("unitId", merchandise.getUnit());
					UnitMongo unit = unitMongoDao.findOne(map2);
					if(unit != null) {
						itemTemp.setContracts_unit(unit.getCode()); // 单位
					}
					itemTemp.setUnit_price(orderItem.getPrice().doubleValue()); // 单价

					sumMap.put(itemTemp.getGood_id(), itemTemp);
				}
				try {
					if (!sumMap.isEmpty()) {
						root.setGood_list(new ArrayList<>(sumMap.values()));
					}
					JSONObject jsonObject = JSONObject.fromObject(root);
					jsonObject.put("method", EpassAPIMethodEnum.EPASS_E08_METHOD.getMethod());
					jsonObject.put("topideal_code", user.getTopidealCode());
					SendResult result = rocketMQProducer.send(jsonObject.toString(),MQPushApiEnum.PUSH_EPASS.getTopic(), MQPushApiEnum.PUSH_EPASS.getTags());
					System.out.println("销售预申报单下推出库指令消息返回：" + result.toString());
				} catch (Exception e) {
					LOGGER.error("单号：" + declareModel.getCode() + ",推送异常");
				}
			} else if (DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction()) && "c".equals(depot.getType())) {
				// 若出库仓类型为海外仓且对应该公司下进出接口指令为是，调用1.17销售出仓订单
				SalesOutStoreOrderRoot root = new SalesOutStoreOrderRoot();
				root.setOrder_id(declareModel.getCode());
				root.setWarehouse_id(depot.getCode());
				root.setDestion_code(declareModel.getDestination()); // 目的地
				root.setBusi_mode("10"); // 进口模式 10：B2B
				Integer totalNum = 0;
				Map<String, SalesOutStoreGoodsItem> sumMap = new LinkedHashMap<>();
				for (Long key : mergeMap.keySet()) {
					SaleDeclareOrderItemModel item = mergeMap.get(key);
					Integer transferNum = item.getNum();
					SalesOutStoreGoodsItem itemRequest = new SalesOutStoreGoodsItem();
					itemRequest.setGoods_id(item.getGoodsNo());// 调出货号
					itemRequest.setGoods_name(item.getGoodsName()); // 商品名称
					itemRequest.setAmount(transferNum);// 数量
					itemRequest.setUom(declareModel.getTallyingUnit());// 理货单位
					totalNum += transferNum;

					sumMap.put(itemRequest.getGoods_id(), itemRequest);
				}
				root.setTotal_num(totalNum);
				if (!sumMap.isEmpty()) {
					root.setGoods_list(new ArrayList<>(sumMap.values()));
				}
				// 收件人信息
				SalesOutStoreRecipient recipient = new SalesOutStoreRecipient();
				recipient.setName(declareModel.getReceiverName());// 收件人姓名
				recipient.setCountry(declareModel.getCountry());// 收件人国家
				recipient.setAddress(declareModel.getReceiverAddress());// 收件人地址
				root.setRecipient(recipient);

				try {
					// JSONObject jsonObject = JSONObject.fromObject(root);
					com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject
							.toJSON(root);
					jsonObject.put("method", EpassAPIMethodEnum.EPASS_E03_METHOD.getMethod());
					jsonObject.put("topideal_code", user.getTopidealCode());
					SendResult result = rocketMQProducer.send(jsonObject.toString(),
							MQPushApiEnum.PUSH_EPASS.getTopic(), MQPushApiEnum.PUSH_EPASS.getTags());
					System.out.println("销售预申报下推出库指令消息返回：" + result.toString());
					if (result == null) {
						throw new RuntimeException( "单号：" + declareModel.getCode() + ",下推出库指令服务异常");
					}
					if (!result.getSendStatus().name().equals("SEND_OK")) {// SEND_OK-成功
						throw new RuntimeException( "单号：" + declareModel.getCode() + ",下推出库指令失败");

					}
				} catch (Exception e) {
					LOGGER.error("单号：" + declareModel.getCode() + ",推送异常");
				}
			}
		}

		//更新销售预申报单
		declareModel.setApiStatus(DERP_ORDER.SALEDECLARE_APISTATUS_1);
		declareModel.setStatus(DERP_ORDER.SALEDECLARE_STATUS_009);
		declareModel.setPushOutDate(TimeUtils.getNow());
		declareModel.setModifier(user.getId());
		declareModel.setModifierUsername(user.getName());
		declareModel.setModifyDate(TimeUtils.getNow());
		saleDeclareOrderDao.modify(declareModel);

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "推送出库指令", null, null);
	}
	/**
	 * 2-打托完成 3-提交清关资料 4-确认清关资料 5-确认申报时间 6-预约物流 7-导出打托资料
	 */
	@Override
	public void updateTimeTrace(Long id, String orderTime, User user, String operate) throws Exception {
		SaleDeclareOrderModel declareModel = saleDeclareOrderDao.searchById(id);

		if(TimeUtils.daysBetween(TimeUtils.parse(orderTime, "yyyy-MM-dd"), new Date()) < 0) {
			throw new RuntimeException("选择的日期必须小于或等于当前日期") ;
		}

		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(user.getMerchantId());
		closeAccountsMongo.setDepotId(declareModel.getOutDepotId());
		closeAccountsMongo.setBuId(declareModel.getBuId());
		String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 关账下个月日期大于 入库日期
			if (Timestamp.valueOf(orderTime).getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("选择的日期必须大于关账日期") ;
			}
		}

		//operate 2-打托 3-提交清关资料 4-确认清关资料 5-确认申报时间 6-预约物流
		switch(operate) {
			case "2":
				//更新销售预申报单 打托时间
				declareModel.setPallteizeDate(TimeUtils.parseFullTime(orderTime));
				declareModel.setModifier(user.getId());
				declareModel.setModifierUsername(user.getName());
				declareModel.setModifyDate(TimeUtils.getNow());
				if(DERP_ORDER.SALEDECLARE_STATUS_001.equals(declareModel.getStatus())) {//如果状态为 【待打托】 需更新状态为 【待清关】
					declareModel.setStatus(DERP_ORDER.SALEDECLARE_STATUS_002);
				}
				saleDeclareOrderDao.modify(declareModel);

				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "打托", null, null);
				break;
			case "3":
				//更新销售预申报单 提交清关资料 时间
				declareModel.setCustomsSubmitDate(TimeUtils.parseFullTime(orderTime));
				declareModel.setModifier(user.getId());
				declareModel.setModifierUsername(user.getName());
				declareModel.setModifyDate(TimeUtils.getNow());
				if(DERP_ORDER.SALEDECLARE_STATUS_001.equals(declareModel.getStatus())) {//如果状态为 【待打托】 需更新状态为 【待清关】
					declareModel.setStatus(DERP_ORDER.SALEDECLARE_STATUS_002);
				}
				saleDeclareOrderDao.modify(declareModel);
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "提交清关资料", null, null);
				break;
			case "4":
				//更新销售预申报单 确认清关资料 时间
				declareModel.setCustomsConfirmDate(TimeUtils.parseFullTime(orderTime));
				declareModel.setModifier(user.getId());
				declareModel.setModifierUsername(user.getName());
				declareModel.setModifyDate(TimeUtils.getNow());
				if(DERP_ORDER.SALEDECLARE_STATUS_001.equals(declareModel.getStatus())) {//如果状态为 【待打托】 需更新状态为 【待清关】
					declareModel.setStatus(DERP_ORDER.SALEDECLARE_STATUS_002);
				}
				saleDeclareOrderDao.modify(declareModel);

				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "确认清关资料", null, null);
				break;
			case "5":
				//更新销售预申报单 确认申报时间
				declareModel.setConfirmDeclarationDate(TimeUtils.parseFullTime(orderTime));
				declareModel.setModifier(user.getId());
				declareModel.setModifierUsername(user.getName());
				declareModel.setModifyDate(TimeUtils.getNow());
				//如果状态待打托或待清关；更新状态为待物流委托
				if(DERP_ORDER.SALEDECLARE_STATUS_001.equals(declareModel.getStatus()) || DERP_ORDER.SALEDECLARE_STATUS_002.equals(declareModel.getStatus())) {
					declareModel.setStatus(DERP_ORDER.SALEDECLARE_STATUS_003);
				}
				saleDeclareOrderDao.modify(declareModel);

				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "确认申报时间", null, null);
				break;
			case "6":
				//更新销售预申报单 预约物流 时间
				declareModel.setLogisticsCommissionDate(TimeUtils.parseFullTime(orderTime));
				declareModel.setModifier(user.getId());
				declareModel.setModifierUsername(user.getName());
				declareModel.setModifyDate(TimeUtils.getNow());
				//如果预申报单当前状态是待打托或待清关；更新状态为待物流委托
				if(DERP_ORDER.SALEDECLARE_STATUS_001.equals(declareModel.getStatus()) || DERP_ORDER.SALEDECLARE_STATUS_002.equals(declareModel.getStatus())) {
					declareModel.setStatus(DERP_ORDER.SALEDECLARE_STATUS_003);
				}
				saleDeclareOrderDao.modify(declareModel);

				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "预约物流", null, null);
				break;
		}
	}

	@Override
	public void saveSaleDeclareOut(SaleDeclareOrderDTO dto, User user ,String deliverDateStr) throws Exception {
		deliverDateStr = deliverDateStr.trim() +" 00:00:00";
		// 出库日期必须不能大于当前日期
		if(TimeUtils.daysBetween(TimeUtils.parse(deliverDateStr, "yyyy-MM-dd"), new Date()) < 0) {
			throw new RuntimeException("选择的日期必须小于或等于当前日期") ;
		}
		//获取销售预申报单
		SaleDeclareOrderModel declareModel = saleDeclareOrderDao.searchById(dto.getId());

		// 获取仓库信息
		Map<String, Object> depotParams = new HashMap<String, Object>();
		depotParams.put("depotId", declareModel.getOutDepotId());
		DepotInfoMongo depot = depotInfoMongoDao.findOne(depotParams);

		// 非中转仓 确认申报时间为空 不允许打托出库
		if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType()) && declareModel.getConfirmDeclarationDate() == null) {
			throw new RuntimeException("请确认申报时间");
		}

		//查询是否关账
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(user.getMerchantId());
		closeAccountsMongo.setDepotId(declareModel.getOutDepotId());
		closeAccountsMongo.setBuId(declareModel.getBuId());
		String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 关账下个月日期大于 入库日期
			if (Timestamp.valueOf(deliverDateStr).getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("出库日期必须大于关账日期") ;
			}
		}
		SaleOutDepotModel tempSaleOutModel = new SaleOutDepotModel();
		tempSaleOutModel.setSaleDeclareOrderId(declareModel.getId());
		List<SaleOutDepotModel> tempSaleOutList = saleOutDepotDao.list(tempSaleOutModel);
		if(tempSaleOutList != null && tempSaleOutList.size() > 0){
			throw new RuntimeException("销售预申报单已存在销售出库单，打托出库失败") ;
		}

		//出库仓库，校验仓库是否批次强校验
		int outZeroNum = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
		Map<String , SaleOutDepotModel> saleCodeOrderMap = new HashMap<String , SaleOutDepotModel>() ;
		Map<String , List<SaleOutDepotItemModel>> saleOutCodeOrderItemMap = new HashMap<String , List<SaleOutDepotItemModel>>();
		Map<String , String> saleOutTallyingUnitMap = new HashMap<String , String>();
		List<SaleDeclareOrderItemDTO> declareDTOItemList = dto.getItemList();
		Map<String, List<SaleDeclareOrderItemDTO>> declareDTOItemMap = declareDTOItemList.stream().collect(Collectors.groupingBy(d->d.getSaleOrderCode()+"_"+d.getSaleItemId()+"_"
				+(StringUtils.isBlank(d.getBatchNo())? "":d.getBatchNo() )+"_"+(StringUtils.isBlank(d.getProductionDate())? "":d.getProductionDate())
				+"_"+(StringUtils.isBlank(d.getOverdueDate())? "":d.getOverdueDate())));
		for(String key : declareDTOItemMap.keySet()) {
			List<SaleDeclareOrderItemDTO> queryDeclareItemList = declareDTOItemMap.get(key);
			SaleDeclareOrderItemDTO itemDTO = queryDeclareItemList.get(0);
			Integer transferNum = queryDeclareItemList.stream().mapToInt(SaleDeclareOrderItemDTO:: getTransferNum).sum();
			Integer wornNum = queryDeclareItemList.stream().mapToInt(SaleDeclareOrderItemDTO :: getWornNum).sum() ;
			Integer num = queryDeclareItemList.stream().mapToInt(SaleDeclareOrderItemDTO :: getNum).sum();
			if (transferNum == 0 && wornNum == 0) {
				outZeroNum++;
				continue;
			}
			//获取销售订单信息
			SaleOrderModel saleOderModel = saleOrderDao.searchById(itemDTO.getSaleOrderId());
			// 新增销售订单出库表
			if(!saleCodeOrderMap.containsKey(itemDTO.getSaleOrderCode())) {
				SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
				sOutDepotModel.setSaleOrderId(itemDTO.getSaleOrderId());// 销售ID
				sOutDepotModel.setMerchantId(user.getMerchantId());// 商家ID
				sOutDepotModel.setMerchantName(user.getMerchantName());// 商家名称
				sOutDepotModel.setOutDepotId(saleOderModel.getOutDepotId());// 出仓库id
				sOutDepotModel.setOutDepotName(saleOderModel.getOutDepotName());// 出仓库名称
				sOutDepotModel.setCustomerId(saleOderModel.getCustomerId());// 客户id
				sOutDepotModel.setCustomerName(saleOderModel.getCustomerName());// 客户名称
				sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);// 017-待出库 018-已出库 027-出库中
				sOutDepotModel.setSaleOrderCode(itemDTO.getSaleOrderCode());// 销售订单编号
				sOutDepotModel.setSaleType(saleOderModel.getBusinessModel());// 销售类型 1 购销-整批结算 2 代销 3 采销执行 4.购销-实销实结
				sOutDepotModel.setPoNo(itemDTO.getPoNo());
				sOutDepotModel.setLbxNo(saleOderModel.getLbxNo());
				sOutDepotModel.setBuId(saleOderModel.getBuId());// 事业部
				sOutDepotModel.setBuName(saleOderModel.getBuName());
				sOutDepotModel.setDeliverDate(TimeUtils.parseDay(deliverDateStr));
				sOutDepotModel.setCreater(user.getId());
				sOutDepotModel.setCreateDate(TimeUtils.getNow());
				sOutDepotModel.setCurrency(saleOderModel.getCurrency());
				sOutDepotModel.setSaleDeclareOrderId(declareModel.getId());	//预申报单id
				sOutDepotModel.setSaleDeclareOrderCode(declareModel.getCode());//预申报单编码

				saleCodeOrderMap.put(itemDTO.getSaleOrderCode(), sOutDepotModel);
				saleOutTallyingUnitMap.put(itemDTO.getSaleOrderCode(), saleOderModel.getTallyingUnit());
			}

			//校验 出库数量 是否等于 申报数量
//			Integer outNum = itemDTO.getTransferNum() + itemDTO.getWornNum();
//			if(!outNum.equals(itemDTO.getNum())) {
//				throw new RuntimeException("销售单号："+itemDTO.getSaleOrderCode()+"商品货号："+itemDTO.getGoodsNo()+" 出库数量不等于申报数量") ;
//			}

			String transferBatchNo = itemDTO.getBatchNo();
			String productionDate = itemDTO.getProductionDate();
			String overDueDate = itemDTO.getOverdueDate();
			if (depot.getBatchValidation()==null || DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())){
				if(StringUtils.isBlank(transferBatchNo)) {
					throw new RuntimeException("出库仓为批次强校验，批次号不能为空") ;
				}
				if(StringUtils.isBlank(productionDate)) {
					throw new RuntimeException("出库仓为批次强校验，生产日期不能为空") ;
				}
				if(!TimeUtils.isYmdDate(productionDate)) {
					throw new RuntimeException("销售单号："+itemDTO.getSaleOrderCode()+"商品货号："+itemDTO.getGoodsNo()+"生产日期格式有误，正确格式为:yyyy-MM-dd") ;
				}
				if(StringUtils.isBlank(overDueDate)) {
					throw new RuntimeException("出库仓为批次强校验，失效日期不能为空") ;
				}
				if(!TimeUtils.isYmdDate(overDueDate)) {
					throw new RuntimeException("销售单号："+itemDTO.getSaleOrderCode()+"商品货号："+itemDTO.getGoodsNo()+"失效日期格式有误，正确格式为:yyyy-MM-dd") ;
				}
			}

			SaleOutDepotItemModel itemSaveModel= new SaleOutDepotItemModel();
			itemSaveModel.setGoodsId(itemDTO.getGoodsId());
			itemSaveModel.setGoodsCode(itemDTO.getGoodsCode());
			itemSaveModel.setGoodsName(itemDTO.getGoodsName());
			itemSaveModel.setGoodsNo(itemDTO.getGoodsNo());
			itemSaveModel.setTransferNum(transferNum);
			itemSaveModel.setWornNum(wornNum);
			itemSaveModel.setBarcode(itemDTO.getBarcode());
			itemSaveModel.setSaleNum(num);
			itemSaveModel.setSaleItemId(itemDTO.getSaleItemId());
			Map<String, Object> merchandiseParam = new HashMap<>();
			merchandiseParam.put("merchandiseId", itemDTO.getGoodsId());
			MerchandiseInfoMogo mogoDaoOne = merchandiseInfoMogoDao.findOne(merchandiseParam);
			itemSaveModel.setCommbarcode(mogoDaoOne.getCommbarcode());
			itemSaveModel.setPrice(itemDTO.getSalePrice());
			itemSaveModel.setTransferBatchNo(itemDTO.getBatchNo());  // 批次号
			if(StringUtils.isNotBlank(itemDTO.getProductionDate())){
				itemSaveModel.setProductionDate(TimeUtils.parse(itemDTO.getProductionDate(),"yyyy-MM-dd"));  // 生产日期
			}
			if(StringUtils.isNotBlank(itemDTO.getOverdueDate())){
				itemSaveModel.setOverdueDate(TimeUtils.parse(itemDTO.getOverdueDate(),"yyyy-MM-dd"));   // 失效日期
			}

			List<SaleOutDepotItemModel> itemList = saleOutCodeOrderItemMap.get(itemDTO.getSaleOrderCode());
			if(itemList == null) {
				itemList = new ArrayList<SaleOutDepotItemModel>() ;
			}
			itemList.add(itemSaveModel) ;
			saleOutCodeOrderItemMap.put(itemDTO.getSaleOrderCode(), itemList) ;
		}
		if(outZeroNum == declareDTOItemMap.keySet().size()) {
			throw new RuntimeException("出库失败，至少一个货号出库数量大于0") ;
		}
		//校验 出库数量 是否等于 申报数量
		Map<String,List<SaleDeclareOrderItemDTO>> saleDeclareOrderItemMap = dto.getItemList().stream().collect(Collectors.groupingBy(i -> i.getSaleOrderCode()+"&"+i.getGoodsNo()+"&"+i.getSaleItemId()));
		for(String saleCodeAndGoodsNo : saleDeclareOrderItemMap.keySet()) {
			String saleCode = saleCodeAndGoodsNo.split("&")[0];
			String goodsNo = saleCodeAndGoodsNo.split("&")[1];
			List<SaleDeclareOrderItemDTO> goodsList = saleDeclareOrderItemMap.get(saleCodeAndGoodsNo);
			Integer totalTransferNum = goodsList.stream().filter(s->s.getTransferNum() != null).mapToInt(SaleDeclareOrderItemDTO :: getTransferNum).sum();//好品量
			Integer totalWornNum = goodsList.stream().filter(s->s.getWornNum() != null).mapToInt(SaleDeclareOrderItemDTO :: getWornNum).sum();//坏品量
			Integer declareNum = goodsList.get(0).getNum();
			//校验 出库数量 是否等于 申报数量
			Integer outNum = totalTransferNum + totalWornNum;
			if(outNum.intValue() != declareNum.intValue()) {
				throw new RuntimeException("销售单号："+saleCode +"，商品货号："+goodsNo+" 出库数量不等于申报数量") ;
			}

		}
		//生成销售出库单
		Map<String, Object> params = new HashMap<String, Object>();
		for (String saleCode : saleCodeOrderMap.keySet()) {
			SaleOutDepotModel saleOutDepotModel = saleCodeOrderMap.get(saleCode);
			saleOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK));
			Long saleOutDepotId =saleOutDepotDao.save(saleOutDepotModel);

			List<SaleOutDepotItemModel> itemList = saleOutCodeOrderItemMap.get(saleCode);
			for (SaleOutDepotItemModel item : itemList) {
				item.setSaleOutDepotId(saleOutDepotId);
				saleOutDepotItemDao.save(item) ;
			}

			//修改销售订单状态
			SaleOrderModel updateSaleOrderModel = new SaleOrderModel();
			updateSaleOrderModel.setId(saleOutDepotModel.getSaleOrderId());
			updateSaleOrderModel.setState(DERP_ORDER.SALEORDER_STATE_027);
			saleOrderDao.modify(updateSaleOrderModel);

			// 释放并减少冻结量
			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
			List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();

			String inventoryUnit = "";
			if (DERP.ORDER_TALLYINGUNIT_00.equals(saleOutTallyingUnitMap.get(saleCode))) {
				inventoryUnit = DERP.INVENTORY_UNIT_0;
			} else if (DERP.ORDER_TALLYINGUNIT_01.equals(saleOutTallyingUnitMap.get(saleCode))) {
				inventoryUnit = DERP.INVENTORY_UNIT_1;
			} else if (DERP.ORDER_TALLYINGUNIT_02.equals(saleOutTallyingUnitMap.get(saleCode))) {
				inventoryUnit = DERP.INVENTORY_UNIT_2;
			}

			//获取销售订单信息
			SaleOrderModel saleOderModel = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId());
			String now = sdf1.format(new Date());
			//中转仓没有冻结，所以不用释放
			if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
				for (SaleOutDepotItemModel item : itemList) {
					InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();

					inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
					inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());
					inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());

					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
						inventoryFreezeGoodsListJson.setUnit(inventoryUnit);
					}
					inventoryFreezeGoodsListJson.setDivergenceDate(now);

					Integer transferNum = item.getTransferNum()==null?0:item.getTransferNum();// 好品数量
					Integer wornNum = item.getWornNum()==null?0:item.getWornNum();	// 坏品数量

					inventoryFreezeGoodsListJson.setNum(transferNum + wornNum);// 释放冻结量=销售出库数量
					inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
				}
				inventoryFreezeRootJson.setMerchantId(saleOutDepotModel.getMerchantId().toString());
				inventoryFreezeRootJson.setMerchantName(saleOutDepotModel.getMerchantName());
				inventoryFreezeRootJson.setDepotId(saleOutDepotModel.getOutDepotId().toString());
				inventoryFreezeRootJson.setDepotName(saleOutDepotModel.getOutDepotName());
				inventoryFreezeRootJson.setOrderNo(saleOutDepotModel.getCode());
				inventoryFreezeRootJson.setBusinessNo(saleOutDepotModel.getSaleOrderCode());
				inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
				inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
				inventoryFreezeRootJson.setSourceDate(now);
				inventoryFreezeRootJson.setOperateType("1");// 操作类型 0 冻结 1解冻
				// 有表体才需要推库存
				if (inventoryFreezeGoodsListJsonList != null && inventoryFreezeGoodsListJsonList.size() > 0) {
					inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
				}
			}

			// 扣减销售出库库存量
			Date date = new Date();
			for (SaleOutDepotItemModel item : itemList) {
				// 好品
				if (item.getTransferNum() != null && item.getTransferNum() > 0) {
					InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

					invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
					invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
					invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
					invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
					invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());


					invetAddOrSubtractGoodsListJson.setType("0"); // 商品分类 （0 好品 1坏品 ）
					invetAddOrSubtractGoodsListJson.setNum(item.getTransferNum()); // 好品数量
					invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
					invetAddOrSubtractGoodsListJson.setBatchNo(item.getTransferBatchNo());
					// 计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if (item.getOverdueDate() != null) {
						isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());// 判断是否过期是否过期（0是 1否）
					}
					invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);// 是否过期 （0 是 1 否）

					if (item.getProductionDate() != null) {
						Date productionDateTimestamp = item.getProductionDate();
						invetAddOrSubtractGoodsListJson.setProductionDate(sdf.format(productionDateTimestamp));
					}
					if (item.getOverdueDate() != null) {
						Date overdueDateTimestamp = item.getOverdueDate();
						invetAddOrSubtractGoodsListJson.setOverdueDate(sdf.format(overdueDateTimestamp));
						Timestamp currentDate = new Timestamp(date.getTime());
						if (currentDate.before(item.getOverdueDate())) {
							// currentDate 时间比 item.getOverdueDate() 时间早(未过期)
							invetAddOrSubtractGoodsListJson.setIsExpire("1");
						} else {
							invetAddOrSubtractGoodsListJson.setIsExpire("0");
						}
					}
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
						invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
					}
					invetAddOrSubtractGoodsListJson.setStockLocationTypeId(saleOderModel.getStockLocationTypeId() == null ? "" : saleOderModel.getStockLocationTypeId().toString());
					invetAddOrSubtractGoodsListJson.setStockLocationTypeName(saleOderModel.getStockLocationTypeName());
					invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
				}
				// 坏品
				if (item.getWornNum() != null && item.getWornNum() > 0) {
					InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

					invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
					invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
					invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
					invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
					invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());


					invetAddOrSubtractGoodsListJson.setType("1"); // 商品分类 （0 好品 1坏品 ）
					invetAddOrSubtractGoodsListJson.setNum(item.getWornNum()); // 坏品数量
					invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
					invetAddOrSubtractGoodsListJson.setBatchNo(item.getTransferBatchNo());
					// 计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if (item.getOverdueDate() != null) {
						isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());// 判断是否过期是否过期（0是 1否）
					}
					invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);// 是否过期 （0 是 1 否）
					if (item.getProductionDate() != null) {
						Date productionDateTimestamp = item.getProductionDate();
						invetAddOrSubtractGoodsListJson.setProductionDate(sdf.format(productionDateTimestamp));
					}
					if (item.getOverdueDate() != null) {
						Date overdueDateTimestamp = item.getOverdueDate();
						invetAddOrSubtractGoodsListJson.setOverdueDate(sdf.format(overdueDateTimestamp));
						Timestamp currentDate = new Timestamp(date.getTime());
						if (currentDate.before(item.getOverdueDate())) {
							// currentDate 时间比 item.getOverdueDate() 时间早(未过期)
							invetAddOrSubtractGoodsListJson.setIsExpire("1");
						} else {
							invetAddOrSubtractGoodsListJson.setIsExpire("0");
						}
					}
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
						invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);

					}
					invetAddOrSubtractGoodsListJson.setStockLocationTypeId(saleOderModel.getStockLocationTypeId() == null ? "" : saleOderModel.getStockLocationTypeId().toString());
					invetAddOrSubtractGoodsListJson.setStockLocationTypeName(saleOderModel.getStockLocationTypeName());
					invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
				}
			}

			invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTopic());
			invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTags());
			Map<String, Object> customParam = new HashMap<String, Object>();
			customParam.put("saleId", saleOutDepotModel.getSaleOrderId()); // 销售订单ID
			invetAddOrSubtractRootJson.setCustomParam(customParam);
			invetAddOrSubtractRootJson.setMerchantId(saleOutDepotModel.getMerchantId().toString());
			invetAddOrSubtractRootJson.setMerchantName(saleOutDepotModel.getMerchantName());
			invetAddOrSubtractRootJson.setTopidealCode(user.getTopidealCode()); // 公司编码
			invetAddOrSubtractRootJson.setDepotId(saleOutDepotModel.getOutDepotId().toString());
			invetAddOrSubtractRootJson.setDepotName(saleOutDepotModel.getOutDepotName());
			invetAddOrSubtractRootJson.setDepotCode(depot.getCode());
			invetAddOrSubtractRootJson.setDepotType(depot.getType());
			invetAddOrSubtractRootJson.setIsTopBooks(depot.getIsTopBooks());
			invetAddOrSubtractRootJson.setOrderNo(saleOutDepotModel.getCode());
			invetAddOrSubtractRootJson.setBusinessNo(saleOutDepotModel.getSaleOrderCode()); // 销售订单Code
			invetAddOrSubtractRootJson.setBuId(String.valueOf(saleOutDepotModel.getBuId())); // 事业部
			invetAddOrSubtractRootJson.setBuName(saleOutDepotModel.getBuName());
			invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
			if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOutDepotModel.getSaleType())
					|| DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOutDepotModel.getSaleType())) {
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
			} else if (DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOutDepotModel.getSaleType())) {
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0021);
			}
			invetAddOrSubtractRootJson.setSourceDate(sdf.format(new Date())); // 单据时间
			invetAddOrSubtractRootJson.setDivergenceDate(sdf1.format(saleOutDepotModel.getDeliverDate())); // 发货时间

			String now2 = sdf2.format(saleOutDepotModel.getDeliverDate()); // 发货时间
			invetAddOrSubtractRootJson.setOwnMonth(now2); // 归属月份
			invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

			//中转仓没有冻结，所以不用释放
			if (!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
				// 释放冻结库存
				rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(),
						MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),
						MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
			}
			// 减库存
			rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		}

		//修改预申报单状态
		declareModel.setStatus(DERP_ORDER.SALEDECLARE_STATUS_008);
		declareModel.setDeliverDate(TimeUtils.parseDay(deliverDateStr));
		saleDeclareOrderDao.modify(declareModel);

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "打托出库", null, null);

	}

	@Override
	public Map<String, Object> exportLogisticsDelegation(Long id) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
		dto.setId(id);
		dto = saleDeclareOrderDao.searchDetail(dto);

		LogisticsAttorneyModel logisticsAttorneyModel = new LogisticsAttorneyModel() ;
		logisticsAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_2);
		logisticsAttorneyModel.setOrderId(id);
		logisticsAttorneyModel = logisticsAttorneyDao.searchByModel(logisticsAttorneyModel);

		SaleDeclareOrderItemDTO itemModel = new SaleDeclareOrderItemDTO();
		itemModel.setDeclareOrderId(id);
		List<SaleDeclareOrderItemDTO> itemList = saleDeclareOrderItemDao.listDTO(itemModel);
		double netWeight = itemList.stream().mapToDouble(SaleDeclareOrderItemDTO::getNetWeightSum).sum();

		result.put("transport", dto.getTransport());
		result.put("merchantName", dto.getMerchantName());
		result.put("buName", dto.getBuName());
		result.put("inPlatformCustoms", dto.getInCustomsName());
		result.put("netWeight",netWeight);
		result.put("firstGoodName", itemList.get(0).getGoodsName());
		result.put("createDateStr", TimeUtils.format(new Date(), "yyyy-MM-dd"));
		result.put("portDestNoLabel", dto.getPortDesLabel().substring(dto.getPortDesLabel().indexOf("：")+1));
		result.put("destinationPortName", dto.getDestinationPort());
		result.put("contractNo",dto.getContractNo());
		result.put("InvoiceNo",dto.getInvoiceNo());
		result.put("shipDate",dto.getShipDate() != null ? TimeUtils.format(dto.getShipDate(), "yyyy-MM-dd") : "");
		result.put("torrNum",dto.getTorrNum());
		result.put("billWeight",dto.getBillWeight());
		result.put("remark",dto.getRemark());
		result.put("carrierInformationTempId",logisticsAttorneyModel.getCarrierInformationTempId());
		result.put("carrierInformationTempName",logisticsAttorneyModel.getCarrierInformationTempName());
		result.put("carrierInformationTempDetails",logisticsAttorneyModel.getCarrierInformationText());
		result.put("shipperTempId",logisticsAttorneyModel.getShipperTempId());
		result.put("shipperTempName",logisticsAttorneyModel.getShipperTempName());
		result.put("shipperTempDetails",logisticsAttorneyModel.getShipperText());
		result.put("consigneeTempId",logisticsAttorneyModel.getConsigneeTempId());
		result.put("consigneeTempName",logisticsAttorneyModel.getConsigneeTempName());
		result.put("consigneeTempDetails",logisticsAttorneyModel.getConsigneeText());
		result.put("notifierTempId",logisticsAttorneyModel.getNotifierTempId());
		result.put("notifierTempName",logisticsAttorneyModel.getNotifierTempName());
		result.put("notifierTempDetails",logisticsAttorneyModel.getNotifierText());
		result.put("dockingTempId",logisticsAttorneyModel.getDockingTempId());
		result.put("dockingTempName",logisticsAttorneyModel.getDockingTempName());
		result.put("dockingTempDetails",logisticsAttorneyModel.getDockingText());
		result.put("dockingTempDetails",logisticsAttorneyModel.getDockingText());
		result.put("motorcycleType",dto.getMotorcycleType());

		return result;
	}

	@Override
	public void delSaleDeclareOrder(String ids) throws Exception {
		List<Long> orderIds = StrUtils.parseIds(ids);
		//待打托状态且接口状态为未推接口
		for(Long orderId : orderIds) {
			SaleDeclareOrderModel model = saleDeclareOrderDao.searchById(orderId);
			// 仅对“未出库”的单据删除
			if (DERP_ORDER.SALEDECLARE_STATUS_004.equals(model.getStatus())) {
				throw new DerpException("操作失败，销售预申报单："+model.getCode()+"已出库，无法删除");
			}
			if (DERP_ORDER.SALEDECLARE_STATUS_005.equals(model.getStatus())) {
				throw new DerpException("操作失败，销售预申报单："+model.getCode()+"部分上架，无法删除");
			}
			if (DERP_ORDER.SALEDECLARE_STATUS_007.equals(model.getStatus())) {
				throw new DerpException("操作失败，销售预申报单："+model.getCode()+"已上架，无法删除");
			}

			// 仅对“未推接口”的单据删除
			if (DERP_ORDER.SALEDECLARE_APISTATUS_1.equals(model.getApiStatus())) {
				throw new DerpException("操作失败，销售预申报单："+model.getCode()+"已推接口，无法删除");
			}
			model.setStatus(DERP.DEL_CODE_006);
			saleDeclareOrderDao.modify(model);

			/**
			 * 若删除成功，销售订单还有关联的预申单，则销售订单列表的预申报标识为“是”
			 * 若删除成功，销售订单没有关联的预申报单，则更新销售订单列表的预申报标识为“否”
			 */
			String[] saleIds = model.getSaleOrderIds().split(",");
			for(String saleId:saleIds) {
				SaleDeclareOrderDTO queryDTO = new SaleDeclareOrderDTO();
				queryDTO.setSaleOrderIds(saleId);
				List<SaleDeclareOrderDTO> queryList = saleDeclareOrderDao.listSaleDeclareOrder(queryDTO);
				if(queryList == null || queryList.size() < 1) {
					SaleOrderModel saleModel = new SaleOrderModel();
					saleModel.setId(Long.valueOf(saleId));
					saleModel.setIsGenerateDeclare(DERP_ORDER.SALEDECLARE_ISGENERATE_0);
					saleOrderDao.modify(saleModel);
				}
			}
		}

	}

	@Override
	public List<SaleDeclareOrderDTO> listSaleDeclareOrder(SaleDeclareOrderDTO dto, User user) throws Exception {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				return new ArrayList<>();
			}

			dto.setBuList(buIds);
		}
		return saleDeclareOrderDao.listSaleDeclareOrder(dto);
	}
	/**
	 * 编辑页面选择商品弹窗展示
	 * @return
	 */
	public SaleDeclareOrderItemDTO getSalePopupListByPage(MerchandiseForm form) throws Exception{
//		List<SaleDeclareOrderItemDTO> itemList = new ArrayList<SaleDeclareOrderItemDTO>();
		String unNeedIds = "";
		// 解析json
		// 解析json
		if(org.apache.commons.lang3.StringUtils.isNotBlank(form.getUnNeedGoodsJson())){
			JSONObject json = JSONObject.fromObject(form.getUnNeedGoodsJson());
			unNeedIds = (String) json.get("saleItemId");
		}

		if(StringUtils.isNotBlank(form.getOrderCode())) {
			SaleOrderModel saleModel = new SaleOrderModel();
			saleModel.setCode(form.getOrderCode());
			saleModel = saleOrderDao.searchByModel(saleModel);
			form.setOrderIds(saleModel.getId()+"");
		}
		SaleDeclareOrderItemDTO queryDeclareItemDTO = new SaleDeclareOrderItemDTO();
		queryDeclareItemDTO.setSaleOrderIds(form.getOrderIds());
		queryDeclareItemDTO.setUnNeedIds(unNeedIds);
		queryDeclareItemDTO.setGoodsNo(form.getGoodsNo());
		queryDeclareItemDTO.setGoodsName(form.getGoodsName());
		queryDeclareItemDTO.setBarcode(form.getBarcode());
		queryDeclareItemDTO = saleDeclareOrderItemDao.getSalePopupListByPage(queryDeclareItemDTO);
		List<SaleDeclareOrderItemDTO> queryDeclareItemList = queryDeclareItemDTO.getList();
		for(SaleDeclareOrderItemDTO saleItem : queryDeclareItemList) {
			SaleOrderModel querySaleModel = saleOrderDao.searchById(saleItem.getSaleOrderId());
//			SaleDeclareOrderItemDTO saleDeclareOrderItemDTO = new SaleDeclareOrderItemDTO();
			saleItem.setSaleOrderId(querySaleModel.getId());
			saleItem.setSaleOrderCode(querySaleModel.getCode());
			saleItem.setPoNo(querySaleModel.getPoNo());
			saleItem.setBrandName("境外品牌(其他)");
			saleItem.setBoxNum(0);
			saleItem.setMerchantId(querySaleModel.getMerchantId());
			saleItem.setMerchantName(querySaleModel.getMerchantName());

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("saleItemId",saleItem.getSaleItemId());
			Integer outNum = saleOutDepotDao.getTotalNumByOrderGoods(paramMap);//商品总出库量
			Integer stayDeclareNum = saleItem.getNum() - outNum;//默认 申报数量=销售数量-累计出库数量
			saleItem.setNum(stayDeclareNum);
			//获取商品信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", saleItem.getGoodsId());
			MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(params);

			Double grossWeightSum = new BigDecimal(merchandiseMongo.getGrossWeight()).multiply(new BigDecimal(stayDeclareNum))
					.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
			Double netWeightSum = new BigDecimal(merchandiseMongo.getNetWeight()).multiply(new BigDecimal(stayDeclareNum))
					.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
			saleItem.setGrossWeight(merchandiseMongo.getGrossWeight());
			saleItem.setGrossWeightSum(grossWeightSum);
			saleItem.setNetWeight(merchandiseMongo.getNetWeight());
			saleItem.setNetWeightSum(netWeightSum);
			saleItem.setConstituentRatio(merchandiseMongo.getComponent());

			// 根据商品id查询商品母品牌 获取申报系数
			Double priceDeclareRatio = 1.0;
			BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(saleItem.getGoodsId());
			if ((brandSuperiorMongo != null && brandSuperiorMongo.getPriceDeclareRatio() != null)) {
				priceDeclareRatio = brandSuperiorMongo.getPriceDeclareRatio();
			}

			// 备案单价*商家商品备案价申报系数
			BigDecimal declarePrice = merchandiseMongo.getFilingPrice().multiply(new BigDecimal(priceDeclareRatio)).setScale(5, BigDecimal.ROUND_HALF_UP);
			saleItem.setPrice(declarePrice);
			saleItem.setAmount(declarePrice.multiply(new BigDecimal(stayDeclareNum)).setScale(2, BigDecimal.ROUND_HALF_UP));

			if (merchandiseMongo.getUnit() != null) {
				// 根据单位id获取单位信息
				Map<String, Object> unitParams = new HashMap<String, Object>();
				unitParams.put("unitId", merchandiseMongo.getUnit());
				UnitMongo unit = unitMongoDao.findOne(unitParams);
				if (unit != null) {
					saleItem.setUnitName(unit.getName());// 单位
				}
			}
			//平台关区
			Map<String, Object> merchandiseCustomsRelParams = new HashMap<String, Object>();
			merchandiseCustomsRelParams.put("goodsId", saleItem.getGoodsId());
			List<MerchandiseCustomsRelMongo> merchandiseCustomsRelList = merchandiseCustomsRelMongoDao.findAll(merchandiseCustomsRelParams);
			if(merchandiseCustomsRelList != null && merchandiseCustomsRelList.size() > 0) {
				List<String>  customsRelNames = merchandiseCustomsRelList.stream().map(MerchandiseCustomsRelMongo::getCustomsAreaName).collect(Collectors.toList());
				saleItem.setCustomsArea(StringUtils.join(customsRelNames,","));
			}

//			itemList.add(saleDeclareOrderItemDTO);
		}
		return queryDeclareItemDTO;
	}

	@Override
	public Map<String, Object> exportCustomsDeclares(Long id, List<Long> fileTempIds,String type , User user) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		// 仓库模板
		if (fileTempIds == null || fileTempIds.isEmpty()) {
			return null;
		}

		SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
		dto.setId(id);
		dto = saleDeclareOrderDao.searchDetail(dto);
		if (dto == null) {
			return null;
		}

		for (Long fileTempId : fileTempIds) {
			FileTempModel fileTempModel = fileTempDao.searchById(fileTempId);
			if (fileTempModel == null || StringUtils.isEmpty(fileTempModel.getToUrl())) {
				continue;
			}

			// 南沙仓模板（之前）
			if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_ZONGHEYICANG.equals(fileTempModel.getToUrl())) {
				Map<String, Object> firstCustomsInfo = getZONGHEYICANGFirstCustomsInfoDTO(id, user);
				resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
			}else if(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDNANSHA.equals(fileTempModel.getToUrl())){// 京东南沙模板
				Map<String, Object> firstCustomsInfo = getJDNANSHAFirstCustomsInfoDTO(id,  user);
				resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
			} else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPTIANJIN.equals(fileTempModel.getToUrl()) || // 天津模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPONLINE.equals(fileTempModel.getToUrl()) ||// 唯品线上模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPQINGDAO.equals(fileTempModel.getToUrl()) ||// 青岛模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPCHONGQING.equals(fileTempModel.getToUrl()) ||// 重庆模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU.equals(fileTempModel.getToUrl()) ||// 郑州模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPNANSHA.equals(fileTempModel.getToUrl()) ||// 唯品南沙模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHUANGPU.equals(fileTempModel.getToUrl()) ||// 京东黄埔模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDSHANGHAI.equals(fileTempModel.getToUrl()) ||// 京东上海模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDQINGDAO.equals(fileTempModel.getToUrl()) ||// 京东青岛模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDZHENGZHOU.equals(fileTempModel.getToUrl()) ||// 京东郑州模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDNINGBO.equals(fileTempModel.getToUrl()) ||// 京东宁波模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDXIAMEN.equals(fileTempModel.getToUrl()) ||// 京东厦门模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDTIANJIN.equals(fileTempModel.getToUrl()) ||// 京东天津模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDJINYI.equals(fileTempModel.getToUrl()) ||// 京东金义模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDHEBEI.equals(fileTempModel.getToUrl()) ||// 京东河北模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDINANSHA.equals(fileTempModel.getToUrl()) ||// 天猫南沙模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDININGBO.equals(fileTempModel.getToUrl()) ||// 天猫宁波模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDITIANJIN.equals(fileTempModel.getToUrl()) ||// 天猫天津模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_TDIZHENGZHOU.equals(fileTempModel.getToUrl()) ||// 天猫郑州模板
					DERP_ORDER.TEMP_CUSTOMSFILECONFIG_JDCHONGQING.equals(fileTempModel.getToUrl()) ){// 京东重庆模板
				Map<String, Object> firstCustomsInfo = getFirstCustomsInfoDTO(id,  user);
				resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
			}else if(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NBCIXI.equals(fileTempModel.getToUrl())){// 宁波慈溪模板
				Map<String, Object> firstCustomsInfo = getNBCIXIFirstCustomsInfoDTO(dto,  user);
				resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
			}
		}

		return resultMap;
	}
	/**
	 * 封装综合一仓模板实体
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getZONGHEYICANGFirstCustomsInfoDTO(Long id , User user) throws Exception {
		SaleDeclareOrderDTO saleDeclareOrderDTO = new SaleDeclareOrderDTO();
		saleDeclareOrderDTO.setId(id);
		saleDeclareOrderDTO = saleDeclareOrderDao.searchDetail(saleDeclareOrderDTO);
		SaleDeclareOrderItemDTO itemDTO = new SaleDeclareOrderItemDTO();
		itemDTO.setDeclareOrderId(id);
		List<SaleDeclareOrderItemDTO> itemList = saleDeclareOrderItemDao.listDTO(itemDTO);

		// 根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", saleDeclareOrderDTO.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantParams);

		// 根据“公司+仓库”获取关系表
		Map<String, Object> depotMerchantParams = new HashMap<String, Object>();
		depotMerchantParams.put("depotId", saleDeclareOrderDTO.getOutDepotId());
		depotMerchantParams.put("merchantId", saleDeclareOrderDTO.getMerchantId());
		DepotMerchantRelMongo depotMerchant = depotMerchantRelMongoDao.findOne(depotMerchantParams);

		// 根据“入库仓库+关区”获取关系表
		Map<String, Object> depotCustomsParams = new HashMap<String, Object>();
		depotCustomsParams.put("depotId", saleDeclareOrderDTO.getInDepotId());
		depotCustomsParams.put("customsAreaId", saleDeclareOrderDTO.getInCustomsId());
		DepotCustomsRelMongo depotcustoms = depotCustomsRelMongoDao.findOne(depotCustomsParams);

		Map<String,Object> dto = new HashMap<>();
		if (saleDeclareOrderDTO != null) {
			if (depotMerchant != null) {
				dto.put("signNo",depotMerchant.getContractCode());// 合同号
			}
			dto.put("contractNo",saleDeclareOrderDTO.getContractNo());// 合同号
			dto.put("signingDate",saleDeclareOrderDTO.getCreateDate());// 签订日期
			dto.put("orderDate",saleDeclareOrderDTO.getCreateDate());// 订单日期

			if (merchant != null) {
				dto.put("merchantName", merchant.getFullName());
				dto.put("merchantAddr", merchant.getRegisteredAddress());
			}

			dto.put("payRules",saleDeclareOrderDTO.getTradeTermsLabel());// 付款条约
			if (depotcustoms != null) {
				dto.put("eAddressee",depotcustoms.getRecCompanyEnname());// Consignee
			}
			dto.put("shipDate",saleDeclareOrderDTO.getShipDate());// 装船时间
			dto.put("portLoading",saleDeclareOrderDTO.getLoadPort());// 装货港
			dto.put("consignee","NO");// 境外收货人
			dto.put("invoiceNo",saleDeclareOrderDTO.getInvoiceNo());// 发票号
			dto.put("pack",saleDeclareOrderDTO.getPack());// 包装
			dto.put("destination",saleDeclareOrderDTO.getDestinationPort());//目的地

			LogisticsAttorneyModel logisticsAttorneyModel = new LogisticsAttorneyModel() ;
			logisticsAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_2);
			logisticsAttorneyModel.setOrderId(id);
			logisticsAttorneyModel = logisticsAttorneyDao.searchByModel(logisticsAttorneyModel);
			if(logisticsAttorneyModel != null){
				dto.put("addressee", logisticsAttorneyModel.getConsigneeTempName());// 收货人
			}

			Set<String> originCountrySet = new HashSet<String>();
			Integer totalNum = 0;
			Double totalGrossWeight = 0.0;
			Double totalNetWeight = 0.0;
			List<Map<String,Object>> customsItemList = new ArrayList<>();
			Map<String, List<SaleDeclareOrderItemDTO>> itemMap = itemList.stream().collect(Collectors.groupingBy(SaleDeclareOrderItemDTO :: getGoodsNo));
			for(String goodsNo: itemMap.keySet()) {
				List<SaleDeclareOrderItemDTO> items = itemMap.get(goodsNo);
				int totalItemNum = items.stream().mapToInt(SaleDeclareOrderItemDTO :: getNum).sum();
				Double totalItemNetWeight = items.stream().filter(net -> net.getNetWeightSum() !=null).mapToDouble(SaleDeclareOrderItemDTO :: getNetWeightSum).sum();
				Double totalItemGrossWeight = items.stream().filter(net -> net.getGrossWeightSum() !=null).mapToDouble(SaleDeclareOrderItemDTO :: getGrossWeightSum).sum();

				SaleDeclareOrderItemDTO item = items.get(0);
				Map<String,Object> customsItem = new HashMap<>();
				customsItem.put("goodsNo",item.getGoodsNo());
				customsItem.put("goodsName",item.getGoodsName());// 商品名称
				customsItem.put("num",totalItemNum);// 商品数量

				BigDecimal price = item.getPrice() == null ? BigDecimal.ZERO : item.getPrice();
				customsItem.put("price",price);// 单价
				customsItem.put("totalNetWeight",totalItemNetWeight);// 毛重
				customsItem.put("totalGrossWeight",totalItemGrossWeight);// 净重

				// 根据商品id获取商品信息
				Map<String, Object> merchandiseParams = new HashMap<String, Object>();
				merchandiseParams.put("merchandiseId", item.getGoodsId());
				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseParams);

				if (merchandise.getUnit() != null) {
					// 根据单位id获取单位信息
					Map<String, Object> unitParams = new HashMap<String, Object>();
					unitParams.put("unitId", merchandise.getUnit());
					UnitMongo unit = unitMongoDao.findOne(unitParams);
					if (unit != null) {
						customsItem.put("unit",unit.getName());// 单位
					}

				}
				// 根据国家id获取国家信息
				if (merchandise.getCountyId() != null) {
					Map<String, Object> countryParams = new HashMap<String, Object>();
					countryParams.put("countryOriginId", merchandise.getCountyId());
					CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(countryParams);
					if (countryOrigin != null) {
						customsItem.put("countryName",countryOrigin.getName());// 原产国
						originCountrySet.add(countryOrigin.getName());
					}
				}
				customsItem.put("goodsSpec",merchandise.getSpec());// 商品规格
				customsItem.put("constituentRatio",merchandise.getComponent());// 成分占比
				customsItemList.add(customsItem);

				totalNum = totalNum + totalItemNum;
				totalGrossWeight = totalGrossWeight + totalItemGrossWeight;
				totalNetWeight = totalNetWeight + totalItemNetWeight;
			}
			String originCountry = "";
			if (originCountrySet != null && originCountrySet.size() > 0) {
				originCountry = StringUtils.join(originCountrySet, ";");
			}

			dto.put("country",originCountry);// 原产国 用分号隔开
			dto.put("itemList",customsItemList);
			dto.put("totalNum",totalNum);
			dto.put("totalGrossWeight",totalGrossWeight);
			dto.put("totalNetWeight",totalNetWeight);
		}

		return dto;
	}

	/**
	 * 封装导出模板实体
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getFirstCustomsInfoDTO(Long id , User user) throws Exception {
		SaleDeclareOrderDTO saleDeclareOrderDTO = new SaleDeclareOrderDTO();
		saleDeclareOrderDTO.setId(id);
		saleDeclareOrderDTO = saleDeclareOrderDao.searchDetail(saleDeclareOrderDTO);
		SaleDeclareOrderItemDTO itemDTO = new SaleDeclareOrderItemDTO();
		itemDTO.setDeclareOrderId(id);
		List<SaleDeclareOrderItemDTO> itemList = saleDeclareOrderItemDao.listDTO(itemDTO);
		// 根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", saleDeclareOrderDTO.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantParams);

		// 根据“公司+仓库”获取关系表
		Map<String, Object> depotMerchantParams = new HashMap<String, Object>();
		depotMerchantParams.put("depotId", saleDeclareOrderDTO.getInDepotId());
		depotMerchantParams.put("merchantId", saleDeclareOrderDTO.getMerchantId());
		DepotMerchantRelMongo depotMerchant = depotMerchantRelMongoDao.findOne(depotMerchantParams);

		Map<String,Object> dto = new HashMap<>();
		if (saleDeclareOrderDTO != null) {
			if (depotMerchant != null) {
				dto.put("signNo",depotMerchant.getContractCode());// 合同号
			}
			dto.put("transportation",saleDeclareOrderDTO.getTransportLabel());// 运输方式
			dto.put("customsContractNo",saleDeclareOrderDTO.getContractNo());// 报关合同号（excel中的提运单号）);
			dto.put("poNo",saleDeclareOrderDTO.getPoNo());// po号（excel中的合同号）
			dto.put("contractNo",saleDeclareOrderDTO.getContractNo());
			dto.put("invoiceNo",saleDeclareOrderDTO.getInvoiceNo());// 发票号
			dto.put("payRules",saleDeclareOrderDTO.getTradeTerms());// 付款条约
			dto.put("pack",saleDeclareOrderDTO.getPack());// 包装方式
			// 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱
			String packType = DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList,saleDeclareOrderDTO.getTorrPackType());
			dto.put("palletMaterial",packType);// 托盘材质;
			dto.put("torrNum",saleDeclareOrderDTO.getTorrNum());// 托数
			dto.put("payRules",saleDeclareOrderDTO.getTradeTermsLabel());// 付款条约
			dto.put("orderDate",saleDeclareOrderDTO.getCreateDate());// 订单日期
			dto.put("inCustomsName",saleDeclareOrderDTO.getInCustomsName());// 目的地 入库关区
			dto.put("boxNum",saleDeclareOrderDTO.getBoxNum());// 箱数
			dto.put("customerName",saleDeclareOrderDTO.getCustomerName());// 客户名称全称
			dto.put("deliverDate",saleDeclareOrderDTO.getDeliverDate());// 发货时间
			dto.put("portLoading",saleDeclareOrderDTO.getLoadPort());// 装货港
			dto.put("lbxNo",saleDeclareOrderDTO.getLbxNo());// lbx编码
			dto.put("teu",saleDeclareOrderDTO.getTeu());
			dto.put("departurePort",saleDeclareOrderDTO.getDeparturePort());//启运港
			dto.put("shipDate",saleDeclareOrderDTO.getShipDate());//装船时间
			dto.put("destination",saleDeclareOrderDTO.getDestinationPort());//目的地
			dto.put("destinationPort",saleDeclareOrderDTO.getDestination());//香港目的港
			dto.put("ladingBillNo",saleDeclareOrderDTO.getLadingBill());//提单运号
			dto.put("shipper",saleDeclareOrderDTO.getShipper());//承运商

			if (merchant != null) {
				String shopName = "";
				if ("ERP31194100049".equals(merchant.getCode())) {// 健太
					shopName = "jdwqt";
				} else if ("ERP26143500022".equals(merchant.getCode())) {// 卓烨贸易
					shopName = "jdwtwt";
				} else if ("ERP31194300027".equals(merchant.getCode())) {// 广旺
					shopName = "jdwah";
				}
				dto.put("shopName",shopName);
				dto.put("merchantName",merchant.getFullName());// 当前公司全名
				dto.put("merchantAddr",merchant.getRegisteredAddress());// 当前公司地址
				dto.put("merchantEnAddr",merchant.getEnglishRegisteredAddress());// 当前公司地址（英文）
				dto.put("merchantTel",merchant.getTel());
				dto.put("merchantEmail",merchant.getEmail());
				dto.put("merchantEnName",merchant.getEnglishName());// 当前公司全名 英文
			}

			if(saleDeclareOrderDTO.getInCustomsId() != null) {
				Map<String, Object> depotCustomsRelParams = new HashMap<String, Object>();
				depotCustomsRelParams.put("depotId", saleDeclareOrderDTO.getInDepotId());
				depotCustomsRelParams.put("customsAreaId", saleDeclareOrderDTO.getInCustomsId());
				DepotCustomsRelMongo depotCustomsRel = depotCustomsRelMongoDao.findOne(depotCustomsRelParams);
				if(depotCustomsRel != null) {
					dto.put("onlineRegisterNo",depotCustomsRel.getOnlineRegisterNo());	//电子商务账号
					dto.put("addressee",depotCustomsRel.getRecCompanyName());//仓库收货人公司名称（中文）
					dto.put("addresseeAddr",depotCustomsRel.getAddress());//仓库详细地址
					dto.put("customsAreaCode",depotCustomsRel.getCustomsAreaCode());//关区代码

				}

			}
			if(saleDeclareOrderDTO.getOutCustomsId() != null) {
				Map<String, Object> depotCustomsRelParams = new HashMap<String, Object>();
				depotCustomsRelParams.put("depotId", saleDeclareOrderDTO.getOutDepotId());
				depotCustomsRelParams.put("customsAreaId", saleDeclareOrderDTO.getOutCustomsId());
				DepotCustomsRelMongo depotCustomsRel = depotCustomsRelMongoDao.findOne(depotCustomsRelParams);
				if(depotCustomsRel != null) {
					dto.put("outOnlineRegisterNo",depotCustomsRel.getOnlineRegisterNo());	//电子商务账号
					dto.put("outAddressee",depotCustomsRel.getRecCompanyName());//仓库收货人公司名称（中文）
					dto.put("outAddresseeAddr",depotCustomsRel.getAddress());//仓库详细地址
					dto.put("outCustomsAreaCode",depotCustomsRel.getCustomsAreaCode());//关区代码

				}

			}

			Double rate = null;
			// 销售币种不为人民币，则按订单创建日期取对应的汇率进行换算成人民币
			if (!DERP.CURRENCYCODE_CNY.equals(saleDeclareOrderDTO.getCurrency())) {
				Map<String, Object> exchangeRateParams = new HashMap<String, Object>();
				exchangeRateParams.put("origCurrencyCode", saleDeclareOrderDTO.getCurrency());
				exchangeRateParams.put("tgtCurrencyCode", DERP.CURRENCYCODE_CNY);
				exchangeRateParams.put("effectiveDate", TimeUtils.format(saleDeclareOrderDTO.getCreateDate(), "yyyy-MM-dd"));
				ExchangeRateMongo exchangeRateMongo = exchangeRateMongoDao.findOne(exchangeRateParams);
				if (exchangeRateMongo != null) {
					rate = exchangeRateMongo.getRate();
				}
			}
			Double usdRate = null;
			// 销售币种不为美元，则按订单创建日期取对应的汇率进行换算成美元
			if (!DERP.CURRENCYCODE_USD.equals(saleDeclareOrderDTO.getCurrency())) {
				Map<String, Object> exchangeRateParams = new HashMap<String, Object>();
				exchangeRateParams.put("origCurrencyCode", saleDeclareOrderDTO.getCurrency());
				exchangeRateParams.put("tgtCurrencyCode", DERP.CURRENCYCODE_USD);
				exchangeRateParams.put("effectiveDate", TimeUtils.format(saleDeclareOrderDTO.getCreateDate(), "yyyy-MM-dd"));
				ExchangeRateMongo exchangeRateMongo = exchangeRateMongoDao.findOne(exchangeRateParams);
				if (exchangeRateMongo != null) {
					usdRate = exchangeRateMongo.getRate();
				}
			}
			Set<String> originCountrySet = new HashSet<String>();// 原产国集合
			Set<String> brandSet = new HashSet<String>();// 商品品牌集合

			Integer totalNum = 0;
			Integer totalBoxNum = 0;
			Double totalNetWeight = 0.0;
			BigDecimal totalAmount = BigDecimal.ZERO;
			BigDecimal totalUsAmount = BigDecimal.ZERO;
			List<Map<String,Object>> customsItemList = new ArrayList<>();
			Map<String, List<SaleDeclareOrderItemDTO>> itemMap = itemList.stream().collect(Collectors.groupingBy(SaleDeclareOrderItemDTO :: getGoodsNo));
			for(String goodsNo: itemMap.keySet()) {
				List<SaleDeclareOrderItemDTO> items = itemMap.get(goodsNo);
				int totalItemNum = items.stream().mapToInt(SaleDeclareOrderItemDTO :: getNum).sum();
				Double netWeight = 0.0;
				SaleDeclareOrderItemDTO item = items.get(0);
				Map<String,Object> customsItem = new HashMap<>();
				// 1、取销售单中的商品货号，若为美赞商品需查询库位映射表转成原货号；
				// 2、以“条形码+入库关区”，找到商品表中外仓备案商品，入库关区为空，则为商品空，找到多条取其一；
				List<MerchandiseExternalWarehouseMongo> goodsList = new ArrayList<MerchandiseExternalWarehouseMongo>();
				if(saleDeclareOrderDTO.getInCustomsId() != null) {
					Map<String, Object> merchandiseExternalParams = new HashMap<String, Object>();
					// 原货号
					merchandiseExternalParams.put("barcode", item.getBarcode());// 条形码
					merchandiseExternalParams.put("customsAreaId", saleDeclareOrderDTO.getInCustomsId());//入库仓平台关区
					goodsList = merchandiseExternalWarehouseMongoDao.findAll(merchandiseExternalParams);
				}
				if (goodsList != null && goodsList.size() > 0) {
					MerchandiseExternalWarehouseMongo goods = goodsList.get(0);
					netWeight = goods.getNetWeight();

					customsItem.put("unit",goods.getUnit());// 备案单位
					customsItem.put("countryName",goods.getCountyName());// 原产国
					customsItem.put("brandName",goods.getBrandName());// 商品品牌
					customsItem.put("barcode",goods.getBarcode());// 条形码
					customsItem.put("goodsSpec",goods.getSpec());// 规格
					customsItem.put("declareFactor",goods.getDeclareFactor());// 申报要素
					customsItem.put("constituentRatio",goods.getComponent());// 成分占比
					customsItem.put("customsGoodsRecordNo",goods.getCustomsGoodsRecordNo());// 海关商品备案号
					customsItem.put("hsCode",goods.getHsCode());// hs编码
					customsItem.put("shelfLifeDays",goods.getShelfLifeDays());// 保质天数
					customsItem.put("productTypeName",goods.getProductTypeName());// 商品类别 取找到的商品货号在商品表的“二级分类”
					customsItem.put("manufacturer",goods.getManufacturer());// 生产企业 取找到的商品货号在商品表的生产企业
					customsItem.put("materialAccount",goods.getMaterialAccount());//账册备案料号
					customsItem.put("saleGoodNames",goods.getSaleGoodNames());//售卖商品名称（中文）
					customsItem.put("emsCode",goods.getEmsCode());//EMS编码
					customsItem.put("boxToUnit",goods.getBoxToUnit());//箱规
					customsItem.put("jinerxiang",goods.getJinerxiang());
					customsItem.put("skuCode",goods.getSkuCode());//sku编码
					customsItem.put("goodsNo",goods.getGoodsNo());// 商品货号
					customsItem.put("goodsName",goods.getGoodsName());// 商品名称
					customsItem.put("netWeight",goods.getNetWeight());// 净重
					customsItem.put("englishGoodsName",goods.getEnglishGoodsName());//商品英文名
					customsItem.put("volume",goods.getLength() +"*"+goods.getWidth()+"*"+goods.getHeight());//长*宽*高

					// 根据国家id获取国家信息
					originCountrySet.add(goods.getCountyName());
					// 查询商品品牌
					brandSet.add(goods.getBrandName());

				}

				Double netWeightSum = 0.0;
				netWeightSum = new BigDecimal(netWeight).multiply(new BigDecimal(totalItemNum)).setScale(5,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
				customsItem.put("totalNetWeight",netWeightSum);// 总净重
				customsItem.put("num",totalItemNum);// 商品数量
				customsItem.put("palletNo",item.getTorrNo());// 托盘号
				customsItem.put("cartons",item.getBoxNum());// 箱数


				// 单价
				BigDecimal price = BigDecimal.ZERO;
				if (rate != null) {
					price = item.getPrice().multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);// 人民币单价
				} else {
					price = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				customsItem.put("price",price);// 单价
				BigDecimal amount = price.multiply(new BigDecimal(totalItemNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
//				customsItem.put("amount",amount);// 总价
				// 美元单价
				BigDecimal usdPrice = BigDecimal.ZERO;
				if (usdRate != null) {
					usdPrice = item.getPrice().multiply(new BigDecimal(usdRate)).setScale(2, BigDecimal.ROUND_HALF_UP);// 美元单价
				} else {
					usdPrice = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				customsItem.put("usPrice",usdPrice);// 美元单价
				BigDecimal usAmount = usdPrice.multiply(new BigDecimal(totalItemNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
//				customsItem.put("usAmount",usAmount);// 美元总价

				customsItemList.add(customsItem);

				totalNum = totalNum + totalItemNum;
				totalNetWeight = totalNetWeight + netWeightSum;
				totalAmount = totalAmount.add(amount);
				totalUsAmount = totalUsAmount.add(usAmount);
				totalBoxNum = totalBoxNum + (item.getBoxNum() == null ? 0 : item.getBoxNum());

			}
			Double totalGrossWeight = 0.0;
			for(Map<String,Object> customsItem: customsItemList){
				Double goodsNetWeightSum = (Double) customsItem.get("totalNetWeight");
				Integer num = (Integer) customsItem.get("num");
				//毛重 = 商品净重/总净重 * 提单毛重
				Double grossWeightSum =0.0;
				if(totalNetWeight.doubleValue() > 0){
					grossWeightSum = new BigDecimal(saleDeclareOrderDTO.getBillWeight()).multiply(
							new BigDecimal(goodsNetWeightSum).divide(new BigDecimal(totalNetWeight),5,BigDecimal.ROUND_HALF_UP))
							.setScale(5,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
				}

				Double grossWeight = new BigDecimal(grossWeightSum).divide(new BigDecimal(num),5, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
				customsItem.put("totalGrossWeight",grossWeightSum);
				customsItem.put("grossWeight",grossWeight);
				totalGrossWeight = totalGrossWeight + grossWeightSum;
			}


			String originCountry = "";
			if (originCountrySet != null && originCountrySet.size() > 0) {
				originCountry = StringUtils.join(originCountrySet, ";");
			}
			dto.put("country",originCountry);// 原产国 用分号隔开
			String brands = "";
			if (brandSet != null && brandSet.size() > 0) {
				brands = StringUtils.join(brandSet, ";");
			}
			dto.put("brands",brands);// 品牌 用分号隔开
			dto.put("itemList",customsItemList);
			dto.put("totalNum",totalNum);
			dto.put("totalGrossWeight",totalGrossWeight);
			dto.put("totalNetWeight",totalNetWeight);
			dto.put("totalAmount",totalAmount);
			dto.put("totalUsAmount",totalUsAmount);
			dto.put("totalBoxNum",totalBoxNum);

		}

		return dto;
	}
	/**
	 * 封装京东南沙导出模板实体
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getJDNANSHAFirstCustomsInfoDTO(Long id , User user) throws Exception {
		SaleDeclareOrderDTO saleDeclareOrderDTO = new SaleDeclareOrderDTO();
		saleDeclareOrderDTO.setId(id);
		saleDeclareOrderDTO = saleDeclareOrderDao.searchDetail(saleDeclareOrderDTO);
		SaleDeclareOrderItemDTO itemDTO = new SaleDeclareOrderItemDTO();
		itemDTO.setDeclareOrderId(id);
		List<SaleDeclareOrderItemDTO> itemList = saleDeclareOrderItemDao.listDTO(itemDTO);
		// 根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", saleDeclareOrderDTO.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantParams);

		Map<String, Object> dto = new HashMap<>();
//		FirstCustomsInfoDTO firstCustomsInfoDTO = new FirstCustomsInfoDTO();
		if (saleDeclareOrderDTO != null) {

			if (merchant != null) {
				dto.put("merchantName",merchant.getFullName());// 当前公司全名
				dto.put("merchantAddr",merchant.getRegisteredAddress());// 当前公司地址
			}
			dto.put("poNo",saleDeclareOrderDTO.getPoNo());// po号（excel中的合同号）
			dto.put("invoiceNo",saleDeclareOrderDTO.getInvoiceNo());// 发票号
			dto.put("pack",saleDeclareOrderDTO.getPack());// 包装方式
			dto.put("orderDate",saleDeclareOrderDTO.getCreateDate());// 订单日期
			if(saleDeclareOrderDTO.getOutCustomsId() != null) {
				Map<String, Object> depotCustomsRelParams = new HashMap<String, Object>();
				depotCustomsRelParams.put("depotId", saleDeclareOrderDTO.getOutDepotId());
				depotCustomsRelParams.put("customsAreaId", saleDeclareOrderDTO.getOutCustomsId());
				DepotCustomsRelMongo depotCustomsRel = depotCustomsRelMongoDao.findOne(depotCustomsRelParams);
				if(depotCustomsRel != null) {
					dto.put("onlineRegisterNo",depotCustomsRel.getOnlineRegisterNo());	//电子商务账号
					dto.put("addressee",depotCustomsRel.getRecCompanyName());//仓库收货人公司名称（中文）
					dto.put("addresseeAddr",depotCustomsRel.getAddress());//仓库详细地址
					dto.put("customsAreaCode",depotCustomsRel.getCustomsAreaCode());//关区代码
				}

			}
			Double rate = null;
			// 销售币种不为人民币，则按订单创建日期取对应的汇率进行换算成人民币
			if (!DERP.CURRENCYCODE_CNY.equals(saleDeclareOrderDTO.getCurrency())) {
				Map<String, Object> exchangeRateParams = new HashMap<String, Object>();
				exchangeRateParams.put("origCurrencyCode", saleDeclareOrderDTO.getCurrency());
				exchangeRateParams.put("tgtCurrencyCode", DERP.CURRENCYCODE_CNY);
				exchangeRateParams.put("effectiveDate", TimeUtils.format(saleDeclareOrderDTO.getCreateDate(), "yyyy-MM-dd"));
				ExchangeRateMongo exchangeRateMongo = exchangeRateMongoDao.findOne(exchangeRateParams);
				if (exchangeRateMongo != null) {
					rate = exchangeRateMongo.getRate();
				}
			}


			Integer totalNum = 0;
			Double totalNetWeight = 0.0;
			BigDecimal totalAmount = BigDecimal.ZERO;
//			List<CustomsDeclareItemDTO> customsDeclareItemList = new ArrayList<CustomsDeclareItemDTO>();
			List<Map<String,Object>> customsItemList = new ArrayList<>();
			Map<String, List<SaleDeclareOrderItemDTO>> itemMap = itemList.stream().collect(Collectors.groupingBy(SaleDeclareOrderItemDTO :: getGoodsNo));
			for(String goodsNo: itemMap.keySet()) {
				List<SaleDeclareOrderItemDTO> items = itemMap.get(goodsNo);
				int totalItemNum = items.stream().mapToInt(SaleDeclareOrderItemDTO :: getNum).sum();
				Double netWeight = 0.0;

				SaleDeclareOrderItemDTO item = items.get(0);
				Map<String,Object> customsItem = new HashMap<>();
				// 1、取销售单中的商品货号，若为美赞商品需查询库位映射表转成原货号；
				// 2、以“条形码+入库关区”，找到商品表中外仓备案商品，入库关区为空，则为商品空，找到多条取其一；
				List<MerchandiseExternalWarehouseMongo> goodsList = new ArrayList<MerchandiseExternalWarehouseMongo>();
				if(saleDeclareOrderDTO.getInCustomsId() != null) {
					Map<String, Object> merchandiseExternalParams = new HashMap<String, Object>();
					// 原货号
					merchandiseExternalParams.put("barcode", item.getBarcode());// 条形码
					merchandiseExternalParams.put("customsAreaId", saleDeclareOrderDTO.getInCustomsId());//入库仓平台关区
					goodsList = merchandiseExternalWarehouseMongoDao.findAll(merchandiseExternalParams);
				}
				if (goodsList != null && goodsList.size() > 0) {
					MerchandiseExternalWarehouseMongo goods = goodsList.get(0);

					netWeight = goods.getNetWeight();
					customsItem.put("unit",goods.getUnit());// 备案单位
					customsItem.put("countryName",goods.getCountyName());// 原产国
					customsItem.put("barcode",goods.getBarcode());// 条形码
					customsItem.put("hsCode",goods.getHsCode());// hs编码
					customsItem.put("skuCode",goods.getSkuCode());//sku编码
					customsItem.put("goodsNo",goods.getGoodsNo());// 商品货号
					customsItem.put("goodsName",goods.getGoodsName());// 商品名称
					customsItem.put("netWeight",goods.getNetWeight());// 净重
				}

				Double netWeightSum = 0.0;
				netWeightSum = new BigDecimal(netWeight).multiply(new BigDecimal(totalItemNum)).setScale(5,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();

				customsItem.put("totalNetWeight",netWeightSum);// 总净重
				customsItem.put("num",totalItemNum);// 商品数量

				// 单价
				BigDecimal price = BigDecimal.ZERO;
				if (rate != null) {
					price = item.getPrice().multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);// 人民币单价
				} else {
					price = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				customsItem.put("price",price);// 单价
				BigDecimal amount = price.multiply(new BigDecimal(totalItemNum)).setScale(2, BigDecimal.ROUND_HALF_UP);

				totalNum = totalNum + totalItemNum;
				totalNetWeight = totalNetWeight + netWeightSum;
				totalAmount = totalAmount.add(amount);

				customsItemList.add(customsItem);
			}
			Double totalGrossWeight = 0.0;
			for(Map<String,Object> customsItem: customsItemList){
				Double goodsNetWeightSum = (Double) customsItem.get("totalNetWeight");
				Integer num = (Integer) customsItem.get("num");
				//毛重 = 商品净重/总净重 * 提单毛重
				Double grossWeightSum = new BigDecimal(saleDeclareOrderDTO.getBillWeight()).multiply(
						new BigDecimal(goodsNetWeightSum).divide(new BigDecimal(totalNetWeight),5,BigDecimal.ROUND_HALF_UP))
						.setScale(5,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();

				Double grossWeight = new BigDecimal(grossWeightSum).divide(new BigDecimal(num),5, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
				customsItem.put("totalGrossWeight",grossWeightSum);
				customsItem.put("grossWeight",grossWeight);
				totalGrossWeight = totalGrossWeight + grossWeightSum;
			}
			dto.put("itemList",customsItemList);
			dto.put("totalNum",totalNum);
			dto.put("totalGrossWeight",totalGrossWeight);
			dto.put("totalNetWeight",totalNetWeight);
			dto.put("totalAmount",totalAmount);
		}

		return dto;
	}

	/**
	 * 宁波慈溪一线清关资料
	 * @param declareOrder
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getNBCIXIFirstCustomsInfoDTO(SaleDeclareOrderDTO declareOrder , User user) throws Exception{
		SaleDeclareOrderItemDTO itemDTO = new SaleDeclareOrderItemDTO();
		itemDTO.setDeclareOrderId(declareOrder.getId());
		List<SaleDeclareOrderItemDTO> itemList = saleDeclareOrderItemDao.listDTO(itemDTO);
		// 根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", declareOrder.getMerchantId());
		MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantParams);

		Map<String,Object> dto = new HashMap<>();
		if (declareOrder != null) {
			dto.put("transportation",declareOrder.getTransportLabel());// 运输方式
			dto.put("customsContractNo",declareOrder.getContractNo());// 报关合同号（excel中的提运单号）);
			dto.put("poNo",declareOrder.getPoNo());// po号（excel中的合同号）
			dto.put("contractNo",declareOrder.getContractNo());
			dto.put("invoiceNo",declareOrder.getInvoiceNo());// 发票号
			dto.put("payRules",declareOrder.getTradeTerms());// 付款条约
			dto.put("pack",declareOrder.getPack());// 包装方式
			// 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱
			String packType = DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList,declareOrder.getTorrPackType());
			dto.put("palletMaterial",packType);// 托盘材质;
			dto.put("torrNum",declareOrder.getTorrNum());// 托数
			dto.put("payRules",declareOrder.getTradeTermsLabel());// 付款条约
			dto.put("orderDate",declareOrder.getCreateDate());// 订单日期
			dto.put("boxNum",declareOrder.getBoxNum());// 箱数
			dto.put("portLoading",declareOrder.getLoadPort());// 装货港
			dto.put("shipDate",declareOrder.getShipDate());//装船时间
			dto.put("destination",declareOrder.getDestinationPort());//目的地
			dto.put("shipper",declareOrder.getShipper());//承运商

			if (merchant != null) {
				dto.put("merchantName",merchant.getFullName());// 当前公司全名
				dto.put("merchantAddr",merchant.getRegisteredAddress());// 当前公司地址
				dto.put("merchantEnAddr",merchant.getEnglishRegisteredAddress());// 当前公司地址（英文）
				dto.put("merchantTel",merchant.getTel());
				dto.put("merchantEmail",merchant.getEmail());
				dto.put("merchantEnName",merchant.getEnglishName());// 当前公司全名 英文
			}

			Set<String> originCountrySet = new HashSet<String>();// 原产国集合
			Double totalNetWeight = 0.0;
			List<Map<String,Object>> customsItemList = new ArrayList<>();
			Map<String, List<SaleDeclareOrderItemDTO>> itemMap = itemList.stream().collect(Collectors.groupingBy(SaleDeclareOrderItemDTO :: getGoodsNo));
			for(String goodsNo: itemMap.keySet()) {
				List<SaleDeclareOrderItemDTO> items = itemMap.get(goodsNo);
				int totalItemNum = items.stream().mapToInt(SaleDeclareOrderItemDTO :: getNum).sum();
				Double netWeight = 0.0;
				SaleDeclareOrderItemDTO item = items.get(0);
				Map<String,Object> customsItem = new HashMap<>();
				Map<String, Object> merchandisePramMap = new HashMap<String, Object>();
				merchandisePramMap.put("merchandiseId", item.getGoodsId());
				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandisePramMap);
				if (merchandise != null) {
					netWeight = merchandise.getNetWeight();
					customsItem.put("barcode",merchandise.getBarcode());// 条形码
					customsItem.put("goodsSpec",merchandise.getSpec());// 规格
					customsItem.put("declareFactor",merchandise.getDeclareFactor());// 申报要素
					customsItem.put("constituentRatio",merchandise.getComponent());// 成分占比
					customsItem.put("goodsNo",merchandise.getGoodsNo());// 商品货号
					customsItem.put("goodsName",merchandise.getName());// 商品名称
					customsItem.put("netWeight",merchandise.getNetWeight());// 净重
					customsItem.put("hsCode",merchandise.getHsCode());// hs编码
					customsItem.put("shelfLifeDays",merchandise.getShelfLifeDays());// 保质天数

					// 根据国家id获取国家信息
					Map<String, Object> queryParams = new HashMap<String, Object>();
					queryParams.put("countryOriginId", merchandise.getCountyId());
					CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(queryParams);
					if(countryOrigin != null){
						customsItem.put("countryName",countryOrigin.getName());
						originCountrySet.add(countryOrigin.getName()) ;// 原产国
					}

					queryParams.clear();
					queryParams.put("brandId", merchandise.getBrandId());
					BrandMongo brand = brandMongoDao.findOne(queryParams);
					if(brand != null){
						customsItem.put("brandName",brand.getName());
					}
					if (merchandise.getUnit() != null) {
						// 根据单位id获取单位信息
						Map<String, Object> unitParams = new HashMap<String, Object>();
						unitParams.put("unitId", merchandise.getUnit());
						UnitMongo unit = unitMongoDao.findOne(unitParams);
						if (unit != null) {
							customsItem.put("unit",unit.getName());// 单位
						}
					}
				}

				Double netWeightSum = 0.0;
				netWeightSum = new BigDecimal(netWeight).multiply(new BigDecimal(totalItemNum)).setScale(5,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
				customsItem.put("totalNetWeight",netWeightSum);// 总净重
				customsItem.put("num",totalItemNum);// 商品数量
				customsItem.put("cartons", item.getBoxNum());
				// 单价
				BigDecimal price = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
				customsItem.put("price",price);// 单价

				customsItemList.add(customsItem);
				totalNetWeight = totalNetWeight + netWeightSum;

			}

			for(Map<String,Object> customsItem: customsItemList){
				Double goodsNetWeightSum = (Double) customsItem.get("totalNetWeight");
				Integer num = (Integer) customsItem.get("num");
				//毛重 = 商品净重/总净重 * 提单毛重
				Double grossWeightSum =0.0;
				if(totalNetWeight.doubleValue() > 0){
					grossWeightSum = new BigDecimal(declareOrder.getBillWeight()).multiply(
							new BigDecimal(goodsNetWeightSum).divide(new BigDecimal(totalNetWeight),5,BigDecimal.ROUND_HALF_UP))
							.setScale(5,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
				}

				Double grossWeight = new BigDecimal(grossWeightSum).divide(new BigDecimal(num),5, BigDecimal.ROUND_HALF_UP).doubleValue();
				customsItem.put("totalGrossWeight",grossWeightSum);
				customsItem.put("grossWeight",grossWeight);
			}


			String originCountry = "";
			if (originCountrySet != null && originCountrySet.size() > 0) {
				originCountry = org.apache.commons.lang.StringUtils.join(originCountrySet, ";");
			}
			dto.put("country",originCountry);// 原产国 用分号隔开
			dto.put("itemList",customsItemList);
		}

		return dto;
	}

	@Override
	public SXSSFWorkbook exportPallteizeData(String sheetName, String[] columns, Long id , User user) throws Exception {
		/**--------封装数据 start-------**/
		List<Map<String, Object>> resultList = new ArrayList<>();
		SaleDeclareOrderModel saleDeclareOrderModel = saleDeclareOrderDao.searchById(id);
		if(saleDeclareOrderModel.getExportPallteizeDate() == null){
			//更新销售预申报单 导出打托资料 时间
			saleDeclareOrderModel.setExportPallteizeDate(TimeUtils.getNow());
			saleDeclareOrderModel.setModifier(user.getId());
			saleDeclareOrderModel.setModifierUsername(user.getName());
			saleDeclareOrderModel.setModifyDate(TimeUtils.getNow());
			saleDeclareOrderDao.modify(saleDeclareOrderModel);

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, saleDeclareOrderModel.getCode(), "导出打托资料", null, null);
		}
		SaleDeclareOrderItemModel queryItem = new SaleDeclareOrderItemModel();
		queryItem.setDeclareOrderId(id);
		List<SaleDeclareOrderItemModel> itemList = saleDeclareOrderItemDao.list(queryItem);
		for(SaleDeclareOrderItemModel itemModel : itemList){
			Map<String, Object> map = new HashMap<>();
			//"主体","出库仓库","条形码","货号","产品名称","订单数量","效期需求"
			map.put("merchantName",saleDeclareOrderModel.getMerchantName());
			map.put("outDepotName",saleDeclareOrderModel.getOutDepotName());
			map.put("barcode",itemModel.getBarcode());
			map.put("goodsNo",itemModel.getGoodsNo());
			map.put("goodsName",itemModel.getGoodsName());
			map.put("num",itemModel.getNum());

			resultList.add(map);
		}
		/**--------封装数据 end-------**/

		/**--------生成excel start-------**/
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		Sheet excel = workbook.createSheet(sheetName);
		Row headrow = excel.createRow(0);

		// 设置字体
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)10);//设置字体大小

		CellStyle cellStyle = workbook.createCellStyle();// 设置表头
		cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
		cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
		cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
		cellStyle.setBorderTop(BorderStyle.THIN);//上边框
		cellStyle.setBorderRight(BorderStyle.THIN);//右边框
		cellStyle.setFont(font);


		CellStyle textCellStyle = workbook.createCellStyle();// 设置表体(针对文本类型居左)
		textCellStyle.setAlignment(HorizontalAlignment.LEFT);// 水平居左
		textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
		textCellStyle.setBorderBottom(BorderStyle.THIN); //下边框
		textCellStyle.setBorderLeft(BorderStyle.THIN);//左边框
		textCellStyle.setBorderTop(BorderStyle.THIN);//上边框
		textCellStyle.setBorderRight(BorderStyle.THIN);//右边框
		textCellStyle.setFont(font);

		CellStyle numCellStyle = workbook.createCellStyle();// 设置表体(针对数值型居右)
		numCellStyle.setAlignment(HorizontalAlignment.RIGHT);// 水平居右
		numCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
		numCellStyle.setBorderBottom(BorderStyle.THIN); //下边框
		numCellStyle.setBorderLeft(BorderStyle.THIN);//左边框
		numCellStyle.setBorderTop(BorderStyle.THIN);//上边框
		numCellStyle.setBorderRight(BorderStyle.THIN);//右边框
		numCellStyle.setFont(font);

		int i = 0;
		for (String title : columns) { // 设置表头
			Cell cell = headrow.createCell(i);
			excel.setColumnWidth(i, 20 * 256);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(title);
			i++;
		}
		// 注入参数
		int rowNum = 1;
		BigDecimal totaNum = BigDecimal.ZERO;
		for (int j = 0; j < resultList.size(); j++) { // 填值
			Map model = resultList.get(j);
			Row row = excel.createRow(rowNum++);
			i = 0;
			//主体
			Cell cell1 = row.createCell(i++);
			Object obj = model.get("merchantName");
			cell1.setCellValue(obj != null ? obj.toString() : "");
			cell1.setCellStyle(textCellStyle);
			//出库仓库
			Cell cell2 = row.createCell(i++);
			obj = model.get("outDepotName");
			cell2.setCellValue(obj != null ? obj.toString() : "");
			cell2.setCellStyle(textCellStyle);
			//出库仓库
			Cell cell3 = row.createCell(i++);
			obj = model.get("barcode");
			cell3.setCellValue(obj != null ? obj.toString() : "");
			cell3.setCellStyle(textCellStyle);
			//出库仓库
			Cell cell4 = row.createCell(i++);
			obj = model.get("goodsNo");
			cell4.setCellValue(obj != null ? obj.toString() : "");
			cell4.setCellStyle(textCellStyle);
			//产品名称
			Cell cell5 = row.createCell(i++);
			obj = model.get("goodsName");
			cell5.setCellValue(obj != null ? obj.toString() : "");
			cell5.setCellStyle(textCellStyle);
			//订单数量
			Cell cell6 = row.createCell(i++);
			obj = model.get("num");
			cell6.setCellValue(obj != null ? obj.toString() : "");
			cell6.setCellStyle(numCellStyle);
			totaNum = totaNum.add(new BigDecimal(obj != null ? obj.toString() : ""));
			//效期需求
			Cell cell7 = row.createCell(i++);
			cell7.setCellValue("");
			cell7.setCellStyle(textCellStyle);
			//备注
			Cell cell8 = row.createCell(i++);
			cell8.setCellValue("");
			cell8.setCellStyle(textCellStyle);
		}

		CellStyle totalCellStyle = workbook.createCellStyle();// 设置表体(针对文本类型居左)
		totalCellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
		totalCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
		totalCellStyle.setBorderBottom(BorderStyle.THIN); //下边框
		totalCellStyle.setBorderLeft(BorderStyle.THIN);//左边框
		totalCellStyle.setBorderTop(BorderStyle.THIN);//上边框
		totalCellStyle.setBorderRight(BorderStyle.THIN);//右边框
		totalCellStyle.setFont(font);
		excel.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 4));
		Row row = excel.createRow(rowNum++);
		Cell totalCell = row.createCell(0);
		totalCell.setCellValue("合计：");
		totalCell.setCellStyle(totalCellStyle);

		Cell totalCell_1 = row.createCell(1);
		totalCell_1.setCellValue("");
		totalCell_1.setCellStyle(totalCellStyle);
		Cell totalCell_2 = row.createCell(2);
		totalCell_2.setCellValue("");
		totalCell_2.setCellStyle(totalCellStyle);
		Cell totalCell_3 = row.createCell(3);
		totalCell_3.setCellValue("");
		totalCell_3.setCellStyle(totalCellStyle);
		Cell totalCell_4 = row.createCell(4);
		totalCell_4.setCellValue("");
		totalCell_4.setCellStyle(totalCellStyle);

		Cell totalCell_5 = row.createCell(5);
		totalCell_5.setCellValue(totaNum.toString());
		totalCell_5.setCellStyle(numCellStyle);
		/**--------生成excel end-------**/
		return workbook;
	}

	//编辑物流委托书
	@Override
	public void modifyLogisticsAttorney(LogisticsAttorneyModel logisticsAttorneyModel, User user) throws Exception {
		//物流委托书
		LogisticsAttorneyModel queryAttorneyModel = new LogisticsAttorneyModel() ;
		queryAttorneyModel.setOrderId(logisticsAttorneyModel.getOrderId());
		queryAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_2);// 1-采购预申报 2-销售预申报
		queryAttorneyModel = logisticsAttorneyDao.searchByModel(queryAttorneyModel) ;

		logisticsAttorneyModel.setId(queryAttorneyModel.getId());
		logisticsAttorneyModel.setModifyDate(TimeUtils.getNow());
		logisticsAttorneyDao.modifyWithNull(logisticsAttorneyModel);

		//获取预申报单信息
		SaleDeclareOrderModel declareModel = saleDeclareOrderDao.searchById(logisticsAttorneyModel.getOrderId());
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "编辑物流委托书", null, null);
	}
	//复制
	@Override
	public SaleDeclareOrderDTO copySaleDeclare(Long declareId) throws Exception {
		SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
		dto.setId(declareId);
		dto = saleDeclareOrderDao.searchDetail(dto);
		List<Long> ids = StrUtils.parseIds(dto.getSaleOrderIds());
		for(Long id : ids) {
			SaleOrderModel saleModel = saleOrderDao.searchById(id);
			if(!DERP_ORDER.SALEORDER_STATE_003.equals(saleModel.getState()) &&!DERP_ORDER.SALEORDER_STATE_019.equals(saleModel.getState())) {
				throw new RuntimeException("销售订单："+saleModel.getCode()+"状态不为“已审核”或“部分出库”");
			}
		}
		LogisticsAttorneyModel logisticsAttorneyModel = new LogisticsAttorneyModel() ;
		logisticsAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_2);
		logisticsAttorneyModel.setOrderId(declareId);
		logisticsAttorneyModel = logisticsAttorneyDao.searchByModel(logisticsAttorneyModel);
		dto.setCarrierInformationTempId(logisticsAttorneyModel.getCarrierInformationTempId());
		dto.setCarrierInformationTempName(logisticsAttorneyModel.getCarrierInformationTempName());
		dto.setCarrierInformationTempDetails(logisticsAttorneyModel.getCarrierInformationText());
		dto.setShipperTempId(logisticsAttorneyModel.getShipperTempId());
		dto.setShipperTempName(logisticsAttorneyModel.getShipperTempName());
		dto.setShipperTempDetails(logisticsAttorneyModel.getShipperText());
		dto.setConsigneeTempId(logisticsAttorneyModel.getConsigneeTempId());
		dto.setConsigneeTempName(logisticsAttorneyModel.getConsigneeTempName());
		dto.setConsigneeTempDetails(logisticsAttorneyModel.getConsigneeText());
		dto.setNotifierTempId(logisticsAttorneyModel.getNotifierTempId());
		dto.setNotifierTempName(logisticsAttorneyModel.getNotifierTempName());
		dto.setNotifierTempDetails(logisticsAttorneyModel.getNotifierText());
		dto.setDockingTempId(logisticsAttorneyModel.getDockingTempId());
		dto.setDockingTempName(logisticsAttorneyModel.getDockingTempName());
		dto.setDockingTempDetails(logisticsAttorneyModel.getDockingText());

		SaleDeclareOrderItemDTO itemModel = new SaleDeclareOrderItemDTO();
		itemModel.setDeclareOrderId(declareId);
		List<SaleDeclareOrderItemDTO> itemList = saleDeclareOrderItemDao.listDTO(itemModel);
		for(SaleDeclareOrderItemDTO item : itemList){
			item.setNum(0);
			item.setDeclareOrderId(null);
			item.setId(null);
		}
		//“物流信息”（不带出发票号、报关合同号、提单号）
		dto.setInvoiceNo(null);
		dto.setContractNo(null);
		dto.setLadingBill(null);
		dto.setId(null);
		dto.setCode(null);
		dto.setItemList(itemList);

		return dto;
	}
}
