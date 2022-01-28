package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 销售退货入库
 * @author wenyan
 *
 */
@ApiModel
public class SaleReturnIdepotDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "销售退货入库单号")
    private String code;
    @ApiModelProperty(value = "销售退货ID")
    private Long orderId;
    @ApiModelProperty(value = "合同号")
    private String contractNo;
    @ApiModelProperty(value = "退出仓库id")
    private Long outDepotId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "申报地海关编码")
    private String customsNo;
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
    @ApiModelProperty(value = "LBX单号")
    private String lbxNo;
    @ApiModelProperty(value = "退货入库数量")
    private Integer returnInNum;

    @ApiModelProperty(value = "商家ID")
    private Long merchantId;
    @ApiModelProperty(value = "客户id")
    private Long customerId;
    @ApiModelProperty(value = "业务场景 账册内货权转移/账册内货权转移调仓")
    private String model;
    @ApiModelProperty(value = "业务场景（中文）")
    private String modelLabel;
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "退货入库时间")
    private Timestamp returnInDate;
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    @ApiModelProperty(value = "申报地国检编码")
    private String inspectNo;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "退入仓库名称")
    private String inDepotName;
    @ApiModelProperty(value = "退出仓库名称")
    private String outDepotName;
    @ApiModelProperty(value = "企业退运单号")
    private String merchantReturnNo;
    @ApiModelProperty(value = "退入仓库id")
    private Long inDepotId;
    @ApiModelProperty(value = "创建人")
    private Long creater;
    @ApiModelProperty(value = "销售退货编号")
    private String orderCode;
    @ApiModelProperty(value = "服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务")
    private String serveTypes;
    @ApiModelProperty(value = "服务类型（中文）")
    private String serveTypesLabel;
    @ApiModelProperty(value = "状态 011:待入仓  012:已入仓  006:已删除  028:入库中")
    private String status;
    @ApiModelProperty(value = "状态（中文）")
    private String statusLabel;
    @ApiModelProperty(value = "销售退入外部单号")
    private String inExternalCode;
    @ApiModelProperty(value = "表体信息")
    private List<SaleReturnIdepotItemDTO> itemList;
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "云集结算账单号")
    private String yunjiAccountNo;
    @ApiModelProperty(value = "退货入库开始时间",hidden=true) 
    private String returnInDateStartDate;
    @ApiModelProperty(value = "退货入库结束时间",hidden=true) 
    private String returnInDateEndDate;

    @ApiModelProperty(value = "事业部名称")
    private String buName;
    @ApiModelProperty(value = "事业部id")
    private Long buId;
    @ApiModelProperty(value = "单据来源")
    private String dataSource;
    @ApiModelProperty(value = "事业部集合")
    private List<Long> buList;
    @ApiModelProperty(value = "主键id集合")
    private String ids;

    @ApiModelProperty(value = "销售退预申报单ID")
    private Long returnDeclareOrderId;

    @ApiModelProperty(value = "销售预申报单编号")
    private String returnDeclareOrderCode;

    @ApiModelProperty(value = "po号集合", hidden = true)
    private List<String> poNos;

    @ApiModelProperty(value = "关联业务单号集合", hidden = true)
    private List<String> relCodes;

    @ApiModelProperty(hidden = true)
    private String poNo;

    @ApiModelProperty(hidden = true)
    private String currency;
    
   public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getInExternalCode() {
		return inExternalCode;
	}
	public void setInExternalCode(String inExternalCode) {
		this.inExternalCode = inExternalCode;
	}
	public String getLbxNo() {
		return lbxNo;
	}
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}
	/*code get 方法 */
   public String getCode(){
       return code;
   }
   /*code set 方法 */
   public void setCode(String  code){
       this.code=code;
   }
   /*orderId get 方法 */
   public Long getOrderId(){
       return orderId;
   }
   /*orderId set 方法 */
   public void setOrderId(Long  orderId){
       this.orderId=orderId;
   }
   /*contractNo get 方法 */
   public String getContractNo(){
       return contractNo;
   }
   /*contractNo set 方法 */
   public void setContractNo(String  contractNo){
       this.contractNo=contractNo;
   }
   /*outDepotId get 方法 */
   public Long getOutDepotId(){
       return outDepotId;
   }
   /*outDepotId set 方法 */
   public void setOutDepotId(Long  outDepotId){
       this.outDepotId=outDepotId;
   }
   /*remark get 方法 */
   public String getRemark(){
       return remark;
   }
   /*remark set 方法 */
   public void setRemark(String  remark){
       this.remark=remark;
   }
   /*customsNo get 方法 */
   public String getCustomsNo(){
       return customsNo;
   }
   /*customsNo set 方法 */
   public void setCustomsNo(String  customsNo){
       this.customsNo=customsNo;
   }
   /*merchantName get 方法 */
   public String getMerchantName(){
       return merchantName;
   }
   /*merchantName set 方法 */
   public void setMerchantName(String  merchantName){
       this.merchantName=merchantName;
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
   /*model get 方法 */
   public String getModel(){
       return model;
   }
   /*model set 方法 */
   public void setModel(String  model){
       this.model=model;
       if(StringUtils.isNotBlank(model)){
    	   this.modelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturn_modelList, model);
       }
   }
   /*id get 方法 */
   public Long getId(){
       return id;
   }
   /*id set 方法 */
   public void setId(Long  id){
       this.id=id;
   }
   /*returnInDate get 方法 */
   public Timestamp getReturnInDate(){
       return returnInDate;
   }
   /*returnInDate set 方法 */
   public void setReturnInDate(Timestamp  returnInDate){
       this.returnInDate=returnInDate;
   }
   /*createDate get 方法 */
   public Timestamp getCreateDate(){
       return createDate;
   }
   /*createDate set 方法 */
   public void setCreateDate(Timestamp  createDate){
       this.createDate=createDate;
   }
   /*returnInNum get 方法 */
   public Integer getReturnInNum(){
       return returnInNum;
   }
   public void setReturnInNum(Integer returnInNum) {
		this.returnInNum = returnInNum;
	}
	/*inspectNo get 方法 */
   public String getInspectNo(){
       return inspectNo;
   }
   /*inspectNo set 方法 */
   public void setInspectNo(String  inspectNo){
       this.inspectNo=inspectNo;
   }
   /*customerName get 方法 */
   public String getCustomerName(){
       return customerName;
   }
   /*customerName set 方法 */
   public void setCustomerName(String  customerName){
       this.customerName=customerName;
   }
   /*inDepotName get 方法 */
   public String getInDepotName(){
       return inDepotName;
   }
   /*inDepotName set 方法 */
   public void setInDepotName(String  inDepotName){
       this.inDepotName=inDepotName;
   }
   /*outDepotName get 方法 */
   public String getOutDepotName(){
       return outDepotName;
   }
   /*outDepotName set 方法 */
   public void setOutDepotName(String  outDepotName){
       this.outDepotName=outDepotName;
   }
   /*merchantReturnNo get 方法 */
   public String getMerchantReturnNo(){
       return merchantReturnNo;
   }
   /*merchantReturnNo set 方法 */
   public void setMerchantReturnNo(String  merchantReturnNo){
       this.merchantReturnNo=merchantReturnNo;
   }
   /*inDepotId get 方法 */
   public Long getInDepotId(){
       return inDepotId;
   }
   /*inDepotId set 方法 */
   public void setInDepotId(Long  inDepotId){
       this.inDepotId=inDepotId;
   }
   /*creater get 方法 */
   public Long getCreater(){
       return creater;
   }
   /*creater set 方法 */
   public void setCreater(Long  creater){
       this.creater=creater;
   }
   /*orderCode get 方法 */
   public String getOrderCode(){
       return orderCode;
   }
   /*orderCode set 方法 */
   public void setOrderCode(String  orderCode){
       this.orderCode=orderCode;
   }
   /*serveTypes get 方法 */
   public String getServeTypes(){
       return serveTypes;
   }
   /*serveTypes set 方法 */
   public void setServeTypes(String  serveTypes){
       this.serveTypes=serveTypes;
       if(StringUtils.isNotBlank(serveTypes)){
    	   this.serveTypesLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturn_serveTypesList, serveTypes);
       }
   }
   /*status get 方法 */
   public String getStatus(){
       return status;
   }
   /*status set 方法 */
   public void setStatus(String  status){
       this.status=status;
       if(StringUtils.isNotBlank(status)){
    	   this.statusLabel=DERP_ORDER.getLabelByKey(DERP_ORDER.saleReturnIdepot_statusList, status);
       }
   }
	public List<SaleReturnIdepotItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleReturnIdepotItemDTO> itemList) {
		this.itemList = itemList;
	}
	public String getModelLabel() {
		return modelLabel;
	}
	public void setModelLabel(String modelLabel) {
		this.modelLabel = modelLabel;
	}
	public String getServeTypesLabel() {
		return serveTypesLabel;
	}
	public void setServeTypesLabel(String serveTypesLabel) {
		this.serveTypesLabel = serveTypesLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

    public String getYunjiAccountNo() {
        return yunjiAccountNo;
    }

    public void setYunjiAccountNo(String yunjiAccountNo) {
        this.yunjiAccountNo = yunjiAccountNo;
    }
	public String getReturnInDateStartDate() {
		return returnInDateStartDate;
	}
	public void setReturnInDateStartDate(String returnInDateStartDate) {
		this.returnInDateStartDate = returnInDateStartDate;
	}
	public String getReturnInDateEndDate() {
		return returnInDateEndDate;
	}
	public void setReturnInDateEndDate(String returnInDateEndDate) {
		this.returnInDateEndDate = returnInDateEndDate;
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

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

    public Long getReturnDeclareOrderId() {
        return returnDeclareOrderId;
    }

    public void setReturnDeclareOrderId(Long returnDeclareOrderId) {
        this.returnDeclareOrderId = returnDeclareOrderId;
    }

    public String getReturnDeclareOrderCode() {
        return returnDeclareOrderCode;
    }

    public void setReturnDeclareOrderCode(String returnDeclareOrderCode) {
        this.returnDeclareOrderCode = returnDeclareOrderCode;
    }

    public List<String> getPoNos() {
        return poNos;
    }

    public void setPoNos(List<String> poNos) {
        this.poNos = poNos;
    }

    public List<String> getRelCodes() {
        return relCodes;
    }

    public void setRelCodes(List<String> relCodes) {
        this.relCodes = relCodes;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
