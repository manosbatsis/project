package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LogisticsAttorneyForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
	
	@ApiModelProperty(value = "关联预申报单ID")
    private Long orderId;

	@ApiModelProperty(value = "发货人/提货信息模版ID")
    private Long shipperTempId;

	@ApiModelProperty(value = "发货人/提货信息模版名称")
    private String shipperTempName;

	@ApiModelProperty(value = "发货人/提货信息内容")
    private String shipperText;

	@ApiModelProperty(value = "收货人/卸货信息模版ID")
    private Long consigneeTempId;

	@ApiModelProperty(value = "收货人/卸货信息模版名称")
    private String consigneeTempName;

	@ApiModelProperty(value = "收货人/卸货信息内容")
    private String consigneeText;

	@ApiModelProperty(value = "通知人模版ID")
    private Long notifierTempId;

	@ApiModelProperty(value = "通知人模版名")
    private String notifierTempName;

	@ApiModelProperty(value = "通知人内容")
    private String notifierText;
    
	@ApiModelProperty(value = "对接人模版ID")
    private Long dockingTempId;

	@ApiModelProperty(value = "对接人模版名")
    private String dockingTempName;

	@ApiModelProperty(value = "对接人内容")
    private String dockingText;

	@ApiModelProperty(value = "承运商信息模版ID")
    private Long carrierInformationTempId;

	@ApiModelProperty(value = "承运商信息模版名")
    private String carrierInformationTempName;

	@ApiModelProperty(value = "承运商内容")
    private String carrierInformationText;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getShipperTempId() {
		return shipperTempId;
	}

	public void setShipperTempId(Long shipperTempId) {
		this.shipperTempId = shipperTempId;
	}

	public String getShipperTempName() {
		return shipperTempName;
	}

	public void setShipperTempName(String shipperTempName) {
		this.shipperTempName = shipperTempName;
	}

	public String getShipperText() {
		return shipperText;
	}

	public void setShipperText(String shipperText) {
		this.shipperText = shipperText;
	}

	public Long getConsigneeTempId() {
		return consigneeTempId;
	}

	public void setConsigneeTempId(Long consigneeTempId) {
		this.consigneeTempId = consigneeTempId;
	}

	public String getConsigneeTempName() {
		return consigneeTempName;
	}

	public void setConsigneeTempName(String consigneeTempName) {
		this.consigneeTempName = consigneeTempName;
	}

	public String getConsigneeText() {
		return consigneeText;
	}

	public void setConsigneeText(String consigneeText) {
		this.consigneeText = consigneeText;
	}

	public Long getNotifierTempId() {
		return notifierTempId;
	}

	public void setNotifierTempId(Long notifierTempId) {
		this.notifierTempId = notifierTempId;
	}

	public String getNotifierTempName() {
		return notifierTempName;
	}

	public void setNotifierTempName(String notifierTempName) {
		this.notifierTempName = notifierTempName;
	}

	public String getNotifierText() {
		return notifierText;
	}

	public void setNotifierText(String notifierText) {
		this.notifierText = notifierText;
	}

	public Long getDockingTempId() {
		return dockingTempId;
	}

	public void setDockingTempId(Long dockingTempId) {
		this.dockingTempId = dockingTempId;
	}

	public String getDockingTempName() {
		return dockingTempName;
	}

	public void setDockingTempName(String dockingTempName) {
		this.dockingTempName = dockingTempName;
	}

	public String getDockingText() {
		return dockingText;
	}

	public void setDockingText(String dockingText) {
		this.dockingText = dockingText;
	}

	public Long getCarrierInformationTempId() {
		return carrierInformationTempId;
	}

	public void setCarrierInformationTempId(Long carrierInformationTempId) {
		this.carrierInformationTempId = carrierInformationTempId;
	}

	public String getCarrierInformationTempName() {
		return carrierInformationTempName;
	}

	public void setCarrierInformationTempName(String carrierInformationTempName) {
		this.carrierInformationTempName = carrierInformationTempName;
	}

	public String getCarrierInformationText() {
		return carrierInformationText;
	}

	public void setCarrierInformationText(String carrierInformationText) {
		this.carrierInformationText = carrierInformationText;
	}
   

}
