package com.topideal.entity.dto.main;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CustomerSalePriceCountDTO implements Serializable {

    @ApiModelProperty(value="待审核",required = false)
    private Long beAudit;
    @ApiModelProperty(value="待提交",required = false)
    private Long beSubmited;
    @ApiModelProperty(value="已审核",required = false)
    private Long beAreadyAudit;
    @ApiModelProperty(value="已驳回",required = false)
    private Long beRejected;
    @ApiModelProperty(value="所有",required = false)
    private Long beAll;
    @ApiModelProperty(value="待作废",required = false)
    private Long beInvalid;
    @ApiModelProperty(value="已作废",required = false)
    private Long beAlreadyInvalid;

    public Long getBeAudit() {
        return beAudit;
    }

    public void setBeAudit(Long beAudit) {
        this.beAudit = beAudit;
    }

    public Long getBeSubmited() {
        return beSubmited;
    }

    public void setBeSubmited(Long beSubmited) {
        this.beSubmited = beSubmited;
    }

    public Long getBeAreadyAudit() {
        return beAreadyAudit;
    }

    public void setBeAreadyAudit(Long beAreadyAudit) {
        this.beAreadyAudit = beAreadyAudit;
    }

    public Long getBeRejected() {
        return beRejected;
    }

    public void setBeRejected(Long beRejected) {
        this.beRejected = beRejected;
    }

    public Long getBeAll() {
        return beAll;
    }

    public void setBeAll(Long beAll) {
        this.beAll = beAll;
    }

	public Long getBeInvalid() {
		return beInvalid;
	}

	public void setBeInvalid(Long beInvalid) {
		this.beInvalid = beInvalid;
	}

	public Long getBeAlreadyInvalid() {
		return beAlreadyInvalid;
	}

	public void setBeAlreadyInvalid(Long beAlreadyInvalid) {
		this.beAlreadyInvalid = beAlreadyInvalid;
	}
}
