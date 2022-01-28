package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class ReceivePaymentSubjectForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
	
	@ApiModelProperty(value = "NC收支费项id")
    private String id;
	
	@ApiModelProperty(value = "NC收支费项编码")
    private String code;

	@ApiModelProperty(value = "NC收支费项名称")
    private String name;

	@ApiModelProperty(value = "科目编码")
    private String subCode;

	@ApiModelProperty(value = "科目名称")
    private String subName;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}
	
}
