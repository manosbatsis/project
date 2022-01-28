package com.topideal.order.webapi.common.form;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class LogisticsContactTemplateLinkForm extends PageForm implements Serializable{

    /**
    * 记录ID
    */
	@ApiModelProperty("记录ID")
    private Long id;
    /**
    * 名称
    */
	@ApiModelProperty("名称")
    private String name;
    /**
    * 发货人/提货信息模版ID
    */
	@ApiModelProperty("发货人/提货信息模版ID")
    private Long shipperTempId;
    /**
    * 发货人/提货信息模版名称
    */
	@ApiModelProperty("发货人/提货信息模版名称")
    private String shipperTempName;
    /**
    * 收货人/卸货信息模版ID
    */
	@ApiModelProperty("收货人/卸货信息模版ID")
    private Long consigneeTempId;
    /**
    * 收货人/卸货信息模版名称
    */
	@ApiModelProperty("收货人/卸货信息模版名称")
    private String consigneeTempName;
    /**
    * 通知人模版ID
    */
	@ApiModelProperty("通知人模版ID")
    private Long notifierTempId;
    /**
    * 通知人模版名
    */
	@ApiModelProperty("通知人模版名")
    private String notifierTempName;
    /**
    * 对接人模版ID
    */
	@ApiModelProperty("对接人模版ID")
    private Long dockingTempId;
    /**
    * 对接人模版名
    */
	@ApiModelProperty("对接人模版名")
    private String dockingTempName;
    /**
    * 承运商信息模版ID
    */
	@ApiModelProperty("承运商信息模版ID")
    private Long carrierInformationTempId;
    /**
    * 承运商信息模版名
    */
	@ApiModelProperty("承运商信息模版名")
    private String carrierInformationTempName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
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



}
