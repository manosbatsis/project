package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BuInventorySummaryModel extends PageModel implements Serializable{

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
    * 商家卓志编码
    */
    private String topidealCode;
    /**
    * 仓库ID
    */
    private Long depotId;
    /**
    * 仓库编码
    */
    private String depotCode;
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
    * 事业部编码
    */
    private String buCode;
    /**
    * 归属月份
    */
    private String ownMonth;
    /**
    * 货品名称
    */
    private String productName;
    /**
    * 品牌id
    */
    private Long brandId;
    /**
    * 品牌名称
    */
    private String brandName;
    /**
    * 条码
    */
    private String barcode;
    /**
    * 工厂编码
    */
    private String factoryNo;
    /**
    * 理货单位 0 托盘 1箱  2件
    */
    private String unit;
    /**
    * 单价HKD
    */
    private BigDecimal supplyMinPrice;
    /**
    * 本月期初库存
    */
    private Integer monthBeginNum;
    /**
    * 本月正常品期初库存
    */
    private Integer monthBeginNormalNum;
    /**
    * 本月入库数量
    */
    private Integer monthInstorageNum;
    /**
    * 本月出库数量
    */
    private Integer monthOutstorageNum;
    /**
    * 来货残损数量
    */
    private Integer inDamagedNum;
    /**
    * 出库残损数量
    */
    private Integer outDamagedNum;
    /**
    * 本月损益数量
    */
    private Integer monthProfitlossNum;
    /**
    * 本月期末库存
    */
    private Integer monthEndNum;
    /**
    * 本月正常品期末库存
    */
    private Integer monthEndNormalNum;
    /**
    * 仓库现存量
    */
    private Integer availableNum;
    /**
    * 本月销售未确认数量
    */
    private Integer monthSaleUnconfirmedNum;
    /**
    * 本月采购未上架数量
    */
    private Integer monthPurchaseNotshelfNum;
    /**
    * 本月期末库存金额
    */
    private BigDecimal monthEndAmount;
    /**
    * 本月残次品期初库存量
    */
    private Integer monthBeginDamagedNum;
    /**
    * 本月残次品期末库存量
    */
    private Integer monthEndDamagedNum;
    /**
    * 好品损益
    */
    private Integer profitlossGoodNum;
    /**
    * 坏品损益
    */
    private Integer profitlossDamagedNum;
    /**
    * 正常品过期量
    */
    private Integer monthNormalExpireNum;
    /**
    * 本期调拨在途
    */
    private Integer monthTransferNotshelfNum;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;

    /**
     * 本期来货残次
     */
    private Integer monthInBadNum;
    /**
     * 本期出库残次
     */
    private Integer monthOutBadNum;

    //标准条码
    private String commbarcode;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 商品货号
     */
    private String goodsNo;
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 上级母品牌
     */
    private String superiorParentBrand;

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
    /*topidealCode get 方法 */
    public String getTopidealCode(){
    return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
    this.topidealCode=topidealCode;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotCode get 方法 */
    public String getDepotCode(){
    return depotCode;
    }
    /*depotCode set 方法 */
    public void setDepotCode(String  depotCode){
    this.depotCode=depotCode;
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
    /*buCode get 方法 */
    public String getBuCode(){
    return buCode;
    }
    /*buCode set 方法 */
    public void setBuCode(String  buCode){
    this.buCode=buCode;
    }
    /*ownMonth get 方法 */
    public String getOwnMonth(){
    return ownMonth;
    }
    /*ownMonth set 方法 */
    public void setOwnMonth(String  ownMonth){
    this.ownMonth=ownMonth;
    }
    /*productName get 方法 */
    public String getProductName(){
    return productName;
    }
    /*productName set 方法 */
    public void setProductName(String  productName){
    this.productName=productName;
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
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*factoryNo get 方法 */
    public String getFactoryNo(){
    return factoryNo;
    }
    /*factoryNo set 方法 */
    public void setFactoryNo(String  factoryNo){
    this.factoryNo=factoryNo;
    }
    /*unit get 方法 */
    public String getUnit(){
    return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
    this.unit=unit;
    }
    /*supplyMinPrice get 方法 */
    public BigDecimal getSupplyMinPrice(){
    return supplyMinPrice;
    }
    /*supplyMinPrice set 方法 */
    public void setSupplyMinPrice(BigDecimal  supplyMinPrice){
    this.supplyMinPrice=supplyMinPrice;
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
    /*inDamagedNum get 方法 */
    public Integer getInDamagedNum(){
    return inDamagedNum;
    }
    /*inDamagedNum set 方法 */
    public void setInDamagedNum(Integer  inDamagedNum){
    this.inDamagedNum=inDamagedNum;
    }
    /*outDamagedNum get 方法 */
    public Integer getOutDamagedNum(){
    return outDamagedNum;
    }
    /*outDamagedNum set 方法 */
    public void setOutDamagedNum(Integer  outDamagedNum){
    this.outDamagedNum=outDamagedNum;
    }
    /*monthProfitlossNum get 方法 */
    public Integer getMonthProfitlossNum(){
    return monthProfitlossNum;
    }
    /*monthProfitlossNum set 方法 */
    public void setMonthProfitlossNum(Integer  monthProfitlossNum){
    this.monthProfitlossNum=monthProfitlossNum;
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
    /*availableNum get 方法 */
    public Integer getAvailableNum(){
    return availableNum;
    }
    /*availableNum set 方法 */
    public void setAvailableNum(Integer  availableNum){
    this.availableNum=availableNum;
    }
    /*monthSaleUnconfirmedNum get 方法 */
    public Integer getMonthSaleUnconfirmedNum(){
    return monthSaleUnconfirmedNum;
    }
    /*monthSaleUnconfirmedNum set 方法 */
    public void setMonthSaleUnconfirmedNum(Integer  monthSaleUnconfirmedNum){
    this.monthSaleUnconfirmedNum=monthSaleUnconfirmedNum;
    }
    /*monthPurchaseNotshelfNum get 方法 */
    public Integer getMonthPurchaseNotshelfNum(){
    return monthPurchaseNotshelfNum;
    }
    /*monthPurchaseNotshelfNum set 方法 */
    public void setMonthPurchaseNotshelfNum(Integer  monthPurchaseNotshelfNum){
    this.monthPurchaseNotshelfNum=monthPurchaseNotshelfNum;
    }
    /*monthEndAmount get 方法 */
    public BigDecimal getMonthEndAmount(){
    return monthEndAmount;
    }
    /*monthEndAmount set 方法 */
    public void setMonthEndAmount(BigDecimal  monthEndAmount){
    this.monthEndAmount=monthEndAmount;
    }
    /*monthBeginDamagedNum get 方法 */
    public Integer getMonthBeginDamagedNum(){
    return monthBeginDamagedNum;
    }
    /*monthBeginDamagedNum set 方法 */
    public void setMonthBeginDamagedNum(Integer  monthBeginDamagedNum){
    this.monthBeginDamagedNum=monthBeginDamagedNum;
    }
    /*monthEndDamagedNum get 方法 */
    public Integer getMonthEndDamagedNum(){
    return monthEndDamagedNum;
    }
    /*monthEndDamagedNum set 方法 */
    public void setMonthEndDamagedNum(Integer  monthEndDamagedNum){
    this.monthEndDamagedNum=monthEndDamagedNum;
    }
    /*profitlossGoodNum get 方法 */
    public Integer getProfitlossGoodNum(){
    return profitlossGoodNum;
    }
    /*profitlossGoodNum set 方法 */
    public void setProfitlossGoodNum(Integer  profitlossGoodNum){
    this.profitlossGoodNum=profitlossGoodNum;
    }
    /*profitlossDamagedNum get 方法 */
    public Integer getProfitlossDamagedNum(){
    return profitlossDamagedNum;
    }
    /*profitlossDamagedNum set 方法 */
    public void setProfitlossDamagedNum(Integer  profitlossDamagedNum){
    this.profitlossDamagedNum=profitlossDamagedNum;
    }
    /*monthNormalExpireNum get 方法 */
    public Integer getMonthNormalExpireNum(){
    return monthNormalExpireNum;
    }
    /*monthNormalExpireNum set 方法 */
    public void setMonthNormalExpireNum(Integer  monthNormalExpireNum){
    this.monthNormalExpireNum=monthNormalExpireNum;
    }
    /*monthTransferNotshelfNum get 方法 */
    public Integer getMonthTransferNotshelfNum(){
    return monthTransferNotshelfNum;
    }
    /*monthTransferNotshelfNum set 方法 */
    public void setMonthTransferNotshelfNum(Integer  monthTransferNotshelfNum){
    this.monthTransferNotshelfNum=monthTransferNotshelfNum;
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

    public Integer getMonthInBadNum() {
        return monthInBadNum;
    }

    public void setMonthInBadNum(Integer monthInBadNum) {
        this.monthInBadNum = monthInBadNum;
    }

    public Integer getMonthOutBadNum() {
        return monthOutBadNum;
    }

    public void setMonthOutBadNum(Integer monthOutBadNum) {
        this.monthOutBadNum = monthOutBadNum;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSuperiorParentBrand() {
        return superiorParentBrand;
    }

    public void setSuperiorParentBrand(String superiorParentBrand) {
        this.superiorParentBrand = superiorParentBrand;
    }
}
