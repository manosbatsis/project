package com.topideal.order.service.common.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.DepartmentQuatoDao;
import com.topideal.dao.common.DepartmentQuatoItemDao;
import com.topideal.dao.purchase.ProjectQuotaConfigDao;
import com.topideal.entity.dto.common.DepartmentQuatoDTO;
import com.topideal.entity.dto.common.DepartmentQuatoItemDTO;
import com.topideal.entity.vo.common.DepartmentQuatoItemModel;
import com.topideal.entity.vo.common.DepartmentQuatoModel;
import com.topideal.entity.vo.purchase.ProjectQuotaConfigModel;
import com.topideal.mongo.dao.BusinessUnitMongoDao;
import com.topideal.mongo.entity.BusinessUnitMongo;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.common.DepartmentQuotaConfigService;

@Service
public class DepartmentQuotaConfigServiceImpl implements DepartmentQuotaConfigService{

	@Autowired
	private DepartmentQuatoDao departmentQuatoDao ;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private BusinessUnitMongoDao businessUnitMongoDao ;
	@Autowired
	private ProjectQuotaConfigDao projectQuotaConfigDao ;
	@Autowired
	private DepartmentQuatoItemDao departmentQuatoItemDao ;
	
	@Override
	public DepartmentQuatoDTO getListByPage(DepartmentQuatoDTO dto, User user) {
		
		dto = departmentQuatoDao.getListByPage(dto) ;
		
		return dto;
	}

	@Override
	public Long saveOrUpdateDepartmentQuotaConfig(DepartmentQuatoDTO dto, User user) throws SQLException {
		
		DepartmentQuatoModel model = new DepartmentQuatoModel() ;
        BeanUtils.copyProperties(dto, model);
        
        if(model.getId() == null){
            model.setCreater(user.getId());
            model.setCreateName(user.getName());
            model.setCreateDate(TimeUtils.getNow());
            model.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_0);
            model.setUsedQuota(new BigDecimal(0));
            model.setSurplusQuota(new BigDecimal(0));

            departmentQuatoDao.save(model) ;
            
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_7, String.valueOf(dto.getId()), "新增", null, null);
            
        }else{
            model.setModifyDate(TimeUtils.getNow());

            departmentQuatoDao.modify(model) ;
            
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_7, String.valueOf(dto.getId()), "修改", null, null);
        }

        return model.getId();
	}

	@Override
	public DepartmentQuatoDTO getDepartmentQuotaConfigById(Long id) throws SQLException {
		
		DepartmentQuatoModel model = departmentQuatoDao.searchById(id);
		
		DepartmentQuatoDTO dto = new DepartmentQuatoDTO() ;

        BeanUtils.copyProperties(model, dto);

        return dto ;
		
	}

	@Override
	public void auditDepartmentQuotaConfig(Long id, User user) throws SQLException {
		
		DepartmentQuatoModel departmentQuatoModel = departmentQuatoDao.searchById(id);

        if(departmentQuatoModel == null){
            throw new DerpException("根据ID查询配置不存在") ;
        }
        
        DepartmentQuatoModel queryModel = new DepartmentQuatoModel() ;
        queryModel.setDepartmentId(departmentQuatoModel.getDepartmentId());
        queryModel.setEffectiveDate(departmentQuatoModel.getEffectiveDate());
        queryModel.setExpirationDate(departmentQuatoModel.getExpirationDate());
        queryModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        
        List<DepartmentQuatoModel> tempList = departmentQuatoDao.getCoincidenceTimeList(queryModel) ;
        
        if(!tempList.isEmpty()) {
        	throw new DerpException("相同部门在同个生效区间内仅能有一条额度记录") ;
        }
        
        Map<String, Object> busiMap = new HashMap<String, Object>() ;
        busiMap.put("departmentId", departmentQuatoModel.getDepartmentId()) ;
        
        Map<String, DepartmentQuatoItemModel> brandSuperiodMap = new HashMap<String, DepartmentQuatoItemModel>() ;
        List<BusinessUnitMongo> businessList = businessUnitMongoDao.findAll(busiMap);
        
        for (BusinessUnitMongo businessUnitMongo : businessList) {
			
        	ProjectQuotaConfigModel queryProjectQuatoConfigModel = new ProjectQuotaConfigModel() ;
        	queryProjectQuatoConfigModel.setBuId(businessUnitMongo.getBusinessUnitId());
        	queryProjectQuatoConfigModel.setCurrency(departmentQuatoModel.getCurrency());
        	queryProjectQuatoConfigModel.setEffectiveDate(departmentQuatoModel.getEffectiveDate());
        	queryProjectQuatoConfigModel.setExpirationDate(departmentQuatoModel.getExpirationDate());
        	queryProjectQuatoConfigModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        	
        	List<ProjectQuotaConfigModel> projectQuatoConfigList = projectQuotaConfigDao.getInScopeTimeList(queryProjectQuatoConfigModel);
        	
        	for (ProjectQuotaConfigModel projectQuotaConfigModel : projectQuatoConfigList) {
				
        		DepartmentQuatoItemModel departmentQuatoItemModel = brandSuperiodMap.get(projectQuotaConfigModel.getSuperiorParentBrand());
        		
        		if(departmentQuatoItemModel == null) {
        			
        			departmentQuatoItemModel = new DepartmentQuatoItemModel() ;
        			
        			departmentQuatoItemModel.setCurrency(departmentQuatoModel.getCurrency());
        			departmentQuatoItemModel.setQuato(new BigDecimal(0));
        			departmentQuatoItemModel.setSuperiorParentBrand(departmentQuatoItemModel.getSuperiorParentBrand());
        			departmentQuatoItemModel.setSuperiorParentBrandId(departmentQuatoItemModel.getSuperiorParentBrandId());
        			
        		}
        		
        		BigDecimal quato = departmentQuatoItemModel.getQuato();
        		quato = quato.add(projectQuotaConfigModel.getProjectQuota()) ;
        		
        		departmentQuatoItemModel.setQuato(quato);
        		
        		brandSuperiodMap.put(projectQuotaConfigModel.getSuperiorParentBrand(), departmentQuatoItemModel) ;
        		
			}
        	
		}
        
        List<DepartmentQuatoItemModel> itemList = new ArrayList<>(brandSuperiodMap.values()) ;
        
        BigDecimal usedQuota = itemList.stream().map(DepartmentQuatoItemModel::getQuato)
        		.reduce(BigDecimal.ZERO, BigDecimal::add);
        
        
        BigDecimal departmentQuota = departmentQuatoModel.getDepartmentQuota();
        
        BigDecimal surplusQuota = departmentQuota.subtract(usedQuota) ;
        
        departmentQuatoModel.setAuditDate(TimeUtils.getNow());
        departmentQuatoModel.setAuditer(user.getId());
        departmentQuatoModel.setAuditName(user.getName());
        departmentQuatoModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        departmentQuatoModel.setSurplusQuota(surplusQuota);
        departmentQuatoModel.setUsedQuota(usedQuota);
        
        departmentQuatoDao.modify(departmentQuatoModel) ;
        
        for (DepartmentQuatoItemModel departmentQuatoItemModel : itemList) {
        	departmentQuatoItemModel.setDepartmentQuatoId(departmentQuatoModel.getId());
        	departmentQuatoItemModel.setCreateDate(TimeUtils.getNow());
        	
        	departmentQuatoItemDao.save(departmentQuatoItemModel) ;
		}
        
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_7, String.valueOf(id), "审核", null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delDepartmentQuotaConfig(String ids) throws SQLException {
		
		List<Long> idsList = (List<Long>)StrUtils.parseIds(ids);
		
		for (Long id : idsList) {
			
			DepartmentQuatoModel departmentQuato = departmentQuatoDao.searchById(id);
			
			if(departmentQuato != null
					&& !DERP_ORDER.PROJECTQUOTACONFIG_STATUS_0.equals(departmentQuato.getStatus())) {
				throw new DerpException("仅对【待审核】状态的数据可删除") ;
			}
			
			DepartmentQuatoItemModel queryItemModel = new DepartmentQuatoItemModel() ;
			
			queryItemModel.setDepartmentQuatoId(departmentQuato.getDepartmentId());
			
			List<DepartmentQuatoItemModel> itemList = departmentQuatoItemDao.list(queryItemModel);
			
			List<Long> itemIds = itemList.stream().map(DepartmentQuatoItemModel::getId).collect(Collectors.toList());
			
			if(!itemIds.isEmpty()) {
				departmentQuatoItemDao.delete(itemIds) ;
			}
			
		}
		
		departmentQuatoDao.delete(idsList) ;
		
	}

	@Override
	public void updateDepartmentQuota(DepartmentQuatoDTO dto, User user) throws SQLException {
		
		DepartmentQuatoModel departmentQuato = departmentQuatoDao.searchById(dto.getId());
		
		BigDecimal departmentQuota = dto.getDepartmentQuota();
		
		BigDecimal usedQuota = departmentQuato.getUsedQuota();
		
		BigDecimal surplusQuota = departmentQuota.subtract(usedQuota) ;
		
		DepartmentQuatoModel updateModel = new DepartmentQuatoModel() ;
		updateModel.setId(dto.getId());
		updateModel.setDepartmentQuota(departmentQuota);
		updateModel.setSurplusQuota(surplusQuota);
		updateModel.setUsedQuota(usedQuota);
		updateModel.setModifyDate(TimeUtils.getNow());
		
		departmentQuatoDao.modify(updateModel) ;
		
		String remarks = "原有额度：" +  departmentQuato.getDepartmentQuota().toString() + ", 调整额度：" + departmentQuota.toString();
		
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_7, String.valueOf(dto.getId()), "调整额度", null, remarks);
		
	}

	@Override
	public List<DepartmentQuatoItemDTO> getItemList(Long departmentQuatoId) throws SQLException {
		
		DepartmentQuatoItemModel itemModel = new DepartmentQuatoItemModel() ;
		
		itemModel.setDepartmentQuatoId(departmentQuatoId);
		
		List<DepartmentQuatoItemModel> itemList = departmentQuatoItemDao.list(itemModel);
		List<DepartmentQuatoItemDTO> itemDtoList = new ArrayList<DepartmentQuatoItemDTO>() ;
		
		for (DepartmentQuatoItemModel departmentQuatoItemModel : itemList) {
			
			DepartmentQuatoItemDTO dto = new DepartmentQuatoItemDTO() ;
			
			BeanUtils.copyProperties(departmentQuatoItemModel, dto);
			
			itemDtoList.add(dto) ;
			
		}
		
		return itemDtoList;
	}

}
