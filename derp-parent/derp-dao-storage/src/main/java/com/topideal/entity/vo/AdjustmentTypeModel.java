package com.topideal.entity.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 类型调整
 * 
 * @author lian_
 *
 */
public class AdjustmentTypeModel extends PageModel implements Serializable {

	// 盘点仓库名称
	private String depotName;
	// 类型调整单号
	private String code;
	// 调整原因
	private String adjustmentRemark;
	// 盘点仓库id
	private Long depotId;
	// 商家名称
	private String merchantName;
	// 来源单据号
	private String sourceCode;
	// 商家id
	private Long merchantId;
	// 类型调整名称
	private String adjustmentTypeName;
	// 单据时间
	private Timestamp codeTime;
	// 业务类别
	private String model;
	// id
	private Long id;
	// 调整时间
	private Timestamp adjustmentTime;
	// 归属日期
	private Timestamp pushTime;
	// 状态
	private String status;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;

	/**
	 * 确认人id
	 */
	private Long confirmUserId;
	/**
	 * 确认人名称
	 */
	private String confirmUsername;
	/**
	 * 确认时间
	 */
	private Timestamp confirmTime;

	/**
	 * 单据来源 01:接口回传 02:手工录入
	 */
	private String source;
	/**
	 * 创建人id
	 */
	private Long createUserId;
	/**
	 * 创建人名称
	 */
	private String createUsername;

	// 表体
//	private List<AdjustmentTypeItemModel> itemList;

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

	/* depotName get 方法 */
	public String getDepotName() {
		return depotName;
	}

	/* depotName set 方法 */
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* adjustmentRemark get 方法 */
	public String getAdjustmentRemark() {
		return adjustmentRemark;
	}

	/* adjustmentRemark set 方法 */
	public void setAdjustmentRemark(String adjustmentRemark) {
		this.adjustmentRemark = adjustmentRemark;
	}

	/* depotId get 方法 */
	public Long getDepotId() {
		return depotId;
	}

	/* depotId set 方法 */
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* sourceCode get 方法 */
	public String getSourceCode() {
		return sourceCode;
	}

	/* sourceCode set 方法 */
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* adjustmentTypeName get 方法 */
	public String getAdjustmentTypeName() {
		return adjustmentTypeName;
	}

	/* adjustmentTypeName set 方法 */
	public void setAdjustmentTypeName(String adjustmentTypeName) {
		this.adjustmentTypeName = adjustmentTypeName;
	}

	/* codeTime get 方法 */
	public Timestamp getCodeTime() {
		return codeTime;
	}

	/* codeTime set 方法 */
	public void setCodeTime(Timestamp codeTime) {
		this.codeTime = codeTime;
	}

	/* model get 方法 */
	public String getModel() {
		return model;
	}

	/* model set 方法 */
	public void setModel(String model) {
		this.model = model;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* adjustmentTime get 方法 */
	public Timestamp getAdjustmentTime() {
		return adjustmentTime;
	}

	/* adjustmentTime set 方法 */
	public void setAdjustmentTime(Timestamp adjustmentTime) {
		this.adjustmentTime = adjustmentTime;
	}

	/* pushTime get 方法 */
	public Timestamp getPushTime() {
		return pushTime;
	}

	/* pushTime set 方法 */
	public void setPushTime(Timestamp pushTime) {
		this.pushTime = pushTime;
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	public Long getConfirmUserId() {
		return confirmUserId;
	}

	public void setConfirmUserId(Long confirmUserId) {
		this.confirmUserId = confirmUserId;
	}

	public String getConfirmUsername() {
		return confirmUsername;
	}

	public void setConfirmUsername(String confirmUsername) {
		this.confirmUsername = confirmUsername;
	}

	public Timestamp getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}
}
