package com.topideal.order.webapi.filetemp.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class CustomsFileConfigForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token ;

	@ApiModelProperty("关联模板名称")
    private String fileTempName;
    
	@ApiModelProperty("进出仓配置 0-进仓，1-出仓")
    private String depotConfig;
   
    @ApiModelProperty("状态 1-启用 0-禁用")
    private String status;

    @ApiModelProperty("仓库id集合,用于检索多选")
    private String depotIds;
    
    @ApiModelProperty("是否同关区 0-否，1-是")
     private String isSameArea;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFileTempName() {
		return fileTempName;
	}

	public void setFileTempName(String fileTempName) {
		this.fileTempName = fileTempName;
	}

	public String getDepotConfig() {
		return depotConfig;
	}

	public void setDepotConfig(String depotConfig) {
		this.depotConfig = depotConfig;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDepotIds() {
		return depotIds;
	}

	public void setDepotIds(String depotIds) {
		this.depotIds = depotIds;
	}

	public String getIsSameArea() {
		return isSameArea;
	}

	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
	}

}
