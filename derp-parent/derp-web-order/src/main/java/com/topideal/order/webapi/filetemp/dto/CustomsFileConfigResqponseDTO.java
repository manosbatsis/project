package com.topideal.order.webapi.filetemp.dto;

import java.util.List;

import com.topideal.entity.dto.common.CustomsFileConfigDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CustomsFileConfigResqponseDTO {
	@ApiModelProperty("返回编码，00-出仓入仓只有一条模板，不展示，直接导出；01-没有找到关联模板")
	String code;
	
	@ApiModelProperty("出仓模板集合")
	List<CustomsFileConfigDTO> outList;
	
	@ApiModelProperty("入仓模板集合")
	List<CustomsFileConfigDTO> inList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<CustomsFileConfigDTO> getOutList() {
		return outList;
	}

	public void setOutList(List<CustomsFileConfigDTO> outList) {
		this.outList = outList;
	}

	public List<CustomsFileConfigDTO> getInList() {
		return inList;
	}

	public void setInList(List<CustomsFileConfigDTO> inList) {
		this.inList = inList;
	}
	
}
