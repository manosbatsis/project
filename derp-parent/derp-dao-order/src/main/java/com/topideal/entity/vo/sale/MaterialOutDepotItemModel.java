package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class MaterialOutDepotItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 领料出库ID
    */
    private Long materialOutDepotId;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品编码
    */
    private String goodsCode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 正常品数量
    */
    private Integer transferNum;
    /**
    * 坏品数量
    */
    private Integer wornNum;
    /**
    * 调拨批次
    */
    private String transferBatchNo;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 理货单位 00-托盘 01-箱 02-件
    */
    private String tallyingUnit;
    /**
    * 领取数量
    */
    private Integer materialNum;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 标准条码
    */
    private String commbarcode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*materialOutDepotId get 方法 */
    public Long getMaterialOutDepotId(){
    return materialOutDepotId;
    }
    /*materialOutDepotId set 方法 */
    public void setMaterialOutDepotId(Long  materialOutDepotId){
    this.materialOutDepotId=materialOutDepotId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
    return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
    this.goodsCode=goodsCode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*transferNum get 方法 */
    public Integer getTransferNum(){
    return transferNum;
    }
    /*transferNum set 方法 */
    public void setTransferNum(Integer  transferNum){
    this.transferNum=transferNum;
    }
    /*wornNum get 方法 */
    public Integer getWornNum(){
    return wornNum;
    }
    /*wornNum set 方法 */
    public void setWornNum(Integer  wornNum){
    this.wornNum=wornNum;
    }
    /*transferBatchNo get 方法 */
    public String getTransferBatchNo(){
    return transferBatchNo;
    }
    /*transferBatchNo set 方法 */
    public void setTransferBatchNo(String  transferBatchNo){
    this.transferBatchNo=transferBatchNo;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
    return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(java.util.Date  date){
    this.productionDate=date;
    }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
    return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
    this.overdueDate=overdueDate;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    }
    /*materialNum get 方法 */
    public Integer getMaterialNum(){
    return materialNum;
    }
    /*materialNum set 方法 */
    public void setMaterialNum(Integer  materialNum){
    this.materialNum=materialNum;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }



}
