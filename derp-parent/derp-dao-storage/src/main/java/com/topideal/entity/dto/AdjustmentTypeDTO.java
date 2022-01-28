package com.topideal.entity.dto;

import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.AdjustmentTypeItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 类型调整DTO
 */
@ApiModel
public class AdjustmentTypeDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "仓库名称")
	private String depotName;

	@ApiModelProperty(value = "类型调整单号")
	private String code;

	@ApiModelProperty(value = "令牌 必填")
	private String adjustmentRemark;

	@ApiModelProperty(value = "仓库id")
	private Long depotId;
	// 商家名称
	@ApiModelProperty(value = "公司名称")
	private String merchantName;

	@ApiModelProperty(value = "来源单据号")
	private String sourceCode;

	@ApiModelProperty(value = "公司id")
	private Long merchantId;

	@ApiModelProperty(value = "类型调整名称")
	private String adjustmentTypeName;
	//
	@ApiModelProperty(value = "单据时间")
	private Timestamp codeTime;
	// 业务类别
	@ApiModelProperty(value = "业务类别")
	private String model;
	@ApiModelProperty(value = "业务类别(中文)")
	private String modelLabel;
	// id
	@ApiModelProperty(value = "id")
	private Long id;
	//
	@ApiModelProperty(value = "调整时间")
	private Timestamp adjustmentTime;

	@ApiModelProperty(value = "归属日期")
	private Timestamp pushTime;
	//
	@ApiModelProperty(value = "状态")
	private String status;
	@ApiModelProperty(value = "状态(中文)")
	private String statusLabel;
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	//事业部名称
	@ApiModelProperty(value = "令牌 必填")
	private String buName;

	@ApiModelProperty(value = "事业部id")
	private Long buId;

	@ApiModelProperty(value = "确认人id")
	private Long confirmUserId;

	@ApiModelProperty(value = "确认人名称")
	private String confirmUsername;

	@ApiModelProperty(value = "确认时间")
	private Timestamp confirmTime;

	//
	@ApiModelProperty(value = "仓库类型")
	private String depotType;
	@ApiModelProperty(value = "仓库编码")
	private String depotCode;

	/**
	 * 单据来源 01:接口回传 02:手工录入
	 */
	@ApiModelProperty(value = "单据来源")
	private String source;
	@ApiModelProperty(value = "单据来源(中文)")
	private String sourceLabel;

	@ApiModelProperty(value = "创建人id")
	private Long createUserId;

	@ApiModelProperty(value = "创建人名称")
	private String createUsername;

	//调整开始时间
	@ApiModelProperty(hidden = true)
	private String adjustmentStartTime;
	//调整结束时间
	@ApiModelProperty(hidden = true)
	private String adjustmentEndTime;

	@ApiModelProperty(value = "表体")
	private List<AdjustmentTypeItemDTO> itemList;

	@ApiModelProperty(value = "单据id集合，多个用逗号隔开")
	private String ids;

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public List<AdjustmentTypeItemDTO> getItemList() {
		return itemList;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void setItemList(List<AdjustmentTypeItemDTO> itemList) {
		this.itemList = itemList;
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
		if (StringUtils.isNotBlank(model)) {
			this.modelLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentType_modelList, model);
		}
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
		if (StringUtils.isNotBlank(status)) {
			this.statusLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentType_statusList, status);
		}
	}

	public String getModelLabel() {
		return modelLabel;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
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

	public String getDepotType() {
		return depotType;
	}

	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public void setModelLabel(String modelLabel) {
		this.modelLabel = modelLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
		if (StringUtils.isNotBlank(source)) {
			this.sourceLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.adjustmentType_sourceList, source);
		}
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

	public String getSourceLabel() {
		return sourceLabel;
	}

	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}

	public String getAdjustmentStartTime() {
		return adjustmentStartTime;
	}

	public void setAdjustmentStartTime(String adjustmentStartTime) {
		this.adjustmentStartTime = adjustmentStartTime;
	}

	public String getAdjustmentEndTime() {
		return adjustmentEndTime;
	}

	public void setAdjustmentEndTime(String adjustmentEndTime) {
		this.adjustmentEndTime = adjustmentEndTime;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
