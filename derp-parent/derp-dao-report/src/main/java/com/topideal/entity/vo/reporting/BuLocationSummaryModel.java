package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BuLocationSummaryModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 仓库ID
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 归属月份
    */
    private String month;
    /**
    * 条码
    */
    private String barcode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 标准品牌id
    */
    private Long brandId;
    /**
    * 标准品牌名称
    */
    private String brandName;
    /**
    * 上级母品牌
    */
    private String superiorParentBrand;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 库位类型id
    */
    private Long stockLocationTypeId;
    /**
    * 库位类型
    */
    private String stockLocationTypeName;
    /**
    * 本月入库数量
    */
    private Integer monthInstorageNum;
    /**
    * 本月出库数量
    */
    private Integer monthOutstorageNum;
    /**
    * 本月损益数量
    */
    private Integer monthProfitlossNum;
    /**
    * 本月调整数量
    */
    private Integer monthAdjustmentNum;
    /**
    * 本月期初库存
    */
    private Integer monthBeginNum;
    /**
    * 本月正常品期初库存
    */
    private Integer monthBeginNormalNum;
    /**
    * 本月残次品期初库存
    */
    private Integer monthBeginDamagedNum;
    /**
    * 本月期末库存
    */
    private Integer monthEndNum;
    /**
    * 本月正常品期末库存
    */
    private Integer monthEndNormalNum;
    /**
    * 本月残次品期末库存量
    */
    private Integer monthEndDamagedNum;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
     * 累计采购未上架数量
     */
    private Integer addPurchaseNotshelfNum;
    /**
     * 累计调拨在途
     */
    private Integer addTransferNotshelfNum;

    /**
     * 理货单位 0 托盘 1箱  2件
     */
    private String unit;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
    return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
    this.brandId=brandId;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*superiorParentBrand get 方法 */
    public String getSuperiorParentBrand(){
    return superiorParentBrand;
    }
    /*superiorParentBrand set 方法 */
    public void setSuperiorParentBrand(String  superiorParentBrand){
    this.superiorParentBrand=superiorParentBrand;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*stockLocationTypeId get 方法 */
    public Long getStockLocationTypeId(){
    return stockLocationTypeId;
    }
    /*stockLocationTypeId set 方法 */
    public void setStockLocationTypeId(Long  stockLocationTypeId){
    this.stockLocationTypeId=stockLocationTypeId;
    }
    /*stockLocationTypeName get 方法 */
    public String getStockLocationTypeName(){
    return stockLocationTypeName;
    }
    /*stockLocationTypeName set 方法 */
    public void setStockLocationTypeName(String  stockLocationTypeName){
    this.stockLocationTypeName=stockLocationTypeName;
    }
    /*monthInstorageNum get 方法 */
    public Integer getMonthInstorageNum(){
    return monthInstorageNum;
    }
    /*monthInstorageNum set 方法 */
    public void setMonthInstorageNum(Integer  monthInstorageNum){
    this.monthInstorageNum=monthInstorageNum;
    }
    /*monthOutstorageNum get 方法 */
    public Integer getMonthOutstorageNum(){
    return monthOutstorageNum;
    }
    /*monthOutstorageNum set 方法 */
    public void setMonthOutstorageNum(Integer  monthOutstorageNum){
    this.monthOutstorageNum=monthOutstorageNum;
    }
    /*monthProfitlossNum get 方法 */
    public Integer getMonthProfitlossNum(){
    return monthProfitlossNum;
    }
    /*monthProfitlossNum set 方法 */
    public void setMonthProfitlossNum(Integer  monthProfitlossNum){
    this.monthProfitlossNum=monthProfitlossNum;
    }
    /*monthAdjustmentNum get 方法 */
    public Integer getMonthAdjustmentNum(){
    return monthAdjustmentNum;
    }
    /*monthAdjustmentNum set 方法 */
    public void setMonthAdjustmentNum(Integer  monthAdjustmentNum){
    this.monthAdjustmentNum=monthAdjustmentNum;
    }
    /*monthBeginNum get 方法 */
    public Integer getMonthBeginNum(){
    return monthBeginNum;
    }
    /*monthBeginNum set 方法 */
    public void setMonthBeginNum(Integer  monthBeginNum){
    this.monthBeginNum=monthBeginNum;
    }
    /*monthBeginNormalNum get 方法 */
    public Integer getMonthBeginNormalNum(){
    return monthBeginNormalNum;
    }
    /*monthBeginNormalNum set 方法 */
    public void setMonthBeginNormalNum(Integer  monthBeginNormalNum){
    this.monthBeginNormalNum=monthBeginNormalNum;
    }
    /*monthBeginDamagedNum get 方法 */
    public Integer getMonthBeginDamagedNum(){
    return monthBeginDamagedNum;
    }
    /*monthBeginDamagedNum set 方法 */
    public void setMonthBeginDamagedNum(Integer  monthBeginDamagedNum){
    this.monthBeginDamagedNum=monthBeginDamagedNum;
    }
    /*monthEndNum get 方法 */
    public Integer getMonthEndNum(){
    return monthEndNum;
    }
    /*monthEndNum set 方法 */
    public void setMonthEndNum(Integer  monthEndNum){
    this.monthEndNum=monthEndNum;
    }
    /*monthEndNormalNum get 方法 */
    public Integer getMonthEndNormalNum(){
    return monthEndNormalNum;
    }
    /*monthEndNormalNum set 方法 */
    public void setMonthEndNormalNum(Integer  monthEndNormalNum){
    this.monthEndNormalNum=monthEndNormalNum;
    }
    /*monthEndDamagedNum get 方法 */
    public Integer getMonthEndDamagedNum(){
    return monthEndDamagedNum;
    }
    /*monthEndDamagedNum set 方法 */
    public void setMonthEndDamagedNum(Integer  monthEndDamagedNum){
    this.monthEndDamagedNum=monthEndDamagedNum;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }

    public Integer getAddPurchaseNotshelfNum() {
        return addPurchaseNotshelfNum;
    }

    public void setAddPurchaseNotshelfNum(Integer addPurchaseNotshelfNum) {
        this.addPurchaseNotshelfNum = addPurchaseNotshelfNum;
    }

    public Integer getAddTransferNotshelfNum() {
        return addTransferNotshelfNum;
    }

    public void setAddTransferNotshelfNum(Integer addTransferNotshelfNum) {
        this.addTransferNotshelfNum = addTransferNotshelfNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
