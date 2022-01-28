package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问商品详情页面响应
 * @author 杨创
 */
@ApiModel
public class MerchandiseToDetailsResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "商品详情")
	private MerchandiseInfoDTO detail;
	@ApiModelProperty(value = "商品品牌名称")
	private String brandName;
	@ApiModelProperty(value = "原产国名称")
	private String countryName;
	@ApiModelProperty(value = "单位名称")
	private String unitName;
	@ApiModelProperty(value = "关区名称")
	private String customsAreaIdStr;
	@ApiModelProperty(value = "创建时间")
	private String createDate;
	@ApiModelProperty(value = "修改时间")
	private String modifyDate;
	public MerchandiseInfoDTO getDetail() {
		return detail;
	}
	public void setDetail(MerchandiseInfoDTO detail) {
		this.detail = detail;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getCustomsAreaIdStr() {
		return customsAreaIdStr;
	}
	public void setCustomsAreaIdStr(String customsAreaIdStr) {
		this.customsAreaIdStr = customsAreaIdStr;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	

	




}
