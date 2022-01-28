package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import com.topideal.entity.dto.main.CustomerBankDTO;
import com.topideal.entity.dto.main.CustomerInfoDTO;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.CustomerAptitudeModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 访问供应商详情页面
 * @author 杨创
 */
@ApiModel
public class SupplierToDetailsResponseDTO implements Serializable{ 
	@ApiModelProperty(value = "客户信息")
	private CustomerInfoDTO detail;
	@ApiModelProperty(value = "应商资质信息")
	private CustomerAptitudeModel customerAptitude;
	
	@ApiModelProperty(value = "商家信息list")
	private List<MerchantInfoDTO> relList1;
	@ApiModelProperty(value = "客户银行信息")
	private List<CustomerBankDTO> customerBankList;
	public CustomerInfoDTO getDetail() {
		return detail;
	}
	public void setDetail(CustomerInfoDTO detail) {
		this.detail = detail;
	}
	public CustomerAptitudeModel getCustomerAptitude() {
		return customerAptitude;
	}
	public void setCustomerAptitude(CustomerAptitudeModel customerAptitude) {
		this.customerAptitude = customerAptitude;
	}
	public List<MerchantInfoDTO> getRelList1() {
		return relList1;
	}
	public void setRelList1(List<MerchantInfoDTO> relList1) {
		this.relList1 = relList1;
	}
	public List<CustomerBankDTO> getCustomerBankList() {
		return customerBankList;
	}
	public void setCustomerBankList(List<CustomerBankDTO> customerBankList) {
		this.customerBankList = customerBankList;
	}
	
	

	
	




}
