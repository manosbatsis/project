package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiSecretConfigDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * api密钥配置访问编辑页面响应
 * @author 杨创
 */
@ApiModel
public class ApiToEditResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "商家下拉框list")
	private List<SelectBean> merchantList;
	@ApiModelProperty(value = "api密钥配置")
	private ApiSecretConfigDTO detail;
	public List<SelectBean> getMerchantList() {
		return merchantList;
	}
	public void setMerchantList(List<SelectBean> merchantList) {
		this.merchantList = merchantList;
	}
	public ApiSecretConfigDTO getDetail() {
		return detail;
	}
	public void setDetail(ApiSecretConfigDTO detail) {
		this.detail = detail;
	}

	



}
