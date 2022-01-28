package com.topideal.entity.dto.platformdata;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class PlatformCategoryConfigDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    @ApiModelProperty("记录ID")
    private Long id;
    /**
    * 客户ID
    */
    @ApiModelProperty("客户ID")
    private Long customerId;
    /**
    * 客户名称
    */
    @ApiModelProperty("客户名称")
    private String customerName;
    /**
    * 商家ID
    */
    @ApiModelProperty("商家ID")
    private Long merchantId;
    /**
    * 商家名称
    */
    @ApiModelProperty("商家名称")
    private String merchantName;
    /**
    * 平台品类名称
    */
    @ApiModelProperty("平台品类名称")
    private String name;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
    private String remarks;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private Long creater;
    /**
    * 创建人用户名
    */
    @ApiModelProperty("创建人用户名")
    private String createName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
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
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*remarks get 方法 */
    public String getRemarks(){
    return remarks;
    }
    /*remarks set 方法 */
    public void setRemarks(String  remarks){
    this.remarks=remarks;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
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






}
