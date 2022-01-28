package com.topideal.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 库存调整
 *
 * @author lian_
 */
public class AdjustmentInventoryModel extends PageModel implements Serializable {

    // 盘点仓库名称
    private String depotName;
    // 创建人id
    private Long createUserId;
    // 库存调整单号
    private String code;
    // 盘点仓库id
    private Long depotId;
    // 商家名称
    private String merchantName;
    // 来源单据号
    private String sourceCode;
    // 创建人名称
    private String createUsername;
    // 商家id
    private Long merchantId;
    // 创建时间
    private Timestamp createDate;
    private Timestamp modifyDate;
    // 业务类别
    private String model;
    // id
    private Long id;
    // 调整时间
    private Timestamp adjustmentTime;
    // 状态
    private String status;
    // 月份
    private String months;
    //单据所属日期
    private Timestamp sourceTime;
    //备注
    private String remark;

    /**
     * 确认人id
     */
    private Long confirmUserId;
    /**
     * 确认人名称
     */
    private String confirmUsername;
    /**
     * 确认时间
     */
    private Timestamp confirmTime;

    /**
     * 扩展字段
     */
    //商品名称
//	private String goodsName;
//
//
//	// 表体
//	private List<AdjustmentInventoryItemModel> itemList;
    //po号
    private String poNo;

    //单据来源（01:接口回传 02:手工录入）
    private String source;

    //币种
    private String currency;

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(Timestamp sourceTime) {
        this.sourceTime = sourceTime;
    }

    /* months get 方法 */
    public String getMonths() {
        return months;
    }

    /* months set 方法 */
    public void setMonths(String months) {
        this.months = months;
    }

    /* depotName get 方法 */
    public String getDepotName() {
        return depotName;
    }

    /* depotName set 方法 */
    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    /* createUserId get 方法 */
    public Long getCreateUserId() {
        return createUserId;
    }

    /* createUserId set 方法 */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /* code get 方法 */
    public String getCode() {
        return code;
    }

    /* code set 方法 */
    public void setCode(String code) {
        this.code = code;
    }

    /* depotId get 方法 */
    public Long getDepotId() {
        return depotId;
    }

    /* depotId set 方法 */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    /* merchantName get 方法 */
    public String getMerchantName() {
        return merchantName;
    }

    /* merchantName set 方法 */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /* sourceCode get 方法 */
    public String getSourceCode() {
        return sourceCode;
    }

    /* sourceCode set 方法 */
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    /* createUsername get 方法 */
    public String getCreateUsername() {
        return createUsername;
    }

    /* createUsername set 方法 */
    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    /* merchantId get 方法 */
    public Long getMerchantId() {
        return merchantId;
    }

    /* merchantId set 方法 */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }


    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /* model get 方法 */
    public String getModel() {
        return model;
    }

    /* model set 方法 */
    public void setModel(String model) {
        this.model = model;
    }

    /* id get 方法 */
    public Long getId() {
        return id;
    }

    /* id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /* adjustmentTime get 方法 */
    public Timestamp getAdjustmentTime() {
        return adjustmentTime;
    }

    /* adjustmentTime set 方法 */
    public void setAdjustmentTime(Timestamp adjustmentTime) {
        this.adjustmentTime = adjustmentTime;
    }

    /* status get 方法 */
    public String getStatus() {
        return status;
    }

    /* status set 方法 */
    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getConfirmUserId() {
        return confirmUserId;
    }

    public void setConfirmUserId(Long confirmUserId) {
        this.confirmUserId = confirmUserId;
    }

    public String getConfirmUsername() {
        return confirmUsername;
    }

    public void setConfirmUsername(String confirmUsername) {
        this.confirmUsername = confirmUsername;
    }

    public Timestamp getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Timestamp confirmTime) {
        this.confirmTime = confirmTime;
    }
}
