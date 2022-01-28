package com.topideal.entity.dto.common;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class PlatformCostConfigDTO  extends PageModel implements Serializable {

    @ApiModelProperty(value = "ID")
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

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "生效日期")
    private Timestamp effectiveDate;

    @ApiModelProperty(value = "失效日期")
    private Timestamp expirationDate;

    @ApiModelProperty(value = "电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790")
    private String storePlatformCode;

    @ApiModelProperty(value = "电商平台名称")
    private String storePlatformName;

    @ApiModelProperty(value = "状态 0-未审核 1-已审核")
    private String status;
    @ApiModelProperty(value = "状态 0-未审核 1-已审核")
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

    @ApiModelProperty(value = "事业部集合")
    private List<Long> buList;

    @ApiModelProperty(value = "关联子品牌集合")
    private List<PlatformCostConfigItemDTO> itemList;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "生效月份字符串")
    private String effectiveDateStr;

    @ApiModelProperty(value = "失效月份字符串")
    private String expirationDateStr;

    @ApiModelProperty(value = "校验")
    private Long operaId;

    @ApiModelProperty(value = "平台集合", hidden = true)
    private List<String> storePlatformCodes;

    public Long getOperaId() {
        return operaId;
    }

    public void setOperaId(Long operaId) {
        this.operaId = operaId;
    }

    public String getEffectiveDateStr() {
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public String getExpirationDateStr() {
        return expirationDateStr;
    }

    public void setExpirationDateStr(String expirationDateStr) {
        this.expirationDateStr = expirationDateStr;
    }

    public List<PlatformCostConfigItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<PlatformCostConfigItemDTO> itemList) {
        this.itemList = itemList;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

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
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
        return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
        this.storePlatformCode=storePlatformCode;
        this.storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode);
    }
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
        return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
        this.storePlatformName=storePlatformName;
    }
    /*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platFormConfigConfig_statusList, status) ;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<String> getStorePlatformCodes() {
        return storePlatformCodes;
    }

    public void setStorePlatformCodes(List<String> storePlatformCodes) {
        this.storePlatformCodes = storePlatformCodes;
    }
}
