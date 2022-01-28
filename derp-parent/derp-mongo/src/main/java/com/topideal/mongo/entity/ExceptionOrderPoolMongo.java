package com.topideal.mongo.entity;

/**
 * 智能日志运维——异常单号池mongo
 * @author gy
 *
 */
public class ExceptionOrderPoolMongo {
	
	String id ;
	// 接口编码
	String point ;
	// 模块编码
	String modelCode ;
	// 关键字
	String keyword ;
	// 重推次数
	Integer rePushTimes ;
	// 创建时间
	String createDate ;
	// 类型
	String type ;
	// 异常类型
	String expType ;
	
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getRePushTimes() {
		return rePushTimes;
	}
	public void setRePushTimes(Integer rePushTimes) {
		this.rePushTimes = rePushTimes;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExpType() {
		return expType;
	}
	public void setExpType(String expType) {
		this.expType = expType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
