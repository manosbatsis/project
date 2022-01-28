package com.topideal.entity.vo.order;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 采购入库单商品批次详情
 * @author lian_
 */
public class PurchaseWarehouseBatchModel extends PageModel implements Serializable{

    /**
    * 失效时间
    */
    private Date overdueDate;
    /**
    * 批次号
    */
    private String batchNo;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 正常数量
    */
    private Integer normalNum;
    /**
    * 过期数量
    */
    private Integer expireNum;
    /**
    * 采购入库商品列表id
    */
    private Long itemId;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * id
    */
    private Long id;
    /**
    * 坏货数量
    */
    private Integer wornNum;
    /**
    * 创建时间
    */
    private Timestamp createDate;

    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
        return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
        this.batchNo=batchNo;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*normalNum get 方法 */
    public Integer getNormalNum(){
        return normalNum;
    }
    /*normalNum set 方法 */
    public void setNormalNum(Integer  normalNum){
        this.normalNum=normalNum;
    }
    /*expireNum get 方法 */
    public Integer getExpireNum(){
        return expireNum;
    }
    /*expireNum set 方法 */
    public void setExpireNum(Integer  expireNum){
        this.expireNum=expireNum;
    }
    /*itemId get 方法 */
    public Long getItemId(){
        return itemId;
    }
    /*itemId set 方法 */
    public void setItemId(Long  itemId){
        this.itemId=itemId;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
        return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*wornNum get 方法 */
    public Integer getWornNum(){
        return wornNum;
    }
    /*wornNum set 方法 */
    public void setWornNum(Integer  wornNum){
        this.wornNum=wornNum;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }






}
