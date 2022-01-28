package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SaleCreditOrderItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "销售赊销单ID")
    private Long creditOrderId;
  
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;

	@ApiModelProperty(value = "商品编码")
    private String goodsCode;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "条形码")
    private String barcode;

	@ApiModelProperty(value = "销售数量")
    private Integer num;

	@ApiModelProperty(value = "销售单价")
    private BigDecimal price;

	@ApiModelProperty(value = "销售金额")
    private BigDecimal amount;

	@ApiModelProperty(value = "本金")
    private BigDecimal principal;

	@ApiModelProperty(value = "保证金")
    private BigDecimal marginAmount;

	@ApiModelProperty(value = "到期单价")
    private BigDecimal expirePrice;

	@ApiModelProperty(value = "到期金额")
    private BigDecimal expireAmount;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
	
	@ApiModelProperty(value = "可赎回数量")
    private Integer stayRedempNum;
	
	@ApiModelProperty(value = "可赎回金额")
    private BigDecimal stayRedempAmount;
	
	@ApiModelProperty(value = "本地赎回金额")
    private Integer redempNum;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreditOrderId() {
		return creditOrderId;
	}

	public void setCreditOrderId(Long creditOrderId) {
		this.creditOrderId = creditOrderId;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getMarginAmount() {
		return marginAmount;
	}

	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}

	public BigDecimal getExpirePrice() {
		return expirePrice;
	}

	public void setExpirePrice(BigDecimal expirePrice) {
		this.expirePrice = expirePrice;
	}

	public BigDecimal getExpireAmount() {
		return expireAmount;
	}

	public void setExpireAmount(BigDecimal expireAmount) {
		this.expireAmount = expireAmount;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Integer getStayRedempNum() {
		return stayRedempNum;
	}

	public void setStayRedempNum(Integer stayRedempNum) {
		this.stayRedempNum = stayRedempNum;
	}

	public BigDecimal getStayRedempAmount() {
		return stayRedempAmount;
	}

	public void setStayRedempAmount(BigDecimal stayRedempAmount) {
		this.stayRedempAmount = stayRedempAmount;
	}

	public Integer getRedempNum() {
		return redempNum;
	}

	public void setRedempNum(Integer redempNum) {
		this.redempNum = redempNum;
	}

}
