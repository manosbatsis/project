package com.topideal.json.pushapi.epass.e03;

import java.util.Date;
/**
 * 销售出仓订单商品
 * @author 杨创
 *2018/5/30
 */
public class SalesOutStoreGoodsItem {

	private String goods_id;//商品编码
	private String goods_name;//商品名称
	private Integer amount;//数量，整数
	private String pro_date;//生产日期格式：yyyy-mm-dd HH:mi:ss
	private String due_date;//有效日期格式：yyyy-mm-dd HH:mi:ss
	private String batch;//原始批次号
	private String uom;//：00：托盘，01：箱 , 02：件
	
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
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getPro_date() {
		return pro_date;
	}
	public void setPro_date(String pro_date) {
		this.pro_date = pro_date;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	
	
}
