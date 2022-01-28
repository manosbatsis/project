package com.topideal.webapi.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.main.CustomerSalePriceDTO;

/**
 * 客户销售价格表
 * @author lian_
 */
public class CustomerSalePriceForm extends PageForm implements Serializable{

    //品牌id
    private Long brandId;
    //客户销售价格表头id
    private Long salePriceId;
     //修改日期
     private Timestamp modifyDate;
     //客户名称
     private String customerName;
     //商家名称
     private String merchantName;
     //价格失效时间
     private Timestamp expiryDate;
     //商家ID
     private Long merchantId;
     //客户ID
     private Long customerId;
     //id
     private Long id;
     //创建时间
     private Timestamp createDate;
     //价格生效时间
     private Timestamp effectiveDate;
     //1立即生效 0不立即生效
     private String immediateEffect;
     private String immediateEffectLabel;
  	//主数据ID
  	private Long mainId;
 	//客户编码
 	private String customerCode;
    // 币种  01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑
    private String currency;
    private String currencyLabel ;
    /**
    * 状态 006删除
    */
    private String status;
     
     //拓展字段
     // 储存选择过的id
 	private List ids;
 	//客户销售价格表体
 	private List<CustomerSalePriceDTO> itemList;
    //商品货号
    private String goodsNo;
    //单位名称
    private String unitName;
    //销售价格（RMB）
    private BigDecimal salePrice;
    //商品ID
    private Long goodsId;
    //规格型号
    private String spec;
    //单位ID
    private Long unitId;
    //条形码
    private String barcode;
    //商品名称
    private String goodsName;
    //品牌名称
    private String brandName;
    //属性说明
    private Long productTypeId;
    //属性说明
    private String goodsClassifyName;
    //
    private String isGroup;
    private String isGroupLabel ;
    //
    private Long salePriceItemId;
 	
    

    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
		if(currency != null) {
			this.currencyLabel = DERP_SYS.getLabelByKey(DERP.currencyCodeList, currency);
		}
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public Long getSalePriceId() {
		return salePriceId;
	}
	public void setSalePriceId(Long salePriceId) {
		this.salePriceId = salePriceId;
	}
	public Long getSalePriceItemId() {
		return salePriceItemId;
	}
	public void setSalePriceItemId(Long salePriceItemId) {
		this.salePriceItemId = salePriceItemId;
	}
	public String getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
		if(isGroup != null) {
			this.isGroupLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_isGroupList, isGroup);
		}
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public String getGoodsClassifyName() {
		return goodsClassifyName;
	}
	public void setGoodsClassifyName(String goodsClassifyName) {
		this.goodsClassifyName = goodsClassifyName;
	}
	public List<CustomerSalePriceDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<CustomerSalePriceDTO> itemList) {
		this.itemList = itemList;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public Long getMainId() {
		return mainId;
	}
	public void setMainId(Long mainId) {
		this.mainId = mainId;
	}
	public String getImmediateEffect() {
		return immediateEffect;
	}
	public void setImmediateEffect(String immediateEffect) {
		this.immediateEffect = immediateEffect;
		if(immediateEffect != null) {
			this.immediateEffectLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_immediateEffectList, immediateEffect);
		}
	}
	public List getIds() {
		return ids;
	}
	public void setIds(List ids) {
		this.ids = ids;
	}
	public Timestamp getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Timestamp effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	/*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*unitName get 方法 */
    public String getUnitName(){
        return unitName;
    }
    /*unitName set 方法 */
    public void setUnitName(String  unitName){
        this.unitName=unitName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*salePrice get 方法 */
    public BigDecimal getSalePrice(){
        return salePrice;
    }
    /*salePrice set 方法 */
    public void setSalePrice(BigDecimal  salePrice){
        this.salePrice=salePrice;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
        return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
        this.customerName=customerName;
    }
    /*spec get 方法 */
    public String getSpec(){
        return spec;
    }
    /*spec set 方法 */
    public void setSpec(String  spec){
        this.spec=spec;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*expiryDate get 方法 */
    public Timestamp getExpiryDate(){
        return expiryDate;
    }
    /*expiryDate set 方法 */
    public void setExpiryDate(Timestamp  expiryDate){
        this.expiryDate=expiryDate;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
        return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
        this.customerId=customerId;
    }
    /*unitId get 方法 */
    public Long getUnitId(){
        return unitId;
    }
    /*unitId set 方法 */
    public void setUnitId(Long  unitId){
        this.unitId=unitId;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*barcode get 方法 */
    public String getBarcode(){
        return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
        this.barcode=barcode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public String getImmediateEffectLabel() {
		return immediateEffectLabel;
	}
	public void setImmediateEffectLabel(String immediateEffectLabel) {
		this.immediateEffectLabel = immediateEffectLabel;
	}
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	public String getIsGroupLabel() {
		return isGroupLabel;
	}
	public void setIsGroupLabel(String isGroupLabel) {
		this.isGroupLabel = isGroupLabel;
	}

    




}

