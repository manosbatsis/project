package com.topideal.order.webapi.purchase.form;

import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 采购金额调整模态框信息
 * @author Guobs
 *
 */

@ApiModel
public class PurchaseAmountAdjustmentModalForm {

	@ApiModelProperty(value="表头信息")
	private List<SdPurchaseConfigItemModel> headerList ;
	
	@ApiModelProperty(value="金额集合")
	private Map<String, Object> amountMap ;
	
	@ApiModelProperty(value="采购订单")
	private PurchaseOrderDTO details ;

	public List<SdPurchaseConfigItemModel> getHeaderList() {
		return headerList;
	}

	public void setHeaderList(List<SdPurchaseConfigItemModel> headerList) {
		this.headerList = headerList;
	}

	public Map<String, Object> getAmountMap() {
		return amountMap;
	}

	public void setAmountMap(Map<String, Object> amountMap) {
		this.amountMap = amountMap;
	}

	public PurchaseOrderDTO getDetails() {
		return details;
	}

	public void setDetails(PurchaseOrderDTO details) {
		this.details = details;
	}
	
	
}
