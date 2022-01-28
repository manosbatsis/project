package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PlatformPurchaseOrderItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "主键ID")
    private Long id;
    
	@ApiModelProperty(value = "平台采购订单id")
    private Long orderId;
  
	@ApiModelProperty(value = "平台商品名称")
    private String platformGoodsName;
   
	@ApiModelProperty(value = "平台条码")
    private String platformBarcode;

	@ApiModelProperty(value = "数量")
    private Integer num;
  
	@ApiModelProperty(value = "单价")
    private BigDecimal price;
  
	@ApiModelProperty(value = "销售总金额")
    private BigDecimal amount;
 
	@ApiModelProperty(value = "实收好品数量")
    private Integer receiptOkNum;

	@ApiModelProperty(value = "实收坏品数量")
    private Integer receiptBadNum;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "平台货号")
    private String platformGoodsNo;

	@ApiModelProperty(value = "商家ID")
    private Long merchantId;

	@ApiModelProperty(value = "账号")
    private String userCode;

	@ApiModelProperty(value = "PO号")
    private String poNo ;
	
	@ApiModelProperty(value = "转销售商品ID")
    private Long saleGoodsId;
   
	@ApiModelProperty(value = "转销售商品货号")
    private String saleGoodsNo;
    
	@ApiModelProperty(value = "转销售商品名称")
    private String saleGoodsName;
    
	@ApiModelProperty(value = "转销售数量")
    private Integer saleNum;

	@ApiModelProperty(value = "转销售条形码")
    private String saleBarcode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
    }
    /*platformGoodsName get 方法 */
    public String getPlatformGoodsName(){
    return platformGoodsName;
    }
    /*platformGoodsName set 方法 */
    public void setPlatformGoodsName(String  platformGoodsName){
    this.platformGoodsName=platformGoodsName;
    }
    /*platformBarcode get 方法 */
    public String getPlatformBarcode(){
    return platformBarcode;
    }
    /*platformBarcode set 方法 */
    public void setPlatformBarcode(String  platformBarcode){
    this.platformBarcode=platformBarcode;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*receiptOkNum get 方法 */
    public Integer getReceiptOkNum(){
    return receiptOkNum;
    }
    /*receiptOkNum set 方法 */
    public void setReceiptOkNum(Integer  receiptOkNum){
    this.receiptOkNum=receiptOkNum;
    }
    /*receiptBadNum get 方法 */
    public Integer getReceiptBadNum(){
    return receiptBadNum;
    }
    /*receiptBadNum set 方法 */
    public void setReceiptBadNum(Integer  receiptBadNum){
    this.receiptBadNum=receiptBadNum;
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
    /*platformGoodsNo get 方法 */
    public String getPlatformGoodsNo(){
    return platformGoodsNo;
    }
    /*platformGoodsNo set 方法 */
    public void setPlatformGoodsNo(String  platformGoodsNo){
    this.platformGoodsNo=platformGoodsNo;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public Long getSaleGoodsId() {
		return saleGoodsId;
	}
	public void setSaleGoodsId(Long saleGoodsId) {
		this.saleGoodsId = saleGoodsId;
	}
	public String getSaleGoodsNo() {
		return saleGoodsNo;
	}
	public void setSaleGoodsNo(String saleGoodsNo) {
		this.saleGoodsNo = saleGoodsNo;
	}
	public String getSaleGoodsName() {
		return saleGoodsName;
	}
	public void setSaleGoodsName(String saleGoodsName) {
		this.saleGoodsName = saleGoodsName;
	}
	public Integer getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}
	public String getSaleBarcode() {
		return saleBarcode;
	}
	public void setSaleBarcode(String saleBarcode) {
		this.saleBarcode = saleBarcode;
	}

}
