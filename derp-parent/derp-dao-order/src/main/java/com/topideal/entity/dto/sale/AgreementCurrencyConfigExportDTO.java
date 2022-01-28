package com.topideal.entity.dto.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class AgreementCurrencyConfigExportDTO extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 状态 032-已生效,006-已删除
    */
    private String status;
    private String statusLabel;
    /**
    * 移入事业部id
    */
    private Long inBuId;
    /**
    * 移入事业部名称
    */
    private String inBuName;
    /**
    * 移出事业部id
    */
    private Long outBuId;
    /**
    * 移出事业部名称
    */
    private String outBuName;
    /**
    * 协议币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String currency;
    private String currencyLabel;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 创建人id
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    // 扩展子弹
    // 当前登录用户绑定的移出事业部集合
    private List<Long> outBuList;
    // 当前登录用户绑定的移入事业部集合
    private List<Long> inBuList;
    /**
     * 商品货号
     */
    private String goodsNo;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 协议单价
     */
    private BigDecimal price;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
        if(StringUtils.isNotBlank(status)){
            this.statusLabel= DERP_ORDER.getLabelByKey(DERP_ORDER.declareOrder_statusList, status);
        }
    }
    /*inBuId get 方法 */
    public Long getInBuId(){
    return inBuId;
    }
    /*inBuId set 方法 */
    public void setInBuId(Long  inBuId){
    this.inBuId=inBuId;
    }
    /*inBuName get 方法 */
    public String getInBuName(){
    return inBuName;
    }
    /*inBuName set 方法 */
    public void setInBuName(String  inBuName){
    this.inBuName=inBuName;
    }
    /*outBuId get 方法 */
    public Long getOutBuId(){
    return outBuId;
    }
    /*outBuId set 方法 */
    public void setOutBuId(Long  outBuId){
    this.outBuId=outBuId;
    }
    /*outBuName get 方法 */
    public String getOutBuName(){
    return outBuName;
    }
    /*outBuName set 方法 */
    public void setOutBuName(String  outBuName){
    this.outBuName=outBuName;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
        this.currency=currency;
        if(StringUtils.isNotBlank(currency)){
            this.currencyLabel= DERP_ORDER.getLabelByKey(DERP.currencyCodeList, currency);
        }
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

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public List<Long> getOutBuList() {
        return outBuList;
    }

    public void setOutBuList(List<Long> outBuList) {
        this.outBuList = outBuList;
    }

    public List<Long> getInBuList() {
        return inBuList;
    }

    public void setInBuList(List<Long> inBuList) {
        this.inBuList = inBuList;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
