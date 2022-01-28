package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * MQ日志流水
 * 
 * @author zhanghx
 */
public class LogStreamMqDTO extends PageModel implements Serializable {

	// 创建时间
	private Timestamp createTime;
	// 模块编码
	private String modelCode;
	// 模块描述
	private String model;
	// 关联日志id（mongoDB）
	private String logId;
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
	// 类型（用于冻结/释放库存）0-冻结 1-释放冻结
	private String type;
	// 耗时
	private Double differenceTime;
	// 属性说明
	private String isClose;

	// 拓展字段
	// 消费时间段开头
	private String consumeStartDate;
	// 消费时间段结束
	private String consumeEndDate;
	// 耗时选项
	private String difference;
	// 是否查看api日志 1-是 0-否
	private String isLoadApi;
	// 时间
	private String month;
	
	private String endDateStr;
	
	private String selectScope;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getIsLoadApi() {
		return isLoadApi;
	}

	public void setIsLoadApi(String isLoadApi) {
		this.isLoadApi = isLoadApi;
	}

	/* isClose get 方法 */
	public String getIsClose() {
		return isClose;
	}

	/* isClose set 方法 */
	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
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
	}

	/* model get 方法 */
	public String getModel() {
		return model;
	}

	/* model set 方法 */
	public void setModel(String model) {
		this.model = model;
	}

	/* logId get 方法 */
	public String getLogId() {
		return logId;
	}

	/* logId set 方法 */
	public void setLogId(String logId) {
		this.logId = logId;
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
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getSelectScope() {
		return selectScope;
	}

	public void setSelectScope(String selectScope) {
		this.selectScope = selectScope;
	}

	
}
