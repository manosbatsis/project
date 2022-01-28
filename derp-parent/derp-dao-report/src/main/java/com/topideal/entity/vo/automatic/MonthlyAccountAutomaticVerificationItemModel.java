package com.topideal.entity.vo.automatic;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class MonthlyAccountAutomaticVerificationItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 仓库id
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 归属月份 YYYY-MM
    */
    private String month;
    /**
    * 库存类型: 1-好品 0-坏品
    */
    private String type;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 月库结转表的库存总量
    */
    private Integer monthlyAccountSurplusNum;
    /**
    * 事业部库存总量
    */
    private Integer buInventorySurplusNum;
    /**
    * 事业部业务进销存期末库存总量
    */
    private Integer buInventorySummaryEndNum;
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
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
    /*monthlyAccountSurplusNum get 方法 */
    public Integer getMonthlyAccountSurplusNum(){
    return monthlyAccountSurplusNum;
    }
    /*monthlyAccountSurplusNum set 方法 */
    public void setMonthlyAccountSurplusNum(Integer  monthlyAccountSurplusNum){
    this.monthlyAccountSurplusNum=monthlyAccountSurplusNum;
    }
    /*buInventorySurplusNum get 方法 */
    public Integer getBuInventorySurplusNum(){
    return buInventorySurplusNum;
    }
    /*buInventorySurplusNum set 方法 */
    public void setBuInventorySurplusNum(Integer  buInventorySurplusNum){
    this.buInventorySurplusNum=buInventorySurplusNum;
    }
    /*buInventorySummaryEndNum get 方法 */
    public Integer getBuInventorySummaryEndNum(){
    return buInventorySummaryEndNum;
    }
    /*buInventorySummaryEndNum set 方法 */
    public void setBuInventorySummaryEndNum(Integer  buInventorySummaryEndNum){
    this.buInventorySummaryEndNum=buInventorySummaryEndNum;
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






}
