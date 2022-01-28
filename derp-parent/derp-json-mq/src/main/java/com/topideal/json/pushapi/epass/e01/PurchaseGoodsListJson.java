package com.topideal.json.pushapi.epass.e01;

/**
 * 采购订单表体 推送报文
 *
 * @author zhanghx
 */
public class PurchaseGoodsListJson {

	// 必填（保税仓：Y，海外仓：Y）
	// 序号
	private String index;
	// 商品编码
	private String goods_id;
	// 数量，整数
	private String amount;
	// 单价
	private String price;

	// 卓志保税仓必填（保税仓：Y，海外仓：N）
	// 毛重
	private String weight;
	// 净重
	private String net_weight;
	// 箱号
	private String cont_no;
	// 品牌类型
	private String brand;

	// 非必填（保税仓：N，海外仓：N）
	// 合同号
	private String bargainno;
	// SKU编码
	private String skuid;
	// 商品名称
	private String goods_name;
	// 溯源机构备案号
	private String traceins_code;
	// 溯源委托单号
	private String entrust_no;
	// 商品登记号
	private String goodsreg_no;
	// 生产批次号
	private String prodtbatch_no;
	// 成分占比
	private String con_ratio;
	// 备注
	private String note;

	//新增字段
	private String is_tracesrc; //是否溯源 0：不溯源 1:溯源

	// 采购明细id
	private Long purchase_item_id;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
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

	public String getSkuid() {
		return skuid;
	}

	public void setSkuid(String skuid) {
		this.skuid = skuid;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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

	public String getProdtbatch_no() {
		return prodtbatch_no;
	}

	public void setProdtbatch_no(String prodtbatch_no) {
		this.prodtbatch_no = prodtbatch_no;
	}

	public String getCon_ratio() {
		return con_ratio;
	}

	public void setCon_ratio(String con_ratio) {
		this.con_ratio = con_ratio;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getIs_tracesrc() {
		return is_tracesrc;
	}

	public void setIs_tracesrc(String is_tracesrc) {
		this.is_tracesrc = is_tracesrc;
	}

	public Long getPurchase_item_id() {
		return purchase_item_id;
	}

	public void setPurchase_item_id(Long purchase_item_id) {
		this.purchase_item_id = purchase_item_id;
	}
}
