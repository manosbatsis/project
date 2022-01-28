package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class ProjectQuotaWarningItemDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    @ApiModelProperty("记录ID")
    private Long id;
    /**
    * 预警ID
    */
    @ApiModelProperty("预警ID")
    private Long waringId;
    /**
    * 商家ID
    */
    @ApiModelProperty("商家ID")
    private Long merchantId;
    /**
    * 商家名称
    */
    @ApiModelProperty("商家名称")
    private String merchantName;
    /**
    * 采购单号或应收订单号
    */
    @ApiModelProperty("采购单号或应收订单号")
    private String code;
    /**
    * PO号（类型为采购时有值）
    */
    @ApiModelProperty("PO号（类型为采购时有值）")
    private String poNo;
    /**
    * 明细类型 1-采购 2-应收
    */
    @ApiModelProperty("明细类型 1-累计采购冻结金额 2-累计销售已回款金额 3.累计采购已付金额")
    private String type;
    @ApiModelProperty("明细类型 1-累计采购冻结金额 2-累计销售已回款金额 3.累计采购已付金额")
    private String typeLabel ;
    /**
    * 明细类型为应收时，存值，应收类型 1-应收 2-费用
    */
    @ApiModelProperty("明细类型为应收时，存值，应收类型 1-应收 2-费用")
    private String receiveType;
    @ApiModelProperty("明细类型为应收时，存值，应收类型 1-应收 2-费用")
    private String receiveTypeLabel;
    /**
    * 数量
    */
    @ApiModelProperty("数量")
    private Integer num;
    /**
    * 金额（原币）
    */
    @ApiModelProperty("金额（原币）")
    private BigDecimal amount;
    /**
    * 折算汇率
    */
    @ApiModelProperty("折算汇率")
    private Double rate;
    /**
    * 汇率日期
    */
    @ApiModelProperty("汇率日期")
    private Timestamp rateDate;
    /**
    * 占用金额
    */
    @ApiModelProperty("占用金额")
    private BigDecimal occupationAmount;
    /**
    * 币种
    */
    @ApiModelProperty("币种")
    private String currency;
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
    /**
     * 客户/供应商id
     */
    @ApiModelProperty("客户/供应商id")
    private Long customerId;
     /**
     * 客户/供应商名称
     */
    @ApiModelProperty("客户/供应商名称")
    private String customerName;
     /**
     * TO B,TO C
     */
    @ApiModelProperty("TO B,TO C")
    private String orderType;
     /**
     * 状态名称
     */
    @ApiModelProperty("状态名称")
    private String statusName;
     /**
     * 订单创建时间
     */
    @ApiModelProperty("订单创建时间")
    private Timestamp orderCreateDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*waringId get 方法 */
    public Long getWaringId(){
    return waringId;
    }
    /*waringId set 方法 */
    public void setWaringId(Long  waringId){
    this.waringId=waringId;
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
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    this.typeLabel= DERP_ORDER.getLabelByKey(DERP_ORDER.projectquotaWarningItem_TypeList, type) ;
    }
    /*receiveType get 方法 */
    public String getReceiveType(){
    return receiveType;
    }
    /*receiveType set 方法 */
    public void setReceiveType(String  receiveType){
    this.receiveType=receiveType;
    this.receiveTypeLabel= DERP_ORDER.getLabelByKey(DERP_ORDER.projectquotaWarningItem_receiveTypeList, receiveType) ;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*rate get 方法 */
    public Double getRate(){
    return rate;
    }
    /*rate set 方法 */
    public void setRate(Double  rate){
    this.rate=rate;
    }
    /*rateDate get 方法 */
    public Timestamp getRateDate(){
    return rateDate;
    }
    /*rateDate set 方法 */
    public void setRateDate(Timestamp  rateDate){
    this.rateDate=rateDate;
    }
    /*occupationAmount get 方法 */
    public BigDecimal getOccupationAmount(){
    return occupationAmount;
    }
    /*occupationAmount set 方法 */
    public void setOccupationAmount(BigDecimal  occupationAmount){
    this.occupationAmount=occupationAmount;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
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


    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String getReceiveTypeLabel() {
        return receiveTypeLabel;
    }

    public void setReceiveTypeLabel(String receiveTypeLabel) {
        this.receiveTypeLabel = receiveTypeLabel;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Timestamp getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(Timestamp orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }
}
