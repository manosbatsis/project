package com.topideal.service.main.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.user.UserBuRelModel;
import com.topideal.mongo.entity.UserBuRelMongo;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandParentDao;
import com.topideal.dao.main.BusinessUnitDao;
import com.topideal.dao.main.DepartmentInfoDao;
import com.topideal.dao.main.MerchantBuRelDao;
import com.topideal.dao.user.UserBuRelDao;
import com.topideal.entity.dto.main.BusinessUnitDTO;
import com.topideal.entity.vo.main.BusinessUnitModel;
import com.topideal.entity.vo.main.DepartmentInfoModel;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.service.main.BusinessUnitService;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService{

	@Autowired
	private BusinessUnitDao businessUnitDao ;
	
	@Autowired
	private BrandParentDao brandParentDao;
	
	@Autowired
	private MerchantBuRelDao merchantBuRelDao;
	
	@Autowired
	private UserBuRelDao userBuRelDao;
	@Autowired
	private DepartmentInfoDao departInfoDao;
	
	@Override
	public BusinessUnitDTO listBusinessUnits(BusinessUnitDTO dto) throws SQLException {
		return businessUnitDao.searchDTOByPage(dto);
	}

	@Override
	public boolean save(BusinessUnitModel model) throws SQLException {
		model.setCreateDate(TimeUtils.getNow());
		model.setModifyDate(TimeUtils.getNow());
		Long id = businessUnitDao.save(model);
		
		if(id != null) {
			return true ;
		}
		
		return false ;
	}

	@Override
	public boolean modify(BusinessUnitModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		int rows = businessUnitDao.modify(model);
		
		if(rows > 0) {
			
			/*BrandParentModel queryModel = new BrandParentModel() ;
			queryModel.setBusinessUnitId(model.getId());
			
			List<BrandParentModel> list = brandParentDao.list(queryModel);
			
			for (BrandParentModel brandParentModel : list) {
				brandParentModel.setBusinessUnitName(model.getName());
				
				brandParentDao.modify(brandParentModel) ;
			}*/
			
			MerchantBuRelModel queryRelModel = new MerchantBuRelModel() ;
			queryRelModel.setBuId(model.getId());
			List<MerchantBuRelModel> relList = merchantBuRelDao.list(queryRelModel);
			
			for (MerchantBuRelModel merchantBuRelModel : relList) {
				merchantBuRelModel.setBuName(model.getName());
				merchantBuRelModel.setModifyDate(TimeUtils.getNow());
				
				merchantBuRelDao.modify(merchantBuRelModel) ;
			}
			
			return true ;
		}
		
		return false ;
	}

	@Override
	public BusinessUnitModel query(BusinessUnitModel model) throws SQLException {
		return businessUnitDao.searchById(model.getId());
	}

	@Override
	public List<BusinessUnitModel> list(BusinessUnitModel queryModel) throws SQLException {
		return businessUnitDao.list(queryModel);
	}

	@Override
	public boolean delBusinessUnits(BusinessUnitModel model) throws SQLException {
		
		List<Long> ids = new ArrayList<Long>() ;
		ids.add(model.getId()) ;
		
		int rows = businessUnitDao.delete(ids);
		
		if(rows > 0) {
			return true ;
		}
		
		return false ;
	}

	@Override
	public boolean judgeQuote(BusinessUnitModel model) {
		/**
		 * 查询是否被引用
		 */
		BusinessUnitModel judgeModel = businessUnitDao.judgeQuote(model); 
		
		if(judgeModel == null) {
			return false;
		}else {
			return true ;
		}
	}

	@Override
	public List<BusinessUnitModel> getAllList() throws SQLException {
		return businessUnitDao.list(new BusinessUnitModel());
	}
	/**查询编码/名称是否已存在 */
	@Override
	public List<BusinessUnitDTO> getcheckCodeName(BusinessUnitDTO model) {
		return businessUnitDao.getcheckCodeName(model);
	}

	@Override
	public List<SelectBean> getDepartSelectBeanByUserId(List<Long> buIds) throws SQLException {
		List<SelectBean> result = new ArrayList<SelectBean>();
		List<Long> ids = new ArrayList<Long>();	
		List<DepartmentInfoModel> departInfoList = new ArrayList<DepartmentInfoModel>();
		for(Long buId : buIds){
			BusinessUnitModel buModel = businessUnitDao.searchById(buId);
			if(buModel.getDepartmentId() != null && !ids.contains(buModel.getDepartmentId())){
				DepartmentInfoModel departInfo = departInfoDao.searchById(buModel.getDepartmentId());
				if(departInfo != null) {					
					departInfoList.add(departInfo);
				}
				ids.add(buModel.getDepartmentId());
			}
		}
		departInfoList = departInfoList.stream().sorted(Comparator.comparing(DepartmentInfoModel::getCode)).collect(Collectors.toList());
		for(DepartmentInfoModel depart : departInfoList) {
			SelectBean selectBean = new SelectBean();
			selectBean.setLabel(depart.getName());
			selectBean.setValue(depart.getId().toString());
			result.add(selectBean);
		}

		return result;
	}

	@Override
	public List<BusinessUnitDTO> listDTO(BusinessUnitDTO dto, User user) throws Exception {
		UserBuRelModel userBuRelModel = new UserBuRelModel();
		userBuRelModel.setUserId(user.getId());
        List<UserBuRelModel> userBuRelList = userBuRelDao.list(userBuRelModel);
        List<Long> userBuIds =userBuRelList.stream().map(UserBuRelModel :: getBuId).collect(Collectors.toList());  
        
        List<BusinessUnitDTO> businessList = businessUnitDao.listDTO(dto);
        List<BusinessUnitDTO> result = businessList.stream().filter(b -> userBuIds.contains(b.getId())).collect(Collectors.toList());
        
        
		return result;
	}

}
