package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 库存在途明细
 * @author lian_
 *
 */
public class InventoryOnwayDetailsModel extends PageModel implements Serializable{

     //商品货号
     private Long goodsNo;
     //仓库名称
     private String depotName;
     //商品ID
     private Long goodsId;
     //仓库ID
     private Long depotId;
     //来源类型  1采购入库  2 销售入库
     private String status;
     //商家名称
     private String merchantName;
     //来源单据号
     private String source;
     //商家ID
     private Long merchantId;
     //商品名称
     private String goodsName;
     //在途数量
     private Integer onwayNum;
     //id
     private Long id;
     //创建时间
     private Timestamp createDate;
     //订单编号
     private String orderNo;
     //出入时间
     private Timestamp divergenceDate;
     //单据状态 名称
     private String statusName;
     //库存类型  1 正常品  2 残次品
     private String type;
     //单据日期
     private Timestamp sourceDate;
     //单位（ 0 托盘 1箱  2件）
     private String unit;
     //是否代销仓(1-是,0-否)
     private String isTopBooks;
     //失效日期  
     private Date overdueDate;
     //生产日期
     private Date productionDate;
     //仓库类型(a-卓志保税仓，b-非卓志保税仓，c-卓志海外仓，d-在途仓,e非卓志国内仓）
     private String depotType;
     //操作类型  0 调减 1调增
     private String operateType;
     
     /*operateType get 方法 */
     public String getOperateType(){
         return operateType;
     }
     /*operateType set 方法 */
     public void setOperateType(String  operateType){
         this.operateType=operateType;
     }
     
     /*depotType get 方法 */
     public String getDepotType(){
         return depotType;
     }
     /*depotType set 方法 */
     public void setDepotType(String  depotType){
         this.depotType=depotType;
     }
     /*productionDate get 方法 */
     public Date getProductionDate(){
         return productionDate;
     }
     /*productionDate set 方法 */
     public void setProductionDate(Date  productionDate){
         this.productionDate=productionDate;
     }
     /*overdueDate get 方法 */
     public Date getOverdueDate(){
         return overdueDate;
     }
     /*overdueDate set 方法 */
     public void setOverdueDate(Date  overdueDate){
         this.overdueDate=overdueDate;
     }
     
     /*isTopBooks get 方法 */
     public String getIsTopBooks(){
         return isTopBooks;
     }
     /*isTopBooks set 方法 */
     public void setIsTopBooks(String  isTopBooks){
         this.isTopBooks=isTopBooks;
     }
     /*sourceDate get 方法 */
     public Timestamp getSourceDate(){
         return sourceDate;
     }
     /*sourceDate set 方法 */
     public void setSourceDate(Timestamp  sourceDate){
         this.sourceDate=sourceDate;
     }
     /*unit get 方法 */
     public String getUnit(){
         return unit;
     }
     /*unit set 方法 */
     public void setUnit(String  unit){
         this.unit=unit;
     }
     /*type get 方法 */
     public String getType(){
         return type;
     }
     /*type set 方法 */
     public void setType(String  type){
         this.type=type;
     }
     
     /*statusName get 方法 */
     public String getStatusName(){
         return statusName;
     }
     /*statusName set 方法 */
     public void setStatusName(String  statusName){
         this.statusName=statusName;
     }
     
     /*source get 方法 */
     public String getSource(){
         return source;
     }
     /*source set 方法 */
     public void setSource(String  source){
         this.source=source;
     }
     /*divergenceDate get 方法 */
     public Timestamp getDivergenceDate(){
         return divergenceDate;
     }
     /*divergenceDate set 方法 */
     public void setDivergenceDate(Timestamp  divergenceDate){
         this.divergenceDate=divergenceDate;
     }
     /*orderNo get 方法 */
     public String getOrderNo(){
         return orderNo;
     }
     /*orderNo set 方法 */
     public void setOrderNo(String  orderNo){
         this.orderNo=orderNo;
     }
    /*goodsNo get 方法 */
    public Long getGoodsNo(){
        return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(Long  goodsNo){
        this.goodsNo=goodsNo;
    }
    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
        return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
        this.goodsId=goodsId;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*status get 方法 */
    public String getStatus(){
        return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
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
    /*onwayNum get 方法 */
    public Integer getOnwayNum(){
        return onwayNum;
    }
    /*onwayNum set 方法 */
    public void setOnwayNum(Integer  onwayNum){
        this.onwayNum=onwayNum;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
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

