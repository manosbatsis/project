package com.topideal.order.service.filetemp.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.CustomsFileConfigDao;
import com.topideal.dao.common.CustomsFileDepotRelDao;
import com.topideal.dao.common.FileTempDao;
import com.topideal.entity.dto.common.CustomsFileConfigDTO;
import com.topideal.entity.dto.common.CustomsFileDepotRelDTO;
import com.topideal.entity.vo.common.CustomsFileConfigModel;
import com.topideal.entity.vo.common.CustomsFileDepotRelModel;
import com.topideal.mongo.dao.DepotCustomsRelMongoDao;
import com.topideal.mongo.entity.DepotCustomsRelMongo;
import com.topideal.order.service.filetemp.CustomsFileConfigService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class CustomsFileConfigServiceImpl implements CustomsFileConfigService{
	@Autowired
	private CustomsFileConfigDao customsFileConfigDao;
	@Autowired
	private CustomsFileDepotRelDao customsFileDepotRelDao;
	@Autowired
	FileTempDao fileTempDao ;
	@Autowired
	DepotCustomsRelMongoDao  depotCustomsRelMongoDao;
	
	@Override
	public CustomsFileConfigDTO listDTOByPage(CustomsFileConfigDTO dto) throws SQLException {		
		Integer total = customsFileConfigDao.getCountOrder(dto);
		CustomsFileConfigDTO totalDto = customsFileConfigDao.listDTOByPage(dto);
		if(total == null) {
			total = 0;
		}
		totalDto.setTotal(total);
		return totalDto;
	}

	@Override
	public CustomsFileConfigModel searchById(Long id) throws SQLException {		
		return customsFileConfigDao.searchById(id);
	}

	@Override
	public List<CustomsFileDepotRelDTO> listDepotRel(Long fileId) throws SQLException {
		CustomsFileDepotRelDTO relDTO = new CustomsFileDepotRelDTO();
		relDTO.setCustomsFileConfigId(fileId);
		return customsFileDepotRelDao.listDTO(relDTO);
	}

	@Override
	public  Map<String, String> saveOrModity(String json,User user) throws Exception {
		 Map<String, String> result = new HashMap<String, String>();
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);		
		String depotConfig = (String) jsonObj.get("depotConfig");//进出仓配置
		String status = (String) jsonObj.get("status");//状态
		String  fileTempName  = (String) jsonObj.get("fileTempName");
		Long  fileTempId = Long.valueOf(jsonObj.getString("fileTempId"));
		String  isSameArea  = (String) jsonObj.get("isSameArea");
		String fileTempCode = (String) jsonObj.get("fileTempCode");
		Long id = null;
		if(StringUtils.isNotBlank(jsonObj.getString("id"))) {
			id = Long.valueOf(jsonObj.getString("id"));			
		}
		CustomsFileConfigModel configModel = new CustomsFileConfigModel();
		configModel.setFileTempId(fileTempId);
		configModel.setFileTempCode(fileTempCode);
		configModel.setFileTempName(fileTempName);
		configModel.setDepotConfig(depotConfig);
		configModel.setStatus(status);
		configModel.setIsSameArea(isSameArea);
		// 解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("depotRelList"));
		List<CustomsFileDepotRelModel> relList = new ArrayList<CustomsFileDepotRelModel>();
		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			Long depotId = Long.valueOf(job.getString("depotId"));//仓库id
			String depotName = (String) job.get("depotName");//仓库名称
			String depotCode = (String) job.get("depotCode");//仓库编码
			String platformCustomsIds = (String) job.get("platformCustomsIds");//平台关区id集合			
			
			if(StringUtils.isNotBlank(platformCustomsIds)) {
				String[] platformCustomsIdArr = platformCustomsIds.split(",");
				for(String platformCustomsId :  platformCustomsIdArr) {
					CustomsFileDepotRelModel relModel = new CustomsFileDepotRelModel();
					
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("depotId", depotId);
					params.put("customsAreaId", Long.valueOf(platformCustomsId));
					DepotCustomsRelMongo depotCustomsRelMong = depotCustomsRelMongoDao.findOne(params);//平台关区信息
					if(depotCustomsRelMong != null) {
						relModel.setCustomsId(Long.valueOf(platformCustomsId));
						relModel.setCustomsCode(depotCustomsRelMong.getCustomsAreaCode());
						relModel.setPlatformCustomsName(depotCustomsRelMong.getCustomsAreaName());
					}else {
						result.put("code", "01");
						result.put("message", "保存失败，仓库："+ depotName +" 未关联选中平台关区");
						return result;
					}
					
					relModel.setDepotId(depotId);
					relModel.setDepotCode(depotCode);
					relModel.setDepotName(depotName);
					relModel.setCustomsId(Long.valueOf(platformCustomsId));
					relList.add(relModel);
				}
			}else {
				CustomsFileDepotRelModel relModel = new CustomsFileDepotRelModel();
				relModel.setDepotId(depotId);
				relModel.setDepotCode(depotCode);
				relModel.setDepotName(depotName);
				relList.add(relModel);
			}
		}
		//判断是否有相同的配置
		List<CustomsFileConfigModel> existList = customsFileConfigDao.list(configModel);
		if(existList != null && existList.size() > 0) {
			for(CustomsFileConfigModel existModel : existList) {
				if(existModel != null && !existModel.getId().equals(id)) {
					//判断关联仓库 、选择关区等是否一致
					Boolean flag = true;
					a:for(CustomsFileDepotRelModel relModel : relList) {
						CustomsFileDepotRelModel existRelModel = new CustomsFileDepotRelModel();
						existRelModel.setCustomsFileConfigId(existModel.getId());
						existRelModel.setDepotId(relModel.getDepotId());
//						existRelModel.setCustomsId(relModel.getCustomsId());
						List<CustomsFileDepotRelModel >existRelList = customsFileDepotRelDao.list(existRelModel);
						if(existRelList != null && existRelList.size() > 0) {
							for(CustomsFileDepotRelModel existRel:existRelList) {
								if(existRel.getCustomsId() == relModel.getCustomsId()) {
									flag = false;
									break a;
								}
							}							
							
						}
					}
					if(!flag) {
						result.put("code", "01");
						result.put("message", "保存失败，已存在相同配置");
						return result;
					}
				}
			}
		}		
		
		
		//新增
		if(id == null) {
			configModel.setCreater(user.getId());
			configModel.setCreateDate(TimeUtils.getNow());
			Long num = customsFileConfigDao.save(configModel);
			
			for(CustomsFileDepotRelModel depotRelModel : relList) {
				depotRelModel.setCustomsFileConfigId(num);
				depotRelModel.setCreateDate(TimeUtils.getNow());
				customsFileDepotRelDao.save(depotRelModel);
			}			
		}else {//编辑
			//先删除之前的仓库关联
			CustomsFileDepotRelModel relModel = new CustomsFileDepotRelModel();
			relModel.setCustomsFileConfigId(id);
			customsFileDepotRelDao.delByModel(relModel);
			
			//更新配置
			configModel.setId(id);
			configModel.setModifier(user.getId());
			configModel.setModifyDate(TimeUtils.getNow());
			customsFileConfigDao.modify(configModel);
			//重新新增仓库关联
			for(CustomsFileDepotRelModel depotRelModel : relList) {
				depotRelModel.setId(null);
				depotRelModel.setCustomsFileConfigId(id);
				depotRelModel.setCreateDate(TimeUtils.getNow());
				customsFileDepotRelDao.save(depotRelModel);
			}
		}
		result.put("code", "00");
		return result;
	}

	@Override
	public void delCustomsFileConfig(List<Long> ids) throws SQLException {
		for(Long id : ids) {
			CustomsFileConfigModel model = customsFileConfigDao.searchById(id);
			if(model != null && DERP_ORDER.CUSTOMSFILECONFIG_STATUS_1.equals(model.getStatus())) {
				throw new RuntimeException("删除失败，只能删除禁用的清关单证配置") ;
			}
		}
		int num = customsFileConfigDao.delete(ids);
		if(num > 0) {
			for(Long id:ids) {
				CustomsFileDepotRelModel relModel = new CustomsFileDepotRelModel();
				relModel.setCustomsFileConfigId(id);
				customsFileDepotRelDao.delByModel(relModel);
			}
		}
		
	}

	@Override
	public Map<String, Object> getExportTemplate(String json) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);		
		String isSameArea = null;// 是否同关区
		if(jsonObj.containsKey("isSameArea") && jsonObj.getString("isSameArea") != null && !"".equals(jsonObj.getString("isSameArea"))){
			isSameArea =(String) jsonObj.get("isSameArea");
		}
		Long outDepotId = null; //出仓id
		if(jsonObj.containsKey("outDepotId") && jsonObj.getString("outDepotId") != null && !"".equals(jsonObj.getString("outDepotId"))){
			outDepotId = Long.valueOf(jsonObj.getString("outDepotId"));
		}
		Long inDepotId  = null;//入仓id
		if(jsonObj.containsKey("inDepotId") && jsonObj.getString("inDepotId") != null && !"".equals(jsonObj.getString("inDepotId"))){
			inDepotId = Long.valueOf(jsonObj.getString("inDepotId"));
		}
		Long outCustomsId  = null;//出仓关区
		if(jsonObj.containsKey("outCustomsId") && jsonObj.getString("outCustomsId") != null && !"".equals(jsonObj.getString("outCustomsId"))){
			outCustomsId = Long.valueOf(jsonObj.getString("outCustomsId"));
		}
		Long inCustomsId  = null;//入仓关区
		if(jsonObj.containsKey("inCustomsId") && jsonObj.getString("inCustomsId") != null && !"".equals(jsonObj.getString("inCustomsId"))){
			inCustomsId = Long.valueOf(jsonObj.getString("inCustomsId"));
		}
		
		//出仓 同关区必填
		List<CustomsFileConfigDTO> outList = null;
		if(outDepotId != null) {
			CustomsFileConfigDTO outConfigDTO = new CustomsFileConfigDTO();
			outConfigDTO.setDepotId(outDepotId);
			outConfigDTO.setIsSameArea(isSameArea);
			outConfigDTO.setDepotConfig(DERP_ORDER.CUSTOMSFILECONFIG_DEPOTCONFIG_1);
			outConfigDTO.setStatus(DERP_ORDER.CUSTOMSFILECONFIG_STATUS_1);
			if(outCustomsId != null) {
				outConfigDTO.setCustomsId(outCustomsId);
			}
			outList = customsFileConfigDao.getExportTemplate(outConfigDTO);
		}
		
		//入仓
		List<CustomsFileConfigDTO> inList = null;
		if(inDepotId != null) {
			CustomsFileConfigDTO inConfigDTO = new CustomsFileConfigDTO();
			inConfigDTO.setDepotId(inDepotId);
			inConfigDTO.setDepotConfig(DERP_ORDER.CUSTOMSFILECONFIG_DEPOTCONFIG_0);
			inConfigDTO.setStatus(DERP_ORDER.CUSTOMSFILECONFIG_STATUS_1);
			if(inCustomsId != null) {
				inConfigDTO.setCustomsId(inCustomsId);
			}
			inList = customsFileConfigDao.getExportTemplate(inConfigDTO);
		}
		
		// 出仓模板 和 入仓模板都为空，则返回 null
		if((outList == null || outList.size() < 1) && ((inList == null || inList.size() < 1))) {
			result.put("code", "01");
			
		}else {	
			result.put("outList", outList);
			result.put("inList", inList);
			
			if(outList != null && outList.size() > 1 ) {
				result.put("code", "02");				
			}
			if(inList != null && inList.size() > 1 ) {
				result.put("code", "02");				
			}
			
			if(outList != null && outList.size() == 1 && inList != null && inList.size() == 1 ) {
				result.put("code", "00");
			}else if((outList == null || outList.size() < 1) && (inList != null && inList.size() == 1)) {
				result.put("code", "00");
			}else if((outList != null && outList.size() == 1) && (inList == null || inList.size() < 1)) {
				result.put("code", "00");
			}
		}
		
		return result;
	}
}
