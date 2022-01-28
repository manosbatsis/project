package com.topideal.api.epass.e04;

import java.util.List;

/**
 * 调拨指令推送
 * @author 杨创
 *2018/5/31
 */
public class TransferOrderRequest {
	private String order_id;//企业调拨单号
	private String odate;//调拨时间 时间格式：yyyy-mm-dd HH:mi:ss
	private String customs_code;//申报地海关BBC关区码表：埔开发区：5208 ,南沙旅检：5165
	private String ciqb_code;//申报地国检 BBC国检码表：,黄埔局本部：000072,南沙局本部：000069
	private String busi_scene;//业务场景10：账册内调仓,20：账册内货号变更,30：账册内货号变更调仓,40：账册内货权转移,50：账册内货权转移调仓,60：区内跨海关账册调入,70：区内跨海关账册调出,80：非实物调拨
	private String serve_types;//服务类型  E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨
	private String to_electric_code;//调入电商企业
	private String trust_code;//委托单位
	private String from_store_code;//调出仓库
	private String to_store_code;//调入仓库
	private String contract_no;//合同号
	private Integer transport_servic;//运输服务  调出仓库为海外仓时选填 9210:中港运输  9220:香港本地运输
	private String wms_operations;//仓内作业   调出仓库为海外仓时选填（多选）以“，”隔开9110:理货,9120:集拼,9130:打托,9140:打包,9150:称重,9160:贴单
	private String add_services;//增值服务 调出仓库为海外仓时选填（多选）以“，”隔开,9310:包材推荐,9320:委托溯源,9330:贴溯源码
	private Integer total_num; //件数合计数量  调出仓库为海外仓时选填
	private Integer panel_wallTotalNum;//托板合计数量   调出仓库为海外仓时选填
	private Double volume_totalnum;//体积合计 调出仓库为海外仓时选填  保留两位小数
	private String invoice_no;//发票号  调出仓库为海外仓，调入仓库为综合仓必填
	private String server_types;//服务类型  	调出仓库为海外仓，调入仓库为综合仓必填,01：港口接货,02：机场转关,03：境外接货,04：港口接货（拼柜）,05：境外接货（拼柜）,06：仓门接货
	private String recadd;//recadd	调出仓库为海外仓，调入仓库为综合仓必填
	private String shipment;//shipment
	private String port_loading;//装货港  调出仓库为海外仓，调入仓库为综合仓必填
	private String pack_type;//包装方式 调出仓库为海外仓，调入仓库为综合仓必填
	private String pallet_mate;//托盘材质  调出仓库为海外仓，调入仓库为综合仓必填
	private String lpnqty;//托数   调出仓库为海外仓，调入仓库为综合仓必填
	private String cartons;//箱数   调出仓库为海外仓，调入仓库为综合仓必填
	private String pay_rules;//付款条约   调出仓库为海外仓，调入仓库为综合仓必填
	private String bill_weight;//提单毛重   调出仓库为海外仓，调入仓库为综合仓必填
	
	private List<TransferOrderItemRequest>good_list;//
	
	

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOdate() {
		return odate;
	}

	public void setOdate(String odate) {
		this.odate = odate;
	}

	public String getCustoms_code() {
		return customs_code;
	}

	public void setCustoms_code(String customs_code) {
		this.customs_code = customs_code;
	}

	public String getCiqb_code() {
		return ciqb_code;
	}

	public void setCiqb_code(String ciqb_code) {
		this.ciqb_code = ciqb_code;
	}

	public String getBusi_scene() {
		return busi_scene;
	}

	public void setBusi_scene(String busi_scene) {
		this.busi_scene = busi_scene;
	}

	public String getServe_types() {
		return serve_types;
	}

	public void setServe_types(String serve_types) {
		this.serve_types = serve_types;
	}

	public String getTo_electric_code() {
		return to_electric_code;
	}

	public void setTo_electric_code(String to_electric_code) {
		this.to_electric_code = to_electric_code;
	}

	public String getTrust_code() {
		return trust_code;
	}

	public void setTrust_code(String trust_code) {
		this.trust_code = trust_code;
	}

	public String getFrom_store_code() {
		return from_store_code;
	}

	public void setFrom_store_code(String from_store_code) {
		this.from_store_code = from_store_code;
	}

	public String getTo_store_code() {
		return to_store_code;
	}

	public void setTo_store_code(String to_store_code) {
		this.to_store_code = to_store_code;
	}

	public String getContract_no() {
		return contract_no;
	}

	public void setContract_no(String contract_no) {
		this.contract_no = contract_no;
	}

	public Integer getTransport_servic() {
		return transport_servic;
	}

	public void setTransport_servic(Integer transport_servic) {
		this.transport_servic = transport_servic;
	}

	public String getWms_operations() {
		return wms_operations;
	}

	public void setWms_operations(String wms_operations) {
		this.wms_operations = wms_operations;
	}

	public String getAdd_services() {
		return add_services;
	}

	public void setAdd_services(String add_services) {
		this.add_services = add_services;
	}

	public Integer getTotal_num() {
		return total_num;
	}

	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
	}

	public Integer getPanel_wallTotalNum() {
		return panel_wallTotalNum;
	}

	public void setPanel_wallTotalNum(Integer panel_wallTotalNum) {
		this.panel_wallTotalNum = panel_wallTotalNum;
	}

	public Double getVolume_totalnum() {
		return volume_totalnum;
	}

	public void setVolume_totalnum(Double volume_totalnum) {
		this.volume_totalnum = volume_totalnum;
	}

	public String getInvoice_no() {
		return invoice_no;
	}

	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}

	public String getServer_types() {
		return server_types;
	}

	public void setServer_types(String server_types) {
		this.server_types = server_types;
	}

	public String getRecadd() {
		return recadd;
	}

	public void setRecadd(String recadd) {
		this.recadd = recadd;
	}

	public String getShipment() {
		return shipment;
	}

	public void setShipment(String shipment) {
		this.shipment = shipment;
	}

	public String getPort_loading() {
		return port_loading;
	}

	public void setPort_loading(String port_loading) {
		this.port_loading = port_loading;
	}

	public String getPack_type() {
		return pack_type;
	}

	public void setPack_type(String pack_type) {
		this.pack_type = pack_type;
	}

	public String getPallet_mate() {
		return pallet_mate;
	}

	public void setPallet_mate(String pallet_mate) {
		this.pallet_mate = pallet_mate;
	}

	public String getLpnqty() {
		return lpnqty;
	}

	public void setLpnqty(String lpnqty) {
		this.lpnqty = lpnqty;
	}

	public String getCartons() {
		return cartons;
	}

	public void setCartons(String cartons) {
		this.cartons = cartons;
	}

	public String getPay_rules() {
		return pay_rules;
	}

	public void setPay_rules(String pay_rules) {
		this.pay_rules = pay_rules;
	}

	public String getBill_weight() {
		return bill_weight;
	}

	public void setBill_weight(String bill_weight) {
		this.bill_weight = bill_weight;
	}

	public List<TransferOrderItemRequest> getGood_list() {
		return good_list;
	}

	public void setGood_list(List<TransferOrderItemRequest> good_list) {
		this.good_list = good_list;
	}
	
	
	
	
	
	


}
