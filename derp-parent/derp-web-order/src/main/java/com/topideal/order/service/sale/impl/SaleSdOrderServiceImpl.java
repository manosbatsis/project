package com.topideal.order.service.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.TobTemporaryReceiveBillDao;
import com.topideal.dao.common.SdSaleTypeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.SaleSdOrderDTO;
import com.topideal.entity.dto.sale.SaleSdOrderItemDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillModel;
import com.topideal.entity.vo.common.SdSaleTypeModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.CommbarcodeMongo;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.sale.SaleSdOrderService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SaleSdOrderServiceImpl implements SaleSdOrderService{
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(SaleSdOrderServiceImpl.class);
	@Autowired
	private SaleSdOrderDao saleSdOrderDao;
	@Autowired
	private SaleSdOrderItemDao saleSdOrderItemDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao ;
	@Autowired
	private CommbarcodeMongoDao commbarcodeMongoDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private ShelfDao shelfDao;
	@Autowired
	private SaleShelfDao saleShelfDao;
	@Autowired
	private SdSaleTypeDao sdSaleTypeDao ;

	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private RMQProducer rocketMQProducer;// MQ
	@Autowired
	private SaleOrderDao saleOrderDao;
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
    private TobTemporaryReceiveBillDao tobTemporaryReceiveBillDao;
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;
	// 销售退货出库单表体
	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;


	/**
	 * 查询列表 分页
	 */
	@Override
	public SaleSdOrderDTO listDTOByPage(SaleSdOrderDTO dto, User user) throws Exception {
		if (dto.getBuId() == null) {
            List<Long> buList = new ArrayList<Long>();
            Map<String, Object> queryMap = new HashMap<String, Object>() ;
            queryMap.put("merchantId", user.getMerchantId()) ;
            List<MerchantBuRelMongo> merchantBuRelList = merchantBuRelMongoDao.findAll(queryMap);
            if(merchantBuRelList != null && merchantBuRelList.size() > 0) {
                for(MerchantBuRelMongo mongo : merchantBuRelList) {
                    buList.add(mongo.getBuId());
                }
            }

            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
		SaleSdOrderDTO resultDTO = saleSdOrderDao.listDTOByPage(dto);
		for(SaleSdOrderDTO saleSdOrder : (List<SaleSdOrderDTO>)resultDTO.getList()) {
			if(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2.equals(saleSdOrder.getOrderType())) {
				BigDecimal amount = saleSdOrder.getTotalSdAmount().multiply(new BigDecimal(-1));
				saleSdOrder.setTotalSdAmount(amount);
			}
		}
		return resultDTO;
	}

	/**
	 * 获取详情
	 */
	@Override
	public SaleSdOrderDTO searchDetail(Long id) throws Exception {
		SaleSdOrderModel model =  saleSdOrderDao.searchById(id);
		SaleSdOrderDTO dto = new SaleSdOrderDTO();
		BeanUtils.copyProperties(model, dto);
		return dto;
	}

	/**
	 * 获取商品sd明细
	 */
	@Override
	public List<SaleSdOrderItemDTO> searchItemDetail(Long sdOrderId, String orderType) throws Exception {
		SaleSdOrderItemDTO model = new SaleSdOrderItemDTO();
		model.setSaleSdOrderId(sdOrderId);
		model.setOrderType(orderType);
		return saleSdOrderItemDao.listDTO(model);
	}

	/**
	 * 获取导出数据
	 */
	@Override
	public List<Map<String, Object>> exportSaleSdOrder(SaleSdOrderDTO dto, User user,String operate) throws Exception {
		if (dto.getBuId() == null) {
            List<Long> buList = new ArrayList<Long>();
            Map<String, Object> queryMap = new HashMap<String, Object>() ;
            queryMap.put("merchantId", user.getMerchantId()) ;
            List<MerchantBuRelMongo> merchantBuRelList = merchantBuRelMongoDao.findAll(queryMap);
            if(merchantBuRelList != null && merchantBuRelList.size() > 0) {
                for(MerchantBuRelMongo mongo : merchantBuRelList) {
                    buList.add(mongo.getBuId());
                }
            }

            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return new ArrayList<Map<String,Object>>();
            }
            dto.setBuList(buList);
        }

		List<Map<String,Object>> mpList = saleSdOrderDao.exportSaleSdOrder(dto);
		if(!"1".equals(operate) && mpList != null && mpList.size() > 0) {
			for(Map<String,Object> map : mpList) {
				String commbarcode = (String) map.get("commbarcode");
				//查询标准品牌名称
				if(StringUtils.isNotBlank(commbarcode)) {
					Map<String,Object> commParam = new HashMap<String, Object>();
					commParam.put("commbarcode", commbarcode);
					CommbarcodeMongo commMongo = commbarcodeMongoDao.findOne(commParam);
					if(commMongo != null) {
						map.put("commBrandParentName", commMongo.getCommBrandParentName());
					}
				}
			}
		}
		return mpList;
	}

	/**
	 * 逻辑删除销售SD单
	 */
	@Override
	public void delSaleSdOrder(String ids, User user) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		for(Long id:idList) {
			SaleSdOrderModel model = saleSdOrderDao.searchById(id);
			if(model.getOrderCode().indexOf("SJD") > -1){
				SaleOrderModel saleOrderModel = saleOrderDao.searchById(model.getBusinessId());
				if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
					throw new RuntimeException("销售SD单：" + model.getCode() + " 对应的销售订单红冲状态不为空，无法删除");
				}
			}
			// 查询最大关账月份
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(model.getMerchantId());
			closeAccountsMongo.setBuId(model.getBuId());
			String maxDate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxDate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (model.getOwnDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				throw new RuntimeException("单据:"+model.getCode()+ "所在归属月份"+TimeUtils.formatMonth(model.getOwnDate())+"已经关账，不能删除");
			}

			model.setState(DERP.DEL_CODE_006);
			saleSdOrderDao.modify(model);
		}

	}

	/**
	 * 编辑
	 */
	@Override
	public String saveSaleSdOrder(SaleSdOrderDTO dto, User user) throws Exception {
		SaleSdOrderModel model = new SaleSdOrderModel();
		model = saleSdOrderDao.searchById(dto.getId());
		// 查询最大关账月份
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(model.getMerchantId());
		closeAccountsMongo.setBuId(model.getBuId());
		String maxDate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxDate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxDate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth) && model.getOwnDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
			throw new RuntimeException("单据:"+model.getCode()+ "所在归属月份"+TimeUtils.formatMonth(model.getOwnDate())+"已经关账，不能修改");
		}
		BigDecimal totalAmount = BigDecimal.ZERO;
		for(SaleSdOrderItemDTO itemDTO: dto.getItemList()) {//上架单
			if(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1.equals(model.getOrderType())) {
				BigDecimal price = itemDTO.getSdAmount().divide(new BigDecimal(itemDTO.getNum()), 8 , BigDecimal.ROUND_HALF_UP);
				itemDTO.setSdPrice(price);
			}else if(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2.equals(model.getOrderType())) {//销售退
				BigDecimal sdAmount = itemDTO.getSdAmount().multiply(new BigDecimal(-1)).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal price = sdAmount.divide(new BigDecimal(itemDTO.getNum()), 8 , BigDecimal.ROUND_HALF_UP);
				itemDTO.setSdPrice(price);
				itemDTO.setSdAmount(sdAmount);
			}

			SaleSdOrderItemModel itemModel = new SaleSdOrderItemModel();
			BeanUtils.copyProperties(itemDTO,itemModel);
			saleSdOrderItemDao.modify(itemModel);

			totalAmount = totalAmount.add(itemModel.getSdAmount());
		}
		model.setTotalSdAmount(totalAmount);
		model.setModifier(user.getId());
		model.setModifiyName(user.getName());
		model.setModifyDate(TimeUtils.getNow());
		saleSdOrderDao.modify(model);

		TobTemporaryReceiveBillModel  billModel = new TobTemporaryReceiveBillModel();
		billModel.setOrderCode(model.getOrderCode());
		List<TobTemporaryReceiveBillModel> billList = tobTemporaryReceiveBillDao.list(billModel);

		boolean flag = true;
		String message = "";
		for(TobTemporaryReceiveBillModel tempBillModel : billList) {
			if(!DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1.equals(tempBillModel.getRebateStatus())) {
				flag = false;
	        	message = "来源单号：" + tempBillModel.getShelfCode() + "的暂估费用状态不为“已上架未核销”，请注意同步更新ToB暂估费用金额";
	        	break;
			}
		}

		if(flag) {
			//更新to B暂估
			Map<String,Object> map = new HashMap<String, Object>();
        	if(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1.equals(model.getOrderType())) {
        		map.put("orderCodes", model.getOrderCode());
        	}else if(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2.equals(model.getOrderType())) {
        		map.put("orderCodes", model.getBusinessCode());
        	}
        	map.put("saleSdCode", model.getCode());
        	map.put("dataSource", model.getOrderType());
			rocketMQProducer.send(JSONObject.fromObject(map).toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
		}
		return message;
	}
	/**
	 * 导入SD单
	 */
	@Override
	public Map<String, Object> importSaleSdOrder(List<List<Map<String, String>>> data, User user) throws Exception {
		//记录导入结果
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		Map<String, SaleSdOrderModel> orderMap = new HashMap<String, SaleSdOrderModel>() ;
		Map<String, List<SaleSdOrderItemModel>> itemMap = new HashMap<String, List<SaleSdOrderItemModel>>() ;
		Map<String, String> orderTypeMap = new HashMap<String, String>() ;//单据类型 1-上架单 2-销售退入库单
		/**表头*/
		List<Map<String, String>> orderData = data.get(0);
		for (int i = 1 ; i <= orderData.size() ; i++) {

			Map<String, String> map = orderData.get(i - 1);
			Long buId =null;
			String buName = "";
			String currency = "";
			Long customerId  = null;
			String customerName = "";
			String poNo = "";
			String orderCode = "";
			Long orderId = null;
			Long businessId = null;
			String businessCode = "";

			String index = map.get("序号");

			if(checkIsNullOrNot(i, index, "序号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			index = index.trim() ;
			if(orderMap.containsKey(index)){
				setErrorMsg(i, "表头信息，序号："+index +" 重复", resultList);
				failure += 1;
				continue;
			}

			String excelOrderCode = map.get("来源单号");
			if (checkIsNullOrNot(i, excelOrderCode, "来源单号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			excelOrderCode = excelOrderCode.trim() ;
			if(excelOrderCode.indexOf("SJD") > -1) {
				ShelfModel shelfModel = new ShelfModel();
				shelfModel.setCode(excelOrderCode);
				shelfModel = shelfDao.searchByModel(shelfModel);
				if(shelfModel == null){
					setErrorMsg(i, "上架单："+excelOrderCode+"不存在", resultList);
					failure += 1;
					continue;
				}
				buId = shelfModel.getBuId();
				buName = shelfModel.getBuName();
				currency = shelfModel.getCurrency();
				customerId  = shelfModel.getCustomerId();
				customerName = shelfModel.getCustomerName();
				poNo = shelfModel.getPoNo();
				orderCode = shelfModel.getCode();
				orderId = shelfModel.getId();
				businessId = shelfModel.getSaleOrderId();
				businessCode = shelfModel.getSaleOrderCode();
				orderTypeMap.put(index, DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1);

				SaleOrderModel saleOrderModel = saleOrderDao.searchById(shelfModel.getSaleOrderId());
				if(StringUtils.isNotBlank(saleOrderModel.getWriteOffStatus())){
					setErrorMsg(i, "上架单："+excelOrderCode+" 对应的销售订单红冲状态不为空", resultList);
					failure += 1;
					continue;
				}

			}
			if(excelOrderCode.indexOf("XST") > -1) {
				SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
				saleReturnIdepotModel.setCode(excelOrderCode);
				saleReturnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);
				if(saleReturnIdepotModel == null){
					setErrorMsg(i, "销售退入库单："+excelOrderCode+"不存在", resultList);
					failure += 1;
					continue;
				}
				SaleReturnOrderModel saleReturnOrder = saleReturnOrderDao.searchById(saleReturnIdepotModel.getOrderId());
				if(!DERP_ORDER.SALERETURNORDER_RETURNMODE_1.equals(saleReturnOrder.getReturnMode())) {
					throw new RuntimeException("销售退订单退货方式不为“退货退款”");
				}

				buId = saleReturnIdepotModel.getBuId();
				buName = saleReturnIdepotModel.getBuName();
				currency = saleReturnOrder.getCurrency();
				customerId  = saleReturnIdepotModel.getCustomerId();
				customerName = saleReturnIdepotModel.getCustomerName();
				poNo = saleReturnOrder.getPoNo();
				orderCode = saleReturnIdepotModel.getCode();
				orderId = saleReturnIdepotModel.getId();
				businessId = saleReturnIdepotModel.getOrderId();
				businessCode = saleReturnIdepotModel.getOrderCode();
				orderTypeMap.put(index, DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2);
			}

			String excelOwnDate = map.get("调整归属日期") ;
			if (checkIsNullOrNot(i, excelOwnDate, "调整归属日期不能为空", resultList)) {
				failure += 1;
				continue;
			}
			excelOwnDate = excelOwnDate.trim() ;

			if(!isValidDate(excelOwnDate)) {
				setErrorMsg(i, "该归属时间格式有误，格式为：yyyy-MM-dd", resultList);
				failure += 1;
				continue;
			}

			if(TimeUtils.daysBetween(TimeUtils.parse(excelOwnDate, "yyyy-MM-dd"), new Date()) < 0) {
				setErrorMsg(i, "归属时间不可超过当前时间", resultList);
				failure += 1;
				continue;
			}

			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setBuId(buId);

			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (Timestamp.valueOf(excelOwnDate + " 00:00:00").getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(i, "归属时间必须大于关账日期", resultList);
					failure += 1;
					continue;
				}
			}

			String sdType = map.get("SD类型") ;
			if (checkIsNullOrNot(i, sdType, "SD类型不能为空", resultList)) {
				failure += 1;
				continue;
			}
			sdType = sdType.trim() ;

			SdSaleTypeModel sdSaleTypeModel = new SdSaleTypeModel() ;

			sdSaleTypeModel.setStatus(DERP_ORDER.SDTYPECONFIG_STATUS_1);
			sdSaleTypeModel.setSdType(sdType);

			sdSaleTypeModel = sdSaleTypeDao.searchByModel(sdSaleTypeModel) ;

			if(sdSaleTypeModel == null) {
				setErrorMsg(i, "SD类型不存在或未启用", resultList);
				failure += 1;
				continue;
			}

			Map<String, Object> merchantMap = new HashMap<String, Object>() ;
			merchantMap.put("merchantId", user.getMerchantId()) ;

			SaleSdOrderModel sdModel = new SaleSdOrderModel() ;
			sdModel.setBuId(buId);
			sdModel.setBuName(buName);
			sdModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSSD));
			sdModel.setCurrency(currency);
			sdModel.setCustomerId(customerId);
			sdModel.setCustomerName(customerName);
			sdModel.setOwnDate(TimeUtils.parse(excelOwnDate, "yyyy-MM-dd"));
			sdModel.setMerchantId(user.getMerchantId());
			sdModel.setMerchantName(user.getMerchantName());
			sdModel.setPoNo(poNo);
			sdModel.setSdTypeId(sdSaleTypeModel.getId());
			sdModel.setSdType(sdSaleTypeModel.getSdType());
			sdModel.setSdTypeName(sdSaleTypeModel.getSdTypeName());
			sdModel.setTotalSdNum(0);
			sdModel.setTotalSdAmount(BigDecimal.ZERO);
			sdModel.setOrderId(orderId);
			sdModel.setOrderCode(orderCode);
			sdModel.setBusinessId(businessId);
			sdModel.setBusinessCode(businessCode);
			sdModel.setCreateDate(TimeUtils.getNow());
			sdModel.setCreateName(user.getName());
			sdModel.setCreater(user.getId());
			sdModel.setState("001");
			sdModel.setOrderSource("2");
			sdModel.setOrderType(orderTypeMap.get(index));

			orderMap.put(index, sdModel) ;
		}

		if(failure < 1) {
			/**表体*/
			List<Map<String, String>> itemData = data.get(1);
			for (int j = 1 ; j <= itemData.size() ; j++) {
				Map<String, String> map = itemData.get(j - 1);
				String index = map.get("序号");
				if(checkIsNullOrNot(j, index, "序号不能为空", resultList)) {
					failure += 1;
					continue;
				}
				index = index.trim() ;

				String goodsNo = map.get("商品货号");
				if(checkIsNullOrNot(j, goodsNo, "商品货号不能为空", resultList)) {
					failure += 1;
					continue;
				}
				goodsNo = goodsNo.trim() ;

				String amountStr = map.get("调整SD金额");
				if(checkIsNullOrNot(j, amountStr, "调整SD金额不能为空", resultList)) {
					failure += 1;
					continue;
				}
				amountStr = amountStr.trim() ;

				if(amountStr.indexOf(".") > -1 && doubleyn(amountStr, 2)) {
					setErrorMsg(j, "调整金额应是2位小数的数字", resultList);
					failure += 1;
					continue;
				}
				BigDecimal sdItemSdAmount = new BigDecimal(amountStr);

				SaleSdOrderModel sdOrder = orderMap.get(index);
				if(sdOrder == null) {
					setErrorMsg(j, "【SD金额调整】序号：" + index + "不存在于【表头信息】", resultList);
					failure += 1;
					continue;
				}
				// 根据商品货号获取商品id
				Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();
				merchandiseInfo_params.put("goodsNo", goodsNo);
				merchandiseInfo_params.put("merchantId", sdOrder.getMerchantId());
				merchandiseInfo_params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

				MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
				if (merchandiseInfo == null) {
					setErrorMsg(j, "商品货号不存在", resultList);
					failure += 1;
					continue;
				}

				Integer num = 0;
				BigDecimal price = BigDecimal.ZERO;
				Long saleItemId = null;
				if(DERP_ORDER.SALE_SD_ORDER_ORDERSOURCE_1.equals(orderTypeMap.get(index))) {
					String salePrice = map.get("销售单价");

					SaleShelfModel saleShelfModel = new SaleShelfModel();
					saleShelfModel.setShelfId(sdOrder.getOrderId());
					saleShelfModel.setGoodsNo(goodsNo);
					List<SaleShelfModel> saleShelfList = saleShelfDao.list(saleShelfModel);
					if(saleShelfList == null || saleShelfList.size() < 1){
						setErrorMsg(j, "商品货号:"+goodsNo+"在上架单："+sdOrder.getOrderCode()+"中不存在", resultList);
						failure += 1;
						continue;
					}

//					if(saleShelfModel.getShelfNum().intValue() < 1){
//						setErrorMsg(j, "商品货号:"+goodsNo+"上架单好品量为0", resultList);
//						failure += 1;
//						continue;
//					}

					String msg = "";
					SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
					saleOrderItemModel.setOrderId(sdOrder.getBusinessId());
					saleOrderItemModel.setGoodsNo(goodsNo);
					if(StringUtils.isNotBlank(salePrice)){
						saleOrderItemModel.setPrice(new BigDecimal(salePrice));
						msg = "，销售价格："+ salePrice ;
					}
					List<SaleOrderItemModel> saleOrderItemList = saleOrderItemDao.list(saleOrderItemModel);
					if(saleOrderItemList == null || saleOrderItemList.size() < 1){
						setErrorMsg(j, "商品货号:"+goodsNo+msg+"在销售订单："+sdOrder.getBusinessCode()+"中不存在", resultList);
						failure += 1;
						continue;
					}

					if(saleOrderItemList.size() > 1 && StringUtils.isBlank(salePrice)){
						setErrorMsg(j, "商品货号:"+goodsNo+msg+"在销售订单："+sdOrder.getBusinessCode()+"存在多条，销售单价不能为空", resultList);
						failure += 1;
						continue;
					}
					if(saleOrderItemList.size() > 1 && StringUtils.isNotBlank(salePrice)){
						setErrorMsg(j, "商品货号:"+goodsNo+msg+"在销售订单："+sdOrder.getBusinessCode()+"存在多条，不允许手工导入", resultList);
						failure += 1;
						continue;
					}
					saleOrderItemModel = saleOrderItemList.get(0);


					price = saleOrderItemModel.getPrice();
					saleItemId = saleOrderItemModel.getId();

					saleShelfModel = new SaleShelfModel();
					saleShelfModel.setShelfId(sdOrder.getOrderId());
					saleShelfModel.setSaleItemId(saleItemId);
					saleShelfList = saleShelfDao.list(saleShelfModel);
					if(saleShelfList == null || saleShelfList.size() < 1){
						setErrorMsg(j, "商品货号:"+goodsNo+msg+"在上架单："+sdOrder.getOrderCode()+"存在多条，不允许手工导入", resultList);
						failure += 1;
						continue;
					}

					if(saleShelfList.get(0).getShelfNum().intValue() < 1){
						setErrorMsg(j, "商品货号:"+goodsNo+msg+"上架单好品量为0", resultList);
						failure += 1;
						continue;
					}
					num = saleShelfList.get(0).getShelfNum();
				}
				if(DERP_ORDER.SALE_SD_ORDER_ORDERSOURCE_2.equals(orderTypeMap.get(index))) {
					SaleReturnIdepotItemModel saleReturnIdepotItem = new SaleReturnIdepotItemModel();
					saleReturnIdepotItem.setSreturnIdepotId(sdOrder.getOrderId());
					saleReturnIdepotItem.setInGoodsNo(goodsNo);
					List<SaleReturnIdepotItemModel> saleReturnIdepotItemList = saleReturnIdepotItemDao.list(saleReturnIdepotItem);
					if(saleReturnIdepotItemList == null || saleReturnIdepotItemList.size() == 0){
						setErrorMsg(j, "商品货号:"+goodsNo+"在退货入库单："+sdOrder.getOrderCode()+"中不存在", resultList);
						failure += 1;
						continue;
					}
					int goodNum = saleReturnIdepotItemList.stream().mapToInt(SaleReturnIdepotItemModel :: getReturnNum).sum();
					int wornNum = saleReturnIdepotItemList.stream().mapToInt(SaleReturnIdepotItemModel :: getWornNum).sum();
					num = goodNum + wornNum;
					if(num < 1){
						setErrorMsg(j, "商品货号:"+goodsNo+"好品量+坏品量为0", resultList);
						failure += 1;
						continue;
					}

					SaleReturnOrderItemModel saleReturnOrderItemModel = new SaleReturnOrderItemModel();
					saleReturnOrderItemModel.setOrderId(sdOrder.getBusinessId());
					saleReturnOrderItemModel.setInGoodsNo(goodsNo);
					List<SaleReturnOrderItemModel> saleReturnOrderItemList = saleReturnOrderItemDao.list(saleReturnOrderItemModel);
					if(saleReturnOrderItemList == null || saleReturnOrderItemList.size() == 0){
						setErrorMsg(j, "商品货号:"+goodsNo+"在销售退货单："+sdOrder.getBusinessCode()+"中不存在", resultList);
						failure += 1;
						continue;
					}
					num = goodNum + wornNum;
					price = saleReturnOrderItemList.get(0).getPrice();
				}

				SaleSdOrderItemModel sdItem = new SaleSdOrderItemModel() ;
				sdItem.setBarcode(merchandiseInfo.getBarcode());
				sdItem.setGoodsId(merchandiseInfo.getMerchandiseId());
				sdItem.setGoodsNo(merchandiseInfo.getGoodsNo());
				sdItem.setGoodsName(merchandiseInfo.getName());
				sdItem.setCommbarcode(merchandiseInfo.getCommbarcode());
				sdItem.setNum(num);
				sdItem.setPrice(price);
				sdItem.setAmount(price.multiply(new BigDecimal(num)).setScale(2 , BigDecimal.ROUND_HALF_UP));
				sdItem.setSdPrice(sdItemSdAmount.divide(new BigDecimal(num) , 8 , BigDecimal.ROUND_HALF_UP));
				sdItem.setSdAmount(sdItemSdAmount);
				sdItem.setSdRatio(sdItemSdAmount.divide(sdItem.getAmount(),5,BigDecimal.ROUND_HALF_UP).doubleValue());// sd金额/金额
				sdItem.setCreateDate(TimeUtils.getNow());
				sdItem.setSaleItemId(saleItemId);

				List<SaleSdOrderItemModel> sdItemList = itemMap.get(index);
				if(sdItemList == null) {
					sdItemList = new ArrayList<SaleSdOrderItemModel>() ;
				}
				BigDecimal totalSdAmount = sdOrder.getTotalSdAmount();
				totalSdAmount = totalSdAmount.add(sdItemSdAmount) ;
				sdOrder.setTotalSdAmount(totalSdAmount);

				Integer totalSdNum = sdOrder.getTotalSdNum();
				totalSdNum = totalSdNum +  num;
				sdOrder.setTotalSdNum(totalSdNum);
				orderMap.put(index, sdOrder) ;

				sdItemList.add(sdItem) ;
				itemMap.put(index, sdItemList) ;
			}
		}

		List<String> shelfCodeList = new ArrayList<String>();
		List<String> returnCodeList = new ArrayList<String>();
		if(failure < 1) {
			for (String index : orderMap.keySet()) {
				//有表体信息才新增
				List<SaleSdOrderItemModel> sdItemList = itemMap.get(index);
				if(sdItemList != null && sdItemList.size() > 0){
					SaleSdOrderModel saleSdOrderModel = orderMap.get(index);
					Long orderId = saleSdOrderDao.save(saleSdOrderModel);
					for (SaleSdOrderItemModel sdItem : sdItemList) {
						sdItem.setSaleSdOrderId(orderId);
						saleSdOrderItemDao.save(sdItem) ;
					}
					if(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1.equals(saleSdOrderModel.getOrderType())) {
						if(!shelfCodeList.contains(saleSdOrderModel.getOrderCode())) {
							shelfCodeList.add(saleSdOrderModel.getOrderCode());
						}
					}else if(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2.equals(saleSdOrderModel.getOrderType())) {
						if(!returnCodeList.contains(saleSdOrderModel.getBusinessCode())) {
							returnCodeList.add(saleSdOrderModel.getBusinessCode());
						}
					}
					success++;

				}
			}
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("success", success);
		returnMap.put("pass", pass);
		returnMap.put("failure", failure);
		returnMap.put("message", resultList);
		returnMap.put("shelfCodeList", shelfCodeList.stream().distinct().collect(Collectors.toList()));
		returnMap.put("returnCodeList", returnCodeList.stream().distinct().collect(Collectors.toList()));

		return returnMap;
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

	private boolean isValidDate(String date) {
		String path="\\d{4}-\\d{1,2}-\\d{1,2}";//定义匹配规则
		Pattern p=Pattern.compile(path);//实例化Pattern
		return p.matcher(date).matches();
	}

	/**
	 * 判断小数位
	 * @param str
	 * @param dousize
	 * @return
	 */
	private boolean doubleyn(String str, int dousize) {

		int fourplace = str.trim().length() - str.trim().indexOf(".") - 1;
		if (fourplace <= dousize) {
			return false;
		} else {
			return true;
		}

	}
}
