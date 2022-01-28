package com.topideal.order.webapi.transfer.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 调拨订单查询条件form
 */
@ApiModel
public class TransferOrderForm extends PageForm {

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "调拨订单号")
	private String code;
	@ApiModelProperty(value = "LBX单号")
	private String lbxNo;
	@ApiModelProperty(value = "合同号")
	private String contractNo;
	@ApiModelProperty(value = "调出仓库Id")
	private Long outDepotId;
	@ApiModelProperty(value = "调入仓库Id")
	private Long inDepotId;
	@ApiModelProperty(value = "单据状态")
	private String status;
	@ApiModelProperty(value = "事业部Id")
	private Long buId;
	@ApiModelProperty(value = "创建开始时间 yyyy-MM-dd HH:mm:ss")
	private String createDateStart;
	@ApiModelProperty(value = "创建结束时间 yyyy-MM-dd HH:mm:ss")
	private String createDateEnd;
	@ApiModelProperty(value = "id集合,多个用英文逗号隔开")
	private String ids;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	public Long getInDepotId() {
		return inDepotId;
	}

	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
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

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
