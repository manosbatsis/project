package com.topideal.entity.dto.common;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class FileTempCustomerRelDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value="记录ID", required=false)
    private Long id;
    /**
    * 模版id
    */
	@ApiModelProperty(value="模版id", required=false)
    private Long fileId;
    /**
    * 客户id
    */
	@ApiModelProperty(value="客户id", required=false)
    private Long customerId;
    /**
    * 客户名称
    */
	@ApiModelProperty(value="客户名称", required=false)
    private String customerName;
    /**
    * 客户编码
    */
	@ApiModelProperty(value="客户编码", required=false)
    private String customerCode;
    /**
    * 创建时间
    */
	@ApiModelProperty(value="创建时间", required=false)
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value="修改时间", required=false)
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*fileId get 方法 */
    public Long getFileId(){
    return fileId;
    }
    /*fileId set 方法 */
    public void setFileId(Long  fileId){
    this.fileId=fileId;
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
    /*customerCode get 方法 */
    public String getCustomerCode(){
    return customerCode;
    }
    /*customerCode set 方法 */
    public void setCustomerCode(String  customerCode){
    this.customerCode=customerCode;
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
