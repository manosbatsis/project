package com.topideal.entity.dto.common;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FileTempDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value="模版ID", required=false)
    private Long id;
    /**
    * 模版标题
    */
	@ApiModelProperty(value="模版标题", required=false)
    private String title;
    /**
    * 模版编码
    */
	@ApiModelProperty(value="模版编码", required=false)
    private String code;
    /**
    * 模版内容
    */
	@ApiModelProperty(value="模版内容", required=false)
    private String contentBody;
    /**
    * 公告类型 1-采购合同
    */
	@ApiModelProperty(value="公告类型", required=false)
    private String type;
	@ApiModelProperty(value="公告类型中文", required=false)
    private String typeLabel ;
    /**
     * 状态 1-启用 0-禁用
     */
	@ApiModelProperty(value="状态", required=false)
    private String status;
	@ApiModelProperty(value="状态中文", required=false)
    private String statusLabel;
    /**
    * 创建时间
    */
	@ApiModelProperty(value="创建时间", required=false)
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value="修改时间", required=false)
    private Timestamp modifyDate;

    //适用客户
	@ApiModelProperty(value="适用客户，多个以‘，’隔开", required=false)
    private String customers;
	@ApiModelProperty(hidden=true)
    private List<Long> customerIds;
	@ApiModelProperty(hidden=true)
    private List<Map<String, Object>> customerRelList = new ArrayList<>();

    //适用类型  1-客户  2-供应商
	@ApiModelProperty(value="适用类型  1-客户  2-供应商", required=false)
    private String cusType;

    //跳转页面地址
	@ApiModelProperty(value="跳转页面地址", required=false)
    private String toUrl;

    /**
     * 商家id
     */
	@ApiModelProperty(value="商家id", required=false)
    private Long merchantId;

    /**
     * 商家名称
     */
	@ApiModelProperty(value="商家名称", required=false)
    private String merchantName;

    @ApiModelProperty(value="页面模板文件地址", required=false)
    private String pageFileUrl;

    @ApiModelProperty(value="发票模板文件地址", required=false)
    private String invoiceFileUrl;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*title get 方法 */
    public String getTitle(){
    return title;
    }
    /*title set 方法 */
    public void setTitle(String  title){
    this.title=title;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*contentBody get 方法 */
    public String getContentBody(){
    return contentBody;
    }
    /*contentBody set 方法 */
    public void setContentBody(String  contentBody){
    this.contentBody=contentBody;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.temp_typelist, type) ;
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
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.temp_statusList, status) ;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public List<Map<String, Object>> getCustomerRelList() {
        return customerRelList;
    }

    public void setCustomerRelList(List<Map<String, Object>> customerRelList) {
        this.customerRelList = customerRelList;
    }

    public String getCustomers() {
        return customers;
    }

    public void setCustomers(String customers) {
        this.customers = customers;
    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getToUrl() {
        return toUrl;
    }

    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
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

    public String getPageFileUrl() {
        return pageFileUrl;
    }

    public void setPageFileUrl(String pageFileUrl) {
        this.pageFileUrl = pageFileUrl;
    }

    public String getInvoiceFileUrl() {
        return invoiceFileUrl;
    }

    public void setInvoiceFileUrl(String invoiceFileUrl) {
        this.invoiceFileUrl = invoiceFileUrl;
    }
}
