package com.topideal.order.webapi.transfer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 合并明细DTO
 */
@ApiModel
public class MergeDTO {
	@ApiModelProperty(value = "调出仓库id")
	String out_depot_id;
	@ApiModelProperty(value = "仓库编码")
	String depot_code;
	@ApiModelProperty(value = "仓库名称")
	String depot_name;
	@ApiModelProperty(value = "仓库类型")
	String depot_type;
	@ApiModelProperty(value = "调出商品id")
	String out_goods_id;
	@ApiModelProperty(value = "调出商品货号")
	String out_goods_no;
	@ApiModelProperty(value = "理货单位")
	String tallying_unit;
	@ApiModelProperty(value = "调拨量")
	String transfer_num;

	public String getOut_depot_id() {
		return out_depot_id;
	}

	public void setOut_depot_id(String out_depot_id) {
		this.out_depot_id = out_depot_id;
	}

	public String getDepot_code() {
		return depot_code;
	}

	public void setDepot_code(String depot_code) {
		this.depot_code = depot_code;
	}

	public String getDepot_name() {
		return depot_name;
	}

	public void setDepot_name(String depot_name) {
		this.depot_name = depot_name;
	}

	public String getDepot_type() {
		return depot_type;
	}

	public void setDepot_type(String depot_type) {
		this.depot_type = depot_type;
	}

	public String getOut_goods_id() {
		return out_goods_id;
	}

	public void setOut_goods_id(String out_goods_id) {
		this.out_goods_id = out_goods_id;
	}

	public String getOut_goods_no() {
		return out_goods_no;
	}

	public void setOut_goods_no(String out_goods_no) {
		this.out_goods_no = out_goods_no;
	}

	public String getTallying_unit() {
		return tallying_unit;
	}

	public void setTallying_unit(String tallying_unit) {
		this.tallying_unit = tallying_unit;
	}

	public String getTransfer_num() {
		return transfer_num;
	}

	public void setTransfer_num(String transfer_num) {
		this.transfer_num = transfer_num;
	}
}
