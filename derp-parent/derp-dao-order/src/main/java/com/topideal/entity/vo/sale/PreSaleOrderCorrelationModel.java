package com.topideal.entity.vo.sale;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

public class PreSaleOrderCorrelationModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 预售单ID
    */
    private Long preSaleOrderId;
    /**
    * 预售单号
    */
    private String preSaleOrderCode;
    /**
    * 销售单号
    */
    private String saleOrderCode;
    /**
    * 销售出库单号
    */
    private String saleOutDepotCode;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
     * 商品编码
     */
    private String goodsCode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 预售数量
    */
    private Integer preNum;
    /**
    * 销售数量
    */
    private Integer saleNum;
    /**
    * 出库数量
    */
    private Integer outNum;
    /**
    * 审核时间
    */
    private Timestamp auditDate;
    /**
    * 审核人
    */
    private Long auditor;
    /**
    * 审核人用户名
    */
    private String auditName;
    /**
    * 出库时间
    */
    private Timestamp outDepotDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
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
    /*preSaleOrderId get 方法 */
    public Long getPreSaleOrderId(){
    return preSaleOrderId;
    }
    /*preSaleOrderId set 方法 */
    public void setPreSaleOrderId(Long  preSaleOrderId){
    this.preSaleOrderId=preSaleOrderId;
    }
    /*preSaleOrderCode get 方法 */
    public String getPreSaleOrderCode(){
    return preSaleOrderCode;
    }
    /*preSaleOrderCode set 方法 */
    public void setPreSaleOrderCode(String  preSaleOrderCode){
    this.preSaleOrderCode=preSaleOrderCode;
    }
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
    return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
    this.saleOrderCode=saleOrderCode;
    }
    /*saleOutDepotCode get 方法 */
    public String getSaleOutDepotCode(){
    return saleOutDepotCode;
    }
    /*saleOutDepotCode set 方法 */
    public void setSaleOutDepotCode(String  saleOutDepotCode){
    this.saleOutDepotCode=saleOutDepotCode;
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
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*preNum get 方法 */
    public Integer getPreNum(){
    return preNum;
    }
    /*preNum set 方法 */
    public void setPreNum(Integer  preNum){
    this.preNum=preNum;
    }
    /*saleNum get 方法 */
    public Integer getSaleNum(){
    return saleNum;
    }
    /*saleNum set 方法 */
    public void setSaleNum(Integer  saleNum){
    this.saleNum=saleNum;
    }
    /*outNum get 方法 */
    public Integer getOutNum(){
    return outNum;
    }
    /*outNum set 方法 */
    public void setOutNum(Integer  outNum){
    this.outNum=outNum;
    }
    /*auditDate get 方法 */
    public Timestamp getAuditDate(){
    return auditDate;
    }
    /*auditDate set 方法 */
    public void setAuditDate(Timestamp  auditDate){
    this.auditDate=auditDate;
    }
    /*auditor get 方法 */
    public Long getAuditor(){
    return auditor;
    }
    /*auditor set 方法 */
    public void setAuditor(Long  auditor){
    this.auditor=auditor;
    }
    /*auditName get 方法 */
    public String getAuditName(){
    return auditName;
    }
    /*auditName set 方法 */
    public void setAuditName(String  auditName){
    this.auditName=auditName;
    }
    /*outDepotDate get 方法 */
    public Timestamp getOutDepotDate(){
    return outDepotDate;
    }
    /*outDepotDate set 方法 */
    public void setOutDepotDate(Timestamp  outDepotDate){
    this.outDepotDate=outDepotDate;
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

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }
}
