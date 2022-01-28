package com.topideal.entity.vo.transfer;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 调拨入库表体
 * @author lian_
 */
public class TransferInDepotItemModel extends PageModel implements Serializable{

     //失效日期
     private Date overdueDate;
     //调拨出库ID
     private Long transferDepotId;
     //调入商品id
     private Long inGoodsId;
     //调入商品货号
     private String inGoodsNo;
     //调拨批次
     private String transferBatchNo;
     //生产日期
     private Date productionDate;
     //调入商品名称
     private String inGoodsName;
     //退货正常品数量
     private Integer transferNum;
     //创建人
     private Long creater;
     //调入商品编码
     private String inGoodsCode;
     //id
     private Long id;
     //创建时间
     private Timestamp createDate;
     //理货单位 00-托盘 01-箱 02-件
     private String tallyingUnit;
     //条形码
     private String barcode;
     //坏货数量
     private Integer wornNum;
     //过期数量
     private Integer expireNum;
     /**
      * 修改时间
      */
      private Timestamp modifyDate;

    //调入商品标准条码
    private String inCommbarcode;

    public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
    }
    /*transferDepotId get 方法 */
    public Long getTransferDepotId(){
        return transferDepotId;
    }
    /*transferDepotId set 方法 */
    public void setTransferDepotId(Long  transferDepotId){
        this.transferDepotId=transferDepotId;
    }
    /*inGoodsId get 方法 */
    public Long getInGoodsId(){
        return inGoodsId;
    }
    /*inGoodsId set 方法 */
    public void setInGoodsId(Long  inGoodsId){
        this.inGoodsId=inGoodsId;
    }
    /*inGoodsNo get 方法 */
    public String getInGoodsNo(){
        return inGoodsNo;
    }
    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String  inGoodsNo){
        this.inGoodsNo=inGoodsNo;
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
    /*inGoodsName get 方法 */
    public String getInGoodsName(){
        return inGoodsName;
    }
    /*inGoodsName set 方法 */
    public void setInGoodsName(String  inGoodsName){
        this.inGoodsName=inGoodsName;
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
    /*inGoodsCode get 方法 */
    public String getInGoodsCode(){
        return inGoodsCode;
    }
    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String  inGoodsCode){
        this.inGoodsCode=inGoodsCode;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }

    public String getInCommbarcode() {
        return inCommbarcode;
    }

    public void setInCommbarcode(String inCommbarcode) {
        this.inCommbarcode = inCommbarcode;
    }

}

