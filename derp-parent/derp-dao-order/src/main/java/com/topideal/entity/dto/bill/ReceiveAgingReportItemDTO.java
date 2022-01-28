package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class ReceiveAgingReportItemDTO extends PageModel implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 应收账龄id
     */
    @ApiModelProperty(value = "应收账龄id")
    private Long agingReportId;
    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型")
    private String orderType;
    private String orderTypeLabel;
    /**
     * PO号
     */
    @ApiModelProperty(value = "PO号")
    private String poNo;
    /**
     * 单据号
     */
    @ApiModelProperty(value = "单据号")
    private String code;
    /**
     * 应收月份
     */
    @ApiModelProperty(value = "应收月份")
    private String month;
    /**
     * 待核销金额
     */
    @ApiModelProperty(value = "待核销金额")
    private BigDecimal writtenOffAmount;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;


    private String merchantName;
    private String customerName;
    private String buName;
    private String simpleName;
    private String currency;
    private String channelType;


    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*agingReportId get 方法 */
    public Long getAgingReportId(){
        return agingReportId;
    }
    /*agingReportId set 方法 */
    public void setAgingReportId(Long  agingReportId){
        this.agingReportId=agingReportId;
    }
    /*orderType get 方法 */
    public String getOrderType(){
        return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
        this.orderType=orderType;
        this.orderTypeLabel= DERP.getLabelByKey(DERP_ORDER.receiveAging_orderTypeList, orderType);
    }

    public String getOrderTypeLabel() {
        return orderTypeLabel;
    }

    public void setOrderTypeLabel(String orderTypeLabel) {
        this.orderTypeLabel = orderTypeLabel;
    }

    /*poNo get 方法 */
    public String getPoNo(){
        return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
        this.poNo=poNo;
    }
    /*code get 方法 */
    public String getCode(){
        return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
        this.code=code;
    }
    /*month get 方法 */
    public String getMonth(){
        return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
        this.month=month;
    }
    /*writtenOffAmount get 方法 */
    public BigDecimal getWrittenOffAmount(){
        return writtenOffAmount;
    }
    /*writtenOffAmount set 方法 */
    public void setWrittenOffAmount(BigDecimal  writtenOffAmount){
        this.writtenOffAmount=writtenOffAmount;
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



}
