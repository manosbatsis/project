package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class TmallscmPurchaseOrderDetailModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购单编号
    */
    private String purchaseOrderNo;
    /**
    * 货品id
    */
    private String scItemId;
    /**
    * 货品名称
    */
    private String title;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 下单数量
    */
    private Integer quantity;
    /**
    * 实收正品数量
    */
    private Integer receivedNormalQty;
    /**
    * 实收残品数量
    */
    private Integer receivedDefectiveQty;
    /**
    * 采购价
    */
    private String priceOfYuan;
    /**
    * 币别
    */
    private String currency;
    /**
    * 采购金额
    */
    private BigDecimal purchaseAmountDec;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 账号
    */
    private String userCode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*purchaseOrderNo get 方法 */
    public String getPurchaseOrderNo(){
    return purchaseOrderNo;
    }
    /*purchaseOrderNo set 方法 */
    public void setPurchaseOrderNo(String  purchaseOrderNo){
    this.purchaseOrderNo=purchaseOrderNo;
    }
    /*scItemId get 方法 */
    public String getScItemId(){
    return scItemId;
    }
    /*scItemId set 方法 */
    public void setScItemId(String  scItemId){
    this.scItemId=scItemId;
    }
    /*title get 方法 */
    public String getTitle(){
    return title;
    }
    /*title set 方法 */
    public void setTitle(String  title){
    this.title=title;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*quantity get 方法 */
    public Integer getQuantity(){
    return quantity;
    }
    /*quantity set 方法 */
    public void setQuantity(Integer  quantity){
    this.quantity=quantity;
    }
    /*receivedNormalQty get 方法 */
    public Integer getReceivedNormalQty(){
    return receivedNormalQty;
    }
    /*receivedNormalQty set 方法 */
    public void setReceivedNormalQty(Integer  receivedNormalQty){
    this.receivedNormalQty=receivedNormalQty;
    }
    /*receivedDefectiveQty get 方法 */
    public Integer getReceivedDefectiveQty(){
    return receivedDefectiveQty;
    }
    /*receivedDefectiveQty set 方法 */
    public void setReceivedDefectiveQty(Integer  receivedDefectiveQty){
    this.receivedDefectiveQty=receivedDefectiveQty;
    }
    /*priceOfYuan get 方法 */
    public String getPriceOfYuan(){
    return priceOfYuan;
    }
    /*priceOfYuan set 方法 */
    public void setPriceOfYuan(String  priceOfYuan){
    this.priceOfYuan=priceOfYuan;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*purchaseAmountDec get 方法 */
    public BigDecimal getPurchaseAmountDec(){
    return purchaseAmountDec;
    }
    /*purchaseAmountDec set 方法 */
    public void setPurchaseAmountDec(BigDecimal  purchaseAmountDec){
    this.purchaseAmountDec=purchaseAmountDec;
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
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }






}
