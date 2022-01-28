package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;
import com.topideal.entity.vo.main.CustomerInfoModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问新增页面响应
 * @author 杨创
 */
@ApiModel
public class CustomerToAddPageResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "用户")
	private User user;
	@ApiModelProperty(value = "商家下拉数据")
	private List<SelectBean>  merchantList;
	@ApiModelProperty(value = "客户")
	private CustomerInfoModel detail;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<SelectBean> getMerchantList() {
		return merchantList;
	}
	public void setMerchantList(List<SelectBean> merchantList) {
		this.merchantList = merchantList;
	}
	public CustomerInfoModel getDetail() {
		return detail;
	}
	public void setDetail(CustomerInfoModel detail) {
		this.detail = detail;
	}

	
	
	




}
