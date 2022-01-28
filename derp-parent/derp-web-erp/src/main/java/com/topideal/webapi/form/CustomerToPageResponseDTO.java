package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问列表页面响应
 * @author 杨创
 */
@ApiModel
public class CustomerToPageResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "商家下拉框")
	private List<SelectBean>  merchantList;
	@ApiModelProperty(value = "商家id")
	private Long merchantId;
	public List<SelectBean> getMerchantList() {
		return merchantList;
	}
	public void setMerchantList(List<SelectBean> merchantList) {
		this.merchantList = merchantList;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	
	
	




}
