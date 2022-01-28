package com.topideal.order.service.sale.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.OperateLogDao;
import com.topideal.dao.sale.CustomerQuotaConfigDao;
import com.topideal.entity.dto.sale.CustomerQuotaConfigDTO;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.sale.CustomerQuotaConfigModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.UserBuRelMongo;
import com.topideal.order.service.sale.CustomerQuotaConfigService;

@Service
public class CustomerQuotaConfigServiceImpl implements CustomerQuotaConfigService {

	@Autowired
	private CustomerQuotaConfigDao customerQuotaConfigDao;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
	private  OperateLogDao operateLogDao;
	
	/**
	 * 调整客户额度
	 */
	@Override
	public void updateCustomerQuota(CustomerQuotaConfigModel model, User user) throws Exception {
		CustomerQuotaConfigModel query = customerQuotaConfigDao.searchById(model.getId());
		
		
		// 调整客户额度
		customerQuotaConfigDao.modify(model);
		//保存日志
		//保存操作日志
		OperateLogModel saveLogModel = new OperateLogModel() ;
		saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_8);
		saveLogModel.setOperateDate(TimeUtils.getNow());
		saveLogModel.setOperater(user.getName());
		saveLogModel.setOperateId(user.getId());
		saveLogModel.setOperateAction("调整客户额度");
		saveLogModel.setOperateRemark("调整客户额度 旧客户额度:"+query.getCustomerQuota());
		saveLogModel.setRelCode(model.getId().toString());// 没有单号  存id
		saveLogModel.setCreateDate(TimeUtils.getNow());			
		operateLogDao.save(saveLogModel) ;
	}
	
	
	/**
	 * 获取分页数据
	 */
	@Override
	public CustomerQuotaConfigDTO listDTOByPage(CustomerQuotaConfigDTO dto,User user) throws Exception {		
		if (dto.getBuId() == null) {
			List<Long> buList = new ArrayList<Long>();
            Map<String, Object> queryMap = new HashMap<>() ;
            queryMap.put("userId", user.getId()) ;
            List<UserBuRelMongo> userBuRelMongoList = userBuRelMongoDao.findAll(queryMap);
            if(userBuRelMongoList != null && userBuRelMongoList.size() > 0) {
                for(UserBuRelMongo mongo : userBuRelMongoList) {
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
		return customerQuotaConfigDao.listDTOByPage(dto);
	}
	/**
	 * 保存
	 */
	@Override
	public void saveCustomerQuotaConfigDTO(CustomerQuotaConfigDTO dto, User user) throws Exception {
		saveConfig(dto,user,"1");
	}
	/**
	 * 审核
	 */
	@Override
	public void auditCustomerQuotaConfigDTO(CustomerQuotaConfigDTO dto, User user) throws Exception {
		CustomerQuotaConfigModel model = saveConfig(dto,user,"2");
		
		model.setAuditer(user.getId());
		model.setAuditName(user.getName());
		model.setAuditDate(TimeUtils.getNow());
		model.setStatus(DERP_ORDER.CUSTOMERQUOTACONFIG_STATUS_1);//已审核
		customerQuotaConfigDao.modify(model);
		
	}
	//新增、修改、审核 保存数据
	private CustomerQuotaConfigModel saveConfig(CustomerQuotaConfigDTO dto,User user,String operate) throws Exception{
		
		Map<String, Object> queryMap = new HashMap<>() ;
        queryMap.put("buId", dto.getBuId()) ;
        queryMap.put("userId", user.getId()) ;
        UserBuRelMongo userBuRelMongo = userBuRelMongoDao.findOne(queryMap);

        if(userBuRelMongo == null){
            throw new RuntimeException("用户事业部关联不存在") ;
        }
		
        CustomerInfoMogo customerInfo = null;
        Map<String,Object> param =  new HashMap<String,Object>();
        param.put("customerid", dto.getCustomerId());
        customerInfo= customerInfoMongoDao.findOne(param);
		
        if("2".equals(operate) && customerInfo == null) {
			throw new RuntimeException("客户信息不存在") ;						
		}
        //类型是审核的时候校验
        String operateRemark=null;
		if ("2".equals(operate) ) {
			if(customerInfo == null)throw new RuntimeException("客户信息不存在") ;
			operateRemark="审核";
			// 校验修改时 生效日期和失效日期不交叉
			// 根据商家+ 事业部+客户+状态
			CustomerQuotaConfigModel query = new CustomerQuotaConfigModel();
			query.setBuId(userBuRelMongo.getBuId());
			query.setCustomerId(customerInfo.getCustomerid());
			query.setStatus(DERP_ORDER.CUSTOMERQUOTACONFIG_STATUS_1);
			List<CustomerQuotaConfigModel> queryList = customerQuotaConfigDao.list(query);
			// 获取最小的生效日期 和最大的失效日期
			Timestamp effectiveDate=dto.getEffectiveDate();
			Timestamp expirationDate=dto.getExpirationDate(); 
			if (effectiveDate.getTime()>=expirationDate.getTime()) {
				throw new RuntimeException("失效日期必须大于生效日期") ;
			}
			//审核时盘点日期是否存在交叉
			for (CustomerQuotaConfigModel queryModel : queryList) {				
				 Timestamp effectiveDateQuery = queryModel.getEffectiveDate();//生效日期
				 Timestamp expirationQuery = queryModel.getExpirationDate();//失效日期
				 //存在交叉的情况
				 //1.页面传的失效日期在查下到的数据之间				 
				 if (expirationDate.getTime()>=effectiveDateQuery.getTime()&&expirationDate.getTime()<=expirationQuery.getTime()) {
					 throw new RuntimeException("生效日期和失效日期存在交叉情况") ;
				 }
				 //2.页面传的生效日期在查下到的数据之间
				 if (effectiveDate.getTime()>=effectiveDateQuery.getTime()&&effectiveDate.getTime()<=expirationQuery.getTime()) {
					 throw new RuntimeException("生效日期和失效日期存在交叉情况") ;
				 }
				 //3.  页面生效日期<=查询生效日期&& 页面 失效日期>=查询失效日期()
				 if (effectiveDate.getTime()<=effectiveDateQuery.getTime()&&expirationDate.getTime()>=expirationQuery.getTime()) {
					 throw new RuntimeException("生效日期和失效日期存在交叉情况") ;
				 }
			}
			
			
		}
        
		CustomerQuotaConfigModel model = new CustomerQuotaConfigModel();
		BeanUtils.copyProperties(dto, model);
		
		model.setBuName(userBuRelMongo.getBuName());
		if(customerInfo != null) {
			model.setCustomerName(customerInfo.getName());			
		}
		model.setStatus(DERP_ORDER.CUSTOMERQUOTACONFIG_STATUS_0);//待审核
		
		if(dto.getId() == null) {//新增
			model.setCreater(user.getId());
			model.setCreateName(user.getName());
			model.setCreateDate(TimeUtils.getNow());
			customerQuotaConfigDao.save(model);
			 /**记录操作日志*/
			OperateLogModel saveLogModel = new OperateLogModel() ;
			if (StringUtils.isBlank(operateRemark))operateRemark="新增";
			//保存操作日志
			saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_8);
			saveLogModel.setOperateDate(TimeUtils.getNow());
			saveLogModel.setOperater(user.getName());
			saveLogModel.setOperateId(user.getId());	
			saveLogModel.setOperateAction("新增");
			saveLogModel.setOperateRemark(operateRemark);
			saveLogModel.setRelCode(model.getId().toString());// 没有单号  存id
			saveLogModel.setCreateDate(TimeUtils.getNow());			
			operateLogDao.save(saveLogModel) ;

		}else {//编辑
			model.setModifier(user.getId());
			model.setModifyName(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			customerQuotaConfigDao.modify(model);
			/**记录操作日志*/
			OperateLogModel saveLogModel = new OperateLogModel() ;
			if (StringUtils.isBlank(operateRemark))operateRemark="编辑";
			saveLogModel.setModule(DERP_ORDER.OPERATE_LOG_MODULE_8);
			saveLogModel.setOperateDate(TimeUtils.getNow());
			saveLogModel.setOperater(user.getName());
			saveLogModel.setOperateId(user.getId());
			saveLogModel.setOperateAction("编辑");
			saveLogModel.setOperateRemark(operateRemark);
			saveLogModel.setRelCode(model.getId().toString());// 没有单号  存id
			saveLogModel.setCreateDate(TimeUtils.getNow());			
			operateLogDao.save(saveLogModel) ;
		}
		
		return model;
	} 
	
	//操作日志
	
	/**
	 * 删除
	 */
	@Override
	public void delCustomerQuotaConfigDTO(String ids) throws Exception {
		List<Long> list = StrUtils.parseIds(ids);
		for(Long id : list) {			
			CustomerQuotaConfigModel model = customerQuotaConfigDao.searchById(id);
			if(model == null) {
				throw new RuntimeException("配置ID："+id+" 不存在") ;
			}
			if(DERP_ORDER.CUSTOMERQUOTACONFIG_STATUS_1.equals(model.getStatus())) {
				throw new RuntimeException("只能删除“待审核”配置") ;
			}
		}		
		customerQuotaConfigDao.delete(list);
	}
	/**
	 * 事业部+客户 查询历史配置记录，默认带出最后一个已审核记录
	 */
	/*@Override
	public CustomerQuotaConfigDTO getLastestCustomerQuotaConfig(Long buId, Long customerId) throws Exception {
		Map<String,Object> paramMap = new HashedMap<String, Object>();
		paramMap.put("buId", buId);
		paramMap.put("customerId", customerId);
		List<CustomerQuotaConfigDTO> list = customerQuotaConfigDao.listEffectiveDTO(paramMap);
		
		CustomerQuotaConfigDTO dto = new CustomerQuotaConfigDTO();
		if(list != null && list.size() > 0) {
			dto = list.get(0);
		}
		
		return dto;
	}*/

}
