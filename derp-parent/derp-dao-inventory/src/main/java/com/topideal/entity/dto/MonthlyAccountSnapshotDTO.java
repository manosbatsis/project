package com.topideal.entity.dto;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 月结账单快照
 *
 * @author lian_
 */
@ApiModel
public class MonthlyAccountSnapshotDTO extends PageModel implements Serializable {

    //商品货号
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    //失效日期
    @ApiModelProperty(value = "失效日期")
    private Date overdueDate;
    //仓库名称
    @ApiModelProperty(value = "仓库名称")
    private String depotName;
    //批次号
    @ApiModelProperty(value = "批次号")
    private String batchNo;
    //商品id
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    //仓库id
    @ApiModelProperty(value = "仓库id")
    private Long depotId;
    //库存余量
    @ApiModelProperty(value = "库存余量")
    private Integer surplusNum;
    //现库存
    @ApiModelProperty(value = "现库存")
    private Integer availableNum;
    //库存类型  0 正常品  1 残次品
    @ApiModelProperty(value = "库存类型  0 正常品  1 残次品")
    private String type;
    //商家名称
    @ApiModelProperty(value = "商家名称")
    private String merchantName;
    //生产日期
    @ApiModelProperty(value = "生产日期")
    private Date productionDate;
    //商家ID
    @ApiModelProperty(value = "商家ID")
    private Long merchantId;
    //创建人
    @ApiModelProperty(value = "创建人")
    private Long creater;
    //结转月份
    @ApiModelProperty(value = "结转月份")
    private String settlementMonth;
    //id
    @ApiModelProperty(value = "id")
    private Long id;
    //商品名称
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    //创建时间
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    //库存单位
    @ApiModelProperty(value = "库存单位")
    private String unit;

    @ApiModelProperty(value = "快照日期")
    private String snapshotDate;

    //状态：1未转结 2 已转结
    @ApiModelProperty(value = "状态：1未转结 2 已转结")
    private String state;

    //标准条码
    @ApiModelProperty(value = "标准条码")
    private String commbarcode;

    @ApiModelProperty(value = "库存类型中文")
    private String typeLabel;
    @ApiModelProperty(value = "库存单位中文")
    private String unitLabel;
    @ApiModelProperty(value = "状态中文")
    private String stateLabel;

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public void setUnitLabel(String unitLabel) {
        this.unitLabel = unitLabel;
    }

    public String getStateLabel() {
        return stateLabel;
    }

    public void setStateLabel(String stateLabel) {
        this.stateLabel = stateLabel;
    }

    public String getUnit() {
        return unit;
    }

    /*goodsNo get 方法 */
    public String getGoodsNo() {
        return goodsNo;
    }

    /*goodsNo set 方法 */
    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    /*overdueDate get 方法 */
    public Date getOverdueDate() {
        return overdueDate;
    }

    /*overdueDate set 方法 */
    public void setOverdueDate(Date overdueDate) {
        this.overdueDate = overdueDate;
    }

    /*depotName get 方法 */
    public String getDepotName() {
        return depotName;
    }

    /*depotName set 方法 */
    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    /*batchNo get 方法 */
    public String getBatchNo() {
        return batchNo;
    }

    /*batchNo set 方法 */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /*goodsId get 方法 */
    public Long getGoodsId() {
        return goodsId;
    }

    /*goodsId set 方法 */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /*depotId get 方法 */
    public Long getDepotId() {
        return depotId;
    }

    /*depotId set 方法 */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    /*surplusNum get 方法 */
    public Integer getSurplusNum() {
        return surplusNum;
    }

    /*surplusNum set 方法 */
    public void setSurplusNum(Integer surplusNum) {
        this.surplusNum = surplusNum;
    }

    /*availableNum get 方法 */
    public Integer getAvailableNum() {
        return availableNum;
    }

    /*availableNum set 方法 */
    public void setAvailableNum(Integer availableNum) {
        this.availableNum = availableNum;
    }

    /*type get 方法 */
    public String getType() {
        return type;
    }

    /*merchantName get 方法 */
    public String getMerchantName() {
        return merchantName;
    }

    /*merchantName set 方法 */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /*productionDate get 方法 */
    public Date getProductionDate() {
        return productionDate;
    }

    /*productionDate set 方法 */
    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    /*merchantId get 方法 */
    public Long getMerchantId() {
        return merchantId;
    }

    /*merchantId set 方法 */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /*creater get 方法 */
    public Long getCreater() {
        return creater;
    }

    /*creater set 方法 */
    public void setCreater(Long creater) {
        this.creater = creater;
    }

    /*settlementMonth get 方法 */
    public String getSettlementMonth() {
        return settlementMonth;
    }

    /*settlementMonth set 方法 */
    public void setSettlementMonth(String settlementMonth) {
        this.settlementMonth = settlementMonth;
    }

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*goodsName get 方法 */
    public String getGoodsName() {
        return goodsName;
    }

    /*goodsName set 方法 */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /*createDate get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(String snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public String getState() {
        return state;
    }


    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    /*type set 方法 */
    public void setType(String type) {
        this.type = type;
        this.typeLabel = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, type);
    }

    public void setUnit(String unit) {
        this.unit = unit;
        this.unitLabel = DERP.getLabelByKey(DERP.inventory_unitList, unit);
    }

    public void setState(String state) {
        this.state = state;
        this.stateLabel = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.monthlyAccount_stateList, state);
    }

}

