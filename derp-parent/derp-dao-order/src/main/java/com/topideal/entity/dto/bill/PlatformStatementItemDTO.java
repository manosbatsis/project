package com.topideal.entity.dto.bill;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PlatformStatementItemDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "平台结算单ID")
    private Long platformStatementOrderId;

    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "类型中文")
    private String typeLabel;

    @ApiModelProperty(value = "po号")
    private String poNo;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "商品名")
    private String goodsName;

    @ApiModelProperty(value = "标准品牌")
    private String brandParent;

    @ApiModelProperty(value = "结算数量")
    private Integer settlementNum;

    @ApiModelProperty(value = "结算金额（本位币）")
    private BigDecimal settlementAmount;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "费项配置表id")
    private Long settlementConfigId;

    @ApiModelProperty(value = "费项配置表名称")
    private String settlementConfigName;

    @ApiModelProperty(value = "天猫爬虫表费项")
    private String tmRemarks;

    @ApiModelProperty(value = "结算金额（RMB）")
    private BigDecimal settlementAmountRmb;

    @ApiModelProperty(value = "汇率")
    private BigDecimal rate;
    /**
     * 平台费项配置表id
     */
    @ApiModelProperty(value = "平台费项配置表id")
    private Long platformSettlementConfigId;
     /**
     * 平台费项配置表名称
     */
    @ApiModelProperty(value = "平台费项配置表名称")
    private String platformSettlementConfigName;
    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*platformStatementOrderId get 方法 */
    public Long getPlatformStatementOrderId(){
    return platformStatementOrderId;
    }
    /*platformStatementOrderId set 方法 */
    public void setPlatformStatementOrderId(Long  platformStatementOrderId){
    this.platformStatementOrderId=platformStatementOrderId;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformStatement_itemTypeList, type) ;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
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
    /*brandParent get 方法 */
    public String getBrandParent(){
    return brandParent;
    }
    /*brandParent set 方法 */
    public void setBrandParent(String  brandParent){
    this.brandParent=brandParent;
    }
    /*settlementNum get 方法 */
    public Integer getSettlementNum(){
    return settlementNum;
    }
    /*settlementNum set 方法 */
    public void setSettlementNum(Integer  settlementNum){
    this.settlementNum=settlementNum;
    }
    /*settlementAmount get 方法 */
    public BigDecimal getSettlementAmount(){
    return settlementAmount;
    }
    /*settlementAmount set 方法 */
    public void setSettlementAmount(BigDecimal  settlementAmount){
    this.settlementAmount=settlementAmount;
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
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	public Long getSettlementConfigId() {
		return settlementConfigId;
	}
	public void setSettlementConfigId(Long settlementConfigId) {
		this.settlementConfigId = settlementConfigId;
	}
	public String getSettlementConfigName() {
		return settlementConfigName;
	}
	public void setSettlementConfigName(String settlementConfigName) {
		this.settlementConfigName = settlementConfigName;
	}
	public String getTmRemarks() {
		return tmRemarks;
	}
	public void setTmRemarks(String tmRemarks) {
		this.tmRemarks = tmRemarks;
	}
	public BigDecimal getSettlementAmountRmb() {
		return settlementAmountRmb;
	}
	public void setSettlementAmountRmb(BigDecimal settlementAmountRmb) {
		this.settlementAmountRmb = settlementAmountRmb;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public Long getPlatformSettlementConfigId() {
		return platformSettlementConfigId;
	}
	public void setPlatformSettlementConfigId(Long platformSettlementConfigId) {
		this.platformSettlementConfigId = platformSettlementConfigId;
	}
	public String getPlatformSettlementConfigName() {
		return platformSettlementConfigName;
	}
	public void setPlatformSettlementConfigName(String platformSettlementConfigName) {
		this.platformSettlementConfigName = platformSettlementConfigName;
	}






}
