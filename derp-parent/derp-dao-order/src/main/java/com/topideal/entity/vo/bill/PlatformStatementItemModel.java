package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class PlatformStatementItemModel extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    private Long id;
    /**
    * 平台结算单ID
    */
    private Long platformStatementOrderId;
    /**
    * 1-销售、2-客退、3-国检、4-盘亏、5-报废、6-盘盈、7-客退补贴、8-活动折扣、9-补偿折扣
    */
    private String type;
    /**
    * po号
    */
    private String poNo;
    /**
    * 条形码,对应账单SKU
    */
    private String barcode;
    /**
    * 商品名
    */
    private String goodsName;
    /**
    * 标准品牌
    */
    private String brandParent;
    /**
    * 结算数量
    */
    private Integer settlementNum;
    /**
    * 结算金额（本位币）
    */
    private BigDecimal settlementAmount;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 费项配置表id
     */
    private Long settlementConfigId;
    /**
     * 费项配置表名称
     */
    private String settlementConfigName;
    /**
     * 天猫爬虫表费项
     */
    private String tmRemarks;
    /**
     * 结算金额（RMB）
     */
    private BigDecimal settlementAmountRmb;
    /**
     * 汇率
     */
    private BigDecimal rate;
    /**
     * 平台费项配置表id
     */
    private Long platformSettlementConfigId;
     /**
     * 平台费项配置表名称
     */
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
