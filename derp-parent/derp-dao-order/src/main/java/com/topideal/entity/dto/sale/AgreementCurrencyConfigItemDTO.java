package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AgreementCurrencyConfigItemDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;
    
	@ApiModelProperty(value = "协议单价配置表头ID")
    private Long agreementId;
    
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;
    
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
   
	@ApiModelProperty(value = "协议单价")
    private BigDecimal price;
    
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
   
	@ApiModelProperty(value = "商品编码")
    private String goodsCode;
   
	@ApiModelProperty(value = "品牌")
    private String brandName;
    
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
   
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*agreementId get 方法 */
    public Long getAgreementId(){
    return agreementId;
    }
    /*agreementId set 方法 */
    public void setAgreementId(Long  agreementId){
    this.agreementId=agreementId;
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
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
    return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
    this.goodsCode=goodsCode;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }






}
