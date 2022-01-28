package com.topideal.service.main.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandDao;
import com.topideal.dao.base.OperateSysLogDao;
import com.topideal.dao.main.*;
import com.topideal.entity.dto.main.CustomerInfoDTO;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.dto.main.ReminderEmailUserDTO;
import com.topideal.entity.dto.main.SupplierMerchandisePriceDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.main.*;
import com.topideal.mongo.dao.CountryOriginMongoDao;
import com.topideal.service.base.OperateSysLogService;
import com.topideal.service.main.ProductInfoService;
import com.topideal.service.main.SupplierMerchandisePriceService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 供应商商品价格表service实现类
 */
@Service
public class SupplierMerchandisePriceServiceImpl implements SupplierMerchandisePriceService {
	@Autowired
    SupplierMerchandisePriceDao sMPriceDao;
	@Autowired
    CustomerInfoDao customerInfoDao;
	@Autowired
    CustomerMerchantRelDao customerMerchantRelDao ;
	@Autowired
    MerchandiseInfoDao merchandiseInfoDao;
	@Autowired
	BrandDao brandDao;
	@Autowired
	CountryOriginMongoDao countryOriginMongoDao;
	@Autowired
	ProductInfoService productInfoService;
	@Autowired
	private RMQProducer rocketMQProducer;// MQ
	@Autowired
	private MerchantBuRelDao merchantBuRelDao;
	@Autowired
	private OperateSysLogDao operateSysLogDao;

	@Autowired
	private OperateSysLogService operateSysLogService;
	@Autowired
	private BuStockLocationTypeConfigDao buStockLocationTypeConfigDao;

	/**
	 * 列表（分页）
	 */
	@Override
	public SupplierMerchandisePriceDTO listSMPrice(SupplierMerchandisePriceDTO dto) throws SQLException {

		return sMPriceDao.getListByPage(dto);
	}

	@Override

	public boolean delSMPrice(List ids) throws SQLException {
		boolean flag = false;
		//判断是否有已审核的
		for(Object id:ids){
			SupplierMerchandisePriceModel sMModel = sMPriceDao.searchById(Long.parseLong(id.toString()));
			//单据状态是已审核
			if(sMModel.getState() != null && sMModel.getState().equals(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003)){
				flag = true;
				break;
			}
		}
		if(flag){
			throw new RuntimeException("删除失败，只能删除待提交、待审核、已驳回数据");
		}
		int num = sMPriceDao.delete(ids);
        if(num >= 1){
            return true;
        }
        return false;
	}

	/**
	 * 导入
	 * @param data
	 * @return
	 */
	@Override
	public Map importSMPrice(Map<Integer, List<List<Object>>> data,User user) {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;
		List<Map<String, Object>> mapList=new ArrayList<Map<String, Object>>();
		Set<SupplierMerchandisePriceModel> modelSet = new HashSet<>();

		//检查公司+事业部+供应商+标准条码+币种+采购价+生效日期+失效日期是否存在相同数据
		Set<String> keySet = new HashSet<>();
		//校验数据
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
				try{
					//必填字段的校验
					String buCode = map.get("事业部");
					if(StringUtils.isBlank(buCode)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("事业部不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					MerchantBuRelModel merchantBuRelModel=new MerchantBuRelModel();
					merchantBuRelModel.setBuCode(buCode.trim());
					merchantBuRelModel.setMerchantId(user.getMerchantId());
					merchantBuRelModel.setStatus(DERP_SYS.MERCHANT_BU_REL_STATUS_1);
					merchantBuRelModel = merchantBuRelDao.searchByModel(merchantBuRelModel);
					if (merchantBuRelModel == null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("事业部在该商家下不存在");
						resultList.add(message);
						failure+=1;
						continue;
					}

					String customerCode = map.get("供应商编码");
					if(customerCode == null || "".equals(customerCode)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("供应商编码不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}

					//判断供应商是否存在
					CustomerInfoModel customerInfoModel =new CustomerInfoModel();
					customerInfoModel.setCode(customerCode);
					customerInfoModel.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
					customerInfoModel.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);
					List<CustomerInfoModel> customerInfoModels = customerInfoDao.getDetailsList(customerInfoModel);
					if(customerInfoModels == null || customerInfoModels.size()==0){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("该供应商不存在");
						resultList.add(message);
						failure+=1;
						continue;
					}

					customerInfoModel = customerInfoModels.get(0) ;
					
					CustomerMerchantRelModel relModel=new CustomerMerchantRelModel();
					relModel.setCode(customerCode);	
					relModel.setMerchantId(user.getMerchantId());
					relModel = customerMerchantRelDao.searchByModel(relModel);
					if (relModel==null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("根据供应商编码没有查询到对应的供应商");
						resultList.add(message);
						failure += 1;
						continue;
					}
					
					
					/*CustomerMerchantRelModel relModel = new CustomerMerchantRelModel() ;
					relModel.setMerchantId(user.getMerchantId());
					relModel.setCustomerId(customerInfoModel.getId());
					
					relModel = customerMerchantRelDao.searchByModel(relModel) ;*/
					
					String barcode = map.get("条形码");
					if(barcode == null || "".equals(barcode)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("条形码不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}

					String stockLocationType = map.get("库位类型");

					BuStockLocationTypeConfigModel existType = null;

					//查询公司事业部是否启用了库位管理
					if (merchantBuRelModel.getStockLocationManage().equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0) &&
							StringUtils.isEmpty(stockLocationType)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("公司事业部启用库位管理，库位类型不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}


					if (merchantBuRelModel.getStockLocationManage().equals(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0)) {
						BuStockLocationTypeConfigModel buStockLocationTypeConfigModel = new BuStockLocationTypeConfigModel();
						buStockLocationTypeConfigModel.setName(stockLocationType);
						buStockLocationTypeConfigModel.setStatus(DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
						buStockLocationTypeConfigModel.setMerchantId(user.getMerchantId());
						buStockLocationTypeConfigModel.setBuId(merchantBuRelModel.getBuId());
						existType = buStockLocationTypeConfigDao.searchByModel(buStockLocationTypeConfigModel);
						if (existType == null) {
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("库位类型在公司事业部下不存在");
							resultList.add(message);
							failure+=1;
							continue;
						}

					}

					// 报价生效日期
					String effectiveDateStr = map.get("报价生效日期");
					if(effectiveDateStr == null || "".equals(effectiveDateStr)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("报价生效日期不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					
					// 报价失效日期
					String expiryDateStr = map.get("报价失效日期");
					if(expiryDateStr == null || "".equals(expiryDateStr)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("报价失效日期不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					Timestamp effectiveDate = TimeUtils.parse(effectiveDateStr, "yyyy-MM-dd");//生效日期
					Timestamp expiryDate = TimeUtils.parse(expiryDateStr, "yyyy-MM-dd");//失效日期
					if (effectiveDate==null||expiryDate==null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("报价生效日期或者报价失效日期格式不对");
						resultList.add(message);
						failure += 1;
						continue;	
					}
					
					//失效日期>生效日期
					if(TimeUtils.daysBetween(TimeUtils.parse(effectiveDateStr, "yyyy-MM-dd"),TimeUtils.parse(expiryDateStr, "yyyy-MM-dd") ) < 0) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("生效日期不可超过失效日期");
						resultList.add(message);
						failure+=1;
						continue;
					}
					
					

					String currency = map.get("报价币种");
					if(currency == null || "".equals(currency)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("报价币种不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}else{
						String currencyLabel = DERP_ORDER.getLabelByKey(DERP.currencyCodeList, currency);
						if(StringUtils.isBlank(currencyLabel)){
							ImportErrorMessage message = new ImportErrorMessage();
							message.setRows(j+1);
							message.setMessage("报价币种输入有误");
							resultList.add(message);
							failure+=1;
							continue;
						}
					}
					// 供货价
					String supplyPriceStr = map.get("采购价");
					if(supplyPriceStr == null || "".equals(supplyPriceStr)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("采购价不能为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					BigDecimal supplyPrice=null;
					try {
						supplyPrice=new BigDecimal(supplyPriceStr).setScale(8, BigDecimal.ROUND_HALF_UP); 						
					} catch (Exception e) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("采购价导入格式不正确");
						resultList.add(message);
						failure += 1;
						continue;
					}
					
					if (supplyPrice.compareTo(BigDecimal.ZERO)==-1) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("价格不能为负数");
						resultList.add(message);
						failure += 1;
						continue;
					}
					// 用公司+标准条码查询商品档案,拿第一条
					MerchandiseInfoModel searchMerchandiseInfoModel = new MerchandiseInfoModel();
					searchMerchandiseInfoModel.setBarcode(barcode);
					searchMerchandiseInfoModel.setMerchantId(user.getMerchantId());
					searchMerchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
					List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(searchMerchandiseInfoModel);
					if(merchandiseList==null || merchandiseList.size()==0){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("根据标准条码没有找到商品");
						resultList.add(message);
						failure+=1;
						continue;
					}
					MerchandiseInfoModel merchandiseInfoModel = merchandiseList.get(merchandiseList.size()-1);
					String commbarcode = merchandiseInfoModel.getCommbarcode();
					if (StringUtils.isBlank(commbarcode)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("条码对应的标准条码为空");
						resultList.add(message);
						failure+=1;
						continue;
					}
					BrandModel brandModel=null;
					if (merchandiseInfoModel.getBrandId()!=null) {
						brandModel = brandDao.searchById(merchandiseInfoModel.getBrandId());
					}
					if (brandModel==null)brandModel=new BrandModel();
					
					
					//检查公司+事业部+供应商+标准条码+币种+采购价+生效日期+失效日期是否存在相同数据
					String key = merchantBuRelModel.getBuId() + "_" + customerInfoModel.getCode() + "_"
							+ commbarcode + "_" + currency + "_" + supplyPrice + "_" + effectiveDate + "_" + expiryDate;

					if (keySet.contains(key)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j+1);
						message.setMessage("事业部："+ buCode + ",供应商：" + customerCode + ",标准条码：" + commbarcode
								+",币种：" + currency + ",采购价：" + supplyPrice + ",生效日期：" + effectiveDateStr
								+",失效日期：" + expiryDateStr + "存在相同数据");
						resultList.add(message);
						failure+=1;
						continue;
					}
					keySet.add(key);

					SupplierMerchandisePriceModel model = new SupplierMerchandisePriceModel();
					model.setMerchantId(user.getMerchantId());
					model.setMerchantName(user.getMerchantName());
					model.setCustomerId(customerInfoModel.getId());
					model.setCustomerCode(customerInfoModel.getCode());
					model.setCustomerName(customerInfoModel.getName());
					model.setCommbarcode(commbarcode);
					model.setGoodsName(merchandiseInfoModel.getName());
					model.setBarcode(barcode);
					model.setBrandId(brandModel.getId());
					model.setBrandName(brandModel.getName());
					model.setSpec(merchandiseInfoModel.getSpec());
					model.setCurrency(currency);
					model.setSupplyPrice(supplyPrice);
					model.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_002);
					model.setEffectiveDate(effectiveDate);
					model.setExpiryDate(expiryDate);
					model.setCreater(user.getId());
					model.setCreateName(user.getName());
					model.setCreateDate(TimeUtils.getNow());
					model.setBuId(merchantBuRelModel.getBuId());
					model.setBuName(merchantBuRelModel.getBuName());
					if (existType != null) {
						model.setStockLocationTypeId(existType.getId());
						model.setStockLocationTypeName(existType.getName());
					}
					model.setDataSource(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_DATASOURCE_2);
					modelSet.add(model);

				}catch(SQLException e){
					e.printStackTrace();
					failure+=1;
				}
			}
		}
		if(failure == 0){
			//保存数据
			for (SupplierMerchandisePriceModel supplierMerchandisePriceModel : modelSet) {
				try {
					Long itemId = sMPriceDao.save(supplierMerchandisePriceModel);
					if(itemId!=null){
						success+=1;
					}else{
						failure+=1;
					}
					//记录日志操作
					operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_2,itemId,"导入",null,null);

				} catch (Exception e) {
					e.printStackTrace();
					failure+=1;
				}
			}
		}
		Map map =new HashMap();
		map.put("success",success);
		map.put("pass",pass);
		map.put("failure",failure);
		map.put("message",resultList);
		return map;
	}
	

	@Override
	public List<SupplierMerchandisePriceDTO> getSMPriceList(SupplierMerchandisePriceDTO dto) throws SQLException {
		return sMPriceDao.queryDTOList(dto);
	}

	/**
	 * 审核
	 * @param list
	 * @param user
	 * @param auditType 0-通过 1-驳回
	 * @throws Exception
	 */
	@Override
	public void auditSMPrice(List<Long> list, User user, String auditType) throws Exception {
		List<SupplierMerchandisePriceModel> supplierMerchandisePriceModels = sMPriceDao.listByIds(list);

		Long merchantId = null;
		String merchantName = null;
		Long customerId = null;
		String customerName = null;
		Long buId = null;
		String buName = null;
		List<String> submitIds = new ArrayList<>();
		List<SupplierMerchandisePriceModel> updateModels = new ArrayList<>();

		if (list.size() != supplierMerchandisePriceModels.size()) {
			throw new RuntimeException("审核失败，供应商价目信息不存在！");
		}

		for (int i = 0; i < supplierMerchandisePriceModels.size(); i++) {
			SupplierMerchandisePriceModel model = supplierMerchandisePriceModels.get(i);
			if (model.getCustomerId() == null) {
				throw new RuntimeException("供应商为空！");
			}

			if (i == 0) {
				merchantId = model.getMerchantId();
				customerId = model.getCustomerId();
				buId = model.getBuId();
				merchantName = model.getMerchantName();
				customerName = model.getCustomerName();
				buName = model.getBuName();
			}

			if (!merchantId.equals(model.getMerchantId())) {
				throw new RuntimeException("请选择相同“公司”！");
			}

			if (!customerId.equals(model.getCustomerId())) {
				throw new RuntimeException("请选择相同“供应商”！");
			}

			if (!buId.equals(model.getBuId())) {
				throw new RuntimeException("请选择相同“事业部”！");
			}

			if (!DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_001.equals(model.getState())) {
				throw new RuntimeException("仅对状态为”待审核“的可操作审核");
			}
			submitIds.add(String.valueOf(model.getSubmitId()));

			SupplierMerchandisePriceModel updateModel = new SupplierMerchandisePriceModel();
			updateModel.setId(model.getId());
			updateModel.setAuditDate(TimeUtils.getNow());
			updateModel.setAuditName(user.getName());
			updateModel.setAuditor(user.getId());
			updateModel.setModifyDate(TimeUtils.getNow());
			if ("0".equals(auditType)) {
				updateModel.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003);
			} else {
				updateModel.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_004);
			}
			updateModels.add(updateModel);

		}

		//修改状态
		for (SupplierMerchandisePriceModel updateModel : updateModels) {
			sMPriceDao.modify(updateModel);
		}

		//发送邮件
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setMerchantId(merchantId);
		mailDTO.setMerchantName(merchantName);
		mailDTO.setBuId(buId);
		mailDTO.setBuName(buName);
		mailDTO.setSupplier(customerName);
		mailDTO.setBusinessModuleType("4");
		mailDTO.setTypeName("采购价格");
		mailDTO.setType("2");// 1：提交 2：审核 3：上架 4：核销 5：开票 6：作废审核 7：盖章发票 8：审核完毕 9：作废完成 10：金额修改 11：金额确认
		mailDTO.setUserId(submitIds);
		mailDTO.setCount(list.size());
		mailDTO.setAuditorId(user.getId());
		mailDTO.setUserName(user.getName());
		if ("0".equals(auditType)) {
			mailDTO.setStatus("已通过");
		} else {
			mailDTO.setStatus("已驳回");
		}

		rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());

		//记录日志操作
		for(Long id:list){
			operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_2,id,"审核","0".equals(auditType)?"通过":"驳回","0".equals(auditType)?"通过":"驳回");
		}
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

	@Override
	public com.alibaba.fastjson.JSONObject getSMPriceByPurchaseOrder(SupplierMerchandisePriceModel model) throws SQLException {
		
		//Timestamp effectiveDate = model.getEffectiveDate() != null ? model.getEffectiveDate() : TimeUtils.getNow() ;
		
		//String dateStr = TimeUtils.format(effectiveDate, "yyyy-MM-dd");
		
		/*model.setEffectiveDate(null);
		model.setEffectiveDateStr(dateStr);
		model.setExpiryDateStr(dateStr);
		model.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003);
		
		model = sMPriceDao.getSMPriceByPurchaseOrder(model) ;

		if(model == null){
			return new com.alibaba.fastjson.JSONObject() ;
		}*/

		com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(model) ;
		
		MerchandiseInfoModel queryModel = new MerchandiseInfoModel() ;
		queryModel.setCommbarcode(model.getCommbarcode());
		queryModel.setMerchantId(model.getMerchantId());
		
		List<MerchandiseInfoModel> goodsList = merchandiseInfoDao.list(queryModel);
		
		if(!goodsList.isEmpty()) {
			MerchandiseInfoModel merchandiseInfoModel = goodsList.get(0);
			
			Integer torrToUnit = merchandiseInfoModel.getTorrToUnit();
			Integer boxToUnit = merchandiseInfoModel.getBoxToUnit();
			
			jsonObject.put("torrToUnit", torrToUnit) ;
			jsonObject.put("boxToUnit", boxToUnit) ;
		}
		
		
		return jsonObject;
	}

	@Override
	public SupplierMerchandisePriceDTO statisticsStateNum(SupplierMerchandisePriceDTO dto) {
    	List<Map<String, Object>> statisticsStateList = sMPriceDao.statisticsStateNum(dto);
		SupplierMerchandisePriceDTO sMDto = new SupplierMerchandisePriceDTO();

		Long totalNum = 0L;
		sMDto.setSubmitNum(0L);
		sMDto.setAuditNum(0L);
		sMDto.setBeAuditNum(0L);
		sMDto.setRejectNum(0L);
		sMDto.setInvalidNum(0L);
		sMDto.setAreadyInvalidNum(0L);
		sMDto.setAllNum(0L);
    	for (Map<String, Object> map : statisticsStateList) {
    		String state = (String) map.get("state");
    		Long countNum = (Long) map.get("num");

    		if (DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_002.equals(state)) {
				sMDto.setSubmitNum(countNum);
			}
			if (DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_001.equals(state)) {
				sMDto.setAuditNum(countNum);
			}
			if (DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003.equals(state)) {
				sMDto.setBeAuditNum(countNum);
			}
			if (DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_004.equals(state)) {
				sMDto.setRejectNum(countNum);
			}
			if (DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_005.equals(state)) {
				sMDto.setInvalidNum(countNum);
			}
			if (DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_007.equals(state)) {
				sMDto.setAreadyInvalidNum(countNum);
			}
			totalNum += countNum;
		}

		sMDto.setAllNum(totalNum);
		return sMDto;
	}

	@Override
	public void submitSMPrice(List<Long> ids, User user) throws Exception {

		Long merchantId = null;
		String merchantName = null;
		Long customerId = null;
		String customerName = null;
		Long buId = null;
		String buName = null;
		List<SupplierMerchandisePriceModel> updateModels = new ArrayList<>();

		List<SupplierMerchandisePriceModel> supplierMerchandisePriceModels = sMPriceDao.listByIds(ids);

		if (ids.size() != supplierMerchandisePriceModels.size()) {
			throw new RuntimeException("提交失败，供应商价目信息不存在！");
		}

		// 选中多个对象时检查（公司、事业部、供应商、条形码、币种、采购价、报价生效时间、报价失效时间）是否存在多条，若是则提示XXX存在多条，请勿重复提交！
		Map<String, List<SupplierMerchandisePriceModel>> collect = supplierMerchandisePriceModels.stream().collect(Collectors.groupingBy(entity -> {
			String key = entity.getMerchantId() + "_" + entity.getBuId() + "_" + entity.getCustomerId()
					+ "_" + entity.getCommbarcode() + "_" + entity.getCurrency() + "_" + entity.getSupplyPrice()
					+ "_" + entity.getEffectiveDate() + "_" + entity.getExpiryDate();
			return key;
		}));

		collect.entrySet().stream().forEach(entity -> {
			int size = entity.getValue().size();
			if(size > 1) {
				SupplierMerchandisePriceModel value = entity.getValue().get(0);
				throw new RuntimeException("公司:" + value.getMerchantName() + ",事业部:" + value.getBuName()
						+ ",供应商:" + value.getCustomerName() + ",标准条码:" + value.getCommbarcode()
						+ ",币种" + value.getCurrency()
						+ "存在多条，请勿重复提交");
			}
		});

		for (int i = 0; i < supplierMerchandisePriceModels.size(); i++) {
			SupplierMerchandisePriceModel model = supplierMerchandisePriceModels.get(i);
			if (model.getCustomerId() == null) {
				throw new RuntimeException("供应商为空！");
			}

			if (i == 0) {
				merchantId = model.getMerchantId();
				customerId = model.getCustomerId();
				buId = model.getBuId();
				merchantName = model.getMerchantName();
				customerName = model.getCustomerName();
				buName = model.getBuName();
			}

			if (!merchantId.equals(model.getMerchantId())) {
				throw new RuntimeException("请选择相同“公司”！");
			}

			if (!customerId.equals(model.getCustomerId())) {
				throw new RuntimeException("请选择相同“供应商”！");
			}

			if (!buId.equals(model.getBuId())) {
				throw new RuntimeException("请选择相同“事业部”！");
			}

			if (!DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_002.equals(model.getState())) {
				throw new RuntimeException("仅对状态为”待提交“的可操作审核");
			}

			SupplierMerchandisePriceModel validateModel = new SupplierMerchandisePriceModel();
			validateModel.setMerchantId(model.getMerchantId());
			validateModel.setCustomerId(model.getCustomerId());
			validateModel.setBuId(model.getBuId());
			validateModel.setCurrency(model.getCurrency());
			validateModel.setEffectiveDate(model.getEffectiveDate());
			validateModel.setExpiryDate(model.getExpiryDate());
			validateModel.setSupplyPrice(model.getSupplyPrice());
			validateModel.setCommbarcode(model.getCommbarcode());
			List<SupplierMerchandisePriceModel> existModels = sMPriceDao.list(validateModel);

			//检查提交数据维度以公司+事业部+供应商+标准条码+币种+采购价+生效日期+失效日期 去校验待审核、已审核、已驳回，若查询一致则提示数据重复提交
			List<SupplierMerchandisePriceModel> reSumbitModels = new ArrayList<>();
			for (SupplierMerchandisePriceModel sMPrice : existModels) {
				if (!DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_002.equals(sMPrice.getState())) {
					reSumbitModels.add(sMPrice);
				}
			}

			if (reSumbitModels.size() > 0) {
				BigDecimal supplyPrice = model.getSupplyPrice();
				if (BigDecimal.ZERO.compareTo(supplyPrice)==0)supplyPrice=new BigDecimal(0);// 页面显示可选计数法问题
				throw new RuntimeException("公司:" + model.getMerchantName() + "事业部:" + model.getBuName() +
						"标准条码:" + model.getCommbarcode() + "币种:" + model.getCurrency() + "采购价:"
						+ supplyPrice + "生效日期:" + model.getEffectiveDate()
						+ "失效日期:" + model.getExpiryDate() + "存在重复数据！");
			}

			SupplierMerchandisePriceModel updateModel = new SupplierMerchandisePriceModel();
			updateModel.setId(model.getId());
			updateModel.setSubmitDate(TimeUtils.getNow());
			updateModel.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_001);
			updateModel.setSubmitName(user.getName());
			updateModel.setSubmitId(user.getId());
			updateModel.setModifyDate(TimeUtils.getNow());
			updateModels.add(updateModel);
		}

		//修改状态
		for (SupplierMerchandisePriceModel updateModel : updateModels) {
			sMPriceDao.modify(updateModel);
		}

		//发送邮件
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setMerchantId(merchantId);
		mailDTO.setMerchantName(merchantName);
		mailDTO.setBuId(buId);
		mailDTO.setBuName(buName);
		mailDTO.setSupplier(customerName);
		mailDTO.setBusinessModuleType("4");
		mailDTO.setTypeName("采购价格");
		mailDTO.setType("1");// 1：提交 2：审核 3：上架 4：核销 5：开票 6：作废审核 7：盖章发票 8：审核完毕 9：作废完成 10：金额修改 11：金额确认
		mailDTO.setUserId(Arrays.asList(String.valueOf(user.getId())));
		mailDTO.setCount(ids.size());
		mailDTO.setStatus("待审核");
		mailDTO.setUserName(user.getName());

		rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());

		//记录日志操作
		for(Long id:ids){
			operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_2,id,"提交",null,null);
		}

	}

	@Override
	public void modifySMPrice(SupplierMerchandisePriceDTO dto, User user) throws Exception {
		SupplierMerchandisePriceModel existModel = sMPriceDao.searchById(dto.getId());

		if (existModel == null) {
			throw new RuntimeException("供应商价目信息不存在！");
		}

		if (DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_001.equals(existModel.getState()) ||
				DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003.equals(existModel.getState())) {
			throw new RuntimeException("仅对状态为”待提交“,“已驳回”的进行编辑保存！");
		}

		Timestamp effectiveDate = TimeUtils.parseDay(dto.getEffectiveDateStr());
		Timestamp expiryDate = TimeUtils.parseDay(dto.getExpiryDateStr());

		if (expiryDate.getTime()<effectiveDate.getTime()) {
			throw new RuntimeException("失效日期<生效日期");
		}

		MerchantBuRelModel merchantBuRelModel=new MerchantBuRelModel();
		merchantBuRelModel.setBuId(dto.getBuId());
		merchantBuRelModel.setMerchantId(user.getMerchantId());
		merchantBuRelModel.setStatus(DERP_SYS.MERCHANT_BU_REL_STATUS_1);
		merchantBuRelModel = merchantBuRelDao.searchByModel(merchantBuRelModel);
		if (merchantBuRelModel == null) {
			throw new RuntimeException("事业部在该商家下不存在");
		}

		if (DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelModel.getStockLocationManage()) && dto.getStockLocationTypeId() == null) {
			throw new RuntimeException("公司事业部启用库位管理，库位类型不能为空");
		}

		SupplierMerchandisePriceModel updateModel = new SupplierMerchandisePriceModel();
		if (dto.getStockLocationTypeId() != null) {
			BuStockLocationTypeConfigModel buStockLocationTypeConfigModel = new BuStockLocationTypeConfigModel();
			buStockLocationTypeConfigModel.setId(dto.getStockLocationTypeId());
			buStockLocationTypeConfigModel.setMerchantId(user.getMerchantId());
			buStockLocationTypeConfigModel.setBuId(merchantBuRelModel.getBuId());
			buStockLocationTypeConfigModel.setStatus(DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
			buStockLocationTypeConfigModel = buStockLocationTypeConfigDao.searchByModel(buStockLocationTypeConfigModel);
			if (buStockLocationTypeConfigModel == null) {
				throw new RuntimeException("库位类型不存在在该主体事业部下");
			}
			updateModel.setStockLocationTypeId(buStockLocationTypeConfigModel.getId());
			updateModel.setStockLocationTypeName(buStockLocationTypeConfigModel.getName());
		}


		//修改数据“保存”时，校验公司+事业部+供应商+标准条码+币种+销售价+生效日期+失效日期同一个维度在”待审核“，”已审核“状态是否存在若存在，
		// 若是则提示：该数据已存在，若不存在则保存成功，状态更改为待审核。
		if (DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_004.equals(existModel.getState())) {
			SupplierMerchandisePriceModel validateModel = new SupplierMerchandisePriceModel();
			validateModel.setMerchantId(existModel.getMerchantId());
			validateModel.setCustomerId(existModel.getCustomerId());
			validateModel.setBuId(existModel.getBuId());
			validateModel.setCurrency(dto.getCurrency());
			validateModel.setEffectiveDate(effectiveDate);
			validateModel.setExpiryDate(expiryDate);
			validateModel.setSupplyPrice(dto.getSupplyPrice());
			validateModel.setCommbarcode(existModel.getCommbarcode());
			List<SupplierMerchandisePriceModel> existModels = sMPriceDao.list(validateModel);

			List<SupplierMerchandisePriceModel> reSumbitModels = new ArrayList<>();
			for (SupplierMerchandisePriceModel sMPrice : existModels) {
				if (DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_001.equals(sMPrice.getState()) ||
						DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003.equals(sMPrice.getState())) {
					reSumbitModels.add(sMPrice);
				}
			}

			if (reSumbitModels.size() > 0) {
				throw new RuntimeException("公司:" + existModel.getMerchantName() + "事业部:" + existModel.getBuName() +
						"标准条码:" + existModel.getCommbarcode() + "币种:" + existModel.getCurrency() + "采购价:"
						+ existModel.getSupplyPrice() + "生效日期:" + existModel.getEffectiveDate()
						+ "失效日期:" + existModel.getExpiryDate() + "存在重复数据！");
			}

			updateModel.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_001);
		}

		updateModel.setId(existModel.getId());
		updateModel.setSupplyPrice(dto.getSupplyPrice());
		updateModel.setCurrency(dto.getCurrency());
		updateModel.setEffectiveDate(TimeUtils.parseDay(dto.getEffectiveDateStr()));
		updateModel.setExpiryDate(TimeUtils.parseDay(dto.getExpiryDateStr()));
		updateModel.setModifyDate(TimeUtils.getNow());
		updateModel.setModifier(user.getId());
		updateModel.setModifierName(user.getName());
		updateModel.setRemark(dto.getRemark());
		updateModel.setCode(dto.getCode());

		sMPriceDao.modify(updateModel);

		//记录日志操作
		operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_2,existModel.getId(),"编辑",null,null);
	}

	@Override
	public SupplierMerchandisePriceDTO searchDTOById(Long id) {
		return sMPriceDao.searchDTOById(id);
	}

	@Override
	public void  submitInvalid(User user, String ids, String remark) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		if(StringUtils.isBlank(remark)){
			throw new RuntimeException("作废原因不能为空！");
		}
		Long merchantId = null;
		Long customerId = null;
		Long buId = null;
		String buName="";
		String merchantName="";
		String supplierName="";

		for (int i = 0; i < idList.size(); i++) {
			SupplierMerchandisePriceDTO supplierDto=sMPriceDao.getDetails(idList.get(i));

			if (i == 0) {
				merchantId = supplierDto.getMerchantId();
				customerId = supplierDto.getCustomerId();
				buId = supplierDto.getBuId();
				buName=supplierDto.getBuName();
				merchantName=supplierDto.getMerchantName();
				supplierName=supplierDto.getCustomerName();
			}

			if (!merchantId.equals(supplierDto.getMerchantId())) {
				throw new RuntimeException("请选择相同“公司”！");
			}

			if (!customerId.equals(supplierDto.getCustomerId())) {
				throw new RuntimeException("请选择相同“供应商”！");
			}

			if (!buId.equals(supplierDto.getBuId())) {
				throw new RuntimeException("请选择相同“事业部”！");
			}

			if(!DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003.equals(supplierDto.getState())){
				throw new RuntimeException("存在不是已审核的数据!");
			}

			supplierDto.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_005);
			supplierDto.setCancelReason(remark);
			SupplierMerchandisePriceModel model=new SupplierMerchandisePriceModel();
			BeanUtils.copyProperties(supplierDto,model);
			sMPriceDao.modify(model);
		}

		//发送邮件
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setMerchantId(merchantId);
		mailDTO.setMerchantName(merchantName);
		mailDTO.setBuId(buId);
		mailDTO.setBuName(buName);
		mailDTO.setSupplier(supplierName);
		mailDTO.setBusinessModuleType("4");
		mailDTO.setTypeName("采购价格");
		mailDTO.setType("16");// 1：提交 2：审核 3：上架 4：核销 5：开票 6：作废审核 7：盖章发票 8：审核完毕 9：作废完成 10：金额修改 11：金额确认
		mailDTO.setUserId(Arrays.asList(String.valueOf(user.getId())));
		mailDTO.setCount(idList.size());
		mailDTO.setStatus("待审核");
		mailDTO.setUserName(user.getName());

		rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());

		//插入日志
		for(Long id:idList){
			operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_2,id,"申请作废",null,remark);
		}
	}

	@Override
	public void invalidAudit(User user, String ids, String auditResult) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		Long merchantId = null;
		Long customerId = null;
		Long buId = null;
		String buName="";
		String merchantName="";
		String supplierName="";


		for (int i = 0; i < idList.size(); i++) {
			SupplierMerchandisePriceDTO supplierDto=sMPriceDao.getDetails(idList.get(i));

			if (i == 0) {
				merchantId = supplierDto.getMerchantId();
				customerId = supplierDto.getCustomerId();
				buId = supplierDto.getBuId();
				buName=supplierDto.getBuName();
				merchantName=supplierDto.getMerchantName();
				supplierName=supplierDto.getCustomerName();
			}

			if (!merchantId.equals(supplierDto.getMerchantId())) {
				throw new RuntimeException("请选择相同“公司”！");
			}

			if (!customerId.equals(supplierDto.getCustomerId())) {
				throw new RuntimeException("请选择相同“供应商”！");
			}

			if (!buId.equals(supplierDto.getBuId())) {
				throw new RuntimeException("请选择相同“事业部”！");
			}

			if(!DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_005.equals(supplierDto.getState())){
				throw new RuntimeException("存在不是待作废的数据!");
			}

			if("1".equals(auditResult)){
				supplierDto.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_007);
			}else{
				supplierDto.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003);
			}
			SupplierMerchandisePriceModel model=new SupplierMerchandisePriceModel();
			BeanUtils.copyProperties(supplierDto,model);
			sMPriceDao.modify(model);
		}

		//发送邮件
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setMerchantId(merchantId);
		mailDTO.setMerchantName(merchantName);
		mailDTO.setBuId(buId);
		mailDTO.setBuName(buName);
		mailDTO.setSupplier(supplierName);
		mailDTO.setBusinessModuleType("4");
		mailDTO.setTypeName("采购价格");
		mailDTO.setType("6");// 1：提交 2：审核 3：上架 4：核销 5：开票 6：作废审核 7：盖章发票 8：审核完毕 9：作废完成 10：金额修改 11：金额确认
		mailDTO.setUserId(Arrays.asList(String.valueOf(user.getId())));
		mailDTO.setCount(idList.size());
		mailDTO.setStatus("1".equals(auditResult)?"已通过":"已驳回");
		mailDTO.setUserName(user.getName());

		rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());

		//插入日志
		for(Long id:idList){
			operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_2,id,"作废审核","1".equals(auditResult)?"通过":"驳回","1".equals(auditResult)?"通过":"驳回");
		}
	}

	@Override
	public ResponseBean addSMPrice(User user, SupplierMerchandisePriceDTO dto) throws Exception {
		ResponseBean responseBean=new ResponseBean();
		//必填字段的校验
		if(dto.getBuId() == null){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("事业部不能为空");
			return responseBean;
		}

		MerchantBuRelModel merchantBuRelModel=new MerchantBuRelModel();
		merchantBuRelModel.setBuId(dto.getBuId());
		merchantBuRelModel.setMerchantId(user.getMerchantId());
		merchantBuRelModel.setStatus(DERP_SYS.MERCHANT_BU_REL_STATUS_1);
		merchantBuRelModel = merchantBuRelDao.searchByModel(merchantBuRelModel);
		if (merchantBuRelModel == null) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("事业部在该商家下不存在");
			return responseBean;
		}

		if(dto.getCustomerId() == null){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("供应商编码不能为空");
			return responseBean;
		}

		if (DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelModel.getStockLocationManage()) && dto.getStockLocationTypeId() == null) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("公司事业部启用库位管理，库位类型不能为空");
			return responseBean;
		}

		SupplierMerchandisePriceModel model = new SupplierMerchandisePriceModel();
		if (dto.getStockLocationTypeId() != null) {
			BuStockLocationTypeConfigModel buStockLocationTypeConfigModel = new BuStockLocationTypeConfigModel();
			buStockLocationTypeConfigModel.setId(dto.getStockLocationTypeId());
			buStockLocationTypeConfigModel.setMerchantId(user.getMerchantId());
			buStockLocationTypeConfigModel.setBuId(merchantBuRelModel.getBuId());
			buStockLocationTypeConfigModel.setStatus(DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
			buStockLocationTypeConfigModel = buStockLocationTypeConfigDao.searchByModel(buStockLocationTypeConfigModel);
			if (buStockLocationTypeConfigModel == null) {
				responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
				responseBean.setMessage("库位类型不存在在该主体事业部下");
				return responseBean;
			}

			model.setStockLocationTypeId(buStockLocationTypeConfigModel.getId());
			model.setStockLocationTypeName(buStockLocationTypeConfigModel.getName());
		}

		//判断供应商是否存在
		CustomerInfoDTO customerInfoDTO = customerInfoDao.getDetails(dto.getCustomerId());
		if(customerInfoDTO == null){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("该供应商不存在或不可用");
			return responseBean;
		}

		CustomerMerchantRelModel relModel=new CustomerMerchantRelModel();
		relModel.setCode(customerInfoDTO.getCode());
		relModel.setMerchantId(user.getMerchantId());
		relModel = customerMerchantRelDao.searchByModel(relModel);
		if (relModel==null) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("根据供应商编码没有查询到对应的供应商");
			return responseBean;
		}

		String barcode = dto.getBarcode();
		if(StringUtils.isBlank(barcode)){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("条形码不能为空");
			return responseBean;
		}

		// 报价生效日期
		String effectiveDateStr = dto.getEffectiveDateStr();
		if(StringUtils.isBlank(effectiveDateStr)){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("报价生效日期不能为空");
			return responseBean;
		}

		// 报价失效日期
		String expiryDateStr = dto.getExpiryDateStr();
		if(StringUtils.isBlank(expiryDateStr)){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("报价失效日期不能为空");
			return responseBean;
		}
		Timestamp effectiveDate = TimeUtils.parse(effectiveDateStr, "yyyy-MM-dd");//生效日期
		Timestamp expiryDate = TimeUtils.parse(expiryDateStr, "yyyy-MM-dd");//失效日期
		if (effectiveDate == null || expiryDate == null) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("报价生效日期或者报价失效日期格式不对");
			return responseBean;
		}

		//失效日期>生效日期
		if(TimeUtils.daysBetween(effectiveDate, expiryDate) < 0) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("生效日期不可超过失效日期");
			return responseBean;
		}

		String currency = dto.getCurrency();
		if(StringUtils.isBlank(currency)){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("报价币种不能为空");
			return responseBean;
		}else{
			String currencyLabel = DERP_ORDER.getLabelByKey(DERP.currencyCodeList, currency);
			if(StringUtils.isBlank(currencyLabel)){
				responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
				responseBean.setMessage("报价币种输入有误");
				return responseBean;
			}
		}

		// 供货价
		BigDecimal supplyPrice = dto.getSupplyPrice();
		if(supplyPrice == null){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("采购价不能为空");
			return responseBean;
		}

		if (supplyPrice.compareTo(BigDecimal.ZERO)==-1) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("价格不能为负数");
			return responseBean;
		}

		// 用公司+标准条码查询商品档案,拿第一条
		MerchandiseInfoModel searchMerchandiseInfoModel = new MerchandiseInfoModel();
		searchMerchandiseInfoModel.setBarcode(barcode);
		searchMerchandiseInfoModel.setMerchantId(user.getMerchantId());
		searchMerchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
		List<MerchandiseInfoModel> merchandiseList = merchandiseInfoDao.list(searchMerchandiseInfoModel);
		if(merchandiseList==null || merchandiseList.size()==0){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("根据标准条码没有找到商品");
			return responseBean;
		}

		MerchandiseInfoModel merchandiseInfoModel = merchandiseList.get(merchandiseList.size()-1);
		String commbarcode = merchandiseInfoModel.getCommbarcode();
		if (StringUtils.isBlank(commbarcode)) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("条码对应的标准条码为空");
			return responseBean;
		}

		BrandModel brandModel=null;
		if (merchandiseInfoModel.getBrandId()!=null) {
			brandModel = brandDao.searchById(merchandiseInfoModel.getBrandId());
		}
		if (brandModel==null){brandModel=new BrandModel();}


		model.setMerchantId(user.getMerchantId());
		model.setMerchantName(user.getMerchantName());
		model.setCustomerId(customerInfoDTO.getId());
		model.setCustomerCode(customerInfoDTO.getCode());
		model.setCustomerName(customerInfoDTO.getName());
		model.setCommbarcode(commbarcode);
		model.setGoodsName(merchandiseInfoModel.getName());
		model.setBrandId(brandModel.getId());
		model.setBrandName(brandModel.getName());
		model.setSpec(merchandiseInfoModel.getSpec());
		model.setCurrency(currency);
		model.setSupplyPrice(supplyPrice);
		model.setState(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_002);
		model.setEffectiveDate(effectiveDate);
		model.setExpiryDate(expiryDate);
		model.setCreater(user.getId());
		model.setCreateName(user.getName());
		model.setCreateDate(TimeUtils.getNow());
		model.setBuId(merchantBuRelModel.getBuId());
		model.setBuName(merchantBuRelModel.getBuName());
		model.setCode(dto.getCode());
		model.setRemark(dto.getRemark());

		model.setBarcode(dto.getBarcode());
		model.setDataSource(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_DATASOURCE_1);

		Long itemId = sMPriceDao.save(model);

		//记录日志操作
		operateSysLogService.saveLog(user, DERP_SYS.OREARTE_SYS_LOG_2, itemId,"页面新增",null,null);

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@Override
	public String preGetCode(Long id) throws Exception{
		String code = SourceStatusEnum.CGJGGL.name() + UUID.randomUUID().toString();
		if(id != null) {
			SupplierMerchandisePriceModel model = new SupplierMerchandisePriceModel();
			model.setId(id);
			model.setCode(code);
			sMPriceDao.modify(model);
		}
		return code;
	}

}
