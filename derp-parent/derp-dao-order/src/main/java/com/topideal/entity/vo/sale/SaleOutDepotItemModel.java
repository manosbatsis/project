package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 销售出库表体
 * @author lian_
 *
 */
public class SaleOutDepotItemModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //失效日期
     private Date overdueDate;
     //商品id
     private Long goodsId;
     //调拨批次
     private String transferBatchNo;
     //生产日期
     private Date productionDate;
     //调拨数量
     private Integer transferNum;
     //创建人
     private Long creater;
     //id
     private Long id;
     //商品编码
     private String goodsCode;
     //商品名称
     private String goodsName;
     //创建时间
     private Timestamp createDate;
     //销售出库ID
     private Long saleOutDepotId;
     //条形码
     private String barcode;
     //爬虫账单id
     private Long crawlerBillId;
     //理货单位 00-托盘 01-箱 02-件
     private String tallyingUnit;
     //销售数量
     private Integer saleNum;
     //修改时间
     private Timestamp modifyDate;

    //账单类型 00-销售明细、01-库存买断
    private String billType;

    //标准条码
    private String commbarcode;

    //单价
    private BigDecimal price;

    //坏品数量
    private Integer wornNum;

    //销售明细id
    private Long saleItemId;

     public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Integer getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	/*crawlerBillId get 方法 */
     public Long getCrawlerBillId(){
         return crawlerBillId;
     }
     /*crawlerBillId set 方法 */
     public void setCrawlerBillId(Long  crawlerBillId){
         this.crawlerBillId=crawlerBillId;
     }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*saleOutDepotId get 方法 */
    public Long getSaleOutDepotId(){
        return saleOutDepotId;
    }
    /*saleOutDepotId set 方法 */
    public void setSaleOutDepotId(Long  saleOutDepotId){
        this.saleOutDepotId=saleOutDepotId;
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getWornNum() {
        return wornNum;
    }

    public void setWornNum(Integer wornNum) {
        this.wornNum = wornNum;
    }

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }
}

