package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 批次库存明细
 *
 * @author lian_
 */
@ApiModel
public class InventoryBatchDTO extends PageModel implements Serializable {

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
    //在途库存
    @ApiModelProperty(value = "在途库存")
    private Integer onWayNum;
    //商品id
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    //仓库id
    @ApiModelProperty(value = "仓库id")
    private Long depotId;
    //库存余量
    @ApiModelProperty(value = "库存余量")
    private Integer surplusNum;
    //可用库存量
    @ApiModelProperty(value = "可用库存量")
    private Integer availableNum;
    //库存类型  0 正常品  1 残次品
    @ApiModelProperty(value = "库存类型  0 正常品  1 残次品")
    private String type;
    //商家名称
    @ApiModelProperty(value = "公司名称")
    private String merchantName;
    //商家名称
    @ApiModelProperty(value = "商家名称")
    private Date productionDate;
    //商家ID
    @ApiModelProperty(value = "公司ID")
    private Long merchantId;
    //商品名称
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    //创建人
    @ApiModelProperty(value = "创建人")
    private Long creater;
    //id
    @ApiModelProperty(value = "id")
    private Long id;
    //创建时间
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    //冻结库存量
    @ApiModelProperty(value = "冻结库存量")
    private Integer freezeNum;
    //托盘号
    @ApiModelProperty(value = "托盘号")
    private String lpn;
    //理货单位
    @ApiModelProperty(value = "理货单位")
    private String unit;
    //商家卓志编码
    @ApiModelProperty(value = "商家卓志编码")
    private String topidealCode;
    //仓库编码
    @ApiModelProperty(value = "仓库编码")
    private String depotCode;
    //仓库类型(a-卓志保税仓，b-非卓志保税仓，c-卓志海外仓，d-在途仓,e非卓志国内仓）
    @ApiModelProperty(value = "仓库类型")
    private String depotType;
    //是否代销仓(1-是,0-否)
    @ApiModelProperty(value = "是否代销仓")
    private String isTopBooks;
    //结转月份
    @ApiModelProperty(value = "结转月份")
    private String ownMonth;
    //是否过期（0 未过期 1已过期）
    @ApiModelProperty(value = "是否过期")
    private String isExpire;
    //条形码
    @ApiModelProperty(value = "条形码")
    private String barcode;
    /**
     * 品牌id
     */
    @ApiModelProperty(value = "品牌id")
    private Long brandId;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    // 修改时间
    @ApiModelProperty(value = "商品货号")
    private Timestamp modifyDate;


    //新增字段用于效期预警
    //剩余效期（天）
    @ApiModelProperty(value = "剩余效期（天）")
    private String surplusDays;
    //总效期（天）
    @ApiModelProperty(value = "总效期（天）")
    private String totalDays;
    //剩余效期
    @ApiModelProperty(value = "剩余效期")
    private String surplusDate;
    //筛选字段效期标识
    @ApiModelProperty(value = "筛选字段效期标识")
    private String validityType;
    ////筛选字段效期
    @ApiModelProperty(value = "筛选字段效期")
    private Double validityTime;
    //坏品总数
    @ApiModelProperty(value = "坏品总数")
    private Integer badNum;
    //好品总数
    @ApiModelProperty(value = "好品总数")
    private Integer okayNum;

    // 效期预警扩展字段
    @ApiModelProperty(value = "0＜X≤1/10")
    private String validityType1;//0＜X≤1/10
    @ApiModelProperty(value = "1/10＜X≤1/5")
    private String validityType2;//1/10＜X≤1/5
    @ApiModelProperty(value = "1/5＜X≤1/3")
    private String validityType3;//1/5＜X≤1/3
    @ApiModelProperty(value = "1/3＜X≤1/2")
    private String validityType4;//1/3＜X≤1/2
    @ApiModelProperty(value = "1/2＜X≤2/3")
    private String validityType5;//1/2＜X≤2/3
    @ApiModelProperty(value = "2/3以上")
    private String validityType6;//2/3以上
    @ApiModelProperty(value = "过期品(为负)")
    private String validityType7;//过期品(为负)

    //标准条码
    @ApiModelProperty(value = "标准条码")
    private String commbarcode;

    //'库存类型  0 正常品  1 残次品',
    @ApiModelProperty(value = "库存类型中文  0 正常品  1 残次品")
    private String typeLabel;
    //理货单位 0 托盘 1箱  2 件'
    @ApiModelProperty(value = "理货单位中文 0 托盘 1箱  2 件")
    private String unitLabel;
    //'仓库类型',
    @ApiModelProperty(value = "仓库类型中文")
    private String depotTypeLabel;
    //' 是否代销仓(1-是,0-否)'
    @ApiModelProperty(value = "是否代销仓中文(1-是,0-否)")
    private String isTopBooksLabel;
    /**
     * 是否过期 0-过期 1-未过期
     */
    @ApiModelProperty(value = "是否过期中文")
    private String isExpireLabel;


    /*查询条件非数据表字段start*/
    @ApiModelProperty(value = "仓库id集合")
    private List<Long> depotIdsList;//仓库id

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

    public String getDepotTypeLabel() {
        return depotTypeLabel;
    }

    public void setDepotTypeLabel(String depotTypeLabel) {
        this.depotTypeLabel = depotTypeLabel;
    }

    public String getIsTopBooksLabel() {
        return isTopBooksLabel;
    }

    public void setIsTopBooksLabel(String isTopBooksLabel) {
        this.isTopBooksLabel = isTopBooksLabel;
    }

    public String getValidityType1() {
        return validityType1;
    }

    public void setValidityType1(String validityType1) {
        this.validityType1 = validityType1;
    }

    public String getValidityType2() {
        return validityType2;
    }

    public void setValidityType2(String validityType2) {
        this.validityType2 = validityType2;
    }

    public String getValidityType3() {
        return validityType3;
    }

    public void setValidityType3(String validityType3) {
        this.validityType3 = validityType3;
    }

    public String getValidityType4() {
        return validityType4;
    }

    public void setValidityType4(String validityType4) {
        this.validityType4 = validityType4;
    }

    public String getValidityType5() {
        return validityType5;
    }

    public void setValidityType5(String validityType5) {
        this.validityType5 = validityType5;
    }

    public String getValidityType6() {
        return validityType6;
    }

    public void setValidityType6(String validityType6) {
        this.validityType6 = validityType6;
    }

    public String getValidityType7() {
        return validityType7;
    }

    public void setValidityType7(String validityType7) {
        this.validityType7 = validityType7;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
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

    /*barcode get 方法 */
    public String getBarcode() {
        return barcode;
    }

    /*barcode set 方法 */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /*isExpire get 方法 */
    public String getIsExpire() {
        return isExpire;
    }

    /*ownMonth get 方法 */
    public String getOwnMonth() {
        return ownMonth;
    }

    /*ownMonth set 方法 */
    public void setOwnMonth(String ownMonth) {
        this.ownMonth = ownMonth;
    }

    /*isTopBooks get 方法 */
    public String getIsTopBooks() {
        return isTopBooks;
    }

    /*depotType get 方法 */
    public String getDepotType() {
        return depotType;
    }

    /*depotCode get 方法 */
    public String getDepotCode() {
        return depotCode;
    }

    /*depotCode set 方法 */
    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }

    /*topidealCode get 方法 */
    public String getTopidealCode() {
        return topidealCode;
    }

    /*topidealCode set 方法 */
    public void setTopidealCode(String topidealCode) {
        this.topidealCode = topidealCode;
    }

    /*unit get 方法 */
    public String getUnit() {
        return unit;
    }

    /*lpn get 方法 */
    public String getLpn() {
        return lpn;
    }

    /*lpn set 方法 */
    public void setLpn(String lpn) {
        this.lpn = lpn;
    }

    /*freezeNum get 方法 */
    public Integer getFreezeNum() {
        return freezeNum;
    }

    /*freezeNum set 方法 */
    public void setFreezeNum(Integer freezeNum) {
        this.freezeNum = freezeNum;
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

    /*onWayNum get 方法 */
    public Integer getOnWayNum() {
        return onWayNum;
    }

    /*onWayNum set 方法 */
    public void setOnWayNum(Integer onWayNum) {
        this.onWayNum = onWayNum;
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

    /*goodsName get 方法 */
    public String getGoodsName() {
        return goodsName;
    }

    /*goodsName set 方法 */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    /*createDate get 方法 */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getSurplusDays() {
        return surplusDays;
    }

    public void setSurplusDays(String surplusDays) {
        this.surplusDays = surplusDays;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getSurplusDate() {
        return surplusDate;
    }

    public void setSurplusDate(String surplusDate) {
        this.surplusDate = surplusDate;
    }

    public String getValidityType() {
        return validityType;
    }

    public void setValidityType(String validityType) {
        this.validityType = validityType;
    }

    public Double getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Double validityTime) {
        this.validityTime = validityTime;
    }

    public Integer getBadNum() {
        return badNum;
    }

    public void setBadNum(Integer badNum) {
        this.badNum = badNum;
    }

    public Integer getOkayNum() {
        return okayNum;
    }

    public void setOkayNum(Integer okayNum) {
        this.okayNum = okayNum;
    }

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public String getIsExpireLabel() {
        return isExpireLabel;
    }

    public void setIsExpireLabel(String isExpireLabel) {
        this.isExpireLabel = isExpireLabel;
    }

    /*type set 方法 */
    public void setType(String type) {
        this.type = type;
        this.typeLabel = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, type);
    }

    /*isExpire set 方法 */
    public void setIsExpire(String isExpire) {
        this.isExpire = isExpire;
        this.isExpireLabel = (DERP.getLabelByKey(DERP.isExpireList, isExpire));
    }

    /*unit set 方法 */
    public void setUnit(String unit) {
        this.unit = unit;
        this.unitLabel = DERP.getLabelByKey(DERP.inventory_unitList, unit);
    }

    /*depotType set 方法 */
    public void setDepotType(String depotType) {
        this.depotType = depotType;
        this.depotTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_typeList, depotType);
    }

    /*isTopBooks set 方法 */
    public void setIsTopBooks(String isTopBooks) {
        this.isTopBooks = isTopBooks;
        this.isTopBooksLabel = DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_isTopBooksList, isTopBooks);

    }

    public List<Long> getDepotIdsList() {
        return depotIdsList;
    }

    public void setDepotIdsList(List<Long> depotIdsList) {
        this.depotIdsList = depotIdsList;
    }


}

