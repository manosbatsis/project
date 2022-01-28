package com.topideal.service.main.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CustomerAptitudeDao;
import com.topideal.dao.main.CustomerBankDao;
import com.topideal.dao.main.CustomerBankMerchantRelDao;
import com.topideal.dao.main.CustomerInfoDao;
import com.topideal.dao.main.CustomerMainDao;
import com.topideal.dao.main.CustomerMerchantRelDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.dao.main.TariffRateConfigDao;
import com.topideal.dao.user.UserMerchantRelDao;
import com.topideal.entity.dto.main.CustomerInfoDTO;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.CustomerAptitudeModel;
import com.topideal.entity.vo.main.CustomerBankMerchantRelModel;
import com.topideal.entity.vo.main.CustomerBankModel;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.CustomerMainModel;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.TariffRateConfigModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.service.main.CustomerService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 客户信息service实现类
 * 
 * @author zhanghx
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	// 客户信息
	@Autowired
	private CustomerInfoDao customerInfoDao;
	@Autowired
	private CustomerAptitudeDao customerAptitudeDao;

	@Autowired
	private MerchantInfoDao merchantInfoDao ;
	@Autowired
	private CustomerMerchantRelDao customerMerchantRelDao ;
	@Autowired
	private CustomerMainDao customerMainDao ;
	@Autowired
	private TariffRateConfigDao tariffRateConfigDao ;
	@Autowired
	private UserMerchantRelDao userMerchantRelDao ;
	@Autowired
	private CustomerBankDao customerBankDao;
	@Autowired 
	private CustomerBankMerchantRelDao customerBankMerchantRelDao;
	
	// mq
	@Autowired
	private RMQProducer rocketMQProducer;
	
	

	/**
	 * 客户合作公司
	 */
	@Override
	public Map<String, Object> saveMerchantRelJSon(JSONArray jSONArray,String customerId,User user) throws Exception {
		
		Map<String, Object>map=new HashMap<>();
		map.put("code", "00");
		map.put("message", "成功");
		
		CustomerInfoModel selectedCustomer = customerInfoDao.searchById(Long.valueOf(customerId));
		for (Object object : jSONArray) {
			JSONObject jsondata=(JSONObject) object;
			if (jsondata.get("merchantId")==null) {
				map.put("code", "01");
				map.put("message", "商家id不能为空");
				return map;
			}
			String merchantId =  jsondata.getString("merchantId");
			//String businessModel = (String) jsondata.get("businessModel");
			//String accountPeriod = (String) jsondata.get("accountPeriod");
			if(merchantId.equals(selectedCustomer.getInnerMerchantId())) {
				map.put("code", "01");
				map.put("message", "该公司已设为内部公司，无法关联");
				return map;
			}
		}

		
		// 获取删除id 此用户管理的商家 对应的商家客户关系数据
		List<Long> delList=new ArrayList<>();		
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("customerId", customerId);
		paramMap.put("userId", user.getId());
		List<CustomerMerchantRelModel> relQureyList = customerMerchantRelDao.getCustMerListByUser(paramMap);
		if (relQureyList!=null&&relQureyList.size()>0) {
			for (CustomerMerchantRelModel model : relQureyList) {
				delList.add(model.getId());
			}
		}
		
		List<CustomerMerchantRelModel> saveList=new ArrayList<>();
		//封装实体
		for (Object object : jSONArray) {
			JSONObject jsondata=(JSONObject) object;
			String merchantId = jsondata.getString("merchantId");
			String businessModel=null;
			if (jsondata.get("businessModel")!=null&&!"null".equals(jsondata.getString("businessModel"))) businessModel = jsondata.getString("businessModel");
			String accountPeriod = null;
			if (jsondata.get("accountPeriod")!=null&&!"null".equals(jsondata.getString("accountPeriod"))) accountPeriod =  jsondata.getString("accountPeriod");		
			
			MerchantInfoModel merchant = merchantInfoDao.searchById(Long.valueOf(merchantId));			
			CustomerMerchantRelModel saveModel = new CustomerMerchantRelModel() ;

			saveModel.setCode(selectedCustomer.getCode());
			saveModel.setCustomerId(Long.valueOf(customerId));
			saveModel.setName(selectedCustomer.getName());
			saveModel.setMerchantId(merchant.getId());
			saveModel.setMerchantName(merchant.getName());
			saveModel.setStatus(selectedCustomer.getStatus());// 客户的状态
			saveModel.setCreateDate(TimeUtils.getNow());
			saveModel.setCreater(user.getId());
			saveModel.setModifyDate(TimeUtils.getNow());
			saveModel.setBusinessModel(businessModel);
			saveModel.setAccountPeriod(accountPeriod);
			saveModel.setSalePriceManage(DERP_SYS.SALE_PRICE_MANAGE_1);
			saveList.add(saveModel);
		}
		//客户关系表
		if (delList!=null&&delList.size()>0) {
			customerMerchantRelDao.delete(delList);
		}
		
		// 新增
		for (CustomerMerchantRelModel model : saveList) {
			customerMerchantRelDao.save(model);
		}
		
		return map;
	}

	/**
	 * 根据id获取客户详情
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public CustomerInfoDTO searchDetail(Long id) throws SQLException {
		return customerInfoDao.getDetails(id);
	}

	/**
	 * 客户列表（分页）
	 * 
	 * @param dto
	 * @return
	 */
	@Override
	public CustomerInfoDTO listCustomer(CustomerInfoDTO dto)
			throws SQLException {
		return customerInfoDao.searchDTOByPage(dto);
	}

	/**
	 * 根据客户id删除客户(支持批量)
	 * 
	 * @param ids
	 * @return
	 */
	@Override

	public boolean delCustomer(List ids) throws SQLException {
		int num = customerInfoDao.delete(ids);
		
		for (Object idObj : ids) {
			Map<String, Object> param=new HashMap<>();
			if (idObj==null)continue;
			param.put("customerId", Long.valueOf(idObj.toString()));
			// 删除客户银行数据
			customerBankDao.deleteByParam(param);
			// 删除商家客户银行数据
			customerBankMerchantRelDao.deleteByParam(param);
		}
		
		if (num >= 1) {
			return true;
		}
		return false;
	}

	/**
	 * 客户信息
	 * 新增客户
	 * @param model
	 * @return
	 */
	@Override
	public Map<String,Object> saveCustomer(CustomerInfoDTO model,JSONArray arry,JSONArray bankArry) throws Exception {
		Map<String,Object> map = new  HashMap<>();
		// 1成功 0失败
		map.put("status", "1");
		map.put("massage", "成功");
		// 客户编码
		model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ERP));
		if(StringUtils.isNotBlank(model.getMainId())){
			//判断主数据id是否已存在
			CustomerInfoModel customertemp = new CustomerInfoModel();
			customertemp.setCusType(model.getCusType());
			customertemp.setMainId(model.getMainId());
			List<CustomerInfoModel> list =  customerInfoDao .list(customertemp);
			if(list!=null&&list.size()>0){
				map.put("massage", "主数据ID已存在");
				map.put("status", "0");
				return map;
			}
		}
		if(StringUtils.isNotBlank(model.getMainId())) {
			CustomerMainModel customerMainQuery = new CustomerMainModel() ;
			customerMainQuery.setCcode(model.getMainId());
			customerMainQuery = customerMainDao.searchByModel(customerMainQuery) ;
			if(customerMainQuery != null) {
				CustomerMainModel updateCustomerMain= new CustomerMainModel() ;
				updateCustomerMain.setId(customerMainQuery.getId());
				if(DERP_SYS.CUSTOMERINFO_CUSTYPE_2.equals(model.getCusType())) {
					updateCustomerMain.setIsSupplier(DERP_SYS.CUSTOMER_MAIN_IS_1);
				}else if(DERP_SYS.CUSTOMERINFO_CUSTYPE_1.equals(model.getCusType())) {
					updateCustomerMain.setIsCustomer(DERP_SYS.CUSTOMER_MAIN_IS_1);
				}
				updateCustomerMain.setModifyDate(TimeUtils.getNow());
				customerMainDao.modify(updateCustomerMain) ;
			}
			
		}
		if(model.getInnerMerchantId() != null) {
			MerchantInfoModel merchant = merchantInfoDao.searchById(model.getInnerMerchantId());
			if(merchant != null) {
				model.setInnerMerchantName(merchant.getName());
			}
		}
		CustomerInfoModel customerInfo = new CustomerInfoModel();
		BeanUtils.copyProperties(model, customerInfo);
        Long id = customerInfoDao.save(customerInfo);
        if (DERP_SYS.CUSTOMERINFO_CUSTYPE_2.equals(model.getCusType())) {
        	// 新增商家 客户关系表
        	for (Object object2 : arry) {
    			JSONObject jSONObject =(JSONObject) object2;
    			String merchantId = jSONObject.getString("merchantId");
    			String merchantName = jSONObject.getString("merchantName");
    			String purchasePriceManage = jSONObject.getString("purchasePriceManage");
    			String tariffRateConfigId = jSONObject.getString("tariffRateConfigId");
    			TariffRateConfigModel tariffRateConfig=null;
    			if (StringUtils.isNotBlank(tariffRateConfigId)) {
    				tariffRateConfig = tariffRateConfigDao.searchById(Long.valueOf(tariffRateConfigId));
				}
    			if (tariffRateConfig==null)tariffRateConfig=new TariffRateConfigModel();
    			
    			CustomerMerchantRelModel customerMerchantRel=new CustomerMerchantRelModel();
    			customerMerchantRel.setMerchantId(Long.valueOf(merchantId));
    			customerMerchantRel.setMerchantName(merchantName);
    			customerMerchantRel.setCustomerId(id);
    			customerMerchantRel.setCode(customerInfo.getCode());
    			customerMerchantRel.setName(customerInfo.getName());
    			customerMerchantRel.setCreater(customerInfo.getCreater());
    			customerMerchantRel.setStatus(DERP_SYS.CUSTOMER_MERCHANT_REL_status1);
    			customerMerchantRel.setPurchasePriceManage(purchasePriceManage);
    			customerMerchantRel.setCreateDate(TimeUtils.getNow());
    			customerMerchantRel.setRate(tariffRateConfig.getRate());
    			customerMerchantRel.setRateId(tariffRateConfig.getId());
    			customerMerchantRelDao.save(customerMerchantRel);
    		}
        	//供应商 新增 客户银行新增表数据/商家客户银行 关系数据
        	if (bankArry!=null&&bankArry.size()>0) {
        		saveCustomerBank(customerInfo,bankArry);
			}
        	
        	
		}
        if (DERP_SYS.CUSTOMERINFO_CUSTYPE_1.equals(model.getCusType())) {
        	for (Object object2 : arry) {
    			JSONObject jSONObject =(JSONObject) object2;
    			String merchantId = jSONObject.getString("merchantId");
    			String merchantName = (String) jSONObject.get("merchantName");
    			String settlementType = (String) jSONObject.get("settlementType");
    			String accountPeriod = (String) jSONObject.get("accountPeriod");
    			String salePriceManage = (String) jSONObject.get("salePriceManage");
    			String businessModel = (String) jSONObject.get("businessModel");
    			String tariffRateConfigId = jSONObject.getString("tariffRateConfigId");
    			TariffRateConfigModel tariffRateConfig=null;
    			if (StringUtils.isNotBlank(tariffRateConfigId)) {
    				tariffRateConfig = tariffRateConfigDao.searchById(Long.valueOf(tariffRateConfigId));
				}
    			if (tariffRateConfig==null)tariffRateConfig=new TariffRateConfigModel();
    			
    			CustomerMerchantRelModel customerMerchantRel=new CustomerMerchantRelModel();
    			customerMerchantRel.setMerchantId(Long.valueOf(merchantId));
    			customerMerchantRel.setMerchantName(merchantName);
    			customerMerchantRel.setCustomerId(id);
    			customerMerchantRel.setCode(customerInfo.getCode());
    			customerMerchantRel.setName(customerInfo.getName());
    			customerMerchantRel.setCreater(customerInfo.getCreater());
    			customerMerchantRel.setStatus(DERP_SYS.CUSTOMER_MERCHANT_REL_status1);
    			customerMerchantRel.setSettlementType(settlementType);
    			customerMerchantRel.setAccountPeriod(accountPeriod);
    			customerMerchantRel.setSalePriceManage(salePriceManage);
    			customerMerchantRel.setBusinessModel(businessModel);
    			customerMerchantRel.setRate(tariffRateConfig.getRate());
    			customerMerchantRel.setRateId(tariffRateConfig.getId());
    			customerMerchantRel.setCreateDate(TimeUtils.getNow());
    			customerMerchantRelDao.save(customerMerchantRel);
    		}
		}
        
		
        
		return map;
	}
	/**
	 * 新增商家客户银行数据
	 * @param customerInfo
	 * @param bankArry
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private void saveCustomerBank(CustomerInfoModel customerInfo, JSONArray bankArry) throws Exception {
		if (customerInfo.getId()==null) return;
						
		Map<String, Object> param=new HashMap<>();
		param.put("customerId", customerInfo.getId());
		// 删除客户银行数据
		customerBankDao.deleteByParam(param);
		// 删除商家客户银行数据
		customerBankMerchantRelDao.deleteByParam(param);
	
		for (Object object : bankArry) {
			JSONObject jSONObject =(JSONObject) object;	
			String bankAccount = (String)jSONObject.get("bankAccount");
			if (StringUtils.isBlank(bankAccount)) continue;
			CustomerBankModel bankModel=new CustomerBankModel();
			bankModel.setCustomerId(customerInfo.getId());
			bankModel.setCustomerName(customerInfo.getName());
			if (jSONObject.get("depositBank")!=null&&StringUtils.isNotBlank(jSONObject.getString("depositBank"))) {
				bankModel.setDepositBank(jSONObject.getString("depositBank"));
			}
			if (jSONObject.get("bankAccount")!=null&&StringUtils.isNotBlank(jSONObject.getString("bankAccount"))) {
				bankModel.setBankAccount(jSONObject.getString("bankAccount"));
			}
			if (jSONObject.get("beneficiaryName")!=null&&StringUtils.isNotBlank(jSONObject.getString("beneficiaryName"))) {
				bankModel.setBeneficiaryName(jSONObject.getString("beneficiaryName"));
			}
			if (jSONObject.get("bankAddress")!=null&&StringUtils.isNotBlank(jSONObject.getString("bankAddress"))) {
				bankModel.setBankAddress(jSONObject.getString("bankAddress"));
			}
			if (jSONObject.get("swiftCode")!=null&&StringUtils.isNotBlank(jSONObject.getString("swiftCode"))) {
				bankModel.setSwiftCode(jSONObject.getString("swiftCode"));
			}
			bankModel.setCreateDate(TimeUtils.getNow());
			Long id = customerBankDao.save(bankModel);
			
			String bankMerchantIdsStr=(String)jSONObject.get("bankMerchantIdsStr");
			
			if (StringUtils.isNotBlank(bankMerchantIdsStr)) {
				String[] bankMerchantIds = bankMerchantIdsStr.split(",");
				for (String MerchantId : bankMerchantIds) {
					CustomerBankMerchantRelModel bankMerchantRel=new CustomerBankMerchantRelModel();
					if (StringUtils.isBlank(MerchantId))continue;
					MerchantInfoModel merchantInfo = merchantInfoDao.searchById(Long.valueOf(MerchantId));
					if (merchantInfo==null)continue;
					bankMerchantRel.setCustomerBankId(bankModel.getId());
					bankMerchantRel.setCustomerId(customerInfo.getId());
					bankMerchantRel.setCustomerName(customerInfo.getName());
					bankMerchantRel.setMerchantId(merchantInfo.getId());
					bankMerchantRel.setMerchantName(merchantInfo.getName());
					bankMerchantRel.setCreateDate(TimeUtils.getNow());
					customerBankMerchantRelDao.save(bankMerchantRel);
					
				}
				
			}
			
		}
		

		
	}

	/**
	 * 新增供应商
	 * @param model
	 * @return
	 */
	@Override
	public Map<String,Object> saveSupplier(CustomerInfoDTO model) throws Exception {
		Map<String,Object> map = new  HashMap<>();
		// 1成功 0失败
		map.put("status", "1");
		map.put("massage", "成功");
		// 客户编码
		model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ERP));
		if(StringUtils.isNotBlank(model.getMainId())){
			//判断主数据id是否已存在
			CustomerInfoModel customertemp = new CustomerInfoModel();
			customertemp.setCusType(model.getCusType());
			customertemp.setMainId(model.getMainId());
			List<CustomerInfoModel> list =  customerInfoDao .list(customertemp);
			if(list!=null&&list.size()>0){
				map.put("massage", "主数据ID已存在");
				map.put("status", "0");
				return map;
			}
		}
		if(DERP_SYS.CUSTOMERINFO_SOURCE_1.equals(model.getSource())) {
			CustomerMainModel customerMainModel = new CustomerMainModel() ;
			customerMainModel.setCcode(model.getMainId());
			customerMainModel = customerMainDao.searchByModel(customerMainModel) ;
			if(customerMainModel == null) {
				map.put("massage", "主数据信息不存在");
				map.put("status", "0");
				return map;
			}
			customerMainModel.setIsSupplier(DERP_SYS.CUSTOMER_MAIN_IS_1);
			customerMainModel.setModifyDate(TimeUtils.getNow());
			customerMainDao.modify(customerMainModel);
		}

		if(model.getInnerMerchantId() != null) {
			MerchantInfoModel merchant = merchantInfoDao.searchById(model.getInnerMerchantId());
			if(merchant != null) {
				model.setInnerMerchantName(merchant.getName());
			}
		}
		CustomerInfoModel customerInfo = new CustomerInfoModel();
		BeanUtils.copyProperties(model, customerInfo);
		Long id = customerInfoDao.save(customerInfo);
		return map;
	}


	/**
	 * 修改客户
	 * 说明 客户和供应商修改公用一个service
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@Override

	public Map<String,Object> modifyCustomer(CustomerInfoDTO model,JSONArray arry,List<Object> deleteIdsList,JSONArray bankArry) throws Exception {
		Map<String,Object> map = new  HashMap<>();
		// 1成功 0失败
		map.put("status", "1");
		map.put("massage", "成功");

		CustomerInfoModel customerInfo = new CustomerInfoModel();
		BeanUtils.copyProperties(model, customerInfo);
		customerInfo.setId(model.getId());

		if(model.getInnerMerchantId() != null) {
			MerchantInfoModel merchant = merchantInfoDao.searchById(model.getInnerMerchantId());
			if(merchant != null) {
				customerInfo.setInnerMerchantName(merchant.getName());
			}
		}
		// 从新查询 获取客户编码  编辑 页面没有传客户编码   商家客户关系表要取客户编码
		int num = customerInfoDao.modify(customerInfo);
		
		customerInfo = customerInfoDao.searchById(model.getId());
		//删除商家客户关系表// 客商关系表 
		if (deleteIdsList!=null&&deleteIdsList.size()>0) {
			List<Long>idsDel=new ArrayList<>();
			for (Object object : deleteIdsList) {
				if (object==null||StringUtils.isBlank(object.toString()))continue;
				idsDel.add(Long.valueOf(object.toString()));
			}
			if (idsDel.size()>0) {
				customerMerchantRelDao.delete(idsDel);
			}
						
		}
		if (DERP_SYS.CUSTOMERINFO_CUSTYPE_1.equals(model.getCusType())) {
			for (Object object2 : arry) {
				JSONObject jSONObject =(JSONObject) object2;
				String merchantId = jSONObject.getString("merchantId");
				String merchantName = jSONObject.getString("merchantName");
				String settlementType = jSONObject.getString("settlementType");
				String accountPeriod = jSONObject.getString("accountPeriod");
				String salePriceManage = jSONObject.getString("salePriceManage");
				String businessModel = jSONObject.getString("businessModel");
    			String tariffRateConfigId = jSONObject.getString("tariffRateConfigId");
    			TariffRateConfigModel tariffRateConfig=null;
    			if (StringUtils.isNotBlank(tariffRateConfigId)) {
    				tariffRateConfig = tariffRateConfigDao.searchById(Long.valueOf(tariffRateConfigId));
				}
    			if (tariffRateConfig==null)tariffRateConfig=new TariffRateConfigModel();
				CustomerMerchantRelModel customerMerchantRel=new CustomerMerchantRelModel();
				customerMerchantRel.setMerchantId(Long.valueOf(merchantId));
				customerMerchantRel.setMerchantName(merchantName);
				customerMerchantRel.setCustomerId(customerInfo.getId());
				customerMerchantRel.setCode(customerInfo.getCode());
				customerMerchantRel.setName(customerInfo.getName());
				customerMerchantRel.setCreater(customerInfo.getCreater());
				customerMerchantRel.setStatus(DERP_SYS.CUSTOMER_MERCHANT_REL_status1);
				customerMerchantRel.setSettlementType(settlementType);
				customerMerchantRel.setAccountPeriod(accountPeriod);
				customerMerchantRel.setBusinessModel(businessModel);
				customerMerchantRel.setSalePriceManage(salePriceManage);
				customerMerchantRel.setRateId(tariffRateConfig.getId());
				customerMerchantRel.setRate(tariffRateConfig.getRate());
				
				
				CustomerMerchantRelModel relQuery=new CustomerMerchantRelModel();
				relQuery.setMerchantId(Long.valueOf(merchantId));
				relQuery.setCustomerId(customerInfo.getId());
				relQuery = customerMerchantRelDao.searchByModel(relQuery);
				if (relQuery==null) {
					customerMerchantRel.setCreateDate(TimeUtils.getNow());
					customerMerchantRelDao.save(customerMerchantRel);
				}else {
					customerMerchantRel.setModifyDate(TimeUtils.getNow());
					customerMerchantRel.setId(relQuery.getId());
					customerMerchantRelDao.modify(customerMerchantRel);
				}

			}
		}

		if (DERP_SYS.CUSTOMERINFO_CUSTYPE_2.equals(model.getCusType())) {
			
			for (Object object2 : arry) {
				JSONObject jSONObject =(JSONObject) object2;
				String merchantId = jSONObject.getString("merchantId");
				String merchantName = jSONObject.getString("merchantName");
				String purchasePriceManage = jSONObject.getString("purchasePriceManage");
    			String tariffRateConfigId = jSONObject.getString("tariffRateConfigId");
    			TariffRateConfigModel tariffRateConfig=null;
    			if (StringUtils.isNotBlank(tariffRateConfigId)) {
    				tariffRateConfig = tariffRateConfigDao.searchById(Long.valueOf(tariffRateConfigId));
				}
    			if (tariffRateConfig==null) tariffRateConfig=new TariffRateConfigModel();
				CustomerMerchantRelModel customerMerchantRel=new CustomerMerchantRelModel();
				customerMerchantRel.setMerchantId(Long.valueOf(merchantId));
				customerMerchantRel.setMerchantName(merchantName);
				customerMerchantRel.setCustomerId(customerInfo.getId());
				customerMerchantRel.setCode(customerInfo.getCode());
				customerMerchantRel.setName(customerInfo.getName());
				customerMerchantRel.setCreater(customerInfo.getCreater());
				customerMerchantRel.setStatus(DERP_SYS.CUSTOMER_MERCHANT_REL_status1);
				customerMerchantRel.setPurchasePriceManage(purchasePriceManage);
				customerMerchantRel.setRateId(tariffRateConfig.getId());
				customerMerchantRel.setRate(tariffRateConfig.getRate());
				
				
				CustomerMerchantRelModel relQuery=new CustomerMerchantRelModel();
				relQuery.setMerchantId(Long.valueOf(merchantId));
				relQuery.setCustomerId(customerInfo.getId());
				relQuery = customerMerchantRelDao.searchByModel(relQuery);
				if (relQuery==null) {
					customerMerchantRel.setCreateDate(TimeUtils.getNow());
					customerMerchantRelDao.save(customerMerchantRel);
				}else {
					customerMerchantRel.setId(relQuery.getId());
					customerMerchantRel.setModifyDate(TimeUtils.getNow());
					customerMerchantRelDao.modify(customerMerchantRel);
				}

			}
			//供应商 新增 客户银行新增表数据/商家客户银行 关系数据
			if (bankArry==null) bankArry=new JSONArray();
			saveCustomerBank(customerInfo,bankArry);
		}

		
		/*CustomerMerchantRelModel relModel = new CustomerMerchantRelModel() ;
		relModel.setCustomerId(model.getId());
		List<CustomerMerchantRelModel> relList = customerMerchantRelDao.list(relModel);
		
		for (CustomerMerchantRelModel customerMerchantRelModel : relList) {
            if(model.getRelId()!=null&&model.getRelId().equals(customerMerchantRelModel.getId())){
				customerMerchantRelModel.setSettlementType(model.getSettlementType());
				customerMerchantRelModel.setAccountPeriod(model.getAccountPeriod());
			}
			customerMerchantRelModel.setName(model.getName());
			customerMerchantRelDao.modify(customerMerchantRelModel) ;
		}*/
		return map;
	}

	/**
	 * 客商信息导入  说明:(客户导入和供应商导入公用一个service)
	 * type  1 客户  2 供应商',
	 * @param
	 * @return
	 */
	@Override
	public Map saveImportCustomer(Map<Integer, List<List<Object>>> data,Long merchantId,Long userId,String merchantName) throws Exception {
		List<CustomerInfoModel> customerList = new ArrayList<CustomerInfoModel>() ;
		
		List<ImportMessage> resultList = new ArrayList<ImportMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(0);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(0).get(0).toArray(),
						objects.get(j).toArray());
				try {
					//必填字段的校验
					String name = null ;
					name = map.get("客户名称");
					if (checkIsNullOrNot(j, name, "客户名称不能为空", resultList)) {
						failure += 1;
						continue;
					}
					String creditCode = map.get("营业执照号");
					if (checkIsNullOrNot(j, creditCode, "营业执照号不能为空", resultList)) {
						failure += 1;
						continue;
					}
					String orgCode = map.get("组织机构代码");
					if (checkIsNullOrNot(j, orgCode, "组织机构代码不能为空", resultList)) {
						failure += 1;
						continue;
					}
					String oTelNo = map.get("公司电话");
					if(StringUtils.isBlank(oTelNo) ){
						setErrorMsg(j, "公司电话不能为空", resultList);
						failure += 1;
						continue;
					}
					// 判断是否在数据库存在，是：跳过;否：新增
					CustomerInfoModel customerInfo = new CustomerInfoModel();
					customerInfo.setName(name);//名称
					customerInfo.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);//类型  1 客户  2 供应商',
					List<CustomerInfoModel> customerInfoList = customerInfoDao.list(customerInfo);
					if(customerInfoList!=null&&customerInfoList.size()>0) {
						String message = "客户已存在";
						setErrorMsg(j, message, resultList);
						pass+=1;
						continue;
					}
					// 保存
					CustomerInfoModel model = new CustomerInfoModel();
					model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ERP));// 客户编码
					model.setName(name);// 客户名称
					model.setCreditCode(creditCode);// 营业执照号
					model.setOrgCode(orgCode);// 组织机构代码
					model.setCreater(userId);// 创建人
					model.setCreateDate(TimeUtils.getNow());// 创建时间
					model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);//'类型  1 客户  2 供应商'
					model.setOTelNo(oTelNo);// 公司电话
					String c_type = map.get("是否内部公司");
					String merchantCode = map.get("内部公司编码");
					if (StringUtils.isNotBlank(c_type)) {
						c_type = c_type.trim();
						if(c_type.equals("是")){
							model.setType(DERP_SYS.CUSTOMERINFO_TYPE_1);
							MerchantInfoModel merchant = new MerchantInfoModel();
							merchant.setCode(merchantCode);
							merchant = merchantInfoDao.searchByModel(merchant);
							if(merchant != null) {
								model.setInnerMerchantId(merchant.getId());
								model.setInnerMerchantName(merchant.getName());
							}
						}else{
							model.setType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
						}
					}
					if(StringUtils.isNotBlank(map.get("主数据客户ID"))){
						model.setMainId(map.get("主数据客户ID"));
					}
					if(StringUtils.isNotBlank(map.get("客户简称"))) {
						model.setSimpleName(map.get("客户简称"));
					}
					if (StringUtils.isNotBlank(map.get("中文名"))) {
						model.setChinaName(map.get("中文名"));
					}
					if (StringUtils.isNotBlank(map.get("英文名"))) {
						model.setEnName(map.get("英文名"));
					}
					if (StringUtils.isNotBlank(map.get("英文简称"))) {
						model.setEnSimpleName(map.get("英文简称"));
					}
					if(StringUtils.isNotBlank(map.get("企业性质"))){
						model.setNature(map.get("企业性质"));
					}
					if (StringUtils.isNotBlank(map.get("注册地"))) {
						model.setCompanyAddr(map.get("注册地"));
					}
					if(StringUtils.isNotBlank(map.get("E-Mail"))){
						model.setEmail(map.get("E-Mail"));
					}
					if(StringUtils.isNotBlank(map.get("传真号码"))){
						model.setFax(map.get("传真号码"));
					}
					if(StringUtils.isNotBlank(map.get("客户等级编码"))){
						model.setCodeGrade(map.get("客户等级编码"));
					}
					if(StringUtils.isNotBlank(map.get("客户授权码"))){
						model.setAuthNo(map.get("客户授权码"));
					}
					if(StringUtils.isNotBlank(map.get("经营范围"))) {
						model.setOperationScope(map.get("经营范围"));
					}
					if(StringUtils.isNotBlank(map.get("税号"))) {
						model.setTaxNo(map.get("税号"));
					}
					if(StringUtils.isNotBlank(map.get("法人代表"))) {
						model.setLegalPerson(map.get("法人代表"));
					}
					if(StringUtils.isNotBlank(map.get("法人国籍"))) {
						model.setLegalNationality(map.get("法人国籍"));
					}
					if(StringUtils.isNotBlank(map.get("法人代表身份证"))) {
						model.setLegalCardNo(map.get("法人代表身份证"));
					}
					if(StringUtils.isNotBlank(map.get("法人电话"))) {
						model.setLegalTel(map.get("法人电话"));
					}
					if(StringUtils.isNotBlank(map.get("税号"))) {
						model.setTaxNo(map.get("税号"));
					}
					String currency = map.get("销售币种");
					if(StringUtils.isNotBlank(currency)) {
						if(StringUtils.isNotBlank(DERP.getLabelByKey(DERP.currencyCodeList, currency))){
							model.setCurrency(currency);
						}else{
							String msg = "销售币种输入有误" ;
							setErrorMsg(j, msg, resultList);
							failure += 1;
							continue;
						}
					}

					// NC平台编码
					String NCPlatformName = map.get("NC平台名称");
					if(StringUtils.isNotBlank(NCPlatformName)) {
						model.setNcPlatformCode((String) DERP_ORDER.getKeyByLabel(DERP_ORDER.platform_codeList,NCPlatformName));
					}
					// NC渠道编码
					String NCChannelName = map.get("NC渠道名称");
					if(StringUtils.isNotBlank(NCChannelName)) {
						model.setNcChannelCode((String) DERP_ORDER.getKeyByLabel(DERP_ORDER.channel_codeList,NCChannelName));
					}
					model.setSource(DERP_SYS.CUSTOMERINFO_SOURCE_2);
					customerList.add(model) ;
				} catch (Exception e) {
					e.printStackTrace();
					failure += 1;
					continue;
				}
			}
		}
		
		if(failure == 0) {
			for(CustomerInfoModel customerInfoModel : customerList) {
					customerInfoDao.save(customerInfoModel) ;
					success++;
			}
		}
		
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message",resultList);
		return map;
	}
	/**
	 * 客商信息导入  说明:(客户导入和供应商导入公用一个service)
	 * type  1 客户  2 供应商',
	 * @param
	 * @return
	 */
	@Override
	public Map saveImportSupplier(Map<Integer, List<List<Object>>> data,Long merchantId,Long userId,String merchantName) throws Exception{
		List<CustomerInfoModel> customerList = new ArrayList<CustomerInfoModel>() ;

		List<ImportMessage> resultList = new ArrayList<ImportMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(0);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(0).get(0).toArray(),
						objects.get(j).toArray());
				try {
					//必填字段的校验
					String name = map.get("供应商名称");
					if (checkIsNullOrNot(j, name, "供应商名称不能为空", resultList)) {
						failure += 1;
						continue;
					}
					String creditCode = map.get("营业执照号");
					if(checkIsNullOrNot(j, creditCode, "营业执照号不能为空", resultList)) {
						failure += 1;
						continue;
					}
					String orgCode = map.get("组织机构代码");
					if (checkIsNullOrNot(j, orgCode, "组织机构代码不能为空", resultList)) {
						failure += 1;
						continue;
					}
					String oTelNo = map.get("公司电话");
					if(StringUtils.isBlank(oTelNo)){
						setErrorMsg(j, "公司电话不能为空", resultList);
						failure += 1;
						continue;
					}
					// 判断是否在数据库存在，是：跳过;否：新增
					CustomerInfoModel customerInfo = new CustomerInfoModel();
					customerInfo.setName(name);//名称
					customerInfo.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);//类型  1 客户  2 供应商',
					List<CustomerInfoModel> customerInfoList = customerInfoDao.list(customerInfo);
					if(customerInfoList!=null&&customerInfoList.size()>0) {
						String message = "供应商已存在" ;
						setErrorMsg(j, message, resultList);
						pass+=1;
						continue;
					}
					// 保存
					CustomerInfoModel model = new CustomerInfoModel();
					model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ERP));// 客户编码
					model.setName(name);// 客户名称
					model.setCreditCode(creditCode);// 营业执照号
					model.setOrgCode(orgCode);// 组织机构代码
					model.setCreater(userId);// 创建人
					model.setCreateDate(TimeUtils.getNow());// 创建时间
					model.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);//'类型  1 客户  2 供应商'
					model.setOTelNo(oTelNo);// 公司电话
					//是否内部公司
					String c_type = map.get("是否内部公司");
					String merchantCode = map.get("内部公司编码");
					if(StringUtils.isNotBlank(c_type)) {
						c_type = c_type.trim();
						if(c_type.equals("是")){
							model.setType(DERP_SYS.CUSTOMERINFO_CUSTYPE_1);
							MerchantInfoModel merchant = new MerchantInfoModel();
							merchant.setCode(merchantCode);
							merchant = merchantInfoDao.searchByModel(merchant);
							if(merchant != null) {
								model.setInnerMerchantId(merchant.getId());
								model.setInnerMerchantName(merchant.getName());
							}
						}else{
							model.setType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
						}
					}
					if(StringUtils.isNotBlank(map.get("主数据客户ID"))){
						model.setMainId(map.get("主数据客户ID"));
					}
					if(StringUtils.isNotBlank(map.get("供应商简称"))) {
						model.setSimpleName(map.get("供应商简称"));
					}
					if (StringUtils.isNotBlank(map.get("英文名"))) {
						model.setEnName(map.get("英文名"));
					}
					if(StringUtils.isNotBlank(map.get("英文简称"))) {
						model.setEnSimpleName(map.get("英文简称"));
					}
					if(StringUtils.isNotBlank(map.get("企业性质"))){
						model.setNature(map.get("企业性质"));
					}
					if (StringUtils.isNotBlank(map.get("注册地"))) {
						model.setCompanyAddr(map.get("注册地"));
					}
					if(StringUtils.isNotBlank(map.get("企业地址"))){
						model.setBusinessAddress(map.get("企业地址"));
					}
					if(StringUtils.isNotBlank(map.get("E-Mail"))){
						model.setEmail(map.get("E-Mail"));
					}
					if(StringUtils.isNotBlank(map.get("传真号码"))){
						model.setFax(map.get("传真号码"));
					}
					if(StringUtils.isNotBlank(map.get("客户等级编码"))){
						model.setCodeGrade(map.get("客户等级编码"));
					}
					if(StringUtils.isNotBlank(map.get("经营范围"))) {
						model.setOperationScope(map.get("经营范围"));
					}
					if(StringUtils.isNotBlank(map.get("法人代表"))) {
						model.setLegalPerson(map.get("法人代表"));
					}
					if(StringUtils.isNotBlank(map.get("法人国籍"))) {
						model.setLegalNationality(map.get("法人国籍"));
					}
					if(StringUtils.isNotBlank(map.get("法人代表身份证"))) {
						model.setLegalCardNo(map.get("法人代表身份证"));
					}
					if(StringUtils.isNotBlank(map.get("法人电话"))) {
						model.setLegalTel(map.get("法人电话"));
					}
					if (StringUtils.isNotBlank(map.get("税号"))) {
						model.setTaxNo(map.get("税号"));
					}

					// 币种
					String currency = map.get("采购币种");
					if(StringUtils.isNotBlank(currency)) {
						if(StringUtils.isNotBlank(DERP.getLabelByKey(DERP.currencyCodeList, currency))){
							model.setCurrency(currency);
						}else{
							String msg = "采购币种输入有误";
							setErrorMsg(j, msg, resultList);
							failure += 1;
							continue;
						}
					}
					model.setSource(DERP_SYS.CUSTOMERINFO_SOURCE_2);
					customerList.add(model);
				} catch (Exception e) {
					e.printStackTrace();
					failure += 1;
					continue;
				}
			}
		}

		if(failure == 0) {
			for(CustomerInfoModel customerInfoModel : customerList) {
				customerInfoDao.save(customerInfoModel) ;
				success++;
			}
		}

		Map<String , Object> map = new HashMap<String , Object>();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message",resultList);
		return map;
	}
	/**
	 * 把两个数组组成一个map
	 * 
	 * @param keys
	 *            键数组
	 * @param values
	 *            值数组
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
	 * 根据条件查询供应商下拉列表
	 * @param id
	 * */
	@Override
	public List<SelectBean> getSelectBeanBySupplier(Long id) throws SQLException {
		return customerInfoDao.getSelectBeanBySupplier(id);
	}

	/**
	 * 根据条件查询客户下拉列表
	 * @param id
	 * */
	/*@Override
	public List<SelectBean> getSelectBeanByCustomer(Long id)
			throws SQLException {
		return customerInfoDao.getSelectBeanByCustomer(id);
	}*/
	
	/**
	 * 根据商家ID获取客户下拉列表(必须是启用的状态)
	 * @param dto
	 * */
	@Override
	public List<SelectBean> getSelectBeanByMerchantId(CustomerInfoDTO dto)
			throws SQLException {
		return customerInfoDao.getSelectBeanByMerchantId(dto);
	}
	/**
     *  导出客户信息
     * @param model
     * @return
     * @throws Exception
     */
	@Override
	public List<CustomerInfoDTO> exportList(CustomerInfoDTO model) throws SQLException {
		return customerInfoDao.exportList(model);
	}
	/**
     *  导出供应商信息
     * @param model
     * @return
     * @throws Exception
     */
	@Override
	public List<CustomerInfoDTO> exportSupplierList(CustomerInfoDTO model) throws SQLException {
		return customerInfoDao.exportSupplierList(model);
	}
	/**
	 * 上传图片
	 * @param fileName   文件名称
	 * @param fileBytes  文件字节数组
	 * @param fileSize   文件大小
	 * @param ext        文件后缀
	 * @param userId     用户id
	 * @param userName   用户名称
	 * @param id         供应商Id
	 * @param type       用于标识上传图片的字段 1:营业执照副本  2:组织机构代码副本  3:注册登记证   4:供货记录  
	 *                                    5:证明信息  6:品牌授权  7:银行流水  8:企业备案表  9:法人身份证
	 * @return
	 * @throws SQLException
	 */
	@Override

	public String uploadFile(String fileName,byte[] fileBytes,Long fileSize,String ext, Long userId, String userName,Long id,String type) throws SQLException {
		JSONObject jsonObject = new JSONObject();//推送内容
		jsonObject.put("fileTypeCode", SmurfsAPICodeEnum.UPLOAD_00002.getCode());
		jsonObject.put("fileName",fileName);
		jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
		jsonObject.put("fileExt",ext);
		jsonObject.put("fileSize",String.valueOf(fileSize));
		//调用外部接口上传图片
        String resultMsg= SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);
        System.out.println(resultMsg);
        if(resultMsg == null || "".equals(resultMsg)){
        	
        }
        JSONObject jsonObj = JSONObject.fromObject(resultMsg);
        if(jsonObj.getInt("code")!= 200){
        	
        }
        //获取供应商资质
        CustomerAptitudeModel customerAptitude = new CustomerAptitudeModel();
        customerAptitude.setCustomerId(id);
        List<CustomerAptitudeModel> customerAptitudeList = customerAptitudeDao.list(customerAptitude);
        if(customerAptitudeList != null && customerAptitudeList.size()>0){
        	customerAptitude = customerAptitudeList.get(0);
        }else{
        	customerAptitude = null;
        }
        //修改对应的图片
        if("1".equals(type)){
        	if(customerAptitude != null){
        		customerAptitude.setCharteredNo(jsonObj.getString("url"));
        		customerAptitude.setChartModifier(userId);
        		customerAptitude.setChartModifierName(userName);
        		customerAptitude.setChartModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.modify(customerAptitude);
        	}else{
        		CustomerAptitudeModel customerAptitude1 = new CustomerAptitudeModel();
        		customerAptitude1.setCustomerId(id);
        		customerAptitude1.setCharteredNo(jsonObj.getString("url"));
        		customerAptitude1.setChartModifier(userId);
        		customerAptitude1.setChartModifierName(userName);
        		customerAptitude1.setChartModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.save(customerAptitude1);
        	}
        }else if("2".equals(type)){
        	if(customerAptitude != null){
        		customerAptitude.setOrganizationCode(jsonObj.getString("url"));
        		customerAptitude.setOrgModifier(userId);
        		customerAptitude.setOrgModifierName(userName);
        		customerAptitude.setOrgModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.modify(customerAptitude);
        	}else{
        		CustomerAptitudeModel customerAptitude1 = new CustomerAptitudeModel();
        		customerAptitude1.setCustomerId(id);
        		customerAptitude1.setOrganizationCode(jsonObj.getString("url"));
        		customerAptitude1.setOrgModifier(userId);
        		customerAptitude1.setOrgModifierName(userName);
        		customerAptitude1.setOrgModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.save(customerAptitude1);
        	}
        }else if("3".equals(type)){
        	if(customerAptitude != null){
        		customerAptitude.setRegistrationNo(jsonObj.getString("url"));
        		customerAptitude.setRegistModifier(userId);
        		customerAptitude.setRegistModifierName(userName);
        		customerAptitude.setRegistModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.modify(customerAptitude);
        	}else{
        		CustomerAptitudeModel customerAptitude1 = new CustomerAptitudeModel();
        		customerAptitude1.setCustomerId(id);
        		customerAptitude1.setRegistrationNo(jsonObj.getString("url"));
        		customerAptitude1.setRegistModifier(userId);
        		customerAptitude1.setRegistModifierName(userName);
        		customerAptitude1.setRegistModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.save(customerAptitude1);
        	}
        }else if("4".equals(type)){
        	if(customerAptitude != null){
        		customerAptitude.setSupplyRecord(jsonObj.getString("url"));
        		customerAptitude.setSupplyModifier(userId);
        		customerAptitude.setSupplyModifierName(userName);
        		customerAptitude.setSupplyModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.modify(customerAptitude);
        	}else{
        		CustomerAptitudeModel customerAptitude1 = new CustomerAptitudeModel();
        		customerAptitude1.setCustomerId(id);
        		customerAptitude1.setSupplyRecord(jsonObj.getString("url"));
        		customerAptitude1.setSupplyModifier(userId);
        		customerAptitude1.setSupplyModifierName(userName);
        		customerAptitude1.setSupplyModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.save(customerAptitude1);
        	}
        }else if("5".equals(type)){
        	if(customerAptitude != null){
        		customerAptitude.setProofInfo(jsonObj.getString("url"));
        		customerAptitude.setProofModifier(userId);
        		customerAptitude.setProofModifierName(userName);
        		customerAptitude.setProofModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.modify(customerAptitude);
        	}else{
        		CustomerAptitudeModel customerAptitude1 = new CustomerAptitudeModel();
        		customerAptitude1.setCustomerId(id);
        		customerAptitude1.setProofInfo(jsonObj.getString("url"));
        		customerAptitude1.setProofModifier(userId);
        		customerAptitude1.setProofModifierName(userName);
        		customerAptitude1.setProofModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.save(customerAptitude1);
        	}
        }else if("6".equals(type)){
        	if(customerAptitude != null){
        		customerAptitude.setBrandAuthorization(jsonObj.getString("url"));
        		customerAptitude.setBrandModifier(userId);
        		customerAptitude.setBrandModifierName(userName);
        		customerAptitude.setBrandModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.modify(customerAptitude);
        	}else{
        		CustomerAptitudeModel customerAptitude1 = new CustomerAptitudeModel();
        		customerAptitude1.setCustomerId(id);
        		customerAptitude1.setBrandAuthorization(jsonObj.getString("url"));
        		customerAptitude1.setBrandModifier(userId);
        		customerAptitude1.setBrandModifierName(userName);
        		customerAptitude1.setBrandModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.save(customerAptitude1);
        	}
        }else if("7".equals(type)){
        	if(customerAptitude != null){
        		customerAptitude.setBankFlow(jsonObj.getString("url"));
        		customerAptitude.setBankModifier(userId);
        		customerAptitude.setBankModifierName(userName);
        		customerAptitude.setBankModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.modify(customerAptitude);
        	}else{
        		CustomerAptitudeModel customerAptitude1 = new CustomerAptitudeModel();
        		customerAptitude1.setCustomerId(id);
        		customerAptitude1.setBankFlow(jsonObj.getString("url"));
        		customerAptitude1.setBankModifier(userId);
        		customerAptitude1.setBankModifierName(userName);
        		customerAptitude1.setBankModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.save(customerAptitude1);
        	}
        }else if("8".equals(type)){
        	if(customerAptitude != null){
        		customerAptitude.setKeepRecord(jsonObj.getString("url"));
        		customerAptitude.setKeepModifier(userId);
        		customerAptitude.setKeepModifierName(userName);
        		customerAptitude.setKeepModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.modify(customerAptitude);
        	}else{
        		CustomerAptitudeModel customerAptitude1 = new CustomerAptitudeModel();
        		customerAptitude1.setCustomerId(id);
        		customerAptitude1.setKeepRecord(jsonObj.getString("url"));
        		customerAptitude1.setKeepModifier(userId);
        		customerAptitude1.setKeepModifierName(userName);
        		customerAptitude1.setKeepModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.save(customerAptitude1);
        	}
        }else if("9".equals(type)){
        	if(customerAptitude != null){
        		customerAptitude.setLegalCardNo(jsonObj.getString("url"));
        		customerAptitude.setLegalModifier(userId);
        		customerAptitude.setLegalModifierName(userName);
        		customerAptitude.setLegalModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.modify(customerAptitude);
        	}else{
        		CustomerAptitudeModel customerAptitude1 = new CustomerAptitudeModel();
        		customerAptitude1.setCustomerId(id);
        		customerAptitude1.setLegalCardNo(jsonObj.getString("url"));
        		customerAptitude1.setLegalModifier(userId);
        		customerAptitude1.setLegalModifierName(userName);
        		customerAptitude1.setLegalModifyDate(TimeUtils.getNow());
        		customerAptitudeDao.save(customerAptitude1);
        	}
        }
		return jsonObj.getString("url");
	}
	/**
	 * 通过供应商id获取资质图片
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public CustomerAptitudeModel searchAptitude(Long id) throws SQLException {
		//获取供应商资质
        CustomerAptitudeModel customerAptitude = new CustomerAptitudeModel();
        customerAptitude.setCustomerId(id);
        List<CustomerAptitudeModel> customerAptitudeList = customerAptitudeDao.list(customerAptitude);
        if(customerAptitudeList != null && customerAptitudeList.size()>0){
        	customerAptitude = customerAptitudeList.get(0);
        }else{
        	customerAptitude = new CustomerAptitudeModel();
        }
        return customerAptitude;
	}

	/*@Override
	public List<SelectBean> getNameById(Long id) throws SQLException {
		return customerInfoDao.getNameById(id);
	}*/
	/**
	 * 获取所有启用状态下的供应商下拉框
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getAllSelectBeanBySupplier() throws SQLException {
		return customerInfoDao.getAllSelectBeanBySupplier();
	}
	
	
	/**
	 * 客户和供应商的禁用和启用
	 * 说明 客户和供应商公用
	 */
	@Override

	public boolean modifyEnabledCustomer(CustomerInfoModel model) throws SQLException {
		CustomerInfoModel customer =new CustomerInfoModel();		
		customer.setId(model.getId());
		customer.setStatus(model.getStatus());
		int num = customerInfoDao.modify(customer);
		if (num>0) {
			return true;
		}
		return false;
	}

	@Override
	public List<SelectBean> getMerchantList(User user) throws SQLException {
		UserMerchantRelModel relModel=new UserMerchantRelModel();
		relModel.setUserId(user.getId());
		List<SelectBean> result = userMerchantRelDao.getUserSelectBean(relModel);
		return result;
	}

	@Override
	public List<CustomerMerchantRelModel> getMerchantRel(String id) throws SQLException {
		
		CustomerMerchantRelModel queryModel = new CustomerMerchantRelModel() ;
		
		queryModel.setCustomerId(Long.valueOf(id));
		queryModel.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);
		
		return customerMerchantRelDao.list(queryModel) ;
	}
	
	/**
	 * 供应商页面合作公司 
	 * 采购价格管理默认是启用
	 * @throws Exception 
	 */
	@Override
	public boolean saveMerchantRel(List<Long> list, CustomerMerchantRelModel relModel,User user) throws Exception {
		
		CustomerInfoModel selectedCustomer = customerInfoDao.searchById(relModel.getCustomerId());
				
		for (Long merchantId : list) {
			if(merchantId.equals(selectedCustomer.getInnerMerchantId())) {
				throw new RuntimeException("该公司已设为内部公司，无法关联") ;
			}
		}
		
		// 获取删除id 此用户管理的商家 对应的商家客户关系数据
		List<Long> delList=new ArrayList<>();		
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("customerId", selectedCustomer.getId());
		paramMap.put("userId", user.getId());
		List<CustomerMerchantRelModel> relQureyList = customerMerchantRelDao.getCustMerListByUser(paramMap);
		if (relQureyList!=null&&relQureyList.size()>0) {
			for (CustomerMerchantRelModel model : relQureyList) {
				delList.add(model.getId());
			}
		}		
		
		
		
		for (Long merchantId : list) {
			
			CustomerMerchantRelModel updateModel = new CustomerMerchantRelModel() ;			
			MerchantInfoModel merchant = merchantInfoDao.searchById(merchantId);				
			updateModel = new CustomerMerchantRelModel() ;
			updateModel.setCode(selectedCustomer.getCode());
			updateModel.setCustomerId(selectedCustomer.getId());
			updateModel.setName(selectedCustomer.getName());
			updateModel.setMerchantId(merchant.getId());
			updateModel.setMerchantName(merchant.getName());
			updateModel.setStatus(selectedCustomer.getStatus());
			updateModel.setCreateDate(TimeUtils.getNow());
			updateModel.setCreater(user.getId());
			updateModel.setPurchasePriceManage(DERP_SYS.PURCHASE_PRICE_MANAGE_1);//供应商页面合作公司 采购价格管理默认是启用								
			customerMerchantRelDao.save(updateModel) ;

		}
		
		//客户关系表
		if (delList!=null&&delList.size()>0) {
			customerMerchantRelDao.delete(delList);

		}		
		
		return true;
	}

	@Override
	public List<MerchantInfoModel> getMerchantRelInfo(Long id) throws SQLException {
		CustomerMerchantRelModel queryModel = new CustomerMerchantRelModel() ;
		
		queryModel.setCustomerId(Long.valueOf(id));
		queryModel.setStatus(DERP_SYS.CUSTOMERINFO_STATUS_1);
		
		List<CustomerMerchantRelModel> relList = customerMerchantRelDao.list(queryModel);
		
		List<MerchantInfoModel> merchantList = new ArrayList<MerchantInfoModel>() ;
		
		for (CustomerMerchantRelModel rel : relList) {
			
			MerchantInfoModel merchant = merchantInfoDao.searchById(rel.getMerchantId());
			
			merchantList.add(merchant) ;
		}
		
		return merchantList ;
	}

	@Override
	public CustomerInfoModel getCustomerMainInfo(Long mainId) throws SQLException {
		
		CustomerMainModel customerMain = customerMainDao.searchById(mainId);
		
		if(customerMain != null) {
			CustomerInfoModel customer = new CustomerInfoModel() ;
			customer.setName(customerMain.getCname());    					//客户名称
			customer.setSimpleName(customerMain.getCshortname());			//客户简称
			customer.setEnName(customerMain.getCnameen());					//英文名
			customer.setEnSimpleName(customerMain.getCshortnameen());		//英文简称
			customer.setCompanyAddr(customerMain.getRegisteredaddr());		//注册地
			customer.setOrgCode(customerMain.getCcodethk());				//组织机构代码
			customer.setNature(customerMain.getCmprop());					//企业性质
			customer.setOperationScope(customerMain.getBusinessscope());	//经营范围
			customer.setBusinessAddress(customerMain.getAddr());			//企业地址
			customer.setEnBusinessAddress(customerMain.getAddren());		//英文企业地址
			customer.setCreditCode(customerMain.getBusilicenseno());		//营业执照号
			customer.setMainId(customerMain.getCcode());					//主数据表ID
			customer.setCodeGrade(customerMain.getCcodegrade()); 			//客户等级编码
			customer.setCityCode(customerMain.getCitycode());				//省市区代码
			customer.setRemark(customerMain.getRemark()); 					//备注
			customer.setSource(DERP_SYS.CUSTOMERINFO_SOURCE_1);
			
			return customer ;
		}
		
		return null;
	}

	@Override
	public CustomerInfoDTO searchDetailByRelId(Long relId) {
		return customerInfoDao.getDetailsByRelId(relId);
	}

	@Override
	public List<CustomerInfoDTO> getSelectBeanByDTO(CustomerInfoDTO dto) throws SQLException {
		return customerInfoDao.getSelectBeanByDTO(dto);
	}

	@Override
	public List<CustomerInfoDTO> getCustomerByMerchantId(CustomerInfoDTO dto) throws SQLException {
		return customerInfoDao.getCustomerByMerchantId(dto);
	}

	@Override
	public List<CustomerMerchantRelModel> getSupplierMerchantRelByMainIdURL(CustomerInfoDTO dto) throws SQLException {
		return customerInfoDao.getSupplierMerchantRelByMainIdURL(dto);
	}

	@Override
	public CustomerInfoDTO getCustomerByMainMerchantId(CustomerInfoDTO dto) throws SQLException {
		return customerInfoDao.getCustomerByMainMerchantId(dto);
	}

	@Override
	public List<MerchantInfoDTO> getMerchantRelInfoAndMerchantInfo(Map<String, Object> map) throws SQLException {
		return merchantInfoDao.getMerchantRelInfoAndMerchantInfo(map);
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
			String msg ,List<ImportMessage> resultList ) {
		
		if(StringUtils.isBlank(content)) {
			ImportMessage message = new ImportMessage();
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
	private void setErrorMsg(int index , String msg ,List<ImportMessage> resultList) {
		ImportMessage message = new ImportMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}

	@Override
	public List<SelectBean> getAllSupplierSelectBean() throws SQLException {
		
		CustomerInfoModel querModel = new CustomerInfoModel() ;
		querModel.setCusType(DERP_SYS.CUSTOMERINFO_CUSTYPE_2);
		
		List<CustomerInfoModel> list = customerInfoDao.list(querModel);
		
		List<SelectBean> selectList = new ArrayList<SelectBean>() ;
		
		for (CustomerInfoModel supplier : list) {
			SelectBean selectBean = new SelectBean() ;
			
			selectBean.setValue(supplier.getId().toString());
			selectBean.setLabel(supplier.getName());
			
			selectList.add(selectBean) ;
		}
		
		return selectList;
	}

	@Override
	public List<Map<String, Object>> getCustomerMerchantRelList(Map<String, Object> map) throws Exception {
		return customerMerchantRelDao.getCustomerMerchantRelList(map);
	}

	@Override
	public List<CustomerMerchantRelModel> getCustMerListByUser(Map<String, Object> map) throws SQLException {
		List<CustomerMerchantRelModel> relQureyList = customerMerchantRelDao.getCustMerListByUser(map);
		return relQureyList;
	}

	/**
	 * 获取客户供应商公共方法
	 */
	@Override
	public List<SelectBean> getCusOrSurpSelectBean(CustomerInfoDTO dto) throws SQLException {
		return customerInfoDao.getCusOrSurpSelectBean(dto);
	}

}
