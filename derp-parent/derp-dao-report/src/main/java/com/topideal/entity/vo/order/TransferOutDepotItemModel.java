package com.topideal.entity.vo.order;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 调拨出库表体
 * @author lian_
 *
 */
public class TransferOutDepotItemModel extends PageModel implements Serializable{

    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 调出商品编码
    */
    private String outGoodsCode;
    /**
    * 调拨出库ID
    */
    private Long transferDepotId;
    /**
    * 调出商品名称
    */
    private String outGoodsName;
    /**
    * 调拨批次
    */
    private String transferBatchNo;
    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 调出商品id
    */
    private Long outGoodsId;
    /**
    * 调拨数量
    */
    private Integer transferNum;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * id
    */
    private Long id;
    /**
    * 理货单位 00-托盘 01-箱 02-件
    */
    private String tallyingUnit;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 调出商品货号
    */
    private String outGoodsNo;
    /**
    * 创建日期
    */
    private Timestamp createDate;
    //修改时间
    private Timestamp modifyDate;

    //调出商品标准条码
    private String outCommbarcode;

    //坏货数量
    private Integer wornNum;
    //过期数量
    private Integer expireNum;

   public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
    }
    /*outGoodsCode get 方法 */
    public String getOutGoodsCode(){
        return outGoodsCode;
    }
    /*outGoodsCode set 方法 */
    public void setOutGoodsCode(String  outGoodsCode){
        this.outGoodsCode=outGoodsCode;
    }
    /*transferDepotId get 方法 */
    public Long getTransferDepotId(){
        return transferDepotId;
    }
    /*transferDepotId set 方法 */
    public void setTransferDepotId(Long  transferDepotId){
        this.transferDepotId=transferDepotId;
    }
    /*outGoodsName get 方法 */
    public String getOutGoodsName(){
        return outGoodsName;
    }
    /*outGoodsName set 方法 */
    public void setOutGoodsName(String  outGoodsName){
        this.outGoodsName=outGoodsName;
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
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*outGoodsId get 方法 */
    public Long getOutGoodsId(){
        return outGoodsId;
    }
    /*outGoodsId set 方法 */
    public void setOutGoodsId(Long  outGoodsId){
        this.outGoodsId=outGoodsId;
    }
    /*transferNum get 方法 */
    public Integer getTransferNum(){
        return transferNum;
    }
    /*transferNum set 方法 */
    public void setTransferNum(Integer  transferNum){
        this.transferNum=transferNum;
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
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
        return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
        this.tallyingUnit=tallyingUnit;
    }
    /*barcode get 方法 */
    public String getBarcode(){
        return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
        this.barcode=barcode;
    }
    /*outGoodsNo get 方法 */
    public String getOutGoodsNo(){
        return outGoodsNo;
    }
    /*outGoodsNo set 方法 */
    public void setOutGoodsNo(String  outGoodsNo){
        this.outGoodsNo=outGoodsNo;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }

    public String getOutCommbarcode() {
        return outCommbarcode;
    }

    public void setOutCommbarcode(String outCommbarcode) {
        this.outCommbarcode = outCommbarcode;
    }

    public Integer getWornNum() {
        return wornNum;
    }

    public void setWornNum(Integer wornNum) {
        this.wornNum = wornNum;
    }

    public Integer getExpireNum() {
        return expireNum;
    }

    public void setExpireNum(Integer expireNum) {
        this.expireNum = expireNum;
    }

}
