package com.topideal.order.service.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.BillOutinDepotBatchDao;
import com.topideal.dao.sale.BillOutinDepotDao;
import com.topideal.dao.sale.BillOutinDepotItemDao;
import com.topideal.dao.sale.VipPoBillSyncDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.BillOutinDepotDTO;
import com.topideal.entity.dto.sale.BillOutinDepotItemDTO;
import com.topideal.entity.vo.sale.BillOutinDepotBatchModel;
import com.topideal.entity.vo.sale.BillOutinDepotItemModel;
import com.topideal.entity.vo.sale.BillOutinDepotModel;
import com.topideal.entity.vo.sale.VipPoBillSyncModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.sale.BillOutinDepotService;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BillOutinDepotServiceImpl implements BillOutinDepotService{

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(BillOutinDepotServiceImpl.class);

	@Autowired
	private BillOutinDepotDao billOutinDepotDao ;

	@Autowired
	private BillOutinDepotItemDao billOutinDepotItemDao ;

	@Autowired
	private BillOutinDepotBatchDao billOutinDepotBatchDao ;

	@Autowired
	private CustomerMerchantRelMongoDao customerMerchantRelMongoDao ;

	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;

	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;

	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;

	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongo;

	@Autowired
	private MerchantDepotBuRelMongoDao MerchantDepotBuRelMongoDao;

	@Autowired
	private RMQProducer rocketMQProducer;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private VipPoBillSyncDao vipPoBillSyncDao ;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;

	private static final String[] DEPOT_CODES = {"CNS028", "CRX030"} ;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;

	@SuppressWarnings("unchecked")
	@Override
	public BillOutinDepotDTO listBillOutinDepotByPage(BillOutinDepotDTO dto,User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return billOutinDepotDao.listBillOutinDepotByPage(dto);
	}

	@Override
	public BillOutinDepotDTO searchDetail(Long id) {
		return billOutinDepotDao.searchDTOById(id);
	}

	@Override
	public Integer getOrderCount(BillOutinDepotDTO dto) throws SQLException {
		return billOutinDepotDao.getOrderCount(dto);
	}

	@Override
	public List<BillOutinDepotDTO> getExportMainList(BillOutinDepotDTO dto,User user) {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return new ArrayList<BillOutinDepotDTO>();
			}
			dto.setBuList(buIds);
		}
		return billOutinDepotDao.listDto(dto);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> importBillOutinDepot(List<Map<String, String>> data, User user) throws Exception {

		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		//以相同“出仓仓库+结算账单号+处理类型+币种+事业部”为维度缓存集合
		Map<String, Map<String,Object>> cacheMap = new LinkedHashMap<String, Map<String,Object>>() ;

		for (int j = 1; j <= data.size(); j++) {
			Map<String, String> row = data.get(j - 1);

			/**出仓仓库*/
			String depotCode = row.get("出仓仓库");
			if(checkIsNullOrNot(j, depotCode, "出仓仓库不能为空", resultList)) {
				failure += 1;
				continue;
			}
			depotCode = depotCode.trim() ;

			/**事业部编码*/
			String buCode = row.get("事业部编码");
			if(checkIsNullOrNot(j, buCode, "事业部编码不能为空", resultList)) {
				failure += 1;
				continue;
			}

			//仅限制唯品备查库不能导入
			if("VIP001".equals(depotCode)) {
				setErrorMsg(j, "出仓仓库不能为唯品会备查库", resultList);
				failure += 1;
				continue;
			}

			Map<String, Object> depotInfoParams = new HashMap<String, Object>();
			depotInfoParams.put("depotCode", depotCode);
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfoParams);
			if (depot == null) {
				setErrorMsg(j, "仓库不存在", resultList);
				failure += 1;
				continue;
			}

			depotInfoParams.clear();
			depotInfoParams.put("depotId", depot.getDepotId());
			depotInfoParams.put("merchantId", user.getMerchantId());
			DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotInfoParams);
			if (depotMerchantRel == null) {
				setErrorMsg(j, "公司仓库关联不存在", resultList);
				failure += 1;
				continue;
			}

			/**商家事业部*/
			Map<String, Object> mbrMap = new HashMap<String, Object>();
			mbrMap.put("merchantId", user.getMerchantId());
			mbrMap.put("buCode", buCode);
		    MerchantBuRelMongo MBuRelMongo = merchantBuRelMongo.findOne(mbrMap);
		    if(MBuRelMongo==null) {
		    	setErrorMsg(j, "商家事业部关系不存在", resultList);
				failure += 1;
				continue;
			}
		    /**商家仓库事业部*/
		    Map<String, Object> mdebrMap = new HashMap<String, Object>();
		    mdebrMap.put("merchantId", user.getMerchantId());
		    mdebrMap.put("depotId", depot.getDepotId());
		    mdebrMap.put("buId", MBuRelMongo.getBuId());
		    MerchantDepotBuRelMongo MDeBuRelMongo = MerchantDepotBuRelMongoDao.findOne(mdebrMap);
		    if(MDeBuRelMongo==null) {
		    	setErrorMsg(j, "商家仓库事业部关系不存在", resultList);
				failure += 1;
				continue;
			}
			// 校验事业部与当前账号所绑定的事业部是否匹配
			boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), MBuRelMongo.getBuId());
			if(!userRelateBu){
				setErrorMsg(j, "事业部编码为："+MBuRelMongo.getBuCode()+",用户id："+user.getId()+",无权限操作该事业部", resultList);
				failure += 1;
				continue;
			}

			/**客户*/
			String customerName = row.get("客户");
			if(checkIsNullOrNot(j, customerName, "客户不能为空", resultList)) {
				failure += 1;
				continue;
			}
			customerName = customerName.trim() ;

			Map<String,Object> customerQueryMap = new HashMap<String, Object>() ;
			customerQueryMap.put("merchantId", user.getMerchantId()) ;
			customerQueryMap.put("name", customerName) ;
			customerQueryMap.put("status", "1") ;

			CustomerMerchantRelMongo customer = customerMerchantRelMongoDao.findOne(customerQueryMap);
			if(customer == null) {
				setErrorMsg(j, "根据客户名查询客户列表不存在", resultList);
				failure += 1;
				continue;
			}
			/**
			 * （1）校验导入的仓库若为保税仓时，客户仅能是配置合作模式为“代销”的客户；
			 * （2）校验导入的仓库若为备查库时，客户仅能是配置合作模式为“购销-实销实结”的客户；
			 * */
			if (DERP_SYS.DEPOTINFO_TYPE_A.equals(depot.getType()) && !DERP_SYS.BUSINESS_MODEL2.equals(customer.getBusinessModel())) {
				setErrorMsg(j, "仓库为保税仓时，客户仅能是配置合作模式为“代销”的客户", resultList);
				failure += 1;
				continue;
			}
			if (DERP_SYS.DEPOTINFO_TYPE_B.equals(depot.getType()) && !DERP_SYS.BUSINESS_MODEL4.equals(customer.getBusinessModel())) {
				setErrorMsg(j, "仓库若为备查库时，客户仅能是配置合作模式为“购销-实销实结”的客户", resultList);
				failure += 1;
				continue;
			}

			/**结算账单号*/
			String billCode = row.get("结算账单号");
			if(checkIsNullOrNot(j, billCode, "结算账单号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			billCode = billCode.trim() ;

			/**处理类型*/
			String processingType = row.get("处理类型");
			if(checkIsNullOrNot(j, processingType, "处理类型不能为空", resultList)) {
				failure += 1;
				continue;
			}
			processingType = processingType.trim() ;

			if(!DERP_ORDER.PROCESSINGTYPE_XSC.equals(processingType) && !DERP_ORDER.PROCESSINGTYPE_KTR.equals(processingType)) {
				setErrorMsg(j, "处理类型仅能为 销售出库、客退入库 两种类型", resultList);
				failure += 1;
				continue;
			}

			/**币种*/
			String currency = row.get("币种");
			if(checkIsNullOrNot(j, currency, "币种不能为空", resultList)) {
				failure += 1;
				continue;
			}
			currency = currency.trim() ;

			//币种key-value 转换
			for(DerpBasic item : DERP.currencyCodeList) {
				if(item.getValue().equals(currency)) {
					currency = String.valueOf(item.getKey()) ;
					break ;
				}
			}

			if(StringUtils.isBlank(DERP.getLabelByKey(DERP.currencyCodeList, currency))) {
				setErrorMsg(j, "币种不存在", resultList);
				failure += 1;
				continue;
			}
			/**PO号*/
			String poNo = row.get("PO号");
			if(checkIsNullOrNot(j, poNo, "PO号不能为空，若无PO号时，请填写“无”", resultList)) {
				failure += 1;
				continue;
			}
			poNo = poNo.trim();

			/**库位类型*/
			String stockLocationName = row.get("库位类型");
			//通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
			if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(MBuRelMongo.getStockLocationManage()) && StringUtils.isBlank(stockLocationName)){
				setErrorMsg(j, "当前公司主体下，事业部编码：" + MBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空", resultList);
				failure += 1;
				continue;
			}else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(MBuRelMongo.getStockLocationManage()) && StringUtils.isNotBlank(stockLocationName)){
				setErrorMsg(j, "当前公司主体下，事业部编码：" + MBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写", resultList);
				failure += 1;
				continue;
			}
			BuStockLocationTypeConfigMongo stockLocationMongo = null;
			if(StringUtils.isNotBlank(stockLocationName)){
				Map<String,Object> stockLocationMap = new HashMap<>();
				stockLocationMap.put("merchantId", user.getMerchantId());
				stockLocationMap.put("buId", MBuRelMongo.getBuId());
				stockLocationMap.put("name", stockLocationName);
				stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
				stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);

				if(stockLocationMongo == null){
					setErrorMsg(j, "当前公司主体下，事业部编码：" + MBuRelMongo.getBuCode()+"库位类型："+stockLocationName+"，不存在", resultList);
					failure += 1;
					continue;
				}
			}


			/**系统商品货号*/
			String goodsNo = row.get("系统商品货号");
			if(checkIsNullOrNot(j, goodsNo, "系统商品货号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			goodsNo = goodsNo.trim() ;

			// 查询商品
			Map<String, Object> merchandiseMap = new HashMap<String, Object>();
			merchandiseMap.put("goodsNo", goodsNo);
			merchandiseMap.put("merchantId", user.getMerchantId());
			merchandiseMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRel.getProductRestriction())) {
				merchandiseMap.put("isRecord", DERP_SYS.MERCHANDISEINFO_ISRECORD_1);// 是否备案 1-是
			}

			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRel.getProductRestriction())) {
				merchandiseMap.put("outDepotFlag", DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
			}

			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);

			if(merchandise == null) {
				setErrorMsg(j, "商品不存在", resultList);
				failure += 1;
				continue;
			}

			/**平台SKU条码*/
			String platformSku = row.get("平台SKU编码");
			if(checkIsNullOrNot(j, platformSku, "平台SKU编码不能为空", resultList)) {
				failure += 1;
				continue;
			}
			platformSku = platformSku.trim() ;

			/**数量*/
			String num = row.get("数量");
			if(checkIsNullOrNot(j, num, "数量不能为空", resultList)) {
				failure += 1;
				continue;
			}
			num = num.trim() ;

			if(!isNumber(num)) {
				setErrorMsg(j, "数量非数字类型", resultList);
				failure += 1;
				continue;
			}

			if(Integer.valueOf(num) < 1) {
				setErrorMsg(j, "数量必须大于0", resultList);
				failure += 1;
				continue;
			}

			/**结算单价*/
			String price = row.get("结算单价（不含税）");
			if(checkIsNullOrNot(j, price, "结算单价（不含税）不能为空", resultList)) {
				failure += 1;
				continue;
			}
			price = price.trim() ;

			if(!isNumber(price)) {
				setErrorMsg(j, "结算单价（不含税）非数字类型", resultList);
				failure += 1;
				continue;
			}

			/**结算金额*/
			String amount = row.get("结算金额（不含税）");
			if(checkIsNullOrNot(j, amount, "结算金额（不含税）不能为空", resultList)) {
				failure += 1;
				continue;
			}
			amount = amount.trim() ;

			if(!isNumber(amount)) {
				setErrorMsg(j, "结算金额（不含税）非数字类型", resultList);
				failure += 1;
				continue;
			}
			if(StringUtils.isNotBlank(amount)) {
				int indexOf = amount.indexOf(".");
				// 如果是小数
				if (indexOf != -1) {
					int count = amount.length() - 1 - indexOf;
					if (count > 2) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("结算账单号"+billCode+" 商品货号："+goodsNo+"，结算金额小数点后只能为两位数");
						resultList.add(message);

						failure += 1;
						continue;
					}
				}
			}
			/**账单出库时间*/
			String deliverDate = row.get("账单出库时间");
			if(checkIsNullOrNot(j, deliverDate, "账单出库时间不能为空", resultList)) {
				failure += 1;
				continue;
			}
			deliverDate = deliverDate.trim() ;

			if(!isDate(deliverDate)) {
				setErrorMsg(j, "账单出库时间格式有误", resultList);
				failure += 1;
				continue;
			}

			if(TimeUtils.daysBetween(TimeUtils.parse(deliverDate, "yyyy-MM-dd"), new Date()) < 0) {
				setErrorMsg(j, "账单出库时间不可超过当前时间", resultList);
				failure += 1;
				continue;
			}

			String taxRateStr = row.get("税率");
			taxRateStr = taxRateStr.trim() ;


			/**批次号*/
			String batchNo = row.get("批次号");
			/**生产日期*/
			String productionDate = row.get("生产日期");
			/**失效日期*/
			String overdueDate = row.get("失效日期");
			/**
			 * 1、销售出库类型：检查该仓库若为批次效期强校验，批次效期必填，按照先失效先出库的原则扣减库存；
			 * 2、客退入库类型：检查该仓库若为批次效期强校验，或入库强校验，批次效期必填
			 **/
			if ((DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation()) && DERP_ORDER.PROCESSINGTYPE_XSC.equals(processingType)) ||
					(DERP_ORDER.PROCESSINGTYPE_KTR.equals(processingType) &&
							depot.getBatchValidation().matches(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1 + "|" + DERP_SYS.DEPOTINFO_BATCHVALIDATION_2))) {
				if(checkIsNullOrNot(j, batchNo, "批次号不能为空", resultList)) {
					failure += 1;
					continue;
				}
				batchNo = batchNo.trim() ;

				if(checkIsNullOrNot(j, productionDate, "生产日期不能为空", resultList)) {
					failure += 1;
					continue;
				}
				productionDate = productionDate.trim() ;
				if(!isDate(productionDate)) {
					setErrorMsg(j, "生产日期格式有误", resultList);
					failure += 1;
					continue;
				}

				if(checkIsNullOrNot(j, overdueDate, "失效日期不能为空", resultList)) {
					failure += 1;
					continue;
				}
				overdueDate = overdueDate.trim() ;
				if(!isDate(overdueDate)) {
					setErrorMsg(j, "失效日期格式有误", resultList);
					failure += 1;
					continue;
				}
			}

			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(depot.getDepotId());
			closeAccountsMongo.setBuId(MBuRelMongo.getBuId());
			String maxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			String deliverDateTime = deliverDate +" 00:00:00";
			// 关账日期和发货日期比较
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 出库日期
				if (Timestamp.valueOf(deliverDateTime).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(j,"出入库日期必须大于关账日期/月结日期", resultList);
					failure += 1;
					continue;
				}
			}

			BillOutinDepotModel tempModel = new BillOutinDepotModel() ;
			tempModel.setDepotId(depot.getDepotId());
			tempModel.setBillCode(billCode);
			tempModel.setProcessingType(processingType);
			tempModel.setCurrency(currency);
			tempModel.setBuId(MBuRelMongo.getBuId());
			tempModel.setMerchantId(user.getMerchantId());
			tempModel = billOutinDepotDao.searchUnDelModel(tempModel) ;

			if(tempModel != null) {
				setErrorMsg(j, "结算账单号+出库仓库+处理类型+币种+事业部对应已存在账单出库单", resultList);
				failure += 1;
				continue;
			}

			String key = depotCode + "_" + billCode + "_" + processingType + "_" + currency+"_"+MBuRelMongo.getBuId();

			//以结算账单号+出库仓库+处理类型+币种+事业部存储对应表头表体
			Map<String, Object> tempMap = cacheMap.get(key);
			if(tempMap == null) {
				tempMap = new HashMap<String, Object>() ;

				tempModel = new BillOutinDepotModel() ;
				tempModel.setDepotId(depot.getDepotId());
				tempModel.setDepotName(depot.getName());
				tempModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ZDCK));
				tempModel.setCustomerId(customer.getCustomerId());
				tempModel.setCustomerName(customer.getName());
				tempModel.setBillCode(billCode);
				tempModel.setProcessingType(processingType);
				tempModel.setDeliverDate(TimeUtils.parse(deliverDate, "yyyy-MM-dd"));
				tempModel.setTotalNum(Integer.valueOf(num));
				tempModel.setTotalAmount(new BigDecimal(amount));
				tempModel.setState(DERP_ORDER.BILLOUTINDEPOT_STATE_00);
				tempModel.setBillSource(DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_3);
				tempModel.setMerchantId(user.getMerchantId());
				tempModel.setMerchantName(user.getMerchantName());
				tempModel.setCreater(user.getName());
				tempModel.setCreaterId(user.getId());
				tempModel.setCurrency(currency);
				tempModel.setCreateDate(TimeUtils.getNow());
				tempModel.setBuId(MBuRelMongo.getBuId());
				tempModel.setBuName(MBuRelMongo.getBuName());
				if(stockLocationMongo != null){
					tempModel.setStockLocationTypeId(stockLocationMongo.getBuStockLocationTypeId());
					tempModel.setStockLocationTypeName(stockLocationMongo.getName());
				}

				if("XSC".equals(processingType) || "GJC".equals(processingType) || "PKC".equals(processingType) || "BFC".equals(processingType)) {
					tempModel.setOperateType(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_0);
				}else if("PYR".equals(processingType) || "KTR".equals(processingType)) {
					tempModel.setOperateType(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_1);
				}

				tempMap.put("itemList", new ArrayList<BillOutinDepotItemModel>()) ;

			}else {

				tempModel = (BillOutinDepotModel) tempMap.get("detail") ;

				Integer totalNum = tempModel.getTotalNum() + Integer.valueOf(num);
				BigDecimal totalAmount = tempModel.getTotalAmount().add(new BigDecimal(amount));

				tempModel.setTotalNum(totalNum);
				tempModel.setTotalAmount(totalAmount);
			}

			List<BillOutinDepotItemModel> tempItemList = (List<BillOutinDepotItemModel>)tempMap.get("itemList") ;

			boolean flag = true ;

			//按系统商品货号 + 平台SKU条码 + 批次效期 合并数量
			for (BillOutinDepotItemModel itemModel : tempItemList) {
				//批次效期
				String batchKey = batchNo + "_" + productionDate + "_" + overdueDate;
				String existBatchKey = itemModel.getBatchNo() + "_" + itemModel.getProductionDate() + "_" + itemModel.getOverdueDate();

				if(itemModel.getGoodsNo().equals(merchandise.getGoodsNo())&& itemModel.getPlatformSku().equals(platformSku) && batchKey.equals(existBatchKey)) {

					if(!Arrays.asList(DEPOT_CODES).contains(depotCode) && itemModel.getPrice().compareTo((new BigDecimal(price))) == 0) {

						itemModel.setNum(itemModel.getNum() + Integer.valueOf(num));
						itemModel.setAmount(itemModel.getAmount().add(new BigDecimal(amount)));

						flag = false ;

					}else if(Arrays.asList(DEPOT_CODES).contains(depotCode)) {

						itemModel.setNum(itemModel.getNum() + Integer.valueOf(num));
						itemModel.setAmount(itemModel.getAmount().add(new BigDecimal(amount)));

						flag = false ;

					}
					//税额=结算金额（不含税）*税率，税额保留2位小数
					BigDecimal tax = itemModel.getAmount().multiply(new BigDecimal(itemModel.getTaxRate())).setScale(2, BigDecimal.ROUND_HALF_UP);
					itemModel.setTax(tax);
					//结算金额（含税）=结算金额（不含税）+ 税额，金额保留2位小数
					BigDecimal taxAmount = itemModel.getAmount().add(tax).setScale(2, BigDecimal.ROUND_HALF_UP);
					itemModel.setTaxAmount(taxAmount);
					//结算单价（含税）=结算金额（含税）/数量，保留8位小数
					BigDecimal taxPrice = taxAmount.divide(new BigDecimal(itemModel.getNum()), 8, BigDecimal.ROUND_HALF_UP);
					itemModel.setTaxPrice(taxPrice);
				}

			}

			if(flag) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				BillOutinDepotItemModel tempItem = new BillOutinDepotItemModel() ;
				tempItem.setGoodsId(merchandise.getMerchandiseId());
				tempItem.setGoodsName(merchandise.getName());
				tempItem.setGoodsNo(merchandise.getGoodsNo());
				tempItem.setPlatformSku(platformSku);
				tempItem.setCommbarcode(merchandise.getCommbarcode());
				tempItem.setNum(Integer.valueOf(num));
				tempItem.setAmount(new BigDecimal(amount));
				tempItem.setPrice(new BigDecimal(price));
				tempItem.setCreateDate(TimeUtils.getNow());
				tempItem.setBatchNo(batchNo);
				tempItem.setPoNo(poNo);
				if (StringUtils.isNotBlank(productionDate)) {
					tempItem.setProductionDate(sdf.parse(productionDate));
				}

				if (StringUtils.isNotBlank(overdueDate)) {
					tempItem.setOverdueDate(sdf.parse(overdueDate));
				}
				// 查询商家 内贸商家，税率不能为空
				Map<String, Object> merchantMap = new HashMap<String, Object>();
				merchantMap.put("merchantId", user.getMerchantId());
				MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
				if(StringUtils.isBlank(taxRateStr) && customer.getRate() == null && "1".equals(merchant.getRegisteredType())) {
					failure += 1;
					setErrorMsg(j, "公司：" + merchant.getName() + "为内贸主体，税率为空，且客户档案中未维护税率", resultList);
					continue;
				}
				//导入模板税率为空则取客户档案中维护的税率值；若客户档案维护值为空，则默认取税率表中的0
				Double taxRate = 0.0;
				if(StringUtils.isNotBlank(taxRateStr)) {
					taxRate = new BigDecimal(taxRateStr).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
				}else if(customer.getRate() != null) {
					taxRate = customer.getRate().doubleValue();
				}
				tempItem.setTaxRate(taxRate);
				//税额=结算金额（不含税）*税率，税额保留2位小数
				BigDecimal tax = new BigDecimal(amount).multiply(new BigDecimal(tempItem.getTaxRate())).setScale(2, BigDecimal.ROUND_HALF_UP);
				tempItem.setTax(tax);
				//结算金额（含税）=结算金额（不含税）+ 税额，金额保留2位小数
				BigDecimal taxAmount = new BigDecimal(amount).add(tax).setScale(2, BigDecimal.ROUND_HALF_UP);
				tempItem.setTaxAmount(taxAmount);
				//结算单价（含税）=结算金额（含税）/数量，保留8位小数
				BigDecimal taxPrice = taxAmount.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);
				tempItem.setTaxPrice(taxPrice);

				/**若导入为妮素、融信备查库时，需增加以下逻辑进行处理生成账单出库单*/
				if(Arrays.asList(DEPOT_CODES).contains(depotCode)) {
					VipPoBillSyncModel querySyncModel = new VipPoBillSyncModel();
					querySyncModel.setMerchantId(user.getMerchantId());
					querySyncModel.setDepotId(tempModel.getDepotId());
					querySyncModel.setBuId(tempModel.getBuId());
					querySyncModel.setCommbarcode(tempItem.getCommbarcode());

					List<VipPoBillSyncModel> vipUnSettleAccountList = vipPoBillSyncDao.list(querySyncModel);
					if(vipUnSettleAccountList.isEmpty()) {
						failure += 1;
						setErrorMsg(j, "结算单号：" + tempModel.getBillCode() + "找不到商品："+ tempItem.getGoodsNo() +"对应PO单价信息", resultList);
						continue;
					}
				}


				tempItemList.add(tempItem) ;
			}

			tempMap.put("itemList", tempItemList) ;
			tempMap.put("detail", tempModel) ;
			cacheMap.put(key, tempMap) ;

		}

		if (failure == 0) {

			for(String key : cacheMap.keySet()) {

				String[] arr = key.split("_");

				String depotCode = arr[0] ;

				/**若导入为妮素、融信备查库时，需增加以下逻辑进行处理生成账单出库单*/
				if(!Arrays.asList(DEPOT_CODES).contains(depotCode)) {
					continue ;
				}

				Map<String, Object> tempMap = cacheMap.get(key);
				BillOutinDepotModel tempModel = (BillOutinDepotModel) tempMap.get("detail") ;
				List<BillOutinDepotItemModel> tempItemList = (List<BillOutinDepotItemModel>)tempMap.get("itemList") ;

//				for (BillOutinDepotItemModel itemModel : tempItemList) {
				for(int i=0 ; i < tempItemList.size(); i++) {
					VipPoBillSyncModel querySyncModel = new VipPoBillSyncModel();

					querySyncModel.setMerchantId(user.getMerchantId());
					querySyncModel.setDepotId(tempModel.getDepotId());
					querySyncModel.setBuId(tempModel.getBuId());
					querySyncModel.setCommbarcode(tempItemList.get(i).getCommbarcode());

					List<VipPoBillSyncModel> vipUnSettleAccountList = vipPoBillSyncDao.list(querySyncModel);

//					if(vipUnSettleAccountList.isEmpty()) {
//						failure += 1;
//						setErrorMsg(failure, "结算单号：" + tempModel.getBillCode() + "找不到商品："+ tempItemList.get(i).getGoodsNo() +"对应PO单价信息", resultList);
//						continue;
//					}

					vipUnSettleAccountList = vipUnSettleAccountList.stream()
					.sorted(Comparator.comparing(VipPoBillSyncModel::getFirstShelfDate))
					.collect(Collectors.toList()) ;

					if("KTR".equals(tempModel.getProcessingType())) {
						VipPoBillSyncModel vipPoBillSyncModel = vipUnSettleAccountList.get(0);

						tempItemList.get(i).setPoNo(vipPoBillSyncModel.getPoNo());
						tempItemList.get(i).setPrice(vipPoBillSyncModel.getSalePrice());

						BigDecimal numBd = new BigDecimal(tempItemList.get(i).getNum());

						tempItemList.get(i).setAmount(vipPoBillSyncModel.getSalePrice().multiply(numBd));

					}else if("XSC".equals(tempModel.getProcessingType())) {
						/**3.1 导入处理类型为“销售出库”时，
						 * 以标准条码汇总商品出库数量并查询PO可核销量
						 * （可核销量=PO未核销量-待审核销售出库类型的数量-当天已审核出库的数量+当天已审入库的数量）*/

						Integer totalUnSettledAccount = vipUnSettleAccountList.stream().map(VipPoBillSyncModel::getUnsettledAccount)
						.reduce(Integer::sum).get();

						Map<String, Object> queryMap = new HashMap<>() ;
						queryMap.put("commbarcode", tempItemList.get(i).getCommbarcode()) ;
						queryMap.put("merchantId", user.getMerchantId()) ;
						queryMap.put("depotId", tempModel.getDepotId()) ;

						//现占用出库数量
						Integer outOccupyNum = billOutinDepotItemDao.getOutOccupyNum(queryMap) ;
						//现占用入库数量
						Integer inOccupyNum = billOutinDepotItemDao.getInOccupyNum(queryMap) ;

						if(tempItemList.get(i).getNum() > totalUnSettledAccount - outOccupyNum + inOccupyNum) {
							failure += 1;
							setErrorMsg(failure, "结算单号：" + tempModel.getBillCode() +  "商品：" + tempItemList.get(i).getGoodsNo() +"可核销量不足", resultList);
							continue;
						}

						for (VipPoBillSyncModel vipPoBillSyncModel : vipUnSettleAccountList) {

							Integer unsettledAccount = vipPoBillSyncModel.getUnsettledAccount();
							queryMap.put("poNo", vipPoBillSyncModel.getPoNo()) ;

							//现占用出库数量
							Integer outTempOutOccupyNum = billOutinDepotItemDao.getOutOccupyNum(queryMap) ;
							//现占用入库数量
							Integer outTempinOccupyNum = billOutinDepotItemDao.getInOccupyNum(queryMap) ;
							/**可核销量=PO未核销量-待审核销售出库类型的数量-当天已审核出库的数量+当天已审入库的数量*/
							unsettledAccount = unsettledAccount - outTempOutOccupyNum + outTempinOccupyNum ;

							/** 3.1.1 若本次出库量小于或等于PO可核销量，则回填该PO号、销售单价到账单出库单里，重算对应的结算金额（销售单价*出库数量）*/
							if(unsettledAccount >= tempItemList.get(i).getNum()) {

								tempItemList.get(i).setPoNo(vipPoBillSyncModel.getPoNo());
								tempItemList.get(i).setPrice(vipPoBillSyncModel.getSalePrice());

								BigDecimal numBd = new BigDecimal(tempItemList.get(i).getNum());

								tempItemList.get(i).setAmount(vipPoBillSyncModel.getSalePrice().multiply(numBd));

								tempItemList.get(i).setAmount(vipPoBillSyncModel.getSalePrice().multiply(numBd));
								//税额=结算金额（不含税）*税率，税额保留2位小数
								BigDecimal tax = tempItemList.get(i).getAmount().multiply(new BigDecimal(tempItemList.get(i).getTaxRate())).setScale(2, BigDecimal.ROUND_HALF_UP);
								tempItemList.get(i).setTax(tax);
								//结算金额（含税）=结算金额（不含税）+ 税额，金额保留2位小数
								BigDecimal taxAmount = tempItemList.get(i).getAmount().add(tax).setScale(2, BigDecimal.ROUND_HALF_UP);
								tempItemList.get(i).setTaxAmount(taxAmount);
								//结算单价（含税）=结算金额（含税）/数量，保留8位小数
								BigDecimal taxPrice = taxAmount.divide(numBd, 8, BigDecimal.ROUND_HALF_UP);
								tempItemList.get(i).setTaxPrice(taxPrice);

								break ;
							}
							/**3.1.2若本次出库量大于PO可核销量，
							 * 则按照先上架先核销的方式进行倒核，回填被核销PO号、销售单价，重算对应的结算金额，
							 * 生成的账单出库单明细也需拆分成PO+SKU的维度记录结算单价、结算金额*/
							else {
								/**拆分表体*/
								BillOutinDepotItemModel targetModel = new BillOutinDepotItemModel() ;

								BeanUtils.copyProperties(tempItemList.get(i), targetModel);

								targetModel.setNum(unsettledAccount);
								targetModel.setPoNo(vipPoBillSyncModel.getPoNo());
								targetModel.setPrice(vipPoBillSyncModel.getSalePrice());

								BigDecimal numBd = new BigDecimal(unsettledAccount);

								targetModel.setAmount(vipPoBillSyncModel.getSalePrice().multiply(numBd));
								//税额=结算金额（不含税）*税率，税额保留2位小数
								BigDecimal tax = targetModel.getAmount().multiply(new BigDecimal(targetModel.getTaxRate())).setScale(2, BigDecimal.ROUND_HALF_UP);
								targetModel.setTax(tax);
								//结算金额（含税）=结算金额（不含税）+ 税额，金额保留2位小数
								BigDecimal taxAmount = targetModel.getAmount().add(tax).setScale(2, BigDecimal.ROUND_HALF_UP);
								targetModel.setTaxAmount(taxAmount);
								//结算单价（含税）=结算金额（含税）/数量，保留8位小数
								BigDecimal taxPrice = taxAmount.divide(numBd, 8, BigDecimal.ROUND_HALF_UP);
								targetModel.setTaxPrice(taxPrice);

								tempItemList.add(targetModel) ;
								/**拆分表体*/

								tempItemList.get(i).setNum(tempItemList.get(i).getNum() - unsettledAccount);

								continue ;
							}

						}

					}

				}
				//重新统计表头总金额,总数
				BigDecimal totalAmount = BigDecimal.ZERO;
				Integer totalNum = 0;
				for (BillOutinDepotItemModel itemModel : tempItemList) {
					totalAmount = totalAmount.add(itemModel.getAmount());
					totalNum = totalNum+itemModel.getNum();
				}
				tempModel.setTotalAmount(totalAmount);
				tempModel.setTotalNum(totalNum);
			}

		}

		if (failure == 0) {
			//保存表头表体
			for(Map<String, Object> tempMap : cacheMap.values()) {
				BillOutinDepotModel tempModel = (BillOutinDepotModel) tempMap.get("detail") ;
				List<BillOutinDepotItemModel> tempItemList = (List<BillOutinDepotItemModel>)tempMap.get("itemList") ;

				Long outinDepotId = billOutinDepotDao.save(tempModel);

				for (BillOutinDepotItemModel itemModel : tempItemList) {
					itemModel.setOutinDepotId(outinDepotId);

					billOutinDepotItemDao.save(itemModel) ;
				}

			}

			success = data.size() ;
		}


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}

	/**
	 * 错误时，设置错误内容
	 *
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index, String msg, List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}

	/**
	 * 判断输入字段是否为空
	 *
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index, String content, String msg, List<ImportErrorMessage> resultList) {

		if (StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true;

		} else {
			return false;
		}

	}

	/**
	 * 判断是否yyyy-MM-dd
	 * @param date
	 * @return
	 */
	private boolean isDate(String date) {
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		return p.matcher(date).matches();
	}

	/**
	 * 判断是否
	 * @param str
	 * @return
	 */
	private boolean isNumber(String str) {
        //采用正则表达式的方式来判断一个字符串是否为数字，这种方式判断面比较全
        //可以判断正负、整数小数

        boolean isInt = Pattern.compile("^-?[1-9]\\d*$").matcher(str).find();
        boolean isDouble = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$").matcher(str).find();

        return isInt || isDouble;

	}

	@Override
	public BillOutinDepotItemModel listBillOutinDepotItemByPage(BillOutinDepotItemModel model) throws SQLException {
		model = billOutinDepotItemDao.searchByPage(model) ;
		return model;
	}

	@Override
	public BillOutinDepotBatchModel listBillOutinDepotBatchByPage(BillOutinDepotBatchModel model) throws SQLException {
		model = billOutinDepotBatchDao.searchByPage(model) ;
		return model;
	}

	@Override
	public boolean delBillOutinDepot(List<Long> list) throws Exception {

		for (Long id : list) {
			BillOutinDepotModel tempModel = billOutinDepotDao.searchById(id) ;

			if(tempModel == null) {
				throw new RuntimeException("出库单不存在") ;
			}

			if(!DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_3.equals(tempModel.getBillSource())) {
				throw new RuntimeException("存在非手工导入出库单") ;
			}

			if(!DERP_ORDER.BILLOUTINDEPOT_STATE_00.equals(tempModel.getState())) {
				throw new RuntimeException("存在非待审核出库单") ;
			}

			tempModel = new BillOutinDepotModel() ;
			tempModel.setId(id);
			tempModel.setState(DERP.DEL_CODE_006);

			billOutinDepotDao.modify(tempModel) ;
		}

		return true;
	}

	@Override
	public Map<String, Integer> getOrderGoodsInfo(List<Long> ids) throws SQLException {
		Map<String,Integer> map = new HashMap<String,Integer>();
		for (Long id : ids) {
			// 根据id获取信息
			BillOutinDepotDTO dto = billOutinDepotDao.searchDTOById(id);
			// 单据状态必须为待审核并且是手工导入
			if(!DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_3.equals(dto.getBillSource()) ||
					!DERP_ORDER.BILLOUTINDEPOT_STATE_00.equals(dto.getState())){
				throw new RuntimeException("审核失败，必须是手工导入且状态为待审核");
			}

			//调增跳过
			if(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_1.equals(dto.getOperateType())) {
				continue ;
			}

			//获取仓库信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", dto.getDepotId());
			DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params);

			//获取出库表体（商品信息）
			BillOutinDepotItemModel queryItem = new BillOutinDepotItemModel();
			queryItem.setOutinDepotId(dto.getId());
			List<BillOutinDepotItemModel> itemList = billOutinDepotItemDao.list(queryItem);
			for(BillOutinDepotItemModel item:itemList){
				Long goodsId = item.getGoodsId();
				String goodsNo = item.getGoodsNo();
				Integer num = 0;

				if(map.containsKey(goodsId+"-"+dto.getDepotId()+"-"+outDepot.getCode()+"-"+goodsNo)){
					num = map.get(goodsId+"-"+dto.getDepotId()+"-"+outDepot.getCode()+"-"+goodsNo);
				}

				map.put(goodsId+"-"+dto.getDepotId()+"-"+outDepot.getCode()+"-"+goodsNo, num);
			}
		}
		return map;
	}

	@Override
	public Map<String, String> auditSaleOutDepot(Long id, User user) throws Exception {

		Map<String, String> result = new HashMap<>();
		BillOutinDepotModel tempModel = billOutinDepotDao.searchById(id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(tempModel == null) {
			result.put("code", "01");
			result.put("msg", "审核失败，出库单不存在 id:" + id + "\n");
			return result;
		}

		if(!DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_3.equals(tempModel.getBillSource()) ||
				!DERP_ORDER.BILLOUTINDEPOT_STATE_00.equals(tempModel.getState())){
			result.put("code", "01");
			result.put("msg", "单据：" + tempModel.getCode() + "审核失败，单据必须是手工导入且状态为待审核\n");
			return result;
		}

		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", tempModel.getDepotId());// 根据仓库id
		DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params); 	// 获取仓库信息

		//商家事业部
		/**商家事业部*/
		Map<String, Object> mbrMap = new HashMap<String, Object>();
		mbrMap.put("merchantId", tempModel.getMerchantId());
		mbrMap.put("buId", tempModel.getBuId());
		MerchantBuRelMongo MBuRelMongo = merchantBuRelMongo.findOne(mbrMap);

		// 获取最大的关账日/月结日期
		FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(tempModel.getMerchantId());
		closeAccountsMongo.setDepotId(tempModel.getDepotId());
		closeAccountsMongo.setBuId(tempModel.getBuId());
		String maxdate = "";
		if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
		}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
		}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
		}
		String maxCloseAccountsMonth="";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
			maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
		}
		// 关账日期和发货日期比较
		if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 关账下个月日期大于出库日期
			if (tempModel.getDeliverDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
				result.put("code", "01");
				result.put("msg", "单据：" + tempModel.getCode() + "审核失败，出库日期必须大于关账日期/月结日期库\n");
				return result;
			}
		}

		BillOutinDepotItemModel queryItemModel = new BillOutinDepotItemModel() ;
		queryItemModel.setOutinDepotId(id);
		List<BillOutinDepotItemModel> itemList = billOutinDepotItemDao.list(queryItemModel);

		if ((DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation()) && DERP_ORDER.PROCESSINGTYPE_XSC.equals(tempModel.getProcessingType())) ||
				(DERP_ORDER.PROCESSINGTYPE_KTR.equals(tempModel.getProcessingType()) &&
						depot.getBatchValidation().matches(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1 + "|" + DERP_SYS.DEPOTINFO_BATCHVALIDATION_2))) {
			for (BillOutinDepotItemModel billOutinDepotItemModel : itemList) {
				if (StringUtils.isBlank(billOutinDepotItemModel.getBatchNo())) {
					result.put("code", "01");
					result.put("msg", "单据：" + tempModel.getCode() + "商品货号：" + billOutinDepotItemModel.getGoodsNo() + "的批次号不能为空\n");
					return result;
				}

				if (billOutinDepotItemModel.getProductionDate() == null) {
					result.put("code", "01");
					result.put("msg", "单据：" + tempModel.getCode() + "商品货号：" + billOutinDepotItemModel.getGoodsNo() + "的生产日期不能为空\n");
					return result;
				}

				if (billOutinDepotItemModel.getOverdueDate() == null) {
					result.put("code", "01");
					result.put("msg", "单据：" + tempModel.getCode() + "商品货号：" + billOutinDepotItemModel.getGoodsNo() + "的失效日期不能为空\n");
					return result;
				}
			}
		}

		if(Arrays.asList(DEPOT_CODES).contains(depot.getDepotCode())
				&& "XSC".equals(tempModel.getProcessingType())) {


			for (BillOutinDepotItemModel billOutinDepotItemModel : itemList) {

				VipPoBillSyncModel querySyncModel = new VipPoBillSyncModel();

				querySyncModel.setMerchantId(user.getMerchantId());
				querySyncModel.setDepotId(depot.getDepotId());
				querySyncModel.setCommbarcode(billOutinDepotItemModel.getCommbarcode());
				querySyncModel.setBuId(MBuRelMongo.getBuId());
				querySyncModel.setPoNo(billOutinDepotItemModel.getPoNo());

				List<VipPoBillSyncModel> vipUnSettleAccountList = vipPoBillSyncDao.list(querySyncModel);

				if(vipUnSettleAccountList.isEmpty()) {
					result.put("code", "01");
					result.put("msg", "找不到商品："+ billOutinDepotItemModel.getGoodsNo() +"对应PO单价信息");
					return result;
				}

				vipUnSettleAccountList = vipUnSettleAccountList.stream()
						.sorted(Comparator.comparing(VipPoBillSyncModel::getFirstShelfDate))
						.collect(Collectors.toList()) ;

				Map<String, Object> queryMap = new HashMap<>() ;
				queryMap.put("commbarcode", billOutinDepotItemModel.getCommbarcode()) ;
				queryMap.put("merchantId", user.getMerchantId()) ;
				queryMap.put("depotId", depot.getDepotId()) ;
				queryMap.put("poNo", billOutinDepotItemModel.getPoNo()) ;

				//现占用出库数量
				Integer outOccupyNum = billOutinDepotItemDao.getOutOccupyNum(queryMap) ;
				//现占用入库数量
				Integer inOccupyNum = billOutinDepotItemDao.getInOccupyNum(queryMap) ;

				boolean flag = false ;

				for (VipPoBillSyncModel itemModel : vipUnSettleAccountList) {

					if(itemModel.getUnsettledAccount() - outOccupyNum + inOccupyNum >= 0) {
						flag = true ;

						break ;
					}
				}

				if(!flag) {
					result.put("code", "01");
					result.put("msg", "商品："+ billOutinDepotItemModel.getGoodsNo() +
							"PO号：" + billOutinDepotItemModel.getPoNo() + "可核销量不足");
					return result;
				}

			}
		}

		//设置为处理中
		BillOutinDepotModel updateModel = new BillOutinDepotModel() ;
		updateModel.setId(id);
		updateModel.setState(DERP_ORDER.BILLOUTINDEPOT_STATE_01);
		updateModel.setModifyDate(TimeUtils.getNow());
		billOutinDepotDao.modify(updateModel) ;
		//对应表体(合并相同商品)
		List<Map<String, Object>> goodsMapList = billOutinDepotItemDao.getCodeGoodsBacthNum(tempModel.getCode());

		//判断来源类型
		String sourceType = judgeSourceType(tempModel.getProcessingType()) ;

		//加减库存json
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();

		invetAddOrSubtractRootJson.setMerchantId(String.valueOf(tempModel.getMerchantId()));
		invetAddOrSubtractRootJson.setMerchantName(tempModel.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(user.getTopidealCode());
		invetAddOrSubtractRootJson.setDepotId(String.valueOf(tempModel.getDepotId()));
		invetAddOrSubtractRootJson.setDepotName(tempModel.getDepotName());
		invetAddOrSubtractRootJson.setDepotCode(depot.getCode());
		invetAddOrSubtractRootJson.setDepotType(depot.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(depot.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(tempModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(tempModel.getBillCode());	// 平台账单号
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0016);
		invetAddOrSubtractRootJson.setSourceType(sourceType);
		invetAddOrSubtractRootJson.setSourceDate(TimeUtils.formatFullTime());	// 单据时间
		invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.format(tempModel.getDeliverDate(), "yyyy-MM-dd HH:mm:ss"));	// 出库时间
		invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.format(tempModel.getDeliverDate(), "yyyy-MM")); // 归属月份
		invetAddOrSubtractRootJson.setBuId(String.valueOf(MBuRelMongo.getBuId()));
		invetAddOrSubtractRootJson.setBuName(MBuRelMongo.getBuName());

		//回调mq
		invetAddOrSubtractRootJson.setBackTopic(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTopic());
		invetAddOrSubtractRootJson.setBackTags(MQInventoryEnum.CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK.getTags());
		Map<String, Object> customParam = new HashMap<String, Object>();
		customParam.put("code", tempModel.getCode());	// 订单code
		invetAddOrSubtractRootJson.setCustomParam(customParam);

		//设置加减表体
		Map<String, Object> merchandiseMap = new HashMap<String, Object>() ;

		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		for (Map<String, Object> item : goodsMapList) {
			Long goodsId = (Long)item.get("goods_id");
			String goodsNo = (String)item.get("goods_no");
			String goodsName = (String)item.get("goods_name");
			BigDecimal numBigDecimal = (BigDecimal)item.get("num");
			Integer goodsNum = numBigDecimal.intValue();
			Long originalGoodsId = (Long)item.get("original_goods_id");
			String batchNo = (String)item.get("batch_no");
			Date productionDate = (Date)item.get("production_date");
			Date overdueDate = (Date)item.get("overdue_date");

			merchandiseMap.put("goodsNo", goodsNo);
			merchandiseMap.put("merchantId", tempModel.getMerchantId());
			merchandiseMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);

			Map<String, Object> orgMerchandiseMap = new HashMap<String, Object>();
			MerchandiseInfoMogo orgMerchandiseMogo=null;
			if (originalGoodsId!=null) {
				orgMerchandiseMap.put("merchandiseId", originalGoodsId);
				orgMerchandiseMogo = merchandiseInfoMogoDao.findOne(orgMerchandiseMap);
			}

			InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
			if (orgMerchandiseMogo==null) {
				invetAddOrSubtractGoodsListJson.setGoodsId(goodsId.toString());
				invetAddOrSubtractGoodsListJson.setGoodsName(goodsName);
				invetAddOrSubtractGoodsListJson.setGoodsNo(goodsNo);
				invetAddOrSubtractGoodsListJson.setBarcode(merchandise.getBarcode());
			}else {
				invetAddOrSubtractGoodsListJson.setGoodsId(orgMerchandiseMogo.getMerchandiseId().toString());
				invetAddOrSubtractGoodsListJson.setGoodsName(orgMerchandiseMogo.getName());
				invetAddOrSubtractGoodsListJson.setGoodsNo(orgMerchandiseMogo.getGoodsNo());
				invetAddOrSubtractGoodsListJson.setBarcode(orgMerchandiseMogo.getBarcode());

			}
			invetAddOrSubtractGoodsListJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);	// 商品分类 （0 好品 1坏品 ）
			invetAddOrSubtractGoodsListJson.setNum(goodsNum);	//数量
			invetAddOrSubtractGoodsListJson.setOperateType(tempModel.getOperateType());// 字符串 0 调减 1调增
			invetAddOrSubtractGoodsListJson.setBatchNo(batchNo);
			String isExpire = DERP.ISEXPIRE_1;

			if (productionDate != null) {
				invetAddOrSubtractGoodsListJson.setProductionDate(sdf.format(productionDate));
			}
			if (overdueDate != null) {
				invetAddOrSubtractGoodsListJson.setOverdueDate(sdf.format(overdueDate));
				isExpire = TimeUtils.isNotIsExpireByDate(overdueDate);//判断是否过期是否过期（0是 1否）
			}
			invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);
			invetAddOrSubtractGoodsListJson.setStockLocationTypeId(tempModel.getStockLocationTypeId() == null ? "" : tempModel.getStockLocationTypeId().toString());
			invetAddOrSubtractGoodsListJson.setStockLocationTypeName(tempModel.getStockLocationTypeName());
			invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
		}

		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

		//加库存
		JSONObject addjson = JSONObject.fromObject(invetAddOrSubtractRootJson);

		try {
			rocketMQProducer.send(addjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		} catch (Exception e) {
			LOGGER.error("-----------------账单出入库加减库存失败---------------------------");
			throw new RuntimeException("单据：" + tempModel.getCode() + "账单出入库加减库存失败");
		}

		result.put("code", "00");
		result.put("msg", "审核成功");
        return result;
	}

	private String judgeSourceType(String processType) {

		String sourceType = null ;

		switch (processType) {
		case DERP_ORDER.PROCESSINGTYPE_XSC:
			sourceType = DERP_INVENTORY.INVENTORY_SOURCETYPE_0028 ;
			break;

		case DERP_ORDER.PROCESSINGTYPE_BFC:
			sourceType = DERP_INVENTORY.INVENTORY_SOURCETYPE_0025 ;
			break;

		case DERP_ORDER.PROCESSINGTYPE_GJC:
			sourceType = DERP_INVENTORY.INVENTORY_SOURCETYPE_0022 ;
			break;

		case DERP_ORDER.PROCESSINGTYPE_KTR:
			sourceType = DERP_INVENTORY.INVENTORY_SOURCETYPE_0027 ;
			break;

		case DERP_ORDER.PROCESSINGTYPE_PKC:
			sourceType = DERP_INVENTORY.INVENTORY_SOURCETYPE_0024 ;
			break;

		case DERP_ORDER.PROCESSINGTYPE_PYR:
			sourceType = DERP_INVENTORY.INVENTORY_SOURCETYPE_0024 ;
			break;

		default:
			break;
		}

		return sourceType ;

	}

	@Override
	public List<BillOutinDepotItemDTO> getExportItemList(BillOutinDepotDTO dto,User user) {
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return new ArrayList<BillOutinDepotItemDTO>();
			}
			dto.setBuList(buIds);
		}
		List<BillOutinDepotItemDTO> itemList = billOutinDepotDao.getExportItemList(dto);
		if(itemList==null||itemList.size()<=0) return  itemList;

		//获取所有商品id
		Set<Long> goodsIdSet = new HashSet<>();
		for(BillOutinDepotItemDTO item : itemList){
			goodsIdSet.add(item.getGoodsId());
		}
        List<Long> goodsIdList = new ArrayList<>(goodsIdSet);
		List<MerchandiseInfoMogo> merchandiseList = merchandiseInfoMogoDao.findAllByIn("merchandiseId",goodsIdList);
		Map<Long,String> goodsBarcodeMap = new HashMap<>();//存放商品条码 key=goodsId value=条码
		for(MerchandiseInfoMogo merchandise : merchandiseList){
			goodsBarcodeMap.put(merchandise.getMerchandiseId(),merchandise.getBarcode());
		}
		//填充商品条码到dto
		for(BillOutinDepotItemDTO item : itemList){
			item.setBarcode(goodsBarcodeMap.get(item.getGoodsId()));
		}
		return itemList;
	}

}
