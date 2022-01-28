package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MerchandiseContrastDTO extends PageModel implements Serializable {
    
	@ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "平台名称")
    private String platformName;
    @ApiModelProperty(value = "平台用户名")
    private String platformUsername;
    @ApiModelProperty(value = "修改日期")
    private Timestamp modifyDate;
    @ApiModelProperty(value = "爬虫商品货号")
    private String crawlerGoodsNo;
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
    @ApiModelProperty(value = "爬虫商品名称")
    private String crawlerGoodsName;
    @ApiModelProperty(value = "创建人用户名")
    private String createUsername;
    @ApiModelProperty(value = "商家ID")
    private Long merchantId;
    @ApiModelProperty(value = "修改人用户名")
    private String updateUsername;
    @ApiModelProperty(value = "状态 0-启用 1-禁用")
    private String status;
    @ApiModelProperty(value = "状态（中文）")
    private String statusLabel;
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    @ApiModelProperty(value = "标准条码")
    private String commbarcode;
    @ApiModelProperty(value = "事业部名称")
    private String buName;
    @ApiModelProperty(value = "事业部id")
    private Long buId;    
    @ApiModelProperty(value = "类型 1-云集 2-唯品")
    private String type ;
    @ApiModelProperty(value = "类型（中文）")
    private String typeLabel ;
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    @ApiModelProperty(value = "数量")
    private Integer num;
    @ApiModelProperty(value = "销售标准单价")
    private BigDecimal price;
    @ApiModelProperty(value = "id集合，多个用逗号隔开")
    private String ids;

    /*goodsNo get 方法 */
    public String getGoodsNo() {
        return goodsNo;
    }

    /*goodsNo set 方法 */
    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    /*modifyDate get 方法 */
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    /*crawlerGoodsNo get 方法 */
    public String getCrawlerGoodsNo() {
        return crawlerGoodsNo;
    }

    /*crawlerGoodsNo set 方法 */
    public void setCrawlerGoodsNo(String crawlerGoodsNo) {
        this.crawlerGoodsNo = crawlerGoodsNo;
    }

    /*merchantName get 方法 */
    public String getMerchantName() {
        return merchantName;
    }

    /*merchantName set 方法 */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /*crawlerGoodsName get 方法 */
    public String getCrawlerGoodsName() {
        return crawlerGoodsName;
    }

    /*crawlerGoodsName set 方法 */
    public void setCrawlerGoodsName(String crawlerGoodsName) {
        this.crawlerGoodsName = crawlerGoodsName;
    }

    /*createUsername get 方法 */
    public String getCreateUsername() {
        return createUsername;
    }

    /*createUsername set 方法 */
    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    /*merchantId get 方法 */
    public Long getMerchantId() {
        return merchantId;
    }

    /*merchantId set 方法 */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /*updateUsername get 方法 */
    public String getUpdateUsername() {
        return updateUsername;
    }

    /*updateUsername set 方法 */
    public void setUpdateUsername(String updateUsername) {
        this.updateUsername = updateUsername;
    }

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*platformName get 方法 */
    public String getPlatformName() {
        return platformName;
    }

    /*platformName set 方法 */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }


    /*platformUsername get 方法 */
    public String getPlatformUsername() {
        return platformUsername;
    }

    /*platformUsername set 方法 */
    public void setPlatformUsername(String platformUsername) {
        this.platformUsername = platformUsername;
    }

    /*status get 方法 */
    public String getStatus() {
        return status;
    }

    /*status set 方法 */
    public void setStatus(String status) {
        this.status = status;
        if (StringUtils.isNotBlank(status)) {
            this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.merchandiseContrast_statusList, status);
        }
    }

    /*createDate get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }


    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.merchandiseContrast_typeList, type) ;
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
    
}