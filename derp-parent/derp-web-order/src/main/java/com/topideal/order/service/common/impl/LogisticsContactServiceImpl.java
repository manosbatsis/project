package com.topideal.order.service.common.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.exception.DerpException;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.LogisticsContactTemplateDao;
import com.topideal.dao.common.LogisticsContactTemplateLinkDao;
import com.topideal.entity.dto.common.LogisticsContactTemplateDTO;
import com.topideal.entity.dto.common.LogisticsContactTemplateLinkDTO;
import com.topideal.entity.vo.common.LogisticsContactTemplateLinkModel;
import com.topideal.entity.vo.common.LogisticsContactTemplateModel;
import com.topideal.order.service.common.LogisticsContactService;

@Service
public class LogisticsContactServiceImpl implements LogisticsContactService {

	@Autowired
	private LogisticsContactTemplateDao logisticsContactTemplateDao ;
	@Autowired
	private LogisticsContactTemplateLinkDao logisticsContactTemplateLinkDao ;

	@Override
	public LogisticsContactTemplateDTO saveOrUpdateLogisTemplate(LogisticsContactTemplateModel saveModel) throws SQLException {
		
		if(saveModel.getId() == null) {
			
			saveModel.setCreateDate(TimeUtils.getNow());
			
			logisticsContactTemplateDao.save(saveModel) ;
			
		}else {
			
			saveModel.setModifyDate(TimeUtils.getNow());
			
			logisticsContactTemplateDao.modify(saveModel) ;
			
		}
		
		LogisticsContactTemplateDTO dto = new LogisticsContactTemplateDTO() ;
		
		BeanUtils.copyProperties(saveModel, dto);
		
		return dto;
	}

	@Override
	public LogisticsContactTemplateDTO getDetailsTemplate(Long id) throws SQLException {
		
		LogisticsContactTemplateModel model = logisticsContactTemplateDao.searchById(id);
		
		if(model == null) {
			throw new DerpException("物流联系人模版不存在") ;
		}
		
		LogisticsContactTemplateDTO dto = new LogisticsContactTemplateDTO() ;
		
		BeanUtils.copyProperties(model, dto);
		
		return dto;
	}

	@Override
	public void delTemplate(Long id) throws SQLException {
		
		List<Long> ids = new ArrayList<Long>() ;
		
		ids.add(id) ;
		
		logisticsContactTemplateDao.delete(ids) ;
	}

	@Override
	public List<LogisticsContactTemplateDTO> listTemplate(String type) throws SQLException {
		
		LogisticsContactTemplateModel queryModel = new LogisticsContactTemplateModel() ;
		queryModel.setType(type);
		
		List<LogisticsContactTemplateModel> list = logisticsContactTemplateDao.list(queryModel);
		List<LogisticsContactTemplateDTO> dtoList = new ArrayList<LogisticsContactTemplateDTO>();
		
		for (LogisticsContactTemplateModel model : list) {
			
			LogisticsContactTemplateDTO dto = new LogisticsContactTemplateDTO() ;
			
			BeanUtils.copyProperties(model, dto);
			
			dtoList.add(dto) ;
		}
		
		return dtoList;
	}

	@Override
	public LogisticsContactTemplateLinkDTO getDetailsTemplateLink(Long id) throws SQLException {
		
		LogisticsContactTemplateModel link = logisticsContactTemplateDao.searchById(id);
		
		LogisticsContactTemplateLinkDTO dto = new LogisticsContactTemplateLinkDTO() ;
		
		BeanUtils.copyProperties(link, dto);
		
		if(dto.getShipperTempId() != null) {
			LogisticsContactTemplateModel shipper = logisticsContactTemplateDao.searchById(dto.getShipperTempId());
			
			dto.setShipperTempDetails(shipper.getDetails());
		}
		
		if(dto.getCarrierInformationTempId() != null) {
			LogisticsContactTemplateModel carrierInformation = logisticsContactTemplateDao.searchById(dto.getCarrierInformationTempId());
			
			dto.setCarrierInformationTempDetails(carrierInformation.getDetails());
		}
		
		if(dto.getConsigneeTempId() != null) {
			LogisticsContactTemplateModel consignee = logisticsContactTemplateDao.searchById(dto.getConsigneeTempId());
			
			dto.setConsigneeTempDetails(consignee.getDetails());
		}
		
		if(dto.getDockingTempId() != null) {
			LogisticsContactTemplateModel docking = logisticsContactTemplateDao.searchById(dto.getDockingTempId());
			
			dto.setDockingTempDetails(docking.getDetails());
		}
		
		if(dto.getNotifierTempId() != null) {
			LogisticsContactTemplateModel notifier = logisticsContactTemplateDao.searchById(dto.getNotifierTempId());
			
			dto.setNotifierTempDetails(notifier.getDetails());
		}
		
		return dto;
	}

	@Override
	public LogisticsContactTemplateDTO saveOrUpdateLogisTemplateLink(LogisticsContactTemplateLinkModel saveModel) throws SQLException {
		
		if(saveModel.getShipperTempId() != null) {
			LogisticsContactTemplateModel shipper = logisticsContactTemplateDao.searchById(saveModel.getShipperTempId());
			
			saveModel.setShipperTempName(shipper.getName());
		}
		
		if(saveModel.getCarrierInformationTempId() != null) {
			LogisticsContactTemplateModel carrierInformation = logisticsContactTemplateDao.searchById(saveModel.getCarrierInformationTempId());
			
			saveModel.setCarrierInformationTempName(carrierInformation.getName());
		}
		
		if(saveModel.getConsigneeTempId() != null) {
			LogisticsContactTemplateModel consignee = logisticsContactTemplateDao.searchById(saveModel.getConsigneeTempId());
			
			saveModel.setConsigneeTempName(consignee.getName());
		}
		
		if(saveModel.getDockingTempId() != null) {
			LogisticsContactTemplateModel docking = logisticsContactTemplateDao.searchById(saveModel.getDockingTempId());
			
			saveModel.setDockingTempName(docking.getName());
		}
		
		if(saveModel.getNotifierTempId() != null) {
			LogisticsContactTemplateModel notifier = logisticsContactTemplateDao.searchById(saveModel.getNotifierTempId());
			
			saveModel.setNotifierTempName(notifier.getName());
		}
		
		if(saveModel.getId() == null) {
			saveModel.setCreateDate(TimeUtils.getNow());
			
			logisticsContactTemplateLinkDao.save(saveModel) ;
		}else {
			saveModel.setModifyDate(TimeUtils.getNow());
			
			logisticsContactTemplateLinkDao.modify(saveModel) ;
		}
		
		LogisticsContactTemplateDTO dto = new LogisticsContactTemplateDTO() ;
		BeanUtils.copyProperties(saveModel, dto);
		
		return dto;
	}

	@Override
	public void delTemplateLink(Long id) throws SQLException {
		
		List<Long> ids = new ArrayList<Long>() ;
		
		ids.add(id) ;
		
		logisticsContactTemplateLinkDao.delete(ids) ;
		
	}

	@Override
	public List<LogisticsContactTemplateLinkDTO> listTemplateLink() throws SQLException {
		
		List<LogisticsContactTemplateLinkModel> list = logisticsContactTemplateLinkDao.list(new LogisticsContactTemplateLinkModel());
		List<LogisticsContactTemplateLinkDTO> dtoList = new ArrayList<>() ;
		
		for (LogisticsContactTemplateLinkModel logisticsContactTemplateLinkModel : list) {
			
			LogisticsContactTemplateLinkDTO dto = new LogisticsContactTemplateLinkDTO() ;
			BeanUtils.copyProperties(logisticsContactTemplateLinkModel, dto);
			
			dtoList.add(dto) ;
			
		}
		
		return dtoList;
	}
}
