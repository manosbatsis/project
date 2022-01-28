package com.topideal.entity.dto.base;


import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;
/**
 * api密钥配置
 * @author lian_
 *
 */
public class ApiSecretConfigDTO extends PageModel implements Serializable{

	 @ApiModelProperty(value = "APP_Key")
     private String appKey;
	 @ApiModelProperty(value = "密钥")
     private String appSecret;
	 @ApiModelProperty(value = "备注")
     private String remark;
	 @ApiModelProperty(value = "id")
     private Long id;
	 @ApiModelProperty(value = "平台名称")
     private String platformName;
	 @ApiModelProperty(value = "创建时间")
     private Timestamp createDate;
	 @ApiModelProperty(value = "状态(1-启用,0-禁用)")
     private String status;
	 @ApiModelProperty(value = "statusLabel")
     private String statusLabel ;
	 @ApiModelProperty(value = "商家ID")
     private Long merchantId;
	 @ApiModelProperty(value = "卓志编码")
     private String topidealCode;
	 @ApiModelProperty(value = "商家名称")
     private String merchantName;

     /*topidealCode get 方法 */
     public String getTopidealCode(){
         return topidealCode;
     }
     /*topidealCode set 方法 */
     public void setTopidealCode(String  topidealCode){
         this.topidealCode=topidealCode;
     }
     public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	/*merchantId get 方法 */
     public Long getMerchantId(){
         return merchantId;
     }
     /*merchantId set 方法 */
     public void setMerchantId(Long  merchantId){
         this.merchantId=merchantId;
     }
     /*status get 方法 */
     public String getStatus(){
         return status;
     }
     /*status set 方法 */
     public void setStatus(String  status){
         this.status=status;
         
         if(status != null) {
        	 this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.apiSecretConfig_statusList, status);
         }
     }
    /*appKey get 方法 */
    public String getAppKey(){
        return appKey;
    }
    /*appKey set 方法 */
    public void setAppKey(String  appKey){
        this.appKey=appKey;
    }
    /*appSecret get 方法 */
    public String getAppSecret(){
        return appSecret;
    }
    /*appSecret set 方法 */
    public void setAppSecret(String  appSecret){
        this.appSecret=appSecret;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}






}

