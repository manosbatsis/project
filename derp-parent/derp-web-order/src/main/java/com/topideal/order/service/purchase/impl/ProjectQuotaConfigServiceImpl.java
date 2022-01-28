package com.topideal.order.service.purchase.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.DepartmentQuatoDao;
import com.topideal.dao.common.DepartmentQuatoItemDao;
import com.topideal.dao.purchase.ProjectQuotaConfigDao;
import com.topideal.entity.dto.common.DepartmentQuatoDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaConfigDTO;
import com.topideal.entity.vo.common.DepartmentQuatoItemModel;
import com.topideal.entity.vo.common.DepartmentQuatoModel;
import com.topideal.entity.vo.purchase.ProjectQuotaConfigModel;
import com.topideal.mongo.dao.BrandSuperiorMongoDao;
import com.topideal.mongo.dao.BusinessUnitMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.BrandSuperiorMongo;
import com.topideal.mongo.entity.BusinessUnitMongo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.ProjectQuotaConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectQuotaConfigServiceImpl implements ProjectQuotaConfigService {

    @Autowired
    private ProjectQuotaConfigDao projectQuotaConfigDao ;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao ;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao ;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao ;
    @Autowired
    private DepartmentQuatoDao departmentQuatoDao ;
    @Autowired
    private BusinessUnitMongoDao businessUnitMongoDao ;
    @Autowired
    private DepartmentQuatoItemDao departmentQuatoItemDao ;
    @Autowired
	private CommonBusinessService commonBusinessService ;

    @SuppressWarnings("unchecked")
	@Override
    public ProjectQuotaConfigDTO getListByPage(ProjectQuotaConfigDTO dto, User user) {

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

        dto = projectQuotaConfigDao.getListByPage(dto) ;

        return dto;
    }

    @Override
    public Long saveOrUpdateProjectQuotaConfig(ProjectQuotaConfigDTO dto, User user) throws SQLException {

        ProjectQuotaConfigModel model = new ProjectQuotaConfigModel() ;
        BeanUtils.copyProperties(dto, model);

        Map<String, Object> queryMap = new HashMap<>() ;
        queryMap.put("buId", model.getBuId()) ;
        queryMap.put("merchantId", user.getMerchantId()) ;

        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(queryMap);

        if(merchantBuRelMongo == null){
            throw new DerpException("公司事业部关联不存在") ;
        }

        queryMap = new HashMap<>() ;
        queryMap.put("brandSuperiorId", model.getSuperiorParentBrandId()) ;

        BrandSuperiorMongo superiorMongoDao = brandSuperiorMongoDao.findOne(queryMap);

        if(model.getSuperiorParentBrandId() != null && superiorMongoDao == null){
            throw new DerpException("标准母品牌不存在") ;
        }

        //项目额度 类型为数值，最多2位小数的大于0的正数
        if(model.getProjectQuota() == null || model.getProjectQuota().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DerpException("项目额度不合理") ;
        }

        if(superiorMongoDao != null){
            model.setSuperiorParentBrand(superiorMongoDao.getName());
        }

        model.setBuName(merchantBuRelMongo.getBuName());

        if(model.getId() == null){
            model.setCreater(user.getId());
            model.setCreateName(user.getName());
            model.setCreateDate(TimeUtils.getNow());
            model.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_0);

            projectQuotaConfigDao.save(model) ;
            
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_9, String.valueOf(model.getId()), "新增", null, null);
            
        }else{
            model.setModifyDate(TimeUtils.getNow());

            projectQuotaConfigDao.modify(model) ;
            
            commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_9, String.valueOf(model.getId()), "编辑", null, null);
        }

        return model.getId();
    }

    @Override
    public void auditProjectQuotaConfig(Long id, User user) throws SQLException {
        ProjectQuotaConfigModel projectQuotaConfigModel = projectQuotaConfigDao.searchById(id);

        if(projectQuotaConfigModel == null){
            throw new DerpException("根据ID查询配置不存在") ;
        }
        
        ProjectQuotaConfigModel queryModel = new ProjectQuotaConfigModel() ;
        queryModel.setBuId(projectQuotaConfigModel.getBuId());
        queryModel.setSuperiorParentBrandId(projectQuotaConfigModel.getSuperiorParentBrandId());
        queryModel.setEffectiveDate(projectQuotaConfigModel.getEffectiveDate());
        queryModel.setExpirationDate(projectQuotaConfigModel.getExpirationDate());
        queryModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        
        List<ProjectQuotaConfigModel> tempList = projectQuotaConfigDao.getCoincidenceTimeList(queryModel) ;
        
        if(!tempList.isEmpty()){
        	throw new DerpException("相同事业部+母品牌在同个生效区间内仅能有一条额度记录") ;
        }
        
        Map<String, Object> queryMap = new HashMap<String, Object>() ;
        queryMap.put("businessUnitId", projectQuotaConfigModel.getBuId()) ;
        BusinessUnitMongo buMongo = businessUnitMongoDao.findOne(queryMap);
        
        if(buMongo == null || buMongo.getDepartmentId() == null) {
        	throw new DerpException("未找到事业部信息或未找到事业部关联部门信息") ;
        }
        
        DepartmentQuatoModel departmentQuatoModel = new DepartmentQuatoModel() ;
        departmentQuatoModel.setDepartmentId(buMongo.getDepartmentId());
        departmentQuatoModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        departmentQuatoModel.setEffectiveDate(projectQuotaConfigModel.getEffectiveDate());
        departmentQuatoModel.setExpirationDate(projectQuotaConfigModel.getExpirationDate());
        
        departmentQuatoModel = departmentQuatoDao.searchByModel(departmentQuatoModel) ;
        
        if(departmentQuatoModel == null) {
        	throw new DerpException("未找到关联部门额度配置信息") ;
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
        	
        	for (ProjectQuotaConfigModel tempConfigModel : projectQuatoConfigList) {
				
        		DepartmentQuatoItemModel departmentQuatoItemModel = brandSuperiodMap.get(tempConfigModel.getSuperiorParentBrand());
        		
        		if(departmentQuatoItemModel == null) {
        			
        			departmentQuatoItemModel = new DepartmentQuatoItemModel() ;
        			
        			departmentQuatoItemModel.setCurrency(departmentQuatoModel.getCurrency());
        			departmentQuatoItemModel.setQuato(new BigDecimal(0));
        			departmentQuatoItemModel.setSuperiorParentBrand(departmentQuatoItemModel.getSuperiorParentBrand());
        			departmentQuatoItemModel.setSuperiorParentBrandId(departmentQuatoItemModel.getSuperiorParentBrandId());
        			
        		}
        		
        		BigDecimal quato = departmentQuatoItemModel.getQuato();
        		quato = quato.add(tempConfigModel.getProjectQuota()) ;
        		
        		departmentQuatoItemModel.setQuato(quato);
        		
        		brandSuperiodMap.put(tempConfigModel.getSuperiorParentBrand(), departmentQuatoItemModel) ;
        		
			}
        	
		}
        
        List<DepartmentQuatoItemModel> itemList = new ArrayList<>(brandSuperiodMap.values()) ;
        
        BigDecimal usedQuota = itemList.stream().map(DepartmentQuatoItemModel::getQuato)
        		.reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal departmentQuota = departmentQuatoModel.getDepartmentQuota();
        
        BigDecimal surplusQuota = departmentQuota.subtract(usedQuota) ;
        
//        if(projectQuotaConfigModel.getProjectQuota().compareTo(surplusQuota) > 0) {
//        	throw new DerpException("项目额度不能大于部门剩余额度") ;
//        }
        
        surplusQuota = surplusQuota.subtract(projectQuotaConfigModel.getProjectQuota()) ;
        usedQuota = usedQuota.add(projectQuotaConfigModel.getProjectQuota()) ;
        
        departmentQuatoModel.setSurplusQuota(surplusQuota);
        departmentQuatoModel.setUsedQuota(usedQuota);
        departmentQuatoModel.setModifyDate(TimeUtils.getNow());
        
        departmentQuatoDao.modify(departmentQuatoModel) ;
        
        DepartmentQuatoItemModel departmentQuatoItemModel = new DepartmentQuatoItemModel() ;
        departmentQuatoItemModel.setDepartmentQuatoId(departmentQuatoModel.getId());
        departmentQuatoItemModel.setSuperiorParentBrandId(projectQuotaConfigModel.getSuperiorParentBrandId());
        
        departmentQuatoItemModel = departmentQuatoItemDao.searchByModel(departmentQuatoItemModel) ;
        
        if(departmentQuatoItemModel == null) {
        	
        	departmentQuatoItemModel = new DepartmentQuatoItemModel() ;
            departmentQuatoItemModel.setDepartmentQuatoId(departmentQuatoModel.getId());
            departmentQuatoItemModel.setSuperiorParentBrandId(projectQuotaConfigModel.getSuperiorParentBrandId());
            departmentQuatoItemModel.setSuperiorParentBrand(projectQuotaConfigModel.getSuperiorParentBrand());
            departmentQuatoItemModel.setQuato(projectQuotaConfigModel.getProjectQuota());
            departmentQuatoItemModel.setCurrency(departmentQuatoModel.getCurrency());
            departmentQuatoItemModel.setCreateDate(TimeUtils.getNow());
            
            departmentQuatoItemDao.save(departmentQuatoItemModel) ;
        }else {
        	
        	BigDecimal quato = departmentQuatoItemModel.getQuato();
        	quato = quato.add(projectQuotaConfigModel.getProjectQuota()) ;
        	
        	departmentQuatoItemModel.setQuato(quato);
        	departmentQuatoItemModel.setModifyDate(TimeUtils.getNow());
        	
        	departmentQuatoItemDao.modify(departmentQuatoItemModel) ;
        }
        
        ProjectQuotaConfigModel updateModel = new ProjectQuotaConfigModel() ;
        updateModel.setId(id);
        updateModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        updateModel.setAuditDate(TimeUtils.getNow());
        updateModel.setAuditer(user.getId());
        updateModel.setAuditName(user.getName());

        projectQuotaConfigDao.modify(updateModel) ;
        
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_9, String.valueOf(id), "审核", null, null);
    }

    @Override
    public void delProjectQuotaConfig(String ids) throws SQLException {

        String[] idArr = ids.split(",");

        List<Long> configIdList = new ArrayList<>() ;

        for (String id: idArr) {

            ProjectQuotaConfigModel queryModel = projectQuotaConfigDao.searchById(Long.valueOf(id));

            if(queryModel == null){
                throw new DerpException("根据ID查询配置不存在") ;
            }

            if(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1.equals(queryModel.getStatus())){
                throw new DerpException("存在已审核配置") ;
            }

            configIdList.add(Long.valueOf(id)) ;
            
        }

        projectQuotaConfigDao.delete(configIdList) ;
    }

    @Override
    public ProjectQuotaConfigDTO getProjectQuotaConfigById(Long id) throws SQLException {

        ProjectQuotaConfigModel projectQuotaConfigModel = projectQuotaConfigDao.searchById(id);

        ProjectQuotaConfigDTO dto = new ProjectQuotaConfigDTO() ;

        BeanUtils.copyProperties(projectQuotaConfigModel, dto);

        return dto ;
    }

    @Override
    public ProjectQuotaConfigDTO getLatestPeriodInfo(User user, Long buId, Long superiorParentBrandId) {

        Map<String, Object> queryMap = new HashMap<>() ;
        queryMap.put("buId", buId) ;
        queryMap.put("superiorParentBrandId", superiorParentBrandId) ;
        List<ProjectQuotaConfigModel> dtoList = projectQuotaConfigDao.getLatestConfigList(queryMap);

        ProjectQuotaConfigDTO dto = new ProjectQuotaConfigDTO() ;
        if(!dtoList.isEmpty()){
            ProjectQuotaConfigModel configModel = dtoList.get(0);

            BeanUtils.copyProperties(configModel, dto);

        }

        return dto;
    }

	@Override
	public DepartmentQuatoDTO getLatestDepartmentQuato(User user, Long buId) {
		
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		
		queryMap.put("businessUnitId", buId) ;
		
		BusinessUnitMongo busiUnit = businessUnitMongoDao.findOne(queryMap);
		
		if(busiUnit == null || busiUnit.getDepartmentId() == null) {
			return null ;
		}
		
		Long departmentId = busiUnit.getDepartmentId() ;

		queryMap = new HashMap<>() ;
		queryMap.put("departmentId", departmentId) ;
		
		DepartmentQuatoModel departmentQuatoModel = departmentQuatoDao.getLatestDepartmentQuato(queryMap) ;
		
		if(departmentQuatoModel == null) {
			throw new DerpException("请先配置部门额度") ;
		}
		
		DepartmentQuatoDTO dto = new DepartmentQuatoDTO() ;
		BeanUtils.copyProperties(departmentQuatoModel, dto);
		
		return dto;
	}

	@Override
	public void updateProjectQuota(ProjectQuotaConfigDTO dto, User user) throws SQLException {
		
		ProjectQuotaConfigModel projectQuotaConfigModel = projectQuotaConfigDao.searchById(dto.getId());
		ProjectQuotaConfigModel updateModel = new ProjectQuotaConfigModel() ;

        if(projectQuotaConfigModel == null){
            throw new DerpException("根据ID查询配置不存在") ;
        }
        
        Map<String, Object> queryMap = new HashMap<String, Object>() ;
        queryMap.put("businessUnitId", projectQuotaConfigModel.getBuId()) ;
        BusinessUnitMongo buMongo = businessUnitMongoDao.findOne(queryMap);
        
        if(buMongo == null || buMongo.getDepartmentId() == null) {
        	throw new DerpException("未找到事业部信息或未找到事业部关联部门信息") ;
        }
        
        DepartmentQuatoModel departmentQuatoModel = new DepartmentQuatoModel() ;
        departmentQuatoModel.setDepartmentId(buMongo.getDepartmentId());
        departmentQuatoModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        departmentQuatoModel.setEffectiveDate(projectQuotaConfigModel.getEffectiveDate());
        departmentQuatoModel.setExpirationDate(projectQuotaConfigModel.getExpirationDate());
        
        departmentQuatoModel = departmentQuatoDao.searchByModel(departmentQuatoModel) ;
        
        if(departmentQuatoModel == null) {
        	throw new DerpException("未找到关联部门额度配置信息") ;
        }
        
        BigDecimal usedQuota = new BigDecimal(0) ;
        
        Map<String, Object> busiMap = new HashMap<String, Object>() ;
        busiMap.put("departmentId", departmentQuatoModel.getDepartmentId()) ;
        
        List<BusinessUnitMongo> businessList = businessUnitMongoDao.findAll(busiMap);
        
        for (BusinessUnitMongo businessUnitMongo : businessList) {
			
        	ProjectQuotaConfigModel queryProjectQuatoConfigModel = new ProjectQuotaConfigModel() ;
        	queryProjectQuatoConfigModel.setBuId(businessUnitMongo.getBusinessUnitId());
        	queryProjectQuatoConfigModel.setCurrency(departmentQuatoModel.getCurrency());
        	queryProjectQuatoConfigModel.setEffectiveDate(departmentQuatoModel.getEffectiveDate());
        	queryProjectQuatoConfigModel.setExpirationDate(departmentQuatoModel.getExpirationDate());
        	queryProjectQuatoConfigModel.setStatus(DERP_ORDER.PROJECTQUOTACONFIG_STATUS_1);
        	
        	List<ProjectQuotaConfigModel> projectQuatoConfigList = projectQuotaConfigDao.getInScopeTimeList(queryProjectQuatoConfigModel);
        	
        	for (ProjectQuotaConfigModel tempConfigModel : projectQuatoConfigList) {
        		
        		if(tempConfigModel.getId().longValue() == dto.getId().longValue()) {
        			continue ;
        		}
				
        		usedQuota = usedQuota.add(tempConfigModel.getProjectQuota()) ;
			}
        	
		}
        
        BigDecimal departmentQuota = departmentQuatoModel.getDepartmentQuota();
        
        BigDecimal surplusQuota = departmentQuota.subtract(usedQuota) ;
        
//        if(dto.getProjectQuota().compareTo(surplusQuota) > 0) {
//        	throw new DerpException("项目额度不能大于部门剩余额度") ;
//        }
        
        surplusQuota = surplusQuota.subtract(dto.getProjectQuota()) ;
        usedQuota = usedQuota.add(dto.getProjectQuota()) ;
        
        //更新项目额度配置
        updateModel.setId(dto.getId());
        updateModel.setProjectQuota(dto.getProjectQuota());
        updateModel.setModifyDate(TimeUtils.getNow());
        
        projectQuotaConfigDao.modify(updateModel) ;
        
        //更新部门额度配置
        DepartmentQuatoModel updateDepartmentQuatoModel = new DepartmentQuatoModel() ;
        updateDepartmentQuatoModel.setId(departmentQuatoModel.getId());
        updateDepartmentQuatoModel.setSurplusQuota(surplusQuota);
        updateDepartmentQuatoModel.setUsedQuota(usedQuota);
        updateDepartmentQuatoModel.setModifyDate(TimeUtils.getNow());
		
		departmentQuatoDao.modify(updateDepartmentQuatoModel) ;
		
		//更新部门额度配置明细
		DepartmentQuatoItemModel queryItemModel = new DepartmentQuatoItemModel() ;
		queryItemModel.setDepartmentQuatoId(departmentQuatoModel.getId());
		queryItemModel.setSuperiorParentBrandId(projectQuotaConfigModel.getSuperiorParentBrandId());
		
		queryItemModel = departmentQuatoItemDao.searchByModel(queryItemModel) ;
		
		if(queryItemModel == null) {
			
			DepartmentQuatoItemModel saveModel = new DepartmentQuatoItemModel() ;
			saveModel.setDepartmentQuatoId(departmentQuatoModel.getId());
			saveModel.setCurrency(departmentQuatoModel.getCurrency());
			saveModel.setQuato(projectQuotaConfigModel.getProjectQuota());
			saveModel.setSuperiorParentBrand(projectQuotaConfigModel.getSuperiorParentBrand());
			saveModel.setSuperiorParentBrandId(projectQuotaConfigModel.getSuperiorParentBrandId());
			saveModel.setCreateDate(TimeUtils.getNow());
			
			departmentQuatoItemDao.save(saveModel) ;
			
		}else {
			
			BigDecimal quato = queryItemModel.getQuato();
			quato = quato.subtract(projectQuotaConfigModel.getProjectQuota()).add(dto.getProjectQuota()) ;
			
			queryItemModel.setQuato(quato);
			queryItemModel.setModifyDate(TimeUtils.getNow());
			
			departmentQuatoItemDao.modify(queryItemModel) ;
			
		}
		
		String remarks = "原有额度：" +  projectQuotaConfigModel.getProjectQuota().toString() + ", 调整额度：" + dto.getProjectQuota().toString();
		
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_9, String.valueOf(dto.getId()), "调整额度", null, remarks);
	}
}
