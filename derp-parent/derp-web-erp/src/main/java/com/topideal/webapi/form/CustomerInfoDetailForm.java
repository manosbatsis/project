package com.topideal.webapi.form;

import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;

/**
 * 客户信息表
 */
public class CustomerInfoDetailForm extends PageForm  implements Serializable {
    @ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "商家名称")
	private String name;
    @ApiModelProperty(value = "主数据客户id")
    private String mainId;
    @ApiModelProperty(value = "列表查询关联商家Id")
    private Long merchantId ;
    @ApiModelProperty(value = "来源 1-主数据  2-录入/导入")
    private Long id;
    @ApiModelProperty(value = "客户类型 1客户2供应商")
 	private String cusType;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
	
	
}
