package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class InventoryLocationAdjustmentOrderDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "主键id")
    private Long id;
 
	@ApiModelProperty(value = "库位调整单号")
    private String code;
 
	@ApiModelProperty(value = "订单状态:001:待审核,003:已审核,006:已删除")
    private String state;
	
	@ApiModelProperty(value = "订单状态（中文）")
    private String stateLabel;
 
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;

	@ApiModelProperty(value = "商家名称")
    private String merchantName;
  
	@ApiModelProperty(value = "仓库ID")
    private Long depotId;
  
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
 
	@ApiModelProperty(value = "事业部id")
    private Long buId;
 
	@ApiModelProperty(value = "事业部名称")
    private String buName;
    
	@ApiModelProperty(value = "归属月份")
    private Timestamp ownDate;
 
	@ApiModelProperty(value = "备注")
    private String remark;
   
	@ApiModelProperty(value = "创建人")
    private Long creater;
  
	@ApiModelProperty(value = "创建人名称")
    private String createName;
   
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
   
	@ApiModelProperty(value = "审核人")
    private Long auditor;
   
	@ApiModelProperty(value = "审核人名称")
    private String auditName;
   
	@ApiModelProperty(value = "审核时间")
    private Timestamp auditDate;
  
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
   
	@ApiModelProperty(value = "事业部集合")
    private List<Long> buList;
    
	@ApiModelProperty(value = "商品信息-表体")
    private List<InventoryLocationAdjustmentOrderItemDTO> itemList;

	@ApiModelProperty(value = "平台编码")
    private String platformCode;
	
	@ApiModelProperty(value = "店铺编码")
    private String shopCode;
  
	@ApiModelProperty(value = "单据类型 DSDD-电商订单 DBRK-调拨入库 DBCK-调拨出库 XSCK-销售出库")
    private String type;
	
	@ApiModelProperty(value = "单据类型（中文）")
    private String typeLabel;
	@ApiModelProperty(value = "主键id集合")
	private String ids;
    @ApiModelProperty(value = "客户id")
    private Long customerId;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
	
    public String getPlatformCode(){
        return platformCode;
    }
    public void setPlatformCode(String  platformCode){
        this.platformCode=platformCode;
    }

    public String getShopCode(){
        return shopCode;
    }
    public void setShopCode(String  shopCode){
        this.shopCode=shopCode;
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
    /*state get 方法 */
    public String getState(){
    return state;
    }
    /*state set 方法 */
    public void setState(String  state){
        this.state=state;
        if(StringUtils.isNotBlank(state)){
            this.stateLabel= DERP_ORDER.getLabelByKey(DERP_ORDER.inventoryLocationAdjustmentOrder_statusList, state);
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
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
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

    public Timestamp getOwnDate() {
        return ownDate;
    }

    public void setOwnDate(Timestamp ownDate) {
        this.ownDate = ownDate;
    }

    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
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
    /*auditor get 方法 */
    public Long getAuditor(){
    return auditor;
    }
    /*auditor set 方法 */
    public void setAuditor(Long  auditor){
    this.auditor=auditor;
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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }


    public String getStateLabel() {
        return stateLabel;
    }

    public void setStateLabel(String stateLabel) {
        this.stateLabel = stateLabel;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

    public List<InventoryLocationAdjustmentOrderItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<InventoryLocationAdjustmentOrderItemDTO> itemList) {
        this.itemList = itemList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.inventoryLocationAdjustmentOrder_typeList, type);
    }
    
	public String getTypeLabel() {
		return typeLabel;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
