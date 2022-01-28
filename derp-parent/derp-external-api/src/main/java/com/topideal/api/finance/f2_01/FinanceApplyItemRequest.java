package com.topideal.api.finance.f2_01;

import java.math.BigDecimal;
import java.util.List;

/**
 * 融资申请请求表体
 * @author 杨创
 * 2021-04-09
 */
public class FinanceApplyItemRequest {
	private String goodsNo;//必填  外部商品编码
	private String goodsName;//必填 商品名称
	private Integer num;//必填 数量
	private BigDecimal unitPrice;//必填 单价（采购价）
	private String relationPO;//非必填 关联PO单 
	private String relationGoods;//必填 关联PO单商品 非必填 
	private String pledgeGoods;// 质押商品，可循环，存货质押且质押类型为部分质押时必填，否则非必填必填
	private String pledgegoodsNo;// 外部商品编码 必填
	private String pledgegoodsName;//商品名称  必填
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
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getRelationPO() {
		return relationPO;
	}
	public void setRelationPO(String relationPO) {
		this.relationPO = relationPO;
	}
	public String getRelationGoods() {
		return relationGoods;
	}
	public void setRelationGoods(String relationGoods) {
		this.relationGoods = relationGoods;
	}
	public String getPledgeGoods() {
		return pledgeGoods;
	}
	public void setPledgeGoods(String pledgeGoods) {
		this.pledgeGoods = pledgeGoods;
	}
	public String getPledgegoodsNo() {
		return pledgegoodsNo;
	}
	public void setPledgegoodsNo(String pledgegoodsNo) {
		this.pledgegoodsNo = pledgegoodsNo;
	}
	public String getPledgegoodsName() {
		return pledgegoodsName;
	}
	public void setPledgegoodsName(String pledgegoodsName) {
		this.pledgegoodsName = pledgegoodsName;
	}


	
	
	
}
