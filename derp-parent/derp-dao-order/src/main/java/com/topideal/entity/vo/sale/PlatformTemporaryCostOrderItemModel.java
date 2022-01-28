package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PlatformTemporaryCostOrderItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 暂估费用单ID
    */
    private Long platformCostId;
    /**
    * 电商订单号
    */
    private String orderCode;
    /**
    * 外部交易单号
    */
    private String externalCode;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 母品牌id
    */
    private Long parentBrandId;
    /**
    * 母品牌编码
    */
    private String parentBrandCode;
    /**
    * 母品牌
    */
    private String parentBrandName;
    /**
    * 平台费项id
    */
    private Long platformSettlementId;
    /**
    * 平台费项名称
    */
    private String platformSettlementName;
    /**
    * 结算金额
    */
    private BigDecimal amount;
    /**
    * 费项比例
    */
    private Double ratio;
    /**
    * 费项金额
    */
    private BigDecimal settlementAmount;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 标准品牌Id
     */
    private Long brandParentId;
    /**
     * 标准品牌名称
     */
    private String brandParentName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }

    public Long getPlatformCostId() {
        return platformCostId;
    }

    public void setPlatformCostId(Long platformCostId) {
        this.platformCostId = platformCostId;
    }

    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
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

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getParentBrandCode() {
        return parentBrandCode;
    }

    public void setParentBrandCode(String parentBrandCode) {
        this.parentBrandCode = parentBrandCode;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    /*platformSettlementId get 方法 */
    public Long getPlatformSettlementId(){
    return platformSettlementId;
    }
    /*platformSettlementId set 方法 */
    public void setPlatformSettlementId(Long  platformSettlementId){
    this.platformSettlementId=platformSettlementId;
    }
    /*platformSettlementName get 方法 */
    public String getPlatformSettlementName(){
    return platformSettlementName;
    }
    /*platformSettlementName set 方法 */
    public void setPlatformSettlementName(String  platformSettlementName){
    this.platformSettlementName=platformSettlementName;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*ratio get 方法 */
    public Double getRatio(){
    return ratio;
    }
    /*ratio set 方法 */
    public void setRatio(Double  ratio){
    this.ratio=ratio;
    }
    /*settlementAmount get 方法 */
    public BigDecimal getSettlementAmount(){
    return settlementAmount;
    }
    /*settlementAmount set 方法 */
    public void setSettlementAmount(BigDecimal  settlementAmount){
    this.settlementAmount=settlementAmount;
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

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public Long getBrandParentId() {
        return brandParentId;
    }

    public void setBrandParentId(Long brandParentId) {
        this.brandParentId = brandParentId;
    }

    public String getBrandParentName() {
        return brandParentName;
    }

    public void setBrandParentName(String brandParentName) {
        this.brandParentName = brandParentName;
    }
}
