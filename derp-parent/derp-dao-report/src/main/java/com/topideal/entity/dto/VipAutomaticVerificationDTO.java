package com.topideal.entity.dto;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

@ApiModel
public class VipAutomaticVerificationDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "公司id")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "po号")
    private String poNo;

    @ApiModelProperty(value = "爬虫账单号")
    private String crawlerNo;

    @ApiModelProperty(value = "唯品SKU")
    private String crawlerGoodsNo;

    @ApiModelProperty(value = "账单销售总量")
    private Integer billSalesAccount;

    @ApiModelProperty(value = "系统销售出库总量")
    private Integer systemSalesOutAccount;

    @ApiModelProperty(value = "销售出库差异")
    private Integer salesOutDifference;

    @ApiModelProperty(value = "账单红冲总量")
    private Integer billHcAccount;

    @ApiModelProperty(value = "系统红冲总量")
    private Integer systemHcAccount;

    @ApiModelProperty(value = "红冲差异")
    private Integer hcDifference;

    @ApiModelProperty(value = "账单其他总量（调增）")
    private Integer billAdjustmentIncreaseAccount;

    @ApiModelProperty(value = "系统库存调整（调增）")
    private Integer systemAdjustmentIncreaseAccount;

    @ApiModelProperty(value = "调增差异")
    private Integer adjustmentIncreaseDifferent;

    @ApiModelProperty(value = "账单其他总量（调减）")
    private Integer billAdjustmentDecreaseAccount;

    @ApiModelProperty(value = "系统库存调整（调减）")
    private Integer systemAdjustmentDecreaseAccount;

    @ApiModelProperty(value = "调减差异")
    private Integer adjustmentDecreaseDifferent;

    @ApiModelProperty(value = "校验结果：1-已对平、0-未对平")
    private String verificationResult;
    @ApiModelProperty(value = "校验结果中文")
    private String verificationResultLabel;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*month get 方法 */
    public String getMonth(){
        return month;
    }

    /*month set 方法 */
    public void setMonth(String  month){
        this.month = month;
    }

    /*platform get 方法 */
    public String getPlatform(){
        return platform;
    }

    /*platform set 方法 */
    public void setPlatform(String  platform){
        this.platform = platform;
    }

    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }

    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId = merchantId;
    }

    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }

    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName = merchantName;
    }

    /*poNo get 方法 */
    public String getPoNo(){
        return poNo;
    }

    /*poNo set 方法 */
    public void setPoNo(String  poNo){
        this.poNo = poNo;
    }

    /*crawlerNo get 方法 */
    public String getCrawlerNo(){
        return crawlerNo;
    }

    /*crawlerNo set 方法 */
    public void setCrawlerNo(String  crawlerNo){
        this.crawlerNo = crawlerNo;
    }

    /*crawlerGoodsNo get 方法 */
    public String getCrawlerGoodsNo(){
        return crawlerGoodsNo;
    }

    /*crawlerGoodsNo set 方法 */
    public void setCrawlerGoodsNo(String  crawlerGoodsNo){
        this.crawlerGoodsNo = crawlerGoodsNo;
    }

    /*billSalesAccount get 方法 */
    public Integer getBillSalesAccount(){
        return billSalesAccount;
    }

    /*billSalesAccount set 方法 */
    public void setBillSalesAccount(Integer  billSalesAccount){
        this.billSalesAccount = billSalesAccount;
    }

    /*systemSalesOutAccount get 方法 */
    public Integer getSystemSalesOutAccount(){
        return systemSalesOutAccount;
    }

    /*systemSalesOutAccount set 方法 */
    public void setSystemSalesOutAccount(Integer  systemSalesOutAccount){
        this.systemSalesOutAccount = systemSalesOutAccount;
    }

    /*salesOutDifference get 方法 */
    public Integer getSalesOutDifference(){
        return salesOutDifference;
    }

    /*salesOutDifference set 方法 */
    public void setSalesOutDifference(Integer  salesOutDifference){
        this.salesOutDifference = salesOutDifference;
    }

    /*billHcAccount get 方法 */
    public Integer getBillHcAccount(){
        return billHcAccount;
    }

    /*billHcAccount set 方法 */
    public void setBillHcAccount(Integer  billHcAccount){
        this.billHcAccount = billHcAccount;
    }

    /*systemHcAccount get 方法 */
    public Integer getSystemHcAccount(){
        return systemHcAccount;
    }

    /*systemHcAccount set 方法 */
    public void setSystemHcAccount(Integer  systemHcAccount){
        this.systemHcAccount = systemHcAccount;
    }

    /*hcDifference get 方法 */
    public Integer getHcDifference(){
        return hcDifference;
    }

    /*hcDifference set 方法 */
    public void setHcDifference(Integer  hcDifference){
        this.hcDifference = hcDifference;
    }

    /*billAdjustmentIncreaseAccount get 方法 */
    public Integer getBillAdjustmentIncreaseAccount(){
        return billAdjustmentIncreaseAccount;
    }

    /*billAdjustmentIncreaseAccount set 方法 */
    public void setBillAdjustmentIncreaseAccount(Integer  billAdjustmentIncreaseAccount){
        this.billAdjustmentIncreaseAccount = billAdjustmentIncreaseAccount;
    }

    /*systemAdjustmentIncreaseAccount get 方法 */
    public Integer getSystemAdjustmentIncreaseAccount(){
        return systemAdjustmentIncreaseAccount;
    }

    /*systemAdjustmentIncreaseAccount set 方法 */
    public void setSystemAdjustmentIncreaseAccount(Integer  systemAdjustmentIncreaseAccount){
        this.systemAdjustmentIncreaseAccount = systemAdjustmentIncreaseAccount;
    }

    /*adjustmentIncreaseDifferent get 方法 */
    public Integer getAdjustmentIncreaseDifferent(){
        return adjustmentIncreaseDifferent;
    }

    /*adjustmentIncreaseDifferent set 方法 */
    public void setAdjustmentIncreaseDifferent(Integer  adjustmentIncreaseDifferent){
        this.adjustmentIncreaseDifferent = adjustmentIncreaseDifferent;
    }

    /*billAdjustmentDecreaseAccount get 方法 */
    public Integer getBillAdjustmentDecreaseAccount(){
        return billAdjustmentDecreaseAccount;
    }

    /*billAdjustmentDecreaseAccount set 方法 */
    public void setBillAdjustmentDecreaseAccount(Integer  billAdjustmentDecreaseAccount){
        this.billAdjustmentDecreaseAccount = billAdjustmentDecreaseAccount;
    }

    /*systemAdjustmentDecreaseAccount get 方法 */
    public Integer getSystemAdjustmentDecreaseAccount(){
        return systemAdjustmentDecreaseAccount;
    }

    /*systemAdjustmentDecreaseAccount set 方法 */
    public void setSystemAdjustmentDecreaseAccount(Integer  systemAdjustmentDecreaseAccount){
        this.systemAdjustmentDecreaseAccount = systemAdjustmentDecreaseAccount;
    }

    /*adjustmentDecreaseDifferent get 方法 */
    public Integer getAdjustmentDecreaseDifferent(){
        return adjustmentDecreaseDifferent;
    }

    /*adjustmentDecreaseDifferent set 方法 */
    public void setAdjustmentDecreaseDifferent(Integer  adjustmentDecreaseDifferent){
        this.adjustmentDecreaseDifferent = adjustmentDecreaseDifferent;
    }

    /*verificationResult get 方法 */
    public String getVerificationResult(){
        return verificationResult;
    }

    /*verificationResult set 方法 */
    public void setVerificationResult(String  verificationResult) {
        this.verificationResult = verificationResult;
        this.verificationResultLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.vipAutoVeri_verificationResultList, verificationResult);
    }

    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate = createDate;
    }

    /*modifyDate get 方法 */
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getVerificationResultLabel() {
        return verificationResultLabel;
    }

    public void setVerificationResultLabel(String verificationResultLabel) {
        this.verificationResultLabel = verificationResultLabel;
    }


}
