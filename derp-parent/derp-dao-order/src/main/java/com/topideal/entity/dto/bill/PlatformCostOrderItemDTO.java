package com.topideal.entity.dto.bill;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@ApiModel
public class PlatformCostOrderItemDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "平台费用单ID")
    private Long platformCostOrderId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品货号")
    private String goodsNo;

    @ApiModelProperty(value = "结算单价")
    private BigDecimal price;

    @ApiModelProperty(value = "销售/结算数量")
    private Integer num;

    @ApiModelProperty(value = "po单号")
    private String poNo;

    @ApiModelProperty(value = "爬虫货号")
    private String skuNo;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "费用项目Id")
    private Long itemProjectId;

    @ApiModelProperty(value = "费用项目名称")
    private String itemProjectName;


    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*platformCostOrderId get 方法 */
    public Long getPlatformCostOrderId(){
    return platformCostOrderId;
    }
    /*platformCostOrderId set 方法 */
    public void setPlatformCostOrderId(Long  platformCostOrderId){
    this.platformCostOrderId=platformCostOrderId;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
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
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*skuNo get 方法 */
    public String getSkuNo(){
    return skuNo;
    }
    /*skuNo set 方法 */
    public void setSkuNo(String  skuNo){
    this.skuNo=skuNo;
    }

    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
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

    public Long getItemProjectId() {
        return itemProjectId;
    }

    public void setItemProjectId(Long itemProjectId) {
        this.itemProjectId = itemProjectId;
    }

    public String getItemProjectName() {
        return itemProjectName;
    }

    public void setItemProjectName(String itemProjectName) {
        this.itemProjectName = itemProjectName;
    }

}
