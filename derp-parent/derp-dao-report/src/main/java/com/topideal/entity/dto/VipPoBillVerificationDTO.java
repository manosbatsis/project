package com.topideal.entity.dto;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class VipPoBillVerificationDTO extends PageModel implements Serializable{

    /**
     * 记录ID
     */
    @ApiModelProperty("id")
    private Long id;
    /**
     * 商家ID
     */
    @ApiModelProperty("商家ID")
    private Long merchantId;
    /**
     * 商家名
     */
    @ApiModelProperty("商家名")
    private String merchantName;
    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private Long depotId;
    /**
     * 仓库名
     */
    @ApiModelProperty("仓库名")
    private String depotName;
    /**
     * PO号
     */
    @ApiModelProperty("PO号")
    private String po;
    /**
     * PO时间
     */
    @ApiModelProperty("PO时间")
    private Timestamp poDate;
    /**
     * 标准条码
     */
    @ApiModelProperty("标准条码")
    private String commbarcode;

    /**
     * 初次上架时间
     */
    @ApiModelProperty("初次上架时间")
    private Timestamp firstShelfDate;
    /**
     * 账单总量
     */
    @ApiModelProperty("账单总量")
    private Integer billTotalAccount;
    /**
     * 最近账单时间
     */
    @ApiModelProperty("最近账单时间")
    private Timestamp billRecentDate;
    /**
     * 出库总量
     */
    @ApiModelProperty("出库总量")
    private Integer outDepotTotalAccont;
    /**
     * 国检抽样
     */
    @ApiModelProperty("国检抽样")
    private Integer nationalInspectionSampleAccount;
    /**
     *销售退货量
     */
    @ApiModelProperty("销售退货量")
    private Integer saleReturnAccount;
    /**
     * 未结算量
     */
    @ApiModelProperty("未结算量")
    private Integer unsettledAccount;
    /**
     * 未核销量
     */
    @ApiModelProperty("未结算量")
    private Integer unverificateAccount;
    /**
     * 天数
     */
    @ApiModelProperty("天数")
    private Integer days;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;

    //上架总量 (正常量+残损量)
    @ApiModelProperty("上架总量 (正常量+残损量)")
    private Integer shelfTotalAccount;
    //上架总金额
    @ApiModelProperty("上架总金额")
    private BigDecimal shelfTotalAmount;

    //唯品红冲数量
    @ApiModelProperty("唯品红冲数量")
    private Integer vipHcAccount;

    //调拨入量
    @ApiModelProperty("调拨入量")
    private Integer transferInAccount;

    //调拨出量
    @ApiModelProperty("调拨出量")
    private Integer transferOutAccount;
    //完结状态 0-未完结，1-已完结
    @ApiModelProperty("完结状态 0-未完结，1-已完结")
    private String status;

    //完结时间
    @ApiModelProperty("完结时间")
    private Timestamp overDate;

    //操作人id
    @ApiModelProperty("操作人id")
    private Long operatorId;

    //操作人
    @ApiModelProperty("操作人")
    private String operator;

    //唯品报废数量
    @ApiModelProperty("唯品报废数量")
    private Integer scrapAccount;
    //盘盈数量
    @ApiModelProperty("盘盈数量")
    private Integer inventorySurplusAccount;
    //盘亏数量
    @ApiModelProperty("盘亏数量")
    private Integer inventoryDeficientAccount;
    //未出库量
    @ApiModelProperty("未出库量")
    private Integer undepotAccount;

    private String statusLabel;


    //上架残损量
    @ApiModelProperty("上架残损量")
    private Integer shelfDamagedAccount;
    //母品牌
    @ApiModelProperty("母品牌")
    private String superiorParentBrand;
    //标准品牌
    @ApiModelProperty("标准品牌")
    private String brandParent;
    /**
     * 事业部ID
     */
    @ApiModelProperty("事业部ID")
    private Long buId;
    /**
     * 事业部名称
     */
    @ApiModelProperty("事业部名称")
    private String buName;
    /**
     * 销售单价
     */
    @ApiModelProperty("销售单价")
    private BigDecimal salePrice;
    /**
     * 库存金额
     */
    @ApiModelProperty("库存金额")
    private BigDecimal inventoryAmount;
    /**
     * 销售币种
     */
    @ApiModelProperty("销售币种")
    private String currency;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;
    /**
     * 客户ID
     */
    @ApiModelProperty("客户ID")
    private Long customerId;
    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty(value = "事业部集合")
    private List<Long> buList;

    public String getStatusLabel() {
        return statusLabel;
    }
    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
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
    /*po get 方法 */
    public String getPo(){
        return po;
    }
    /*po set 方法 */
    public void setPo(String  po){
        this.po=po;
    }
    /*poDate get 方法 */
    public Timestamp getPoDate(){
        return poDate;
    }
    /*poDate set 方法 */
    public void setPoDate(Timestamp  poDate){
        this.poDate=poDate;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    /*firstShelfDate get 方法 */
    public Timestamp getFirstShelfDate(){
        return firstShelfDate;
    }
    /*firstShelfDate set 方法 */
    public void setFirstShelfDate(Timestamp  firstShelfDate){
        this.firstShelfDate=firstShelfDate;
    }
    /*billTotalAccount get 方法 */
    public Integer getBillTotalAccount(){
        return billTotalAccount;
    }
    /*billTotalAccount set 方法 */
    public void setBillTotalAccount(Integer  billTotalAccount){
        this.billTotalAccount=billTotalAccount;
    }
    /*billRecentDate get 方法 */
    public Timestamp getBillRecentDate(){
        return billRecentDate;
    }
    /*billRecentDate set 方法 */
    public void setBillRecentDate(Timestamp  billRecentDate){
        this.billRecentDate=billRecentDate;
    }
    /*outDepotTotalAccont get 方法 */
    public Integer getOutDepotTotalAccont(){
        return outDepotTotalAccont;
    }
    /*outDepotTotalAccont set 方法 */
    public void setOutDepotTotalAccont(Integer  outDepotTotalAccont){
        this.outDepotTotalAccont=outDepotTotalAccont;
    }
    /*nationalInspectionSampleAccount get 方法 */
    public Integer getNationalInspectionSampleAccount(){
        return nationalInspectionSampleAccount;
    }
    /*nationalInspectionSampleAccount set 方法 */
    public void setNationalInspectionSampleAccount(Integer  nationalInspectionSampleAccount){
        this.nationalInspectionSampleAccount=nationalInspectionSampleAccount;
    }
    /*saleReturnAccount get 方法 */
    public Integer getSaleReturnAccount(){
        return saleReturnAccount;
    }
    /*saleReturnAccount set 方法 */
    public void setSaleReturnAccount(Integer  saleReturnAccount){
        this.saleReturnAccount=saleReturnAccount;
    }
    /*unsettledAccount get 方法 */
    public Integer getUnsettledAccount(){
        return unsettledAccount;
    }
    /*unsettledAccount set 方法 */
    public void setUnsettledAccount(Integer  unsettledAccount){
        this.unsettledAccount=unsettledAccount;
    }
    /*unverificateAccount get 方法 */
    public Integer getUnverificateAccount(){
        return unverificateAccount;
    }
    /*unverificateAccount set 方法 */
    public void setUnverificateAccount(Integer  unverificateAccount){
        this.unverificateAccount=unverificateAccount;
    }
    /*days get 方法 */
    public Integer getDays(){
        return days;
    }
    /*days set 方法 */
    public void setDays(Integer  days){
        this.days=days;
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

    public Integer getShelfTotalAccount() {
        return shelfTotalAccount;
    }

    public void setShelfTotalAccount(Integer shelfTotalAccount) {
        this.shelfTotalAccount = shelfTotalAccount;
    }

    public BigDecimal getShelfTotalAmount() {
        return shelfTotalAmount;
    }

    public void setShelfTotalAmount(BigDecimal shelfTotalAmount) {
        this.shelfTotalAmount = shelfTotalAmount;
    }

    public Integer getVipHcAccount() {
        return vipHcAccount;
    }

    public void setVipHcAccount(Integer vipHcAccount) {
        this.vipHcAccount = vipHcAccount;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
        this.statusLabel=DERP_REPORT.getLabelByKey(DERP_REPORT.vipPoBillVerification_statusList, status);
    }

    public Timestamp getOverDate() {
        return overDate;
    }

    public void setOverDate(Timestamp overDate) {
        this.overDate = overDate;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getScrapAccount() {
        return scrapAccount;
    }

    public void setScrapAccount(Integer scrapAccount) {
        this.scrapAccount = scrapAccount;
    }

    public Integer getInventorySurplusAccount() {
        return inventorySurplusAccount;
    }

    public void setInventorySurplusAccount(Integer inventorySurplusAccount) {
        this.inventorySurplusAccount = inventorySurplusAccount;
    }

    public Integer getInventoryDeficientAccount() {
        return inventoryDeficientAccount;
    }

    public void setInventoryDeficientAccount(Integer inventoryDeficientAccount) {
        this.inventoryDeficientAccount = inventoryDeficientAccount;
    }

    public Integer getUndepotAccount() {
        return undepotAccount;
    }

    public void setUndepotAccount(Integer undepotAccount) {
        this.undepotAccount = undepotAccount;
    }
    public Integer getTransferInAccount() {
        return transferInAccount;
    }
    public void setTransferInAccount(Integer transferInAccount) {
        this.transferInAccount = transferInAccount;
    }
    public Integer getTransferOutAccount() {
        return transferOutAccount;
    }
    public void setTransferOutAccount(Integer transferOutAccount) {
        this.transferOutAccount = transferOutAccount;
    }

    public Integer getShelfDamagedAccount() {
        return shelfDamagedAccount;
    }

    public void setShelfDamagedAccount(Integer shelfDamagedAccount) {
        this.shelfDamagedAccount = shelfDamagedAccount;
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

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getInventoryAmount() {
        return inventoryAmount;
    }

    public void setInventoryAmount(BigDecimal inventoryAmount) {
        this.inventoryAmount = inventoryAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getBrandParent() {
        return brandParent;
    }

    public void setBrandParent(String brandParent) {
        this.brandParent = brandParent;
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

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }
}
