package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class OperateReportLogModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "id", required = false)
    private Long id;
	@ApiModelProperty(value = "操作日志 模块 1-关账", required = false)
    private String module;
	@ApiModelProperty(value = "业务单号,(如果没有单号可用逗号拼接数据)", required = false)
    private String relCode;
	@ApiModelProperty(value = "操作人ID", required = false)
    private Long operateId;
	@ApiModelProperty(value = "操作人名称", required = false)
    private String operater;
	@ApiModelProperty(value = "操作人岗位名称", required = false)
    private String operaterDepot;
	@ApiModelProperty(value = "操作项", required = false)
    private String operateAction;
	@ApiModelProperty(value = "操作时间", required = false)
    private Timestamp operateDate;
	@ApiModelProperty(value = "操作结果", required = false)
    private String operateResult;
	@ApiModelProperty(value = "备注", required = false)
    private String operateRemark;
	@ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;
	@ApiModelProperty(value = "修改时间", required = false)
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
    /*operaterDepot get 方法 */
    public String getOperaterDepot(){
    return operaterDepot;
    }
    /*operaterDepot set 方法 */
    public void setOperaterDepot(String  operaterDepot){
    this.operaterDepot=operaterDepot;
    }
    /*operateAction get 方法 */
    public String getOperateAction(){
    return operateAction;
    }
    /*operateAction set 方法 */
    public void setOperateAction(String  operateAction){
    this.operateAction=operateAction;
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






}
