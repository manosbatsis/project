package com.topideal.entity.dto.platformdata;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class PlatfromGoodsCategoryDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    @ApiModelProperty("记录ID")
    private Long id;
    /**
    * 客户ID
    */
    @ApiModelProperty("客户ID")
    private Long customerId;
    /**
    * 客户名称
    */
    @ApiModelProperty("客户名称")
    private String customerName;
    /**
    * 事业部ID
    */
    @ApiModelProperty("事业部ID")
    private Long buId;
    /**
    * 事业部名称
    */
    @ApiModelProperty("事业部名称")
    private String buName;
    /**
    * 商家ID
    */
    @ApiModelProperty("商家ID")
    private Long merchantId;
    /**
    * 商家名称
    */
    @ApiModelProperty("商家名称")
    private String merchantName;
    /**
    * 平台品类Id
    */
    @ApiModelProperty("平台品类Id")
    private Long categoryId;
    /**
    * 平台品类名称
    */
    @ApiModelProperty("平台品类名称")
    private String categoryName;
    /**
    * 标准条码
    */
    @ApiModelProperty("标准条码")
    private String commbarcode;
    /**
    * 条码
    */
    @ApiModelProperty("条码")
    private String barcode;
    /**
    * 商品名称
    */
    @ApiModelProperty("商品名称")
    private String goodsName;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private Long creater;
    /**
    * 创建人用户名
    */
    @ApiModelProperty("创建人用户名")
    private String createName;
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

    private List<Long> buIds ;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
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
    /*categoryId get 方法 */
    public Long getCategoryId(){
    return categoryId;
    }
    /*categoryId set 方法 */
    public void setCategoryId(Long  categoryId){
    this.categoryId=categoryId;
    }
    /*categoryName get 方法 */
    public String getCategoryName(){
    return categoryName;
    }
    /*categoryName set 方法 */
    public void setCategoryName(String  categoryName){
    this.categoryName=categoryName;
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
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public List<Long> getBuIds() {
        return buIds;
    }

    public void setBuIds(List<Long> buIds) {
        this.buIds = buIds;
    }
}
