package com.topideal.order.webapi.filetemp.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FileTempForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token ;

	@ApiModelProperty(value = "模板id",required = false)
	private Long fileTempId;
    /**
    * 客户ID
    */
	@ApiModelProperty(value = "客户ID，多选以','隔开",required = false)
    private String customerIds;
    /**
    * 模版类型
    */
	@ApiModelProperty(value = "模版类型",required = false)
    private String type;
    /**
    * 状态
    */
	@ApiModelProperty(value = "状态 1-启用 0-禁用",required = false)
    private String status;

	@ApiModelProperty(value="模版标题", required=false)
	private String title;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCustomerIds() {
		return customerIds;
	}
	public void setCustomerIds(String customerIds) {
		this.customerIds = customerIds;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getFileTempId() {
		return fileTempId;
	}

	public void setFileTempId(Long fileTempId) {
		this.fileTempId = fileTempId;
	}
}