package com.topideal.order.service.sale.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.dao.transfer.TransferOrderDao;
import com.topideal.entity.dto.sale.SaleReturnDeclareOrderDTO;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.pushapi.epass.e01.PurchaseGoodsListJson;
import com.topideal.json.pushapi.epass.e01.PurchaseOrderRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.SaleReturnDeclareOrderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 销售退货订单service实现类
 */
@Service
public class SaleReturnDeclareOrderServiceImpl implements SaleReturnDeclareOrderService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(SaleReturnDeclareOrderServiceImpl.class);
	// 销售退货订单表头
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;
	// 销售退货订单表体
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	//供应商
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	//仓库公司关联表 mongo
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	//lbx来源
	@Autowired
	private LbxNoMongoDao lbxNoMongoDao;
	@Autowired
	private ApiSecretConfigMongoDao apiSecretConfigMongoDao;
	//销售出库清单
	@Autowired
	SaleOutDepotDao saleOutDepotDao;
	@Autowired
	SaleReturnIdepotDao saleReturnIdepotDao;
	@Autowired
	SaleReturnIdepotItemDao saleReturnIdepotItemDao;
	// 销售上架信息
	@Autowired
	SaleShelfDao saleShelfDao;
	// 库存调整明细
	@Autowired
	InventoryAdjustmentDetailDao inventoryAdjustmentDetailDao;
	// 销售退货出库表体
	@Autowired
	SaleReturnOdepotItemDao saleReturnOdepotItemDao;

	// 销售退货出库
	@Autowired
	SaleReturnOdepotDao saleReturnOdepotDao;

	@Autowired
	SalePoRelDao salePoRelDao;

	@Autowired
	MerchandiseContrastDao merchandiseContrastDao;

	@Autowired
	CrawlerBillDao crawlerBillDao;

	@Autowired
	private TransferOrderDao transferOrderDao;
	// 上架入库单
	@Autowired
	private SaleShelfIdepotItemDao shelfIdepotItemDao;
	//po历史结转临时表
	@Autowired
	private PojzTempDao pojzTempDao;
	//账单出入库单表
	@Autowired
	private BillOutinDepotItemDao billOutinDepotItemDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private SaleReturnDeclareOrderDao saleReturnDeclareOrderDao;
	@Autowired
	private SaleReturnDeclareOrderItemDao saleReturnDeclareOrderItemDao;
	@Autowired
	DepotCustomsRelMongoDao depotCustomsRelMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;

	@Override
	public SaleReturnDeclareOrderDTO listDTOByPage(SaleReturnDeclareOrderDTO dto, User user) throws Exception {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}

			dto.setBuList(buIds);
		}
		return saleReturnDeclareOrderDao.listDTOByPage(dto);
	}

	@Override
	public SaleReturnDeclareOrderDTO searchDetail(Long id) throws Exception {
		SaleReturnDeclareOrderDTO dto = new SaleReturnDeclareOrderDTO();
		dto.setId(id);
		return saleReturnDeclareOrderDao.searchDetail(dto);
	}

	@Override
	public List<SaleReturnDeclareOrderItemModel> getItemList(Long declareId) throws Exception {
		SaleReturnDeclareOrderItemModel itemModel = new SaleReturnDeclareOrderItemModel();
		itemModel.setReturnDeclareOrderId(declareId);
		return saleReturnDeclareOrderItemDao.list(itemModel);
	}

	@Override
	public SaleReturnDeclareOrderDTO saleReturnToDeclareOrder(String saleReturnIds) throws Exception {
		List<Long> saleReturnIdList = StrUtils.parseIds(saleReturnIds);
		List<SaleReturnDeclareOrderItemModel> itemList = new ArrayList<SaleReturnDeclareOrderItemModel>();

		Integer seq = 1;
		List<String> codes = new ArrayList<String>();
		Set<String> lBXNoSet = new HashSet<String>();
		Set<String> poNoList = new HashSet<String>();
		BigDecimal billWeight = BigDecimal.ZERO;
		for(Long id : saleReturnIdList) {
			SaleReturnOrderModel saleReturnModel = saleReturnOrderDao.searchById(id);
			SaleReturnOrderItemModel saleReturnItemModel = new SaleReturnOrderItemModel();
			saleReturnItemModel.setOrderId(saleReturnModel.getId());
			List<SaleReturnOrderItemModel> saleReturnItemList = saleReturnOrderItemDao.list(saleReturnItemModel);
			if(saleReturnItemList == null || saleReturnItemList.size() < 1) {
				throw new RuntimeException("销售退订单："+saleReturnModel.getCode() +" 商品信息为空");
			}
			for(SaleReturnOrderItemModel saleReturnItem : saleReturnItemList) {
				SaleReturnDeclareOrderItemModel saleReturnDeclareOrderItemModel = new SaleReturnDeclareOrderItemModel();
				saleReturnDeclareOrderItemModel.setSaleReturnOrderId(saleReturnModel.getId());
				saleReturnDeclareOrderItemModel.setSaleReturnOrderCode(saleReturnModel.getCode());
				saleReturnDeclareOrderItemModel.setPoNo(saleReturnItem.getPoNo());
				saleReturnDeclareOrderItemModel.setPrice(saleReturnItem.getPrice());
				saleReturnDeclareOrderItemModel.setAmount(saleReturnItem.getAmount());
				saleReturnDeclareOrderItemModel.setTaxRate(saleReturnItem.getTaxRate());
				saleReturnDeclareOrderItemModel.setTaxPrice(saleReturnItem.getTaxPrice());
				saleReturnDeclareOrderItemModel.setTaxAmount(saleReturnItem.getTaxAmount());
				saleReturnDeclareOrderItemModel.setTax(saleReturnItem.getTax());
				saleReturnDeclareOrderItemModel.setInGoodsId(saleReturnItem.getInGoodsId());
				saleReturnDeclareOrderItemModel.setInGoodsNo(saleReturnItem.getInGoodsNo());
				saleReturnDeclareOrderItemModel.setInGoodsCode(saleReturnItem.getInGoodsCode());
				saleReturnDeclareOrderItemModel.setInGoodsName(saleReturnItem.getInGoodsName());
				saleReturnDeclareOrderItemModel.setInBarcode(saleReturnItem.getBarcode());
				saleReturnDeclareOrderItemModel.setOutGoodsId(saleReturnItem.getOutGoodsId());
				saleReturnDeclareOrderItemModel.setOutGoodsNo(saleReturnItem.getOutGoodsNo());
				saleReturnDeclareOrderItemModel.setOutGoodsCode(saleReturnItem.getOutGoodsCode());
				saleReturnDeclareOrderItemModel.setOutGoodsName(saleReturnItem.getOutGoodsName());
				saleReturnDeclareOrderItemModel.setBrandName(saleReturnItem.getBrandName());
				saleReturnDeclareOrderItemModel.setNum(saleReturnItem.getReturnNum());
				//获取商品信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("merchandiseId", saleReturnItem.getInGoodsId());
				MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(params);
				saleReturnDeclareOrderItemModel.setGrossWeight(merchandiseMongo.getGrossWeight());
				saleReturnDeclareOrderItemModel.setNetWeight(merchandiseMongo.getNetWeight());

				BigDecimal grossWeightSum = new BigDecimal(merchandiseMongo.getGrossWeight()).multiply(new BigDecimal(saleReturnItem.getReturnNum()));
				BigDecimal netWeightSum = new BigDecimal(merchandiseMongo.getNetWeight()).multiply(new BigDecimal(saleReturnItem.getReturnNum()));
				saleReturnDeclareOrderItemModel.setGrossWeightSum(grossWeightSum.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue());
				saleReturnDeclareOrderItemModel.setNetWeightSum(netWeightSum.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue());

				saleReturnDeclareOrderItemModel.setSeq(seq++);

				itemList.add(saleReturnDeclareOrderItemModel);
				billWeight = billWeight.add(new BigDecimal(saleReturnDeclareOrderItemModel.getGrossWeightSum()));
			}

			codes.add(saleReturnModel.getCode());
			lBXNoSet.add(saleReturnModel.getLbxNo());

			//po去重
			List<String> poNos = Arrays.stream(saleReturnModel.getPoNo().split("&")).collect(Collectors.toList());
			poNoList.addAll(poNos);
		}
		//公司+事业部+退入库仓库+退出库仓库+销售类型+销售币种 相同，客户随机取一条
		SaleReturnOrderModel saleReturnModel = saleReturnOrderDao.searchById(saleReturnIdList.get(0));
		SaleReturnDeclareOrderDTO dto =  new SaleReturnDeclareOrderDTO();
		dto.setMerchantId(saleReturnModel.getMerchantId());
		dto.setMerchantName(saleReturnModel.getMerchantName());
		dto.setBuId(saleReturnModel.getBuId());
		dto.setBuName(saleReturnModel.getBuName());
		dto.setCustomerId(saleReturnModel.getCustomerId());
		dto.setCustomerName(saleReturnModel.getCustomerName());
		dto.setOutDepotId(saleReturnModel.getOutDepotId());
		dto.setOutDepotName(saleReturnModel.getOutDepotName());
		dto.setCurrency(saleReturnModel.getCurrency());
		dto.setInDepotId(saleReturnModel.getInDepotId());
		dto.setInDepotName(saleReturnModel.getInDepotName());
		dto.setSaleReturnOrderIds(StringUtils.join(saleReturnIdList,","));
		dto.setSaleReturnOrderCodes(StringUtils.join(codes,","));
		dto.setLbxNo(StringUtils.join(lBXNoSet,","));
		dto.setPoNo(StringUtils.join(poNoList,"&"));
		dto.setBillWeight(billWeight.doubleValue());
		dto.setItemList(itemList);

		return dto;
	}

	@Override
	public void addOrModifySaleReturnOrder(SaleReturnDeclareOrderDTO dto, User user) throws Exception {
		//operate 1-保存  2-审核
		saveSaleReturnOrder(dto , user ,"1");

	}

	@Override
	public void auditSaleReturnOrder(SaleReturnDeclareOrderDTO dto, User user) throws Exception {
		//operate 1-保存  2-审核
		SaleReturnDeclareOrderModel model = saveSaleReturnOrder(dto , user ,"2");

		//获取销售退货表体（商品信息）
		SaleReturnDeclareOrderItemModel saleReturnDeclareOrderItemModel = new SaleReturnDeclareOrderItemModel();
		saleReturnDeclareOrderItemModel.setReturnDeclareOrderId(model.getId());	// 销售退货订单ID
		// 根据销售退预申报订单ID到销售退货表体里查询该销售退货订单ID的商品有哪些
		List<SaleReturnDeclareOrderItemModel> itemList = saleReturnDeclareOrderItemDao.list(saleReturnDeclareOrderItemModel);

		Map<String, Object> outDepotParam = new HashMap<String, Object>();
		outDepotParam.put("depotId", model.getOutDepotId());
		DepotInfoMongo outDepot = depotInfoMongoDao.findOne(outDepotParam);

		Map<String, Object> inDepotParam = new HashMap<String, Object>();
		inDepotParam.put("depotId", model.getInDepotId());
		DepotInfoMongo inDepot = depotInfoMongoDao.findOne(inDepotParam);
		if (inDepot==null) {
			throw new RuntimeException("没有查询到入库仓");
		}

		/*
		 * 销售退入仓仓库是 在对应公司下的“进出接口指令”标识为“是”的，才走此接口下推指令（调用1.13采购入库）
		 */
		// 查询某公司某仓库的相关信息
		Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
		depotMerchantRelParam.put("merchantId", user.getMerchantId());
		depotMerchantRelParam.put("depotId", inDepot.getDepotId());
		DepotMerchantRelMongo depotMerchantRelInfo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
		if(depotMerchantRelInfo == null || "".equals(depotMerchantRelInfo)){
			throw new RuntimeException("单号："+model.getCode()+",仓库ID为:"+inDepot.getDepotId()+"，未查到该公司下调出仓库信息，审核失败");
		}

	 	//销售退入仓仓库是 在对应公司下的“进出接口指令”标识为“是”的，才走此接口下推指令（调用1.13采购入库）
		if(depotMerchantRelInfo!=null && DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelInfo.getIsInOutInstruction())){
				PurchaseOrderRootJson rootJson = new PurchaseOrderRootJson();
				if(DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepot.getType())){// 海外仓传理货单位
					rootJson.setUom(model.getTallyingUnit());
				}
				rootJson.setDatasource("DISTRIBUTED");//数据来源  经分销：DISTRIBUTED（经分销此字段必填）
				rootJson.setEnt_inbound_id(model.getCode()); // 企业入仓编号
				rootJson.setContract_no(model.getContractNo());// 合同号
				rootJson.setInvoice_no(model.getInvoiceNo());// 发票号
				rootJson.setServer_types("");// 服务类型
				rootJson.setRecadd(model.getDeliveryAddr());	// 收货地址
				rootJson.setPort_loading(model.getLoadPort());	// 装货港
				rootJson.setPack_type(model.getPack());
				if(model.getBoxNum() != null){
					rootJson.setCartons(model.getBoxNum().toString());// 箱数
				}
				if(model.getBillWeight() != null){
					rootJson.setBill_weight(model.getBillWeight().toString());// 提单毛重
				}
				rootJson.setTransportbl_no(model.getLadingBill());
				rootJson.setPort_dest_no(model.getPortDestNo());// 卸货港编码
				String portDestination = DERP.getLabelByKey(DERP.portDestList,model.getPortDestNo());//卸货港名称
				if(StringUtils.isNotBlank(portDestination)&&portDestination.contains("：")){
					String str1=portDestination.substring(0, portDestination.indexOf("："));
					portDestination=portDestination.substring(str1.length()+1);
				}
				rootJson.setPort_destination(portDestination);// 卸货港名称
				rootJson.setStatus("1");
				rootJson.setWarehouse_id(inDepot.getCode());
				rootJson.setNotes(model.getRemark());
				rootJson.setMark(model.getMark());
				rootJson.setOverseas_shipper(model.getShipper());// 境外发货人
				rootJson.setBusiness_moshi(DERP_ORDER.ORDER_IMPORTMODE_10);//业务模式
				Map<String, PurchaseGoodsListJson> sumMap = new LinkedHashMap<>() ;
				//不同销售退订单出现相同商品时，合并商品的退入数量、总金额、毛重、净重
				for(int i=0;i<itemList.size();i++){
					SaleReturnDeclareOrderItemModel orderItem=itemList.get(i);
					PurchaseGoodsListJson goods = new PurchaseGoodsListJson();
					// 退入数量不为空
					if(orderItem.getNum() != null ){
						goods.setAmount(orderItem.getNum().toString());
					}
					if(orderItem.getPrice() != null){
						// 出仓仓库是：唯品代销仓  入仓仓库是：综合1仓 类型为跨关区,采购指令商品单价小数位不能超过两位小数
						BigDecimal priceBD = orderItem.getPrice().setScale(2, RoundingMode.UP);
						goods.setPrice(priceBD.toString());//单价
					}
					goods.setIndex(String.valueOf(i+1));//序号
					goods.setCont_no(orderItem.getBoxNo());
					goods.setBargainno(model.getContractNo());
					goods.setBrand(orderItem.getBrandName());
					goods.setIs_tracesrc("0");  //是否溯源 0：不溯源
					goods.setGoods_name(orderItem.getInGoodsName());
					goods.setGoods_id(orderItem.getInGoodsNo());
					goods.setWeight(String.valueOf(new BigDecimal(orderItem.getGrossWeightSum().toString())));
					goods.setNet_weight(String.valueOf(new BigDecimal(orderItem.getNetWeightSum().toString())));

					sumMap.put(goods.getGoods_id(), goods) ;
				}
				if(!sumMap.isEmpty()) {
					rootJson.setGoods_list(new ArrayList<>(sumMap.values()));
				}
				String signTopidealCode = user.getTopidealCode();
				//判断仓库使用当前公司秘钥,还是关联公司的秘钥 1公司key 2关联公司key
				if(outDepot != null && outDepot.getIsMerchant()!=null && outDepot.getIsMerchant().equals("2")){
					Map<String, Object> aMap = new HashMap<String, Object>();
					aMap.put("merchantId", outDepot.getMerchantId());
					ApiSecretConfigMongo apiSecret = apiSecretConfigMongoDao.findOne(aMap);
					if(apiSecret==null){
						throw new RuntimeException(model.getCode()+":调出仓库关联公司秘钥不存在");
					}
					signTopidealCode = apiSecret.getTopidealCode();
				}
				// 调采购入库接口
				//JSONObject jsonObject = JSONObject.fromObject(rootJson);
				com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(rootJson);
				jsonObject.put("method", EpassAPIMethodEnum.EPASS_E01_METHOD.getMethod());
				jsonObject.put("topideal_code", signTopidealCode);
				try {
					SendResult result = rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(), MQPushApiEnum.PUSH_EPASS.getTags());
					System.out.println("销售退审核发送采购入库消息返回："+result.toString());
					if(result== null){
						throw new RuntimeException("销售单提交审核调拨服务异常");
					}
					if(!result.getSendStatus().name().equals("SEND_OK")){//SEND_OK-成功
						throw new RuntimeException("销售退提交审核发送采购入库失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		//修改单据状态
		model.setStatus(DERP_ORDER.SALERETURNDECLARE_STATUS_002);//已审核
		model.setApiStatus(DERP_ORDER.SALEDECLARE_APISTATUS_1);//已推接口
		model.setAuditerDate(TimeUtils.getNow());
		model.setAuditer(user.getId());
		model.setAuditUsername(user.getName());
		saleReturnDeclareOrderDao.modify(model);

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_17, model.getCode(), "审核", null, null);

	}

	@Override
	public void delSaleReturnOrder(String ids) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		for(Long declareId : idList){
			SaleReturnDeclareOrderModel model = saleReturnDeclareOrderDao.searchById(declareId);
			if(!DERP_ORDER.SALERETURNDECLARE_STATUS_001.equals(model.getStatus())){
				throw new RuntimeException("销售退预申报订单:"+model.getCode() +" 状态不为“待审核”");
			}
			SaleReturnDeclareOrderModel updateModel = new SaleReturnDeclareOrderModel();
			updateModel.setId(declareId);
			updateModel.setStatus(DERP.DEL_CODE_006);
			saleReturnDeclareOrderDao.modify(updateModel);

			if(StringUtils.isNotBlank(model.getSaleReturnOrderIds())) {
				String[] arr = model.getSaleReturnOrderIds().split(",");
				// 更新销售退单状态
				for (String saleReturnId : arr) {
					SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
					saleReturnOrderModel.setId(Long.valueOf(saleReturnId));
					saleReturnOrderModel.setIsGenerateDeclare("0");// 0-否 1-是
					saleReturnOrderDao.modify(saleReturnOrderModel);


					//更新销售退出库单关联的预申报单号
					SaleReturnOdepotModel saleReturnOdepotModel = new SaleReturnOdepotModel();
					saleReturnOdepotModel.setOrderId(Long.valueOf(saleReturnId));
					saleReturnOdepotModel = saleReturnOdepotDao.searchByModel(saleReturnOdepotModel);
					if(saleReturnOdepotModel != null) {
						saleReturnOdepotModel.setReturnDeclareOrderId(null);
						saleReturnOdepotModel.setReturnDeclareOrderCode("");
						saleReturnOdepotModel.setModifyDate(TimeUtils.getNow());
						saleReturnOdepotDao.modifyWithNULL(saleReturnOdepotModel);
					}
				}
			}


		}
	}

	private SaleReturnDeclareOrderModel saveSaleReturnOrder(SaleReturnDeclareOrderDTO dto, User user ,String operate) throws Exception {
		if (dto.getCustomerId() == null) {
			throw new RuntimeException("客户不能为空");
		}
		if (dto.getBuId() == null) {
			throw new RuntimeException("事业部不能为空");
		}
		//保存不校验必填项 ，审核校验必填项
		if("2".equals(operate)){
			if(StringUtils.isBlank(dto.getTradeTerms())) {
				throw new RuntimeException("贸易条款不能为空");
			}
			if(StringUtils.isBlank(dto.getLoadPort())) {
				throw new RuntimeException("装货港不能为空");
			}
			if(StringUtils.isBlank(dto.getPortDestNo())) {
				throw new RuntimeException("卸货港不能为空");
			}
			if(StringUtils.isBlank(dto.getDeliveryAddr())) {
				throw new RuntimeException("收货地址不能为空");
			}
			if(StringUtils.isBlank(dto.getShipper())) {
				throw new RuntimeException("境外发货人不能为空");
			}
			if(StringUtils.isBlank(dto.getContractNo())) {
				throw new RuntimeException("报关合同号不能为空");
			}
			if(dto.getBillWeight() == null) {
				throw new RuntimeException("提单毛重不能为空");
			}
			if(dto.getBoxNum() == null) {
				throw new RuntimeException("提单毛重不能为空");
			}
			if(StringUtils.isBlank(dto.getPack())) {
				throw new RuntimeException("包装方式不能为空");
			}
			if(StringUtils.isBlank(dto.getMark())) {
				throw new RuntimeException("唛头不能为空");
			}
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
		}
		// 入仓关区
		DepotCustomsRelMongo inDepotCustomsRelMongo = null;
		if (dto.getInDepotId() != null && dto.getInCustomsId() != null) {
			Map<String, Object> inDepotCustomsRelMap = new HashMap<String, Object>();
			inDepotCustomsRelMap.put("depotId", dto.getInDepotId());
			inDepotCustomsRelMap.put("customsAreaId", dto.getInCustomsId());
			inDepotCustomsRelMongo = depotCustomsRelMongoDao.findOne(inDepotCustomsRelMap);
		}

		//海外仓时，理货单位必填
		if(inDepot != null && DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepot.getType()) ){
			if(StringUtils.isBlank(dto.getTallyingUnit())) {
				throw new RuntimeException("当海外仓时，理货单位不能为空") ;
			}
		}

		Map<Long, BigDecimal> checkGoodsPrice = new HashMap<Long, BigDecimal>();
		for(SaleReturnDeclareOrderItemModel itemModel : dto.getItemList()) {
			//不同销售单，相同商品货号的申报单价必须相等
			Long key = itemModel.getInGoodsId();
			if(checkGoodsPrice.containsKey(key) && itemModel.getPrice().compareTo(checkGoodsPrice.get(key)) != 0) {
				throw new RuntimeException("相同商品货号："+ itemModel.getInGoodsNo() +" 退货单价不相等") ;
			}
			if(!checkGoodsPrice.containsKey(key)){
				checkGoodsPrice.put(key, itemModel.getPrice());
			}
			//获取退入商品信息
			Map<String, Object> inParams = new HashMap<String, Object>();
			inParams.put("merchandiseId", itemModel.getInGoodsId());
			MerchandiseInfoMogo inMerchandise = merchandiseInfoMogoDao.findOne(inParams);

			//获取退出商品信息
			Map<String, Object> outParams = new HashMap<String, Object>();
			outParams.put("merchandiseId", itemModel.getOutGoodsId());
			MerchandiseInfoMogo outMerchandise = merchandiseInfoMogoDao.findOne(outParams);

			itemModel.setInGoodsCode(inMerchandise.getGoodsCode());
			itemModel.setInGoodsNo(inMerchandise.getGoodsNo());
			itemModel.setInGoodsName(inMerchandise.getName());
			itemModel.setInBarcode(inMerchandise.getBarcode());
			itemModel.setOutGoodsNo(outMerchandise.getGoodsNo());
			itemModel.setOutGoodsName(outMerchandise.getName());
			itemModel.setOutGoodsCode(outMerchandise.getGoodsCode());
			itemModel.setOutBarcode(outMerchandise.getBarcode());
			itemModel.setCommbarcode(inMerchandise.getCommbarcode());

			BigDecimal taxPrice = itemModel.getTaxAmount().divide(new BigDecimal(itemModel.getNum()), 8 , BigDecimal.ROUND_HALF_UP);
			itemModel.setTaxPrice(taxPrice);
		}

		//预申报单
		SaleReturnDeclareOrderModel model = new SaleReturnDeclareOrderModel();
		BeanUtils.copyProperties(dto,model);
		model.setMerchantName(merchantBuRelMongo.getMerchantName());
		model.setBuName(merchantBuRelMongo.getBuName());
		model.setCustomerName(customer == null ? "" :customer.getName());
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
			List<Long> saleReturnIds = new ArrayList<>();
			if(StringUtils.isNotBlank(dto.getSaleReturnOrderCodes())) {
				String[] arr = dto.getSaleReturnOrderCodes().split(",");
				// 更新销售退单状态
				for (String code : arr) {
					SaleReturnOrderModel queryModel = new SaleReturnOrderModel() ;
					queryModel.setCode(code);
					queryModel = saleReturnOrderDao.searchByModel(queryModel);

					SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
					saleReturnOrderModel.setId(queryModel.getId());
					saleReturnOrderModel.setIsGenerateDeclare("1");// 0-否 1-是
					saleReturnOrderDao.modify(saleReturnOrderModel);
					saleReturnIds.add(queryModel.getId());
				}
				model.setSaleReturnOrderIds(StringUtils.join(saleReturnIds , ","));
				model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTOD));
				model.setStatus(DERP_ORDER.SALEDECLARE_STATUS_001);
				model.setApiStatus(DERP_ORDER.SALEDECLARE_APISTATUS_0);
				model.setCreateDate(TimeUtils.getNow());
				model.setCreater(user.getId());
				model.setCreateName(user.getName());
				num = saleReturnDeclareOrderDao.save(model) ;

				//更新销售退出库单 预申报单号
				for(Long saleReturenId : saleReturnIds) {
					SaleReturnOdepotModel saleReturnOdepotModel = new SaleReturnOdepotModel();
					saleReturnOdepotModel.setOrderId(saleReturenId);
					saleReturnOdepotModel = saleReturnOdepotDao.searchByModel(saleReturnOdepotModel);
					if(saleReturnOdepotModel != null) {
						saleReturnOdepotModel.setReturnDeclareOrderId(num);
						saleReturnOdepotModel.setReturnDeclareOrderCode(model.getCode());
						saleReturnOdepotModel.setModifyDate(TimeUtils.getNow());
						saleReturnOdepotDao.modify(saleReturnOdepotModel);
					}
				}

			}
			//新增表体
			for(SaleReturnDeclareOrderItemModel itemModel : dto.getItemList()) {
				itemModel.setReturnDeclareOrderId(num);
				saleReturnDeclareOrderItemDao.save(itemModel);
			}

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_17, model.getCode(), "新增", null, null);
			for(String returnCode : model.getSaleReturnOrderCodes().split(",")){
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_16,returnCode, "生成销售退预申报", null, null);
			}

		}else {
			//更新表头数据
			model.setModifier(user.getId());
			model.setModifierUsername(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			saleReturnDeclareOrderDao.updateWithNull(model);

			//更新表体
			for(SaleReturnDeclareOrderItemModel itemModel : dto.getItemList()) {
				saleReturnDeclareOrderItemDao.modify(itemModel);
			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_17, model.getCode(), "编辑", null, null);
		}

		return model;
	}

	/**
	 * 可核销量=上架入库量+临时结转量+账单入库量+调拨入量-账单出库量-调拨出量-销售退货量
	 */
	private Integer getVerifiNum(Long merchantId, Long depotId, String poNo,List<String> goodsNoList){

		/**公共查询条件*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId",merchantId);
		paramMap.put("depotId",depotId);
		paramMap.put("poNo",poNo);
		paramMap.put("goodsNoList",goodsNoList);

		//1.上架入库量
		Integer shelfInNum = shelfIdepotItemDao.getshelfInNum(paramMap);
		if (shelfInNum == null) shelfInNum = 0;

		//2.临时结转量
		Integer jztemNum = pojzTempDao.getPojzNum(paramMap);
		if (jztemNum == null) jztemNum = 0;

		//3.账单入库量
		paramMap.put("operateType", DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//库存调整类型  0 调减 1调增
		Integer billInNum = billOutinDepotItemDao.getBillOutInDepotNum(paramMap);
		if(billInNum ==null) billInNum = 0;

		//4.账单出库量
		paramMap.put("operateType",DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//库存调整类型  0 调减 1调增
		Integer billOutNum = billOutinDepotItemDao.getBillOutInDepotNum(paramMap);
		if(billOutNum ==null) billOutNum = 0;
		//唯品暂存区
		Map<String,Object> depotParam = new HashMap<>();
		depotParam.put("depotCode", "WPTH001");
		DepotInfoMongo wzcDepot = depotInfoMongoDao.findOne(depotParam);
		//唯品备查库
		depotParam.put("depotCode", "VIP001");
		DepotInfoMongo vipDepot = depotInfoMongoDao.findOne(depotParam);

		//5.调拨入量=调拨单调出仓库为“唯品暂存区”，且调入仓库为“唯品备查库” 已入库的入库数量
		paramMap.put("outDepotId", wzcDepot.getDepotId());
		paramMap.put("inDepotId", vipDepot.getDepotId());
		Integer transferInNum = transferOrderDao.getTransferInNumByMap(paramMap);
		if(transferInNum==null) transferInNum=0;

		//6.调拨出量=调拨单调出仓库为“唯品备查库”，调入仓库为“唯品暂存区” 已出库的数量
		paramMap.put("outDepotId", vipDepot.getDepotId());
		paramMap.put("inDepotId", wzcDepot.getDepotId());
		Integer transferOutNum = transferOrderDao.getTransferOutNumByMap(paramMap);
		if(transferOutNum==null) transferOutNum=0;

		//7.销售退货数量
		Integer saleRetrunNum = saleReturnOrderDao.getReturnCount(paramMap); // 退货量(好品+坏品)
		if (saleRetrunNum == null) saleRetrunNum = 0;

		//可核销量=上架入库量+临时结转量+账单入库量+调拨入量-账单出库量-调拨出量-销售退货量
		Integer verifiNum = shelfInNum+jztemNum+billInNum+transferInNum-billOutNum-transferOutNum-saleRetrunNum;
		return verifiNum;
	}
	@Override
	public List<Map<String, Object>> listForExport(SaleReturnDeclareOrderDTO dto, User user) throws Exception {
		if (dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if (buIds.isEmpty()) {
				return new ArrayList<>();
			}
			dto.setBuList(buIds);
		}
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		List<SaleReturnDeclareOrderDTO> list = saleReturnDeclareOrderDao.listReturnDeclareOrder(dto);
		for (SaleReturnDeclareOrderDTO orderDTO : list) {
			SaleReturnDeclareOrderItemModel itemModel = new SaleReturnDeclareOrderItemModel();
			itemModel.setReturnDeclareOrderId(orderDTO.getId());
			List<SaleReturnDeclareOrderItemModel> itemList = saleReturnDeclareOrderItemDao.list(itemModel);
			for (SaleReturnDeclareOrderItemModel item : itemList) {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", orderDTO.getCode());
				map.put("saleReturnOrderCode", item.getSaleReturnOrderCode());
				map.put("buName", orderDTO.getBuName());
				map.put("outDepotName", orderDTO.getOutDepotName());
				map.put("inDepotName", orderDTO.getInDepotName());
				map.put("status", orderDTO.getStatusLabel());
				map.put("currency", orderDTO.getCurrencyLabel());
				map.put("lbxNo", orderDTO.getLbxNo());
				map.put("tallyingUnit", orderDTO.getTallyingUnitLabel());
				map.put("tradeTerms", orderDTO.getTradeTermsLabel());
				map.put("returnReason", orderDTO.getReturnReason());
				map.put("createName", orderDTO.getCreateName());
				map.put("createDate", orderDTO.getCreateDate());
				map.put("auditName", orderDTO.getAuditUsername());
				map.put("auditerDate", orderDTO.getAuditerDate());
				map.put("loadPort", orderDTO.getLoadPort());
				String portDes = StringUtils.isBlank(orderDTO.getPortDestLabel())? "" : orderDTO.getPortDestLabel().substring(orderDTO.getPortDestLabel().indexOf("：")+1);
				map.put("portDes", portDes);
				map.put("deliveryAddr", orderDTO.getDeliveryAddr());
				map.put("shipper", orderDTO.getShipper());
				map.put("invoiceNo", orderDTO.getInvoiceNo());
				map.put("contractNo", orderDTO.getContractNo());
				map.put("billWeight", orderDTO.getBillWeight());
				map.put("boxNum", orderDTO.getBoxNum());
				map.put("torrNum", orderDTO.getTorrNum());
				map.put("torrPackType", DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList,orderDTO.getTorrPackType()));
				map.put("pack", orderDTO.getPack());
				map.put("transport", DERP.getLabelByKey(DERP.transportList,orderDTO.getTransport()));
				map.put("outCustomsName", orderDTO.getOutCustomsName());
				map.put("inCustomsName", orderDTO.getInCustomsName());
				map.put("ladingBill", orderDTO.getLadingBill());
				map.put("mark", orderDTO.getMark());
				map.put("remark", orderDTO.getRemark());
				map.put("poNo", item.getPoNo());
				map.put("inGoodsNo", item.getInGoodsNo());
				map.put("inGoodsName", item.getInGoodsName());
				map.put("inBarcode", item.getInBarcode());
				map.put("num", item.getNum());
				map.put("price", item.getPrice());
				map.put("amount", item.getAmount());
				map.put("taxRate", item.getTaxRate());
				map.put("tax", item.getTax());
				map.put("batchNo", item.getBatchNo());
				map.put("brandName", item.getBrandName());
				map.put("grossWeightSum", item.getGrossWeightSum());
				map.put("netWeightSum", item.getNetWeightSum());
				map.put("boxNo", item.getBoxNo());
				mapList.add(map);
			}
		}
		return mapList;
	}

}
