package com.topideal.entity.vo.reporting;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.topideal.common.system.ibatis.PageModel;

public class InventoryFallingPriceReservesModel extends PageModel implements Serializable{

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
    * 仓库
    */
    private Long depotId;
    /**
     * 仓库id集合
     */
    private String depotIds ;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 批次号
    */
    private String batchNo;

    //报表日期
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    private Date reportDate;

    /**
    * 生产日期
    */
    private Date productionDate;
    /**
    * 失效日期
    */
    private Date overdueDate;
    /**
    * 剩余效期（天）
    */
    private Long surplusDays;
    /**
    * 总效期(天)
    */
    private Long totalDays;
    /**
    * 剩余效期
    */
    private Timestamp surplusDate;
    /**
    * 库存类型  0 正常品  1 残次品
    */
    private String inverntoryType;
    /**
    * 总数量
    */
    private Long surplusNum;
    /**
    * 剩余效期占比
    */
    private BigDecimal surplusProportion;
    /**
    * 效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品
    */
    private String effectiveInterval;
    
    /**
     * 效期区间集合
     */
     private String effectiveIntervals;
    
    /**
    * 跌价准备比例
    */
    private BigDecimal depreciationReserveProportion;
    /**
    * 标准成本单价
    */
    private BigDecimal settlementPrice;
    /**
    * 计提总额
    */
    private BigDecimal totalProvision;
    /**
    * 创建时间
    */
    private Timestamp createDate;

    /**
     * 条形码
     */
    private String barcode;
    /**
     * 标准品牌id
     */
    private Long brandParentId;
    /**
     * 标准品牌
     */
    private String brandParent;
    /**
     * 商品二级分类id
     */
    private Long typeId;
    /**
     * 商品二级分类名称
     */
    private String typeName;
    /**
     * 失效月份
     */
    private String overdueMonth;
    /**
     * 剩余效期占比(财务逻辑)1:1/10 ; 2: 1/5 ; 3: 1/3; 4: 1/2 ; 5: 1/2及以上 ; 7:过期品(为负) ; 8: 残次品
     */
    private String financialSurplusProportion;

    /**
     * 2个月后效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品
     */
    private String twoMonthsEffectiveInterval;
    /**
     * 2个月后剩余效期占比
     */
    private BigDecimal twoMonthsSurplusProportion;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    //报表月份
    private String reportMonth;
    //标准条码
    private String commbarcode;

    /**
     * 首次上架日期
     */
     private Date firstShelfDate;

    /**
     * 在仓天数
     */
    private Integer inWarehouseDays;

    /**
     * 在仓天数区间1: 0~30天;2: 30天~60天;3: 60天~90天; 4: 90天~120天;5: 120天以上
     */
    private String inWarehouseInterval;
    /**
     * 事业部id
     */
     private Long buId;
     /**
     * 事业部名称
     */
     private String buName;
     /**
     * 库存状态 1-在库库存 2-采购在途 3-销售在途 4-调拨在途
     */
     private String inverntoryStatus;
     /**
      * 在途单号
      */
     private String noshelfCode;
     
    public Integer getInWarehouseDays() {
        return inWarehouseDays;
    }

    public void setInWarehouseDays(Integer inWarehouseDays) {
        this.inWarehouseDays = inWarehouseDays;
    }

    public String getInWarehouseInterval() {
        return inWarehouseInterval;
    }

    public void setInWarehouseInterval(String inWarehouseInterval) {
        this.inWarehouseInterval = inWarehouseInterval;
    }

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
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
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
    /*surplusDays get 方法 */
    public Long getSurplusDays(){
    return surplusDays;
    }
    /*surplusDays set 方法 */
    public void setSurplusDays(Long  surplusDays){
    this.surplusDays=surplusDays;
    }
    /*totalDays get 方法 */
    public Long getTotalDays(){
    return totalDays;
    }
    /*totalDays set 方法 */
    public void setTotalDays(Long  totalDays){
    this.totalDays=totalDays;
    }
    /*surplusDate get 方法 */
    public Timestamp getSurplusDate(){
    return surplusDate;
    }
    /*surplusDate set 方法 */
    public void setSurplusDate(Timestamp  surplusDate){
    this.surplusDate=surplusDate;
    }
    /*inverntoryType get 方法 */
    public String getInverntoryType(){
    return inverntoryType;
    }
    /*inverntoryType set 方法 */
    public void setInverntoryType(String  inverntoryType){
    this.inverntoryType=inverntoryType;
    }
    /*surplusNum get 方法 */
    public Long getSurplusNum(){
    return surplusNum;
    }
    /*surplusNum set 方法 */
    public void setSurplusNum(Long  surplusNum){
    this.surplusNum=surplusNum;
    }
    /*surplusProportion get 方法 */
    public BigDecimal getSurplusProportion(){
    return surplusProportion;
    }
    /*surplusProportion set 方法 */
    public void setSurplusProportion(BigDecimal  surplusProportion){
    this.surplusProportion=surplusProportion;
    }
    /*effectiveInterval get 方法 */
    public String getEffectiveInterval(){
    return effectiveInterval;
    }
    /*effectiveInterval set 方法 */
    public void setEffectiveInterval(String  effectiveInterval){
    this.effectiveInterval=effectiveInterval;
    }
    /*depreciationReserveProportion get 方法 */
    public BigDecimal getDepreciationReserveProportion(){
    return depreciationReserveProportion;
    }
    /*depreciationReserveProportion set 方法 */
    public void setDepreciationReserveProportion(BigDecimal  depreciationReserveProportion){
    this.depreciationReserveProportion=depreciationReserveProportion;
    }
    /*settlementPrice get 方法 */
    public BigDecimal getSettlementPrice(){
    return settlementPrice;
    }
    /*settlementPrice set 方法 */
    public void setSettlementPrice(BigDecimal  settlementPrice){
    this.settlementPrice=settlementPrice;
    }
    /*totalProvision get 方法 */
    public BigDecimal getTotalProvision(){
    return totalProvision;
    }
    /*totalProvision set 方法 */
    public void setTotalProvision(BigDecimal  totalProvision){
    this.totalProvision=totalProvision;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getEffectiveIntervals() {
		return effectiveIntervals;
	}
	public void setEffectiveIntervals(String effectiveIntervals) {
		this.effectiveIntervals = effectiveIntervals;
	}
	public String getDepotIds() {
		return depotIds;
	}
	public void setDepotIds(String depotIds) {
		this.depotIds = depotIds;
	}

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getBrandParentId() {
        return brandParentId;
    }

    public void setBrandParentId(Long brandParentId) {
        this.brandParentId = brandParentId;
    }

    public String getBrandParent() {
        return brandParent;
    }

    public void setBrandParent(String brandParent) {
        this.brandParent = brandParent;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOverdueMonth() {
        return overdueMonth;
    }

    public void setOverdueMonth(String overdueMonth) {
        this.overdueMonth = overdueMonth;
    }

    public String getFinancialSurplusProportion() {
        return financialSurplusProportion;
    }

    public void setFinancialSurplusProportion(String financialSurplusProportion) {
        this.financialSurplusProportion = financialSurplusProportion;
    }

    public String getTwoMonthsEffectiveInterval() {
        return twoMonthsEffectiveInterval;
    }

    public void setTwoMonthsEffectiveInterval(String twoMonthsEffectiveInterval) {
        this.twoMonthsEffectiveInterval = twoMonthsEffectiveInterval;
    }

    public BigDecimal getTwoMonthsSurplusProportion() {
        return twoMonthsSurplusProportion;
    }

    public void setTwoMonthsSurplusProportion(BigDecimal twoMonthsSurplusProportion) {
        this.twoMonthsSurplusProportion = twoMonthsSurplusProportion;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReportMonth() {
        return reportMonth;
    }

    public void setReportMonth(String reportMonth) {
        this.reportMonth = reportMonth;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

	public Date getFirstShelfDate() {
		return firstShelfDate;
	}
	public void setFirstShelfDate(Date firstShelfDate) {
		this.firstShelfDate = firstShelfDate;
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

	public String getInverntoryStatus() {
		return inverntoryStatus;
	}

	public void setInverntoryStatus(String inverntoryStatus) {
		this.inverntoryStatus = inverntoryStatus;
	}

	public String getNoshelfCode() {
		return noshelfCode;
	}

	public void setNoshelfCode(String noshelfCode) {
		this.noshelfCode = noshelfCode;
	}
    
    
}
