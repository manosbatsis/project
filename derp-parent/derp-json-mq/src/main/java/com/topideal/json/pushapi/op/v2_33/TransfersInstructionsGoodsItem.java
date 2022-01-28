package com.topideal.json.pushapi.op.v2_33;

import java.io.Serializable;


/**
 * 调拨指令推送接口商品实体类
 * @author 杨创
 *2018/4/3
 */
public class TransfersInstructionsGoodsItem implements Serializable {

	// 序号
	private String gnum;
	// 调出商品货号
	private String fromGoodId;
	// 调入商品货号
	private String toGoodId;
	// 调拨数量
	private int qtp;
	// 调出批次
	private String lotNo;

	public String getGnum() {
		return gnum;
	}

	public String getFromGoodId() {
		return fromGoodId;
	}

	public String getToGoodId() {
		return toGoodId;
	}

	public int getQtp() {
		return qtp;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setGnum(String gnum) {
		this.gnum = gnum;
	}

	public void setFromGoodId(String fromGoodId) {
		this.fromGoodId = fromGoodId;
	}

	public void setToGoodId(String toGoodId) {
		this.toGoodId = toGoodId;
	}

	public void setQtp(int qtp) {
		this.qtp = qtp;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

}
