package com.topideal.entity.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class InWarehouseDetailsDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "商家ID")
    private Long merchantId;

	@ApiModelProperty(value = "商家名称")
    private String merchantName;

	@ApiModelProperty(value = "月份")
    private String month;

	@ApiModelProperty(value = "商品名称")
    private String goodsName;

	@ApiModelProperty(value = "标准品牌id")
    private Long brandId;

	@ApiModelProperty(value = "标准品牌名称")
    private String brandName;

	@ApiModelProperty(value = "标准条码")
    private String commbarcode;

	@ApiModelProperty(value = "商品二级分类id")
    private Long typeId;

	@ApiModelProperty(value = "商品二级分类名称")
    private String typeName;

	@ApiModelProperty(value = "在库天数")
    private Integer inWarehouseDays;

	@ApiModelProperty(value = "在库数量")
    private Integer inWarehouseNum;

	@ApiModelProperty(value = "在库金额")
    private BigDecimal inWarehouseAmount;

	@ApiModelProperty(value = "归属入库单号")
    private String warehouseNo;

	@ApiModelProperty(value = "入库时间")
    private Timestamp warehouseDate;
    
	@ApiModelProperty(value = "入库量")
    private Integer warehouseNum;

	@ApiModelProperty(value = "入库仓库id")
    private Long depotId;

	@ApiModelProperty(value = "入库仓库名称")
    private String depotName;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "状态（01-已匹配，00未匹配）")
    private String status;
    
	@ApiModelProperty(value = "查询开始日子")
    private Integer startDay;

	@ApiModelProperty(value = "查询结束日子")
    private Integer endDay;

	@ApiModelProperty(value = "在库天数范围")
    private String inWarehouseRange;

	@ApiModelProperty(value = "事业部ID")
    private Long buId;
	@ApiModelProperty(value = "事业部ID集合")
    private List<Long> buIds;

	@ApiModelProperty(value = "事业部")
    private String buName;

	@ApiModelProperty(value = "币种")
    private String currency;

	@ApiModelProperty(value = "统计截止时间")
    private Timestamp statisticsDate;

	@ApiModelProperty(value = "条形码")
    private String barcode;

	@ApiModelProperty(value = "加权单价")
    private BigDecimal weightedPrice;

	@ApiModelProperty(value = "加权金额")
    private BigDecimal weightedAmount;

	@ApiModelProperty(value = "在库单价")
    private BigDecimal inWarehousePrice;

	@ApiModelProperty(value = "母品牌名称")
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
	public Integer getStartDay() {
		return startDay;
	}
	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}
	public Integer getEndDay() {
		return endDay;
	}
	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
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
	public List<Long> getBuIds() {
		return buIds;
	}
	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
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

    public String getSuperiorParentBrand() {
        return superiorParentBrand;
    }

    public void setSuperiorParentBrand(String superiorParentBrand) {
        this.superiorParentBrand = superiorParentBrand;
    }
}
