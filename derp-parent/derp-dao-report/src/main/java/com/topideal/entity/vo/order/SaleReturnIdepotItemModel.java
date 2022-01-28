package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 销售退货入库表体
 * @author lian_
 */
public class SaleReturnIdepotItemModel extends PageModel implements Serializable{

    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 退货批次
    */
    private String returnBatchNo;
    /**
    * 销售退货入库ID
    */
    private Long sreturnIdepotId;
    /**
    * 退入商品id
    */
    private Long inGoodsId;
    /**
    * 过期数量
    */
    private Integer expireNum;
    /**
    * 退入商品货号
    */
    private String inGoodsNo;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 退货正常品数量
    */
    private Integer returnNum;
    /**
    * 退入商品名称
    */
    private String inGoodsName;
    /**
    * 退入商品编码
    */
    private String inGoodsCode;
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
    * 条形码
    */
    private String barcode;
    /**
    * 创建时间
    */
    private Timestamp createDate;

    //标准条码
    private String commbarcode;

    //PO号
    private String poNo;

    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*returnBatchNo get 方法 */
    public String getReturnBatchNo(){
        return returnBatchNo;
    }
    /*returnBatchNo set 方法 */
    public void setReturnBatchNo(String  returnBatchNo){
        this.returnBatchNo=returnBatchNo;
    }
    /*sreturnIdepotId get 方法 */
    public Long getSreturnIdepotId(){
        return sreturnIdepotId;
    }
    /*sreturnIdepotId set 方法 */
    public void setSreturnIdepotId(Long  sreturnIdepotId){
        this.sreturnIdepotId=sreturnIdepotId;
    }
    /*inGoodsId get 方法 */
    public Long getInGoodsId(){
        return inGoodsId;
    }
    /*inGoodsId set 方法 */
    public void setInGoodsId(Long  inGoodsId){
        this.inGoodsId=inGoodsId;
    }
    /*expireNum get 方法 */
    public Integer getExpireNum(){
        return expireNum;
    }
    /*expireNum set 方法 */
    public void setExpireNum(Integer  expireNum){
        this.expireNum=expireNum;
    }
    /*inGoodsNo get 方法 */
    public String getInGoodsNo(){
        return inGoodsNo;
    }
    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String  inGoodsNo){
        this.inGoodsNo=inGoodsNo;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
        return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*returnNum get 方法 */
    public Integer getReturnNum(){
        return returnNum;
    }
    /*returnNum set 方法 */
    public void setReturnNum(Integer  returnNum){
        this.returnNum=returnNum;
    }
    /*inGoodsName get 方法 */
    public String getInGoodsName(){
        return inGoodsName;
    }
    /*inGoodsName set 方法 */
    public void setInGoodsName(String  inGoodsName){
        this.inGoodsName=inGoodsName;
    }
    /*inGoodsCode get 方法 */
    public String getInGoodsCode(){
        return inGoodsCode;
    }
    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String  inGoodsCode){
        this.inGoodsCode=inGoodsCode;
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
    /*barcode get 方法 */
    public String getBarcode(){
        return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
        this.barcode=barcode;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }


    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }
}
