package com.topideal.entity.dto.main;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.main.GroupCommbarcodeModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class GroupCommbarcodeDTO extends PageModel implements Serializable{


	@ApiModelProperty(value = "标准条码")
	private String commbarcode ;
	@ApiModelProperty(value = "标准品牌Id")
    private Long id;
	@ApiModelProperty(value = "组码")
    private String groupCommbarcode;
	@ApiModelProperty(value = "组品名")
    private String groupName;
	@ApiModelProperty(value = "创建时间   默认服务器时间")
    private Timestamp createDate;
	@ApiModelProperty(value = "修改日期   默认服务器时间")
    private Timestamp modifyDate;
	
	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupCommbarcode() {
		return groupCommbarcode;
	}

	public void setGroupCommbarcode(String groupCommbarcode) {
		this.groupCommbarcode = groupCommbarcode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	
	
}
