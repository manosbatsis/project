package com.topideal.service.epass;

import com.topideal.entity.vo.base.ApiSecretConfigModel;

/**
 * 根据appkey 获取api配置表信息
 * @author 杨创
 *2018/7/17
 */
public interface ApiSecretConfigService {
	//根据appkey 获取 商家信息表
	public ApiSecretConfigModel getApiSecretConfig(String appKey)throws Exception;
}
