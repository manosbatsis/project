package com.topideal.entity.vo.base;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 对外接口配置表
 * @author lian_
 */
public class ApiExternalConfigModel extends PageModel implements Serializable{

    /**
    * 秘钥
    */
    private String appSecret;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 备注
    */
    private String remark;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 卓志编码
    */
    private String topidealCode;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * app_key
    */
    private String appKey;
    /**
    * id
    */
    private Long id;
    /**
    * 平台名称
    */
    private String platformName;
    /**
    * 平台编码
    */
    private String platformCode;
    /**
    * 状态状态(1使用中,0已禁用)
    */
    private String status;
    /**
    * 创建时间
    */
    private Timestamp createDate;

    /*appSecret get 方法 */
    public String getAppSecret(){
        return appSecret;
    }
    /*appSecret set 方法 */
    public void setAppSecret(String  appSecret){
        this.appSecret=appSecret;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*topidealCode get 方法 */
    public String getTopidealCode(){
        return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
        this.topidealCode=topidealCode;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*appKey get 方法 */
    public String getAppKey(){
        return appKey;
    }
    /*appKey set 方法 */
    public void setAppKey(String  appKey){
        this.appKey=appKey;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*platformName get 方法 */
    public String getPlatformName(){
        return platformName;
    }
    /*platformName set 方法 */
    public void setPlatformName(String  platformName){
        this.platformName=platformName;
    }
    /*platformCode get 方法 */
    public String getPlatformCode(){
        return platformCode;
    }
    /*platformCode set 方法 */
    public void setPlatformCode(String  platformCode){
        this.platformCode=platformCode;
    }
    /*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
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