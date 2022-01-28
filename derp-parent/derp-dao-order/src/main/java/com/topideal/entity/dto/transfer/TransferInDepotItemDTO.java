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
 * 调拨入库表体
 *
 * @author lian_
 */
@ApiModel
public class TransferInDepotItemDTO extends PageModel implements Serializable {

    //失效日期
    @ApiModelProperty(value = "失效日期", required = false)
    private Date overdueDate;
    //调拨出库ID
    @ApiModelProperty(value = "调拨出库id", required = false)
    private Long transferDepotId;
    //调入商品id
    @ApiModelProperty(value = "调入商品id", required = false)
    private Long inGoodsId;
    //调入商品货号
    @ApiModelProperty(value = "调入商品货号", required = false)
    private String inGoodsNo;
    //调拨批次
    @ApiModelProperty(value = "调拨批次", required = false)
    private String transferBatchNo;
    //生产日期
    @ApiModelProperty(value = "生产日期", required = false)
    private Date productionDate;
    //调入商品名称
    @ApiModelProperty(value = "调入商品名称", required = false)
    private String inGoodsName;
    //退货正常品数量
    @ApiModelProperty(value = "退货正常品数量", required = false)
    private Integer transferNum;
    //创建人
    @ApiModelProperty(value = "创建人", required = false)
    private Long creater;
    //调入商品编码
    @ApiModelProperty(value = "调入商品编码", required = false)
    private String inGoodsCode;
    //id
    @ApiModelProperty(value = "id", required = false)
    private Long id;
    //创建时间
    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;
    //理货单位 00-托盘 01-箱 02-件
    @ApiModelProperty(value = "理货单位", required = false)
    private String tallyingUnit;
    @ApiModelProperty(value = "理货单位中文", required = false)
    private String tallyingUnitLabel;
    //条形码
    @ApiModelProperty(value = "条形码", required = false)
    private String barcode;
    //坏货数量
    @ApiModelProperty(value = "坏货数量", required = false)
    private Integer wornNum;
    //过期数量
    @ApiModelProperty(value = "过期数量", required = false)
    private Integer expireNum;
    //修改时间
    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;
    //调入商品标准条码
    @ApiModelProperty(value = "调入商品标准条码", required = false)
    private String inCommbarcode;

    //扩展字段
    //调出商品id
    @ApiModelProperty(value = "调出商品id", required = false)
    private Long outGoodsId;
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

    /*transferDepotId get 方法 */
    public Long getTransferDepotId() {
        return transferDepotId;
    }

    /*transferDepotId set 方法 */
    public void setTransferDepotId(Long transferDepotId) {
        this.transferDepotId = transferDepotId;
    }

    /*inGoodsId get 方法 */
    public Long getInGoodsId() {
        return inGoodsId;
    }

    /*inGoodsId set 方法 */
    public void setInGoodsId(Long inGoodsId) {
        this.inGoodsId = inGoodsId;
    }

    /*inGoodsNo get 方法 */
    public String getInGoodsNo() {
        return inGoodsNo;
    }

    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String inGoodsNo) {
        this.inGoodsNo = inGoodsNo;
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

    /*inGoodsName get 方法 */
    public String getInGoodsName() {
        return inGoodsName;
    }

    /*inGoodsName set 方法 */
    public void setInGoodsName(String inGoodsName) {
        this.inGoodsName = inGoodsName;
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

    /*inGoodsCode get 方法 */
    public String getInGoodsCode() {
        return inGoodsCode;
    }

    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String inGoodsCode) {
        this.inGoodsCode = inGoodsCode;
    }

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
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

    public String getInCommbarcode() {
        return inCommbarcode;
    }

    public void setInCommbarcode(String inCommbarcode) {
        this.inCommbarcode = inCommbarcode;
    }

    public Long getOutGoodsId() {
        return outGoodsId;
    }

    public void setOutGoodsId(Long outGoodsId) {
        this.outGoodsId = outGoodsId;
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

