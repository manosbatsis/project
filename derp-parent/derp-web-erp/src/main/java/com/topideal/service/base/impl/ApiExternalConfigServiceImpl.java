package com.topideal.service.base.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.dao.base.ApiExternalConfigDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;
import com.topideal.entity.vo.base.ApiExternalConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.base.ApiExternalConfigService;

/**
 * 对外接口配置 
 * @author lian_
 */
@Service
public class ApiExternalConfigServiceImpl implements ApiExternalConfigService {

	@Autowired
	private ApiExternalConfigDao dao;
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	
	/**
	 * 列表
	 */
	@Override
	public ApiExternalConfigDTO getListByPage(ApiExternalConfigDTO dto) throws SQLException {
		return dao.getListByPage(dto);
	}

	/**
	 * 新增
	 */
	@Override
	public boolean saveApiExternal(ApiExternalConfigModel model) throws Exception {
		ApiExternalConfigModel apiModel = new ApiExternalConfigModel();
		apiModel.setMerchantId(model.getMerchantId());
		apiModel.setPlatformName(model.getPlatformName());
		ApiExternalConfigModel apiModel1 = new ApiExternalConfigModel();
		apiModel1.setAppKey(model.getAppKey());
		ApiExternalConfigModel apiModel2 = new ApiExternalConfigModel();
		apiModel2.setAppSecret(model.getAppSecret());
		apiModel1 = dao.searchByModel(apiModel1);
		apiModel2 = dao.searchByModel(apiModel2);
		apiModel = dao.searchByModel(apiModel);
		if(apiModel != null){
			throw new RuntimeException("该外部对象商家的接口配置已存在");
		}
		if(apiModel1 != null){
			throw new RuntimeException("该app_key系统已存在");
		}
		if(apiModel2 != null){
			throw new RuntimeException("该秘钥系统已存在");
		}
		
		// 根据商家id获取 商家名称 和卓志编码
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(model.getMerchantId());
		model.setMerchantName(merchantInfoModel.getName());
		model.setTopidealCode(merchantInfoModel.getTopidealCode());
		model.setStatus(DERP_SYS.APIEXTERNALCONFIG_STATUS_1);//启用
		
		Long id= dao.save(model);
		if(id>0){
			return true;
		}
		return false;
	}

	/**
	 * 删除
	 */
	@Override
	public boolean delApiExternal(List<Long> list) throws Exception {
		int flag=0;
		for (Long id : list) {
			// 根据id获取信息
			ApiExternalConfigModel apiModel = new ApiExternalConfigModel();
			apiModel.setId(id);
			apiModel = dao.searchByModel(apiModel);
			// 判断状态是否为 已启用
			if (DERP_SYS.APIEXTERNALCONFIG_STATUS_1.equals(apiModel.getStatus())) {
				flag = 1;
				break;
			}
		}
		if(flag == 1){
			throw new RuntimeException("操作失败，只能删除禁用状态！");
		}
		int num = dao.delete(list);
	    if(num >= 1){
	        return true;
	    }
	    return false;
	}

	/**
	 * 编辑
	 */
	@Override
	public boolean modifyApiExternal(ApiExternalConfigModel model) throws Exception {
		if(null != model.getPlatformName() && !"".equals(model.getPlatformName()) || 
				null != model.getMerchantId() && !"".equals(model.getMerchantId())){
			ApiExternalConfigModel apiModel =dao.searchById(model.getId());
			ApiExternalConfigModel apiModel2 = new ApiExternalConfigModel();
			apiModel2.setPlatformName(model.getPlatformName());
			apiModel2.setMerchantId(model.getMerchantId());
			apiModel2 = dao.searchByModel(apiModel2);
			if(apiModel2 != null && !model.getPlatformName().equals(apiModel.getPlatformName()) 
					&& !model.getMerchantId().equals(apiModel.getMerchantId())){
				throw new RuntimeException("该外部对象商家的接口配置已存在");
			}
		}
		
		if( null != model.getAppKey() && !"".equals(model.getAppKey())){
			ApiExternalConfigModel apiModel =dao.searchById(model.getId());
			ApiExternalConfigModel apiModel2 = new ApiExternalConfigModel();
			apiModel2.setAppKey(model.getAppKey());
			apiModel2 = dao.searchByModel(apiModel2);
			if(apiModel2 != null && !model.getAppKey().equals(apiModel.getAppKey())){
				throw new RuntimeException("该app_key系统已存在");
			}
		}
		if( null != model.getAppSecret() && !"".equals(model.getAppSecret())){
			ApiExternalConfigModel apiModel =dao.searchById(model.getId());
			ApiExternalConfigModel apiModel2 = new ApiExternalConfigModel();
			apiModel2.setAppSecret(model.getAppSecret());
			apiModel2 = dao.searchByModel(apiModel2);
			if(apiModel2 != null && !model.getAppSecret().equals(apiModel.getAppSecret())){
				throw new RuntimeException("该秘钥系统已存在");
			}
		}
		
		
		ApiExternalConfigModel model1 =new ApiExternalConfigModel();
		model1.setMerchantId(model.getMerchantId());
		// 根据 商家id 获取 商家名称 和卓志编码
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(model.getMerchantId());
		model.setMerchantName(merchantInfoModel.getName());
		model.setTopidealCode(merchantInfoModel.getTopidealCode());
		int num = dao.modify(model);
		if (num >= 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 详情
	 */
	@Override
	public ApiExternalConfigDTO getDetails(Long id) throws SQLException {
		return dao.searchDTOById(id);
	}

	/**
	 * 禁用启用
	 */
	@Override
	public boolean auditApiExternal(ApiExternalConfigModel model) throws SQLException {
		int num = dao.modify(model);
		if (num >= 1) {
			return true;
		}
		return false;
	}
	
}
