package com.topideal.webapi.form;

import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;

/**
 * 客户信息表
 */
public class CustomerInfoForm extends PageForm  implements Serializable {
    @ApiModelProperty(value = "令牌",required = true)
	private String token;
    @ApiModelProperty(value = "id")
	private Long id;
    @ApiModelProperty(value = "客户编码")
	private String code;
    @ApiModelProperty(value = "客户名称")
	private String name;
    @ApiModelProperty(value = "状态(1使用中,0已禁用)")
    private String status;
    @ApiModelProperty(value = "主数据客户id")
    private String mainId;
    @ApiModelProperty(value = "列表查询关联商家Id")
    private Long merchantId ;
    @ApiModelProperty(value = "来源 1-主数据  2-录入/导入")
    private String source;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
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

	
}
