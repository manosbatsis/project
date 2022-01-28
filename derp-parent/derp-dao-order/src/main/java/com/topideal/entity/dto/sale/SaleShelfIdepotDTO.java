package com.topideal.entity.dto.sale;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
@ApiModel
public class SaleShelfIdepotDTO extends PageModel implements Serializable{


	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "上架入库单号")
    private String code;

	@ApiModelProperty(value = "销售订单编号")
    private String saleOrderCode;

	@ApiModelProperty(value = "销售出库清单编号")
    private String saleOutCode;

	@ApiModelProperty(value = "销售上架单id")
    private Long saleShelfId;

	@ApiModelProperty(value = "入仓仓库id")
    private Long inDepotId;

	@ApiModelProperty(value = "入仓仓库")
    private String inDepotName;

	@ApiModelProperty(value = "入仓仓库编码")
    private String inDepotCode;

	@ApiModelProperty(value = "出仓仓库ID")
    private Long outDepotId;

	@ApiModelProperty(value = " 出仓仓库名称")
    private String outDepotName;

	@ApiModelProperty(value = "出仓仓库编码")
    private String outDepotCode;

	@ApiModelProperty(value = "po单号")
    private String poNo;
    private List<String> poNos;

	@ApiModelProperty(value = "单据状态:  011-待入仓,012-已入仓,028-入库中")
    private String state;
	@ApiModelProperty(value = "单据状态（中文）")
    private String stateLabel;

	@ApiModelProperty(value = "上架时间")
    private Timestamp shelfDate;

	@ApiModelProperty(value = "商家ID")
    private Long merchantId;

	@ApiModelProperty(value = "商家名称")
    private String merchantName;

	@ApiModelProperty(value = "客户id")
    private Long customerId;

	@ApiModelProperty(value = "客户名称")
    private String customerName;

	@ApiModelProperty(value = "操作人")
    private String operator;

	@ApiModelProperty(value = "操作人ID")
    private Long operatorId;

	@ApiModelProperty(value = "操作时间")
    private Timestamp operatorDate;

	@ApiModelProperty(value = "创建人")
    private Long creater;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "入库开始时间",hidden=true)
    private String shelfStartDate;
	@ApiModelProperty(value = "入库结束时间",hidden=true)
    private String shelfEndDate;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "事业部id")
    private Long buId;

	@ApiModelProperty(value = "上架单号")
    private String code1;

	@ApiModelProperty(value = "事业部集合")
    private List<Long> buList;

	@ApiModelProperty(value = "销售出库单id")
    private Long saleOutDepotId;

	@ApiModelProperty(value = "销售预申报编码")
    private String saleDeclareOrderCode;

	@ApiModelProperty(value = "上架单id集合")
    private List<Long> saleShelfIdList;

    @ApiModelProperty(value = "是否红冲单：0-否，1-是")
    private String isWriteOff;
    @ApiModelProperty(value = "是否红冲单(中文)")
    private String isWriteOffLabel;
    /**
     * 原上架入库单id
     */
    @ApiModelProperty(value = "原上架入库单id")
    private Long originalShelfIdepotId;
    /**
     * 原上架入库单号
     */
    @ApiModelProperty(value = "原上架入库单号")
    private String originalShelfIdepotCode;

    public String getCode1(){
        return code1;
    }

    public void setCode1(String  code1){
        this.code1=code1;
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
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
    return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
    this.saleOrderCode=saleOrderCode;
    }
    /*saleOutCode get 方法 */
    public String getSaleOutCode(){
    return saleOutCode;
    }
    /*saleOutCode set 方法 */
    public void setSaleOutCode(String  saleOutCode){
    this.saleOutCode=saleOutCode;
    }
    /*saleShelfId get 方法 */
    public Long getSaleShelfId(){
    return saleShelfId;
    }
    /*saleShelfId set 方法 */
    public void setSaleShelfId(Long  saleShelfId){
    this.saleShelfId=saleShelfId;
    }
    /*inDepotId get 方法 */
    public Long getInDepotId(){
    return inDepotId;
    }
    /*inDepotId set 方法 */
    public void setInDepotId(Long  inDepotId){
    this.inDepotId=inDepotId;
    }
    /*inDepotName get 方法 */
    public String getInDepotName(){
    return inDepotName;
    }
    /*inDepotName set 方法 */
    public void setInDepotName(String  inDepotName){
    this.inDepotName=inDepotName;
    }
    /*inDepotCode get 方法 */
    public String getInDepotCode(){
    return inDepotCode;
    }
    /*inDepotCode set 方法 */
    public void setInDepotCode(String  inDepotCode){
    this.inDepotCode=inDepotCode;
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
    /*outDepotCode get 方法 */
    public String getOutDepotCode(){
    return outDepotCode;
    }
    /*outDepotCode set 方法 */
    public void setOutDepotCode(String  outDepotCode){
    this.outDepotCode=outDepotCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*state get 方法 */
    public String getState(){
    return state;
    }
    /*state set 方法 */
    public void setState(String  state){
    this.state=state;
    this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleShelfIdepot_statusList, this.state) ;
    }
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
    return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
    this.shelfDate=shelfDate;
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
    /*operator get 方法 */
    public String getOperator(){
    return operator;
    }
    /*operator set 方法 */
    public void setOperator(String  operator){
    this.operator=operator;
    }
    /*operatorId get 方法 */
    public Long getOperatorId(){
    return operatorId;
    }
    /*operatorId set 方法 */
    public void setOperatorId(Long  operatorId){
    this.operatorId=operatorId;
    }
    /*operatorDate get 方法 */
    public Timestamp getOperatorDate(){
    return operatorDate;
    }
    /*operatorDate set 方法 */
    public void setOperatorDate(Timestamp  operatorDate){
    this.operatorDate=operatorDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
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
	public String getStateLabel() {
		return stateLabel;
	}
	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel ;
	}
	public String getShelfStartDate() {
		return shelfStartDate;
	}
	public void setShelfStartDate(String shelfStartDate) {
		this.shelfStartDate = shelfStartDate;
	}
	public String getShelfEndDate() {
		return shelfEndDate;
	}
	public void setShelfEndDate(String shelfEndDate) {
		this.shelfEndDate = shelfEndDate;
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

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

    public List<String> getPoNos() {
        return poNos;
    }

    public void setPoNos(List<String> poNos) {
        this.poNos = poNos;
    }

	public Long getSaleOutDepotId() {
		return saleOutDepotId;
	}

	public void setSaleOutDepotId(Long saleOutDepotId) {
		this.saleOutDepotId = saleOutDepotId;
	}

	public String getSaleDeclareOrderCode() {
		return saleDeclareOrderCode;
	}

	public void setSaleDeclareOrderCode(String saleDeclareOrderCode) {
		this.saleDeclareOrderCode = saleDeclareOrderCode;
	}

	public List<Long> getSaleShelfIdList() {
		return saleShelfIdList;
	}

	public void setSaleShelfIdList(List<Long> saleShelfIdList) {
		this.saleShelfIdList = saleShelfIdList;
	}

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
        if(org.apache.commons.lang3.StringUtils.isNotBlank(isWriteOff)){
            this.isWriteOffLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleShelfIdepot_isWriteOffList, isWriteOff);
        }
    }

    public Long getOriginalShelfIdepotId() {
        return originalShelfIdepotId;
    }

    public void setOriginalShelfIdepotId(Long originalShelfIdepotId) {
        this.originalShelfIdepotId = originalShelfIdepotId;
    }

    public String getOriginalShelfIdepotCode() {
        return originalShelfIdepotCode;
    }

    public void setOriginalShelfIdepotCode(String originalShelfIdepotCode) {
        this.originalShelfIdepotCode = originalShelfIdepotCode;
    }
    public String getIsWriteOffLabel() {
        return isWriteOffLabel;
    }
}
