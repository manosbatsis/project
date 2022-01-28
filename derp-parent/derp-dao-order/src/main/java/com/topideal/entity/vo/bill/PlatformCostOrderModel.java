package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PlatformCostOrderModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 客户id
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 单号(自生成)
    */
    private String code;
    /**
    * 账单号
    */
    private String billCode;
    /**
    * 费用项目Id
    */
    private Long itemProjectId;
    /**
    * 费用项目名称
    */
    private String itemProjectName;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 金额
    */
    private BigDecimal amount;
    /**
    * 币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String currency;
    /**
    * 付款主体   1 补款 2 扣款
    */
    private String costType;
    /**
    * 单据状态：  1-待确认  2-已确认 3-已转账单
    */
    private String status;
    /**
    * 确认人
    */
    private Long confirmer;
    /**
    * 确认人名称
    */
    private String confirmName;
    /**
    * 提交时间
    */
    private Timestamp confirmDate;
    /**
    * 转账单人
    */
    private Long transferSliper;
    /**
    * 转账单人名称
    */
    private String transferSlipName;
    /**
    * 转账单时间
    */
    private Timestamp transferSlipDate;
    /**
    * 单据来源  1:爬虫账单
    */
    private String source;
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
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
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
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*billCode get 方法 */
    public String getBillCode(){
    return billCode;
    }
    /*billCode set 方法 */
    public void setBillCode(String  billCode){
    this.billCode=billCode;
    }
    /*itemProjectId get 方法 */
    public Long getItemProjectId(){
    return itemProjectId;
    }
    /*itemProjectId set 方法 */
    public void setItemProjectId(Long  itemProjectId){
    this.itemProjectId=itemProjectId;
    }
    /*itemProjectName get 方法 */
    public String getItemProjectName(){
    return itemProjectName;
    }
    /*itemProjectName set 方法 */
    public void setItemProjectName(String  itemProjectName){
    this.itemProjectName=itemProjectName;
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
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*costType get 方法 */
    public String getCostType(){
    return costType;
    }
    /*costType set 方法 */
    public void setCostType(String  costType){
    this.costType=costType;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*confirmer get 方法 */
    public Long getConfirmer(){
    return confirmer;
    }
    /*confirmer set 方法 */
    public void setConfirmer(Long  confirmer){
    this.confirmer=confirmer;
    }
    /*confirmName get 方法 */
    public String getConfirmName(){
    return confirmName;
    }
    /*confirmName set 方法 */
    public void setConfirmName(String  confirmName){
    this.confirmName=confirmName;
    }
    /*confirmDate get 方法 */
    public Timestamp getConfirmDate(){
    return confirmDate;
    }
    /*confirmDate set 方法 */
    public void setConfirmDate(Timestamp  confirmDate){
    this.confirmDate=confirmDate;
    }
    /*transferSliper get 方法 */
    public Long getTransferSliper(){
    return transferSliper;
    }
    /*transferSliper set 方法 */
    public void setTransferSliper(Long  transferSliper){
    this.transferSliper=transferSliper;
    }
    /*transferSlipName get 方法 */
    public String getTransferSlipName(){
    return transferSlipName;
    }
    /*transferSlipName set 方法 */
    public void setTransferSlipName(String  transferSlipName){
    this.transferSlipName=transferSlipName;
    }
    /*transferSlipDate get 方法 */
    public Timestamp getTransferSlipDate(){
    return transferSlipDate;
    }
    /*transferSlipDate set 方法 */
    public void setTransferSlipDate(Timestamp  transferSlipDate){
    this.transferSlipDate=transferSlipDate;
    }
    /*source get 方法 */
    public String getSource(){
    return source;
    }
    /*source set 方法 */
    public void setSource(String  source){
    this.source=source;
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
