package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 运单表体
 * @author lian_
 *
 */
public class WayBillItemModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //税率
     private Double cphTaxRate;
     //商品ID
     private Long goodsId;
     //单价
     private BigDecimal price;
     //运单号
     private Long billId;
     //数量
     private Integer num;
     //id
     private Long id;
     //商品名称
     private String goodsName;
     //税费
     private BigDecimal cphTaxFee;
     //批次号
     private String batchNo;
   //生产日期
     private Date productionDate;
   //失效日期
     private Date overdueDate;
     //条形码
     private String barcode;
     //商品编码
     private String goodsCode;

     /**
      * 修改时间
      */
      private Timestamp modifyDate;
      /**
       * 创建时间
       */
       private Timestamp createDate;


    //拓展字段
    /**
     * 事业部id
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;

     public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	/*barcode get 方法 */
     public String getBarcode(){
         return barcode;
     }
     /*barcode set 方法 */
     public void setBarcode(String  barcode){
         this.barcode=barcode;
     }
     /*overdueDate get 方法 */
     public Date getOverdueDate(){
         return overdueDate;
     }
     /*overdueDate set 方法 */
     public void setOverdueDate(Date  overdueDate){
         this.overdueDate=overdueDate;
     }
     /*productionDate get 方法 */
     public Date getProductionDate(){
         return productionDate;
     }
     /*productionDate set 方法 */
     public void setProductionDate(Date  productionDate){
         this.productionDate=productionDate;
     }
     /*batchNo get 方法 */
    public String getBatchNo() {
		return batchNo;
	}
    /*batchNo set 方法 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	/*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*cphTaxRate get 方法 */
    public Double getCphTaxRate(){
        return cphTaxRate;
    }
    /*cphTaxRate set 方法 */
    public void setCphTaxRate(Double  cphTaxRate){
        this.cphTaxRate=cphTaxRate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
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
    /*num get 方法 */
    public Integer getNum(){
        return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
        this.num=num;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*cphTaxFee get 方法 */
    public BigDecimal getCphTaxFee(){
        return cphTaxFee;
    }
    /*cphTaxFee set 方法 */
    public void setCphTaxFee(BigDecimal  cphTaxFee){
        this.cphTaxFee=cphTaxFee;
    }
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

}

