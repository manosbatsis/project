package com.topideal.order.webapi.filetemp.form;
import java.sql.Timestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FileTempModelForm {

	@ApiModelProperty(value = "令牌",required = true)
	private String token ;
	/**
    * id
    */
	@ApiModelProperty(value = "模版ID",required = false)
    private Long id;
    /**
    * 模版标题
    */
	@ApiModelProperty(value = "模版标题",required = false)
    private String title;
    /**
    * 模版编码
    */
	@ApiModelProperty(value = "模版编码",required = false)
    private String code;
    /**
    * 模版内容
    */
	@ApiModelProperty(value = "模版内容",required = false)
    private String contentBody;
    /**
    * 公告类型 1-采购合同
    */
	@ApiModelProperty(value = "公告类型",required = false)
    private String type;
    /**
     * 状态 1-启用 0-禁用
     */
	@ApiModelProperty(value = "状态",required = false)
    private String status;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间",required = false)
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间",required = false)
    private Timestamp modifyDate;
	
    //适用类型  1-客户  2-供应商
	@ApiModelProperty(value = "适用类型",required = false)
    private String cusType;
	
    //跳转页面地址
	@ApiModelProperty(value = "跳转页面地址",required = false)
    private String toUrl;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContentBody() {
		return contentBody;
	}

	public void setContentBody(String contentBody) {
		this.contentBody = contentBody;
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

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCusType() {
		return cusType;
	}

	public void setCusType(String cusType) {
		this.cusType = cusType;
	}

	public String getToUrl() {
		return toUrl;
	}

	public void setToUrl(String toUrl) {
		this.toUrl = toUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
   
	
}
