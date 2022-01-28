package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiSecretConfigDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 爬虫配置新增页面响应
 * @author 杨创
 */
@ApiModel
public class ReptileToAddResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "商家下list数据")
	private List<MerchantInfoModel> merchantBean;
	@ApiModelProperty(value = "代理商家下拉")
	private List<MerchantInfoModel> isPoxy;
	public List<MerchantInfoModel> getMerchantBean() {
		return merchantBean;
	}
	public void setMerchantBean(List<MerchantInfoModel> merchantBean) {
		this.merchantBean = merchantBean;
	}
	public List<MerchantInfoModel> getIsPoxy() {
		return isPoxy;
	}
	public void setIsPoxy(List<MerchantInfoModel> isPoxy) {
		this.isPoxy = isPoxy;
	} 


	



}
