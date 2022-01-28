package com.topideal.mongo.entity;

public class FileTempDataMongo {

	//对应订单号
	private String code ;
	
	//模版编号
	private String fileTempCode ;
	
	private String path ;
	
	//json字符串
	private String dataJson ;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFileTempCode() {
		return fileTempCode;
	}

	public void setFileTempCode(String fileTempCode) {
		this.fileTempCode = fileTempCode;
	}

	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
}
