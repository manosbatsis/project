package com.topideal.order.service.sale.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.dao.sale.PreSaleOrderCorrelationDao;
import com.topideal.dao.sale.PreSaleOrderDao;
import com.topideal.dao.sale.PreSaleOrderItemDao;
import com.topideal.entity.dto.purchase.PurchaseOrderExportDTO;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.entity.dto.sale.PreSaleOrderDTO;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.sale.PreSaleOrderCorrelationModel;
import com.topideal.entity.vo.sale.PreSaleOrderItemModel;
import com.topideal.entity.vo.sale.PreSaleOrderModel;
import com.topideal.mongo.dao.BrandMongoDao;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.CustomerMerchantRelMongoDao;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.MerchantDepotBuRelMongoDao;
import com.topideal.mongo.dao.PurchaseCommissionMongoDao;
import com.topideal.mongo.dao.SupplierMerchandisePriceMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.BrandMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.CustomerMerchantRelMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.mongo.entity.MerchantDepotBuRelMongo;
import com.topideal.mongo.entity.PurchaseCommissionMongo;
import com.topideal.mongo.entity.SupplierMerchandisePriceMongo;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.sale.PreSaleOrderService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 预售单service实现类
 */
@Service
public class PreSaleOrderServiceImpl implements PreSaleOrderService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory
			.getLogger(PreSaleOrderServiceImpl.class);
	// 销售订单表头
	@Autowired
	private PreSaleOrderDao preSaleOrderDao;
	// 销售订单表体
	@Autowired
	private PreSaleOrderItemDao preSaleOrderItemDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	// 公司仓库事业部关联表
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	//预售勾稽
	@Autowired
	private PreSaleOrderCorrelationDao preSaleOrderCorrelationDao;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;
	// 仓库公司关联表 mongo
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	//供应商
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private BrandMongoDao brandMongoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	// 采购执行Mongo
	@Autowired
	private PurchaseCommissionMongoDao purchaseCommissionMongoDao;
	// 采购订单表头
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	// 采购订单表体
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	@Autowired
	private SupplierMerchandisePriceMongoDao supplierMerchandisePriceMongoDao ;

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public PreSaleOrderDTO listPreSaleOrderByPage(PreSaleOrderDTO dto,User user)
			throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		PreSaleOrderDTO preSaleOrderDTO = preSaleOrderDao.queryDTOListByPage(dto);
		int total = preSaleOrderDao.getTotal(dto);
		preSaleOrderDTO.setTotal(total);
		return preSaleOrderDTO;
	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public PreSaleOrderDTO searchDetail(Long id) throws SQLException {
		PreSaleOrderDTO dto = new PreSaleOrderDTO();
		dto.setId(id);
		return preSaleOrderDao.searchDTOById(id);
	}

	/**
	 * 根据表头ID获取表体(包括商品信息)
	 * @param id
	 * @return
	 */
	@Override
	public List<PreSaleOrderItemDTO> listItemByOrderId(Long id) throws SQLException {
		PreSaleOrderItemDTO preSaleOrderItemDTO = new PreSaleOrderItemDTO();
		preSaleOrderItemDTO.setOrderId(id);
		return preSaleOrderItemDao.listPreSaleOrderItemDTO(preSaleOrderItemDTO);
	}

	@Override
	public List<PreSaleOrderDTO> listPreSaleOrder(PreSaleOrderDTO dto,User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return new ArrayList<PreSaleOrderDTO>();
			}
			dto.setBuList(buIds);
		}
		return preSaleOrderDao.queryDTOList(dto);
	}

	@Override
	public boolean saveOrModifyTempOrder(String json, Long userId, String name, String topidealCode) throws Exception {
		PreSaleOrderModel preSaleOrderModel = new PreSaleOrderModel();
		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		//判断传过来的id是否为空，为空则新增，不为空则修改
		String idStr = jsonObj.getString("orderId");
		Long merchantId = Long.valueOf(jsonObj.getString("merchantId"));
		String merchantName = (String) jsonObj.get("merchantName");
		Long id = null;
		if(StringUtils.isNotBlank(idStr)){	// 修改预售单
			id = Long.valueOf(idStr);
			String orderCode = jsonObj.getString("orderCode");
			preSaleOrderModel.setCode(orderCode);
			preSaleOrderModel.setId(id);
			// 首先查询预售单是否已审核
			PreSaleOrderDTO preSaleOrderDTO = preSaleOrderDao.searchDTOById(id);
			if(DERP_ORDER.PRESALEORDER_STATE_003.equals(preSaleOrderDTO.getState())){
				throw new RuntimeException("保存失败,该预售单已经审核");
			}
		}else{	// 新增预售单
			preSaleOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSD));
		}
		Long customerId =null;
		if(StringUtils.isNotBlank(jsonObj.getString("customerId"))){
			customerId = Long.valueOf(jsonObj.getString("customerId"));
		}
		String businessModel = (String) jsonObj.get("businessModel");
		Long outDepotId = null;
		if(StringUtils.isNotBlank(jsonObj.getString("outDepotId"))){
			outDepotId = Long.valueOf(jsonObj.getString("outDepotId"));
		}
		Long buId = null;
		String buIdStr = jsonObj.getString("buId");	// 事业部
		MerchantBuRelMongo merchantBuRelMongo = null;
		if(StringUtils.isNotBlank(buIdStr)){
			buId = Long.valueOf(buIdStr);	// 事业部ID
			// 获取该事业部信息
			Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
			merchantBuRelParam.put("merchantId", merchantId);
			merchantBuRelParam.put("buId", buId);
			merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
			merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
			if(merchantBuRelMongo == null || "".equals(merchantBuRelMongo)){
				throw new RuntimeException("事业部ID为："+buId+",未查到该公司下事业部信息");
			}
		}
		String manyPoNo = (String) jsonObj.get("poNo");	// 获取所有的po号
		String currency = (String) jsonObj.get("currency");		// 销售币种
		String remark = (String) jsonObj.get("remark");// 备注
		String tallyingUnit = null;// 理货单位
		if(jsonObj.containsKey("tallyingUnit") && jsonObj.getString("tallyingUnit") != null && !"".equals(jsonObj.getString("tallyingUnit"))){
			tallyingUnit = (String) jsonObj.get("tallyingUnit");
		}
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("depotId", outDepotId);
		DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params1);
		if(null != outDepot && null != merchantBuRelMongo){
			// 校验公司-仓库-事业部的关联表
			Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
			merchantDepotBuRelParam.put("merchantId", merchantId);
			merchantDepotBuRelParam.put("depotId", outDepot.getDepotId());	// 出仓仓库
			merchantDepotBuRelParam.put("buId", buId);
			MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
			if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
				throw new RuntimeException("事业部编码为："+merchantBuRelMongo.getBuCode()+",出仓仓库："+outDepot.getDepotCode()+",未查到该公司仓库事业部关联信息");
			}
		}
		//保存表头数据
		preSaleOrderModel.setMerchantId(merchantId);
		preSaleOrderModel.setMerchantName(merchantName);
		preSaleOrderModel.setBusinessModel(businessModel);
		preSaleOrderModel.setCreateDate(TimeUtils.getNow());
		preSaleOrderModel.setCreater(userId);
		preSaleOrderModel.setCreateName(name);
		preSaleOrderModel.setCustomerId(customerId);
		if(customerId!=null){
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("customerid", customerId);
			CustomerInfoMogo customer = customerInfoMongoDao.findOne(params2);
			preSaleOrderModel.setCustomerName(customer.getName());
		}
		if(merchantBuRelMongo!=null){
			preSaleOrderModel.setBuId(merchantBuRelMongo.getBuId());	// 事业部ID
			preSaleOrderModel.setBuName(merchantBuRelMongo.getBuName());// 事业部名称
		}
		preSaleOrderModel.setPoNo(manyPoNo);
		preSaleOrderModel.setOutDepotId(outDepotId);
		preSaleOrderModel.setOutDepotName(outDepot.getName());
		if(tallyingUnit != null){
			preSaleOrderModel.setTallyingUnit(tallyingUnit);
		}
		preSaleOrderModel.setRemark(remark);
		preSaleOrderModel.setCurrency(currency);	// 销售币种
		preSaleOrderModel.setState(DERP_ORDER.PRESALEORDER_STATE_001);	// 待审核
		//解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		List<Long> itemIds = new ArrayList<Long>();
		BigDecimal totalAmount = BigDecimal.ZERO;
		List<PreSaleOrderItemModel> itemList=new ArrayList<PreSaleOrderItemModel>();
		for(int i=0;i<itemArr.size();i++){
			JSONObject job = itemArr.getJSONObject(i);
			if( job.isNullObject() || job.isEmpty()){
				continue;
			}
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsCode = (String) job.get("goodsCode");
			String goodsNo = (String) job.get("goodsNo");
			String goodsName = (String) job.get("goodsName");
			String barcode = (String) job.get("barcode");
			String numStr =(String) job.getString("num");
			Integer num = Integer.valueOf(numStr.trim());
			String price = (String) job.get("price");
			price = price.trim();
			String amount = (String) job.get("amount");	// 销售总金额
			amount = amount.trim();
			String brandName = (String) job.get("brandName");				
			String taxRate = (String) job.get("taxRate");
			taxRate = taxRate.trim();
			if(StringUtils.isBlank(taxRate)) {
				throw new RuntimeException("商品货号："+goodsNo+",税率为空");
			}
			String tax = (String) job.get("tax");
			tax = tax.trim();
			String taxPrice = (String) job.get("taxPrice");
			taxPrice = taxPrice.trim();
			String taxAmount = (String) job.get("taxAmount");
			taxAmount = taxAmount.trim();

			totalAmount = totalAmount.add(new BigDecimal(amount));
			//注入数据
			PreSaleOrderItemModel itemModel = new PreSaleOrderItemModel();
			itemModel.setBarcode(barcode);
			itemModel.setGoodsCode(goodsCode);
			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsName(goodsName);
			itemModel.setGoodsNo(goodsNo);
			itemModel.setNum(num);
			itemModel.setBrandName(brandName);
			itemModel.setPrice(new BigDecimal(price).setScale(8,BigDecimal.ROUND_HALF_UP));//预售单价（不含税），单价保留8位小数；
			itemModel.setAmount(new BigDecimal(amount).setScale(2,BigDecimal.ROUND_HALF_UP));//预售总金额（不含税），金额保留2位小数；
			itemModel.setCreateDate(TimeUtils.getNow());

			//获取商品信息
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(paramMap);
			if(merchandise==null){	// 商品不存在保存失败
				return false;
			}
			itemModel.setCommbarcode(merchandise.getCommbarcode());	// 标准条码
			if(id!=null){	// 如果是编辑
				if(job.containsKey("id") && StringUtils.isNotBlank(job.getString("id"))){//修改
					Long itemId = Long.valueOf(job.getString("id"));
					itemModel.setId(itemId);
					preSaleOrderItemDao.modify(itemModel);
					itemIds.add(itemId);
				}else{//新增
					itemModel.setOrderId(id);
					Long itemId = preSaleOrderItemDao.save(itemModel);
					itemIds.add(itemId);
				}
			}
			itemModel.setTaxRate(Double.valueOf(taxRate));
			itemModel.setTax(new BigDecimal(tax));
			itemModel.setTaxPrice(new BigDecimal(taxPrice));
			itemModel.setTaxAmount(new BigDecimal(taxAmount));
			itemList.add(itemModel);
		}
		List<Long> reportIds =new ArrayList<>();
		int num=0;
		if(id == null){
			// 新增表头
			id = preSaleOrderDao.save(preSaleOrderModel);
		}else{
			// 编辑表头
			preSaleOrderDao.modify(preSaleOrderModel);
			////删除原表体
			PreSaleOrderItemModel queryItemModel = new PreSaleOrderItemModel();
			queryItemModel.setOrderId(id);
			List<PreSaleOrderItemModel> delItemList = preSaleOrderItemDao.list(queryItemModel);
			for(PreSaleOrderItemModel itemModel : delItemList){
				reportIds.add(itemModel.getId());
			}
			//有商品的先删除
			preSaleOrderItemDao.delete(reportIds);
		}
		for(PreSaleOrderItemModel itemModel : itemList){
			itemModel.setOrderId(id);
			// 新增表体
			preSaleOrderItemDao.save(itemModel);
		}
		
		// 推送报表 删除预售单表体
		if(id != null || num>0){
			return true;
		}
		return false;
	}

	/**
	 * 新增预售单
	 * @param json
	 * @param userId
	 * @param userName
	 * @param topidealCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public String addPreSaleOrder(String json, Long userId, String userName, String topidealCode) throws Exception {
		PreSaleOrderModel preSaleOrderModel = new PreSaleOrderModel();
		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		Long merchantId = Long.valueOf(jsonObj.getString("merchantId"));
		String merchantName = (String) jsonObj.get("merchantName");
		Long customerId =null;
		if(StringUtils.isNotBlank(jsonObj.getString("customerId"))){
			customerId = Long.valueOf(jsonObj.getString("customerId"));
		}
		String businessModel = (String) jsonObj.get("businessModel");
		Long outDepotId = null;
		if(StringUtils.isNotBlank(jsonObj.getString("outDepotId"))){
			outDepotId = Long.valueOf(jsonObj.getString("outDepotId"));
		}
		Long buId = null;
		String buIdStr = jsonObj.getString("buId");	// 事业部
		MerchantBuRelMongo merchantBuRelMongo = null;
		if(StringUtils.isNotBlank(buIdStr)){
			buId = Long.valueOf(buIdStr);	// 事业部ID
			// 获取该事业部信息
			Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
			merchantBuRelParam.put("merchantId", merchantId);
			merchantBuRelParam.put("buId", buId);
			merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
			merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
			if(merchantBuRelMongo == null || "".equals(merchantBuRelMongo)){
				throw new RuntimeException("事业部ID为："+buId+",未查到该公司下事业部信息");
			}
		}
		String manyPoNo = (String) jsonObj.get("poNo");	// 获取所有的po号
		String currency = (String) jsonObj.get("currency");		// 销售币种
		String remark = (String) jsonObj.get("remark");// 备注
		String tallyingUnit = null;// 理货单位
		if(jsonObj.containsKey("tallyingUnit") && jsonObj.getString("tallyingUnit") != null && !"".equals(jsonObj.getString("tallyingUnit"))){
			tallyingUnit = (String) jsonObj.get("tallyingUnit");
		}
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("depotId", outDepotId);
		DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params1);
		if(null != outDepot && null != merchantBuRelMongo){
			// 校验公司-仓库-事业部的关联表
			Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
			merchantDepotBuRelParam.put("merchantId", merchantId);
			merchantDepotBuRelParam.put("depotId", outDepot.getDepotId());	// 出仓仓库
			merchantDepotBuRelParam.put("buId", buId);
			MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
			if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
				throw new RuntimeException("事业部编码为："+merchantBuRelMongo.getBuCode()+",出仓仓库："+outDepot.getDepotCode()+",未查到该公司仓库事业部关联信息");
			}
		}
		// 校验事业部与当前账号所绑定的事业部是否匹配
		boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(userId, merchantBuRelMongo.getBuId());
		if(!userRelateBu){
			throw new RuntimeException("事业部编码为："+merchantBuRelMongo.getBuCode()+",用户id："+userId+",无权限操作该事业部");
		}
		//保存表头数据
		preSaleOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSD));// 预售单单号
		preSaleOrderModel.setMerchantId(merchantId);
		preSaleOrderModel.setMerchantName(merchantName);
		preSaleOrderModel.setBusinessModel(businessModel);
		preSaleOrderModel.setCreateDate(TimeUtils.getNow());
		preSaleOrderModel.setCreater(userId);
		preSaleOrderModel.setCreateName(userName);
		preSaleOrderModel.setCustomerId(customerId);
		if(customerId!=null){
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("customerid", customerId);
			CustomerInfoMogo customer = customerInfoMongoDao.findOne(params2);
			preSaleOrderModel.setCustomerName(customer.getName());
		}
		if(merchantBuRelMongo!=null){
			preSaleOrderModel.setBuId(merchantBuRelMongo.getBuId());	// 事业部ID
			preSaleOrderModel.setBuName(merchantBuRelMongo.getBuName());// 事业部名称
		}
		preSaleOrderModel.setPoNo(manyPoNo);
		preSaleOrderModel.setOutDepotId(outDepotId);
		preSaleOrderModel.setOutDepotName(outDepot.getName());
		if(tallyingUnit != null){
			preSaleOrderModel.setTallyingUnit(tallyingUnit);
		}
		preSaleOrderModel.setRemark(remark);
		preSaleOrderModel.setCurrency(currency);	// 销售币种
		preSaleOrderModel.setState(DERP_ORDER.PRESALEORDER_STATE_001);	// 待审核
		//解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		BigDecimal totalAmount = BigDecimal.ZERO;
		List<PreSaleOrderItemModel> itemList=new ArrayList<PreSaleOrderItemModel>();
		for(int i=0;i<itemArr.size();i++){
			JSONObject job = itemArr.getJSONObject(i);
			if( job.isNullObject() || job.isEmpty()){
				continue;
			}
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsCode = (String) job.get("goodsCode");
			String goodsNo = (String) job.get("goodsNo");
			String goodsName = (String) job.get("goodsName");
			String barcode = (String) job.get("barcode");
			String numStr =(String) job.getString("num");
			Integer num = Integer.valueOf(numStr.trim());
			String price = (String) job.get("price");
			price = price.trim();
			String amount = (String) job.get("amount");	// 销售总金额
			amount = amount.trim();
			String brandName = (String) job.get("brandName");
			String taxRate = (String) job.get("taxRate");
			taxRate = taxRate.trim();
			if(StringUtils.isBlank(taxRate)) {
				throw new RuntimeException("商品货号："+goodsNo+",税率为空");
			}
			String tax = (String) job.get("tax");
			tax = tax.trim();
			String taxPrice = (String) job.get("taxPrice");
			taxPrice = taxPrice.trim();
			String taxAmount = (String) job.get("taxAmount");
			taxAmount = taxAmount.trim();

			totalAmount = totalAmount.add(new BigDecimal(amount));
			//注入数据
			PreSaleOrderItemModel itemModel = new PreSaleOrderItemModel();
			itemModel.setBarcode(barcode);
			itemModel.setGoodsCode(goodsCode);
			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsName(goodsName);
			itemModel.setGoodsNo(goodsNo);
			itemModel.setNum(num);
			itemModel.setBrandName(brandName);
			itemModel.setPrice(new BigDecimal(price));
			itemModel.setAmount(new BigDecimal(amount));
			itemModel.setCreateDate(TimeUtils.getNow());

			//获取商品信息
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(paramMap);
			if(merchandise==null){	// 商品不存在保存失败
				throw new RuntimeException("新增失败,没有查询到商品信息");
			}
			itemModel.setCommbarcode(merchandise.getCommbarcode());	// 标准条码
			itemModel.setTaxRate(Double.valueOf(taxRate));
			itemModel.setTax(new BigDecimal(tax));
			itemModel.setTaxPrice(new BigDecimal(taxPrice));
			itemModel.setTaxAmount(new BigDecimal(taxAmount));
			itemList.add(itemModel);
		}
		// 新增表头
		Long id = preSaleOrderDao.save(preSaleOrderModel);
		for(PreSaleOrderItemModel itemModel : itemList){
			itemModel.setOrderId(id);
			// 新增表体
			preSaleOrderItemDao.save(itemModel);
		}

		if(id == null){
			return "新增失败";
		}
		// 审核
		Map<String,Object> msgMap = auditPreSaleOrder(preSaleOrderModel, userId, userName);
		String errorMsg = (String)msgMap.get("msg");
		if(StringUtils.isNotBlank(errorMsg)){	// 如果有错误信息
			return errorMsg;
		}
		return "新增并审核成功";
	}

	/**
	 * 审核预售单
	 * @param preSaleOrderModel
	 * @param userId
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object>  auditPreSaleOrder(PreSaleOrderModel preSaleOrderModel,Long userId, String name) throws Exception {
		int num=0;
		StringBuffer sb = new StringBuffer();
		String msg = null ;
		Long flag2=null;
		// 新增编辑预售单审核==========>往预售勾稽明细表插入预售单数量
		// 1.修改预售单为已审核
		PreSaleOrderModel modifyModel = new PreSaleOrderModel();
		modifyModel.setId(preSaleOrderModel.getId());
		modifyModel.setModifyDate(TimeUtils.getNow());
		modifyModel.setState(DERP_ORDER.PRESALEORDER_STATE_003);// 已审核
		modifyModel.setAuditDate(TimeUtils.getNow());
		modifyModel.setAuditName(name);
		modifyModel.setAuditor(userId);
		int flag1 = preSaleOrderDao.modify(modifyModel);
		// 2.往预售勾稽明细表增数据
		PreSaleOrderItemModel preSaleOrderItemModel = new PreSaleOrderItemModel();
		preSaleOrderItemModel.setOrderId(preSaleOrderModel.getId());
		List<PreSaleOrderItemModel> itemList = preSaleOrderItemDao.list(preSaleOrderItemModel);
		for (int i = 0; i < itemList.size(); i++) {
			PreSaleOrderItemModel itemModel = itemList.get(i);
			PreSaleOrderCorrelationModel correlationModel = new PreSaleOrderCorrelationModel();
			correlationModel.setMerchantId(preSaleOrderModel.getMerchantId());
			correlationModel.setMerchantName(preSaleOrderModel.getMerchantName());
			correlationModel.setPreSaleOrderId(preSaleOrderModel.getId());
			correlationModel.setPreSaleOrderCode(preSaleOrderModel.getCode());
			correlationModel.setGoodsId(itemModel.getGoodsId());
			correlationModel.setGoodsName(itemModel.getGoodsName());
			correlationModel.setGoodsNo(itemModel.getGoodsNo());
			correlationModel.setGoodsCode(itemModel.getGoodsCode());
			correlationModel.setBarcode(itemModel.getBarcode());
			correlationModel.setCommbarcode(itemModel.getCommbarcode());
			correlationModel.setPreNum(itemModel.getNum());
			correlationModel.setCreateDate(TimeUtils.getNow());
			correlationModel.setAuditDate(TimeUtils.getNow());
			correlationModel.setAuditName(name);
			correlationModel.setAuditor(userId);
			flag2 = preSaleOrderCorrelationDao.save(correlationModel);
		}
		if(flag1==0 || flag2==null){
			msg+="单号："+preSaleOrderModel.getCode()+",审核出错\n";
		}else{
			num++;
		}
		msg = sb.toString() ;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",num);
		map.put("msg",msg);
		return map;
	}
	/**
	 * 删除预售单
	 * @param ids
	 * @return
	 * @throws RuntimeException
	 */
	@Override
	public boolean delPreSaleOrder(List ids) throws SQLException, RuntimeException {
		boolean flag = false;
		//判断是否有已审核的订单
		for(Object id:ids){
			//获取销售订单信息
			PreSaleOrderModel saleOrder = preSaleOrderDao.searchById(Long.parseLong(id.toString()));
			//单据状态是已审核，
			if(saleOrder.getState() != null && !saleOrder.getState().equals(DERP_ORDER.SALEORDER_STATE_001)){
				flag = true;
				break;
			}
		}
		if(flag){
			throw new RuntimeException("只能删除待审核预售单");
		}
		int num = preSaleOrderDao.delPreSaleOrder(ids);
		if(num >= 1){
            return true;
        }
        return false;
	}

	/**
	 * 修改并审核预售单
	 * @param json
	 * @param userId
	 * @param userName
	 * @param topidealCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public String modifyPreSaleOrder(String json, Long userId, String userName, String topidealCode) throws Exception {
		PreSaleOrderModel preSaleOrderModel = new PreSaleOrderModel();
		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		Long orderId = Long.valueOf(jsonObj.getString("orderId"));
		// 首先查询预售单是否已审核
		PreSaleOrderDTO preSaleOrderDTO = preSaleOrderDao.searchDTOById(orderId);
		if(DERP_ORDER.PRESALEORDER_STATE_003.equals(preSaleOrderDTO.getState())){
			throw new RuntimeException("审核失败,该预售单已经审核");
		}
		String orderCode = jsonObj.getString("orderCode");
		Long merchantId = Long.valueOf(jsonObj.getString("merchantId"));
		String merchantName = (String) jsonObj.get("merchantName");
		Long customerId =null;
		if(StringUtils.isNotBlank(jsonObj.getString("customerId"))){
			customerId = Long.valueOf(jsonObj.getString("customerId"));
		}
		String businessModel = (String) jsonObj.get("businessModel");
		Long outDepotId = null;
		if(StringUtils.isNotBlank(jsonObj.getString("outDepotId"))){
			outDepotId = Long.valueOf(jsonObj.getString("outDepotId"));
		}
		Long buId = null;
		String buIdStr = jsonObj.getString("buId");	// 事业部
		MerchantBuRelMongo merchantBuRelMongo = null;
		if(StringUtils.isNotBlank(buIdStr)){
			buId = Long.valueOf(buIdStr);	// 事业部ID
			// 获取该事业部信息
			Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
			merchantBuRelParam.put("merchantId", merchantId);
			merchantBuRelParam.put("buId", buId);
			merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
			merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
			if(merchantBuRelMongo == null || "".equals(merchantBuRelMongo)){
				throw new RuntimeException("事业部ID为："+buId+",未查到该公司下事业部信息");
			}
		}
		String manyPoNo = (String) jsonObj.get("poNo");	// 获取所有的po号
		String currency = (String) jsonObj.get("currency");		// 销售币种
		String remark = (String) jsonObj.get("remark");// 备注
		String tallyingUnit = null;// 理货单位
		if(jsonObj.containsKey("tallyingUnit") && jsonObj.getString("tallyingUnit") != null && !"".equals(jsonObj.getString("tallyingUnit"))){
			tallyingUnit = (String) jsonObj.get("tallyingUnit");
		}
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("depotId", outDepotId);
		DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params1);
		if(null != outDepot && null != merchantBuRelMongo){
			// 校验公司-仓库-事业部的关联表
			Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
			merchantDepotBuRelParam.put("merchantId", merchantId);
			merchantDepotBuRelParam.put("depotId", outDepot.getDepotId());	// 出仓仓库
			merchantDepotBuRelParam.put("buId", buId);
			MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
			if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
				throw new RuntimeException("事业部编码为："+merchantBuRelMongo.getBuCode()+",出仓仓库："+outDepot.getDepotCode()+",未查到该公司仓库事业部关联信息");
			}
		}
		//修改数据
		preSaleOrderModel.setId(orderId);
		preSaleOrderModel.setCode(orderCode);// 预售单单号
		preSaleOrderModel.setMerchantId(merchantId);
		preSaleOrderModel.setMerchantName(merchantName);
		preSaleOrderModel.setBusinessModel(businessModel);
		preSaleOrderModel.setModifyDate(TimeUtils.getNow());// 修改时间
		preSaleOrderModel.setModifier(userId);
		preSaleOrderModel.setModifierUsername(userName);
		preSaleOrderModel.setCustomerId(customerId);
		if(customerId!=null){
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("customerid", customerId);
			CustomerInfoMogo customer = customerInfoMongoDao.findOne(params2);
			preSaleOrderModel.setCustomerName(customer.getName());
		}
		if(merchantBuRelMongo!=null){
			preSaleOrderModel.setBuId(merchantBuRelMongo.getBuId());	// 事业部ID
			preSaleOrderModel.setBuName(merchantBuRelMongo.getBuName());// 事业部名称
		}
		preSaleOrderModel.setPoNo(manyPoNo);
		preSaleOrderModel.setOutDepotId(outDepotId);
		preSaleOrderModel.setOutDepotName(outDepot.getName());
		if(tallyingUnit != null){
			preSaleOrderModel.setTallyingUnit(tallyingUnit);
		}
		preSaleOrderModel.setRemark(remark);
		preSaleOrderModel.setCurrency(currency);	// 销售币种
		preSaleOrderModel.setState(DERP_ORDER.PRESALEORDER_STATE_001);	// 待审核
		//解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		List<Long> itemIds = new ArrayList<Long>();
		BigDecimal totalAmount = BigDecimal.ZERO;
		for(int i=0;i<itemArr.size();i++){
			JSONObject job = itemArr.getJSONObject(i);
			if( job.isNullObject() || job.isEmpty()){
				continue;
			}
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsCode = (String) job.get("goodsCode");
			String goodsNo = (String) job.get("goodsNo");
			String goodsName = (String) job.get("goodsName");
			String barcode = (String) job.get("barcode");
			String numStr =(String) job.getString("num");
			Integer num = Integer.valueOf(numStr.trim());
			String price = (String) job.get("price");
			price = price.trim();
			String amount = (String) job.get("amount");	// 销售总金额
			amount = amount.trim();
			String brandName = (String) job.get("brandName");
			String taxRate = (String) job.get("taxRate");
			taxRate = taxRate.trim();
			if(StringUtils.isBlank(taxRate)) {
				throw new RuntimeException("商品货号："+goodsNo+",税率为空");
			}
			String tax = (String) job.get("tax");
			tax = tax.trim();
			String taxPrice = (String) job.get("taxPrice");
			taxPrice = taxPrice.trim();
			String taxAmount = (String) job.get("taxAmount");
			taxAmount = taxAmount.trim();

			totalAmount = totalAmount.add(new BigDecimal(amount));
			//注入数据
			PreSaleOrderItemModel itemModel = new PreSaleOrderItemModel();
			itemModel.setBarcode(barcode);
			itemModel.setGoodsCode(goodsCode);
			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsName(goodsName);
			itemModel.setGoodsNo(goodsNo);
			itemModel.setNum(num);
			itemModel.setBrandName(brandName);
			itemModel.setPrice(new BigDecimal(price));
			itemModel.setAmount(new BigDecimal(amount));
			itemModel.setCreateDate(TimeUtils.getNow());
			itemModel.setTaxRate(Double.valueOf(taxRate));
			itemModel.setTax(new BigDecimal(tax));
			itemModel.setTaxPrice(new BigDecimal(taxPrice));
			itemModel.setTaxAmount(new BigDecimal(taxAmount));

			//获取商品信息
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(paramMap);
			if(merchandise==null){	// 商品不存在保存失败
				throw new RuntimeException("修改失败,没有查询到商品信息");
			}
			itemModel.setCommbarcode(merchandise.getCommbarcode());	// 标准条码
			if(job.containsKey("id") && StringUtils.isNotBlank(job.getString("id"))){//修改
				Long itemId = Long.valueOf(job.getString("id"));
				itemModel.setId(itemId);
				preSaleOrderItemDao.modify(itemModel);
				itemIds.add(itemId);
			}else{//新增
				itemModel.setOrderId(orderId);
				Long itemId = preSaleOrderItemDao.save(itemModel);
				itemIds.add(itemId);
			}
		}
		//根据表头Id删除不要的数据（除itemIds之外的数据）
		// 根据表头Id查询的数据（除itemIds之外的数据）
		List<PreSaleOrderItemModel> reportDelItemList = preSaleOrderItemDao.getListByBesidesIds(itemIds,orderId);
		List<Long> reportIds =new ArrayList<>();
		for (PreSaleOrderItemModel preSaleOrderItemModel : reportDelItemList) {
			reportIds.add(preSaleOrderItemModel.getId());
		}
		// 批量删除数据,根据表头ID删除表体
		preSaleOrderItemDao.delBesidesIds(itemIds,orderId);
		// 修改表头
		int id = preSaleOrderDao.modify(preSaleOrderModel);

		if(id == 0){
			return "修改失败";
		}
		// 审核
		Map<String,Object> msgMap = auditPreSaleOrder(preSaleOrderModel, userId, userName);
		String errorMsg = (String)msgMap.get("msg");
		if(StringUtils.isNotBlank(errorMsg)){	// 如果有错误信息
			return errorMsg;
		}
		return "修改并审核成功";
	}

	/**
	 * 校验预售单能否转成销售单
	 * @param ids
	 * @param codes
	 * @return
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	@Override
	public List<PreSaleOrderCorrelationDTO> checkPreSale(String ids,String codes) throws RuntimeException, SQLException {
		List<PreSaleOrderCorrelationDTO> correlationList = new ArrayList<>();	// 勾稽集合
		Set<String> checkSet = new HashSet<>();
		Map<String,Integer> preNumMap = new HashMap<>();//key:商品ID+单价 value:预售数量
		Map<String,Integer> saleNumMap = new HashMap<>();//key:商品ID+单价 value:销售数量
		Map<String,Integer> staySaleNumMap = new HashMap<>();//key:商品ID+单价 value:待销量
		List<PreSaleOrderItemModel> preSaleOrderItemList = new ArrayList<>();// 预售单商品
		Set<String> goodsSet = new HashSet<>();
		String[] idArr =null;
		int type=0;//1:传的预售单id 2:传的预售单单号
		if(StringUtils.isNotBlank(ids)){
			idArr = ids.split(",");
			type=1;
		}else{
			idArr = codes.split(",");
			type=2;
		}
		String poList="";
		Long customerId=null;
		Long outDepotId=null;
		Long buId=null;
		String businessModel=null;
		String preSaleOrderCodeList="";
		Long merchantId=null;
		String merchantName="";
		for (int i = 0; i < idArr.length; i++) {
			PreSaleOrderModel preSaleOrderModel = new PreSaleOrderModel();
			if(type==1){//1:传的预售单id 2:传的预售单单号
				preSaleOrderModel = preSaleOrderDao.searchById(Long.parseLong(idArr[i]));
			}else if(type==2){
				PreSaleOrderModel paramModel = new PreSaleOrderModel();
				paramModel.setCode(idArr[i]);
				preSaleOrderModel = preSaleOrderDao.searchByModel(paramModel);
			}

			if(!DERP_ORDER.PRESALEORDER_STATE_003.equals(preSaleOrderModel.getState())){
				throw new RuntimeException("订单状态不为已审核，不能预售转销");
			}
			// 校验是否相同“客户+公司+销售类型+出仓仓库+事业部+理货单位（香港仓需要）”，若否则弹窗报错预警，不允许预售转销
			DepotInfoMongo outDepot = depotInfoService.getDetails(preSaleOrderModel.getOutDepotId());
			String key=null;
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())){// 海外仓
				key = preSaleOrderModel.getCustomerId()+preSaleOrderModel.getBusinessModel()+preSaleOrderModel.getOutDepotId()+
						preSaleOrderModel.getBuId()+preSaleOrderModel.getTallyingUnit();
			}else{
				key = preSaleOrderModel.getCustomerId()+preSaleOrderModel.getBusinessModel()+preSaleOrderModel.getOutDepotId()+
						preSaleOrderModel.getBuId();
			}
			if(i==0){
				checkSet.add(key);
			}
			if(!checkSet.contains(key)){
				throw new RuntimeException("仅同客户同出仓仓库同销售类型同事业部，才能预售转销");
			}
			buId = preSaleOrderModel.getBuId();
			customerId = preSaleOrderModel.getCustomerId();
			outDepotId=preSaleOrderModel.getOutDepotId();
			businessModel=preSaleOrderModel.getBusinessModel();
			poList += preSaleOrderModel.getPoNo()+"&";
			preSaleOrderCodeList += preSaleOrderModel.getCode()+",";
			merchantId=preSaleOrderModel.getMerchantId();
			merchantName=preSaleOrderModel.getMerchantName();

			PreSaleOrderItemModel preSaleOrderItemModel =new PreSaleOrderItemModel();
			preSaleOrderItemModel.setOrderId(preSaleOrderModel.getId());
			List<PreSaleOrderItemModel> itemList = preSaleOrderItemDao.list(preSaleOrderItemModel);
			int num=0;	// 记录该预售单下待销量为0的次数
			for (int k = 0; k < itemList.size(); k++) {
				PreSaleOrderItemModel itemModel = itemList.get(k);
				String priceStr = String.valueOf(itemModel.getPrice());
				String goodsIdStr = String.valueOf(itemModel.getGoodsId());
				String goodsKey = goodsIdStr+priceStr;
				// 预售单数量
				Integer preNumInt = preSaleOrderCorrelationDao.getPreNumByPreSaleId(Arrays.asList(preSaleOrderModel.getCode()), itemModel.getGoodsId());
				Integer preNum = preNumInt==null?0:preNumInt;
				// 销售量
				Integer saleNumInt = preSaleOrderCorrelationDao.getSaleNumByPreSaleId(Arrays.asList(preSaleOrderModel.getCode()), itemModel.getGoodsId());
				Integer saleNum = saleNumInt==null?0:saleNumInt;
				// 待销量
				Integer staySaleNum = preNum-saleNum;
				if(staySaleNum==0){
					num=num+1;
				}
				if(num==itemList.size()){
					throw new RuntimeException("预售单："+preSaleOrderModel.getCode()+"下所有商品的待销量全为0，不能再预售转销了");
				}
				if(staySaleNum!=0){//待销量不为0，才显示到弹框
					if(!goodsSet.contains(goodsKey)){
						preSaleOrderItemList.add(itemModel);
						goodsSet.add(goodsKey);
					}
				}
				if(!preNumMap.containsKey(goodsKey)){
					preNumMap.put(goodsKey,preNum);
				}else{
					preNumMap.put(goodsKey,preNumMap.get(goodsKey)+preNum);
				}

				if(!saleNumMap.containsKey(goodsKey)){
					saleNumMap.put(goodsKey,saleNum);
				}else {
					saleNumMap.put(goodsKey,saleNumMap.get(goodsKey)+saleNum);
				}
				if(!staySaleNumMap.containsKey(goodsKey)){
					staySaleNumMap.put(goodsKey,staySaleNum);
				}else {
					staySaleNumMap.put(goodsKey,staySaleNumMap.get(goodsKey)+staySaleNum);
				}
			}
		}

		String[] poSplit = poList.split("&");
		HashSet<String> poSet = new HashSet<>();
		for (String po:poSplit) {
			poSet.add(po);
		}

		// 去掉重复PO
		String poStr="";
		for (String po:poSet) {
			poStr+=po+"&";
		}
		String poLastStr = poStr.substring(poStr.length() - 1);
		String poNo=null;
		if(poLastStr.equals("&")){
			int index = poStr.lastIndexOf("&");
			poNo = poStr.substring(0, index);
		}
		// 所有预售单相同商品合并
		for (int i = 0; i < preSaleOrderItemList.size(); i++) {
			PreSaleOrderItemModel itemModel = preSaleOrderItemList.get(i);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchandiseId", itemModel.getGoodsId());// 商品id
			MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(params);

			String priceStr = String.valueOf(itemModel.getPrice());
			String goodsIdStr = String.valueOf(itemModel.getGoodsId());
			String goodsKey = goodsIdStr+priceStr;
			// 预售勾稽
			PreSaleOrderCorrelationDTO correlationDTO = new PreSaleOrderCorrelationDTO();
			correlationDTO.setGrossWeight(merchandiseInfoMogo.getGrossWeight());
			correlationDTO.setNetWeight(merchandiseInfoMogo.getNetWeight());
			correlationDTO.setMerchantId(merchantId);
			correlationDTO.setMerchantName(merchantName);
			correlationDTO.setBuId(buId);
			correlationDTO.setCustomerId(customerId);
			correlationDTO.setOutDepotId(outDepotId);
			correlationDTO.setBusinessModel(businessModel);
			correlationDTO.setPoNo(poNo);// 多个PO
			String preSaleOrderCodeLastStr = preSaleOrderCodeList.substring(preSaleOrderCodeList.length() - 1);
			String preSaleOrderCode=null;
			if(preSaleOrderCodeLastStr.equals(",")){
				int index = preSaleOrderCodeList.lastIndexOf(",");
				preSaleOrderCode = preSaleOrderCodeList.substring(0, index);
			}
			correlationDTO.setPreSaleOrderCode(preSaleOrderCode);// 多个预售单
			correlationDTO.setGoodsId(itemModel.getGoodsId());
			correlationDTO.setGoodsName(itemModel.getGoodsName());
			correlationDTO.setGoodsNo(itemModel.getGoodsNo());
			correlationDTO.setGoodsCode(itemModel.getGoodsCode());
			correlationDTO.setBarcode(itemModel.getBarcode());
			correlationDTO.setCommbarcode(itemModel.getCommbarcode());
			correlationDTO.setPrice(itemModel.getPrice());
			correlationDTO.setBrandName(itemModel.getBrandName());//品牌类型

			if(preNumMap.containsKey(goodsKey)){
				correlationDTO.setPreNum(preNumMap.get(goodsKey));
			}
			if(saleNumMap.containsKey(goodsKey)){
				correlationDTO.setSaleNum(saleNumMap.get(goodsKey));
			}
			if(staySaleNumMap.containsKey(goodsKey)){
				correlationDTO.setStaySaleNum(staySaleNumMap.get(goodsKey));
			}
			correlationList.add(correlationDTO);
		}
		return correlationList;
	}

	@Override
	public PreSaleOrderDTO checkOrderState(Long id) throws Exception {
		PreSaleOrderDTO preSaleOrderDto =preSaleOrderDao.searchDTOById(id);
		if(!DERP_ORDER.PRESALEORDER_STATE_003.equals(preSaleOrderDto.getState())){
			throw new RuntimeException(preSaleOrderDto.getCode() + "订单状态不为已审核，不能生成采购订单");
		}
		
		PreSaleOrderItemDTO itemDto = new PreSaleOrderItemDTO();
		itemDto.setOrderId(id);
		List<PreSaleOrderItemDTO> itemList = preSaleOrderItemDao.listPreSaleOrderItemDTO(itemDto);
		preSaleOrderDto.setItemList(itemList);		
		
		return preSaleOrderDto;
	}
	/**
	 * 导入预售单
	 */
	@Override
	public Map saveImportPreSaleOrder(Map<Integer, List<List<Object>>> data, Long userId, String name, Long merchantId,
							   String merchantName, String topidealCode,String relMerchantIds) throws SQLException {
		int success = 0;
		int pass = 0;
		int failure = 0;
		String contractNo="";	// 合同号
		List<Map<String,String>> msgList = new ArrayList<Map<String,String>>();
		//ps:序号是表头与表体关联的标识    序号与表头是1对1，表头与表体是1对多
		Map<String,PreSaleOrderModel> modelMap = new HashMap<String,PreSaleOrderModel>();
		Map<String,List<PreSaleOrderItemModel>> itemMap= new HashMap<String,List<PreSaleOrderItemModel>>();
		// 检查某个预售单编号+该仓库公司关联的信息
		Map<String , DepotMerchantRelMongo> checkGoodsNoByDepotMap = new HashMap<>();
		// 检查某个预售单序号+该销售订单下的商品是否重复
		Set<String> checkGoodsNoBySalecodeSet = new HashSet<>();
		// 香港仓某个预售单的理货单位
		Map<String , String> tallyingUnitMap = new HashMap<>();
		//出仓仓库信息
		DepotInfoMongo outDepotInfo =null;
		//客户信息
		CustomerMerchantRelMongo customerRel  = null;
		for (int i = 0; i < 1; i++) {//表头
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				try{
					Map<String,String> msg = new HashMap<String,String>();
					Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
					String index = map.get("序号");
					if(index == null || "".equals(index)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "序号不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String code = map.get("客户编码");
					if(code == null || "".equals(code)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "客户编码不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					//获取客户信息
					Map<String, Object> params2 = new HashMap<String, Object>();
					params2.put("code", code);
					params2.put("merchantId", merchantId) ;
					customerRel = customerMerchantRelMongoDao.findOne(params2);
					if(customerRel == null){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "客户信息不存在");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					params2.clear();
					params2.put("customerid", customerRel.getCustomerId());
					CustomerInfoMogo customer = customerInfoMongoDao.findOne(params2);// 客户信息

					String businessModel = map.get("销售类型");
					if(businessModel == null || "".equals(businessModel)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "销售类型不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					businessModel = businessModel.trim();
					if("购销-整批结算".equals(businessModel)){
						businessModel = "1";
					}else if("购销-实销实结".equals(businessModel)){
						businessModel = "4";
					}else{
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "请输入正确的销售类型");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String outDepotCode = map.get("出库仓库自编码");
					if(outDepotCode == null || "".equals(outDepotCode)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "出库仓库自编码不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					//获取出仓仓库信息
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("depotCode", outDepotCode);
					outDepotInfo = depotInfoMongoDao.findOne(params);
					if(null == outDepotInfo){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "出仓仓库不存在");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					// 仓库公司关联表
					Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
					depotMerchantRelParam.put("merchantId", merchantId);
					depotMerchantRelParam.put("depotId", outDepotInfo.getDepotId());
					DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
					if(depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "仓库ID为："+outDepotInfo.getDepotId()+",未查到该公司下调出仓库信息");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String key=index;
					if(!checkGoodsNoByDepotMap.containsKey(key)){
						checkGoodsNoByDepotMap.put(key, depotMerchantRelMongo);
					}
					String  buCode= map.get("事业部");
					if(buCode == null || "".equals(buCode)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "事业部不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					// 获取该事业部信息
					Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
					merchantBuRelParam.put("merchantId", merchantId);
					merchantBuRelParam.put("buCode", buCode);
					merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
					MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
					if(merchantBuRelMongo == null || "".equals(merchantBuRelMongo)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "事业部编码为："+buCode+",未查到该公司下事业部信息");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					// 校验公司-仓库-事业部的关联表
					Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
					merchantDepotBuRelParam.put("merchantId", merchantId);
					merchantDepotBuRelParam.put("depotId", outDepotInfo.getDepotId());	// 出仓仓库
					merchantDepotBuRelParam.put("buId", merchantBuRelMongo.getBuId());
					MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
					if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "事业部编码为："+merchantBuRelMongo.getBuCode()+",出仓仓库："+outDepotCode+",未查到该公司仓库事业部关联信息");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					// 校验事业部与当前账号所绑定的事业部是否匹配
					boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(userId, merchantBuRelMongo.getBuId());
					if(!userRelateBu){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "事业部编码为："+merchantBuRelMongo.getBuCode()+",用户id："+userId+",无权限操作该事业部");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String poNo= map.get("PO号");
					if(buCode == null || "".equals(buCode)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "PO号不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String currency= map.get("销售币种");
					if(currency == null || "".equals(currency)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "销售币种不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}else{
						String currencyLabel = DERP_ORDER.getLabelByKey(DERP.currencyCodeList, currency);
						if(StringUtils.isBlank(currencyLabel)){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "销售币种输入有误");
							msgList.add(msg);
							failure+=1;
							continue;
						}
					}
					//理货单位校验
					String tallyingUnit = map.get("海外理货单位");
					if (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepotInfo.getType())) {
						if (StringUtils.isBlank(tallyingUnit)) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "出库仓海外仓时理货单位必填");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						if (!("00".equals(tallyingUnit)||"01".equals(tallyingUnit)||"02".equals(tallyingUnit))) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "理货单位值不正确");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						if(!tallyingUnitMap.containsKey(key)){
							tallyingUnitMap.put(key, tallyingUnit);
						}
					}else{
						if(StringUtils.isNotBlank(tallyingUnit)){
							if (!("00".equals(tallyingUnit)||"01".equals(tallyingUnit)||"02".equals(tallyingUnit))) {
								msg.put("row", String.valueOf(j+1));
								msg.put("msg", "理货单位值不正确");
								msgList.add(msg);
								failure+=1;
								continue;
							}
						}
					}
					
					String remark = map.get("备注");
					if (modelMap.containsKey(index)) {
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "表头序号："+index+"出现重复");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					//注入数据
					PreSaleOrderModel preSaleOrderModel = new PreSaleOrderModel();
					preSaleOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSD));
					preSaleOrderModel.setState(DERP_ORDER.PRESALEORDER_STATE_001);//001-待审核
					preSaleOrderModel.setMerchantId(merchantId);
					preSaleOrderModel.setMerchantName(merchantName);
					preSaleOrderModel.setCustomerId(customerRel.getCustomerId());
					preSaleOrderModel.setCustomerName(customer.getName());
					preSaleOrderModel.setBusinessModel(businessModel);
					preSaleOrderModel.setOutDepotId(outDepotInfo.getDepotId());
					preSaleOrderModel.setOutDepotName(outDepotInfo.getName());
					preSaleOrderModel.setBuId(merchantBuRelMongo.getBuId());
					preSaleOrderModel.setBuName(merchantBuRelMongo.getBuName());
					preSaleOrderModel.setPoNo(poNo);
					preSaleOrderModel.setCurrency(currency);
					preSaleOrderModel.setTallyingUnit(tallyingUnit);
					preSaleOrderModel.setRemark(remark);
					preSaleOrderModel.setCreater(userId);
					preSaleOrderModel.setCreateName(name);

					modelMap.put(index, preSaleOrderModel);
				}catch (Exception e) {
					e.printStackTrace();
					failure+=1;
					continue;
				}
			}
		}
		if(failure == 0){
			for (int i = 1; i < 2; i++) {//表体
				List<List<Object>> objects = data.get(i);
				for (int j = 1; j < objects.size(); j++) {
					try{
						Map<String,String> msg = new HashMap<String,String>();
						Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
						String index = map.get("序号");
						if(index == null || "".equals(index)){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "序号不能为空");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						String goodsNo = map.get("商品货号");
						if(goodsNo == null || "".equals(goodsNo)){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "商品货号不能为空");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						goodsNo=goodsNo.trim();
						// 判断是否有相同序号号+商品货号
						String isKey=index+goodsNo;
						if(!checkGoodsNoBySalecodeSet.contains(isKey)){
							checkGoodsNoBySalecodeSet.add(isKey);
						}else{
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "序号:"+index+"，商品货号:"+goodsNo+"有多条数据,合并后导入");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						String goodsName = map.get("商品名称");//非必填
						String num = map.get("预售数量");
						if(num == null || "".equals(num)){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "销售数量不能为空");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						num=num.trim();
						String amount = map.get("预售金额（不含税）");
						if(amount == null || "".equals(amount)){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "预售金额（不含税）不能为空");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						amount=amount.trim();
						String taxRateStr = map.get("税率");
						//注入数据
						PreSaleOrderItemModel preSaleOrderItemModel = new PreSaleOrderItemModel();

						//获取商品信息
						Map<String, Object> params1 = new HashMap<String,Object>();
						params1.put("goodsNo", goodsNo);
						params1.put("merchantId", merchantId);
						MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(params1);
						if(goods == null){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "自编销售订单号:"+index+"，商品货号:"+goodsNo+"，该商品不存在");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						/**
						 *（1）当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品为该公司下仅为备案商品
						 *（2）当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号；
						 *（3）当出库仓库在该公司下的“选品限制”为“无限制”时，可选的商品为该公司下所有的商品货号信息；
						 */
						String key=index;
						if(checkGoodsNoByDepotMap.containsKey(key)){
							DepotMerchantRelMongo depotMerchantRelMongo = checkGoodsNoByDepotMap.get(index);
							// （1）当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品为该公司下仅为备案商品
							if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRelMongo.getProductRestriction())){
								if(!DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(goods.getIsRecord())){
									msg.put("row", String.valueOf(j+1));
									msg.put("msg", "找不到该商品货号"+goods.getGoodsNo()+"，当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品必须是该公司下的备案商品");
									msgList.add(msg);
									failure+=1;
									continue;
								}
							}else if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRelMongo.getProductRestriction())){
								// （2）当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号；
								if(!DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0.equals(goods.getOutDepotFlag())){
									msg.put("row", String.valueOf(j+1));
									msg.put("msg", "找不到该商品货号"+goods.getGoodsNo()+"，当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号");
									msgList.add(msg);
									failure+=1;
									continue;
								}
							}
						}
						if (!modelMap.containsKey(index)) {
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "序号:"+index+"在表头不存在");
							msgList.add(msg);
							failure+=1;
							continue;
						}
						if(goods != null){
							Map<String, Object> params3 = new HashMap<String,Object>();
							params3.put("brandId", goods.getBrandId());
							BrandMongo brandMongo = brandMongoDao.findOne(params3);
							if(brandMongo != null){
								preSaleOrderItemModel.setBrandName(brandMongo.getName());
							}
						}						
						preSaleOrderItemModel.setCommbarcode(goods.getCommbarcode());// 标准条码
						preSaleOrderItemModel.setBarcode(goods.getBarcode());
						preSaleOrderItemModel.setGoodsCode(goods.getCode());
						preSaleOrderItemModel.setGoodsId(goods.getMerchandiseId());
						preSaleOrderItemModel.setGoodsName(goods.getName());
						preSaleOrderItemModel.setGoodsNo(goods.getGoodsNo());
						preSaleOrderItemModel.setNum(Integer.valueOf(num));
						preSaleOrderItemModel.setPrice(new BigDecimal(amount).divide(new BigDecimal(num),8,BigDecimal.ROUND_HALF_UP));
						preSaleOrderItemModel.setAmount(new BigDecimal(amount));
						//税率 导入时取导入模板的值（税率需存在税率表中），若导入模板中的税率为空则取客户档案中维护的税率值；
						//若客户档案维护值为空，则默认取税率表中的0；
						Double taxRate = 0.0;
						if(StringUtils.isBlank(taxRateStr)) {
							taxRate = customerRel.getRate() == null ? 0.0 : customerRel.getRate().doubleValue();
						}else {							
							taxRate = Double.valueOf(taxRateStr);
						}
						preSaleOrderItemModel.setTaxRate(taxRate);
						//税额=税率*预售总金额（不含税），税额保留2位小数
						BigDecimal tax = preSaleOrderItemModel.getAmount().multiply(new BigDecimal(taxRate)).setScale(2,BigDecimal.ROUND_HALF_UP);
						preSaleOrderItemModel.setTax(tax);
						//预售总金额（含税）=预售总金额（不含税）+税额
						BigDecimal taxAmount = preSaleOrderItemModel.getAmount().add(tax);
						preSaleOrderItemModel.setTaxAmount(taxAmount);
						//预售单价（含税）=预售总金额（含税）/预售数量，单价保留8位小数
						BigDecimal taxPrice = taxAmount.divide(new BigDecimal(preSaleOrderItemModel.getNum()) , 8, BigDecimal.ROUND_HALF_UP);
						preSaleOrderItemModel.setTaxPrice(taxPrice);
						
						//记录表体信息
						List<PreSaleOrderItemModel> itemModelList = new ArrayList<PreSaleOrderItemModel>();
						if(itemMap.containsKey(index)){
							itemModelList = itemMap.get(index);
						}
						itemModelList.add(preSaleOrderItemModel);
						itemMap.put(index, itemModelList);
					} catch(Exception e){
						e.printStackTrace();
						failure+=1;
						continue;
					}
				}
			}
		}
		//保存数据
		for(Map.Entry<String, PreSaleOrderModel> entry : modelMap.entrySet()){
			Map<String,String> msg = new HashMap<String,String>();
			String index = entry.getKey();
			PreSaleOrderModel preSaleOrderModel = entry.getValue();
			// 校验表体
			List<PreSaleOrderItemModel> itemList = itemMap.get(index);
			if(itemList == null || itemList.size() == 0){
				msg.put("row", "");
				msg.put("msg", "序号："+index+"的商品信息为空");
				msgList.add(msg);
				failure+=1;
				continue;
			}
			if(failure==0){
				preSaleOrderModel.setCreateDate(TimeUtils.getNow());
				preSaleOrderDao.save(preSaleOrderModel);
				for(PreSaleOrderItemModel item:itemList){
					item.setCreateDate(TimeUtils.getNow());
					item.setOrderId(preSaleOrderModel.getId());
					preSaleOrderItemDao.save(item);
					success++;
				}
			}
		}
		Map map =new HashMap();
		map.put("success",success);
		map.put("pass",pass);
		map.put("failure",failure);
		map.put("msgList",msgList);
		return map;
	}

	/**
	 * 把两个数组组成一个map
	 * @param keys   键数组
	 * @param values 值数组
	 * @return 键值对应的map
	 */
	private Map<String, String> toMap(Object[] keys, Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				map.put((String) keys[i], values[i].toString());
			}
			return map;
		}
		return null;
	}
	/**
	 * 根据“供应商-公司-客户”查询是否存在加价比例设置
	 * @param preSaleIds
	 * @param supplierId
	 * @return
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> checkPurchaseCommission(String preSaleIds,Long supplierId,User user) throws RuntimeException, SQLException {
		Map<String, Object> resultMap = new HashMap<>();
		Set<Long> checkCustomerSet = new HashSet<>();
		String[] idArr = preSaleIds.split(",");
		int isSameCustomer=0;// 0：客户不同 1：客户相同
		Long customerId=null;

		for (int i = 0; i < idArr.length; i++) {
			PreSaleOrderModel preSaleOrderModel = preSaleOrderDao.searchById(Long.parseLong(idArr[i]));
			if(checkCustomerSet.contains(preSaleOrderModel.getCustomerId())){
				isSameCustomer=1;
			}else{
				isSameCustomer=0;
				customerId = preSaleOrderModel.getCustomerId();
				checkCustomerSet.add(customerId);
			}
		}
		if(idArr.length==1){
			isSameCustomer=1;
		}
		if(isSameCustomer==1){
			// “供应商-公司-客户”查询是否存在加价比例设置
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchantId", user.getMerchantId());
			params.put("configType", DERP_SYS.PURCHASE_COMMISSION_CONFIGTYPE_2);//2-公司加价比例
			params.put("customerId", customerId);
			params.put("supplierId", supplierId);
			PurchaseCommissionMongo purchaseCommissionMongo = purchaseCommissionMongoDao.findOne(params);
			if(purchaseCommissionMongo!=null){ // 存在配置
				resultMap.put("purchaseCommission",purchaseCommissionMongo.getPurchaseCommission());
			}else{
				resultMap.put("purchaseCommission",0);
			}
		}else{
			resultMap.put("purchaseCommission",0);
		}
		return resultMap;
	}

	@Override
	public Map<String, String> checkExistPurchaseByPO(String poNo, User user) throws RuntimeException, SQLException {
		Map<String,String> result = new HashMap<String, String>();
		PurchaseOrderExportDTO  purchaseModel = new PurchaseOrderExportDTO();
		purchaseModel.setMerchantId(user.getMerchantId());
		purchaseModel.setPoNo(poNo);
		List<PurchaseOrderExportDTO> purList = purchaseOrderDao.getDetailsByExport(purchaseModel);
		if(purList != null && purList.size() > 0) {
			result.put("code", "01");
			return result;
		}			
		result.put("code", "00");
		return result;
	}

	@Override
	public Long GeneratePurchaseOrder(String json, User user) throws Exception {
		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		Long saleOrderId = Long.valueOf(jsonObj.getString("saleOrderId"));
		Long supplierId = Long.valueOf(jsonObj.getString("supplierId"));
		String poNo = (String) jsonObj.get("poNo");
		
		Map<String, Object> customer_params = new HashMap<String, Object>();
		customer_params.put("customerid", supplierId);
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(customer_params);
		if (customer == null) {
			throw new RuntimeException("供应商不存在");
		}
		
		PreSaleOrderModel preSaleModel = preSaleOrderDao.searchById(Long.valueOf(saleOrderId));
		if(preSaleModel == null) {
			throw new RuntimeException("预售订单不存在");
		}
				
		PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
		purchaseOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGO));
		purchaseOrderModel.setSupplierId(supplierId);
		purchaseOrderModel.setSupplierName(customer.getName());
		purchaseOrderModel.setMerchantId(preSaleModel.getMerchantId());
		purchaseOrderModel.setMerchantName(preSaleModel.getMerchantName());
		purchaseOrderModel.setPoNo(poNo);		
		purchaseOrderModel.setDepotId(preSaleModel.getOutDepotId());
		purchaseOrderModel.setDepotName(preSaleModel.getOutDepotName());		
		purchaseOrderModel.setBuId(preSaleModel.getBuId());
		purchaseOrderModel.setBuName(preSaleModel.getBuName());
		purchaseOrderModel.setBusinessModel(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_1);//默认 购销		
		purchaseOrderModel.setCurrency(preSaleModel.getCurrency());
		purchaseOrderModel.setTallyingUnit(preSaleModel.getTallyingUnit());
		purchaseOrderModel.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_001);		
		purchaseOrderModel.setTopidealCode(user.getTopidealCode());
		purchaseOrderModel.setCreater(user.getId());
		purchaseOrderModel.setCreateName(user.getName());
		purchaseOrderModel.setCreateDate(TimeUtils.getNow());
		purchaseOrderModel.setAmountAdjustmentStatus(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0);
		
		
		//解析表体数据
		Double grossWeightTotal = 0.0;
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		List<PurchaseOrderItemModel> itemList = new ArrayList<PurchaseOrderItemModel>();
		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			if (job.isNullObject() || job.isEmpty()) {
				continue;
			}
			
			Long id = Long.valueOf(job.getString("id"));
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsNo = (String) job.get("goodsNo");
			String numStr = (String) job.getString("num");
			Integer num = Integer.valueOf(numStr.trim());
			String price = (String) job.get("price");
			price = price.trim();
			String amount = (String) job.get("amount");    // 销售总金额
			amount = amount.trim();
			
			PreSaleOrderItemModel preItemModel = preSaleOrderItemDao.searchById(id);
			if(preItemModel == null) {
				throw new RuntimeException("商品货号："+goodsNo+"，在预售订单中不存在");
			}
			
			//根据商品id获取商品信息
			Map<String, Object> merchandiseParams = new HashMap<String, Object>();
			merchandiseParams.put("merchandiseId", preItemModel.getGoodsId());
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseParams);			
			
			Double grossWeight = merchandise.getGrossWeight() == null ? 0.0 : merchandise.getGrossWeight();
			Double netWeight = merchandise.getNetWeight() == null ? 0.0 : merchandise.getNetWeight();
			Double grossWeightSum = new BigDecimal(grossWeight).multiply(new BigDecimal(num)).doubleValue();
			Double netWeightSum = new BigDecimal(netWeight).multiply(new BigDecimal(num)).doubleValue();
			
			//注入数据
			PurchaseOrderItemModel itemModel = new PurchaseOrderItemModel();
			itemModel.setBarcode(preItemModel.getBarcode());
			itemModel.setGoodsCode(preItemModel.getGoodsCode());
			itemModel.setGoodsId(goodsId);
			itemModel.setGoodsName(preItemModel.getGoodsName());
			itemModel.setGoodsNo(preItemModel.getGoodsNo());
			itemModel.setNum(num);			
			itemModel.setBrandName(preItemModel.getBrandName());
			itemModel.setPrice(new BigDecimal(price));
			itemModel.setAmount(new BigDecimal(amount));			
			itemModel.setCreater(user.getId());
			itemModel.setCreateName(user.getName());
			itemModel.setCreateDate(TimeUtils.getNow());
			itemList.add(itemModel);
			itemModel.setNetWeight(netWeight);
			itemModel.setGrossWeight(grossWeight);
			itemModel.setNetWeightSum(netWeightSum);
			itemModel.setGrossWeightSum(grossWeightSum);
			
			grossWeightTotal = grossWeightTotal + grossWeightSum;
			
		}
		Long id = null;
		if(itemList != null && itemList.size() > 0) {
			purchaseOrderModel.setGrossWeight(grossWeightTotal);
			id = purchaseOrderDao.save(purchaseOrderModel);
			for(PurchaseOrderItemModel item : itemList) {
				item.setPurchaseOrderId(id);
				purchaseOrderItemDao.save(item);
			}
		}
		return id;
	}
	/**
	 * 是否开启价格管理
	 */
	@Override
	public Boolean getPurchasePriceManage(Long id, Long supplierId, User user) throws Exception {
		PreSaleOrderModel preSaleOrderModel = preSaleOrderDao.searchById(id);
		if(preSaleOrderModel == null){
			throw new RuntimeException("预售单不存在");
		}

		/**查询是否开启采购价格管理*/
		boolean purchasePriceManage = false;
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("buId", preSaleOrderModel.getBuId()) ;
		queryMap.put("merchantId", user.getMerchantId()) ;
		queryMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
		MerchantBuRelMongo merchantBuRel = merchantBuRelMongoDao.findOne(queryMap);
		//事业部是否开启
		purchasePriceManage = !org.apache.commons.lang3.StringUtils.isBlank(merchantBuRel.getPurchasePriceManage()) && !DERP_SYS.PURCHASE_PRICE_MANAGE_0.equals(merchantBuRel.getPurchasePriceManage());
		if(purchasePriceManage){
			//供应商是否开启
			queryMap = new HashMap<String, Object>() ;
			queryMap.put("customerId", supplierId) ;
			queryMap.put("merchantId", user.getMerchantId()) ;
			queryMap.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;
			CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(queryMap);

			purchasePriceManage = customerRel != null && DERP_SYS.PURCHASE_PRICE_MANAGE_1.equals(customerRel.getPurchasePriceManage());
		}
		return purchasePriceManage;
	}
	/**
	 * 获取采购价格
	 */
	@Override
	public Map<String, List<BigDecimal>> getPurchasePrice(Long id, Long supplierId , User user) throws Exception {
		Map<String, List<BigDecimal>> supplyPriceMap = new HashMap<String, List<BigDecimal>>();		
		//获取商品信息
		PreSaleOrderModel preSaleOrderModel = preSaleOrderDao.searchById(id);
		if(preSaleOrderModel == null){
			throw new RuntimeException("预售单不存在");
		}
		PreSaleOrderItemModel queryItem = new PreSaleOrderItemModel();
		queryItem.setOrderId(id);
		List<PreSaleOrderItemModel> itemList = preSaleOrderItemDao.list(queryItem);		
		for(PreSaleOrderItemModel item : itemList) {	
			Map<String, Object> smPriceMap = new HashMap<String, Object>() ;
			smPriceMap.put("merchantId", preSaleOrderModel.getMerchantId()) ;
			smPriceMap.put("customerId", supplierId) ;
			smPriceMap.put("commbarcode", item.getCommbarcode()) ;
			smPriceMap.put("buId", preSaleOrderModel.getBuId());
			smPriceMap.put("currency", preSaleOrderModel.getCurrency()) ;
			smPriceMap.put("state", DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003);			
			List<SupplierMerchandisePriceMongo> smList = supplierMerchandisePriceMongoDao.findAll(smPriceMap);
			
			if(smList.isEmpty()) {
				return null;
			}
			
			for (SupplierMerchandisePriceMongo tempMongo : smList) {				
				String effectiveDate = tempMongo.getEffectiveDate();
				String expiryDate = tempMongo.getExpiryDate();
				//是否在有效期内
				if(TimeUtils.parseFullTime(effectiveDate + " 00:00:00").getTime() <= TimeUtils.getNow().getTime()
						&& TimeUtils.parseFullTime(expiryDate + " 23:59:59").getTime() >= TimeUtils.getNow().getTime()) { 
					BigDecimal supplyPrice = tempMongo.getSupplyPrice();
					// 理货单位不为空，且不为件时，进行箱、件、托转换
					if (StringUtils.isNotBlank(preSaleOrderModel.getTallyingUnit()) && !DERP.ORDER_TALLYINGUNIT_02.equals(preSaleOrderModel.getTallyingUnit())) {
						HashMap<String, Object> merchandiseMap = new HashMap<>();
						merchandiseMap.put("merchandiseId", item.getGoodsId());
						merchandiseMap.put("status", "1");
						
						MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseMap);
						if (DERP.ORDER_TALLYINGUNIT_01.equals(preSaleOrderModel.getTallyingUnit())) {
							if(merchandiseInfo.getBoxToUnit() == null || merchandiseInfo.getBoxToUnit() == 0) {
								throw new RuntimeException( "货号：" + merchandiseInfo.getGoodsNo() + "箱转件未维护");
							}
							supplyPrice = supplyPrice.multiply(new BigDecimal(merchandiseInfo.getBoxToUnit())).setScale(8, BigDecimal.ROUND_HALF_UP);
							
						} else if (DERP.ORDER_TALLYINGUNIT_00.equals(preSaleOrderModel.getTallyingUnit())) {
							if(merchandiseInfo.getTorrToUnit() == null || merchandiseInfo.getTorrToUnit() == 0) {
								throw new RuntimeException( "货号：" + merchandiseInfo.getGoodsNo() + "托转件未维护");
							}
							supplyPrice = supplyPrice.multiply(new BigDecimal(merchandiseInfo.getTorrToUnit())).setScale(8, BigDecimal.ROUND_HALF_UP);
						}
					}
					List<BigDecimal> priceList = new ArrayList<BigDecimal>();
					if(supplyPriceMap.containsKey(item.getGoodsNo())) {
						priceList =  supplyPriceMap.get(item.getGoodsNo());
					}
					if(!priceList.contains(supplyPrice)) {						
						priceList.add(supplyPrice);
					}
					supplyPriceMap.put(item.getGoodsNo(), priceList);
				}
			}
		}
		return supplyPriceMap;
	}
	
}
