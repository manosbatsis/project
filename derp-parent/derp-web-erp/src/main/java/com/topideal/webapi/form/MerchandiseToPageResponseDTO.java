package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.ApiExternalConfigDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问商品详情响应
 * @author 杨创
 */
@ApiModel
public class MerchandiseToPageResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "品牌下拉框")
	private List<SelectBean> brandBean;
	@ApiModelProperty(value = "二级分类下拉框")
	private List<SelectBean> productTypeBean;
	public List<SelectBean> getBrandBean() {
		return brandBean;
	}
	public void setBrandBean(List<SelectBean> brandBean) {
		this.brandBean = brandBean;
	}
	public List<SelectBean> getProductTypeBean() {
		return productTypeBean;
	}
	public void setProductTypeBean(List<SelectBean> productTypeBean) {
		this.productTypeBean = productTypeBean;
	}
	
	




}
