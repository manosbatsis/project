package com.topideal.entity.vo.order;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class MaterialOutDepotModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 领料单id
    */
    private Long materialOrderId;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * PO号
    */
    private String poNo;
    /**
    * 调出仓库id
    */
    private Long outDepotId;
    /**
    * 调出仓库名称
    */
    private String outDepotName;
    /**
    * 客户id(供应商)
    */
    private Long customerId;
    /**
    * 客户名称 
    */
    private String customerName;
    /**
    * 发货时间
    */
    private Timestamp deliverDate;
    /**
    * 状态 017-待出库,018-已出库,架,027-出库中 006-已删除 
    */
    private String status;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 出库清单编号
    */
    private String code;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 领料单编号
    */
    private String materialOrderCode;
    /**
    * 物流企业名称
    */
    private String logisticsName;
    /**
    * 提单号
    */
    private String blNo;
    /**
    * 运单号
    */
    private String wayBillNo;
    /**
    * 进口模式 10：BBC;20：BC 30：保留; 40：CC
    */
    private String importMode;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 领料出库外部单号
    */
    private String outExternalCode;
    /**
    * 币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String currency;
    /**
    * 订单来源  1手工导入 2.接口回传
    */
    private String orderSource;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    *  事业部id
    */
    private Long buId;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 审核人ID
     */
    private Long auditor;
    /**
     * 审核时间
     */
    private Timestamp auditDate;
    /**
     * 审核人名称
     */
    private String auditName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*materialOrderId get 方法 */
    public Long getMaterialOrderId(){
    return materialOrderId;
    }
    /*materialOrderId set 方法 */
    public void setMaterialOrderId(Long  materialOrderId){
    this.materialOrderId=materialOrderId;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
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
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
    return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
    this.deliverDate=deliverDate;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*materialOrderCode get 方法 */
    public String getMaterialOrderCode(){
    return materialOrderCode;
    }
    /*materialOrderCode set 方法 */
    public void setMaterialOrderCode(String  materialOrderCode){
    this.materialOrderCode=materialOrderCode;
    }
    /*logisticsName get 方法 */
    public String getLogisticsName(){
    return logisticsName;
    }
    /*logisticsName set 方法 */
    public void setLogisticsName(String  logisticsName){
    this.logisticsName=logisticsName;
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
    /*importMode get 方法 */
    public String getImportMode(){
    return importMode;
    }
    /*importMode set 方法 */
    public void setImportMode(String  importMode){
    this.importMode=importMode;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*outExternalCode get 方法 */
    public String getOutExternalCode(){
    return outExternalCode;
    }
    /*outExternalCode set 方法 */
    public void setOutExternalCode(String  outExternalCode){
    this.outExternalCode=outExternalCode;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*orderSource get 方法 */
    public String getOrderSource(){
    return orderSource;
    }
    /*orderSource set 方法 */
    public void setOrderSource(String  orderSource){
    this.orderSource=orderSource;
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

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Long getAuditor() {
        return auditor;
    }

    public void setAuditor(Long auditor) {
        this.auditor = auditor;
    }

    public Timestamp getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Timestamp auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }
}
