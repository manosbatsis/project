package com.topideal.entity.vo.order;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class PurchaseReturnOdepotItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购退货出库ID
    */
    private Long odepotOrderId;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 批次号
    */
    private String batchNo;
    /**
    * 生产日期
    */
    private Timestamp productionDate;
    /**
    * 失效日期
    */
    private Timestamp overdueDate;
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
    /*odepotOrderId get 方法 */
    public Long getOdepotOrderId(){
    return odepotOrderId;
    }
    /*odepotOrderId set 方法 */
    public void setOdepotOrderId(Long  odepotOrderId){
    this.odepotOrderId=odepotOrderId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
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
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
    return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
    this.batchNo=batchNo;
    }
    /*productionDate get 方法 */
    public Timestamp getProductionDate(){
    return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Timestamp  productionDate){
    this.productionDate=productionDate;
    }
    /*overdueDate get 方法 */
    public Timestamp getOverdueDate(){
    return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Timestamp  overdueDate){
    this.overdueDate=overdueDate;
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
