package com.topideal.entity.dto.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TobTemporaryReceiveBillItemDTO extends PageModel implements Serializable {

	@ApiModelProperty("ID")
	private Long id;

	@ApiModelProperty("账单Id")
	private Long billId;

	@ApiModelProperty("商品id")
	private Long goodsId;

	@ApiModelProperty("商品名称")
	private String goodsName;

	@ApiModelProperty("商品货号")
	private String goodsNo;

	@ApiModelProperty("销售单价")
	private BigDecimal price;

	@ApiModelProperty("上架好品量")
	private Integer shelfNum;

	@ApiModelProperty("创建时间")
	private Timestamp createDate;

	@ApiModelProperty("修改时间")
	private Timestamp modifyDate;

	@ApiModelProperty("账单Id集合")
	private List<Long> billIds;
	/**
	 * 已核销金额
	 */
	@ApiModelProperty("已核销金额")
	private BigDecimal verifiyAmount;
	/**
	 * 应收账单号
	 */
	@ApiModelProperty("应收账单号")
	private String receiveCode;

	@ApiModelProperty(hidden = true)
	private String shelfCode;

	//待核销金额
	@ApiModelProperty(hidden = true)
	private BigDecimal beVerifyAmount;
	/**
    * 母品牌
    */
	@ApiModelProperty("母品牌")
    private String parentBrandName;
    /**
    * 母品牌id
    */
	@ApiModelProperty("母品牌id")
    private Long parentBrandId;
    /**
    * 母品牌编码
    */
	@ApiModelProperty("母品牌编码")
    private String parentBrandCode;
	/**
     * po号
     */
	@ApiModelProperty("po号")
    private String poNo;

	@ApiModelProperty(value = "原上架单号", hidden = true)
    private String originalShelfCode;

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* billId get 方法 */
	public Long getBillId() {
		return billId;
	}

	/* billId set 方法 */
	public void setBillId(Long billId) {
		this.billId = billId;
	}

	/* goodsId get 方法 */
	public Long getGoodsId() {
		return goodsId;
	}

	/* goodsId set 方法 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/* goodsName get 方法 */
	public String getGoodsName() {
		return goodsName;
	}

	/* goodsName set 方法 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/* goodsNo get 方法 */
	public String getGoodsNo() {
		return goodsNo;
	}

	/* goodsNo set 方法 */
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	/* price get 方法 */
	public BigDecimal getPrice() {
		return price;
	}

	/* price set 方法 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/* shelfNum get 方法 */
	public Integer getShelfNum() {
		return shelfNum;
	}

	/* shelfNum set 方法 */
	public void setShelfNum(Integer shelfNum) {
		this.shelfNum = shelfNum;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public List<Long> getBillIds() {
		return billIds;
	}

	public void setBillIds(List<Long> billIds) {
		this.billIds = billIds;
	}

	public BigDecimal getVerifiyAmount() {
		return verifiyAmount;
	}

	public void setVerifiyAmount(BigDecimal verifiyAmount) {
		this.verifiyAmount = verifiyAmount;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public String getShelfCode() {
		return shelfCode;
	}

	public void setShelfCode(String shelfCode) {
		this.shelfCode = shelfCode;
	}

	public BigDecimal getBeVerifyAmount() {
		return beVerifyAmount;
	}

	public void setBeVerifyAmount(BigDecimal beVerifyAmount) {
		this.beVerifyAmount = beVerifyAmount;
	}

	public String getParentBrandName() {
		return parentBrandName;
	}

	public void setParentBrandName(String parentBrandName) {
		this.parentBrandName = parentBrandName;
	}

	public Long getParentBrandId() {
		return parentBrandId;
	}

	public void setParentBrandId(Long parentBrandId) {
		this.parentBrandId = parentBrandId;
	}

	public String getParentBrandCode() {
		return parentBrandCode;
	}

	public void setParentBrandCode(String parentBrandCode) {
		this.parentBrandCode = parentBrandCode;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getOriginalShelfCode() {
		return originalShelfCode;
	}

	public void setOriginalShelfCode(String originalShelfCode) {
		this.originalShelfCode = originalShelfCode;
	}
}
