package com.topideal.entity.dto.common;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class LogisticsContactTemplateLinkDTO extends PageModel implements Serializable{

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
	@ApiModelProperty("发货人/提货信息模版详情")
    private String shipperTempDetails;
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
	@ApiModelProperty("收货人/卸货信息模版详情")
	private String consigneeTempDetails;
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
	@ApiModelProperty("通知人模版详情")
    private String notifierTempDetails;
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
	@ApiModelProperty("对接人模版详情")
    private String dockingTempDetails;
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
	@ApiModelProperty("承运商信息模版详情")
	private String carrierInformationTempDetails;
    /**
    * 创建时间
    */
	@ApiModelProperty("创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty("修改时间")
    private Timestamp modifyDate;

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
	public String getShipperTempDetails() {
		return shipperTempDetails;
	}
	public void setShipperTempDetails(String shipperTempDetails) {
		this.shipperTempDetails = shipperTempDetails;
	}
	public String getConsigneeTempDetails() {
		return consigneeTempDetails;
	}
	public void setConsigneeTempDetails(String consigneeTempDetails) {
		this.consigneeTempDetails = consigneeTempDetails;
	}
	public String getNotifierTempDetails() {
		return notifierTempDetails;
	}
	public void setNotifierTempDetails(String notifierTempDetails) {
		this.notifierTempDetails = notifierTempDetails;
	}
	public String getDockingTempDetails() {
		return dockingTempDetails;
	}
	public void setDockingTempDetails(String dockingTempDetails) {
		this.dockingTempDetails = dockingTempDetails;
	}
	public String getCarrierInformationTempDetails() {
		return carrierInformationTempDetails;
	}
	public void setCarrierInformationTempDetails(String carrierInformationTempDetails) {
		this.carrierInformationTempDetails = carrierInformationTempDetails;
	}






}
