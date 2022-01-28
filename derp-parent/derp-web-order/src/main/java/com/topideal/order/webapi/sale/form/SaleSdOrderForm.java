package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.sale.SaleSdOrderItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class SaleSdOrderForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;

	@ApiModelProperty(value = "销售SD 单id")
    private Long id;

	@ApiModelProperty(value = "销售SD编码")
    private String code;

	@ApiModelProperty(value = "事业部ID")
    private Long buId;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;

	@ApiModelProperty(value = "po号")
    private String poNo;

	@ApiModelProperty(value = "销售SD类型id")
    private Long sdTypeId;

	@ApiModelProperty(value = "id集合，多个用逗号隔开")
	private String ids;

	@ApiModelProperty(value = "表体信息集合")
	List<SaleSdOrderItemDTO> itemList ;

    @ApiModelProperty(value = "来源单号")
    private String orderCode;

    @ApiModelProperty(value = "业务单号")
    private String businessCode;

    @ApiModelProperty(value = "单据类型 1-上架单 2-销售退入库单")
    private String orderType;

    @ApiModelProperty(value = "归属开始日期")
    private String startOwnDate;

    @ApiModelProperty(value = "归属结束日期")
    private String endOwnDate;

	@ApiModelProperty(value = "是否红冲单：0-否，1-是")
	private String isWriteOff;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Long getSdTypeId() {
		return sdTypeId;
	}

	public void setSdTypeId(Long sdTypeId) {
		this.sdTypeId = sdTypeId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<SaleSdOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleSdOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getStartOwnDate() {
		return startOwnDate;
	}

	public void setStartOwnDate(String startOwnDate) {
		this.startOwnDate = startOwnDate;
	}

	public String getEndOwnDate() {
		return endOwnDate;
	}

	public void setEndOwnDate(String endOwnDate) {
		this.endOwnDate = endOwnDate;
	}

	public String getIsWriteOff() {
		return isWriteOff;
	}

	public void setIsWriteOff(String isWriteOff) {
		this.isWriteOff = isWriteOff;
	}
}
