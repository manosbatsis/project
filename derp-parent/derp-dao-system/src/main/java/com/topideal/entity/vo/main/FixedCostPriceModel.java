package com.topideal.entity.vo.main;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class FixedCostPriceModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 状态(0待审核, 1已审核)
    */
    private String status;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
     * 事业部编码
     */
    private String buCode;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 标准品牌ID
    */
    private Long brandParentId;
    /**
    * 标准品牌名称
    */
    private String brandParentName;
    /**
     * 商品条形码
     */
    private String barcode;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 固定成本价
    */
    private BigDecimal fixedCost;

    /**
     * 币种
     */
    private String currency;
    /**
    * 生效日期
    */
    private String effectiveDate;

    /**
     * 审核时间
     */
    private Timestamp auditDate;
    /**
     * 审核人ID
     */
    private Long auditer;
    /**
     * 审核人名称
     */
    private String auditName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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
    /*brandParentId get 方法 */
    public Long getBrandParentId(){
    return brandParentId;
    }
    /*brandParentId set 方法 */
    public void setBrandParentId(Long  brandParentId){
    this.brandParentId=brandParentId;
    }
    /*brandParentName get 方法 */
    public String getBrandParentName(){
    return brandParentName;
    }
    /*brandParentName set 方法 */
    public void setBrandParentName(String  brandParentName){
    this.brandParentName=brandParentName;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /*fixedCost get 方法 */
    public BigDecimal getFixedCost(){
    return fixedCost;
    }
    /*fixedCost set 方法 */
    public void setFixedCost(BigDecimal  fixedCost){
    this.fixedCost=fixedCost;
    }
    /*effectiveDate get 方法 */
    public String getEffectiveDate(){
    return effectiveDate;
    }
    /*effectiveDate set 方法 */
    public void setEffectiveDate(String  effectiveDate){
    this.effectiveDate=effectiveDate;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBuCode() {
        return buCode;
    }

    public void setBuCode(String buCode) {
        this.buCode = buCode;
    }

    public Timestamp getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Timestamp auditDate) {
        this.auditDate = auditDate;
    }

    public Long getAuditer() {
        return auditer;
    }

    public void setAuditer(Long auditer) {
        this.auditer = auditer;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }
}
