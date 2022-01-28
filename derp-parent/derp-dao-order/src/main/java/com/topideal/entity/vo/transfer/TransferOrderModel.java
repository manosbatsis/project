package com.topideal.entity.vo.transfer;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 调拨订单
 * @author lianchenxing
 *
 */
public class TransferOrderModel extends PageModel implements Serializable{

     //调拨订单编号
     private String code;
     //LBX单号
     private String lbxNo;
     //修改时间
     private Timestamp modifyDate;
     //合同号
     private String contractNo;
     //调出仓库id
     private Long outDepotId;
     //修改人
     private Long modifier;
     //备注
     private String remark;
     //商家名称
     private String merchantName;
     //调入仓库名称
     private String inDepotName;
     //调出仓库名称
     private String outDepotName;
     //商家ID
     private Long merchantId;
     //调入仓库id
     private Long inDepotId;
     //创建人
     private Long creater;
     //业务场影  账册内调拨       枚举
     private String model;
     //id
     private Long id;
     //创建时间
     private Timestamp createDate;
     //状态：013-待提交 014-已提交 006-已删除
     private String status;
     //服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
     private String serveTypes;
     //提交时间
     private Timestamp submitTime;
     //提交人
     private Long submitOne;
     //调入客户id
     private Long inCustomerId;
     //调入客户名称
     private String inCustomerName;
     //发票号
     private String invoiceNo;
     //装货港
     private String portLoading;
     //包装方式
     private String packType;
     //箱数
     private String cartons;
     //付款条约
     private String payRules;
     //提单毛重
     private Double billWeight;
     //提交人名称
     private String submitUsername;
     //创建人名称
     private String createUsername;
     //修改人名称
     private String modifierUsername;
     //唛头
     private String mark;
     //境外发货人
     private String shipper;
     
     private String zcode;//自编码
     //卓志编码
     private String topidealCode;
     //收货地址
     private String receivingAddress;
     //头程提单号
     private String firstLadingBillNo;
     //理货单位 00-托盘 01-箱 02-件
     private String tallyingUnit;
     //国家
     private String country;
     //收货人姓名
     private String consigneeUsername;
     //目的地
     private String destination;
     // 调入仓地址
     private String  depotScheduleAddress;
     // 仓库附表id
     private Long  depotScheduleId;
     
	 //是否同关区（0-否，1-是）
     private String isSameArea;
     
     //商品信息-表体
     private List<TransferOrderItemModel> itemList;
     // code 的集合
     private List<String> codeList;
     //po号
     private String poNo;

    //事业部名称
    private String buName;

    /**
     *  事业部id
     */
    private Long buId;
    /**
     * 车次
     */
    private String trainNumber;
    /**
     * 标准箱TEU
     */
    private String standardCaseTeu;
    /**
     * 托数
     */
    private Integer torrNum;
    /**
     * 承运商
     */
    private String carrier;
    /**
     * 出库地点
     */
    private String outdepotAddr;
	/**
	 * 运输方式   1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他
	 */
	private String transport;
    /**
     * 托盘材质
     */
    private String palletMaterial;
    /**
     * 卸货港
     */
    private String unloadPort;
    /**
     * 入库关区id
     */
    private Long inCustomsId;
    /**
     * 入库关区编码
     */
    private String inCustomsCode;
    /**
     * 入库关区名称
     */
    private String inPlatformCustoms;
    /**
     * 出库关区id
     */
    private Long outCustomsId;
    /**
     * 出库关区编码
     */
    private String outCustomsCode;
    /**
     * 出库关区名称
     */
    private String outPlatformCustoms;

    /**
     * 事业部库位类型ID
     */
    private Long stockLocationTypeId;
    /**
     * 事业部库位类型
     */
    private String stockLocationTypeName;
	
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getIsSameArea() {
		return isSameArea;
	}
	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getConsigneeUsername() {
		return consigneeUsername;
	}
	public void setConsigneeUsername(String consigneeUsername) {
		this.consigneeUsername = consigneeUsername;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	/*firstLadingBillNo get 方法 */
     public String getFirstLadingBillNo(){
         return firstLadingBillNo;
     }
     /*firstLadingBillNo set 方法 */
     public void setFirstLadingBillNo(String  firstLadingBillNo){
         this.firstLadingBillNo=firstLadingBillNo;
     }
     /*receivingAddress get 方法 */
     public String getReceivingAddress(){
         return receivingAddress;
     }
     /*receivingAddress set 方法 */
     public void setReceivingAddress(String  receivingAddress){
         this.receivingAddress=receivingAddress;
     }
     /*topidealCode get 方法 */
     public String getTopidealCode(){
         return topidealCode;
     }
     /*topidealCode set 方法 */
     public void setTopidealCode(String  topidealCode){
         this.topidealCode=topidealCode;
     }
     /*submitUsername get 方法 */
     public String getSubmitUsername(){
         return submitUsername;
     }
     /*submitUsername set 方法 */
     public void setSubmitUsername(String  submitUsername){
         this.submitUsername=submitUsername;
     }
     /*createUsername get 方法 */
     public String getCreateUsername(){
         return createUsername;
     }
     /*createUsername set 方法 */
     public void setCreateUsername(String  createUsername){
         this.createUsername=createUsername;
     }
     /*modifierUsername get 方法 */
     public String getModifierUsername(){
         return modifierUsername;
     }
     /*modifierUsername set 方法 */
     public void setModifierUsername(String  modifierUsername){
         this.modifierUsername=modifierUsername;
     }
     public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPortLoading() {
		return portLoading;
	}
	public void setPortLoading(String portLoading) {
		this.portLoading = portLoading;
	}
	public String getPackType() {
		return packType;
	}
	public void setPackType(String packType) {
		this.packType = packType;
	}
	public String getCartons() {
		return cartons;
	}
	public void setCartons(String cartons) {
		this.cartons = cartons;
	}
	public String getPayRules() {
		return payRules;
	}
	public void setPayRules(String payRules) {
		this.payRules = payRules;
	}
	public Double getBillWeight() {
		return billWeight;
	}
	public void setBillWeight(Double billWeight) {
		this.billWeight = billWeight;
	}
	/*inCustomerName get 方法 */
     public String getInCustomerName(){
         return inCustomerName;
     }
     /*inCustomerName set 方法 */
     public void setInCustomerName(String  inCustomerName){
         this.inCustomerName=inCustomerName;
     }
     /*inCustomerId get 方法 */
     public Long getInCustomerId(){
         return inCustomerId;
     }
     /*inCustomerId set 方法 */
     public void setInCustomerId(Long  inCustomerId){
         this.inCustomerId=inCustomerId;
     }
     /*submitTime get 方法 */
     public Timestamp getSubmitTime() {
		return submitTime;
	}
     /*submitTime set 方法 */
	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}
	/*submitOne get 方法 */
	public Long getSubmitOne() {
		return submitOne;
	}
	/*submitOne set 方法 */
	public void setSubmitOne(Long submitOne) {
		this.submitOne = submitOne;
	}
	/*serveTypes get 方法 */
     public String getServeTypes() {
		return serveTypes;
	}
     /*serveTypes set 方法 */
	public void setServeTypes(String serveTypes) {
		this.serveTypes = serveTypes;
	}
	/*status get 方法 */
    public String getStatus() {
		return status;
	}
    /*status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}
	/*code get 方法 */
    public String getCode(){
        return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
        this.code=code;
    }
    /*lbxNo get 方法 */
    public String getLbxNo(){
        return lbxNo;
    }
    /*lbxNo set 方法 */
    public void setLbxNo(String  lbxNo){
        this.lbxNo=lbxNo;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
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
    /*modifier get 方法 */
    public Long getModifier(){
        return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
        this.modifier=modifier;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
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
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
	public List<TransferOrderItemModel> getItemList() {
		return itemList;
	}
	public void setItemList(List<TransferOrderItemModel> itemList) {
		this.itemList = itemList;
	}
	public String getZcode() {
		return zcode;
	}
	public void setZcode(String zcode) {
		this.zcode = zcode;
	}
	public String getDepotScheduleAddress() {
		return depotScheduleAddress;
	}
	public void setDepotScheduleAddress(String depotScheduleAddress) {
		this.depotScheduleAddress = depotScheduleAddress;
	}
	public Long getDepotScheduleId() {
		return depotScheduleId;
	}
	public void setDepotScheduleId(Long depotScheduleId) {
		this.depotScheduleId = depotScheduleId;
	}
	public List<String> getCodeList() {
		return codeList;
	}
	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
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

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getStandardCaseTeu() {
        return standardCaseTeu;
    }

    public void setStandardCaseTeu(String standardCaseTeu) {
        this.standardCaseTeu = standardCaseTeu;
    }

    public Integer getTorrNum() {
        return torrNum;
    }

    public void setTorrNum(Integer torrNum) {
        this.torrNum = torrNum;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getOutdepotAddr() {
        return outdepotAddr;
    }

    public void setOutdepotAddr(String outdepotAddr) {
        this.outdepotAddr = outdepotAddr;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getPalletMaterial() {
        return palletMaterial;
    }

    public void setPalletMaterial(String palletMaterial) {
        this.palletMaterial = palletMaterial;
    }

    public String getUnloadPort() {
        return unloadPort;
    }

    public void setUnloadPort(String unloadPort) {
        this.unloadPort = unloadPort;
    }

    public Long getInCustomsId() {
        return inCustomsId;
    }

    public void setInCustomsId(Long inCustomsId) {
        this.inCustomsId = inCustomsId;
    }

    public String getInCustomsCode() {
        return inCustomsCode;
    }

    public void setInCustomsCode(String inCustomsCode) {
        this.inCustomsCode = inCustomsCode;
    }

    public String getInPlatformCustoms() {
        return inPlatformCustoms;
    }

    public void setInPlatformCustoms(String inPlatformCustoms) {
        this.inPlatformCustoms = inPlatformCustoms;
    }

    public Long getOutCustomsId() {
        return outCustomsId;
    }

    public void setOutCustomsId(Long outCustomsId) {
        this.outCustomsId = outCustomsId;
    }

    public String getOutCustomsCode() {
        return outCustomsCode;
    }

    public void setOutCustomsCode(String outCustomsCode) {
        this.outCustomsCode = outCustomsCode;
    }

    public String getOutPlatformCustoms() {
        return outPlatformCustoms;
    }

    public void setOutPlatformCustoms(String outPlatformCustoms) {
        this.outPlatformCustoms = outPlatformCustoms;
    }

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}

