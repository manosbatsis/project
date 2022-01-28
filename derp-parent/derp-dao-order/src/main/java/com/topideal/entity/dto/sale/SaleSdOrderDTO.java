package com.topideal.entity.dto.sale;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class SaleSdOrderDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "销售SD编码")
    private String code;

	@ApiModelProperty(value = "事业部ID")
    private Long buId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "公司ID")
    private Long merchantId;

	@ApiModelProperty(value = "公司名称")
    private String merchantName;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;

	@ApiModelProperty(value = "客户名称")
    private String customerName;

	@ApiModelProperty(value = "po号")
    private String poNo;

	@ApiModelProperty(value = "销售SD类型id")
    private Long sdTypeId;

    @ApiModelProperty(value = "销售SD类型")
    private String sdType;

	@ApiModelProperty(value = "销售SD类型名称")
    private String sdTypeName;

	@ApiModelProperty(value = "币种")
    private String currency;

	@ApiModelProperty(value = "SD总数量")
	private Integer totalSdNum;

	@ApiModelProperty(value = "SD总金额")
	private BigDecimal totalSdAmount;

	@ApiModelProperty(value = "订单状态:006-已删除")
	private String state;

	@ApiModelProperty(value = "创建人id",hidden=true)
    private Long creater;

	@ApiModelProperty(value = "创建人名称",hidden=true)
    private String createName;

	@ApiModelProperty(value = "创建时间",hidden=true)
    private Timestamp createDate;

	@ApiModelProperty(value = "修改人id",hidden=true)
    private Long modifier;

	@ApiModelProperty(value = "修改人名称",hidden=true)
    private String modifiyName;

	@ApiModelProperty(value = "修改时间",hidden=true)
    private Timestamp modifyDate;

	@ApiModelProperty(value = "表体集合")
	private List<SaleSdOrderItemDTO> itemList;

	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;

	@ApiModelProperty(value = "上架开始时间")
	private String startShelfDate;

	@ApiModelProperty(value = "上架结束时间")
	private String endShelfDate;

	@ApiModelProperty(value = "id集合，多个用逗号隔开")
	private String ids;

    @ApiModelProperty(value = "数据来源 1-系统生成 2-手工导入")
    private String orderSource;

    @ApiModelProperty(value = "数据来源(中文)")
    private String orderSourceLabel;

    @ApiModelProperty(value = "来源id")
    private Long orderId;

    @ApiModelProperty(value = "来源单号")
    private String orderCode;

    @ApiModelProperty(value = "业务Id")
    private Long businessId;

    @ApiModelProperty(value = "业务单号")
    private String businessCode;

    @ApiModelProperty(value = "单据类型 1-上架单 2-销售退入库单")
    private String orderType;

    @ApiModelProperty(value = "归属日期")
    private Timestamp ownDate;

    @ApiModelProperty(value = "归属开始日期")
    private String startOwnDate;

    @ApiModelProperty(value = "归属结束日期")
    private String endOwnDate;

    @ApiModelProperty(value = "是否红冲单：0-否，1-是")
    private String isWriteOff;
    @ApiModelProperty(value = "是否红冲单(中文)")
    private String isWriteOffLabel;

    /**
     * 原销售SD单id
     */
    @ApiModelProperty(value = "原销售SD单id")
    private Long originalSaleSdOrderId;
    /**
     * 原销售SD单号
     */
    @ApiModelProperty(value = "原销售SD单号")
    private String originalSaleSdOrderCode;

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
    public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*sdTypeId get 方法 */
    public Long getSdTypeId(){
    return sdTypeId;
    }
    /*sdTypeId set 方法 */
    public void setSdTypeId(Long  sdTypeId){
    this.sdTypeId=sdTypeId;
    }
    public String getSdType() {
        return sdType;
    }
    public void setSdType(String sdType) {
        this.sdType = sdType;
    }
    /*sdTypeName get 方法 */
    public String getSdTypeName(){
    return sdTypeName;
    }
    /*sdTypeName set 方法 */
    public void setSdTypeName(String  sdTypeName){
    this.sdTypeName=sdTypeName;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*totalSdNum get 方法 */
    public Integer getTotalSdNum(){
    return totalSdNum;
    }
    /*totalSdNum set 方法 */
    public void setTotalSdNum(Integer  totalSdNum){
    this.totalSdNum=totalSdNum;
    }
    /*totalSdAmount get 方法 */
    public BigDecimal getTotalSdAmount(){
    return totalSdAmount;
    }
    /*totalSdAmount set 方法 */
    public void setTotalSdAmount(BigDecimal  totalSdAmount){
    this.totalSdAmount=totalSdAmount;
    }
    /*state get 方法 */
    public String getState(){
    return state;
    }
    /*state set 方法 */
    public void setState(String  state){
    this.state=state;
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
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifiyName get 方法 */
    public String getModifiyName(){
    return modifiyName;
    }
    /*modifiyName set 方法 */
    public void setModifiyName(String  modifiyName){
    this.modifiyName=modifiyName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
	public List<SaleSdOrderItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleSdOrderItemDTO> itemList) {
		this.itemList = itemList;
	}
	public List<Long> getBuList() {
		return buList;
	}
	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}

	public String getStartShelfDate() {
		return startShelfDate;
	}
	public void setStartShelfDate(String startShelfDate) {
		this.startShelfDate = startShelfDate;
	}
	public String getEndShelfDate() {
		return endShelfDate;
	}
	public void setEndShelfDate(String endShelfDate) {
		this.endShelfDate = endShelfDate;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
        if(StringUtils.isNotBlank(orderSource)){
            this.orderSourceLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleSdOrder_orderSourceList,orderSource);
        }
    }

    public String getOrderSourceLabel() {
        return orderSourceLabel;
    }

    public void setOrderSourceLabel(String orderSourceLabel) {
        this.orderSourceLabel = orderSourceLabel;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Timestamp getOwnDate() {
        return ownDate;
    }

    public void setOwnDate(Timestamp ownDate) {
        this.ownDate = ownDate;
    }
	public String getStartOwnDate() {
		return startOwnDate;
	}
	public void setStartOwnDate(String startOwnDate) {
		this.startOwnDate = startOwnDate;
	}
	public String getEndOwnDate() {
		return endOwnDate;
	}
	public void setEndOwnDate(String endOwnDate) {
		this.endOwnDate = endOwnDate;
	}

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
        if(StringUtils.isNotBlank(isWriteOff)){
            this.isWriteOffLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOutDepot_isWriteOffList, isWriteOff);
        }
    }

    public Long getOriginalSaleSdOrderId() {
        return originalSaleSdOrderId;
    }

    public void setOriginalSaleSdOrderId(Long originalSaleSdOrderId) {
        this.originalSaleSdOrderId = originalSaleSdOrderId;
    }

    public String getOriginalSaleSdOrderCode() {
        return originalSaleSdOrderCode;
    }

    public void setOriginalSaleSdOrderCode(String originalSaleSdOrderCode) {
        this.originalSaleSdOrderCode = originalSaleSdOrderCode;
    }

    public String getIsWriteOffLabel() {
        return isWriteOffLabel;
    }
}
