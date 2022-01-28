package com.topideal.entity.dto.transfer;


import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 调拨订单
 *
 * @author lianchenxing
 */
@ApiModel
public class TransferOrderDTO extends PageModel implements Serializable {

    //调拨订单编号
    @ApiModelProperty(value = "调拨订单编号", required = false)
    private String code;
    //LBX单号
    @ApiModelProperty(value = "LBX单号", required = false)
    private String lbxNo;
    //修改时间
    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;
    //合同号
    @ApiModelProperty(value = "合同号", required = false)
    private String contractNo;
    //调出仓库id
    @ApiModelProperty(value = "调出仓库id", required = false)
    private Long outDepotId;
    //修改人
    @ApiModelProperty(value = "修改人id", required = false)
    private Long modifier;
    //备注
    @ApiModelProperty(value = "备注", required = false)
    private String remark;
    //商家名称
    @ApiModelProperty(value = "商家名称", required = false)
    private String merchantName;
    //调入仓库名称
    @ApiModelProperty(value = "调入仓库名称", required = false)
    private String inDepotName;
    //调出仓库名称
    @ApiModelProperty(value = "调出仓库名称", required = false)
    private String outDepotName;
    //商家ID
    @ApiModelProperty(value = "商家id", required = false)
    private Long merchantId;
    //调入仓库id
    @ApiModelProperty(value = "调入仓库id", required = false)
    private Long inDepotId;
    //创建人
    @ApiModelProperty(value = "创建人id", required = false)
    private Long creater;
    //业务场影  账册内调拨       枚举
    @ApiModelProperty(value = "业务场景", required = false)
    private String model;
    @ApiModelProperty(value = "业务场景中文", required = false)
    private String modelLabel;
    //id
    @ApiModelProperty(value = "调拨订单id", required = false)
    private Long id;
    //创建时间
    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;
    //状态：013-待提交 014-已提交 006-已删除
    @ApiModelProperty(value = "单据状态", required = false)
    private String status;
    @ApiModelProperty(value = "单据状态中文", required = false)
    private String statusLabel;
    //服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
    @ApiModelProperty(value = "服务类型", required = false)
    private String serveTypes;
    @ApiModelProperty(value = "服务类型中文", required = false)
    private String serveTypesLabel;
    //提交时间
    @ApiModelProperty(value = "提交时间", required = false)
    private Timestamp submitTime;
    //提交人
    @ApiModelProperty(value = "提交人id", required = false)
    private Long submitOne;
    //调入客户id
    @ApiModelProperty(value = "调入客户id", required = false)
    private Long inCustomerId;
    //调入客户名称
    @ApiModelProperty(value = "调入客户名称", required = false)
    private String inCustomerName;
    //发票号
    @ApiModelProperty(value = "发票号", required = false)
    private String invoiceNo;
    //装货港
    @ApiModelProperty(value = "装货港", required = false)
    private String portLoading;
    //包装方式
    @ApiModelProperty(value = "包装方式", required = false)
    private String packType;
    @ApiModelProperty(value = "包装方式中文", required = false)
    private String packTypeLabel;
    //箱数
    @ApiModelProperty(value = "箱数", required = false)
    private String cartons;
    //付款条约
    @ApiModelProperty(value = "付款条约", required = false)
    private String payRules;
    //提单毛重
    @ApiModelProperty(value = "提单毛重", required = false)
    private String billWeight;
    //提交人名称
    @ApiModelProperty(value = "提交人名称", required = false)
    private String submitUsername;
    //创建人名称
    @ApiModelProperty(value = "调拨订单编号", required = false)
    private String createUsername;
    //修改人名称
    @ApiModelProperty(value = "修改人名称", required = false)
    private String modifierUsername;
    //唛头
    @ApiModelProperty(value = "唛头", required = false)
    private String mark;
    //境外发货人
    @ApiModelProperty(value = "调拨订境外发货人单编号", required = false)
    private String shipper;
    //自编码
    @ApiModelProperty(value = "自编码", required = false)
    private String zcode;
    //卓志编码
    @ApiModelProperty(value = "卓志编码", required = false)
    private String topidealCode;
    //收货地址
    @ApiModelProperty(value = "收货地址", required = false)
    private String receivingAddress;
    //头程提单号
    @ApiModelProperty(value = "头程提单号", required = false)
    private String firstLadingBillNo;
    //理货单位 00-托盘 01-箱 02-件
    @ApiModelProperty(value = "理货单位", required = false)
    private String tallyingUnit;
    @ApiModelProperty(value = "理货单位中文", required = false)
    private String tallyingUnitLabel;
    //国家
    @ApiModelProperty(value = "国家", required = false)
    private String country;
    //收货人姓名
    @ApiModelProperty(value = "收货人姓名", required = false)
    private String consigneeUsername;
    //目的地
    @ApiModelProperty(value = "目的地", required = false)
    private String destination;
    @ApiModelProperty(value = "目的地中文", required = false)
    private String destinationLabel;
    // 调入仓地址
    @ApiModelProperty(value = "调入仓地址", required = false)
    private String depotScheduleAddress;
    // 仓库附表id
    @ApiModelProperty(value = "仓库附表id", required = false)
    private Long depotScheduleId;

    //是否同关区（0-否，1-是）
    @ApiModelProperty(value = "是否同关区", required = false)
    private String isSameArea;
    @ApiModelProperty(value = "是否同关区中文", required = false)
    private String isSameAreaLabel;
    //商品信息-表体
    @ApiModelProperty(value = "商品信息表体集合", required = false)
    private List<TransferOrderItemModel> itemList;
    //商品信息dto表体集合
    @ApiModelProperty(value = "商品信息dto表体集合", required = false)
    private List<TransferOrderItemDTO> orderItemDTOS;
    //调拨订单编码集合
    @ApiModelProperty(value = "调拨订单编码集合", required = false)
    private List<String> codeList;
    //po号
    @ApiModelProperty(value = "po号", required = false)
    private String poNo;
    //创建时间开始
    @ApiModelProperty(value = "创建开始时间", required = false)
    private String createDateStart;
    //创建时间结束
    @ApiModelProperty(value = "创建结束时间", required = false)
    private String createDateEnd;
    //事业部名称
    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;
    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;
    //调拨出入库类型 0-出库 1-入库
    @ApiModelProperty(value = "调拨出入库类型", required = false)
    private String orderType;
    //入仓仓库进出接口指令 1-是 0 - 否
    @ApiModelProperty(value = "入仓仓库进出接口指令", required = false)
    private String inDepotIsInOutInstruction;
    //出仓仓库进出接口指令 1-是 0 - 否
    @ApiModelProperty(value = "出仓仓库进出接口指令", required = false)
    private String outDepotIsInOutInstruction;
    //出库仓库是否已入定出 1-是 0-否
    @ApiModelProperty(value = "出库仓库是否已入定出", required = false)
    private String outDepotIsInDependOut;
    //入库仓库是否已出定入 1-是 0-否
    @ApiModelProperty(value = "入库仓库是否已出定入", required = false)
    private String inDepotIsOutDependIn;
    //出库仓库是否批次强校验 1-是 0-否
    @ApiModelProperty(value = "出库仓库是否批次强校验", required = false)
    private String outDepotBatchValidation;
    //入库仓库是否批次强校验 1-是 0-否
    @ApiModelProperty(value = "入库仓库是否批次强校验", required = false)
    private String inDepotBatchValidation;
    //登录用户关联的事业部集合
    @ApiModelProperty(hidden = true)
    private List<Long> userBuList;
    //id集合
    @ApiModelProperty(hidden = true)
    private String ids;
    //车次
    @ApiModelProperty(value = "车次", required = false)
    private String trainNumber;
    //标准箱TEU
    @ApiModelProperty(value = "标准箱TEU", required = false)
    private String standardCaseTeu;
    //托数
    @ApiModelProperty(value = "托数", required = false)
    private Integer torrNum;
    //承运商
    @ApiModelProperty(value = "承运商", required = false)
    private String carrier;
    //出库地点
    @ApiModelProperty(value = "出库地点", required = false)
    private String outdepotAddr;
    //运输方式   1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他
    @ApiModelProperty(value = "运输方式", required = false)
    private String transport;
    @ApiModelProperty(value = "运输方式中文", required = false)
    private String transportLabel;

    @ApiModelProperty(value = "托盘材质", required = false)
    private String palletMaterial;
    private String palletMaterialLabel;

    @ApiModelProperty(value = "卸货港", required = false)
    private String unloadPort;

    @ApiModelProperty(value = "入库关区id", required = false)
    private Long inCustomsId;

    @ApiModelProperty(value = "入库关区编码", required = false)
    private String inCustomsCode;

    @ApiModelProperty(value = "入库关区名称", required = false)
    private String inPlatformCustoms;

    @ApiModelProperty(value = "出库关区id", required = false)
    private Long outCustomsId;

    @ApiModelProperty(value = "出库关区编码", required = false)
    private String outCustomsCode;

    @ApiModelProperty(value = "出库关区名称", required = false)
    private String outPlatformCustoms;

    @ApiModelProperty(value = "事业部库位类型ID", required = false)
    private Long stockLocationTypeId;

    @ApiModelProperty(value = "事业部库位类型", required = false)
    private String stockLocationTypeName;

    @ApiModelProperty(value="装箱明细")
    private List<PackingDetailsModel> packingList ;

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
        if (StringUtils.isNotBlank(isSameArea)) {
            this.isSameAreaLabel = DERP.getLabelByKey(DERP.isSameAreaList, isSameArea);
        }
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
        if (StringUtils.isNotBlank(destination)) {
            this.destinationLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.transferOrder_destinationList, destination);
        }
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
        if (StringUtils.isNotBlank(tallyingUnit)) {
            this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
        }
    }

    /*firstLadingBillNo get 方法 */
    public String getFirstLadingBillNo() {
        return firstLadingBillNo;
    }

    /*firstLadingBillNo set 方法 */
    public void setFirstLadingBillNo(String firstLadingBillNo) {
        this.firstLadingBillNo = firstLadingBillNo;
    }

    /*receivingAddress get 方法 */
    public String getReceivingAddress() {
        return receivingAddress;
    }

    /*receivingAddress set 方法 */
    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    /*topidealCode get 方法 */
    public String getTopidealCode() {
        return topidealCode;
    }

    /*topidealCode set 方法 */
    public void setTopidealCode(String topidealCode) {
        this.topidealCode = topidealCode;
    }

    /*submitUsername get 方法 */
    public String getSubmitUsername() {
        return submitUsername;
    }

    /*submitUsername set 方法 */
    public void setSubmitUsername(String submitUsername) {
        this.submitUsername = submitUsername;
    }

    /*createUsername get 方法 */
    public String getCreateUsername() {
        return createUsername;
    }

    /*createUsername set 方法 */
    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    /*modifierUsername get 方法 */
    public String getModifierUsername() {
        return modifierUsername;
    }

    /*modifierUsername set 方法 */
    public void setModifierUsername(String modifierUsername) {
        this.modifierUsername = modifierUsername;
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
        if (StringUtils.isNotBlank(packType)) {
            this.packTypeLabel = DERP.getLabelByKey(DERP.packTypeList, packType);
        }
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

    public String getBillWeight() {
        return billWeight;
    }

    public void setBillWeight(String billWeight) {
        this.billWeight = billWeight;
    }

    /*inCustomerName get 方法 */
    public String getInCustomerName() {
        return inCustomerName;
    }

    /*inCustomerName set 方法 */
    public void setInCustomerName(String inCustomerName) {
        this.inCustomerName = inCustomerName;
    }

    /*inCustomerId get 方法 */
    public Long getInCustomerId() {
        return inCustomerId;
    }

    /*inCustomerId set 方法 */
    public void setInCustomerId(Long inCustomerId) {
        this.inCustomerId = inCustomerId;
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
        if (StringUtils.isNotBlank(serveTypes)) {
            this.serveTypesLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.transferOrder_serveTypesList, serveTypes);
        }
    }

    /*status get 方法 */
    public String getStatus() {
        return status;
    }

    /*status set 方法 */
    public void setStatus(String status) {
        this.status = status;
        if (StringUtils.isNotBlank(status)) {
            this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.transferOrder_statusList, status);
        }
    }

    /*code get 方法 */
    public String getCode() {
        return code;
    }

    /*code set 方法 */
    public void setCode(String code) {
        this.code = code;
    }

    /*lbxNo get 方法 */
    public String getLbxNo() {
        return lbxNo;
    }

    /*lbxNo set 方法 */
    public void setLbxNo(String lbxNo) {
        this.lbxNo = lbxNo;
    }

    /*modifyDate get 方法 */
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    /*contractNo get 方法 */
    public String getContractNo() {
        return contractNo;
    }

    /*contractNo set 方法 */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /*outDepotId get 方法 */
    public Long getOutDepotId() {
        return outDepotId;
    }

    /*outDepotId set 方法 */
    public void setOutDepotId(Long outDepotId) {
        this.outDepotId = outDepotId;
    }

    /*modifier get 方法 */
    public Long getModifier() {
        return modifier;
    }

    /*modifier set 方法 */
    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    /*remark get 方法 */
    public String getRemark() {
        return remark;
    }

    /*remark set 方法 */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /*merchantName get 方法 */
    public String getMerchantName() {
        return merchantName;
    }

    /*merchantName set 方法 */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /*inDepotName get 方法 */
    public String getInDepotName() {
        return inDepotName;
    }

    /*inDepotName set 方法 */
    public void setInDepotName(String inDepotName) {
        this.inDepotName = inDepotName;
    }

    /*outDepotName get 方法 */
    public String getOutDepotName() {
        return outDepotName;
    }

    /*outDepotName set 方法 */
    public void setOutDepotName(String outDepotName) {
        this.outDepotName = outDepotName;
    }

    /*merchantId get 方法 */
    public Long getMerchantId() {
        return merchantId;
    }

    /*merchantId set 方法 */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /*inDepotId get 方法 */
    public Long getInDepotId() {
        return inDepotId;
    }

    /*inDepotId set 方法 */
    public void setInDepotId(Long inDepotId) {
        this.inDepotId = inDepotId;
    }

    /*creater get 方法 */
    public Long getCreater() {
        return creater;
    }

    /*creater set 方法 */
    public void setCreater(Long creater) {
        this.creater = creater;
    }

    /*model get 方法 */
    public String getModel() {
        return model;
    }

    /*model set 方法 */
    public void setModel(String model) {
        this.model = model;
        if (StringUtils.isNotBlank(model)) {
            this.modelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.transferOrder_modelList, model);
        }
    }

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*createDate get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
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

    public String getModelLabel() {
        return modelLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public String getServeTypesLabel() {
        return serveTypesLabel;
    }

    public String getPackTypeLabel() {
        return packTypeLabel;
    }

    public String getTallyingUnitLabel() {
        return tallyingUnitLabel;
    }

    public String getIsSameAreaLabel() {
        return isSameAreaLabel;
    }

    public String getDestinationLabel() {
        return destinationLabel;
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getInDepotIsInOutInstruction() {
        return inDepotIsInOutInstruction;
    }

    public void setInDepotIsInOutInstruction(String inDepotIsInOutInstruction) {
        this.inDepotIsInOutInstruction = inDepotIsInOutInstruction;
    }

    public String getOutDepotIsInOutInstruction() {
        return outDepotIsInOutInstruction;
    }

    public void setOutDepotIsInOutInstruction(String outDepotIsInOutInstruction) {
        this.outDepotIsInOutInstruction = outDepotIsInOutInstruction;
    }

    public String getOutDepotIsInDependOut() {
        return outDepotIsInDependOut;
    }

    public void setOutDepotIsInDependOut(String outDepotIsInDependOut) {
        this.outDepotIsInDependOut = outDepotIsInDependOut;
    }

    public String getInDepotIsOutDependIn() {
        return inDepotIsOutDependIn;
    }

    public void setInDepotIsOutDependIn(String inDepotIsOutDependIn) {
        this.inDepotIsOutDependIn = inDepotIsOutDependIn;
    }

    public List<Long> getUserBuList() {
        return userBuList;
    }

    public void setUserBuList(List<Long> userBuList) {
        this.userBuList = userBuList;
    }

    public void setModelLabel(String modelLabel) {
        this.modelLabel = modelLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setServeTypesLabel(String serveTypesLabel) {
        this.serveTypesLabel = serveTypesLabel;
    }

    public void setPackTypeLabel(String packTypeLabel) {
        this.packTypeLabel = packTypeLabel;
    }

    public void setTallyingUnitLabel(String tallyingUnitLabel) {
        this.tallyingUnitLabel = tallyingUnitLabel;
    }

    public void setDestinationLabel(String destinationLabel) {
        this.destinationLabel = destinationLabel;
    }

    public void setIsSameAreaLabel(String isSameAreaLabel) {
        this.isSameAreaLabel = isSameAreaLabel;
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
        this.transportLabel = DERP.getLabelByKey(DERP.transportList, transport);
    }

    public String getTransportLabel() {
        return transportLabel;
    }

    public void setTransportLabel(String transportLabel) {
        this.transportLabel = transportLabel;
    }

    public String getOutDepotBatchValidation() {
        return outDepotBatchValidation;
    }

    public void setOutDepotBatchValidation(String outDepotBatchValidation) {
        this.outDepotBatchValidation = outDepotBatchValidation;
    }

    public String getInDepotBatchValidation() {
        return inDepotBatchValidation;
    }

    public void setInDepotBatchValidation(String inDepotBatchValidation) {
        this.inDepotBatchValidation = inDepotBatchValidation;
    }

    public List<TransferOrderItemDTO> getOrderItemDTOS() {
        return orderItemDTOS;
    }

    public void setOrderItemDTOS(List<TransferOrderItemDTO> orderItemDTOS) {
        this.orderItemDTOS = orderItemDTOS;
    }

    public String getPalletMaterial() {
        return palletMaterial;
    }

    public void setPalletMaterial(String palletMaterial) {
        this.palletMaterial = palletMaterial;
        this.palletMaterialLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList, palletMaterial);
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

    public String getPalletMaterialLabel() {
        return palletMaterialLabel;
    }

    public void setPalletMaterialLabel(String palletMaterialLabel) {
        this.palletMaterialLabel = palletMaterialLabel;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

    public List<PackingDetailsModel> getPackingList() {
        return packingList;
    }

    public void setPackingList(List<PackingDetailsModel> packingList) {
        this.packingList = packingList;
    }
}

