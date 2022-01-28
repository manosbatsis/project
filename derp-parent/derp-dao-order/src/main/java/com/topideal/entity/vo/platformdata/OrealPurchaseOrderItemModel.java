package com.topideal.entity.vo.platformdata;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class OrealPurchaseOrderItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 欧莱雅采购ID
    */
    private Long orealPurchaseOrderId;
    /**
    * 厂商编码
    */
    private String invbasdoc;
    /**
    * 经销商编码
    */
    private String cinvmecode;
    /**
    * 存货名称
    */
    private String invname;
    /**
    * 建议采购订单数量
    */
    private Integer vdef5;
    /**
    * CSR确认数量
    */
    private Integer nordernum;
    /**
    * 建议零售价
    */
    private BigDecimal refsaleprice;
    /**
    * 备注
    */
    private String vmemo;
    /**
    * 创建日期
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
    /*orealPurchaseOrderId get 方法 */
    public Long getOrealPurchaseOrderId(){
    return orealPurchaseOrderId;
    }
    /*orealPurchaseOrderId set 方法 */
    public void setOrealPurchaseOrderId(Long  orealPurchaseOrderId){
    this.orealPurchaseOrderId=orealPurchaseOrderId;
    }
    /*invbasdoc get 方法 */
    public String getInvbasdoc(){
    return invbasdoc;
    }
    /*invbasdoc set 方法 */
    public void setInvbasdoc(String  invbasdoc){
    this.invbasdoc=invbasdoc;
    }
    /*cinvmecode get 方法 */
    public String getCinvmecode(){
    return cinvmecode;
    }
    /*cinvmecode set 方法 */
    public void setCinvmecode(String  cinvmecode){
    this.cinvmecode=cinvmecode;
    }
    /*invname get 方法 */
    public String getInvname(){
    return invname;
    }
    /*invname set 方法 */
    public void setInvname(String  invname){
    this.invname=invname;
    }
    /*vdef5 get 方法 */
    public Integer getVdef5(){
    return vdef5;
    }
    /*vdef5 set 方法 */
    public void setVdef5(Integer  vdef5){
    this.vdef5=vdef5;
    }
    /*nordernum get 方法 */
    public Integer getNordernum(){
    return nordernum;
    }
    /*nordernum set 方法 */
    public void setNordernum(Integer  nordernum){
    this.nordernum=nordernum;
    }
    /*refsaleprice get 方法 */
    public BigDecimal getRefsaleprice(){
    return refsaleprice;
    }
    /*refsaleprice set 方法 */
    public void setRefsaleprice(BigDecimal  refsaleprice){
    this.refsaleprice=refsaleprice;
    }
    /*vmemo get 方法 */
    public String getVmemo(){
    return vmemo;
    }
    /*vmemo set 方法 */
    public void setVmemo(String  vmemo){
    this.vmemo=vmemo;
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
