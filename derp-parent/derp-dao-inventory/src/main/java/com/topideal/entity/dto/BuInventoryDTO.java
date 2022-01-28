package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class BuInventoryDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value = "id")
    private Long id;
    /**
    * 商家ID
    */
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
    /**
    * 商家名称
    */
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
    /**
    * 商家卓志编码
    */
	@ApiModelProperty(value = "商家卓志编码")
    private String topidealCode;
    /**
    * 仓库ID
    */
	@ApiModelProperty(value = "仓库ID")
    private Long depotId;
    /**
    * 仓库名称
    */
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
    /**
    * 仓库编码
    */
	@ApiModelProperty(value = "仓库编码")
    private String depotCode;
    /**
    * 仓库类型(a-保税仓，b-备查仓，c-海外仓，d-中转仓,e-暂存区，f-销毁区）
    */
	@ApiModelProperty(value = "仓库类型(a-保税仓，b-备查仓，c-海外仓，d-中转仓,e-暂存区，f-销毁区）")
    private String depotType;
    /**
    * 事业部ID
    */
	@ApiModelProperty(value = " 事业部ID")
    private Long buId;
    /**
    * 事业部名称
    */
	@ApiModelProperty(value = "事业部名称")
    private String buName;

    /**
    * 商品ID
    */
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;
    /**
    * 商品名称
    */
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
    * 商品货号
    */
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
    /**
    * 标准条码
    */
	@ApiModelProperty(value = "标准条码")
    private String commbarcode;
    /**
    * 条形码
    */
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
    /**
    * 库存余量
    */
	@ApiModelProperty(value = "库存余量")
    private Integer surplusNum;
    /**
    * 坏货数量
    */
	@ApiModelProperty(value = "坏货数量")
    private Integer wornNum;
    /**
    * 好品数量
    */
	@ApiModelProperty(value = "好品数量")
    private Integer okNum;
    /**
    * 理货单位 0 托盘 1箱  2 件
    */
	@ApiModelProperty(value = " 理货单位 0 托盘 1箱  2 件")
    private String unit;
    /**
    * 月份
    */
	@ApiModelProperty(value = "月份")
    private String month;
    /**
    * 创建人
    */
	@ApiModelProperty(value = "创建人")
    private Long creater;
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
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    /**
     * 标准品牌Id
     */
    @ApiModelProperty(value = "标准品牌Id")
    private Long parentBrandId;

    /**
     * 标准品牌名"
     */
    @ApiModelProperty(value = "标准品牌名")
    private String parentBrandName;

    /**
     * 母品牌名称
     */
    @ApiModelProperty(value = "母品牌名称")
    private String superiorParentBrand;
    
    /**
     * 母品牌ID
     */
    @ApiModelProperty(value = "母品牌ID")
    private Long superiorParentBrandId;
    /**
     * 部门编码
     */
    @ApiModelProperty(value = "部门编码")
    private String departmentCode;
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
    
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public String getSuperiorParentBrand() {
        return superiorParentBrand;
    }

    public void setSuperiorParentBrand(String superiorParentBrand) {
        this.superiorParentBrand = superiorParentBrand;
    }

    //理货单位 0 托盘 1箱  2 件'
	@ApiModelProperty(value = "理货单位 0 托盘 1箱  2 件'")
    private String unitLabel;
    //'仓库类型',
	@ApiModelProperty(value = "仓库类型Label;")
    private String depotTypeLabel;



  
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
    /*topidealCode get 方法 */
    public String getTopidealCode(){
    return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
    this.topidealCode=topidealCode;
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
    /*depotCode get 方法 */
    public String getDepotCode(){
    return depotCode;
    }
    /*depotCode set 方法 */
    public void setDepotCode(String  depotCode){
    this.depotCode=depotCode;
    }
    /*depotType get 方法 */
    public String getDepotType(){
    return depotType;
    }
    /*depotType set 方法 */
    public void setDepotType(String  depotType){
        this.depotType=depotType;
        this.depotTypeLabel=DERP_SYS.getLabelByKey(DERP_SYS.depotInfo_typeList, depotType);
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

    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
    return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
    this.brandId=brandId;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*surplusNum get 方法 */
    public Integer getSurplusNum(){
    return surplusNum;
    }
    /*surplusNum set 方法 */
    public void setSurplusNum(Integer  surplusNum){
    this.surplusNum=surplusNum;
    }
    /*wornNum get 方法 */
    public Integer getWornNum(){
    return wornNum;
    }
    /*wornNum set 方法 */
    public void setWornNum(Integer  wornNum){
    this.wornNum=wornNum;
    }

 
    public Integer getOkNum() {
		return okNum;
	}
	public void setOkNum(Integer okNum) {
		this.okNum = okNum;
	}
	/*unit get 方法 */
    public String getUnit(){
    return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
    	this.unit=unit;
        this.unitLabel=DERP.getLabelByKey(DERP.inventory_unitList, unit);
    }
    
    public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	/*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
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

	public Long getSuperiorParentBrandId() {
		return superiorParentBrandId;
	}

	public void setSuperiorParentBrandId(Long superiorParentBrandId) {
		this.superiorParentBrandId = superiorParentBrandId;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}




}
