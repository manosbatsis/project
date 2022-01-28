package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问对应接口配置表编辑页面响应
 * @author 杨创
 */
@ApiModel
public class ApiExternalToEditResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "商家下拉框")
	private List<SelectBean> merchantList;
	@ApiModelProperty(value = "对外接口配置表")
	private ApiExternalConfigDTO detail;
	public List<SelectBean> getMerchantList() {
		return merchantList;
	}
	public void setMerchantList(List<SelectBean> merchantList) {
		this.merchantList = merchantList;
	}
	public ApiExternalConfigDTO getDetail() {
		return detail;
	}
	public void setDetail(ApiExternalConfigDTO detail) {
		this.detail = detail;
	}
	
	




}
