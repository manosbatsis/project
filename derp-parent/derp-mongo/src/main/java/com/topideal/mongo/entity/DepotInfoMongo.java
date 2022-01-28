package com.topideal.mongo.entity;

/**
 * 仓库信息表
 * 
 * @author lianchenxing
 */
public class DepotInfoMongo {

	// 管理员
	private String adminName;
	// 仓库编码
	private String code;
	// 仓库详细地址
	private String address;
	// 单位名称
	private String name;
	// 创建人
	private Long creater;
	// 申报地国检编码
	private String inspectNo;
	// 自增主键
	private Long depotId;
	// 仓库类别(a-保税仓，b-备查仓，c-海外仓，d-中转仓,e-暂存区，f-销毁区 g-内贸仓)
	private String type;
	// 申报地海关编码
	private String customsNo;
	// 是否卓志仓(1-是,0-否)
	private String isTopBooks;
	// 仓库类型(1-仓库(BBC),2-场站(BC),3-其他)
	private String depotType;
	// 联系电话
	private String tel;
	// 状态 1-启用,0-不启用，默认是1启用
	private String status;
	// 仓库所在国家
	private String country;
	// 商家id
	private Long merchantId;
	// 1商家key 2关联商家key
	private String isMerchant;
	// 仓库自编码
	private String depotCode;
	// 批次效期强校验：1-是 0-否
	private String batchValidation;
	//gss仓库Id
	private Long warehouseId;
	//ISV仓库类型
	private String ISVDepotType;
	//是否代客管理仓库
	private String isValetManage;
	
	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* isMerchant get 方法 */
	public String getIsMerchant() {
		return isMerchant;
	}

	/* isMerchant set 方法 */
	public void setIsMerchant(String isMerchant) {
		this.isMerchant = isMerchant;
	}

	/* tel get 方法 */
	public String getTel() {
		return tel;
	}

	/* tel set 方法 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* country get 方法 */
	public String getCountry() {
		return country;
	}

	/* country set 方法 */
	public void setCountry(String country) {
		this.country = country;
	}

	/* isTopBooks get 方法 */
	public String getIsTopBooks() {
		return isTopBooks;
	}

	/* isTopBooks set 方法 */
	public void setIsTopBooks(String isTopBooks) {
		this.isTopBooks = isTopBooks;
	}

	/* depotType get 方法 */
	public String getDepotType() {
		return depotType;
	}

	/* depotType set 方法 */
	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}

	/* adminName get 方法 */
	public String getAdminName() {
		return adminName;
	}

	/* adminName set 方法 */
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* address get 方法 */
	public String getAddress() {
		return address;
	}

	/* address set 方法 */
	public void setAddress(String address) {
		this.address = address;
	}

	/* name get 方法 */
	public String getName() {
		return name;
	}

	/* name set 方法 */
	public void setName(String name) {
		this.name = name;
	}

	/* creatorId get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creatorId set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* inspectNo get 方法 */
	public String getInspectNo() {
		return inspectNo;
	}

	/* inspectNo set 方法 */
	public void setInspectNo(String inspectNo) {
		this.inspectNo = inspectNo;
	}

	/* type get 方法 */
	public String getType() {
		return type;
	}

	/* type set 方法 */
	public void setType(String type) {
		this.type = type;
	}

	/* customsNo get 方法 */
	public String getCustomsNo() {
		return customsNo;
	}

	/* customsNo set 方法 */
	public void setCustomsNo(String customsNo) {
		this.customsNo = customsNo;
	}

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
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

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getISVDepotType() {
		return ISVDepotType;
	}

	public void setISVDepotType(String ISVDepotType) {
		this.ISVDepotType = ISVDepotType;
	}

	public String getIsValetManage() {
		return isValetManage;
	}

	public void setIsValetManage(String isValetManage) {
		this.isValetManage = isValetManage;
	}
	
}
