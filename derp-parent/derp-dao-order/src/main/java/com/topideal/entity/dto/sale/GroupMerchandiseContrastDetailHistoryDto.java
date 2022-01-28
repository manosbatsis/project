package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class GroupMerchandiseContrastDetailHistoryDto extends PageModel implements Serializable{

	@ApiModelProperty(value = "主键ID")
    private Long id;
   
	@ApiModelProperty(value = "组合品对照表主键")
    private Long groupMerchandiseId;
   
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;
    
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
    
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
   
	@ApiModelProperty(value = "条形码")
    private String barcode;
   
	@ApiModelProperty(value = "商品品牌")
    private String brand;
   
	@ApiModelProperty(value = "商品数量")
    private Integer num;
  
	@ApiModelProperty(value = "销售标准价格")
    private BigDecimal price;
    
	@ApiModelProperty(value = "修改人id")
    private Long modifyId;
  
	@ApiModelProperty(value = "修改人")
    private String editor;
    
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
   
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*groupMerchandiseId get 方法 */
    public Long getGroupMerchandiseId(){
    return groupMerchandiseId;
    }
    /*groupMerchandiseId set 方法 */
    public void setGroupMerchandiseId(Long  groupMerchandiseId){
    this.groupMerchandiseId=groupMerchandiseId;
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
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*brand get 方法 */
    public String getBrand(){
    return brand;
    }
    /*brand set 方法 */
    public void setBrand(String  brand){
    this.brand=brand;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*modifyId get 方法 */
    public Long getModifyId(){
    return modifyId;
    }
    /*modifyId set 方法 */
    public void setModifyId(Long  modifyId){
    this.modifyId=modifyId;
    }
    /*editor get 方法 */
    public String getEditor(){
    return editor;
    }
    /*editor set 方法 */
    public void setEditor(String  editor){
    this.editor=editor;
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






}
