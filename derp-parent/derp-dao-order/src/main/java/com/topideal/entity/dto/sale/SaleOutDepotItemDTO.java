package com.topideal.entity.dto.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@ApiModel
public class SaleOutDepotItemDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    @ApiModelProperty(value = "失效日期")
    private Date overdueDate;
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "调拨批次")
    private String transferBatchNo;
    @ApiModelProperty(value = "生产日期")
    private Date productionDate;
    @ApiModelProperty(value = "调拨数量")
    private Integer transferNum;
    @ApiModelProperty(value = "创建人")
    private Long creater;
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "商品编码")
    private String goodsCode;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    @ApiModelProperty(value = "销售出库ID")
    private Long saleOutDepotId;
    @ApiModelProperty(value = "条形码")
    private String barcode;
    @ApiModelProperty(value = "爬虫账单id")
    private Long crawlerBillId;
    @ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;
    @ApiModelProperty(value = "理货单位（中文）")
    private String tallyingUnitLabel;
    @ApiModelProperty(value = "销售数量")
    private Integer saleNum;
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

   @ApiModelProperty(value = "账单类型 00-销售明细、01-库存买断")
   private String billType;
   @ApiModelProperty(value = "账单类型（中文）")
   private String billTypeLabel;

   @ApiModelProperty(value = "标准条码")
   private String commbarcode;

   @ApiModelProperty(value = "单价")
   private BigDecimal price;


   @ApiModelProperty(value = "坏品数量")
   private Integer wornNum;
    //-----------扩展字段----------------------
   @ApiModelProperty(value = "出库清单编码")
   private String saleOutDepotCode;
    @ApiModelProperty(value = "原货号")
    private String originalGoodsNo;
    @ApiModelProperty(value = "原货号id")
    private Long originalGoodsId;

    @ApiModelProperty(value = "生产日期(字符串)")
    private String productionDateStr;
    @ApiModelProperty(value = "失效日期(字符串)")
    private String overdueDateStr;

    @ApiModelProperty(value = "销售明细id")
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
		if(StringUtils.isNotBlank(tallyingUnit)){
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
		}
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
	public String getSaleOutDepotCode() {
		return saleOutDepotCode;
	}
	public void setSaleOutDepotCode(String saleOutDepotCode) {
		this.saleOutDepotCode = saleOutDepotCode;
	}

   public String getBillType() {
       return billType;
   }

   public void setBillType(String billType) {
       this.billType = billType;
       if(StringUtils.isNotBlank(billType)){
    	   this.billTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOutDepotItem_billTypeList, billType);
       }
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
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}
	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}
	public String getBillTypeLabel() {
		return billTypeLabel;
	}
	public void setBillTypeLabel(String billTypeLabel) {
		this.billTypeLabel = billTypeLabel;
	}
	public Integer getWornNum() {
		return wornNum;
	}
	public void setWornNum(Integer wornNum) {
		this.wornNum = wornNum;
	}

    public String getOriginalGoodsNo() {
        return originalGoodsNo;
    }

    public void setOriginalGoodsNo(String originalGoodsNo) {
        this.originalGoodsNo = originalGoodsNo;
    }

    public Long getOriginalGoodsId() {
        return originalGoodsId;
    }

    public void setOriginalGoodsId(Long originalGoodsId) {
        this.originalGoodsId = originalGoodsId;
    }
	public String getProductionDateStr() {
		return productionDateStr;
	}
	public void setProductionDateStr(String productionDateStr) {
		this.productionDateStr = productionDateStr;
	}
	public String getOverdueDateStr() {
		return overdueDateStr;
	}
	public void setOverdueDateStr(String overdueDateStr) {
		this.overdueDateStr = overdueDateStr;
	}

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }
}

