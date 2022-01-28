package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 库存预警
 * @author lian_
 *
 */
public class InventoryWarningModel extends PageModel implements Serializable{

     //商品货号
     private String goodsNo;
     //失效日期
     private Date overdueDate;
     //仓库名称
     private String depotName;
     //批次号
     private String batchNo;
     //商品id
     private Long goodsId;
     //剩余效期（天）
     private Integer surplusDays;
     //仓库id
     private Long depotId;
     //商家名称
     private String merchantName;
     //生产日期
     private Date productionDate;
     //总效期(天)
     private Integer totalDays;
     //商家ID
     private Long merchantId;
     //商品名称
     private String goodsName;
     //id
     private Long id;
     //剩余效期
     private Timestamp surplusDate;
     //创建时间
     private Timestamp createDate;

    /*goodsNo get 方法 */
    public String getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*overdueDate get 方法 */
    public Date getOverdueDate(){
        return overdueDate;
    }
    /*overdueDate set 方法 */
    public void setOverdueDate(Date  overdueDate){
        this.overdueDate=overdueDate;
    }
    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*batchNo get 方法 */
    public String getBatchNo(){
        return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
        this.batchNo=batchNo;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*surplusDays get 方法 */
    public Integer getSurplusDays(){
        return surplusDays;
    }
    /*surplusDays set 方法 */
    public void setSurplusDays(Integer  surplusDays){
        this.surplusDays=surplusDays;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
    }
    /*productionDate get 方法 */
    public Date getProductionDate(){
        return productionDate;
    }
    /*productionDate set 方法 */
    public void setProductionDate(Date  productionDate){
        this.productionDate=productionDate;
    }
    /*totalDays get 方法 */
    public Integer getTotalDays(){
        return totalDays;
    }
    /*totalDays set 方法 */
    public void setTotalDays(Integer  totalDays){
        this.totalDays=totalDays;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*name get 方法 */
    public String getGoodsName(){
        return goodsName;
    }
    /*name set 方法 */
    public void setGoodsName(String  goodsName){
        this.goodsName=goodsName;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*surplusDate get 方法 */
    public Timestamp getSurplusDate(){
        return surplusDate;
    }
    /*surplusDate set 方法 */
    public void setSurplusDate(Timestamp  surplusDate){
        this.surplusDate=surplusDate;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }






}

