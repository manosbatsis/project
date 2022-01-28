package com.topideal.entity.dto;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class VipAdjustmentInventoryDetailsDTO extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名
    */
    private String merchantName;
    /**
    * 仓库ID
    */
    private Long depotId;
    /**
    * 仓库名
    */
    private String depotName;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 货号
    */
    private String goodsNo;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 库存调整单号
    */
    private String adjustmentInventoryOrder;
    /**
    * 调整数量
    */
    private Integer adjustmentInventoryNum;
    /**
    * 调整类型
    */
    private String adjustmentInventoryType;
    /**
    * 归属月份
    */
    private String adjustmentInventoryMonths;
    /**
    * 调整时间
    */
    private Timestamp adjustmentInventoryDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 业务模式  4-唯品红冲
    */
    private String model;
    /**
    * 来源单号 
    */
    private String sourceCode;
    /**
    * po单号
    */
    private String poNo;

    //po单时间
    private Timestamp poDate;
    /**事业部id*/
    private Long buId;
    /**客户ID*/
    private Long customerId;

    private List<Long> buList;

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
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
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
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*adjustmentInventoryOrder get 方法 */
    public String getAdjustmentInventoryOrder(){
    return adjustmentInventoryOrder;
    }
    /*adjustmentInventoryOrder set 方法 */
    public void setAdjustmentInventoryOrder(String  adjustmentInventoryOrder){
    this.adjustmentInventoryOrder=adjustmentInventoryOrder;
    }
    /*adjustmentInventoryNum get 方法 */
    public Integer getAdjustmentInventoryNum(){
    return adjustmentInventoryNum;
    }
    /*adjustmentInventoryNum set 方法 */
    public void setAdjustmentInventoryNum(Integer  adjustmentInventoryNum){
    this.adjustmentInventoryNum=adjustmentInventoryNum;
    }
    /*adjustmentInventoryType get 方法 */
    public String getAdjustmentInventoryType(){
    return adjustmentInventoryType;
    }
    /*adjustmentInventoryType set 方法 */
    public void setAdjustmentInventoryType(String  adjustmentInventoryType){
    this.adjustmentInventoryType=adjustmentInventoryType;
    }
    /*adjustmentInventoryMonths get 方法 */
    public String getAdjustmentInventoryMonths(){
    return adjustmentInventoryMonths;
    }
    /*adjustmentInventoryMonths set 方法 */
    public void setAdjustmentInventoryMonths(String  adjustmentInventoryMonths){
    this.adjustmentInventoryMonths=adjustmentInventoryMonths;
    }
    /*adjustmentInventoryDate get 方法 */
    public Timestamp getAdjustmentInventoryDate(){
    return adjustmentInventoryDate;
    }
    /*adjustmentInventoryDate set 方法 */
    public void setAdjustmentInventoryDate(Timestamp  adjustmentInventoryDate){
    this.adjustmentInventoryDate=adjustmentInventoryDate;
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
    /*model get 方法 */
    public String getModel(){
    return model;
    }
    /*model set 方法 */
    public void setModel(String  model){
    this.model=model;
    }
    /*sourceCode get 方法 */
    public String getSourceCode(){
    return sourceCode;
    }
    /*sourceCode set 方法 */
    public void setSourceCode(String  sourceCode){
    this.sourceCode=sourceCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }

    public Timestamp getPoDate() {
        return poDate;
    }

    public void setPoDate(Timestamp poDate) {
        this.poDate = poDate;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }
}
