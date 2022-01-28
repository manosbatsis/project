package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问商品编辑页面响应
 * @author 杨创
 */
@ApiModel
public class MerchandiseToEditPageResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "商品详情")
	private MerchandiseInfoDTO detail;	
	@ApiModelProperty(value = "关键id多个应该逗号隔开")
	private String customsAreaIdStr;
	@ApiModelProperty(value = "一级分类下拉框")
	private List<SelectBean> oneCatBean;
	@ApiModelProperty(value = "二级分类下拉框")
	private List<SelectBean> twoCatBean;
	@ApiModelProperty(value = "品牌下拉框")
	private List<SelectBean> brandBean;
	@ApiModelProperty(value = "原产国下拉框")
	private List<SelectBean> countryBean;
	@ApiModelProperty(value = "全部分类下拉框")
	private List<SelectBean> catBean;		
	@ApiModelProperty(value = "单位下拉框")
	private List<SelectBean> unitBean;
	@ApiModelProperty(value = "包装方式下拉框")
	private List<SelectBean> packBean;
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
	public String getCustomsAreaIdStr() {
		return customsAreaIdStr;
	}
	public void setCustomsAreaIdStr(String customsAreaIdStr) {
		this.customsAreaIdStr = customsAreaIdStr;
	}
	public List<SelectBean> getOneCatBean() {
		return oneCatBean;
	}
	public void setOneCatBean(List<SelectBean> oneCatBean) {
		this.oneCatBean = oneCatBean;
	}
	public List<SelectBean> getTwoCatBean() {
		return twoCatBean;
	}
	public void setTwoCatBean(List<SelectBean> twoCatBean) {
		this.twoCatBean = twoCatBean;
	}
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
