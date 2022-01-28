package com.topideal.report.webapi.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleAmountTargetForm  extends PageForm{

	@ApiModelProperty(value = "令牌")
    private String token;

	@ApiModelProperty(value="事业部ID集合")
    private String buIds;
	
	@ApiModelProperty(value="月份")
    private String month;
	
	@ApiModelProperty(value="单据id集合，多个用逗号隔开")
    private String ids;

	@ApiModelProperty(value="部门id集合")
	private String departmentIds;

	@ApiModelProperty(value="母品牌id集合")
	private String brandSuperiorIds;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBuIds() {
		return buIds;
	}

	public void setBuIds(String buIds) {
		this.buIds = buIds;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(String departmentIds) {
		this.departmentIds = departmentIds;
	}

	public String getBrandSuperiorIds() {
		return brandSuperiorIds;
	}

	public void setBrandSuperiorIds(String brandSuperiorIds) {
		this.brandSuperiorIds = brandSuperiorIds;
	}
}
