package com.topideal.entity.vo.order;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 运单表体
 * @author lian_
 */
public class WayBillItemModel extends PageModel implements Serializable{

    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 批次号
    */
    private String batchNo;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 税率
    */
    private Double cphTaxRate;
    /**
    * 单价
    */
    private BigDecimal price;
    /**
    * 运单号
    */
    private Long billId;
    /**
    * id
    */
    private Long id;
    /**
    * 商品编码
    */
    private String goodsCode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 税费
    */
    private BigDecimal cphTaxFee;
    /**
    * 创建时间
    */
    private Timestamp createDate;

    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
        return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
        this.batchNo=batchNo;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*num get 方法 */
    public Integer getNum(){
        return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
        this.num=num;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
        return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*cphTaxRate get 方法 */
    public Double getCphTaxRate(){
        return cphTaxRate;
    }
    /*cphTaxRate set 方法 */
    public void setCphTaxRate(Double  cphTaxRate){
        this.cphTaxRate=cphTaxRate;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
        return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
        this.price=price;
    }
    /*billId get 方法 */
    public Long getBillId(){
        return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
        this.billId=billId;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
        return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
        this.goodsCode=goodsCode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*barcode get 方法 */
    public String getBarcode(){
        return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
        this.barcode=barcode;
    }
    /*cphTaxFee get 方法 */
    public BigDecimal getCphTaxFee(){
        return cphTaxFee;
    }
    /*cphTaxFee set 方法 */
    public void setCphTaxFee(BigDecimal  cphTaxFee){
        this.cphTaxFee=cphTaxFee;
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
