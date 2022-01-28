package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import com.topideal.common.system.ibatis.PageModel;
/**
 * 商品收发汇总 
 * @author lian_
 *
 */
public class InventorySummaryModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //仓库名称
     private String depotName;
     //商品ID
     private Long goodsId;
     //销售在途量
     private Integer saleOnwayNum;
     //仓库ID
     private Long depotId;
     //本月期初库存
     private Integer openingInventoryNum;
     //库存余量
     private Integer surplusNum;
     //库存周转天数
     private Integer turnoverDays;
     //可用库存量
     private Integer availableNum;
     //商家名称
     private String merchantName;
     //电商在途量
     private Integer eOnwayNum;
     //商家ID
     private Long merchantId;
     //本月累计入库
     private Integer inDepotTotal;
     //商品名称
     private String goodsName;
     //采购在途量
     private Integer onwayNum;
     //id
     private Long id;
     //退货在途量
     private Integer returnGoodsNum;
     //本月累计出库
     private Integer outDepotTotal;
     //创建时间
     private Timestamp createDate;
     //库存断销时间
     private Timestamp noSaleDate;
     
     //调拨出库在途量
     private Integer transferOutNum;
     
     private String  lastMonth   ;//上个月
     
     private String  currentMonth;  //当前月
     
     private String  unit;  //单位


     /*eOnwayNum get 方法 */
    public Integer geteOnwayNum() {
		return eOnwayNum;
	}
    /*eOnwayNum set 方法 */
	public void seteOnwayNum(Integer eOnwayNum) {
		this.eOnwayNum = eOnwayNum;
	}
	/*noSaleDate get 方法 */
	public Timestamp getNoSaleDate() {
		return noSaleDate;
	}
	/*noSaleDate set 方法 */
	public void setNoSaleDate(Timestamp noSaleDate) {
		this.noSaleDate = noSaleDate;
	}
    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
	/*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*saleOnwayNum get 方法 */
    public Integer getSaleOnwayNum(){
        return saleOnwayNum;
    }
    /*saleOnwayNum set 方法 */
    public void setSaleOnwayNum(Integer  saleOnwayNum){
        this.saleOnwayNum=saleOnwayNum;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*openingInventoryNum get 方法 */
    public Integer getOpeningInventoryNum(){
        return openingInventoryNum;
    }
    /*openingInventoryNum set 方法 */
    public void setOpeningInventoryNum(Integer  openingInventoryNum){
        this.openingInventoryNum=openingInventoryNum;
    }
    /*surplusNum get 方法 */
    public Integer getSurplusNum(){
        return surplusNum;
    }
    /*surplusNum set 方法 */
    public void setSurplusNum(Integer  surplusNum){
        this.surplusNum=surplusNum;
    }
    /*turnoverDays get 方法 */
    public Integer getTurnoverDays(){
        return turnoverDays;
    }
    /*turnoverDays set 方法 */
    public void setTurnoverDays(Integer  turnoverDays){
        this.turnoverDays=turnoverDays;
    }
    /*availableNum get 方法 */
    public Integer getAvailableNum(){
        return availableNum;
    }
    /*availableNum set 方法 */
    public void setAvailableNum(Integer  availableNum){
        this.availableNum=availableNum;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*inDepotTotal get 方法 */
    public Integer getInDepotTotal(){
        return inDepotTotal;
    }
    /*inDepotTotal set 方法 */
    public void setInDepotTotal(Integer  inDepotTotal){
        this.inDepotTotal=inDepotTotal;
    }
    /*name get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*name set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*onwayNum get 方法 */
    public Integer getOnwayNum(){
        return onwayNum;
    }
    /*onwayNum set 方法 */
    public void setOnwayNum(Integer  onwayNum){
        this.onwayNum=onwayNum;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*returnGoodsNum get 方法 */
    public Integer getReturnGoodsNum(){
        return returnGoodsNum;
    }
    /*returnGoodsNum set 方法 */
    public void setReturnGoodsNum(Integer  returnGoodsNum){
        this.returnGoodsNum=returnGoodsNum;
    }
    /*outDepotTotal get 方法 */
    public Integer getOutDepotTotal(){
        return outDepotTotal;
    }
    /*outDepotTotal set 方法 */
    public void setOutDepotTotal(Integer  outDepotTotal){
        this.outDepotTotal=outDepotTotal;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public String getLastMonth() {
		return lastMonth;
	}
	public void setLastMonth(String lastMonth) {
		this.lastMonth = lastMonth;
	}
	public String getCurrentMonth() {
		return currentMonth;
	}
	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	public Integer getTransferOutNum() {
		return transferOutNum;
	}
	public void setTransferOutNum(Integer transferOutNum) {
		this.transferOutNum = transferOutNum;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

}

