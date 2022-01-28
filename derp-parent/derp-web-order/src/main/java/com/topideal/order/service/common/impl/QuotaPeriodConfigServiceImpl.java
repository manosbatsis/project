package com.topideal.order.service.common.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.DepartmentQuatoDao;
import com.topideal.dao.common.DepartmentQuatoItemDao;
import com.topideal.dao.common.QuotaPeriodConfigDao;
import com.topideal.entity.dto.common.QuotaPeriodConfigDTO;
import com.topideal.entity.vo.common.DepartmentQuatoItemModel;
import com.topideal.entity.vo.common.DepartmentQuatoModel;
import com.topideal.entity.vo.common.QuotaPeriodConfigModel;
import com.topideal.mongo.dao.BrandSuperiorMongoDao;
import com.topideal.mongo.dao.BusinessUnitMongoDao;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.BrandSuperiorMongo;
import com.topideal.mongo.entity.BusinessUnitMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.common.QuotaPeriodConfigService;

@Service
public class QuotaPeriodConfigServiceImpl implements QuotaPeriodConfigService{

	@Autowired
	private QuotaPeriodConfigDao quotaPeriodConfigDao ;
	@Autowired
    private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao ;
	@Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao ;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
    private BusinessUnitMongoDao businessUnitMongoDao ;
	@Autowired
    private DepartmentQuatoDao departmentQuatoDao ;
	@Autowired
    private DepartmentQuatoItemDao departmentQuatoItemDao ;

	@SuppressWarnings("unchecked")
	@Override
	public QuotaPeriodConfigDTO getListByPage(QuotaPeriodConfigDTO dto, User user) {
		
		if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }

            dto.setBuIds(buIds);

        }
		
		dto = quotaPeriodConfigDao.getListByPage(dto) ;
		
		return dto;
	}

	@Override
	public Long saveOrUpdateQuotaPeriodConfig(QuotaPeriodConfigDTO dto, User user) throws Exception {
		
		QuotaPeriodConfigModel model = new QuotaPeriodConfigModel() ;
        BeanUtils.copyProperties(dto, model);

        Map<String, Object> queryMap = new HashMap<>() ;
        queryMap.put("buId", model.getBuId()) ;
        queryMap.put("merchantId", user.getMerchantId()) ;

        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(queryMap);

        if(merchantBuRelMongo == null){
            throw new DerpException("公司事业部关联不存在") ;
        }
        
        if(DERP_ORDER.QUOTACONFIG_quotaType_1.equals(dto.getQuotaType())) {
        	
        	queryMap = new HashMap<>() ;
            queryMap.put("brandSuperiorId", model.getConfigObjectId()) ;

            BrandSuperiorMongo superiorMongoDao = brandSuperiorMongoDao.findOne(queryMap);

            if(model.getConfigObjectId() != null && superiorMongoDao == null){
                throw new DerpException("标准母品牌不存在") ;
            }

            model.setConfigObjectName(superiorMongoDao.getName());
            
        }else if(DERP_ORDER.QUOTACONFIG_quotaType_2.equals(dto.getQuotaType())) {
        	
        	queryMap = new HashMap<>() ;
            queryMap.put("customerid", model.getConfigObjectId()) ;
            
            CustomerInfoMogo customer = customerInfoMongoDao.findOne(queryMap);
    		
            if (customer == null) {
    			throw new DerpException("客户不存在") ;
    		}
            
            
            model.setConfigObjectName(customer.getName());
            
        }else {
        	throw new DerpException("非有效配置类型") ;
        }

        model.setBuName(merchantBuRelMongo.getBuName());
        
        if(model.getId() == null){
            model.setCreater(user.getId());
            model.setCreateName(user.getName());
            model.setCreateDate(TimeUtils.getNow());
            model.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_0);

            quotaPeriodConfigDao.save(model) ;
        }else{
            model.setModifyDate(TimeUtils.getNow());

            quotaPeriodConfigDao.modify(model) ;
        }

        return model.getId();
	}

	@Override
	public QuotaPeriodConfigDTO getQuotaPeriodConfigById(Long id) throws SQLException {
		
		QuotaPeriodConfigModel model = quotaPeriodConfigDao.searchById(id);
		
		QuotaPeriodConfigDTO dto = new QuotaPeriodConfigDTO() ;

        BeanUtils.copyProperties(model, dto);

        return dto ;
		
	}

	@Override
	public void auditQuotaPeriodConfig(Long id, User user) throws SQLException {

		QuotaPeriodConfigModel quotaPeriodConfigModel = quotaPeriodConfigDao.searchById(id);

        if(quotaPeriodConfigModel == null){
            throw new DerpException("根据ID查询配置不存在") ;
        }
        
        QuotaPeriodConfigModel queryModel = new QuotaPeriodConfigModel() ;
        queryModel.setBuId(quotaPeriodConfigModel.getBuId());
        queryModel.setQuotaType(quotaPeriodConfigModel.getQuotaType());
        queryModel.setConfigObjectId(quotaPeriodConfigModel.getConfigObjectId());
        queryModel.setPeriodDate(quotaPeriodConfigModel.getPeriodDate());
        queryModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        
        queryModel = quotaPeriodConfigDao.searchByModel(queryModel) ;
        
        if(queryModel != null) {
        	throw new DerpException("同类型配置模块+同事业部+期初日期仅能存在一条已审核记录") ;
        }

        QuotaPeriodConfigModel updateModel = new QuotaPeriodConfigModel() ;
        updateModel.setId(id);
        updateModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        updateModel.setAuditDate(TimeUtils.getNow());
        updateModel.setAuditer(user.getId());
        updateModel.setAuditName(user.getName());

        quotaPeriodConfigDao.modify(updateModel) ;
	}

	@Override
	public List<SelectBean> getQuotaSelectBeanByBuId(Long buId) throws SQLException {
		
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
        queryMap.put("businessUnitId", buId) ;
        BusinessUnitMongo buMongo = businessUnitMongoDao.findOne(queryMap);
        
        if(buMongo == null || buMongo.getDepartmentId() == null) {
        	return null ;
        }
        
        Map<String, SelectBean> sumMap = new LinkedHashMap<String, SelectBean>() ;
        
        DepartmentQuatoModel departmentQuatoModel = new DepartmentQuatoModel() ;
        departmentQuatoModel.setDepartmentId(buMongo.getDepartmentId());
        departmentQuatoModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        
        List<DepartmentQuatoModel> departmentQuatoModelList = departmentQuatoDao.list(departmentQuatoModel) ;
        
        if(departmentQuatoModelList.isEmpty()) {
        	return null ;
        }
        
        for (DepartmentQuatoModel tempModel : departmentQuatoModelList) {
        	
        	DepartmentQuatoItemModel queryItemModel = new DepartmentQuatoItemModel() ;
            queryItemModel.setDepartmentQuatoId(tempModel.getId());
            
            List<DepartmentQuatoItemModel> itemList = departmentQuatoItemDao.list(queryItemModel);
            
            for (DepartmentQuatoItemModel departmentQuatoItemModel : itemList) {
            	
            	if(sumMap.containsKey(departmentQuatoItemModel.getSuperiorParentBrandId().toString())) {
            		continue ;
            	}
    			
            	SelectBean selectBean = new SelectBean() ;
            	selectBean.setLabel(departmentQuatoItemModel.getSuperiorParentBrand());
            	selectBean.setValue(departmentQuatoItemModel.getSuperiorParentBrandId().toString());
            	
            	sumMap.put(departmentQuatoItemModel.getSuperiorParentBrandId().toString(), selectBean) ;
    		}
            
		}
        
		return new ArrayList<>(sumMap.values());
	}
}
