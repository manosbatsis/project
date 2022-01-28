package com.topideal.entity.dto.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.dto.common.PlatformTemporaryCostOrderItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 平台暂估费用DTO
 */
@ApiModel
public class PlatformTemporaryCostOrderDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "暂估费用单号")
    private String code;

    @ApiModelProperty(value = "外部交易单号")
    private String externalCode;
    private List<String> externalCodeList;

    @ApiModelProperty(value = "电商订单号")
    private String orderCode;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "店铺编码")
    private String shopCode;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "电商平台编码")
    private String storePlatformCode;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "仓库ID")
    private Long depotId;

    @ApiModelProperty(value = "仓库名称")
    private String depotName;

    @ApiModelProperty(value = "发货时间")
    private Timestamp deliverDate;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "修改人id")
    private Long modifier;

    @ApiModelProperty(value = "修改人名称")
    private String modifyName;

    //表体信息
    @ApiModelProperty(value = "暂估费用总金额")
    private BigDecimal sumAmount;

    @ApiModelProperty(value = "结算总金额")
    private BigDecimal sumAmountCost;

    @ApiModelProperty(value = "平台费项id")
    private Long platformSettlementId;

    @ApiModelProperty(value = "表体数据")
    private List<PlatformTemporaryCostOrderItemDTO> itemList ;

    @ApiModelProperty(value = "发货日期开始时间")
    private String deliverStartDate;

    @ApiModelProperty(value = "发货日期束时间")
    private String deliverEndDate;

    private String ids;

    @ApiModelProperty(value = "电商平台名称")
    private String storePlatformName;

    @ApiModelProperty(hidden = true)
    private String month;

    /**
     * 事业部id
     */
    @ApiModelProperty(value = "事业部id")
    private Long buId;

    /**
     * 事业部名称
     */
    @ApiModelProperty(value = "事业部名称")
    private String buName;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Long customerId;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 店铺类型值编码 001:POP; 002:一件代发
     */
    @ApiModelProperty(value = "店铺类型值编码 001:POP; 002:一件代发")
    private String shopTypeCode;
    @ApiModelProperty(value = "店铺类型值编码中文 001:POP; 002:一件代发")
    private String shopTypeName;

    /**
     * 订单类型 0-发货单 1-退款单
     */
    @ApiModelProperty(value = "订单类型 0-发货单 1-退款单")
    private String orderType;

    @ApiModelProperty(value = "订单类型Label 0-发货单 1-退款单")
    private String orderTypeLabel;

    @ApiModelProperty(value = "退款单单号")
    private String returnOrderCode;

    private List<String> orderCodeList;

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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShopTypeCode() {
        return shopTypeCode;
    }

    public void setShopTypeCode(String shopTypeCode) {
        this.shopTypeCode = shopTypeCode;
        this.shopTypeName = DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode);
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
    }

    public String getDeliverStartDate() {
        return deliverStartDate;
    }

    public void setDeliverStartDate(String deliverStartDate) {
        this.deliverStartDate = deliverStartDate;
    }

    public String getDeliverEndDate() {
        return deliverEndDate;
    }

    public void setDeliverEndDate(String deliverEndDate) {
        this.deliverEndDate = deliverEndDate;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public BigDecimal getSumAmountCost() {
        return sumAmountCost;
    }

    public void setSumAmountCost(BigDecimal sumAmountCost) {
        this.sumAmountCost = sumAmountCost;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public List<PlatformTemporaryCostOrderItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<PlatformTemporaryCostOrderItemDTO> itemList) {
        this.itemList = itemList;
    }

    public Long getPlatformSettlementId() {
        return platformSettlementId;
    }

    public void setPlatformSettlementId(Long platformSettlementId) {
        this.platformSettlementId = platformSettlementId;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
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
    /*externalCode get 方法 */
    public String getExternalCode(){
        return externalCode;
    }
    /*externalCode set 方法 */
    public void setExternalCode(String  externalCode){
        this.externalCode=externalCode;
    }

    public List<String> getExternalCodeList() {
        return externalCodeList;
    }

    public void setExternalCodeList(List<String> externalCodeList) {
        this.externalCodeList = externalCodeList;
    }

    /*orderCode get 方法 */
    public String getOrderCode(){
        return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
        this.orderCode=orderCode;
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
        this.storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
    }
    /*currency get 方法 */
    public String getCurrency(){
        return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
        this.currency=currency;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
        return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
        this.deliverDate=deliverDate;
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
    /*modifier get 方法 */
    public Long getModifier(){
        return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
        this.modifier=modifier;
    }
    /*modifyName get 方法 */
    public String getModifyName(){
        return modifyName;
    }
    /*modifyName set 方法 */
    public void setModifyName(String  modifyName){
        this.modifyName=modifyName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getReturnOrderCode() {
        return returnOrderCode;
    }

    public void setReturnOrderCode(String returnOrderCode) {
        this.returnOrderCode = returnOrderCode;
    }

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
    }

    public List<String> getOrderCodeList() {
        return orderCodeList;
    }

    public void setOrderCodeList(List<String> orderCodeList) {
        this.orderCodeList = orderCodeList;
    }
}
