package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class PaymentCostItemDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
	@ApiModelProperty("记录ID")
    private Long id;
    /**
    * 应付ID
    */
	@ApiModelProperty("应付ID")
    private Long paymentId;
	private String paymentIds;
	//应付账单号
	private String paymentCode;
    /**
    * 类型 1-补款 0-扣款
    */
	@ApiModelProperty("类型 1-补款 0-扣款")
    private String type;
    @ApiModelProperty("类型 Label")
    private String typeLabel;
    /**
    * 费项id
    */
	@ApiModelProperty("费项id")
    private Long projectId;
    /**
    * 费项名称
    */
	@ApiModelProperty("费项名称")
    private String projectName;
    /**
    * po单号
    */
	@ApiModelProperty("po单号")
    private String poNo;
    /**
    * 商品id
    */
	@ApiModelProperty("商品id")
    private Long goodsId;
    /**
    * 商品名称
    */
	@ApiModelProperty("商品名称")
    private String goodsName;
    /**
    * 商品货号
    */
	@ApiModelProperty("商品货号")
    private String goodsNo;
    /**
    * 母品牌
    */
	@ApiModelProperty("母品牌")
    private String superiorParentBrandName;
    /**
    * 母品牌id
    */
	@ApiModelProperty("母品牌id")
    private Long superiorParentBrandId;
    /**
    * 母品牌编码
    */
	@ApiModelProperty("母品牌编码")
    private String superiorParentBrandCode;
    /**
    * 数量
    */
	@ApiModelProperty("数量")
    private Integer num;
    /**
    * 费用金额（不含税）
    */
	@ApiModelProperty("费用金额（不含税）")
    private BigDecimal costAmount;
    /**
    * 税额
    */
	@ApiModelProperty("税额")
    private BigDecimal tax;
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

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*paymentId get 方法 */
    public Long getPaymentId(){
    return paymentId;
    }
    /*paymentId set 方法 */
    public void setPaymentId(Long  paymentId){
    this.paymentId=paymentId;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
        this.type=type;
        this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.paymentCost_item_typeList, type);

    }
    /*projectId get 方法 */
    public Long getProjectId(){
    return projectId;
    }
    /*projectId set 方法 */
    public void setProjectId(Long  projectId){
    this.projectId=projectId;
    }
    /*projectName get 方法 */
    public String getProjectName(){
    return projectName;
    }
    /*projectName set 方法 */
    public void setProjectName(String  projectName){
    this.projectName=projectName;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
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
    /*superiorParentBrandName get 方法 */
    public String getSuperiorParentBrandName(){
    return superiorParentBrandName;
    }
    /*superiorParentBrandName set 方法 */
    public void setSuperiorParentBrandName(String  superiorParentBrandName){
    this.superiorParentBrandName=superiorParentBrandName;
    }
    /*superiorParentBrandId get 方法 */
    public Long getSuperiorParentBrandId(){
    return superiorParentBrandId;
    }
    /*superiorParentBrandId set 方法 */
    public void setSuperiorParentBrandId(Long  superiorParentBrandId){
    this.superiorParentBrandId=superiorParentBrandId;
    }
    /*superiorParentBrandCode get 方法 */
    public String getSuperiorParentBrandCode(){
    return superiorParentBrandCode;
    }
    /*superiorParentBrandCode set 方法 */
    public void setSuperiorParentBrandCode(String  superiorParentBrandCode){
    this.superiorParentBrandCode=superiorParentBrandCode;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*costAmount get 方法 */
    public BigDecimal getCostAmount(){
    return costAmount;
    }
    /*costAmount set 方法 */
    public void setCostAmount(BigDecimal  costAmount){
    this.costAmount=costAmount;
    }
    /*tax get 方法 */
    public BigDecimal getTax(){
    return tax;
    }
    /*tax set 方法 */
    public void setTax(BigDecimal  tax){
    this.tax=tax;
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

    public String getPaymentIds() {
        return paymentIds;
    }

    public void setPaymentIds(String paymentIds) {
        this.paymentIds = paymentIds;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }
}
