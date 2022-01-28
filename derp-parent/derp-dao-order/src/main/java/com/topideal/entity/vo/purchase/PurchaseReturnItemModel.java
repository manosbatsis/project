package com.topideal.entity.vo.purchase;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PurchaseReturnItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购退货ID
    */
    private Long orderId;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 采购退货数量
    */
    private Integer returnNum;
    /**
    * 采购退货单价
    */
    private BigDecimal returnPrice;
    /**
    * 采购退货总金额
    */
    private BigDecimal returnAmount;
    /**
    * 箱号
    */
    private String boxNo;
    /**
    * 子合同号
    */
    private String contractNo;
    /**
    * 品牌类型
    */
    private String brandName;
    /**
    * 备注
    */
    private String remark;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //本位币单价
    private BigDecimal tgtReturnPrice;
    //本位币总价
    private BigDecimal tgtReturnAmount;
    //申报单价
    private BigDecimal declarePrice;

    /**
     * 采购退货含税单价
     */
    private BigDecimal taxReturnPrice;
    /**
     * 采购退货含税总金额
     */
    private BigDecimal taxReturnAmount;
    /**
     * 税额
     */
    private BigDecimal tax;
    /**
     * 税率
     */
    private Double taxRate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
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
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*returnNum get 方法 */
    public Integer getReturnNum(){
    return returnNum;
    }
    /*returnNum set 方法 */
    public void setReturnNum(Integer  returnNum){
    this.returnNum=returnNum;
    }
    /*returnPrice get 方法 */
    public BigDecimal getReturnPrice(){
    return returnPrice;
    }
    /*returnPrice set 方法 */
    public void setReturnPrice(BigDecimal  returnPrice){
    this.returnPrice=returnPrice;
    }
    /*returnAmount get 方法 */
    public BigDecimal getReturnAmount(){
    return returnAmount;
    }
    /*returnAmount set 方法 */
    public void setReturnAmount(BigDecimal  returnAmount){
    this.returnAmount=returnAmount;
    }
    /*boxNo get 方法 */
    public String getBoxNo(){
    return boxNo;
    }
    /*boxNo set 方法 */
    public void setBoxNo(String  boxNo){
    this.boxNo=boxNo;
    }
    /*contractNo get 方法 */
    public String getContractNo(){
    return contractNo;
    }
    /*contractNo set 方法 */
    public void setContractNo(String  contractNo){
    this.contractNo=contractNo;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
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

    public BigDecimal getTgtReturnPrice() {
        return tgtReturnPrice;
    }

    public void setTgtReturnPrice(BigDecimal tgtReturnPrice) {
        this.tgtReturnPrice = tgtReturnPrice;
    }

    public BigDecimal getTgtReturnAmount() {
        return tgtReturnAmount;
    }

    public void setTgtReturnAmount(BigDecimal tgtReturnAmount) {
        this.tgtReturnAmount = tgtReturnAmount;
    }

    public BigDecimal getDeclarePrice() {
        return declarePrice;
    }

    public void setDeclarePrice(BigDecimal declarePrice) {
        this.declarePrice = declarePrice;
    }

    public BigDecimal getTaxReturnPrice() {
        return taxReturnPrice;
    }

    public void setTaxReturnPrice(BigDecimal taxReturnPrice) {
        this.taxReturnPrice = taxReturnPrice;
    }

    public BigDecimal getTaxReturnAmount() {
        return taxReturnAmount;
    }

    public void setTaxReturnAmount(BigDecimal taxReturnAmount) {
        this.taxReturnAmount = taxReturnAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }
}
