package com.topideal.webapi.form;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class NoticeAddForm implements Serializable{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "id新增非必填,修改必填")
    private Long id;
	@ApiModelProperty(value = "公告标题",required = true)
    private String title;
	@ApiModelProperty(value = "公告内容")
    private String contentBody;
	@ApiModelProperty(value = "类型",required = true)
    private String type;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContentBody() {
		return contentBody;
	}
	public void setContentBody(String contentBody) {
		this.contentBody = contentBody;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}









}
