package com.topideal.entity.dto.common;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SdSaleConfigDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "公司ID")
    private Long merchantId;

	@ApiModelProperty(value = "公司名称")
    private String merchantName;

	@ApiModelProperty(value = "事业部ID")
    private Long buId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;

	@ApiModelProperty(value = "客户名称d")
    private String customerName;

	@ApiModelProperty(value = "生效日期")
    private Timestamp effectiveDate;
  
	@ApiModelProperty(value = "失效日期")
    private Timestamp expirationDate;

	@ApiModelProperty(value = "平台备注")
    private String remark;

	@ApiModelProperty(value = "状态 0-待审核 1-已审核")
    private String status;
	@ApiModelProperty(value = "状态(中文)")
    private String statusLabel;

	@ApiModelProperty(value = "创建人id")
    private Long creater;

	@ApiModelProperty(value = "创建人名称")
    private String createName;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改人id")
    private Long modifier;

	@ApiModelProperty(value = "修改人名称")
    private String modifiyName;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "审核人")
    private Long auditer;

	@ApiModelProperty(value = "审核人ID")
    private String auditName;
  
	@ApiModelProperty(value = "审核时间")
    private Timestamp auditDate;

    @ApiModelProperty(value = "销售sd配置明细")
    private List<SdSaleConfigItemModel> itemList;
	
	@ApiModelProperty(value = "单比例明细")
	private List<SdSaleConfigItemModel> singleList;

    @ApiModelProperty(value = "多比例明细")
    private List<SdSaleConfigItemModel> multipleList;
	
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;

	@ApiModelProperty(value = "单据日期")
    private Timestamp orderDate;
	
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
    /*effectiveDate get 方法 */
    public Timestamp getEffectiveDate(){
    return effectiveDate;
    }
    /*effectiveDate set 方法 */
    public void setEffectiveDate(Timestamp  effectiveDate){
    this.effectiveDate=effectiveDate;
    }
    /*expirationDate get 方法 */
    public Timestamp getExpirationDate(){
    return expirationDate;
    }
    /*expirationDate set 方法 */
    public void setExpirationDate(Timestamp  expirationDate){
    this.expirationDate=expirationDate;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
	    this.status=status;
	    this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.sdPurchase_statusList, status);
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
    /*auditer get 方法 */
    public Long getAuditer(){
    return auditer;
    }
    /*auditer set 方法 */
    public void setAuditer(Long  auditer){
    this.auditer=auditer;
    }
    /*auditName get 方法 */
    public String getAuditName(){
    return auditName;
    }
    /*auditName set 方法 */
    public void setAuditName(String  auditName){
    this.auditName=auditName;
    }
    /*auditDate get 方法 */
    public Timestamp getAuditDate(){
    return auditDate;
    }
    /*auditDate set 方法 */
    public void setAuditDate(Timestamp  auditDate){
    this.auditDate=auditDate;
    }
	public String getStatusLabel() {
		return statusLabel;
	}

    public List<SdSaleConfigItemModel> getSingleList() {
        return singleList;
    }

    public List<SdSaleConfigItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<SdSaleConfigItemModel> itemList) {
        this.itemList = itemList;
    }

    public void setSingleList(List<SdSaleConfigItemModel> singleList) {
        this.singleList = singleList;
    }

    public List<SdSaleConfigItemModel> getMultipleList() {
        return multipleList;
    }

    public void setMultipleList(List<SdSaleConfigItemModel> multipleList) {
        this.multipleList = multipleList;
    }

    public List<Long> getBuList() {
		return buList;
	}
	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	
}
