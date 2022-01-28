package com.topideal.entity.dto;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class MonthlyAccountAutomaticVerificationDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value = "id")
    private Long id;
    /**
    * 商家id
    */
	@ApiModelProperty(value = "商家id")
    private Long merchantId;
    /**
    * 商家名称
    */
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
    /**
    * 仓库id
    */
	@ApiModelProperty(value = "仓库id")
    private Long depotId;
    /**
    * 仓库名称
    */
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
    /**
    * 归属月份 YYYY-MM
    */
	@ApiModelProperty(value = "归属月份 YYYY-MM")
    private String month;
    /**
    * 校验结果: 1-已对平 0-未对平
    */
	@ApiModelProperty(value = "校验结果: 1-已对平 0-未对平")
    private String status;
	@ApiModelProperty(value = "校验结果: 1-已对平 0-未对平Label")
    private String statusLabel;
    /**
    * 条形码
    */
    @ApiModelProperty(value = "条形码")
    private String barcode;
    /**
    * 商品名称
    */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
    * 月库结转表的库存总量
    */
    @ApiModelProperty(value = "月库结转表的库存总量")
    private Integer monthlyAccountSurplusNum;
    /**
    * 月库结转表的库存正常量
    */
    @ApiModelProperty(value = "月库结转表的库存正常量")
    private Integer monthlyAccountNormalNum;
    /**
    * 月库结转表的库存正常量
    */
    @ApiModelProperty(value = "月库结转表的库存正常量")
    private Integer monthlyAccountWornNum;
    /**
    * 事业部库存总量
    */
    @ApiModelProperty(value = "事业部库存")
    private Integer buInventorySurplusNum;
    /**
    * 事业部库存正常品
    */
    @ApiModelProperty(value = "事业部库存正常品")
    private Integer buInventoryNormalNum;
    /**
    * 事业部库存坏品
    */
    @ApiModelProperty(value = "事业部库存坏品")
    private Integer buInventoryWornNum;
    /**
    * 事业部业务进销存期末库存总量
    */
    @ApiModelProperty(value = "事业部业务进销存期末库存总量")
    private Integer buInventorySummaryEndNum;
    /**
    * 事业部业务进销存期末库存好品
    */
    @ApiModelProperty(value = "事业部业务进销存期末库存好品")
    private Integer buInventorySummaryNormalNum;
    /**
    * 事业部业务进销存期末库存坏品
    */
    @ApiModelProperty(value = "事业部业务进销存期末库存坏品")
    private Integer buInventorySummaryWornNum;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    /**
     * 库存现存量
     */
    @ApiModelProperty(value = "库存现存量")
    private Integer inventoryRealTimeQty;
    /**
     * 库存现存好品量
     */
    @ApiModelProperty(value = "库存现存好品量")
    private Integer inventoryRealTimeNormalQty;
    /**
     * 库存现存坏品量
     */
    @ApiModelProperty(value = "库存现存坏品量")
    private Integer inventoryRealTimeWornQty;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    this.statusLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.businessFinanceAutomaticVerification_statusList, status) ;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*monthlyAccountSurplusNum get 方法 */
    public Integer getMonthlyAccountSurplusNum(){
    return monthlyAccountSurplusNum;
    }
    /*monthlyAccountSurplusNum set 方法 */
    public void setMonthlyAccountSurplusNum(Integer  monthlyAccountSurplusNum){
    this.monthlyAccountSurplusNum=monthlyAccountSurplusNum;
    }
    /*monthlyAccountNormalNum get 方法 */
    public Integer getMonthlyAccountNormalNum(){
    return monthlyAccountNormalNum;
    }
    /*monthlyAccountNormalNum set 方法 */
    public void setMonthlyAccountNormalNum(Integer  monthlyAccountNormalNum){
    this.monthlyAccountNormalNum=monthlyAccountNormalNum;
    }
    /*monthlyAccountWornNum get 方法 */
    public Integer getMonthlyAccountWornNum(){
    return monthlyAccountWornNum;
    }
    /*monthlyAccountWornNum set 方法 */
    public void setMonthlyAccountWornNum(Integer  monthlyAccountWornNum){
    this.monthlyAccountWornNum=monthlyAccountWornNum;
    }
    /*buInventorySurplusNum get 方法 */
    public Integer getBuInventorySurplusNum(){
    return buInventorySurplusNum;
    }
    /*buInventorySurplusNum set 方法 */
    public void setBuInventorySurplusNum(Integer  buInventorySurplusNum){
    this.buInventorySurplusNum=buInventorySurplusNum;
    }
    /*buInventoryNormalNum get 方法 */
    public Integer getBuInventoryNormalNum(){
    return buInventoryNormalNum;
    }
    /*buInventoryNormalNum set 方法 */
    public void setBuInventoryNormalNum(Integer  buInventoryNormalNum){
    this.buInventoryNormalNum=buInventoryNormalNum;
    }
    /*buInventoryWornNum get 方法 */
    public Integer getBuInventoryWornNum(){
    return buInventoryWornNum;
    }
    /*buInventoryWornNum set 方法 */
    public void setBuInventoryWornNum(Integer  buInventoryWornNum){
    this.buInventoryWornNum=buInventoryWornNum;
    }
    /*buInventorySummaryEndNum get 方法 */
    public Integer getBuInventorySummaryEndNum(){
    return buInventorySummaryEndNum;
    }
    /*buInventorySummaryEndNum set 方法 */
    public void setBuInventorySummaryEndNum(Integer  buInventorySummaryEndNum){
    this.buInventorySummaryEndNum=buInventorySummaryEndNum;
    }
    /*buInventorySummaryNormalNum get 方法 */
    public Integer getBuInventorySummaryNormalNum(){
    return buInventorySummaryNormalNum;
    }
    /*buInventorySummaryNormalNum set 方法 */
    public void setBuInventorySummaryNormalNum(Integer  buInventorySummaryNormalNum){
    this.buInventorySummaryNormalNum=buInventorySummaryNormalNum;
    }
    /*buInventorySummaryWornNum get 方法 */
    public Integer getBuInventorySummaryWornNum(){
    return buInventorySummaryWornNum;
    }
    /*buInventorySummaryWornNum set 方法 */
    public void setBuInventorySummaryWornNum(Integer  buInventorySummaryWornNum){
    this.buInventorySummaryWornNum=buInventorySummaryWornNum;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

    public Integer getInventoryRealTimeQty() {
        return inventoryRealTimeQty;
    }

    public void setInventoryRealTimeQty(Integer inventoryRealTimeQty) {
        this.inventoryRealTimeQty = inventoryRealTimeQty;
    }

    public Integer getInventoryRealTimeNormalQty() {
        return inventoryRealTimeNormalQty;
    }

    public void setInventoryRealTimeNormalQty(Integer inventoryRealTimeNormalQty) {
        this.inventoryRealTimeNormalQty = inventoryRealTimeNormalQty;
    }

    public Integer getInventoryRealTimeWornQty() {
        return inventoryRealTimeWornQty;
    }

    public void setInventoryRealTimeWornQty(Integer inventoryRealTimeWornQty) {
        this.inventoryRealTimeWornQty = inventoryRealTimeWornQty;
    }
}
