package com.topideal.entity.vo.automatic;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BusinessFinanceAutomaticVerificationItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 业财自核表id
    */
    private Long businessFinanceId;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
     * 事业部id
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;
    /**
    * 归属月份 YYYY-MM
    */
    private String month;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 事业部业务进销存
    */
    private Integer buInventorySummaryEndNum;
    /**
    * 事业部财务进销存
    */
    private Integer buFinanceSummaryEndNum;
    /**
    * 事业部财务期末结转量
    */
    private Integer buFinanceSummaryNum;
    /**
    * 累计销售在途量
    */
    private Integer addSaleNoshelfNum;
    /**
    * 累计调拨在途量
    */
    private Integer addTransferNoshelfNum;
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
    /*businessFinanceId get 方法 */
    public Long getBusinessFinanceId(){
    return businessFinanceId;
    }
    /*businessFinanceId set 方法 */
    public void setBusinessFinanceId(Long  businessFinanceId){
    this.businessFinanceId=businessFinanceId;
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*buInventorySummaryEndNum get 方法 */
    public Integer getBuInventorySummaryEndNum(){
    return buInventorySummaryEndNum;
    }
    /*buInventorySummaryEndNum set 方法 */
    public void setBuInventorySummaryEndNum(Integer  buInventorySummaryEndNum){
    this.buInventorySummaryEndNum=buInventorySummaryEndNum;
    }
    /*buFinanceSummaryEndNum get 方法 */
    public Integer getBuFinanceSummaryEndNum(){
    return buFinanceSummaryEndNum;
    }
    /*buFinanceSummaryEndNum set 方法 */
    public void setBuFinanceSummaryEndNum(Integer  buFinanceSummaryEndNum){
    this.buFinanceSummaryEndNum=buFinanceSummaryEndNum;
    }
    /*buFinanceSummaryNum get 方法 */
    public Integer getBuFinanceSummaryNum(){
    return buFinanceSummaryNum;
    }
    /*buFinanceSummaryNum set 方法 */
    public void setBuFinanceSummaryNum(Integer  buFinanceSummaryNum){
    this.buFinanceSummaryNum=buFinanceSummaryNum;
    }
    /*addSaleNoshelfNum get 方法 */
    public Integer getAddSaleNoshelfNum(){
    return addSaleNoshelfNum;
    }
    /*addSaleNoshelfNum set 方法 */
    public void setAddSaleNoshelfNum(Integer  addSaleNoshelfNum){
    this.addSaleNoshelfNum=addSaleNoshelfNum;
    }
    /*addTransferNoshelfNum get 方法 */
    public Integer getAddTransferNoshelfNum(){
    return addTransferNoshelfNum;
    }
    /*addTransferNoshelfNum set 方法 */
    public void setAddTransferNoshelfNum(Integer  addTransferNoshelfNum){
    this.addTransferNoshelfNum=addTransferNoshelfNum;
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
