package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 预收账单操作日志DTO
 */
public class AdvanceBillOperateItemDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "预收账单Id", required = false)
    private Long advanceId;

    @ApiModelProperty(value = "操作人ID", required = false)
    private Long operateId;

    @ApiModelProperty(value = "操作人名称", required = false)
    private String operater;

    @ApiModelProperty(value = "操作时间", required = false)
    private Timestamp operateDate;

    @ApiModelProperty(value = "操作结果 00-通过 01-驳回 02-提交审核 03-提交作废", required = false)
    private String operateResult;
    private String operateResultLabel;

    @ApiModelProperty(value = "备注", required = false)
    private String operateRemark;

    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;

    @ApiModelProperty(value = "操作类型 0-提交 1-审核 2-提交作废 3-审核作废", required = false)
    private String operateType;
    private String operateTypeLabel;

    @ApiModelProperty(value = "操作内容", required = false)
    private String content;


    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*advanceId get 方法 */
    public Long getAdvanceId(){
        return advanceId;
    }
    /*advanceId set 方法 */
    public void setAdvanceId(Long  advanceId){
        this.advanceId=advanceId;
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
        this.operateResultLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_resultList, operateResult) ;
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
    /*operateType get 方法 */
    public String getOperateType(){
        return operateType;
    }
    /*operateType set 方法 */
    public void setOperateType(String  operateType){
        this.operateType=operateType;
        this.operateTypeLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_typeList, operateType) ;
    }

    public String getOperateResultLabel() {
        return operateResultLabel;
    }

    public void setOperateResultLabel(String operateResultLabel) {
        this.operateResultLabel = operateResultLabel;
    }

    public String getOperateTypeLabel() {
        return operateTypeLabel;
    }

    public void setOperateTypeLabel(String operateTypeLabel) {
        this.operateTypeLabel = operateTypeLabel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
