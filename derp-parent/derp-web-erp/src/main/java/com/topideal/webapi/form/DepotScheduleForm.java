package com.topideal.webapi.form;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 仓库附表
 * @author lian_
 *
 */
public class DepotScheduleForm  implements Serializable{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "id")
    private Long id;
	@ApiModelProperty(value = "仓库id")
    private Long depotId;
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
	@ApiModelProperty(value = "详细地址")
    private String address;
	
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
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}


  

}
