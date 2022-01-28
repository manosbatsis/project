package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SaleCreditBillOrderItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value="id")
    private Long id;

	@ApiModelProperty(value="赊销收款单ID")
    private Long creditBillOrderId;

	@ApiModelProperty(value="商品ID")
    private Long goodsId;

	@ApiModelProperty(value="商品编码")
    private String goodsCode;

	@ApiModelProperty(value="商品名称")
    private String goodsName;

	@ApiModelProperty(value="商品货号")
    private String goodsNo;

	@ApiModelProperty(value="标准条码")
    private String commbarcode;

	@ApiModelProperty(value="条形码")
    private String barcode;

	@ApiModelProperty(value="赎回数量")
    private Integer num;

	@ApiModelProperty(value="本金")
    private BigDecimal principalAmount;

	@ApiModelProperty(value="保证金")
    private BigDecimal marginAmount;

	@ApiModelProperty(value="资金占用费")
    private BigDecimal occupationAmount;

	@ApiModelProperty(value="代理费")
    private BigDecimal agencyAmount;

	@ApiModelProperty(value="滞纳金")
    private BigDecimal delayAmount;

	@ApiModelProperty(value="应收款金额")
    private BigDecimal receivableAmount;

	@ApiModelProperty(value="创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "滞纳金减免金额")
	private BigDecimal discountDelayAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreditBillOrderId() {
		return creditBillOrderId;
	}

	public void setCreditBillOrderId(Long creditBillOrderId) {
		this.creditBillOrderId = creditBillOrderId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}

	public BigDecimal getMarginAmount() {
		return marginAmount;
	}

	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}

	public BigDecimal getOccupationAmount() {
		return occupationAmount;
	}

	public void setOccupationAmount(BigDecimal occupationAmount) {
		this.occupationAmount = occupationAmount;
	}

	public BigDecimal getAgencyAmount() {
		return agencyAmount;
	}

	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}

	public BigDecimal getDelayAmount() {
		return delayAmount;
	}

	public void setDelayAmount(BigDecimal delayAmount) {
		this.delayAmount = delayAmount;
	}

	public BigDecimal getReceivableAmount() {
		return receivableAmount;
	}

	public void setReceivableAmount(BigDecimal receivableAmount) {
		this.receivableAmount = receivableAmount;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getDiscountDelayAmount() {
		return discountDelayAmount;
	}

	public void setDiscountDelayAmount(BigDecimal discountDelayAmount) {
		this.discountDelayAmount = discountDelayAmount;
	}
}
