package com.topideal.order.service.common.impl;

import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SdSaleTypeDao;
import com.topideal.entity.dto.common.SdSaleTypeDTO;
import com.topideal.entity.vo.common.SdSaleTypeModel;
import com.topideal.order.service.common.SdSaleTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SdSaleTypeServiceImpl implements SdSaleTypeService{
	
	@Autowired
	private SdSaleTypeDao sdSaleTypeDao;
	
	/**
	 * 查询列表 分页
	 */
	@Override
	public SdSaleTypeDTO listDTOByPage(SdSaleTypeDTO dto) throws Exception {		
		return sdSaleTypeDao.listDTOByPage(dto);
	}

	/**
	 * 保存/编辑 信息
	 */
	@Override
	public void saveSdSaleType(SdSaleTypeModel model,User user) throws Exception {
		if(model.getId() == null) {//新增
			SdSaleTypeModel existModel = new SdSaleTypeModel();
			existModel.setSdType(model.getSdType());
			existModel = sdSaleTypeDao.searchByModel(existModel);
			if(existModel != null) {
				throw new RuntimeException("SD类型已存在");
			}
			existModel = new SdSaleTypeModel();
			existModel.setSdTypeName(model.getSdTypeName());
			existModel = sdSaleTypeDao.searchByModel(existModel);
			if(existModel != null) {
				throw new RuntimeException("SD简称已存在");
			}			
			
			model.setCreator(user.getId());
			model.setCreateName(user.getName());
			model.setCreateDate(TimeUtils.getNow());
			sdSaleTypeDao.save(model);
		}else {//编辑
			model.setModifier(user.getId());
			model.setModifiyName(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			sdSaleTypeDao.modify(model);
		}
		
	}

	/**
	 * 根据id查询信息
	 */
	@Override
	public SdSaleTypeDTO searchById(Long id) throws Exception {		
		SdSaleTypeModel model = sdSaleTypeDao.searchById(id);
		SdSaleTypeDTO dto = new SdSaleTypeDTO();
		BeanUtils.copyProperties(model,dto); 
		return dto;
	}

	@Override
	public List<SdSaleTypeDTO> getSdSaleTypeList(SdSaleTypeDTO dto) throws Exception {
		return sdSaleTypeDao.listDTO(dto);
	}

}
