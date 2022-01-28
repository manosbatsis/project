package com.topideal.entity.dto;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class VipDifferenceAnalysisDTO extends PageModel implements Serializable{

    /**
    * 
    */
    private Long id;
    /**
    * 月份
    */
    private String month;
    /**
    * 平台
    */
    private String platform;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名
    */
    private String merchantName;
    /**
    * po号
    */
    private String poNo;
    /**
    * 爬虫账单号
    */
    private String crawlerNo;
    /**
    * 唯品SKU
    */
    private String crawlerGoodsNo;
    /**
    * 00-销售明细、01-库存买断、02库存盘亏、03报废、04库存盘盈、05国检抽样、06唯品红冲
    */
    private String billType;
    private String billTypeLabel;
    /**
    * 差异原因
    */
    private String differenceResult;
    /**
    * 差异数量
    */
    private Integer differenceAccount;
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*platform get 方法 */
    public String getPlatform(){
    return platform;
    }
    /*platform set 方法 */
    public void setPlatform(String  platform){
    this.platform=platform;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*crawlerNo get 方法 */
    public String getCrawlerNo(){
    return crawlerNo;
    }
    /*crawlerNo set 方法 */
    public void setCrawlerNo(String  crawlerNo){
    this.crawlerNo=crawlerNo;
    }
    /*crawlerGoodsNo get 方法 */
    public String getCrawlerGoodsNo(){
    return crawlerGoodsNo;
    }
    /*crawlerGoodsNo set 方法 */
    public void setCrawlerGoodsNo(String  crawlerGoodsNo){
    this.crawlerGoodsNo=crawlerGoodsNo;
    }
    /*billType get 方法 */
    public String getBillType(){
    return billType;
    }
    /*billType set 方法 */
    public void setBillType(String  billType){
    this.billType=billType;
    this.billTypeLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.vipAutoVeri_BillTypeList, billType);
    }
    /*differenceResult get 方法 */
    public String getDifferenceResult(){
    return differenceResult;
    }
    /*differenceResult set 方法 */
    public void setDifferenceResult(String  differenceResult){
    this.differenceResult=differenceResult;
    }
    /*differenceAccount get 方法 */
    public Integer getDifferenceAccount(){
    return differenceAccount;
    }
    /*differenceAccount set 方法 */
    public void setDifferenceAccount(Integer  differenceAccount){
    this.differenceAccount=differenceAccount;
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
	public String getBillTypeLabel() {
		return billTypeLabel;
	}
	public void setBillTypeLabel(String billTypeLabel) {
		this.billTypeLabel = billTypeLabel;
	}






}
