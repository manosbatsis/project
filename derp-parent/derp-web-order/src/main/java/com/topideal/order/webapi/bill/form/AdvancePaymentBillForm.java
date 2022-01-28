package com.topideal.order.webapi.bill.form;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 列表页搜索参数
 */
@ApiModel
public class AdvancePaymentBillForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "预付账单号", required = false)
    private String code;

    @ApiModelProperty(value = "供应商id", required = false)
    private Long supplierId;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value = "预计付款日期开始", required = false)
    private String expectedPaymentDateStart;

    @ApiModelProperty(value = "预计付款日期结束", required = false)
    private String expectedPaymentDateEnd;

    @ApiModelProperty(value = "账单状态 00-待提交、01-审核中、02-已驳回、03-待付款、04-待作废、05-已作废、06-待核销、07已核销", required = false)
    private String billStatus;
    
    @ApiModelProperty(value = "预付单导出ID，多个以','隔开", required = false)
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

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getExpectedPaymentDateStart() {
        return expectedPaymentDateStart;
    }

    public void setExpectedPaymentDateStart(String expectedPaymentDateStart) {
        this.expectedPaymentDateStart = expectedPaymentDateStart;
    }

    public String getExpectedPaymentDateEnd() {
        return expectedPaymentDateEnd;
    }

    public void setExpectedPaymentDateEnd(String expectedPaymentDateEnd) {
        this.expectedPaymentDateEnd = expectedPaymentDateEnd;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
    
    
}
