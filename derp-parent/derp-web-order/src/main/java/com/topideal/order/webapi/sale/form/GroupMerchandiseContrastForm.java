package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class GroupMerchandiseContrastForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	
	@ApiModelProperty(value = "主键ID")
    private String id;
	
	@ApiModelProperty(value = "商家ID")
    private String merchantId;
  
	@ApiModelProperty(value = "组合品ID")
    private String skuId;
   
	@ApiModelProperty(value = "组合品名称")
    private String groupGoodsName;
	
	@ApiModelProperty(value = "店铺ID")
    private String shopId;
	
	@ApiModelProperty(value = "店铺名称")
    private String shopName;

	@ApiModelProperty(value = "状态：0-已停用 1-已启用")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "更新开始时间")
    private String updateStartDate ;
 
	@ApiModelProperty(value = "更新结束时间")
    private String updateEndDate ;
	
	@ApiModelProperty(value = "id集合，多个用逗号隔开")
	private String ids;
	
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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getGroupGoodsName() {
		return groupGoodsName;
	}

	public void setGroupGoodsName(String groupGoodsName) {
		this.groupGoodsName = groupGoodsName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdateStartDate() {
		return updateStartDate;
	}

	public void setUpdateStartDate(String updateStartDate) {
		this.updateStartDate = updateStartDate;
	}

	public String getUpdateEndDate() {
		return updateEndDate;
	}

	public void setUpdateEndDate(String updateEndDate) {
		this.updateEndDate = updateEndDate;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
}
