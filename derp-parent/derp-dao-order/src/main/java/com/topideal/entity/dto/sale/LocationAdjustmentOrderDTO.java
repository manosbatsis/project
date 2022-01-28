package com.topideal.entity.dto.sale;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class LocationAdjustmentOrderDTO extends PageModel implements Serializable{


    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "库位调整单号")
    private String code;

    @ApiModelProperty(value = "调整单据类型")
    private String orderType;
    private String orderTypeLabel;

    @ApiModelProperty(value = "公司id")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "仓库id")
    private Long depotId;

    @ApiModelProperty(value = "仓库名称")
    private String depotName;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "平台编号")
    private String platformCode;

    @ApiModelProperty(value = "平台名称")
    private String platformName;

    @ApiModelProperty(value = "订单号")
    private String orderCode;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "调整数量")
    private Integer adjustNum;

    @ApiModelProperty(value = "库存类型 0：好品 1：坏品")
    private String inventoryType;

    @ApiModelProperty(value = "调增库位类型id")
    private Long inStockLocationTypeId;
    @ApiModelProperty(value = "调增库位类型名称")
    private String inStockLocationTypeName;

    @ApiModelProperty(value = "调减库位类型id")
    private Long outStockLocationTypeId;

    @ApiModelProperty(value = "调减库位类型")
    private String outStockLocationTypeName;

    @ApiModelProperty(value = "归属月份")
    private String month;

    @ApiModelProperty(value = "状态")
    private String status;
    private String statusLabel;

    @ApiModelProperty(value = "调整原因")
    private String reason;

    @ApiModelProperty(value = "创建人id")
    private Long creater;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "订单id", hidden = true)
    private Long orderId;

    @ApiModelProperty(value = "用户关联的事业部集合", hidden = true)
    private List<Long> buList;

    @ApiModelProperty(value = "勾选的单据id, 多个以英文逗号隔开")
    private String ids;

    //理货单位 00-托盘 01-箱 02-件
    @ApiModelProperty(value = "理货单位", required = false)
    private String tallyingUnit;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /*orderType get 方法 */
    public String getOrderType(){
    return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
    this.orderType=orderType;
    this.orderTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.locationAdjustmentOrder_orderTypeList, orderType);
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
    /*platformCode get 方法 */
    public String getPlatformCode(){
    return platformCode;
    }
    /*platformCode set 方法 */
    public void setPlatformCode(String  platformCode){
    this.platformCode=platformCode;
    }
    /*platformName get 方法 */
    public String getPlatformName(){
    return platformName;
    }
    /*platformName set 方法 */
    public void setPlatformName(String  platformName){
    this.platformName=platformName;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
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
    /*adjustNum get 方法 */
    public Integer getAdjustNum(){
    return adjustNum;
    }
    /*adjustNum set 方法 */
    public void setAdjustNum(Integer  adjustNum){
    this.adjustNum=adjustNum;
    }
    /*inventoryType get 方法 */
    public String getInventoryType(){
    return inventoryType;
    }
    /*inventoryType set 方法 */
    public void setInventoryType(String  inventoryType){
    this.inventoryType=inventoryType;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.locationAdjustmentOrder_statusList, status);
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Long getInStockLocationTypeId() {
        return inStockLocationTypeId;
    }

    public void setInStockLocationTypeId(Long inStockLocationTypeId) {
        this.inStockLocationTypeId = inStockLocationTypeId;
    }

    public String getInStockLocationTypeName() {
        return inStockLocationTypeName;
    }

    public void setInStockLocationTypeName(String inStockLocationTypeName) {
        this.inStockLocationTypeName = inStockLocationTypeName;
    }

    public Long getOutStockLocationTypeId() {
        return outStockLocationTypeId;
    }

    public void setOutStockLocationTypeId(Long outStockLocationTypeId) {
        this.outStockLocationTypeId = outStockLocationTypeId;
    }

    public String getOutStockLocationTypeName() {
        return outStockLocationTypeName;
    }

    public void setOutStockLocationTypeName(String outStockLocationTypeName) {
        this.outStockLocationTypeName = outStockLocationTypeName;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
    }
}
