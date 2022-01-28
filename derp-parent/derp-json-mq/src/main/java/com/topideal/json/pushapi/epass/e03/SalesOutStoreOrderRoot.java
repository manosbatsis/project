package com.topideal.json.pushapi.epass.e03;

import java.util.List;

/**
 * 销售出仓订单
 * @author 杨创
 *2018/5/30
 */
public class SalesOutStoreOrderRoot {
	private String order_id;//企业订单编号
	private String warehouse_id;//仓库编码
	private Integer transport_servic;//运输服务 9210:中港运输 9220:香港本地运输
	private Integer wms_operations;//仓内作业（多选）以“，”隔开,9110:理货,9120:集拼,9130:打托,9140:打包,9150:称重,9160:贴单
	private Integer add_services;//增值服务（多选）以“，”隔开,9310:包材推荐,9320:委托溯源,9330:贴溯源码
	private Integer total_num;//件数合计数量
	private Integer panel_wallTotalNum;//托板合计数量
	private Double volume_totalnum;//体积合计 2位小数
	private String destion_code;//目的地代码
	private String busi_mode; //进口模式 10:B2B 20:B2C 30:C2C
	private List<SalesOutStoreGoodsItem> goods_list;//商品信息标签
	private SalesOutStoreRecipient recipient;// 收货信息


	public String getDestion_code() {
		return destion_code;
	}

	public void setDestion_code(String destion_code) {
		this.destion_code = destion_code;
	}

	public SalesOutStoreRecipient getRecipient() {
		return recipient;
	}
	public void setRecipient(SalesOutStoreRecipient recipient) {
		this.recipient = recipient;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getWarehouse_id() {
		return warehouse_id;
	}
	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}
	public Integer getTransport_servic() {
		return transport_servic;
	}
	public void setTransport_servic(Integer transport_servic) {
		this.transport_servic = transport_servic;
	}
	public Integer getWms_operations() {
		return wms_operations;
	}
	public void setWms_operations(Integer wms_operations) {
		this.wms_operations = wms_operations;
	}
	public Integer getAdd_services() {
		return add_services;
	}
	public void setAdd_services(Integer add_services) {
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
	public List<SalesOutStoreGoodsItem> getGoods_list() {
		return goods_list;
	}
	public void setGoods_list(List<SalesOutStoreGoodsItem> goods_list) {
		this.goods_list = goods_list;
	}

	public String getBusi_mode() {
		return busi_mode;
	}

	public void setBusi_mode(String busi_mode) {
		this.busi_mode = busi_mode;
	}
}
