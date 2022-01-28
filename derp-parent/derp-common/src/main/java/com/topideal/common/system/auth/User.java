package com.topideal.common.system.auth;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 保存登陆的用户信息
 */
public class User  implements Serializable{
	 @ApiModelProperty(value = "id")
     private Long id;
     //用户编码（平台自编码）
	 @ApiModelProperty(value = "用户编码（平台自编码）")
     private String code;
     //父级权用户ID
	 @ApiModelProperty(value = "父级权用户ID")
     private Long parentId;
     //头像
	 @ApiModelProperty(value = "头像")
     private String logoImg;
     //用户姓名
	 @ApiModelProperty(value = "用户姓名")
     private String name;
     //用户类型  1 平台用户  2 商家（超管理）  3 商家用户
	 @ApiModelProperty(value = "用户类型  1 平台用户  2 商家（超管理）  3 商家用户")
     private String userType;
     //用户名称
	 @ApiModelProperty(value = "用户名称")
     private String username;
     //密码
	 @ApiModelProperty(value = "密码")
     private String password;
    //商家id
	@ApiModelProperty(value = "商家id")
    private Long merchantId;
    //商家名称
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
    //商家编码
	@ApiModelProperty(value = "商家编码")
    private String merchantCode;
    //卓志编码
	@ApiModelProperty(value = "卓志编码")
    private String topidealCode;
    //关联商家
	@ApiModelProperty(value = "关联商家")
    private String relMerchantIds;
    //权限字符
	@ApiModelProperty(value = "权限字符")
    private List<String> permissionList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getTopidealCode() {
        return topidealCode;
    }

    public void setTopidealCode(String topidealCode) {
        this.topidealCode = topidealCode;
    }

    public String getRelMerchantIds() {
        return relMerchantIds;
    }

    public void setRelMerchantIds(String relMerchantIds) {
        this.relMerchantIds = relMerchantIds;
    }

	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}
    
}

