package com.topideal.webapi.form;

import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModelProperty;

/**
 * 仓库信息表
 * @author lianchenxing
 */
public class DepotInfoForm extends PageForm implements Serializable {
	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "仓管员")
	private String adminName;	
	@ApiModelProperty(value = "OP仓库编号",required = true)
	private String code;
	@ApiModelProperty(value = "仓库详细地址")
	private String address;	
	@ApiModelProperty(value = "仓库名称",required = true)
	private String name;
	@ApiModelProperty(value = "申报国检编号",required = true)
	private String inspectNo;
	@ApiModelProperty(value = "id,新增为空,修改id必填")
	private Long id;
	@ApiModelProperty(value = "仓库类别 a-保税仓，b-备查仓，c-海外仓，d-中转仓,e-暂存区，f-销毁区  g-内贸仓",required = true)
	private String type;
	@ApiModelProperty(value = "申报海关编号",required = true)
	private String customsNo;
	//是否卓志仓(1-是,0-否)
	@ApiModelProperty(value = "是否代销仓")
    private String isTopBooks;
    // 仓库类型(1-仓库(BBC),2-场站(BC),3-其他)
    @ApiModelProperty(value = "仓库类型",required = true)
    private String depotType;
    @ApiModelProperty(value = "联系电话")
    private String tel;
    //状态  1-启用,0-不启用，默认是1启用   
    @ApiModelProperty(value = "是否启用 状态  1-启用,0-不启用，默认是1启用 ")
    private String status;
    @ApiModelProperty(value = "仓库所在国家")
    private String country;
    @ApiModelProperty(value = "选择关联公司Id")
	private Long merchantId;
	//1商家key 2关联商家key
	@ApiModelProperty(value = "公司key",required = true)
    private String isMerchant;
    @ApiModelProperty(value = "仓库自编码",required = true)
    private String depotCode;
    //批次效期强校验：1-是 0-否
    @ApiModelProperty(value = "是否批次效期强校验",required = true)
    private String batchValidation;
	@ApiModelProperty(value = "ISV仓库类型",required = true)
	private String ISVDepotType;
	//gss仓库Id
	@ApiModelProperty(value = "gss仓库Id")
	private Long warehouseId;
	//gss仓库Id
	@ApiModelProperty(value = "是否代客管理仓库")
	private String isValetManage;
	
	@ApiModelProperty(value = "json")
	private String json;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInspectNo() {
		return inspectNo;
	}
	public void setInspectNo(String inspectNo) {
		this.inspectNo = inspectNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCustomsNo() {
		return customsNo;
	}
	public void setCustomsNo(String customsNo) {
		this.customsNo = customsNo;
	}
	public String getIsTopBooks() {
		return isTopBooks;
	}
	public void setIsTopBooks(String isTopBooks) {
		this.isTopBooks = isTopBooks;
	}
	public String getDepotType() {
		return depotType;
	}
	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getIsMerchant() {
		return isMerchant;
	}
	public void setIsMerchant(String isMerchant) {
		this.isMerchant = isMerchant;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getBatchValidation() {
		return batchValidation;
	}
	public void setBatchValidation(String batchValidation) {
		this.batchValidation = batchValidation;
	}
	public String getISVDepotType() {
		return ISVDepotType;
	}
	public void setISVDepotType(String iSVDepotType) {
		ISVDepotType = iSVDepotType;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getIsValetManage() {
		return isValetManage;
	}
	public void setIsValetManage(String isValetManage) {
		this.isValetManage = isValetManage;
	}


	
}
