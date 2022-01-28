package com.topideal.order.webapi.purchase.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/24 16:13
 * @Description: 采购试单申请Form
 */
@ApiModel
public class PurchaseTryApplyOrderForm extends PageForm {

    @ApiModelProperty(value="token")
    private String token;

    @ApiModelProperty(value="id集合")
    private String ids;

    @ApiModelProperty(value="流水编号")
    private String oaBillCode;

    @ApiModelProperty(value="供应商编码")
    private String counterpartContSignComy;

    @ApiModelProperty(value="申请人")
    private String creater;

    @ApiModelProperty(value="申请人名称")
    private String createrName;

    @ApiModelProperty(value="采购方式")
    private String purchaseMethod;
    @ApiModelProperty(value="供应商id")
    private Long supplierId;

    @ApiModelProperty(value="是否查询所有状态, true:是, false：否")
    private Boolean allStatusFlag;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getOaBillCode() {
        return oaBillCode;
    }

    public void setOaBillCode(String oaBillCode) {
        this.oaBillCode = oaBillCode;
    }

    public String getCounterpartContSignComy() {
        return counterpartContSignComy;
    }

    public void setCounterpartContSignComy(String counterpartContSignComy) {
        this.counterpartContSignComy = counterpartContSignComy;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getPurchaseMethod() {
        return purchaseMethod;
    }

    public void setPurchaseMethod(String purchaseMethod) {
        this.purchaseMethod = purchaseMethod;
    }

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Boolean getAllStatusFlag() {
        return allStatusFlag;
    }

    public void setAllStatusFlag(Boolean allStatusFlag) {
        this.allStatusFlag = allStatusFlag;
    }
}
