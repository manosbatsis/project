package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class VipOutDepotDetailsModel extends PageModel implements Serializable{

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
    * PO号
    */
    private String poNo;
    /**
    * 唯品账单号
    */
    private String vipBillCode;
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
    * 销售订单号
    */
    private String saleOrder;
    /**
    * 销售出库单号
    */
    private String saleOutOrder;
    /**
    * 出库数量
    */
    private Integer outDepotNum;
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

    //po单时间
    private Timestamp poDate;

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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*vipBillCode get 方法 */
    public String getVipBillCode(){
    return vipBillCode;
    }
    /*vipBillCode set 方法 */
    public void setVipBillCode(String  vipBillCode){
    this.vipBillCode=vipBillCode;
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
    /*saleOrder get 方法 */
    public String getSaleOrder(){
    return saleOrder;
    }
    /*saleOrder set 方法 */
    public void setSaleOrder(String  saleOrder){
    this.saleOrder=saleOrder;
    }
    /*saleOutOrder get 方法 */
    public String getSaleOutOrder(){
    return saleOutOrder;
    }
    /*saleOutOrder set 方法 */
    public void setSaleOutOrder(String  saleOutOrder){
    this.saleOutOrder=saleOutOrder;
    }
    /*outDepotNum get 方法 */
    public Integer getOutDepotNum(){
    return outDepotNum;
    }
    /*outDepotNum set 方法 */
    public void setOutDepotNum(Integer  outDepotNum){
    this.outDepotNum=outDepotNum;
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

    public Timestamp getPoDate() {
        return poDate;
    }

    public void setPoDate(Timestamp poDate) {
        this.poDate = poDate;
    }
}
