package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class ReceiveAgingReportItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 应收账龄id
    */
    private Long agingReportId;
    /**
    * 单据类型
    */
    private String orderType;
    /**
    * PO号
    */
    private String poNo;
    /**
    * 单据号
    */
    private String code;
    /**
    * 应收月份
    */
    private String month;
    /**
    * 待核销金额
    */
    private BigDecimal writtenOffAmount;
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
    /*agingReportId get 方法 */
    public Long getAgingReportId(){
    return agingReportId;
    }
    /*agingReportId set 方法 */
    public void setAgingReportId(Long  agingReportId){
    this.agingReportId=agingReportId;
    }
    /*orderType get 方法 */
    public String getOrderType(){
    return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
    this.orderType=orderType;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*writtenOffAmount get 方法 */
    public BigDecimal getWrittenOffAmount(){
    return writtenOffAmount;
    }
    /*writtenOffAmount set 方法 */
    public void setWrittenOffAmount(BigDecimal  writtenOffAmount){
    this.writtenOffAmount=writtenOffAmount;
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
