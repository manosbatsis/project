package com.topideal.order.service.common.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SdTypeConfigDao;
import com.topideal.entity.dto.common.SdTypeConfigDTO;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import com.topideal.order.service.common.SdTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SdTypeServiceImpl implements SdTypeService{
	
	@Autowired
	SdTypeConfigDao sdTypeConfigDao ;

	@Override
	public SdTypeConfigDTO getSdTypeConfigListByPage(SdTypeConfigDTO dto) {
		return sdTypeConfigDao.getSdTypeConfigListByPage(dto);
	}

	@Override
	public void saveOrModify(SdTypeConfigModel model, User user) throws Exception {
		
		SdTypeConfigModel queryModel = new SdTypeConfigModel() ;
		queryModel.setSdTypeName(model.getSdTypeName());
				
		queryModel = sdTypeConfigDao.searchByModel(queryModel) ;
		
		if(queryModel != null && (model.getId() == null || queryModel.getId() != model.getId())) {
			throw new RuntimeException("SD类型已存在") ;
		}
		
		queryModel = new SdTypeConfigModel() ;
		queryModel.setSdSimpleName(model.getSdSimpleName());
				
		queryModel = sdTypeConfigDao.searchByModel(queryModel) ;
		
		if(queryModel != null && (model.getId() == null || queryModel.getId() != model.getId())) {
			throw new RuntimeException("简称已存在") ;
		}

		if(model.getId() == null) {
			model.setCreateDate(TimeUtils.getNow());
			model.setCreator(user.getName());
			model.setCreatorId(user.getId());
			
			sdTypeConfigDao.save(model) ;
		}else {
			model.setModifier(user.getName());
			model.setModifierId(user.getId());
			model.setModifyDate(TimeUtils.getNow());
			
			sdTypeConfigDao.modify(model) ;
		}
	}

	@Override
	public SdTypeConfigModel searchByModel(SdTypeConfigModel model) throws SQLException {
		return sdTypeConfigDao.searchByModel(model);
	}

	@Override
	public List<SdTypeConfigModel> getList(SdTypeConfigModel model) throws SQLException {
		
		model.setStatus(DERP_ORDER.SDTYPECONFIG_STATUS_1);
		
		return sdTypeConfigDao.list(model);
	}

}
