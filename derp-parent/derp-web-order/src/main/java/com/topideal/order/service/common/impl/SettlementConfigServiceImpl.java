package com.topideal.order.service.common.impl;

import java.sql.SQLException;
import java.util.*;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.dao.common.SettlementModuleRelDao;
import com.topideal.entity.vo.common.SettlementModuleRelModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.common.SettlementCustomerRelDao;
import com.topideal.entity.dto.common.SettlementConfigDTO;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.common.SettlementCustomerRelModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.order.service.common.SettlementConfigService;

import net.sf.json.JSONArray;

/**
 * 爬虫商品对照表
 * @author lian_
 */
@Service
public class SettlementConfigServiceImpl implements SettlementConfigService{

	@Autowired
	private SettlementConfigDao dao;
	@Autowired
	private SettlementCustomerRelDao settlementCustomerRelDao;
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private SettlementModuleRelDao settlementModuleRelDao;



	/**
	 * 分页查询
	 */
	@Override
	public SettlementConfigDTO getSettlementListByPage(SettlementConfigDTO dto) throws SQLException {
		SettlementConfigDTO settlementListByPage = dao.getSettlementListByPage(dto);
		List<SettlementConfigDTO> list = settlementListByPage.getList();

		for (SettlementConfigDTO model : list) {
			SettlementCustomerRelModel settlementCustomerRel=new SettlementCustomerRelModel();
			settlementCustomerRel.setSettlementConfigId(model.getId());
			List<SettlementCustomerRelModel> customerRelList = settlementCustomerRelDao.list(settlementCustomerRel);

			String customerNames="";
			for (SettlementCustomerRelModel relModel : customerRelList) {
				if (StringUtils.isBlank(customerNames)) {
					customerNames=relModel.getCustomerName();
				}else {
					customerNames=customerNames+","+relModel.getCustomerName();
				}

			}
			model.setCustomerNames(customerNames);
		}

		return settlementListByPage;
	}

	/**
	 * 详情
	 */
	@Override
	public SettlementConfigDTO searchDetail(Long id) throws SQLException {
		SettlementConfigDTO dto =new SettlementConfigDTO();
		dto.setId(id);
		dto = dao.searchDetail(dto);

		SettlementCustomerRelModel relModle=new SettlementCustomerRelModel();
		relModle.setSettlementConfigId(id);
		List<SettlementCustomerRelModel> relList = settlementCustomerRelDao.list(relModle);
		String customerIdsStr ="";
		for (SettlementCustomerRelModel customerRel : relList) {
			if (StringUtils.isBlank(customerIdsStr)) {
				customerIdsStr=customerIdsStr+customerRel.getCustomerId();
			}else {
				customerIdsStr=customerIdsStr+","+customerRel.getCustomerId();
			}

		}
		SettlementModuleRelModel moduleModel=new SettlementModuleRelModel();
		moduleModel.setSettlementConfigId(id);
		List<SettlementModuleRelModel> moduleList=settlementModuleRelDao.list(moduleModel);
		String moduleTypeStr ="";
		for(SettlementModuleRelModel module:moduleList){
			if (StringUtils.isBlank(moduleTypeStr)) {
				moduleTypeStr=moduleTypeStr+module.getModuleType();
			}else {
				moduleTypeStr=moduleTypeStr+","+module.getModuleType();
			}
		}
		if(StringUtils.isNotBlank(dto.getType())){
			if(dto.getType().equals(DERP_ORDER.SETTLEMENTCONFIG_TYPE3)){
				dto.setType(DERP_ORDER.SETTLEMENTCONFIG_TYPE1+","+DERP_ORDER.SETTLEMENTCONFIG_TYPE2);
			}else{
				dto.setType(dto.getType()+",");
			}
		}
		dto.setModuleType(moduleTypeStr);
		dto.setCustomerIdsStr(customerIdsStr);
		return dto ;
	}

	/**
	 * 新增
	 */
	@Override
	public Map<String, Object> saveSettlement(String type,String module,SettlementConfigModel model,JSONArray customerIdArr) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");

		if(StringUtils.isNotBlank(type)){
			//费项适应类型
			String[] strType= type.split(",");
			for(String val:strType){
				model.setType(val);
			}
			if(strType.length==2){
				model.setType(DERP_ORDER.SETTLEMENTCONFIG_TYPE3);
			}
		}
		SettlementConfigModel qeury=new SettlementConfigModel();
		qeury.setLevel(model.getLevel());
		qeury.setProjectName(model.getProjectName());
		qeury = dao.searchByModel(qeury);
		if (qeury!=null) {
			retMap.put("code", "01");
			retMap.put("message", "同级下已经存在该费项名称");
			return retMap;
		}
		Long id=null;
		model.setProjectCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_JSXM));
		String[] moduleType= module.split(",");
		try {
			id = dao.save(model);
		} catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", "同级下已经存在该费项名称");
			return retMap;
		}

		if (id!=null) {
			//添加费项适应模块关系表
			for(String moduleVal:moduleType){
				SettlementModuleRelModel settlementModuleRelModel=new SettlementModuleRelModel();
				settlementModuleRelModel.setModuleType(moduleVal);
				settlementModuleRelModel.setSettlementConfigId(id);
				settlementModuleRelModel.setCreateDate(TimeUtils.getNow());
				settlementModuleRelDao.save(settlementModuleRelModel);
			}
			if (customerIdArr!=null) {
				for (Object object : customerIdArr) {
					Long customerId = Long.valueOf(object.toString());
					SettlementCustomerRelModel relModel=new SettlementCustomerRelModel();
					Map<String, Object>paramsMap=new HashMap<>();
					paramsMap.put("customerid", customerId);
					CustomerInfoMogo customerInfoMongo = customerInfoMongoDao.findOne(paramsMap);
					if (customerInfoMongo==null) customerInfoMongo=new CustomerInfoMogo();
					relModel.setSettlementConfigId(id);
					relModel.setCustomerId(customerId);
					relModel.setCustomerName(customerInfoMongo.getName());
					relModel.setCreateDate(TimeUtils.getNow());
					Long save = settlementCustomerRelDao.save(relModel);
				}
			}

			return retMap;
		}
		return retMap;
	}



	/**
	 * 修改
	 */
	@Override
	public Map<String, Object>  modifySettlement(String type,String module,SettlementConfigModel model,JSONArray customerIdArr,String oldProjectName) throws Exception {
		Map<String, Object> retMap=new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "添加成功");

		//费项适应类型
		if(StringUtils.isNotBlank(type)){
			//费项适应类型
			String[] strType= type.split(",");
			for(String val:strType){
				model.setType(val);
			}
			if(strType.length==2){
				model.setType(DERP_ORDER.SETTLEMENTCONFIG_TYPE3);
			}
		}

		SettlementConfigModel qeury=null;
		if (!model.getProjectName().equals(oldProjectName)) {
			qeury= new SettlementConfigModel();
			qeury.setLevel(model.getLevel());
			qeury.setProjectName(model.getProjectName());
			qeury = dao.searchByModel(qeury);
		}

		if (qeury!=null) {
			retMap.put("code", "01");
			retMap.put("message", "同级下已经存在该费项名称");
			return retMap;
		}
		try {
			dao.update(model);
		} catch (Exception e) {
			retMap.put("code", "01");
			retMap.put("message", "同级下已经存在该费项名称");
			return retMap;
		}

		// 删除客户费用中间表
		SettlementCustomerRelModel relQuery=new SettlementCustomerRelModel();
		relQuery.setSettlementConfigId(model.getId());
		List<SettlementCustomerRelModel> relList = settlementCustomerRelDao.list(relQuery);
		List ids=new ArrayList<>();
		for (SettlementCustomerRelModel settlementCustomerRelModel : relList) {
			ids.add(settlementCustomerRelModel.getId());
		}
		if (ids.size()>0) {
			settlementCustomerRelDao.delete(ids);
		}

		//删除费项适应模型中间表
		SettlementModuleRelModel moduleRelModel=new SettlementModuleRelModel();
		moduleRelModel.setSettlementConfigId(model.getId());
		List<SettlementModuleRelModel> moduleList=settlementModuleRelDao.list(moduleRelModel);
		List idsModule=new ArrayList<>();
		for (SettlementModuleRelModel settlementModuleRelModel : moduleList) {
			idsModule.add(settlementModuleRelModel.getId());
		}
		if (idsModule.size()>0) {
			settlementModuleRelDao.delete(idsModule);
		}
		//添加费项适应模块关系表
		String[] moduleType= module.split(",");
		for(String moduleVal:moduleType){
			SettlementModuleRelModel settlementModuleRelModel=new SettlementModuleRelModel();
			settlementModuleRelModel.setModuleType(moduleVal);
			settlementModuleRelModel.setSettlementConfigId(model.getId());
			settlementModuleRelModel.setCreateDate(TimeUtils.getNow());
			settlementModuleRelDao.save(settlementModuleRelModel);
		}
		// 新增客户费用中间表
		if (customerIdArr!=null) {
			for (Object object : customerIdArr) {
				Long customerId = Long.valueOf(object.toString());
				SettlementCustomerRelModel relModel=new SettlementCustomerRelModel();
				Map<String, Object>paramsMap=new HashMap<>();
				paramsMap.put("customerid", customerId);
				CustomerInfoMogo customerInfoMongo = customerInfoMongoDao.findOne(paramsMap);
				if (customerInfoMongo==null) customerInfoMongo=new CustomerInfoMogo();
				relModel.setSettlementConfigId(model.getId());
				relModel.setCustomerId(customerId);
				relModel.setCustomerName(customerInfoMongo.getName());
				relModel.setCreateDate(TimeUtils.getNow());
				Long save = settlementCustomerRelDao.save(relModel);
			}
			return retMap;
		}

		return retMap;
	}

	/**
	 * 获取父类下拉框
	 */
	@Override
	public List<SettlementConfigModel> getParentProjectNameList(SettlementConfigModel model) throws Exception {
		return dao.list(model);
	}

	@Override
	public SettlementConfigDTO getSettlementListByModuleTypePage(SettlementConfigDTO dto) throws SQLException {
		List<String> typeList = new ArrayList<>();
		if (StringUtils.isNotBlank(dto.getType())) {
			typeList.add(dto.getType());
			typeList.add(DERP_ORDER.SETTLEMENTCONFIG_TYPE3);
			dto.setTypes(typeList);
		}
		return dao.getSettlementListByModuleTypePage(dto);
	}

	@Override
	public List<SelectBean> getSelectBean(SettlementConfigDTO dto) throws SQLException {
		List<String> typeList = new ArrayList<>();
		if (StringUtils.isNotBlank(dto.getType())) {
			typeList.add(dto.getType());
			typeList.add(DERP_ORDER.SETTLEMENTCONFIG_TYPE3);
			dto.setTypes(typeList);
		}
		return dao.getSelectBean(dto);
	}

	@Override
	public List<SettlementConfigDTO> exportSettlementList(SettlementConfigDTO dto) throws SQLException {
		List<SettlementConfigDTO> list = dao.exportSettlementList(dto);
		for (SettlementConfigDTO model : list) {
			SettlementCustomerRelModel settlementCustomerRel=new SettlementCustomerRelModel();
			settlementCustomerRel.setSettlementConfigId(model.getId());
			List<SettlementCustomerRelModel> customerRelList = settlementCustomerRelDao.list(settlementCustomerRel);

			String customerNames="";
			for (SettlementCustomerRelModel relModel : customerRelList) {
				if (StringUtils.isBlank(customerNames)) {
					customerNames=relModel.getCustomerName();
				}else {
					customerNames=customerNames+","+relModel.getCustomerName();
				}

			}
			model.setCustomerNames(customerNames);
		}
		return list;
	}

	@Override
	public boolean isOrNotEnable(SettlementConfigModel model) throws SQLException {
		int num = dao.modify(model);
		if (num>0) {
			return true;
		}
		return false;
	}

	@Override
	public List<SettlementConfigModel> getByModelList(SettlementConfigModel model) throws SQLException {
		return dao.list(model);
	}


}
