package com.topideal.entity.vo.sale;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class SaleShelfIdepotItemModel extends PageModel implements Serializable{

    /**
    * 主键
    */
    private Long id;
    /**
    * 上架入库单ID
    */
    private Long sshelfIdepotId;
    /**
    * 入库商品id
    */
    private Long inGoodsId;
    /**
    * 入库商品编码
    */
    private String inGoodsCode;
    /**
    * 入库商品货号
    */
    private String inGoodsNo;
    /**
    * 入库商品名称
    */
    private String inGoodsName;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 好品数量
    */
    private Integer normalNum;
    /**
    * 坏品数量
    */
    private Integer damagedNum;
    /**
    * 入库批次号
    */
    private String batchNo;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /**
     * 销售出库单表体ID
     */
    private Long saleOutDepotItemId;

    //销售明细id
    private Long saleItemId;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*sshelfIdepotId get 方法 */
    public Long getSshelfIdepotId(){
    return sshelfIdepotId;
    }
    /*sshelfIdepotId set 方法 */
    public void setSshelfIdepotId(Long  sshelfIdepotId){
    this.sshelfIdepotId=sshelfIdepotId;
    }
    /*inGoodsId get 方法 */
    public Long getInGoodsId(){
    return inGoodsId;
    }
    /*inGoodsId set 方法 */
    public void setInGoodsId(Long  inGoodsId){
    this.inGoodsId=inGoodsId;
    }
    /*inGoodsCode get 方法 */
    public String getInGoodsCode(){
    return inGoodsCode;
    }
    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String  inGoodsCode){
    this.inGoodsCode=inGoodsCode;
    }
    /*inGoodsNo get 方法 */
    public String getInGoodsNo(){
    return inGoodsNo;
    }
    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String  inGoodsNo){
    this.inGoodsNo=inGoodsNo;
    }
    /*inGoodsName get 方法 */
    public String getInGoodsName(){
    return inGoodsName;
    }
    /*inGoodsName set 方法 */
    public void setInGoodsName(String  inGoodsName){
    this.inGoodsName=inGoodsName;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*normalNum get 方法 */
    public Integer getNormalNum(){
    return normalNum;
    }
    /*normalNum set 方法 */
    public void setNormalNum(Integer  normalNum){
    this.normalNum=normalNum;
    }
    /*damagedNum get 方法 */
    public Integer getDamagedNum(){
    return damagedNum;
    }
    /*damagedNum set 方法 */
    public void setDamagedNum(Integer  damagedNum){
    this.damagedNum=damagedNum;
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
    public Date getProductionDate(){
    return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
    this.productionDate=productionDate;
    }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
    return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
    this.overdueDate=overdueDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
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

    public Long getSaleOutDepotItemId() {
        return saleOutDepotItemId;
    }

    public void setSaleOutDepotItemId(Long saleOutDepotItemId) {
        this.saleOutDepotItemId = saleOutDepotItemId;
    }

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }
}
