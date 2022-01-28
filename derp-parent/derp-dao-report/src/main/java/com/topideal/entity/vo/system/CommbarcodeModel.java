package com.topideal.entity.vo.system;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class CommbarcodeModel extends PageModel implements Serializable{

    /**
    * 标准品牌Id
    */
    private Long id;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 标准品牌Id
    */
    private Long commBrandParentId;
    /**
    * 标准品牌名
    */
    private String commBrandParentName;
    /**
    * 标准品类Id(标准二级分类id)
    */
    private Long commTypeId;
    /**
    * 标准品类名（标准二级分类名）
    */
    private String commTypeName;
    /**
    * 标准品名（标准商品名称）
    */
    private String commGoodsName;
    /**
    * 维护状态 0-未维护 1-已维护
    */
    private String maintainStatus;
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
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*commBrandParentId get 方法 */
    public Long getCommBrandParentId(){
    return commBrandParentId;
    }
    /*commBrandParentId set 方法 */
    public void setCommBrandParentId(Long  commBrandParentId){
    this.commBrandParentId=commBrandParentId;
    }
    /*commBrandParentName get 方法 */
    public String getCommBrandParentName(){
    return commBrandParentName;
    }
    /*commBrandParentName set 方法 */
    public void setCommBrandParentName(String  commBrandParentName){
    this.commBrandParentName=commBrandParentName;
    }
    /*commTypeId get 方法 */
    public Long getCommTypeId(){
    return commTypeId;
    }
    /*commTypeId set 方法 */
    public void setCommTypeId(Long  commTypeId){
    this.commTypeId=commTypeId;
    }
    /*commTypeName get 方法 */
    public String getCommTypeName(){
    return commTypeName;
    }
    /*commTypeName set 方法 */
    public void setCommTypeName(String  commTypeName){
    this.commTypeName=commTypeName;
    }
    /*commGoodsName get 方法 */
    public String getCommGoodsName(){
    return commGoodsName;
    }
    /*commGoodsName set 方法 */
    public void setCommGoodsName(String  commGoodsName){
    this.commGoodsName=commGoodsName;
    }
    /*maintainStatus get 方法 */
    public String getMaintainStatus(){
    return maintainStatus;
    }
    /*maintainStatus set 方法 */
    public void setMaintainStatus(String  maintainStatus){
    this.maintainStatus=maintainStatus;
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
