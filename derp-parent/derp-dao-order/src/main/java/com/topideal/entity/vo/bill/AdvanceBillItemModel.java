package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class AdvanceBillItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 预收账单Id
    */
    private Long advanceId;
    /**
    * 系统业务单号
    */
    private String relCode;
    /**
    * 费项id
    */
    private Long projectId;
    /**
    * 费项名称
    */
    private String projectName;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 结算金额
    */
    private BigDecimal amount;
    /**
    * 备注
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
    /*advanceId get 方法 */
    public Long getAdvanceId(){
    return advanceId;
    }
    /*advanceId set 方法 */
    public void setAdvanceId(Long  advanceId){
    this.advanceId=advanceId;
    }
    /*relCode get 方法 */
    public String getRelCode(){
    return relCode;
    }
    /*relCode set 方法 */
    public void setRelCode(String  relCode){
    this.relCode=relCode;
    }
    /*projectId get 方法 */
    public Long getProjectId(){
    return projectId;
    }
    /*projectId set 方法 */
    public void setProjectId(Long  projectId){
    this.projectId=projectId;
    }
    /*projectName get 方法 */
    public String getProjectName(){
    return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
    this.projectName=projectName;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
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






}
