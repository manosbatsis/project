package com.topideal.order.service.common.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.bean.SelectBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.PlatformSettlementConfigDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.common.PlatformSettlementConfigDTO;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.order.service.common.PlatformSettlementConfigService;

/**
 * 平台费用映射表
 * @author lian_
 */
@Service
public class PlatformSettlementConfigServiceImpl implements PlatformSettlementConfigService{

	@Autowired
	private PlatformSettlementConfigDao dao;
	@Autowired
	private SettlementConfigDao settlementConfigDao;

	
	

	/**
	 * 分页查询
	 */
	@Override
	public PlatformSettlementConfigDTO getPlatSettlementListByPage(PlatformSettlementConfigDTO dto) throws SQLException {
		PlatformSettlementConfigDTO settlementListByPage = dao.getPlatSettlementListByPage(dto);
		return settlementListByPage;
	}


	
	/**
	 * 新增
	 */
	@Override
	public Map<String, Object> saveSettlement(PlatformSettlementConfigModel model) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");

		PlatformSettlementConfigModel qeury=new PlatformSettlementConfigModel();
		qeury.setStorePlatformCode(model.getStorePlatformCode());
		qeury.setName(model.getName());
		qeury = dao.searchByModel(qeury);
		if (qeury!=null) {
			retMap.put("code", "01");
			retMap.put("message", model.getStorePlatformName()+",已经存在该平台费项名称");
			return retMap;
		}

		try {
			dao.save(model);
		} catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", model.getStorePlatformName()+",已经存在该平台费项名称");
			return retMap;
		}
		return retMap;
	}



	/**
	 * 编辑
	 */
	@Override
	public Map<String, Object>  modifySettlement(PlatformSettlementConfigModel model,String oldName) throws Exception {
		Map<String, Object> retMap=new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");
		PlatformSettlementConfigModel qeury=null;
		if (!model.getName().equals(oldName)) {
			qeury= new PlatformSettlementConfigModel();
			qeury.setStorePlatformCode(model.getStorePlatformCode());;
			qeury.setName(model.getName());
			qeury = dao.searchByModel(qeury);
		}
		
		if (qeury!=null) {
			retMap.put("code", "01");
			retMap.put("message", model.getStorePlatformName()+",已经存在该平台费项名称");
			return retMap;
		}
		try {
			dao.modify(model);
		} catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", model.getStorePlatformName()+",已经存在该平台费项名称");
			return retMap;
		}
		return retMap;
	}


	@Override
	public List<PlatformSettlementConfigDTO> exportSettlementList(PlatformSettlementConfigDTO dto) throws SQLException {
		List<PlatformSettlementConfigDTO> list = dao.exportSettlementList(dto);
		return list;
	}

	@Override
	public boolean isOrNotEnable(PlatformSettlementConfigModel model) throws SQLException {
		int num = dao.modify(model);
		if (num>0) {
			return true;
		}
		return false;
	}



	@Override
	public PlatformSettlementConfigModel searchDetail(PlatformSettlementConfigModel model) throws SQLException {		
		return dao.searchByModel(model);
	}


	/**
	 * 导入
	 */
	@Override
	public Map savePlatformMerchandiseImport(List<Map<String, String>> data, User user) throws Exception {
		Map retMap = new HashMap();
		List<ImportErrorMessage> errorList = new ArrayList<>();
		int success = 0;//检查通过数量
		int failure = 0;//检查失败数量
		List<PlatformSettlementConfigModel> modelList = new ArrayList<>();
		
		Map<String, Object>containMap=new HashMap<>();
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			String storePlatformCode = map.get("平台编码");
			storePlatformCode = storePlatformCode.trim();
			String name = map.get("平台费项名称");
			name = name.trim();
			String settlementConfigName = map.get("经分销二级费项名称");
			settlementConfigName = settlementConfigName.trim();
			String platformNameKey=storePlatformCode+","+name;
			if (containMap.containsKey(platformNameKey)) {
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 2);
				message.setMessage("列表中存在多条相同的平台名称和编码");
				errorList.add(message);
				failure += 1;
				continue;
			}
			containMap.put(platformNameKey,platformNameKey);
			
			//检查归属账号
			if(StringUtils.isBlank(storePlatformCode)) {
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 2);
				message.setMessage("平台编码不能为空");
				errorList.add(message);
				failure += 1;
				continue;
			}
			String storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
			if(StringUtils.isBlank(storePlatformName)) {
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 2);
				message.setMessage("根据平台编码没有找到平台名称");
				errorList.add(message);
				failure += 1;
				continue;
			}
			if(StringUtils.isBlank(name)) {
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 2);
				message.setMessage("平台费项名称不能为空");
				errorList.add(message);
				failure += 1;
				continue;
			}
			if(StringUtils.isBlank(settlementConfigName)) {
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 2);
				message.setMessage("经分销二级费项名称不能为空");
				errorList.add(message);
				failure += 1;
				continue;
			}




			//检查商品编码在本账号下是否已存在
			PlatformSettlementConfigModel platformSettlement = new PlatformSettlementConfigModel();
			platformSettlement.setName(name);
			platformSettlement.setStorePlatformCode(storePlatformCode);
			platformSettlement = dao.searchByModel(platformSettlement);
			if (platformSettlement!=null) {
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 2);
				message.setMessage(storePlatformCode+",名称:"+name+"数据库已经存在");
				errorList.add(message);
				failure += 1;
				continue;
			}
			//
			SettlementConfigModel settlementConfig= new SettlementConfigModel();
			settlementConfig.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
			settlementConfig.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
			settlementConfig.setProjectName(settlementConfigName);
			settlementConfig = settlementConfigDao.searchByModel(settlementConfig);
			if (settlementConfig==null) {
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(i + 2);
				message.setMessage("根据平台名称:"+storePlatformCode+",费用项名"+name+"没有找到费用项");
				errorList.add(message);
				failure += 1;
				continue;
			}
			PlatformSettlementConfigModel savePlatform = new PlatformSettlementConfigModel();
			savePlatform.setStorePlatformName(storePlatformName);// 平台名称
			savePlatform.setStorePlatformCode(storePlatformCode);// 平台编码
			savePlatform.setName(name);//名称
			savePlatform.setSettlementConfigId(settlementConfig.getId());
			savePlatform.setSettlementConfigName(settlementConfigName);
			savePlatform.setNcPaymentId(settlementConfig.getPaymentSubjectId());
			savePlatform.setNcPaymentName(settlementConfig.getPaymentSubjectName());
			savePlatform.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
			savePlatform.setCreater(user.getId());
			savePlatform.setCreaterName(user.getName());
			Timestamp now = TimeUtils.getNow();
			savePlatform.setCreateDate(now);
			modelList.add(savePlatform);
		}

		if(failure>0){
			retMap.put("success", success);
			retMap.put("failure", failure);
			retMap.put("errorList", errorList);
			return retMap;
		}
		// 循环新增
		for (PlatformSettlementConfigModel model : modelList) {
			dao.save(model);
			success++;
		}

		retMap.put("success", success);
		retMap.put("failure", failure);
		retMap.put("errorList", errorList);
		return retMap;
	}

	@Override
	public List<PlatformSettlementConfigDTO> getSelectBean(String storePlatformCode) {
		return dao.getSelectBean(storePlatformCode);
	}


}
