package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class WayBillHistoryModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 电商订单ID
    */
    private Long orderId;
    /**
    * 运单号
    */
    private String wayBillNo;
    /**
    * 发货时间
    */
    private Timestamp deliverDate;
    /**
    * 物流公司
    */
    private String logisticsName;
    /**
    * 物流公司编码
    */
    private String logisticsCode;
    /**
    * 提单号
    */
    private String blNo;
    /**
    * 服务类型 20
    */
    private String type;
    /**
    * 备注
    */
    private String remark;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 商家id
    */
    private Long merchantId;
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
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
    }
    /*wayBillNo get 方法 */
    public String getWayBillNo(){
    return wayBillNo;
    }
    /*wayBillNo set 方法 */
    public void setWayBillNo(String  wayBillNo){
    this.wayBillNo=wayBillNo;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
    return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
    this.deliverDate=deliverDate;
    }
    /*logisticsName get 方法 */
    public String getLogisticsName(){
    return logisticsName;
    }
    /*logisticsName set 方法 */
    public void setLogisticsName(String  logisticsName){
    this.logisticsName=logisticsName;
    }
    /*logisticsCode get 方法 */
    public String getLogisticsCode(){
    return logisticsCode;
    }
    /*logisticsCode set 方法 */
    public void setLogisticsCode(String  logisticsCode){
    this.logisticsCode=logisticsCode;
    }
    /*blNo get 方法 */
    public String getBlNo(){
    return blNo;
    }
    /*blNo set 方法 */
    public void setBlNo(String  blNo){
    this.blNo=blNo;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }






}
