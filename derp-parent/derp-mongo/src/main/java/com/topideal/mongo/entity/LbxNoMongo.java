package com.topideal.mongo.entity;

/**
 * 
 * @author lianchenxing
 */
public class LbxNoMongo {

	//
	private String lbxNo;//LBXNo 
	//
	private String type;// XSO-销售 , CGO-采购 , DBO-调拨, XSTO-销售退货

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
