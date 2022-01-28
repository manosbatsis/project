package com.topideal.entity.dto.sale;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class ShelfDTO extends PageModel implements Serializable{

	@ApiModelProperty(value="id")
    private Long id;

	@ApiModelProperty(value="上架单号")
    private String code;

	@ApiModelProperty(value="销售订单编号")
    private String saleOrderCode;

	@ApiModelProperty(value="状态")
    private String state;
	@ApiModelProperty(value="状态（中文）")
    private String stateLabel;

	@ApiModelProperty(value="po单号")
    private String poNo;

	@ApiModelProperty(value="商家ID")
    private Long merchantId;

	@ApiModelProperty(value="商家名称")
    private String merchantName;

	@ApiModelProperty(value="客户id")
    private Long customerId;

	@ApiModelProperty(value="客户名称")
    private String customerName;

	@ApiModelProperty(value="事业部id")
    private Long buId;

	@ApiModelProperty(value="事业部名称")
    private String buName;

	@ApiModelProperty(value="已上架数量")
    private Integer totalShelfNum;

	@ApiModelProperty(value="上架时间")
    private Timestamp shelfDate;

	@ApiModelProperty(value="币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;

	@ApiModelProperty(value="操作人ID")
    private Long operatorId;

	@ApiModelProperty(value="操作人")
    private String operator;

	@ApiModelProperty(value="创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value="修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value="上架开始时间")
    private String shelfStartDate;
	@ApiModelProperty(value="上架结束时间")
    private String shelfEndDate;

	@ApiModelProperty(value="出仓仓库id")
    private Long outDepotId;

	@ApiModelProperty(value="条形码")
    private String barcode;

	@ApiModelProperty(value="仓库名称")
	private String outDepotName;

	@ApiModelProperty(value="销售订单id")
	private Long saleOrderId;
    @ApiModelProperty(value = "是否生成暂估 0-未生成 1-已生成")
    private String isGenerate;
    @ApiModelProperty(value = "业务模式")
    private String businessModel;

    @ApiModelProperty(value = "销售出库单id")
    private Long saleOutDepotId;

    @ApiModelProperty(value = "销售出库编码")
    private String saleOutDepotCode;

    @ApiModelProperty(value = "销售预申报编码")
    private String saleDeclareOrderCode;

    @ApiModelProperty(value="上架单id,多个用逗号隔开")
    private String ids;

    @ApiModelProperty(value = "事业部集合")
	private List<Long> buList;

    @ApiModelProperty(value = "上架月份", hidden = true)
    private String shelfMonth;

    @ApiModelProperty(value = "上架单号，多个用逗号隔开", hidden = true)
    private String shelfCodes;

    /**
     * 是否红冲单：0-否，1-是
     */
    @ApiModelProperty(value = "是否红冲单：0-否，1-是")
    private String isWriteOff;
    @ApiModelProperty(value = "是否红冲单(中文)")
    private String isWriteOffLabel;

    /**
     * 原上架单id
     */
    @ApiModelProperty(value = "原上架单id")
    private Long originalShelfId;
    /**
     * 原上架号
     */
    @ApiModelProperty(value = "原上架号")
    private String originalShelfCode;

    @ApiModelProperty(value = "上架单明细信息")
    private List<SaleShelfDTO> itemList;

    public Long getOutDepotId() {
        return outDepotId;
    }

    public void setOutDepotId(Long outDepotId) {
        this.outDepotId = outDepotId;
    }

    public String getShelfStartDate() {
        return shelfStartDate;
    }

    public String getShelfEndDate() {
        return shelfEndDate;
    }

    public void setShelfStartDate(String shelfStartDate) {
        this.shelfStartDate = shelfStartDate;
    }

    public void setShelfEndDate(String shelfEndDate) {
        this.shelfEndDate = shelfEndDate;
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
    /*state get 方法 */
    public String getState(){

        return state;
    }
    /*state set 方法 */
    public void setState(String  state){

        this.state=state;
        this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.shelf_statusList, this.state) ;
    }

    public String getStateLabel() {
        return stateLabel;
    }
    public void setStateLabel(String stateLabel) {
        this.stateLabel = stateLabel ;
    }
    /*poNo get 方法 */
    public String getPoNo(){
        return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
        this.poNo=poNo;
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
    /*totalShelfNum get 方法 */
    public Integer getTotalShelfNum(){
        return totalShelfNum;
    }
    /*totalShelfNum set 方法 */
    public void setTotalShelfNum(Integer  totalShelfNum){
        this.totalShelfNum=totalShelfNum;
    }
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
        return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
        this.shelfDate=shelfDate;
    }
    /*currency get 方法 */
    public String getCurrency(){
        return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
        this.currency=currency;
    }
    /*operatorId get 方法 */
    public Long getOperatorId(){
        return operatorId;
    }
    /*operatorId set 方法 */
    public void setOperatorId(Long  operatorId){
        this.operatorId=operatorId;
    }
    /*operator get 方法 */
    public String getOperator(){
        return operator;
    }
    /*operator set 方法 */
    public void setOperator(String  operator){
        this.operator=operator;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

	public String getOutDepotName() {
		return outDepotName;
	}

	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}

	public Long getSaleOrderId() {
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
	}

    public String getIsGenerate() {
        return isGenerate;
    }

    public void setIsGenerate(String isGenerate) {
        this.isGenerate = isGenerate;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public Long getSaleOutDepotId() {
        return saleOutDepotId;
    }

    public void setSaleOutDepotId(Long saleOutDepotId) {
        this.saleOutDepotId = saleOutDepotId;
    }

    public String getSaleOutDepotCode() {
        return saleOutDepotCode;
    }

    public void setSaleOutDepotCode(String saleOutDepotCode) {
        this.saleOutDepotCode = saleOutDepotCode;
    }

	public String getSaleDeclareOrderCode() {
		return saleDeclareOrderCode;
	}

	public void setSaleDeclareOrderCode(String saleDeclareOrderCode) {
		this.saleDeclareOrderCode = saleDeclareOrderCode;
	}

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}

    public String getShelfMonth() {
        return shelfMonth;
    }

    public void setShelfMonth(String shelfMonth) {
        this.shelfMonth = shelfMonth;
    }

    public String getShelfCodes() {
        return shelfCodes;
    }

    public void setShelfCodes(String shelfCodes) {
        this.shelfCodes = shelfCodes;
    }

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
        if(StringUtils.isNotBlank(isWriteOff)){
            this.isWriteOffLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.shelf_isWriteOffList, isWriteOff);
        }
    }

    public List<SaleShelfDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<SaleShelfDTO> itemList) {
        this.itemList = itemList;
    }

    public Long getOriginalShelfId() {
        return originalShelfId;
    }

    public void setOriginalShelfId(Long originalShelfId) {
        this.originalShelfId = originalShelfId;
    }

    public String getOriginalShelfCode() {
        return originalShelfCode;
    }

    public void setOriginalShelfCode(String originalShelfCode) {
        this.originalShelfCode = originalShelfCode;
    }
    public String getIsWriteOffLabel() {
        return isWriteOffLabel;
    }
}
