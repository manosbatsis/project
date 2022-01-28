package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PurchaseReturnOdepotModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购退货出库订单号
    */
    private String code;
    /**
    * 采购退订单号
    */
    private String purchaseReturnCode;
    /**
    * 退出仓库id
    */
    private Long outDepotId;
    /**
    * 退出仓库名称
    */
    private String outDepotName;
    /**
    * 出库时间
    */
    private Timestamp returnOutDate;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 状态：001-待审核 ,003-已审核 ,015:待出仓,016:已出仓,027:出库中 ,007已完结
    */
    private String status;
    /**
    * 审核时间
    */
    private Timestamp auditDate;
    /**
    * 客户id
    */
    private Long supplierId;
    /**
    * 客户名称
    */
    private String supplierName;
    /**
    * 币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑
    */
    private String currency;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    *  事业部id
    */
    private Long buId;
    /**
    * po号
    */
    private String poNo;
    /**
    * 物流企业名称
    */
    private String logisticsName;
    /**
    * 进口模式10：BBC;20：BC;30：保留; 40：CC
    */
    private String importMode;
    /**
    * LBX单号
    */
    private String lbxNo;
    /**
    * 提单号
    */
    private String blNo;
    /**
    * 运单号
    */
    private String wayBillNo;
    /**
    * 审核人
    */
    private Long auditor;
    /**
    * 审核人名称
    */
    private String auditName;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 创建人名称
    */
    private String createName;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //理货单位
    private String tallyingUnit;

    //采购退货ID
    private Long purchaseReturnId;
    
    /**
     * 外部订单号
     */
     private String extraCode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*purchaseReturnCode get 方法 */
    public String getPurchaseReturnCode(){
    return purchaseReturnCode;
    }
    /*purchaseReturnCode set 方法 */
    public void setPurchaseReturnCode(String  purchaseReturnCode){
    this.purchaseReturnCode=purchaseReturnCode;
    }
    /*outDepotId get 方法 */
    public Long getOutDepotId(){
    return outDepotId;
    }
    /*outDepotId set 方法 */
    public void setOutDepotId(Long  outDepotId){
    this.outDepotId=outDepotId;
    }
    /*outDepotName get 方法 */
    public String getOutDepotName(){
    return outDepotName;
    }
    /*outDepotName set 方法 */
    public void setOutDepotName(String  outDepotName){
    this.outDepotName=outDepotName;
    }
    /*returnOutDate get 方法 */
    public Timestamp getReturnOutDate(){
    return returnOutDate;
    }
    /*returnOutDate set 方法 */
    public void setReturnOutDate(Timestamp  returnOutDate){
    this.returnOutDate=returnOutDate;
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
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*auditDate get 方法 */
    public Timestamp getAuditDate(){
    return auditDate;
    }
    /*auditDate set 方法 */
    public void setAuditDate(Timestamp  auditDate){
    this.auditDate=auditDate;
    }
    /*supplierId get 方法 */
    public Long getSupplierId(){
    return supplierId;
    }
    /*supplierId set 方法 */
    public void setSupplierId(Long  supplierId){
    this.supplierId=supplierId;
    }
    /*supplierName get 方法 */
    public String getSupplierName(){
    return supplierName;
    }
    /*supplierName set 方法 */
    public void setSupplierName(String  supplierName){
    this.supplierName=supplierName;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*logisticsName get 方法 */
    public String getLogisticsName(){
    return logisticsName;
    }
    /*logisticsName set 方法 */
    public void setLogisticsName(String  logisticsName){
    this.logisticsName=logisticsName;
    }
    /*importMode get 方法 */
    public String getImportMode(){
    return importMode;
    }
    /*importMode set 方法 */
    public void setImportMode(String  importMode){
    this.importMode=importMode;
    }
    /*lbxNo get 方法 */
    public String getLbxNo(){
    return lbxNo;
    }
    /*lbxNo set 方法 */
    public void setLbxNo(String  lbxNo){
    this.lbxNo=lbxNo;
    }
    /*blNo get 方法 */
    public String getBlNo(){
    return blNo;
    }
    /*blNo set 方法 */
    public void setBlNo(String  blNo){
    this.blNo=blNo;
    }
    /*wayBillNo get 方法 */
    public String getWayBillNo(){
    return wayBillNo;
    }
    /*wayBillNo set 方法 */
    public void setWayBillNo(String  wayBillNo){
    this.wayBillNo=wayBillNo;
    }
    /*auditor get 方法 */
    public Long getAuditor(){
    return auditor;
    }
    /*auditor set 方法 */
    public void setAuditor(Long  auditor){
    this.auditor=auditor;
    }
    /*auditName get 方法 */
    public String getAuditName(){
    return auditName;
    }
    /*auditName set 方法 */
    public void setAuditName(String  auditName){
    this.auditName=auditName;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }

    public Long getPurchaseReturnId() {
        return purchaseReturnId;
    }

    public void setPurchaseReturnId(Long purchaseReturnId) {
        this.purchaseReturnId = purchaseReturnId;
    }
	public String getExtraCode() {
		return extraCode;
	}
	public void setExtraCode(String extraCode) {
		this.extraCode = extraCode;
	}
    
    
}
