package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class ShelfModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 上架单号
    */
    private String code;
    /**
    * 销售订单编号
    */
    private String saleOrderCode;
    /**
    * 状态
    */
    private String state;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 客户id
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 已上架数量
    */
    private Integer totalShelfNum;
    /**
    * 上架时间
    */
    private Timestamp shelfDate;
    /**
    * 币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String currency;
    /**
    * 操作人ID
    */
    private Long operatorId;
    /**
    * 操作人
    */
    private String operator;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 仓库ID
     */
    private Long outDepotId;
    /**
     * 仓库名称
     */
    private String outDepotName;
    /**
     * 销售订单ID
     */
    private Long saleOrderId;
    /**
     * 是否生成暂估 0-未生成 1-已生成
     */
    private String isGenerate;

    /**
     * 销售出库单id
     */
    private Long saleOutDepotId;
    /**
     * 销售出库编码
     */
    private String saleOutDepotCode;

    /**
     * 是否红冲单：0-否，1-是
     */
    private String isWriteOff;
    /**
     * 原上架单id
     */
    private Long originalShelfId;
    /**
     * 原上架号
     */
    private String originalShelfCode;

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
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
    return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
    this.saleOrderCode=saleOrderCode;
    }
    /*state get 方法 */
    public String getState(){
    return state;
    }
    /*state set 方法 */
    public void setState(String  state){
    this.state=state;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
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
    /*totalShelfNum get 方法 */
    public Integer getTotalShelfNum(){
    return totalShelfNum;
    }
    /*totalShelfNum set 方法 */
    public void setTotalShelfNum(Integer  totalShelfNum){
    this.totalShelfNum=totalShelfNum;
    }
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
    return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
    this.shelfDate=shelfDate;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*operatorId get 方法 */
    public Long getOperatorId(){
    return operatorId;
    }
    /*operatorId set 方法 */
    public void setOperatorId(Long  operatorId){
    this.operatorId=operatorId;
    }
    /*operator get 方法 */
    public String getOperator(){
    return operator;
    }
    /*operator set 方法 */
    public void setOperator(String  operator){
    this.operator=operator;
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

    public Long getOutDepotId() {
        return outDepotId;
    }

    public void setOutDepotId(Long outDepotId) {
        this.outDepotId = outDepotId;
    }

    public String getOutDepotName() {
        return outDepotName;
    }

    public void setOutDepotName(String outDepotName) {
        this.outDepotName = outDepotName;
    }

    public Long getSaleOrderId() {
        return saleOrderId;
    }

    public void setSaleOrderId(Long saleOrderId) {
        this.saleOrderId = saleOrderId;
    }

    public String getIsGenerate() {
        return isGenerate;
    }

    public void setIsGenerate(String isGenerate) {
        this.isGenerate = isGenerate;
    }

    public Long getSaleOutDepotId() {
        return saleOutDepotId;
    }

    public void setSaleOutDepotId(Long saleOutDepotId) {
        this.saleOutDepotId = saleOutDepotId;
    }

    public String getSaleOutDepotCode() {
        return saleOutDepotCode;
    }

    public void setSaleOutDepotCode(String saleOutDepotCode) {
        this.saleOutDepotCode = saleOutDepotCode;
    }

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
    }

    public Long getOriginalShelfId() {
        return originalShelfId;
    }

    public void setOriginalShelfId(Long originalShelfId) {
        this.originalShelfId = originalShelfId;
    }

    public String getOriginalShelfCode() {
        return originalShelfCode;
    }

    public void setOriginalShelfCode(String originalShelfCode) {
        this.originalShelfCode = originalShelfCode;
    }
}
