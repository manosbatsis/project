package com.topideal.entity.dto.sale;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class CutomerQuotaWarningItemDTO extends PageModel implements Serializable{
    
	@ApiModelProperty(value = "ID")
    private Long id;
    
	@ApiModelProperty(value = "预警ID")
    private Long waringId;
   
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
    
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
    
	@ApiModelProperty(value = "销售单号或应收订单号")
    private String code;
   
	@ApiModelProperty(value = "PO号")
    private String poNo;
    
	@ApiModelProperty(value = "明细类型 1-销售在途 2-待确认 3-待开票 4-待回款 ")
    private String type;
    
	@ApiModelProperty(value = "明细类型为应收时，存值，应收类型 1-应收 2-费用")
    private String receiveType;
    @ApiModelProperty(value = "应收类型（中文）")
    private String receiveTypeLabel;
   
	@ApiModelProperty(value = "数量")
    private Integer num;
   
	@ApiModelProperty(value = "金额（原币）")
    private BigDecimal amount;
    
	@ApiModelProperty(value = "折算汇率")
    private Double rate;
   
	@ApiModelProperty(value = "汇率日期")
    private Timestamp rateDate;
   
	@ApiModelProperty(value = "占用金额")
    private BigDecimal occupationAmount;
   
	@ApiModelProperty(value = "币种")
    private String currency;
  
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
  
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

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
    }
    /*receiveType get 方法 */
    public String getReceiveType(){
    return receiveType;
    }
    /*receiveType set 方法 */
    public void setReceiveType(String  receiveType){
    this.receiveType=receiveType;
    this.receiveTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.customerQuotaWarningItem_receiveTypeList,receiveType);
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

    public String getReceiveTypeLabel() {
        return receiveTypeLabel;
    }
}
