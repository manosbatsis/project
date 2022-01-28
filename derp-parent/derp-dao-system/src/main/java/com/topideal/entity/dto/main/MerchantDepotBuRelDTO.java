package com.topideal.entity.dto.main;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MerchantDepotBuRelDTO extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 公司id
    */
    private Long merchantId;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 仓库ID
    */
    private Long depotId;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;


    //事业部下拉框关联仓库1
    private Long depotId1;

    //事业部下拉框关联仓库2
    private Long depotId2;

    //事业部下拉框多个关联仓库
    private List<Long> depotIdList;

    //仓库类型
    private String type ;

    //是否代销仓(1-是,0-否)
    private String isTopBooks;

    //是否代客管理仓库
    private String isValetManage;


    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
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



    public Long getDepotId1() {
        return depotId1;
    }

    public void setDepotId1(Long depotId1) {
        this.depotId1 = depotId1;
    }

    public Long getDepotId2() {
        return depotId2;
    }

    public void setDepotId2(Long depotId2) {
        this.depotId2 = depotId2;
    }

    public List<Long> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<Long> depotIdList) {
        this.depotIdList = depotIdList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsTopBooks() {
        return isTopBooks;
    }

    public void setIsTopBooks(String isTopBooks) {
        this.isTopBooks = isTopBooks;
    }

    public String getIsValetManage() {
        return isValetManage;
    }

    public void setIsValetManage(String isValetManage) {
        this.isValetManage = isValetManage;
    }
}
