package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;

public class BillOutinDepotItemModel extends PageModel implements Serializable{

    /**
    * id主键
    */
    private Long id;
    /**
    * 出入库单id
    */
    private Long outinDepotId;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 平台sku
    */
    private String platformSku;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * po号
    */
    private String poNo;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 结算单价（不含税）
    */
    private BigDecimal price;
    /**
    * 结算金额（不含税）
    */
    private BigDecimal amount;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 原货号
    */
    private String originalGoodsNo;
    /**
    * 原货号id
    */
    private Long originalGoodsId;
    /**
    * 爬虫映射量
    */
    private Integer contrastNum;
    /**
    * 批次号
    */
    private String batchNo;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 结算单价（含税）
    */
    private BigDecimal taxPrice;
    /**
    * 结算金额（含税）
    */
    private BigDecimal taxAmount;
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
    /*outinDepotId get 方法 */
    public Long getOutinDepotId(){
    return outinDepotId;
    }
    /*outinDepotId set 方法 */
    public void setOutinDepotId(Long  outinDepotId){
    this.outinDepotId=outinDepotId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*platformSku get 方法 */
    public String getPlatformSku(){
    return platformSku;
    }
    /*platformSku set 方法 */
    public void setPlatformSku(String  platformSku){
    this.platformSku=platformSku;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
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
    /*originalGoodsNo get 方法 */
    public String getOriginalGoodsNo(){
    return originalGoodsNo;
    }
    /*originalGoodsNo set 方法 */
    public void setOriginalGoodsNo(String  originalGoodsNo){
    this.originalGoodsNo=originalGoodsNo;
    }
    /*originalGoodsId get 方法 */
    public Long getOriginalGoodsId(){
    return originalGoodsId;
    }
    /*originalGoodsId set 方法 */
    public void setOriginalGoodsId(Long  originalGoodsId){
    this.originalGoodsId=originalGoodsId;
    }
    /*contrastNum get 方法 */
    public Integer getContrastNum(){
    return contrastNum;
    }
    /*contrastNum set 方法 */
    public void setContrastNum(Integer  contrastNum){
    this.contrastNum=contrastNum;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
    return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
    this.batchNo=batchNo;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
    return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
    this.productionDate=productionDate;
    }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
    return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
    this.overdueDate=overdueDate;
    }
    /*taxPrice get 方法 */
    public BigDecimal getTaxPrice(){
    return taxPrice;
    }
    /*taxPrice set 方法 */
    public void setTaxPrice(BigDecimal  taxPrice){
    this.taxPrice=taxPrice;
    }
    /*taxAmount get 方法 */
    public BigDecimal getTaxAmount(){
    return taxAmount;
    }
    /*taxAmount set 方法 */
    public void setTaxAmount(BigDecimal  taxAmount){
    this.taxAmount=taxAmount;
    }
    /*tax get 方法 */
    public BigDecimal getTax(){
    return tax;
    }
    /*tax set 方法 */
    public void setTax(BigDecimal  tax){
    this.tax=tax;
    }
    /*taxRate get 方法 */
    public Double getTaxRate(){
    return taxRate;
    }
    /*taxRate set 方法 */
    public void setTaxRate(Double  taxRate){
    this.taxRate=taxRate;
    }






}
