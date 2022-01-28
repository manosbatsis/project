package com.topideal.report.webapi.form;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AutomaticCheckTaskSaveCheckForm extends PageForm implements Serializable{

	@ApiModelProperty(value = "token", required = true)
    private String token;
    
	@ApiModelProperty(value = "电商平台编码：100011111-第e仓 100044998-京东 1000000310-天猫 1000004790-拼多多 1000005237-有赞 1000004941-斑马 1000000390-考拉 1000002612-小红书 1000000687-澳新 1000004058-小小包")
    private String storePlatformCode;
	@ApiModelProperty(value = "店铺编码")
    private String shopCode;
	@ApiModelProperty(value = "出仓仓库ID")
    private Long outDepotId;
	@ApiModelProperty(value = "核对开始时间")
    private String checkStartDate;
	@ApiModelProperty(value = "核对结束时间")
    private String checkEndDate;
	@ApiModelProperty(value = "任务类型  1:POP流水核对 2:仓库流水核对")
    private String taskType;
	@ApiModelProperty(value = "数据源 1：GSS报表 2：菜鸟后台")
    private String dataSource;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getStorePlatformCode() {
		return storePlatformCode;
	}
	public void setStorePlatformCode(String storePlatformCode) {
		this.storePlatformCode = storePlatformCode;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getCheckStartDate() {
		return checkStartDate;
	}
	public void setCheckStartDate(String checkStartDate) {
		this.checkStartDate = checkStartDate;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getCheckEndDate() {
		return checkEndDate;
	}
	public void setCheckEndDate(String checkEndDate) {
		this.checkEndDate = checkEndDate;
	}
	public Long getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	
	


    
}
