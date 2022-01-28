package com.topideal.order.service.purchase.impl;

import com.alibaba.fastjson.JSONArray;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.*;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.LogisticsAttorneyDao;
import com.topideal.dao.common.PackingDetailsDao;
import com.topideal.dao.purchase.*;
import com.topideal.entity.dto.common.CustomsDeclareItemDTO;
import com.topideal.entity.dto.common.CustomsPackingDetailsDTO;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.purchase.DeclareOrderDTO;
import com.topideal.entity.dto.purchase.DeclareOrderItemDTO;
import com.topideal.entity.dto.purchase.FirstCustomsInfoDTO;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.pushapi.epass.e01.PurchaseGoodsListJson;
import com.topideal.json.pushapi.epass.e01.PurchaseOrderRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.DeclareOrderService;
import com.topideal.order.webapi.purchase.form.DeclareDetailsAddForm;
import com.topideal.order.webapi.purchase.form.DeclareOrderDeliveryForm;
import com.topideal.order.webapi.purchase.form.DeclareOrderDeliveryItemForm;
import com.topideal.order.webapi.sale.form.MerchandiseForm;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预审报单 impl
 *
 * @author lian_
 *
 */
@Service
public class DeclareOrderServiceImpl implements DeclareOrderService {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(DeclareOrderServiceImpl.class);

	// 预审报单
	@Autowired
	private DeclareOrderDao declareOrderDao;
	// 预审报单表体
	@Autowired
	private DeclareOrderItemDao declareOrderItemDao;
	// mq
	@Autowired
	private RMQProducer rocketMQProducer;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 仓库
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	// 商品
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private FirstCustomsInfoDao firstCustomsInfoDao;
	//商家信息
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	//原产国
	@Autowired
	private CountryOriginMongoDao countryOriginMongoDao;

	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;

	@Autowired
	private UnitMongoDao unitMongoDao ;

	@Autowired
	private DepotCustomsRelMongoDao depotCustomsRelMongoDao ;

	@Autowired
	private PackingDetailsDao packingDetailsDao ;
	@Autowired
	private DeclarePurchaseRelDao declarePurchaseRelDao ;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao ;
	@Autowired
	private LogisticsAttorneyDao logisticsAttorneyDao ;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao ;
	@Autowired
	private BrandSuperiorMongoDao brandSuperiorMongoDao;
	@Autowired
	private MerchandiseCustomsRelMongoDao merchandiseCustomsRelMongoDao;
	@Autowired
	private FileTempDao fileTempDao;
	@Autowired
	private BrandMongoDao brandMongoDao;

	/**
	 * 预审报单（分页）
	 *
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DeclareOrderDTO listDeclare(DeclareOrderDTO dto,User user) throws SQLException {

		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}

			dto.setBuIds(buIds);
		}

		dto = declareOrderDao.getListByPage(dto);
		List<DeclareOrderDTO> list = dto.getList();
		for (DeclareOrderDTO d : list) {
			// 根据预申报单id获取采购订单编码集合
			List<PurchaseOrderModel> resultList = declareOrderDao.getPurchaseListById(d.getId());
			if (resultList != null) {
				if (resultList.size() == 1) {
					// 采购订单编码
					d.setPurchaseCode(resultList.get(0).getCode());
				} else if (resultList.size() > 1) {
					String code = "";
					for (PurchaseOrderModel p : resultList) {
						code += p.getCode() + ",";
					}
					// 采购订单编码
					d.setPurchaseCode(code.substring(0, code.length() - 1));
				}
			}
		}
		dto.setList(list);
		return dto;
	}

	/**
	 * 根据id获取商品详情
	 *
	 * @param id
	 * @return
	 */
	@Override
	public DeclareOrderModel searchDetail(Long id) throws SQLException {
		return declareOrderDao.searchById(id);
	}

	/**
	 * 修改
	 * @throws Exception
	 */
	@Override
	public void modifyDeclare(DeclareOrderModel model, LogisticsAttorneyModel logisticsAttorneyModel, User user, String purchaseCode,List<PackingDetailsModel> packingList) throws Exception {

		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("depotId", model.getDepotId()) ;
		DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);

		if(!DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())
				&& StringUtils.isBlank(model.getTransport())) {
			throw new DerpException("运输方式不能为空") ;
		}

		if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())){
			// 必填项空值校验
			Boolean isNull = new EmptyCheckUtils().addObject(model.getTallyingUnit()).empty();

			if(isNull) {
				throw new DerpException("当海外仓时，理货单位不能为空") ;
			}
		}

		if(model.getPortDestNo()!=null && !"".equals(model.getPortDestNo())){
			if(model.getPortDestNo().equals(DERP.PORTDEST_44011501)){
				String portDest = DERP.getLabelByKey(DERP.portDestList, DERP.PORTDEST_44011501);
				portDest = portDest.split("：")[1] ;
				model.setPortDest(portDest);
			}else if(model.getPortDestNo().equals(DERP.PORTDEST_44010318)){
				String portDest = DERP.getLabelByKey(DERP.portDestList, DERP.PORTDEST_44010318);
				portDest = portDest.split("：")[1] ;
				model.setPortDest(portDest);
			}else if(model.getPortDestNo().equals(DERP.PORTDEST_21021001)){
				String portDest = DERP.getLabelByKey(DERP.portDestList, DERP.PORTDEST_21021001);
				portDest = portDest.split("：")[1] ;
				model.setPortDest(portDest);
			}else if(model.getPortDestNo().equals(DERP.PORTDEST_50010001)){
				String portDest = DERP.getLabelByKey(DERP.portDestList, DERP.PORTDEST_50010001);
				portDest = portDest.split("：")[1] ;
				model.setPortDest(portDest);
			}
		}

		// 入库关区
		if(model.getInCustomsId() != null) {

			Map<String, Object> relParams = new HashMap<String, Object>() ;
			relParams.put("depotId", model.getDepotId());
			relParams.put("customsAreaId", model.getInCustomsId());

			DepotCustomsRelMongo depotCustomsRel = depotCustomsRelMongoDao.findOne(relParams);

			if(depotCustomsRel != null) {
				model.setInCustomsCode(depotCustomsRel.getCustomsAreaCode());
				model.setInPlatformCustoms(depotCustomsRel.getCustomsAreaName());
			}

		}
		//同一商品，申报单价必须一致
		List<DeclareOrderItemModel> itemList = model.getItemList();
		Map<Long,BigDecimal> checkPriceMap = new HashMap<>();
		for (DeclareOrderItemModel item : itemList) {
			if(item.getGoodsId() == null){
				throw new DerpException("请选择商品") ;
			}

			BigDecimal queryPrice = checkPriceMap.get(item.getGoodsId());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", item.getGoodsId());
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);
			if(queryPrice == null){
				checkPriceMap.put(item.getGoodsId(), item.getPrice());
			}else if(queryPrice.compareTo(item.getPrice()) != 0){
				throw new DerpException("同一商品货号："+mogo.getGoodsNo()+"，申报单价必须一致") ;
			}
			if(mogo != null){
				params.clear();
				params.put("goodsNo", mogo.getGoodsNo());
				params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
				List<MerchandiseInfoMogo> goodsList = merchandiseInfoMogoDao.findMerchandiseByDepotId(params, depot.getDepotId());
				if(goodsList == null || goodsList.size() < 1){
					throw new DerpException("商品货号："+mogo.getGoodsNo()+"，未与入库仓："+depot.getName()+" 关联绑定") ;
				}
			}

			//根据采购明细id获取已入库数量
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("purchaseItemId", item.getPurchaseItemId());
			paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
			List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
			Integer warehouseNum = 0;
			if(numList != null && numList.size() > 0){
				BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
				warehouseNum = queryNum.intValue();
			}
			Integer declareNum = 0;
			List<String> statusList = new ArrayList<>();
			statusList.add(DERP_ORDER.DECLAREORDER_STATUS_001);
			statusList.add(DERP_ORDER.DECLAREORDER_STATUS_002);
			statusList.add(DERP_ORDER.DECLAREORDER_STATUS_004);
			List<Long> unNeedDeclareIds = new ArrayList<>();
			if(model.getId() != null){//新增、复制不需要过滤当前预申报单
				unNeedDeclareIds.add(model.getId());
			}
			paramMap.clear();
			paramMap.put("purchaseItemId", item.getPurchaseItemId());
			paramMap.put("statusList", statusList);
			paramMap.put("unNeedDeclareId", unNeedDeclareIds);
			DeclareOrderItemModel declareModel = declareOrderItemDao.getDeclareOrderItems(paramMap);
			if(declareModel != null){
				declareNum = declareModel.getNum();
			}
			PurchaseOrderItemModel purchaseItemModel = purchaseOrderItemDao.searchById(item.getPurchaseItemId());
			Integer unDeclareNum = purchaseItemModel.getNum() - warehouseNum - declareNum;//待预申报量 = 采购量 - 已入库量 - 预申报但未入库的量（不包括当前与申报单）
			if(item.getNum().intValue() > unDeclareNum.intValue()){
				throw new RuntimeException("采购订单："+item.getPurchase()+"，商品货号："+ item.getGoodsNo() +" 累计申报数量不能大于采购数量，剩余申报数量为:"+unDeclareNum) ;
			}
		}

		if(model.getId() == null) {

			Map<String, Object> relParams = new HashMap<String, Object>() ;
			relParams.put("merchantId", user.getMerchantId());
			relParams.put("buId", model.getBuId());
			MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(relParams);
			if(merchantBuRelMongo != null) {
				model.setBuName(merchantBuRelMongo.getBuName());
			}

			model.setCreateDate(TimeUtils.getNow());
			model.setCreater(user.getId());
			model.setCreateName(user.getName());
			model.setTopidealCode(user.getTopidealCode());
			model.setState(DERP_ORDER.DECLAREORDER_STATE_0);

			declareOrderDao.save(model) ;

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, model.getCode(), "新增", null, null);

			if(StringUtils.isNotBlank(purchaseCode)) {

				String[] arr = purchaseCode.split(",");

				// 添加预申报单与采购订单关系
				for (String code : arr) {

					PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
					queryModel.setCode(code);

					queryModel = purchaseOrderDao.searchByModel(queryModel);

					DeclarePurchaseRelModel relModel = new DeclarePurchaseRelModel();
					relModel.setDeclareOrderId(model.getId());// 预申报单ID
					relModel.setPurchaseOrderId(queryModel.getId());// 采购订单id
					declarePurchaseRelDao.save(relModel);

					PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
					purchaseOrderModel.setId(queryModel.getId());
					purchaseOrderModel.setIsGenerate(DERP_ORDER.PURCHASEORDER_ISGENERATE_1);// 是
					purchaseOrderDao.modify(purchaseOrderModel);

					commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, queryModel.getCode(), "生成预申报单", null, null);
				}
			}


		}else {

			model.setModifier(user.getId());
			model.setModifyDate(TimeUtils.getNow());
			declareOrderDao.modify(model);

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, model.getCode(), "编辑", null, null);

		}

		/**物理委托书**/
		LogisticsAttorneyModel queryModel = new LogisticsAttorneyModel() ;
		queryModel.setOrderId(model.getId());
		queryModel.setType("1");

		queryModel = logisticsAttorneyDao.searchByModel(queryModel) ;

		if(queryModel == null) {
			logisticsAttorneyModel.setOrderId(model.getId());
			logisticsAttorneyModel.setCreateDate(TimeUtils.getNow());

			logisticsAttorneyDao.save(logisticsAttorneyModel) ;
		}else {

			logisticsAttorneyModel.setId(queryModel.getId());
			logisticsAttorneyModel.setModifyDate(TimeUtils.getNow());

			logisticsAttorneyDao.modify(logisticsAttorneyModel) ;
		}

		// 根据表头id删除表体，重新添加
		declareOrderDao.delById(model.getId());

		Set<String> originSet = new TreeSet<>() ; //原产国

		// 新增表体
		for (DeclareOrderItemModel item : itemList) {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", item.getGoodsId());
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);

			item.setDeclareOrderId(model.getId());// 表头id
			item.setGoodsCode(mogo.getGoodsCode());// 商品编码
			item.setGoodsName(mogo.getName());// 商品名称
			item.setGoodsNo(mogo.getGoodsNo());// 商品货号
			item.setGoodsSpec(mogo.getSpec());

			Map<String, Object> countryParams = new HashMap<String, Object>();
			countryParams.put("countryOriginId", mogo.getCountyId());
			CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(countryParams);

			if(countryOrigin != null){
				originSet.add(countryOrigin.getName()) ;
			}

			declareOrderItemDao.save(item);
		}

		//没有则生成，有则更新一线清关资料
		FirstCustomsInfoModel firstCustomsInfoQueryModel = new FirstCustomsInfoModel() ;
		firstCustomsInfoQueryModel.setDeclareOrderId(model.getId());

		firstCustomsInfoQueryModel = firstCustomsInfoDao.searchByModel(firstCustomsInfoQueryModel) ;

		if(firstCustomsInfoQueryModel == null) {
			saveCustomsDeclare(model.getId()) ;
		}else {

			//签订日期 为 装船日期前2天
			Timestamp shipDate = model.getShipDate();

			if(shipDate != null) {
				Timestamp signingDate = TimeUtils.addDay(shipDate, -2);
				firstCustomsInfoQueryModel.setSigningDate(signingDate);//签订日期
			}

			firstCustomsInfoQueryModel.setShipDate(shipDate);
			firstCustomsInfoQueryModel.setInvoiceNo(model.getInvoiceNo());
			firstCustomsInfoQueryModel.setShipper(model.getShipper());
			firstCustomsInfoQueryModel.setContractNo(model.getContractNo());
			firstCustomsInfoQueryModel.setPack(model.getPackType());
			firstCustomsInfoQueryModel.setPortLoading(model.getPortLoading());// 装货港

			String originName = StringUtils.join(originSet, ";") ;
			firstCustomsInfoQueryModel.setCountryOrigin(originName);
			firstCustomsInfoQueryModel.setCountry(originName);

			firstCustomsInfoQueryModel.setModifyDate(TimeUtils.getNow());
			firstCustomsInfoQueryModel.setModifier(user.getId());

			firstCustomsInfoDao.modify(firstCustomsInfoQueryModel) ;
		}
		//删除原有的关联箱装明细
		PackingDetailsModel packingDetailsModel = new PackingDetailsModel();
		packingDetailsModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_1);
		packingDetailsModel.setOrderId(model.getId());
		packingDetailsDao.deleteByModel(packingDetailsModel);
		//保存装箱明细
		if(packingList != null && packingList.size() > 0){
			for (PackingDetailsModel packingModel : packingList) {
				packingModel.setOrderId(model.getId());
				packingModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_1);
				packingModel.setCreateDate(TimeUtils.getNow());
				packingDetailsDao.save(packingModel) ;
			}
		}

	}

	/**
	 * 批量提交采购订单
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Override
	public void submitDeclareOrder(Long id, User user) throws Exception {

		//获取商家的邮箱
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", user.getMerchantId());
		MerchantInfoMongo merchantMogo = merchantInfoMongoDao.findOne(merchantParams);

		PurchaseOrderRootJson vo = declareOrderDao.searchVoById(id);
		vo.setTopideal_code(user.getTopidealCode());
		vo.setDatasource("DISTRIBUTED");
		vo.setEmail_add(merchantMogo.getEmail());//邮箱

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depotId", Long.parseLong(vo.getWarehouse_id()));
		DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

		if (depot != null) {
			//若采购入库仓库是 在对应商家下的“进出接口指令”标识为“是”的，才走此接口下推指令；
			params.put("merchantId", user.getMerchantId());
			DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(params);

			if(depotMerchantRel == null
					|| DERP_SYS.DEPOTMERCHANTREL_ISOUTDEPENDIN_0.equals(depotMerchantRel.getIsInOutInstruction())) {
				throw new DerpException(vo.getEnt_inbound_id() + "对应公司下的“进出接口指令”不为“是”") ;
			}

			//海外仓 ，业务模式必填B2B
			if(depot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)){
				vo.setBusiness_moshi(DERP_ORDER.ORDER_IMPORTMODE_10);//业务模式
				vo.setBusinessUnit("30");
			}

			vo.setWarehouse_id(depot.getCode());// 仓库编码
		}

		DeclarePurchaseRelModel queryModel = new DeclarePurchaseRelModel() ;
		queryModel.setDeclareOrderId(id);
		List<DeclarePurchaseRelModel> relList = declarePurchaseRelDao.list(queryModel);

		List<Long> ids = relList.stream().map(DeclarePurchaseRelModel::getPurchaseOrderId).collect(Collectors.toList());
		String purchaseIds = StringUtils.join(ids, ",") ;

		Map<String, PurchaseGoodsListJson> sumMap = new LinkedHashMap<>() ;
		for (PurchaseGoodsListJson item : vo.getGoods_list()) {
			if(item.getPurchase_item_id() == null){
				throw new DerpException("预申报单表体对应的商品关联的采购明细id为空，请及时维护");
			}
		}

		int index = 1;
		for (PurchaseGoodsListJson item : vo.getGoods_list()) {
			if (item.getBargainno() == null || "".equals(item.getBargainno())) {
				item.setBargainno(vo.getContract_no());
			}
			DecimalFormat fmt = new DecimalFormat("0.00");
			String price = fmt.format(Double.parseDouble(item.getPrice()));
			item.setPrice(price);
			item.setIndex(index + "");

			PurchaseGoodsListJson queryItemModel = sumMap.get(item.getGoods_id());
			if(queryItemModel != null){
				BigDecimal amount = new BigDecimal(queryItemModel.getAmount()).add(new BigDecimal(item.getAmount()));
				BigDecimal netWeightSum = new BigDecimal(queryItemModel.getNet_weight()).add(new BigDecimal(item.getNet_weight()));
				BigDecimal grossWeightSum = new BigDecimal(queryItemModel.getWeight()).add(new BigDecimal(item.getWeight()));

				item.setNet_weight(netWeightSum.toPlainString());
				item.setWeight(grossWeightSum.toPlainString());
				item.setAmount(amount.toPlainString());
			}
			sumMap.put(item.getGoods_id(), item) ;
			index++;
		}

		//包装方式默认为9999-其他
		vo.setPack_type("9999");

		if(!sumMap.isEmpty()) {
			vo.setGoods_list(new ArrayList<>(sumMap.values()));
		}

		JSONObject jsonObject = JSONObject.fromObject(vo);
		jsonObject.put("method", EpassAPIMethodEnum.EPASS_E01_METHOD.getMethod());

		//下推跨境宝
		rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(),
				MQPushApiEnum.PUSH_EPASS.getTags());

		DeclareOrderModel dModel = new DeclareOrderModel();
		dModel.setId(id);
		dModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_004);
		dModel.setSubmitter(user.getName());
		dModel.setSubmitDate(TimeUtils.getNow());
		dModel.setState(DERP_ORDER.DECLAREORDER_STATE_1);
		dModel.setPushWarehouseDate(TimeUtils.getNow());
		declareOrderDao.modify(dModel);

		FirstCustomsInfoModel firstModel = new FirstCustomsInfoModel() ;
		firstModel.setDeclareOrderId(id);

		firstModel = firstCustomsInfoDao.searchByModel(firstModel) ;

		if(firstModel == null) {
			saveCustomsDeclare(id) ;
		}

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4,
				vo.getEnt_inbound_id(), "下推指令", null, null);
	}

	/**
	 * 批量取消预申报单
	 *
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	@Override
	public void delDeclare(List<Long> list, User user) throws SQLException, DerpException {

		for (Long id : list) {
			// 根据id获取信息
			DeclareOrderModel model = declareOrderDao.searchById(id);
			// 仅对“未推接口”的单据删除
			if (DERP_ORDER.DECLAREORDER_STATE_1.equals(model.getState())) {
				throw new DerpException("操作失败，预申报单："+model.getCode()+"已推接口，无法删除");
			}
			// 仅对“上架入库”的单据删除
			if (DERP_ORDER.DECLAREORDER_STATUS_003.equals(model.getStatus())) {
				throw new DerpException("操作失败，预申报单："+model.getCode()+"状态为已上架，无法删除");
			}
		}

		List<Long> purchaseIds = new ArrayList<Long>() ;

		for (Long id : list) {
			// 删除预申报采购联关系
			DeclarePurchaseRelModel queryRel = new DeclarePurchaseRelModel() ;
			queryRel.setDeclareOrderId(id);
			List<DeclarePurchaseRelModel> relList = declarePurchaseRelDao.list(queryRel);
			for (DeclarePurchaseRelModel relModel : relList) {
				purchaseIds.add(relModel.getPurchaseOrderId()) ;

			}

			List<Long> ids = relList.stream().map(DeclarePurchaseRelModel::getId).collect(Collectors.toList());

			declarePurchaseRelDao.delete(ids) ;

			// 根据id获取信息
			DeclareOrderModel model = new DeclareOrderModel();
			model.setId(id);
			model.setStatus(DERP.DEL_CODE_006);// 已取消
			model.setSubmitter(user.getName());
			model.setSubmitDate(TimeUtils.getNow());
			declareOrderDao.modify(model);

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, model.getCode(), "删除", null, null);

		}

		for (Long purchaseId : purchaseIds) {

			DeclarePurchaseRelModel queryRel = new DeclarePurchaseRelModel() ;
			queryRel.setPurchaseOrderId(purchaseId);

			List<DeclarePurchaseRelModel> relList = declarePurchaseRelDao.list(queryRel);

			if(relList.isEmpty()) {

				PurchaseOrderModel updateModel = new PurchaseOrderModel() ;

				updateModel.setId(purchaseId);
				updateModel.setIsGenerate(DERP_ORDER.PURCHASEORDER_ISGENERATE_0);

				purchaseOrderDao.modify(updateModel) ;

			}

		}

	}

	/**
	 * 根据表头ID获取表体(包括商品信息)
	 *
	 * @param id
	 * @return
	 */
	@Override
	public List<DeclareOrderItemModel> getItem(Long id,String type) throws SQLException {

		List<DeclareOrderItemModel> items = declareOrderDao.getItem(id);

		for (DeclareOrderItemModel item : items) {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", item.getGoodsId());
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);

			if (mogo == null) {
				continue ;
			}

			//如果type为2，则说明是复制，设置采购数量为0
			if("2".equals(type)){
				item.setNum(0);
				item.setDeclareOrderId(null);
				item.setId(null);
			}

			//获取申报单价
			item.setBarcode(mogo.getBarcode());// 条形码

		}

		return items;
	}

	/**
	 * 导出获取表体
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<DeclareOrderItemDTO> getDetailsByExport(Long id)throws SQLException {

		DeclareOrderItemDTO itemDto=new DeclareOrderItemDTO();
		itemDto.setDeclareOrderId(id);
		List<DeclareOrderItemDTO> orderDtoItem= declareOrderItemDao.getDetailsByExport(itemDto);

//		Map<Long,String> mapPurchaseCoderMap=new HashMap<>();
		Map<Long,String> mapBrandSupriror=new HashMap<>();

		for(DeclareOrderItemDTO order:orderDtoItem){
			if(mapBrandSupriror.containsKey(order.getGoodsId())){
				order.setBrand(mapBrandSupriror.get(order.getGoodsId()));
			}else{
				//获取商品的品牌名称
				BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(order.getGoodsId());
				if(brandSuperior!=null){
					order.setBrand(brandSuperior.getName());
					mapBrandSupriror.put(order.getGoodsId(),order.getBrand());
				}
			}
		}
		return orderDtoItem;
	}

	/**
	 * 导出获取表头
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<DeclareOrderDTO> getDeclareOrderByExport(DeclareOrderDTO dto, User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				return new ArrayList<DeclareOrderDTO>();
			}

			dto.setBuIds(buIds);
		}

		return declareOrderDao.getDeclareOrderByExport(dto);
	}

	/**
	 * 一线清关资料导出
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public FirstCustomsInfoDTO getCustomsDeclare(Long id) throws SQLException {

		FirstCustomsInfoModel model = new FirstCustomsInfoModel();
		model.setDeclareOrderId(id);
		model = firstCustomsInfoDao.searchByModel(model);

		if (model == null) {
			return null ;
		}

		DeclareOrderModel declareOrder = declareOrderDao.searchById(id);

		DeclareOrderItemModel itemModel = new DeclareOrderItemModel();
		itemModel.setDeclareOrderId(id);

		List<DeclareOrderItemModel> list = declareOrderItemDao.list(itemModel);
		List<CustomsDeclareItemDTO> dtoList = new ArrayList<CustomsDeclareItemDTO>() ;

		Set<String> originSet = new TreeSet<>() ; //原产国

		Map<String,List<DeclareOrderItemModel>> itemMap = list.stream().collect(Collectors.groupingBy(DeclareOrderItemModel::getGoodsNo));
//		for (DeclareOrderItemModel item : list) {
		for (String goodsNo : itemMap.keySet()) {
			List<DeclareOrderItemModel> itemList = itemMap.get(goodsNo);
			DeclareOrderItemModel item = itemList.get(0);
			Integer totalItemNum = itemList.stream().mapToInt(DeclareOrderItemModel::getNum).sum();
			Double totalItemNetWeight = itemList.stream().mapToDouble(DeclareOrderItemModel::getNetWeightSum).sum();
			Double totalItemGrossWeight = itemList.stream().mapToDouble(DeclareOrderItemModel::getGrossWeightSum).sum();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", item.getGoodsId());
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);

			if (mogo == null) {
				continue ;
			}

			//获取申报单价
			item.setBarcode(mogo.getBarcode());// 条形码

			CustomsDeclareItemDTO itemDto = new CustomsDeclareItemDTO() ;
			BeanUtils.copyProperties(item, itemDto);
			itemDto.setNum(totalItemNum);
			itemDto.setGoodsSpec(mogo.getSpec());
			itemDto.setNetWeight(totalItemNetWeight);
			itemDto.setGrossWeight(totalItemGrossWeight);
			itemDto.setConstituentRatio(mogo.getComponent());

			Map<String, Object> unitParams = new HashMap<String, Object>();
			unitParams.put("unitId", mogo.getUnit());
			UnitMongo unit = unitMongoDao.findOne(unitParams);

			if(unit != null) {
				itemDto.setUnit(unit.getName());
			}

			Map<String, Object> countryParams = new HashMap<String, Object>();
			countryParams.put("countryOriginId", mogo.getCountyId());
			CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(countryParams);

			if(countryOrigin != null){

				itemDto.setCountryName(countryOrigin.getName());

				originSet.add(countryOrigin.getName()) ;
			}
			BigDecimal amount = item.getPrice().multiply(new BigDecimal(totalItemNum));
			itemDto.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
			dtoList.add(itemDto) ;

		}

		String originName = StringUtils.join(originSet, ";") ;
		model.setCountryOrigin(originName);

		if(StringUtils.isBlank(model.getCountry())){
			model.setCountry(originName);
		}

		FirstCustomsInfoDTO dto = new FirstCustomsInfoDTO() ;

		BeanUtils.copyProperties(model, dto);
		dto.setTorrNum(declareOrder.getTorrNum());
		dto.setItemList(dtoList);

		Map<String, Object> depotMerchantParams = new HashMap<String, Object>();
		depotMerchantParams.put("merchantId", declareOrder.getMerchantId());
		depotMerchantParams.put("depotId", declareOrder.getDepotId());
		DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantParams);

		if(depotMerchantRel != null) {
			dto.setSignNo(depotMerchantRel.getContractCode());
		}

		/**构造包装明细*/
		List<CustomsPackingDetailsDTO> packDtoList = new ArrayList<CustomsPackingDetailsDTO>() ;

		PackingDetailsModel queryModel = new PackingDetailsModel() ;

		queryModel.setOrderId(id);
		queryModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_1);

		List<PackingDetailsModel> packingDetailsModelList = packingDetailsDao.listGroupCabinetNo(queryModel) ;

		if(!packingDetailsModelList.isEmpty()){

			for (int i = 0 ; i < packingDetailsModelList.size(); i ++) {

				PackingDetailsModel packingDetailsModel = packingDetailsModelList.get(i);

				CustomsPackingDetailsDTO tepmDto = new CustomsPackingDetailsDTO() ;

				BeanUtils.copyProperties(packingDetailsModel, tepmDto);

				DeclareOrderItemModel queryItemModel = new DeclareOrderItemModel() ;

				queryItemModel.setDeclareOrderId(id);
				queryItemModel.setGoodsNo(packingDetailsModel.getGoodsNo());

				queryItemModel = declareOrderItemDao.searchByModel(queryItemModel) ;

				if(queryItemModel != null){

					Double netWeight = queryItemModel.getNetWeightSum();
					BigDecimal netWeightBd = new BigDecimal(netWeight);

					netWeightBd = netWeightBd.divide(new BigDecimal(queryItemModel.getNum()), 5, BigDecimal.ROUND_HALF_UP) ;

					tepmDto.setGoodsName(queryItemModel.getGoodsName());
					tepmDto.setNetWeight(netWeightBd.doubleValue());
					tepmDto.setTotalGrossWeight(queryItemModel.getGrossWeightSum());
				}

				packDtoList.add(tepmDto) ;

			}

		}

		dto.setDetailsDTOList(packDtoList);
		/**构造包装明细*/

		return dto;
	}

	/**
	 * 编辑一线清关资料
	 * @param model
	 * @return
	 */
	@Override
	public boolean modifyCustomsDeclare(FirstCustomsInfoModel model, String[] goodsIds, String[] palletNos,
			String[] cartons, String name) throws SQLException, DerpException {
		model.setModifyDate(new Timestamp(new Date().getTime()));
		int num = firstCustomsInfoDao.modify(model);
		if (num > 0) {
			if (goodsIds.length > 0) {
				DeclareOrderItemModel itemModel = new DeclareOrderItemModel();
				itemModel.setDeclareOrderId(model.getDeclareOrderId());
				List<DeclareOrderItemModel> list = declareOrderItemDao.list(itemModel);
				for (DeclareOrderItemModel item : list) {
					for (int i = 0; i < goodsIds.length; i++) {
						String goodsId = item.getGoodsId() + "";
						if (goodsId.equals(goodsIds[i])) {
							DeclareOrderItemModel order = new DeclareOrderItemModel();
							order.setId(item.getId());
							if (palletNos[i] != null && !"".equals(palletNos[i])) {
								if (!" ".equals(palletNos[i])) {
									order.setPalletNo(palletNos[i].replace(" ", ""));
								}
							}
							if (cartons[i] != null && !"".equals(cartons[i])) {
								order.setCartons(Integer.parseInt(cartons[i]));
							}

							declareOrderItemDao.modify(order);
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 保存一线清关资料
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DerpException
	 */
	@Override
	public FirstCustomsInfoDTO saveCustomsDeclare(Long id)
			throws SQLException, DerpException {

		Set<String> originSet = new TreeSet<>() ; //原产国

		DeclareOrderModel declareOrder = declareOrderDao.searchById(id) ;

		DeclareOrderItemModel queryModel = new DeclareOrderItemModel() ;
		queryModel.setDeclareOrderId(id);

		List<DeclareOrderItemModel> itemList = declareOrderItemDao.list(queryModel);
		List<CustomsDeclareItemDTO> itemDtoList = new ArrayList<CustomsDeclareItemDTO>() ;

		for (DeclareOrderItemModel declareOrderItemModel : itemList) {

			CustomsDeclareItemDTO itemDto = new CustomsDeclareItemDTO() ;
			BeanUtils.copyProperties(declareOrderItemModel, itemDto);

			itemDto.setNetWeight(declareOrderItemModel.getNetWeightSum());
			itemDto.setGrossWeight(declareOrderItemModel.getGrossWeightSum());

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", declareOrderItemModel.getGoodsId());
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(params);

			Map<String, Object> unitParams = new HashMap<String, Object>();
			unitParams.put("unitId", merchandise.getUnit());
			UnitMongo unit = unitMongoDao.findOne(unitParams);

			if(unit != null) {
				itemDto.setUnit(unit.getName());
			}

			params = new HashMap<String, Object>();
			params.put("countryOriginId", merchandise.getCountyId());

			CountryOriginMongo country = countryOriginMongoDao.findOne(params);

			if(country != null) {

				itemDto.setCountryName(country.getName());

				originSet.add(country.getName()) ;
			}

			itemDtoList.add(itemDto) ;
		}

		//根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", declareOrder.getMerchantId());
		MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantParams);

		// 新增一线清关
		FirstCustomsInfoModel firstModel = new FirstCustomsInfoModel();
		String code = TimeUtils.format(new Date(), "ddHHmmsssss");
		firstModel.setCode("YXQG" + code);// 自编码

		if(merchant != null){
			firstModel.setFirstParty(merchant.getFullName());// 甲方 商家全称
			firstModel.setFirstPartyAddr(merchant.getRegisteredAddress());//甲方地址  商家地址
		}

		//签订日期 为 装船日期前2天
		Timestamp shipDate = declareOrder.getShipDate();

		if(shipDate != null) {
			Timestamp signingDate = TimeUtils.addDay(shipDate, -2);
			firstModel.setSigningDate(signingDate);//签订日期
		}

		firstModel.setInvoiceDate(declareOrder.getCreateDate());//发票date
		firstModel.setSigningAddr("中国广州市");//签订地点
		firstModel.setDeclareOrderId(id);// 预申报单id
		firstModel.seteAddressee("Guangdong Top Ideal Cross-border E-Commerce SCM Service Co., Ltd.");//Consignee
		firstModel.seteAddresseeAddr("Rm 401-2, No.3, Gangrong First Street, Nansha Dist., Guangzhou");//Address
		firstModel.setContractNo(declareOrder.getContractNo());// 合同号
		firstModel.setAddressee("广东卓志跨境电商供应链服务有限公司");//收货人
		firstModel.setAddresseeAddr("广州市南沙区港荣一街3号401-2房");//收货人地址
		firstModel.setSecondParty("广东卓志跨境电商供应链服务有限公司");// 乙方
		firstModel.setPack(declareOrder.getPackType());// 包装
		firstModel.setPortLoading(declareOrder.getPortLoading());// 装货港
		firstModel.setPayment("T/T"); //付款方式
		firstModel.setPortDest("NANSHA");// 卸货港
		firstModel.setPayRules("CIF NANSHA");// 付款条约
		firstModel.setShipper(declareOrder.getShipper());// 境外发货人
		firstModel.setMark("N/M");// 唛头
		firstModel.setInvoiceNo(declareOrder.getInvoiceNo());// 发票号
		firstModel.setCreateDate(TimeUtils.getNow());

		String originName = StringUtils.join(originSet, ";") ;
		firstModel.setCountryOrigin(originName);

		if(StringUtils.isBlank(firstModel.getCountry())){
			firstModel.setCountry(originName);
		}

		firstCustomsInfoDao.save(firstModel);

		FirstCustomsInfoDTO dto = new FirstCustomsInfoDTO() ;
		BeanUtils.copyProperties(firstModel, dto);

		dto.setItemList(itemDtoList);

		return dto;
	}

	@Override
	public DeclareOrderDTO searchDTODetail(Long id) throws Exception {

		DeclareOrderDTO dto = declareOrderDao.searchDTOById(id);


		DeclarePurchaseRelModel queryRel = new DeclarePurchaseRelModel() ;
		queryRel.setDeclareOrderId(id);

		List<DeclarePurchaseRelModel> list = declarePurchaseRelDao.list(queryRel);
		Set<String> idsList = new HashSet<String>() ;

		String purchaseCodes = "" ;
		Set<String> poList=new HashSet<>();


		for (DeclarePurchaseRelModel relModel : list) {

			Long purchaseOrderId = relModel.getPurchaseOrderId();

			PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(purchaseOrderId);

			if(StringUtils.isNotBlank(purchaseCodes)) {
				purchaseCodes += "," ;
			}

			purchaseCodes += purchaseOrder.getCode() ;
			poList.add(purchaseOrder.getPoNo());

			PurchaseOrderItemModel queryItem = new PurchaseOrderItemModel() ;
			queryItem.setPurchaseOrderId(purchaseOrderId);
			List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(queryItem);

			for (PurchaseOrderItemModel item : itemList) {
				idsList.add(String.valueOf(item.getGoodsId())) ;
			}

		}

		String needsId = StringUtils.join(idsList, ",");

		dto.setNeedsId(needsId);
		dto.setPurchaseCode(purchaseCodes);
		dto.setPoNo(StringUtils.join(poList,","));

		/**物流委托书*/
		LogisticsAttorneyModel queryModel = new LogisticsAttorneyModel() ;
		queryModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_1);
		queryModel.setOrderId(id);

		queryModel = logisticsAttorneyDao.searchByModel(queryModel) ;

		if(queryModel != null) {

			dto.setShipperTempId(queryModel.getShipperTempId());
			dto.setShipperTempName(queryModel.getShipperTempName());
			dto.setShipperTempDetails(queryModel.getShipperText());
			dto.setCarrierInformationTempDetails(queryModel.getCarrierInformationText());
			dto.setCarrierInformationTempId(queryModel.getCarrierInformationTempId());
			dto.setCarrierInformationTempName(queryModel.getCarrierInformationTempName());
			dto.setConsigneeTempDetails(queryModel.getConsigneeText());
			dto.setConsigneeTempId(queryModel.getConsigneeTempId());
			dto.setConsigneeTempName(queryModel.getConsigneeTempName());
			dto.setDockingTempDetails(queryModel.getDockingText());
			dto.setDockingTempId(queryModel.getDockingTempId());
			dto.setDockingTempName(queryModel.getDockingTempName());
			dto.setNotifierTempDetails(queryModel.getNotifierText());
			dto.setNotifierTempId(queryModel.getNotifierTempId());
			dto.setNotifierTempDetails(queryModel.getNotifierText());

		}

		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("depotId", dto.getDepotId()) ;
		queryMap.put("merchantId", dto.getMerchantId()) ;

		DepotMerchantRelMongo depotMechant = depotMerchantRelMongoDao.findOne(queryMap);

		dto.setDepotIsInOutInstruction(depotMechant.getIsInOutInstruction());

		return dto;
	}

	@Override
	public DeclareOrderDTO declareOrderByCopyId(Long id) throws Exception {

		DeclareOrderDTO copyDTO = declareOrderDao.searchDTOById(id);

		// 客户编码
		copyDTO.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGOD));// 预申报单号
		copyDTO.setStatus(DERP_ORDER.DECLAREORDER_STATUS_001);
		copyDTO.setState(DERP_ORDER.DECLAREORDER_STATE_0);//状态
		copyDTO.setId(null);
		//“物流信息”（不带出发票号、报关合同号、提单号）
		copyDTO.setInvoiceNo(null);
		copyDTO.setContractNo(null);
		copyDTO.setLadingBill(null);
		copyDTO.setLogisticsCommissionDate(null);
		copyDTO.setConfirmBookingDate(null);
		copyDTO.setEstimatedDeliveryDate(null);
		copyDTO.setShipDate(null);
		copyDTO.setCustomsSubmitDate(null);
		copyDTO.setCustomsConfirmDate(null);
		copyDTO.setPushWarehouseDate(null);
		copyDTO.setArriveDate(null);
		copyDTO.setArriveSeaDate(null);
		copyDTO.setArriveStartDate(null);
		copyDTO.setConfirmDeclarationDate(null);
		copyDTO.setLandCommissionDate(null);
		copyDTO.setConfirmDeclarationDate(null);
		copyDTO.setConfirmCatDate(null);
		copyDTO.setConfirmDepotDate(null);
		copyDTO.setShelfDate(null);


		DeclarePurchaseRelModel queryRel = new DeclarePurchaseRelModel() ;
		queryRel.setDeclareOrderId(id);

		List<DeclarePurchaseRelModel> list = declarePurchaseRelDao.list(queryRel);
		Set<String> idsList = new HashSet<String>() ;

		String purchaseCodes = "" ;
		Set<String> poList=new HashSet<>();


		for (DeclarePurchaseRelModel relModel : list) {

			Long purchaseOrderId = relModel.getPurchaseOrderId();

			PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(purchaseOrderId);

			if(StringUtils.isNotBlank(purchaseCodes)) {
				purchaseCodes += "," ;
			}

			purchaseCodes += purchaseOrder.getCode() ;
			poList.add(purchaseOrder.getPoNo());

			PurchaseOrderItemModel queryItem = new PurchaseOrderItemModel() ;
			queryItem.setPurchaseOrderId(purchaseOrderId);
			List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(queryItem);

			for (PurchaseOrderItemModel item : itemList) {
				idsList.add(String.valueOf(item.getGoodsId())) ;
			}

		}

		String needsId = StringUtils.join(idsList, ",");

		copyDTO.setNeedsId(needsId);
		copyDTO.setPurchaseCode(purchaseCodes);
		copyDTO.setPoNo(StringUtils.join(poList,","));

		/**物流委托书*/
		LogisticsAttorneyModel queryModel = new LogisticsAttorneyModel() ;
		queryModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_1);
		queryModel.setOrderId(id);

		queryModel = logisticsAttorneyDao.searchByModel(queryModel) ;

		if(queryModel != null) {
			copyDTO.setShipperTempId(queryModel.getShipperTempId());
			copyDTO.setShipperTempName(queryModel.getShipperTempName());
			copyDTO.setShipperTempDetails(queryModel.getShipperText());
			copyDTO.setCarrierInformationTempDetails(queryModel.getCarrierInformationText());
			copyDTO.setCarrierInformationTempId(queryModel.getCarrierInformationTempId());
			copyDTO.setCarrierInformationTempName(queryModel.getCarrierInformationTempName());
			copyDTO.setConsigneeTempDetails(queryModel.getConsigneeText());
			copyDTO.setConsigneeTempId(queryModel.getConsigneeTempId());
			copyDTO.setConsigneeTempName(queryModel.getConsigneeTempName());
			copyDTO.setDockingTempDetails(queryModel.getDockingText());
			copyDTO.setDockingTempId(queryModel.getDockingTempId());
			copyDTO.setDockingTempName(queryModel.getDockingTempName());
			copyDTO.setNotifierTempDetails(queryModel.getNotifierText());
			copyDTO.setNotifierTempId(queryModel.getNotifierTempId());
			copyDTO.setNotifierTempDetails(queryModel.getNotifierText());
		}

		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("depotId", copyDTO.getDepotId()) ;
		queryMap.put("merchantId", copyDTO.getMerchantId()) ;

		DepotMerchantRelMongo depotMechant = depotMerchantRelMongoDao.findOne(queryMap);

		copyDTO.setDepotIsInOutInstruction(depotMechant.getIsInOutInstruction());
		return copyDTO;
	}

	@Override
	public List<Map<String, Object>> getGoodsList(String itemList) throws Exception {

		ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		if(StringUtils.isBlank(itemList)) {
			return mapList;
		}

		List<DeclareOrderItemModel> goodsList = JSONArray.parseArray(itemList, DeclareOrderItemModel.class);

		for (DeclareOrderItemModel declareOrderItemModel : goodsList) {
			// 根据商品货号获取商品id
			Map<String, Object> merchandiseInfoParams = new HashMap<String, Object>();

			merchandiseInfoParams.put("merchandiseId", declareOrderItemModel.getGoodsId());
			merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

			MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(merchandiseInfoParams);

			declareOrderItemModel.setGoodsNo(goods.getGoodsNo());

			com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject)com.alibaba.fastjson.JSONObject.toJSON(declareOrderItemModel);
			json.put("barcode", goods.getBarcode()) ;

			Map<String, Object> map = (Map<String, Object>) json ;

			mapList.add(map) ;
		}

		return mapList;
	}

	@Override
	public Map<String, Object> importGoods(List<Map<String, String>> data, String orderId,String type,User user) throws SQLException {

		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int pass = 0;
		int failure = 0;

		DeclareOrderModel declareOrder =null;
		List<String> checkPurchaseCode = new ArrayList<>();
		//type区分预申报单是否已存在，如果type=1则说明预申报单已存在，type=2则说明从采购单生成预申报单
		if("1".equals(type)) {
			declareOrder = declareOrderDao.searchById(Long.valueOf(orderId));

			if (declareOrder == null) {
				throw new DerpException("订单不存在");
			}

		}else{
			for(String id :orderId.split(",")){
				PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(Long.valueOf(id));
				if(purchaseOrderModel==null){
					throw new DerpException("关联的采购订单不存在");
				}
				checkPurchaseCode.add(purchaseOrderModel.getCode());
			}

		}

		List<DeclareOrderItemModel> goodsList = new ArrayList<DeclareOrderItemModel>() ;

		Map<String , DeclareOrderItemModel> goodsMap = new HashMap<String ,DeclareOrderItemModel>() ;
		List<String> checkRepeatGoodList = new ArrayList<String>();
		int seq = 1;
		for (int i = 1; i <= data.size(); i++) {
			Map<String, String> map = data.get(i - 1);
			String purchaseOrderCode = map.get("采购订单号");
			if(checkIsNullOrNot(i, purchaseOrderCode, "采购订单号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			purchaseOrderCode = purchaseOrderCode.trim();

			PurchaseOrderModel queryPurchaseOrder = new PurchaseOrderModel();
			queryPurchaseOrder.setCode(purchaseOrderCode);
			queryPurchaseOrder = purchaseOrderDao.searchByModel(queryPurchaseOrder);
			if(queryPurchaseOrder==null){
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 1);
				message.setMessage("导入的采购订单不存在");
				resultList.add(message);
			}
			if(checkPurchaseCode.size() > 0 && !checkPurchaseCode.contains(purchaseOrderCode)){
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 1);
				message.setMessage("导入的采购订单与关联的采购订单不一致");
				resultList.add(message);
			}
			Long depotId = queryPurchaseOrder.getDepotId();

			String poNo = map.get("PO号");
			if(checkIsNullOrNot(i, poNo, "po号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			poNo = poNo.trim();

			String goodsNo = map.get("商品货号");
			if(checkIsNullOrNot(i, goodsNo, "商品货号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			goodsNo = goodsNo.trim() ;

			String num = map.get("申报数量");
			if (checkIsNullOrNot(i, num, "申报数量不能为空", resultList)) {
				failure += 1;
				continue;
			}else if (!StringUtils.isNumeric(num)) {
				setErrorMsg(i, "申报数量不为数值", resultList);
				failure += 1;
				continue;
			}
			num = num.trim() ;

			String purchasePrice = map.get("采购单价");
			if (checkIsNullOrNot(i, purchasePrice, "采购单价不能为空", resultList)) {
				failure += 1;
				continue;
			}
			purchasePrice = purchasePrice.trim() ;

			if (!isDoubleNumber(purchasePrice)) {
				setErrorMsg(i, "采购单价不为数值", resultList);
				failure += 1;
				continue;
			}

			String price = map.get("申报单价");
			if (checkIsNullOrNot(i, price, "申报单价不能为空", resultList)) {
				failure += 1;
				continue;
			}
			price = price.trim() ;

			if (!isDoubleNumber(price)) {
				setErrorMsg(i, "申报单价不为数值", resultList);
				failure += 1;
				continue;
			}

			String amount = map.get("采购总金额");
			if (checkIsNullOrNot(i, amount, "采购总金额不能为空", resultList)) {
				failure += 1;
				continue;
			}
			amount = amount.trim() ;

			if (!isDoubleNumber(amount)) {
				setErrorMsg(i, "采购总金额不为数值", resultList);
				failure += 1;
				continue;
			}

			int indexOf = amount.indexOf(".");
			// 如果是小数
			if (indexOf != -1) {
				int count = amount.length() - 1 - indexOf;
				if (count > 2) {
					setErrorMsg(i, "商品货号：" + goodsNo + "，采购总金额小数点后只能为两位数", resultList);
					failure += 1;
					continue;
				}
			}
			String repeatGoodskey = purchaseOrderCode+"_"+goodsNo+"_"+price;
			if(checkRepeatGoodList.contains(repeatGoodskey)){
				setErrorMsg(i, "采购订单："+purchaseOrderCode+"相同商品货号：" + goodsNo + "，不允许存在两个单价", resultList);
				failure += 1;
				continue;
			}else{
				checkRepeatGoodList.add(repeatGoodskey);
			}

			String brandName = map.get("品牌类型");
			if (checkIsNullOrNot(i, brandName, "品牌类型不能为空", resultList)) {
				failure += 1;
				continue;
			}

			String netWeightSum = map.get("净重（KG）");
			if (checkIsNullOrNot(i, netWeightSum, "净重（KG）不能为空", resultList)) {
				failure += 1;
				continue;
			}
			netWeightSum = netWeightSum.trim() ;

			if (!isDoubleNumber(netWeightSum)) {
				setErrorMsg(i, "净重（KG）不为数值", resultList);
				failure += 1;
				continue;
			}

			indexOf = netWeightSum.indexOf(".");
			// 如果是小数
			if (indexOf != -1) {
				int count = netWeightSum.length() - 1 - indexOf;
				if (count > 5) {
					setErrorMsg(i, "商品货号：" + goodsNo + "，净重（KG）小数点后只能为五位数", resultList);
					failure += 1;
					continue;
				}
			}

			String grossWeightSum = map.get("毛重（KG）");
			if (checkIsNullOrNot(i, grossWeightSum, "毛重（KG）不能为空", resultList)) {
				failure += 1;
				continue;
			}
			grossWeightSum = grossWeightSum.trim() ;

			if (!isDoubleNumber(grossWeightSum)) {
				setErrorMsg(i, "毛重（KG）不为数值", resultList);
				failure += 1;
				continue;
			}

			indexOf = grossWeightSum.indexOf(".");
			// 如果是小数
			if (indexOf != -1) {
				int count = grossWeightSum.length() - 1 - indexOf;
				if (count > 2) {
					setErrorMsg(i, "商品货号：" + goodsNo + "，毛重（KG）小数点后只能为两位数", resultList);
					failure += 1;
					continue;
				}
			}

			String palletNo = map.get("托盘号") ;

			String cartons = map.get("箱数") ;
			if (!StringUtils.isNumeric(num)) {
				setErrorMsg(i, "箱数不为数字类型", resultList);
				failure += 1;
				continue;
			}

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", depotId);
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params);

			Map<String, Object> depotMerchantRelMap = new HashMap<String, Object>() ;
			depotMerchantRelMap.put("merchantId", user.getMerchantId()) ;
			depotMerchantRelMap.put("depotId", depotId) ;
			DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantRelMap);

			if(depotMerchantRel == null) {
				setErrorMsg(i, "商家仓库关联不存在", resultList);
				failure += 1;
				continue;
			}

//			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRel.getProductRestriction())) {
//				merchandiseInfoParams.put("isRecord", DERP_SYS.MERCHANDISEINFO_ISRECORD_1);//是否备案 1-是
//			}
//
//			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRel.getProductRestriction())) {
//				merchandiseInfoParams.put("outDepotFlag", DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
//			}

			// 根据商品货号获取商品id
			Map<String, Object> merchandiseInfoParams = new HashMap<String, Object>();
			merchandiseInfoParams.put("goodsNo", goodsNo);
			merchandiseInfoParams.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
			List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseInfoParams, depotId);

			if (merchandiseList == null || merchandiseList.size() < 1) {
				setErrorMsg(i, "商品货号："+goodsNo+" 为关联仓库", resultList);
				failure += 1;
				continue;
			}
			MerchandiseInfoMogo merchandiseInfo = merchandiseList.get(0);

			boolean exsitflag = false;

			/*
			* type区分预申报单是否已存在
			* 如果type=1则说明预申报单已存在，type=2则说明从采购单生成预申报单
			* */
			PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel();
			queryItemModel.setPurchaseOrderId(queryPurchaseOrder.getId());
			queryItemModel.setGoodsId(merchandiseInfo.getMerchandiseId());
			queryItemModel.setPrice(new BigDecimal(purchasePrice));
			queryItemModel = purchaseOrderItemDao.searchByModel(queryItemModel);

			if (queryItemModel != null) {
				exsitflag = true;
			}
//			if("1".equals(type)) {
//				/**判断货号是否存在于采购订单**/
//				DeclarePurchaseRelModel queryRel = new DeclarePurchaseRelModel();
//				queryRel.setDeclareOrderId(declareOrder.getId());
//
//				List<DeclarePurchaseRelModel> list = declarePurchaseRelDao.list(queryRel);
//
//				for (DeclarePurchaseRelModel relModel : list) {
//
//					queryItemModel = new PurchaseOrderItemModel();
//					//采购商品货号+单价 唯一维度
//					queryItemModel.setPurchaseOrderId(relModel.getPurchaseOrderId());
//					queryItemModel.setGoodsId(merchandiseInfo.getMerchandiseId());
//					queryItemModel.setPrice(new BigDecimal(purchasePrice));
//
//					queryItemModel = purchaseOrderItemDao.searchByModel(queryItemModel);
//
//					if (queryItemModel != null) {
//						exsitflag = true;
//					}
//
//				}
//			}else{
//
//				queryItemModel = new PurchaseOrderItemModel();
//
//				queryItemModel.setPurchaseOrderId(queryPurchaseOrder.getId());
//				queryItemModel.setGoodsId(merchandiseInfo.getMerchandiseId());
//				queryItemModel.setPrice(new BigDecimal(purchasePrice));
//				queryItemModel = purchaseOrderItemDao.searchByModel(queryItemModel);
//
//				if (queryItemModel != null) {
//					exsitflag = true;
//				}
//			}


			if(!exsitflag) {
				setErrorMsg(i, "商品货号：" + goodsNo + "不存在于对应采购订单", resultList);
				failure += 1;
				continue;
			}

			String key = queryPurchaseOrder.getId()+"_"+goodsNo+"_"+purchasePrice;
			DeclareOrderItemModel itemModel = goodsMap.get(key);
			if(itemModel == null){
				itemModel = new DeclareOrderItemModel() ;
				itemModel.setAmount(new BigDecimal(amount));
				itemModel.setBarcode(merchandiseInfo.getBarcode());
				if("1".equals(type)) {
					itemModel.setDeclareOrderId(Long.valueOf(orderId));
				}
				itemModel.setNum(Integer.valueOf(num));
				itemModel.setBrandName(brandName);
				itemModel.setGoodsId(merchandiseInfo.getMerchandiseId());
				itemModel.setGoodsCode(merchandiseInfo.getGoodsCode());
				itemModel.setGoodsNo(goodsNo);
				itemModel.setGoodsName(merchandiseInfo.getName());
				itemModel.setGrossWeightSum(Double.valueOf(grossWeightSum));
				itemModel.setNetWeightSum(Double.valueOf(netWeightSum));
				itemModel.setSeq(seq);
				itemModel.setPurchasePrice(new BigDecimal(purchasePrice));
				itemModel.setPrice(new BigDecimal(price));
				itemModel.setGrossWeight(merchandiseInfo.getGrossWeight());
				itemModel.setNetWeight(merchandiseInfo.getNetWeight());
				itemModel.setConstituentRatio(merchandiseInfo.getComponent());
				itemModel.setPurchase(queryPurchaseOrder.getCode());
				itemModel.setPurchaseId(queryPurchaseOrder.getId());
				itemModel.setPurchaseItemId(queryItemModel.getId());
				itemModel.setPoNo(queryPurchaseOrder.getPoNo());

				if(StringUtils.isNotBlank(palletNo)) {
					itemModel.setPalletNo(palletNo);
				}

				if(StringUtils.isNotBlank(cartons)) {
					itemModel.setCartons(Integer.valueOf(cartons));
				}
				seq++;

			}else{
				Integer totalNum = itemModel.getNum() + Integer.valueOf(num);
				Double totalNetWeightSum = new BigDecimal(itemModel.getNetWeightSum()).add(new BigDecimal(netWeightSum)).doubleValue();
				Double totalGrossWeightSum = new BigDecimal(itemModel.getGrossWeightSum()).add(new BigDecimal(grossWeightSum)).doubleValue();
				BigDecimal totalAmount = itemModel.getAmount().add(new BigDecimal(amount));

				itemModel.setNum(totalNum);
				itemModel.setNetWeightSum(totalNetWeightSum);
				itemModel.setGrossWeightSum(totalGrossWeightSum);
				itemModel.setAmount(totalAmount);

			}
//			goodsList.add(itemModel) ;
			goodsMap.put(key,itemModel);
		}

		Map<String, Object> map = new HashMap<String, Object>();

		if(failure == 0) {
			goodsList = new ArrayList<>(goodsMap.values());
			goodsList = goodsList.stream().sorted(Comparator.comparing(DeclareOrderItemModel::getSeq)).collect(Collectors.toList()) ;

			map.put("itemList", goodsList) ;
		}

		map.put("success", goodsList.size());
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);

		return map;
	}

	@Override
	public Map<String, Object> importPackingDetails(List<Map<String, String>> data, User user, List<DeclareOrderItemDTO> itemList) throws SQLException {

		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		Map<String, Object> returnMap = new HashMap<String, Object>() ;
		List<PackingDetailsModel> packingList = new ArrayList<PackingDetailsModel>() ;
		if(itemList == null || itemList.size() <1){
			setErrorMsg(1, "商品信息为空，无法导入装箱明细", resultList);

			returnMap.put("success", success);
			returnMap.put("pass", pass);
			returnMap.put("failure", failure);
			returnMap.put("message", resultList);
			returnMap.put("packingList", packingList);
			return returnMap ;
		}
		 Map<String, List<DeclareOrderItemDTO>> itemModelMap = itemList.stream().collect(Collectors.groupingBy(DeclareOrderItemDTO::getBarcode));
		if(failure == 0){
			for (int i = 1 ; i  <= data.size(); i++) {

				Map<String, String> row = data.get(i - 1);

				String barcode = row.get("条形码");

				if(checkIsNullOrNot(i, barcode, "条形码不能为空", resultList)){
					failure += 1;
					continue;
				}

				List<DeclareOrderItemDTO> declareItemList = itemModelMap.get(barcode);
				if(declareItemList == null || declareItemList.size() < 1){
					setErrorMsg(i, "找不到对应的商品条码", resultList);
					failure += 1;
					continue;
				}

				String boxNum = row.get("箱数");
				if(checkIsNullOrNot(i, boxNum, "箱数不能为空", resultList)){
					failure += 1;
					continue;
				}

				if(!isNumber(boxNum)){
					setErrorMsg(i, "箱数非数值类型", resultList);
					failure += 1;
					continue;
				}

				String piecesNum = row.get("件数");

				if(checkIsNullOrNot(i, piecesNum, "件数不能为空", resultList)){
					failure += 1;
					continue;
				}

				if(!isNumber(piecesNum)){
					setErrorMsg(i, "箱数非数值类型", resultList);
					failure += 1;
					continue;
				}

				String cabinetNo = row.get("柜号");
				String torrNo = row.get("托盘号");

				DeclareOrderItemDTO tempItem = declareItemList.get(0);
				PackingDetailsModel saveModel = new PackingDetailsModel();
				saveModel.setBarcode(barcode);
				saveModel.setBoxNum(Integer.valueOf(boxNum));
				saveModel.setPiecesNum(Integer.valueOf(piecesNum));
				saveModel.setTorrNo(torrNo);
				saveModel.setCabinetNo(cabinetNo);
				saveModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_1);
				saveModel.setGoodsNo(tempItem.getGoodsNo());

				packingList.add(saveModel) ;

				success ++ ;
			}
		}

		returnMap.put("success", success);
		returnMap.put("pass", pass);
		returnMap.put("failure", failure);
		returnMap.put("message", resultList);
		returnMap.put("packingList", packingList);

		return returnMap ;
	}

	/**
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index , String content ,
			String msg ,List<ImportErrorMessage> resultList ) {

		if(StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true ;

		}else {
			return false ;
		}

	}

	/**
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}

	/**
	 * 判断是否数字
	 * @param str
	 * @return
	 */
	private boolean isNumber(String str){

		if(StringUtils.isBlank(str)) {
			return false ;
		}

        String reg = "\\d+";
        return str.matches(reg);
    }

	/**
	 * 判断是否小数
	 * @param str
	 * @return
	 */
	private boolean isDoubleNumber(String str){

		if(StringUtils.isBlank(str)) {
			return false ;
		}

        String reg = "\\d+(\\.\\d+)?";
        return str.matches(reg);
    }

	@Override
	public void updateTrajectory(DeclareDetailsAddForm form, User user) throws SQLException {

		DeclareOrderModel declare = declareOrderDao.searchById(form.getId());

		if(declare == null) {
			throw new DerpException("预申报单不存在") ;
		}

		List<String> dateList = new ArrayList<String>() ;

		DeclareOrderModel saveModel = new DeclareOrderModel() ;
		saveModel.setId(form.getId());

		//更新类型 2-导出物流委托 3-确认订舱信息 4-工厂提货（装船）5- 提交一线清关资料 6-审核一线清关资料 8-申报完成 10-订车信息确认 11-提货入仓 12-确认到港

		switch (form.getType()) {
		case "2":

			if(StringUtils.isBlank(form.getLogisticsCommissionDate())) {
				throw new DerpException("物流委托时间不能为空") ;
			}

			dateList.add(form.getLogisticsCommissionDate()) ;

			saveModel.setLogisticsCommissionDate(TimeUtils.parse(form.getLogisticsCommissionDate(), "yyyy-MM-dd HH:mm:ss"));

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declare.getCode(), "导出物流委托", null, null);

			break;

		case "3":

			if(StringUtils.isBlank(form.getEstimatedDeliveryDate())) {
				throw new DerpException("预计离港时间不能为空") ;
			}

			if(StringUtils.isBlank(form.getArriveDate())) {
				throw new DerpException("预计到港时间不能为空") ;
			}

			if(StringUtils.isBlank(form.getConfirmBookingDate())) {
				throw new DerpException("确认订舱/车时间不能为空") ;
			}

			//预计离港时间、预计到港时间，需大于或等于确认订舱时间
			if(TimeUtils.daysBetween(TimeUtils.parse(form.getEstimatedDeliveryDate(), "yyyy-MM-dd"), TimeUtils.parse(form.getConfirmBookingDate(), "yyyy-MM-dd")) >0) {
				throw new DerpException("预计离港时间必须大于或等于确认订舱时间") ;
			}
			if(TimeUtils.daysBetween(TimeUtils.parse(form.getArriveDate(), "yyyy-MM-dd"), TimeUtils.parse(form.getConfirmBookingDate(), "yyyy-MM-dd")) >0) {
				throw new DerpException("预计到港时间必须大于或等于确认订舱时间") ;
			}
			//dateList.add(form.getEstimatedDeliveryDate()) ;
			//dateList.add(form.getArriveDate()) ;
			dateList.add(form.getConfirmBookingDate()) ;

			saveModel.setEstimatedDeliveryDate(TimeUtils.parse(form.getEstimatedDeliveryDate(), "yyyy-MM-dd HH:mm:ss"));
			saveModel.setArriveDate(TimeUtils.parse(form.getArriveDate(), "yyyy-MM-dd HH:mm:ss"));
			saveModel.setConfirmBookingDate(TimeUtils.parse(form.getConfirmBookingDate(), "yyyy-MM-dd HH:mm:ss"));

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declare.getCode(), "订舱信息确认", null, null);

			break;

		case "4":

			if(StringUtils.isBlank(form.getShipDate())) {
				throw new DerpException("提货确认时间不能为空") ;
			}

			dateList.add(form.getShipDate()) ;

			saveModel.setShipDate(TimeUtils.parse(form.getShipDate(), "yyyy-MM-dd HH:mm:ss"));

			//如果预申报单当前状态是待物流委托；更新状态为待清关
			if(DERP_ORDER.DECLAREORDER_STATUS_001.equals(declare.getStatus())) {
				saveModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_002);
			}

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declare.getCode(), "提货确认", null, null);

			break;

		case "5":

			if(StringUtils.isBlank(form.getCustomsSubmitDate())) {
				throw new DerpException("提交一线清关资料时间不能为空") ;
			}

			dateList.add(form.getCustomsSubmitDate()) ;

			saveModel.setCustomsSubmitDate(TimeUtils.parse(form.getCustomsSubmitDate(), "yyyy-MM-dd HH:mm:ss"));
			//如果预申报单当前状态是待物流委托；更新状态为待清关
			if(DERP_ORDER.DECLAREORDER_STATUS_001.equals(declare.getStatus())) {
				saveModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_002);
			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declare.getCode(), "提交一线清关资料", null, null);

			break;

		case "6":

			if(StringUtils.isBlank(form.getCustomsConfirmDate())) {
				throw new DerpException("清关确认时间不能为空") ;
			}

			dateList.add(form.getCustomsConfirmDate()) ;

			saveModel.setCustomsConfirmDate(TimeUtils.parse(form.getCustomsConfirmDate(), "yyyy-MM-dd HH:mm:ss"));
			//如果预申报单当前状态是待物流委托；更新状态为待清关
			if(DERP_ORDER.DECLAREORDER_STATUS_001.equals(declare.getStatus())) {
				saveModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_002);
			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declare.getCode(), "确认清关资料", null, null);

			break;

		case "8":

			if(StringUtils.isBlank(form.getConfirmDeclarationDate())) {
				throw new DerpException("确认申报时间不能为空") ;
			}

			dateList.add(form.getConfirmDeclarationDate()) ;

			saveModel.setConfirmDeclarationDate(TimeUtils.parse(form.getConfirmDeclarationDate(), "yyyy-MM-dd HH:mm:ss"));

			//如果预申报单当前状态是待物流委托或待清关状态；更新状态为带入仓
			if(DERP_ORDER.DECLAREORDER_STATUS_001.equals(declare.getStatus())||DERP_ORDER.DECLAREORDER_STATUS_002.equals(declare.getStatus())) {
				saveModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_004);
			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declare.getCode(), "确认申报时间", null, null);

			break;
		case "10":

			if(StringUtils.isBlank(form.getConfirmCatDate())) {
				throw new DerpException("确认订车时间不能为空") ;
			}

			dateList.add(form.getConfirmCatDate()) ;

			saveModel.setConfirmCatDate(TimeUtils.parse(form.getConfirmCatDate(), "yyyy-MM-dd HH:mm:ss"));

			//如果预申报单当前状态是待物流委托或待清关状态；更新状态为带入仓
			if(DERP_ORDER.DECLAREORDER_STATUS_001.equals(declare.getStatus())||DERP_ORDER.DECLAREORDER_STATUS_002.equals(declare.getStatus())) {
				saveModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_004);
			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declare.getCode(), "确认订车时间", null, null);

			break;
		case "11":

			if(StringUtils.isBlank(form.getConfirmDepotDate())) {
				throw new DerpException("入仓时间不能为空") ;
			}
			if(StringUtils.isBlank(form.getPickUpDate())) {
				throw new DerpException("提货时间不能为空") ;
			}

			dateList.add(form.getConfirmDepotDate()) ;
			dateList.add(form.getPickUpDate());

			saveModel.setConfirmDepotDate(TimeUtils.parse(form.getConfirmDepotDate(), "yyyy-MM-dd HH:mm:ss"));
			saveModel.setPickUpDate(TimeUtils.parse(form.getPickUpDate(), "yyyy-MM-dd HH:mm:ss"));

			if(declare.getConfirmDeclarationDate() == null) {
				saveModel.setConfirmDeclarationDate(TimeUtils.parse(form.getConfirmDepotDate(), "yyyy-MM-dd HH:mm:ss"));
			}

			//如果预申报单当前状态是待清关或带物流委托状态；更新状态为带入仓
			if(DERP_ORDER.DECLAREORDER_STATUS_001.equals(declare.getStatus())||DERP_ORDER.DECLAREORDER_STATUS_002.equals(declare.getStatus())) {
				saveModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_004);
			}

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declare.getCode(), "确认入仓时间", null, null);

			break;
		case "12":

			if(StringUtils.isBlank(form.getArriveSeaDate())) {
				throw new DerpException("到港时间不能为空") ;
			}
			dateList.add(form.getArriveSeaDate()) ;

			saveModel.setArriveSeaDate(TimeUtils.parse(form.getArriveSeaDate(), "yyyy-MM-dd HH:mm:ss"));

			//如果预申报单当前状态是待清关或带物流委托状态；更新状态为带入仓
			if(DERP_ORDER.DECLAREORDER_STATUS_001.equals(declare.getStatus())||DERP_ORDER.DECLAREORDER_STATUS_002.equals(declare.getStatus())) {
				saveModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_004);
			}

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declare.getCode(), "到港时间", null, null);

			break;
		default:

			throw new DerpException("非规定更新时间类型") ;
		}

		for (String dateStr : dateList) {

			if(TimeUtils.daysBetween(TimeUtils.parse(dateStr, "yyyy-MM-dd"), new Date()) < 0) {
				throw new DerpException("选择的日期必须小于或等于当前日期") ;
			}

			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(declare.getDepotId());
			closeAccountsMongo.setBuId(declare.getBuId());
			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (Timestamp.valueOf(dateStr).getTime() < Timestamp
						.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new DerpException("选择的日期不能选择财务报表已关账的月份") ;
				}
			}

		}

		declareOrderDao.modify(saveModel) ;

	}

	@Override
	public void saveDeclareDelivery(DeclareOrderDeliveryForm form, User user) throws Exception {

		DeclareOrderModel declareOrderModel = declareOrderDao.searchById(form.getDeclareOrderId());

		if(declareOrderModel == null) {
			throw new DerpException("预申报订单不存在") ;
		}

		Timestamp inboundDate = TimeUtils.parse(form.getInboundDate(), "yyyy-MM-dd");

		Integer days = TimeUtils.daysBetween(new Date(), inboundDate) ;

		if(days > 0) {
			throw new DerpException("入库时间不能晚于当前时间") ;
		}

		// 判断归属日期是否小于 关账日期/月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(declareOrderModel.getMerchantId());
		closeAccountsMongo.setDepotId(declareOrderModel.getDepotId());
		closeAccountsMongo.setBuId(declareOrderModel.getBuId());

		String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 关账下个月日期大于 入库日期
			if (inboundDate.getTime() < Timestamp
					.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new DerpException("入库时间必须大于关账日期/月结日期");
			}
		}

		// 获取仓库信息
		Map<String, Object> depotMap = new HashMap<String, Object>() ;
		depotMap.put("depotId", declareOrderModel.getDepotId()) ;

		DepotInfoMongo depot = depotInfoMongoDao.findOne(depotMap);

		Integer zeroNum = 0;
		Map<Long,Long> itemIdMap = new HashMap<>();
		List<DeclareOrderDeliveryItemForm> itemList = form.getItemList();
		for(DeclareOrderDeliveryItemForm item : itemList){
			if(item.getPurchaseItemId() == null){
				throw new DerpException("预申报单商品关联的采购商品明细id为空，请及时维护");
			}
			Integer tallyNum = item.getNormalNum() + item.getExpireNum() + item.getWornNum();
			if(tallyNum.intValue() == 0){
				zeroNum++;
			}
		}
		if(zeroNum == itemList.size()){
			throw new DerpException("入库失败，至少一个货号入库数量大于0") ;
		}
		PurchaseWarehouseModel tempWarehouseModel = new PurchaseWarehouseModel();
		tempWarehouseModel.setDeclareOrderId(declareOrderModel.getId());
		List<PurchaseWarehouseModel> tempWarehouseList = purchaseWarehouseDao.list(tempWarehouseModel);
		if(tempWarehouseList != null && tempWarehouseList.size() > 0){
			throw new DerpException("预申报单已存在采购入库单，上架入库失败") ;
		}

		Map<Long, List<DeclareOrderDeliveryItemForm>> itemFormMap = itemList.stream().collect(Collectors.groupingBy(DeclareOrderDeliveryItemForm::getPurchaseId));
		DeclarePurchaseRelModel queryRelModel = new DeclarePurchaseRelModel();
		queryRelModel.setDeclareOrderId(form.getDeclareOrderId());
		List<DeclarePurchaseRelModel> relList = declarePurchaseRelDao.list(queryRelModel);
		for(DeclarePurchaseRelModel relModel : relList){
			PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(relModel.getPurchaseOrderId());

			PurchaseWarehouseModel purchaseWarehouse = new PurchaseWarehouseModel();
			purchaseWarehouse.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGRK));// 入库单号
			purchaseWarehouse.setDepotId(declareOrderModel.getDepotId());// 仓库iD
			purchaseWarehouse.setDepotName(declareOrderModel.getDepotName());// 仓库名称
			purchaseWarehouse.setCreater(user.getId());// 创建人
			purchaseWarehouse.setMerchantId(user.getMerchantId());// 商家id
			purchaseWarehouse.setMerchantName(user.getMerchantName());// 商家名称
			purchaseWarehouse.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);// 入库中
			purchaseWarehouse.setDeclareOrderCode(declareOrderModel.getCode());// 企业订单号
			purchaseWarehouse.setDeclareOrderId(declareOrderModel.getId());
			purchaseWarehouse.setContractNo(declareOrderModel.getContractNo());// 合同号
			purchaseWarehouse.setInboundDate(inboundDate);
			purchaseWarehouse.setBusinessModel(declareOrderModel.getBusinessModel()); // 设置业务主体
			purchaseWarehouse.setBuId(declareOrderModel.getBuId()); // 事业部
			purchaseWarehouse.setBuName(declareOrderModel.getBuName());
			purchaseWarehouse.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_1);
			purchaseWarehouse.setCurrency(purchaseOrderModel.getCurrency());
			if(depot!=null){
				if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())){
					purchaseWarehouse.setTallyingUnit(declareOrderModel.getTallyingUnit());;// 海外仓理货单位
				}
			}
			Long orderId = purchaseWarehouseDao.save(purchaseWarehouse);

			// 新增 采购入库推单关联采购订单表
			WarehouseOrderRelModel wRelModel = new WarehouseOrderRelModel();
			wRelModel.setWarehouseId(orderId);// 采购入库单id
			wRelModel.setPurchaseOrderId(relModel.getPurchaseOrderId());// 采购订单ID
			warehouseOrderRelDao.save(wRelModel);

			//修改预申报单的上架时间
			declareOrderModel.setShelfDate(inboundDate);
			declareOrderDao.modify(declareOrderModel);

			List<DeclareOrderDeliveryItemForm> tempList = itemFormMap.get(relModel.getPurchaseOrderId());
			Map<Long, List<DeclareOrderDeliveryItemForm>> itemMap = tempList.stream().collect(Collectors.groupingBy(DeclareOrderDeliveryItemForm::getPurchaseItemId));
			for (Long purchaseItemId : itemMap.keySet()) {
				List<DeclareOrderDeliveryItemForm> queryItemList = itemMap.get(purchaseItemId);
				Integer normalSum = queryItemList.stream().mapToInt(DeclareOrderDeliveryItemForm::getNormalNum).sum();
				Integer expireSum = queryItemList.stream().mapToInt(DeclareOrderDeliveryItemForm::getExpireNum).sum();
				Integer wornSum = queryItemList.stream().mapToInt(DeclareOrderDeliveryItemForm::getWornNum).sum();
				DeclareOrderDeliveryItemForm item = queryItemList.get(0);

				DeclareOrderItemModel queryItemModel = new DeclareOrderItemModel() ;
				queryItemModel.setDeclareOrderId(declareOrderModel.getId());
				queryItemModel.setPurchaseItemId(item.getPurchaseItemId());
				queryItemModel = declareOrderItemDao.searchByModel(queryItemModel) ;

				Integer tallyingNum = normalSum + expireSum + wornSum ;

				if(queryItemModel.getNum().intValue() != tallyingNum.intValue()) {
					throw new DerpException("商品货号：" + queryItemModel.getGoodsNo() + "入库数量不等于预申报单数量") ;
				}

				Integer lackNum = queryItemModel.getNum() - tallyingNum ;

				PurchaseWarehouseItemModel warehouseItem = new PurchaseWarehouseItemModel() ;
				warehouseItem.setWarehouseId(orderId);
				warehouseItem.setGoodsId(queryItemModel.getGoodsId());// 商品id
				warehouseItem.setGoodsNo(queryItemModel.getGoodsNo());// 商品货号
				warehouseItem.setGoodsName(queryItemModel.getGoodsName());// 商品名称
				warehouseItem.setBarcode(queryItemModel.getBarcode());
				warehouseItem.setTallyingNum(tallyingNum);
				warehouseItem.setMultiNum(0);
				warehouseItem.setLackNum(lackNum);
				warehouseItem.setNormalNum(normalSum);
				warehouseItem.setTallyingUnit(declareOrderModel.getTallyingUnit());
				warehouseItem.setPurchaseNum(queryItemModel.getNum());
				warehouseItem.setCreateDate(TimeUtils.getNow());
				warehouseItem.setPurchaseItemId(queryItemModel.getPurchaseItemId());

				Map<String, Object> queryGoodsMap = new HashMap<String, Object>() ;
				queryGoodsMap.put("merchandiseId", queryItemModel.getGoodsId()) ;
				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(queryGoodsMap);

				Double grossWeight = merchandise.getGrossWeight();// 毛重
				Double netWeight = merchandise.getNetWeight();// 净重

				if (grossWeight != null) {
					grossWeight = grossWeight * tallyingNum;
				}

				if (netWeight != null) {
					netWeight = netWeight * tallyingNum;
				}

				warehouseItem.setGrossWeight(grossWeight);// 毛重
				warehouseItem.setNetWeight(netWeight);// 净重

				warehouseItem.setLength(merchandise.getLength());// 长
				warehouseItem.setWidth(merchandise.getWidth());// 宽
				warehouseItem.setVolume(merchandise.getVolume());// 体积
				warehouseItem.setHeight(merchandise.getHeight());// 高
				warehouseItem.setBarcode(merchandise.getBarcode());

				Long itemId = purchaseWarehouseItemDao.save(warehouseItem);

				itemIdMap.put(purchaseItemId,itemId);
			}


	//		commonBusinessService.saveAutoPurchaseAnalysis(purchaseWarehouse.getCode());
			//批次信息
			Map<String, List<DeclareOrderDeliveryItemForm>> batchItemMap = tempList.stream().filter(i-> (i.getNormalNum() + i.getWornNum() + i.getExpireNum()) > 0)
					.collect(Collectors.groupingBy(i-> i.getPurchaseItemId()+"_"+(StringUtils.isBlank(i.getBatchNo())? "":i.getBatchNo() )
							+"_"+(StringUtils.isBlank(i.getProductionDate())? "":i.getProductionDate()) +"_"+(StringUtils.isBlank(i.getOverdueDate())? "":i.getOverdueDate())));

			for(String batchKey : batchItemMap.keySet()){
				Long purchaseItemId = Long.valueOf(batchKey.split("_")[0]);
				PurchaseOrderItemModel queryItemModel = purchaseOrderItemDao.searchById(purchaseItemId);
				List<DeclareOrderDeliveryItemForm> queryItemList = batchItemMap.get(batchKey);
				DeclareOrderDeliveryItemForm itemForm = queryItemList.get(0);
				Integer normalSum = queryItemList.stream().mapToInt(DeclareOrderDeliveryItemForm::getNormalNum).sum();
				Integer expireSum = queryItemList.stream().mapToInt(DeclareOrderDeliveryItemForm::getExpireNum).sum();
				Integer wornSum = queryItemList.stream().mapToInt(DeclareOrderDeliveryItemForm::getWornNum).sum();

				PurchaseWarehouseBatchModel batchModel = new PurchaseWarehouseBatchModel() ;
				batchModel.setGoodsId(queryItemModel.getGoodsId());
				batchModel.setItemId(itemIdMap.get(purchaseItemId));
				batchModel.setNormalNum(normalSum);
				batchModel.setExpireNum(expireSum);
				batchModel.setWornNum(wornSum);
				batchModel.setCreateDate(TimeUtils.getNow());

				if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation()) || DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation())) {
					if(StringUtils.isBlank(itemForm.getBatchNo()) ){
						throw new DerpException("入库仓库入库批次强校验，批次信息不能为空") ;
					}
					if(StringUtils.isBlank(itemForm.getProductionDate()) ){
						throw new DerpException("入库仓库入库批次强校验，生效日期不能为空") ;
					}
					if(StringUtils.isBlank(itemForm.getOverdueDate()) ){
						throw new DerpException("入库仓库入库批次强校验，失效日期不能为空") ;
					}

				}

				if(StringUtils.isNotBlank(itemForm.getProductionDate())) {
					batchModel.setProductionDate(TimeUtils.parse(itemForm.getProductionDate(), "yyyy-MM-dd"));
				}
				if(StringUtils.isNotBlank(itemForm.getOverdueDate())) {
					batchModel.setOverdueDate(TimeUtils.parse(itemForm.getOverdueDate(), "yyyy-MM-dd"));
				}
				batchModel.setBatchNo(itemForm.getBatchNo());
				purchaseWarehouseBatchDao.save(batchModel) ;
			}

			InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
			inventoryRoot.setBackTopic(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTopic());
			inventoryRoot.setBackTags(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTags());
			Map<String, Object> customParam = new HashMap<String, Object>();
			inventoryRoot.setCustomParam(customParam);
			// 增加库存

			inventoryRoot.setMerchantId(purchaseWarehouse.getMerchantId().toString());
			inventoryRoot.setMerchantName(purchaseWarehouse.getMerchantName());
			inventoryRoot.setBusinessNo(declareOrderModel.getCode());
			inventoryRoot.setTopidealCode(declareOrderModel.getTopidealCode());
			inventoryRoot.setDepotId(purchaseWarehouse.getDepotId().toString());
			inventoryRoot.setDepotName(purchaseWarehouse.getDepotName());
			inventoryRoot.setOrderNo(purchaseWarehouse.getCode());
			inventoryRoot.setSource(SourceStatusEnum.CGRK.getKey());
			inventoryRoot.setSourceType(InventoryStatusEnum.CGRK.getKey());
			inventoryRoot.setSourceDate(TimeUtils.formatFullTime());
			inventoryRoot.setBuId(purchaseWarehouse.getBuId().toString());
			inventoryRoot.setBuName(purchaseWarehouse.getBuName());

			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", purchaseWarehouse.getDepotId());// 根据仓库id
			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
			if (mongo != null) {
				inventoryRoot.setDepotCode(mongo.getCode());
				inventoryRoot.setDepotType(mongo.getType());
				inventoryRoot.setIsTopBooks(mongo.getIsTopBooks());
			}

			// 获取当前年月
			inventoryRoot.setOwnMonth(TimeUtils.format(purchaseWarehouse.getInboundDate(), "yyyy-MM"));
			inventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(purchaseWarehouse.getInboundDate()));

			int depot_flag = 0;
			if (mongo != null && mongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
				depot_flag = 1;
			}

			List<InvetAddOrSubtractGoodsListJson> inventoryItemList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch(orderId);
			for (PurchaseWarehouseBatchModel bModel : batchList) {
				// 坏货数量
				if (bModel.getWornNum() != null && bModel.getWornNum() > 0) {
					InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag, user);
					listJson.setNum(bModel.getWornNum());
					listJson.setType(DERP.ISDAMAGE_1);// 商品类型（0 好品 1坏品）

					if (bModel.getOverdueDate() != null) {
						int daysBetween = TimeUtils.daysBetween(bModel.getOverdueDate(), inboundDate);

						if (daysBetween > 0) {
							listJson.setIsExpire(DERP.ISEXPIRE_0);// 是否过期（0是 1否）
						} else {
							listJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
						}

					} else {
						listJson.setIsExpire(DERP.ISEXPIRE_1);
					}
					listJson.setStockLocationTypeId(purchaseOrderModel.getStockLocationTypeId() == null ? "":purchaseOrderModel.getStockLocationTypeId().toString());
					listJson.setStockLocationTypeName(purchaseOrderModel.getStockLocationTypeName());

					inventoryItemList.add(listJson);
				}

				// 过期数量
				if (bModel.getExpireNum() != null && bModel.getExpireNum() > 0) {
					InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag, user);
					listJson.setType(DERP.ISDAMAGE_0);// 商品类型（0 好品 1坏品 ）
					listJson.setIsExpire(DERP.ISEXPIRE_0);// 是否过期（0是 1否）
					listJson.setNum(bModel.getExpireNum());
					listJson.setStockLocationTypeId(purchaseOrderModel.getStockLocationTypeId() == null ? "":purchaseOrderModel.getStockLocationTypeId().toString());
					listJson.setStockLocationTypeName(purchaseOrderModel.getStockLocationTypeName());
					inventoryItemList.add(listJson);
				}
				// 正常数量
				else if(bModel.getNormalNum() != null && bModel.getNormalNum() > 0){
					InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag, user);
					listJson.setNum(bModel.getNormalNum());
					listJson.setType(DERP.ISDAMAGE_0);// 商品类型（0 好品 1坏品 ）
					listJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
					listJson.setStockLocationTypeId(purchaseOrderModel.getStockLocationTypeId() == null ? "":purchaseOrderModel.getStockLocationTypeId().toString());
					listJson.setStockLocationTypeName(purchaseOrderModel.getStockLocationTypeName());
					inventoryItemList.add(listJson);
				}
			}

			inventoryRoot.setGoodsList(inventoryItemList);

			rocketMQProducer.send(JSONObject.fromObject(inventoryRoot).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_1, purchaseOrderModel.getCode(), "预申报上架入库", null, null);
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_21, purchaseWarehouse.getCode(), "预申报上架入库", null, null);
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_4, declareOrderModel.getCode(), "上架入库", null, null);
	}

	/**
	 * 确认入库构建传输对象
	 * @throws Exception
	 */
	private InvetAddOrSubtractGoodsListJson generateTransportGoods(PurchaseWarehouseBatchModel bModel, int depot_flag, User user) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
		listJson.setGoodsId(bModel.getGoodsId().toString());
		listJson.setGoodsNo(bModel.getGoodsNo());
		listJson.setGoodsName(bModel.getGoodsName());
		listJson.setBatchNo(bModel.getBatchNo());
		listJson.setBarcode(bModel.getBarcode());
		listJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 字符串 0 调减 1调增

		if (bModel.getProductionDate() != null) {
			listJson.setProductionDate(sdf.format(bModel.getProductionDate()));
		}

		if (bModel.getOverdueDate() != null) {
			listJson.setOverdueDate(sdf.format(bModel.getOverdueDate()));
		}

		// 海外仓必填
		if (depot_flag == 1) {
			if (bModel.getTallyingUnit() != null) {
				// 托盘
				if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
					listJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
				} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
					listJson.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
				} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
					listJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
				}
			} else {
				listJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
			}
		}

		return listJson;
	}

	@Override
	public List<Map<String, Object>> getDeclareTypeNum(DeclareOrderDTO dto, User user) {

		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				return null;
			}

			dto.setBuIds(buIds);
		}

		List<Map<String, Object>> mapList = declareOrderDao.getDeclareTypeNum(dto) ;

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

	@Override
	public void modifyDeclareOrder(DeclareOrderModel declareOrderModel) throws SQLException {
		declareOrderDao.modify(declareOrderModel);
	}


	@Override
	public void modifyLogisticsAttorney(LogisticsAttorneyModel logisticsAttorneyModel, User user) throws SQLException {
		//物流委托书
		LogisticsAttorneyModel queryAttorneyModel = new LogisticsAttorneyModel() ;
		queryAttorneyModel.setOrderId(logisticsAttorneyModel.getOrderId());
		queryAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_1);// 1-采购预申报 2-销售预申报
		queryAttorneyModel = logisticsAttorneyDao.searchByModel(queryAttorneyModel) ;
		if(queryAttorneyModel!=null){
            logisticsAttorneyModel.setId(queryAttorneyModel.getId());
            logisticsAttorneyModel.setModifyDate(TimeUtils.getNow());
            logisticsAttorneyDao.modifyWithNull(logisticsAttorneyModel);
        }else{
		    logisticsAttorneyModel.setCreateDate(TimeUtils.getNow());
            logisticsAttorneyDao.save(logisticsAttorneyModel);
        }



		//获取预申报单信息
		DeclareOrderModel declareModel = declareOrderDao.searchById(logisticsAttorneyModel.getOrderId());
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_6, declareModel.getCode(), "编辑物流委托书", null, null);
	}

	@Override
	public void getDeclareDetailCheckList(User user, Long id) throws SQLException {
		DeclareOrderModel declareOrderModel=declareOrderDao.searchById(id);
		if(declareOrderModel==null){
			throw new DerpException("预申报订单不存在") ;
		}

		DeclarePurchaseRelModel queryRel = new DeclarePurchaseRelModel() ;
		queryRel.setDeclareOrderId(id);

		List<DeclarePurchaseRelModel> list = declarePurchaseRelDao.list(queryRel);

		for (DeclarePurchaseRelModel relModel : list) {

			Long purchaseOrderId = relModel.getPurchaseOrderId();

			PurchaseOrderModel purchaseOrder = purchaseOrderDao.searchById(purchaseOrderId);
			if(purchaseOrder==null){
				throw new DerpException("关联的采购订单不存在") ;
			}
			if(!(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus())
					|| DERP_ORDER.PURCHASEORDER_STATUS_005.equals(purchaseOrder.getStatus()))){
				throw new DerpException("采购订单状态不为【已审核、部分入库】，采购订单号："+purchaseOrder.getCode()) ;
			}
		}
	}

	//检查相同SKU是否存在多条单价
	@Override
	public void checkRepeatGoods(Long declareOrderId) throws Exception {
		if(declareOrderId == null){
			throw new DerpException("预申报单不存在");
		}
		//检查同一采购订单相同SKU是否存在多条
		DeclareOrderItemModel declareOrderItemModel = new DeclareOrderItemModel();
		declareOrderItemModel.setDeclareOrderId(declareOrderId);
		List<DeclareOrderItemModel> itemList = declareOrderItemDao.list(declareOrderItemModel);
		Map<String, List<DeclareOrderItemModel>> itemGroupByGoodsNoPriceMap = itemList.stream().collect(Collectors.groupingBy(i->i.getPurchase()+"_"+ i.getGoodsNo()+"_"+i.getPrice()));
		for(String key : itemGroupByGoodsNoPriceMap.keySet()){
			List<DeclareOrderItemModel> repeatGoodsList = itemGroupByGoodsNoPriceMap.get(key);
			if(repeatGoodsList.size() > 1){
				throw new DerpException("采购订单："+repeatGoodsList.get(0).getPurchase()+" 存在相同SKU多个单价，不允许导入!");
			}
		}
	}

	@Override
	public DeclareOrderItemDTO getItemPopupListByPage(MerchandiseForm form) throws Exception {
		String unNeedIds = "";
		// 解析json
		if(StringUtils.isNotBlank(form.getUnNeedGoodsJson())){
			JSONObject json = JSONObject.fromObject(form.getUnNeedGoodsJson());
			unNeedIds = (String) json.get("purchaseItemId");
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(form.getOrderCode())) {
			PurchaseOrderModel purchaseModel = new PurchaseOrderModel();
			purchaseModel.setCode(form.getOrderCode());
			purchaseModel = purchaseOrderDao.searchByModel(purchaseModel);
			if(form.getOrderIds().indexOf(purchaseModel.getId().toString()) < 0){
				return new DeclareOrderItemDTO();
			}
			form.setOrderIds(purchaseModel.getId()+"");
		}
		DeclareOrderItemDTO queryDeclareItemDTO = new DeclareOrderItemDTO();
		queryDeclareItemDTO.setPurchaseOrderIds(form.getOrderIds());
		queryDeclareItemDTO.setUnNeedIds(unNeedIds);
		queryDeclareItemDTO.setGoodsNo(form.getGoodsNo());
		queryDeclareItemDTO.setGoodsName(form.getGoodsName());
		queryDeclareItemDTO.setBarcode(form.getBarcode());
		queryDeclareItemDTO = declareOrderItemDao.getItemPopupListByPage(queryDeclareItemDTO);
		List<DeclareOrderItemDTO> queryDeclareItemList = queryDeclareItemDTO.getList();
		for(DeclareOrderItemDTO purchaseItem : queryDeclareItemList) {
			if(purchaseItem.getPurchaseItemId() == null){
				throw new DerpException("采购明细id为空，请及时维护") ;
			}
			purchaseItem.setBrandName("境外品牌(其他)");
		    //根据采购明细id获取已入库数量
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("purchaseItemId", purchaseItem.getPurchaseItemId());
			paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
			List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
			Integer warehouseNum = 0;
			if(numList != null && numList.size() > 0){
				BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
				warehouseNum = queryNum.intValue();
			}
			Integer stayDeclareNum = purchaseItem.getNum() - warehouseNum;//默认 申报数量=采购数量-累计入库数量
			purchaseItem.setNum(stayDeclareNum);
			//获取商品信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", purchaseItem.getGoodsId());
			MerchandiseInfoMogo merchandiseMongo = merchandiseInfoMogoDao.findOne(params);

			Double grossWeightSum = new BigDecimal(merchandiseMongo.getGrossWeight()).multiply(new BigDecimal(stayDeclareNum)).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
			Double netWeightSum = new BigDecimal(merchandiseMongo.getNetWeight()).multiply(new BigDecimal(stayDeclareNum)).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
			purchaseItem.setGrossWeight(merchandiseMongo.getGrossWeight());
			purchaseItem.setGrossWeightSum(grossWeightSum);
			purchaseItem.setNetWeight(merchandiseMongo.getNetWeight());
			purchaseItem.setNetWeightSum(netWeightSum);
			purchaseItem.setConstituentRatio(merchandiseMongo.getComponent());

			// 根据商品id查询商品母品牌 获取申报系数
			Double priceDeclareRatio = 1.0;
			BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(purchaseItem.getGoodsId());
			if ((brandSuperiorMongo != null && brandSuperiorMongo.getPriceDeclareRatio() != null)) {
				priceDeclareRatio = brandSuperiorMongo.getPriceDeclareRatio();
			}

			// 备案单价*商家商品备案价申报系数
			BigDecimal declarePrice = merchandiseMongo.getFilingPrice().multiply(new BigDecimal(priceDeclareRatio)).setScale(5, BigDecimal.ROUND_HALF_UP);
			purchaseItem.setPrice(declarePrice);
			purchaseItem.setAmount(declarePrice.multiply(new BigDecimal(stayDeclareNum)).setScale(2, BigDecimal.ROUND_HALF_UP));

			if (merchandiseMongo.getUnit() != null) {
				// 根据单位id获取单位信息
				Map<String, Object> unitParams = new HashMap<String, Object>();
				unitParams.put("unitId", merchandiseMongo.getUnit());
				UnitMongo unit = unitMongoDao.findOne(unitParams);
				if (unit != null) {
					purchaseItem.setUnitName(unit.getName());// 单位
				}
			}
			//平台关区
			Map<String, Object> merchandiseCustomsRelParams = new HashMap<String, Object>();
			merchandiseCustomsRelParams.put("goodsId", purchaseItem.getGoodsId());
			List<MerchandiseCustomsRelMongo> merchandiseCustomsRelList = merchandiseCustomsRelMongoDao.findAll(merchandiseCustomsRelParams);
			if(merchandiseCustomsRelList != null && merchandiseCustomsRelList.size() > 0) {
				List<String>  customsRelNames = merchandiseCustomsRelList.stream().map(MerchandiseCustomsRelMongo::getCustomsAreaName).collect(Collectors.toList());
				purchaseItem.setCustomsArea(org.apache.commons.lang.StringUtils.join(customsRelNames,","));
			}
		}
		return queryDeclareItemDTO;
	}

	@Override
	public Map<String, Object> exportCustomsDeclares(Long id, List<Long> fileTempIds, User user) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		// 仓库模板
		if (fileTempIds == null || fileTempIds.isEmpty()) {
			return null;
		}

		DeclareOrderDTO dto = declareOrderDao.searchDTOById(id);
		if (dto == null) {
			return null;
		}

		for (Long fileTempId : fileTempIds) {
			FileTempModel fileTempModel = fileTempDao.searchById(fileTempId);
			if (fileTempModel == null || org.apache.commons.lang.StringUtils.isEmpty(fileTempModel.getToUrl())) {
				continue;
			}

			// 南沙仓模板（之前）
			if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NANSHA.equals(fileTempModel.getToUrl())) {
				Map<String, Object> firstCustomsInfo = getNANSHAFirstCustomsInfoDTO(dto, user);
				resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
			}else if (DERP_ORDER.TEMP_CUSTOMSFILECONFIG_NBCIXI.equals(fileTempModel.getToUrl())) {// 宁波慈溪模板
				Map<String, Object> firstCustomsInfo = getFirstCustomsInfoDTO(dto,  user);
				resultMap.put(fileTempModel.getToUrl(), firstCustomsInfo);
			}
		}

		return resultMap;
	}

	/**
	 * 南沙一线清关资料
	 * @param declareOrder
	 * @param user
	 * @return
	 */
	private Map<String, Object> getNANSHAFirstCustomsInfoDTO(DeclareOrderDTO declareOrder , User user) throws Exception {

		DeclareOrderItemModel itemModel = new DeclareOrderItemModel();
		itemModel.setDeclareOrderId(declareOrder.getId());

		List<DeclareOrderItemModel> list = declareOrderItemDao.list(itemModel);
		Set<String> originSet = new TreeSet<>() ; //原产国

		Map<String,List<DeclareOrderItemModel>> itemMap = list.stream().collect(Collectors.groupingBy(DeclareOrderItemModel::getGoodsNo));
		List<Map<String, Object>> itemList = new ArrayList<>();
		for (String goodsNo : itemMap.keySet()) {
			List<DeclareOrderItemModel> items = itemMap.get(goodsNo);
			DeclareOrderItemModel item = items.get(0);
			Integer totalItemNum = items.stream().mapToInt(DeclareOrderItemModel::getNum).sum();
			Double totalItemNetWeight = items.stream().mapToDouble(DeclareOrderItemModel::getNetWeightSum).sum();
			Double totalItemGrossWeight = items.stream().mapToDouble(DeclareOrderItemModel::getGrossWeightSum).sum();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", item.getGoodsId());
			MerchandiseInfoMogo mogo = merchandiseInfoMogoDao.findOne(params);

			if (mogo == null) {
				continue ;
			}

			//获取申报单价
			item.setBarcode(mogo.getBarcode());// 条形码

			Map<String,Object> customsItem = new HashMap<>();
			customsItem.put("goodsNo", item.getGoodsNo());
			customsItem.put("goodsName", item.getGoodsName());
			customsItem.put("goodsSpec", mogo.getSpec());
			customsItem.put("goodsCode", item.getGoodsCode());
			customsItem.put("palletNo", item.getPalletNo());
			customsItem.put("boxNum", item.getCartons());
			customsItem.put("num", totalItemNum);
			customsItem.put("netWeight", totalItemNetWeight);
			customsItem.put("grossWeight", totalItemGrossWeight);
			customsItem.put("constituentRatio", mogo.getComponent());

			Map<String, Object> unitParams = new HashMap<String, Object>();
			unitParams.put("unitId", mogo.getUnit());
			UnitMongo unit = unitMongoDao.findOne(unitParams);

			if(unit != null) {
				customsItem.put("unit", unit.getName());
			}

			Map<String, Object> countryParams = new HashMap<String, Object>();
			countryParams.put("countryOriginId", mogo.getCountyId());
			CountryOriginMongo countryOrigin = countryOriginMongoDao.findOne(countryParams);

			if(countryOrigin != null){
				customsItem.put("country", countryOrigin.getName());
				originSet.add(countryOrigin.getName()) ;
			}
			customsItem.put("price", item.getPrice());
			itemList.add(customsItem) ;

		}

		//根据商家id获取商家信息
		Map<String, Object> merchant_params = new HashMap<String, Object>();
		merchant_params.put("merchantId", declareOrder.getMerchantId());
		MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchant_params);

		String originName = StringUtils.join(originSet, ";") ;

		Map<String,Object> dto = new HashMap<>();
		dto.put("merchantName", merchant.getFullName());
		dto.put("merchantAddr", merchant.getRegisteredAddress());
		dto.put("contractNo", declareOrder.getContractNo());
		dto.put("orderDate", declareOrder.getCreateDate());
		dto.put("country", originName);
		dto.put("invoiceNo", declareOrder.getInvoiceNo());
		dto.put("shipDate", declareOrder.getShipDate());
		dto.put("pack", declareOrder.getPackType());
		dto.put("portLoading", declareOrder.getPortLoading());
		dto.put("shipper", declareOrder.getShipper());
		dto.put("torrNum", declareOrder.getTorrNum());
		dto.put("payRules", DERP_ORDER.getLabelByKey(DERP_ORDER.declareorder_tradeTermsList,declareOrder.getTradeTerms()));
		dto.put("destinationPort", declareOrder.getDestinationPortName());
		dto.put("mark", declareOrder.getMark());
		dto.put("itemList", itemList);

		Map<String, Object> depotMerchantParams = new HashMap<String, Object>();
		depotMerchantParams.put("merchantId", declareOrder.getMerchantId());
		depotMerchantParams.put("depotId", declareOrder.getDepotId());
		DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantParams);

		if(depotMerchantRel != null) {
			dto.put("signNo", depotMerchantRel.getContractCode());
		}

		/**构造包装明细*/
		List<CustomsPackingDetailsDTO> packDtoList = new ArrayList<CustomsPackingDetailsDTO>() ;

		PackingDetailsModel queryModel = new PackingDetailsModel() ;

		queryModel.setOrderId(declareOrder.getId());
		queryModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_1);
		List<PackingDetailsModel> packingDetailsModelList = packingDetailsDao.list(queryModel) ;

		if(!packingDetailsModelList.isEmpty()){
			Map<String,Map<String,Object>> alreadyCalWeightMap = new HashMap<>();
			Map<String, List<PackingDetailsModel>> packingDetailsMap = packingDetailsModelList.stream().collect(Collectors.groupingBy(PackingDetailsModel::getBarcode));
			for (int i = 0 ; i < packingDetailsModelList.size(); i ++) {

				PackingDetailsModel packingDetailsModel = packingDetailsModelList.get(i);

				CustomsPackingDetailsDTO tepmDto = new CustomsPackingDetailsDTO() ;

				BeanUtils.copyProperties(packingDetailsModel, tepmDto);

				//条码找到预申报的商品货号（找到多个随机取一个）的净重*件数
				DeclareOrderItemDTO queryItemDTO = new DeclareOrderItemDTO() ;
				queryItemDTO.setDeclareOrderId(declareOrder.getId());
				queryItemDTO.setBarcode(packingDetailsModel.getBarcode());
				List<DeclareOrderItemDTO> queryItemList = declareOrderItemDao.listDTO(queryItemDTO) ;

				if(queryItemList != null && queryItemList.size() > 0){
					DeclareOrderItemDTO queryDeclareItemDTO = queryItemList.get(0);
					BigDecimal netWeight = new BigDecimal(queryDeclareItemDTO.getNetWeightSum()).divide(new BigDecimal(queryDeclareItemDTO.getNum()),5 ,BigDecimal.ROUND_HALF_UP);
					BigDecimal netWeightSum = netWeight.multiply(new BigDecimal(packingDetailsModel.getPiecesNum()));

					tepmDto.setGoodsName(queryDeclareItemDTO.getGoodsName());
					tepmDto.setNetWeight(netWeightSum.doubleValue());
					/**
					 * 2、毛重取值逻辑：
					 * （1）条码只有1行时：汇总求和预申报单对应条码的毛重，保留3位小数
					 * （2）条码不只有1行时，汇总求和预申报单对应条码的毛重:TOTAL_MZ,及总净重：TOTAL_JZ分以下2种情况
					 *    前N-1行：(该行总净重/TOTAL_JZ)*TOTAL_MZ,保留3位小数
					 *    第N行：TOTAL_MZ - 前N-1行总毛重
					 */
					Double totalItemGrossWeight = queryItemList.stream().map(DeclareOrderItemDTO::getGrossWeightSum).reduce(0.0,Double::sum);//根据条码累计总毛重
					Double totalItemNetWeight = queryItemList.stream().map(DeclareOrderItemDTO::getNetWeightSum).reduce(0.0,Double::sum);//根据条码累计总净重

					BigDecimal grossWeight = BigDecimal.ZERO;
					List<PackingDetailsModel> packingDetailsList = packingDetailsMap.get(packingDetailsModel.getBarcode());
//					if(packingDetailsList.size() == 1){
//						//（1）条码只有1行时：汇总求和预申报单对应条码的毛重，保留3位小数
//						grossWeight = new BigDecimal(totalItemGrossWeight).setScale(3,BigDecimal.ROUND_HALF_UP);
//					}else{
//
//					}
					Map<String,Object> map = alreadyCalWeightMap.get(packingDetailsModel.getBarcode());
					int index = 1;
					BigDecimal alreadyCalWeight = BigDecimal.ZERO;
					if(map != null){
						index = (int) map.get("index");
						alreadyCalWeight = (BigDecimal) map.get("alreadyCalWeight");
					}

					if(index == packingDetailsList.size()){
						//第N行：TOTAL_MZ - 前N-1行总毛重
						grossWeight = new BigDecimal(totalItemGrossWeight).subtract(alreadyCalWeight).setScale(3,BigDecimal.ROUND_HALF_UP);
					}else{
						//前N-1行：(该行总净重/TOTAL_JZ)*TOTAL_MZ,保留3位小数
						if(totalItemNetWeight.compareTo(0.0) > 0){
							grossWeight = netWeightSum.divide(new BigDecimal(totalItemNetWeight), 5 ,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(totalItemGrossWeight)).setScale(3, BigDecimal.ROUND_HALF_UP);
							alreadyCalWeight =  alreadyCalWeight.add(grossWeight);
							map = new HashMap<>();
							map.put("index", ++index);
							map.put("alreadyCalWeight", alreadyCalWeight);
							alreadyCalWeightMap.put(packingDetailsModel.getBarcode(), map);
						}
					}

					tepmDto.setGrossWeight(grossWeight.doubleValue());
				}
				packDtoList.add(tepmDto) ;
			}
		}
//		List<CustomsPackingDetailsDTO> detailsDTOList = DownloadExcelUtil.calculateWeights(packDtoList);
		List<Map<String, Object>> detailsList = new ArrayList<>();
		if (packDtoList.isEmpty()){
			for (Map<String,Object> item : itemList){
				Map<String,Object> detailItem = new HashMap<>();
				String goodsNo = (String) item.get("goodsNo");
				String goodsName = (String) item.get("goodsName");
				Integer num = (Integer) item.get("num");
				Integer boxNum = (Integer) item.get("boxNum");
				Double netWeight = (Double) item.get("netWeight");
				Double grossWeight = (Double) item.get("grossWeight");
				String palletNo = (String) item.get("palletNo");
				String cabinetNo = "/";

				detailItem.put("goodsNo", goodsNo);
				detailItem.put("goodsName", goodsName);
				detailItem.put("num", num);
				detailItem.put("boxNum",boxNum );
				detailItem.put("netWeight", netWeight);
				detailItem.put("grossWeight", grossWeight);
				detailItem.put("palletNo", palletNo);
				detailItem.put("cabinetNo", cabinetNo);
				detailsList.add(detailItem);
			}
		}else{
			for (CustomsPackingDetailsDTO item : packDtoList){
				Map<String,Object> detailItem = new HashMap<>();
				detailItem.put("goodsNo", item.getGoodsNo());
				detailItem.put("goodsName",  item.getGoodsName());
				detailItem.put("num", item.getPiecesNum() == null ? 0 :item.getPiecesNum());
				detailItem.put("boxNum",item.getBoxNum() == null ? 0 :item.getBoxNum() );
				detailItem.put("netWeight", item.getNetWeight() == null ? 0 :item.getNetWeight());
				detailItem.put("grossWeight", item.getGrossWeight()== null ? 0 :item.getGrossWeight());
				detailItem.put("palletNo", item.getTorrNo());
				detailItem.put("cabinetNo", item.getCabinetNo());
				detailsList.add(detailItem);
			}
		}

		dto.put("detailsDTOList", detailsList);
		/**构造包装明细*/

		return dto;
	}

	/**
	 * 一线清关资料
	 * @param declareOrder
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getFirstCustomsInfoDTO(DeclareOrderDTO declareOrder , User user) throws Exception{
		DeclareOrderItemDTO itemDTO = new DeclareOrderItemDTO();
		itemDTO.setDeclareOrderId(declareOrder.getId());
		List<DeclareOrderItemDTO> itemList = declareOrderItemDao.listDTO(itemDTO);
		// 根据商家id获取商家信息
		Map<String, Object> merchantParams = new HashMap<String, Object>();
		merchantParams.put("merchantId", declareOrder.getMerchantId());
		MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantParams);

		Map<String,Object> dto = new HashMap<>();
		if (declareOrder != null) {
			dto.put("transportation",declareOrder.getTransportLabel());// 运输方式
			dto.put("customsContractNo",declareOrder.getContractNo());// 报关合同号（excel中的提运单号）);
			dto.put("poNo",declareOrder.getPoNo());// po号（excel中的合同号）
			dto.put("contractNo",declareOrder.getContractNo());
			dto.put("invoiceNo",declareOrder.getInvoiceNo());// 发票号
			dto.put("payRules",declareOrder.getTradeTerms());// 付款条约
			dto.put("pack",declareOrder.getPackType());// 包装方式
			// 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱
			String packType = DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList,declareOrder.getPalletMaterial());
			dto.put("palletMaterial",packType);// 托盘材质;
			dto.put("torrNum",declareOrder.getTorrNum());// 托数
			dto.put("payRules",declareOrder.getTradeTermsLabel());// 付款条约
			dto.put("orderDate",declareOrder.getCreateDate());// 订单日期
			dto.put("boxNum",declareOrder.getBoxNum());// 箱数
			dto.put("portLoading",declareOrder.getPortLoading());// 装货港
			dto.put("shipDate",declareOrder.getShipDate());//装船时间
			dto.put("destination",declareOrder.getDestinationPortName());//目的地
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
			Map<String, List<DeclareOrderItemDTO>> itemMap = itemList.stream().collect(Collectors.groupingBy(DeclareOrderItemDTO :: getGoodsNo));
			for(String goodsNo: itemMap.keySet()) {
				List<DeclareOrderItemDTO> items = itemMap.get(goodsNo);
				int totalItemNum = items.stream().mapToInt(DeclareOrderItemDTO :: getNum).sum();
				Double netWeight = 0.0;
				DeclareOrderItemDTO item = items.get(0);
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
				customsItem.put("cartons", item.getCartons());
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
	public List<PackingDetailsModel> getPackingDetail(Long id) throws SQLException {
		PackingDetailsModel queryPackingModel = new PackingDetailsModel() ;
		queryPackingModel.setOrderId(id);
		queryPackingModel.setOrderType(DERP_ORDER.PACKINGDETAILS_ORDERTYPE_1);
		return packingDetailsDao.list(queryPackingModel);
	}
}
