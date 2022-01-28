package com.topideal.entity.vo.bill;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class ReceiveBillOperateModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 应收账单Id
    */
    private Long billId;
    /**
    * 操作人ID
    */
    private Long operateId;
    /**
    * 操作人名称
    */
    private String operator;
    /**
    * 操作时间
    */
    private Timestamp operateDate;
    /**
    * 操作节点 0-提交 1-审核通过 2-审核驳回 3-作废 4-作废审核通过 5—作废审核驳回 6-开票 7-发票签章 8-核销
    */
    private String operateNode;
    private String operateNodeLabel;
    /**
    * 审批备注
    */
    private String remark;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
    }
    /*operateId get 方法 */
    public Long getOperateId(){
    return operateId;
    }
    /*operateId set 方法 */
    public void setOperateId(Long  operateId){
    this.operateId=operateId;
    }
    /*operator get 方法 */
    public String getOperator(){
    return operator;
    }
    /*operator set 方法 */
    public void setOperator(String  operator){
    this.operator=operator;
    }
    /*operateDate get 方法 */
    public Timestamp getOperateDate(){
    return operateDate;
    }
    /*operateDate set 方法 */
    public void setOperateDate(Timestamp  operateDate){
    this.operateDate=operateDate;
    }
    /*operateNode get 方法 */
    public String getOperateNode(){
    return operateNode;
    }
    /*operateNode set 方法 */
    public void setOperateNode(String  operateNode){
    this.operateNode=operateNode;
    this.operateNodeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillOperate_operateNodeList, operateNode);
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
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

    public String getOperateNodeLabel() {
        return operateNodeLabel;
    }

    public void setOperateNodeLabel(String operateNodeLabel) {
        this.operateNodeLabel = operateNodeLabel;
    }
}
