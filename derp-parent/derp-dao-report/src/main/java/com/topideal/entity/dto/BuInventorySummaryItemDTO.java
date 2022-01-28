package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class BuInventorySummaryItemDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value = "id")
    private Long id;
    /**
    * 单号
    */
	@ApiModelProperty(value = "单号")
    private String code;
    /**
    * 业务单号
    */
	@ApiModelProperty(value = "业务单号")
    private String businessNo;
    /**
    * 合同号
    */
	@ApiModelProperty(value = "合同号")
    private String contractNo;
    /**
    * po单号
    */
	@ApiModelProperty(value = "po单号")
    private String poNo;
    /**
    * 单据类型
    */
	@ApiModelProperty(value = "单据类型")
    private String sourceType;
    /**
    * 操作类型  0 调减 1调增
    */
	@ApiModelProperty(value = "操作类型  0 调减 1调增")
    private String operateType;
    /**
    * 客户ID
    */
	@ApiModelProperty(value = "客户ID")
    private Long customerId;
    /**
    * 客户名称
    */
	@ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
    * 商家ID
    */
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
    /**
    * 商家名称
    */
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
    /**
    * 仓库ID
    */
	@ApiModelProperty(value = "仓库ID")
    private Long depotId;
    /**
    * 仓库名称
    */
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
    /**
    * 事业部ID
    */
	@ApiModelProperty(value = "事业部ID")
    private Long buId;
    /**
    * 事业部名称
    */
	@ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
    * 出入时间
    */
	@ApiModelProperty(value = "出入时间")
    private Timestamp divergenceDate;
    /**
    * 归属月份
    */
	@ApiModelProperty(value = "归属月份")
    private String ownMonth;
    /**
    * 货品名称
    */
	@ApiModelProperty(value = "货品名称")
    private String productName;
    /**
    * 条码
    */
	@ApiModelProperty(value = "条码")
    private String barcode;
    /**
    * 工厂编码
    */
	@ApiModelProperty(value = "工厂编码")
    private String factoryNo;
    /**
    * 商品ID
    */
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;
    /**
    * 商品货号
    */
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
    /**
    * 商品名称
    */
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
    * 数量
    */
	@ApiModelProperty(value = "数量")
    private Integer num;
    /**
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 备注
    */
	@ApiModelProperty(value = "备注")
    private String remark;
    /**
    * 事物类型名称
    */
	@ApiModelProperty(value = "事物类型名称")
    private String thingName;
    /**
    * 理货单位 0 托盘 1箱  2件
    */
	@ApiModelProperty(value = "理货单位 0 托盘 1箱  2件")
    private String unit;
    /**
    * 明细类型：001 出入库明细 002 损益明细
    */
	@ApiModelProperty(value = "明细类型：001 出入库明细 002 损益明细")
    private String detailType;
    /**
    * 原始批次号
    */
	@ApiModelProperty(value = "原始批次号")
    private String batchNo;
    /**
    * 是否坏品 0 好品 1坏品
    */
	@ApiModelProperty(value = "是否坏品 0 好品 1坏品")
    private String isWorn;
    /**
    * 生产日期
    */
	@ApiModelProperty(value = "生产日期")
    private Date productionDate;
    /**
    * 失效日期
    */
	@ApiModelProperty(value = "失效日期")
    private Date overdueDate;
    /**
    * 电商平台名称
    */
	@ApiModelProperty(value = "电商平台名称")
    private String storePlatformName;
    /**
    * 店铺编码
    */
	@ApiModelProperty(value = "店铺编码")
    private String shopCode;
    /**
    * 店铺名称
    */
	@ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
    * 电商平台编码
    */
	@ApiModelProperty(value = "电商平台编码")
    private String storePlatformCode;
    /**
    * 调入仓库id
    */
	@ApiModelProperty(value = "调入仓库id")
    private Long inDepotId;
    /**
    * 调入仓库名称
    */
	@ApiModelProperty(value = "调入仓库名称")
    private String inDepotName;
    /**
    * 标准条码
    */
	@ApiModelProperty(value = "标准条码")
    private String commbarcode;
    /**
    * 平台订单号
    */
	@ApiModelProperty(value = "平台订单号")
    private String exOrderId;
	@ApiModelProperty(value = "operateTypeLabel")
    private String operateTypeLabel;
	@ApiModelProperty(value = "sourceTypeLabel")
    private String sourceTypeLabel;
	@ApiModelProperty(value = "unitLabel")
    private String unitLabel;
	@ApiModelProperty(value = "detailTypeLabel")
    private String detailTypeLabel;
	@ApiModelProperty(value = "isWornLabel")
    private String isWornLabel;
    /**
     * 上级母品牌
     */
    @ApiModelProperty(value = "上级母品牌")
    private String superiorParentBrand;

    /**
     * 库位类型id
     */
    @ApiModelProperty(value = "库位类型id")
    private Long stockLocationTypeId;
    /**
     * 库位类型名称
     */
    @ApiModelProperty(value = "库位类型名称")
    private String stockLocationTypeName;

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

    public String getSuperiorParentBrand() {
		return superiorParentBrand;
	}
	public void setSuperiorParentBrand(String superiorParentBrand) {
		this.superiorParentBrand = superiorParentBrand;
	}
	public String getOperateTypeLabel() {
		return operateTypeLabel;
	}
	public void setOperateTypeLabel(String operateTypeLabel) {
		this.operateTypeLabel = operateTypeLabel;
	}
	public String getSourceTypeLabel() {
		return sourceTypeLabel;
	}
	public void setSourceTypeLabel(String sourceTypeLabel) {
		this.sourceTypeLabel = sourceTypeLabel;
	}
	public String getUnitLabel() {
		return unitLabel;
	}
	public void setUnitLabel(String unitLabel) {
		this.unitLabel = unitLabel;
	}
	public String getDetailTypeLabel() {
		return detailTypeLabel;
	}
	public void setDetailTypeLabel(String detailTypeLabel) {
		this.detailTypeLabel = detailTypeLabel;
	}
	public String getIsWornLabel() {
		return isWornLabel;
	}
	public void setIsWornLabel(String isWornLabel) {
		this.isWornLabel = isWornLabel;
	}
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
    /*businessNo get 方法 */
    public String getBusinessNo(){
    return businessNo;
    }
    /*businessNo set 方法 */
    public void setBusinessNo(String  businessNo){
    this.businessNo=businessNo;
    }
    /*contractNo get 方法 */
    public String getContractNo(){
    return contractNo;
    }
    /*contractNo set 方法 */
    public void setContractNo(String  contractNo){
    this.contractNo=contractNo;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*sourceType get 方法 */
    public String getSourceType(){
    return sourceType;
    }
    /*sourceType set 方法 */
    public void setSourceType(String  sourceType){
    this.sourceType=sourceType;
    this.sourceTypeLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.inventory_sourceTypeList, sourceType);
    }
    /*operateType get 方法 */
    public String getOperateType(){
    return operateType;
    }
    /*operateType set 方法 */
    public void setOperateType(String  operateType){
    this.operateType=operateType;
    this.operateTypeLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.inventory_operateTypeList, operateType);

    }
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
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
    /*divergenceDate get 方法 */
    public Timestamp getDivergenceDate(){
    return divergenceDate;
    }
    /*divergenceDate set 方法 */
    public void setDivergenceDate(Timestamp  divergenceDate){
    this.divergenceDate=divergenceDate;
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
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
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
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*thingName get 方法 */
    public String getThingName(){
    return thingName;
    }
    /*thingName set 方法 */
    public void setThingName(String  thingName){
    this.thingName=thingName;
    }
    /*unit get 方法 */
    public String getUnit(){
    return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
    this.unit=unit;
    this.unitLabel = DERP.getLabelByKey(DERP.inventory_unitList, unit);
    }
    /*detailType get 方法 */
    public String getDetailType(){
    return detailType;
    }
    /*detailType set 方法 */
    public void setDetailType(String  detailType){
    this.detailType=detailType;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
    return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
    this.batchNo=batchNo;
    }
    /*isWorn get 方法 */
    public String getIsWorn(){
    return isWorn;
    }
    /*isWorn set 方法 */
    public void setIsWorn(String  isWorn){
    this.isWorn=isWorn;
    this.isWornLabel=DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, isWorn);
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
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
    return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
    this.storePlatformName=storePlatformName;
    }
    /*shopCode get 方法 */
    public String getShopCode(){
    return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
    this.shopCode=shopCode;
    }
    /*shopName get 方法 */
    public String getShopName(){
    return shopName;
    }
    /*shopName set 方法 */
    public void setShopName(String  shopName){
    this.shopName=shopName;
    }
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
    return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
    this.storePlatformCode=storePlatformCode;
    }
    /*inDepotId get 方法 */
    public Long getInDepotId(){
    return inDepotId;
    }
    /*inDepotId set 方法 */
    public void setInDepotId(Long  inDepotId){
    this.inDepotId=inDepotId;
    }
    /*inDepotName get 方法 */
    public String getInDepotName(){
    return inDepotName;
    }
    /*inDepotName set 方法 */
    public void setInDepotName(String  inDepotName){
    this.inDepotName=inDepotName;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*exOrderId get 方法 */
    public String getExOrderId(){
    return exOrderId;
    }
    /*exOrderId set 方法 */
    public void setExOrderId(String  exOrderId){
    this.exOrderId=exOrderId;
    }






}
