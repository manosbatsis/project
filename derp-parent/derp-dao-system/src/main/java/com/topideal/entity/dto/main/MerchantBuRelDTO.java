package com.topideal.entity.dto.main;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

public class MerchantBuRelDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty("id")
    private Long id;
    /**
    * 公司id
    */
	@ApiModelProperty("公司id")
    private Long merchantId;
    /**
    * 公司名称
    */
	@ApiModelProperty("公司名称")
    private String merchantName;
    /**
    * 公司编码
    */
	@ApiModelProperty("公司编码")
    private String merchantCode;
    /**
    * 事业部ID
    */
	@ApiModelProperty("事业部ID")
    private Long buId;
    /**
    * 事业部名称
    */
	@ApiModelProperty("事业部名称")
    private String buName;
    /**
    * 事业部编码
    */
	@ApiModelProperty("事业部编码")
    private String buCode;
    /**
    * 状态 1-启用 0-禁用
    */
	@ApiModelProperty("状态 1-启用 0-禁用")
    private String status;
	@ApiModelProperty("状态 1-启用 0-禁用")
    private String statusLabel;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;
    //是否启用采购价格管理 1-启用 0-不启用
    @ApiModelProperty("是否启用采购价格管理 1-启用 0-不启用")
    private String purchasePriceManage;
    @ApiModelProperty("是否启用采购价格管理 1-启用 0-不启用")
    private String purchasePriceManageLabel;
    //状态 1-启用 0-不启用
    @ApiModelProperty("是否启用销售状态 1-启用 0-不启用")
    private String salePriceManage;
    @ApiModelProperty("是否启用销售状态 1-启用 0-不启用")
    private String salePriceManageLabel;

    //审批方式 1-OA审批 2-经分销审批
    @ApiModelProperty("审批方式 1-OA审批 2-经分销审批")
    private String purchaseAuditMethod;
    //审批方式 1-OA审批 2-经分销审批
    @ApiModelProperty("审批方式 1-OA审批 2-经分销审批")
    private String purchaseAuditMethodLabel;
    //库位管理 0:启用 1:禁用
    @ApiModelProperty("库位管理 0:启用 1:禁用")
    private String stockLocationManage;
    private String stockLocationManageLabel;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*merchantCode get 方法 */
    public String getMerchantCode(){
    return merchantCode;
    }
    /*merchantCode set 方法 */
    public void setMerchantCode(String  merchantCode){
    this.merchantCode=merchantCode;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*buCode get 方法 */
    public String getBuCode(){
    return buCode;
    }
    /*buCode set 方法 */
    public void setBuCode(String  buCode){
    this.buCode=buCode;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchant_bu_rel_statuslist, status) ;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

    public String getPurchasePriceManage() {
        return purchasePriceManage;
    }

    public void setPurchasePriceManage(String purchasePriceManage) {
        this.purchasePriceManage = purchasePriceManage;
        this.purchasePriceManageLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchant_bu_rel_purchase_price_manageList, purchasePriceManage) ;
    }

    public String getPurchasePriceManageLabel() {
        return purchasePriceManageLabel;
    }

    public void setPurchasePriceManageLabel(String purchasePriceManageLabel) {
        this.purchasePriceManageLabel = purchasePriceManageLabel;
    }
	public String getSalePriceManage() {
		return salePriceManage;
	}
	public void setSalePriceManage(String salePriceManage) {
		this.salePriceManage = salePriceManage;
        this.salePriceManageLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchant_bu_rel_sale_price_manageList, salePriceManage) ;

	}
	public String getSalePriceManageLabel() {
		return salePriceManageLabel;
	}
	public void setSalePriceManageLabel(String salePriceManageLabel) {
		this.salePriceManageLabel = salePriceManageLabel;
	}

    public String getPurchaseAuditMethod() {
        return purchaseAuditMethod;
    }

    public void setPurchaseAuditMethod(String purchaseAuditMethod) {
        this.purchaseAuditMethod = purchaseAuditMethod;
        this.purchaseAuditMethodLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchant_bu_rel_purchase_audit_methodList, purchaseAuditMethod) ;

    }
	public String getPurchaseAuditMethodLabel() {
		return purchaseAuditMethodLabel;
	}
	public void setPurchaseAuditMethodLabel(String purchaseAuditMethodLabel) {
		this.purchaseAuditMethodLabel = purchaseAuditMethodLabel;
	}

    public String getStockLocationManage() {
        return stockLocationManage;
    }

    public void setStockLocationManage(String stockLocationManage) {
        this.stockLocationManage = stockLocationManage;
        this.stockLocationManageLabel = DERP_SYS.getLabelByKey(DERP_SYS.merchant_bu_rel_stock_location_managelist, stockLocationManage);
    }

    public String getStockLocationManageLabel() {
        return stockLocationManageLabel;
    }

    public void setStockLocationManageLabel(String stockLocationManageLabel) {
        this.stockLocationManageLabel = stockLocationManageLabel;
    }
}
