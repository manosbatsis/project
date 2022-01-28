package com.topideal.entity.vo.system;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class CommbarcodeItemModel extends PageModel implements Serializable{

    /**
    * 标准品牌Id
    */
    private Long id;
    /**
    * 标准条码id
    */
    private Long commbarcodeId;
    /**
     * 商品id
     */
    private Long goodsId;

    /**
    * 商品编码
    */
    private String goodsCode;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 品牌Id
    */
    private Long brandId;
    /**
    * 品牌名
    */
    private String brandName;
    /**
    * 二级分类id
    */
    private Long typeId;
    /**
    * 二级分类
    */
    private String typeName;
    /**
    * 创建日期
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //外仓统一码
    private String outDepotFlag;

    //标准品牌Id
    private Long brandParentId;
    //标准品牌名
    private String brandParentName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*commbarcodeId get 方法 */
    public Long getCommbarcodeId(){
    return commbarcodeId;
    }
    /*commbarcodeId set 方法 */
    public void setCommbarcodeId(Long  commbarcodeId){
    this.commbarcodeId=commbarcodeId;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
    return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
    this.goodsCode=goodsCode;
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
    /*typeId get 方法 */
    public Long getTypeId(){
    return typeId;
    }
    /*typeId set 方法 */
    public void setTypeId(Long  typeId){
    this.typeId=typeId;
    }
    /*typeName get 方法 */
    public String getTypeName(){
    return typeName;
    }
    /*typeName set 方法 */
    public void setTypeName(String  typeName){
    this.typeName=typeName;
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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getOutDepotFlag() {
        return outDepotFlag;
    }

    public void setOutDepotFlag(String outDepotFlag) {
        this.outDepotFlag = outDepotFlag;
    }

    public Long getBrandParentId() {
        return brandParentId;
    }

    public void setBrandParentId(Long brandParentId) {
        this.brandParentId = brandParentId;
    }

    public String getBrandParentName() {
        return brandParentName;
    }

    public void setBrandParentName(String brandParentName) {
        this.brandParentName = brandParentName;
    }
}
