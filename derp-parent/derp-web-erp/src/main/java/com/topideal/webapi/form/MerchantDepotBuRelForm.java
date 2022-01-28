package com.topideal.webapi.form;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class MerchantDepotBuRelForm {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "公司id")
    private Long merchantId;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "仓库id")
    private Long depotId;

    @ApiModelProperty(value = "仓库类别 a-保税仓，b-备查库，c-海外仓，d-中转仓,e-暂存区，f-销毁区 g-内贸仓（多个值以“,”隔开）")
    private String type ;

    @ApiModelProperty(value = "是否代销仓(1-是,0-否)")
    private String isTopBooks;

    @ApiModelProperty(value = "是否代客管理仓库")
    private String isValetManage;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsTopBooks() {
        return isTopBooks;
    }

    public void setIsTopBooks(String isTopBooks) {
        this.isTopBooks = isTopBooks;
    }

    public String getIsValetManage() {
        return isValetManage;
    }

    public void setIsValetManage(String isValetManage) {
        this.isValetManage = isValetManage;
    }
}
