package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class JdPurchaseOrderDetailModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购单号
    */
    private String orderId;
    /**
    * 商品编号
    */
    private String goodsNo;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 配送机构
    */
    private String delivery;
    /**
    * 库房
    */
    private String storage;
    /**
    * 采购数量
    */
    private Integer purchaseNum;
    /**
    * 实收数量
    */
    private Integer actualNum;
    /**
    * 波次数量
    */
    private Integer wavesNum;
    /**
    * 重码UPC
    */
    private String codeUpc;
    /**
    * UPC码
    */
    private String upc;
    /**
    * 是否序列号管理
    */
    private String isSerialNum;
    /**
    * 贴码建议
    */
    private String codeSuggestion;
    /**
    * 京东贴码数量
    */
    private Integer codeNum;
    /**
    * 采购价
    */
    private BigDecimal purchasePrice;
    /**
    * 不含税价
    */
    private BigDecimal excludedPrice;
    /**
    * 税率
    */
    private BigDecimal taxRate;
    /**
    * 税额
    */
    private BigDecimal taxAmount;
    /**
    * 金额小计
    */
    private BigDecimal totalAmount;
    /**
    * 收货异常信息
    */
    private String abnormalInfo;
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
    /*orderId get 方法 */
    public String getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(String  orderId){
    this.orderId=orderId;
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
    /*delivery get 方法 */
    public String getDelivery(){
    return delivery;
    }
    /*delivery set 方法 */
    public void setDelivery(String  delivery){
    this.delivery=delivery;
    }
    /*storage get 方法 */
    public String getStorage(){
    return storage;
    }
    /*storage set 方法 */
    public void setStorage(String  storage){
    this.storage=storage;
    }
    /*purchaseNum get 方法 */
    public Integer getPurchaseNum(){
    return purchaseNum;
    }
    /*purchaseNum set 方法 */
    public void setPurchaseNum(Integer  purchaseNum){
    this.purchaseNum=purchaseNum;
    }
    /*actualNum get 方法 */
    public Integer getActualNum(){
    return actualNum;
    }
    /*actualNum set 方法 */
    public void setActualNum(Integer  actualNum){
    this.actualNum=actualNum;
    }
    /*wavesNum get 方法 */
    public Integer getWavesNum(){
    return wavesNum;
    }
    /*wavesNum set 方法 */
    public void setWavesNum(Integer  wavesNum){
    this.wavesNum=wavesNum;
    }
    /*codeUpc get 方法 */
    public String getCodeUpc(){
    return codeUpc;
    }
    /*codeUpc set 方法 */
    public void setCodeUpc(String  codeUpc){
    this.codeUpc=codeUpc;
    }
    /*upc get 方法 */
    public String getUpc(){
    return upc;
    }
    /*upc set 方法 */
    public void setUpc(String  upc){
    this.upc=upc;
    }
    /*isSerialNum get 方法 */
    public String getIsSerialNum(){
    return isSerialNum;
    }
    /*isSerialNum set 方法 */
    public void setIsSerialNum(String  isSerialNum){
    this.isSerialNum=isSerialNum;
    }
    /*codeSuggestion get 方法 */
    public String getCodeSuggestion(){
    return codeSuggestion;
    }
    /*codeSuggestion set 方法 */
    public void setCodeSuggestion(String  codeSuggestion){
    this.codeSuggestion=codeSuggestion;
    }
    /*codeNum get 方法 */
    public Integer getCodeNum(){
    return codeNum;
    }
    /*codeNum set 方法 */
    public void setCodeNum(Integer  codeNum){
    this.codeNum=codeNum;
    }
    /*purchasePrice get 方法 */
    public BigDecimal getPurchasePrice(){
    return purchasePrice;
    }
    /*purchasePrice set 方法 */
    public void setPurchasePrice(BigDecimal  purchasePrice){
    this.purchasePrice=purchasePrice;
    }
    /*excludedPrice get 方法 */
    public BigDecimal getExcludedPrice(){
    return excludedPrice;
    }
    /*excludedPrice set 方法 */
    public void setExcludedPrice(BigDecimal  excludedPrice){
    this.excludedPrice=excludedPrice;
    }
    /*taxRate get 方法 */
    public BigDecimal getTaxRate(){
    return taxRate;
    }
    /*taxRate set 方法 */
    public void setTaxRate(BigDecimal  taxRate){
    this.taxRate=taxRate;
    }
    /*taxAmount get 方法 */
    public BigDecimal getTaxAmount(){
    return taxAmount;
    }
    /*taxAmount set 方法 */
    public void setTaxAmount(BigDecimal  taxAmount){
    this.taxAmount=taxAmount;
    }
    /*totalAmount get 方法 */
    public BigDecimal getTotalAmount(){
    return totalAmount;
    }
    /*totalAmount set 方法 */
    public void setTotalAmount(BigDecimal  totalAmount){
    this.totalAmount=totalAmount;
    }
    /*abnormalInfo get 方法 */
    public String getAbnormalInfo(){
    return abnormalInfo;
    }
    /*abnormalInfo set 方法 */
    public void setAbnormalInfo(String  abnormalInfo){
    this.abnormalInfo=abnormalInfo;
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
