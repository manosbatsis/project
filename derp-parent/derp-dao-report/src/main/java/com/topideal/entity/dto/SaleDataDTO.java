package com.topideal.entity.dto;
import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class SaleDataDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id", required = false)
    private Long id;

	@ApiModelProperty(value = "事业部ID", required = false)
    private Long buId;
	@ApiModelProperty(value = "事业部ID集合", required = false)
    private List<Long> buIds;

	@ApiModelProperty(value = "事业部名称", required = false)
    private String buName;

	@ApiModelProperty(value = "公司id", required = false)
    private Long merchantId;

	@ApiModelProperty(value = "公司名称", required = false)
    private String merchantName;

	@ApiModelProperty(value = "出库仓库id", required = false)
    private Long outDepotId;

	@ApiModelProperty(value = "出库仓库名称", required = false)
    private String outDepotName;

	@ApiModelProperty(value = "客户名称", required = false)
    private String customerName;

	@ApiModelProperty(value = "客户ID", required = false)
    private Long customerId;

	@ApiModelProperty(value = "月份", required = false)
    private String month;

	@ApiModelProperty(value = "商品货号", required = false)
    private String goodsNo;

	@ApiModelProperty(value = "标准条码", required = false)
    private String commbarcode;

	@ApiModelProperty(value = "标准品牌", required = false)
    private String brandParent;

	@ApiModelProperty(value = "类型 1-按销售类型计划 2-按平台计划 3-按店铺类型", required = false)
    private String type;

	@ApiModelProperty(value = "电商平台编码", required = false)
    private String storePlatformCode;

	@ApiModelProperty(value = "电商平台名称", required = false)
    private String storePlatformName;

	@ApiModelProperty(value = "店铺编码", required = false)
    private String shopCode;

	@ApiModelProperty(value = "店铺名称", required = false)
    private String shopName;

	@ApiModelProperty(value = "数量", required = false)
    private Integer num;

	@ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;

	@ApiModelProperty(value = "销售币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑", required = false)
    private String saleCurrency;

	@ApiModelProperty(value = "销售金额", required = false)
    private BigDecimal saleAmount;

	@ApiModelProperty(value = "人民币汇率", required = false)
    private Double cnyRate;

	@ApiModelProperty(value = "人民币金额", required = false)
    private BigDecimal cnyAmount;

	@ApiModelProperty(value = "销售类型：1-购销A 2-购销B 3-一件代发 4-POP 5-代销", required = false)
    private String orderType;

	@ApiModelProperty(value = "渠道类型 To B,To C", required = false)
    private String channelType;

	@ApiModelProperty(value = "客户是否为内部公司 1-是 0-否", required = false)
    private String innerMerchantType;

	@ApiModelProperty(value = "购销A金额", required = false)
    private BigDecimal saleAmountA;
 
	@ApiModelProperty(value = "购销B金额", required = false)
    private BigDecimal saleAmountB;
 
	@ApiModelProperty(value = "pop金额", required = false)
    private BigDecimal popAmount;

	@ApiModelProperty(value = "代销金额", required = false)
    private BigDecimal agencySaleAmount;

	@ApiModelProperty(value = "代发金额", required = false)
    private BigDecimal agencyDeliverAmount;
	
	
    @ApiModelProperty(value = "开始年月", required = false)
    private String monthStart;
    @ApiModelProperty(value = "结束年月", required = false)
    private String monthEnd;
    @ApiModelProperty(value = "累计销售成本金额", required = false)
    private BigDecimal addWeightedAmount;
    @ApiModelProperty(value = "saleCurrencyLabel", required = false)
    private String saleCurrencyLabel;
    @ApiModelProperty(value = "商品名称", required = false)
    private String goodsName;
    private List<Long> parentBrandIds;//标准品牌id
    @ApiModelProperty(value = "母品牌" , required = false)
    private String superiorParentBrand;

    @ApiModelProperty(value = "是否按标准品牌 0-否，1-是 " , required = false)
    private String isParentBrand;

    @ApiModelProperty(value = "部门ID" , required = false)
    private Long departmentId;

    @ApiModelProperty(value = "部门ID集合" , required = false)
    private List<Long> departmentIds;

    @ApiModelProperty(value = "部门名称 " , required = false)
    private String departmentName;

    @ApiModelProperty(value = "母品牌ID " , required = false)
    private Long parentBrandId;

    @ApiModelProperty(value = "母品牌编码 " , required = false)
    private String parentBrandCode;

    @ApiModelProperty(value = "母品牌名称" , required = false)
    private String parentBrandName;

    @ApiModelProperty(value = "港币汇率" , required = false)
    private Double hkRate;

    @ApiModelProperty(value = "港币金额" , required = false)
    private BigDecimal hkAmount;

    @ApiModelProperty(value = "标准品牌ID" , required = false)
    private Long brandParentId;
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
    public Long getBrandParentId() {
        return brandParentId;
    }

    public void setBrandParentId(Long brandParentId) {
        this.brandParentId = brandParentId;
    }
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getParentBrandCode() {
        return parentBrandCode;
    }

    public void setParentBrandCode(String parentBrandCode) {
        this.parentBrandCode = parentBrandCode;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public Double getHkRate() {
        return hkRate;
    }

    public void setHkRate(Double hkRate) {
        this.hkRate = hkRate;
    }

    public BigDecimal getHkAmount() {
        return hkAmount;
    }

    public void setHkAmount(BigDecimal hkAmount) {
        this.hkAmount = hkAmount;
    }

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
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
    /*outDepotId get 方法 */
    public Long getOutDepotId(){
        return outDepotId;
    }
    /*outDepotId set 方法 */
    public void setOutDepotId(Long  outDepotId){
        this.outDepotId=outDepotId;
    }
    /*outDepotName get 方法 */
    public String getOutDepotName(){
        return outDepotName;
    }
    /*outDepotName set 方法 */
    public void setOutDepotName(String  outDepotName){
        this.outDepotName=outDepotName;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
        return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
        this.customerName=customerName;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
        return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
        this.customerId=customerId;
    }
    /*month get 方法 */
    public String getMonth(){
        return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
        this.month=month;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
        return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
        this.commbarcode=commbarcode;
    }
    /*brandParent get 方法 */
    public String getBrandParent(){
        return brandParent;
    }
    /*brandParent set 方法 */
    public void setBrandParent(String  brandParent){
        this.brandParent=brandParent;
    }
    /*type get 方法 */
    public String getType(){
        return type;
    }
    /*type set 方法 */
    public void setType(String  type){
        this.type=type;
    }
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
        return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
        this.storePlatformCode=storePlatformCode;
    }
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
        return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
        this.storePlatformName=storePlatformName;
    }
    /*shopCode get 方法 */
    public String getShopCode(){
        return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
        this.shopCode=shopCode;
    }
    /*shopName get 方法 */
    public String getShopName(){
        return shopName;
    }
    /*shopName set 方法 */
    public void setShopName(String  shopName){
        this.shopName=shopName;
    }
    /*num get 方法 */
    public Integer getNum(){
        return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
        this.num=num;
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

    public String getSaleCurrency() {
        return saleCurrency;
    }

    public void setSaleCurrency(String saleCurrency) {
        this.saleCurrency = saleCurrency;
        this.setSaleCurrencyLabel(DERP.getLabelByKey(DERP.currencyCodeList, saleCurrency));
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Double getCnyRate() {
        return cnyRate;
    }

    public void setCnyRate(Double cnyRate) {
        this.cnyRate = cnyRate;
    }

    public BigDecimal getCnyAmount() {
        return cnyAmount;
    }

    public void setCnyAmount(BigDecimal cnyAmount) {
        this.cnyAmount = cnyAmount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getInnerMerchantType() {
        return innerMerchantType;
    }

    public void setInnerMerchantType(String innerMerchantType) {
        this.innerMerchantType = innerMerchantType;
    }

    public List<Long> getBuIds() {
        return buIds;
    }

    public void setBuIds(List<Long> buIds) {
        this.buIds = buIds;
    }

    public BigDecimal getSaleAmountA() {
        return saleAmountA;
    }

    public void setSaleAmountA(BigDecimal saleAmountA) {
        this.saleAmountA = saleAmountA;
    }

    public BigDecimal getSaleAmountB() {
        return saleAmountB;
    }

    public void setSaleAmountB(BigDecimal saleAmountB) {
        this.saleAmountB = saleAmountB;
    }

    public BigDecimal getPopAmount() {
        return popAmount;
    }

    public void setPopAmount(BigDecimal popAmount) {
        this.popAmount = popAmount;
    }

    public BigDecimal getAgencySaleAmount() {
        return agencySaleAmount;
    }

    public void setAgencySaleAmount(BigDecimal agencySaleAmount) {
        this.agencySaleAmount = agencySaleAmount;
    }

    public BigDecimal getAgencyDeliverAmount() {
        return agencyDeliverAmount;
    }

    public void setAgencyDeliverAmount(BigDecimal agencyDeliverAmount) {
        this.agencyDeliverAmount = agencyDeliverAmount;
    }
	public String getMonthStart() {
		return monthStart;
	}
	public void setMonthStart(String monthStart) {
		this.monthStart = monthStart;
	}
	public String getMonthEnd() {
		return monthEnd;
	}
	public void setMonthEnd(String monthEnd) {
		this.monthEnd = monthEnd;
	}
	public BigDecimal getAddWeightedAmount() {
		return addWeightedAmount;
	}
	public void setAddWeightedAmount(BigDecimal addWeightedAmount) {
		this.addWeightedAmount = addWeightedAmount;
	}
	public String getSaleCurrencyLabel() {
		return saleCurrencyLabel;
	}
	public void setSaleCurrencyLabel(String saleCurrencyLabel) {
		this.saleCurrencyLabel = saleCurrencyLabel;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public List<Long> getParentBrandIds() {
		return parentBrandIds;
	}
	public void setParentBrandIds(List<Long> parentBrandIds) {
		this.parentBrandIds = parentBrandIds;
	}
	public String getSuperiorParentBrand() {
		return superiorParentBrand;
	}
	public void setSuperiorParentBrand(String superiorParentBrand) {
		this.superiorParentBrand = superiorParentBrand;
	}

    public String getIsParentBrand() {
        return isParentBrand;
    }

    public void setIsParentBrand(String isParentBrand) {
        this.isParentBrand = isParentBrand;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
    
}
