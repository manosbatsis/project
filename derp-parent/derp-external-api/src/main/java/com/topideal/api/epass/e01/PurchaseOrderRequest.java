package com.topideal.api.epass.e01;

/**
 * 采购订单表头 推送报文
 * @author zhanghx
 * */
public class PurchaseOrderRequest  {

	// 企业入仓编号
	private String ent_inbound_id;
	// 合同号（可以暂时对应采购编号）
	private String contract_no;
	// 仓库编码
	private String warehouse_id;
	// 订单状态
	private String status;
	// 进出口口岸
	private String im_exp_port;
	// 预计到货日期
	private String arrival_date;
	// 资金方编码
	private String foundp_code;
	// 头程提单号
	private String transportbl_no;
	// 二程提单号
	private String bl_no;
	// 运输服务 9210:中港运输 9220:香港本地运输
	private String transport_servic;
	// 仓内作业（多选）以“，”隔开
	// 9110:理货,9120:集拼,9130:打托,9140:打包,9150:称重,9160:贴单
	private String wms_operations;
	// 增值服务（多选）以“，”隔开
	// 9310:包材推荐,9320:委托溯源,9330:贴溯源码
	private String add_services;
	// 运输工具名称
	private String tool_name;
	// 运输工具号
	private String tool_no;
	// 启运港编码 （见国外港口码表）
	private String port_load;
	// 目的港名称
	private String port_dis;
	// 目的地名称
	private String destination;
	// 卸货港名称
	private String port_destination;
	// 托版数量
	private int panel_wallnumber;

	public String getEnt_inbound_id() {
		return ent_inbound_id;
	}

	public void setEnt_inbound_id(String ent_inbound_id) {
		this.ent_inbound_id = ent_inbound_id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIm_exp_port() {
		return im_exp_port;
	}

	public void setIm_exp_port(String im_exp_port) {
		this.im_exp_port = im_exp_port;
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

	public String getTool_name() {
		return tool_name;
	}

	public void setTool_name(String tool_name) {
		this.tool_name = tool_name;
	}

	public String getTool_no() {
		return tool_no;
	}

	public void setTool_no(String tool_no) {
		this.tool_no = tool_no;
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

	public int getPanel_wallnumber() {
		return panel_wallnumber;
	}

	public void setPanel_wallnumber(int panel_wallnumber) {
		this.panel_wallnumber = panel_wallnumber;
	}

}
