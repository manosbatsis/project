package com.topideal.mongo.entity;

/**
 * 商品附表
 * @author lian_
 */
public class MerchandiseScheduleMongo{

	//id 主键ID
    private Long scheduleId;
    
     //商品货号
     private String goodsNo;
     //申报地海关
     private String accountCode;

     //进出口标记
     private String ieFlag;
     //商品ID
     private Long goodsId;
     //唯一标识
     private String uniques;
     
     //业务类型
     private String businessType;
     //申报地海关
     private String customsNo;
     
     private Long merchantId;

    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*accountCode get 方法 */
    public String getAccountCode(){
        return accountCode;
    }
    /*accountCode set 方法 */
    public void setAccountCode(String  accountCode){
        this.accountCode=accountCode;
    }

    /*ieFlag get 方法 */
    public String getIeFlag(){
        return ieFlag;
    }
    /*ieFlag set 方法 */
    public void setIeFlag(String  ieFlag){
        this.ieFlag=ieFlag;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*uniques get 方法 */
    public String getUniques(){
        return uniques;
    }
    /*uniques set 方法 */
    public void setUniques(String  uniques){
        this.uniques=uniques;
    }

    public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	/*businessType get 方法 */
    public String getBusinessType(){
        return businessType;
    }
    /*businessType set 方法 */
    public void setBusinessType(String  businessType){
        this.businessType=businessType;
    }
    /*customsNo get 方法 */
    public String getCustomsNo(){
        return customsNo;
    }
    /*customsNo set 方法 */
    public void setCustomsNo(String  customsNo){
        this.customsNo=customsNo;
    }
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
   
}

