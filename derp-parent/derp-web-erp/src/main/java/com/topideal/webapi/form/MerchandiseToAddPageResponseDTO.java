package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问商品新增页面响应
 * @author 杨创
 */
@ApiModel
public class MerchandiseToAddPageResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "品牌下拉框")
	private List<SelectBean> brandBean;
	@ApiModelProperty(value = "原产国下拉框")
	private List<SelectBean> countryBean;
	@ApiModelProperty(value = "一级分类下拉框")
	private List<SelectBean> catBean;
	@ApiModelProperty(value = "单位下拉框")
	private List<SelectBean> unitBean;
	@ApiModelProperty(value = "包装方式下拉框")
	private List<SelectBean> packBean;
	public List<SelectBean> getBrandBean() {
		return brandBean;
	}
	public void setBrandBean(List<SelectBean> brandBean) {
		this.brandBean = brandBean;
	}
	public List<SelectBean> getCountryBean() {
		return countryBean;
	}
	public void setCountryBean(List<SelectBean> countryBean) {
		this.countryBean = countryBean;
	}
	public List<SelectBean> getCatBean() {
		return catBean;
	}
	public void setCatBean(List<SelectBean> catBean) {
		this.catBean = catBean;
	}
	public List<SelectBean> getUnitBean() {
		return unitBean;
	}
	public void setUnitBean(List<SelectBean> unitBean) {
		this.unitBean = unitBean;
	}
	public List<SelectBean> getPackBean() {
		return packBean;
	}
	public void setPackBean(List<SelectBean> packBean) {
		this.packBean = packBean;
	}

	
	




}
