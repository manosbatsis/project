package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BuPurchaseNotshelfInfoModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购订单编号
    */
    private String code;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 仓库id
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 录单时间 YYYY-MM-dd HH:mm:ss
    */
    private Timestamp orderCreateDate;
    /**
    * 发票时间 YYYY-MM-dd
    */
    private Timestamp drawInvoiceDate;
    /**
    * 采购订单状态 001,待审核,003,已审核,006,已删除,007,已完结
    */
    private String status;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 条码
    */
    private String barcode;
    /**
    * 采购未上架数量
    */
    private Integer notshelfNum;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 理货单位 0 托盘 1箱  2件
    */
    private String unit;
    /**
    * 业务进销存汇总表头id
    */
    private Long inventorySummaryId;
    /**
    * 归属月份 YYYY-MM
    */
    private String ownMonth;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
     * 上级母品牌
     */
    private String superiorParentBrand;

    /**
     * 归属时间
     */
    private Timestamp attributionDate;
    /**
     * 完结入库时间
     */
    private Timestamp endDate;

    /**
     * 库位类型id
     */
    private Long stockLocationTypeId;
    /**
     * 库位类型名称
     */
    private String stockLocationTypeName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*orderCreateDate get 方法 */
    public Timestamp getOrderCreateDate(){
    return orderCreateDate;
    }
    /*orderCreateDate set 方法 */
    public void setOrderCreateDate(Timestamp  orderCreateDate){
    this.orderCreateDate=orderCreateDate;
    }
    /*drawInvoiceDate get 方法 */
    public Timestamp getDrawInvoiceDate(){
    return drawInvoiceDate;
    }
    /*drawInvoiceDate set 方法 */
    public void setDrawInvoiceDate(Timestamp  drawInvoiceDate){
    this.drawInvoiceDate=drawInvoiceDate;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*notshelfNum get 方法 */
    public Integer getNotshelfNum(){
    return notshelfNum;
    }
    /*notshelfNum set 方法 */
    public void setNotshelfNum(Integer  notshelfNum){
    this.notshelfNum=notshelfNum;
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
    /*unit get 方法 */
    public String getUnit(){
    return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
    this.unit=unit;
    }
    /*inventorySummaryId get 方法 */
    public Long getInventorySummaryId(){
    return inventorySummaryId;
    }
    /*inventorySummaryId set 方法 */
    public void setInventorySummaryId(Long  inventorySummaryId){
    this.inventorySummaryId=inventorySummaryId;
    }
    /*ownMonth get 方法 */
    public String getOwnMonth(){
    return ownMonth;
    }
    /*ownMonth set 方法 */
    public void setOwnMonth(String  ownMonth){
    this.ownMonth=ownMonth;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }

    public String getSuperiorParentBrand() {
        return superiorParentBrand;
    }

    public void setSuperiorParentBrand(String superiorParentBrand) {
        this.superiorParentBrand = superiorParentBrand;
    }

    public Timestamp getAttributionDate() {
        return attributionDate;
    }

    public void setAttributionDate(Timestamp attributionDate) {
        this.attributionDate = attributionDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}
