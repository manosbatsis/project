package com.topideal.report.webapi.form;
import java.util.List;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleTargetForm extends PageForm{

	@ApiModelProperty(value = "令牌")
    private String token;
	
	@ApiModelProperty(value = "事业部ID")
    private Long buId;

	@ApiModelProperty(value = "事业部ids集合")
    private List<Long> buIds;

	@ApiModelProperty(value = "月份")
    private String month;

	@ApiModelProperty(value = "类型 1-按销售类型计划 2-按平台计划")
    private String type;

	@ApiModelProperty(value = "年份")
    private String year ;
	
	@ApiModelProperty(value = "id集合，多个用逗号隔开")
	private String ids;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public List<Long> getBuIds() {
		return buIds;
	}

	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
   
}
