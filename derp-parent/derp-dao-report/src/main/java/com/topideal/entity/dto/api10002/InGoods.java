package com.topideal.entity.dto.api10002;

import java.util.List;

public class InGoods {
   
	private String goodsNo;//商品货号
	private String goodsName;//商品名称
	private String brankName;//品牌名称	
	private String commonBarcode;//标准条码
	private String price;//单价
	private Integer totalNum;//入库数量	
	private String unit;//理货单位
	private String remark;//备注
	
	private List<InDetail> detailList;//批次明细

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getBrankName() {
		return brankName;
	}

	public void setBrankName(String brankName) {
		this.brankName = brankName;
	}

	public String getCommonBarcode() {
		return commonBarcode;
	}

	public void setCommonBarcode(String commonBarcode) {
		this.commonBarcode = commonBarcode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<InDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<InDetail> detailList) {
		this.detailList = detailList;
	}
    
}
