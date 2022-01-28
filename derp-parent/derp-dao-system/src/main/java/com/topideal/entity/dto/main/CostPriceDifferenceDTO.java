package com.topideal.entity.dto.main;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class CostPriceDifferenceDTO extends PageModel implements Serializable{

    /**
    * id
    */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
    * 公司ID
    */
    @ApiModelProperty(value = "公司ID")
    private Long merchantId;
    /**
    * 公司名称
    */
    @ApiModelProperty(value = "公司名称")
    private String merchantName;
    /**
    * 事业部ID
    */
    @ApiModelProperty(value = "事业部ID")
    private Long buId;
    /**
    * 事业部名称
    */
    @ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
    * 商品条形码
    */
    @ApiModelProperty(value = "商品条形码")
    private String barcode;
    /**
    * 商品名称
    */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
    * 库位类型id
    */
    @ApiModelProperty(value = "库位类型id")
    private Long stockLocationTypeId;
    /**
    * 库位类型
    */
    @ApiModelProperty(value = "库位类型")
    private String stockLocationTypeName;
    /**
     * 币种
     */
    @ApiModelProperty(value = "币种")
    private String currency;
    /**
    * 固定成本价
    */
    @ApiModelProperty(value = "固定成本价")
    private BigDecimal fixedCost;
    /**
    * 采购价盘
    */
    @ApiModelProperty(value = "采购价盘")
    private BigDecimal purchasePrice;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "用户关联的事业部集合", hidden = true)
    private List<Long> buList;

    @ApiModelProperty(value = "勾选的单据id, 多个以英文逗号隔开")
    private String ids;

    //成本价差
    private String agio;
    //固定成本价
    private String fixedCostStr;
    //采购价盘
    private String purchasePriceStr;

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
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*stockLocationTypeId get 方法 */
    public Long getStockLocationTypeId(){
    return stockLocationTypeId;
    }
    /*stockLocationTypeId set 方法 */
    public void setStockLocationTypeId(Long  stockLocationTypeId){
    this.stockLocationTypeId=stockLocationTypeId;
    }
    /*stockLocationTypeName get 方法 */
    public String getStockLocationTypeName(){
    return stockLocationTypeName;
    }
    /*stockLocationTypeName set 方法 */
    public void setStockLocationTypeName(String  stockLocationTypeName){
    this.stockLocationTypeName=stockLocationTypeName;
    }
    /*fixedCost get 方法 */
    public BigDecimal getFixedCost(){
    return fixedCost;
    }
    /*fixedCost set 方法 */
    public void setFixedCost(BigDecimal  fixedCost){
    this.fixedCost=fixedCost;
    }
    /*purchasePrice get 方法 */
    public BigDecimal getPurchasePrice(){
    return purchasePrice;
    }
    /*purchasePrice set 方法 */
    public void setPurchasePrice(BigDecimal  purchasePrice){
    this.purchasePrice=purchasePrice;
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

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getAgio() {
        return agio;
    }

    public void setAgio(String agio) {
        this.agio = agio;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFixedCostStr() {
        return fixedCostStr;
    }

    public void setFixedCostStr(String fixedCostStr) {
        this.fixedCostStr = fixedCostStr;
    }

    public String getPurchasePriceStr() {
        return purchasePriceStr;
    }

    public void setPurchasePriceStr(String purchasePriceStr) {
        this.purchasePriceStr = purchasePriceStr;
    }
}
