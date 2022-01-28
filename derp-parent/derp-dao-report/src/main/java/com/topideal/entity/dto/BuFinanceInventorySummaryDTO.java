package com.topideal.entity.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 事业部财务进销存汇总
 */
@ApiModel
public class BuFinanceInventorySummaryDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "商家id", required = false)
    private Long merchantId;

    @ApiModelProperty(value = "商家名称", required = false)
    private String merchantName;

    @ApiModelProperty(value = "仓库id", required = false)
    private Long depotId;

    @ApiModelProperty(value = "仓库名称", required = false)
    private String depotName;

    @ApiModelProperty(value = "事业部ID", required = false)
    private Long buId;

    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;

    @ApiModelProperty(value = "事业部编码", required = false)
    private String buCode;

    @ApiModelProperty(value = "归属月份 YYYY-MM", required = false)
    private String month;

    @ApiModelProperty(value = "品牌id", required = false)
    private Long brandId;

    @ApiModelProperty(value = "品牌名称", required = false)
    private String brandName;

    @ApiModelProperty(value = "商品二级分类id", required = false)
    private Long typeId;

    @ApiModelProperty(value = "商品二级分类名称", required = false)
    private String typeName;

    @ApiModelProperty(value = "商品id", required = false)
    private Long goodsId;

    @ApiModelProperty(value = "商品货号", required = false)
    private String goodsNo;

    @ApiModelProperty(value = "商品名称", required = false)
    private String goodsName;

    @ApiModelProperty(value = "商品条码", required = false)
    private String barcode;

    @ApiModelProperty(value = "本月期初数量", required = false)
    private Integer initNum;

    @ApiModelProperty(value = "本月期初金额", required = false)
    private BigDecimal initAmount;

    @ApiModelProperty(value = "标准成本单价", required = false)
    private BigDecimal price;

    @ApiModelProperty(value = "本期采购未上架数量", required = false)
    private Integer purchaseNotshelfNum;

    @ApiModelProperty(value = "本期采购未上架金额", required = false)
    private BigDecimal purchaseNotshelfAmount;

    @ApiModelProperty(value = "本期采购入库数量", required = false)
    private Integer warehouseNum;

    @ApiModelProperty(value = "本期采购入库金额", required = false)
    private BigDecimal warehouseAmount;

    @ApiModelProperty(value = "本期销售已上架数量", required = false)
    private Integer saleShelfNum;

    @ApiModelProperty(value = "本期销售已上架金额", required = false)
    private BigDecimal saleShelfAmount;

    @ApiModelProperty(value = "本期销售未上架数量", required = false)
    private Integer saleNoshelfNum;

    @ApiModelProperty(value = "本期销售未上架金额", required = false)
    private BigDecimal saleNoshelfAmount;

    @ApiModelProperty(value = "来货残损数量", required = false)
    private Integer inDamagedNum;

    @ApiModelProperty(value = "来货残损金额", required = false)
    private BigDecimal inDamagedAmount;

    @ApiModelProperty(value = "出库残损数量", required = false)
    private Integer outDamagedNum;

    @ApiModelProperty(value = "出库残损金额", required = false)
    private BigDecimal outDamagedAmount;

    @ApiModelProperty(value = "盘盈数量", required = false)
    private Integer profitNum;

    @ApiModelProperty(value = "盘盈金额", required = false)
    private BigDecimal profitAmount;

    @ApiModelProperty(value = "期末结存数量", required = false)
    private Integer endNum;

    @ApiModelProperty(value = "期末结存金额", required = false)
    private BigDecimal endAmount;

    @ApiModelProperty(value = "调整标准成本单价", required = false)
    private BigDecimal tzPrice;

    @ApiModelProperty(value = "调整标准单价币种 01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑", required = false)
    private String currency;

    @ApiModelProperty(value = "调整期末结存金额", required = false)
    private BigDecimal tzEndAmount;

    @ApiModelProperty(value = "差异金额", required = false)
    private BigDecimal differenceAmount;

    @ApiModelProperty(value = "关帐时间", required = false)
    private Timestamp closeDate;

    @ApiModelProperty(value = "标准条码", required = false)
    private String commbarcode;

    @ApiModelProperty(value = "本期采购结转数量汇总", required = false)
    private Integer purchaseEndNum;

    @ApiModelProperty(value = "本期采购结转金额汇总", required = false)
    private BigDecimal purchaseEndAmount;

    @ApiModelProperty(value = "本期销售结转数量汇总", required = false)
    private Integer saleEndNum;

    @ApiModelProperty(value = "本期销售结转金额汇总", required = false)
    private BigDecimal saleEndAmount;

    @ApiModelProperty(value = "本月销毁数量", required = false)
    private Integer destroyNum;

    @ApiModelProperty(value = "本月销毁金额", required = false)
    private BigDecimal destroyAmount;

    @ApiModelProperty(value = "本期损益结转数量汇总", required = false)
    private Integer lossOverflowNum;

    @ApiModelProperty(value = "本期损益结转金额汇总", required = false)
    private BigDecimal lossOverflowAmount;

    @ApiModelProperty(value = "累计采购在途量", required = false)
    private Integer addPurchaseNotshelfNum;

    @ApiModelProperty(value = "累计采购在途金额", required = false)
    private BigDecimal addPurchaseNotshelfAmount;

    @ApiModelProperty(value = "累计销售在途量", required = false)
    private Integer addSaleNoshelfNum;

    @ApiModelProperty(value = "累计销售在途金额", required = false)
    private BigDecimal addSaleNoshelfAmount;

    @ApiModelProperty(value = "盘亏数量", required = false)
    private Integer lossNum;

    @ApiModelProperty(value = "盘亏金额", required = false)
    private BigDecimal lossAmount;

    @ApiModelProperty(value = "组码", required = false)
    private String groupCommbarcode;

    @ApiModelProperty(value = "本期减少采购在途量", required = false)
    private Integer decreasePurchaseNotshelfNum;

    @ApiModelProperty(value = "本期减少采购在途金额", required = false)
    private BigDecimal decreasePurchaseNotshelfAmount;

    @ApiModelProperty(value = "库存余量", required = false)
    private Integer surplusNum;

    @ApiModelProperty(value = "累计调拨在途量", required = false)
    private Integer addTransferNoshelfNum;

    @ApiModelProperty(value = "累计调拨在途金额", required = false)
    private BigDecimal addTransferNoshelfAmount;

    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;

    @ApiModelProperty(value = "本期移库入数量", required = false)
    private Integer moveInNum;

    @ApiModelProperty(value = "本期移库出数量", required = false)
    private Integer moveOutNum;

    @ApiModelProperty(value = "本期移库汇总", required = false)
    private Integer moveNum;

    /*查询条件非数据表字段start*/
    private List<Long> parentBrandIds;//标准品牌id

    @ApiModelProperty(value = "核算单价标识", required = false)
    private String accountPrice;
    @ApiModelProperty(value = "核算单价标识 1-标准成本单价 2-月末加权单价", required = false)
    private String accountPriceLabel;

    @ApiModelProperty(value = "状态", required = false)
    private String status;
    @ApiModelProperty(value = "状态 029-未关账 030-已关账", required = false)
    private String statusLabel;
    private String currencyLabel;

    @ApiModelProperty(value = "期初SD金额", required = false)
    private BigDecimal sdInitAmount;

    @ApiModelProperty(value = "期初SD单价", required = false)
    private BigDecimal sdPrice;

    @ApiModelProperty(value = "本期采购SD金额", required = false)
    private BigDecimal sdWarehouseAmount;

    @ApiModelProperty(value = "本期SD单价", required = false)
    private BigDecimal sdTzPrice;

    @ApiModelProperty(value = "本期销售结转SD金额", required = false)
    private BigDecimal sdSaleEndAmount;

    @ApiModelProperty(value = "本期损益结转SD金额汇总", required = false)
    private BigDecimal sdLossOverflowAmount;

    @ApiModelProperty(value = "期末结存SD金额", required = false)
    private BigDecimal sdEndAmount;

    @ApiModelProperty(value = "累计采购在途SD金额", required = false)
    private BigDecimal sdAddPurchaseNotshelfAmount;

    @ApiModelProperty(value = "上级母品牌", required = false)
    private String superiorParentBrand;

    @ApiModelProperty(value = "累计汇总开始年月", required = false)
    private String monthStart;

    @ApiModelProperty(value = "累计汇总结束年月", required = false)
    private String monthEnd;
    @ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
    
    /**
     * 期初SD利息金额
     */
    @ApiModelProperty(value = "期初SD利息金额", required = false)
    private BigDecimal sdInterestInitAmount;
     /**
     * 期初SD利息单价
     */
    @ApiModelProperty(value = "期初SD利息单价", required = false)
    private BigDecimal sdInterestPrice;
     /**
     * 本期采购结转利息金额
     */
    @ApiModelProperty(value = "本期采购结转利息金额", required = false)
    private BigDecimal sdInterestPurchaseEndAmount;
     /**
     * 本期SD利息单价
     */
    @ApiModelProperty(value = "本期SD利息单价", required = false)
    private BigDecimal sdInterestTzPrice;
     /**
     * 本期销售结转SD利息金额
     */
    @ApiModelProperty(value = "本期销售结转SD利息金额", required = false)
    private BigDecimal sdInterestSaleEndAmount;
     /**
     * 本期损益结转SD利息金额汇总
     */
    @ApiModelProperty(value = "本期损益结转SD利息金额汇总", required = false)
    private BigDecimal sdInterestLossOverflowAmount;
     /**
     * 期末结存SD利息金额
     */
    @ApiModelProperty(value = "期末结存SD利息金额", required = false)
    private BigDecimal sdInterestEndAmount;
    
    public String getSuperiorParentBrand() {
		return superiorParentBrand;
	}
	public void setSuperiorParentBrand(String superiorParentBrand) {
		this.superiorParentBrand = superiorParentBrand;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
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
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
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
    /*initNum get 方法 */
    public Integer getInitNum(){
    return initNum;
    }
    /*initNum set 方法 */
    public void setInitNum(Integer  initNum){
    this.initNum=initNum;
    }
    /*initAmount get 方法 */
    public BigDecimal getInitAmount(){
    return initAmount;
    }
    /*initAmount set 方法 */
    public void setInitAmount(BigDecimal  initAmount){
    this.initAmount=initAmount;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*purchaseNotshelfNum get 方法 */
    public Integer getPurchaseNotshelfNum(){
    return purchaseNotshelfNum;
    }
    /*purchaseNotshelfNum set 方法 */
    public void setPurchaseNotshelfNum(Integer  purchaseNotshelfNum){
    this.purchaseNotshelfNum=purchaseNotshelfNum;
    }
    /*purchaseNotshelfAmount get 方法 */
    public BigDecimal getPurchaseNotshelfAmount(){
    return purchaseNotshelfAmount;
    }
    /*purchaseNotshelfAmount set 方法 */
    public void setPurchaseNotshelfAmount(BigDecimal  purchaseNotshelfAmount){
    this.purchaseNotshelfAmount=purchaseNotshelfAmount;
    }
    /*warehouseNum get 方法 */
    public Integer getWarehouseNum(){
    return warehouseNum;
    }
    /*warehouseNum set 方法 */
    public void setWarehouseNum(Integer  warehouseNum){
    this.warehouseNum=warehouseNum;
    }
    /*warehouseAmount get 方法 */
    public BigDecimal getWarehouseAmount(){
    return warehouseAmount;
    }
    /*warehouseAmount set 方法 */
    public void setWarehouseAmount(BigDecimal  warehouseAmount){
    this.warehouseAmount=warehouseAmount;
    }
    /*saleShelfNum get 方法 */
    public Integer getSaleShelfNum(){
    return saleShelfNum;
    }
    /*saleShelfNum set 方法 */
    public void setSaleShelfNum(Integer  saleShelfNum){
    this.saleShelfNum=saleShelfNum;
    }
    /*saleShelfAmount get 方法 */
    public BigDecimal getSaleShelfAmount(){
    return saleShelfAmount;
    }
    /*saleShelfAmount set 方法 */
    public void setSaleShelfAmount(BigDecimal  saleShelfAmount){
    this.saleShelfAmount=saleShelfAmount;
    }
    /*saleNoshelfNum get 方法 */
    public Integer getSaleNoshelfNum(){
    return saleNoshelfNum;
    }
    /*saleNoshelfNum set 方法 */
    public void setSaleNoshelfNum(Integer  saleNoshelfNum){
    this.saleNoshelfNum=saleNoshelfNum;
    }
    /*saleNoshelfAmount get 方法 */
    public BigDecimal getSaleNoshelfAmount(){
    return saleNoshelfAmount;
    }
    /*saleNoshelfAmount set 方法 */
    public void setSaleNoshelfAmount(BigDecimal  saleNoshelfAmount){
    this.saleNoshelfAmount=saleNoshelfAmount;
    }
    /*inDamagedNum get 方法 */
    public Integer getInDamagedNum(){
    return inDamagedNum;
    }
    /*inDamagedNum set 方法 */
    public void setInDamagedNum(Integer  inDamagedNum){
    this.inDamagedNum=inDamagedNum;
    }
    /*inDamagedAmount get 方法 */
    public BigDecimal getInDamagedAmount(){
    return inDamagedAmount;
    }
    /*inDamagedAmount set 方法 */
    public void setInDamagedAmount(BigDecimal  inDamagedAmount){
    this.inDamagedAmount=inDamagedAmount;
    }
    /*outDamagedNum get 方法 */
    public Integer getOutDamagedNum(){
    return outDamagedNum;
    }
    /*outDamagedNum set 方法 */
    public void setOutDamagedNum(Integer  outDamagedNum){
    this.outDamagedNum=outDamagedNum;
    }
    /*outDamagedAmount get 方法 */
    public BigDecimal getOutDamagedAmount(){
    return outDamagedAmount;
    }
    /*outDamagedAmount set 方法 */
    public void setOutDamagedAmount(BigDecimal  outDamagedAmount){
    this.outDamagedAmount=outDamagedAmount;
    }
    /*profitNum get 方法 */
    public Integer getProfitNum(){
    return profitNum;
    }
    /*profitNum set 方法 */
    public void setProfitNum(Integer  profitNum){
    this.profitNum=profitNum;
    }
    /*profitAmount get 方法 */
    public BigDecimal getProfitAmount(){
    return profitAmount;
    }
    /*profitAmount set 方法 */
    public void setProfitAmount(BigDecimal  profitAmount){
    this.profitAmount=profitAmount;
    }
    /*endNum get 方法 */
    public Integer getEndNum(){
    return endNum;
    }
    /*endNum set 方法 */
    public void setEndNum(Integer  endNum){
    this.endNum=endNum;
    }
    /*endAmount get 方法 */
    public BigDecimal getEndAmount(){
    return endAmount;
    }
    /*endAmount set 方法 */
    public void setEndAmount(BigDecimal  endAmount){
    this.endAmount=endAmount;
    }
    /*tzPrice get 方法 */
    public BigDecimal getTzPrice(){
    return tzPrice;
    }
    /*tzPrice set 方法 */
    public void setTzPrice(BigDecimal  tzPrice){
    this.tzPrice=tzPrice;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    this.setCurrencyLabel(DERP.getLabelByKey(DERP.currencyCodeList, currency));
    }
    /*tzEndAmount get 方法 */
    public BigDecimal getTzEndAmount(){
    return tzEndAmount;
    }
    /*tzEndAmount set 方法 */
    public void setTzEndAmount(BigDecimal  tzEndAmount){
    this.tzEndAmount=tzEndAmount;
    }
    /*differenceAmount get 方法 */
    public BigDecimal getDifferenceAmount(){
    return differenceAmount;
    }
    /*differenceAmount set 方法 */
    public void setDifferenceAmount(BigDecimal  differenceAmount){
    this.differenceAmount=differenceAmount;
    }
    /*closeDate get 方法 */
    public Timestamp getCloseDate(){
    return closeDate;
    }
    /*closeDate set 方法 */
    public void setCloseDate(Timestamp  closeDate){
    this.closeDate=closeDate;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*purchaseEndNum get 方法 */
    public Integer getPurchaseEndNum(){
    return purchaseEndNum;
    }
    /*purchaseEndNum set 方法 */
    public void setPurchaseEndNum(Integer  purchaseEndNum){
    this.purchaseEndNum=purchaseEndNum;
    }
    /*purchaseEndAmount get 方法 */
    public BigDecimal getPurchaseEndAmount(){
    return purchaseEndAmount;
    }
    /*purchaseEndAmount set 方法 */
    public void setPurchaseEndAmount(BigDecimal  purchaseEndAmount){
    this.purchaseEndAmount=purchaseEndAmount;
    }
    /*saleEndNum get 方法 */
    public Integer getSaleEndNum(){
    return saleEndNum;
    }
    /*saleEndNum set 方法 */
    public void setSaleEndNum(Integer  saleEndNum){
    this.saleEndNum=saleEndNum;
    }
    /*saleEndAmount get 方法 */
    public BigDecimal getSaleEndAmount(){
    return saleEndAmount;
    }
    /*saleEndAmount set 方法 */
    public void setSaleEndAmount(BigDecimal  saleEndAmount){
    this.saleEndAmount=saleEndAmount;
    }
    /*destroyNum get 方法 */
    public Integer getDestroyNum(){
    return destroyNum;
    }
    /*destroyNum set 方法 */
    public void setDestroyNum(Integer  destroyNum){
    this.destroyNum=destroyNum;
    }
    /*destroyAmount get 方法 */
    public BigDecimal getDestroyAmount(){
    return destroyAmount;
    }
    /*destroyAmount set 方法 */
    public void setDestroyAmount(BigDecimal  destroyAmount){
    this.destroyAmount=destroyAmount;
    }
    /*lossOverflowNum get 方法 */
    public Integer getLossOverflowNum(){
    return lossOverflowNum;
    }
    /*lossOverflowNum set 方法 */
    public void setLossOverflowNum(Integer  lossOverflowNum){
    this.lossOverflowNum=lossOverflowNum;
    }
    /*lossOverflowAmount get 方法 */
    public BigDecimal getLossOverflowAmount(){
    return lossOverflowAmount;
    }
    /*lossOverflowAmount set 方法 */
    public void setLossOverflowAmount(BigDecimal  lossOverflowAmount){
    this.lossOverflowAmount=lossOverflowAmount;
    }
    /*addPurchaseNotshelfNum get 方法 */
    public Integer getAddPurchaseNotshelfNum(){
    return addPurchaseNotshelfNum;
    }
    /*addPurchaseNotshelfNum set 方法 */
    public void setAddPurchaseNotshelfNum(Integer  addPurchaseNotshelfNum){
    this.addPurchaseNotshelfNum=addPurchaseNotshelfNum;
    }
    /*addPurchaseNotshelfAmount get 方法 */
    public BigDecimal getAddPurchaseNotshelfAmount(){
    return addPurchaseNotshelfAmount;
    }
    /*addPurchaseNotshelfAmount set 方法 */
    public void setAddPurchaseNotshelfAmount(BigDecimal  addPurchaseNotshelfAmount){
    this.addPurchaseNotshelfAmount=addPurchaseNotshelfAmount;
    }
    /*addSaleNoshelfNum get 方法 */
    public Integer getAddSaleNoshelfNum(){
    return addSaleNoshelfNum;
    }
    /*addSaleNoshelfNum set 方法 */
    public void setAddSaleNoshelfNum(Integer  addSaleNoshelfNum){
    this.addSaleNoshelfNum=addSaleNoshelfNum;
    }
    /*addSaleNoshelfAmount get 方法 */
    public BigDecimal getAddSaleNoshelfAmount(){
    return addSaleNoshelfAmount;
    }
    /*addSaleNoshelfAmount set 方法 */
    public void setAddSaleNoshelfAmount(BigDecimal  addSaleNoshelfAmount){
    this.addSaleNoshelfAmount=addSaleNoshelfAmount;
    }
    /*lossNum get 方法 */
    public Integer getLossNum(){
    return lossNum;
    }
    /*lossNum set 方法 */
    public void setLossNum(Integer  lossNum){
    this.lossNum=lossNum;
    }
    /*lossAmount get 方法 */
    public BigDecimal getLossAmount(){
    return lossAmount;
    }
    /*lossAmount set 方法 */
    public void setLossAmount(BigDecimal  lossAmount){
    this.lossAmount=lossAmount;
    }
    /*groupCommbarcode get 方法 */
    public String getGroupCommbarcode(){
    return groupCommbarcode;
    }
    /*groupCommbarcode set 方法 */
    public void setGroupCommbarcode(String  groupCommbarcode){
    this.groupCommbarcode=groupCommbarcode;
    }
    /*decreasePurchaseNotshelfNum get 方法 */
    public Integer getDecreasePurchaseNotshelfNum(){
    return decreasePurchaseNotshelfNum;
    }
    /*decreasePurchaseNotshelfNum set 方法 */
    public void setDecreasePurchaseNotshelfNum(Integer  decreasePurchaseNotshelfNum){
    this.decreasePurchaseNotshelfNum=decreasePurchaseNotshelfNum;
    }
    /*decreasePurchaseNotshelfAmount get 方法 */
    public BigDecimal getDecreasePurchaseNotshelfAmount(){
    return decreasePurchaseNotshelfAmount;
    }
    /*decreasePurchaseNotshelfAmount set 方法 */
    public void setDecreasePurchaseNotshelfAmount(BigDecimal  decreasePurchaseNotshelfAmount){
    this.decreasePurchaseNotshelfAmount=decreasePurchaseNotshelfAmount;
    }
    /*surplusNum get 方法 */
    public Integer getSurplusNum(){
    return surplusNum;
    }
    /*surplusNum set 方法 */
    public void setSurplusNum(Integer  surplusNum){
    this.surplusNum=surplusNum;
    }
    /*addTransferNoshelfNum get 方法 */
    public Integer getAddTransferNoshelfNum(){
    return addTransferNoshelfNum;
    }
    /*addTransferNoshelfNum set 方法 */
    public void setAddTransferNoshelfNum(Integer  addTransferNoshelfNum){
    this.addTransferNoshelfNum=addTransferNoshelfNum;
    }
    /*addTransferNoshelfAmount get 方法 */
    public BigDecimal getAddTransferNoshelfAmount(){
    return addTransferNoshelfAmount;
    }
    /*addTransferNoshelfAmount set 方法 */
    public void setAddTransferNoshelfAmount(BigDecimal  addTransferNoshelfAmount){
    this.addTransferNoshelfAmount=addTransferNoshelfAmount;
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
	public List<Long> getParentBrandIds() {
		return parentBrandIds;
	}
	public void setParentBrandIds(List<Long> parentBrandIds) {
		this.parentBrandIds = parentBrandIds;
	}

    public Integer getMoveInNum() {
        return moveInNum;
    }

    public void setMoveInNum(Integer moveInNum) {
        this.moveInNum = moveInNum;
    }

    public Integer getMoveOutNum() {
        return moveOutNum;
    }

    public void setMoveOutNum(Integer moveOutNum) {
        this.moveOutNum = moveOutNum;
    }

    public Integer getMoveNum() {
        return moveNum;
    }

    public void setMoveNum(Integer moveNum) {
        this.moveNum = moveNum;
    }

    public String getAccountPrice() {
        return accountPrice;
    }

    public void setAccountPrice(String accountPrice) {
        this.accountPrice = accountPrice;
        this.accountPriceLabel= DERP_SYS.getLabelByKey(DERP_SYS.merchantInfo_accountPriceList,accountPrice);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.setStatusLabel(DERP_REPORT.getLabelByKey(DERP_REPORT.financeInventorySummary_statusList, status));
    }
	public BigDecimal getSdInitAmount() {
		return sdInitAmount;
	}
	public void setSdInitAmount(BigDecimal sdInitAmount) {
		this.sdInitAmount = sdInitAmount;
	}
	public BigDecimal getSdPrice() {
		return sdPrice;
	}
	public void setSdPrice(BigDecimal sdPrice) {
		this.sdPrice = sdPrice;
	}

	public BigDecimal getSdWarehouseAmount() {
		return sdWarehouseAmount;
	}
	public void setSdWarehouseAmount(BigDecimal sdWarehouseAmount) {
		this.sdWarehouseAmount = sdWarehouseAmount;
	}

	public BigDecimal getSdTzPrice() {
		return sdTzPrice;
	}
	public void setSdTzPrice(BigDecimal sdTzPrice) {
		this.sdTzPrice = sdTzPrice;
	}

	public BigDecimal getSdSaleEndAmount() {
		return sdSaleEndAmount;
	}
	public void setSdSaleEndAmount(BigDecimal sdSaleEndAmount) {
		this.sdSaleEndAmount = sdSaleEndAmount;
	}

	public BigDecimal getSdLossOverflowAmount() {
		return sdLossOverflowAmount;
	}
	public void setSdLossOverflowAmount(BigDecimal sdLossOverflowAmount) {
		this.sdLossOverflowAmount = sdLossOverflowAmount;
	}

	public BigDecimal getSdEndAmount() {
		return sdEndAmount;
	}
	public void setSdEndAmount(BigDecimal sdEndAmount) {
		this.sdEndAmount = sdEndAmount;
	}

	public BigDecimal getSdAddPurchaseNotshelfAmount() {
		return sdAddPurchaseNotshelfAmount;
	}
	public void setSdAddPurchaseNotshelfAmount(BigDecimal sdAddPurchaseNotshelfAmount) {
		this.sdAddPurchaseNotshelfAmount = sdAddPurchaseNotshelfAmount;
	}
	public String getMonthStart() {
		return monthStart;
	}
	public void setMonthStart(String monthStart) {
		this.monthStart = monthStart;
	}
	public String getMonthEnd() {
		return monthEnd;
	}
	public void setMonthEnd(String monthEnd) {
		this.monthEnd = monthEnd;
	}

    public String getAccountPriceLabel() {
        return accountPriceLabel;
    }

    public void setAccountPriceLabel(String accountPriceLabel) {
        this.accountPriceLabel = accountPriceLabel;
    }
	public List<Long> getBuList() {
		return buList;
	}
	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
	public BigDecimal getSdInterestInitAmount() {
		return sdInterestInitAmount;
	}
	public void setSdInterestInitAmount(BigDecimal sdInterestInitAmount) {
		this.sdInterestInitAmount = sdInterestInitAmount;
	}
	public BigDecimal getSdInterestPrice() {
		return sdInterestPrice;
	}
	public void setSdInterestPrice(BigDecimal sdInterestPrice) {
		this.sdInterestPrice = sdInterestPrice;
	}
	public BigDecimal getSdInterestPurchaseEndAmount() {
		return sdInterestPurchaseEndAmount;
	}
	public void setSdInterestPurchaseEndAmount(BigDecimal sdInterestPurchaseEndAmount) {
		this.sdInterestPurchaseEndAmount = sdInterestPurchaseEndAmount;
	}
	public BigDecimal getSdInterestTzPrice() {
		return sdInterestTzPrice;
	}
	public void setSdInterestTzPrice(BigDecimal sdInterestTzPrice) {
		this.sdInterestTzPrice = sdInterestTzPrice;
	}
	public BigDecimal getSdInterestSaleEndAmount() {
		return sdInterestSaleEndAmount;
	}
	public void setSdInterestSaleEndAmount(BigDecimal sdInterestSaleEndAmount) {
		this.sdInterestSaleEndAmount = sdInterestSaleEndAmount;
	}
	public BigDecimal getSdInterestLossOverflowAmount() {
		return sdInterestLossOverflowAmount;
	}
	public void setSdInterestLossOverflowAmount(BigDecimal sdInterestLossOverflowAmount) {
		this.sdInterestLossOverflowAmount = sdInterestLossOverflowAmount;
	}
	public BigDecimal getSdInterestEndAmount() {
		return sdInterestEndAmount;
	}
	public void setSdInterestEndAmount(BigDecimal sdInterestEndAmount) {
		this.sdInterestEndAmount = sdInterestEndAmount;
	}
    
}
