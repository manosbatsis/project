package com.topideal.entity.vo.common;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class LogisticsAttorneyModel extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 类型 1-采购预申报
    */
    private String type;
    /**
    * 关联预申报单ID
    */
    private Long orderId;
    /**
    * 发货人/提货信息模版ID
    */
    private Long shipperTempId;
    /**
    * 发货人/提货信息模版名称
    */
    private String shipperTempName;
    /**
    * 发货人/提货信息内容
    */
    private String shipperText;
    /**
    * 收货人/卸货信息模版ID
    */
    private Long consigneeTempId;
    /**
    * 收货人/卸货信息模版名称
    */
    private String consigneeTempName;
    /**
    * 收货人/卸货信息内容
    */
    private String consigneeText;
    /**
    * 通知人模版ID
    */
    private Long notifierTempId;
    /**
    * 通知人模版名
    */
    private String notifierTempName;
    /**
    * 通知人内容
    */
    private String notifierText;
    /**
    * 对接人模版ID
    */
    private Long dockingTempId;
    /**
    * 对接人模版名
    */
    private String dockingTempName;
    /**
    * 对接人内容
    */
    private String dockingText;
    /**
    * 承运商信息模版ID
    */
    private Long carrierInformationTempId;
    /**
    * 承运商信息模版名
    */
    private String carrierInformationTempName;
    /**
    * 承运商内容
    */
    private String carrierInformationText;
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
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
    }
    /*shipperTempId get 方法 */
    public Long getShipperTempId(){
    return shipperTempId;
    }
    /*shipperTempId set 方法 */
    public void setShipperTempId(Long  shipperTempId){
    this.shipperTempId=shipperTempId;
    }
    /*shipperTempName get 方法 */
    public String getShipperTempName(){
    return shipperTempName;
    }
    /*shipperTempName set 方法 */
    public void setShipperTempName(String  shipperTempName){
    this.shipperTempName=shipperTempName;
    }
    /*shipperText get 方法 */
    public String getShipperText(){
    return shipperText;
    }
    /*shipperText set 方法 */
    public void setShipperText(String  shipperText){
    this.shipperText=shipperText;
    }
    /*consigneeTempId get 方法 */
    public Long getConsigneeTempId(){
    return consigneeTempId;
    }
    /*consigneeTempId set 方法 */
    public void setConsigneeTempId(Long  consigneeTempId){
    this.consigneeTempId=consigneeTempId;
    }
    /*consigneeTempName get 方法 */
    public String getConsigneeTempName(){
    return consigneeTempName;
    }
    /*consigneeTempName set 方法 */
    public void setConsigneeTempName(String  consigneeTempName){
    this.consigneeTempName=consigneeTempName;
    }
    /*consigneeText get 方法 */
    public String getConsigneeText(){
    return consigneeText;
    }
    /*consigneeText set 方法 */
    public void setConsigneeText(String  consigneeText){
    this.consigneeText=consigneeText;
    }
    /*notifierTempId get 方法 */
    public Long getNotifierTempId(){
    return notifierTempId;
    }
    /*notifierTempId set 方法 */
    public void setNotifierTempId(Long  notifierTempId){
    this.notifierTempId=notifierTempId;
    }
    /*notifierTempName get 方法 */
    public String getNotifierTempName(){
    return notifierTempName;
    }
    /*notifierTempName set 方法 */
    public void setNotifierTempName(String  notifierTempName){
    this.notifierTempName=notifierTempName;
    }
    /*notifierText get 方法 */
    public String getNotifierText(){
    return notifierText;
    }
    /*notifierText set 方法 */
    public void setNotifierText(String  notifierText){
    this.notifierText=notifierText;
    }
    /*dockingTempId get 方法 */
    public Long getDockingTempId(){
    return dockingTempId;
    }
    /*dockingTempId set 方法 */
    public void setDockingTempId(Long  dockingTempId){
    this.dockingTempId=dockingTempId;
    }
    /*dockingTempName get 方法 */
    public String getDockingTempName(){
    return dockingTempName;
    }
    /*dockingTempName set 方法 */
    public void setDockingTempName(String  dockingTempName){
    this.dockingTempName=dockingTempName;
    }
    /*dockingText get 方法 */
    public String getDockingText(){
    return dockingText;
    }
    /*dockingText set 方法 */
    public void setDockingText(String  dockingText){
    this.dockingText=dockingText;
    }
    /*carrierInformationTempId get 方法 */
    public Long getCarrierInformationTempId(){
    return carrierInformationTempId;
    }
    /*carrierInformationTempId set 方法 */
    public void setCarrierInformationTempId(Long  carrierInformationTempId){
    this.carrierInformationTempId=carrierInformationTempId;
    }
    /*carrierInformationTempName get 方法 */
    public String getCarrierInformationTempName(){
    return carrierInformationTempName;
    }
    /*carrierInformationTempName set 方法 */
    public void setCarrierInformationTempName(String  carrierInformationTempName){
    this.carrierInformationTempName=carrierInformationTempName;
    }
    /*carrierInformationText get 方法 */
    public String getCarrierInformationText(){
    return carrierInformationText;
    }
    /*carrierInformationText set 方法 */
    public void setCarrierInformationText(String  carrierInformationText){
    this.carrierInformationText=carrierInformationText;
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
