package com.topideal.webapi.form;
import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;

public class CustomerMainForm extends PageForm implements Serializable{
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "id")
    private Long id;
	@ApiModelProperty(value = "主数据客户ID")
    private String ccode;
	@ApiModelProperty(value = "客商名称")
    private String cname;
	@ApiModelProperty(value = "客商简称")
	private String cshortname;
	@ApiModelProperty(value = "是否供应商 1-是 0-否")
	private String isSupplier;
	@ApiModelProperty(value = "是否客户 1-是 0-否")
	private String isCustomer;
	@ApiModelProperty(value = "主数据客户状态 1：有效   0：锁定*")
	private String mainStatus;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCcode() {
		return ccode;
	}
	public void setCcode(String ccode) {
		this.ccode = ccode;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCshortname() {
		return cshortname;
	}
	public void setCshortname(String cshortname) {
		this.cshortname = cshortname;
	}
	public String getIsSupplier() {
		return isSupplier;
	}
	public void setIsSupplier(String isSupplier) {
		this.isSupplier = isSupplier;
	}
	public String getIsCustomer() {
		return isCustomer;
	}
	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}
	public String getMainStatus() {
		return mainStatus;
	}
	public void setMainStatus(String mainStatus) {
		this.mainStatus = mainStatus;
	}
   

   
}
