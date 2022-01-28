package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class InWarehouseDetailsModel extends PageModel implements Serializable{

    /**
    * 记录
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
    * 月份
    */
    private String month;
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
    * 标准条码
    */
    private String commbarcode;
    /**
    * 商品二级分类id
    */
    private Long typeId;
    /**
    * 商品二级分类名称
    */
    private String typeName;
    /**
    * 在库天数
    */
    private Integer inWarehouseDays;
    /**
    * 在库数量
    */
    private Integer inWarehouseNum;
    /**
    * 在库金额
    */
    private BigDecimal inWarehouseAmount;
    /**
    * 归属入库单号
    */
    private String warehouseNo;
    /**
    * 入库时间
    */
    private Timestamp warehouseDate;
    /**
    * 入库量
    */
    private Integer warehouseNum;
    /**
    * 入库仓库id
    */
    private Long depotId;
    /**
    * 入库仓库名称
    */
    private String depotName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //状态（01-已匹配，00未匹配）
    private String status;

    //在库天数范围
    private String inWarehouseRange;

    /**
     * 事业部ID
     */
    private Long buId;
    /**
     * 事业部
     */
    private String buName;
    /**
     * 币种
     */
    private String currency;
    /**
     * 统计截止时间
     */
    private Timestamp statisticsDate;
    /**
     * 条形码
     */
    private String barcode;
    /**
     * 加权单价
     */
    private BigDecimal weightedPrice;
    /**
     * 加权金额
     */
    private BigDecimal weightedAmount;
    /**
     * 在库单价
     */
    private BigDecimal inWarehousePrice;

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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
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
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*typeId get 方法 */
    public Long getTypeId(){
    return typeId;
    }
    /*typeId set 方法 */
    public void setTypeId(Long  typeId){
    this.typeId=typeId;
    }
    /*typeName get 方法 */
    public String getTypeName(){
    return typeName;
    }
    /*typeName set 方法 */
    public void setTypeName(String  typeName){
    this.typeName=typeName;
    }
    /*inWarehouseDays get 方法 */
    public Integer getInWarehouseDays(){
    return inWarehouseDays;
    }
    /*inWarehouseDays set 方法 */
    public void setInWarehouseDays(Integer  inWarehouseDays){
    this.inWarehouseDays=inWarehouseDays;
    }
    /*inWarehouseNum get 方法 */
    public Integer getInWarehouseNum(){
    return inWarehouseNum;
    }
    /*inWarehouseNum set 方法 */
    public void setInWarehouseNum(Integer  inWarehouseNum){
    this.inWarehouseNum=inWarehouseNum;
    }
    /*inWarehouseAmount get 方法 */
    public BigDecimal getInWarehouseAmount(){
    return inWarehouseAmount;
    }
    /*inWarehouseAmount set 方法 */
    public void setInWarehouseAmount(BigDecimal  inWarehouseAmount){
    this.inWarehouseAmount=inWarehouseAmount;
    }
    /*warehouseNo get 方法 */
    public String getWarehouseNo(){
    return warehouseNo;
    }
    /*warehouseNo set 方法 */
    public void setWarehouseNo(String  warehouseNo){
    this.warehouseNo=warehouseNo;
    }
    /*warehouseDate get 方法 */
    public Timestamp getWarehouseDate(){
    return warehouseDate;
    }
    /*warehouseDate set 方法 */
    public void setWarehouseDate(Timestamp  warehouseDate){
    this.warehouseDate=warehouseDate;
    }
    /*warehouseNum get 方法 */
    public Integer getWarehouseNum(){
    return warehouseNum;
    }
    /*warehouseNum set 方法 */
    public void setWarehouseNum(Integer  warehouseNum){
    this.warehouseNum=warehouseNum;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInWarehouseRange() {
        return inWarehouseRange;
    }

    public void setInWarehouseRange(String inWarehouseRange) {
        this.inWarehouseRange = inWarehouseRange;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Timestamp getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Timestamp statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getWeightedPrice() {
        return weightedPrice;
    }

    public void setWeightedPrice(BigDecimal weightedPrice) {
        this.weightedPrice = weightedPrice;
    }

    public BigDecimal getWeightedAmount() {
        return weightedAmount;
    }

    public void setWeightedAmount(BigDecimal weightedAmount) {
        this.weightedAmount = weightedAmount;
    }

    public BigDecimal getInWarehousePrice() {
        return inWarehousePrice;
    }

    public void setInWarehousePrice(BigDecimal inWarehousePrice) {
        this.inWarehousePrice = inWarehousePrice;
    }
}
