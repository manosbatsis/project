package com.topideal.entity.dto.transfer;


import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 * 调拨出库表体
 *
 * @author lian_
 */
@ApiModel
public class TransferOutDepotItemDTO extends PageModel implements Serializable {

    //失效日期
    @ApiModelProperty(value = "失效日期", required = false)
    private Date overdueDate;
    //调出商品编码
    @ApiModelProperty(value = "调出商品编码", required = false)
    private String outGoodsCode;
    //调拨出库ID
    @ApiModelProperty(value = "调拨出库id", required = false)
    private Long transferDepotId;
    //调出商品名称
    @ApiModelProperty(value = "调出商品名称", required = false)
    private String outGoodsName;
    //调拨批次
    @ApiModelProperty(value = "调拨批次", required = false)
    private String transferBatchNo;
    //生产日期
    @ApiModelProperty(value = "生产日期", required = false)
    private Date productionDate;
    //调出商品id
    @ApiModelProperty(value = "调出商品id", required = false)
    private Long outGoodsId;
    //调拨数量
    @ApiModelProperty(value = "调拨数量", required = false)
    private Integer transferNum;
    //创建人
    @ApiModelProperty(value = "创建人id", required = false)
    private Long creater;
    //id
    @ApiModelProperty(value = "id", required = false)
    private Long id;
    //调出商品货号
    @ApiModelProperty(value = "调出商品货号", required = false)
    private String outGoodsNo;
    //创建日期
    @ApiModelProperty(value = "创建日期", required = false)
    private Timestamp createDate;
    //理货单位 00-托盘 01-箱 02-件
    @ApiModelProperty(value = "理货单位", required = false)
    private String tallyingUnit;
    @ApiModelProperty(value = "理货单位中文", required = false)
    private String tallyingUnitLabel;
    //条形码
    @ApiModelProperty(value = "条形码", required = false)
    private String barcode;
    //修改时间
    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;
    //调出商品标准条码
    @ApiModelProperty(value = "调出商品标准条码", required = false)
    private String outCommbarcode;
    //坏货数量
    @ApiModelProperty(value = "坏货数量", required = false)
    private Integer wornNum;
    //过期数量
    @ApiModelProperty(value = "过期数量", required = false)
    private Integer expireNum;
    @ApiModelProperty(value = "生产日期字符串，日期格式：yyyy-MM-dd", required = false)
    private String productionDateStr;
    @ApiModelProperty(value = "失效日期字符串，日期格式：yyyy-MM-dd", required = false)
    private String overdueDateStr;

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getTallyingUnit() {
        return tallyingUnit;
    }

    public void setTallyingUnit(String tallyingUnit) {
        this.tallyingUnit = tallyingUnit;
        if (StringUtils.isNotBlank(tallyingUnit)) {
            this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
        }
    }

    /*overdueDate get 方法 */
    public Date getOverdueDate() {
        return overdueDate;
    }

    /*overdueDate set 方法 */
    public void setOverdueDate(Date overdueDate) {
        this.overdueDate = overdueDate;
    }

    /*outGoodsCode get 方法 */
    public String getOutGoodsCode() {
        return outGoodsCode;
    }

    /*outGoodsCode set 方法 */
    public void setOutGoodsCode(String outGoodsCode) {
        this.outGoodsCode = outGoodsCode;
    }

    /*transferDepotId get 方法 */
    public Long getTransferDepotId() {
        return transferDepotId;
    }

    /*transferDepotId set 方法 */
    public void setTransferDepotId(Long transferDepotId) {
        this.transferDepotId = transferDepotId;
    }

    /*outGoodsName get 方法 */
    public String getOutGoodsName() {
        return outGoodsName;
    }

    /*outGoodsName set 方法 */
    public void setOutGoodsName(String outGoodsName) {
        this.outGoodsName = outGoodsName;
    }

    /*transferBatchNo get 方法 */
    public String getTransferBatchNo() {
        return transferBatchNo;
    }

    /*transferBatchNo set 方法 */
    public void setTransferBatchNo(String transferBatchNo) {
        this.transferBatchNo = transferBatchNo;
    }

    /*productionDate get 方法 */
    public Date getProductionDate() {
        return productionDate;
    }

    /*productionDate set 方法 */
    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    /*outGoodsId get 方法 */
    public Long getOutGoodsId() {
        return outGoodsId;
    }

    /*outGoodsId set 方法 */
    public void setOutGoodsId(Long outGoodsId) {
        this.outGoodsId = outGoodsId;
    }

    /*transferNum get 方法 */
    public Integer getTransferNum() {
        return transferNum;
    }

    /*transferNum set 方法 */
    public void setTransferNum(Integer transferNum) {
        this.transferNum = transferNum;
    }

    /*creater get 方法 */
    public Long getCreater() {
        return creater;
    }

    /*creater set 方法 */
    public void setCreater(Long creater) {
        this.creater = creater;
    }

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*outGoodsNo get 方法 */
    public String getOutGoodsNo() {
        return outGoodsNo;
    }

    /*outGoodsNo set 方法 */
    public void setOutGoodsNo(String outGoodsNo) {
        this.outGoodsNo = outGoodsNo;
    }

    /*createDate get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getTallyingUnitLabel() {
        return tallyingUnitLabel;
    }

    public String getOutCommbarcode() {
        return outCommbarcode;
    }

    public void setOutCommbarcode(String outCommbarcode) {
        this.outCommbarcode = outCommbarcode;
    }

    public Integer getWornNum() {
        return wornNum;
    }

    public void setWornNum(Integer wornNum) {
        this.wornNum = wornNum;
    }

    public Integer getExpireNum() {
        return expireNum;
    }

    public void setExpireNum(Integer expireNum) {
        this.expireNum = expireNum;
    }

    public void setTallyingUnitLabel(String tallyingUnitLabel) {
        this.tallyingUnitLabel = tallyingUnitLabel;
    }

    public String getProductionDateStr() {
        return productionDateStr;
    }

    public void setProductionDateStr(String productionDateStr) {
        this.productionDateStr = productionDateStr;
    }

    public String getOverdueDateStr() {
        return overdueDateStr;
    }

    public void setOverdueDateStr(String overdueDateStr) {
        this.overdueDateStr = overdueDateStr;
    }
}

