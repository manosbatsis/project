package com.topideal.inventory.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class BInitInventorySaveResponseDTO {
	@ApiModelProperty(value = "期初新增成功数量")
	private Integer initSuccess ;
	@ApiModelProperty(value = "期初新增失败数量")
	private Integer initFailure ;
	@ApiModelProperty(value = "库存新增成功数量")
	private Integer innventorySuccess ;
	@ApiModelProperty(value = "库存新增失败数量")
	private Integer innventoryFailure ;
	@ApiModelProperty(value = "库存修改成功数量")
	private Integer updteSuccess ;
	@ApiModelProperty(value = "库存修改失败数量")
	private Integer updteFailure ;
	public Integer getInitSuccess() {
		return initSuccess;
	}
	public void setInitSuccess(Integer initSuccess) {
		this.initSuccess = initSuccess;
	}
	public Integer getInitFailure() {
		return initFailure;
	}
	public void setInitFailure(Integer initFailure) {
		this.initFailure = initFailure;
	}
	public Integer getInnventorySuccess() {
		return innventorySuccess;
	}
	public void setInnventorySuccess(Integer innventorySuccess) {
		this.innventorySuccess = innventorySuccess;
	}
	public Integer getInnventoryFailure() {
		return innventoryFailure;
	}
	public void setInnventoryFailure(Integer innventoryFailure) {
		this.innventoryFailure = innventoryFailure;
	}
	public Integer getUpdteSuccess() {
		return updteSuccess;
	}
	public void setUpdteSuccess(Integer updteSuccess) {
		this.updteSuccess = updteSuccess;
	}
	public Integer getUpdteFailure() {
		return updteFailure;
	}
	public void setUpdteFailure(Integer updteFailure) {
		this.updteFailure = updteFailure;
	}


	
}
