package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class PlatformPurchaseOrderItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 平台采购订单id
    */
    private Long orderId;
    /**
    * 平台商品名称
    */
    private String platformGoodsName;
    /**
    * 平台条码
    */
    private String platformBarcode;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 单价
    */
    private BigDecimal price;
    /**
    * 销售总金额
    */
    private BigDecimal amount;
    /**
    * 实收好品数量
    */
    private Integer receiptOkNum;
    /**
    * 实收坏品数量
    */
    private Integer receiptBadNum;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 平台货号
    */
    private String platformGoodsNo;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 账号
    */
    private String userCode;
    /**
     * 转销售商品ID
     */
    private Long saleGoodsId;
     /**
     * 转销售商品货号
     */
    private String saleGoodsNo;
     /**
     * 转销售商品名称
     */
    private String saleGoodsName;
     /**
     * 转销售数量
     */
    private Integer saleNum;
    /**
     * 转销售条形码
     */
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
