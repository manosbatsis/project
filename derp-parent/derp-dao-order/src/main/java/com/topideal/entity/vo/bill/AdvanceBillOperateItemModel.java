package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class AdvanceBillOperateItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 应收账单Id
    */
    private Long advanceId;
    /**
    * 操作人ID
    */
    private Long operateId;
    /**
    * 操作人名称
    */
    private String operater;
    /**
    * 操作时间
    */
    private Timestamp operateDate;
    /**
    * 操作结果 00-审批通过 01-驳回 02-提交审核 03-提交作废
    */
    private String operateResult;
    /**
    * 备注
    */
    private String operateRemark;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 操作类型 0-提交 1-审核 2-提交作废 3-审核作废
    */
    private String operateType;

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
    }






}
