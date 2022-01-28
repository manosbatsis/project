package com.topideal.webapi.form;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
/**
 * 权限信息表
 */
public class PermissionInfoAddForm  implements Serializable{

	 @ApiModelProperty(value = "令牌",required = true)
	 private String token;
	 @ApiModelProperty(value = "权限名称",required = true)
     private String authorityName;
	 @ApiModelProperty(value = "父级权限ID",required = true)
     private Long parentId;		 
	 @ApiModelProperty(value = "权限类型  1 菜单  2  按钮",required = true)
     private Integer type;
	 @ApiModelProperty(value = "权限地址")
     private String url;		 
	 @ApiModelProperty(value = "服务器地址")
     private String serverAddr;
	 @ApiModelProperty(value = "模块 1-主服务-main 2-业务-order 3-仓储-storage 4-库存-inventory 5-报表-report",required = true)
	 private String module;
	 @ApiModelProperty(value = "授权码")
     private String permission;
	 @ApiModelProperty(value = "序号")
     private Integer seq;
	 @ApiModelProperty(value = "备注")
     private String remark;		 
	 @ApiModelProperty(value = "是否启用  0 不启用  1 启用")
     private Integer isEnabled;
	@ApiModelProperty(value = "id")
	private Long id;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAuthorityName() {
		return authorityName;
	}
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getServerAddr() {
		return serverAddr;
	}
	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

