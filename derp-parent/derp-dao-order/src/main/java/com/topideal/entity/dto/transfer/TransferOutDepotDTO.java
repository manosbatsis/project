package com.topideal.entity.dto.transfer;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * 调拨出库表
 *
 * @author lian_
 */
@ApiModel
public class TransferOutDepotDTO extends PageModel implements Serializable {

    //合同号
    @ApiModelProperty(value = "合同号", required = false)
    private String contractNo;
    //调出仓库id
    @ApiModelProperty(value = "调出仓库id", required = false)
    private Long outDepotId;
    //调拨订单ID
    @ApiModelProperty(value = "调拨订单id", required = false)
    private Long transferOrderId;
    //调出时间
    @ApiModelProperty(value = "调出时间", required = false)
    private Timestamp transferDate;
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
    //创建人id
    @ApiModelProperty(value = "创建人id", required = false)
    private Long creater;
    //创建人名称
    @ApiModelProperty(value = "创建人名称", required = false)
    private String createName;
    //id
    @ApiModelProperty(value = "id", required = false)
    private Long id;
    //状态
    @ApiModelProperty(value = "状态", required = false)
    private String status;
    @ApiModelProperty(value = "状态中文", required = false)
    private String statusLabel;
    //创建时间
    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;
    //调入客户id
    @ApiModelProperty(value = "调入客户id", required = false)
    private Long inCustomerId;
    //调入客户名称
    @ApiModelProperty(value = "调入客户名称", required = false)
    private String inCustomerName;
    //运单号
    @ApiModelProperty(value = "运单号", required = false)
    private String wayBillNo;
    //退运状态  70：成功90：作废
    @ApiModelProperty(value = "退运状态", required = false)
    private String returnStatus;
    @ApiModelProperty(value = "退运状态中文", required = false)
    private String returnStatusLabel;
    //LBX单号
    @ApiModelProperty(value = "LBX单号", required = false)
    private String lbxNo;
    //修改时间
    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;
    //调拨出库外部单号
    @ApiModelProperty(value = "调拨出库外部单号", required = false)
    private String outExternalCode;
    //调拨出单号
    @ApiModelProperty(value = "调拨出单号", required = false)
    private String code;
    //调拨订单编号
    @ApiModelProperty(value = "调拨订单编号", required = false)
    private String transferOrderCode;
    //拓展字段
    //调出结束时间
    @ApiModelProperty(value = "调出结束时间", required = false)
    private String transferEndDate;
    //调出开始时间
    @ApiModelProperty(value = "调出开始时间", required = false)
    private String transferStartDate;
    //事业部名称
    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;
    //事业部id
    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;
    //出库时间
    @ApiModelProperty(value = "出库时间", required = false)
    private String transferOutDate;
    //单据来源：1-手工导入 2-接口生成
    @ApiModelProperty(value = "单据来源", required = false)
    private String dataSource;
    @ApiModelProperty(value = "商品信息", required = false)
    private List<TransferOutDepotItemDTO> goodsList = new ArrayList<>();
    //登录用户关联的事业部集合
    @ApiModelProperty(hidden = true)
    private List<Long> userBuList;
    //id集合
    @ApiModelProperty(hidden = true)
    private String ids;

    public String getOutExternalCode() {
        return outExternalCode;
    }

    public void setOutExternalCode(String outExternalCode) {
        this.outExternalCode = outExternalCode;
    }

    public String getTransferEndDate() {
        return transferEndDate;
    }

    public void setTransferEndDate(String transferEndDate) {
        this.transferEndDate = transferEndDate;
    }

    public String getTransferStartDate() {
        return transferStartDate;
    }

    public void setTransferStartDate(String transferStartDate) {
        this.transferStartDate = transferStartDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    /*lbxNo get 方法 */
    public String getLbxNo() {
        return lbxNo;
    }

    /*lbxNo set 方法 */
    public void setLbxNo(String lbxNo) {
        this.lbxNo = lbxNo;
    }

    /*returnStatus get 方法 */
    public String getReturnStatus() {
        return returnStatus;
    }

    /*returnStatus set 方法 */
    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    /*wayBillNo get 方法 */
    public String getWayBillNo() {
        return wayBillNo;
    }

    /*wayBillNo set 方法 */
    public void setWayBillNo(String wayBillNo) {
        this.wayBillNo = wayBillNo;
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

    /*transferOrderCode get 方法 */
    public String getTransferOrderCode() {
        return transferOrderCode;
    }

    /*transferOrderCode set 方法 */
    public void setTransferOrderCode(String transferOrderCode) {
        this.transferOrderCode = transferOrderCode;
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

    /*transferOrderId get 方法 */
    public Long getTransferOrderId() {
        return transferOrderId;
    }

    /*transferOrderId set 方法 */
    public void setTransferOrderId(Long transferOrderId) {
        this.transferOrderId = transferOrderId;
    }

    /*transferDate get 方法 */
    public Timestamp getTransferDate() {
        return transferDate;
    }

    /*transferDate set 方法 */
    public void setTransferDate(Timestamp transferDate) {
        this.transferDate = transferDate;
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

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*status get 方法 */
    public String getStatus() {
        return status;
    }

    /*status set 方法 */
    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.transferOutDepot_statusList, status);
    }

    /*createDate get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public String getReturnStatusLabel() {
        return returnStatusLabel;
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

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getTransferOutDate() {
        return transferOutDate;
    }

    public void setTransferOutDate(String transferOutDate) {
        this.transferOutDate = transferOutDate;
    }

    public List<TransferOutDepotItemDTO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<TransferOutDepotItemDTO> goodsList) {
        this.goodsList = goodsList;
    }

    public List<Long> getUserBuList() {
        return userBuList;
    }

    public void setUserBuList(List<Long> userBuList) {
        this.userBuList = userBuList;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setReturnStatusLabel(String returnStatusLabel) {
        this.returnStatusLabel = returnStatusLabel;
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
}
