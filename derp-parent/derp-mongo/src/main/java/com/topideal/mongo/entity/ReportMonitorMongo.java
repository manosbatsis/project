package com.topideal.mongo.entity;

/**
 * 报表日志
 * @author zhanghx
 */
public class ReportMonitorMongo {

	// 编码
	private Long point;
	// 状态(1-成功，0-失败)
	private Integer state;
	// 描述
	private String desc;
	// 调用时间
	private long startDate;
	// 结束时间
	private long endDate;
	// 异常原因
	private String expMsg;

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public String getExpMsg() {
		return expMsg;
	}

	public void setExpMsg(String expMsg) {
		this.expMsg = expMsg;
	}

}
