package com.topideal.order.webapi.purchase.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/24 16:13
 * @Description:
 */
@ApiModel
public class PurchaseFrameContractForm extends PageForm {

    @ApiModelProperty(value="token")
    private String token;

    @ApiModelProperty(value="id集合")
    private String ids;

    /**
     * 合同协议号
     */
    @ApiModelProperty(value="合同协议号")
    private String contractNo;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value="供应商编码")
    private String counterpartContSignComy;

    /**
     * 申请人
     */
    @ApiModelProperty(value="申请人")
    private String creater;

    @ApiModelProperty(value="申请人名称")
    private String createrName;

    /**
     * 协议开始日期
     */
    @ApiModelProperty(value="协议开始日期")
    private String contractStartTime;

    /**
     * 协议结束日期
     */
    @ApiModelProperty(value="协议结束日期")
    private String contractEndTime;

    @ApiModelProperty(value="供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "是否需要所有状态， true是, false 否")
    private Boolean allStatusFlag;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
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

    public String getContractStartTime() {
        return contractStartTime;
    }

    public void setContractStartTime(String contractStartTime) {
        this.contractStartTime = contractStartTime;
    }

    public String getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(String contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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
