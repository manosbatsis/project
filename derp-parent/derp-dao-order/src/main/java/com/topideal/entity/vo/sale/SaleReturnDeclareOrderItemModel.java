package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class SaleReturnDeclareOrderItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 销售退预申报单ID
    */
    private Long returnDeclareOrderId;
    /**
    * 销售退货单ID
    */
    private Long saleReturnOrderId;
    /**
    * 销售退货单编号
    */
    private String saleReturnOrderCode;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 退入商品id
    */
    private Long inGoodsId;
    /**
    * 退入商品编码
    */
    private String inGoodsCode;
    /**
    * 退入商品名称
    */
    private String inGoodsName;
    /**
    * 退入商品货号
    */
    private String inGoodsNo;
    /**
    * 退入商品条形码
    */
    private String inBarcode;
    /**
    * 退出商品id
    */
    private Long outGoodsId;
    /**
    * 退出商品编码
    */
    private String outGoodsCode;
    /**
    * 退出商品名称
    */
    private String outGoodsName;
    /**
    * 退出商品货号
    */
    private String outGoodsNo;
    /**
    * 退出商品条形码
    */
    private String outBarcode;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 退货数量
    */
    private Integer num;
    /**
    * 退货单价（不含税）
    */
    private BigDecimal price;
    /**
    * 退货总金额（不含税）
    */
    private BigDecimal amount;
    /**
    * 退货单价（含税）
    */
    private BigDecimal taxPrice;
    /**
    * 退货总金额（含税）
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
    /**
    * 品牌类型
    */
    private String brandName;
    /**
    * 毛重
    */
    private Double grossWeight;
    /**
    * 净重
    */
    private Double netWeight;
    /**
    * 总毛重
    */
    private Double grossWeightSum;
    /**
    * 总净重
    */
    private Double netWeightSum;
    /**
    * 箱号
    */
    private String boxNo;
    /**
    * 批次号
    */
    private String batchNo;
    /**
    * 序号
    */
    private Integer seq;
    /**
    * 创建时间
    */
    private Timestamp createDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*returnDeclareOrderId get 方法 */
    public Long getReturnDeclareOrderId(){
    return returnDeclareOrderId;
    }
    /*returnDeclareOrderId set 方法 */
    public void setReturnDeclareOrderId(Long  returnDeclareOrderId){
    this.returnDeclareOrderId=returnDeclareOrderId;
    }
    /*saleReturnOrderId get 方法 */
    public Long getSaleReturnOrderId(){
    return saleReturnOrderId;
    }
    /*saleReturnOrderId set 方法 */
    public void setSaleReturnOrderId(Long  saleReturnOrderId){
    this.saleReturnOrderId=saleReturnOrderId;
    }
    /*saleReturnOrderCode get 方法 */
    public String getSaleReturnOrderCode(){
    return saleReturnOrderCode;
    }
    /*saleReturnOrderCode set 方法 */
    public void setSaleReturnOrderCode(String  saleReturnOrderCode){
    this.saleReturnOrderCode=saleReturnOrderCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*inGoodsId get 方法 */
    public Long getInGoodsId(){
    return inGoodsId;
    }
    /*inGoodsId set 方法 */
    public void setInGoodsId(Long  inGoodsId){
    this.inGoodsId=inGoodsId;
    }
    /*inGoodsCode get 方法 */
    public String getInGoodsCode(){
    return inGoodsCode;
    }
    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String  inGoodsCode){
    this.inGoodsCode=inGoodsCode;
    }
    /*inGoodsName get 方法 */
    public String getInGoodsName(){
    return inGoodsName;
    }
    /*inGoodsName set 方法 */
    public void setInGoodsName(String  inGoodsName){
    this.inGoodsName=inGoodsName;
    }
    /*inGoodsNo get 方法 */
    public String getInGoodsNo(){
    return inGoodsNo;
    }
    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String  inGoodsNo){
    this.inGoodsNo=inGoodsNo;
    }
    /*inBarcode get 方法 */
    public String getInBarcode(){
    return inBarcode;
    }
    /*inBarcode set 方法 */
    public void setInBarcode(String  inBarcode){
    this.inBarcode=inBarcode;
    }
    /*outGoodsId get 方法 */
    public Long getOutGoodsId(){
    return outGoodsId;
    }
    /*outGoodsId set 方法 */
    public void setOutGoodsId(Long  outGoodsId){
    this.outGoodsId=outGoodsId;
    }
    /*outGoodsCode get 方法 */
    public String getOutGoodsCode(){
    return outGoodsCode;
    }
    /*outGoodsCode set 方法 */
    public void setOutGoodsCode(String  outGoodsCode){
    this.outGoodsCode=outGoodsCode;
    }
    /*outGoodsName get 方法 */
    public String getOutGoodsName(){
    return outGoodsName;
    }
    /*outGoodsName set 方法 */
    public void setOutGoodsName(String  outGoodsName){
    this.outGoodsName=outGoodsName;
    }
    /*outGoodsNo get 方法 */
    public String getOutGoodsNo(){
    return outGoodsNo;
    }
    /*outGoodsNo set 方法 */
    public void setOutGoodsNo(String  outGoodsNo){
    this.outGoodsNo=outGoodsNo;
    }
    /*outBarcode get 方法 */
    public String getOutBarcode(){
    return outBarcode;
    }
    /*outBarcode set 方法 */
    public void setOutBarcode(String  outBarcode){
    this.outBarcode=outBarcode;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
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
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*grossWeight get 方法 */
    public Double getGrossWeight(){
    return grossWeight;
    }
    /*grossWeight set 方法 */
    public void setGrossWeight(Double  grossWeight){
    this.grossWeight=grossWeight;
    }
    /*netWeight get 方法 */
    public Double getNetWeight(){
    return netWeight;
    }
    /*netWeight set 方法 */
    public void setNetWeight(Double  netWeight){
    this.netWeight=netWeight;
    }
    /*grossWeightSum get 方法 */
    public Double getGrossWeightSum(){
    return grossWeightSum;
    }
    /*grossWeightSum set 方法 */
    public void setGrossWeightSum(Double  grossWeightSum){
    this.grossWeightSum=grossWeightSum;
    }
    /*netWeightSum get 方法 */
    public Double getNetWeightSum(){
    return netWeightSum;
    }
    /*netWeightSum set 方法 */
    public void setNetWeightSum(Double  netWeightSum){
    this.netWeightSum=netWeightSum;
    }
    /*boxNo get 方法 */
    public String getBoxNo(){
    return boxNo;
    }
    /*boxNo set 方法 */
    public void setBoxNo(String  boxNo){
    this.boxNo=boxNo;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
    return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
    this.batchNo=batchNo;
    }
    /*seq get 方法 */
    public Integer getSeq(){
    return seq;
    }
    /*seq set 方法 */
    public void setSeq(Integer  seq){
    this.seq=seq;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }






}
