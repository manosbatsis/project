package com.topideal.order.webapi.purchase.form;
import com.topideal.entity.dto.purchase.PurchaseInvoiceItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class PurchaseInvoiceAddForm implements Serializable{

	@ApiModelProperty("票据")
	private String token ;
    /**
    * 自增ID
    */
	@ApiModelProperty("自增ID")
    private Long id;
    /**
    * 编码
    */
	@ApiModelProperty("编码")
    private String code;
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
    * 采购订单ID
    */
	@ApiModelProperty("采购订单ID")
    private Long purchaseOrderId;
    /**
    * 采购订单号
    */
	@ApiModelProperty("采购订单号")
    private String purchaseOrderCode;
    /**
    * 事业部ID
    */
	@ApiModelProperty("事业部ID")
    private Long buId;
    /**
    * 事业部名
    */
	@ApiModelProperty("事业部名")
    private String buName;
    /**
    * 供应商ID
    */
	@ApiModelProperty("供应商ID")
    private Long supplierId;
    /**
    * 供应商
    */
	@ApiModelProperty("供应商")
    private String supplierName;
    /**
    * PO号
    */
	@ApiModelProperty("PO号")
    private String poNo;
    /**
    * 预计付款时间
    */
	@ApiModelProperty("预计付款时间字符串 yyyy-MM-dd HH:mm:ss")
	private String payDate;
    /**
    * 收到发票时间
    */
	@ApiModelProperty("收到发票时间字符串 yyyy-MM-dd HH:mm:ss")
	private String invoiceDate;
    /**
    * 开发票时间
    */
	@ApiModelProperty("开发票时间字符串 yyyy-MM-dd HH:mm:ss")
	private String drawInvoiceDate;
	/**
    * 发票号
    */
	@ApiModelProperty("发票号")
    private String invoiceNo;
    /**
    * 付款人
    */
	@ApiModelProperty("付款人")
    private String payName;

    /**
     * 采购币种
     */
	@ApiModelProperty("采购币种")
	private String currency;

	@ApiModelProperty("商品明细")
	private List<PurchaseInvoiceItemDTO> itemList ;

    @ApiModelProperty("操作标识：1 修改录入日期,2 修改 付款日期")
    private String tag;

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
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
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
    /*purchaseOrderId get 方法 */
    public Long getPurchaseOrderId(){
    return purchaseOrderId;
    }
    /*purchaseOrderId set 方法 */
    public void setPurchaseOrderId(Long  purchaseOrderId){
    this.purchaseOrderId=purchaseOrderId;
    }
    /*purchaseOrderCode get 方法 */
    public String getPurchaseOrderCode(){
    return purchaseOrderCode;
    }
    /*purchaseOrderCode set 方法 */
    public void setPurchaseOrderCode(String  purchaseOrderCode){
    this.purchaseOrderCode=purchaseOrderCode;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*supplierId get 方法 */
    public Long getSupplierId(){
    return supplierId;
    }
    /*supplierId set 方法 */
    public void setSupplierId(Long  supplierId){
    this.supplierId=supplierId;
    }
    /*supplierName get 方法 */
    public String getSupplierName(){
    return supplierName;
    }
    /*supplierName set 方法 */
    public void setSupplierName(String  supplierName){
    this.supplierName=supplierName;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*payName get 方法 */
    public String getPayName(){
    return payName;
    }
    /*payName set 方法 */
    public void setPayName(String  payName){
    this.payName=payName;
    }
    /*creater get 方法 */
	public List<PurchaseInvoiceItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<PurchaseInvoiceItemDTO> itemList) {
		this.itemList = itemList;
	}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDrawInvoiceDate() {
        return drawInvoiceDate;
    }

    public void setDrawInvoiceDate(String drawInvoiceDate) {
        this.drawInvoiceDate = drawInvoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
