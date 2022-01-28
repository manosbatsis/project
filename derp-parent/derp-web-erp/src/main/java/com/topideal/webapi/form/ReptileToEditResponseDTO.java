package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiSecretConfigDTO;
import com.topideal.entity.dto.base.ReptileConfigDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 爬虫配置新增页面响应
 * @author 杨创
 */
@ApiModel
public class ReptileToEditResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "商家下list数据")
	private List<MerchantInfoModel> merchantBean;
	@ApiModelProperty(value = "查询客户下拉框")
	private List<SelectBean> customerList;
	@ApiModelProperty(value = "爬虫配置实体")
	private ReptileConfigDTO detail;
	public List<MerchantInfoModel> getMerchantBean() {
		return merchantBean;
	}
	public void setMerchantBean(List<MerchantInfoModel> merchantBean) {
		this.merchantBean = merchantBean;
	}
	public List<SelectBean> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(List<SelectBean> customerList) {
		this.customerList = customerList;
	}
	public ReptileConfigDTO getDetail() {
		return detail;
	}
	public void setDetail(ReptileConfigDTO detail) {
		this.detail = detail;
	}


	



}
