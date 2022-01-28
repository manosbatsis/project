package com.topideal.entity.dto.bill;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class OperateLogDTO extends PageModel implements Serializable{

    /**
    * ID
    */
	@ApiModelProperty("记录ID")
    private Long id;
    /**
    * 模块 1-采购 2-预收 3-应收
    */
	@ApiModelProperty("模块 1-采购 2-预收 3-应收")
    private String module;
	@ApiModelProperty("模块 1-采购 2-预收 3-应收")
    private String moduleLabel;
    /**
    * 业务单号
    */
	@ApiModelProperty("业务单号")
    private String relCode;
    /**
    * 操作人ID
    */
	@ApiModelProperty("操作人ID")
    private Long operateId;
    /**
    * 操作人名称
    */
	@ApiModelProperty("操作人名称")
    private String operater;
	/**
    * 操作人岗位名称
    */
	@ApiModelProperty("操作人岗位名称")
    private String operaterDepot;
    /**
    * 操作时间
    */
	@ApiModelProperty("操作时间")
    private Timestamp operateDate;
	@ApiModelProperty("操作时间字符串")
    private String operateDateStr;
    /**
    * 操作结果
    */
	@ApiModelProperty("操作结果")
    private String operateResult;
    /**
    * 备注
    */
	@ApiModelProperty("备注")
    private String operateRemark;
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
	
	/**
    * 操作项
    */
	@ApiModelProperty("操作项")
    private String operateAction;

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
    this.moduleLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.operateLog_ModuleList, module) ;
    }
    /*relCode get 方法 */
    public String getRelCode(){
    return relCode;
    }
    /*relCode set 方法 */
    public void setRelCode(String  relCode){
    this.relCode=relCode;
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
    /*operateDate get 方法 */
    public Timestamp getOperateDate(){
    return operateDate;
    }
    /*operateDate set 方法 */
    public void setOperateDate(Timestamp  operateDate){
    this.operateDate=operateDate;
    }
    /*operateResult get 方法 */
    public String getOperateResult(){
    return operateResult;
    }
    /*operateResult set 方法 */
    public void setOperateResult(String  operateResult){
    this.operateResult=operateResult;
    }
    /*operateRemark get 方法 */
    public String getOperateRemark(){
    return operateRemark;
    }
    /*operateRemark set 方法 */
    public void setOperateRemark(String  operateRemark){
    this.operateRemark=operateRemark;
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
	public String getModuleLabel() {
		return moduleLabel;
	}
	public void setModuleLabel(String moduleLabel) {
		this.moduleLabel = moduleLabel;
	}
	public String getOperaterDepot() {
		return operaterDepot;
	}
	public void setOperaterDepot(String operaterDepot) {
		this.operaterDepot = operaterDepot;
	}
	public String getOperateDateStr() {
		return operateDateStr;
	}
	public void setOperateDateStr(String operateDateStr) {
		this.operateDateStr = operateDateStr;
	}
	public String getOperateAction() {
		return operateAction;
	}
	public void setOperateAction(String operateAction) {
		this.operateAction = operateAction;
	}

}
