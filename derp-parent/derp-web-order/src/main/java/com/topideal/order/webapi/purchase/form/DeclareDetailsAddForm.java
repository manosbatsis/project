package com.topideal.order.webapi.purchase.form;


import io.swagger.annotations.ApiModelProperty;

/**
 * 预申报单轨迹时间添加form
 * 
 * @author Guobs
 *
 */
public class DeclareDetailsAddForm {

	@ApiModelProperty(value="令牌", required=true)
    private String token;
	// 预申报单号
	@ApiModelProperty(value = "预申报单ID", required = true)
	private Long id ;
	@ApiModelProperty(value = "更新类型 2-导出物流委托 3-确认订舱信息 4-工厂提货（装船）5- 提交一线清关资料 6-审核一线清关资料 8-申报完成 10-订车信息确认 11-提货入仓 12-确认到港", required = true)
	private String type ;
	/**
	 * 预计到港时间
	 */
	@ApiModelProperty("预计到港时间")
	private String arriveDate;
	/**
	 * 工厂提货（装船） 工厂提货确认时间
	 */
	@ApiModelProperty("工厂提货（装船） 工厂提货确认时间")
	private String shipDate;
	/**
	 * 清关确认时间
	 */
	@ApiModelProperty("清关确认时间")
	private String customsConfirmDate;
	/**
	 * 清关提交时间
	 */
	@ApiModelProperty("清关提交时间")
	private String customsSubmitDate;
	/**
	 * 确认申报时间
	 */
	@ApiModelProperty("确认申报时间")
	private String confirmDeclarationDate;
	/**
	 * 确认入仓时间
	 */
	@ApiModelProperty("确认入仓时间")
	private String confirmDepotDate;
	/**
	 * 确认订舱/车时间
	 */
	@ApiModelProperty("确认订舱/车时间")
	private String confirmBookingDate;
	/**
	 * 物流委托时间
	 */
	@ApiModelProperty("物流委托时间")
	private String logisticsCommissionDate;
	/**
	 * 预计离港时间
	 */
	@ApiModelProperty("预计离港时间")
	private String estimatedDeliveryDate;
	/**
	 * 上架时间
	 */
	@ApiModelProperty("上架时间")
	private String shelfDate;

	@ApiModelProperty("确认订车时间")
	private String confirmCatDate;

	/**
	 * 到港时间
	 */
	@ApiModelProperty("到港时间")
	private String arriveSeaDate;
	/**
	 * 提货时间
	 */
	@ApiModelProperty("提货时间")
	private String pickUpDate;

	public String getArriveSeaDate() {
		return arriveSeaDate;
	}

	public void setArriveSeaDate(String arriveSeaDate) {
		this.arriveSeaDate = arriveSeaDate;
	}

	public String getConfirmCatDate() {
		return confirmCatDate;
	}

	public void setConfirmCatDate(String confirmCatDate) {
		this.confirmCatDate = confirmCatDate;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getArriveDate() {
		return arriveDate;
	}
	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getCustomsConfirmDate() {
		return customsConfirmDate;
	}
	public void setCustomsConfirmDate(String customsConfirmDate) {
		this.customsConfirmDate = customsConfirmDate;
	}
	public String getCustomsSubmitDate() {
		return customsSubmitDate;
	}
	public void setCustomsSubmitDate(String customsSubmitDate) {
		this.customsSubmitDate = customsSubmitDate;
	}
	public String getConfirmDeclarationDate() {
		return confirmDeclarationDate;
	}
	public void setConfirmDeclarationDate(String confirmDeclarationDate) {
		this.confirmDeclarationDate = confirmDeclarationDate;
	}
	public String getConfirmDepotDate() {
		return confirmDepotDate;
	}
	public void setConfirmDepotDate(String confirmDepotDate) {
		this.confirmDepotDate = confirmDepotDate;
	}
	public String getConfirmBookingDate() {
		return confirmBookingDate;
	}
	public void setConfirmBookingDate(String confirmBookingDate) {
		this.confirmBookingDate = confirmBookingDate;
	}
	public String getLogisticsCommissionDate() {
		return logisticsCommissionDate;
	}
	public void setLogisticsCommissionDate(String logisticsCommissionDate) {
		this.logisticsCommissionDate = logisticsCommissionDate;
	}
	public String getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}
	public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}
	public String getShelfDate() {
		return shelfDate;
	}
	public void setShelfDate(String shelfDate) {
		this.shelfDate = shelfDate;
	}

	public String getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(String pickUpDate) {
		this.pickUpDate = pickUpDate;
	}
	
	
}
