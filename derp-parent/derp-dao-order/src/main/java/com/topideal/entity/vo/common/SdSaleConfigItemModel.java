package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SdSaleConfigItemModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

    /**
     * 销售SD配置ID
     */
    @ApiModelProperty(value = "销售SD配置ID")
    private Long saleConfigId;
    /**
     * 销售SD类型id
     */
    @ApiModelProperty(value = "销售SD类型id")
    private Long sdTypeId;
    /**
     * 销售SD类型名称
     */
    @ApiModelProperty(value = "销售SD类型名称")
    private String sdTypeName;
    /**
     * sd类型1-单比例 2-多比例
     */
    @ApiModelProperty(value = "sd类型1-单比例 2-多比例")
    private String sdType;
    /**
     * 映射费项id
     */
    @ApiModelProperty(value = "映射费项id")
    private Long projectId;
    /**
     * 映射费项名称
     */
    @ApiModelProperty(value = "映射费项名称")
    private String projectName;
    /**
     * 标准条码
     */
    @ApiModelProperty(value = "标准条码")
    private String commbarcode;
    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    private String goodsName;
    /**
     * 标准品牌
     */
    @ApiModelProperty(value = "标准品牌")
    private String brandParent;
    /**
     * 比例
     */
    @ApiModelProperty(value = "比例")
    private BigDecimal proportion;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creator;
    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private Long creatorId;

    /**
     * 销售SD类型简称
     */
    @ApiModelProperty(value = "销售SD类型简称")
    private String sdSimpleName;


    public String getSdSimpleName() {
        return sdSimpleName;
    }

    public void setSdSimpleName(String sdSimpleName) {
        this.sdSimpleName = sdSimpleName;
    }

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*saleConfigId get 方法 */
    public Long getSaleConfigId(){
        return saleConfigId;
    }
    /*saleConfigId set 方法 */
    public void setSaleConfigId(Long  saleConfigId){
        this.saleConfigId=saleConfigId;
    }
    /*sdTypeId get 方法 */
    public Long getSdTypeId(){
        return sdTypeId;
    }
    /*sdTypeId set 方法 */
    public void setSdTypeId(Long  sdTypeId){
        this.sdTypeId=sdTypeId;
    }
    /*sdTypeName get 方法 */
    public String getSdTypeName(){
        return sdTypeName;
    }
    /*sdTypeName set 方法 */
    public void setSdTypeName(String  sdTypeName){
        this.sdTypeName=sdTypeName;
    }
    /*sdType get 方法 */
    public String getSdType(){
        return sdType;
    }
    /*sdType set 方法 */
    public void setSdType(String  sdType){
        this.sdType=sdType;
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
    /*commbarcode get 方法 */
    public String getCommbarcode(){
        return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
        this.commbarcode=commbarcode;
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
    /*proportion get 方法 */
    public BigDecimal getProportion(){
        return proportion;
    }
    /*proportion set 方法 */
    public void setProportion(BigDecimal  proportion){
        this.proportion=proportion;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
    /*creator get 方法 */
    public String getCreator(){
        return creator;
    }
    /*creator set 方法 */
    public void setCreator(String  creator){
        this.creator=creator;
    }
    /*creatorId get 方法 */
    public Long getCreatorId(){
        return creatorId;
    }
    /*creatorId set 方法 */
    public void setCreatorId(Long  creatorId){
        this.creatorId=creatorId;
    }






}
