package com.topideal.storage.webapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 合并明细DTO
 * @date 2021-02-07
 */
@ApiModel
public class MergeDTO {
	@ApiModelProperty(value = "仓库id")
	String depot_id;
	@ApiModelProperty(value = "仓库编码")
	String depot_code;
	@ApiModelProperty(value = "仓库类型")
	String depot_type;
	@ApiModelProperty(value = "商品id")
	String goods_id;
	@ApiModelProperty(value = "商品货号")
	String goods_no;
	@ApiModelProperty(value = "旧批次号")
	String old_batch_no;
	@ApiModelProperty(value = "是否坏品")
	String is_damage;
	@ApiModelProperty(value = "是否过期")
	String isExpire;
	@ApiModelProperty(value = "理货单位")
	String tallying_unit;
	@ApiModelProperty(value = "调整量")
	String adjust_total;
	@ApiModelProperty(value = "批次号")
	String batch_no;
	@ApiModelProperty(value = "可用量")
	String deficient_num;
	
	public String getDepot_id() {
		return depot_id;
	}
	public void setDepot_id(String depot_id) {
		this.depot_id = depot_id;
	}
	public String getDepot_code() {
		return depot_code;
	}
	public void setDepot_code(String depot_code) {
		this.depot_code = depot_code;
	}
	public String getDepot_type() {
		return depot_type;
	}
	public void setDepot_type(String depot_type) {
		this.depot_type = depot_type;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_no() {
		return goods_no;
	}
	public void setGoods_no(String goods_no) {
		this.goods_no = goods_no;
	}
	public String getOld_batch_no() {
		return old_batch_no;
	}
	public void setOld_batch_no(String old_batch_no) {
		this.old_batch_no = old_batch_no;
	}
	public String getIs_damage() {
		return is_damage;
	}
	public void setIs_damage(String is_damage) {
		this.is_damage = is_damage;
	}
	public String getIsExpire() {
		return isExpire;
	}
	public void setIsExpire(String isExpire) {
		this.isExpire = isExpire;
	}
	public String getTallying_unit() {
		return tallying_unit;
	}
	public void setTallying_unit(String tallying_unit) {
		this.tallying_unit = tallying_unit;
	}
	public String getAdjust_total() {
		return adjust_total;
	}
	public void setAdjust_total(String adjust_total) {
		this.adjust_total = adjust_total;
	}
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	public String getDeficient_num() {
		return deficient_num;
	}
	public void setDeficient_num(String deficient_num) {
		this.deficient_num = deficient_num;
	}
	
}
