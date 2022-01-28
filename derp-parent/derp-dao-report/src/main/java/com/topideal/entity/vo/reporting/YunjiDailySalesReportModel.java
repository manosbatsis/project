package com.topideal.entity.vo.reporting;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class YunjiDailySalesReportModel extends PageModel implements Serializable{

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
    * 产品一级分类名称
    */
    @ApiModelProperty(value = "产品一级分类名称")
    private String productTypeName0;
    /**
    * 产品一级分类id
    */
    @ApiModelProperty(value = "产品一级分类id")
    private Long productTypeId0;
    /**
    * 产品二级分类名称
    */
    @ApiModelProperty(value = "产品二级分类名称")
    private String productTypeName;
    /**
    * 产品二级分类id
    */
    @ApiModelProperty(value = "产品二级分类id")
    private Long productTypeId;
    /**
    * 商品名称
    */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
    * 商品ID
    */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    /**
    * 商品货号
    */
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    /**
    * 条形码
    */
    @ApiModelProperty(value = "条形码")
    private String barcode;
    /**
    * 标准条码
    */
    @ApiModelProperty(value = "标准条码")
    private String commbarcode;
    /**
    * 当日销售量
    */
    @ApiModelProperty(value = "当日销售量")
    private Integer daySaleNum;
    /**
    * 当月销售量
    */
    @ApiModelProperty(value = "当月销售量")
    private Integer monthSaleNum;
    /**
    * 当年销量
    */
    @ApiModelProperty(value = "当年销量")
    private Integer yearSaleNum;
    /**
    * 保税仓当日库存
    */
    @ApiModelProperty(value = "保税仓当日库存")
    private Integer dayInventoryNum;
    /**
     * 退货仓当日库存
     */
    @ApiModelProperty(value = "退货仓当日库存")
    private Integer dayReturnBinNum;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "快照时间")
    private Timestamp snapshotDate;
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
    /*productTypeName0 get 方法 */
    public String getProductTypeName0(){
    return productTypeName0;
    }
    /*productTypeName0 set 方法 */
    public void setProductTypeName0(String  productTypeName0){
    this.productTypeName0=productTypeName0;
    }
    /*productTypeId0 get 方法 */
    public Long getProductTypeId0(){
    return productTypeId0;
    }
    /*productTypeId0 set 方法 */
    public void setProductTypeId0(Long  productTypeId0){
    this.productTypeId0=productTypeId0;
    }
    /*productTypeName get 方法 */
    public String getProductTypeName(){
    return productTypeName;
    }
    /*productTypeName set 方法 */
    public void setProductTypeName(String  productTypeName){
    this.productTypeName=productTypeName;
    }
    /*productTypeId get 方法 */
    public Long getProductTypeId(){
    return productTypeId;
    }
    /*productTypeId set 方法 */
    public void setProductTypeId(Long  productTypeId){
    this.productTypeId=productTypeId;
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
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*daySaleNum get 方法 */
    public Integer getDaySaleNum(){
    return daySaleNum;
    }
    /*daySaleNum set 方法 */
    public void setDaySaleNum(Integer  daySaleNum){
    this.daySaleNum=daySaleNum;
    }
    /*monthSaleNum get 方法 */
    public Integer getMonthSaleNum(){
    return monthSaleNum;
    }
    /*monthSaleNum set 方法 */
    public void setMonthSaleNum(Integer  monthSaleNum){
    this.monthSaleNum=monthSaleNum;
    }
    /*yearSaleNum get 方法 */
    public Integer getYearSaleNum(){
    return yearSaleNum;
    }
    /*yearSaleNum set 方法 */
    public void setYearSaleNum(Integer  yearSaleNum){
    this.yearSaleNum=yearSaleNum;
    }
    /*dayInventoryNum get 方法 */
    public Integer getDayInventoryNum(){
    return dayInventoryNum;
    }
    /*dayInventoryNum set 方法 */
    public void setDayInventoryNum(Integer  dayInventoryNum){
    this.dayInventoryNum=dayInventoryNum;
    }
    /*snapshotDate get 方法 */
    public Timestamp getSnapshotDate(){
    return snapshotDate;
    }
    /*snapshotDate set 方法 */
    public void setSnapshotDate(Timestamp  snapshotDate){
    this.snapshotDate=snapshotDate;
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

    public Integer getDayReturnBinNum() {
        return dayReturnBinNum;
    }

    public void setDayReturnBinNum(Integer dayReturnBinNum) {
        this.dayReturnBinNum = dayReturnBinNum;
    }
}
