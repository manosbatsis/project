package com.topideal.entity.dto;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel
public class YunjiAutomaticVerificationDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "账单月份")
    private String month;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "结算单号")
    private String settleId;

    @ApiModelProperty(value = "结算日期")
    private Date settleDate;

    @ApiModelProperty(value = "云集商品编码")
    private String skuNo;

    @ApiModelProperty(value = "平台发货量")
    private Integer platformDeliveryAccount;

    @ApiModelProperty(value = "平台退货量")
    private Integer platformReturnAccount;

    @ApiModelProperty(value = "系统商品货号")
    private String goodsNo;

    @ApiModelProperty(value = "系统发货量")
    private Integer systemDeliveryAccount;

    @ApiModelProperty(value = "系统退货量")
    private Integer systemReturnAccount;

    @ApiModelProperty(value = "出货差异")
    private Integer deliveryDifferent;

    @ApiModelProperty(value = "退货差异")
    private Integer returnDifferent;

    @ApiModelProperty(value = "原因")
    private String result;

    @ApiModelProperty(value = "创建日期")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改日期")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "只看差异")
    private String checkDifference;

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

    /*settleId get 方法 */
    public String getSettleId(){
        return settleId;
    }

    /*settleId set 方法 */
    public void setSettleId(String  settleId){
        this.settleId = settleId;
    }

    /*settleDate get 方法 */
    public Date getSettleDate(){
        return settleDate;
    }

    /*settleDate set 方法 */
    public void setSettleDate(Date  settleDate){
        this.settleDate = settleDate;
    }

    /*skuno get 方法 */
    public String getSkuNo(){
        return skuNo;
    }

    /*skuno set 方法 */
    public void setSkuNo(String  skuNo){
        this.skuNo = skuNo;
    }

    /*platformDeliveryAccount get 方法 */
    public Integer getPlatformDeliveryAccount(){
        return platformDeliveryAccount;
    }

    /*platformDeliveryAccount set 方法 */
    public void setPlatformDeliveryAccount(Integer  platformDeliveryAccount){
        this.platformDeliveryAccount = platformDeliveryAccount;
    }

    /*platformReturnAccount get 方法 */
    public Integer getPlatformReturnAccount(){
        return platformReturnAccount;
    }

    /*platformReturnAccount set 方法 */
    public void setPlatformReturnAccount(Integer  platformReturnAccount){
        this.platformReturnAccount = platformReturnAccount;
    }

    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }

    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo = goodsNo;
    }

    /*systemDeliveryAccount get 方法 */
    public Integer getSystemDeliveryAccount(){
        return systemDeliveryAccount;
    }

    /*systemDeliveryAccount set 方法 */
    public void setSystemDeliveryAccount(Integer  systemDeliveryAccount){
        this.systemDeliveryAccount = systemDeliveryAccount;
    }

    /*systemReturnAccount get 方法 */
    public Integer getSystemReturnAccount(){
        return systemReturnAccount;
    }

    /*systemReturnAccount set 方法 */
    public void setSystemReturnAccount(Integer  systemReturnAccount){
        this.systemReturnAccount = systemReturnAccount;
    }

    /*deliveryDifferent get 方法 */
    public Integer getDeliveryDifferent(){
        return deliveryDifferent;
    }

    /*deliveryDifferent set 方法 */
    public void setDeliveryDifferent(Integer  deliveryDifferent){
        this.deliveryDifferent = deliveryDifferent;
    }

    /*returnDifferent get 方法 */
    public Integer getReturnDifferent(){
        return returnDifferent;
    }

    /*returnDifferent set 方法 */
    public void setReturnDifferent(Integer  returnDifferent){
        this.returnDifferent = returnDifferent;
    }

    /*result get 方法 */
    public String getResult(){
        return result;
    }

    /*result set 方法 */
    public void setResult(String  result){
        this.result = result;
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

    public String getCheckDifference() {
        return checkDifference;
    }

    public void setCheckDifference(String checkDifference) {
        this.checkDifference = checkDifference;
    }


}
