package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 盘点结果表
 */
@ApiModel
public class TakesStockResultsDTO extends PageModel implements Serializable {

	@ApiModelProperty(value = "盘点仓库名称")
    private String depotName;
    @ApiModelProperty(value = "盘点单号")
    private String code;
    @ApiModelProperty(value = "盘点指令id")
    private Long takesStockId;
    @ApiModelProperty(value = "盘点仓库id")
    private Long depotId;
    @ApiModelProperty(value = "盘点指令单号")
    private String takesStockCode;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "确认时间")
    private Timestamp confirmTime;
    @ApiModelProperty(value = "驳回原因")
    private String dismissRemark;
    @ApiModelProperty(value = "提交人id")
    private Long confirmUserId;
    @ApiModelProperty(value = "提交人名称")
    private String confirmUsername;
    @ApiModelProperty(value = "接收时间")
    private Timestamp createDate;
    @ApiModelProperty(value = "服务类型")
    private String serverType;
    @ApiModelProperty(value = "服务类型（中文）")
    private String serverTypeLabel;

    @ApiModelProperty(value = "业务场景")
    private String model;
    @ApiModelProperty(value = "业务场景（中文）")
    private String modelLabel;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "状态（中文）")
    private String statusLabel;

    @ApiModelProperty(value = "商家id")
    private Long merchantId;
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
    @ApiModelProperty(value = "归属时间")
    private Timestamp sourceTime;
    @ApiModelProperty(value = "来源状态 1.op盘点结果 2 ofc盘点结果")
    private String sourceType;
    @ApiModelProperty(value = "来源状态（中文）")
    private String sourceTypeLabel;

    @ApiModelProperty(value = "来源单据号")
    private String sourceCode;
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    @ApiModelProperty(value = "po单号")
    private String poNo;

    @ApiModelProperty(value = "po时间")
    private Timestamp poDate;

    @ApiModelProperty(value = "币种")
    private String currency;
    private String currencyLabel;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "事业部id")
    private Long buId;
    @ApiModelProperty(value = "入库确认人id")
    private Long inConfirmUserId;
    @ApiModelProperty(value = "入库确认人名称")
    private String inConfirmUsername;
    @ApiModelProperty(value = "入库确认人id")
    private Timestamp inConfirmTime;
    @ApiModelProperty(value = "表体")
    private List<TakesStockResultItemDTO> details = new ArrayList<>();
    @ApiModelProperty(value = "创建人id") 
    private Long createUserId;
    @ApiModelProperty(value = "创建人名称") 
    private String createUsername;
    @ApiModelProperty(value = "单据id集合，多个用逗号隔开")
    private String ids;

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Timestamp getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(Timestamp sourceTime) {
        this.sourceTime = sourceTime;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
        if (StringUtils.isNotBlank(sourceType)) {
            this.sourceTypeLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_sourceTypeList, sourceType);
        }
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    /*depotName get 方法 */
    public String getDepotName() {
        return depotName;
    }

    /*depotName set 方法 */
    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    /*code get 方法 */
    public String getCode() {
        return code;
    }

    /*code set 方法 */
    public void setCode(String code) {
        this.code = code;
    }

    /*takesStockId get 方法 */
    public Long getTakesStockId() {
        return takesStockId;
    }

    /*takesStockId set 方法 */
    public void setTakesStockId(Long takesStockId) {
        this.takesStockId = takesStockId;
    }

    /*depotId get 方法 */
    public Long getDepotId() {
        return depotId;
    }

    /*depotId set 方法 */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    /*takesStockCode get 方法 */
    public String getTakesStockCode() {
        return takesStockCode;
    }

    /*takesStockCode set 方法 */
    public void setTakesStockCode(String takesStockCode) {
        this.takesStockCode = takesStockCode;
    }

    /*remark get 方法 */
    public String getRemark() {
        return remark;
    }

    /*remark set 方法 */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /*confirmTime get 方法 */
    public Timestamp getConfirmTime() {
        return confirmTime;
    }

    /*confirmTime set 方法 */
    public void setConfirmTime(Timestamp confirmTime) {
        this.confirmTime = confirmTime;
    }

    /*dismissRemark get 方法 */
    public String getDismissRemark() {
        return dismissRemark;
    }

    /*dismissRemark set 方法 */
    public void setDismissRemark(String dismissRemark) {
        this.dismissRemark = dismissRemark;
    }

    /*confirmUserId get 方法 */
    public Long getConfirmUserId() {
        return confirmUserId;
    }

    /*confirmUserId set 方法 */
    public void setConfirmUserId(Long confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    /*confirmUsername get 方法 */
    public String getConfirmUsername() {
        return confirmUsername;
    }

    /*confirmUsername set 方法 */
    public void setConfirmUsername(String confirmUsername) {
        this.confirmUsername = confirmUsername;
    }

    /*createTime get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createTime set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /*serverType get 方法 */
    public String getServerType() {
        return serverType;
    }

    /*serverType set 方法 */
    public void setServerType(String serverType) {
        this.serverType = serverType;
        if (StringUtils.isNotBlank(serverType)) {
            this.serverTypeLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_serverTypeList, serverType);
        }
    }

    /*model get 方法 */
    public String getModel() {
        return model;
    }

    /*model set 方法 */
    public void setModel(String model) {
        this.model = model;
        if (StringUtils.isNotBlank(model)) {
            this.modelLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_modelList, model);
        }
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
        if (StringUtils.isNotBlank(status)) {
            this.statusLabel = DERP_STORAGE.getLabelByKey(DERP_STORAGE.takesStockResult_statusList, status);
        }
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public Timestamp getPoDate() {
        return poDate;
    }

    public void setPoDate(Timestamp poDate) {
        this.poDate = poDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        if (StringUtils.isNotBlank(currency)) {
            this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
        }
    }

    public String getServerTypeLabel() {
        return serverTypeLabel;
    }

    public String getModelLabel() {
        return modelLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public String getSourceTypeLabel() {
        return sourceTypeLabel;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public List<TakesStockResultItemDTO> getDetails() {
        return details;
    }

    public void setDetails(List<TakesStockResultItemDTO> details) {
        this.details = details;
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

    public Long getInConfirmUserId() {
        return inConfirmUserId;
    }

    public void setInConfirmUserId(Long inConfirmUserId) {
        this.inConfirmUserId = inConfirmUserId;
    }

    public String getInConfirmUsername() {
        return inConfirmUsername;
    }

    public void setInConfirmUsername(String inConfirmUsername) {
        this.inConfirmUsername = inConfirmUsername;
    }

    public Timestamp getInConfirmTime() {
        return inConfirmTime;
    }

    public void setInConfirmTime(Timestamp inConfirmTime) {
        this.inConfirmTime = inConfirmTime;
    }

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}

