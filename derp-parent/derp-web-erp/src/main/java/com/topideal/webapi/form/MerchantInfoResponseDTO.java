package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topideal.common.constant.DerpBasic;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.TariffRateConfigModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问对应接口配置表编辑页面响应
 * @author 杨创
 */
@ApiModel
public class MerchantInfoResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "商家下拉框")
	private List<MerchantInfoModel> result;
	@ApiModelProperty(value = "所有的税率配置")
	List<TariffRateConfigModel> tariffRateConfigList;
	@ApiModelProperty(value = "结算类型 1:应收,2:预收")
	private ArrayList<DerpBasic> settlementTypeList;
	@ApiModelProperty(value = "账期0:预售货款,1:信用账期-7天,2:信用账期-15天,3:信用账期-30天,4:信用账期-40天,5:信用账期-45天,6:信用账期-50天,7:信用账期-60天,8:信用账期-90天,9信用账期-90天以")
	ArrayList<DerpBasic> accountPeriodList;	
	@ApiModelProperty(value = "销售类型 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结")
	ArrayList<DerpBasic> saleTypeList;
	
	public List<MerchantInfoModel> getResult() {
		return result;
	}
	public void setResult(List<MerchantInfoModel> result) {
		this.result = result;
	}
	public List<TariffRateConfigModel> getTariffRateConfigList() {
		return tariffRateConfigList;
	}
	public void setTariffRateConfigList(List<TariffRateConfigModel> tariffRateConfigList) {
		this.tariffRateConfigList = tariffRateConfigList;
	}
	public ArrayList<DerpBasic> getSettlementTypeList() {
		return settlementTypeList;
	}
	public void setSettlementTypeList(ArrayList<DerpBasic> settlementTypeList) {
		this.settlementTypeList = settlementTypeList;
	}
	public ArrayList<DerpBasic> getAccountPeriodList() {
		return accountPeriodList;
	}
	public void setAccountPeriodList(ArrayList<DerpBasic> accountPeriodList) {
		this.accountPeriodList = accountPeriodList;
	}
	public ArrayList<DerpBasic> getSaleTypeList() {
		return saleTypeList;
	}
	public void setSaleTypeList(ArrayList<DerpBasic> saleTypeList) {
		this.saleTypeList = saleTypeList;
	}

	
	




}
