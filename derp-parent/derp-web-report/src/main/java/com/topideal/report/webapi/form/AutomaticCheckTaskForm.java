package com.topideal.report.webapi.form;
import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AutomaticCheckTaskForm extends PageForm implements Serializable{

	@ApiModelProperty(value = "token", required = true)
    private String token;
    
    /**
    * 任务类型  1:POP流水核对 2:仓库流水核对
    */
	@ApiModelProperty(value = "任务类型  1:POP流水核对 2:仓库流水核对")
    private String taskType;
    /**
    * 创建人名称
    */
	@ApiModelProperty(value = "创建人名称")
    private String createName;
	
	// 核对开始时间
	@ApiModelProperty(value = "核对开始时间")
	private String checkStartDate;
	// 核对结束时间
	@ApiModelProperty(value = "核对结束时间")
	private String checkEndDate; 	
    /**
    * 核对结果 0:未对平 1:已对平
    */
	@ApiModelProperty(value = "核对结果 0:未对平 1:已对平")
    private String checkResult;
    /**
    * 处理状态 1:处理中 2:已完成
    */
	@ApiModelProperty(value = "处理状态 1:处理中 2:已完成")
    private String state;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private String createDate;
    /**
    * 出仓仓库ID
    */
	@ApiModelProperty(value = "出仓仓库ID")
    private Long outDepotId;
    /**
    * 电商平台编码：100011111-第e仓 100044998-京东 1000000310-天猫 1000004790-拼多多 1000005237-有赞 1000004941-斑马 1000000390-考拉 1000002612-小红书 1000000687-澳新 1000004058-小小包
    */
	@ApiModelProperty(value = "电商平台编码：100011111-第e仓 100044998-京东 1000000310-天猫 1000004790-拼多多 1000005237-有赞 1000004941-斑马 1000000390-考拉 1000002612-小红书 1000000687-澳新 1000004058-小小包")
    private String storePlatformCode;
    /**
    * 店铺编码
    */
	@ApiModelProperty(value = "店铺编码")
    private String shopCode;
    
    /**
    * 任务编码(自生成)
    */
	@ApiModelProperty(value = "任务编码(自生成)")
    private String taskCode;

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCheckStartDate() {
		return checkStartDate;
	}

	public void setCheckStartDate(String checkStartDate) {
		this.checkStartDate = checkStartDate;
	}

	public String getCheckEndDate() {
		return checkEndDate;
	}

	public void setCheckEndDate(String checkEndDate) {
		this.checkEndDate = checkEndDate;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
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

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
    
	
	
	

 	


    
}
