package com.topideal.service.epass.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.base.ApiSecretConfigDao;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.service.epass.ApiSecretConfigService;
/**
 * 根据appkey 获取 api配置表信息
 * @author 杨创
 *2018/7/17
 */
@Service
public class ApiSecretConfigServiceImpl implements ApiSecretConfigService {
	
	@Autowired
	private ApiSecretConfigDao apiSecretConfigDao;// api密钥配置
	// 根据appKey获取api配置表信息
	@Override
	public ApiSecretConfigModel getApiSecretConfig(String appKey) throws Exception {
		ApiSecretConfigModel model = new ApiSecretConfigModel();
		model.setAppKey(appKey);		
		model = apiSecretConfigDao.searchByModel(model);
		return model;
	}

}
