package com.topideal.entity.vo.system;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 客户销售价格表
 * @author lian_
 */
public class CustomerSalePriceModel extends PageModel implements Serializable{


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
  	//主数据ID
  	private Long mainId;
 	//客户编码
 	private String customerCode;
	//表体id
	private Long priceId;
    // 币种  01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑
    private String currency;
    /**
    * 状态 006删除
    */
    private String status;
     // 储存选择过的id
 	private List ids;
    //单位名称
    private String unitName;
    //销售价格（RMB）
    private BigDecimal salePrice;
    //规格型号
    private String goodsSpec;
    //单位ID
    private Long unitId;
    //标准条形码
    private String commbarcode;
    //商品名称
    private String goodsName;
    //品牌名称
    private String brandName;
    //属性说明
    private Long productTypeId;
    //属性说明
    private String productTypeName;
    //
    private String isGroup;
	// 规格型号
	private String spec;
	// 属性说明
	private String goodsClassifyName;
	//
	private Long salePriceItemId;
    //事业部名称
    private String buName;
    //事业部id
    private Long buId;
    //提交人名称
    private String submitName;
    //提交人id
    private Long submitId;
    //提交时间
    private Timestamp submitDate;
    //备注
    private String remark;
    //编码(暂时用于关联附件)
    private String code;
 	
    
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
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getGoodsClassifyName() {
		return goodsClassifyName;
	}
	public void setGoodsClassifyName(String goodsClassifyName) {
		this.goodsClassifyName = goodsClassifyName;
	}
	public Long getSalePriceItemId() {
		return salePriceItemId;
	}
	public void setSalePriceItemId(Long salePriceItemId) {
		this.salePriceItemId = salePriceItemId;
	}
	public Long getPriceId() {
		return priceId;
	}
	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}
	public String getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
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
	public String getProductTypeName() {
		return productTypeName;
	}
	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
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
    /*customerName get 方法 */
    public String getCustomerName(){
        return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
        this.customerName=customerName;
    }
    /*goodsSpec get 方法 */
    public String getGoodsSpec(){
        return goodsSpec;
    }
    /*goodsSpec set 方法 */
    public void setGoodsSpec(String  goodsSpec){
        this.goodsSpec=goodsSpec;
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

    public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
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

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getSubmitName() {
        return submitName;
    }

    public void setSubmitName(String submitName) {
        this.submitName = submitName;
    }

    public Long getSubmitId() {
        return submitId;
    }

    public void setSubmitId(Long submitId) {
        this.submitId = submitId;
    }

    public Timestamp getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Timestamp submitDate) {
        this.submitDate = submitDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

