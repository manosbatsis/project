package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class OperateLogModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 模块 1-采购 2-预付 3-应付
    */
    private String module;
    /**
    * 业务单号
    */
    private String relCode;
    /**
    * 操作人ID
    */
    private Long operateId;
    /**
    * 操作人名称
    */
    private String operater;
    /**
    * 操作人岗位名称
    */
    private String operaterDepot;
    /**
    * 操作项
    */
    private String operateAction;
    /**
    * 操作时间
    */
    private Timestamp operateDate;
    /**
    * 操作结果
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
