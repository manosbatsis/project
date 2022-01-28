package com.topideal.webapi.form;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;
/**
 * 用户信息表
 */
public class UserInfoAddForm  implements Serializable{

     @ApiModelProperty(value = "令牌",required = true)
	 private String token;
     //用户编码（平台自编码）
     @ApiModelProperty(value = "令牌")
     private String code;
     @ApiModelProperty(value = "手机号码",required = true)
     private String tel;
     @ApiModelProperty(value = "id 新增非必填,修改必填")
     private Long id;
     //邮箱
     @ApiModelProperty(value = "邮箱",required = true)
     private String email;

     //性别  m 男   f 女
     @ApiModelProperty(value = "性别  m 男   f 女",required = true)
     private String sex;
     @ApiModelProperty(value = "用户姓名",required = true)
     private String name;
     @ApiModelProperty(value = "用户名称",required = true)
     private String username;
     @ApiModelProperty(value = "账号类型 0-后台管理员 1-普通账号",required = true)
     private String accountType;

    //拓展字段
    //绑定公司
    @ApiModelProperty(value = "公司id集合")
    private String merchantIds;
    @ApiModelProperty(value = "事业部id集合")
    private String buIds;
    @ApiModelProperty(value = "id集合")
    private List<String> ids;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getMerchantIds() {
		return merchantIds;
	}
	public void setMerchantIds(String merchantIds) {
		this.merchantIds = merchantIds;
	}
	public String getBuIds() {
		return buIds;
	}
	public void setBuIds(String buIds) {
		this.buIds = buIds;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}


    
}

