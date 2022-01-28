package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 列表页搜索参数
 */
@ApiModel
public class AdvancePaymentBillSearchForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "po号", required = false)
    private String poNos;

    @ApiModelProperty(value = "供应商id", required = false)
    private Long supplierId;

    @ApiModelProperty(value = "采购单号", required = false)
    private String codes;
    
    @ApiModelProperty(value = "事业部", required = false)
    private Long buId;
    
    @ApiModelProperty(value = "币种", required = false)
    private String currency;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPoNos() {
        return poNos;
    }

    public void setPoNos(String poNos) {
        this.poNos = poNos;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
    
    
}
