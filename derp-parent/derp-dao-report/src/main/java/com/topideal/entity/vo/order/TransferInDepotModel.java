package com.topideal.entity.vo.order;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;


/**
 * 调拨入库
 * @author zhanghx
 */
public class TransferInDepotModel extends PageModel implements Serializable {

	 //合同号
    private String contractNo;
    //调出仓库id
    private Long outDepotId;
    //调拨订单ID
    private Long transferOrderId;
    //调出时间
    private Timestamp transferDate;
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
    //创建人id
    private Long creater;
	//创建人名称
	private String createName;
    //id
    private Long id;
    //状态
    private String status;
    //创建日期
    private Timestamp createDate;
    //调拨入单号
    private String code;
    //调拨订单编号
    private String transferOrderCode;
    //单据来源 1-调拨入自动生成 2-接口回推生成
    private String source;
   //调入客户名称
    private String inCustomerName;
   //调入客户id
    private Long inCustomerId;
  //理货单号
    private String tallyingOrderCode;
  //理货单id
    private Long tallyingOrderId;
    //LBX单号
    private String lbxNo;
    //修改时间
    private Timestamp modifyDate;
    //调拨入库外部单号
    private String inExternalCode;

	//事业部名称
	private String buName;

	/**
	 *  事业部id
	 */
	private Long buId;

	public String getInExternalCode() {
		return inExternalCode;
	}

	public void setInExternalCode(String inExternalCode) {
		this.inExternalCode = inExternalCode;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* lbxNo get 方法 */
	public String getLbxNo() {
		return lbxNo;
	}

	/* lbxNo set 方法 */
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	/* tallyingOrderId get 方法 */
	public Long getTallyingOrderId() {
		return tallyingOrderId;
	}

	/* tallyingOrderId set 方法 */
	public void setTallyingOrderId(Long tallyingOrderId) {
		this.tallyingOrderId = tallyingOrderId;
	}

	/* tallyingOrderCode get 方法 */
	public String getTallyingOrderCode() {
		return tallyingOrderCode;
	}

	/* tallyingOrderCode set 方法 */
	public void setTallyingOrderCode(String tallyingOrderCode) {
		this.tallyingOrderCode = tallyingOrderCode;
	}

	/* inCustomerId get 方法 */
	public Long getInCustomerId() {
		return inCustomerId;
	}

	/* inCustomerId set 方法 */
	public void setInCustomerId(Long inCustomerId) {
		this.inCustomerId = inCustomerId;
	}

	/* inCustomerName get 方法 */
	public String getInCustomerName() {
		return inCustomerName;
	}

	/* inCustomerName set 方法 */
	public void setInCustomerName(String inCustomerName) {
		this.inCustomerName = inCustomerName;
	}

	/* source get 方法 */
	public String getSource() {
		return source;
	}

	/* source set 方法 */
	public void setSource(String source) {
		this.source = source;
	}

	/* transferOrderCode get 方法 */
	public String getTransferOrderCode() {
		return transferOrderCode;
	}

	/* transferOrderCode set 方法 */
	public void setTransferOrderCode(String transferOrderCode) {
		this.transferOrderCode = transferOrderCode;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* contractNo get 方法 */
	public String getContractNo() {
		return contractNo;
	}

	/* contractNo set 方法 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/* outDepotId get 方法 */
	public Long getOutDepotId() {
		return outDepotId;
	}

	/* outDepotId set 方法 */
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	/* transferOrderId get 方法 */
	public Long getTransferOrderId() {
		return transferOrderId;
	}

	/* transferOrderId set 方法 */
	public void setTransferOrderId(Long transferOrderId) {
		this.transferOrderId = transferOrderId;
	}

	/* transferDate get 方法 */
	public Timestamp getTransferDate() {
		return transferDate;
	}

	/* transferDate set 方法 */
	public void setTransferDate(Timestamp transferDate) {
		this.transferDate = transferDate;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* inDepotName get 方法 */
	public String getInDepotName() {
		return inDepotName;
	}

	/* inDepotName set 方法 */
	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}

	/* outDepotName get 方法 */
	public String getOutDepotName() {
		return outDepotName;
	}

	/* outDepotName set 方法 */
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* inDepotId get 方法 */
	public Long getInDepotId() {
		return inDepotId;
	}

	/* inDepotId set 方法 */
	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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
}
