package com.topideal.entity.dto.main;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 仓库信息表
 * @author lianchenxing
 */
public class DepotInfoDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "管理员")
	private String adminName;
	@ApiModelProperty(value = "仓库编码")
	private String code;
	@ApiModelProperty(value = "仓库详细地址")
	private String address;
	@ApiModelProperty(value = "单位名称")
	private String name;
	@ApiModelProperty(value = "创建人")
	private Long creater;
	@ApiModelProperty(value = "申报地国检编码")
	private String inspectNo;
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "仓库类别(a-保税仓，b-备查仓，c-海外仓，d-中转仓,e-暂存区，f-销毁区）")
	private String type;
	@ApiModelProperty(value = "typeLabel")
	private String typeLabel;
	@ApiModelProperty(value = "申报地海关编码")
	private String customsNo;
	@ApiModelProperty(value = "创建日期")
	private Timestamp createDate;
	@ApiModelProperty(value = "是否卓志仓(1-是,0-否)")
    private String isTopBooks;
	@ApiModelProperty(value = "isTopBooksLabel")
    private String isTopBooksLabel;
	@ApiModelProperty(value = "仓库类型(1-仓库(BBC),2-场站(BC),3-其他)")
    private String depotType;
	@ApiModelProperty(value = "depotTypeLabel")
    private String depotTypeLabel;
	@ApiModelProperty(value = "联系电话")
    private String tel;
	@ApiModelProperty(value = "状态  1-启用,0-不启用，默认是1启用")
    private String status;
	@ApiModelProperty(value = "statusLabel")
    private String statusLabel;
	@ApiModelProperty(value = "仓库所在国家")
    private String country;
	@ApiModelProperty(value = "商家id")
	private Long merchantId;
	@ApiModelProperty(value = "1商家key 2关联商家key")
    private String isMerchant;
	@ApiModelProperty(value = "isMerchantLabel")
    private String isMerchantLabel;
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	@ApiModelProperty(value = "仓库自编码")
    private String depotCode;
	@ApiModelProperty(value = "批次效期强校验：1-是 0-否")
    private String batchValidation;
	@ApiModelProperty(value = "batchValidationLabel")
    private String batchValidationLabel;
	@ApiModelProperty(value = "是否同关区")
    private String isSameArea;
	@ApiModelProperty(value = "isSameAreaLabel")
    private String isSameAreaLabel;
	@ApiModelProperty(value = "gss仓库Id")
	private Long warehouseId;
	@ApiModelProperty(value = "ISV仓库类型")
	private String ISVDepotType;
	@ApiModelProperty(value = "ISVDepotTypeLabel")
	private String ISVDepotTypeLabel;	
	@ApiModelProperty(value = "是否代客管理仓库")
	private String isValetManage;
	@ApiModelProperty(value = "isValetManageLabel")
	private String isValetManageLabel;

    public String getBatchValidation() {
		return batchValidation;
	}
	public void setBatchValidation(String batchValidation) {
		this.batchValidation = batchValidation;
		if(batchValidation != null) {
			this.batchValidationLabel = DERP_SYS.getLabelByKey(DERP_SYS.batchValidationList, batchValidation);
		}
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public String getIsSameArea() {
		return isSameArea;
	}
	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
		this.isSameAreaLabel = DERP.getLabelByKey(DERP.isSameAreaList, isSameArea);
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	/*isMerchant get 方法 */
    public String getIsMerchant(){
        return isMerchant;
    }
    /*isMerchant set 方法 */
    public void setIsMerchant(String  isMerchant){
        this.isMerchant=isMerchant;
        if(isMerchant != null) {
        	this.isMerchantLabel = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_isMerchantList, isMerchant);
        }
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
		if(status != null) {
			this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_statusList, status) ;
		}
	}
	/* country get 方法 */
	public String getCountry() {
		return country;
	}
	/* country set 方法 */
	public void setCountry(String country) {
		this.country = country;
	}
	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}
	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	/* isTopBooks get 方法 */
	public String getIsTopBooks() {
		return isTopBooks;
	}
	/* isTopBooks set 方法 */
	public void setIsTopBooks(String isTopBooks) {
		this.isTopBooks = isTopBooks;
		if(isTopBooks != null) {
			this.isTopBooksLabel = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_isTopBooksList, isTopBooks);
		}
	}
	/* depotType get 方法 */
	public String getDepotType() {
		return depotType;
	}
	/* depotType set 方法 */
	public void setDepotType(String depotType) {
		this.depotType = depotType;
		if(depotType != null) {
			this.depotTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_depotTypeList, depotType);
		}
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

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* type get 方法 */
	public String getType() {
		return type;
	}

	/* type set 方法 */
	public void setType(String type) {
		this.type = type;
		if(type != null) {
			this.typeLabel = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_typeList, type);
		}
	}

	/* customsNo get 方法 */
	public String getCustomsNo() {
		return customsNo;
	}

	/* customsNo set 方法 */
	public void setCustomsNo(String customsNo) {
		this.customsNo = customsNo;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}


	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	public String getIsTopBooksLabel() {
		return isTopBooksLabel;
	}
	public void setIsTopBooksLabel(String isTopBooksLabel) {
		this.isTopBooksLabel = isTopBooksLabel;
	}
	public String getDepotTypeLabel() {
		return depotTypeLabel;
	}
	public void setDepotTypeLabel(String depotTypeLabel) {
		this.depotTypeLabel = depotTypeLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getIsMerchantLabel() {
		return isMerchantLabel;
	}
	public void setIsMerchantLabel(String isMerchantLabel) {
		this.isMerchantLabel = isMerchantLabel;
	}
	public String getBatchValidationLabel() {
		return batchValidationLabel;
	}
	public void setBatchValidationLabel(String batchValidationLabel) {
		this.batchValidationLabel = batchValidationLabel;
	}

	public String getIsSameAreaLabel() {
		return isSameAreaLabel;
	}
	public void setIsSameAreaLabel(String isSameAreaLabel) {
		this.isSameAreaLabel = isSameAreaLabel;
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
		this.ISVDepotTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_ISVDepotTypeList, ISVDepotType);
	}

	public String getISVDepotTypeLabel() {
		return ISVDepotTypeLabel;
	}

	public void setISVDepotTypeLabel(String ISVDepotTypeLabel) {
		this.ISVDepotTypeLabel = ISVDepotTypeLabel;
	}
	public String getIsValetManage() {
		return isValetManage;
	}
	public void setIsValetManage(String isValetManage) {
		this.isValetManage = isValetManage;
		this.isValetManageLabel = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_isValetManageList, isValetManage);
	}
	public String getIsValetManageLabel() {
		return isValetManageLabel;
	}
	public void setIsValetManageLabel(String isValetManageLabel) {
		this.isValetManageLabel = isValetManageLabel;
	}
	
	
}
