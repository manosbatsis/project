package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class DepartmentQuatoItemModel extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 
    */
    private Long departmentQuatoId;
    /**
    * 母品牌ID
    */
    private Long superiorParentBrandId;
    /**
    * 母品牌名
    */
    private String superiorParentBrand;
    /**
    * 额度币种
    */
    private String currency;
    /**
    * 
    */
    private BigDecimal quato;
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
    /*departmentQuatoId get 方法 */
    public Long getDepartmentQuatoId(){
    return departmentQuatoId;
    }
    /*departmentQuatoId set 方法 */
    public void setDepartmentQuatoId(Long  departmentQuatoId){
    this.departmentQuatoId=departmentQuatoId;
    }
    /*superiorParentBrandId get 方法 */
    public Long getSuperiorParentBrandId(){
    return superiorParentBrandId;
    }
    /*superiorParentBrandId set 方法 */
    public void setSuperiorParentBrandId(Long  superiorParentBrandId){
    this.superiorParentBrandId=superiorParentBrandId;
    }
    /*superiorParentBrand get 方法 */
    public String getSuperiorParentBrand(){
    return superiorParentBrand;
    }
    /*superiorParentBrand set 方法 */
    public void setSuperiorParentBrand(String  superiorParentBrand){
    this.superiorParentBrand=superiorParentBrand;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*quato get 方法 */
    public BigDecimal getQuato(){
    return quato;
    }
    /*quato set 方法 */
    public void setQuato(BigDecimal  quato){
    this.quato=quato;
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
