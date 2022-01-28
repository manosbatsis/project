package com.topideal.entity.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class OperateSysLogDTO implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 模块 1-外仓备案商品管理
     */
    @ApiModelProperty(value = "模块 1-外仓备案商品管理")
    private String module;
    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Long relId;
    /**
     * 操作人ID
     */
    @ApiModelProperty(value = "操作人ID")
    private Long operateId;
    /**
     * 操作人名称
     */
    @ApiModelProperty(value = "操作人名称")
    private String operater;
    /**
     * 操作项
     */
    @ApiModelProperty(value = "操作项")
    private String operateAction;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String operateRemark;
    /**
     * 操作结果
     */
    @ApiModelProperty(value = "备注")
    private String operateResult;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Timestamp operateDate;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*module get 方法 */
    public String getModule(){
        return module;
    }
    /*module set 方法 */
    public void setModule(String  module){
        this.module=module;
    }
    /*relId get 方法 */
    public Long getRelId(){
        return relId;
    }
    /*relId set 方法 */
    public void setRelId(Long  relId){
        this.relId=relId;
    }
    /*operateId get 方法 */
    public Long getOperateId(){
        return operateId;
    }
    /*operateId set 方法 */
    public void setOperateId(Long  operateId){
        this.operateId=operateId;
    }
    /*operater get 方法 */
    public String getOperater(){
        return operater;
    }
    /*operater set 方法 */
    public void setOperater(String  operater){
        this.operater=operater;
    }
    /*operateAction get 方法 */
    public String getOperateAction(){
        return operateAction;
    }
    /*operateAction set 方法 */
    public void setOperateAction(String  operateAction){
        this.operateAction=operateAction;
    }
    /*operateRemark get 方法 */
    public String getOperateRemark(){
        return operateRemark;
    }
    /*operateRemark set 方法 */
    public void setOperateRemark(String  operateRemark){
        this.operateRemark=operateRemark;
    }
    /*operateResult get 方法 */
    public String getOperateResult(){
        return operateResult;
    }
    /*operateResult set 方法 */
    public void setOperateResult(String  operateResult){
        this.operateResult=operateResult;
    }
    /*operateDate get 方法 */
    public Timestamp getOperateDate(){
        return operateDate;
    }
    /*operateDate set 方法 */
    public void setOperateDate(Timestamp  operateDate){
        this.operateDate=operateDate;
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
