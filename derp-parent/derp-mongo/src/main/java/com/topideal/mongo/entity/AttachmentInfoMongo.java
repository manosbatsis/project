package com.topideal.mongo.entity;

import java.util.Date;

/**
 * 附件mongo 实体
 * @author gy
 *
 */
public class AttachmentInfoMongo{

	/**
	 * 附件编码
	 */
	private String attachmentCode ;
    /**
    * 模块
    */
    private String module;
    /**
    * 关联单号
    */
    private String relationCode;
    /**
    * 附件名
    */
    private String attachmentName;
    /**
    * 附件类型
    */
    private String attachmentType;
    /**
     * 附件地址
     */
     private String attachmentUrl;
    /**
    * 附件大小
    */
    private Long attachmentSize;
    /**
    * 状态
    */
    private String status;
    /**
    * 上传者id
    */
    private Long creator;
    /**
    * 上传者姓名
    */
    private String creatorName;
    
    private Date createDate ;


    /**
     * 商家ID
     */
    private Date delDate;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Long getAttachmentSize() {
		return attachmentSize;
	}

	public void setAttachmentSize(Long attachmentSize) {
		this.attachmentSize = attachmentSize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}



	public Date getDelDate() {
		return delDate;
	}

	public void setDelDate(Date delDate) {
		this.delDate = delDate;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	
	public String getAttachmentCode() {
		return attachmentCode;
	}

	public void setAttachmentCode(String attachmentCode) {
		this.attachmentCode = attachmentCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

    
}
