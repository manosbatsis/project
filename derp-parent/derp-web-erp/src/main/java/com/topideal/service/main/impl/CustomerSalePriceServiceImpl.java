package com.topideal.service.main.impl;

import com.topideal.common.constant.DERP;
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
import com.topideal.dao.main.*;
import com.topideal.entity.dto.main.CustomerSalePriceCountDTO;
import com.topideal.entity.dto.main.CustomerSalePriceDTO;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.dto.main.ReminderEmailUserDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.main.*;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.service.base.OperateSysLogService;
import com.topideal.service.main.CustomerSalePriceService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户销售价格表 serviceImpl
 * @author lian_
 */
@Service
public class CustomerSalePriceServiceImpl implements CustomerSalePriceService {
	/*打印日志*/
	private Logger LOGGER = LoggerFactory.getLogger(CustomerSalePriceServiceImpl.class);
	@Autowired
	private CustomerInfoDao customerDao;// 客户
	@Autowired
	private CustomerMerchantRelDao customerMerchantRelDao;// 客户关联
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;// 商品
	@Autowired
	private BrandDao brandDao;// 品牌
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private MerchantBuRelDao merchantBuRelDao;
	@Autowired
	private CustomerSalePriceDao customerSalePriceDao;//客户销售价格
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
	private OperateSysLogService operateSysLogService;

	/**
	 * 分页
	 */
	@Override
	public CustomerSalePriceDTO listSalePrice(CustomerSalePriceDTO dto,User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setTotal(0);
				return dto;
			}
			dto.setBuIdList(buList);
		}
		CustomerSalePriceDTO salePriceDto=customerSalePriceDao.getListByPage(dto);
		return salePriceDto;
	}




	/**
	 * 导入客户销售价格信息
	 */
	@Override

	public Map importPriceSale(Map<Integer, List<List<Object>>> data, User user) throws Exception{
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;//成功条数
		int pass = 0;// 忽略条数
		int failure = 0;// 失败条数
		List<CustomerSalePriceModel>customerSalePriceList=new ArrayList<>();// 用于新增 销售价格管理实体

		Long merchantId = user.getMerchantId();
		String merchantName = user.getMerchantName();

		//存储不同的销售价格对象
		Map<String,Object> hashMap=new HashMap();

		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
				try {
					String customerCode = map.get("客户编码");
					if (customerCode == null || "".equals(customerCode)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("客户编码不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String buCode = map.get("事业部");
					if(buCode == null || "".equals(buCode)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("事业部不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					
					MerchantBuRelModel merchantBuRelModel=new MerchantBuRelModel();
					merchantBuRelModel.setBuCode(buCode.trim());
					merchantBuRelModel.setMerchantId(user.getMerchantId());
					merchantBuRelModel.setStatus(DERP_SYS.MERCHANT_BU_REL_STATUS_1);
					merchantBuRelModel = merchantBuRelDao.searchByModel(merchantBuRelModel);
					
					
					
					if(merchantBuRelModel==null){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("公司主体下不存在关联的事业部");
						resultList.add(message);
						failure += 1;
						continue;
					}

					String barcode = map.get("条形码");
					if (barcode == null || "".equals(barcode)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("条形码不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String currency = map.get("报价币种");
					if (currency == null || "".equals(currency)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("报价币种不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}

					if (StringUtils.isBlank(DERP.getLabelByKey(DERP.currencyCodeList, currency))) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("报价币种数值不对");
						resultList.add(message);
						failure += 1;
						continue;
					}

					String salePriceStr=map.get("价格");
					if (salePriceStr == null || "".equals(salePriceStr)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("价格不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					BigDecimal salePrice=null;
					try {
						salePrice = new BigDecimal(salePriceStr).setScale(8,BigDecimal.ROUND_HALF_UP);

					} catch (Exception e) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("价格导入格式不正确");
						resultList.add(message);
						failure += 1;
						continue;
					}
					if (salePrice.compareTo(BigDecimal.ZERO)==-1) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("价格不能为负数");
						resultList.add(message);
						failure += 1;
						continue;
					}


					String effectiveDateStr=map.get("报价生效日期");
					if (effectiveDateStr == null || "".equals(effectiveDateStr)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("报价生效日期不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					String expiryDateStr=map.get("报价失效日期");
					if (expiryDateStr == null || "".equals(expiryDateStr)) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("报价失效日期不能为空");
						resultList.add(message);
						failure += 1;
						continue;
					}
					Timestamp effectiveDate = TimeUtils.parse(effectiveDateStr, "yyyy-MM-dd");//生效日期
					Timestamp expiryDate = TimeUtils.parse(expiryDateStr, "yyyy-MM-dd");//失效日期
					if (effectiveDate==null||expiryDate==null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("生产日期或者失效日期格式不对");
						resultList.add(message);
						failure += 1;
						continue;
					}
					// 失效日期必须大于生效日期
					if (expiryDate.getTime()<effectiveDate.getTime()) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("失效日期必须大于生效日期");
						resultList.add(message);
						failure += 1;
						continue;
					}
					// 根据客户编码查询客户
					CustomerMerchantRelModel relModel=new CustomerMerchantRelModel();
					relModel.setCode(customerCode);
					relModel.setMerchantId(merchantId);
					relModel = customerMerchantRelDao.searchByModel(relModel);
					if (relModel==null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("根据客户编码没有查询到对应的客户");
						resultList.add(message);
						failure += 1;
						continue;
					}
					// 根据商家和货号查询商品信息
					MerchandiseInfoModel merchandiseInfoModel=new MerchandiseInfoModel();
					merchandiseInfoModel.setBarcode(barcode);
					merchandiseInfoModel.setMerchantId(merchantId);
					merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
					List<MerchandiseInfoModel>merchandiseInfoList = merchandiseInfoDao.list(merchandiseInfoModel);
					if (merchandiseInfoList.size()==0) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("根据条码没有找到商品信息");
						resultList.add(message);
						failure += 1;
						continue;
					}
					merchandiseInfoModel = merchandiseInfoList.get(merchandiseInfoList.size()-1);
					
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
					if (brandModel==null){ brandModel=new BrandModel();}
					CustomerInfoModel customerInfoModel=new CustomerInfoModel();
					customerInfoModel.setId(relModel.getCustomerId());
					customerInfoModel.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
					customerInfoModel.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);
					customerInfoModel = customerDao.searchByModel(customerInfoModel);
					if (customerInfoModel==null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("根据客户编码没有找到客户或客户未启用");
						resultList.add(message);
						failure += 1;
						continue;
					}

					//校验以公司+事业部+客户+标准条码+币种+销售价+生效日期+失效日期
					String str=merchantId+"_"+buCode+"_"+customerInfoModel.getId()+"_"+commbarcode+"_"+currency+"_"+salePrice+"_"+effectiveDateStr+"_"+expiryDateStr;
					if(hashMap.containsKey(str)){
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage("导入的记录不能为相同的公司+事业部+客户+标准条码+币种+销售价格+生效日期+失效日期");
						resultList.add(message);
						failure += 1;
						continue;
					}
					hashMap.put(str,map);

					// 销售价格管理实体
					CustomerSalePriceModel customerSalePriceModel = new CustomerSalePriceModel();
					customerSalePriceModel.setCommbarcode(commbarcode);
					customerSalePriceModel.setGoodsName(merchandiseInfoModel.getName());
					customerSalePriceModel.setMerchantId(merchantId);
					customerSalePriceModel.setMerchantName(merchantName);
					customerSalePriceModel.setCustomerId(customerInfoModel.getId());
					customerSalePriceModel.setCustomerName(customerInfoModel.getName());
					customerSalePriceModel.setCustomerCode(customerInfoModel.getCode());
					customerSalePriceModel.setStatus(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_002);
					customerSalePriceModel.setBrandId(brandModel.getId());
					customerSalePriceModel.setBrandName(brandModel.getName());
					customerSalePriceModel.setExpiryDate(expiryDate);
					customerSalePriceModel.setEffectiveDate(effectiveDate);
					customerSalePriceModel.setCurrency(currency);
					customerSalePriceModel.setSalePrice(salePrice);
					customerSalePriceModel.setSpec(merchandiseInfoModel.getSpec());
					customerSalePriceModel.setCreater(user.getId());
					customerSalePriceModel.setCreateName(user.getName());
					customerSalePriceModel.setBuId(merchantBuRelModel.getBuId());
					customerSalePriceModel.setBuName(merchantBuRelModel.getBuName());
					//根据事业部查询事业部id和事业部名称
					//MerchantBuRelMongo mongo=merchantBuRelMongoDao.findBuName(merchantId,buCode);
					/*if(mongo!=null){
						customerSalePriceModel.setBuId(mongo.getBuId());
						customerSalePriceModel.setBuName(mongo.getBuName());
					}*/
					customerSalePriceList.add(customerSalePriceModel);

				} catch (SQLException e) {
					e.printStackTrace();
					failure += 1;
				}
			}


			//新增
			if (failure==0) {
				// 新增销售价格管理
				for (CustomerSalePriceModel customerSalePriceModel : customerSalePriceList) {
					Long num = customerSalePriceDao.save(customerSalePriceModel);
					if (num>0) {
						success=success+1;
					}
					
					operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_3, num ,"导入", null, null);
				}
			}
		}
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		if(resultList.size()>0){//导入失败
			map.put("code", "01");
		}else{
			map.put("code", "00");
		}
		return map;
	}

	/**
	 * 把两个数组组成一个map
	 * @param keys 键数组
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
	 * 删除
	 */
	@Override

	public boolean delPriceSale(List<Long> ids) throws SQLException {
		//int num = dao.delete(ids);
		boolean flag=false;
		for (Long id : ids) {
			CustomerSalePriceModel sMModel = customerSalePriceDao.searchById(Long.parseLong(id.toString()));
			//单据状态是已审核
			if(sMModel.getStatus() != null && sMModel.getStatus().equals(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003)){
				flag = true;
				break;
			}
		}
		if(flag){
			throw new RuntimeException("删除失败，只能删除待提交、待审核、已驳回数据");
		}
		if(!flag){
			for (Long id : ids) {
				CustomerSalePriceModel customerSalePriceModel = new CustomerSalePriceModel();
				customerSalePriceModel.setId(id);
				customerSalePriceModel.setStatus(DERP.DEL_CODE_006);
				int modify = customerSalePriceDao.modify(customerSalePriceModel);
			}
		}
		return true;
	}

	@Override
	public CustomerSalePriceDTO searchDetail(Long id) throws SQLException {
		CustomerSalePriceDTO customerSalePriceDTO = new CustomerSalePriceDTO();
		customerSalePriceDTO.setId(id);
		return customerSalePriceDao.searchByDTO(customerSalePriceDTO);
	}



	/**
	 * 根据表头id获取表体信息
	 */
	@Override
	public List<CustomerSalePriceModel> listItemBySalePriceId(Long id) throws SQLException {
		CustomerSalePriceModel model = new CustomerSalePriceModel();
		model.setId(id);
		return customerSalePriceDao.list(model);
	}

	/**
	 * 根据客户ID回显客户编码和主数据客户ID
	 */
	@Override
	public List<CustomerSalePriceModel> getCodeById(Long id) throws SQLException {
		return customerSalePriceDao.getCodeById(id);
	}

	/**
	 * 列表查询
	 */
	@Override
	public List<CustomerSalePriceModel> list(CustomerSalePriceModel model) throws SQLException {
		return customerSalePriceDao.list(model);
	}

	/**
	 * 获取状态数量
	 * @param dtoSalePrice
	 * @return
	 */
	@Override
	public CustomerSalePriceCountDTO getCountStatus(CustomerSalePriceDTO dtoSalePrice,User user) {
		CustomerSalePriceCountDTO dto=new CustomerSalePriceCountDTO();
		if(dtoSalePrice.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dtoSalePrice.setTotal(0);
				return dto;
			}
			dtoSalePrice.setBuIdList(buList);
		}
		List<Map<String,Object>> listMap=customerSalePriceDao.getCountStatus(dtoSalePrice);
		if(listMap.size() > 0){
			for(Map<String,Object> map:listMap){
				dto.setBeAll((Long)map.get("beAll"));
				dto.setBeAudit((Long)map.get("beAudit"));
				dto.setBeSubmited((Long)map.get("beSubmited"));
				dto.setBeAreadyAudit((Long)map.get("beAreadyAudit"));
				dto.setBeRejected((Long)map.get("beRejected"));
				dto.setBeInvalid((Long)map.get("beInvalid"));
				dto.setBeAlreadyInvalid((Long)map.get("beAlreadyInvalid"));
			}
		}
		return dto;
	}

	@Override
	public Map editSalePriceModel(CustomerSalePriceDTO dto, User user) throws SQLException {
		Map map=new HashMap();
		if(StringUtils.isBlank(dto.getCode())){
			map.put("code","01");
			map.put("message","编码不能为空");
			return map;
		}

		CustomerSalePriceModel model=customerSalePriceDao.searchById(dto.getId());
		if(model==null){
			map.put("code","01");
			map.put("message","不存在该销售价格记录");
			return map;
		}
		if (dto.getExpiryDate().getTime()<dto.getEffectiveDate().getTime()) {
			map.put("code","01");
			map.put("message","失效日期必须大于生效日期");
			return map;
		}
		//驳回保存后状态改为待审核
		if(model.getStatus().equals(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_004)){
			dto.setStatus(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_001);

			//校验
			CustomerSalePriceModel validateModel=new CustomerSalePriceModel();
			validateModel.setMerchantId(model.getMerchantId());
			validateModel.setCustomerId(model.getCustomerId());
			validateModel.setBuId(model.getBuId());
			validateModel.setCurrency(dto.getCurrency());
			validateModel.setCommbarcode(model.getCommbarcode());
			validateModel.setEffectiveDate(dto.getEffectiveDate());
			validateModel.setExpiryDate(dto.getExpiryDate());
			validateModel.setSalePrice(dto.getSalePrice());
			List<CustomerSalePriceModel> existModels = customerSalePriceDao.list(validateModel);
			if(existModels.size()>0){
				for(CustomerSalePriceModel item:existModels){
					if(item.getStatus().equals(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_001) || item.getStatus().equals(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003)){
						map.put("code","01");
						map.put("message","数据已经存在“待审核”或“已审核”列表中");
						return map;
					}
				}
			}
		}
		CustomerSalePriceModel upadateModel=new CustomerSalePriceModel();
		upadateModel.setModifierName(user.getName());
		upadateModel.setModifier(user.getId());
		upadateModel.setModifyDate(TimeUtils.getNow());
		BeanUtils.copyProperties(dto,upadateModel);
		customerSalePriceDao.modify(upadateModel);
		
		operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_3, upadateModel.getId() ,"编辑", null, null);
		
		map.put("code","00");
		map.put("message","保存成功");
		return map;
	}


	/**
	 * 审核
	 */
	@Override
	public Map<String,Object>  auditSMPrice(String ids, User user,String type) throws SQLException {
		Map<String,Object> returnMap=new HashMap();
		//转换为list
		List<Long> list = StrUtils.parseIds(ids);
		//存储提交人
		List<String> submitId=new ArrayList<>();

		Long merchantId=null;
		Long buId=null;
		Long customerId=null;

		CustomerSalePriceModel detailModel=new CustomerSalePriceModel();
		for (int i = 0; i < list.size(); i++) {
			detailModel = customerSalePriceDao.searchById(list.get(i));
			if (i == 0) {
				merchantId = detailModel.getMerchantId();
				customerId = detailModel.getCustomerId();
				buId = detailModel.getBuId();
			}
			if (!merchantId.equals(detailModel.getMerchantId())) {
				throw new RuntimeException("请选择相同“公司”！");
			}
			if (!customerId.equals(detailModel.getCustomerId())) {
				throw new RuntimeException("请选择相同“供应商”！");
			}
			if (!buId.equals(detailModel.getBuId())) {
				throw new RuntimeException("请选择相同“事业部”！");
			}
			if (!DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_001.equals(detailModel.getStatus())) {
				returnMap.put("code","01");
				returnMap.put("message","仅对状态为”待审核“的可操作审核");
				return returnMap;
			}
			//获取提交人id
			submitId.add(String.valueOf(detailModel.getSubmitId()));
			//创建对象
			CustomerSalePriceModel model= new CustomerSalePriceModel();
			model.setId(list.get(i));
			if("1".equals(type)){//1 通过 2驳回
				model.setStatus(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003);//已审核
			}else{
				model.setStatus(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_004);//已驳回
			}
			model.setAuditName(user.getName());
			model.setAuditor(user.getId());
			model.setAuditDate(TimeUtils.getNow());
			customerSalePriceDao.modify(model);
			
			operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_3, list.get(i) ,"审核", null, "1".equals(type)?"已通过":"已驳回");
		}
		//销售价格发送邮件提醒
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setBusinessModuleType("5");
		mailDTO.setTypeName("销售价格");
		mailDTO.setType("2");// 1：提交 2：审核
		mailDTO.setMerchantId(detailModel.getMerchantId());
		mailDTO.setMerchantName(detailModel.getMerchantName());
		mailDTO.setBuId(detailModel.getBuId());
		mailDTO.setBuName(detailModel.getBuName());
		mailDTO.setSupplier(detailModel.getCustomerName());
		mailDTO.setSubmitId(submitId);//提交人多个
		mailDTO.setUserName(user.getName());
		mailDTO.setStatus("1".equals(type)?"已通过":"已驳回");
		mailDTO.setCount(list.size());

		try {
			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());
		} catch (Exception e) {
			LOGGER.error("----------------------销售价格审核结束，发送邮件失败----------------------");
		}
		returnMap.put("code","00");
		returnMap.put("message","审核成功！");
		return returnMap;
	}


	/**
	 * 提交
	 * @param ids
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map submitSMPrice(String ids,User user) throws SQLException {
		Map<String,Object> returnMap=new HashMap();

		//遍历为list
		List<Long> list = StrUtils.parseIds(ids);

		Long merchantId=null;
		Long buId=null;
		Long customerId=null;

		List<CustomerSalePriceModel> customerSalePriceModelList = new ArrayList<>();
		CustomerSalePriceModel searchDetail=new CustomerSalePriceModel();
		for(int i = 0; i < list.size(); i++){
			searchDetail = customerSalePriceDao.searchById(list.get(i));
			customerSalePriceModelList.add(searchDetail);
			if (i == 0) {
				merchantId = searchDetail.getMerchantId();
				customerId = searchDetail.getCustomerId();
				buId = searchDetail.getBuId();
			}
			if (!merchantId.equals(searchDetail.getMerchantId())) {
				throw new RuntimeException("请选择相同“公司”！");
			}
			if (!customerId.equals(searchDetail.getCustomerId())) {
				throw new RuntimeException("请选择相同“供应商”！");
			}
			if (!buId.equals(searchDetail.getBuId())) {
				throw new RuntimeException("请选择相同“事业部”！");
			}
			if (!DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_002.equals(searchDetail.getStatus())) {
				returnMap.put("code","01");
				returnMap.put("message","仅对状态为”待提交“的可操作审核");
				return returnMap;
			}
			CustomerSalePriceModel validateModel=new CustomerSalePriceModel();
			validateModel.setMerchantId(searchDetail.getMerchantId());
			validateModel.setCustomerId(searchDetail.getCustomerId());
			validateModel.setBuId(searchDetail.getBuId());
			validateModel.setCommbarcode(searchDetail.getCommbarcode());
			validateModel.setCurrency(searchDetail.getCurrency());
			validateModel.setEffectiveDate(searchDetail.getEffectiveDate());
			validateModel.setExpiryDate(searchDetail.getExpiryDate());
			validateModel.setSalePrice(searchDetail.getSalePrice());
			List<CustomerSalePriceModel> existModels = customerSalePriceDao.list(validateModel);
			if(existModels.size()>0){
				for(CustomerSalePriceModel model:existModels){
					if(model.getStatus().equals(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_001) ||
							model.getStatus().equals(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003) ||
							model.getStatus().equals(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_004) || 
							model.getStatus().equals(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_007)){
						returnMap.put("code","01");
						returnMap.put("message","数据已经存在“待审核”、“已审核、“已驳回”或“待作废”列表中");
						return returnMap;
					}
				}
			}
		}

		// 选中多个对象时检查（公司、事业部、供应商、条形码、币种、采购价、报价生效时间、报价失效时间）是否存在多条，若是则提示XXX存在多条，请勿重复提交！
		Map<String, List<CustomerSalePriceModel>> collect = customerSalePriceModelList.stream().collect(Collectors.groupingBy(entity -> {
			String key = entity.getMerchantId() + "_" + entity.getBuId() + "_" + entity.getCustomerId()
					+ "_" + entity.getCommbarcode() + "_" + entity.getCurrency() + "_" + entity.getSalePrice()
					+ "_" + entity.getEffectiveDate() + "_" + entity.getExpiryDate();
			return key;
		}));

		collect.entrySet().stream().forEach(entity -> {
			int size = entity.getValue().size();
			if(size > 1) {
				CustomerSalePriceModel value = entity.getValue().get(0);
				throw new RuntimeException("公司:" + value.getMerchantName() + ",事业部:" + value.getBuName()
						+ ",供应商:" + value.getCustomerName() + ",标准条码:" + value.getCommbarcode()
						+ ",币种" + value.getCurrency()
						+ "存在多条，请勿重复提交");
			}
		});

		//修改状态为待审核
		for (Long idObj : list) {
			CustomerSalePriceModel model= new CustomerSalePriceModel();
			model.setId(idObj);
			model.setStatus(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_001);
			model.setSubmitName(user.getName());
			model.setSubmitId(user.getId());
			model.setSubmitDate(TimeUtils.getNow());
			customerSalePriceDao.modify(model);
			
			operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_3, idObj ,"提交", null, null);
		}

		//销售价格发送邮件提醒
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setBusinessModuleType("5");
		mailDTO.setTypeName("销售价格");
		mailDTO.setType("1");// 1：提交 2：审核
		mailDTO.setMerchantId(searchDetail.getMerchantId());
		mailDTO.setMerchantName(searchDetail.getMerchantName());
		mailDTO.setBuId(searchDetail.getBuId());
		mailDTO.setBuName(searchDetail.getBuName());
		mailDTO.setSupplier(searchDetail.getCustomerName());
		mailDTO.setSubmitId(Arrays.asList(String.valueOf(user.getId())));
		mailDTO.setUserName(user.getName());
		mailDTO.setStatus("待审核");
		mailDTO.setCount(list.size());
		try {
			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());
		} catch (Exception e) {
			LOGGER.error("----------------------销售价格提交结束，发送邮件失败----------------------");
		}
		returnMap.put("code","00");
		returnMap.put("message","提交成功");
		return returnMap;
	}


	/**
	 * 导出
	 */
	@Override
	public List<CustomerSalePriceDTO> getExportList(CustomerSalePriceDTO dto,User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setTotal(0);
				return null;
			}
			dto.setBuIdList(buList);
		}
		return customerSalePriceDao.getExportList(dto);
	}
	@Override
	public void submitInvalid(User user, String ids, String remark) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		if(StringUtils.isBlank(remark)){
			throw new RuntimeException("作废原因不能为空！");
		}
		Long merchantId = null;
		Long customerId = null;
		Long buId = null;
		String merchantName = "";
		String customerName = "";
		String buName = "";
		//选中多个对象时检查公司+事业部+客户是否一致，若不一致，提示请选择相同 公司或事业部、客户
		for (Long id: idList) {
			CustomerSalePriceModel model = customerSalePriceDao.searchById(id);

			if(merchantId == null) {
				merchantId = model.getMerchantId();
				merchantName = model.getMerchantName();
			}
			if(customerId == null) {
				customerId = model.getCustomerId();	
				customerName = model.getCustomerName();
			}
			if(buId == null) {
				buId = model.getBuId();
				buName = model.getBuName();
			}

			if (!merchantId.equals(model.getMerchantId())) {
				throw new RuntimeException("请选择相同“公司”！");
			}

			if (!customerId.equals(model.getCustomerId())) {
				throw new RuntimeException("请选择相同“客户”！");
			}

			if (!buId.equals(model.getBuId())) {
				throw new RuntimeException("请选择相同“事业部”！");
			}

			if(!DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003.equals(model.getStatus())){
				throw new RuntimeException("存在不是已审核的数据!");
			}

			model.setStatus(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_005);
			model.setModifier(user.getId());
			model.setModifierName(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			customerSalePriceDao.modify(model);
			
			operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_3, id ,"申请作废", null, remark);
		}
		//销售价格发送邮件提醒
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setBusinessModuleType("5");
		mailDTO.setTypeName("销售价格");
		mailDTO.setType("16");// 16:作废提交
		mailDTO.setMerchantId(merchantId);
		mailDTO.setMerchantName(merchantName);
		mailDTO.setBuId(buId);
		mailDTO.setBuName(buName);
		mailDTO.setSupplier(customerName);
		mailDTO.setUserId(Arrays.asList(String.valueOf(user.getId())));
		mailDTO.setUserName(user.getName());
		mailDTO.setStatus("待审核");
		mailDTO.setCount(idList.size());

		try {
			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());
		} catch (Exception e) {
			LOGGER.error("----------------------销售价格申请作废结束，发送邮件失败----------------------");
		}
		
	}

	@Override
	public void auditInvalid(User user, String ids, String auditResult) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);

		Long merchantId = null;
		Long customerId = null;
		Long buId = null;
		String merchantName = "";
		String customerName = "";
		String buName = "";
		//选中多个对象时检查公司+事业部+客户是否一致，若不一致，提示请选择相同 公司或事业部、客户
		for (Long id: idList) {
			CustomerSalePriceModel model = customerSalePriceDao.searchById(id);

			if(merchantId == null) {
				merchantId = model.getMerchantId();
				merchantName = model.getMerchantName();
			}
			if(customerId == null) {
				customerId = model.getCustomerId();	
				customerName = model.getCustomerName();
			}
			if(buId == null) {
				buId = model.getBuId();
				buName = model.getBuName();
			}

			if (!merchantId.equals(model.getMerchantId())) {
				throw new RuntimeException("请选择相同“公司”！");
			}

			if (!customerId.equals(model.getCustomerId())) {
				throw new RuntimeException("请选择相同“客户”！");
			}

			if (!buId.equals(model.getBuId())) {
				throw new RuntimeException("请选择相同“事业部”！");
			}

			if(!DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_005.equals(model.getStatus())){
				throw new RuntimeException("存在不是待作废的数据!");
			}
			if("1".equals(auditResult)){//1 通过 2驳回
				model.setStatus(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_007);//已作废
			}else{
				model.setStatus(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003);//已审核
			}
			model.setModifier(user.getId());
			model.setModifierName(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			customerSalePriceDao.modify(model);
			
			operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_3, id ,"审核", "作废审核", "1".equals(auditResult)?"已通过":"已驳回");
		}
		//销售价格发送邮件提醒
		ReminderEmailUserDTO mailDTO = new ReminderEmailUserDTO();
		mailDTO.setBusinessModuleType("5");
		mailDTO.setTypeName("销售价格");
		mailDTO.setType("6");// 6：作废审核
		mailDTO.setMerchantId(merchantId);
		mailDTO.setMerchantName(merchantName);
		mailDTO.setBuId(buId);
		mailDTO.setBuName(buName);
		mailDTO.setSupplier(customerName);
		mailDTO.setUserId(Arrays.asList(String.valueOf(user.getId())));
		mailDTO.setUserName(user.getName());
		mailDTO.setStatus("1".equals(auditResult)?"已通过":"已驳回");
		mailDTO.setCount(idList.size());

		try {
			rocketMQProducer.send(JSONObject.fromObject(mailDTO).toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(), MQErpEnum.SEND_REMINDER_MAIL.getTags());
		} catch (Exception e) {
			LOGGER.error("----------------------销售价格作废审核结束，发送邮件失败----------------------");
		}
		
	}

	@Override
	public ResponseBean addCustomerPrice(User user, CustomerSalePriceDTO dto) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		Long customerId = dto.getCustomerId();
		if (customerId == null) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("客户编码不能为空");
			return responseBean;
		}

		Long buId = dto.getBuId();
		if(buId == null){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("事业部不能为空");
			return responseBean;
		}

		MerchantBuRelModel merchantBuRelModel=new MerchantBuRelModel();
		merchantBuRelModel.setBuId(buId);
		merchantBuRelModel.setMerchantId(user.getMerchantId());
		merchantBuRelModel.setStatus(DERP_SYS.MERCHANT_BU_REL_STATUS_1);
		merchantBuRelModel = merchantBuRelDao.searchByModel(merchantBuRelModel);

		if(merchantBuRelModel==null){
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("公司主体下不存在关联的事业部");
			return responseBean;
		}

		String barcode = dto.getBarcode();
		if (StringUtils.isBlank(barcode)) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("条形码不能为空");
			return responseBean;
		}

		String currency = dto.getCurrency();
		if (StringUtils.isBlank(currency)) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("报价币种不能为空");
			return responseBean;
		}

		if (StringUtils.isBlank(DERP.getLabelByKey(DERP.currencyCodeList, currency))) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("报价币种数值不对");
			return responseBean;
		}

		BigDecimal salePrice = dto.getSalePrice();
		if (salePrice == null) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("价格不能为空");
			return responseBean;
		}

		if (salePrice.compareTo(BigDecimal.ZERO)==-1) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("价格不能为负数");
			return responseBean;
		}

		String effectiveDateStr = dto.getEffectiveDateStr();
		if (StringUtils.isBlank(effectiveDateStr)) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("报价生效日期不能为空");
			return responseBean;
		}
		String expiryDateStr = dto.getExpiryDateStr();
		if (StringUtils.isBlank(expiryDateStr)) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("报价失效日期不能为空");
			return responseBean;
		}

		Timestamp effectiveDate = TimeUtils.parse(effectiveDateStr, "yyyy-MM-dd");//生效日期
		Timestamp expiryDate = TimeUtils.parse(expiryDateStr, "yyyy-MM-dd");//失效日期
		if (effectiveDate==null||expiryDate==null) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("生产日期或者失效日期格式不对");
			return responseBean;
		}

		// 失效日期必须大于生效日期
		if (expiryDate.getTime()<effectiveDate.getTime()) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("失效日期必须大于生效日期");
			return responseBean;
		}

		// 根据客户编码查询客户
		CustomerMerchantRelModel relModel=new CustomerMerchantRelModel();
		relModel.setCustomerId(customerId);
		relModel.setMerchantId(user.getMerchantId());
		relModel = customerMerchantRelDao.searchByModel(relModel);
		if (relModel==null) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("根据客户编码没有查询到对应的客户");
			return responseBean;
		}
		// 根据商家和货号查询商品信息
		MerchandiseInfoModel merchandiseInfoModel=new MerchandiseInfoModel();
		merchandiseInfoModel.setBarcode(barcode);
		merchandiseInfoModel.setMerchantId(user.getMerchantId());
		merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
		List<MerchandiseInfoModel>merchandiseInfoList = merchandiseInfoDao.list(merchandiseInfoModel);
		if (merchandiseInfoList.size()==0) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("根据条码没有找到商品信息");
			return responseBean;
		}
		merchandiseInfoModel = merchandiseInfoList.get(merchandiseInfoList.size()-1);

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

		if (brandModel==null){ brandModel=new BrandModel();}
		CustomerInfoModel customerInfoModel=new CustomerInfoModel();
		customerInfoModel.setId(relModel.getCustomerId());
		customerInfoModel.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
		customerInfoModel.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);
		customerInfoModel = customerDao.searchByModel(customerInfoModel);
		if (customerInfoModel==null) {
			responseBean.setCode(MessageEnum.MESSAGE_10009.getCode());
			responseBean.setMessage("根据客户编码没有找到客户或客户未启用");
			return responseBean;
		}

		// 销售价格管理实体
		CustomerSalePriceModel customerSalePriceModel = new CustomerSalePriceModel();
		customerSalePriceModel.setCommbarcode(commbarcode);
		customerSalePriceModel.setGoodsName(merchandiseInfoModel.getName());
		customerSalePriceModel.setMerchantId(user.getMerchantId());
		customerSalePriceModel.setMerchantName(user.getMerchantName());
		customerSalePriceModel.setCustomerId(customerInfoModel.getId());
		customerSalePriceModel.setCustomerName(customerInfoModel.getName());
		customerSalePriceModel.setCustomerCode(customerInfoModel.getCode());
		customerSalePriceModel.setStatus(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_002);
		customerSalePriceModel.setBrandId(brandModel.getId());
		customerSalePriceModel.setBrandName(brandModel.getName());
		customerSalePriceModel.setExpiryDate(expiryDate);
		customerSalePriceModel.setEffectiveDate(effectiveDate);
		customerSalePriceModel.setCurrency(currency);
		customerSalePriceModel.setSalePrice(salePrice);
		customerSalePriceModel.setSpec(merchandiseInfoModel.getSpec());
		customerSalePriceModel.setCreater(user.getId());
		customerSalePriceModel.setCreateName(user.getName());
		customerSalePriceModel.setBuId(merchantBuRelModel.getBuId());
		customerSalePriceModel.setBuName(merchantBuRelModel.getBuName());
		customerSalePriceModel.setCode(dto.getCode());
		customerSalePriceModel.setRemark(dto.getRemark());

		Long id = customerSalePriceDao.save(customerSalePriceModel);

		//记录日志
		operateSysLogService.saveLog(user,DERP_SYS.OREARTE_SYS_LOG_3, id ,"页面新增", null, null);

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@Override
	public String preGetCode(Long id) throws Exception{
		String code = SourceStatusEnum.XSJGGL.name() + UUID.randomUUID().toString();
		if(id != null) {
			CustomerSalePriceModel model = new CustomerSalePriceModel();
			model.setId(id);
			model.setCode(code);
			customerSalePriceDao.modify(model);
		}
		return code;
	}

}
