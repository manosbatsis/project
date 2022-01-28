package com.topideal.api.gss.g001;

import java.util.List;

public class HisStockQueryRequest {
   /**
    * 仓库名称	 GSS仓库ID OP仓库编码	 仓库自编码
	菜鸟一期金外滩      301	WMS_360_02 ERPWMS_360_0205
	菜鸟二期天运2仓  300	WMS_360_01 ERPWMS_360_0104
    * */
	private List<Integer> warehouseIds;//仓库ID列表   必填
	private String merchantCode;//GSS商家编码  卓志编码
	private List<String> productCodes;//商品编码列表
	private List<String> gcodes;//货号列表
	private String snapshotDate;//时点日期，格式:YYYY-MM-DD  必填
	private Integer pageNo;//页码，第几页，必须是大于0的正整数；默认值为1
	private Integer pageSize;//每页显示条数，必须是大于0小于等于1000；默认值100
	private String tenantCode;//机构编码（默认为zhuozhi）  必填
	public List<Integer> getWarehouseIds() {
		return warehouseIds;
	}
	public void setWarehouseIds(List<Integer> warehouseIds) {
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
	
	public List<String> getGcodes() {
		return gcodes;
	}
	public void setGcodes(List<String> gcodes) {
		this.gcodes = gcodes;
	}
	public String getSnapshotDate() {
		return snapshotDate;
	}
	public void setSnapshotDate(String snapshotDate) {
		this.snapshotDate = snapshotDate;
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
