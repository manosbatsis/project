package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class CutomerQuotaWarningItemModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 预警ID
    */
    private Long waringId;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 销售单号或应收订单号
    */
    private String code;
    /**
    * PO号
    */
    private String poNo;
    /**
    * 明细类型 1-销售在途 2-待确认 3-待开票 4-待回款 
    */
    private String type;
    /**
    * 明细类型为应收时，存值，应收类型 1-应收 2-费用
    */
    private String receiveType;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 金额（原币）
    */
    private BigDecimal amount;
    /**
    * 折算汇率
    */
    private Double rate;
    /**
    * 汇率日期
    */
    private Timestamp rateDate;
    /**
    * 占用金额
    */
    private BigDecimal occupationAmount;
    /**
    * 币种
    */
    private String currency;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
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






}
