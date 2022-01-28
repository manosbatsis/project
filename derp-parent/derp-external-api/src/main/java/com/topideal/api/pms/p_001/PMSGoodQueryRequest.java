package com.topideal.api.pms.p_001;

import java.util.List;

import net.sf.json.JSONObject;

/**
 * PMS  商品查询接口
 * @author 杨创 2021/12/06
 */
public class PMSGoodQueryRequest {
	private List<String> productCodes;//商品货号数组
	private List<String> cCodes;//客户ID数组
	private String queryType;// 查询信息类型 枚举: 0,1枚举备注: 0:商品信息,1:+海关账册商品信息
	private String source;//来源系统
	private Integer rowsPerPage;//每页条数
	private Integer pageNo;//主商品ID数组	
	private String cuiCode;//租户
	//private String customsCode;//申报地海关
	//private String accountCode;//账册编码
	//private String isDefault;//主商品ID数组	
	//private String version;//版本号
	//private List<String> prdCodes;//产品ID数组
	//private String status;//状态 枚举: 10,20,30 枚举备注: 10制单，20启用，30取消启用,为空则默认查初始、启用、停用
	//private List<String> opgCodes;//主商品ID数组
	public List<String> getProductCodes() {
		return productCodes;
	}
	public void setProductCodes(List<String> productCodes) {
		this.productCodes = productCodes;
	}
	public List<String> getcCodes() {
		return cCodes;
	}
	public void setcCodes(List<String> cCodes) {
		this.cCodes = cCodes;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Integer getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(Integer rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public String getCuiCode() {
		return cuiCode;
	}
	public void setCuiCode(String cuiCode) {
		this.cuiCode = cuiCode;
	}

	

}
