package com.topideal.entity.dto.main;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

public class NoticeDTO extends PageModel implements Serializable{

	/**
    * id
    */
    private Long id;
    /**
    * 公告标题
    */
    private String title;
    /**
    * 公告内容
    */
    private String contentBody;
    /**
    * 状态
    */
    private String status;
    private String statusLabel;
    /**
    * 类型
    */
    private String type;
    private String typeLabel;
    
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 发布时间
    */
    private Timestamp publishDate;
    
    private Timestamp publishStartDate;
    
    private Timestamp publishEndDate;
    
    private String publishStartDateStr;
    
    private String publishEndDateStr;
    
    private String readStatus ;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.notice_statuslist, status);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.typeLabel = DERP_SYS.getLabelByKey(DERP_SYS.notice_typelist, type) ;
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

	public Timestamp getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Timestamp publishDate) {
		this.publishDate = publishDate;
	}

	public Timestamp getPublishStartDate() {
		return publishStartDate;
	}

	public void setPublishStartDate(Timestamp publishStartDate) {
		this.publishStartDate = publishStartDate;
	}

	public Timestamp getPublishEndDate() {
		return publishEndDate;
	}

	public void setPublishEndDate(Timestamp publishEndDate) {
		this.publishEndDate = publishEndDate;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}

	public String getPublishStartDateStr() {
		return publishStartDateStr;
	}

	public void setPublishStartDateStr(String publishStartDateStr) {
		this.publishStartDateStr = publishStartDateStr;
	}

	public String getPublishEndDateStr() {
		return publishEndDateStr;
	}

	public void setPublishEndDateStr(String publishEndDateStr) {
		this.publishEndDateStr = publishEndDateStr;
	}
    
    
}
