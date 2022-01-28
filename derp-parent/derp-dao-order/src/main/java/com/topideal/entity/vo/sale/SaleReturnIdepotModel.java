package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 销售退货入库
 * @author lian_
 *
 */
public class SaleReturnIdepotModel extends PageModel implements Serializable{

     //销售退货入库单号
     private String code;
     //销售退货ID
     private Long orderId;
     //合同号
     private String contractNo;
     //退出仓库id
     private Long outDepotId;
     //备注
     private String remark;
     //申报地海关编码
     private String customsNo;
     //商家名称
     private String merchantName;
     //LBX单号
     private String lbxNo;
     //退货入库数量
     private Integer returnInNum;

     //商家ID
     private Long merchantId;
     //客户id
     private Long customerId;
     //业务场景 账册内货权转移/账册内货权转移调仓
     private String model;
     //id
     private Long id;
     //退货入库时间
     private Timestamp returnInDate;
     //创建时间
     private Timestamp createDate;
     //申报地国检编码
     private String inspectNo;
     //客户名称
     private String customerName;
     //退入仓库名称
     private String inDepotName;
     //退出仓库名称
     private String outDepotName;
     //企业退运单号
     private String merchantReturnNo;
     //退入仓库id
     private Long inDepotId;
     //创建人
     private Long creater;
     //销售退货编号
     private String orderCode;
     //服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
     private String serveTypes;
     //状态 "011","待入仓"  "012","已入仓" "006","已删除" "028","入库中"
     private String status;
     //销售退入外部单号
     private String inExternalCode;
     //表体信息
     private List<SaleReturnIdepotItemModel> itemList;
     //修改时间
     private Timestamp modifyDate;

    //云集结算账单号
    private String yunjiAccountNo;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     *  事业部id
     */
    private Long buId;
    //单据来源：1-手工导入 2-接口生成
    private String dataSource;

    /**
     * 销售退预申报单ID
     */
    private Long returnDeclareOrderId;
    /**
     * 销售预申报单编号
     */
    private String returnDeclareOrderCode;
     
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
    }
    /*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
    }
	public List<SaleReturnIdepotItemModel> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleReturnIdepotItemModel> itemList) {
		this.itemList = itemList;
	}

    public String getYunjiAccountNo() {
        return yunjiAccountNo;
    }

    public void setYunjiAccountNo(String yunjiAccountNo) {
        this.yunjiAccountNo = yunjiAccountNo;
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

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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
}

