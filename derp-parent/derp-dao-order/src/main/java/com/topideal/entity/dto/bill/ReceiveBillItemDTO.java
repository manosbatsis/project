package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ReceiveBillItemDTO extends PageModel implements Serializable {

    /**
     * ID
     */
    private Long id;
    /**
     * 应收账单Id
     */
    private Long billId;
    /**
     * 系统业务单号
     */
    private String code;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * po单号
     */
    private String poNo;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品货号
     */
    private String goodsNo;
    /**
     * 结算金额
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;

    //标准条码
    private String commbarcode;

    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 母品牌
     */
    private String parentBrandName;
    /**
     * 母品牌编码
     */
    private String parentBrandCode;
    /**
     * 科目编码
     */
    private String subjectCode;
    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * NC收支费项名称
     */
    private String paymentSubjectName;
    /**
     * NC收支费项id
     */
    private Long paymentSubjectId;
    /**
     * 平台sku条码
     */
    private String platformSku;
    /**
     * 发票描述
     */
    private String invoiceDescription;
    /**
     * 母品牌id
     */
    private Long parentBrandId;
    /**
     * 数据来源：0-单据 1-手动添加
     */
    private String dataSource;
    /**
     * SD单类型 1-采购SD，2-采购退SD，3-调整SD'
     */
    private String sdType;
    private String sdTypeLabel;

    //核销平台
    private String verifyPlatformCode;
    private String verifyPlatformName;

    //待核销金额
    private BigDecimal beVerifyAmount;
    //应收单号
    private String receiveCode;
    
    /**
     * 税率
     */
     private Double taxRate;
     /**
     * 税额
     */
     private BigDecimal tax;
     /**
     * 结算金额（含税）
     */
     private BigDecimal taxAmount;

    /**
     * 费项编码
     */
     private String projectCode;

    /**
     * 条形码
     */
     private String barcode;
    /**
     * 入账时间
     */
    private Timestamp creditDate;

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*billId get 方法 */
    public Long getBillId() {
        return billId;
    }

    /*billId set 方法 */
    public void setBillId(Long billId) {
        this.billId = billId;
    }

    /*code get 方法 */
    public String getCode() {
        return code;
    }

    /*code set 方法 */
    public void setCode(String code) {
        this.code = code;
    }

    /*projectId get 方法 */
    public Long getProjectId() {
        return projectId;
    }

    /*projectId set 方法 */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /*projectName get 方法 */
    public String getProjectName() {
        return projectName;
    }

    /*projectName set 方法 */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /*poNo get 方法 */
    public String getPoNo() {
        return poNo;
    }

    /*poNo set 方法 */
    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    /*goodsId get 方法 */
    public Long getGoodsId() {
        return goodsId;
    }

    /*goodsId set 方法 */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /*goodsName get 方法 */
    public String getGoodsName() {
        return goodsName;
    }

    /*goodsName set 方法 */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /*goodsNo get 方法 */
    public String getGoodsNo() {
        return goodsNo;
    }

    /*goodsNo set 方法 */
    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    /*price get 方法 */
    public BigDecimal getPrice() {
        return price;
    }

    /*price set 方法 */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /*num get 方法 */
    public Integer getNum() {
        return num;
    }

    /*num set 方法 */
    public void setNum(Integer num) {
        this.num = num;
    }

    /*createDate get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /*modifyDate get 方法 */
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPaymentSubjectName() {
        return paymentSubjectName;
    }

    public void setPaymentSubjectName(String paymentSubjectName) {
        this.paymentSubjectName = paymentSubjectName;
    }

    public Long getPaymentSubjectId() {
        return paymentSubjectId;
    }

    public void setPaymentSubjectId(Long paymentSubjectId) {
        this.paymentSubjectId = paymentSubjectId;
    }

    public String getParentBrandCode() {
        return parentBrandCode;
    }

    public void setParentBrandCode(String parentBrandCode) {
        this.parentBrandCode = parentBrandCode;
    }

    public String getPlatformSku() {
        return platformSku;
    }

    public void setPlatformSku(String platformSku) {
        this.platformSku = platformSku;
    }

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getSdType() {
        return sdType;
    }

    public void setSdType(String sdType) {
        this.sdType = sdType;
        this.sdTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseSdOrder_typeList, sdType) ;
    }

    public String getSdTypeLabel() {
        return sdTypeLabel;
    }

    public void setSdTypeLabel(String sdTypeLabel) {
        this.sdTypeLabel = sdTypeLabel;
    }

    public String getVerifyPlatformCode() {
        return verifyPlatformCode;
    }

    public void setVerifyPlatformCode(String verifyPlatformCode) {
        this.verifyPlatformCode = verifyPlatformCode;
        this.verifyPlatformName = DERP.getLabelByKey(DERP.storePlatformList, verifyPlatformCode);
    }

    public String getVerifyPlatformName() {
        return verifyPlatformName;
    }

    public void setVerifyPlatformName(String verifyPlatformName) {
        this.verifyPlatformName = verifyPlatformName;
    }

    public BigDecimal getBeVerifyAmount() {
        return beVerifyAmount;
    }

    public void setBeVerifyAmount(BigDecimal beVerifyAmount) {
        this.beVerifyAmount = beVerifyAmount;
    }

    public String getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(String receiveCode) {
        this.receiveCode = receiveCode;
    }

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Timestamp getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(Timestamp creditDate) {
        this.creditDate = creditDate;
    }
}
