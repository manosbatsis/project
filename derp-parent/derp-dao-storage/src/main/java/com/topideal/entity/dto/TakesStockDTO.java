package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 盘点指令DTO
 */
@ApiModel
public class TakesStockDTO extends PageModel implements Serializable{

	 @ApiModelProperty(value = "盘点仓库名称")
     private String depotName;
     @ApiModelProperty(value = "创建人id")
     private Long createUserId;
     @ApiModelProperty(value = "盘点单号")
     private String code;
     @ApiModelProperty(value = "提交人名称")
     private String submitUsername;
     @ApiModelProperty(value = "盘点仓库id")
     private Long depotId;
     @ApiModelProperty(value = "备注")
     private String remark;
     @ApiModelProperty(value = "商家名称")
     private String merchantName;
     @ApiModelProperty(value = "提交人id")
     private Long submitUserId;
     @ApiModelProperty(value = "提交时间")
     private Timestamp submitTime;
     @ApiModelProperty(value = "创建人名称")
     private String createUsername;
     @ApiModelProperty(value = "商家id")
     private Long merchantId;
     @ApiModelProperty(value = "创建时间")
     private Timestamp createTime;
     @ApiModelProperty(value = "服务类型")
     private String serverType;
     @ApiModelProperty(value = "服务类型（中文）")
     private String serverTypeLabel;
     @ApiModelProperty(value = "业务场景")
     private String model;
     @ApiModelProperty(value = "业务场景（中文）")
     private String modelLabel;
     @ApiModelProperty(value = "id")
     private Long id;
     @ApiModelProperty(value = "状态")
     private String status;
     @ApiModelProperty(value = "状态（中文）")
     private String statusLabel;

    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*createUserId get 方法 */
    public Long getCreateUserId(){
        return createUserId;
    }
    /*createUserId set 方法 */
    public void setCreateUserId(Long  createUserId){
        this.createUserId=createUserId;
    }
    /*code get 方法 */
    public String getCode(){
        return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
        this.code=code;
    }
    /*submitUsername get 方法 */
    public String getSubmitUsername(){
        return submitUsername;
    }
    /*submitUsername set 方法 */
    public void setSubmitUsername(String  submitUsername){
        this.submitUsername=submitUsername;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
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
    /*submitUserId get 方法 */
    public Long getSubmitUserId(){
        return submitUserId;
    }
    /*submitUserId set 方法 */
    public void setSubmitUserId(Long  submitUserId){
        this.submitUserId=submitUserId;
    }
    /*submitTime get 方法 */
    public Timestamp getSubmitTime(){
        return submitTime;
    }
    /*submitTime set 方法 */
    public void setSubmitTime(Timestamp  submitTime){
        this.submitTime=submitTime;
    }
    /*createUsername get 方法 */
    public String getCreateUsername(){
        return createUsername;
    }
    /*createUsername set 方法 */
    public void setCreateUsername(String  createUsername){
        this.createUsername=createUsername;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*createTime get 方法 */
    public Timestamp getCreateTime(){
        return createTime;
    }
    /*createTime set 方法 */
    public void setCreateTime(Timestamp  createTime){
        this.createTime=createTime;
    }
    /*serverType get 方法 */
    public String getServerType(){
        return serverType;
    }
    /*serverType set 方法 */
    public void setServerType(String  serverType){
        this.serverType=serverType;
        if (StringUtils.isNotBlank(serverType)) {
            this.serverTypeLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStock_serverTypeList, serverType);
        }
    }
    /*model get 方法 */
    public String getModel(){
        return model;
    }
    /*model set 方法 */
    public void setModel(String  model){
        this.model=model;
        if (StringUtils.isNotBlank(model)) {
            this.modelLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStock_modelList, model);
        }
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
        if (StringUtils.isNotBlank(status)) {
            this.statusLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStock_statusList, status);
        }
    }

    public String getServerTypeLabel() {
        return serverTypeLabel;
    }

    public String getModelLabel() {
        return modelLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

}

