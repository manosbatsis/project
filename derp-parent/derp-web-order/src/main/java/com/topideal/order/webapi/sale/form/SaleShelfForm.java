package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 销售上架信息表
 * @author lian_
 *
 */
@ApiModel
public class SaleShelfForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;

	@ApiModelProperty(value = "ID")
    private String id;
	
	@ApiModelProperty(value = "订单/出库单id")
    private String orderId;

	@ApiModelProperty(value = "1 销售   2 销售出库")
    private String orderType;

	@ApiModelProperty(value = "商品id")
    private String goodsId;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;
	
	@ApiModelProperty(value = "条码")
    private String barcode;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "理货单位(00-托盘，01-箱，02-件")
    private String tallyingUnit;

	@ApiModelProperty(value = "销售/出库数量")
    private String num;

	@ApiModelProperty(value = "已上架数量")
    private String totalShelfNum;

	@ApiModelProperty(value = "已计入残损数量")
    private String totalDamagedNum;

	@ApiModelProperty(value = "已计少货数量")
    private String totalLackNum;

	@ApiModelProperty(value = "待上架数量")
    private String stayShelfNum;

	@ApiModelProperty(value = "上架数量")
    private String shelfNum;

	@ApiModelProperty(value = "残损数量")
    private String damagedNum;

	@ApiModelProperty(value = "少货数量")
    private String lackNum;
	
	@ApiModelProperty(value = "po")
    private String poNo;

	@ApiModelProperty(value = "备注")
    private String remark;

	@ApiModelProperty(value = "上架时间")
    private String shelfDate;

	@ApiModelProperty(value = "核销表获取状态（0/空-未获取、1-已获取）")
    private String verifyRelState;

	@ApiModelProperty(value = "操作人")
    private String operator;

	@ApiModelProperty(value = "操作人ID")
    private String operatorId;
	
	@ApiModelProperty(value = "上架入库单id")
    private String saleShelfIdepotId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "事业部id")
    private String buId;

	@ApiModelProperty(value = "上架单ID")
    private String shelfId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTotalShelfNum() {
		return totalShelfNum;
	}

	public void setTotalShelfNum(String totalShelfNum) {
		this.totalShelfNum = totalShelfNum;
	}

	public String getTotalDamagedNum() {
		return totalDamagedNum;
	}

	public void setTotalDamagedNum(String totalDamagedNum) {
		this.totalDamagedNum = totalDamagedNum;
	}

	public String getTotalLackNum() {
		return totalLackNum;
	}

	public void setTotalLackNum(String totalLackNum) {
		this.totalLackNum = totalLackNum;
	}

	public String getStayShelfNum() {
		return stayShelfNum;
	}

	public void setStayShelfNum(String stayShelfNum) {
		this.stayShelfNum = stayShelfNum;
	}

	public String getShelfNum() {
		return shelfNum;
	}

	public void setShelfNum(String shelfNum) {
		this.shelfNum = shelfNum;
	}

	public String getDamagedNum() {
		return damagedNum;
	}

	public void setDamagedNum(String damagedNum) {
		this.damagedNum = damagedNum;
	}

	public String getLackNum() {
		return lackNum;
	}

	public void setLackNum(String lackNum) {
		this.lackNum = lackNum;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShelfDate() {
		return shelfDate;
	}

	public void setShelfDate(String shelfDate) {
		this.shelfDate = shelfDate;
	}

	public String getVerifyRelState() {
		return verifyRelState;
	}

	public void setVerifyRelState(String verifyRelState) {
		this.verifyRelState = verifyRelState;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getSaleShelfIdepotId() {
		return saleShelfIdepotId;
	}

	public void setSaleShelfIdepotId(String saleShelfIdepotId) {
		this.saleShelfIdepotId = saleShelfIdepotId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public String getBuId() {
		return buId;
	}

	public void setBuId(String buId) {
		this.buId = buId;
	}

	public String getShelfId() {
		return shelfId;
	}

	public void setShelfId(String shelfId) {
		this.shelfId = shelfId;
	}
}
