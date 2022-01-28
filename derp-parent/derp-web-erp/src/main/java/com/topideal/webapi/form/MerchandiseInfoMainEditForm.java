package com.topideal.webapi.form;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品信息
 * 
 * @author lchenxing
 */
public class MerchandiseInfoMainEditForm implements Serializable {
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "id",required = true)
	private Long id;
	@ApiModelProperty(value = "备案价格")
	private BigDecimal filingPrice;
	@ApiModelProperty(value = "描述")
	private String describe;
	@ApiModelProperty(value = "工厂编码")
	private String factoryNo;
	@ApiModelProperty(value = "备注")
	private String remark;	
	@ApiModelProperty(value = "是否组合(1-是，0-否)")
    private String isGroup;
	@ApiModelProperty(value = "商品一级级分类ID")
	private Long productTypeId0;
	@ApiModelProperty(value = "商品二级分类ID")
	private Long productTypeId;
	@ApiModelProperty(value = "一箱多少件")
    private Integer boxToUnit;
	@ApiModelProperty(value = "一托多少件")
    private Integer torrToUnit;	
	@ApiModelProperty(value = "groupIds")
    private String groupIds;
    @ApiModelProperty(value = "groupNums")
    private String groupNums;
    @ApiModelProperty(value = "groupPrices")
    private String groupPrices;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getFilingPrice() {
		return filingPrice;
	}
	public void setFilingPrice(BigDecimal filingPrice) {
		this.filingPrice = filingPrice;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getFactoryNo() {
		return factoryNo;
	}
	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}
	public Long getProductTypeId0() {
		return productTypeId0;
	}
	public void setProductTypeId0(Long productTypeId0) {
		this.productTypeId0 = productTypeId0;
	}
	public Long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public Integer getBoxToUnit() {
		return boxToUnit;
	}
	public void setBoxToUnit(Integer boxToUnit) {
		this.boxToUnit = boxToUnit;
	}
	public Integer getTorrToUnit() {
		return torrToUnit;
	}
	public void setTorrToUnit(Integer torrToUnit) {
		this.torrToUnit = torrToUnit;
	}
	public String getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
	public String getGroupNums() {
		return groupNums;
	}
	public void setGroupNums(String groupNums) {
		this.groupNums = groupNums;
	}
	public String getGroupPrices() {
		return groupPrices;
	}
	public void setGroupPrices(String groupPrices) {
		this.groupPrices = groupPrices;
	}
	
	

	

   
}
