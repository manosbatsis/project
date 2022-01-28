package com.topideal.entity.dto;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.math.BigDecimal;


@ApiModel
public class SaleTargetAchievementDTO extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    @ApiModelProperty("自增ID")
    private Long id;
    /**
    * 事业部ID
    */
    @ApiModelProperty("事业部ID")
    private Long buId;
    private List<Long> buIds;
    /**
    * 事业部名称
    */
    @ApiModelProperty("事业部名称")
    private String buName;
    /**
    * 月份
    */
    @ApiModelProperty("月份")
    private String month;

    private String season ;

    private String year ;

    private String startMonth ;

    private String endMonth ;
    /**
    * 商品名称
    */
    @ApiModelProperty("商品名称")
    private String goodsName;
    /**
    * 标准条码
    */
    @ApiModelProperty("标准条码")
    private String commbarcode;
    /**
    * 标准品牌
    */
    @ApiModelProperty("标准品牌")
    private String brandParent;
    /**
    * To B销量
    */
    @ApiModelProperty("To B销量")
    private Integer toBNum;

    private Integer toBTarget ;
    
    /**
    * To B销量完成率
    */
    @ApiModelProperty("To B销量完成率")
    private Double toBRate;
    /**
    * To C销量
    */
    @ApiModelProperty("To C销量")
    private Integer toCNum;
    
    private Integer toCTarget ;
    
    /**
    * To C销量完成率
    */
    @ApiModelProperty("To C销量完成率")
    private Double toCRate;
    /**
    * 类型 1-按销售类型计划 2-按平台计划
    */
    @ApiModelProperty("类型 1-按销售类型计划 2-按平台计划")
    private String type;
    private String typeLabel ;
    /**
    * 电商平台编码
    */
    @ApiModelProperty("电商平台编码")
    private String storePlatformName;
    private String storePlatformNameLabel ;
    /**
    * 总销量
    */
    @ApiModelProperty("总销量")
    private Integer num;


    private Integer target ;
    
    /**
    * 总完成率
    */
    @ApiModelProperty("总完成率")
    private Double rate;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;

    private String barcodes ;

    /**
     * 店铺编码
     */
    @ApiModelProperty("店铺编码")
    private String shopCode;
    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String shopName;
    
    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*brandParent get 方法 */
    public String getBrandParent(){
    return brandParent;
    }
    /*brandParent set 方法 */
    public void setBrandParent(String  brandParent){
    this.brandParent=brandParent;
    }
    /*toBNum get 方法 */
    public Integer getToBNum(){
    return toBNum;
    }
    /*toBNum set 方法 */
    public void setToBNum(Integer  toBNum){
    this.toBNum=toBNum;
    }
    /*toBRate get 方法 */
    public Double getToBRate(){
    return toBRate;
    }
    /*toBRate set 方法 */
    public void setToBRate(Double  toBRate){
    this.toBRate=toBRate;
    }
    /*toCNum get 方法 */
    public Integer getToCNum(){
    return toCNum;
    }
    /*toCNum set 方法 */
    public void setToCNum(Integer  toCNum){
    this.toCNum=toCNum;
    }
    /*toCRate get 方法 */
    public Double getToCRate(){
    return toCRate;
    }
    /*toCRate set 方法 */
    public void setToCRate(Double  toCRate){
    this.toCRate=toCRate;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    this.typeLabel =DERP_REPORT.getLabelByKey(DERP_REPORT.sale_target_typeList, type) ;
    }
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
    return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
    this.storePlatformName=storePlatformName;
    this.storePlatformNameLabel = DERP_REPORT.getLabelByKey(DERP.storePlatformList, storePlatformName) ;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*rate get 方法 */
    public Double getRate(){
    return rate;
    }
    /*rate set 方法 */
    public void setRate(Double  rate){
    this.rate=rate;
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
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}
	public String getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	public String getStorePlatformNameLabel() {
		return storePlatformNameLabel;
	}
	public void setStorePlatformNameLabel(String storePlatformNameLabel) {
		this.storePlatformNameLabel = storePlatformNameLabel;
	}
	public Integer getToBTarget() {
		return toBTarget;
	}
	public void setToBTarget(Integer toBTarget) {
		this.toBTarget = toBTarget;
	}
	public Integer getToCTarget() {
		return toCTarget;
	}
	public void setToCTarget(Integer toCTarget) {
		this.toCTarget = toCTarget;
	}
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
	}
	public String getBarcodes() {
		return barcodes;
	}
	public void setBarcodes(String barcodes) {
		this.barcodes = barcodes;
	}
	public List<Long> getBuIds() {
		return buIds;
	}
	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
	}

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
