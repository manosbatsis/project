package com.topideal.entity.dto.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class PlatformMerchandiseInfoDTO extends PageModel implements Serializable{

    /**
    * 主键id
    */
	@ApiModelProperty(value="主键id", required=false)
    private Long id;
    /**
    * 商品编码
    */
	@ApiModelProperty(value="商品编码", required=false)
    private String wareId;
    /**
    * 商品名称
    */
	@ApiModelProperty(value="商品名称", required=false)
    private String name;
    /**
    * 商品Id
    */
	@ApiModelProperty(value="商品Id", required=false)
    private String pid;
    /**
    * 品牌
    */
	@ApiModelProperty(value="品牌", required=false)
    private String brand;
    /**
    * 单位
    */
	@ApiModelProperty(value="单位", required=false)
    private String unit;
    /**
    * 条形码
    */
	@ApiModelProperty(value="条形码", required=false)
    private String upc;
    /**
    * 平台名称
    */
	@ApiModelProperty(value="平台名称", required=false)
    private String platform;
    /**
    * 账号
    */
	@ApiModelProperty(value="账号", required=false)
    private String userCode;
    /**
    * 商家id
    */
	@ApiModelProperty(value="商家id", required=false)
    private Long merchantId;
    /**
    * 商家名称
    */
	@ApiModelProperty(value="商家名称", required=false)
    private String merchantName;
    /**
    * 创建时间
    */
	@ApiModelProperty(value="创建时间", required=false)
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value="修改时间", required=false)
    private Timestamp modifyDate;
    /**
     * 箱规
     */
	@ApiModelProperty(value="箱规", required=false)
    private Integer cartonSize;
    /**
     * 爬虫平台类型：1-云集  2-唯品 3-京东 4-天猫
     */
	@ApiModelProperty(value="爬虫平台类型：1-云集  2-唯品 3-京东 4-天猫", required=false)
    private String crawlerType;
	@ApiModelProperty(value="爬虫平台类型中文", required=false)
    private String crawlerTypeLabel;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*wareId get 方法 */
    public String getWareId(){
    return wareId;
    }
    /*wareId set 方法 */
    public void setWareId(String  wareId){
    this.wareId=wareId;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*pid get 方法 */
    public String getPid(){
    return pid;
    }
    /*pid set 方法 */
    public void setPid(String  pid){
    this.pid=pid;
    }
    /*brand get 方法 */
    public String getBrand(){
    return brand;
    }
    /*brand set 方法 */
    public void setBrand(String  brand){
    this.brand=brand;
    }
    /*unit get 方法 */
    public String getUnit(){
    return unit;
    }
    /*unit set 方法 */
    public void setUnit(String  unit){
    this.unit=unit;
    }
    /*upc get 方法 */
    public String getUpc(){
    return upc;
    }
    /*upc set 方法 */
    public void setUpc(String  upc){
    this.upc=upc;
    }
    /*platform get 方法 */
    public String getPlatform(){
    return platform;
    }
    /*platform set 方法 */
    public void setPlatform(String  platform){
    this.platform=platform;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
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

    public Integer getCartonSize() {
        return cartonSize;
    }

    public void setCartonSize(Integer cartonSize) {
        this.cartonSize = cartonSize;
    }

    public String getCrawlerType() {
        return crawlerType;
    }

    public void setCrawlerType(String crawlerType) {
        this.crawlerType = crawlerType;
        this.crawlerTypeLabel = DERP.getLabelByKey(DERP.crawler_typeList,crawlerType);
    }

    public String getCrawlerTypeLabel() {
        return crawlerTypeLabel;
    }

    public void setCrawlerTypeLabel(String crawlerTypeLabel) {
        this.crawlerTypeLabel = crawlerTypeLabel;
    }
}
