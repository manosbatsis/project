package com.topideal.entity.vo.main;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class InventoryLocationMappingModel extends PageModel implements Serializable{


	@ApiModelProperty(value = "自增ID")
    private Long id;
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
	@ApiModelProperty(value = "商家名")
    private String merchantName;
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;
	@ApiModelProperty(value = "库位货号")
    private String goodsNo;
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
	@ApiModelProperty(value = "原货号ID")
    private Long originalGoodsId;
	@ApiModelProperty(value = "原货号")
    private String originalGoodsNo;
	@ApiModelProperty(value = "库位类型：1、常规品 2、赠送品 3、sample")
    private String type;
	@ApiModelProperty(value = "创建人ID")
    private Long creator;
	@ApiModelProperty(value = "创建人")
    private String createName;
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
	@ApiModelProperty(value = "修改人ID")
    private Long modifier;
	@ApiModelProperty(value = "修改人")
    private String modifyName;
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
	@ApiModelProperty(value = "指定出入库商品 1. 已指定 0, 未指定")
    private String isorRappoint;

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
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*originalGoodsNo get 方法 */
    public String getOriginalGoodsNo(){
    return originalGoodsNo;
    }
    /*originalGoodsNo set 方法 */
    public void setOriginalGoodsNo(String  originalGoodsNo){
    this.originalGoodsNo=originalGoodsNo;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*creator get 方法 */
    public Long getCreator(){
    return creator;
    }
    /*creator set 方法 */
    public void setCreator(Long  creator){
    this.creator=creator;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifyName get 方法 */
    public String getModifyName(){
    return modifyName;
    }
    /*modifyName set 方法 */
    public void setModifyName(String  modifyName){
    this.modifyName=modifyName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public Long getOriginalGoodsId() {
        return originalGoodsId;
    }

    public void setOriginalGoodsId(Long originalGoodsId) {
        this.originalGoodsId = originalGoodsId;
    }

    public String getIsorRappoint() {
        return isorRappoint;
    }

    public void setIsorRappoint(String isorRappoint) {
        this.isorRappoint = isorRappoint;
    }
}
