package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class TocSettlementReceiveBillItemDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;
    /**
    * 账单Id
    */
    @ApiModelProperty(value = "账单Id")
    private Long billId;
    private String billIds;
    /**
    * 外部订单号
    */
    @ApiModelProperty(value = "外部订单号")
    private String externalCode;
    /**
    * 费项id
    */
    @ApiModelProperty(value = "费项id")
    private Long projectId;
    /**
    * 项目名称
    */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 平台费项id
     */
    @ApiModelProperty(value = "平台费项id")
    private Long platformProjectId;
    /**
     * 平台费项名称
     */
    @ApiModelProperty(value = "平台费项名称")
    private String platformProjectName;  
    /**
    * 商品id
    */
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    /**
    * 商品名称
    */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
    * 商品货号
    */
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    /**
    * 结算金额（原币）
    */
    @ApiModelProperty(value = "结算金额（原币）")
    private BigDecimal originalAmount;
    /**
    * 结算金额（RMB）
    */
    @ApiModelProperty(value = "结算金额（RMB）")
    private BigDecimal rmbAmount;
    /**
    * 结算汇率
    */
    @ApiModelProperty(value = "结算汇率")
    private BigDecimal settlementRate;
    /**
    * 母品牌
    */
    @ApiModelProperty(value = "母品牌")
    private String parentBrandName;
    /**
    * 母品牌id
    */
    @ApiModelProperty(value = "母品牌id")
    private Long parentBrandId;
    /**
    * 母品牌编码
    */
    @ApiModelProperty(value = "母品牌编码")
    private String parentBrandCode;
    /**
    * 数量
    */
    @ApiModelProperty(value = "数量")
    private Integer num;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    //是否异常
    @ApiModelProperty(value = "是否异常")
    private String isException ;
    //异常原因
    @ApiModelProperty(value = "异常原因")
    private String excetionResult ;
    /**
     * 系统订单号
     */
    @ApiModelProperty(value = "系统订单号")
    private String orderCode;
    
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 补扣款类型 0-补款 1-扣款
     */
    @ApiModelProperty(value = "补扣款类型")
    private String billType;
    @ApiModelProperty(value = "补扣款类型中文 0-补款 1-扣款")
    private String billTypeLabel;

    @ApiModelProperty(value = "暂估收入表头id")
    private String tempBillId;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
    }
    /*externalCode get 方法 */
    public String getExternalCode(){
    return externalCode;
    }
    /*externalCode set 方法 */
    public void setExternalCode(String  externalCode){
    this.externalCode=externalCode;
    }
    /*projectId get 方法 */
    public Long getProjectId(){
    return projectId;
    }
    /*projectId set 方法 */
    public void setProjectId(Long  projectId){
    this.projectId=projectId;
    }
    /*projectName get 方法 */
    public String getProjectName(){
    return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
    this.projectName=projectName;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*originalAmount get 方法 */
    public BigDecimal getOriginalAmount(){
    return originalAmount;
    }
    /*originalAmount set 方法 */
    public void setOriginalAmount(BigDecimal  originalAmount){
    this.originalAmount=originalAmount;
    }
    /*rmbAmount get 方法 */
    public BigDecimal getRmbAmount(){
    return rmbAmount;
    }
    /*rmbAmount set 方法 */
    public void setRmbAmount(BigDecimal  rmbAmount){
    this.rmbAmount=rmbAmount;
    }
    /*settlementRate get 方法 */
    public BigDecimal getSettlementRate(){
    return settlementRate;
    }
    /*settlementRate set 方法 */
    public void setSettlementRate(BigDecimal  settlementRate){
    this.settlementRate=settlementRate;
    }
    /*parentBrandName get 方法 */
    public String getParentBrandName(){
    return parentBrandName;
    }
    /*parentBrandName set 方法 */
    public void setParentBrandName(String  parentBrandName){
    this.parentBrandName=parentBrandName;
    }
    /*parentBrandId get 方法 */
    public Long getParentBrandId(){
    return parentBrandId;
    }
    /*parentBrandId set 方法 */
    public void setParentBrandId(Long  parentBrandId){
    this.parentBrandId=parentBrandId;
    }
    /*parentBrandCode get 方法 */
    public String getParentBrandCode(){
    return parentBrandCode;
    }
    /*parentBrandCode set 方法 */
    public void setParentBrandCode(String  parentBrandCode){
    this.parentBrandCode=parentBrandCode;
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

    public String getIsException() {
        return isException;
    }

    public void setIsException(String isException) {
        this.isException = isException;
    }

    public String getExcetionResult() {
        return excetionResult;
    }

    public void setExcetionResult(String excetionResult) {
        this.excetionResult = excetionResult;
    }
	public Long getPlatformProjectId() {
		return platformProjectId;
	}
	public void setPlatformProjectId(Long platformProjectId) {
		this.platformProjectId = platformProjectId;
	}
	public String getPlatformProjectName() {
		return platformProjectName;
	}
	public void setPlatformProjectName(String platformProjectName) {
		this.platformProjectName = platformProjectName;
	}

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
        this.billTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tocReceiveBillCost_billTypeList, billType);
    }

    public String getBillTypeLabel() {
        return billTypeLabel;
    }

    public void setBillTypeLabel(String billTypeLabel) {
        this.billTypeLabel = billTypeLabel;
    }

    public String getBillIds() {
        return billIds;
    }

    public void setBillIds(String billIds) {
        this.billIds = billIds;
    }

    public String getTempBillId() {
        return tempBillId;
    }

    public void setTempBillId(String tempBillId) {
        this.tempBillId = tempBillId;
    }
}
