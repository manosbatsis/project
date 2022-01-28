package com.topideal.entity.dto.transfer;


/**
 * 调拨指令推送商品
 * @author 杨创
 *2018/5/31
 */
public class TransferOrderItemVo {
	private Integer index;//序号
	private String from_good_Id;//调出货号
	private String to_good_Id;//调入货号
	private Integer amount;//数量
	private String batch_id;//调出批次
	private String skuid;//SKU编码  调出仓库为海外仓，调入仓库为综合仓选填
	private String goods_name;//商品名称  调出仓库为海外仓，调入仓库为综合仓选填
	private String brand;//品牌类型  调出仓库为海外仓，调入仓库为综合仓选填
	private String price;//采购单价  调出仓库为海外仓，调入仓库为综合仓选填
	private String weight;//毛重  调出仓库为海外仓，调入仓库为综合仓选填
	private String net_weight;//净重  调出仓库为海外仓，调入仓库为综合仓选填
	private String cont_no;//箱号  调出仓库为海外仓，调入仓库为综合仓选填
	private String bargainno;//合同号  调出仓库为海外仓，调入仓库为综合仓选填
	private String traceins_code;//溯源机构备案号  调出仓库为海外仓，调入仓库为综合仓选填
	private String entrust_no;//溯源委托单号  调出仓库为海外仓，调入仓库为综合仓选填
	private String goodsreg_no;//商品登记号  调出仓库为海外仓，调入仓库为综合仓选填
	private String con_ratio;//成分占比  调出仓库为海外仓，调入仓库为综合仓选填
	private String prodtbatch_no;//生产批次号  调出仓库为海外仓，调入仓库为综合仓选填
	private String note;//备注  调出仓库为海外仓，调入仓库为综合仓选填
	private String stock_type;//0:好品;1:坏品;
	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getFrom_good_Id() {
		return from_good_Id;
	}
	public void setFrom_good_Id(String from_good_Id) {
		this.from_good_Id = from_good_Id;
	}
	public String getTo_good_Id() {
		return to_good_Id;
	}
	public void setTo_good_Id(String to_good_Id) {
		this.to_good_Id = to_good_Id;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	public String getSkuid() {
		return skuid;
	}
	public void setSkuid(String skuid) {
		this.skuid = skuid;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getNet_weight() {
		return net_weight;
	}
	public void setNet_weight(String net_weight) {
		this.net_weight = net_weight;
	}
	public String getCont_no() {
		return cont_no;
	}
	public void setCont_no(String cont_no) {
		this.cont_no = cont_no;
	}
	public String getBargainno() {
		return bargainno;
	}
	public void setBargainno(String bargainno) {
		this.bargainno = bargainno;
	}
	public String getTraceins_code() {
		return traceins_code;
	}
	public void setTraceins_code(String traceins_code) {
		this.traceins_code = traceins_code;
	}
	public String getEntrust_no() {
		return entrust_no;
	}
	public void setEntrust_no(String entrust_no) {
		this.entrust_no = entrust_no;
	}
	public String getGoodsreg_no() {
		return goodsreg_no;
	}
	public void setGoodsreg_no(String goodsreg_no) {
		this.goodsreg_no = goodsreg_no;
	}
	public String getCon_ratio() {
		return con_ratio;
	}
	public void setCon_ratio(String con_ratio) {
		this.con_ratio = con_ratio;
	}
	public String getProdtbatch_no() {
		return prodtbatch_no;
	}
	public void setProdtbatch_no(String prodtbatch_no) {
		this.prodtbatch_no = prodtbatch_no;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getStock_type() {
		return stock_type;
	}
	public void setStock_type(String stock_type) {
		this.stock_type = stock_type;
	}
	
}
