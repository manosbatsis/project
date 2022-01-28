package com.topideal.json.pushapi.epass.e01;

import java.util.List;

/**
 * 采购订单表头 推送报文
 * 
 * @author zhanghx
 */
public class PurchaseOrderRootJson {

	// 必填（保税仓：Y，海外仓：Y）
	// 企业入仓编号
	private String ent_inbound_id;
	// 商家卓志编码
	private String topideal_code;
	// 合同号
	private String contract_no;
	
	// 卓志保税仓必填（保税仓：Y，海外仓：N）
	// 发票号
	private String invoice_no;
	// 收货地址
	private String recadd;
	// 装货港
	private String port_loading;
	// 包装方式
	private String pack_type;
	// 箱数
	private String cartons;
	// 付款条约
	private String pay_rules;
	// 提单毛重
	private String bill_weight;
	// 卸货港编码
	private String port_dest_no;
	// 境外发货人
	private String overseas_shipper;
	// 唛头
	private String mark;
	
	// 海外仓必填（保税仓：N，海外仓：Y）
	// 仓库编码
	private String warehouse_id;
	// 单据状态
	private String status;
	// 理货单位
	private String uom;
	// 业务模式
	private String business_moshi;
	// 事业部单位
	private String businessUnit;
	

	// 非必填（保税仓：N，海外仓：N）
	// 头程提单号
	private String transportbl_no;
	// 二程提单号
	private String bl_no;
	// 进出口口岸
	private String im_exp_port;
	// 服务类型
	private String server_types;
	// 装船时间
	private String shipment;
	// 托盘材质
	private String pallet_mate;
	// 托数
	private String lpnqty;
	// 预计到货时间
	private String arrival_date;
	// 资金方编码
	private String foundp_code;
	// 运输服务
	private String transport_servic;
	// 仓内作业
	private String wms_operations;
	// 增值服务
	private String add_services;
	// 启运港编码
	private String port_load;
	// 目的港名称
	private String port_dis;
	// 目的地名称
	private String destination;
	// 卸货港名称
	private String port_destination;
	// 托版数量
	private Integer panel_wallnumber;
	// 邮箱地址
	private String email_add;
	// 备注
	private String notes;
	//数据来源  经分销：DISTRIBUTED（经分销此字段必填）
	private String datasource;
	private List<PurchaseGoodsListJson> goods_list;//

	public String getBusiness_moshi() {
		return business_moshi;
	}

	public void setBusiness_moshi(String business_moshi) {
		this.business_moshi = business_moshi;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getOverseas_shipper() {
		return overseas_shipper;
	}

	public void setOverseas_shipper(String overseas_shipper) {
		this.overseas_shipper = overseas_shipper;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getEnt_inbound_id() {
		return ent_inbound_id;
	}

	public void setEnt_inbound_id(String ent_inbound_id) {
		this.ent_inbound_id = ent_inbound_id;
	}

	public String getTopideal_code() {
		return topideal_code;
	}

	public void setTopideal_code(String topideal_code) {
		this.topideal_code = topideal_code;
	}

	public String getContract_no() {
		return contract_no;
	}

	public void setContract_no(String contract_no) {
		this.contract_no = contract_no;
	}

	public String getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	public String getInvoice_no() {
		return invoice_no;
	}

	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}

	public String getRecadd() {
		return recadd;
	}

	public void setRecadd(String recadd) {
		this.recadd = recadd;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransportbl_no() {
		return transportbl_no;
	}

	public void setTransportbl_no(String transportbl_no) {
		this.transportbl_no = transportbl_no;
	}

	public String getBl_no() {
		return bl_no;
	}

	public void setBl_no(String bl_no) {
		this.bl_no = bl_no;
	}

	public String getPort_dest_no() {
		return port_dest_no;
	}

	public void setPort_dest_no(String port_dest_no) {
		this.port_dest_no = port_dest_no;
	}

	public String getIm_exp_port() {
		return im_exp_port;
	}

	public void setIm_exp_port(String im_exp_port) {
		this.im_exp_port = im_exp_port;
	}

	public String getServer_types() {
		return server_types;
	}

	public void setServer_types(String server_types) {
		this.server_types = server_types;
	}

	public String getShipment() {
		return shipment;
	}

	public void setShipment(String shipment) {
		this.shipment = shipment;
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

	public String getArrival_date() {
		return arrival_date;
	}

	public void setArrival_date(String arrival_date) {
		this.arrival_date = arrival_date;
	}

	public String getFoundp_code() {
		return foundp_code;
	}

	public void setFoundp_code(String foundp_code) {
		this.foundp_code = foundp_code;
	}

	public String getTransport_servic() {
		return transport_servic;
	}

	public void setTransport_servic(String transport_servic) {
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

	public String getPort_load() {
		return port_load;
	}

	public void setPort_load(String port_load) {
		this.port_load = port_load;
	}

	public String getPort_dis() {
		return port_dis;
	}

	public void setPort_dis(String port_dis) {
		this.port_dis = port_dis;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getPort_destination() {
		return port_destination;
	}

	public void setPort_destination(String port_destination) {
		this.port_destination = port_destination;
	}

	public Integer getPanel_wallnumber() {
		return panel_wallnumber;
	}

	public void setPanel_wallnumber(Integer panel_wallnumber) {
		this.panel_wallnumber = panel_wallnumber;
	}

	public String getEmail_add() {
		return email_add;
	}

	public void setEmail_add(String email_add) {
		this.email_add = email_add;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<PurchaseGoodsListJson> getGoods_list() {
		return goods_list;
	}

	public void setGoods_list(List<PurchaseGoodsListJson> goods_list) {
		this.goods_list = goods_list;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

}
