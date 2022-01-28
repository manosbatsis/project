package com.topideal.api.gss.g002;

import java.util.List;

public class ReadBatchStocksRequest {

	private List<Long> warehouseIds;//仓库ID列表   必填
	private String merchantCode;//GSS商家编码  卓志编码
	private List<String> productCodes;//商品编码列表
	private Integer pageNo;//页码，第几页，必须是大于0的正整数；默认值为1
	private Integer pageSize;//每页显示条数，必须是大于0小于等于1000；默认值100
	private String tenantCode;//机构编码（默认为zhuozhi）  必填
	
	public List<Long> getWarehouseIds() {
		return warehouseIds;
	}
	public void setWarehouseIds(List<Long> warehouseIds) {
		this.warehouseIds = warehouseIds;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public List<String> getProductCodes() {
		return productCodes;
	}
	public void setProductCodes(List<String> productCodes) {
		this.productCodes = productCodes;
	}
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getTenantCode() {
		return tenantCode;
	}
	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}
	
	
}
