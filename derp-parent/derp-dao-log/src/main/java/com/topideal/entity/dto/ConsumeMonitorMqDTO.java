package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.system.ibatis.PageModel;

/**
 * MQ消费监控
 * 
 * @author zhanghx
 */
public class ConsumeMonitorMqDTO extends PageModel implements Serializable {

	// 创建时间
	private Timestamp createTime;
	// 模块编码
	private String modelCode;
	private String modelCodeLabel ;
	// 关闭时间
	private Timestamp closeTime;
	// 模块描述
	private String model;
	private String modelLabel;
	// id
	private Long id;
	// 消费时间
	private Timestamp consumeDate;
	// 关键字
	private String keyword;
	// 埋点
	private String point;
	// 状态（1-成功，0-失败）
	private String status;
	private String statusLabel ; 
	// 关联日志id（mongoDB）
	private String logId;
	// 类型（用于冻结/释放库存）0-冻结 1-释放冻结
	private String type;
	private String typeLabel;
	// 耗时
	private Double differenceTime;
	// 失败原因
	private String expMsg;
	 //是否已重推（0：否   1：是）
    private String isResend;
    //重推时间
    private Timestamp resendDate;
    //异常类型
    private String errorType ;
    //异常类型文字
    private String errorTypeLabel ;

	// 拓展字段
	// 消费时间段开头
	private String consumeStartDate;
	// 消费时间段结束
	private String consumeEndDate;
	// 耗时选项
	private String difference;
	// 创建时间段开头
	private String createStartDate;
	// 创建时间段结束
	private String createEndDate;
	// 时间
	private String month;

	private String endDateStr;
	
	private String createDateStr;
	
	private String selectScope ;

	private String merchantName ;
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getIsResend() {
		return isResend;
	}

	public void setIsResend(String isResend) {
		this.isResend = isResend;
	}

	public Timestamp getResendDate() {
		return resendDate;
	}

	public void setResendDate(Timestamp resendDate) {
		this.resendDate = resendDate;
	}

	public String getCreateStartDate() {
		return createStartDate;
	}

	public void setCreateStartDate(String createStartDate) {
		this.createStartDate = createStartDate;
	}

	public String getCreateEndDate() {
		return createEndDate;
	}

	public void setCreateEndDate(String createEndDate) {
		this.createEndDate = createEndDate;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	/* expMsg get 方法 */
	public String getExpMsg() {
		return expMsg;
	}

	/* expMsg set 方法 */
	public void setExpMsg(String expMsg) {
		this.expMsg = expMsg;
	}

	/* differenceTime get 方法 */
	public Double getDifferenceTime() {
		return differenceTime;
	}

	/* differenceTime set 方法 */
	public void setDifferenceTime(Double differenceTime) {
		this.differenceTime = differenceTime;
	}

	/* type get 方法 */
	public String getType() {
		return type;
	}

	/* type set 方法 */
	public void setType(String type) {
		this.type = type;
		this.typeLabel = DERP_LOG.getLabelByKey(DERP_LOG.log_autologTypeList, type) ;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getConsumeStartDate() {
		return consumeStartDate;
	}

	public void setConsumeStartDate(String consumeStartDate) {
		this.consumeStartDate = consumeStartDate;
	}

	public String getConsumeEndDate() {
		return consumeEndDate;
	}

	public void setConsumeEndDate(String consumeEndDate) {
		this.consumeEndDate = consumeEndDate;
	}

	/* createTime get 方法 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/* createTime set 方法 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/* modelCode get 方法 */
	public String getModelCode() {
		return modelCode;
	}

	/* modelCode set 方法 */
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
		this.modelCodeLabel = DERP_LOG.getLabelByKey(DERP_LOG.log_modelCodeList, modelCode) ;
	}

	/* closeTime get 方法 */
	public Timestamp getCloseTime() {
		return closeTime;
	}

	/* closeTime set 方法 */
	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
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

	/* consumeDate get 方法 */
	public Timestamp getConsumeDate() {
		return consumeDate;
	}

	/* consumeDate set 方法 */
	public void setConsumeDate(Timestamp consumeDate) {
		this.consumeDate = consumeDate;
	}

	/* keyword get 方法 */
	public String getKeyword() {
		return keyword;
	}

	/* keyword set 方法 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/* point get 方法 */
	public String getPoint() {
		return point;
	}

	/* point set 方法 */
	public void setPoint(String point) {
		this.point = point;
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
		this.statusLabel = DERP_LOG.getLabelByKey(DERP_LOG.log_statusList, status) ;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getSelectScope() {
		return selectScope;
	}

	public void setSelectScope(String selectScope) {
		this.selectScope = selectScope;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
		
		if(DERP_LOG.LOG_MODELCODE_4.equals(this.modelCode)) {
			this.errorTypeLabel = DERP_LOG.getLabelByKey(DERP_LOG.logInvertoryErrorTypeList, errorType) ;
		}else if("5".equals(this.modelCode)) {
			this.errorTypeLabel = DERP_LOG.getLabelByKey(DERP_LOG.logApiErrorTypeList, errorType) ;
		}else {
			this.errorTypeLabel = DERP_LOG.getLabelByKey(DERP_LOG.logOrderErrorTypeList, errorType) ;
		}
		
	}

	public String getErrorTypeLabel() {
		return errorTypeLabel;
	}

	public void setErrorTypeLabel(String errorTypeLabel) {
		this.errorTypeLabel = errorTypeLabel;
	}

	public String getModelCodeLabel() {
		return modelCodeLabel;
	}

	public void setModelCodeLabel(String modelCodeLabel) {
		this.modelCodeLabel = modelCodeLabel;
	}

	public String getModelLabel() {
		return modelLabel;
	}

	public void setModelLabel(String modelLabel) {
		this.modelLabel = modelLabel;
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

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}
