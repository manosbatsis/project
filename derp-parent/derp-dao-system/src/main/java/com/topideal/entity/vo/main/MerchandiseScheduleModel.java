package com.topideal.entity.vo.main;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 商品附表
 * @author lian_
 */
public class MerchandiseScheduleModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //申报地海关
     private String accountCode;
     //修改日期
     private Timestamp modifyDate;
     //进出口标记
     private String ieFlag;
     //商品ID
     private Long goodsId;
     //唯一标识
     private String uniques;
     //id
     private Long id;
     //业务类型
     private String businessType;
     //申报地海关
     private String customsNo;
     // 商家id
     private Long merchantId;
     
     //创建时间
     private Timestamp createDate;

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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
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
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}






}

